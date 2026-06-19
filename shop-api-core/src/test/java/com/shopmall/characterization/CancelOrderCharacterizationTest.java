package com.shopmall.characterization;

import com.shopmall.facade.OrderFacade;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Connection;
import java.sql.Statement;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Characterization test for RefundServiceImpl.cancelOrder() behavior.
 * Risk 2: Full + partial cancel, status transitions, stock reversal, point reversal, audit/notify.
 */
public class CancelOrderCharacterizationTest extends BaseIntegrationTest {

    @Autowired
    private OrderFacade orderFacade;

    @Test
    public void cancelOrder_paidToCancel_restoresStockAndPoints() throws Exception {
        // GIVEN: place order (member 1: alice, grade A, initial point 10000, product_option 1: stock 100, price 15000)
        long orderId = placeTestOrder(1L, 1L, 2, "CARD");
        int stockBefore = queryInt("SELECT STOCK_QTY FROM product_option WHERE OPTION_ID = 1");
        int pointBefore = queryInt("SELECT POINT FROM member WHERE MEMBER_ID = 1");

        // WHEN: full cancel
        Object result = orderFacade.cancelOrder(orderId, null);

        // THEN: returns map with refund info
        assertThat(result).isInstanceOf(Map.class);
        Map<String, Object> refundMap = (Map<String, Object>) result;
        assertThat(refundMap.get("orderId")).isEqualTo(orderId);
        assertThat(refundMap.get("refundType")).isEqualTo("FULL");

        // THEN: order status = 4 (CANCELLED)
        int status = queryInt("SELECT STATUS FROM shop_order WHERE ORDER_ID = " + orderId);
        assertThat(status).isEqualTo(4);

        // THEN: stock restored (+2)
        int stockAfter = queryInt("SELECT STOCK_QTY FROM product_option WHERE OPTION_ID = 1");
        assertThat(stockAfter).isEqualTo(stockBefore + 2);

        // THEN: points restored (use_point) and earned points revoked
        int pointAfter = queryInt("SELECT POINT FROM member WHERE MEMBER_ID = 1");
        // alice used no points (requestPoint=0 in placeTestOrder), earned 3% of payAmount
        // payAmount = 15000*2 - 0 = 30000, earned = 30000 * 3 / 100 = 900
        // pointAfter should be pointBefore - 900
        assertThat(pointAfter).isEqualTo(pointBefore - 900);

        // THEN: refund record created
        int refundCount = queryInt("SELECT COUNT(*) FROM refund WHERE ORDER_ID = " + orderId);
        assertThat(refundCount).isEqualTo(1);
    }

    @Test
    public void cancelOrder_shippedToCancel_succeeds() throws Exception {
        // GIVEN: place order then ship
        long orderId = placeTestOrder(1L, 1L, 1, "CARD");
        orderFacade.ship(orderId);

        // WHEN: cancel after shipped
        Object result = orderFacade.cancelOrder(orderId, null);

        // THEN: succeeds (returns map, not -1L)
        assertThat(result).isInstanceOf(Map.class);
        Map<String, Object> refundMap = (Map<String, Object>) result;
        assertThat(refundMap.get("orderId")).isEqualTo(orderId);

        // THEN: status = 4 (CANCELLED)
        int status = queryInt("SELECT STATUS FROM shop_order WHERE ORDER_ID = " + orderId);
        assertThat(status).isEqualTo(4);
    }

    @Test
    public void cancelOrder_placedToCancel_invalidTransition_returnsMinusOne() throws Exception {
        // GIVEN: place order (starts at status 2: PAID) then force status to 1 (PLACED)
        long orderId = placeTestOrder(1L, 1L, 1, "CARD");
        Connection conn = dataSource.getConnection();
        Statement st = conn.createStatement();
        st.executeUpdate("UPDATE shop_order SET STATUS = 1 WHERE ORDER_ID = " + orderId);
        st.close();

        // WHEN: try to cancel from PLACED
        Object result = orderFacade.cancelOrder(orderId, null);

        // THEN: returns -1L (invalid transition PLACED -> CANCELLED)
        assertThat(result).isInstanceOf(Long.class);
        assertThat(((Long) result).longValue()).isEqualTo(-1L);
    }

    @Test
    public void cancelOrder_alreadyCancelled_returnsMinusOne() throws Exception {
        // GIVEN: place and cancel once
        long orderId = placeTestOrder(1L, 1L, 1, "CARD");
        orderFacade.cancelOrder(orderId, null);

        // WHEN: cancel again
        Object result = orderFacade.cancelOrder(orderId, null);

        // THEN: returns -1L (already cancelled)
        assertThat(result).isInstanceOf(Long.class);
        assertThat(((Long) result).longValue()).isEqualTo(-1L);
    }

    @Test
    public void cancelOrder_createsAuditAndNotification() throws Exception {
        // GIVEN: place order
        long orderId = placeTestOrder(1L, 1L, 1, "CARD");

        // WHEN: cancel
        orderFacade.cancelOrder(orderId, null);

        // THEN: wait for async audit/notify
        Thread.sleep(500);

        // THEN: audit record created
        int auditCount = queryInt("SELECT COUNT(*) FROM order_audit WHERE ORDER_ID = " + orderId + " AND EVENT_TYPE = 'CANCELLED'");
        assertThat(auditCount).isEqualTo(1);

        // THEN: notification created
        int notificationCount = queryInt("SELECT COUNT(*) FROM notification_outbox WHERE MEMBER_ID = 1 AND EVENT_TYPE = 'ORDER_CANCELLED'");
        assertThat(notificationCount).isGreaterThan(0);
    }

    // Helper: place a test order
    private long placeTestOrder(long memberId, long optionId, int qty, String payMethod) {
        Map<String, Object> item = new HashMap<String, Object>();
        item.put("optionId", optionId);
        item.put("qty", qty);

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("memberId", memberId);
        param.put("items", Collections.singletonList(item));
        param.put("payMethod", payMethod);
        param.put("zipcode", "12345");
        param.put("address", "Test Address");

        Object result = orderFacade.placeOrder(param);
        Map<String, Object> order = (Map<String, Object>) result;
        return ((Number) order.get("orderId")).longValue();
    }
}
