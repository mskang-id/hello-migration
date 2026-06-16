package com.shopmall.service;

import java.util.Map;

public interface OrderQueryService {
    Map<String, Object> getOrderDetail(long orderId); // {order: Map, items: List<Map>} or null
    Map<String, Object> getMemberOrderHistory(long memberId, int page, int size);
    Map<String, Object> getSettlement(long orderId);
}
