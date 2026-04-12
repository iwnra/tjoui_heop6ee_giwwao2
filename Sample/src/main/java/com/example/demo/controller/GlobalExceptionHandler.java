package com.example.demo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice // 全てのControllerの例外を横断的にキャッチする
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class) // キャッチする例外の種類を指定
    public ResponseEntity<Map<String, Object>> handleAllExceptions(Exception ex) {
        Map<String, Object> errorDetails = new HashMap<>();
        
        // jQuery側の jqXHR.responseJSON で受け取る中身を定義
        errorDetails.put("message", ex.getMessage());
        errorDetails.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorDetails.put("error", "Internal Server Error");

        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
