package com.shopmall.dao.impl;
import com.shopmall.dao.BatchDao;
import com.ibatis.sqlmap.client.SqlMapClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;

@Repository
public class BatchDaoImpl extends SqlMapClientDaoSupport implements BatchDao {
    @Autowired public void init(SqlMapClient sqlMapClient) { super.setSqlMapClient(sqlMapClient); }
    public int rollupSettlementDay(Map<String,Object> param) {
        return getSqlMapClientTemplate().update("SettlementBatch.rollupDay", param);
    }
    @SuppressWarnings("unchecked")
    public List<Map<String,Object>> findExpirablePoints(Map<String,Object> param) {
        return getSqlMapClientTemplate().queryForList("PointExpiry.findExpirable", param);
    }
    public int insertExpireLedger(Map<String,Object> param) {
        getSqlMapClientTemplate().insert("PointExpiry.insertExpireLedger", param);
        return 1;   // normalized to keep the int signature (generated key not needed)
    }
    public int markSwept(long peId) {
        return getSqlMapClientTemplate().update("PointExpiry.markSwept", peId);
    }
    @SuppressWarnings("unchecked")
    public List<Map<String,Object>> findSettlementDaily() {
        return getSqlMapClientTemplate().queryForList("SettlementBatch.findAll");
    }
    @SuppressWarnings("unchecked")
    public List<Map<String,Object>> findPointLedger() {
        return getSqlMapClientTemplate().queryForList("PointExpiry.findLedger");
    }
    public int rollupDailySummary(Map<String,Object> param) {
        return getSqlMapClientTemplate().update("SettlementBatch.rollupDailySummary", param);
    }
}
