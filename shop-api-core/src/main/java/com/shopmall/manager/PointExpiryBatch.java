package com.shopmall.manager;
import com.shopmall.common.util.DateUtil;
import com.shopmall.dao.BatchDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Point-expiry sweep. Appends negative point_ledger rows + flips swept_yn; does not UPDATE
// member.point. The SqlMap floors member_id>=9 so members 1-3 are out of range. fixed-delay=1h, runs on boot.
@Component
public class PointExpiryBatch {
    @Autowired private BatchDao batchDao;
    private static final int EXPIRE_AFTER_DAYS = 365; // points expire one year after grant
    public void run() {
        try {
            Map<String,Object> q = new HashMap<String,Object>();
            q.put("cutoff", DateUtil.today());           // yyyymmdd string compare
            List<Map<String,Object>> rows = batchDao.findExpirablePoints(q);
            if (rows == null) return;
            for (Map<String,Object> r : rows) {
                Number mid = (Number) r.get("MEMBER_ID");      // uppercase keys from the raw HashMap select
                Number amt = (Number) r.get("GRANT_AMOUNT");
                Number pid = (Number) r.get("PE_ID");
                if (mid == null || amt == null || pid == null) continue;
                Map<String,Object> led = new HashMap<String,Object>();
                led.put("memberId", mid.longValue());
                led.put("delta", -Math.abs(amt.intValue()));   // negative EXPIRE delta
                led.put("regDate", DateUtil.today());
                batchDao.insertExpireLedger(led);              // append to point_ledger only
                batchDao.markSwept(pid.longValue());           // flag so a re-run skips it
            }
        } catch (Exception e) {
            // log+continue
        }
    }
}
