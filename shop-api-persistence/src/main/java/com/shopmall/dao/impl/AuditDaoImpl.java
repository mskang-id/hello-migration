package com.shopmall.dao.impl;

import com.shopmall.dao.AuditDao;
import com.ibatis.sqlmap.client.SqlMapClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class AuditDaoImpl extends SqlMapClientDaoSupport implements AuditDao {

    @Autowired
    public void init(SqlMapClient sqlMapClient) {
        super.setSqlMapClient(sqlMapClient);
    }

    public void insertAudit(Map<String, Object> m) {
        getSqlMapClientTemplate().insert("Audit.insertAudit", m);
    }

    public void insertOutbox(Map<String, Object> m) {
        getSqlMapClientTemplate().insert("Audit.insertOutbox", m);
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> findAuditByOrder(long orderId) {
        return getSqlMapClientTemplate().queryForList("Audit.findAuditByOrder", orderId);
    }
}
