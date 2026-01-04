package com.example.demo.service;

import static com.example.demo.common.util.CalcUtil.*;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.demo.dto.PlcSummaryDto;
import com.example.demo.entity.PlcEntity;

@Service
public class PlcAggregationService {

	private static final String GRADE_A = "A";
	private static final String GRADE_B = "B";
	private static final String GRADE_D = "D";
	private static final int BOX_COUNT = 6;

	// 振り分けパターン
	// patternNo -> grade[6]（box1〜6）
	private static final Map<Integer, String[]> PATTERN_MAP = Map.of(
			1, new String[] { GRADE_A, GRADE_A, GRADE_A, GRADE_B, GRADE_D, GRADE_D },
			2, new String[] { GRADE_A, GRADE_A, GRADE_B, GRADE_B, GRADE_D, GRADE_D },
			3, new String[] { GRADE_A, GRADE_A, GRADE_B, GRADE_B, GRADE_B, GRADE_D }
	// ～ No.10
	);

	// 画面表示色
	private static final String COLOR_WHITE = "WHITE"; // 95%以上
	private static final String COLOR_YELLOW = "YELLOW"; // 90～94%
	private static final String COLOR_RED = "RED";

	/**
	 * box枚数を等級別に振り分けて集計
	 */
	public PlcSummaryDto aggregateByGrade() {

		// PlcEntity row = plcMapper.selectLatestPlcRow();
		PlcEntity row = new PlcEntity();

		// box1〜6の値を配列化
		int[] boxes = {
				row.getBox1(),
				row.getBox2(),
				row.getBox3(),
				row.getBox4(),
				row.getBox5(),
				row.getBox6()
		};

		// パターン（BOX → A品/B品/D品）
		String[] pattern = PATTERN_MAP.get(row.getPatternNo());

		// 振り分け
		int a = 0;
		int b = 0;
		int d = 0;
		for (int i = 0; i < BOX_COUNT; i++) {
			switch (pattern[i]) {
			case GRADE_A -> a += boxes[i];
			case GRADE_B -> b += boxes[i];
			case GRADE_D -> d += boxes[i];
			}
		}

		return new PlcSummaryDto(a, b, d);
	}

	/**
	 * 率計算（%）
	 */
	public BigDecimal calcRate(int value, int total) {
		if (total == 0) {
			return BigDecimal.ZERO;
		}
		return toPercent(div(BigDecimal.valueOf(value), BigDecimal.valueOf(total)));
	}
	//		return BigDecimal.valueOf(value)
	//				.multiply(cmn.HUNDRED)
	//				.divide(BigDecimal.valueOf(total), 1, RoundingMode.DOWN);

	/**
	 * 色判定
	 */
	public String judgeColor(BigDecimal rate) {
		if (rate.compareTo(BigDecimal.valueOf(95)) >= 0) {
			return COLOR_WHITE;
		}
		if (rate.compareTo(BigDecimal.valueOf(90)) >= 0) {
			return COLOR_YELLOW;
		}
		return COLOR_RED;
	}
}
