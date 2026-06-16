package com.shopmall.dao;

import java.util.Map;

public interface DeliveryDao {
    void insertDelivery(Map<String, Object> delivery); // selectKey -> deliveryId back into map
    Map<String, Object> findByOrderId(long orderId);
}
