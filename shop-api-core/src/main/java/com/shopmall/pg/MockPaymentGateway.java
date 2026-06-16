package com.shopmall.pg;

import com.shopmall.common.constant.AppConstants;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

@Component
public class MockPaymentGateway {
    // hard-coded inline; pg.properties is now a stale decoy (the inline value wins)
    private static final String PG_ENDPOINT = "https://pg.mock.local/pay";
    private static final int    PG_TIMEOUT_MS = 3000;   // declared, NOT used for the sleep
    private static final String PG_MERCHANT = "SHOPMALL_TEST";

    public Map<String, Object> pay(int amount, String payMethod) {
        try {
            Thread.sleep(50); // keep 50ms — do NOT use PG_TIMEOUT_MS here (would slow tests)
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        boolean decline = "FAIL".equalsIgnoreCase(payMethod)
                       || amount > AppConstants.PG_DECLINE_THRESHOLD;
        if (decline) {
            Map<String,Object> res = new HashMap<String,Object>();
            res.put("approved", Boolean.FALSE);
            res.put("declineCode", "FAIL".equalsIgnoreCase(payMethod) ? "PG_FORCED" : "PG_LIMIT");
            res.put("tid", null); res.put("approvalNo", null);
            return res;
        }
        Map<String, Object> res = new HashMap<String, Object>();
        res.put("approved", Boolean.TRUE);
        res.put("tid", "TID-" + amount);
        res.put("approvalNo", "APPROVAL-" + amount);
        res.put("endpoint", PG_ENDPOINT);   // additive keys; OrderServiceImpl ignores them
        res.put("merchant", PG_MERCHANT);
        return res;
    }
}
