package com.shopmall.web.dto.wishlist;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "wishlist")
@XmlAccessorType(XmlAccessType.FIELD)
public class WishlistXml {
    private Long memberId;
    @XmlElementWrapper(name = "items")
    @XmlElement(name = "item")
    private List<WishlistItemXml> items = new ArrayList<WishlistItemXml>();
    private Integer totalCount;
    private Integer onSaleCount;

    public WishlistXml() {}
    public Long getMemberId() { return memberId; }
    public void setMemberId(Long memberId) { this.memberId = memberId; }
    public List<WishlistItemXml> getItems() { return items; }
    public void setItems(List<WishlistItemXml> items) { this.items = items; }
    public Integer getTotalCount() { return totalCount; }
    public void setTotalCount(Integer totalCount) { this.totalCount = totalCount; }
    public Integer getOnSaleCount() { return onSaleCount; }
    public void setOnSaleCount(Integer onSaleCount) { this.onSaleCount = onSaleCount; }
}
