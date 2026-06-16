package com.shopmall.dao;

import java.util.List;
import java.util.Map;

public interface PromotionDao {
    long insertPromotion(Map<String,Object> p);
    List<Map<String,Object>> findActive(Map<String,Object> p);
    Map<String,Object> findById(long promotionId);
    List<Map<String,Object>> findCampaignProducts(long promotionId);
    int insertCampaignProduct(Map<String,Object> p);

    List<Map<String,Object>> findStackableForProduct(Map<String,Object> p);
    Map<String,Object> findBestPromotionForProduct(Map<String,Object> p);
    int sumStackedDiscountForOption(Map<String,Object> p);
    List<Map<String,Object>> findExpiringSoon(Map<String,Object> p);
}
