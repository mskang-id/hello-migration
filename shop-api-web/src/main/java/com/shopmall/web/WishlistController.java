package com.shopmall.web;

import com.shopmall.common.envelope.ApiResponse;
import com.shopmall.service.WishlistService;
import com.shopmall.web.dto.wishlist.WishlistItemXml;
import com.shopmall.web.dto.wishlist.WishlistSignalListXml;
import com.shopmall.web.dto.wishlist.WishlistSignalXml;
import com.shopmall.web.dto.wishlist.WishlistXml;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/wishlist")
public class WishlistController {

    @Autowired private WishlistService wishlistService;

    @RequestMapping(value = "/{memberId}/products/{productId}", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponse add(@PathVariable("memberId") long memberId, @PathVariable("productId") long productId) {
        long wishlistId = wishlistService.add(memberId, productId);
        WishlistItemXml out = new WishlistItemXml();
        out.setWishlistId(wishlistId);
        out.setMemberId(memberId);
        out.setProductId(productId);
        return ResponseFactory.ok(out);
    }

    @RequestMapping(value = "/items/{wishlistId}", method = RequestMethod.DELETE)
    @ResponseBody
    public ApiResponse remove(@PathVariable("wishlistId") long wishlistId) {
        wishlistService.remove(wishlistId);
        WishlistItemXml out = new WishlistItemXml();
        out.setWishlistId(wishlistId);
        return ResponseFactory.ok(out);
    }

    @RequestMapping(value = "/{memberId}", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse list(@PathVariable("memberId") long memberId) {
        List<Map<String, Object>> rows = wishlistService.listByMember(memberId);
        WishlistXml out = new WishlistXml();
        out.setMemberId(memberId);
        int onSaleCount = 0;   // controller-tier business logic: derive on-sale count by iterating rows
        if (rows != null) {
            for (Map<String, Object> r : rows) {
                WishlistItemXml item = new WishlistItemXml();
                item.setWishlistId(((Number) r.get("WISHLIST_ID")).longValue());
                item.setMemberId(((Number) r.get("MEMBER_ID")).longValue());
                item.setProductId(((Number) r.get("PRODUCT_ID")).longValue());
                item.setRegDate(r.get("REG_DATE") == null ? null : String.valueOf(r.get("REG_DATE")));
                item.setProductName(r.get("PRODUCT_NAME") == null ? null : String.valueOf(r.get("PRODUCT_NAME")));
                item.setPrice(((Number) r.get("PRICE")).longValue());
                item.setStatus(r.get("STATUS") == null ? null : String.valueOf(r.get("STATUS")));
                item.setDisplayLabel(r.get("DISPLAY_LABEL") == null ? null : String.valueOf(r.get("DISPLAY_LABEL")));
                if ("ON_SALE".equals(r.get("STATUS"))) {   // UPPERCASE key read + Java-side counting
                    onSaleCount++;
                }
                out.getItems().add(item);
            }
        }
        out.setTotalCount(wishlistService.countByMember(memberId));   // controller-tier count math via DAO
        out.setOnSaleCount(onSaleCount);
        return ResponseFactory.ok(out);
    }

    @RequestMapping(value = "/{memberId}/signals", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse signals(@PathVariable("memberId") long memberId) {
        List<Map<String, Object>> rows = wishlistService.signals(memberId);
        WishlistSignalListXml out = new WishlistSignalListXml();
        out.setMemberId(memberId);
        int buyNowCount = 0;   // controller-tier business logic: derive buy-now count off the STOCK_SIGNAL
        if (rows != null) {
            for (Map<String, Object> r : rows) {
                WishlistSignalXml sig = new WishlistSignalXml();
                sig.setWishlistId(((Number) r.get("WISHLIST_ID")).longValue());
                sig.setMemberId(((Number) r.get("MEMBER_ID")).longValue());
                sig.setProductId(((Number) r.get("PRODUCT_ID")).longValue());
                sig.setProductName(r.get("PRODUCT_NAME") == null ? null : String.valueOf(r.get("PRODUCT_NAME")));
                sig.setPrice(((Number) r.get("PRICE")).longValue());
                sig.setStatus(r.get("STATUS") == null ? null : String.valueOf(r.get("STATUS")));
                sig.setTotalStock(((Number) r.get("TOTAL_STOCK")).longValue());
                sig.setStockSignal(r.get("STOCK_SIGNAL") == null ? null : String.valueOf(r.get("STOCK_SIGNAL")));
                sig.setDisplayLabel(r.get("DISPLAY_LABEL") == null ? null : String.valueOf(r.get("DISPLAY_LABEL")));
                if ("BUY_NOW".equals(r.get("STOCK_SIGNAL"))) {   // UPPERCASE key read + Java-side counting
                    buyNowCount++;
                }
                out.getSignals().add(sig);
            }
        }
        List<Map<String, Object>> soldOut = wishlistService.wishlistedSoldOut(memberId);   // NOT-EXISTS anti-join
        if (soldOut != null) {
            for (Map<String, Object> r : soldOut) {
                WishlistItemXml item = new WishlistItemXml();
                item.setWishlistId(((Number) r.get("WISHLIST_ID")).longValue());
                item.setProductId(((Number) r.get("PRODUCT_ID")).longValue());
                item.setProductName(r.get("PRODUCT_NAME") == null ? null : String.valueOf(r.get("PRODUCT_NAME")));
                out.getSoldOut().add(item);
            }
        }
        out.setBackInStockCount(wishlistService.countBackInStock(memberId));   // COUNT-with-HAVING via DAO
        out.setBuyNowCount(buyNowCount);
        return ResponseFactory.ok(out);
    }
}
