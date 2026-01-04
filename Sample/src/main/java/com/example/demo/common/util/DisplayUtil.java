package com.example.demo.common.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class DisplayUtil {

	// privateコンストラクタで、外部からの new を禁止
	private DisplayUtil() {
		// インスタンス化しようとしたら例外を投げる（より厳格なやり方）
		throw new UnsupportedOperationException("Utility class");
	}

	/**
	 * 数値（BigDecimal）を画面表示用にフォーマット
	 * 
	 * @param value 対象の数値
	 * @param scale 小数点以下の桁数
	 * @param trimZero trueの場合、末尾の不要な0を表示しない（例：1.50 → 1.5）
	 * @return フォーマット後の文字列
	 */
	public static String format(BigDecimal value, int scale, boolean trimZero) {
		// nullの場合はゼロとして扱う
		BigDecimal target = (value == null) ? BigDecimal.ZERO : value;

		// 指定の桁数で四捨五入
		BigDecimal rounded = target.setScale(scale, RoundingMode.HALF_UP);

		if (trimZero) {
			// 末尾の0をカット
			return rounded.stripTrailingZeros().toPlainString();
		} else {
			// 指定した桁数まで0埋め
			return rounded.toPlainString();
		}
	}

	/**
	 * 数値（Integer）を画面表示用にフォーマット
	 * 
	 * @param value 対象の数値
	 * @return フォーマット後の文字列
	 */
	public static String format(Integer value) {
		if (value == null) {
			return "0";
		}
		return value.toString();
	}

}