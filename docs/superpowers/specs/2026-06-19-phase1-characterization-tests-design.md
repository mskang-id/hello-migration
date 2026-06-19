# Phase 1 설계: 동작 기반 안전망 구축 및 내부 결합도 완화

## 개요

shop-legacy-api의 현대화 Phase 1을 위한 설계 문서.
테스트 없는 레거시 코드베이스에 특성화 테스트를 구축하고, 이후 안전하게 리팩터링한다.

## 전략

**테스트 먼저, 리팩터 나중** — 15개 고정 대상의 특성화 테스트를 전부 작성한 뒤, 안전망이 확보된 상태에서 Map→VO, -1L→예외, 불필요 코드 제거를 순차 진행.

**3배치 점진 전달:**
- Batch 1: 위험 1~5 (주문 생성/취소, 포인트, 재고, 상태전이)
- Batch 2: 위험 6~9 (배송비/VAT/커미션/쿠폰 계산)
- Batch 3: 위험 10~15 (배치잡/장바구니/조회/프로모션)

각 배치 완료 시 리뷰 후 다음 배치 진행.

## 종료 조건 (CLAUDE.md에서 발췌)

- [ ] 핵심 도메인 흐름 전체에 특성화 테스트가 존재하고 CI에서 통과
- [ ] `Map<String, Object>` 파라미터/반환값이 타입 안전한 객체로 교체
- [ ] 매직 센티넬 `-1L`이 제거되고 예외 또는 결과 타입으로 대체
- [ ] `UnsafeMemoryUtil` 등 불필요한 코드가 제거

---

## 1. 테스트 인프라

### 1.1 의존성 (root POM, test scope)

```xml
junit:junit:4.13.2
org.springframework:spring-test:3.2.18.RELEASE
org.assertj:assertj-core:1.7.1
```

Mockito 미사용. H2 실DB 통합 테스트만 작성 (CLAUDE.md 지침).

### 1.2 테스트 Spring 컨텍스트

**파일:** `shop-api-core/src/test/resources/spring/test-context.xml`

- 프로덕션 XML import: `datasource-context.xml`, `ibatis-context.xml`, `service-context.xml`, `tx-context.xml`
- **배치 컨텍스트 제외** — 배치 자동실행 차단
- 배치 테스트용 별도 컨텍스트: `test-batch-context.xml` (배치 빈만 수동 등록)

### 1.3 테스트 베이스 클래스

```java
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/test-context.xml")
@Transactional
public abstract class BaseIntegrationTest {
    @Autowired protected SqlMapClient sqlMapClient;
}
```

- `@Transactional`: 각 테스트 후 자동 롤백 (시드 데이터 보존)
- 배치 테스트는 `@Transactional` 없이 수동 검증

### 1.4 테스트 위치

- 모듈: `shop-api-core/src/test/java/`
- 패키지: `com.shopmall.characterization`
- 이유: ServiceImpl 호출이 핵심 진입점, core가 persistence 의존하므로 E2E 가능

---

## 2. Batch 1: 위험 1~5 (핵심 상태 변이)

### 2.1 PlaceOrderCharacterizationTest

| # | 시나리오 | 검증 |
|---|---|---|
| 1 | 정상 주문 (Card, Grade A) | orders/order_item INSERT, stock_qty 감소, inventory_log(ORDER), point 변화(차감+3%적립), settlement INSERT |
| 2 | 정상 주문 (POINT 결제) | flat 1% 적립 경로 |
| 3 | 쿠폰 적용 주문 | coupon.used_yn='Y', settlement.discount 반영 |
| 4 | 첫 주문 웰컴 보너스 | +500 추가 적립 |
| 5 | 재고 부족 | -1L, 변이 없음 |
| 6 | PG 실패 (≥1,000,000원) | -1L, 변이 없음 |

**기대값 소스:** data.sql (member, product_option), AppConstants (PG_DECLINE_THRESHOLD), SettlementBiz.earnRate()

### 2.2 CancelOrderCharacterizationTest

| # | 시나리오 | 검증 |
|---|---|---|
| 1 | PAID→CANCELLED | status=4, stock복구, point복원, refund INSERT |
| 2 | SHIPPED→CANCELLED | 동일 취소 흐름 |
| 3 | PLACED→CANCELLED (무효) | -1L, 변이 없음 |
| 4 | CANCELLED→CANCELLED (무효) | -1L |
| 5 | 비동기 감사 | order_audit, notification_outbox INSERT |

**기대값 소스:** OrderStatus.java, OrderStatusGuard.canTransition(), schema.sql refund DDL

### 2.3 PointEarningCharacterizationTest

| # | 시나리오 | 검증 |
|---|---|---|
| 1 | POINT 결제 → flat 1% | payAmount * 1/100 |
| 2 | Card + Grade A → 3% | payAmount * 3/100 |
| 3 | Card + Grade B → 2% | payAmount * 2/100 |
| 4 | Card + Grade C → 1% | payAmount * 1/100 |
| 5 | 첫 주문 보너스 | 등급적립 + 500 |
| 6 | 두 번째 주문 | 등급적립만 |

