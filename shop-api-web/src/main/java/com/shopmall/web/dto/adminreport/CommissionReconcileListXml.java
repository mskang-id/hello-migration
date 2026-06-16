package com.shopmall.web.dto.adminreport;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "commissionReconcile")
@XmlAccessorType(XmlAccessType.FIELD)
public class CommissionReconcileListXml {
    @XmlElement(name = "row")
    private List<CommissionReconcileXml> rows = new ArrayList<CommissionReconcileXml>();

    public CommissionReconcileListXml() {}
    public CommissionReconcileListXml(List<CommissionReconcileXml> rows) { this.rows = rows; }
    public List<CommissionReconcileXml> getRows() { return rows; }
    public void setRows(List<CommissionReconcileXml> rows) { this.rows = rows; }
}
