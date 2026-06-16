package com.shopmall.web.dto.cart;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "cartLineRequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class CartLineRequestXml {
    private Long optionId;
    private Integer qty;

    public CartLineRequestXml() {}
    public Long getOptionId() { return optionId; }
    public void setOptionId(Long optionId) { this.optionId = optionId; }
    public Integer getQty() { return qty; }
    public void setQty(Integer qty) { this.qty = qty; }
}
