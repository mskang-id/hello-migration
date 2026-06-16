package com.shopmall.service;

import java.util.Map;

public interface DeliveryService {
    void createForOrder(long orderId, long memberId);
    Map<String, Object> getByOrderId(long orderId);
}
