package com.shopmall.web.dto.product;

import com.shopmall.common.vo.ProductVO;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "products")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProductListXml {
    @XmlElement(name = "product")
    private List<ProductVO> products = new ArrayList<ProductVO>();

    public ProductListXml() {}
    public ProductListXml(List<ProductVO> products) { this.products = products; }
    public List<ProductVO> getProducts() { return products; }
    public void setProducts(List<ProductVO> products) { this.products = products; }
}
