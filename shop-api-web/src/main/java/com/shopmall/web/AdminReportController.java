package com.shopmall.web;

import com.shopmall.common.envelope.ApiResponse;
import com.shopmall.service.ReportService;
import com.shopmall.web.dto.adminreport.BigSpenderListXml;
import com.shopmall.web.dto.adminreport.BigSpenderXml;
import com.shopmall.web.dto.adminreport.CategorySalesListXml;
import com.shopmall.web.dto.adminreport.CategorySalesXml;
import com.shopmall.web.dto.adminreport.CohortRetentionListXml;
import com.shopmall.web.dto.adminreport.CohortRetentionXml;
import com.shopmall.web.dto.adminreport.CommissionReconcileListXml;
import com.shopmall.web.dto.adminreport.CommissionReconcileXml;
import com.shopmall.web.dto.adminreport.CouponUsageListXml;
import com.shopmall.web.dto.adminreport.CouponUsageXml;
import com.shopmall.web.dto.adminreport.DiscountReconListXml;
import com.shopmall.web.dto.adminreport.DiscountReconXml;
import com.shopmall.web.dto.adminreport.GradeBenchmarkListXml;
import com.shopmall.web.dto.adminreport.GradeBenchmarkXml;
import com.shopmall.web.dto.adminreport.InventoryTurnoverListXml;
import com.shopmall.web.dto.adminreport.InventoryTurnoverXml;
import com.shopmall.web.dto.adminreport.NeverOrderedListXml;
import com.shopmall.web.dto.adminreport.NeverOrderedXml;
import com.shopmall.web.dto.adminreport.NoReviewListXml;
import com.shopmall.web.dto.adminreport.NoReviewXml;
import com.shopmall.web.dto.adminreport.PointLedgerListXml;
import com.shopmall.web.dto.adminreport.PointLedgerXml;
import com.shopmall.web.dto.adminreport.RatingDistributionListXml;
import com.shopmall.web.dto.adminreport.RatingDistributionXml;
import com.shopmall.web.dto.adminreport.RevenueShareListXml;
import com.shopmall.web.dto.adminreport.RevenueShareXml;
import com.shopmall.web.dto.adminreport.SettlementDailyListXml;
import com.shopmall.web.dto.adminreport.SettlementDailyXml;
import com.shopmall.web.dto.adminreport.StatusBreakdownListXml;
import com.shopmall.web.dto.adminreport.StatusBreakdownXml;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

// Group-2 admin analytics. Reuses the existing ReportService/ReportDao tier —
// the admin reads are layered onto the same Report tier rather than a new one.
// Hand-maps the raw UPPERCASE HashMap keys returned by Report2.* into DTOs,
// same null-guard / Number-cast idiom as ReportController.
@Controller
@RequestMapping("/api/admin/reports")
public class AdminReportController {

    @Autowired private ReportService reportService;

