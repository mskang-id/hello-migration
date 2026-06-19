package com.shopmall.characterization;

import com.shopmall.facade.OrderFacade;
import com.shopmall.manager.OrderStatusGuard;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Connection;
import java.sql.Statement;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Characterization test for OrderStatusGuard transition logic.
 * Risk 5: Valid/invalid transitions, optimistic locking, sentinel -1 returns.
 */
public class OrderStatusGuardCharacterizationTest extends BaseIntegrationTest {

    @Autowired
    private OrderStatusGuard orderStatusGuard;

    @Autowired
    private OrderFacade orderFacade;

    @Test
    public void canTransition_validTransitions() {
        // Valid transitions: PLACED→PAID, PAID→SHIPPED, PAID→CANCELLED, SHIPPED→CANCELLED
        assertThat(orderStatusGuard.canTransition(1, 2)).isTrue();  // PLACED → PAID
        assertThat(orderStatusGuard.canTransition(2, 3)).isTrue();  // PAID → SHIPPED
        assertThat(orderStatusGuard.canTransition(2, 4)).isTrue();  // PAID → CANCELLED
        assertThat(orderStatusGuard.canTransition(3, 4)).isTrue();  // SHIPPED → CANCELLED
    }

    @Test
    public void canTransition_invalidTransitions() {
        // Invalid: skip, backward, self-transition
        assertThat(orderStatusGuard.canTransition(1, 3)).isFalse(); // PLACED → SHIPPED (skip)
        assertThat(orderStatusGuard.canTransition(3, 2)).isFalse(); // SHIPPED → PAID (backward)
        assertThat(orderStatusGuard.canTransition(2, 2)).isFalse(); // PAID → PAID (self)
        assertThat(orderStatusGuard.canTransition(4, 2)).isFalse(); // CANCELLED → PAID (backward)
        assertThat(orderStatusGuard.canTransition(1, 4)).isFalse(); // PLACED → CANCELLED (invalid)
    }

    @Test
    public void transition_validPaidToShipped_returnsNewStatus() throws Exception {
        // GIVEN: place order (starts at status 2: PAID)
        long orderId = placeTestOrder(1L, 1L, 1, "CARD");

        // WHEN: transition to SHIPPED
        int result = orderStatusGuard.transition(orderId, 3);

        // THEN: returns 3 (new status)
        assertThat(result).isEqualTo(3);

        // THEN: status in DB is 3
        int status = queryInt("SELECT STATUS FROM shop_order WHERE ORDER_ID = " + orderId);
        assertThat(status).isEqualTo(3);
    }

    @Test
    public void transition_invalidPlacedToShipped_returnsMinusOne() throws Exception {
        // GIVEN: place order then force status to 1 (PLACED)
        long orderId = placeTestOrder(1L, 1L, 1, "CARD");
        Connection conn = dataSource.getConnection();
        Statement st = conn.createStatement();
        st.executeUpdate("UPDATE shop_order SET STATUS = 1 WHERE ORDER_ID = " + orderId);
        st.close();

        // WHEN: try to transition from PLACED to SHIPPED (invalid)
        int result = orderStatusGuard.transition(orderId, 3);

        // THEN: returns -1 (invalid transition)
        assertThat(result).isEqualTo(-1);

        // THEN: status remains 1 (unchanged)
        int status = queryInt("SELECT STATUS FROM shop_order WHERE ORDER_ID = " + orderId);
        assertThat(status).isEqualTo(1);
    }

    @Test
    public void transition_optimisticLock_concurrentChange_returnsMinusOne() throws Exception {
        // GIVEN: place order (status 2: PAID)
        long orderId = placeTestOrder(1L, 1L, 1, "CARD");

        // WHEN: force status to 4 (simulating concurrent cancel)
        Connection conn = dataSource.getConnection();
        Statement st = conn.createStatement();
        st.executeUpdate("UPDATE shop_order SET STATUS = 4 WHERE ORDER_ID = " + orderId);
        st.close();

        // WHEN: try to transition to SHIPPED (expects status=2, but actual is 4)
        int result = orderStatusGuard.transition(orderId, 3);

        // THEN: returns -1 (optimistic lock failure)
        assertThat(result).isEqualTo(-1);

        // THEN: status remains 4 (unchanged)
        int status = queryInt("SELECT STATUS FROM shop_order WHERE ORDER_ID = " + orderId);
        assertThat(status).isEqualTo(4);
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
