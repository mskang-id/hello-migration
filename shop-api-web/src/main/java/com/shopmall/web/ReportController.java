package com.shopmall.web;

import com.shopmall.common.envelope.ApiResponse;
import com.shopmall.service.ReportService;
import com.shopmall.web.dto.report.BestSellerListXml;
import com.shopmall.web.dto.report.BestSellerXml;
import com.shopmall.web.dto.report.DailySalesListXml;
import com.shopmall.web.dto.report.DailySalesXml;
import com.shopmall.web.dto.report.SettlementListXml;
import com.shopmall.web.dto.report.SettlementXml;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired private ReportService reportService;

    @RequestMapping(value = "/best-sellers", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse bestSellers() {
        List<Map<String, Object>> rows = reportService.bestSellers();
        BestSellerListXml out = new BestSellerListXml();
        for (Map<String, Object> row : rows) {
            BestSellerXml b = new BestSellerXml();
            b.setProductId(((Number) row.get("PRODUCT_ID")).longValue());
            b.setName(row.get("NAME") == null ? null : String.valueOf(row.get("NAME")));
            b.setCategory(row.get("CATEGORY") == null ? null : String.valueOf(row.get("CATEGORY")));
            b.setTotalQty(((Number) row.get("TOTAL_QTY")).longValue());
            b.setTotalAmount(((Number) row.get("TOTAL_AMOUNT")).longValue());
            out.getRows().add(b);
        }
        return ResponseFactory.ok(out);
    }

    @RequestMapping(value = "/daily-sales", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse dailySales() {
        List<Map<String, Object>> rows = reportService.dailySales();
        DailySalesListXml out = new DailySalesListXml();
        for (Map<String, Object> row : rows) {
            DailySalesXml d = new DailySalesXml();
            d.setOrderDate(row.get("ORDER_DATE") == null ? null : String.valueOf(row.get("ORDER_DATE")));
            d.setOrderCnt(((Number) row.get("ORDER_CNT")).longValue());
            d.setSalesAmount(((Number) row.get("SALES_AMOUNT")).longValue());
            out.getRows().add(d);
        }
        return ResponseFactory.ok(out);
    }

    @RequestMapping(value = "/settlement", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse settlement() {
        List<Map<String, Object>> rows = reportService.sellerSettlement();
        SettlementListXml out = new SettlementListXml();
        for (Map<String, Object> row : rows) {
            SettlementXml s = new SettlementXml();
            s.setSellerName(row.get("SELLER_NAME") == null ? null : String.valueOf(row.get("SELLER_NAME")));
            s.setGrossAmount(((Number) row.get("GROSS_AMOUNT")).longValue());
            s.setCommission(((Number) row.get("COMMISSION")).longValue());
            s.setPayout(((Number) row.get("PAYOUT")).longValue());
            out.getRows().add(s);
        }
        return ResponseFactory.ok(out);
    }
}
