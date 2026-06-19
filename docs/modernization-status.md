# 현대화 진행 상태

> **이 파일이 현재 단계와 진척 상황을 소유한다.** 단계 모델과 종료 조건의 영속적 정의는 [CLAUDE.md](../CLAUDE.md)에 있다.

---

## 현재 단계: Phase 1 (시작 전)

Phase 1을 아직 시작하지 않았다.

---

## Phase 1 — 동작 기반 안전망 구축 및 내부 결합도 완화

**상태:** 진행 중

### 종료 조건 체크리스트

- [ ] 핵심 도메인 흐름(주문·결제·재고·포인트·배송) 전체에 특성화 테스트가 존재하고 CI에서 통과한다
- [ ] `Map<String, Object>` 파라미터/반환값이 타입 안전한 객체로 교체되어 있다
- [ ] 매직 센티넬 `-1L`이 제거되고 예외 또는 결과 타입으로 대체되어 있다
- [ ] `UnsafeMemoryUtil` 등 불필요한 코드가 제거되어 있다

### 작업 내역

#### 2026-06-19: 특성화 테스트 코드 작성 완료

- 안전망 계획서 작성 (`docs/safety-net-plan.md`) — 15개 고정 대상 식별
- 설계 문서 작성 (`docs/superpowers/specs/2026-06-19-phase1-characterization-tests-design.md`)
- 구현 계획 작성 (`docs/superpowers/plans/2026-06-19-phase1-characterization-tests.md`)
- 테스트 인프라 구축: JUnit 4 + spring-test + AssertJ 1.7.1, BaseIntegrationTest, test-context.xml
- 15개 특성화 테스트 클래스 작성 완료:
  - Batch 1 (위험 1~5): PlaceOrder, CancelOrder, PointEarning, InventoryLedger, OrderStatusGuard
  - Batch 2 (위험 6~9): ShippingFee, VatCalculation, CommissionCalculation, CouponApplication
  - Batch 3 (위험 10~15): SettlementBatch, PointExpiryBatch, DailySummaryBatch, CartCheckout, ProductQuery, PromotionApply
- GitHub 푸시 완료: https://github.com/mskang-id/hello-migration

#### 다음 작업 (Docker 필요)

1. `./tools/build.sh test` 실행 → 테스트 Green 확인 (실패 시 assertion 조정)
2. 테스트 전체 통과 후 리팩터링 시작:
   - Map<String, Object> → VO 교체 (311곳)
   - -1L → 예외 교체 (14곳)
   - UnsafeMemoryUtil 제거

---

## Phase 2 — 런타임과 의존성 현대화

**상태:** 대기 중 (Phase 1 미완)

### 종료 조건 체크리스트

- [ ] Java 21, Spring Boot 3.x (Spring Framework 6.x) 전환 완료
- [ ] iBATIS 2 → MyBatis 3 (또는 Spring Data JPA) 마이그레이션 완료
- [ ] commons-dbcp → HikariCP 교체 완료
- [ ] log4j 1.x → SLF4J + Logback 교체 완료
- [ ] WAR + 외장 Tomcat → Spring Boot Embedded Tomcat 전환 완료
- [ ] XML-only API → JSON 지원 추가 완료
- [ ] Phase 1 특성화 테스트가 Phase 2 이후에도 통과한다

### 작업 내역

_아직 없음._

---

## Phase 3 — 서비스 분해 및 AWS 배포

**상태:** 대기 중 (Phase 1·2 미완)

구체적 작업은 Phase 2 완료 시점의 코드를 보고 결정한다.

### 작업 내역

_아직 없음._
