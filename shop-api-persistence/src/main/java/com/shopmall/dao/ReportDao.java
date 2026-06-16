package com.shopmall.dao;

import java.util.List;
import java.util.Map;

public interface ReportDao {
    List<Map<String, Object>> bestSellers(int topN);
    List<Map<String, Object>> dailySales();
    List<Map<String, Object>> sellerSettlement();

    // Group-2 complex-SQL admin analytics (Report2.* statements; same Report tier as the basic reports)
    List<Map<String, Object>> bestSellerRevenueShare(int topN);
    List<Map<String, Object>> bigSpenders();
    List<Map<String, Object>> categorySales();
    List<Map<String, Object>> orderStatusBreakdown();
    List<Map<String, Object>> neverOrderedProducts();
    List<Map<String, Object>> couponUsageStats();
    List<Map<String, Object>> gradeBenchmark();

    // Group-3 domain-depth admin report topologies (Report2.* statements)
    List<Map<String, Object>> cohortRetention();
    List<Map<String, Object>> inventoryTurnover();
    List<Map<String, Object>> discountReconciliation();
    List<Map<String, Object>> ratingDistribution();
    List<Map<String, Object>> productsWithoutReviews();
}
