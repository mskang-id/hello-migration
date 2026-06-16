package com.shopmall.service.impl;

import com.shopmall.biz.PromotionBiz;
import com.shopmall.dao.PromotionDao;
import com.shopmall.service.PromotionService;
import com.shopmall.common.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PromotionServiceImpl implements PromotionService {

    @Autowired private PromotionDao promotionDao;
    @Autowired private PromotionBiz promotionBiz;

    public List<Map<String,Object>> listActive() {
        Map<String,Object> p = new HashMap<String,Object>();
        p.put("status", "ACTIVE");
        p.put("today", DateUtil.today());
        return promotionDao.findActive(p);
    }

    public List<Map<String,Object>> applyPreview(long promotionId) {
        return promotionBiz.applyPreview(promotionId);
    }

    public List<Map<String,Object>> stackableForProduct(long productId) {
        Map<String,Object> p = new HashMap<String,Object>();
        p.put("productId", productId);
        p.put("today", DateUtil.today());
        return promotionDao.findStackableForProduct(p);   // IN_WINDOW/OUT_OF_WINDOW eligibility
    }

    public Map<String,Object> bestPromotionForProduct(long productId) {
        Map<String,Object> p = new HashMap<String,Object>();
        p.put("productId", productId);
        p.put("today", DateUtil.today());
        return promotionDao.findBestPromotionForProduct(p);
    }

    public List<Map<String,Object>> expiringSoon(int withinDays) {
        Map<String,Object> p = new HashMap<String,Object>();
        p.put("status", "ACTIVE");
        p.put("today", DateUtil.today());
        // VARCHAR(8) yyyyMMdd date math in Java (no DB DATE type): today + withinDays days
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.add(java.util.Calendar.DATE, withinDays);
        p.put("soon", new java.text.SimpleDateFormat("yyyyMMdd").format(cal.getTime()));
        return promotionDao.findExpiringSoon(p);   // EXPIRING/EXPIRED lifecycle CASE
    }
}
