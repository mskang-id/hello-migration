package com.shopmall.web.dto.product;

import com.shopmall.common.vo.ProductVO;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "productPage")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProductPageXml {
    private Integer page;
    private Integer size;
    private Integer total;
    private Integer totalPages;
    @XmlElementWrapper(name = "products")
    @XmlElement(name = "product")
    private List<ProductVO> products = new ArrayList<ProductVO>();

    public ProductPageXml() {}
    public Integer getPage() { return page; }
    public void setPage(Integer page) { this.page = page; }
    public Integer getSize() { return size; }
    public void setSize(Integer size) { this.size = size; }
    public Integer getTotal() { return total; }
    public void setTotal(Integer total) { this.total = total; }
    public Integer getTotalPages() { return totalPages; }
    public void setTotalPages(Integer totalPages) { this.totalPages = totalPages; }
    public List<ProductVO> getProducts() { return products; }
    public void setProducts(List<ProductVO> products) { this.products = products; }
}
