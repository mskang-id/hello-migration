package com.shopmall.web.dto.adminreport;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "bigSpenders")
@XmlAccessorType(XmlAccessType.FIELD)
public class BigSpenderListXml {
    @XmlElement(name = "row")
    private List<BigSpenderXml> rows = new ArrayList<BigSpenderXml>();

    public BigSpenderListXml() {}
    public BigSpenderListXml(List<BigSpenderXml> rows) { this.rows = rows; }
    public List<BigSpenderXml> getRows() { return rows; }
    public void setRows(List<BigSpenderXml> rows) { this.rows = rows; }
}
