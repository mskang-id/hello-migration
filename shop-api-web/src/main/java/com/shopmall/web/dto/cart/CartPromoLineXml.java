package com.shopmall.web.dto.cart;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "cartPromoLine")
@XmlAccessorType(XmlAccessType.FIELD)
public class CartPromoLineXml {
    private Long cartItemId;
    private Long cartId;
    private Long optionId;
    private Integer qty;
    private String optionName;
    private Long unitPrice;
    private Long lineTotal;
    private String productName;
    private Long promoDiscount;

    public CartPromoLineXml() {}
    public Long getCartItemId() { return cartItemId; }
    public void setCartItemId(Long cartItemId) { this.cartItemId = cartItemId; }
    public Long getCartId() { return cartId; }
    public void setCartId(Long cartId) { this.cartId = cartId; }
    public Long getOptionId() { return optionId; }
    public void setOptionId(Long optionId) { this.optionId = optionId; }
    public Integer getQty() { return qty; }
    public void setQty(Integer qty) { this.qty = qty; }
    public String getOptionName() { return optionName; }
    public void setOptionName(String optionName) { this.optionName = optionName; }
    public Long getUnitPrice() { return unitPrice; }
    public void setUnitPrice(Long unitPrice) { this.unitPrice = unitPrice; }
    public Long getLineTotal() { return lineTotal; }
    public void setLineTotal(Long lineTotal) { this.lineTotal = lineTotal; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public Long getPromoDiscount() { return promoDiscount; }
    public void setPromoDiscount(Long promoDiscount) { this.promoDiscount = promoDiscount; }
}
