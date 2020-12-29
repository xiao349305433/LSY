package wu.loushanyun.com.libraryfive;

import java.util.List;

public class ResponseAnalysis {
	private Integer err_code;
	private String err_msg;
	private List<String> data;
	
	public Integer getErr_code() {
		return err_code;
	}
	public void setErr_code(Integer err_code) {
		this.err_code = err_code;
	}
	public String getErr_msg() {
		return err_msg;
	}
	public void setErr_msg(String err_msg) {
		this.err_msg = err_msg;
	}
	public List<String> getData() {
		return data;
	}
	public void setData(List<String> data) {
		this.data = data;
	}
	@Override
	public String toString() {
		return "ResultResponse [err_code=" + err_code + ", err_msg=" + err_msg
				+ ", data=" + data + "]";
	}
	
}
