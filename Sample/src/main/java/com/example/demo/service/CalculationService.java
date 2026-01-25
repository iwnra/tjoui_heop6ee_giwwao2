package com.example.demo.service;

import static com.example.demo.common.util.CalcUtil.*;
import static com.example.demo.common.util.DisplayUtil.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.common.enums.DisplayPattern;
import com.example.demo.dto.HistoryRowDto;
import com.example.demo.dto.PlcRateViewDto;
import com.example.demo.dto.PlcSummaryDto;
import com.example.demo.entity.SizeInfoEntity;

@Service
public class CalculationService {

	@Autowired
	private PlcAggregationService plc;

	public PlcRateViewDto getData() {

		// PLCの枚数を振り分け
		PlcSummaryDto calcDto = plc.aggregateByGrade();

		PlcRateViewDto dto = new PlcRateViewDto();
		dto.setSizeA(format(calcDto.getA()));
		dto.setSizeB(format(calcDto.getB()));
		dto.setSizeD(format(calcDto.getD()));

		// 率計算
		BigDecimal aRate = calcDto.getARate();
		BigDecimal bRate = calcDto.getBRate();
		BigDecimal dRate = calcDto.getDRate();

		dto.setSizeARate(format(aRate, 2, true));
		dto.setSizeBRate(format(bRate, 2, true));
		dto.setSizeDRate(format(dRate, 2, true));

		// 色判定
		dto.setSizeAFlg(plc.judgeColor(aRate));
		dto.setSizeBFlg(plc.judgeColor(bRate));
		dto.setSizeDFlg(plc.judgeColor(dRate));

		List<SizeInfoEntity> entityList = new ArrayList<>();
		boolean isEnd = entityList.get(0).getEndDate() != null;
		PlcSummaryDto totalSummary = entityList.stream().skip(isEnd ? 0 : 1) // リストの1行目をスキップ
				// equalsメソッドは定数を左側にすると安全（引数がnullの場合は結果がfalseになる）
				.filter(e -> "1".equals(e.getOperationKbn())) // 稼働区分が1の場合のみ集計
				.map(SizeInfoEntity::toPlcSummaryDto)
				// .reduce(PlcSummaryDto.empty(), PlcSummaryDto::add);
				.reduce(calcDto, PlcSummaryDto::add); // PLCの値にサイズ情報の値を加算

		return dto;
	}

	public List<HistoryRowDto> getHistory(List<SizeInfoEntity> sizeInfoList, PlcSummaryDto plc) {
//		List<SizeInfoEntity> safeEntities = (entities == null) ? Collections.emptyList() : entities;
//
//		return IntStream.range(0, 5)
//				.mapToObj(i -> {
//					HistoryRowDto dto = new HistoryRowDto(); // すでにフィールドは "" になっている
//
//					if (i < safeEntities.size()) {
//						SizeInfoEntity entity = safeEntities.get(i);
//						// 共通項目
//						dto.setSizeNo(format(entity.getSizeNo()));
//						dto.setThickness(format(entity.getThickness(), 2, true));
//						// ... (中略) ...
//
//						// 特殊項目
//						if (i == 0) {
//							dto.setCount(String.valueOf(plc.getTotal()));
//							dto.setProductivity(format(plc.getARate(), 2, false));
//						} else {
//							dto.setCount(String.valueOf(entity.getCount()));
//							//	 dto.setProductivity(format(entity.getProductivity()));
//						}
//					}
//					return dto;
//				})
//				.collect(Collectors.toList());

		List<HistoryRowDto> historyList = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			HistoryRowDto dto = new HistoryRowDto(); // デフォルトで空文字("")が入っている想定

			if (i < sizeInfoList.size()) {
				SizeInfoEntity sizeInfo = sizeInfoList.get(i);
				// 共通項目
				dto.setSizeNo(format(sizeInfo.getSizeNo()));
				dto.setThickness(format(sizeInfo.getThickness(), 2, true));
				// ... (中略) ...

				if (i == 0) {
					// 1行目の特殊処理
					dto.setCount(String.valueOf(plc.getTotal()));
					dto.setProductivity(format(plc.getARate(), 2, false));
				} else {
					dto.setCount(String.valueOf(sizeInfo.getCount()));
					// dto.setProductivity(format(entity.getProductivity()));
				}
				
				// 表示判定（最後）
		        DisplayPattern pattern = DisplayPattern.judge(sizeInfo);
		        dto.applyDisplay(pattern);
			}
			historyList.add(dto);
		}
		return historyList;
	}

	public BigDecimal calculate(BigDecimal v1, BigDecimal v2, BigDecimal v3, BigDecimal v4, BigDecimal v5,
			BigDecimal v6, BigDecimal v7, BigDecimal v8, BigDecimal v9) {

		// 式: ((9.5 + 1.2) / 1000 * (1000 + 50) / 1200 * 1200 / 950) * 3000

		// 厚み: (9.5 + 1.2) / 1000
		BigDecimal step1 = div(add(v1, v2), v3);

		// 幅: (1000 + 50) / 1200
		BigDecimal step2 = div(add(v4, v5), v6);

		// 長さ: 1200 / 950
		BigDecimal step3 = div(v7, v8);

		// 残りの計算と結合: (step1 * step2 * step3) * 3000
		return mul(mul(mul(step1, step2), step3), v9);
	}

	public BigDecimal calculate2(BigDecimal v1, BigDecimal v2, BigDecimal v3, BigDecimal v4, BigDecimal v5) {

		// 式: 9.5 * 1000 * 1200 * 3000 / 1000 ^ 3

		// 分子: v1*v2*v3*v4
		BigDecimal numerator = mul(mul(mul(v1, v2), v3), v4);

		// 分母: v2の3乗
		BigDecimal denominator = pow(v5, 3);

		return div(numerator, denominator);
	}
}