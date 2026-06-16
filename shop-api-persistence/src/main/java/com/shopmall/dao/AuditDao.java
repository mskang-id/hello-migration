package com.shopmall.dao;

import java.util.List;
import java.util.Map;

public interface AuditDao {
    void insertAudit(Map<String, Object> m);
    void insertOutbox(Map<String, Object> m);
    List<Map<String, Object>> findAuditByOrder(long orderId);
}
