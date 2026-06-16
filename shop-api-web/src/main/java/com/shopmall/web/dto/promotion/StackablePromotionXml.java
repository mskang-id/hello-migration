package com.shopmall.web.dto.promotion;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "stackablePromotion")
@XmlAccessorType(XmlAccessType.FIELD)
public class StackablePromotionXml {
    private Long promotionId;
    private String name;
    private String discountType;
    private Long discountVal;
    private Long computedDiscount;
    private String windowState;

    public StackablePromotionXml() {}
    public Long getPromotionId() { return promotionId; }
    public void setPromotionId(Long promotionId) { this.promotionId = promotionId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDiscountType() { return discountType; }
    public void setDiscountType(String discountType) { this.discountType = discountType; }
    public Long getDiscountVal() { return discountVal; }
    public void setDiscountVal(Long discountVal) { this.discountVal = discountVal; }
    public Long getComputedDiscount() { return computedDiscount; }
    public void setComputedDiscount(Long computedDiscount) { this.computedDiscount = computedDiscount; }
    public String getWindowState() { return windowState; }
    public void setWindowState(String windowState) { this.windowState = windowState; }
}
