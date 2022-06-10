package com.projectlms.projectlms.util;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.projectlms.projectlms.constant.AppConstant;
import com.projectlms.projectlms.domain.common.ApiResponse;

public class ResponseUtil {
    private ResponseUtil() {}

    public static <T> ResponseEntity<Object> build(AppConstant.ResponseCode responseCode, T data, HttpStatus httpStatus) {
        return new ResponseEntity<>(build(responseCode, data), httpStatus);
    }

    private static <T> ApiResponse<T> build(AppConstant.ResponseCode responseCode, T data) {
        return ApiResponse.<T>builder()
                .timestamp(LocalDateTime.now())
                .responseCode(responseCode.name())
                .message(responseCode.getMessage())
                .data(data)
                .build();
    }
}
