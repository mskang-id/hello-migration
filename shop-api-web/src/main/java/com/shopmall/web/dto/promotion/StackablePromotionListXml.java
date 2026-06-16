package com.shopmall.web.dto.promotion;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "stackablePromotions")
@XmlAccessorType(XmlAccessType.FIELD)
public class StackablePromotionListXml {
    private Long productId;
    private Long bestPromotionId;
    private Long bestComputedDiscount;
    private Long totalStackedDiscount;
    @XmlElementWrapper(name = "promotions")
    @XmlElement(name = "promotion")
    private List<StackablePromotionXml> promotions = new ArrayList<StackablePromotionXml>();

    public StackablePromotionListXml() {}
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public Long getBestPromotionId() { return bestPromotionId; }
    public void setBestPromotionId(Long bestPromotionId) { this.bestPromotionId = bestPromotionId; }
    public Long getBestComputedDiscount() { return bestComputedDiscount; }
    public void setBestComputedDiscount(Long bestComputedDiscount) { this.bestComputedDiscount = bestComputedDiscount; }
    public Long getTotalStackedDiscount() { return totalStackedDiscount; }
    public void setTotalStackedDiscount(Long totalStackedDiscount) { this.totalStackedDiscount = totalStackedDiscount; }
    public List<StackablePromotionXml> getPromotions() { return promotions; }
    public void setPromotions(List<StackablePromotionXml> promotions) { this.promotions = promotions; }
}
