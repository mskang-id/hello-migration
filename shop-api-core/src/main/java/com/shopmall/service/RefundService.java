package com.shopmall.service;

public interface RefundService {
    Object cancelOrder(long orderId, Integer partialAmount);
    Object ship(long orderId);
}
