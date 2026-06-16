package com.shopmall.dao;

import java.util.List;
import java.util.Map;

public interface WishlistDao {
    long insertWish(Map<String,Object> p);
    Map<String,Object> findOne(Map<String,Object> p);
    int deleteWish(long wishlistId);
    List<Map<String,Object>> findByMember(long memberId);
    int countByMember(long memberId);

    List<Map<String,Object>> findSignalsByMember(long memberId);
    int countBackInStock(long memberId);
    List<Map<String,Object>> findWishlistedSoldOut(long memberId);
}
