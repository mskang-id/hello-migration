package com.shopmall.web;

import com.shopmall.common.constant.AppConstants;
import com.shopmall.common.envelope.ApiResponse;
import com.shopmall.common.error.ErrorCode;
import com.shopmall.facade.OrderFacade;
import com.shopmall.web.dto.order.OrderDetailXml;
import com.shopmall.web.dto.order.OrderHistoryRowXml;
import com.shopmall.web.dto.order.OrderHistoryXml;
import com.shopmall.web.dto.order.OrderItemXml;
import com.shopmall.web.dto.order.OrderSettlementXml;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;
import java.util.Map;

@Controller
public class OrderQueryController {

    @Autowired private OrderFacade orderFacade;

    @RequestMapping(value = "/api/orders/{id}", method = RequestMethod.GET)
    @ResponseBody
    @SuppressWarnings("unchecked")
    public ApiResponse detail(@PathVariable("id") long id) {
        Map<String, Object> detail = orderFacade.getOrderDetail(id);
        if (detail == null) {
            return ResponseFactory.fail(ErrorCode.NOT_FOUND, "order not found", null);
        }
        Map<String, Object> order = (Map<String, Object>) detail.get("order");
        List<Map<String, Object>> items = (List<Map<String, Object>>) detail.get("items");

        OrderDetailXml xml = new OrderDetailXml();
        xml.setOrderId(((Number) order.get("ORDER_ID")).longValue());
        xml.setMemberId(((Number) order.get("MEMBER_ID")).longValue());
        xml.setOrderDate(String.valueOf(order.get("ORDER_DATE")));
        xml.setStatus(((Number) order.get("STATUS")).intValue());
        xml.setTotalPrice(((Number) order.get("TOTAL_PRICE")).intValue());
        xml.setPayMethod(order.get("PAY_METHOD") == null ? null : String.valueOf(order.get("PAY_METHOD")));
        for (Map<String, Object> it : items) {
            OrderItemXml ix = new OrderItemXml();
            ix.setOptionId(((Number) it.get("OPTION_ID")).longValue());
            ix.setQty(((Number) it.get("QTY")).intValue());
            ix.setUnitPrice(((Number) it.get("UNIT_PRICE")).intValue());
            xml.getItems().add(ix);
        }
        return ResponseFactory.ok(xml);
    }

    @RequestMapping(value = "/api/members/{id}/orders", method = RequestMethod.GET)
    @ResponseBody
    @SuppressWarnings("unchecked")
    public ApiResponse memberOrders(@PathVariable("id") long id,
                                    @RequestParam(value = "page", defaultValue = "0") int page,
                                    @RequestParam(value = "size", required = false) Integer sizeParam) {
        int size = (sizeParam == null || sizeParam <= 0) ? AppConstants.DEFAULT_PAGE_SIZE : sizeParam;
        Map<String, Object> result = orderFacade.getMemberOrderHistory(id, page, size);
        List<Map<String, Object>> rows = (List<Map<String, Object>>) result.get("rows");

        OrderHistoryXml xml = new OrderHistoryXml();
        xml.setMemberId(id);
        xml.setPage(page);
        xml.setSize(size);
        xml.setTotal(((Number) result.get("total")).intValue());
        xml.setLoyaltyTier(result.get("loyaltyTier") == null ? null : String.valueOf(result.get("loyaltyTier")));
        for (Map<String, Object> row : rows) {
            OrderHistoryRowXml r = new OrderHistoryRowXml();
            r.setOrderId(((Number) row.get("ORDER_ID")).longValue());
            r.setOrderDate(row.get("ORDER_DATE") == null ? null : String.valueOf(row.get("ORDER_DATE")));
            r.setStatus(((Number) row.get("STATUS")).intValue());
            r.setTotalPrice(((Number) row.get("TOTAL_PRICE")).intValue());
            r.setProductName(row.get("PRODUCT_NAME") == null ? null : String.valueOf(row.get("PRODUCT_NAME")));
            r.setOptionName(row.get("OPTION_NAME") == null ? null : String.valueOf(row.get("OPTION_NAME")));
            r.setQty(((Number) row.get("QTY")).intValue());
            r.setUnitPrice(((Number) row.get("UNIT_PRICE")).intValue());
            xml.getRows().add(r);
        }
        return ResponseFactory.ok(xml);
    }

    @RequestMapping(value = "/api/orders/{id}/settlement", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse settlement(@PathVariable("id") long id) {
        Map<String, Object> s = orderFacade.getSettlement(id);
        if (s == null) {
            return ResponseFactory.fail(ErrorCode.NOT_FOUND, "settlement not found", null);
        }
        OrderSettlementXml xml = new OrderSettlementXml();
        xml.setOrderId(((Number) s.get("ORDER_ID")).longValue());
        xml.setItemTotal(((Number) s.get("ITEM_TOTAL")).intValue());
        xml.setDiscount(((Number) s.get("DISCOUNT")).intValue());
        xml.setUsePoint(((Number) s.get("USE_POINT")).intValue());
        xml.setPayAmount(((Number) s.get("PAY_AMOUNT")).intValue());
        xml.setShippingFee(((Number) s.get("SHIPPING_FEE")).intValue());
        xml.setVat(((Number) s.get("VAT")).intValue());
        xml.setGrandTotal(((Number) s.get("GRAND_TOTAL")).intValue());
        xml.setRegDate(String.valueOf(s.get("REG_DATE")));
        return ResponseFactory.ok(xml);
    }
}
