package com.shopmall.manager;

import com.shopmall.common.constant.OrderStatus;
import com.shopmall.dao.OrderLifecycleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

// Order status transitions. Manager tier so it runs outside the *ServiceImpl tx proxy;
// the guarded UPDATE (... AND status = #fromStatus#) is the optimistic transition check.
@Component
public class OrderStatusGuard {

    @Autowired private OrderLifecycleDao dao;

    // 1 PLACED ->2 PAID ->3 SHIPPED ; 2/3 ->4 CANCELLED ; no backward, no self
    public boolean canTransition(int from, int to) {
        if (from == OrderStatus.PLACED && to == OrderStatus.PAID) return true;
        if (from == OrderStatus.PAID && to == OrderStatus.SHIPPED) return true;
        if ((from == OrderStatus.PAID || from == OrderStatus.SHIPPED) && to == OrderStatus.CANCELLED) return true;
        return false;
    }

    public int transition(long orderId, int to) {
        int from = dao.findStatus(orderId);
        if (!canTransition(from, to)) return -1;     // magic -1 sentinel preserved
        Map<String, Object> p = new HashMap<String, Object>();
        p.put("orderId", orderId);
        p.put("status", to);
        p.put("fromStatus", from);
        int n = dao.updateStatus(p);
        return n > 0 ? to : -1;
    }
}
