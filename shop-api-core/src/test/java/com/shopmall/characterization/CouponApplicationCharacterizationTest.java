package com.shopmall.characterization;

import com.shopmall.biz.OrderBiz;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class CouponApplicationCharacterizationTest extends BaseIntegrationTest {

    @Autowired
    private OrderBiz orderBiz;

    @Test
    public void rateCoupon_appliesPercentDiscount() throws Exception {
        int discount = orderBiz.applyCoupon(1L, 100000);
        assertThat(discount).isEqualTo(10000);

        String usedYn = queryString("SELECT used_yn FROM coupon WHERE coupon_id = 1");
        assertThat(usedYn).isEqualTo("Y");
    }

    @Test
    public void flatCoupon_appliesFixedDiscount() throws Exception {
        int discount = orderBiz.applyCoupon(2L, 50000);
        assertThat(discount).isEqualTo(5000);

        String usedYn = queryString("SELECT used_yn FROM coupon WHERE coupon_id = 2");
        assertThat(usedYn).isEqualTo("Y");
    }

    @Test
    public void minOrderNotMet_returnsZero() throws Exception {
        int discount = orderBiz.applyCoupon(2L, 20000);
        assertThat(discount).isEqualTo(0);

        String usedYn = queryString("SELECT used_yn FROM coupon WHERE coupon_id = 2");
        assertThat(usedYn).isEqualTo("N");
    }

    @Test
    public void expiredCoupon_returnsZero() {
        int discount = orderBiz.applyCoupon(3L, 100000);
        assertThat(discount).isEqualTo(0);
    }

    @Test
    public void nullCouponId_returnsZero() {
        int discount = orderBiz.applyCoupon(null, 100000);
        assertThat(discount).isEqualTo(0);
    }

    @Test
    public void alreadyUsedCoupon_returnsZero() {
        orderBiz.applyCoupon(1L, 100000);

        int discount = orderBiz.applyCoupon(1L, 100000);
        assertThat(discount).isEqualTo(0);
    }

    @Test
    public void categoryRestrictedCoupon_matchingCategory_applies() throws Exception {
        int couponId = queryInt("SELECT coupon_id FROM coupon WHERE code = 'CAT-ELECTRONICS'");

        int discount = orderBiz.applyCoupon((long) couponId, 100000);
        assertThat(discount).isGreaterThan(0);
    }

    @Test
    public void categoryRestrictedCoupon_noMatchingProducts_returnsZero() throws Exception {
        int couponId = queryInt("SELECT coupon_id FROM coupon WHERE code = 'CAT-TOYS'");

        int discount = orderBiz.applyCoupon((long) couponId, 100000);
        assertThat(discount).isEqualTo(0);
    }
}
