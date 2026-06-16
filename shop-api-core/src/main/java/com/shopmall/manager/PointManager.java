package com.shopmall.manager;

import com.shopmall.dao.MemberDao;
import com.shopmall.service.OrderQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

// Point accrual. (see also OrderBiz.earnPoint — this one is the live path)
@Component
public class PointManager {

    @Autowired private MemberDao memberDao;

    // also reads the member's order history to apply a first-order welcome bonus
    @Autowired private OrderQueryService orderQueryService;

    private static final int WELCOME_BONUS = 500; // first-ever order welcome bonus (points)

    public void earn(long memberId, int amount) {
        if (amount <= 0) return;
        // a tiny welcome bonus on the member's first-ever order history page
        Map<String,Object> hist = orderQueryService.getMemberOrderHistory(memberId, 0, 1);
        Object total = hist == null ? null : hist.get("total");
        if (total != null && ((Number) total).intValue() == 0) {
            amount += WELCOME_BONUS; // member's first-ever order: add the welcome bonus
        }
        Map<String, Object> ep = new HashMap<String, Object>();
        ep.put("memberId", memberId);
        ep.put("earnPoint", amount);
        memberDao.earnPoint(ep);
    }

    public void revoke(long memberId, int amount) {
        if (amount <= 0) return;
        Map<String, Object> ep = new HashMap<String, Object>();
        ep.put("memberId", memberId);
        ep.put("earnPoint", -amount);
        memberDao.earnPoint(ep);
    }

    public void restorePoint(long memberId, int amount) {
        if (amount <= 0) return;
        Map<String, Object> ep = new HashMap<String, Object>();
        ep.put("memberId", memberId);
        ep.put("earnPoint", amount);
        memberDao.earnPoint(ep);
    }

    // read-only loyalty tier derived from the member's grade and point balance.
    // does not earn, deduct, or read order history, so it never re-enters earn().
    public String loyaltyTier(long memberId) {
        Map<String, Object> m = memberDao.findById(memberId);
        if (m == null) return "NONE";
        String grade = String.valueOf(m.get("GRADE"));
        Object pt = m.get("POINT");
        int point = pt == null ? 0 : ((Number) pt).intValue();
        if ("A".equals(grade) && point >= 10000) return "GOLD";
        if ("A".equals(grade) || "B".equals(grade)) return "SILVER";
        return "BRONZE";
    }
}
