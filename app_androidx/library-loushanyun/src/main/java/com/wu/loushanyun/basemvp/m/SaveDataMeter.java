package com.wu.loushanyun.basemvp.m;


import com.elvishew.xlog.XLog;
import com.wu.loushanyun.base.util.LouShanYunUtils;
import com.wu.loushanyun.base.util.MapParams;

import java.util.HashMap;

/**
 * 读单表的数据存储实体类
 */
public class  SaveDataMeter {
    private String paramOrUnit;//参数单位
    private String measuringMode;//测量方式
    private String joinForm;//接入类型
    private String userId;//设备Id
    private String meterNumber;//表号
    private String ADCDRMax;//最高压力值
    private String ADCDRMin;//最低压力值
    private String impulseInitial;//初始脉冲
    private String pulseConstant = "0";//脉冲常数
    private String flowDirection;//流向
    private String disassemblyState;//拆卸
    private String magneticDisturb;//磁场干扰
    private String sensorState;//传感器状态
    private String valveState;//阀状态
    private String powerState;//自身电源状态
    private String readMeterState;//读表状态
    private String zhenDuanMa;//读表状态
    private String hub;//hub号
    private String type="";//集中器或者3号初始化
    private String modeltype;
    private HashMap<String, String> meterMap;//hashmap

    public HashMap<String, String> getMeterMap() {
        return meterMap;
    }

    public void setMeterMap(HashMap<String, String> meterMap) {
        setDefaultParams(meterMap,paramOrUnit);
    }


    public SaveDataMeter(HashMap<String, String> meterMap,String paramOrUnit) {
        setDefaultParams(meterMap,paramOrUnit);
    }

    private void setDefaultParams(HashMap<String, String> meterMap,String paramOrUnit){
        this.meterMap = meterMap;
        setParamOrUnit(paramOrUnit);//用量是2，压力计是1
        setMeterNumber(meterMap.get(MapParams.表号));
        setUserId(meterMap.get(MapParams.设备ID));
        setImpulseInitial(meterMap.get(MapParams.脉冲数));
        setDisassemblyState(meterMap.get(MapParams.表拆卸状态));
        setPowerState(meterMap.get(MapParams.表电池状态));
        setFlowDirection(meterMap.get(MapParams.表流向状态));
        setMagneticDisturb(meterMap.get(MapParams.表强磁状态));
        setHub(meterMap.get(MapParams.HUB号));
        try {
            setPulseConstant(String.valueOf(1.0 / Double.valueOf(LouShanYunUtils.getBLReadStringByCode(Long.valueOf(meterMap.get(MapParams.倍率))))));
        } catch (Exception e) {
            XLog.e("表倍率解析出错啦", e.getMessage());
        }
    }

    public String getModeltype() {
        return modeltype;
    }

    public void setModeltype(String modeltype) {
        this.modeltype = modeltype;
    }

    public String getHub() {
        return hub;
    }

    public void setHub(String hub) {
        this.hub = hub;
    }

    @Override
    public String toString() {
        return "SaveDataMeter{" +
                "paramOrUnit='" + paramOrUnit + '\'' +
                ", measuringMode='" + measuringMode + '\'' +
                ", joinForm='" + joinForm + '\'' +
                ", userId='" + userId + '\'' +
                ", meterNumber='" + meterNumber + '\'' +
                ", ADCDRMax='" + ADCDRMax + '\'' +
                ", ADCDRMin='" + ADCDRMin + '\'' +
                ", impulseInitial='" + impulseInitial + '\'' +
                ", pulseConstant='" + pulseConstant + '\'' +
                ", flowDirection='" + flowDirection + '\'' +
                ", disassemblyState='" + disassemblyState + '\'' +
                ", magneticDisturb='" + magneticDisturb + '\'' +
                ", sensorState='" + sensorState + '\'' +
                ", valveState='" + valveState + '\'' +
                ", powerState='" + powerState + '\'' +
                ", readMeterState='" + readMeterState + '\'' +
                ", zhenDuanMa='" + zhenDuanMa + '\'' +
                ", hub='" + hub + '\'' +
                '}';
    }

    public String getMeterNumber() {
        return meterNumber;
    }

    public void setMeterNumber(String meterNumber) {
        this.meterNumber = meterNumber;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getPowerState() {
        return powerState;
    }

    public void setPowerState(String powerState) {
        this.powerState = powerState;
    }

    public String getZhenDuanMa() {
        return zhenDuanMa;
    }

    public void setZhenDuanMa(String zhenDuanMa) {
        this.zhenDuanMa = zhenDuanMa;
    }

    public String getReadMeterState() {
        return readMeterState;
    }

    public void setReadMeterState(String readMeterState) {
        this.readMeterState = readMeterState;
    }

    public String getSensorState() {
        return sensorState;
    }

    public void setSensorState(String sensorState) {
        this.sensorState = sensorState;
    }

    public String getValveState() {
        return valveState;
    }

    public void setValveState(String valveState) {
        this.valveState = valveState;
    }

    public String getParamOrUnit() {
        return paramOrUnit;
    }

    public void setParamOrUnit(String paramOrUnit) {
        this.paramOrUnit = paramOrUnit;
    }

    public String getMeasuringMode() {
        return measuringMode;
    }

    public void setMeasuringMode(String measuringMode) {
        this.measuringMode = measuringMode;
    }

    public String getJoinForm() {
        return joinForm;
    }

    public void setJoinForm(String joinForm) {
        this.joinForm = joinForm;
    }

    public String getADCDRMax() {
        return ADCDRMax;
    }

    public void setADCDRMax(String ADCDRMax) {
        this.ADCDRMax = ADCDRMax;
    }

    public String getADCDRMin() {
        return ADCDRMin;
    }

    public void setADCDRMin(String ADCDRMin) {
        this.ADCDRMin = ADCDRMin;
    }

}
