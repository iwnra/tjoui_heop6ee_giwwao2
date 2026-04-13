package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.dto.EvacuationDto;
import com.example.demo.dto.MonitorResponseDto;
import com.example.demo.service.CommonService;
import com.example.demo.service.MonitorService;

@Controller
public class MonitorController {

	@Autowired
	private MonitorService service;

	@Autowired
	private CommonService commonService;

	/**
	 * 初期表示
	 * 
	 * @param model
	 * @return 不良因子画面
	 */
	@GetMapping("/furyoinshi")
	public String index(Model model) {
		MonitorResponseDto data = service.getData();
		model.addAttribute("data", data);
		return "furyoinshi";
	}

//	/**
//	 * 画面更新
//	 * 
//	 * @return レスポンスDto
//	 */
//	@GetMapping("/furyoinshi_ajax")
//	@ResponseBody
//	public MonitorResponseDto ajax() {
//		return service.getData();
//	}

	/**
	 * 画面更新
	 * 
	 * @return レスポンスDto
	 */
	@GetMapping("/furyoinshi_ajax")
	public ResponseEntity<?> ajax() {
		// 1. 共通サービスで避難指示を最優先チェック
		// MONITOR_A は Java側で定義した定数（Enum等）
		EvacuationDto alert = commonService.checkEvacuation("MONITOR_A");

		if (alert != null) {
			// 避難指示があれば、通常のDB検索（重い処理など）をスキップして即返却
			return ResponseEntity.ok(alert);
		}

		// 2. 通常の監視ロジック（ここには到達しない）
		MonitorResponseDto data = service.getData();
		return ResponseEntity.ok(data);
	}
}
