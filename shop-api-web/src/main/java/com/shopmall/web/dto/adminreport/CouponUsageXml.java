package com.shopmall.web.dto.adminreport;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class CouponUsageXml {
    private String discountType;
    private Long totalCoupons;
    private Long usedCnt;
    private Long useRatePct;
    private Long avgDiscountVal;
    private String effectiveness;

    public CouponUsageXml() {}
    public String getDiscountType() { return discountType; }
    public void setDiscountType(String discountType) { this.discountType = discountType; }
    public Long getTotalCoupons() { return totalCoupons; }
    public void setTotalCoupons(Long totalCoupons) { this.totalCoupons = totalCoupons; }
    public Long getUsedCnt() { return usedCnt; }
    public void setUsedCnt(Long usedCnt) { this.usedCnt = usedCnt; }
    public Long getUseRatePct() { return useRatePct; }
    public void setUseRatePct(Long useRatePct) { this.useRatePct = useRatePct; }
    public Long getAvgDiscountVal() { return avgDiscountVal; }
    public void setAvgDiscountVal(Long avgDiscountVal) { this.avgDiscountVal = avgDiscountVal; }
    public String getEffectiveness() { return effectiveness; }
    public void setEffectiveness(String effectiveness) { this.effectiveness = effectiveness; }
}
