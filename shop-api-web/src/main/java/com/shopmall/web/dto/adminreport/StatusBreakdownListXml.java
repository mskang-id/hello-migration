package com.shopmall.web.dto.adminreport;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "statusBreakdown")
@XmlAccessorType(XmlAccessType.FIELD)
public class StatusBreakdownListXml {
    @XmlElement(name = "row")
    private List<StatusBreakdownXml> rows = new ArrayList<StatusBreakdownXml>();

    public StatusBreakdownListXml() {}
    public StatusBreakdownListXml(List<StatusBreakdownXml> rows) { this.rows = rows; }
    public List<StatusBreakdownXml> getRows() { return rows; }
    public void setRows(List<StatusBreakdownXml> rows) { this.rows = rows; }
}
