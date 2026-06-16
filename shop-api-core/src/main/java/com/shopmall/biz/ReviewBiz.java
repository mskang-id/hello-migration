package com.shopmall.biz;

import com.shopmall.common.util.DateUtil;
import com.shopmall.common.vo.ProductVO;
import com.shopmall.dao.MemberDao;
import com.shopmall.dao.OrderDao;
import com.shopmall.dao.ProductDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

// Review business component. Looks up product + member info and enforces the
// 1..5 rating rule in Java before persisting.
@Component
public class ReviewBiz {

    @Autowired private ProductDao productDao;
    @Autowired private MemberDao memberDao;
    @Autowired private OrderDao orderDao;

    // validates the referenced product/member exist, clamps rating into 1..5,
    // and builds the insert param Map. Returns null when the product is missing
    // (caller maps that to the magic -1 / NOT_FOUND).
    public Map<String,Object> validateAndBuild(Map<String,Object> param) {
        Object pidObj = param.get("productId");
        Object midObj = param.get("memberId");
        if (pidObj == null || midObj == null) {
            return null;
        }
        long productId = ((Number) pidObj).longValue();
        long memberId = ((Number) midObj).longValue();
        ProductVO product = productDao.findById(productId);
        if (product == null) {
            return null;
        }
        Map<String,Object> member = memberDao.findById(memberId);
        if (member == null) {
            return null;
        }
        int rating = param.get("rating") == null ? 0 : ((Number) param.get("rating")).intValue();
        if (rating < 1) { rating = 1; }
        if (rating > 5) { rating = 5; }
        Map<String,Object> vp = new HashMap<String,Object>();
        vp.put("memberId", memberId); vp.put("productId", productId);
        int bought = orderDao.countMemberProductPurchases(vp);
        Map<String,Object> built = new HashMap<String,Object>();
        built.put("productId", productId);
        built.put("memberId", memberId);
        built.put("rating", rating);
        built.put("title", param.get("title"));
        built.put("body", param.get("body"));
        built.put("regDate", DateUtil.today());
        built.put("verified", bought > 0 ? "Y" : "N"); // verified-buyer flag
        return built;
    }
}
