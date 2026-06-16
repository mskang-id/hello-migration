package com.shopmall.common.constant;
public final class AppConstants {
    private AppConstants() {}
    public static final String DEFAULT_CARRIER   = "CJ";
    public static final int    POINT_EARN_RATE   = 1;       // percent (mirrors existing inline */100)
    public static final int    FREE_SHIP_THRESHOLD = 50000; // KRW
    public static final int    SHIPPING_FEE       = 2500;   // flat
    public static final int    VAT_RATE           = 10;     // percent
    public static final int    REPORT_TOP_N       = 10;
    public static final int    DEFAULT_PAGE_SIZE  = 20;
    public static final boolean AUDIT_ENABLED      = true;
    public static final int    PG_DECLINE_THRESHOLD = 1000000; // 1,000,000 KRW
}
