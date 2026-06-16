package com.shopmall.web.dto.adminreport;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class SettlementDailyXml {
    private String sellerName;
    private String settleDay;
    private Long grossAmount;
    private Long commission;
    private Long payout;

    public SettlementDailyXml() {}
    public String getSellerName() { return sellerName; }
    public void setSellerName(String sellerName) { this.sellerName = sellerName; }
    public String getSettleDay() { return settleDay; }
    public void setSettleDay(String settleDay) { this.settleDay = settleDay; }
    public Long getGrossAmount() { return grossAmount; }
    public void setGrossAmount(Long grossAmount) { this.grossAmount = grossAmount; }
    public Long getCommission() { return commission; }
    public void setCommission(Long commission) { this.commission = commission; }
    public Long getPayout() { return payout; }
    public void setPayout(Long payout) { this.payout = payout; }
}
