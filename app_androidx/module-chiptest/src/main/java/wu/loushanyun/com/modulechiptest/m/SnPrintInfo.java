package wu.loushanyun.com.modulechiptest.m;

import com.sensoro.sensor.kit.entity.SensoroDevice;

public class SnPrintInfo {
    private int num;
    private SensoroDevice sensoroDevice;
    private String tishi = "";
    private String token;
    private String rxDelay;
    private String rssi;
    private String snr;
    private String sendStatus;
    private String firmwareVersion;
    private boolean IsSend;
    private boolean IsUpData;

    public boolean isUpData() {
        return IsUpData;
    }

    public void setUpData(boolean upData) {
        IsUpData = upData;
    }

    public boolean isSend() {
        return IsSend;
    }

    public void setSend(boolean send) {
        IsSend = send;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SnPrintInfo) {
            SnPrintInfo snPrintInfo = (SnPrintInfo) obj;
            return sensoroDevice.equals(snPrintInfo.getSensoroDevice());
        }
        return false;
    }

    public SnPrintInfo(int num, SensoroDevice sensoroDevice, String token, String rxDelay, String firmwareVersion) {
        this.num = num;
        this.sensoroDevice = sensoroDevice;
        this.token = token;
        this.rxDelay = rxDelay;
        this.firmwareVersion = firmwareVersion;
    }

    public SnPrintInfo(int num, SensoroDevice sensoroDevice, String token, String rxDelay) {
        this.num = num;
        this.sensoroDevice = sensoroDevice;
        this.token = token;
        this.rxDelay = rxDelay;
    }

    public String getFirmwareVersion() {
        return firmwareVersion;
    }

    public void setFirmwareVersion(String firmwareVersion) {
        this.firmwareVersion = firmwareVersion;
    }

    public String getSnr() {
        return snr;
    }

    public String getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(String sendStatus) {
        this.sendStatus = sendStatus;
    }

    @Override
    public String toString() {
        return "SnPrintInfo{" +
                "num=" + num +
                ", sensoroDevice=" + sensoroDevice +
                ", tishi='" + tishi + '\'' +
                ", token='" + token + '\'' +
                ", rxDelay='" + rxDelay + '\'' +
                ", rssi='" + rssi + '\'' +
                ", snr='" + snr + '\'' +
                ", sendStatus='" + sendStatus + '\'' +
                '}';
    }

    public void setSnr(String snr) {
        this.snr = snr;
    }

    public String getToken() {
        return token;
    }

    public String getRssi() {
        return rssi;
    }

    public void setRssi(String rssi) {
        this.rssi = rssi;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRxDelay() {
        return rxDelay;
    }

    public void setRxDelay(String rxDelay) {
        this.rxDelay = rxDelay;
    }

    public String getTishi() {
        return tishi;
    }

    public void setTishi(String tishi) {
        this.tishi = tishi;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public SensoroDevice getSensoroDevice() {
        return sensoroDevice;
    }

    public void setSensoroDevice(SensoroDevice sensoroDevice) {
        this.sensoroDevice = sensoroDevice;
    }
}
