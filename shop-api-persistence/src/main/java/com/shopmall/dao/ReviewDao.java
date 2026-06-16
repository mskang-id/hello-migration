package com.shopmall.dao;

import java.util.List;
import java.util.Map;

public interface ReviewDao {
    long insertReview(Map<String,Object> p);
    List<Map<String,Object>> findByProduct(Map<String,Object> p);
    int countByProduct(long productId);
    Map<String,Object> ratingSummary(long productId);

    List<Map<String,Object>> findByProductVerified(Map<String,Object> p);
    Map<String,Object> weightedScore(long productId);
    List<Map<String,Object>> ratingHistogram(long productId);
    List<Map<String,Object>> topHelpful(Map<String,Object> p);
    int countVerified(long productId);
}
