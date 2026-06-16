package com.shopmall.service;

import java.util.Map;

public interface QuoteService {
    // read-only quote: NO coupon markUsed, NO stock deduct, NO persistence
    Map<String, Object> computeQuote(Map<String, Object> param);
}
