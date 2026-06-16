package com.shopmall.web.dto.adminreport;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class RatingDistributionXml {
    private Long productId;
    private String name;
    private Long reviewCnt;
    private Long star5;
    private Long star4;
    private Long star3;
    private Long star2;
    private Long star1;
    private Long avgRating;
    private String sentiment;

    public RatingDistributionXml() {}
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Long getReviewCnt() { return reviewCnt; }
    public void setReviewCnt(Long reviewCnt) { this.reviewCnt = reviewCnt; }
    public Long getStar5() { return star5; }
    public void setStar5(Long star5) { this.star5 = star5; }
    public Long getStar4() { return star4; }
    public void setStar4(Long star4) { this.star4 = star4; }
    public Long getStar3() { return star3; }
    public void setStar3(Long star3) { this.star3 = star3; }
    public Long getStar2() { return star2; }
    public void setStar2(Long star2) { this.star2 = star2; }
    public Long getStar1() { return star1; }
    public void setStar1(Long star1) { this.star1 = star1; }
    public Long getAvgRating() { return avgRating; }
    public void setAvgRating(Long avgRating) { this.avgRating = avgRating; }
    public String getSentiment() { return sentiment; }
    public void setSentiment(String sentiment) { this.sentiment = sentiment; }
}
