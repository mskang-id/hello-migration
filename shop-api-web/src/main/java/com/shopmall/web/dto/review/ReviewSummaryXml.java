package com.shopmall.web.dto.review;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class ReviewSummaryXml {
    private Long productId;
    private Long reviewCnt;
    private Long avgRating;
    private Long positiveCnt;

    public ReviewSummaryXml() {}
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public Long getReviewCnt() { return reviewCnt; }
    public void setReviewCnt(Long reviewCnt) { this.reviewCnt = reviewCnt; }
    public Long getAvgRating() { return avgRating; }
    public void setAvgRating(Long avgRating) { this.avgRating = avgRating; }
    public Long getPositiveCnt() { return positiveCnt; }
    public void setPositiveCnt(Long positiveCnt) { this.positiveCnt = positiveCnt; }
}
