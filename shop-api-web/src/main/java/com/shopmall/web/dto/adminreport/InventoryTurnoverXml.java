package com.shopmall.web.dto.adminreport;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class InventoryTurnoverXml {
    private Long productId;
    private String name;
    private String category;
    private Long unitsSold;
    private Long unitsRestocked;
    private Long netFlow;
    private String turnoverTier;

    public InventoryTurnoverXml() {}
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public Long getUnitsSold() { return unitsSold; }
    public void setUnitsSold(Long unitsSold) { this.unitsSold = unitsSold; }
    public Long getUnitsRestocked() { return unitsRestocked; }
    public void setUnitsRestocked(Long unitsRestocked) { this.unitsRestocked = unitsRestocked; }
    public Long getNetFlow() { return netFlow; }
    public void setNetFlow(Long netFlow) { this.netFlow = netFlow; }
    public String getTurnoverTier() { return turnoverTier; }
    public void setTurnoverTier(String turnoverTier) { this.turnoverTier = turnoverTier; }
}
