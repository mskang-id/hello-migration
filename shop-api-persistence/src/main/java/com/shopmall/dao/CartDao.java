package com.shopmall.dao;

import java.util.List;
import java.util.Map;

public interface CartDao {
    long insertCart(Map<String,Object> p);
    Map<String,Object> findOpenCartByMember(long memberId);
    long insertItem(Map<String,Object> p);
    int updateItemQty(Map<String,Object> p);
    int deleteItem(long cartItemId);
    List<Map<String,Object>> findItemsByCart(long cartId);
    int markOrdered(long cartId);
    int countOpenCarts();

    List<Map<String,Object>> findItemsWithPromo(Map<String,Object> p);
    int cartDiscountedTotal(Map<String,Object> p);
    int countItemsOnPromo(Map<String,Object> p);
}
