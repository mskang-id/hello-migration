package com.shopmall.web.dto.cart;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "cartView")
@XmlAccessorType(XmlAccessType.FIELD)
public class CartViewXml {
    private Long cartId;
    @XmlElementWrapper(name = "items")
    @XmlElement(name = "item")
    private List<CartLineXml> items = new ArrayList<CartLineXml>();
    private Long cartTotal;

    public CartViewXml() {}
    public Long getCartId() { return cartId; }
    public void setCartId(Long cartId) { this.cartId = cartId; }
    public List<CartLineXml> getItems() { return items; }
    public void setItems(List<CartLineXml> items) { this.items = items; }
    public Long getCartTotal() { return cartTotal; }
    public void setCartTotal(Long cartTotal) { this.cartTotal = cartTotal; }
}
