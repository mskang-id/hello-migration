package com.shopmall.web;

import com.shopmall.common.envelope.ApiResponse;
import com.shopmall.service.PromotionService;
import com.shopmall.web.dto.promotion.ApplyPreviewXml;
import com.shopmall.web.dto.promotion.CampaignProductXml;
import com.shopmall.web.dto.promotion.ExpiringPromotionListXml;
import com.shopmall.web.dto.promotion.ExpiringPromotionXml;
import com.shopmall.web.dto.promotion.PromotionListXml;
import com.shopmall.web.dto.promotion.PromotionXml;
import com.shopmall.web.dto.promotion.StackablePromotionListXml;
import com.shopmall.web.dto.promotion.StackablePromotionXml;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/promotions")
public class PromotionController {

    @Autowired private PromotionService promotionService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse list() {
        List<Map<String, Object>> rows = promotionService.listActive();
        PromotionListXml out = new PromotionListXml();
        if (rows != null) {
            for (Map<String, Object> row : rows) {
                PromotionXml p = new PromotionXml();
                p.setPromotionId(((Number) row.get("PROMOTION_ID")).longValue());
                p.setName(row.get("NAME") == null ? null : String.valueOf(row.get("NAME")));
                p.setDiscountType(row.get("DISCOUNT_TYPE") == null ? null : String.valueOf(row.get("DISCOUNT_TYPE")));
                p.setDiscountVal(((Number) row.get("DISCOUNT_VAL")).longValue());
                p.setStartDate(row.get("START_DATE") == null ? null : String.valueOf(row.get("START_DATE")));
                p.setEndDate(row.get("END_DATE") == null ? null : String.valueOf(row.get("END_DATE")));
                p.setStatus(row.get("STATUS") == null ? null : String.valueOf(row.get("STATUS")));
                out.getRows().add(p);
            }
        }
        return ResponseFactory.ok(out);
    }

    @RequestMapping(value = "/{id}/preview", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse preview(@PathVariable("id") long id) {
        List<Map<String, Object>> rows = promotionService.applyPreview(id);
        ApplyPreviewXml out = new ApplyPreviewXml();
        out.setPromotionId(id);
        long totalDiscount = 0L;
        if (rows != null) {
            for (Map<String, Object> row : rows) {
                CampaignProductXml c = new CampaignProductXml();
                c.setProductId(((Number) row.get("PRODUCT_ID")).longValue());
                c.setName(row.get("NAME") == null ? null : String.valueOf(row.get("NAME")));
                c.setCategory(row.get("CATEGORY") == null ? null : String.valueOf(row.get("CATEGORY")));
                c.setPrice(((Number) row.get("PRICE")).longValue());
                c.setComputedDiscount(((Number) row.get("COMPUTED_DISCOUNT")).longValue());
                c.setFinalPrice(((Number) row.get("FINAL_PRICE")).longValue());
                totalDiscount += ((Number) row.get("COMPUTED_DISCOUNT")).longValue();   // controller-tier math
                out.getProducts().add(c);
            }
        }
        out.setTotalDiscount(totalDiscount);
        return ResponseFactory.ok(out);
    }

    @RequestMapping(value = "/product/{productId}/stackable", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse stackable(@PathVariable("productId") long productId) {
        List<Map<String, Object>> rows = promotionService.stackableForProduct(productId);
        StackablePromotionListXml out = new StackablePromotionListXml();
        out.setProductId(productId);
        long totalStacked = 0L;   // controller-tier stacking math over IN_WINDOW rows
        if (rows != null) {
            for (Map<String, Object> row : rows) {
                StackablePromotionXml s = new StackablePromotionXml();
                s.setPromotionId(((Number) row.get("PROMOTION_ID")).longValue());
                s.setName(row.get("NAME") == null ? null : String.valueOf(row.get("NAME")));
                s.setDiscountType(row.get("DISCOUNT_TYPE") == null ? null : String.valueOf(row.get("DISCOUNT_TYPE")));
                s.setDiscountVal(((Number) row.get("DISCOUNT_VAL")).longValue());
                s.setComputedDiscount(((Number) row.get("COMPUTED_DISCOUNT")).longValue());
                s.setWindowState(row.get("WINDOW_STATE") == null ? null : String.valueOf(row.get("WINDOW_STATE")));
                totalStacked += ((Number) row.get("COMPUTED_DISCOUNT")).longValue();
                out.getPromotions().add(s);
            }
        }
        out.setTotalStackedDiscount(totalStacked);
        Map<String, Object> best = promotionService.bestPromotionForProduct(productId);   // top-1 promotion
        if (best != null) {
            out.setBestPromotionId(((Number) best.get("PROMOTION_ID")).longValue());
            out.setBestComputedDiscount(((Number) best.get("COMPUTED_DISCOUNT")).longValue());
        }
        return ResponseFactory.ok(out);
    }

    @RequestMapping(value = "/expiring", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse expiring(@RequestParam(value = "withinDays", required = false, defaultValue = "7") int withinDays) {
        List<Map<String, Object>> rows = promotionService.expiringSoon(withinDays);
        ExpiringPromotionListXml out = new ExpiringPromotionListXml();
        int expiringCount = 0;   // controller-tier count off the LIFECYCLE CASE
        if (rows != null) {
            for (Map<String, Object> row : rows) {
                ExpiringPromotionXml e = new ExpiringPromotionXml();
                e.setPromotionId(((Number) row.get("PROMOTION_ID")).longValue());
                e.setName(row.get("NAME") == null ? null : String.valueOf(row.get("NAME")));
                e.setDiscountType(row.get("DISCOUNT_TYPE") == null ? null : String.valueOf(row.get("DISCOUNT_TYPE")));
                e.setDiscountVal(((Number) row.get("DISCOUNT_VAL")).longValue());
                e.setStartDate(row.get("START_DATE") == null ? null : String.valueOf(row.get("START_DATE")));
                e.setEndDate(row.get("END_DATE") == null ? null : String.valueOf(row.get("END_DATE")));
                e.setStatus(row.get("STATUS") == null ? null : String.valueOf(row.get("STATUS")));
                e.setLifecycle(row.get("LIFECYCLE") == null ? null : String.valueOf(row.get("LIFECYCLE")));
                if ("EXPIRING".equals(row.get("LIFECYCLE"))) {
                    expiringCount++;
                }
                out.getRows().add(e);
            }
        }
        out.setExpiringCount(expiringCount);
        return ResponseFactory.ok(out);
    }
}
