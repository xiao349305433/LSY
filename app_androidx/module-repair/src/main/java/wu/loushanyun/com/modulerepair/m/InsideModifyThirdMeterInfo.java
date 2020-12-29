package wu.loushanyun.com.modulerepair.m;

import org.litepal.crud.LitePalSupport;

import met.hx.com.librarybase.some_utils.TimeUtils;

public class InsideModifyThirdMeterInfo extends LitePalSupport {
    private String newMeterId;// 新设备Id
    private String sn;// 集中器sn
    private String loginId;// id
    private String oldMeterId;// 旧设备Id
    private String meterNumber;// 表号
    private String impulseInitial;// 初始脉冲
    private String pulseConstant;// 脉冲常数
    private String flowDirection;// 流向
    private String disassemblyState;// 拆卸
    private String magneticDisturb;// 磁场干扰
    private String sensorState;// 传感器状态
    private String powerState;// 自身电源状态
    private String readMeterState;// 读表状态
    private String time;// 时间
    private String realMeterReading;//旧设备表读数  (m³)

    private String sendingPower;//发送功率
    private String sf;//扩频
    private String snr;//信噪比
    private String rssi;//信号强度
    private String channel;//信道

    public InsideModifyThirdMeterInfo() {
        this.time = TimeUtils.getCurTimeString();
    }

    @Override
    public String toString() {
        return "InsideModifyThirdMeterInfo{" +
                "newMeterId='" + newMeterId + '\'' +
                ", sn='" + sn + '\'' +
                ", loginId='" + loginId + '\'' +
                ", oldMeterId='" + oldMeterId + '\'' +
                ", meterNumber='" + meterNumber + '\'' +
                ", impulseInitial='" + impulseInitial + '\'' +
                ", pulseConstant='" + pulseConstant + '\'' +
                ", flowDirection='" + flowDirection + '\'' +
                ", disassemblyState='" + disassemblyState + '\'' +
                ", magneticDisturb='" + magneticDisturb + '\'' +
                ", sensorState='" + sensorState + '\'' +
                ", powerState='" + powerState + '\'' +
                ", readMeterState='" + readMeterState + '\'' +
                ", time='" + time + '\'' +
                ", realMeterReading='" + realMeterReading + '\'' +
                ", sendingPower='" + sendingPower + '\'' +
                ", sf='" + sf + '\'' +
                ", snr='" + snr + '\'' +
                ", rssi='" + rssi + '\'' +
                ", channel='" + channel + '\'' +
                '}';
    }

    public String getRealMeterReading() {
        return realMeterReading;
    }

    public void setRealMeterReading(String realMeterReading) {
        this.realMeterReading = realMeterReading;
    }

    @Override
    public long getBaseObjId() {
        return super.getBaseObjId();
    }

    public String getNewMeterId() {
        return newMeterId;
    }

    public void setNewMeterId(String newMeterId) {
        this.newMeterId = newMeterId;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getLoginId() {
        return loginId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getOldMeterId() {
        return oldMeterId;
    }

    public void setOldMeterId(String oldMeterId) {
        this.oldMeterId = oldMeterId;
    }

    public String getMeterNumber() {
        return meterNumber;
    }

    public void setMeterNumber(String meterNumber) {
        this.meterNumber = meterNumber;
    }

    public String getImpulseInitial() {
        return impulseInitial;
    }

    public void setImpulseInitial(String impulseInitial) {
        this.impulseInitial = impulseInitial;
    }

    public String getPulseConstant() {
        return pulseConstant;
    }

    public void setPulseConstant(String pulseConstant) {
        this.pulseConstant = pulseConstant;
    }

    public String getFlowDirection() {
        return flowDirection;
    }

    public void setFlowDirection(String flowDirection) {
        this.flowDirection = flowDirection;
    }

    public String getDisassemblyState() {
        return disassemblyState;
    }

    public void setDisassemblyState(String disassemblyState) {
        this.disassemblyState = disassemblyState;
    }

    public String getMagneticDisturb() {
        return magneticDisturb;
    }

    public void setMagneticDisturb(String magneticDisturb) {
        this.magneticDisturb = magneticDisturb;
    }

    public String getSensorState() {
        return sensorState;
    }

    public void setSensorState(String sensorState) {
        this.sensorState = sensorState;
    }

    public String getPowerState() {
        return powerState;
    }

    public void setPowerState(String powerState) {
        this.powerState = powerState;
    }

    public String getReadMeterState() {
        return readMeterState;
    }

    public void setReadMeterState(String readMeterState) {
        this.readMeterState = readMeterState;
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
}