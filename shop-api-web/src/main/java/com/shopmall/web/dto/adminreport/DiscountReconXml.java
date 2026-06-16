package com.shopmall.web.dto.adminreport;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class DiscountReconXml {
    private String source;
    private String discountType;
    private Long defCnt;
    private Long openCnt;
    private Long totalFlatOrRate;
    private String redemptionState;

    public DiscountReconXml() {}
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    public String getDiscountType() { return discountType; }
    public void setDiscountType(String discountType) { this.discountType = discountType; }
    public Long getDefCnt() { return defCnt; }
    public void setDefCnt(Long defCnt) { this.defCnt = defCnt; }
    public Long getOpenCnt() { return openCnt; }
    public void setOpenCnt(Long openCnt) { this.openCnt = openCnt; }
    public Long getTotalFlatOrRate() { return totalFlatOrRate; }
    public void setTotalFlatOrRate(Long totalFlatOrRate) { this.totalFlatOrRate = totalFlatOrRate; }
    public String getRedemptionState() { return redemptionState; }
    public void setRedemptionState(String redemptionState) { this.redemptionState = redemptionState; }
}
