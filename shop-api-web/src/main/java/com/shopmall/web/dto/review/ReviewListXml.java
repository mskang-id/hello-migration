package com.shopmall.web.dto.review;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "reviews")
@XmlAccessorType(XmlAccessType.FIELD)
public class ReviewListXml {
    private Long total;
    private ReviewSummaryXml summary;
    @XmlElementWrapper(name = "rows")
    @XmlElement(name = "review")
    private List<ReviewXml> rows = new ArrayList<ReviewXml>();

    public ReviewListXml() {}
    public Long getTotal() { return total; }
    public void setTotal(Long total) { this.total = total; }
    public ReviewSummaryXml getSummary() { return summary; }
    public void setSummary(ReviewSummaryXml summary) { this.summary = summary; }
    public List<ReviewXml> getRows() { return rows; }
    public void setRows(List<ReviewXml> rows) { this.rows = rows; }
}
