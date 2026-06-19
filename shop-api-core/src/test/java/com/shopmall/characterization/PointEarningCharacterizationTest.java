package com.shopmall.characterization;

import com.shopmall.facade.OrderFacade;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Characterization test for point earning behavior.
 * Risk 3: Grade-tiered earn rate, POINT method flat rate, welcome bonus on first order.
 */
public class PointEarningCharacterizationTest extends BaseIntegrationTest {

    @Autowired
    private OrderFacade orderFacade;

    @Test
    public void pointPayment_earnsFlatOnePercent() throws Exception {
        // GIVEN: member 1 (alice, grade A, point 10000)
        int pointBefore = queryInt("SELECT POINT FROM member WHERE MEMBER_ID = 1");

        // WHEN: place order with POINT method (payAmount = 15000 * 2 = 30000)
        placeTestOrder(1L, 1L, 2, "POINT");

        // THEN: earns flat 1% regardless of grade (OrderBiz.earnPoint path)
        int pointAfter = queryInt("SELECT POINT FROM member WHERE MEMBER_ID = 1");
        int earned = 30000 * 1 / 100; // 300
        // Note: POINT method also deducts payment from points, but we're testing the earn side
        // The net change is: -30000 (payment) + 300 (earn) = -29700
        assertThat(pointAfter).isEqualTo(pointBefore - 30000 + earned);
    }

    @Test
    public void cardPayment_gradeA_earnsThreePercent() throws Exception {
        // GIVEN: member 1 (alice, grade A, point 10000), has existing orders (no welcome bonus)
        int pointBefore = queryInt("SELECT POINT FROM member WHERE MEMBER_ID = 1");

        // WHEN: place order with CARD (payAmount = 15000)
        placeTestOrder(1L, 1L, 1, "CARD");

        // THEN: earns 3% (grade A)
        int pointAfter = queryInt("SELECT POINT FROM member WHERE MEMBER_ID = 1");
        int earned = 15000 * 3 / 100; // 450
        assertThat(pointAfter).isEqualTo(pointBefore + earned);
    }

    @Test
    public void cardPayment_gradeB_earnsTwoPercent() throws Exception {
        // GIVEN: member 2 (bob, grade B)
        int pointBefore = queryInt("SELECT POINT FROM member WHERE MEMBER_ID = 2");

        // WHEN: place order with CARD (payAmount = 15000)
        placeTestOrder(2L, 1L, 1, "CARD");

        // THEN: earns 2% (grade B)
        int pointAfter = queryInt("SELECT POINT FROM member WHERE MEMBER_ID = 2");
        int earned = 15000 * 2 / 100; // 300
        assertThat(pointAfter).isEqualTo(pointBefore + earned);
    }

    @Test
    public void cardPayment_gradeC_earnsOnePercent() throws Exception {
        // GIVEN: member 3 (carol, grade C)
        int pointBefore = queryInt("SELECT POINT FROM member WHERE MEMBER_ID = 3");

        // WHEN: place order with CARD (payAmount = 15000)
        placeTestOrder(3L, 1L, 1, "CARD");

        // THEN: earns 1% (grade C)
        int pointAfter = queryInt("SELECT POINT FROM member WHERE MEMBER_ID = 3");
        int earned = 15000 * 1 / 100; // 150
        assertThat(pointAfter).isEqualTo(pointBefore + earned);
    }

    @Test
    public void firstOrder_getsWelcomeBonus500() throws Exception {
        // GIVEN: member 49 (newbie, zero order history)
        int pointBefore = queryInt("SELECT POINT FROM member WHERE MEMBER_ID = 49");
        String grade = queryString("SELECT GRADE FROM member WHERE MEMBER_ID = 49");

        // WHEN: place first order with CARD (payAmount = 15000)
        placeTestOrder(49L, 1L, 1, "CARD");

        // THEN: earns grade rate + 500 welcome bonus
        int pointAfter = queryInt("SELECT POINT FROM member WHERE MEMBER_ID = 49");
        int earnRate = "A".equals(grade) ? 3 : ("B".equals(grade) ? 2 : 1);
        int earned = (15000 * earnRate / 100) + 500;
        assertThat(pointAfter).isEqualTo(pointBefore + earned);
    }

    @Test
    public void secondOrder_noWelcomeBonus() throws Exception {
        // GIVEN: member 49 places first order (gets welcome bonus)
        placeTestOrder(49L, 1L, 1, "CARD");
        int pointAfterFirst = queryInt("SELECT POINT FROM member WHERE MEMBER_ID = 49");
        String grade = queryString("SELECT GRADE FROM member WHERE MEMBER_ID = 49");

        // WHEN: place second order (payAmount = 15000)
        placeTestOrder(49L, 2L, 1, "CARD");

        // THEN: earns only grade rate, no welcome bonus
        int pointAfterSecond = queryInt("SELECT POINT FROM member WHERE MEMBER_ID = 49");
        int earnRate = "A".equals(grade) ? 3 : ("B".equals(grade) ? 2 : 1);
        int earned = 15000 * earnRate / 100;
        assertThat(pointAfterSecond).isEqualTo(pointAfterFirst + earned);
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
