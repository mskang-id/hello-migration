package com.shopmall.web;

import com.shopmall.common.envelope.ApiResponse;
import com.shopmall.common.error.ErrorCode;
import com.shopmall.facade.OrderFacade;
import com.shopmall.web.dto.order.OrderItemXml;
import com.shopmall.web.dto.order.OrderQuoteXml;
import com.shopmall.web.dto.order.OrderRequestXml;
import com.shopmall.web.dto.order.OrderResultXml;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class OrderController {

    @Autowired private OrderFacade orderFacade;

    @RequestMapping(value = "/orders", method = RequestMethod.POST)
    @ResponseBody
    @SuppressWarnings("unchecked")
    public ApiResponse placeOrder(@RequestBody OrderRequestXml req) {
        // adapt the XML DTO into the legacy Map param shape the service expects
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("memberId", req.getMemberId());
        List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
        if (req.getItems() != null) {
            for (OrderItemXml it : req.getItems()) {
                Map<String, Object> line = new HashMap<String, Object>();
                line.put("optionId", it.getOptionId());
                line.put("qty", it.getQty());
                items.add(line);
            }
        }
        param.put("items", items);
        param.put("couponId", req.getCouponId());
        param.put("usePoint", req.getUsePoint());
        param.put("payMethod", req.getPayMethod());
        param.put("zipcode", req.getZipcode());
        param.put("address", req.getAddress());

        Object result = orderFacade.placeOrder(param);

        // failure: legacy magic -1, double-encoded as header E4090 + body orderId=-1
        if (result instanceof Long && ((Long) result).longValue() == -1L) {
            OrderResultXml fail = new OrderResultXml();
            fail.setOrderId(-1L);
            return ResponseFactory.fail(ErrorCode.OUT_OF_STOCK, "out of stock or invalid option", fail);
        }

        Map<String, Object> order = (Map<String, Object>) result;
        OrderResultXml xml = new OrderResultXml();
        xml.setOrderId(order.get("orderId") == null ? null : ((Number) order.get("orderId")).longValue());
        xml.setMemberId(((Number) order.get("memberId")).longValue());
        xml.setOrderDate(String.valueOf(order.get("orderDate")));
        xml.setStatus(((Number) order.get("status")).intValue());
        xml.setTotalPrice(((Number) order.get("totalPrice")).intValue());
        xml.setPayMethod(order.get("payMethod") == null ? null : String.valueOf(order.get("payMethod")));
        xml.setPgTid(String.valueOf(order.get("pgTid")));
        xml.setApprovalNo(String.valueOf(order.get("approvalNo")));
        return ResponseFactory.ok(xml);
    }

    // non-persisting price preview: grade-tiered earn + shipping + VAT branching.
    // does NOT mark coupons used, does NOT deduct stock, does NOT persist.
    @RequestMapping(value = "/api/orders/quote", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponse quote(@RequestBody OrderRequestXml req) {
        // adapt the XML DTO into the legacy Map param shape the service expects
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("memberId", req.getMemberId());
        List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
        if (req.getItems() != null) {
            for (OrderItemXml it : req.getItems()) {
                Map<String, Object> line = new HashMap<String, Object>();
                line.put("optionId", it.getOptionId());
                line.put("qty", it.getQty());
                items.add(line);
            }
        }
        param.put("items", items);
        param.put("couponId", req.getCouponId());
        param.put("usePoint", req.getUsePoint());
        param.put("payMethod", req.getPayMethod());
        param.put("zipcode", req.getZipcode());
        param.put("address", req.getAddress());

        Map<String, Object> q = orderFacade.quote(param);
        if (q == null) {
            return ResponseFactory.fail(ErrorCode.NOT_FOUND, "invalid option or member", null);
        }
        OrderQuoteXml xml = new OrderQuoteXml();
        xml.setItemsTotal(((Number) q.get("itemsTotal")).intValue());
        xml.setDiscount(((Number) q.get("discount")).intValue());
        xml.setShippingFee(((Number) q.get("shippingFee")).intValue());
        xml.setVat(((Number) q.get("vat")).intValue());
        xml.setGrandTotal(((Number) q.get("grandTotal")).intValue());
        xml.setEarnPreview(((Number) q.get("earnPreview")).intValue());
        xml.setGrade(String.valueOf(q.get("grade")));
        return ResponseFactory.ok(xml);
    }
}
