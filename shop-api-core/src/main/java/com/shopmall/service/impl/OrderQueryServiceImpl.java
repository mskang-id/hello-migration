package com.shopmall.service.impl;

import com.shopmall.dao.OrderDao;
import com.shopmall.manager.PointManager;
import com.shopmall.service.OrderQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderQueryServiceImpl implements OrderQueryService {

    @Autowired private OrderDao orderDao;

    // used to attach the read-only loyalty tier to the order-history view;
    // only loyaltyTier() is called here, never earn(), so there is no re-entry.
    @Autowired private PointManager pointManager;

    public Map<String, Object> getOrderDetail(long orderId) {
        Map<String, Object> order = orderDao.findOrderById(orderId);
        if (order == null) {
            return null;
        }
        List<Map<String, Object>> items = orderDao.findOrderItems(orderId);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("order", order);
        result.put("items", items);
        return result;
    }

    public Map<String, Object> getMemberOrderHistory(long memberId, int page, int size) {
        int offset = page * size;
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("memberId", memberId);
        param.put("size", size);
        param.put("offset", offset);
        List<Map<String, Object>> rows = orderDao.findOrdersByMember(param);
        int total = orderDao.countOrdersByMember(memberId);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("memberId", memberId);
        result.put("page", page);
        result.put("size", size);
        result.put("total", total);
        result.put("rows", rows);
        result.put("loyaltyTier", pointManager.loyaltyTier(memberId));
        return result;
    }

    public Map<String, Object> getSettlement(long orderId) {
        return orderDao.findSettlement(orderId);
    }
}
