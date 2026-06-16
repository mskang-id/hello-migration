package com.shopmall.dao.impl;

import com.shopmall.dao.ProductDao;
import com.shopmall.common.vo.ProductVO;
import com.shopmall.common.vo.ProductOptionVO;
import com.ibatis.sqlmap.client.SqlMapClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class ProductDaoImpl extends SqlMapClientDaoSupport implements ProductDao {

    @Autowired
    public void init(SqlMapClient sqlMapClient) {
        super.setSqlMapClient(sqlMapClient);
    }

    @SuppressWarnings("unchecked")
    public List<ProductVO> findAllOnSale() {
        return getSqlMapClientTemplate().queryForList("Product.findAllOnSale");
    }

    @SuppressWarnings("unchecked")
    public List<ProductVO> search(Map<String, Object> param) {
        return getSqlMapClientTemplate().queryForList("Product.search", param);
    }

    public ProductVO findById(long id) {
        return (ProductVO) getSqlMapClientTemplate().queryForObject("Product.findById", id);
    }

    @SuppressWarnings("unchecked")
    public List<ProductOptionVO> findOptionsByProductId(long productId) {
        return getSqlMapClientTemplate().queryForList("Product.findOptionsByProductId", productId);
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> findOptionById(long optionId) {
        return (Map<String, Object>) getSqlMapClientTemplate().queryForObject("Product.findOptionById", optionId);
    }

    public void deductStock(Map<String, Object> param) {
        getSqlMapClientTemplate().update("Product.deductStock", param);
    }

    public void restock(Map<String, Object> param) {
        getSqlMapClientTemplate().update("Product.restock", param);
    }

    @SuppressWarnings("unchecked")
    public List<ProductVO> findAllPaged(Map<String, Object> param) {
        return getSqlMapClientTemplate().queryForList("Product.findAllPaged", param);
    }

    public int countAll() {
        return (Integer) getSqlMapClientTemplate().queryForObject("Product.countAll");
    }

    public int countOnSaleByCategory(Map<String, Object> p) {
        return (Integer) getSqlMapClientTemplate().queryForObject("Product.countOnSaleByCategory", p);
    }
}
