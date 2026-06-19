package com.shopmall.characterization;

import com.shopmall.biz.ReportSettlementCalc;
import com.shopmall.biz.SettlementBiz;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class VatCalculationCharacterizationTest extends BaseIntegrationTest {

    @Autowired
    private SettlementBiz settlementBiz;

    @Autowired
    private ReportSettlementCalc reportSettlementCalc;

    @Test
    public void settlementBiz_vat_tenPercent() {
        int vat = settlementBiz.vat(100000);
        assertThat(vat).isEqualTo(10000);
    }

    @Test
    public void settlementBiz_vat_zero() {
        int vat = settlementBiz.vat(0);
        assertThat(vat).isEqualTo(0);
    }

    @Test
    public void settlementBiz_vat_integerDivision() {
        int vat = settlementBiz.vat(33333);
        assertThat(vat).isEqualTo(3333);
    }

    @Test
    public void reportCalc_vat_elevenPercent() {
        long vat = reportSettlementCalc.vat(100000L);
        assertThat(vat).isEqualTo(11000L);
    }

    @Test
    public void reportCalc_vat_integerDivision() {
        long vat = reportSettlementCalc.vat(33333L);
        assertThat(vat).isEqualTo(3666L);
    }
}
