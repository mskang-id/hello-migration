package com.shopmall.dao;
import java.util.List;
import java.util.Map;
public interface BatchDao {
    int rollupSettlementDay(Map<String,Object> param);            // SettlementBatch.rollupDay
    List<Map<String,Object>> findExpirablePoints(Map<String,Object> param); // PointExpiry.findExpirable
    int insertExpireLedger(Map<String,Object> param);             // PointExpiry.insertExpireLedger
    int markSwept(long peId);                                     // PointExpiry.markSwept
    List<Map<String,Object>> findSettlementDaily();               // SettlementBatch.findAll
    List<Map<String,Object>> findPointLedger();                   // PointExpiry.findLedger
    int rollupDailySummary(Map<String,Object> param);             // SettlementBatch.rollupDailySummary
}
