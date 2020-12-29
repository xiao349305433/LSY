package com.loushanyun.modulefactory.m;


public class FactorysSettingsModule4 {
    private String id;
    private String sn;
    private String useScene;//使用场景:1公用 2民用
    private String productForm;//产品形式
    private String hardwareVersion;//硬件版本
    private String softVersion;//软件版本
    private String firmwareVersion;//固件版本
    private String productionTime;//出厂时间
    private String sensingSignalModule;//传感模式 1累计脉冲;2状态切换;3数字信号
    private String sensingSignal;//传感信号
    private String powerType;//电源类型 0物联电池  1外接电源
    private String meterId;//设备ID
    private String manufacturersIdentification;//厂家标识
    private String protocolVersion;//自定义协议版本号
    private String rate;//倍率
    private String batteryStatus;//电池状态0,1
    private String bps;//波特率
    private String impulse;//脉冲
    private String magneticInterferenceState;//磁场干扰
    private String disassemblyState;//拆卸
    private String valveControlState;//阀门状态:1开，0关
    private String backflowState;//倒流
    private String valveControlBreakDownState;//阀门故障
    private String meterStatus;//状态位 0正  1负
    private String minusStatus;//负状态时常;单位:s
    private String correctStatus;//正状态时常;单位:s
    private String parseData;//自定义协议解析出的数据

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getUseScene() {
        return useScene;
    }

    public void setUseScene(String useScene) {
        this.useScene = useScene;
    }

    public String getProductForm() {
        return productForm;
    }

    public void setProductForm(String productForm) {
        this.productForm = productForm;
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

    public String getFirmwareVersion() {
        return firmwareVersion;
    }

    public void setFirmwareVersion(String firmwareVersion) {
        this.firmwareVersion = firmwareVersion;
    }

    public String getProductionTime() {
        return productionTime;
    }

    public void setProductionTime(String productionTime) {
        this.productionTime = productionTime;
    }

    public String getSensingSignalModule() {
        return sensingSignalModule;
    }

    public void setSensingSignalModule(String sensingSignalModule) {
        this.sensingSignalModule = sensingSignalModule;
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

    public String getMeterId() {
        return meterId;
    }

    public void setMeterId(String meterId) {
        this.meterId = meterId;
    }

    public String getManufacturersIdentification() {
        return manufacturersIdentification;
    }

    public void setManufacturersIdentification(String manufacturersIdentification) {
        this.manufacturersIdentification = manufacturersIdentification;
    }

    public String getProtocolVersion() {
        return protocolVersion;
    }

    public void setProtocolVersion(String protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getBatteryStatus() {
        return batteryStatus;
    }

    public void setBatteryStatus(String batteryStatus) {
        this.batteryStatus = batteryStatus;
    }

    public String getBps() {
        return bps;
    }

    public void setBps(String bps) {
        this.bps = bps;
    }

    public String getImpulse() {
        return impulse;
    }

    public void setImpulse(String impulse) {
        this.impulse = impulse;
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

    public String getMeterStatus() {
        return meterStatus;
    }

    public void setMeterStatus(String meterStatus) {
        this.meterStatus = meterStatus;
    }

    public String getMinusStatus() {
        return minusStatus;
    }

    public void setMinusStatus(String minusStatus) {
        this.minusStatus = minusStatus;
    }

    public String getCorrectStatus() {
        return correctStatus;
    }

    public void setCorrectStatus(String correctStatus) {
        this.correctStatus = correctStatus;
    }

    public String getParseData() {
        return parseData;
    }

    public void setParseData(String parseData) {
        this.parseData = parseData;
    }
}