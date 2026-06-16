package com.shopmall.dao;

import java.util.List;
import java.util.Map;

public interface OrderDao {
    void insertOrder(Map<String, Object> order);        // selectKey -> orderId back into map
    void insertOrderItem(Map<String, Object> item);
    Map<String, Object> findOrderById(long orderId);
    List<Map<String, Object>> findOrderItems(long orderId);
    List<Map<String, Object>> findOrdersByMember(Map<String, Object> param);
    int countOrdersByMember(long memberId);
    void insertSettlement(Map<String, Object> settle);
    Map<String, Object> findSettlement(long orderId);
    int countMemberProductPurchases(Map<String, Object> p);
}
