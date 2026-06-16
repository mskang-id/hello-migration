package com.shopmall.web.dto.promotion;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "promotions")
@XmlAccessorType(XmlAccessType.FIELD)
public class PromotionListXml {
    @XmlElement(name = "promotion")
    private List<PromotionXml> rows = new ArrayList<PromotionXml>();

    public PromotionListXml() {}
    public PromotionListXml(List<PromotionXml> rows) { this.rows = rows; }
    public List<PromotionXml> getRows() { return rows; }
    public void setRows(List<PromotionXml> rows) { this.rows = rows; }
}
