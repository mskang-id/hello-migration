package com.shopmall.dao.impl;

import com.shopmall.dao.CartDao;
import com.ibatis.sqlmap.client.SqlMapClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;

@Repository
public class CartDaoImpl extends SqlMapClientDaoSupport implements CartDao {
    @Autowired public void init(SqlMapClient sqlMapClient) { super.setSqlMapClient(sqlMapClient); }
    public long insertCart(Map<String,Object> p) {
        getSqlMapClientTemplate().insert("Cart.insertCart", p);
        return ((Number) p.get("cartId")).longValue();   // selectKey populated cartId
    }
    @SuppressWarnings("unchecked")
    public Map<String,Object> findOpenCartByMember(long memberId) {
        return (Map<String,Object>) getSqlMapClientTemplate().queryForObject("Cart.findOpenCartByMember", memberId);
    }
    public long insertItem(Map<String,Object> p) {
        getSqlMapClientTemplate().insert("Cart.insertItem", p);
        return ((Number) p.get("cartItemId")).longValue();
    }
    public int updateItemQty(Map<String,Object> p) { return getSqlMapClientTemplate().update("Cart.updateItemQty", p); }
    public int deleteItem(long cartItemId) { return getSqlMapClientTemplate().delete("Cart.deleteItem", cartItemId); }
    @SuppressWarnings("unchecked")
    public List<Map<String,Object>> findItemsByCart(long cartId) { return getSqlMapClientTemplate().queryForList("Cart.findItemsByCart", cartId); }
    public int markOrdered(long cartId) { return getSqlMapClientTemplate().update("Cart.markOrdered", cartId); }
    public int countOpenCarts() { return (Integer) getSqlMapClientTemplate().queryForObject("Cart.countOpenCarts"); }
    @SuppressWarnings("unchecked")
    public List<Map<String,Object>> findItemsWithPromo(Map<String,Object> p) {
        return getSqlMapClientTemplate().queryForList("Cart.findItemsWithPromo", p);
    }
    public int cartDiscountedTotal(Map<String,Object> p) {
        return (Integer) getSqlMapClientTemplate().queryForObject("Cart.cartDiscountedTotal", p);
    }
    public int countItemsOnPromo(Map<String,Object> p) {
        return (Integer) getSqlMapClientTemplate().queryForObject("Cart.countItemsOnPromo", p);
    }
}
