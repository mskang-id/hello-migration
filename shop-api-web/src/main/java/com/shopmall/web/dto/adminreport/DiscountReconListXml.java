package com.shopmall.web.dto.adminreport;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "discountReconciliation")
@XmlAccessorType(XmlAccessType.FIELD)
public class DiscountReconListXml {
    @XmlElement(name = "row")
    private List<DiscountReconXml> rows = new ArrayList<DiscountReconXml>();

    public DiscountReconListXml() {}
    public DiscountReconListXml(List<DiscountReconXml> rows) { this.rows = rows; }
    public List<DiscountReconXml> getRows() { return rows; }
    public void setRows(List<DiscountReconXml> rows) { this.rows = rows; }
}
