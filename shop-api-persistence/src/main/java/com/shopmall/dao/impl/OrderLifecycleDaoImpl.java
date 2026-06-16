package com.shopmall.dao.impl;

import com.shopmall.dao.OrderLifecycleDao;
import com.ibatis.sqlmap.client.SqlMapClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class OrderLifecycleDaoImpl extends SqlMapClientDaoSupport implements OrderLifecycleDao {

    @Autowired
    public void init(SqlMapClient sqlMapClient) {
        super.setSqlMapClient(sqlMapClient);
    }

    public int updateStatus(Map<String, Object> p) {
        return getSqlMapClientTemplate().update("OrderLifecycle.updateStatus", p);
    }

    public int findStatus(long orderId) {
        return (Integer) getSqlMapClientTemplate().queryForObject("OrderLifecycle.findStatus", orderId);
    }
}
