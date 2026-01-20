package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.dto.MonitorResponseDto;
import com.example.demo.service.MonitorService;

@Controller
public class MonitorController {

	@Autowired
	private MonitorService service;

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

	/**
	 * 画面更新
	 * 
	 * @return レスポンスDto
	 */
	@GetMapping("/furyoinshi_ajax")
	@ResponseBody
	public MonitorResponseDto ajax() {
		return service.getData();
	}
}
