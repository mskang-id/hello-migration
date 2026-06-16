package com.shopmall.web.dto.review;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "topHelpful")
@XmlAccessorType(XmlAccessType.FIELD)
public class HelpfulReviewListXml {
    private Long productId;
    @XmlElement(name = "review")
    private List<HelpfulReviewXml> rows = new ArrayList<HelpfulReviewXml>();

    public HelpfulReviewListXml() {}
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public List<HelpfulReviewXml> getRows() { return rows; }
    public void setRows(List<HelpfulReviewXml> rows) { this.rows = rows; }
}
