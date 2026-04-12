package com.example.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * メール Service
 */
@Service
public class EmailService {

	@Autowired
	private JavaMailSender mailSender;

	private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

	/**
	 * メール自動送信
	 */
	public void sendEmail(String to, String subject, String body) {

		try {
			SimpleMailMessage message = new SimpleMailMessage();
			// 送信元
			message.setFrom("your-email@example.com");
			// 宛先
			message.setTo(to);
			// 件名
			message.setSubject(subject);
			// 本文
			message.setText(body);

			mailSender.send(message);

		} catch (Exception e) {
//			e.printStackTrace(); // 標準エラー出力
			
			// エラーログ
			logger.error("メールの送信に失敗しました。宛先: {}", to, e); 
		    
		    // 画面にエラーを伝えるための処理（例外を投げ直すなど）
		    throw new RuntimeException("メール送信に失敗しました。");
		}
	}
}