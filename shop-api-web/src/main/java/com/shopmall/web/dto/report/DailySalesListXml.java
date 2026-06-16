package com.shopmall.web.dto.report;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "dailySales")
@XmlAccessorType(XmlAccessType.FIELD)
public class DailySalesListXml {
    @XmlElement(name = "day")
    private List<DailySalesXml> rows = new ArrayList<DailySalesXml>();

    public DailySalesListXml() {}
    public DailySalesListXml(List<DailySalesXml> rows) { this.rows = rows; }
    public List<DailySalesXml> getRows() { return rows; }
    public void setRows(List<DailySalesXml> rows) { this.rows = rows; }
}