**기대값 소스:** SettlementBiz.earnRate(), PointManager(500), data.sql member.grade

### 2.4 InventoryLedgerCharacterizationTest

| # | 시나리오 | 검증 |
|---|---|---|
| 1 | 주문 후 | stock_qty -= qty, inventory_log(ORDER, -qty) |
| 2 | 취소 후 | stock_qty += qty, inventory_log(RESTOCK, +qty) |
| 3 | 다중 아이템 | 각 option별 독립 감소 + 로그 쌍 |

**기대값 소스:** data.sql product_option.stock_qty, ProductSqlMap.xml

### 2.5 OrderStatusGuardCharacterizationTest

| # | 시나리오 | 검증 |
|---|---|---|
| 1 | PLACED→PAID | 성공 |
| 2 | PAID→SHIPPED | 성공 |
| 3 | PAID→CANCELLED | 성공 |
| 4 | SHIPPED→CANCELLED | 성공 |
| 5 | PLACED→SHIPPED (건너뛰기) | -1L |
| 6 | PAID→PLACED (역방향) | -1L |
| 7 | CANCELLED→PAID (역방향) | -1L |
| 8 | PLACED→PLACED (자기자신) | -1L |

**기대값 소스:** OrderStatus.java (PLACED=1, PAID=2, SHIPPED=3, CANCELLED=4), OrderStatusGuard

---

## 3. Batch 2: 위험 6~9 (비즈니스 계산)

### 3.1 ShippingFeeCharacterizationTest

| # | 시나리오 | 검증 |
|---|---|---|
| 1 | Grade A, 아무 금액 | 0원 |
| 2 | Grade B, 49,999원 | 2,500원 |
| 3 | Grade B, 50,000원 | 0원 |
| 4 | Grade C, 30,000원 | 2,500원 |
| 5 | Grade C, 100,000원 | 0원 |
| 6 | Grade B, 30,000원, zipcode "63001" | 5,500원 |
| 7 | Grade A, zipcode "40123" | 3,000원 |
| 8 | Grade B, 50,000원, zipcode "63500" | 3,000원 |

**기대값 소스:** AppConstants (SHIPPING_FEE=2500, FREE_SHIP_THRESHOLD=50000), SettlementBiz:27 (3000)

### 3.2 VatCalculationCharacterizationTest

| # | 시나리오 | 검증 |
|---|---|---|
| 1 | SettlementBiz.vat(100,000) | 10,000 (10%) |
| 2 | SettlementBiz.vat(0) | 0 |
| 3 | SettlementBiz.vat(33,333) | 정수 나눗셈 결과 고정 |
| 4 | ReportSettlementCalc.vat(100,000) | 11,000 (11%) |
| 5 | ReportSettlementCalc.vat(33,333) | 정수 나눗셈 결과 고정 |

**기대값 소스:** AppConstants.VAT_RATE(10), ReportSettlementCalc(11 하드코딩)

**참고:** 두 경로의 불일치를 명시적으로 기록하는 테스트. 현대화 시 정규화 결정 근거.

### 3.3 CommissionCalculationCharacterizationTest

| # | 시나리오 | 검증 |
|---|---|---|
| 1 | Java: ELECTRONICS 100,000 | 12,000 (12%) |
| 2 | Java: BOOKS 100,000 | 6,000 (6%) |
| 3 | Java: default 100,000 | 11,000 (11%) |
| 4 | SQL 배치: ELECTRONICS | 12% |
| 5 | SQL 배치: BOOKS | 5% |
| 6 | SQL 배치: default | 10% |

**기대값 소스:** ReportSettlementCalc:12-20 (Java), SettlementBatchSqlMap.xml:12-14 (SQL)

**참고:** Java/SQL 불일치를 독립 테스트로 각각 기록.

### 3.4 CouponApplicationCharacterizationTest

| # | 시나리오 | 검증 |
|---|---|---|
| 1 | Rate(R) 10%, 100,000원 | discount=10,000, used_yn='Y' |
| 2 | Flat(F) 5,000원 | discount=5,000, used_yn='Y' |
| 3 | min_order 미달 | 미적용, used_yn='N' |
| 4 | 만료 쿠폰 | 미적용 |
| 5 | 사용 완료 쿠폰 | 미적용 |
| 6 | 카테고리 쿠폰 — 매칭 | 적용 |
| 7 | 카테고리 쿠폰 — 불일치 | 미적용 |

**기대값 소스:** data.sql + data-extra.sql coupon, CouponSqlMap.xml, OrderBiz.applyCoupon()

---

## 4. Batch 3: 위험 10~15 (배치/장바구니/조회/프로모션)

### 4.1 SettlementBatchCharacterizationTest

| # | 시나리오 | 검증 |
|---|---|---|
| 1 | 시드 주문 기반 배치 실행 | settlement_daily row 생성 |
| 2 | gross_amount | SUM(qty*unit_price) 정합 |
| 3 | 카테고리별 커미션 | SQL CASE WHEN 결과 |
| 4 | 멱등성 | 2회 실행 후 row 수 불변 |

