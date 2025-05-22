package com.greenda.be.test.controller;

import io.micrometer.common.util.StringUtils;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponse<T> {

    private int status;
    private boolean success;
    private String message;

    private T data;

    public static <T> BaseResponse<T> ok(String message) {
        return BaseResponse.<T>builder()
                .success(true)
                .status(200)
                .message(StringUtils.isNotBlank(message) ? message : "")
                .data(null)
                .build();
    }

    public static <T> BaseResponse<T> ok(String message, T data) {
        return BaseResponse.<T>builder()
                .success(true)
                .status(200)
                .message(StringUtils.isNotBlank(message) ? message : "")
                .data(data)
                .build();
    }

    public static <T> BaseResponse<T> badRequest(String message) {
        return BaseResponse.<T>builder()
                .status(400)
                .success(false)
                .message(message)
                .data(null)
                .build();
    }

    public static <T> BaseResponse<T> badRequest(String message, T data) {
        return BaseResponse.<T>builder()
                .status(400)
                .success(false)
                .message(message)
                .data(data)
                .build();
    }

    public static <T> BaseResponse<T> notFound(String message) {
        return BaseResponse.<T>builder()
                .status(404)
                .success(false)
                .message(message)
                .data(null)
                .build();
    }

}
