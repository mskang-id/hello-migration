package com.shopmall.service.impl;

import com.shopmall.dao.MemberDao;
import com.shopmall.dao.OrderDao;
import com.shopmall.dao.RefundDao;
import com.shopmall.biz.SettlementBiz;
import com.shopmall.manager.AuditNotifier;
import com.shopmall.manager.InventoryManager;
import com.shopmall.manager.OrderStatusGuard;
import com.shopmall.manager.PointManager;
import com.shopmall.manager.StockManager;
import com.shopmall.common.constant.OrderStatus;
import com.shopmall.common.util.DateUtil;
import com.shopmall.common.util.OrderXmlExporter;
import com.shopmall.service.RefundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// *ServiceImpl -> auto tx advice: the whole cancel/refund reversal is one atomic unit.
@Service
public class RefundServiceImpl implements RefundService {

    @Autowired private OrderDao orderDao;
    @Autowired private MemberDao memberDao;
    @Autowired private RefundDao refundDao;
    @Autowired private StockManager stockManager;
    @Autowired private PointManager pointManager;
    @Autowired private InventoryManager inventoryManager;
    @Autowired private SettlementBiz settlementBiz;
    @Autowired private OrderStatusGuard orderStatusGuard;
    @Autowired private AuditNotifier auditNotifier; // fire-and-forget audit/notify, outside this tx

    @SuppressWarnings({"unchecked", "rawtypes"})
    public Object cancelOrder(long orderId, Integer partialAmount) {
        try {
            Map order = orderDao.findOrderById(orderId);
            if (order == null) return new Long(-1);
            long memberId = ((Number) order.get("MEMBER_ID")).longValue();
            boolean full = (partialAmount == null);

            if (full) {
                int to = orderStatusGuard.transition(orderId, OrderStatus.CANCELLED);
                if (to == -1) return new Long(-1);                 // cannot cancel (already CANCELLED, etc.)
                List<Map<String, Object>> lines = orderDao.findOrderItems(orderId);
                for (Map<String, Object> li : lines) {
                    long optId = ((Number) li.get("OPTION_ID")).longValue();
                    int q = ((Number) li.get("QTY")).intValue();
                    stockManager.restock(optId, q);
                    inventoryManager.logChange(optId, +q, "RESTOCK");  // positive log reversing the ORDER rows
                }
            }
            // reverse points from the settlement row
            Map settle = orderDao.findSettlement(orderId);
            int usePoint = settle == null ? 0 : ((Number) settle.get("USE_POINT")).intValue();
            int payAmount = settle == null ? 0 : ((Number) settle.get("PAY_AMOUNT")).intValue();
            Map member = memberDao.findById(memberId);
            String grade = member == null ? "C" : String.valueOf(member.get("GRADE"));
            int earned = payAmount * settlementBiz.earnRate(grade) / 100;
            int refundAmount = full ? payAmount : partialAmount.intValue();
            if (full) {
                pointManager.restorePoint(memberId, usePoint);
                pointManager.revoke(memberId, earned);
            } else {
                // pro-rata partial: scale points by refundAmount/payAmount (integer math; legacy)
                int ratioNum = payAmount == 0 ? 0 : refundAmount;
                pointManager.restorePoint(memberId, payAmount == 0 ? 0 : usePoint * ratioNum / payAmount);
                pointManager.revoke(memberId, payAmount == 0 ? 0 : earned * ratioNum / payAmount);
            }

            Map<String, Object> r = new HashMap<String, Object>();
            r.put("orderId", orderId);
            r.put("refundType", full ? "FULL" : "PARTIAL");
            r.put("refundAmount", refundAmount);
            r.put("pointRestore", usePoint);
            r.put("pointRevoke", earned);
            r.put("pgTid", order.get("PG_TID"));
            r.put("reason", "user-requested");
            r.put("regDate", DateUtil.today());
            refundDao.insertRefund(r);
            long refundId = ((Number) r.get("refundId")).longValue();

            // fire-and-forget audit + notify, outside this tx
            auditNotifier.auditEvent(orderId, "CANCELLED", OrderXmlExporter.toXml(order));
            auditNotifier.notify(memberId, "ORDER_CANCELLED", "refund=" + refundAmount);

            Map<String, Object> out = new HashMap<String, Object>();
            out.put("orderId", orderId);
            out.put("refundId", refundId);
            out.put("refundAmount", refundAmount);
            out.put("refundType", full ? "FULL" : "PARTIAL");
            return out;
        } catch (Exception e) {
            e.printStackTrace();          // returns -1 so the caller can detect failure
            return new Long(-1);
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public Object ship(long orderId) {
        int to = orderStatusGuard.transition(orderId, OrderStatus.SHIPPED);
        Map<String, Object> out = new HashMap<String, Object>();
        out.put("orderId", orderId);
        out.put("status", to == -1 ? null : to);
        out.put("result", to == -1 ? "INVALID_STATE" : "SHIPPED");
        return to == -1 ? (Object) new Long(-1) : out;
    }
}
