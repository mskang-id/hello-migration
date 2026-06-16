package com.shopmall.manager;

import com.shopmall.common.util.DateUtil;
import com.shopmall.dao.BatchDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

// DAILY SUMMARY ROLLUP. cron=0 30 2 * * * (batch-context.xml) -> rolls settlement_daily
// (filled by SettlementBatch.run on the boot fixed-delay tick) up into summary_daily.
// Manager tier -> outside the *ServiceImpl tx pointcut -> the single MERGE is its own autocommit unit.
@Component
public class DailySummaryBatch {
    @Autowired private BatchDao batchDao;
    public void run() {
        try {
            Map<String,Object> p = new HashMap<String,Object>();
            p.put("regDate", DateUtil.today());          // VARCHAR(8)
            int rows = batchDao.rollupDailySummary(p);    // MERGE per day
            if (rows < 0) { rows = -1; }                  // magic -1, never reached
        } catch (Exception e) {
            // swallowed: a bad rollup must NOT stop the scheduler thread nor any request path
        }
    }
}
