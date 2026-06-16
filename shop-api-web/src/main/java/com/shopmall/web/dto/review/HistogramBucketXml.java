package com.shopmall.web.dto.review;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "histogramBucket")
@XmlAccessorType(XmlAccessType.FIELD)
public class HistogramBucketXml {
    private Integer star;
    private Long cnt;

    public HistogramBucketXml() {}
    public Integer getStar() { return star; }
    public void setStar(Integer star) { this.star = star; }
    public Long getCnt() { return cnt; }
    public void setCnt(Long cnt) { this.cnt = cnt; }
}
