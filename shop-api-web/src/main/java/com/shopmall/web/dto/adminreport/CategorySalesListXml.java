package com.shopmall.web.dto.adminreport;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "categorySales")
@XmlAccessorType(XmlAccessType.FIELD)
public class CategorySalesListXml {
    @XmlElement(name = "row")
    private List<CategorySalesXml> rows = new ArrayList<CategorySalesXml>();

    public CategorySalesListXml() {}
    public CategorySalesListXml(List<CategorySalesXml> rows) { this.rows = rows; }
    public List<CategorySalesXml> getRows() { return rows; }
    public void setRows(List<CategorySalesXml> rows) { this.rows = rows; }
}
