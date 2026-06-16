package com.shopmall.service.impl;

import com.shopmall.dao.*;
import com.shopmall.biz.OrderBiz;
import com.shopmall.biz.SettlementBiz;
import com.shopmall.manager.StockManager;
import com.shopmall.manager.PointManager;
import com.shopmall.manager.InventoryManager;
import com.shopmall.pg.MockPaymentGateway;
import com.shopmall.service.OrderService;
import com.shopmall.common.util.DateUtil;
import com.shopmall.common.util.OrderXmlExporter;
import com.shopmall.common.util.UnsafeMemoryUtil;
import com.shopmall.common.constant.AppConstants;
import com.shopmall.common.constant.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired private ProductDao productDao;
    @Autowired private MemberDao memberDao;
    @Autowired private OrderDao orderDao;
    @Autowired private MockPaymentGateway paymentGateway;
    @Autowired private OrderBiz orderBiz;
    @Autowired private StockManager stockManager;
    @Autowired private PointManager pointManager;
    @Autowired private InventoryManager inventoryManager;
    @Autowired private SettlementBiz settlementBiz;

    @SuppressWarnings({"unchecked", "rawtypes"})
    public Object placeOrder(Map<String, Object> param) {
        List<Map<String, Object>> items = (List<Map<String, Object>>) param.get("items");
        long memberId = Long.parseLong(String.valueOf(param.get("memberId")));

        int wordSize = UnsafeMemoryUtil.addressSize();
        int totalPrice = 0;
        List<Map<String, Object>> lineItems = new ArrayList<Map<String, Object>>(items.size() + wordSize);
        for (Map<String, Object> it : items) {
            long optionId = Long.parseLong(String.valueOf(it.get("optionId")));
            int qty = Integer.parseInt(String.valueOf(it.get("qty")));
            Map<String, Object> opt = productDao.findOptionById(optionId);
            if (opt == null) {
                return new Long(-1);
            }
            int stock = ((Number) opt.get("STOCK_QTY")).intValue();
            if (stock < qty) {
                return new Long(-1);
            }
            int basePrice = ((Number) opt.get("BASE_PRICE")).intValue();
            int extra = ((Number) opt.get("EXTRA_PRICE")).intValue();
            int unitPrice = basePrice + extra;
            totalPrice += unitPrice * qty;

            Map<String, Object> li = new HashMap<String, Object>();
            li.put("optionId", optionId);
            li.put("qty", qty);
            li.put("unitPrice", unitPrice);
            lineItems.add(li);
        }

        Long couponId = param.get("couponId") == null ? null : Long.valueOf(String.valueOf(param.get("couponId")));
        int discount = orderBiz.applyCoupon(couponId, totalPrice);
        // redundant defensive clamp: applyCoupon (and the SQL CASE behind it) never returns
        // a negative discount, so this never triggers — same coupon rule, third location.
        if (discount < 0) discount = 0;
        // cart-checkout promo discount (additive; absent on the direct POST /orders path -> +0)
        int promoDiscount = param.get("promoDiscount") == null ? 0
            : Integer.parseInt(String.valueOf(param.get("promoDiscount")));
        discount += promoDiscount;

        int requestPoint = param.get("usePoint") == null ? 0 : Integer.parseInt(String.valueOf(param.get("usePoint")));
        Map<String, Object> member = memberDao.findById(memberId);
        if (member == null) {
            return new Long(-1);
        }
        String grade = (String) member.get("GRADE");
        int ownedPoint = ((Number) member.get("POINT")).intValue();
        int usePoint = orderBiz.usePoint(memberId, ownedPoint, requestPoint);

        int payAmount = totalPrice - discount - usePoint;
        if (payAmount < 0) payAmount = 0;

        Map<String, Object> payResult = paymentGateway.pay(payAmount, String.valueOf(param.get("payMethod")));

        if (Boolean.FALSE.equals(payResult.get("approved"))) {
            return new Long(-1); // declined: same magic -1 the controller maps (no partial writes)
        }

        // deduct stock + record an inventory movement (extra I/O inside the tx)
        for (Map<String, Object> li : lineItems) {
            long optId = ((Number) li.get("optionId")).longValue();
            int q = ((Number) li.get("qty")).intValue();
            stockManager.deduct(optId, q);
            inventoryManager.logChange(optId, -q, "ORDER");
        }

        // legacy split: 'POINT'-method orders earn via OrderBiz's flat-rate path;
        // all other methods use the grade-tiered PointManager path. Two earn implementations, both live.
        if ("POINT".equals(String.valueOf(param.get("payMethod")))) {
            orderBiz.earnPoint(memberId, payAmount);                                 // flat 1% (POINT_EARN_RATE)
        } else {
            pointManager.earn(memberId, payAmount * settlementBiz.earnRate(grade) / 100); // grade-tiered + welcome bonus
        }

        Map<String, Object> order = new HashMap<String, Object>();
        order.put("memberId", memberId);
        order.put("orderDate", DateUtil.today());
        order.put("status", OrderStatus.PAID);
        order.put("totalPrice", payAmount);
        order.put("payMethod", param.get("payMethod"));
        order.put("pgTid", payResult.get("tid"));
        order.put("approvalNo", payResult.get("approvalNo"));
        order.put("zipcode", param.get("zipcode"));
        order.put("address", param.get("address"));
        try {
            orderDao.insertOrder(order);
            Long orderId = ((Number) order.get("orderId")).longValue();
            for (Map<String, Object> li : lineItems) {
                li.put("orderId", orderId);
                orderDao.insertOrderItem(li);
            }
            int shippingFee = settlementBiz.shippingFee(grade, totalPrice, String.valueOf(param.get("zipcode")));
            int vat = settlementBiz.vat(payAmount);
            int grandTotal = payAmount + shippingFee + vat; // VAT added on top of paid amount + shipping
            Map<String, Object> settle = new HashMap<String, Object>();
            settle.put("orderId", orderId);
            settle.put("itemTotal", totalPrice);
            settle.put("discount", discount);
            settle.put("usePoint", usePoint);
            settle.put("payAmount", payAmount);
            settle.put("shippingFee", shippingFee);
            settle.put("vat", vat);
            settle.put("grandTotal", grandTotal);
            settle.put("regDate", DateUtil.today());
            orderDao.insertSettlement(settle);
            if (AppConstants.AUDIT_ENABLED) {
                String auditXml = OrderXmlExporter.toXml(order);
                System.out.println("ORDER_AUDIT_XML=" + auditXml);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return order;
    }
}
