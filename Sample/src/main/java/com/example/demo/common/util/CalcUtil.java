package com.example.demo.common.util;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public final class CalcUtil {

	// privateコンストラクタで、外部からの new を禁止
	private CalcUtil() {
		// インスタンス化しようとしたら例外を投げる（より厳格なやり方）
		throw new UnsupportedOperationException("Utility class");
	}

	// Excel精度に準拠
	// MathContext.DECIMAL64 は Excel の精度（15~17桁）に近く、
	// かつ「割り切れない場合のエラー」を防ぐために有効です。
	private static final MathContext MC = MathContext.DECIMAL64;

	/**
	 * 加算（a + b）
	 */
	public static BigDecimal add(BigDecimal a, BigDecimal b) {
		if (a == null || b == null) {
			return BigDecimal.ZERO;
		}
		return a.add(b, MC);
	}

	/**
	 * 減算（a - b）
	 */
	public static BigDecimal sub(BigDecimal a, BigDecimal b) {
		if (a == null || b == null) {
			return BigDecimal.ZERO;
		}
		return a.subtract(b, MC);
	}

	/**
	 * 乗算（a * b）
	 */
	public static BigDecimal mul(BigDecimal a, BigDecimal b) {
		if (a == null || b == null) {
			return BigDecimal.ZERO;
		}
		return a.multiply(b, MC);
	}

	/**
	 * 除算（a / b）
	 */
	public static BigDecimal div(BigDecimal a, BigDecimal b) {
		// if (a == null || b == null || b.signum() == 0) {
		if (a == null || b == null || b.compareTo(BigDecimal.ZERO) == 0) {
			return BigDecimal.ZERO;
		}
		return a.divide(b, MC);
	}

	/**
	 * べき乗
	 * 
	 * @param base     底
	 * @param exponent 指数
	 */
	public static BigDecimal pow(BigDecimal base, int exponent) {
		if (base == null) {
			return BigDecimal.ZERO;
		}
		return base.pow(exponent, MC);
	}

	/**
	 * パーセントに変換（小数第二位で四捨五入）
	 */
	public static BigDecimal toPercent(BigDecimal value) {
		if (value == null) {
			return BigDecimal.ZERO;
		}
		return mul(value, BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP);
	}

	// a.compareTo(b) > 0 ... a > b
	// a.compareTo(b) == 0 ... a == b
	// a.compareTo(b) < 0 ... a < b

	/**
	 * Long (long) を BigDecimal に変換
	 */
	public static BigDecimal toBigDecimal(Long value) {
		return (value == null) ? BigDecimal.ZERO : BigDecimal.valueOf(value);
	}

	/**
	 * Integer (int) を BigDecimal に変換
	 */
	public static BigDecimal toBigDecimal(Integer value) {
		return (value == null) ? BigDecimal.ZERO : BigDecimal.valueOf(value);
	}

	/**
	 * Double (double) を BigDecimal に変換
	 */
	public static BigDecimal toBigDecimal(Double value) {
		// BigDecimal.valueOf は内部で Double.toString(double) を使うため、
		// double 特有の浮動小数点誤差を回避して生成してくれます。
		return (value == null) ? BigDecimal.ZERO : BigDecimal.valueOf(value);
	}

	/**
	 * String を BigDecimal に変換
	 */
	public static BigDecimal toBigDecimal(String value) {
		if (value == null || value.isBlank()) {
			return BigDecimal.ZERO;
		}

		try {
			return new BigDecimal(value.trim());
		} catch (NumberFormatException e) {
			return BigDecimal.ZERO;
		}
	}

	/**
	 * 指定の桁数で四捨五入
	 */
	public static BigDecimal round(BigDecimal value, int scale) {
		return value.setScale(scale, RoundingMode.HALF_UP);
	}
}