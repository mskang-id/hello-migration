package com.shopmall.dao.impl;

import com.shopmall.dao.ReportDao;
import com.ibatis.sqlmap.client.SqlMapClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ReportDaoImpl extends SqlMapClientDaoSupport implements ReportDao {

    @Autowired
    public void init(SqlMapClient sqlMapClient) {
        super.setSqlMapClient(sqlMapClient);
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> bestSellers(int topN) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("topN", topN);
        return getSqlMapClientTemplate().queryForList("Report.bestSellers", param);
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> dailySales() {
        return getSqlMapClientTemplate().queryForList("Report.dailySales");
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> sellerSettlement() {
        return getSqlMapClientTemplate().queryForList("Report.sellerSettlement");
    }

    // ===== Group-2 complex-SQL admin analytics (Report2.* statements) =====

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> bestSellerRevenueShare(int topN) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("topN", topN);
        return getSqlMapClientTemplate().queryForList("Report2.bestSellerRevenueShare", param);
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> bigSpenders() {
        return getSqlMapClientTemplate().queryForList("Report2.bigSpenders");
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> categorySales() {
        return getSqlMapClientTemplate().queryForList("Report2.categorySales");
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> orderStatusBreakdown() {
        return getSqlMapClientTemplate().queryForList("Report2.orderStatusBreakdown");
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> neverOrderedProducts() {
        return getSqlMapClientTemplate().queryForList("Report2.neverOrderedProducts");
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> couponUsageStats() {
        return getSqlMapClientTemplate().queryForList("Report2.couponUsageStats");
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> gradeBenchmark() {
        return getSqlMapClientTemplate().queryForList("Report2.gradeBenchmark");
    }

    // ===== Group-3 domain-depth admin report topologies =====

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> cohortRetention() {
        return getSqlMapClientTemplate().queryForList("Report2.cohortRetention");
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> inventoryTurnover() {
        return getSqlMapClientTemplate().queryForList("Report2.inventoryTurnover");
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> discountReconciliation() {
        return getSqlMapClientTemplate().queryForList("Report2.discountReconciliation");
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> ratingDistribution() {
        return getSqlMapClientTemplate().queryForList("Report2.ratingDistribution");
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> productsWithoutReviews() {
        return getSqlMapClientTemplate().queryForList("Report2.productsWithoutReviews");
    }
}
