package com.shopmall.web.dto.cart;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "cartPromoView")
@XmlAccessorType(XmlAccessType.FIELD)
public class CartPromoViewXml {
    private Long cartId;
    @XmlElementWrapper(name = "items")
    @XmlElement(name = "item")
    private List<CartPromoLineXml> items = new ArrayList<CartPromoLineXml>();
    private Long cartTotal;
    private Long discountedTotal;
    private Integer itemsOnPromo;

    public CartPromoViewXml() {}
    public Long getCartId() { return cartId; }
    public void setCartId(Long cartId) { this.cartId = cartId; }
    public List<CartPromoLineXml> getItems() { return items; }
    public void setItems(List<CartPromoLineXml> items) { this.items = items; }
    public Long getCartTotal() { return cartTotal; }
    public void setCartTotal(Long cartTotal) { this.cartTotal = cartTotal; }
    public Long getDiscountedTotal() { return discountedTotal; }
    public void setDiscountedTotal(Long discountedTotal) { this.discountedTotal = discountedTotal; }
    public Integer getItemsOnPromo() { return itemsOnPromo; }
    public void setItemsOnPromo(Integer itemsOnPromo) { this.itemsOnPromo = itemsOnPromo; }
}
