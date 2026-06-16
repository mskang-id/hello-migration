package com.shopmall.common.util;

import javax.xml.bind.JAXB;
import java.io.StringWriter;
import java.util.Map;

public class OrderXmlExporter {

    // serializes an order summary to XML for the audit log.
    public static String toXml(Map<String, Object> order) {
        OrderXml x = new OrderXml();
        x.orderDate = String.valueOf(order.get("orderDate"));
        x.totalPrice = String.valueOf(order.get("totalPrice"));
        x.pgTid = String.valueOf(order.get("pgTid"));
        StringWriter sw = new StringWriter();
        JAXB.marshal(x, sw);
        return sw.toString();
    }

    public static class OrderXml {
        public String orderDate;
        public String totalPrice;
        public String pgTid;
    }
}
