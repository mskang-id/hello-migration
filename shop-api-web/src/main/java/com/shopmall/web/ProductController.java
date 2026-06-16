package com.shopmall.web;

import com.shopmall.common.constant.AppConstants;
import com.shopmall.common.envelope.ApiResponse;
import com.shopmall.common.vo.ProductVO;
import com.shopmall.common.vo.ProductOptionVO;
import com.shopmall.common.error.ErrorCode;
import com.shopmall.facade.ProductFacade;
import com.shopmall.web.dto.product.ProductDetailXml;
import com.shopmall.web.dto.product.ProductListXml;
import com.shopmall.web.dto.product.ProductPageXml;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/products")
public class ProductController {

    @Autowired private ProductFacade productFacade;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse list() {
        return ResponseFactory.ok(new ProductListXml(productFacade.getOnSaleProducts()));
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse search(@RequestParam Map<String, Object> params) {
        Map<String, Object> cond = new HashMap<String, Object>();
        cond.put("keyword", params.get("keyword"));
        cond.put("category", params.get("category"));
        if (params.get("minPrice") != null) cond.put("minPrice", Integer.valueOf(String.valueOf(params.get("minPrice"))));
        if (params.get("maxPrice") != null) cond.put("maxPrice", Integer.valueOf(String.valueOf(params.get("maxPrice"))));
        List<String> statuses = new ArrayList<String>();
        statuses.add("ON_SALE");
        cond.put("statuses", statuses);
        return ResponseFactory.ok(new ProductListXml(productFacade.searchProducts(cond)));
    }

    @RequestMapping(value = "/page", method = RequestMethod.GET)
    @ResponseBody
    @SuppressWarnings("unchecked")
    public ApiResponse page(@RequestParam(value = "page", defaultValue = "0") int page,
                            @RequestParam(value = "size", required = false) Integer sizeParam) {
        int size = (sizeParam == null || sizeParam <= 0) ? AppConstants.DEFAULT_PAGE_SIZE : sizeParam;
        Map<String, Object> result = productFacade.getProductPage(page, size);
        int total = ((Number) result.get("total")).intValue();
        int totalPages = size <= 0 ? 0 : (total + size - 1) / size;
        ProductPageXml xml = new ProductPageXml();
        xml.setPage(page);
        xml.setSize(size);
        xml.setTotal(total);
        xml.setTotalPages(totalPages);
        xml.setProducts((List<ProductVO>) result.get("products"));
        return ResponseFactory.ok(xml);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    @SuppressWarnings("unchecked")
    public ApiResponse detail(@PathVariable("id") long id) {
        Map<String, Object> detail = productFacade.getProductDetail(id);
        ProductVO product = (ProductVO) detail.get("product");
        if (product == null) {
            return ResponseFactory.fail(ErrorCode.NOT_FOUND, "product not found", null);
        }
        ProductDetailXml xml = new ProductDetailXml();
        xml.setProduct(product);
        xml.setOptions((List<ProductOptionVO>) detail.get("options"));
        return ResponseFactory.ok(xml);
    }
}
