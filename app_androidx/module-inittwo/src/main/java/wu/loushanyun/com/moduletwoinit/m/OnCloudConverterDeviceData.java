package wu.loushanyun.com.moduletwoinit.m;

import com.sensoro.sensor.kit.entity.SensoroDevice;

public class OnCloudConverterDeviceData {
    private SensoroDevice device;
    private String sensingSignal;//传感信号
    private String manufacturersName;
    private String equipmentSN;
    private String remark;
    private long equipmentTime;
    private boolean isCloud;


    public OnCloudConverterDeviceData(String sensingSignal, String manufacturersName, String equipmentSN, String remark, long equipmentTime, boolean isCloud) {
        this.sensingSignal = sensingSignal;
        this.manufacturersName = manufacturersName;
        this.equipmentSN = equipmentSN;
        this.remark = remark;
        this.equipmentTime = equipmentTime;
        this.isCloud = isCloud;
    }
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof OnCloudConverterDeviceData) {
            OnCloudConverterDeviceData converterDeviceData= (OnCloudConverterDeviceData) obj;
            return equipmentSN.equals(converterDeviceData.equipmentSN);
        }
        return false;
    }

    public OnCloudConverterDeviceData(SensoroDevice device, String equipmentSN, boolean isCloud) {
        this.device = device;
        this.equipmentSN = equipmentSN;
        this.isCloud = isCloud;
    }

    public boolean isCloud() {
        return isCloud;
    }

    public void setCloud(boolean cloud) {
        isCloud = cloud;
    }

    public SensoroDevice getDevice() {
        return device;
    }

    public void setDevice(SensoroDevice device) {
        this.device = device;
    }

    public String getSensingSignal() {
        return sensingSignal;
    }

    public void setSensingSignal(String sensingSignal) {
        this.sensingSignal = sensingSignal;
    }

    public String getManufacturersName() {
        return manufacturersName;
    }

    public void setManufacturersName(String manufacturersName) {
        this.manufacturersName = manufacturersName;
    }

    public String getEquipmentSN() {
        return equipmentSN;
    }

    public void setEquipmentSN(String equipmentSN) {
        this.equipmentSN = equipmentSN;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public long getEquipmentTime() {
        return equipmentTime;
    }

    public void setEquipmentTime(long equipmentTime) {
        this.equipmentTime = equipmentTime;
    }
}
