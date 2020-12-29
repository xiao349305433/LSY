package wu.loushanyun.com.libraryfive.m;

/**
 * 配置信息存储实体类
 */
public class SaveDataConverter {
    private String frequency;//上送频率//
    private String firmwareVersion;//固件版本(没有)
    private String hardwareVersion;//硬件版本//
    private String softVersion;//软件版本//
    private String sensingSignal;//传感信号//
    private String powerType;//电源类型//
    private String powerState;//电源状态//
    private String equipmentPower;//电源电压
    private String equipmentTime;//出厂日期//
    private String equipmentType;//设备接入类型//
    private String pulseConstant;//脉冲常数（传默认0）//
    private String remark;//备注//
    private String caliber;//口径;//
    private String provinces;//省份//
    private String cities;//市//
    private String counties;//县城//
    private String sf;//扩频
    private String channel;//信道参数
    private String rssi;//信号强度
    private String snr;//信噪比
    private String startMeterNumber;//开始表号//
    private String endMeterNumber;//结束表号//

    //ver 2017/09/07
    private String sendingPower;//发送功率

    public String getSendingPower() {
        return sendingPower;
    }

    public void setSendingPower(String sendingPower) {
        this.sendingPower = sendingPower;
    }

    public SaveDataConverter(){

    }

    public String getRssi() {
        return rssi;
    }

    public void setRssi(String rssi) {
        this.rssi = rssi;
    }

    public String getSnr() {
        return snr;
    }

    public void setSnr(String snr) {
        this.snr = snr;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getFirmwareVersion() {
        return firmwareVersion;
    }

    public void setFirmwareVersion(String firmwareVersion) {
        this.firmwareVersion = firmwareVersion;
    }

    public String getHardwareVersion() {
        return hardwareVersion;
    }

    public void setHardwareVersion(String hardwareVersion) {
        this.hardwareVersion = hardwareVersion;
    }

    public String getSoftVersion() {
        return softVersion;
    }

    public void setSoftVersion(String softVersion) {
        this.softVersion = softVersion;
    }

    public String getSensingSignal() {
        return sensingSignal;
    }

    public void setSensingSignal(String sensingSignal) {
        this.sensingSignal = sensingSignal;
    }

    public String getPowerType() {
        return powerType;
    }

    public void setPowerType(String powerType) {
        this.powerType = powerType;
    }

    public String getPowerState() {
        return powerState;
    }

    public void setPowerState(String powerState) {
        this.powerState = powerState;
    }

    public String getEquipmentPower() {
        return equipmentPower;
    }

    public void setEquipmentPower(String equipmentPower) {
        this.equipmentPower = equipmentPower;
    }

    public String getEquipmentTime() {
        return equipmentTime;
    }

    public void setEquipmentTime(String equipmentTime) {
        this.equipmentTime = equipmentTime;
    }

    public String getEquipmentType() {
        return equipmentType;
    }

    public void setEquipmentType(String equipmentType) {
        this.equipmentType = equipmentType;
    }

    public String getPulseConstant() {
        return pulseConstant;
    }

    public void setPulseConstant(String pulseConstant) {
        this.pulseConstant = pulseConstant;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCaliber() {
        return caliber;
    }

    public void setCaliber(String caliber) {
        this.caliber = "0";
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

    public String getStartMeterNumber() {
        return startMeterNumber;
    }

    public void setStartMeterNumber(String startMeterNumber) {
        this.startMeterNumber = startMeterNumber;
    }

    public String getEndMeterNumber() {
        return endMeterNumber;
    }

    public void setEndMeterNumber(String endMeterNumber) {
        this.endMeterNumber = endMeterNumber;
    }

    public String getSf() {
        return sf;
    }

    public void setSf(String sf) {
        this.sf = sf;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    @Override
    public String toString() {
        return "SaveDataConverter{" +
                "frequency='" + frequency + '\'' +
                ", firmwareVersion='" + firmwareVersion + '\'' +
                ", hardwareVersion='" + hardwareVersion + '\'' +
                ", softVersion='" + softVersion + '\'' +
                ", sensingSignal='" + sensingSignal + '\'' +
                ", powerType='" + powerType + '\'' +
                ", powerState='" + powerState + '\'' +
                ", equipmentPower='" + equipmentPower + '\'' +
                ", equipmentTime='" + equipmentTime + '\'' +
                ", equipmentType='" + equipmentType + '\'' +
                ", pulseConstant='" + pulseConstant + '\'' +
                ", remark='" + remark + '\'' +
                ", caliber='" + caliber + '\'' +
                ", provinces='" + provinces + '\'' +
                ", cities='" + cities + '\'' +
                ", counties='" + counties + '\'' +
                ", sf='" + sf + '\'' +
                ", channel='" + channel + '\'' +
                ", rssi='" + rssi + '\'' +
                ", snr='" + snr + '\'' +
                ", startMeterNumber='" + startMeterNumber + '\'' +
                ", endMeterNumber='" + endMeterNumber + '\'' +
                ", sendingPower='" + sendingPower + '\'' +
                '}';
    }
}
