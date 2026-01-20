package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.CurrentDto;
import com.example.demo.dto.HistoryDto;
import com.example.demo.dto.MonitorResponseDto;
import com.example.demo.repository.MonitorMapper;

/**
 * 不良因子画面 Service
 */
@Service
public class MonitorService {

	@Autowired
	private MonitorMapper mapper;

	/**
	 * データ取得
	 */
	public MonitorResponseDto getData() {
		MonitorResponseDto res = new MonitorResponseDto();
//		res = mapper.selectSummary(1L);
		
	    CurrentDto current = mapper.selectCurrent(1L);
		res.setCurrent(current == null ? new CurrentDto() : current);
		res.setDefects(mapper.selectDefects(1L));
		
		List<HistoryDto> history = mapper.selectTodayHistory(1L);
		List<HistoryDto> historyList = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			HistoryDto dto = new HistoryDto();
			if (i < history.size()) {
				dto = history.get(i);
			}
			historyList.add(dto);
		}
		res.setHistory(historyList);
		
		return res;
	}
}