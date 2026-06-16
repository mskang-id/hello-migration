package com.shopmall.web.dto.promotion;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "expiringPromotion")
@XmlAccessorType(XmlAccessType.FIELD)
public class ExpiringPromotionXml {
    private Long promotionId;
    private String name;
    private String discountType;
    private Long discountVal;
    private String startDate;
    private String endDate;
    private String status;
    private String lifecycle;

    public ExpiringPromotionXml() {}
    public Long getPromotionId() { return promotionId; }
    public void setPromotionId(Long promotionId) { this.promotionId = promotionId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDiscountType() { return discountType; }
    public void setDiscountType(String discountType) { this.discountType = discountType; }
    public Long getDiscountVal() { return discountVal; }
    public void setDiscountVal(Long discountVal) { this.discountVal = discountVal; }
    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }
    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getLifecycle() { return lifecycle; }
    public void setLifecycle(String lifecycle) { this.lifecycle = lifecycle; }
}
