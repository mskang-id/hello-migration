package com.shopmall.service;

import java.util.Map;

public interface CartService {
    Map<String,Object> getCart(long memberId);
    Map<String,Object> getCartWithPromo(long memberId);
    long addItem(long memberId, long optionId, int qty);
    int updateItem(long cartItemId, int qty);
    int removeItem(long cartItemId);
    Object checkout(long memberId);
}
