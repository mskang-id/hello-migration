package com.shopmall.dao;

import java.util.List;
import java.util.Map;

public interface RefundDao {
    void insertRefund(Map<String, Object> r);   // selectKey -> refundId back into map
    List<Map<String, Object>> findByOrderId(long orderId);
}
