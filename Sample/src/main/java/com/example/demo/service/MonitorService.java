package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.CurrentDto;
import com.example.demo.dto.HistoryDto;
import com.example.demo.dto.MonitorResponseDto;
import com.example.demo.dto.PlcSummaryDto;
import com.example.demo.entity.SizeInfoEntity;
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

		List<SizeInfoEntity> entityList = new ArrayList<>();
		Map<String, PlcSummaryDto> summaryByKubun = aggregatePlcSummary(entityList);

		// 区分"1"の集計結果
		PlcSummaryDto kubun1 = summaryByKubun.getOrDefault("1", PlcSummaryDto.empty());
		// 区分"2"の集計結果
		PlcSummaryDto kubun2 = summaryByKubun.getOrDefault("2", PlcSummaryDto.empty());
		// 稼働情報DTOの枚数を区分を判定してから上記のDTOに加算
		// 合計
		PlcSummaryDto all = kubun1.add(kubun2);

		return res;
	}

	public Map<String, PlcSummaryDto> aggregatePlcSummary(List<SizeInfoEntity> entityList) {
		return entityList.stream().collect(Collectors.groupingBy(SizeInfoEntity::getKubun,
				Collectors.reducing(PlcSummaryDto.empty(), PlcSummaryDto::fromEntity, PlcSummaryDto::add)));
	}

}