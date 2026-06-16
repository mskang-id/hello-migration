package com.shopmall.web.dto.order;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "quote")
@XmlAccessorType(XmlAccessType.FIELD)
public class OrderQuoteXml {
    private Integer itemsTotal;
    private Integer discount;
    private Integer shippingFee;
    private Integer vat;
    private Integer grandTotal;
    private Integer earnPreview;
    private String grade;

    public OrderQuoteXml() {}
    public Integer getItemsTotal() { return itemsTotal; }
    public void setItemsTotal(Integer itemsTotal) { this.itemsTotal = itemsTotal; }
    public Integer getDiscount() { return discount; }
    public void setDiscount(Integer discount) { this.discount = discount; }
    public Integer getShippingFee() { return shippingFee; }
    public void setShippingFee(Integer shippingFee) { this.shippingFee = shippingFee; }
    public Integer getVat() { return vat; }
    public void setVat(Integer vat) { this.vat = vat; }
    public Integer getGrandTotal() { return grandTotal; }
    public void setGrandTotal(Integer grandTotal) { this.grandTotal = grandTotal; }
    public Integer getEarnPreview() { return earnPreview; }
    public void setEarnPreview(Integer earnPreview) { this.earnPreview = earnPreview; }
    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }
}
