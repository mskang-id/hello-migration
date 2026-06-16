package com.shopmall.dao;

import java.util.Map;

public interface CouponDao {
    Map<String, Object> findApplicableCoupon(Map<String, Object> param);
    void markUsed(long couponId);
}
