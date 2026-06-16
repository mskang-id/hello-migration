package com.shopmall.web;

import com.shopmall.common.envelope.ApiResponse;
import com.shopmall.common.error.ErrorCode;
import com.shopmall.service.CartService;
import com.shopmall.web.dto.cart.CartLineRequestXml;
import com.shopmall.web.dto.cart.CartLineXml;
import com.shopmall.web.dto.cart.CartPromoLineXml;
import com.shopmall.web.dto.cart.CartPromoViewXml;
import com.shopmall.web.dto.cart.CartViewXml;
import com.shopmall.web.dto.cart.CartXml;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/cart")
public class CartController {

    @Autowired private CartService cartService;

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/{memberId}", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse getCart(@PathVariable("memberId") long memberId) {
        Map<String, Object> view = cartService.getCart(memberId);
        CartViewXml out = new CartViewXml();
        out.setCartId(view.get("cartId") == null ? null : ((Number) view.get("cartId")).longValue());
        long cartTotal = 0L;
        List<Map<String, Object>> rows = (List<Map<String, Object>>) view.get("items");
        if (rows != null) {
            for (Map<String, Object> r : rows) {
                CartLineXml line = new CartLineXml();
                line.setCartItemId(((Number) r.get("CART_ITEM_ID")).longValue());
                line.setCartId(((Number) r.get("CART_ID")).longValue());
                line.setOptionId(((Number) r.get("OPTION_ID")).longValue());
                line.setQty(((Number) r.get("QTY")).intValue());
                line.setOptionName(r.get("OPTION_NAME") == null ? null : String.valueOf(r.get("OPTION_NAME")));
                line.setUnitPrice(((Number) r.get("UNIT_PRICE")).longValue());
                line.setLineTotal(((Number) r.get("LINE_TOTAL")).longValue());
                line.setProductName(r.get("PRODUCT_NAME") == null ? null : String.valueOf(r.get("PRODUCT_NAME")));
                cartTotal += ((Number) r.get("LINE_TOTAL")).longValue();   // controller-tier arithmetic
                out.getItems().add(line);
            }
        }
        out.setCartTotal(cartTotal);
        return ResponseFactory.ok(out);
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/{memberId}/promo", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse getCartWithPromo(@PathVariable("memberId") long memberId) {
        Map<String, Object> view = cartService.getCartWithPromo(memberId);
        CartPromoViewXml out = new CartPromoViewXml();
        out.setCartId(view.get("cartId") == null ? null : ((Number) view.get("cartId")).longValue());
        long cartTotal = 0L;
        List<Map<String, Object>> rows = (List<Map<String, Object>>) view.get("items");
        if (rows != null) {
            for (Map<String, Object> r : rows) {
                CartPromoLineXml line = new CartPromoLineXml();
                line.setCartItemId(((Number) r.get("CART_ITEM_ID")).longValue());
                line.setCartId(((Number) r.get("CART_ID")).longValue());
                line.setOptionId(((Number) r.get("OPTION_ID")).longValue());
                line.setQty(((Number) r.get("QTY")).intValue());
                line.setOptionName(r.get("OPTION_NAME") == null ? null : String.valueOf(r.get("OPTION_NAME")));
                line.setUnitPrice(((Number) r.get("UNIT_PRICE")).longValue());
                line.setLineTotal(((Number) r.get("LINE_TOTAL")).longValue());
                line.setProductName(r.get("PRODUCT_NAME") == null ? null : String.valueOf(r.get("PRODUCT_NAME")));
                line.setPromoDiscount(((Number) r.get("PROMO_DISCOUNT")).longValue());   // per-line promo
                cartTotal += ((Number) r.get("LINE_TOTAL")).longValue();   // controller-tier arithmetic
                out.getItems().add(line);
            }
        }
        out.setCartTotal(cartTotal);
        out.setDiscountedTotal((long) ((Number) view.get("discountedTotal")).intValue());   // SUM(line - promo*qty)
        out.setItemsOnPromo(((Number) view.get("itemsOnPromo")).intValue());
        return ResponseFactory.ok(out);
    }

    @RequestMapping(value = "/{memberId}/items", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponse addItem(@PathVariable("memberId") long memberId, @RequestBody CartLineRequestXml req) {
        long optionId = req.getOptionId() == null ? 0L : req.getOptionId().longValue();
        int qty = req.getQty() == null ? 0 : req.getQty().intValue();
        long cartItemId = cartService.addItem(memberId, optionId, qty);
        CartLineXml out = new CartLineXml();
        out.setCartItemId(cartItemId);
        out.setOptionId(optionId);
        out.setQty(qty);
        return ResponseFactory.ok(out);
    }

    @RequestMapping(value = "/items/{cartItemId}", method = RequestMethod.PUT)
    @ResponseBody
    public ApiResponse updateItem(@PathVariable("cartItemId") long cartItemId, @RequestBody CartLineRequestXml req) {
        int qty = req.getQty() == null ? 0 : req.getQty().intValue();
        cartService.updateItem(cartItemId, qty);
        CartLineXml out = new CartLineXml();
        out.setCartItemId(cartItemId);
        out.setQty(qty);
        return ResponseFactory.ok(out);
    }

    @RequestMapping(value = "/items/{cartItemId}", method = RequestMethod.DELETE)
    @ResponseBody
    public ApiResponse removeItem(@PathVariable("cartItemId") long cartItemId) {
        cartService.removeItem(cartItemId);
        CartLineXml out = new CartLineXml();
        out.setCartItemId(cartItemId);
        return ResponseFactory.ok(out);
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/{memberId}/checkout", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponse checkout(@PathVariable("memberId") long memberId) {
        Object result = cartService.checkout(memberId);
        if (result instanceof Long && ((Long) result).longValue() == -1L) {
            CartXml fail = new CartXml();
            fail.setOrderId(-1L);
            return ResponseFactory.fail(ErrorCode.OUT_OF_STOCK, "out of stock or empty cart", fail);
        }
        Map<String, Object> order = (Map<String, Object>) result;
        CartXml out = new CartXml();
        out.setOrderId(order.get("orderId") == null ? null : ((Number) order.get("orderId")).longValue());
        out.setMemberId(((Number) order.get("memberId")).longValue());
        out.setOrderDate(String.valueOf(order.get("orderDate")));
        out.setStatus(((Number) order.get("status")).intValue());
        out.setTotalPrice(((Number) order.get("totalPrice")).intValue());
        out.setPayMethod(order.get("payMethod") == null ? null : String.valueOf(order.get("payMethod")));
        return ResponseFactory.ok(out);
    }
}
