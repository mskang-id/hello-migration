CREATE TABLE product (
    product_id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    name         VARCHAR(200) NOT NULL,
    category     VARCHAR(50),
    price        INT NOT NULL,
    seller_name  VARCHAR(100),
    status       VARCHAR(20) NOT NULL
);

CREATE TABLE product_option (
    option_id    BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id   BIGINT NOT NULL,
    option_name  VARCHAR(100) NOT NULL,
    extra_price  INT DEFAULT 0,
    stock_qty    INT NOT NULL
);

CREATE TABLE member (
    member_id    BIGINT AUTO_INCREMENT PRIMARY KEY,
    login_id     VARCHAR(50) NOT NULL,
    name         VARCHAR(100),
    grade        CHAR(1) NOT NULL,
    point        INT DEFAULT 0
);

CREATE TABLE orders (
    order_id     BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id    BIGINT NOT NULL,
    order_date   VARCHAR(8) NOT NULL,
    status       INT NOT NULL,
    total_price  INT NOT NULL,
    pay_method   VARCHAR(20),
    pg_tid       VARCHAR(50),
    approval_no  VARCHAR(50),
    zipcode      VARCHAR(6),
    address      VARCHAR(300)
);

CREATE TABLE order_item (
    order_item_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id      BIGINT NOT NULL,
    option_id     BIGINT NOT NULL,
    qty           INT NOT NULL,
    unit_price    INT NOT NULL
);

CREATE TABLE coupon (
    coupon_id     BIGINT AUTO_INCREMENT PRIMARY KEY,
    code          VARCHAR(50) NOT NULL,
    discount_type CHAR(1) NOT NULL,
    discount_val  INT NOT NULL,
    min_order     INT DEFAULT 0,
    expire_date   VARCHAR(8) NOT NULL,
    used_yn       CHAR(1) DEFAULT 'N'
);

CREATE TABLE delivery (
    delivery_id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id      BIGINT NOT NULL,
    carrier       VARCHAR(50),
    tracking_no   VARCHAR(50),
    status        VARCHAR(20) NOT NULL,
    reg_date      VARCHAR(8) NOT NULL
);

CREATE TABLE inventory_log (
    log_id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    option_id     BIGINT NOT NULL,
    change_qty    INT NOT NULL,
    reason        VARCHAR(30) NOT NULL,
    reg_date      VARCHAR(8) NOT NULL
);

CREATE TABLE order_settlement (
    order_id      BIGINT PRIMARY KEY,
    item_total    INT NOT NULL,
    discount      INT NOT NULL,
    use_point     INT NOT NULL,
    pay_amount    INT NOT NULL,
    shipping_fee  INT NOT NULL,
    vat           INT NOT NULL,
    grand_total   INT NOT NULL,
    reg_date      VARCHAR(8) NOT NULL
);

-- ===== Group-2 DOMAIN tables (ADDITIVE; never touched by baseline paths) =====
CREATE TABLE cart (
    cart_id     BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id   BIGINT NOT NULL,
    status      VARCHAR(20) NOT NULL,        -- OPEN / ORDERED
    reg_date    VARCHAR(8) NOT NULL          -- yyyymmdd
);
CREATE TABLE cart_item (
    cart_item_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cart_id      BIGINT NOT NULL,
    option_id    BIGINT NOT NULL,            -- -> product_option (no FK, legacy)
    qty          INT NOT NULL
);
CREATE TABLE promotion (
    promotion_id  BIGINT AUTO_INCREMENT PRIMARY KEY,
    name          VARCHAR(200) NOT NULL,
    discount_type CHAR(1) NOT NULL,          -- 'R' rate / 'F' flat (mirrors coupon)
    discount_val  INT NOT NULL,
    start_date    VARCHAR(8) NOT NULL,
    end_date      VARCHAR(8) NOT NULL,
    status        VARCHAR(20) NOT NULL        -- ACTIVE / ENDED
);
CREATE TABLE campaign_product (
    campaign_product_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    promotion_id        BIGINT NOT NULL,
    product_id          BIGINT NOT NULL       -- -> product (no FK)
);
CREATE TABLE review (
    review_id     BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id    BIGINT NOT NULL,             -- -> product (no FK)
    member_id     BIGINT NOT NULL,             -- -> member (no FK)
    rating        INT NOT NULL,                -- 1..5 validated in Biz, NOT a DB constraint
    title         VARCHAR(200),
    body          VARCHAR(1000),
    reg_date      VARCHAR(8) NOT NULL,
    helpful_count INT DEFAULT 0,               -- G3-3 additive
    verified      VARCHAR(1) DEFAULT 'N'       -- G3-3 additive (verified-purchase flag)
);
CREATE TABLE wishlist (
    wishlist_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id   BIGINT NOT NULL,             -- -> member (no FK)
    product_id  BIGINT NOT NULL,             -- -> product (no FK)
    reg_date    VARCHAR(8) NOT NULL
);

