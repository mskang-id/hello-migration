package com.shopmall.web.dto.adminreport;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "ratingDistribution")
@XmlAccessorType(XmlAccessType.FIELD)
public class RatingDistributionListXml {
    @XmlElement(name = "row")
    private List<RatingDistributionXml> rows = new ArrayList<RatingDistributionXml>();

    public RatingDistributionListXml() {}
    public RatingDistributionListXml(List<RatingDistributionXml> rows) { this.rows = rows; }
    public List<RatingDistributionXml> getRows() { return rows; }
    public void setRows(List<RatingDistributionXml> rows) { this.rows = rows; }
}
