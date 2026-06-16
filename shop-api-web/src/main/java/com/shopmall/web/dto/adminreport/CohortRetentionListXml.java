package com.shopmall.web.dto.adminreport;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "cohortRetention")
@XmlAccessorType(XmlAccessType.FIELD)
public class CohortRetentionListXml {
    @XmlElement(name = "row")
    private List<CohortRetentionXml> rows = new ArrayList<CohortRetentionXml>();

    public CohortRetentionListXml() {}
    public CohortRetentionListXml(List<CohortRetentionXml> rows) { this.rows = rows; }
    public List<CohortRetentionXml> getRows() { return rows; }
    public void setRows(List<CohortRetentionXml> rows) { this.rows = rows; }
}
