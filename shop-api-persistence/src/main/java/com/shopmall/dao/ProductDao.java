package com.shopmall.dao;

import com.shopmall.common.vo.ProductVO;
import com.shopmall.common.vo.ProductOptionVO;
import java.util.List;
import java.util.Map;

public interface ProductDao {
    List<ProductVO> findAllOnSale();
    List<ProductVO> search(Map<String, Object> param);
    ProductVO findById(long id);
    List<ProductOptionVO> findOptionsByProductId(long productId);
    Map<String, Object> findOptionById(long optionId);
    void deductStock(Map<String, Object> param);
    void restock(Map<String, Object> param);
    List<ProductVO> findAllPaged(Map<String, Object> param);
    int countAll();
    int countOnSaleByCategory(Map<String, Object> p);
}
