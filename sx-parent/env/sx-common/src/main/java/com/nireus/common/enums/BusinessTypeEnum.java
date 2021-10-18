package com.nireus.common.enums;

/**
 * 
 * <p>
 * 业务类型枚举
 * </p>
 * 
 * @author wushixiong
 * @since 2021-05-26 17:44:32
 */
public enum BusinessTypeEnum {

	USER("user", 100), MEMBER("member", 200), ORDER("order", 300);

	private String code;

	private Integer value;

	BusinessTypeEnum(String code, Integer value) {
		this.code = code;
		this.value = value;
	}

	public static BusinessTypeEnum getValue(String code) {
		for (BusinessTypeEnum value : values()) {
			if (value.getCode().equals(code)) {
				return value;
			}
		}
		return null;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}
}
