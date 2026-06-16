package com.shopmall.web.dto.lifecycle;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "cancelRequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class CancelRequestXml {
    private Long orderId;
    private Integer partialAmount;
    private String reason;

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public Integer getPartialAmount() { return partialAmount; }
    public void setPartialAmount(Integer partialAmount) { this.partialAmount = partialAmount; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}
