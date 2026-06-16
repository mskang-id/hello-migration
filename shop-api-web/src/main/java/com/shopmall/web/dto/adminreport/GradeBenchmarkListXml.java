package com.shopmall.web.dto.adminreport;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "gradeBenchmark")
@XmlAccessorType(XmlAccessType.FIELD)
public class GradeBenchmarkListXml {
    @XmlElement(name = "row")
    private List<GradeBenchmarkXml> rows = new ArrayList<GradeBenchmarkXml>();

    public GradeBenchmarkListXml() {}
    public GradeBenchmarkListXml(List<GradeBenchmarkXml> rows) { this.rows = rows; }
    public List<GradeBenchmarkXml> getRows() { return rows; }
    public void setRows(List<GradeBenchmarkXml> rows) { this.rows = rows; }
}
