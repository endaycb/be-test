package com.greenda.be.test.config;

import com.greenda.be.test.controller.BaseResponse;
import com.greenda.be.test.execption.BadRequestException;
import com.greenda.be.test.execption.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalAdviceConfig {

    @ExceptionHandler(BadRequestException.class)
    public BaseResponse<String> handleInsufficientStock(BadRequestException ex) {
        return BaseResponse.badRequest(ex.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    public BaseResponse<String> handleNotFound(NotFoundException ex) {
        return BaseResponse.notFound(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public BaseResponse<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return BaseResponse.badRequest("Some mandatory field is missing", errors);
    }
}
