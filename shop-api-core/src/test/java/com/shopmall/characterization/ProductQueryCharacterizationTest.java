package com.shopmall.characterization;

import com.shopmall.service.ProductService;
import com.shopmall.common.vo.ProductVO;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductQueryCharacterizationTest extends BaseIntegrationTest {

    @Autowired
    private ProductService productService;

    @Test
    public void getOnSaleProducts_returnsOnlyOnSale() throws Exception {
        List<ProductVO> products = productService.getOnSaleProducts();

        assertThat(products).hasSize(14);
        for (ProductVO product : products) {
            assertThat(product.getStatus()).isEqualTo("ON_SALE");
        }
    }

    @Test
    public void getOnSaleProducts_excludesSoldOut() throws Exception {
        List<ProductVO> products = productService.getOnSaleProducts();

        for (ProductVO product : products) {
            assertThat(product.getStatus()).isNotEqualTo("SOLD_OUT");
        }
    }

    @Test
    public void getOnSaleProducts_excludesDiscontinued() throws Exception {
        List<ProductVO> products = productService.getOnSaleProducts();

        for (ProductVO product : products) {
            assertThat(product.getStatus()).isNotEqualTo("DISCONTINUED");
        }
    }

    @Test
    public void getOnSaleProducts_hasExpectedFields() throws Exception {
        List<ProductVO> products = productService.getOnSaleProducts();

        assertThat(products).isNotEmpty();
        ProductVO firstProduct = products.get(0);
        assertThat(firstProduct.getProductId()).isNotNull();
        assertThat(firstProduct.getName()).isNotNull();
        assertThat(firstProduct.getCategory()).isNotNull();
        assertThat(firstProduct.getPrice()).isNotNull();
        assertThat(firstProduct.getSellerName()).isNotNull();
        assertThat(firstProduct.getStatus()).isNotNull();
    }
}
