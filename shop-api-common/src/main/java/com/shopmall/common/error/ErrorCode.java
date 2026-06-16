package com.shopmall.common.error;

// Result codes carried in the envelope header. A few call sites also return the
// literal -1 in the body; keep the numeric code in the message too — some old
// clients parse the string.
public final class ErrorCode {
    private ErrorCode() {}
    public static final String SUCCESS        = "0000";
    public static final String NOT_FOUND      = "E4040";
    public static final String OUT_OF_STOCK   = "E4090";
    public static final String BAD_REQUEST    = "E4000";
    public static final String INTERNAL_ERROR = "E5000";
    public static final String PAYMENT_DECLINED = "E4020";
    public static final String INVALID_STATE  = "E4091";
}
