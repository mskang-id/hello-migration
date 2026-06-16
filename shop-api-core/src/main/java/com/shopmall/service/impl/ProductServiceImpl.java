package com.shopmall.service.impl;

import com.shopmall.dao.ProductDao;
import com.shopmall.service.ProductService;
import com.shopmall.common.vo.ProductVO;
import com.shopmall.common.vo.ProductOptionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDao productDao;

    public List<ProductVO> getOnSaleProducts() {
        return productDao.findAllOnSale();
    }

    public List<ProductVO> searchProducts(Map<String, Object> cond) {
        return productDao.search(cond);
    }

    public Map<String, Object> getProductDetail(long productId) {
        Map<String, Object> result = new HashMap<String, Object>();
        ProductVO product = productDao.findById(productId);
        List<ProductOptionVO> options = productDao.findOptionsByProductId(productId);
        result.put("product", product);
        result.put("options", options);
        return result;
    }

    public Map<String, Object> getProductPage(int page, int size) {
        int offset = page * size;
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("size", size);
        param.put("offset", offset);
        List<ProductVO> products = productDao.findAllPaged(param);
        int total = productDao.countAll();
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("page", page);
        result.put("size", size);
        result.put("total", total);
        result.put("products", products);
        return result;
    }
}
