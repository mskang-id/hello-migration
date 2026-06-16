package com.shopmall.service.impl;

import com.shopmall.dao.CartDao;
import com.shopmall.facade.OrderFacade;          // checkout goes through the order facade
import com.shopmall.service.CartService;
import com.shopmall.common.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class CartServiceImpl implements CartService {
    @Autowired private CartDao cartDao;
    @Autowired private OrderFacade orderFacade;   // same entrypoint OrderController uses
    @Autowired private com.shopmall.biz.CartBiz cartBiz;

    @SuppressWarnings("unchecked")
    public Map<String,Object> getCart(long memberId) {
        Map<String,Object> open = cartDao.findOpenCartByMember(memberId);
        Map<String,Object> view = new HashMap<String,Object>();
        if (open == null) {
            view.put("cartId", null);
            view.put("items", new ArrayList<Map<String,Object>>());
            view.put("cartTotal", 0L);
            return view;
        }
        long cartId = ((Number) open.get("CART_ID")).longValue();
        List<Map<String,Object>> rows = cartDao.findItemsByCart(cartId);
        view.put("cartId", cartId);
        view.put("items", rows);                       // raw UPPERCASE rows still leak upward
        view.put("cartTotal", cartBiz.sumCartTotal(rows)); // sum the line items for the cart total
        return view;
    }
    @SuppressWarnings("unchecked")
    public Map<String,Object> getCartWithPromo(long memberId) {
        Map<String,Object> open = cartDao.findOpenCartByMember(memberId);
        Map<String,Object> view = new HashMap<String,Object>();
        if (open == null) {
            view.put("cartId", null);
            view.put("items", new ArrayList<Map<String,Object>>());
            view.put("discountedTotal", 0);
            view.put("itemsOnPromo", 0);
            return view;
        }
        long cartId = ((Number) open.get("CART_ID")).longValue();
        Map<String,Object> q = new HashMap<String,Object>();
        q.put("cartId", cartId);
        q.put("today", DateUtil.today());            // VARCHAR(8) date binding for the promo-window subquery
        List<Map<String,Object>> rows = cartDao.findItemsWithPromo(q);   // PROMO_DISCOUNT per line
        view.put("cartId", cartId);
        view.put("items", rows);                                          // raw UPPERCASE rows still leak upward
        view.put("discountedTotal", cartDao.cartDiscountedTotal(q));      // SUM(line - promo*qty)
        view.put("itemsOnPromo", cartDao.countItemsOnPromo(q));           // DISTINCT items with an active promo
        return view;
    }

    public long addItem(long memberId, long optionId, int qty) {
        Map<String,Object> open = cartDao.findOpenCartByMember(memberId);
        long cartId;
        if (open == null) {
            Map<String,Object> c = new HashMap<String,Object>();
            c.put("memberId", memberId); c.put("regDate", DateUtil.today());
            cartId = cartDao.insertCart(c);
        } else { cartId = ((Number) open.get("CART_ID")).longValue(); }
        Map<String,Object> it = new HashMap<String,Object>();
        it.put("cartId", cartId); it.put("optionId", optionId); it.put("qty", qty);
        return cartDao.insertItem(it);
    }
    public int updateItem(long cartItemId, int qty) {
        Map<String,Object> p = new HashMap<String,Object>(); p.put("cartItemId", cartItemId); p.put("qty", qty);
        return cartDao.updateItemQty(p);
    }
    public int removeItem(long cartItemId) { return cartDao.deleteItem(cartItemId); }

    @SuppressWarnings("unchecked")
    public Object checkout(long memberId) {
        try {
            Map<String,Object> open = cartDao.findOpenCartByMember(memberId);
            if (open == null) return new Long(-1);
            long cartId = ((Number) open.get("CART_ID")).longValue();
            List<Map<String,Object>> rows = cartDao.findItemsByCart(cartId);
            if (rows == null || rows.isEmpty()) return new Long(-1);
            List<Map<String,Object>> items = new ArrayList<Map<String,Object>>();
            for (Map<String,Object> r : rows) {
                Map<String,Object> line = new HashMap<String,Object>();
                line.put("optionId", ((Number) r.get("OPTION_ID")).longValue());   // UPPERCASE key
                line.put("qty", ((Number) r.get("QTY")).intValue());
                items.add(line);
            }
            long promoDisc = cartBiz.stackedDiscountForCart(rows);
            Map<String,Object> param = new HashMap<String,Object>();
            param.put("memberId", memberId);
            param.put("items", items);
            param.put("payMethod", "CARD");
            param.put("promoDiscount", promoDisc);   // NEW key; only the cart path sets it
            Object result = orderFacade.placeOrder(param);     // hand the cart to the order path
            if (result instanceof Long && ((Long) result).longValue() == -1L) return new Long(-1);
            cartDao.markOrdered(cartId);
            return result;                                      // the order Map
        } catch (Exception e) {
            // any failure -> -1 so the cart stays open for retry
            return new Long(-1);
        }
    }
}
