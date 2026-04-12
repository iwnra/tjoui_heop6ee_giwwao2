package com.example.demo.dto;

import java.math.BigDecimal;

import com.example.demo.common.enums.DisplayPattern;

import lombok.Data;

@Data
public class CurrentDto {
    private String setting;
    private String settingClass;
    private String material;
    private String size;
    private Integer count;
    private BigDecimal quality;
    private boolean showTotal;
    
    public void applyDisplay(DisplayPattern p) {
        switch (p) {
            case RECUT:
            	setting = "再カット";
                showTotal = true;
                break;
            case NOT_COUNTED:
            	setting = "再サンダー";
                showTotal = false;
                break;
            default:
            	setting = "";
                showTotal = true;
        }
    }
}
