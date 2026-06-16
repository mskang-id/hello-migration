package com.shopmall.service.impl;

import com.shopmall.biz.ReviewBiz;
import com.shopmall.dao.ReviewDao;
import com.shopmall.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired private ReviewBiz reviewBiz;
    @Autowired private ReviewDao reviewDao;

    public long createReview(Map<String,Object> param) {
        try {
            Map<String,Object> built = reviewBiz.validateAndBuild(param);
            if (built == null) {
                return -1L;   // missing product/member -> -1
            }
            return reviewDao.insertReview(built);
        } catch (Exception e) {
            // any failure -> -1 to the caller
            return -1L;
        }
    }

    public Map<String,Object> listByProduct(long productId, int page, int size) {
        int offset = (page - 1) * size;   // Map-soup int math
        Map<String,Object> q = new HashMap<String,Object>();
        q.put("productId", productId);
        q.put("offset", offset);
        q.put("size", size);
        List<Map<String,Object>> rows = reviewDao.findByProduct(q);
        Map<String,Object> summary = reviewDao.ratingSummary(productId);
        Map<String,Object> out = new HashMap<String,Object>();
        out.put("rows", rows);
        out.put("summary", summary);
        out.put("total", reviewDao.countByProduct(productId));
        return out;
    }

    public Map<String,Object> verifiedByProduct(long productId, int page, int size) {
        int offset = (page - 1) * size;   // Map-soup int math
        Map<String,Object> q = new HashMap<String,Object>();
        q.put("productId", productId);
        q.put("offset", offset);
        q.put("size", size);
        List<Map<String,Object>> rows = reviewDao.findByProductVerified(q);   // PURCHASE_STATE EXISTS flag
        Map<String,Object> out = new HashMap<String,Object>();
        out.put("rows", rows);
        out.put("verifiedCount", reviewDao.countVerified(productId));
        out.put("total", reviewDao.countByProduct(productId));
        return out;
    }

    public Map<String,Object> weightedScore(long productId) {
        return reviewDao.weightedScore(productId);   // helpfulness-weighted score
    }

    public List<Map<String,Object>> ratingHistogram(long productId) {
        return reviewDao.ratingHistogram(productId);
    }

    public List<Map<String,Object>> topHelpful(long productId, int size) {
        Map<String,Object> q = new HashMap<String,Object>();
        q.put("productId", productId);
        q.put("size", size);
        return reviewDao.topHelpful(q);
    }
}
