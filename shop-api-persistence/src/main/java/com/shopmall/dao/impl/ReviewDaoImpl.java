package com.shopmall.dao.impl;

import com.shopmall.dao.ReviewDao;
import com.ibatis.sqlmap.client.SqlMapClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;

@Repository
public class ReviewDaoImpl extends SqlMapClientDaoSupport implements ReviewDao {
    @Autowired public void init(SqlMapClient sqlMapClient) { super.setSqlMapClient(sqlMapClient); }
    public long insertReview(Map<String,Object> p) {
        getSqlMapClientTemplate().insert("Review.insertReview", p);
        return ((Number) p.get("reviewId")).longValue();
    }
    @SuppressWarnings("unchecked")
    public List<Map<String,Object>> findByProduct(Map<String,Object> p) {
        return getSqlMapClientTemplate().queryForList("Review.findByProduct", p);
    }
    public int countByProduct(long productId) {
        return (Integer) getSqlMapClientTemplate().queryForObject("Review.countByProduct", productId);
    }
    @SuppressWarnings("unchecked")
    public Map<String,Object> ratingSummary(long productId) {
        return (Map<String,Object>) getSqlMapClientTemplate().queryForObject("Review.ratingSummary", productId);
    }
    @SuppressWarnings("unchecked")
    public List<Map<String,Object>> findByProductVerified(Map<String,Object> p) {
        return getSqlMapClientTemplate().queryForList("Review.findByProductVerified", p);
    }
    @SuppressWarnings("unchecked")
    public Map<String,Object> weightedScore(long productId) {
        return (Map<String,Object>) getSqlMapClientTemplate().queryForObject("Review.weightedScore", productId);
    }
    @SuppressWarnings("unchecked")
    public List<Map<String,Object>> ratingHistogram(long productId) {
        return getSqlMapClientTemplate().queryForList("Review.ratingHistogram", productId);
    }
    @SuppressWarnings("unchecked")
    public List<Map<String,Object>> topHelpful(Map<String,Object> p) {
        return getSqlMapClientTemplate().queryForList("Review.topHelpful", p);
    }
    public int countVerified(long productId) {
        return (Integer) getSqlMapClientTemplate().queryForObject("Review.countVerified", productId);
    }
}
