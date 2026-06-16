package com.shopmall.dao.impl;

import com.shopmall.dao.PromotionDao;
import com.ibatis.sqlmap.client.SqlMapClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;

@Repository
public class PromotionDaoImpl extends SqlMapClientDaoSupport implements PromotionDao {
    @Autowired public void init(SqlMapClient sqlMapClient) { super.setSqlMapClient(sqlMapClient); }
    public long insertPromotion(Map<String,Object> p) {
        getSqlMapClientTemplate().insert("Promotion.insertPromotion", p);
        return ((Number) p.get("promotionId")).longValue();
    }
    @SuppressWarnings("unchecked")
    public List<Map<String,Object>> findActive(Map<String,Object> p) {
        return getSqlMapClientTemplate().queryForList("Promotion.findActive", p);
    }
    @SuppressWarnings("unchecked")
    public Map<String,Object> findById(long promotionId) {
        return (Map<String,Object>) getSqlMapClientTemplate().queryForObject("Promotion.findById", promotionId);
    }
    @SuppressWarnings("unchecked")
    public List<Map<String,Object>> findCampaignProducts(long promotionId) {
        return getSqlMapClientTemplate().queryForList("Promotion.findCampaignProducts", promotionId);
    }
    public int insertCampaignProduct(Map<String,Object> p) {
        getSqlMapClientTemplate().insert("Promotion.insertCampaignProduct", p);
        return 1;
    }
    @SuppressWarnings("unchecked")
    public List<Map<String,Object>> findStackableForProduct(Map<String,Object> p) {
        return getSqlMapClientTemplate().queryForList("Promotion.findStackableForProduct", p);
    }
    @SuppressWarnings("unchecked")
    public Map<String,Object> findBestPromotionForProduct(Map<String,Object> p) {
        return (Map<String,Object>) getSqlMapClientTemplate().queryForObject("Promotion.findBestPromotionForProduct", p);
    }
    public int sumStackedDiscountForOption(Map<String,Object> p) {
        return (Integer) getSqlMapClientTemplate().queryForObject("Promotion.sumStackedDiscountForOption", p);
    }
    @SuppressWarnings("unchecked")
    public List<Map<String,Object>> findExpiringSoon(Map<String,Object> p) {
        return getSqlMapClientTemplate().queryForList("Promotion.findExpiringSoon", p);
    }
}
