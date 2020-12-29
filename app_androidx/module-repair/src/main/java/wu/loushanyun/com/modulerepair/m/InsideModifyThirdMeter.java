package wu.loushanyun.com.modulerepair.m;

import java.util.List;

public class InsideModifyThirdMeter {

	private String sn;// 操作的sn
	private Integer loginId;// 操作登录Id
	//更换的水表  列表信息
	private List<InsideModifyThirdMeterInfo> insideModifyThirdMeterInfos;


	public Integer getLoginId() {
		return loginId;
	}

	public void setLoginId(Integer loginId) {
		this.loginId = loginId;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public List<InsideModifyThirdMeterInfo> getInsideModifyThirdMeterInfos() {
		return insideModifyThirdMeterInfos;
	}

	public void setInsideModifyThirdMeterInfos(
			List<InsideModifyThirdMeterInfo> insideModifyThirdMeterInfos) {
		this.insideModifyThirdMeterInfos = insideModifyThirdMeterInfos;
	}

}
