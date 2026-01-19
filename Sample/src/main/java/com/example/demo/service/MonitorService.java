package com.example.demo.service;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.CurrentDto;
import com.example.demo.dto.DefectDto;
import com.example.demo.dto.HistoryDto;
import com.example.demo.dto.MonitorResponseDto;
import com.example.demo.repository.MonitorMapper;

@Service
public class MonitorService {

	@Autowired
	private MonitorMapper mapper;

	/**
	 * 
	 */
	public MonitorResponseDto getData() {
		MonitorResponseDto res = new MonitorResponseDto();
//		res = mapper.selectSummary(1L);
		
	    CurrentDto current = mapper.selectCurrent(1L);
	    List<HistoryDto> history = mapper.selectTodayHistory(1L);
	    List<DefectDto> defects = mapper.selectDefects(1L);
	    
		res.setCurrent(current == null ? new CurrentDto() : current);
		res.setHistory(history.isEmpty() ? Collections.emptyList() : history);
		res.setDefects(defects.isEmpty() ? Collections.emptyList() : defects);

		return res;
	}
}