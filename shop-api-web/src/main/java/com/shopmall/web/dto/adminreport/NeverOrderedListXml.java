package com.shopmall.web.dto.adminreport;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "neverOrdered")
@XmlAccessorType(XmlAccessType.FIELD)
public class NeverOrderedListXml {
    @XmlElement(name = "row")
    private List<NeverOrderedXml> rows = new ArrayList<NeverOrderedXml>();

    public NeverOrderedListXml() {}
    public NeverOrderedListXml(List<NeverOrderedXml> rows) { this.rows = rows; }
    public List<NeverOrderedXml> getRows() { return rows; }
    public void setRows(List<NeverOrderedXml> rows) { this.rows = rows; }
}
