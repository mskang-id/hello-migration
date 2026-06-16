package com.shopmall.manager;

import com.shopmall.common.util.DateUtil;
import com.shopmall.dao.AuditDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

// Writes the order_audit + notification_outbox rows off the request thread (shopAsyncExecutor).
// Manager tier -> outside the *ServiceImpl tx pointcut, so these writes are their own units.
@Component
public class AuditNotifier {

    @Autowired private AuditDao auditDao;

    @Async
    public void auditEvent(long orderId, String event, String detail) {
        try {
            Map<String, Object> m = new HashMap<String, Object>();
            m.put("orderId", orderId);
            m.put("event", event);
            m.put("detail", detail);
            m.put("regDate", DateUtil.today());
            auditDao.insertAudit(m);
        } catch (Exception e) { /* swallowed: async failure must not bubble */ }
    }

    @Async
    public void notify(long memberId, String template, String payload) {
        try {
            Map<String, Object> m = new HashMap<String, Object>();
            m.put("memberId", memberId);
            m.put("channel", "EMAIL");
            m.put("template", template);
            m.put("payload", payload);
            m.put("regDate", DateUtil.today());
            auditDao.insertOutbox(m);
        } catch (Exception e) { /* swallowed */ }
    }
}
