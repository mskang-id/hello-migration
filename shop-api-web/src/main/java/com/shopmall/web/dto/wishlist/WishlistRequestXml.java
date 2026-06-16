package com.shopmall.web.dto.wishlist;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "wishlistRequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class WishlistRequestXml {
    private Long memberId;
    private Long productId;

    public WishlistRequestXml() {}
    public Long getMemberId() { return memberId; }
    public void setMemberId(Long memberId) { this.memberId = memberId; }
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
}
