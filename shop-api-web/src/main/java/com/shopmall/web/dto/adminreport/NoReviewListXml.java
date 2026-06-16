package com.shopmall.web.dto.adminreport;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "productsWithoutReviews")
@XmlAccessorType(XmlAccessType.FIELD)
public class NoReviewListXml {
    @XmlElement(name = "row")
    private List<NoReviewXml> rows = new ArrayList<NoReviewXml>();

    public NoReviewListXml() {}
    public NoReviewListXml(List<NoReviewXml> rows) { this.rows = rows; }
    public List<NoReviewXml> getRows() { return rows; }
    public void setRows(List<NoReviewXml> rows) { this.rows = rows; }
}
