# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

이 저장소는 **현대화 대상 레거시 애플리케이션**이다. 기존 코드 패턴은 따라야 할 관례가 아니라, 평가하고 교체할 "현재 상태(as-is)"다. 현재 진행 상태는 [docs/modernization-status.md](docs/modernization-status.md)가 소유한다.

---

## 빌드 및 실행

호스트에 JDK를 설치할 필요 없음. Docker가 필요하다. (`tools/` 스크립트가 `maven:3.9-eclipse-temurin-8` 이미지를 사용하고, M2 캐시는 `shop-api-m2` named volume에 유지된다.)

```bash
# 전체 빌드
./tools/build.sh package

# 스킵 테스트 빌드 (현재 테스트 없음)
./tools/build.sh package -DskipTests

# 단일 모듈 빌드
./tools/build.sh package -pl shop-api-core -am

# 서버 기동 (http://localhost:8080)
./tools/run.sh

# 테스트 실행 (테스트 작성 후)
./tools/build.sh test
./tools/build.sh test -pl shop-api-core          # 특정 모듈만
./tools/build.sh test -pl shop-api-core -Dtest=OrderBizTest  # 특정 클래스만
```

---

## 아키텍처

### 모듈 구조 (Maven multi-module)

```
shop-legacy-api (root POM)
├── shop-api-common   — 공유 VO, 에러 코드, 유틸, JAXB envelope
├── shop-api-persistence — DAO 인터페이스 + iBATIS 2 구현체 + SQL Map XML
├── shop-api-core     — Service / Biz / Manager / Facade / Batch (비즈니스 로직 전부)
└── shop-api-web      — Spring MVC Controller + JAXB DTO + Spring XML 설정
```

### 요청 처리 흐름

```
HTTP → Controller (shop-api-web)
      → Facade (core/facade)       ← 여러 Service를 조합, 트랜잭션 밖
        → ServiceImpl (core/service/impl)  ← @Tx AOP 프록시 (이름 *ServiceImpl만)
          → Biz / Manager (core/biz, core/manager)  ← 트랜잭션 밖
            → DaoImpl (persistence/dao/impl)
              → iBATIS 2 SqlMapClient → H2 (인메모리)
```

**핵심 특성:**
- 트랜잭션 경계: `*ServiceImpl`에만 AOP(`tx-context.xml`)로 적용. Facade·Manager는 트랜잭션 밖에서 실행된다.
- 파라미터 타입: Service 계층 전체가 `Map<String, Object>`를 사용한다. 타입 안전성 없음.
- 오류 표현: 예외 대신 반환값 `-1L` 매직 센티넬을 사용하는 경로가 다수 존재한다.
- 응답 포맷: 모든 엔드포인트가 XML만 반환한다(`application/xml`, `MarshallingHttpMessageConverter` + JAXB2).
- DB: H2 인메모리 (`MODE=MySQL`). 기동 시 `schema.sql` → `data.sql` → `data-extra.sql` → `data-scale.sql` 순서로 로드.

### 핵심 계층 역할

| 패키지 | 역할 |
|---|---|
| `com.shopmall.web` | Spring MVC `@Controller`, JAXB DTO, `ResponseFactory` |
| `com.shopmall.facade` | 여러 서비스를 조합하는 진입 계층 (트랜잭션 없음) |
| `com.shopmall.service` | 도메인 서비스 인터페이스; 구현체(`impl.*ServiceImpl`)에 트랜잭션 적용 |
| `com.shopmall.biz` | 서비스 내부에서 호출되는 비즈니스 계산 단위 (트랜잭션 없음) |
| `com.shopmall.manager` | 횡단 관심사: 재고·포인트·상태 머신·배치·감사 (트랜잭션 없음) |
| `com.shopmall.pg` | `MockPaymentGateway` — 실제 PG 연동 없음 |
| `com.shopmall.dao` | DAO 인터페이스 (persistence 모듈); 구현체는 `dao.impl.*` |

### 배치 잡

`batch-context.xml`에서 Spring `<task:scheduler>`로 스케줄:
- `SettlementBatch.run()` — 1시간 간격
- `PointExpiryBatch.run()` — 1시간 간격
- `DailySummaryBatch.run()` — 매일 02:30

---

## 기술 스택 평가

