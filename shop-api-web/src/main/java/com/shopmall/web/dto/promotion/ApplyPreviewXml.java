package com.shopmall.web.dto.promotion;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "applyPreview")
@XmlAccessorType(XmlAccessType.FIELD)
public class ApplyPreviewXml {
    private Long promotionId;
    @XmlElementWrapper(name = "products")
    @XmlElement(name = "product")
    private List<CampaignProductXml> products = new ArrayList<CampaignProductXml>();
    private Long totalDiscount;

    public ApplyPreviewXml() {}
    public Long getPromotionId() { return promotionId; }
    public void setPromotionId(Long promotionId) { this.promotionId = promotionId; }
    public List<CampaignProductXml> getProducts() { return products; }
    public void setProducts(List<CampaignProductXml> products) { this.products = products; }
    public Long getTotalDiscount() { return totalDiscount; }
    public void setTotalDiscount(Long totalDiscount) { this.totalDiscount = totalDiscount; }
}
