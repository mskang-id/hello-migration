package com.shopmall.web.dto.wishlist;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "wishlistSignals")
@XmlAccessorType(XmlAccessType.FIELD)
public class WishlistSignalListXml {
    private Long memberId;
    @XmlElementWrapper(name = "signals")
    @XmlElement(name = "signal")
    private List<WishlistSignalXml> signals = new ArrayList<WishlistSignalXml>();
    @XmlElementWrapper(name = "soldOut")
    @XmlElement(name = "item")
    private List<WishlistItemXml> soldOut = new ArrayList<WishlistItemXml>();
    private Integer backInStockCount;
    private Integer buyNowCount;

    public WishlistSignalListXml() {}
    public Long getMemberId() { return memberId; }
    public void setMemberId(Long memberId) { this.memberId = memberId; }
    public List<WishlistSignalXml> getSignals() { return signals; }
    public void setSignals(List<WishlistSignalXml> signals) { this.signals = signals; }
    public List<WishlistItemXml> getSoldOut() { return soldOut; }
    public void setSoldOut(List<WishlistItemXml> soldOut) { this.soldOut = soldOut; }
    public Integer getBackInStockCount() { return backInStockCount; }
    public void setBackInStockCount(Integer backInStockCount) { this.backInStockCount = backInStockCount; }
    public Integer getBuyNowCount() { return buyNowCount; }
    public void setBuyNowCount(Integer buyNowCount) { this.buyNowCount = buyNowCount; }
}
