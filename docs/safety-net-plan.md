# 안전망 계획: shop-legacy-api

## 컨텍스트
- 예정 변경: 전체 현대화 — Phase 1(안전망 + 결합도 완화) → Phase 2(Java 21, Spring Boot 3, iBATIS→MyBatis, HikariCP, JSON API)
- 현재 안전망 상태: **없음** — src/test/java 디렉터리가 어느 모듈에도 없음
- 변경 범위: 전체 코드베이스 (4개 모듈)

---

## 고정 대상 목록 (위험 높은 순)

### [위험 1] 주문 생성 (placeOrder) — 다중 테이블 원자적 변이

- **경로**: `OrderFacade.placeOrder()` → `OrderServiceImpl.placeOrder()` → `OrderBiz.usePoint()` + `OrderBiz.earnPoint()` / `PointManager.earn()` + `ProductDao.deductStock()` + `InventoryLogDao.insertLog()` + `CouponDao.markUsed()` + `OrderDao.insertOrder()` / `insertOrderItem()` / `insertSettlement()`
- **위험 이유**: ① 단일 트랜잭션에서 6+개 테이블을 변이. 재고·포인트·쿠폰·정산이 모두 연동되며 실패 시 데이터 불일치 발생
- **고정할 동작**:
  - 정상 주문 후: orders 레코드 생성(status=1), order_item 생성, product_option.stock_qty 감소, inventory_log(reason=ORDER) 생성, member.point 변화(사용분 차감 + 적립분 가산), coupon.used_yn='Y', order_settlement 생성
  - 재고 부족 시: 반환값 -1L, 어떤 테이블도 변이 없음
  - PG 결제 실패(≥1,000,000원): 반환값 -1L
- **기대값 도출**:
  - 초기 재고: `db/data.sql` — product_option 테이블의 stock_qty
  - 회원 포인트 초기값: `db/data.sql` — member 테이블의 point 컬럼
  - 적립률: `shop-api-core` SettlementBiz.earnRate() (A=3%, B=2%, C=1%) + PointManager 웰컴 보너스(500)
  - PG 한도: `shop-api-common` AppConstants.PG_DECLINE_THRESHOLD (1,000,000)
  - 매직 센티넬: 소스 코드에서 `-1L` 반환 지점 확인

---

### [위험 2] 주문 취소/환불 (cancelOrder) — 상태 전이 + 역보상

- **경로**: `OrderLifecycleController.cancel()` → `RefundServiceImpl.cancelOrder()` → `OrderStatusGuard.transition()` + `StockManager.restock()` + `InventoryManager.logChange()` + `PointManager.restorePoint()` + `PointManager.revoke()` + `RefundDao.insertRefund()` + `AuditNotifier.auditEvent()` (async)
- **위험 이유**: ① 상태 전이 + 재고 복구 + 포인트 환원이 하나의 트랜잭션에 결합. 가드 실패 시 -1L 반환. 비동기 감사 로그는 트랜잭션 밖
- **고정할 동작**:
  - 유효 전이(PAID→CANCELLED): orders.status=4, stock_qty 복구, inventory_log(reason=RESTOCK), member.point 복원(사용분 반환 - 적립분 회수), refund 레코드 생성
  - 무효 전이(PLACED→CANCELLED, CANCELLED→CANCELLED): 반환값 -1L, 변이 없음
  - 비동기 감사: order_audit + notification_outbox 레코드 생성 확인
- **기대값 도출**:
  - 유효 전이 집합: `shop-api-core` OrderStatusGuard.canTransition() — (1→2, 2→3, 2→4, 3→4)
  - 에러 코드: `shop-api-common` ErrorCode.INVALID_STATE = "E4091"
  - 낙관적 잠금: `OrderLifecycleSqlMap.xml` — `WHERE status = #fromStatus#`
  - 환불 레코드 구조: `db/schema.sql` — refund 테이블 DDL

---

### [위험 3] 포인트 적립/차감 이중 경로

- **경로 A**: `OrderBiz.earnPoint()` — POINT 결제 시 flat 1%
- **경로 B**: `PointManager.earn()` — Card/기타 결제 시 등급별(A=3%, B=2%, C=1%) + 첫 주문 보너스(500p)
- **경로 C**: `PointExpiryBatch.run()` — 만료 포인트 음수 원장 기록
- **위험 이유**: ① 두 개의 독립 경로가 같은 member.point 컬럼을 변이. 현대화 시 하나가 누락되면 포인트 불일치
- **고정할 동작**:
  - 경로 A: POINT 결제 → payAmount * 1/100 적립
  - 경로 B: Card 결제 + Grade A → payAmount * 3/100 적립; 첫 주문이면 +500
  - 경로 C: 만료일 경과 + swept_yn='N' + member_id≥9 → point_ledger에 음수 INSERT + swept_yn='Y'
