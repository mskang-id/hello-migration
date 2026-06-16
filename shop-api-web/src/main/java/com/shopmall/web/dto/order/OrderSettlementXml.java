package com.shopmall.web.dto.order;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "settlement")
@XmlAccessorType(XmlAccessType.FIELD)
public class OrderSettlementXml {
    private Long orderId;
    private Integer itemTotal;
    private Integer discount;
    private Integer usePoint;
    private Integer payAmount;
    private Integer shippingFee;
    private Integer vat;
    private Integer grandTotal;
    private String regDate;

    public OrderSettlementXml() {}
    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public Integer getItemTotal() { return itemTotal; }
    public void setItemTotal(Integer itemTotal) { this.itemTotal = itemTotal; }
    public Integer getDiscount() { return discount; }
    public void setDiscount(Integer discount) { this.discount = discount; }
    public Integer getUsePoint() { return usePoint; }
    public void setUsePoint(Integer usePoint) { this.usePoint = usePoint; }
    public Integer getPayAmount() { return payAmount; }
    public void setPayAmount(Integer payAmount) { this.payAmount = payAmount; }
    public Integer getShippingFee() { return shippingFee; }
    public void setShippingFee(Integer shippingFee) { this.shippingFee = shippingFee; }
    public Integer getVat() { return vat; }
    public void setVat(Integer vat) { this.vat = vat; }
    public Integer getGrandTotal() { return grandTotal; }
    public void setGrandTotal(Integer grandTotal) { this.grandTotal = grandTotal; }
    public String getRegDate() { return regDate; }
    public void setRegDate(String regDate) { this.regDate = regDate; }
}
