package com.shopmall.web;

import com.shopmall.common.envelope.ApiResponse;
import com.shopmall.common.error.ErrorCode;
import com.shopmall.facade.OrderFacade;
import com.shopmall.web.dto.lifecycle.CancelRequestXml;
import com.shopmall.web.dto.lifecycle.RefundResultXml;
import com.shopmall.web.dto.lifecycle.StatusResultXml;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.Map;

@Controller
public class OrderLifecycleController {

    @Autowired private OrderFacade orderFacade;

    @RequestMapping(value = "/api/orders/{id}/cancel", method = RequestMethod.POST)
    @ResponseBody
    @SuppressWarnings({"unchecked", "rawtypes"})
    public ApiResponse cancel(@PathVariable long id) {
        Object r = orderFacade.cancelOrder(id, null);
        if (r instanceof Long && ((Long) r).longValue() == -1L)
            return ResponseFactory.fail(ErrorCode.INVALID_STATE, "cannot cancel order", null);
        return ResponseFactory.ok(toRefundXml((Map) r));
    }

    @RequestMapping(value = "/api/orders/{id}/refund", method = RequestMethod.POST)
    @ResponseBody
    @SuppressWarnings({"unchecked", "rawtypes"})
    public ApiResponse refund(@PathVariable long id, @RequestBody CancelRequestXml req) {
        Object r = orderFacade.cancelOrder(id, req.getPartialAmount());
        if (r instanceof Long && ((Long) r).longValue() == -1L)
            return ResponseFactory.fail(ErrorCode.INVALID_STATE, "cannot refund order", null);
        return ResponseFactory.ok(toRefundXml((Map) r));
    }

    @RequestMapping(value = "/api/orders/{id}/ship", method = RequestMethod.POST)
    @ResponseBody
    @SuppressWarnings({"unchecked", "rawtypes"})
    public ApiResponse ship(@PathVariable long id) {
        Object r = orderFacade.ship(id);
        if (r instanceof Long && ((Long) r).longValue() == -1L)
            return ResponseFactory.fail(ErrorCode.INVALID_STATE, "invalid status transition", null);
        Map m = (Map) r;
        StatusResultXml xml = new StatusResultXml();
        xml.setOrderId(((Number) m.get("orderId")).longValue());
        xml.setStatus(m.get("status") == null ? null : ((Number) m.get("status")).intValue());
        xml.setResult(String.valueOf(m.get("result")));
        return ResponseFactory.ok(xml);
    }

    @SuppressWarnings("rawtypes")
    private RefundResultXml toRefundXml(Map m) {
        RefundResultXml xml = new RefundResultXml();
        xml.setOrderId(((Number) m.get("orderId")).longValue());
        xml.setRefundId(m.get("refundId") == null ? null : ((Number) m.get("refundId")).longValue());
        xml.setRefundAmount(m.get("refundAmount") == null ? null : ((Number) m.get("refundAmount")).intValue());
        xml.setRefundType(String.valueOf(m.get("refundType")));
        return xml;
    }
}
