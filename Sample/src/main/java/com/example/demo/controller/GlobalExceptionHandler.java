package com.example.demo.controller;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.boot.json.JsonParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.ObjectMapper;

/**
 * 共通エラーハンドラ
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(Exception.class) // キャッチする例外の種類を指定
	public ResponseEntity<Map<String, Object>> handleAllExceptions(Exception ex, WebRequest request) {

		// 1. レスポンス用データの構築（jQuery側の jqXHR.responseJSON で受け取る）
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", LocalDateTime.now());
		body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
		body.put("error", "Internal Server Error");
		body.put("message", ex.getMessage());
		body.put("path", request.getDescription(false));

		try {
			// 2. JSONに変換してログ出力
			String jsonResponse = new ObjectMapper().writeValueAsString(body);

			// 第3引数に ex を渡すことで、スタックトレースも出力される
			log.error("システムエラー: {} ", jsonResponse, ex);

		} catch (JsonParseException e) {
			log.error("ログ用JSON変換失敗", e);
		}

		return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
