package com.shopmall.web.dto.product;

import com.shopmall.common.vo.ProductVO;
import com.shopmall.common.vo.ProductOptionVO;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "productDetail")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProductDetailXml {
    @XmlElement(name = "product")
    private ProductVO product;
    @XmlElementWrapper(name = "options")
    @XmlElement(name = "option")
    private List<ProductOptionVO> options = new ArrayList<ProductOptionVO>();

    public ProductDetailXml() {}
    public ProductVO getProduct() { return product; }
    public void setProduct(ProductVO product) { this.product = product; }
    public List<ProductOptionVO> getOptions() { return options; }
    public void setOptions(List<ProductOptionVO> options) { this.options = options; }
}
