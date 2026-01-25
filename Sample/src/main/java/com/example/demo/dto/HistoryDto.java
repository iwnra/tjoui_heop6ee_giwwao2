package com.example.demo.dto;

import java.math.BigDecimal;

import com.example.demo.common.enums.DisplayPattern;

import lombok.Data;

@Data
public class HistoryDto {
    private String setting;
    private String settingClass;
    private String material;
    private String size;
    private Integer count;
    private BigDecimal quality;
    
    public void applyDisplay(DisplayPattern p) {
        switch (p) {
            case RECUT:
            	setting = "再カット";
                showSize = true;
                showQuality = false;
                break;
            case NOT_COUNTED:
                showSize = false;
                showQuality = false;
                break;
            case UPGRADE:
                showSize = true;
                showQuality = true;
                break;
            case NOT_WORKING:
            	setting = "再サンダー";
                showSize = false;
                showQuality = false;
                break;
            case NORMAL:
                showSize = true;
                showQuality = true;
        }
    }
}
