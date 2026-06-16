package com.shopmall.web.dto.review;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "verifiedReview")
@XmlAccessorType(XmlAccessType.FIELD)
public class VerifiedReviewXml {
    private Long reviewId;
    private Long productId;
    private Long memberId;
    private Integer rating;
    private String title;
    private String body;
    private String regDate;
    private Long helpfulCount;
    private String purchaseState;

    public VerifiedReviewXml() {}
    public Long getReviewId() { return reviewId; }
    public void setReviewId(Long reviewId) { this.reviewId = reviewId; }
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public Long getMemberId() { return memberId; }
    public void setMemberId(Long memberId) { this.memberId = memberId; }
    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }
    public String getRegDate() { return regDate; }
    public void setRegDate(String regDate) { this.regDate = regDate; }
    public Long getHelpfulCount() { return helpfulCount; }
    public void setHelpfulCount(Long helpfulCount) { this.helpfulCount = helpfulCount; }
    public String getPurchaseState() { return purchaseState; }
    public void setPurchaseState(String purchaseState) { this.purchaseState = purchaseState; }
}
