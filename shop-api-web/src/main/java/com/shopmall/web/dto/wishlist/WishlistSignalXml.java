package com.shopmall.web.dto.wishlist;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "wishlistSignal")
@XmlAccessorType(XmlAccessType.FIELD)
public class WishlistSignalXml {
    private Long wishlistId;
    private Long memberId;
    private Long productId;
    private String productName;
    private Long price;
    private String status;
    private Long totalStock;
    private String stockSignal;
    private String displayLabel;

    public WishlistSignalXml() {}
    public Long getWishlistId() { return wishlistId; }
    public void setWishlistId(Long wishlistId) { this.wishlistId = wishlistId; }
    public Long getMemberId() { return memberId; }
    public void setMemberId(Long memberId) { this.memberId = memberId; }
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public Long getPrice() { return price; }
    public void setPrice(Long price) { this.price = price; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Long getTotalStock() { return totalStock; }
    public void setTotalStock(Long totalStock) { this.totalStock = totalStock; }
    public String getStockSignal() { return stockSignal; }
    public void setStockSignal(String stockSignal) { this.stockSignal = stockSignal; }
    public String getDisplayLabel() { return displayLabel; }
    public void setDisplayLabel(String displayLabel) { this.displayLabel = displayLabel; }
}
