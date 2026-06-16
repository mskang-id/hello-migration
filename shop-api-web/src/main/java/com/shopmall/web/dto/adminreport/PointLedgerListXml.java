package com.shopmall.web.dto.adminreport;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "pointLedger")
@XmlAccessorType(XmlAccessType.FIELD)
public class PointLedgerListXml {
    @XmlElement(name = "row")
    private List<PointLedgerXml> rows = new ArrayList<PointLedgerXml>();

    public PointLedgerListXml() {}
    public PointLedgerListXml(List<PointLedgerXml> rows) { this.rows = rows; }
    public List<PointLedgerXml> getRows() { return rows; }
    public void setRows(List<PointLedgerXml> rows) { this.rows = rows; }
}
