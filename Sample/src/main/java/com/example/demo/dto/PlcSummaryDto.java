package com.example.demo.dto;

import static com.example.demo.common.util.CalcUtil.*;

import java.math.BigDecimal;

import lombok.Value;

/**
 * PLC集計データDTO
 * 
 * A品、B品、D品の枚数を保持する不変オブジェクト
 */
@Value
public class PlcSummaryDto {

	/** A品 */
	int a;

	/** B品 */
	int b;

	/** D品 */
	int d;

	//	@Value を使用することで以下のコンストラクタを自動生成
	// また、メンバに private final が自動付与されるため修飾子は付けない
	//	public PlcSummaryDto(int a, int b, int d) {
	//		this.a = a;
	//		this.b = b;
	//		this.d = d;
	//	}

	/**
	* 各品質に指定された値を加算した新しいDTOを返す
	* 
	* @param addA A品に加算する値
	* @param addB B品に加算する値
	* @param addD D品に加算する値
	* @return 加算後の新しいPlcSummaryDto
	*/
	public PlcSummaryDto add(int addA, int addB, int addD) {
		return new PlcSummaryDto(
				this.a + addA,
				this.b + addB,
				this.d + addD);
	}

	/**
	 * 別のPlcSummaryDtoと加算した新しいDTOを返す
	 * 
	 * @param other 加算するPlcSummaryDto
	 * @return 加算後の新しいPlcSummaryDto
	 */
	public PlcSummaryDto add(PlcSummaryDto other) {
		if (other == null)
			return this; // nullなら自分を返す
		return add(other.a, other.b, other.d);
	}

	/**
	 * 空のインスタンスを作成
	 * 
	 * @return 全て0のPlcSummaryDto
	 */
	public static PlcSummaryDto empty() {
		return new PlcSummaryDto(0, 0, 0);
	}

	/**
	 * 合計枚数を取得
	 * 
	 * @return A品 + B品 + D品の合計
	 */
	public int getTotal() {
		return this.a + this.b + this.d;
	}

	/**
	 * A品率を取得
	 * 
	 * @return A品率（％）
	 */
	public BigDecimal getARate() {
		return calcRate(this.a);
	}

	/**
	 * B品率を取得
	 * 
	 * @return B品率（％）
	 */
	public BigDecimal getBRate() {
		return calcRate(this.b);
	}

	/**
	 * D品率を取得
	 * 
	 * @return D品率（％）
	 */
	public BigDecimal getDRate() {
		return calcRate(this.d);
	}

	/**
	 * 率計算
	 */
	private BigDecimal calcRate(int part) {
		int total = getTotal();
		if (total == 0) {
			return BigDecimal.ZERO;
		}
		return toPercent(div(BigDecimal.valueOf(part), BigDecimal.valueOf(total)));
	}
}
