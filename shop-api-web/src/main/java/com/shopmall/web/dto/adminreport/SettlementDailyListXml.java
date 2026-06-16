package com.shopmall.web.dto.adminreport;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "settlementDaily")
@XmlAccessorType(XmlAccessType.FIELD)
public class SettlementDailyListXml {
    @XmlElement(name = "row")
    private List<SettlementDailyXml> rows = new ArrayList<SettlementDailyXml>();

    public SettlementDailyListXml() {}
    public SettlementDailyListXml(List<SettlementDailyXml> rows) { this.rows = rows; }
    public List<SettlementDailyXml> getRows() { return rows; }
    public void setRows(List<SettlementDailyXml> rows) { this.rows = rows; }
}
