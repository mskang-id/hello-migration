package com.shopmall.web.dto.review;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "verifiedReviews")
@XmlAccessorType(XmlAccessType.FIELD)
public class VerifiedReviewListXml {
    private Long total;
    private Long verifiedCount;
    @XmlElementWrapper(name = "rows")
    @XmlElement(name = "review")
    private List<VerifiedReviewXml> rows = new ArrayList<VerifiedReviewXml>();

    public VerifiedReviewListXml() {}
    public Long getTotal() { return total; }
    public void setTotal(Long total) { this.total = total; }
    public Long getVerifiedCount() { return verifiedCount; }
    public void setVerifiedCount(Long verifiedCount) { this.verifiedCount = verifiedCount; }
    public List<VerifiedReviewXml> getRows() { return rows; }
    public void setRows(List<VerifiedReviewXml> rows) { this.rows = rows; }
}
