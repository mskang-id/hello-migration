package com.shopmall.web.dto.adminreport;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class RevenueShareXml {
    private Long productId;
    private String name;
    private String category;
    private Long revenue;
    private Long revenuePct;
    private String tier;

    public RevenueShareXml() {}
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public Long getRevenue() { return revenue; }
    public void setRevenue(Long revenue) { this.revenue = revenue; }
    public Long getRevenuePct() { return revenuePct; }
    public void setRevenuePct(Long revenuePct) { this.revenuePct = revenuePct; }
    public String getTier() { return tier; }
    public void setTier(String tier) { this.tier = tier; }
}
