package com.shopmall.web.dto.adminreport;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "couponUsage")
@XmlAccessorType(XmlAccessType.FIELD)
public class CouponUsageListXml {
    @XmlElement(name = "row")
    private List<CouponUsageXml> rows = new ArrayList<CouponUsageXml>();

    public CouponUsageListXml() {}
    public CouponUsageListXml(List<CouponUsageXml> rows) { this.rows = rows; }
    public List<CouponUsageXml> getRows() { return rows; }
    public void setRows(List<CouponUsageXml> rows) { this.rows = rows; }
}
