package com.shopmall.characterization;

import com.shopmall.manager.PointExpiryBatch;
import com.shopmall.dao.BatchDao;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class PointExpiryBatchCharacterizationTest extends BaseIntegrationTest {

    @Autowired
    private PointExpiryBatch pointExpiryBatch;

    @Autowired
    private BatchDao batchDao;

    @Test
    public void run_sweepsExpiredPoints() throws Exception {
        int beforeUnsweptCount = queryInt(
            "SELECT COUNT(*) FROM point_expiry WHERE swept_yn = 'N' AND expire_date < CURRENT_DATE"
        );
        assertThat(beforeUnsweptCount).isGreaterThan(0);

        pointExpiryBatch.run();

        int afterUnsweptCount = queryInt(
            "SELECT COUNT(*) FROM point_expiry WHERE swept_yn = 'N' AND expire_date < CURRENT_DATE"
        );
        assertThat(afterUnsweptCount).isLessThan(beforeUnsweptCount);
    }

    @Test
    public void run_insertsNegativeLedgerEntries() throws Exception {
        int beforeNegativeCount = queryInt(
            "SELECT COUNT(*) FROM point_ledger WHERE point_amount < 0 AND reason LIKE '%expiry%'"
        );

        pointExpiryBatch.run();

        int afterNegativeCount = queryInt(
            "SELECT COUNT(*) FROM point_ledger WHERE point_amount < 0 AND reason LIKE '%expiry%'"
        );
        assertThat(afterNegativeCount).isGreaterThan(beforeNegativeCount);
    }

    @Test
    public void run_doesNotUpdateMemberPointDirectly() throws Exception {
        int memberIdWithExpiry = queryInt(
            "SELECT member_id FROM point_expiry WHERE swept_yn = 'N' AND expire_date < CURRENT_DATE AND member_id >= 9 LIMIT 1"
        );
        int beforePoint = queryInt(
            "SELECT point FROM member WHERE member_id = " + memberIdWithExpiry
        );

        pointExpiryBatch.run();

        int afterPoint = queryInt(
            "SELECT point FROM member WHERE member_id = " + memberIdWithExpiry
        );
        assertThat(afterPoint).isEqualTo(beforePoint);
    }

    @Test
    public void run_idempotent_secondRunDoesNothing() throws Exception {
        pointExpiryBatch.run();
        int firstLedgerCount = queryInt("SELECT COUNT(*) FROM point_ledger");

        pointExpiryBatch.run();

        int secondLedgerCount = queryInt("SELECT COUNT(*) FROM point_ledger");
        assertThat(secondLedgerCount).isEqualTo(firstLedgerCount);
    }

    @Test
    public void run_skipsLowMemberIds() throws Exception {
        int lowMemberIdUnsweptCount = queryInt(
            "SELECT COUNT(*) FROM point_expiry WHERE swept_yn = 'N' AND expire_date < CURRENT_DATE AND member_id < 9"
        );

        pointExpiryBatch.run();

        int afterLowMemberIdUnsweptCount = queryInt(
            "SELECT COUNT(*) FROM point_expiry WHERE swept_yn = 'N' AND expire_date < CURRENT_DATE AND member_id < 9"
        );
        assertThat(afterLowMemberIdUnsweptCount).isEqualTo(lowMemberIdUnsweptCount);
    }
}
