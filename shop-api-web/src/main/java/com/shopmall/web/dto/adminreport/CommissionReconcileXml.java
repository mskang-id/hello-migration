package com.shopmall.web.dto.adminreport;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class CommissionReconcileXml {
    private String category;
    private Long grossAmount;
    private Long commissionRate;
    private Long commission;
    private Long payout;
    private Long vat;

    public CommissionReconcileXml() {}
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public Long getGrossAmount() { return grossAmount; }
    public void setGrossAmount(Long grossAmount) { this.grossAmount = grossAmount; }
    public Long getCommissionRate() { return commissionRate; }
    public void setCommissionRate(Long commissionRate) { this.commissionRate = commissionRate; }
    public Long getCommission() { return commission; }
    public void setCommission(Long commission) { this.commission = commission; }
    public Long getPayout() { return payout; }
    public void setPayout(Long payout) { this.payout = payout; }
    public Long getVat() { return vat; }
    public void setVat(Long vat) { this.vat = vat; }
}
