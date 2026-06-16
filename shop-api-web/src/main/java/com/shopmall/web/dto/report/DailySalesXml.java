package com.shopmall.web.dto.report;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class DailySalesXml {
    private String orderDate;
    private Long orderCnt;
    private Long salesAmount;

    public DailySalesXml() {}
    public String getOrderDate() { return orderDate; }
    public void setOrderDate(String orderDate) { this.orderDate = orderDate; }
    public Long getOrderCnt() { return orderCnt; }
    public void setOrderCnt(Long orderCnt) { this.orderCnt = orderCnt; }
    public Long getSalesAmount() { return salesAmount; }
    public void setSalesAmount(Long salesAmount) { this.salesAmount = salesAmount; }
}
