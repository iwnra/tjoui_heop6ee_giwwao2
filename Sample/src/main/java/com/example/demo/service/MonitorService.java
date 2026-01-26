package com.example.demo.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.common.enums.DisplayPattern;
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
	
	private static final int RATE_WHITE = 95;
	private static final int RATE_YELLOW = 90;
	private static final int RATE_RED = 85;

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
		
		DisplayPattern pattern = DisplayPattern.judge(entityList.get(0));
		current.applyDisplay(pattern);

		HistoryDto row = new HistoryDto();
		row.applyDisplay(pattern);
		
		SizeInfoEntity targetEntity = determineCalculationTarget(sizeInfoList);

		BigDecimal totalProduction = BigDecimal.ZERO;
		for (SizeInfoEntity e : sizeInfoList) {
		    BigDecimal production =
		            e.getThickness()
		             .multiply(e.getWidth())
		             .multiply(e.getLength())
		             .multiply(BigDecimal.valueOf(e.getCount()))
		             .divide(THOUSAND_CUBED);
		    totalProduction = totalProduction.add(production);
		}

		
		return res;
	}

	private Map<String, PlcSummaryDto> aggregatePlcSummary(List<SizeInfoEntity> entityList) {
		return entityList.stream().collect(Collectors.groupingBy(SizeInfoEntity::getKubun,
				Collectors.reducing(PlcSummaryDto.empty(), PlcSummaryDto::fromEntity, PlcSummaryDto::add)));
	}

//	private PlcSummaryDto sum(OperationEntity operation, List<DefectEntity> defects, ShitanukiEntity shitanuki) {
//		int a = 0;
//		int b = 0;
//		int d = 0;
//		if (operation != null) {
//			a = Optional.ofNullable(operation.getGradeA()).orElse(0);
//		}
//		if (!defects.isEmpty()) {
//			b = Optional.ofNullable(defects.get(0).getGradeB()).orElse(0);
//			d = Optional.ofNullable(defects.get(0).getGradeD()).orElse(0);
//		}
//		if (shitanuki != null) {
//			b += Optional.ofNullable(shitanuki.getGradeB()).orElse(0);
//			d += Optional.ofNullable(shitanuki.getGradeD()).orElse(0);
//		}
//		a -= (b + d);
//		return new PlcSummaryDto(a, b, d);
//	}

	private PlcSummaryDto sum(OperationEntity operation, List<DefectEntity> defects, ShitanukiEntity shitanuki) {
		int a = operation == null ? 0 : toInt(operation.getGradeA());
		int b = defects.isEmpty() ? 0 : toInt(defects.get(0).getGradeB());
		int d = defects.isEmpty() ? 0 : toInt(defects.get(0).getGradeD());
		if (shitanuki != null) {
			b += toInt(shitanuki.getGradeB());
			d += toInt(shitanuki.getGradeD());
		}
		return new PlcSummaryDto(a - (b + d), b, d);
	}
	
	private int toInt(Integer value) {
	    return value != null ? value : 0;
	}
	
	/**
	 * 判定ロジック：計算対象となるレコードを特定する
	 * @return 計算対象のEntity。稼働情報を使用しない場合はnull。
	 */
	private SizeInfoEntity determineCalculationTarget(List<SizeInfoEntity> sizeInfoList) {
	    if (sizeInfoList == null || sizeInfoList.isEmpty()) {
	        return null;
	    }

	    int size = sizeInfoList.size();
	    SizeInfoEntity first = sizeInfoList.get(0);

	    // 条件1: サイズが2以上 かつ 1件目の「前終了」「不良」「品質」が全て入力済み
	    if (size >= 2 && 
	        first.getPreviousSizeEndTime() != null && 
	        first.getDefectInputDateTime() != null && 
	        first.getQualityInputDateTime() != null) {
	        
	        return null; // 稼働情報の枚数を使用しない
	    }

	    // 条件2: サイズが2以上 かつ 1件目の「前サイズ終了」がnull
	    if (size >= 2 && first.getPreviousSizeEndTime() == null) {
	        return sizeInfoList.get(1); // 2件目を対象とする
	    }

	    // 条件3: 上記以外（1件のみ、または1件目が未入力状態など）
	    return first; // 1件目を対象とする
	}
	
	private String getDRateColor(int rate) {
	    if (rate >= RATE_WHITE) {
	        return "white";
	    }
	    if (rate >= RATE_YELLOW) {
	        return "yellow";
	    }
	    if (rate >= RATE_RED) {
	        return "red";
	    }
	    return "black";
	}

}