package com.shopmall.characterization;

import com.shopmall.facade.OrderFacade;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Characterization test for inventory ledger (inventory_log) behavior.
 * Risk 4: Stock deduction + ORDER log, cancel restock + RESTOCK log, multi-item independence.
 */
public class InventoryLedgerCharacterizationTest extends BaseIntegrationTest {

    @Autowired
    private OrderFacade orderFacade;

    @Test
    public void placeOrder_deductsStockAndLogsOrderReason() throws Exception {
        // GIVEN: product_option 1 has stock 100
        int stockBefore = queryInt("SELECT STOCK_QTY FROM product_option WHERE OPTION_ID = 1");

        // WHEN: place order (qty=3)
        long orderId = placeTestOrder(1L, 1L, 3, "CARD");

        // THEN: stock deducted by 3
        int stockAfter = queryInt("SELECT STOCK_QTY FROM product_option WHERE OPTION_ID = 1");
        assertThat(stockAfter).isEqualTo(stockBefore - 3);

        // THEN: inventory_log created with reason=ORDER, change_qty=-3
        Connection conn = dataSource.getConnection();
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(
            "SELECT CHANGE_QTY, REASON FROM inventory_log WHERE OPTION_ID = 1 AND ORDER_ID = " + orderId
        );
        assertThat(rs.next()).isTrue();
        assertThat(rs.getInt("CHANGE_QTY")).isEqualTo(-3);
        assertThat(rs.getString("REASON")).isEqualTo("ORDER");
        rs.close();
        st.close();
    }

    @Test
    public void cancelOrder_restocksAndLogsRestockReason() throws Exception {
        // GIVEN: place order (qty=2)
        long orderId = placeTestOrder(1L, 1L, 2, "CARD");
        int stockAfterOrder = queryInt("SELECT STOCK_QTY FROM product_option WHERE OPTION_ID = 1");

        // WHEN: cancel order (full cancel)
        orderFacade.cancelOrder(orderId, null);

        // THEN: stock restored by 2
        int stockAfterCancel = queryInt("SELECT STOCK_QTY FROM product_option WHERE OPTION_ID = 1");
        assertThat(stockAfterCancel).isEqualTo(stockAfterOrder + 2);

        // THEN: inventory_log created with reason=RESTOCK, change_qty=+2
        Connection conn = dataSource.getConnection();
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(
            "SELECT CHANGE_QTY, REASON FROM inventory_log WHERE OPTION_ID = 1 AND ORDER_ID = " + orderId + " AND REASON = 'RESTOCK'"
        );
        assertThat(rs.next()).isTrue();
        assertThat(rs.getInt("CHANGE_QTY")).isEqualTo(2);
        assertThat(rs.getString("REASON")).isEqualTo("RESTOCK");
        rs.close();
        st.close();
    }

    @Test
    public void multiItemOrder_logsEachOptionIndependently() throws Exception {
        // GIVEN: place order with 2 different options
        Map<String, Object> item1 = new HashMap<String, Object>();
        item1.put("optionId", 1L);
        item1.put("qty", 2);

        Map<String, Object> item2 = new HashMap<String, Object>();
        item2.put("optionId", 2L);
        item2.put("qty", 3);

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("memberId", 1L);
        param.put("items", Arrays.asList(item1, item2));
        param.put("payMethod", "CARD");
        param.put("zipcode", "12345");
        param.put("address", "Test Address");

        Object result = orderFacade.placeOrder(param);
        Map<String, Object> order = (Map<String, Object>) result;
        long orderId = ((Number) order.get("orderId")).longValue();

        // THEN: each option has independent inventory_log entry
        Connection conn = dataSource.getConnection();
        Statement st = conn.createStatement();

        // Check option 1: change_qty=-2
        ResultSet rs1 = st.executeQuery(
            "SELECT CHANGE_QTY, REASON FROM inventory_log WHERE OPTION_ID = 1 AND ORDER_ID = " + orderId
        );
        assertThat(rs1.next()).isTrue();
        assertThat(rs1.getInt("CHANGE_QTY")).isEqualTo(-2);
        assertThat(rs1.getString("REASON")).isEqualTo("ORDER");
        rs1.close();

        // Check option 2: change_qty=-3
        ResultSet rs2 = st.executeQuery(
            "SELECT CHANGE_QTY, REASON FROM inventory_log WHERE OPTION_ID = 2 AND ORDER_ID = " + orderId
        );
        assertThat(rs2.next()).isTrue();
        assertThat(rs2.getInt("CHANGE_QTY")).isEqualTo(-3);
        assertThat(rs2.getString("REASON")).isEqualTo("ORDER");
        rs2.close();

        st.close();
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