| 컴포넌트 | 현재 버전 | 상태 |
|---|---|---|
| Java | 1.8 (JDK 8) | 노후. LTS는 21 |
| Spring Framework | 3.2.18.RELEASE | **심각히 노후.** Spring 3.x는 2016년 EOL. 현재 6.x |
| iBATIS | 2.3.4.726 | **폐기됨.** 2010년 MyBatis로 교체. |
| commons-dbcp | 1.4 | 노후. HikariCP가 사실상 표준 |
| log4j | 1.2.17 | 노후. log4j 1.x는 2015년 EOL (Log4Shell과 별개) |
| H2 | 1.4.200 | 노후. 현재 2.x (API 비호환 변경 있음) |
| Tomcat | tomcat7-maven-plugin | 노후. Tomcat 7은 2021년 EOL |
| 빌드 | Maven 3.9 (Docker) | 적절함 |
| 패키징 | WAR (외장 서블릿 컨테이너) | 노후. 내장 서버 방식이 표준 |
| API 직렬화 | JAXB2/XML only | 노후. REST/JSON이 표준 |

---

## 자동화 테스트 현황

**테스트 없음.** `src/test/java` 디렉터리가 어느 모듈에도 존재하지 않는다. 안전망이 전혀 없는 상태다.

---

## 작업 지침 — 테스트 규율

### 기존 코드 변경 시: 특성화 테스트 우선

변경 전, 현재 동작을 **특성화 테스트(characterization test)**로 고정한다.

- 기대값은 소스 코드와 시드 데이터(`db/schema.sql`, `db/data.sql` 등)에서 직접 도출한다. 숫자를 CLAUDE.md에 하드코딩하지 않는다.
- H2 인메모리 DB는 테스트에서 그대로 사용한다 — 모킹하지 않는다. Spring `ApplicationContext`를 로드하는 통합 테스트로 작성한다.
- 예: 서비스의 `Map` 반환값에 어떤 키가 있는지, 재고 0일 때 반환값이 `null`인지 `-1L`인지 — 이런 계약을 테스트로 박는다.

### 새로운 동작 구현 시: TDD

새로운 기능이나 동작은 테스트를 먼저 작성한다(Red → Green → Refactor).

---

## 현대화 로드맵

이 로드맵은 **순서가 강제된 계획**이다. 속도가 아니라 완결성이 기준이다. 각 단계는 종료 조건을 충족한 뒤에야 다음 단계를 시작할 수 있다. 서비스 추출, DB 분리, 새 API 경계 정의는 Phase 3 작업이다.

### Phase 1 — 동작 기반 안전망 구축 및 내부 결합도 완화

**종료 조건:**
- 핵심 도메인 흐름(주문·결제·재고·포인트·배송) 전체에 특성화 테스트가 존재하고 CI에서 통과한다.
- `Map<String, Object>` 파라미터/반환값이 타입 안전한 객체로 교체되어 있다.
- 매직 센티넬 `-1L`이 제거되고 예외 또는 결과 타입으로 대체되어 있다.
- `UnsafeMemoryUtil` 등 불필요한 코드가 제거되어 있다.

### Phase 2 — 런타임과 의존성 현대화

**게이트: Phase 1 종료 조건이 모두 참(true)일 때 시작한다.**

**종료 조건:**
- Java 21, Spring Boot 3.x (Spring Framework 6.x)로 전환 완료.
- iBATIS 2 → MyBatis 3 (또는 Spring Data JPA) 마이그레이션 완료.
- commons-dbcp → HikariCP 교체 완료.
- log4j 1.x → SLF4J + Logback 교체 완료.
- WAR + 외장 Tomcat → 내장 서버(Spring Boot Embedded Tomcat) 전환 완료.
- XML-only API → JSON 지원 추가 완료.
- Phase 1에서 작성한 특성화 테스트가 Phase 2 이후에도 여전히 통과한다.

### Phase 3 — 서비스 분해 및 AWS 배포

**게이트: Phase 1과 Phase 2 종료 조건이 모두 참(true)일 때 시작한다.**

Phase 3의 구체적 작업(서비스 경계 결정, 데이터베이스 분리 전략, 목표 런타임 — Lambda·ECS·EKS 등)은 Phase 2 완료 시점의 코드를 보고 결정한다. 현재의 내부 도메인 구조(주문·상품·회원·배송·정산 등)는 향후 서비스 경계에 대한 **가설**일 뿐이며, Phase 2 이전에 확정하지 않는다.

---

현재 단계와 진척 상황 → [docs/modernization-status.md](docs/modernization-status.md)
