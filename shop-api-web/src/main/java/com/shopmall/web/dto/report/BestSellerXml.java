package com.shopmall.web.dto.report;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class BestSellerXml {
    private Long productId;
    private String name;
    private String category;
    private Long totalQty;
    private Long totalAmount;

    public BestSellerXml() {}
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public Long getTotalQty() { return totalQty; }
    public void setTotalQty(Long totalQty) { this.totalQty = totalQty; }
    public Long getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Long totalAmount) { this.totalAmount = totalAmount; }
}
