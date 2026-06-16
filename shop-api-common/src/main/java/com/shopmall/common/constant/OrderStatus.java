package com.shopmall.common.constant;

// magic numbers preserved: 1=placed 2=paid 3=shipped 4=cancelled
public final class OrderStatus {
    private OrderStatus() {}
    public static final int PLACED = 1;
    public static final int PAID = 2;
    public static final int SHIPPED = 3;
    public static final int CANCELLED = 4;
}