- **기대값 도출**:
  - 적립률 상수: `shop-api-core` SettlementBiz.earnRate() 소스 코드
  - 첫 주문 보너스: `shop-api-core` PointManager 상수 (500)
  - 만료 기준: PointExpiryBatch의 EXPIRE_AFTER_DAYS (365)
  - member_id≥9 필터: `PointExpirySqlMap.xml` WHERE 절

---

### [위험 4] 재고 차감/복구 + 감사 로그 쌍

- **경로**: `ProductDao.deductStock()` / `ProductDao.restock()` — 항상 `InventoryLogDao.insertLog()`와 쌍으로 호출
- **위험 이유**: ① 재고 변경과 감사 로그가 항상 쌍이어야 하는 불변식. 현대화 시 로그 누락 가능
- **고정할 동작**:
  - deductStock 후: stock_qty 감소량 = 주문 qty, inventory_log(reason=ORDER, change_qty=-qty)
  - restock 후: stock_qty 증가량 = 원래 qty, inventory_log(reason=RESTOCK, change_qty=+qty)
- **기대값 도출**:
  - 초기 재고: `db/data.sql` product_option.stock_qty
  - SQL: `ProductSqlMap.xml` deductStock/restock 쿼리
  - 로그 구조: `db/schema.sql` inventory_log 테이블

---

### [위험 5] 주문 상태 전이 가드 (OrderStatusGuard)

- **경로**: `OrderStatusGuard.canTransition(from, to)` → `OrderLifecycleSqlMap.xml` 낙관적 잠금 UPDATE
- **위험 이유**: ② 상태 머신 규칙 — 유효/금지 전이가 명시적으로 구현됨. 현대화 시 규칙 누락 위험
- **고정할 동작**:
  - 허용: PLACED→PAID, PAID→SHIPPED, PAID→CANCELLED, SHIPPED→CANCELLED
  - 금지: 그 외 모든 조합 (역방향, 자기 자신, 건너뛰기) → -1L
  - 동시성: 다른 요청이 먼저 상태를 바꾼 경우 rowcount=0 → -1L
- **기대값 도출**:
  - 전이 규칙: `shop-api-core` OrderStatusGuard.canTransition() 소스 코드
  - 상태 상수: `shop-api-common` OrderStatus.java (PLACED=1, PAID=2, SHIPPED=3, CANCELLED=4)
  - SQL: `OrderLifecycleSqlMap.xml` — WHERE status = #fromStatus#

---

### [위험 6] 배송비 계산 (등급 + 금액 + 도서산간)

- **경로**: `SettlementBiz.shippingFee(grade, itemsTotal, zipcode)`
- **위험 이유**: ③ 3중 조건 분기 — 등급, 금액 임계값, 우편번호 기반. 상수가 하드코딩됨
- **고정할 동작**:
  - Grade A → 0원 (무조건 무료)
  - Grade B/C + 5만 미만 → 2,500원
  - Grade B/C + 5만 이상 → 0원
  - 도서산간(zipcode "63" 또는 "40" 시작) → +3,000원 추가
- **기대값 도출**:
  - 상수: `shop-api-common` AppConstants — SHIPPING_FEE(2500), FREE_SHIP_THRESHOLD(50000)
  - 도서산간 추가: `shop-api-core` SettlementBiz:27 (3000 하드코딩)
  - 회원 등급: `db/data.sql` member.grade (A/B/C)

---

### [위험 7] VAT 계산 (불일치 존재)

- **경로 A**: `SettlementBiz.vat()` — VAT_RATE = 10%
- **경로 B**: `ReportSettlementCalc.vat()` — 11/100 하드코딩
- **위험 이유**: ③ 두 경로가 다른 세율을 사용. 현대화 시 어느 것이 정답인지 확인 필요
- **고정할 동작**:
  - 경로 A: (itemsTotal - discount + shippingFee) * 10 / 100
  - 경로 B: 같은 과세 기준 * 11 / 100
- **기대값 도출**:
  - 세율 A: `shop-api-common` AppConstants.VAT_RATE
  - 세율 B: `shop-api-core` ReportSettlementCalc 소스 코드 (11 하드코딩)
  - 출처 불명 — 실행해서 확인 필요: 어느 경로가 실제 API 응답에 반영되는지

