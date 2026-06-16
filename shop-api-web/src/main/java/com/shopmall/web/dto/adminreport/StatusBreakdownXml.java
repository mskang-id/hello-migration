package com.shopmall.web.dto.adminreport;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class StatusBreakdownXml {
    private String statusLabel;
    private Long orderCnt;
    private Long gross;

    public StatusBreakdownXml() {}
    public String getStatusLabel() { return statusLabel; }
    public void setStatusLabel(String statusLabel) { this.statusLabel = statusLabel; }
    public Long getOrderCnt() { return orderCnt; }
    public void setOrderCnt(Long orderCnt) { this.orderCnt = orderCnt; }
    public Long getGross() { return gross; }
    public void setGross(Long gross) { this.gross = gross; }
}
