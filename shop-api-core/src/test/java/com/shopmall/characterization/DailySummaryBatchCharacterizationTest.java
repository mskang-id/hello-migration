package com.shopmall.characterization;

import com.shopmall.manager.SettlementBatch;
import com.shopmall.manager.DailySummaryBatch;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class DailySummaryBatchCharacterizationTest extends BaseIntegrationTest {

    @Autowired
    private SettlementBatch settlementBatch;

    @Autowired
    private DailySummaryBatch dailySummaryBatch;

    @Test
    public void run_afterSettlement_producesSummaryRows() throws Exception {
        settlementBatch.run();
        int beforeCount = queryInt("SELECT COUNT(*) FROM summary_daily");

        dailySummaryBatch.run();

        int afterCount = queryInt("SELECT COUNT(*) FROM summary_daily");
        assertThat(afterCount).isGreaterThan(beforeCount);
    }

    @Test
    public void run_withoutSettlement_producesNoRows() throws Exception {
        dataSource.getConnection().createStatement().executeUpdate("DELETE FROM summary_daily");
        dataSource.getConnection().createStatement().executeUpdate("DELETE FROM settlement_daily");

        int beforeCount = queryInt("SELECT COUNT(*) FROM summary_daily");

        dailySummaryBatch.run();

        int afterCount = queryInt("SELECT COUNT(*) FROM summary_daily");
        assertThat(afterCount).isEqualTo(beforeCount);
    }

    @Test
    public void run_idempotent() throws Exception {
        settlementBatch.run();
        dailySummaryBatch.run();
        int firstCount = queryInt("SELECT COUNT(*) FROM summary_daily");

        dailySummaryBatch.run();

        int secondCount = queryInt("SELECT COUNT(*) FROM summary_daily");
        assertThat(secondCount).isEqualTo(firstCount);
    }
}
