package wu.loushanyun.com.moduletwoinit.m;


import org.litepal.crud.LitePalSupport;

import java.util.List;

public class InsidePublicMeter extends LitePalSupport {
    private String sn;
    private String loginId;//登录Id

    private String lat;//经度
    private String lon;//纬度
    private String provinces;//省份
    private String cities;//市
    private String counties;//县城
    private String businessLicense;//营业执照序号
    private List<String> image;
    private List<String> imagePath;
    private String address;//详细地址

    private String paramOrUnit;//参数单位
    private String frequency;//上送频率
    private String networkRetryNumber;//网络交互
    private String caliber;//口径
    private String remark;//备注#
    private String sendingPower;//发送功率
    private String sf;//扩频
    private String snr;//信噪比
    private String rssi;//信号强度
    private String channel;//信道参数
    private String jiHuoTime;//激活时间

    private String sensingSignalModule;//传感模式 1累计脉冲;2状态切换;3数字信号

    private String parseData;//现场通过配置的协议序列按顺序读取到第三方数据（数字信号#）

    private String meterStatus;//表状态位 0正，1负（状态切换#）
    private String abnormityWorkTime = "0";//负状态时长，单位:s（状态切换）
    private String normalWorkTime = "0";//正状态时长，单位:s（状态切换）

    private String impulseInitial;//初始脉冲数（累计脉冲）
    private String rate;//初始倍率（累计脉冲#数字信号#）

    private String magneticInterferenceState = "-1";//磁场干扰状态
    private String disassemblyState = "-1";//拆卸状态
    private String valveControlState = "-1";//阀门状态:1开，0关
    private String backflowState = "-1";//倒流状态
    private String valveControlBreakDownState = "-1";//阀门故障状态：0正常，1故障
    private String batteryStatus = "-1";//电源状态


    public String printInfo() {
        return "sn=" + sn + '\n' +
                "经度=" + lat + '\n' +
                "激活时间=" + jiHuoTime + '\n' +
                "纬度=" + lon + '\n' +
                "省=" + provinces + '\n' +
                "市=" + cities + '\n' +
                "县=" + counties + '\n' +
                "地址=" + address + '\n' +
                "单位=" + paramOrUnit + '\n' +
                "上送频率=" + frequency + '\n' +
                "口径=" + caliber + '\n' +
                "备注=" + remark + '\n' +
                "发送功率=" + sendingPower + '\n' +
                "网络交互=" + networkRetryNumber + '\n' +
                "扩频=" + sf + '\n' +
                "信噪比=" + snr + '\n' +
                "信号强度=" + rssi + '\n' +
                "信道参数=" + channel + '\n' +
                "传感模式=" + sensingSignalModule + '\n' +
                "现场通过配置的协议序列按顺序读取到第三方数据（数字信号#）=" + parseData + '\n' +
                "表状态位(状态切换)=" + meterStatus + '\n' +
                "负状态时长(状态切换)=" + abnormityWorkTime + '\n' +
                "正状态时长(状态切换)=" + normalWorkTime + '\n' +
                "初始脉冲数（累计脉冲）=" + impulseInitial + '\n' +
                "初始倍率（累计脉冲#数字信号#）=" + rate + '\n';
    }

    public String getNetworkRetryNumber() {
        return networkRetryNumber;
    }

    public void setNetworkRetryNumber(String networkRetryNumber) {
        this.networkRetryNumber = networkRetryNumber;
    }

    public List<String> getImagePath() {
        return imagePath;
    }

    public void setImagePath(List<String> imagePath) {
        this.imagePath = imagePath;
    }

    public String getJiHuoTime() {
        return jiHuoTime;
    }

    public void setJiHuoTime(String jiHuoTime) {
        this.jiHuoTime = jiHuoTime;
    }

    @Override
    public long getBaseObjId() {
        return super.getBaseObjId();
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getProvinces() {
        return provinces;
    }

    public void setProvinces(String provinces) {
        this.provinces = provinces;
    }

    public String getCities() {
        return cities;
    }

    public void setCities(String cities) {
        this.cities = cities;
    }

    public String getCounties() {
        return counties;
    }

    public void setCounties(String counties) {
        this.counties = counties;
    }

    public String getBusinessLicense() {
        return businessLicense;
    }

    public void setBusinessLicense(String businessLicense) {
        this.businessLicense = businessLicense;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public List<String> getImage() {
        return image;
    }

    public void setImage(List<String> image) {
        this.image = image;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getParamOrUnit() {
        return paramOrUnit;
    }

    public void setParamOrUnit(String paramOrUnit) {
        this.paramOrUnit = paramOrUnit;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getCaliber() {
        return caliber;
    }

    public void setCaliber(String caliber) {
        this.caliber = caliber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSendingPower() {
        return sendingPower;
    }

    public void setSendingPower(String sendingPower) {
        this.sendingPower = sendingPower;
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

    public String getRssi() {
        return rssi;
    }

    public void setRssi(String rssi) {
        this.rssi = rssi;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getSensingSignalModule() {
        return sensingSignalModule;
    }

    public void setSensingSignalModule(String sensingSignalModule) {
        this.sensingSignalModule = sensingSignalModule;
    }

    public String getParseData() {
        return parseData;
    }

    public void setParseData(String parseData) {
        this.parseData = parseData;
    }

    public String getMeterStatus() {
        return meterStatus;
    }

    public void setMeterStatus(String meterStatus) {
        this.meterStatus = meterStatus;
    }

    public String getAbnormityWorkTime() {
        return abnormityWorkTime;
    }

    public void setAbnormityWorkTime(String abnormityWorkTime) {
        this.abnormityWorkTime = abnormityWorkTime;
    }

    public String getNormalWorkTime() {
        return normalWorkTime;
    }

    public void setNormalWorkTime(String normalWorkTime) {
        this.normalWorkTime = normalWorkTime;
    }

    public String getImpulseInitial() {
        return impulseInitial;
    }

    public void setImpulseInitial(String impulseInitial) {
        this.impulseInitial = impulseInitial;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getMagneticInterferenceState() {
        return magneticInterferenceState;
    }

    public void setMagneticInterferenceState(String magneticInterferenceState) {
        this.magneticInterferenceState = magneticInterferenceState;
    }

    public String getDisassemblyState() {
        return disassemblyState;
    }

    public void setDisassemblyState(String disassemblyState) {
        this.disassemblyState = disassemblyState;
    }

    public String getValveControlState() {
        return valveControlState;
    }

    public void setValveControlState(String valveControlState) {
        this.valveControlState = valveControlState;
    }

    public String getBackflowState() {
        return backflowState;
    }

    public void setBackflowState(String backflowState) {
        this.backflowState = backflowState;
    }

    public String getValveControlBreakDownState() {
        return valveControlBreakDownState;
    }

    public void setValveControlBreakDownState(String valveControlBreakDownState) {
        this.valveControlBreakDownState = valveControlBreakDownState;
    }

    public String getBatteryStatus() {
        return batteryStatus;
    }

    public void setBatteryStatus(String batteryStatus) {
        this.batteryStatus = batteryStatus;
    }
}
