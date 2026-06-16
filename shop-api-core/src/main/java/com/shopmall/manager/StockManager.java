package com.shopmall.manager;

import com.shopmall.dao.ProductDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

// Stock helper. Wraps the deductStock DAO call so callers don't build the param Map.
@Component
public class StockManager {

    @Autowired private ProductDao productDao;

    public void deduct(long optionId, int qty) {
        Map<String, Object> sp = new HashMap<String, Object>();
        sp.put("optionId", optionId);
        sp.put("qty", qty);
        productDao.deductStock(sp);
    }

    public void restock(long optionId, int qty) {
        Map<String, Object> sp = new HashMap<String, Object>();
        sp.put("optionId", optionId);
        sp.put("qty", qty);
        productDao.restock(sp);
    }
}
