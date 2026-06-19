package com.shopmall.characterization;

import com.shopmall.facade.OrderFacade;
import com.shopmall.service.OrderService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.*;
import static org.assertj.core.api.Assertions.assertThat;

public class PlaceOrderCharacterizationTest extends BaseIntegrationTest {

    @Autowired private OrderFacade orderFacade;
    @Autowired private OrderService orderService;

    // data.sql: member id=1 (alice), grade=A, point=10000
    // data.sql: product_option id=1, stock_qty=100, product price=15000, extra_price=0

    @Test
    public void placeOrder_normalCard_gradeA_createsOrderAndDeductsStock() throws Exception {
        int stockBefore = queryInt("SELECT stock_qty FROM product_option WHERE option_id = 1");
        int pointBefore = queryInt("SELECT point FROM member WHERE member_id = 1");

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("memberId", 1L);
        param.put("payMethod", "CARD");
        param.put("zipcode", "12345");
        param.put("address", "test address");
        param.put("usePoint", 0);
        List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
        Map<String, Object> item = new HashMap<String, Object>();
        item.put("optionId", 1L);
        item.put("qty", 2);
        items.add(item);
        param.put("items", items);

        Object result = orderFacade.placeOrder(param);

        assertThat(result).isInstanceOf(Map.class);
        Map<String, Object> order = (Map<String, Object>) result;
        assertThat(order.get("orderId")).isNotNull();

        long orderId = ((Number) order.get("orderId")).longValue();

        // stock reduced
        int stockAfter = queryInt("SELECT stock_qty FROM product_option WHERE option_id = 1");
        assertThat(stockAfter).isEqualTo(stockBefore - 2);

        // inventory log created
        int logCount = queryInt("SELECT COUNT(*) FROM inventory_log WHERE option_id = 1 AND reason = 'ORDER'");
        assertThat(logCount).isGreaterThan(0);

        // order_item created
        int itemCount = queryInt("SELECT COUNT(*) FROM order_item WHERE order_id = " + orderId);
        assertThat(itemCount).isEqualTo(1);

        // settlement created
        int settleCount = queryInt("SELECT COUNT(*) FROM order_settlement WHERE order_id = " + orderId);
        assertThat(settleCount).isEqualTo(1);

        // point earned (grade A = 3% of payAmount)
        int pointAfter = queryInt("SELECT point FROM member WHERE member_id = 1");
        int payAmount = ((Number) order.get("totalPrice")).intValue();
        int expectedEarn = payAmount * 3 / 100;
        assertThat(pointAfter).isEqualTo(pointBefore + expectedEarn);
    }

    @Test
    public void placeOrder_pointPayment_earnsFlatOnePercent() throws Exception {
        int pointBefore = queryInt("SELECT point FROM member WHERE member_id = 1");

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("memberId", 1L);
        param.put("payMethod", "POINT");
        param.put("zipcode", "12345");
        param.put("address", "test");
        param.put("usePoint", 0);
        List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
        Map<String, Object> item = new HashMap<String, Object>();
        item.put("optionId", 1L);
        item.put("qty", 1);
        items.add(item);
        param.put("items", items);

        Object result = orderService.placeOrder(param);

        assertThat(result).isInstanceOf(Map.class);
        Map<String, Object> order = (Map<String, Object>) result;
        int payAmount = ((Number) order.get("totalPrice")).intValue();
        int expectedEarn = payAmount * 1 / 100;

        int pointAfter = queryInt("SELECT point FROM member WHERE member_id = 1");
        assertThat(pointAfter).isEqualTo(pointBefore + expectedEarn);
    }

