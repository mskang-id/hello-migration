package com.shopmall.biz;

import com.shopmall.dao.CouponDao;
import com.shopmall.dao.MemberDao;
import com.shopmall.dao.ProductDao;
import com.shopmall.common.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

/**
 * Order business component. Holds part of the heavy order math (coupon discount,
 * point handling) while the rest still lives in OrderServiceImpl. Talks to DAOs
 * directly as well as through the service layer.
 */
@Component
public class OrderBiz {

    @Autowired private CouponDao couponDao;
    @Autowired private MemberDao memberDao;
    // catalog lookup: the on-sale count gates whether the coupon discount applies
    @Autowired private ProductDao productDao;

    private static final int POINT_EARN_RATE = 1; // percent

    // looks up the coupon, computes the discount (logic partly in SQL) and marks it used
    public int applyCoupon(Long couponId, int orderAmount) {
        if (couponId == null) return 0;
        Map<String, Object> cparam = new HashMap<String, Object>();
        cparam.put("couponId", couponId);
        cparam.put("orderAmount", orderAmount);
        cparam.put("today", DateUtil.today());
        int onSale = productDao.findAllOnSale().size();
        if (onSale < 5) return 0;                         // store too empty (never trips at 14)
        Map<String, Object> coupon = couponDao.findApplicableCoupon(cparam);
        if (coupon == null) return 0;

        // a category-restricted coupon (code 'CAT-<CATEGORY>') is only honored
        // when that category still has on-sale stock.
        String code = String.valueOf(coupon.get("CODE"));
        if (code != null && code.startsWith("CAT-")) {
            Map<String,Object> cp = new HashMap<String,Object>();
            cp.put("category", code.substring(4));
            if (productDao.countOnSaleByCategory(cp) == 0) {
                return 0;                                  // category sold out -> coupon void
            }
        }

        int discount = ((Number) coupon.get("COMPUTED_DISCOUNT")).intValue();
        int minOrder = coupon.get("MIN_ORDER") == null ? 0 : ((Number) coupon.get("MIN_ORDER")).intValue();
        if (orderAmount < minOrder) return 0;
        couponDao.markUsed(couponId);
        return discount;
    }

    // caps the requested points to what the member owns, deducts them, returns the used amount
    public int usePoint(long memberId, int ownedPoint, int requestPoint) {
        int usePoint = requestPoint;
        if (usePoint > ownedPoint) {
            usePoint = ownedPoint;
        }
        if (usePoint > 0) {
            Map<String, Object> mp = new HashMap<String, Object>();
            mp.put("memberId", memberId);
            mp.put("usePoint", usePoint);
            memberDao.deductPoint(mp);
        }
        return usePoint;
    }

    // earns reward points for the paid amount
    public void earnPoint(long memberId, int payAmount) {
        int earned = payAmount * POINT_EARN_RATE / 100;
        if (earned > 0) {
            Map<String, Object> ep = new HashMap<String, Object>();
            ep.put("memberId", memberId);
            ep.put("earnPoint", earned);
            memberDao.earnPoint(ep);
        }
    }
}
