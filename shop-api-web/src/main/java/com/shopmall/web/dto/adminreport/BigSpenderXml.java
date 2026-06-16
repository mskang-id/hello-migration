package com.shopmall.web.dto.adminreport;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class BigSpenderXml {
    private Long memberId;
    private String name;
    private String grade;
    private Long totalSpend;
    private Long orderCnt;

    public BigSpenderXml() {}
    public Long getMemberId() { return memberId; }
    public void setMemberId(Long memberId) { this.memberId = memberId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }
    public Long getTotalSpend() { return totalSpend; }
    public void setTotalSpend(Long totalSpend) { this.totalSpend = totalSpend; }
    public Long getOrderCnt() { return orderCnt; }
    public void setOrderCnt(Long orderCnt) { this.orderCnt = orderCnt; }
}
