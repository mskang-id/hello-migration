package com.shopmall.web.dto.report;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "settlements")
@XmlAccessorType(XmlAccessType.FIELD)
public class SettlementListXml {
    @XmlElement(name = "settlement")
    private List<SettlementXml> rows = new ArrayList<SettlementXml>();

    public SettlementListXml() {}
    public SettlementListXml(List<SettlementXml> rows) { this.rows = rows; }
    public List<SettlementXml> getRows() { return rows; }
    public void setRows(List<SettlementXml> rows) { this.rows = rows; }
}