    @Test
    public void placeOrder_withCoupon_appliesDiscount() throws Exception {
        // data.sql: coupon id=1, code=WELCOME10, discount_type=R, discount_val=10, min_order=10000
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("memberId", 1L);
        param.put("payMethod", "CARD");
        param.put("zipcode", "12345");
        param.put("address", "test");
        param.put("usePoint", 0);
        param.put("couponId", 1L);
        List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
        Map<String, Object> item = new HashMap<String, Object>();
        item.put("optionId", 1L);
        item.put("qty", 2);
        items.add(item);
        param.put("items", items);

        Object result = orderService.placeOrder(param);

        assertThat(result).isInstanceOf(Map.class);
        // coupon marked as used
        String usedYn = queryString("SELECT used_yn FROM coupon WHERE coupon_id = 1");
        assertThat(usedYn).isEqualTo("Y");

        // settlement has discount
        Map<String, Object> order = (Map<String, Object>) result;
        long orderId = ((Number) order.get("orderId")).longValue();
        int discount = queryInt("SELECT discount FROM order_settlement WHERE order_id = " + orderId);
        assertThat(discount).isGreaterThan(0);
    }

    @Test
    public void placeOrder_firstOrder_getsWelcomeBonus() throws Exception {
        // member id=49 has zero order history (data-scale.sql "newbie")
        int pointBefore = queryInt("SELECT point FROM member WHERE member_id = 49");

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("memberId", 49L);
        param.put("payMethod", "CARD");
        param.put("zipcode", "12345");
        param.put("address", "test");
        param.put("usePoint", 0);
        List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
        Map<String, Object> item = new HashMap<String, Object>();
        item.put("optionId", 1L);
        item.put("qty", 1);
        items.add(item);
        param.put("items", items);

        Object result = orderService.placeOrder(param);

        assertThat(result).isInstanceOf(Map.class);
        Map<String, Object> order = (Map<String, Object>) result;
        int payAmount = ((Number) order.get("totalPrice")).intValue();
        // member 49 is grade C (default) -> earnRate = 1%
        int expectedEarn = payAmount * 1 / 100 + 500; // grade earn + welcome bonus

        int pointAfter = queryInt("SELECT point FROM member WHERE member_id = 49");
        assertThat(pointAfter).isEqualTo(pointBefore + expectedEarn);
    }

    @Test
    public void placeOrder_insufficientStock_returnsMinusOne() throws Exception {
        int stockBefore = queryInt("SELECT stock_qty FROM product_option WHERE option_id = 1");

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("memberId", 1L);
        param.put("payMethod", "CARD");
        param.put("zipcode", "12345");
        param.put("address", "test");
        param.put("usePoint", 0);
        List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
        Map<String, Object> item = new HashMap<String, Object>();
        item.put("optionId", 1L);
        item.put("qty", 99999);  // exceeds stock
        items.add(item);
        param.put("items", items);

        Object result = orderService.placeOrder(param);

        assertThat(result).isInstanceOf(Long.class);
        assertThat(((Long) result).longValue()).isEqualTo(-1L);

        // no side effects
        int stockAfter = queryInt("SELECT stock_qty FROM product_option WHERE option_id = 1");
        assertThat(stockAfter).isEqualTo(stockBefore);
    }

    @Test
    public void placeOrder_pgDecline_overThreshold_returnsMinusOne() throws Exception {
        // PG_DECLINE_THRESHOLD = 1,000,000. Need total >= 1M.
        // product_option id=1 price=15000. Need qty >= 67.
        // MockPaymentGateway declines when amount > 1,000,000 (strictly greater)
        int stockBefore = queryInt("SELECT stock_qty FROM product_option WHERE option_id = 1");

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("memberId", 1L);
        param.put("payMethod", "CARD");
        param.put("zipcode", "12345");
        param.put("address", "test");
        param.put("usePoint", 0);
        List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
        Map<String, Object> item = new HashMap<String, Object>();
        item.put("optionId", 1L);
        item.put("qty", 67); // 15000 * 67 = 1,005,000 > PG threshold
        items.add(item);
        param.put("items", items);

        Object result = orderService.placeOrder(param);

        assertThat(result).isInstanceOf(Long.class);
        assertThat(((Long) result).longValue()).isEqualTo(-1L);

        // stock unchanged (PG call happens BEFORE stock deduction at line 86 vs 93-98)
        int stockAfter = queryInt("SELECT stock_qty FROM product_option WHERE option_id = 1");
        assertThat(stockAfter).isEqualTo(stockBefore);
    }
}
