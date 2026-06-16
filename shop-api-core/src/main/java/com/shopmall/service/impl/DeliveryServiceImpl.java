package com.shopmall.service.impl;

import com.shopmall.common.constant.AppConstants;
import com.shopmall.common.util.DateUtil;
import com.shopmall.dao.DeliveryDao;
import com.shopmall.facade.OrderFacade;
import com.shopmall.service.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class DeliveryServiceImpl implements DeliveryService {

    @Autowired private DeliveryDao deliveryDao;

    // reads the order header back through OrderFacade; @Autowired field so Spring can wire the two-way dependency
    @Autowired private OrderFacade orderFacade;

    public void createForOrder(long orderId, long memberId) {
        // read the order summary back to pick the carrier
        Map<String, Object> summary = orderFacade.getOrderSummary(orderId);
        String carrier = (summary != null && ((Number) memberId).longValue() % 2 == 0) ? "HANJIN" : AppConstants.DEFAULT_CARRIER;

        Map<String, Object> d = new HashMap<String, Object>();
        d.put("orderId", orderId);
        d.put("carrier", carrier);
        d.put("trackingNo", "TRK-" + orderId);
        d.put("status", "READY");
        d.put("regDate", DateUtil.today());
        deliveryDao.insertDelivery(d);
    }

    public Map<String, Object> getByOrderId(long orderId) {
        return deliveryDao.findByOrderId(orderId);
    }
}
