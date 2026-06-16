package com.shopmall.dao;

import java.util.List;
import java.util.Map;

public interface InventoryLogDao {
    void insertLog(Map<String, Object> log);
    List<Map<String, Object>> findByOptionId(long optionId);
}
