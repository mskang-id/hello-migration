package com.shopmall.web.dto.adminreport;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "inventoryTurnover")
@XmlAccessorType(XmlAccessType.FIELD)
public class InventoryTurnoverListXml {
    @XmlElement(name = "row")
    private List<InventoryTurnoverXml> rows = new ArrayList<InventoryTurnoverXml>();

    public InventoryTurnoverListXml() {}
    public InventoryTurnoverListXml(List<InventoryTurnoverXml> rows) { this.rows = rows; }
    public List<InventoryTurnoverXml> getRows() { return rows; }
    public void setRows(List<InventoryTurnoverXml> rows) { this.rows = rows; }
}
