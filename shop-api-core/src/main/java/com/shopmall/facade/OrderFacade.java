package com.shopmall.facade;

import com.shopmall.service.OrderService;
import com.shopmall.service.OrderQueryService;
import com.shopmall.service.DeliveryService;
import com.shopmall.service.QuoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Map;

// Order entrypoint: place the order then open its delivery row.
@Component
public class OrderFacade {

    @Autowired private OrderService orderService;
    @Autowired private OrderQueryService orderQueryService;
    @Autowired private DeliveryService deliveryService;
    @Autowired private QuoteService quoteService;
    @Autowired private com.shopmall.service.RefundService refundService;

    @SuppressWarnings("unchecked")
    public Object placeOrder(Map<String, Object> param) {
        Object result = orderService.placeOrder(param);
        if (result instanceof Map) {
            Map<String, Object> order = (Map<String, Object>) result;
            Object oid = order.get("orderId");
            if (oid != null) {
                long orderId = ((Number) oid).longValue();
                long memberId = ((Number) order.get("memberId")).longValue();
                // open the delivery row for the new order
                deliveryService.createForOrder(orderId, memberId);
            }
        }
        return result;
    }

    // used by DeliveryServiceImpl to read the order header back
    public Map<String, Object> getOrderSummary(long orderId) {
        return orderQueryService.getOrderDetail(orderId) == null
            ? null
            : (Map<String, Object>) orderQueryService.getOrderDetail(orderId).get("order");
    }

    public Map<String, Object> getOrderDetail(long orderId) {
        return orderQueryService.getOrderDetail(orderId);
    }

    public Map<String, Object> getMemberOrderHistory(long memberId, int page, int size) {
        return orderQueryService.getMemberOrderHistory(memberId, page, size);
    }

    public Map<String, Object> getSettlement(long orderId) {
        return orderQueryService.getSettlement(orderId);
    }

    // read-only price preview (no persistence) — keeps the facade tier in the loop
    public Map<String, Object> quote(Map<String, Object> param) {
        return quoteService.computeQuote(param);
    }

    public Object cancelOrder(long id, Integer partial) { return refundService.cancelOrder(id, partial); }
    public Object ship(long id) { return refundService.ship(id); }
}
