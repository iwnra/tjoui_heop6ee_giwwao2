package com.example.demo.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.dto.CurrentDto;
import com.example.demo.dto.DefectDto;
import com.example.demo.dto.HistoryDto;

@Mapper
public interface MonitorMapper {

	// 上段（全体・一般・フロア）
//	MonitorResponseDto selectSummary(Long id);

	// 現在サイズ
	CurrentDto selectCurrent(Long id);

	// 当日実績（最大5件）
	List<HistoryDto> selectTodayHistory(Long id);

	// 不良因子
	List<DefectDto> selectDefects(Long id);
}
