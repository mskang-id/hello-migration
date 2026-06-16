package com.shopmall.web.dto.report;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "bestSellers")
@XmlAccessorType(XmlAccessType.FIELD)
public class BestSellerListXml {
    @XmlElement(name = "bestSeller")
    private List<BestSellerXml> rows = new ArrayList<BestSellerXml>();

    public BestSellerListXml() {}
    public BestSellerListXml(List<BestSellerXml> rows) { this.rows = rows; }
    public List<BestSellerXml> getRows() { return rows; }
    public void setRows(List<BestSellerXml> rows) { this.rows = rows; }
}
