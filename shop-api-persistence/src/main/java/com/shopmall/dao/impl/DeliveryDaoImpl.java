package com.shopmall.dao.impl;

import com.shopmall.dao.DeliveryDao;
import com.ibatis.sqlmap.client.SqlMapClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class DeliveryDaoImpl extends SqlMapClientDaoSupport implements DeliveryDao {

    @Autowired
    public void init(SqlMapClient sqlMapClient) {
        super.setSqlMapClient(sqlMapClient);
    }

    public void insertDelivery(Map<String, Object> delivery) {
        getSqlMapClientTemplate().insert("Delivery.insertDelivery", delivery);
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> findByOrderId(long orderId) {
        return (Map<String, Object>) getSqlMapClientTemplate().queryForObject("Delivery.findByOrderId", orderId);
    }
}
