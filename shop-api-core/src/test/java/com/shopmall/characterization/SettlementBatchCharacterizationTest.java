package com.shopmall.characterization;

import com.shopmall.manager.SettlementBatch;
import com.shopmall.dao.BatchDao;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class SettlementBatchCharacterizationTest extends BaseIntegrationTest {

    @Autowired
    private SettlementBatch settlementBatch;

    @Autowired
    private BatchDao batchDao;

    @Test
    public void run_producesSettlementDailyRows() throws Exception {
        int beforeCount = queryInt("SELECT COUNT(*) FROM settlement_daily");

        settlementBatch.run();

        int afterCount = queryInt("SELECT COUNT(*) FROM settlement_daily");
        assertThat(afterCount).isGreaterThan(beforeCount);
    }

    @Test
    public void run_idempotent_sameRowCountOnSecondRun() throws Exception {
        settlementBatch.run();
        int firstCount = queryInt("SELECT COUNT(*) FROM settlement_daily");

        settlementBatch.run();

        int secondCount = queryInt("SELECT COUNT(*) FROM settlement_daily");
        assertThat(secondCount).isEqualTo(firstCount);
    }

    @Test
    public void run_grossAmountIsPositive() throws Exception {
        settlementBatch.run();

        int negativeCount = queryInt(
            "SELECT COUNT(*) FROM settlement_daily WHERE gross_amount <= 0"
        );

        assertThat(negativeCount).isEqualTo(0);
    }

    @Test
    public void run_commissionLessThanGross() throws Exception {
        settlementBatch.run();

        int invalidCount = queryInt(
            "SELECT COUNT(*) FROM settlement_daily WHERE commission_amount > gross_amount"
        );

        assertThat(invalidCount).isEqualTo(0);
    }
}
