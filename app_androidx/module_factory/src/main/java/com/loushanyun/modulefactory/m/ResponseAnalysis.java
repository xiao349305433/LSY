package com.loushanyun.modulefactory.m;

import java.util.List;

public class ResponseAnalysis {

	private String msg;
	private Integer code;
	private String data;
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
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	@Override
	public String toString() {
		return "ResponseAnalysis [msg=" + msg + ", code=" + code + ", data="
				+ data + "]";
	}
	
	
	

	

}



