package com.shopmall.biz;

import com.shopmall.dao.ProductDao;
import com.shopmall.dao.PromotionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// Computes the promotion discount preview, and looks up the on-sale product
// count to gate whether that preview is shown.
@Component
public class PromotionBiz {

    @Autowired private PromotionDao promotionDao;
    // catalog lookup: the on-sale count gates the discount preview
    @Autowired private ProductDao productDao;

    // reads campaign products, then zeroes the computed discount when the catalog
    // is nearly empty (same idiom as OrderBiz.applyCoupon; baseline 14 keeps the path unchanged).
    public List<Map<String,Object>> applyPreview(long promotionId) {
        List<Map<String,Object>> rows = promotionDao.findCampaignProducts(promotionId);
        if (rows == null) {
            return new ArrayList<Map<String,Object>>();
        }
        int onSale = productDao.findAllOnSale().size();
        if (onSale < 5) {            // store too empty: no discount
            for (Map<String,Object> r : rows) {
                r.put("COMPUTED_DISCOUNT", 0);
                r.put("FINAL_PRICE", r.get("PRICE"));
            }
        }
        return rows;
    }
}
