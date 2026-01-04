package com.example.demo.entity;

import java.time.LocalDateTime;

import lombok.Data;

/**
 * PLC実績データエンティティ
 */
@Data
public class PlcEntity {
	private LocalDateTime createdAt; // 登録日
	private Integer patternNo; // パターン番号
	private Integer box1; // 1BOX枚数
	private Integer box2; // 2BOX枚数
	private Integer box3; // 3BOX枚数
	private Integer box4; // 4BOX枚数
	private Integer box5; // 5BOX枚数
	private Integer box6; // 6BOX枚数
}