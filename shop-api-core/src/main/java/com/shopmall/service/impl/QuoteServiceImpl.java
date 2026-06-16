package com.shopmall.service.impl;

import com.shopmall.biz.SettlementBiz;
import com.shopmall.dao.MemberDao;
import com.shopmall.dao.ProductDao;
import com.shopmall.service.QuoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Read-only price preview. Runs the same price loop as placeOrder but writes
// nothing: no coupon markUsed, no stock deduct, no order insert.
@Service
public class QuoteServiceImpl implements QuoteService {

    @Autowired private ProductDao productDao;
    @Autowired private MemberDao memberDao;
    @Autowired private SettlementBiz settlementBiz;

    @SuppressWarnings({"unchecked", "rawtypes"})
    public Map<String, Object> computeQuote(Map<String, Object> param) {
        List<Map<String, Object>> items = (List<Map<String, Object>>) param.get("items");
        long memberId = Long.parseLong(String.valueOf(param.get("memberId")));

        int itemsTotal = 0;
        for (Map<String, Object> it : items) {
            long optionId = Long.parseLong(String.valueOf(it.get("optionId")));
            int qty = Integer.parseInt(String.valueOf(it.get("qty")));
            Map<String, Object> opt = productDao.findOptionById(optionId);
            if (opt == null) {
                return null;
            }
            int basePrice = ((Number) opt.get("BASE_PRICE")).intValue();
            int extra = ((Number) opt.get("EXTRA_PRICE")).intValue();
            int unitPrice = basePrice + extra;
            itemsTotal += unitPrice * qty;
        }

        Map<String, Object> member = memberDao.findById(memberId);
        if (member == null) {
            return null;
        }
        String grade = String.valueOf(member.get("GRADE"));

        Long couponId = param.get("couponId") == null ? null : Long.valueOf(String.valueOf(param.get("couponId")));
        int discount = settlementBiz.previewDiscount(couponId, itemsTotal);

        String zipcode = param.get("zipcode") == null ? null : String.valueOf(param.get("zipcode"));
        int shippingFee = settlementBiz.shippingFee(grade, itemsTotal, zipcode);

        int taxable = itemsTotal - discount + shippingFee;
        int vat = settlementBiz.vat(taxable);
        int grandTotal = taxable + vat;
        int earnPreview = grandTotal * settlementBiz.earnRate(grade) / 100;

        Map<String, Object> quote = new HashMap<String, Object>();
        quote.put("itemsTotal", itemsTotal);
        quote.put("discount", discount);
        quote.put("shippingFee", shippingFee);
        quote.put("vat", vat);
        quote.put("grandTotal", grandTotal);
        quote.put("earnPreview", earnPreview);
        quote.put("grade", grade);
        return quote;
    }
}
