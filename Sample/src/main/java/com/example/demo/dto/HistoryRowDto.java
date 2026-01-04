package com.example.demo.dto;

import lombok.Data;

@Data
public class HistoryRowDto {
    private String sizeNo = "";
    private String thickness = ""; // 厚み
    private String width = "";     // 幅
    private String length = "";    // 長さ
    private String material = "";  // 材種
    private String count = "";     // 枚数
    private String productivity = ""; // 生産性
}
