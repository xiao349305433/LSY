package wu.loushanyun.com.sixapp.m;


public class ResponseAnalysisInfo {
	private String msg;
	private String rssi;
	private String code;
	private String sf;
	private String snr;
	private String channel;
	private String band;
	private String sendPower;
	private String frequency;
	private boolean isok;
	private boolean issendok;
	private boolean rssiisok;
	private String maxTimes;
	private String sendTimes;

	public String getMaxTimes() {
		return maxTimes;
	}

	public void setMaxTimes(String maxTimes) {
		this.maxTimes = maxTimes;
	}

	public String getSendTimes() {
		return sendTimes;
	}

	public void setSendTimes(String sendTimes) {
		this.sendTimes = sendTimes;
	}

	public boolean isIssendok() {
		return issendok;
	}

	public void setIssendok(boolean issendok) {
		this.issendok = issendok;
	}

	public boolean isRssiisok() {
		return rssiisok;
	}

	public void setRssiisok(boolean rssiisok) {
		this.rssiisok = rssiisok;
	}

	public boolean isSnrisok() {
		return snrisok;
	}

	public void setSnrisok(boolean snrisok) {
		this.snrisok = snrisok;
	}

	private boolean snrisok;

	public boolean isIsok() {
		return isok;
	}

	public void setIsok(boolean isok) {
		this.isok = isok;
	}

	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getRssi() {
		return rssi;
	}
	public void setRssi(String rssi) {
		this.rssi = rssi;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getSf() {
		return sf;
	}
	public void setSf(String sf) {
		this.sf = sf;
	}
	public String getSnr() {
		return snr;
	}
	public void setSnr(String snr) {
		this.snr = snr;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getBand() {
		return band;
	}
	public void setBand(String band) {
		this.band = band;
	}
	public String getSendPower() {
		return sendPower;
	}
	public void setSendPower(String sendPower) {
		this.sendPower = sendPower;
	}
	public String getFrequency() {
		return frequency;
	}
	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}
	@Override
	public String toString() {
		return "ResponseAnalysisInfo1 [msg=" + msg + ", rssi=" + rssi
				+ ", code=" + code + ", sf=" + sf + ", snr=" + snr
				+ ", channel=" + channel + ", band=" + band + ", sendPower="
				+ sendPower + ", frequency=" + frequency + "]";
	}
	
	
}
