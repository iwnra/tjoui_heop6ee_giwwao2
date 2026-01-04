package com.example.demo.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.demo.dto.PlcSummaryDto;

import lombok.Data;

/**
 * サイズ情報エンティティ
 */
@Data
public class SizeInfoEntity {
	private Long id; // ID
    private Integer sizeNo;
    private BigDecimal thickness; // 厚み
    private Integer width;     // 幅
    private Integer length;    // 長さ
    private String material;  // 材種
    private Integer count;     // 枚数
	private String operationKbn; // 稼働区分
	private Integer a; // A品
	private Integer b; // B品
	private Integer d; // D品
	LocalDateTime endDate; // 終了日時

//	public PlcSummaryDto toPlcSummaryDto() {
//		return new PlcSummaryDto(this.a, this.b, this.d);
//	}

	/**
	 * PlcSummaryDtoに変換
	 */
	public PlcSummaryDto toPlcSummaryDto() {
	    return new PlcSummaryDto(
	        a != null ? a : 0,
	        b != null ? b : 0,
	        d != null ? d : 0
	    );
	}
}
