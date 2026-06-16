package com.shopmall.web.dto.review;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "weightedScore")
@XmlAccessorType(XmlAccessType.FIELD)
public class WeightedScoreXml {
    private Long productId;
    private Long reviewCnt;
    private Long weightNum;
    private Long weightDen;
    private Long weightedScoreX100;

    public WeightedScoreXml() {}
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public Long getReviewCnt() { return reviewCnt; }
    public void setReviewCnt(Long reviewCnt) { this.reviewCnt = reviewCnt; }
    public Long getWeightNum() { return weightNum; }
    public void setWeightNum(Long weightNum) { this.weightNum = weightNum; }
    public Long getWeightDen() { return weightDen; }
    public void setWeightDen(Long weightDen) { this.weightDen = weightDen; }
    public Long getWeightedScoreX100() { return weightedScoreX100; }
    public void setWeightedScoreX100(Long weightedScoreX100) { this.weightedScoreX100 = weightedScoreX100; }
}
