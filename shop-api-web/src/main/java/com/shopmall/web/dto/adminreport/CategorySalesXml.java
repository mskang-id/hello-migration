package com.shopmall.web.dto.adminreport;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class CategorySalesXml {
    private String category;
    private Long orderCnt;
    private Long units;
    private Long revenue;
    private String volumeTier;

    public CategorySalesXml() {}
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public Long getOrderCnt() { return orderCnt; }
    public void setOrderCnt(Long orderCnt) { this.orderCnt = orderCnt; }
    public Long getUnits() { return units; }
    public void setUnits(Long units) { this.units = units; }
    public Long getRevenue() { return revenue; }
    public void setRevenue(Long revenue) { this.revenue = revenue; }
    public String getVolumeTier() { return volumeTier; }
    public void setVolumeTier(String volumeTier) { this.volumeTier = volumeTier; }
}
