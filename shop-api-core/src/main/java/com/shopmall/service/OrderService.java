package com.shopmall.service;

import java.util.Map;

public interface OrderService {
    // returns the order on success, or -1 when an item is out of stock
    Object placeOrder(Map<String, Object> param);
}
