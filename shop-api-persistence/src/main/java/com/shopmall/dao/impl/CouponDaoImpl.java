package com.shopmall.dao.impl;

import com.shopmall.dao.CouponDao;
import com.ibatis.sqlmap.client.SqlMapClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class CouponDaoImpl extends SqlMapClientDaoSupport implements CouponDao {

    @Autowired
    public void init(SqlMapClient sqlMapClient) {
        super.setSqlMapClient(sqlMapClient);
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> findApplicableCoupon(Map<String, Object> param) {
        return (Map<String, Object>) getSqlMapClientTemplate().queryForObject("Coupon.findApplicableCoupon", param);
    }

    public void markUsed(long couponId) {
        getSqlMapClientTemplate().update("Coupon.markUsed", couponId);
    }
}
