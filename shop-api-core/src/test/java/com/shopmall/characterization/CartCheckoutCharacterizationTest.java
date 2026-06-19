package com.shopmall.characterization;

import com.shopmall.service.CartService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class CartCheckoutCharacterizationTest extends BaseIntegrationTest {

    @Autowired
    private CartService cartService;

    @Test
    public void checkout_success_marksCartOrdered() throws Exception {
        long memberId = queryInt("SELECT member_id FROM member WHERE status = 'ACTIVE' LIMIT 1");
        long optionId = 1L;

        cartService.addItem(memberId, optionId, 2);
        Object checkoutResult = cartService.checkout(memberId);

        assertThat(checkoutResult).isInstanceOf(Map.class);
        String cartStatus = queryString(
            "SELECT status FROM cart WHERE member_id = " + memberId + " ORDER BY cart_id DESC LIMIT 1"
        );
        assertThat(cartStatus).isEqualTo("ORDERED");
    }

    @Test
    public void checkout_noOpenCart_returnsMinusOne() throws Exception {
        long memberIdNoCart = queryInt(
            "SELECT member_id FROM member WHERE member_id NOT IN (SELECT member_id FROM cart WHERE status = 'OPEN') LIMIT 1"
        );

        Object result = cartService.checkout(memberIdNoCart);

        assertThat(result).isEqualTo(-1L);
    }

    @Test
    public void checkout_emptyCart_returnsMinusOne() throws Exception {
        long memberId = queryInt("SELECT member_id FROM member WHERE status = 'ACTIVE' LIMIT 1");
        long optionId = 1L;

        long cartItemId = cartService.addItem(memberId, optionId, 1);
        cartService.removeItem(cartItemId);
        Object result = cartService.checkout(memberId);

        assertThat(result).isEqualTo(-1L);
    }

    @Test
    public void checkout_orderFailure_cartStaysOpen() throws Exception {
        long memberId = queryInt("SELECT member_id FROM member WHERE status = 'ACTIVE' LIMIT 1");
        long optionId = 1L;
        int excessiveQty = 99999;

        cartService.addItem(memberId, optionId, excessiveQty);
        Object result = cartService.checkout(memberId);

        assertThat(result).isEqualTo(-1L);
        String cartStatus = queryString(
            "SELECT status FROM cart WHERE member_id = " + memberId + " ORDER BY cart_id DESC LIMIT 1"
        );
        assertThat(cartStatus).isEqualTo("OPEN");
    }
}