---

### [위험 8] 커미션 계산 (Java/SQL 불일치)

- **경로 A**: `ReportSettlementCalc` (Java) — BOOKS=6%, default=11%
- **경로 B**: `SettlementBatchSqlMap.xml` (SQL) — BOOKS=5%, default=10%
- **위험 이유**: ③ 배치와 실시간 경로에서 다른 값 사용. 현대화 시 통합 결정 필요
- **고정할 동작**:
  - Java 경로: 카테고리별 (ELECTRONICS=12, ACCESSORY=8, HOME=10, FASHION=15, SPORTS=10, BOOKS=6, default=11)
  - SQL 경로: 카테고리별 (ELECTRONICS=12, ACCESSORY=8, HOME=10, FASHION=15, SPORTS=10, BOOKS=5, default=10)
- **기대값 도출**:
  - Java 상수: `shop-api-core` ReportSettlementCalc:12-20
  - SQL CASE: `SettlementBatchSqlMap.xml`:12-14
  - 카테고리 목록: `db/data.sql` product.category 값

---

### [위험 9] 쿠폰 적용 (할인 계산 + 유효성 검증)

- **경로**: `OrderBiz.applyCoupon()` → `CouponDao.findByCode()` + 검증 + `CouponDao.markUsed()`
- **위험 이유**: ③ 할인 타입(R/F), 최소 금액, 만료일, 카테고리 제한 등 다중 조건
- **고정할 동작**:
  - Rate(R): orderAmount * discount_val / 100
  - Flat(F): discount_val 고정 차감
  - 실패 조건: used_yn='Y', 만료, min_order 미달, 카테고리 불일치
- **기대값 도출**:
  - 쿠폰 데이터: `db/data.sql` + `db/data-extra.sql` coupon 테이블 (활성 2개, 만료 1개, VIP20, FLAT2000, CAT-*)
  - SQL: `CouponSqlMap.xml` findByCode 쿼리
  - 검증 로직: `shop-api-core` OrderBiz.applyCoupon() 소스

---

### [위험 10] 정산 배치 (SettlementBatch)

- **경로**: `SettlementBatch.run()` → `BatchDaoImpl` → `SettlementBatchSqlMap.xml` MERGE INTO settlement_daily
- **위험 이유**: ④ 트랜잭션 없이 MERGE 실행. 카테고리별 커미션 계산이 SQL에 내장
- **고정할 동작**:
  - 입력: orders(status=2) + order_item + product_option + product
  - 출력: settlement_daily (seller_name + settle_day별 gross_amount, commission, payout)
  - 커미션: 카테고리별 CASE WHEN (SQL 내장 비율)
- **기대값 도출**:
  - 시드 주문: `db/data-extra.sql` + `db/data-scale.sql` — order_id 101~320
  - SQL MERGE: `SettlementBatchSqlMap.xml`
  - 출처 불명 — 실행해서 확인 필요: MERGE 후 settlement_daily 실제 row 수와 금액

---

### [위험 11] 포인트 만료 배치 (PointExpiryBatch)

- **경로**: `PointExpiryBatch.run()` → `BatchDaoImpl.findExpiredPoints()` → `insertExpireLedger()` + `markSwept()`
- **위험 이유**: ④ 트랜잭션 없이 multi-row 처리. member.point는 직접 갱신하지 않음 (원장만 기록)
- **고정할 동작**:
  - 조건: swept_yn='N' AND member_id≥9 AND expire_date < today
  - 동작: point_ledger에 음수 INSERT (reason='EXPIRE') + point_expiry.swept_yn='Y'
  - 미동작: member.point 컬럼 직접 변경 없음
- **기대값 도출**:
  - 만료 시드: `db/data-scale.sql` — point_expiry 테이블 (2 aged/expired, 1 future)
  - SQL: `PointExpirySqlMap.xml`
  - member_id 필터: SQL WHERE절 (member_id >= 9)

---

### [위험 12] 일일 요약 배치 (DailySummaryBatch)

- **경로**: `DailySummaryBatch.run()` → `BatchDaoImpl` → MERGE INTO summary_daily
- **위험 이유**: ④ SettlementBatch에 의존하는 2차 집계. 선행 배치 실패 시 불완전 데이터
- **고정할 동작**:
  - 입력: settlement_daily
  - 출력: summary_daily (summary_day별 seller_count, gross_amount, payout_amount)
