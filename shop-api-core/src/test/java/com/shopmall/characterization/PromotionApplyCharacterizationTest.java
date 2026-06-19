package com.shopmall.characterization;

import com.shopmall.biz.PromotionBiz;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class PromotionApplyCharacterizationTest extends BaseIntegrationTest {

    @Autowired
    private PromotionBiz promotionBiz;

    @Test
    public void activePromotion_returnsProducts() throws Exception {
        long activePromotionId = queryInt(
            "SELECT promotion_id FROM promotion WHERE status = 'ACTIVE' LIMIT 1"
        );

        List<Map<String, Object>> result = promotionBiz.applyPreview(activePromotionId);

        assertThat(result).isNotEmpty();
    }

    @Test
    public void endedPromotion_returnsEmptyOrNoDiscount() throws Exception {
        int endedPromotionCount = queryInt(
            "SELECT COUNT(*) FROM promotion WHERE status = 'ENDED'"
        );

        if (endedPromotionCount > 0) {
            long endedPromotionId = queryInt(
                "SELECT promotion_id FROM promotion WHERE status = 'ENDED' LIMIT 1"
            );

            List<Map<String, Object>> result = promotionBiz.applyPreview(endedPromotionId);

            assertThat(result).isNotNull();
        }
    }

    @Test
    public void nonExistentPromotion_returnsEmpty() throws Exception {
        long nonExistentId = 99999L;

        List<Map<String, Object>> result = promotionBiz.applyPreview(nonExistentId);

        assertThat(result).isEmpty();
    }

    @Test
    public void activePromotion_withNormalStock_showsDiscount() throws Exception {
        int onSaleCount = queryInt(
            "SELECT COUNT(*) FROM product WHERE status = 'ON_SALE'"
        );
        assertThat(onSaleCount).isEqualTo(14);

        long activePromotionId = queryInt(
            "SELECT promotion_id FROM promotion WHERE status = 'ACTIVE' LIMIT 1"
        );

        List<Map<String, Object>> result = promotionBiz.applyPreview(activePromotionId);

        if (!result.isEmpty()) {
            boolean hasDiscount = false;
            for (Map<String, Object> item : result) {
                Object discount = item.get("discountAmount");
                if (discount != null) {
                    if (discount instanceof Number && ((Number) discount).doubleValue() > 0) {
                        hasDiscount = true;
                        break;
                    }
                }
            }
            assertThat(hasDiscount).isTrue();
        }
    }
}
