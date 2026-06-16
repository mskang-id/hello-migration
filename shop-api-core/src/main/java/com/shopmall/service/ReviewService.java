package com.shopmall.service;

import java.util.Map;

public interface ReviewService {
    long createReview(Map<String,Object> param);
    Map<String,Object> listByProduct(long productId, int page, int size);
    Map<String,Object> verifiedByProduct(long productId, int page, int size);
    Map<String,Object> weightedScore(long productId);
    java.util.List<Map<String,Object>> ratingHistogram(long productId);
    java.util.List<Map<String,Object>> topHelpful(long productId, int size);
}
