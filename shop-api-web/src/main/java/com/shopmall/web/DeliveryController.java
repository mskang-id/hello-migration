package com.shopmall.web;

import com.shopmall.common.envelope.ApiResponse;
import com.shopmall.common.error.ErrorCode;
import com.shopmall.service.DeliveryService;
import com.shopmall.web.dto.delivery.DeliveryXml;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.Map;

@Controller
@RequestMapping("/api/deliveries")
public class DeliveryController {

    @Autowired private DeliveryService deliveryService;

    @RequestMapping(value = "/{orderId}", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse byOrder(@PathVariable("orderId") long orderId) {
        Map<String, Object> d = deliveryService.getByOrderId(orderId);
        if (d == null) {
            return ResponseFactory.fail(ErrorCode.NOT_FOUND, "delivery not found", null);
        }
        DeliveryXml xml = new DeliveryXml();
        xml.setDeliveryId(((Number) d.get("DELIVERY_ID")).longValue());
        xml.setOrderId(((Number) d.get("ORDER_ID")).longValue());
        xml.setCarrier(d.get("CARRIER") == null ? null : String.valueOf(d.get("CARRIER")));
        xml.setTrackingNo(d.get("TRACKING_NO") == null ? null : String.valueOf(d.get("TRACKING_NO")));
        xml.setStatus(String.valueOf(d.get("STATUS")));
        xml.setRegDate(String.valueOf(d.get("REG_DATE")));
        return ResponseFactory.ok(xml);
    }
}
