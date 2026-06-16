package com.shopmall.web.dto.promotion;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class CampaignProductXml {
    private Long productId;
    private String name;
    private String category;
    private Long price;
    private Long computedDiscount;
    private Long finalPrice;

    public CampaignProductXml() {}
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public Long getPrice() { return price; }
    public void setPrice(Long price) { this.price = price; }
    public Long getComputedDiscount() { return computedDiscount; }
    public void setComputedDiscount(Long computedDiscount) { this.computedDiscount = computedDiscount; }
    public Long getFinalPrice() { return finalPrice; }
    public void setFinalPrice(Long finalPrice) { this.finalPrice = finalPrice; }
}
