package com.shopmall.service;

import java.util.List;
import java.util.Map;

public interface ReportService {
    List<Map<String, Object>> bestSellers();
    List<Map<String, Object>> dailySales();
    List<Map<String, Object>> sellerSettlement();

    // admin analytics backed by the Report2.* SQL statements
    List<Map<String, Object>> revenueShare();
    List<Map<String, Object>> bigSpenders();
    List<Map<String, Object>> categorySales();
    List<Map<String, Object>> orderStatusBreakdown();
    List<Map<String, Object>> neverOrderedProducts();
    List<Map<String, Object>> couponUsageStats();
    List<Map<String, Object>> gradeBenchmark();

    // Batch observability reads (route through BatchDao — batch tier tables
    // settlement_daily / point_ledger exposed for read verification)
    List<Map<String, Object>> settlementDaily();
    List<Map<String, Object>> pointLedger();

    // additional admin reports backed by the Report2.* SQL statements
    List<Map<String, Object>> cohortRetention();
    List<Map<String, Object>> inventoryTurnover();
    List<Map<String, Object>> discountReconciliation();
    List<Map<String, Object>> ratingDistribution();
    List<Map<String, Object>> productsWithoutReviews();

    // finance-export commission reconcile (Java-side re-derivation over category sales)
    List<Map<String, Object>> commissionReconcile();
}
