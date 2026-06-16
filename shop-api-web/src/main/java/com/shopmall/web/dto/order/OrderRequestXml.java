package com.shopmall.web.dto.order;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "orderRequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class OrderRequestXml {
    private Long memberId;
    @XmlElementWrapper(name = "items")
    @XmlElement(name = "item")
    private List<OrderItemXml> items = new ArrayList<OrderItemXml>();
    private Long couponId;
    private Integer usePoint;
    private String payMethod;
    private String zipcode;
    private String address;

    public Long getMemberId() { return memberId; }
    public void setMemberId(Long memberId) { this.memberId = memberId; }
    public List<OrderItemXml> getItems() { return items; }
    public void setItems(List<OrderItemXml> items) { this.items = items; }
    public Long getCouponId() { return couponId; }
    public void setCouponId(Long couponId) { this.couponId = couponId; }
    public Integer getUsePoint() { return usePoint; }
    public void setUsePoint(Integer usePoint) { this.usePoint = usePoint; }
    public String getPayMethod() { return payMethod; }
    public void setPayMethod(String payMethod) { this.payMethod = payMethod; }
    public String getZipcode() { return zipcode; }
    public void setZipcode(String zipcode) { this.zipcode = zipcode; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}
