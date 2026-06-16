package com.shopmall.dao.impl;

import com.shopmall.dao.InventoryLogDao;
import com.ibatis.sqlmap.client.SqlMapClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class InventoryLogDaoImpl extends SqlMapClientDaoSupport implements InventoryLogDao {

    @Autowired
    public void init(SqlMapClient sqlMapClient) {
        super.setSqlMapClient(sqlMapClient);
    }

    public void insertLog(Map<String, Object> log) {
        getSqlMapClientTemplate().insert("InventoryLog.insertLog", log);
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> findByOptionId(long optionId) {
        return getSqlMapClientTemplate().queryForList("InventoryLog.findByOptionId", optionId);
    }
}