**테스트 방식:** @Transactional 제외, 수동 호출, 결과 검증

### 4.2 PointExpiryBatchCharacterizationTest

| # | 시나리오 | 검증 |
|---|---|---|
| 1 | 만료 대상 처리 | point_ledger 음수 INSERT, swept_yn='Y' |
| 2 | member_id < 9 제외 | 미처리 |
| 3 | 미래 만료일 제외 | swept_yn='N' 유지 |
| 4 | 멱등성 | 재처리 안 함 |
| 5 | member.point 미갱신 | 배치 전후 동일 |

### 4.3 DailySummaryBatchCharacterizationTest

| # | 시나리오 | 검증 |
|---|---|---|
| 1 | settlement_daily 기반 집계 | summary_daily 생성 |
| 2 | 선행 데이터 없이 실행 | row 미생성 |
| 3 | 멱등성 | 결과 동일 |

### 4.4 CartCheckoutCharacterizationTest

| # | 시나리오 | 검증 |
|---|---|---|
| 1 | 정상 체크아웃 | cart.status='ORDERED', 주문 생성 |
| 2 | 빈 카트 | 실패 동작 고정 |
| 3 | 이미 ORDERED 카트 | 중복 방지 |
| 4 | 주문 실패 시 카트 상태 | 롤백 여부 고정 |

### 4.5 ProductQueryCharacterizationTest

| # | 시나리오 | 검증 |
|---|---|---|
| 1 | findAllOnSale | ON_SALE만 반환 (14개) |
| 2 | SOLD_OUT 제외 | 미포함 |
| 3 | DISCONTINUED 제외 | 미포함 |
| 4 | 응답 키 구조 | Map 키 집합 검증 |

### 4.6 PromotionApplyCharacterizationTest

| # | 시나리오 | 검증 |
|---|---|---|
| 1 | ACTIVE + 범위 내 + 재고≥5 | 할인 반환 |
| 2 | ENDED | 미적용 |
| 3 | 날짜 범위 밖 | 미적용 |
| 4 | 재고 < 5 | 숨김 |
| 5 | 스택 적용 | 다중 할인 |

---

## 5. 리팩터링 (안전망 완성 후)

### 5.1 Map<String, Object> → 타입 안전 객체 (311곳)

**전략:** 도메인별 순차 교체

| 순서 | 도메인 | 대상 DAO/Service | 예상 VO |
|---|---|---|---|
| 1 | Order | OrderDao, OrderServiceImpl | OrderParam, OrderResult, OrderItemParam |
| 2 | Member | MemberDao | MemberParam, MemberResult |
| 3 | Product | ProductDao | ProductResult, StockParam |
| 4 | Refund | RefundDao, RefundServiceImpl | RefundParam, RefundResult |
| 5 | Cart | CartDao, CartServiceImpl | CartParam, CartItemParam |
| 6 | Coupon | CouponDao | CouponParam, CouponResult |
| 7 | Inventory | InventoryLogDao | InventoryLogParam |
| 8 | Delivery | DeliveryDao | DeliveryParam |
| 9 | Audit | AuditDao | AuditParam, OutboxParam |
| 10 | Batch | BatchDao | SettlementResult, PointExpiryResult |
| 11 | Report | ReportDao | ReportResult |
| 12 | Others | Review, Wishlist, Promotion | 각 도메인별 VO |

각 교체 후 전체 테스트 Green 확인.

### 5.2 매직 센티넬 -1L → 예외 (14곳)

| 현재 패턴 | 교체 후 |
|---|---|
| `return -1L` (재고 부족) | `throw new InsufficientStockException(optionId, qty)` |
| `return -1L` (PG 실패) | `throw new PaymentDeclinedException(amount)` |
| `return -1L` (상태 전이 실패) | `throw new InvalidStateTransitionException(from, to)` |

테스트 수정: `assertEquals(-1L, result)` → `assertThrows(XxxException.class, ...)`

### 5.3 불필요 코드 제거

- `UnsafeMemoryUtil.java` 삭제
- `OrderServiceImpl`의 import + 호출부 제거
- 테스트 Green 확인

---

## 6. 출처 불명 항목 (실행 확인 필요)

특성화 테스트 작성 시 실행해서 기대값을 확인해야 하는 항목:

1. VAT 10% vs 11% — 어느 경로가 실제 API 응답에 반영되는지
2. 커미션 BOOKS 6% vs 5% — 어느 것이 정산에 최종 반영되는지
3. SettlementBatch MERGE 후 settlement_daily 실제 row 수/금액
4. 주문 실패 시 cart.status 롤백 여부
5. OrderBiz.earnPoint vs PointManager.earn 분기 조건 정확한 지점

이 항목들은 첫 테스트 실행 시 Green으로 만드는 과정에서 실측값을 기대값으로 채택한다.
