package com.example.demo.common.util;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * BigDecimal計算用ラッパークラス
 * 
 * 使用例： BigDecimal result = Calc.from(100) .add(50) .mul(1.08) .sub(20)
 * .result();
 */
public final class Calc {

	private final BigDecimal value;

	// Excel精度に準拠
	private static final MathContext MC = MathContext.DECIMAL64;

	// 内部コンストラクタ
	private Calc(BigDecimal value) {
		// this.value = Optional.ofNullable(value).orElse(BigDecimal.ZERO);
		this.value = value;
	}

	/**
	 * 計算を開始するためのインスタンスを生成
	 */
	public static Calc from(Object value) {
		return new Calc(toBigDecimal(value));
	}

	/**
	 * 加算（this + n）
	 */
	public Calc add(Object n) {
		return new Calc(this.value.add(toBigDecimal(n), MC));
	}

	/**
	 * 減算（this - n）
	 */
	public Calc sub(Object n) {
		return new Calc(this.value.subtract(toBigDecimal(n), MC));
	}

	/**
	 * 乗算（this * n）
	 */
	public Calc mul(Object n) {
		return new Calc(this.value.multiply(toBigDecimal(n), MC));
	}

	/**
	 * 除算（this / n）
	 */
	public Calc div(Object n) {
		BigDecimal bd = toBigDecimal(n);
		if (bd.compareTo(BigDecimal.ZERO) == 0) {
			return new Calc(BigDecimal.ZERO);
		}
		return new Calc(this.value.divide(bd, MC));
	}

	/**
	 * べき乗（this ^ n）
	 */
	public Calc pow(int n) {
		return new Calc(this.value.pow(n, MC));
	}

	/**
	 * 指定の桁数で四捨五入
	 */
	public Calc round(int scale) {
		return new Calc(this.value.setScale(scale, RoundingMode.HALF_UP));
	}

	/**
	 * 計算結果を取得
	 */
	public BigDecimal result() {
		return this.value;
	}

	/**
	 * ユーティリティ: 型を BigDecimal に変換
	 */
	private static BigDecimal toBigDecimal(Object object) {
		if (object == null) {
			// nullの場合は0として扱う
			return BigDecimal.ZERO;
		}
		if (object instanceof BigDecimal bd) {
			return bd;
		}
		if (object instanceof Integer i) {
			return BigDecimal.valueOf(i);
		}
		if (object instanceof Long l) {
			return BigDecimal.valueOf(l);
		}
		if (object instanceof Short s) {
			return BigDecimal.valueOf(s);
		}
		if (object instanceof Byte b) {
			return BigDecimal.valueOf(b);
		}
		if (object instanceof Double d) {
			return BigDecimal.valueOf(d);
		}
		if (object instanceof Float f) {
			return BigDecimal.valueOf(f);
		}
		if (object instanceof String s) {
			// 空文字は0として扱う
			return s.isBlank() ? BigDecimal.ZERO : new BigDecimal(s);
		}
		throw new IllegalArgumentException("Unsupported type: " + object.getClass());
	}

	// /**
	// * 指定値より大きいか判定
	// */
	// public boolean isGreaterThan(Object n) {
	// return this.value.compareTo(toBigDecimal(n)) > 0;
	// }
	//
	// /**
	// * 指定値以上か判定
	// */
	// public boolean isGreaterThanOrEqual(Object n) {
	// return this.value.compareTo(toBigDecimal(n)) >= 0;
	// }
	//
	// /**
	// * 指定値より小さいか判定
	// */
	// public boolean isLessThan(Object n) {
	// return this.value.compareTo(toBigDecimal(n)) < 0;
	// }
	//
	// /**
	// * 指定値以下か判定
	// */
	// public boolean isLessThanOrEqual(Object n) {
	// return this.value.compareTo(toBigDecimal(n)) <= 0;
	// }

	// // --- 比較メソッド (指定したscaleで四捨五入してから比較) ---
	//
	// /** 大なり (this > threshold) */
	// public boolean isGt(Object threshold, int scale) {
	// return round(scale).compareTo(toBigDecimal(threshold)) > 0;
	// }
	//
	// /** 小なり (this < threshold) */
	// public boolean isLt(Object threshold, int scale) {
	// return round(scale).compareTo(toBigDecimal(threshold)) < 0;
	// }
	//
	// /** 以上 (this >= threshold) */
	// public boolean isGe(Object threshold, int scale) {
	// return round(scale).compareTo(toBigDecimal(threshold)) >= 0;
	// }
	//
	// /** 以下 (this <= threshold) */
	// public boolean isLe(Object threshold, int scale) {
	// return round(scale).compareTo(toBigDecimal(threshold)) <= 0;
	// }
	//
	// /** 等しい (this == threshold) */
	// public boolean isEq(Object threshold, int scale) {
	// return round(scale).compareTo(toBigDecimal(threshold)) == 0;
	// }
	//
	// /** 内部用：指定スケールでの丸め処理 */
	// private BigDecimal round(int scale) {
	// return this.value.setScale(scale, RoundingMode.HALF_UP);
	// }
}