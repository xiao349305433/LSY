package com.loushanyun.modulefactory.m;

public class ErrorCode {
	private String msg;
	private Integer code;
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	@Override
	public String toString() {
		return "ErrorCode [msg=" + msg + ", code=" + code + "]";
	}
	

}
