package com.example.demo.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class CurrentDto {
    private String setting;
    private String settingClass;
    private String material;
    private String size;
    private Integer count;
    private BigDecimal quality;
}
