package com.shopmall.service;

import java.util.List;
import java.util.Map;

public interface WishlistService {
    long add(long memberId, long productId);
    int remove(long wishlistId);
    List<Map<String,Object>> listByMember(long memberId);
    int countByMember(long memberId);
    List<Map<String,Object>> signals(long memberId);
    int countBackInStock(long memberId);
    List<Map<String,Object>> wishlistedSoldOut(long memberId);
}
