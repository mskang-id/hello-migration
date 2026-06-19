package com.shopmall.characterization;

import com.shopmall.biz.ReportSettlementCalc;
import com.shopmall.dao.BatchDao;
import com.shopmall.manager.SettlementBatch;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class CommissionCalculationCharacterizationTest extends BaseIntegrationTest {

    @Autowired
    private ReportSettlementCalc reportSettlementCalc;

    @Autowired
    private SettlementBatch settlementBatch;

    @Autowired
    private BatchDao batchDao;

    @Test
    public void javaPath_electronics_12percent() {
        long commission = reportSettlementCalc.commission("ELECTRONICS", 100000L);
        assertThat(commission).isEqualTo(12000L);
    }

    @Test
    public void javaPath_books_6percent() {
        long commission = reportSettlementCalc.commission("BOOKS", 100000L);
        assertThat(commission).isEqualTo(6000L);
    }

    @Test
    public void javaPath_default_11percent() {
        long commission = reportSettlementCalc.commission("OTHER", 100000L);
        assertThat(commission).isEqualTo(11000L);
    }

    @Test
    public void sqlPath_batchRuns_producesResults() {
        settlementBatch.run();

        List<Map<String, Object>> results = batchDao.findSettlementDaily();
        assertThat(results).isNotNull();
        assertThat(results).isNotEmpty();
    }
}
