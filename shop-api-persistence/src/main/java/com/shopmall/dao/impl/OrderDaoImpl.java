package com.shopmall.dao.impl;

import com.shopmall.dao.OrderDao;
import com.ibatis.sqlmap.client.SqlMapClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class OrderDaoImpl extends SqlMapClientDaoSupport implements OrderDao {

    @Autowired
    public void init(SqlMapClient sqlMapClient) {
        super.setSqlMapClient(sqlMapClient);
    }

    // selectKey type="post" writes the generated order_id back into the map under "orderId"
    public void insertOrder(Map<String, Object> order) {
        getSqlMapClientTemplate().insert("Order.insertOrder", order);
    }

    public void insertOrderItem(Map<String, Object> item) {
        getSqlMapClientTemplate().insert("Order.insertOrderItem", item);
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> findOrderById(long orderId) {
        return (Map<String, Object>) getSqlMapClientTemplate().queryForObject("Order.findOrderById", orderId);
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> findOrderItems(long orderId) {
        return getSqlMapClientTemplate().queryForList("Order.findOrderItems", orderId);
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> findOrdersByMember(Map<String, Object> param) {
        return getSqlMapClientTemplate().queryForList("Order.findOrdersByMember", param);
    }

    public int countOrdersByMember(long memberId) {
        return (Integer) getSqlMapClientTemplate().queryForObject("Order.countOrdersByMember", memberId);
    }

    public void insertSettlement(Map<String, Object> settle) {
        getSqlMapClientTemplate().insert("Order.insertSettlement", settle);
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> findSettlement(long orderId) {
        return (Map<String, Object>) getSqlMapClientTemplate().queryForObject("Order.findSettlement", orderId);
    }

    public int countMemberProductPurchases(Map<String, Object> p) {
        return (Integer) getSqlMapClientTemplate().queryForObject("Order.countMemberProductPurchases", p);
    }
}
