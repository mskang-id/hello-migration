package com.shopmall.web.dto.promotion;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "expiringPromotions")
@XmlAccessorType(XmlAccessType.FIELD)
public class ExpiringPromotionListXml {
    private Integer expiringCount;
    @XmlElement(name = "promotion")
    private List<ExpiringPromotionXml> rows = new ArrayList<ExpiringPromotionXml>();

    public ExpiringPromotionListXml() {}
    public Integer getExpiringCount() { return expiringCount; }
    public void setExpiringCount(Integer expiringCount) { this.expiringCount = expiringCount; }
    public List<ExpiringPromotionXml> getRows() { return rows; }
    public void setRows(List<ExpiringPromotionXml> rows) { this.rows = rows; }
}
