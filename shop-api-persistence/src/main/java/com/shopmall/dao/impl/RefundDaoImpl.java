package com.shopmall.dao.impl;

import com.shopmall.dao.RefundDao;
import com.ibatis.sqlmap.client.SqlMapClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class RefundDaoImpl extends SqlMapClientDaoSupport implements RefundDao {

    @Autowired
    public void init(SqlMapClient sqlMapClient) {
        super.setSqlMapClient(sqlMapClient);
    }

    // selectKey type="post" writes the generated refund_id back into the map under "refundId"
    public void insertRefund(Map<String, Object> r) {
        getSqlMapClientTemplate().insert("Refund.insertRefund", r);
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> findByOrderId(long orderId) {
        return getSqlMapClientTemplate().queryForList("Refund.findByOrderId", orderId);
    }
}
