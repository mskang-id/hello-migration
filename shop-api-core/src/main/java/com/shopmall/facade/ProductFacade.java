package com.shopmall.facade;

import com.shopmall.common.vo.ProductVO;
import com.shopmall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;

// Product facade. Delegates to ProductService so web only depends on the facade pkg.
@Component
public class ProductFacade {

    @Autowired private ProductService productService;

    public List<ProductVO> getOnSaleProducts() { return productService.getOnSaleProducts(); }
    public List<ProductVO> searchProducts(Map<String, Object> cond) { return productService.searchProducts(cond); }
    public Map<String, Object> getProductDetail(long id) { return productService.getProductDetail(id); }
    public Map<String, Object> getProductPage(int page, int size) { return productService.getProductPage(page, size); }
}