- **기대값 도출**:
  - 선행 데이터: SettlementBatch 실행 후 settlement_daily 내용
  - SQL: SettlementBatchSqlMap.xml 또는 별도 DailySummary SQL
  - 출처 불명 — 실행해서 확인 필요: 의존 관계 타이밍 (SettlementBatch 완료 전 DailySummary 실행 시 결과)

---

### [위험 13] 장바구니 → 주문 전환 (Cart checkout)

- **경로**: `CartServiceImpl.checkout()` → cart.status OPEN→ORDERED + `OrderFacade.placeOrder()` 위임
- **위험 이유**: ① 카트 상태 변경과 주문 생성이 결합. 주문 실패 시 카트 상태 불일치 가능
- **고정할 동작**:
  - 성공: cart.status='ORDERED', 주문 생성됨
  - 주문 실패(-1L): cart.status 상태 확인 필요 (롤백 여부)
- **기대값 도출**:
  - 카트 시드: `db/data-scale.sql` — 2개 open cart
  - SQL: `CartSqlMap.xml` markOrdered

---

### [위험 14] 상품 조회 (ON_SALE 필터)

- **경로**: 여러 Controller → `ProductServiceImpl` → `ProductSqlMap.xml` (WHERE status='ON_SALE')
- **위험 이유**: ⑤ API 계약 — 응답에 포함되는 키·구조. DISCONTINUED/SOLD_OUT 상품이 노출되면 회귀
- **고정할 동작**:
  - findAllOnSale: ON_SALE 상품만 반환, SOLD_OUT/DISCONTINUED 제외
  - 응답 키 구조: product_id, name, category, price, seller_name, status, options[]
- **기대값 도출**:
  - 상품 시드: `db/data.sql` (14 ON_SALE + 2 SOLD_OUT) + `db/data-extra.sql` (6 DISCONTINUED) + `db/data-scale.sql` (64 DISCONTINUED)
  - SQL: `ProductSqlMap.xml` findAllOnSale WHERE 절

---

### [위험 15] 프로모션 적용 (스택 + 날짜 게이팅 + 재고 임계값)

- **경로**: `PromotionBiz.applyPreview()` → `PromotionSqlMap.xml` findStackableForProduct
- **위험 이유**: ⑤ 복합 조건(날짜 범위 + ACTIVE 상태 + 재고≥5) 기반 할인 노출. 임계값이 하드코딩
- **고정할 동작**:
  - ACTIVE + 날짜 범위 내 + 재고≥5: 할인 표시
  - 날짜 범위 외 또는 재고<5: 할인 숨김
- **기대값 도출**:
  - 프로모션 시드: `db/data-scale.sql` (2 ACTIVE + 1 ENDED)
  - 임계값: `shop-api-core` PromotionBiz 소스 (5 하드코딩)
  - SQL: `PromotionSqlMap.xml` 날짜 비교 WHERE 절

---

## brainstorming 전달 메모

- **트랜잭션 경계**: `*ServiceImpl` 클래스에만 AOP 프록시로 적용 (`tx-context.xml`). Facade·Manager·Biz·Batch는 트랜잭션 밖
- **오류 표현 방식**: 예외 대신 매직 반환값 `-1L` (재고 부족, 결제 실패, 상태 전이 실패 모두 동일 패턴)
- **시드 데이터 위치**: `shop-api-persistence/src/main/resources/db/` — schema.sql → data.sql → data-extra.sql → data-scale.sql 순서 로드
- **테스트 시 주의할 전제·제약**:
  - H2 인메모리(MODE=MySQL) 그대로 사용 — 모킹 불필요
  - Spring ApplicationContext 로드하는 통합 테스트 형태
  - 배치 잡은 fixed-delay로 앱 기동 시 즉시 실행됨 — 테스트에서 자동 실행 방지 필요
  - AuditNotifier는 @Async — 비동기 검증 필요
  - PointExpiryBatch는 member_id≥9만 처리 — 시드 멤버 1~3은 대상 제외
- **출처 불명 항목** (실행해서 확인 필요):
  1. VAT 10% vs 11% — 실제 API 엔드포인트에서 어느 경로가 사용되는지
  2. 커미션 BOOKS 6% vs 5% — 실시간 조회와 배치 중 어느 것이 정산에 반영되는지
  3. SettlementBatch MERGE 후 settlement_daily 실제 결과값
  4. 주문 실패 시 cart.status 롤백 여부
  5. OrderBiz.earnPoint(flat 1%) vs PointManager.earn(등급별) — 어떤 조건에서 어느 경로가 선택되는지 정확한 분기 지점
