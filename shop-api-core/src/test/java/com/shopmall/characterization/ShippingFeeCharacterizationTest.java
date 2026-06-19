package com.shopmall.characterization;

import com.shopmall.biz.SettlementBiz;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class ShippingFeeCharacterizationTest extends BaseIntegrationTest {

    @Autowired
    private SettlementBiz settlementBiz;

    @Test
    public void gradeA_alwaysFree() {
        int fee = settlementBiz.shippingFee("A", 10000, "10001");
        assertThat(fee).isEqualTo(0);
    }

    @Test
    public void gradeB_belowThreshold_chargesFlat() {
        int fee = settlementBiz.shippingFee("B", 49999, "10001");
        assertThat(fee).isEqualTo(2500);
    }

    @Test
    public void gradeB_atThreshold_free() {
        int fee = settlementBiz.shippingFee("B", 50000, "10001");
        assertThat(fee).isEqualTo(0);
    }

    @Test
    public void gradeC_belowThreshold() {
        int fee = settlementBiz.shippingFee("C", 30000, "10001");
        assertThat(fee).isEqualTo(2500);
    }

    @Test
    public void gradeC_aboveThreshold() {
        int fee = settlementBiz.shippingFee("C", 100000, "10001");
        assertThat(fee).isEqualTo(0);
    }

    @Test
    public void islandSurcharge_zipcode63() {
        int fee = settlementBiz.shippingFee("B", 30000, "63001");
        assertThat(fee).isEqualTo(5500);
    }

    @Test
    public void islandSurcharge_zipcode40() {
        int fee = settlementBiz.shippingFee("A", 30000, "40123");
        assertThat(fee).isEqualTo(3000);
    }

    @Test
    public void islandSurcharge_aboveThreshold() {
        int fee = settlementBiz.shippingFee("B", 50000, "63500");
        assertThat(fee).isEqualTo(3000);
    }
}
