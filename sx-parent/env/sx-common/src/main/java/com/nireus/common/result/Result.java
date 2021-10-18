package com.nireus.common.result;

import java.io.Serializable;

/**
 * 
 * <p>
 * 统一返回结果
 * </p>
 * 
 * @author wushixiong
 * @since 2021-05-26 16:57:42
 * @param <T>
 */
public class Result<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	private String code;

	private T data;

	private String msg;

	private Integer total;

	public static <T> Result<T> success() {
		return success(null);
	}

	public static <T> Result<T> success(T data) {
		ResultCode rce = ResultCode.SUCCESS;
		if (data instanceof Boolean && Boolean.FALSE.equals(data)) {
			rce = ResultCode.SYSTEM_EXECUTION_ERROR;
		}
		return result(rce, data);
	}

	public static <T> Result<T> success(T data, Long total) {
		Result<T> result = new Result();
		result.setCode(ResultCode.SUCCESS.getCode());
		result.setMsg(ResultCode.SUCCESS.getMsg());
		result.setData(data);
		result.setTotal(total.intValue());
		return result;
	}

	public static <T> Result<T> failed() {
		return result(ResultCode.SYSTEM_EXECUTION_ERROR.getCode(), ResultCode.SYSTEM_EXECUTION_ERROR.getMsg(), null);
	}

	public static <T> Result<T> failed(String msg) {
		return result(ResultCode.SYSTEM_EXECUTION_ERROR.getCode(), msg, null);
	}

	public static <T> Result<T> judge(boolean status) {
		if (status) {
			return success();
		} else {
			return failed();
		}
	}

	public static <T> Result<T> failed(IResultCode resultCode) {
		return result(resultCode.getCode(), resultCode.getMsg(), null);
	}

	private static <T> Result<T> result(IResultCode resultCode, T data) {
		return result(resultCode.getCode(), resultCode.getMsg(), data);
	}

	private static <T> Result<T> result(String code, String msg, T data) {
		Result<T> result = new Result<T>();
		result.setCode(code);
		result.setData(data);
		result.setMsg(msg);
		return result;
	}

	public static boolean isSuccess(Result result) {
		if (result != null && ResultCode.SUCCESS.getCode().equals(result.getCode())) {
			return true;
		}
		return false;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	@Override
	public String toString() {
		return "Result [code=" + code + ", data=" + data + ", msg=" + msg + ", total=" + total + "]";
	}
}
