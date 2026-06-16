package com.shopmall.web.dto.adminreport;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "revenueShare")
@XmlAccessorType(XmlAccessType.FIELD)
public class RevenueShareListXml {
    @XmlElement(name = "row")
    private List<RevenueShareXml> rows = new ArrayList<RevenueShareXml>();

    public RevenueShareListXml() {}
    public RevenueShareListXml(List<RevenueShareXml> rows) { this.rows = rows; }
    public List<RevenueShareXml> getRows() { return rows; }
    public void setRows(List<RevenueShareXml> rows) { this.rows = rows; }
}
