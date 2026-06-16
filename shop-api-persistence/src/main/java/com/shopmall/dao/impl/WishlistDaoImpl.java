package com.shopmall.dao.impl;

import com.shopmall.dao.WishlistDao;
import com.ibatis.sqlmap.client.SqlMapClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;

@Repository
public class WishlistDaoImpl extends SqlMapClientDaoSupport implements WishlistDao {
    @Autowired public void init(SqlMapClient sqlMapClient) { super.setSqlMapClient(sqlMapClient); }
    public long insertWish(Map<String,Object> p) {
        getSqlMapClientTemplate().insert("Wishlist.insertWish", p);
        return ((Number) p.get("wishlistId")).longValue();
    }
    @SuppressWarnings("unchecked")
    public Map<String,Object> findOne(Map<String,Object> p) {
        return (Map<String,Object>) getSqlMapClientTemplate().queryForObject("Wishlist.findOne", p);
    }
    public int deleteWish(long wishlistId) { return getSqlMapClientTemplate().delete("Wishlist.deleteWish", wishlistId); }
    @SuppressWarnings("unchecked")
    public List<Map<String,Object>> findByMember(long memberId) {
        return getSqlMapClientTemplate().queryForList("Wishlist.findByMember", memberId);
    }
    public int countByMember(long memberId) {
        return (Integer) getSqlMapClientTemplate().queryForObject("Wishlist.countByMember", memberId);
    }
    @SuppressWarnings("unchecked")
    public List<Map<String,Object>> findSignalsByMember(long memberId) {
        return getSqlMapClientTemplate().queryForList("Wishlist.findSignalsByMember", memberId);
    }
    public int countBackInStock(long memberId) {
        return (Integer) getSqlMapClientTemplate().queryForObject("Wishlist.countBackInStock", memberId);
    }
    @SuppressWarnings("unchecked")
    public List<Map<String,Object>> findWishlistedSoldOut(long memberId) {
        return getSqlMapClientTemplate().queryForList("Wishlist.findWishlistedSoldOut", memberId);
    }
}
