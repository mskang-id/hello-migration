package com.shopmall.service;

import java.util.List;
import java.util.Map;

public interface PromotionService {
    List<Map<String,Object>> listActive();
    List<Map<String,Object>> applyPreview(long promotionId);
    List<Map<String,Object>> stackableForProduct(long productId);
    Map<String,Object> bestPromotionForProduct(long productId);
    List<Map<String,Object>> expiringSoon(int withinDays);
}
