package com.shopmall.biz;

import com.shopmall.common.constant.AppConstants;
import com.shopmall.common.util.DateUtil;
import com.shopmall.dao.CouponDao;
import com.shopmall.dao.MemberDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

@Component
public class SettlementBiz {
    @Autowired private CouponDao couponDao;   // read-only preview (no markUsed)
    @Autowired private MemberDao memberDao;

    // grade-tiered earn rate (grade finally drives computation)
    public int earnRate(String grade) {
        if ("A".equals(grade)) return 3;
        if ("B".equals(grade)) return 2;
        return AppConstants.POINT_EARN_RATE; // C -> 1%
    }
    // shipping fee: free over threshold (grade A always free), else flat; +island surcharge by zipcode
    public int shippingFee(String grade, int itemsTotal, String zipcode) {
        if ("A".equals(grade)) return 0;
        int fee = itemsTotal >= AppConstants.FREE_SHIP_THRESHOLD ? 0 : AppConstants.SHIPPING_FEE;
        if (zipcode != null && (zipcode.startsWith("63") || zipcode.startsWith("40"))) fee += 3000; // island/remote
        return fee;
    }
    public int vat(int taxable) { return taxable * AppConstants.VAT_RATE / 100; }

    // read-only coupon preview reusing the SQL CASE (does NOT mark used)
    public int previewDiscount(Long couponId, int itemsTotal) {
        if (couponId == null) return 0;
        Map<String,Object> p = new HashMap<String,Object>();
        p.put("couponId", couponId); p.put("orderAmount", itemsTotal); p.put("today", DateUtil.today());
        Map<String,Object> c = couponDao.findApplicableCoupon(p);
        return c == null ? 0 : ((Number) c.get("COMPUTED_DISCOUNT")).intValue();
    }
}
