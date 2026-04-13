package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.service.EmailService;

@Controller
public class DisasterController {

	@Autowired
	private EmailService emailService;

	/**
	 * 初期表示
	 * 
	 * @param model
	 * @return 災害モード選択画面
	 */
	@GetMapping("/disaster")
	public String index(Model model) {
//		MonitorResponseDto data = service.getData();
//		model.addAttribute("data", data);
		return "disaster";
	}

	/**
	 * メール自動送信
	 */
	@GetMapping("/sendEmail")
	public void sendEmail() {
		emailService.sendEmail("宛先", "件名", "本文");
	}
}
