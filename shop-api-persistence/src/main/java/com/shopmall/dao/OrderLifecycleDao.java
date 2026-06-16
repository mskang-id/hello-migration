package com.shopmall.dao;

import java.util.Map;

public interface OrderLifecycleDao {
    int updateStatus(Map<String, Object> p); // returns rowcount (guarded by from-status)
    int findStatus(long orderId);
}
