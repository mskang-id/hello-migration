package com.shopmall.web.dto.order;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "orderHistory")
@XmlAccessorType(XmlAccessType.FIELD)
public class OrderHistoryXml {
    private Long memberId;
    private String loyaltyTier;
    private Integer page;
    private Integer size;
    private Integer total;
    @XmlElementWrapper(name = "orders")
    @XmlElement(name = "order")
    private List<OrderHistoryRowXml> rows = new ArrayList<OrderHistoryRowXml>();

    public OrderHistoryXml() {}
    public Long getMemberId() { return memberId; }
    public void setMemberId(Long memberId) { this.memberId = memberId; }
    public String getLoyaltyTier() { return loyaltyTier; }
    public void setLoyaltyTier(String loyaltyTier) { this.loyaltyTier = loyaltyTier; }
    public Integer getPage() { return page; }
    public void setPage(Integer page) { this.page = page; }
    public Integer getSize() { return size; }
    public void setSize(Integer size) { this.size = size; }
    public Integer getTotal() { return total; }
    public void setTotal(Integer total) { this.total = total; }
    public List<OrderHistoryRowXml> getRows() { return rows; }
    public void setRows(List<OrderHistoryRowXml> rows) { this.rows = rows; }
}
