package com.example.demo.common.enums;

import com.example.demo.entity.SizeInfoEntity;

public enum DisplayPattern {

	/** 再カット */
	RECUT,

	/** 枚数計上しない */
	NOT_COUNTED,

	/** 格上げ */
	UPGRADE,

	/** 未稼働（再サンダー） */
	NOT_WORKING,

	/** 通常 */
	NORMAL;

	/**
	 * 判定ロジック
	 */
	public static DisplayPattern judge(SizeInfoEntity e) {

		if (!"0".equals(e.getRecutKbn())) {
			return RECUT;
		}
		if ("0".equals(e.getCountKbn())) {
			return NOT_COUNTED;
		}
		if ("1".equals(e.getUpgradeKbn())) {
			return UPGRADE;
		}
		if ("1".equals(e.getNotWorkingKbn())) {
			return NOT_WORKING;
		}
		return NORMAL;
	}
}
