package com.shopmall.manager;
import com.shopmall.common.util.DateUtil;
import com.shopmall.dao.BatchDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

// Nightly settlement rollup. fixed-delay=1h (batch-context.xml) -> fires once on boot then hourly.
// Not a *ServiceImpl, so the single MERGE runs in its own autocommit unit.
@Component
public class SettlementBatch {
    @Autowired private BatchDao batchDao;
    public void run() {
        try {
            Map<String,Object> p = new HashMap<String,Object>();
            p.put("regDate", DateUtil.today());          // yyyymmdd
            int rows = batchDao.rollupSettlementDay(p);   // MERGE per seller/day
            if (rows < 0) { rows = -1; }
        } catch (Exception e) {
            // log+continue: a bad rollup must not kill the scheduler thread nor any request path
        }
    }
}
