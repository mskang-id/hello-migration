package com.shopmall.service.impl;

import com.shopmall.dao.WishlistDao;
import com.shopmall.service.WishlistService;
import com.shopmall.common.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WishlistServiceImpl implements WishlistService {

    @Autowired private WishlistDao wishlistDao;

    public long add(long memberId, long productId) {
        try {
            // dedup guard in Java (no DB UNIQUE): skip insert when the pair exists
            Map<String,Object> probe = new HashMap<String,Object>();
            probe.put("memberId", memberId);
            probe.put("productId", productId);
            Map<String,Object> existing = wishlistDao.findOne(probe);
            if (existing != null) {
                return ((Number) existing.get("WISHLIST_ID")).longValue();
            }
            Map<String,Object> ins = new HashMap<String,Object>();
            ins.put("memberId", memberId);
            ins.put("productId", productId);
            ins.put("regDate", DateUtil.today());
            return wishlistDao.insertWish(ins);
        } catch (Exception e) {
            // swallowed: add failure surfaces only as magic -1
            return -1L;
        }
    }

    public int remove(long wishlistId) { return wishlistDao.deleteWish(wishlistId); }

    public List<Map<String,Object>> listByMember(long memberId) { return wishlistDao.findByMember(memberId); }

    public int countByMember(long memberId) { return wishlistDao.countByMember(memberId); }

    public List<Map<String,Object>> signals(long memberId) { return wishlistDao.findSignalsByMember(memberId); }

    public int countBackInStock(long memberId) { return wishlistDao.countBackInStock(memberId); }

    public List<Map<String,Object>> wishlistedSoldOut(long memberId) { return wishlistDao.findWishlistedSoldOut(memberId); }
}
