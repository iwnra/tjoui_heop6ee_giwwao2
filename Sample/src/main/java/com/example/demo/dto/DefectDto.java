package com.example.demo.dto;

import lombok.Data;

@Data
public class DefectDto {
    private String name;
    private String type; // "B" or "D"
    private Integer count;
}
