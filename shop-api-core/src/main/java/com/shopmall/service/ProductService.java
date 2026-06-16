package com.shopmall.service;

import com.shopmall.common.vo.ProductVO;
import java.util.List;
import java.util.Map;

public interface ProductService {
    List<ProductVO> getOnSaleProducts();
    List<ProductVO> searchProducts(Map<String, Object> cond);
    Map<String, Object> getProductDetail(long productId);
    Map<String, Object> getProductPage(int page, int size);
}
