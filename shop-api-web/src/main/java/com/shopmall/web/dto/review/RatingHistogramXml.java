package com.shopmall.web.dto.review;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "ratingHistogram")
@XmlAccessorType(XmlAccessType.FIELD)
public class RatingHistogramXml {
    private Long productId;
    @XmlElement(name = "bucket")
    private List<HistogramBucketXml> buckets = new ArrayList<HistogramBucketXml>();

    public RatingHistogramXml() {}
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public List<HistogramBucketXml> getBuckets() { return buckets; }
    public void setBuckets(List<HistogramBucketXml> buckets) { this.buckets = buckets; }
}
