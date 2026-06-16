package com.shopmall.manager;

import com.shopmall.common.util.DateUtil;
import com.shopmall.dao.InventoryLogDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

// Inventory movement log. Separate from StockManager: this writes the audit row, that adjusts the count.
@Component
public class InventoryManager {

    @Autowired private InventoryLogDao inventoryLogDao;

    public void logChange(long optionId, int changeQty, String reason) {
        Map<String, Object> log = new HashMap<String, Object>();
        log.put("optionId", optionId);
        log.put("changeQty", changeQty);
        log.put("reason", reason);
        log.put("regDate", DateUtil.today());
        inventoryLogDao.insertLog(log);
    }
}
