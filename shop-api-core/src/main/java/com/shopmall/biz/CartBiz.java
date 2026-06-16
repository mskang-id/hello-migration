package com.shopmall.biz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Cart business component. Provides the cart-total summation helper used by the
// cart service; the controller keeps its own quick sum over LINE_TOTAL for the view.
@Component
public class CartBiz {

    @Autowired private com.shopmall.dao.PromotionDao promotionDao;

    // sums LINE_TOTAL across the raw UPPERCASE rows the DAO returns
    public long sumCartTotal(List<Map<String,Object>> rows) {
        long total = 0L;
        if (rows == null) {
            return total;
        }
        for (Map<String,Object> r : rows) {
            Object lt = r.get("LINE_TOTAL");
            if (lt != null) {
                total += ((Number) lt).longValue();
            }
        }
        return total;
    }

    public long stackedDiscountForCart(List<Map<String,Object>> rows) {
        long d = 0L;
        if (rows == null) return 0L;
        String today = com.shopmall.common.util.DateUtil.today();
        for (Map<String,Object> r : rows) {
            Map<String,Object> p = new HashMap<String,Object>();
            p.put("optionId", ((Number) r.get("OPTION_ID")).longValue());
            p.put("today", today);
            d += (long) promotionDao.sumStackedDiscountForOption(p) * ((Number) r.get("QTY")).intValue();
        }
        return d;
    }
}