    @RequestMapping(value = "/revenue-share", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse revenueShare() {
        List<Map<String, Object>> rows = reportService.revenueShare();
        RevenueShareListXml out = new RevenueShareListXml();
        for (Map<String, Object> row : rows) {
            RevenueShareXml x = new RevenueShareXml();
            x.setProductId(((Number) row.get("PRODUCT_ID")).longValue());
            x.setName(row.get("NAME") == null ? null : String.valueOf(row.get("NAME")));
            x.setCategory(row.get("CATEGORY") == null ? null : String.valueOf(row.get("CATEGORY")));
            x.setRevenue(((Number) row.get("REVENUE")).longValue());
            x.setRevenuePct(row.get("REVENUE_PCT") == null ? null : ((Number) row.get("REVENUE_PCT")).longValue());
            x.setTier(row.get("TIER") == null ? null : String.valueOf(row.get("TIER")));
            out.getRows().add(x);
        }
        return ResponseFactory.ok(out);
    }

    @RequestMapping(value = "/big-spenders", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse bigSpenders() {
        List<Map<String, Object>> rows = reportService.bigSpenders();
        BigSpenderListXml out = new BigSpenderListXml();
        for (Map<String, Object> row : rows) {
            BigSpenderXml x = new BigSpenderXml();
            x.setMemberId(((Number) row.get("MEMBER_ID")).longValue());
            x.setName(row.get("NAME") == null ? null : String.valueOf(row.get("NAME")));
            x.setGrade(row.get("GRADE") == null ? null : String.valueOf(row.get("GRADE")));
            x.setTotalSpend(((Number) row.get("TOTAL_SPEND")).longValue());
            x.setOrderCnt(((Number) row.get("ORDER_CNT")).longValue());
            out.getRows().add(x);
        }
        return ResponseFactory.ok(out);
    }

    @RequestMapping(value = "/category-sales", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse categorySales() {
        List<Map<String, Object>> rows = reportService.categorySales();
        CategorySalesListXml out = new CategorySalesListXml();
        for (Map<String, Object> row : rows) {
            CategorySalesXml x = new CategorySalesXml();
            x.setCategory(row.get("CATEGORY") == null ? null : String.valueOf(row.get("CATEGORY")));
            x.setOrderCnt(((Number) row.get("ORDER_CNT")).longValue());
            x.setUnits(((Number) row.get("UNITS")).longValue());
            x.setRevenue(((Number) row.get("REVENUE")).longValue());
            x.setVolumeTier(row.get("VOLUME_TIER") == null ? null : String.valueOf(row.get("VOLUME_TIER")));
            out.getRows().add(x);
        }
        return ResponseFactory.ok(out);
    }

    @RequestMapping(value = "/status-breakdown", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse statusBreakdown() {
        List<Map<String, Object>> rows = reportService.orderStatusBreakdown();
        StatusBreakdownListXml out = new StatusBreakdownListXml();
        for (Map<String, Object> row : rows) {
            StatusBreakdownXml x = new StatusBreakdownXml();
            x.setStatusLabel(row.get("STATUS_LABEL") == null ? null : String.valueOf(row.get("STATUS_LABEL")));
            x.setOrderCnt(((Number) row.get("ORDER_CNT")).longValue());
            x.setGross(((Number) row.get("GROSS")).longValue());
            out.getRows().add(x);
        }
        return ResponseFactory.ok(out);
    }

    @RequestMapping(value = "/never-ordered", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse neverOrdered() {
        List<Map<String, Object>> rows = reportService.neverOrderedProducts();
        NeverOrderedListXml out = new NeverOrderedListXml();
        for (Map<String, Object> row : rows) {
            NeverOrderedXml x = new NeverOrderedXml();
            x.setProductId(((Number) row.get("PRODUCT_ID")).longValue());
            x.setName(row.get("NAME") == null ? null : String.valueOf(row.get("NAME")));
            x.setCategory(row.get("CATEGORY") == null ? null : String.valueOf(row.get("CATEGORY")));
            x.setStatus(row.get("STATUS") == null ? null : String.valueOf(row.get("STATUS")));
            x.setPrice(((Number) row.get("PRICE")).longValue());
            x.setFlag(row.get("FLAG") == null ? null : String.valueOf(row.get("FLAG")));
            out.getRows().add(x);
        }
        return ResponseFactory.ok(out);
    }

    @RequestMapping(value = "/coupon-usage", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse couponUsage() {
        List<Map<String, Object>> rows = reportService.couponUsageStats();
        CouponUsageListXml out = new CouponUsageListXml();
        for (Map<String, Object> row : rows) {
            CouponUsageXml x = new CouponUsageXml();
            x.setDiscountType(row.get("DISCOUNT_TYPE") == null ? null : String.valueOf(row.get("DISCOUNT_TYPE")));
            x.setTotalCoupons(((Number) row.get("TOTAL_COUPONS")).longValue());
            x.setUsedCnt(((Number) row.get("USED_CNT")).longValue());
            x.setUseRatePct(row.get("USE_RATE_PCT") == null ? null : ((Number) row.get("USE_RATE_PCT")).longValue());
            x.setAvgDiscountVal(row.get("AVG_DISCOUNT_VAL") == null ? null : ((Number) row.get("AVG_DISCOUNT_VAL")).longValue());
            x.setEffectiveness(row.get("EFFECTIVENESS") == null ? null : String.valueOf(row.get("EFFECTIVENESS")));
            out.getRows().add(x);
        }
        return ResponseFactory.ok(out);
    }

    @RequestMapping(value = "/grade-benchmark", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse gradeBenchmark() {
        List<Map<String, Object>> rows = reportService.gradeBenchmark();
        GradeBenchmarkListXml out = new GradeBenchmarkListXml();
        for (Map<String, Object> row : rows) {
            GradeBenchmarkXml x = new GradeBenchmarkXml();
            x.setMemberId(((Number) row.get("MEMBER_ID")).longValue());
            x.setName(row.get("NAME") == null ? null : String.valueOf(row.get("NAME")));
            x.setGrade(row.get("GRADE") == null ? null : String.valueOf(row.get("GRADE")));
            x.setTotalSpend(((Number) row.get("TOTAL_SPEND")).longValue());
            x.setGradeAvgSpend(row.get("GRADE_AVG_SPEND") == null ? null : ((Number) row.get("GRADE_AVG_SPEND")).longValue());
            x.setBenchmark(row.get("BENCHMARK") == null ? null : String.valueOf(row.get("BENCHMARK")));
            out.getRows().add(x);
        }
        return ResponseFactory.ok(out);
    }

    // Batch observability: read what SettlementBatch wrote to settlement_daily.
    @RequestMapping(value = "/settlement-daily", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse settlementDaily() {
        List<Map<String, Object>> rows = reportService.settlementDaily();
        SettlementDailyListXml out = new SettlementDailyListXml();
        for (Map<String, Object> row : rows) {
            SettlementDailyXml x = new SettlementDailyXml();
            x.setSellerName(row.get("SELLER_NAME") == null ? null : String.valueOf(row.get("SELLER_NAME")));
            x.setSettleDay(row.get("SETTLE_DAY") == null ? null : String.valueOf(row.get("SETTLE_DAY")));
            x.setGrossAmount(row.get("GROSS_AMOUNT") == null ? null : ((Number) row.get("GROSS_AMOUNT")).longValue());
            x.setCommission(row.get("COMMISSION") == null ? null : ((Number) row.get("COMMISSION")).longValue());
            x.setPayout(row.get("PAYOUT") == null ? null : ((Number) row.get("PAYOUT")).longValue());
            out.getRows().add(x);
        }
        return ResponseFactory.ok(out);
    }

    // Batch observability: read what PointExpiryBatch wrote to point_ledger.
    @RequestMapping(value = "/point-ledger", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse pointLedger() {
        List<Map<String, Object>> rows = reportService.pointLedger();
        PointLedgerListXml out = new PointLedgerListXml();
        for (Map<String, Object> row : rows) {
            PointLedgerXml x = new PointLedgerXml();
            x.setLedgerId(((Number) row.get("LEDGER_ID")).longValue());
            x.setMemberId(((Number) row.get("MEMBER_ID")).longValue());
            x.setDelta(row.get("DELTA") == null ? null : ((Number) row.get("DELTA")).intValue());
            x.setReason(row.get("REASON") == null ? null : String.valueOf(row.get("REASON")));
            x.setRegDate(row.get("REG_DATE") == null ? null : String.valueOf(row.get("REG_DATE")));
            out.getRows().add(x);
        }
        return ResponseFactory.ok(out);
    }

    // ===== Group-3 domain-depth admin report topologies =====

    // member cohort/retention: first-order month vs later orders (derived-table self-comparison).
    @RequestMapping(value = "/cohort-retention", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse cohortRetention() {
        List<Map<String, Object>> rows = reportService.cohortRetention();
        CohortRetentionListXml out = new CohortRetentionListXml();
        for (Map<String, Object> row : rows) {
            CohortRetentionXml x = new CohortRetentionXml();
            x.setCohortMonth(row.get("COHORT_MONTH") == null ? null : String.valueOf(row.get("COHORT_MONTH")));
            x.setCohortSize(((Number) row.get("COHORT_SIZE")).longValue());
            x.setRetainedCnt(row.get("RETAINED_CNT") == null ? null : ((Number) row.get("RETAINED_CNT")).longValue());
            x.setRetentionPct(row.get("RETENTION_PCT") == null ? null : ((Number) row.get("RETENTION_PCT")).longValue());
            out.getRows().add(x);
        }
        return ResponseFactory.ok(out);
    }

    // inventory-turnover: inventory_log sold/restocked aggregation + CASE movement tier.
    @RequestMapping(value = "/inventory-turnover", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse inventoryTurnover() {
        List<Map<String, Object>> rows = reportService.inventoryTurnover();
        InventoryTurnoverListXml out = new InventoryTurnoverListXml();
        for (Map<String, Object> row : rows) {
            InventoryTurnoverXml x = new InventoryTurnoverXml();
            x.setProductId(((Number) row.get("PRODUCT_ID")).longValue());
            x.setName(row.get("NAME") == null ? null : String.valueOf(row.get("NAME")));
            x.setCategory(row.get("CATEGORY") == null ? null : String.valueOf(row.get("CATEGORY")));
            x.setUnitsSold(row.get("UNITS_SOLD") == null ? null : ((Number) row.get("UNITS_SOLD")).longValue());
            x.setUnitsRestocked(row.get("UNITS_RESTOCKED") == null ? null : ((Number) row.get("UNITS_RESTOCKED")).longValue());
            x.setNetFlow(row.get("NET_FLOW") == null ? null : ((Number) row.get("NET_FLOW")).longValue());
            x.setTurnoverTier(row.get("TURNOVER_TIER") == null ? null : String.valueOf(row.get("TURNOVER_TIER")));
            out.getRows().add(x);
        }
        return ResponseFactory.ok(out);
    }

    // coupon-vs-promotion reconciliation (UNION ALL across two definition tables).
    @RequestMapping(value = "/discount-reconciliation", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse discountReconciliation() {
        List<Map<String, Object>> rows = reportService.discountReconciliation();
        DiscountReconListXml out = new DiscountReconListXml();
        for (Map<String, Object> row : rows) {
            DiscountReconXml x = new DiscountReconXml();
            x.setSource(row.get("SOURCE") == null ? null : String.valueOf(row.get("SOURCE")));
            x.setDiscountType(row.get("DISCOUNT_TYPE") == null ? null : String.valueOf(row.get("DISCOUNT_TYPE")));
            x.setDefCnt(((Number) row.get("DEF_CNT")).longValue());
            x.setOpenCnt(row.get("OPEN_CNT") == null ? null : ((Number) row.get("OPEN_CNT")).longValue());
            x.setTotalFlatOrRate(row.get("TOTAL_FLAT_OR_RATE") == null ? null : ((Number) row.get("TOTAL_FLAT_OR_RATE")).longValue());
            x.setRedemptionState(row.get("REDEMPTION_STATE") == null ? null : String.valueOf(row.get("REDEMPTION_STATE")));
            out.getRows().add(x);
        }
        return ResponseFactory.ok(out);
    }

    // rating distribution: per-product star histogram + AVG + CASE sentiment.
    @RequestMapping(value = "/rating-distribution", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse ratingDistribution() {
        List<Map<String, Object>> rows = reportService.ratingDistribution();
        RatingDistributionListXml out = new RatingDistributionListXml();
        for (Map<String, Object> row : rows) {
            RatingDistributionXml x = new RatingDistributionXml();
            x.setProductId(((Number) row.get("PRODUCT_ID")).longValue());
            x.setName(row.get("NAME") == null ? null : String.valueOf(row.get("NAME")));
            x.setReviewCnt(((Number) row.get("REVIEW_CNT")).longValue());
            x.setStar5(row.get("STAR5") == null ? null : ((Number) row.get("STAR5")).longValue());
            x.setStar4(row.get("STAR4") == null ? null : ((Number) row.get("STAR4")).longValue());
            x.setStar3(row.get("STAR3") == null ? null : ((Number) row.get("STAR3")).longValue());
            x.setStar2(row.get("STAR2") == null ? null : ((Number) row.get("STAR2")).longValue());
            x.setStar1(row.get("STAR1") == null ? null : ((Number) row.get("STAR1")).longValue());
            // controller-tier math: round the AVG_RATING into a whole-number Long
            x.setAvgRating(row.get("AVG_RATING") == null ? null : Math.round(((Number) row.get("AVG_RATING")).doubleValue()));
            x.setSentiment(row.get("SENTIMENT") == null ? null : String.valueOf(row.get("SENTIMENT")));
            out.getRows().add(x);
        }
        return ResponseFactory.ok(out);
    }

    // on-sale products with no reviews yet (NOT EXISTS anti-join).
    @RequestMapping(value = "/products-without-reviews", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse productsWithoutReviews() {
        List<Map<String, Object>> rows = reportService.productsWithoutReviews();
        NoReviewListXml out = new NoReviewListXml();
        for (Map<String, Object> row : rows) {
            NoReviewXml x = new NoReviewXml();
            x.setProductId(((Number) row.get("PRODUCT_ID")).longValue());
            x.setName(row.get("NAME") == null ? null : String.valueOf(row.get("NAME")));
            x.setStatus(row.get("STATUS") == null ? null : String.valueOf(row.get("STATUS")));
            out.getRows().add(x);
        }
        return ResponseFactory.ok(out);
    }

    // finance-export commission reconcile: per-category commission/payout/VAT
    // re-derived in Java (ReportSettlementCalc), kept separate from the settlement rollup.
    @RequestMapping(value = "/commission-reconcile", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse commissionReconcile() {
        List<Map<String, Object>> rows = reportService.commissionReconcile();
        CommissionReconcileListXml out = new CommissionReconcileListXml();
        for (Map<String, Object> row : rows) {
            CommissionReconcileXml x = new CommissionReconcileXml();
            x.setCategory(row.get("CATEGORY") == null ? null : String.valueOf(row.get("CATEGORY")));
            x.setGrossAmount(row.get("GROSS_AMOUNT") == null ? null : ((Number) row.get("GROSS_AMOUNT")).longValue());
            x.setCommissionRate(row.get("COMMISSION_RATE") == null ? null : ((Number) row.get("COMMISSION_RATE")).longValue());
            x.setCommission(row.get("COMMISSION") == null ? null : ((Number) row.get("COMMISSION")).longValue());
            x.setPayout(row.get("PAYOUT") == null ? null : ((Number) row.get("PAYOUT")).longValue());
            x.setVat(row.get("VAT") == null ? null : ((Number) row.get("VAT")).longValue());
            out.getRows().add(x);
        }
        return ResponseFactory.ok(out);
    }
}
