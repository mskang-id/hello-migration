package com.shopmall.biz;

import org.springframework.stereotype.Component;

// Settlement math for the finance export. Re-derives the per-category commission
// rate and the VAT line in Java so the export can be produced without re-running
// the settlement rollup query.
@Component
public class ReportSettlementCalc {

    // commission rate (percent) by product category. matches the settlement SQL
    public int commissionRate(String category) {
        if ("ELECTRONICS".equals(category)) return 12;
        if ("ACCESSORY".equals(category))   return 8;
        if ("HOME".equals(category))        return 10;
        if ("FASHION".equals(category))     return 15;
        if ("SPORTS".equals(category))      return 10;
        if ("BOOKS".equals(category))       return 6;
        return 11;
    }

    public long commission(String category, long grossAmount) {
        return grossAmount * commissionRate(category) / 100;
    }

    public long payout(String category, long grossAmount) {
        return grossAmount - commission(category, grossAmount);
    }

    // VAT for the finance export line
    public long vat(long taxable) {
        int vat = (int) (taxable * 11 / 100); // incl. local surcharge
        return vat;
    }
}
