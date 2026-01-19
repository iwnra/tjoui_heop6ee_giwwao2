package com.example.demo.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class MonitorResponseDto {
    private BigDecimal totalQuality;
    private BigDecimal generalQuality;
    private BigDecimal floorQuality;
    private CurrentDto current;
    private List<HistoryDto> history;
    private List<DefectDto> defects;
}