-- ===== Group-2 BATCH tables (ADDITIVE; never read/written by baseline paths) =====
CREATE TABLE settlement_daily (
    seller_name   VARCHAR(100) NOT NULL,
    settle_day    VARCHAR(8)   NOT NULL,   -- VARCHAR(8) date
    gross_amount  INT NOT NULL,
    commission    INT NOT NULL,
    payout        INT NOT NULL,
    reg_date      VARCHAR(8)   NOT NULL,
    PRIMARY KEY (seller_name, settle_day)  -- MERGE...KEY replaces a day on re-run (idempotent)
);
CREATE TABLE point_expiry (
    pe_id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id    BIGINT NOT NULL,
    grant_amount INT NOT NULL,
    expire_date  VARCHAR(8) NOT NULL,      -- yyyyMMdd; string-compared (DateUtil.isExpired style)
    swept_yn     CHAR(1) DEFAULT 'N'
);
CREATE TABLE point_ledger (
    ledger_id    BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id    BIGINT NOT NULL,
    delta        INT NOT NULL,             -- negative for EXPIRE
    reason       VARCHAR(30) NOT NULL,     -- 'EXPIRE'
    reg_date     VARCHAR(8) NOT NULL
);

-- ===== Group-3 LIFECYCLE tables (ADDITIVE; never read/written by baseline paths) =====
CREATE TABLE refund (
    refund_id     BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id      BIGINT NOT NULL,            -- -> orders (no FK, legacy)
    refund_type   VARCHAR(10) NOT NULL,       -- FULL / PARTIAL
    refund_amount INT NOT NULL,               -- payAmount reversed (or partial)
    point_restore INT NOT NULL,               -- used-points credited back
    point_revoke  INT NOT NULL,               -- earned-points clawed back
    pg_tid        VARCHAR(50),                -- mirrors orders.pg_tid for the reversal
    reason        VARCHAR(100),
    reg_date      VARCHAR(8) NOT NULL         -- yyyymmdd
);
CREATE TABLE order_audit (
    audit_id    BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id    BIGINT NOT NULL,
    event       VARCHAR(30) NOT NULL,         -- CANCELLED / SHIPPED / REFUNDED / NOTIFY
    detail      VARCHAR(500),                 -- free-form (often the OrderXmlExporter blob)
    reg_date    VARCHAR(8) NOT NULL
);
CREATE TABLE notification_outbox (
    noti_id     BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id   BIGINT NOT NULL,
    channel     VARCHAR(20) NOT NULL,         -- EMAIL / SMS (mock)
    template    VARCHAR(40) NOT NULL,         -- ORDER_CANCELLED etc.
    payload     VARCHAR(500),
    sent_yn     CHAR(1) DEFAULT 'N',
    reg_date    VARCHAR(8) NOT NULL
);
CREATE TABLE summary_daily (
    summary_day   VARCHAR(8) NOT NULL PRIMARY KEY,  -- MERGE...KEY idempotent re-run
    seller_count  INT NOT NULL,
    gross_amount  INT NOT NULL,
    payout_amount INT NOT NULL,
    reg_date      VARCHAR(8) NOT NULL
);
