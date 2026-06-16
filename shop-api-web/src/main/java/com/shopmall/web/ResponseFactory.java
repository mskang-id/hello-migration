package com.shopmall.web;

import com.shopmall.common.envelope.ApiHeader;
import com.shopmall.common.envelope.ApiResponse;
import com.shopmall.common.error.ErrorCode;
import com.shopmall.common.util.DateUtil;

final class ResponseFactory {
    private ResponseFactory() {}

    static ApiResponse ok(Object body) {
        return new ApiResponse(new ApiHeader(ErrorCode.SUCCESS, "SUCCESS", DateUtil.now()), body);
    }

    static ApiResponse fail(String code, String message, Object body) {
        return new ApiResponse(new ApiHeader(code, message, DateUtil.now()), body);
    }
}
