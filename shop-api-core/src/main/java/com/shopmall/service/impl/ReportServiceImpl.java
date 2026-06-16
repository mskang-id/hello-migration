package com.shopmall.service.impl;

import com.shopmall.biz.ReportSettlementCalc;
import com.shopmall.common.constant.AppConstants;
import com.shopmall.dao.BatchDao;
import com.shopmall.dao.ReportDao;
import com.shopmall.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Read-only reporting backed by ReportDao. Default top-N comes from AppConstants
// rather than a request param.
@Service
public class ReportServiceImpl implements ReportService {

    @Autowired private ReportDao reportDao;
    @Autowired private BatchDao batchDao;   // report tier also reads the batch tables for observability
    @Autowired private ReportSettlementCalc reportSettlementCalc;   // Java-side finance-export math

    public List<Map<String, Object>> bestSellers() {
        return reportDao.bestSellers(AppConstants.REPORT_TOP_N);
    }

    public List<Map<String, Object>> dailySales() {
        return reportDao.dailySales();
    }

    public List<Map<String, Object>> sellerSettlement() {
        return reportDao.sellerSettlement();
    }

    // ===== admin analytics backed by the Report2.* SQL statements =====

    public List<Map<String, Object>> revenueShare() {
        return reportDao.bestSellerRevenueShare(AppConstants.REPORT_TOP_N);
    }

    public List<Map<String, Object>> bigSpenders() {
        return reportDao.bigSpenders();
    }

    public List<Map<String, Object>> categorySales() {
        return reportDao.categorySales();
    }

    public List<Map<String, Object>> orderStatusBreakdown() {
        return reportDao.orderStatusBreakdown();
    }

    public List<Map<String, Object>> neverOrderedProducts() {
        return reportDao.neverOrderedProducts();
    }

    public List<Map<String, Object>> couponUsageStats() {
        return reportDao.couponUsageStats();
    }

    public List<Map<String, Object>> gradeBenchmark() {
        return reportDao.gradeBenchmark();
    }

    // ===== Batch observability reads (delegate to BatchDao) =====

    public List<Map<String, Object>> settlementDaily() {
        return batchDao.findSettlementDaily();
    }

    public List<Map<String, Object>> pointLedger() {
        return batchDao.findPointLedger();
    }

    // ===== additional admin reports backed by the Report2.* SQL statements =====

    public List<Map<String, Object>> cohortRetention() {
        return reportDao.cohortRetention();
    }

    public List<Map<String, Object>> inventoryTurnover() {
        return reportDao.inventoryTurnover();
    }

    public List<Map<String, Object>> discountReconciliation() {
        return reportDao.discountReconciliation();
    }

    public List<Map<String, Object>> ratingDistribution() {
        return reportDao.ratingDistribution();
    }

    public List<Map<String, Object>> productsWithoutReviews() {
        return reportDao.productsWithoutReviews();
    }

    // ===== finance-export commission reconcile (read-only) =====
    // Re-derives commission/payout/VAT per category in Java for the finance team's
    // export, sitting alongside the settlement SQL rollup.
    public List<Map<String, Object>> commissionReconcile() {
        List<Map<String, Object>> sales = reportDao.categorySales();
        List<Map<String, Object>> out = new ArrayList<Map<String, Object>>();
        for (Map<String, Object> row : sales) {
            String category = row.get("CATEGORY") == null ? null : String.valueOf(row.get("CATEGORY"));
            long gross = row.get("REVENUE") == null ? 0L : ((Number) row.get("REVENUE")).longValue();
            long commission = reportSettlementCalc.commission(category, gross);
            long payout = reportSettlementCalc.payout(category, gross);
            long vat = reportSettlementCalc.vat(payout);
            Map<String, Object> r = new HashMap<String, Object>();
            r.put("CATEGORY", category);
            r.put("GROSS_AMOUNT", gross);
            r.put("COMMISSION_RATE", (long) reportSettlementCalc.commissionRate(category));
            r.put("COMMISSION", commission);
            r.put("PAYOUT", payout);
            r.put("VAT", vat);
            out.add(r);
        }
        return out;
    }
}
