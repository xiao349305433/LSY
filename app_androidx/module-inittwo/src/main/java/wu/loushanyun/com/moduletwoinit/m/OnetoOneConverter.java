package wu.loushanyun.com.moduletwoinit.m;

import android.os.Parcel;
import android.os.Parcelable;

import com.wu.loushanyun.base.util.LouShanYunUtils;

import org.litepal.crud.LitePalSupport;

import met.hx.com.librarybase.some_utils.XHStringUtil;

public class OnetoOneConverter extends LitePalSupport implements Parcelable {
    private String areaNumber;//区域号
    private Integer loginId;//登录id
    private String sn;
    private String productForm = "3";//产品形式
    private String frequency;//上送频率
    private String firmwareVersion;//固件版本
    private String hardwareVersion;//硬件版本
    private String softVersion;//软件版本
    private String sensingSignal;//传感信号
    private String powerType = "0";//电源类型
    private String powerState;//电源状态
    private String equipmentPower = "0";//电源电压
    private String equipmentTime;//出厂日期
    private String equipmentType = "0";//设备接入类型
    private String remark;//备注
    private String caliber = "0";//口径;
    private String sf;//扩频
    private String channel;//信道参数
    private String rssi = "0";//信号强度
    private String snr = "0";//信噪比
    private String image;
    private String imagePath;
    //ver 2017/09/07
    private String sendingPower;//发送功率
    /**
     * 水表信息
     */
    private String productName;//产品名称
    private String gatherScene = "2";//采集场景
    private String paramOrUnit = "2";//参数单位
    private String measuringMode;//测量方式
    private String joinForm;//接入类型
    private String userId;//设备Id
    private String meterNumber = "1";//表号
    private String ADCDRMax;//最高压力值
    private String ADCDRMin;//最低压力值
    private String impulseInitial;//初始脉冲
    private String pulseConstant;//脉冲常数
    private String flowDirection;//流向
    private String disassemblyState;//拆卸
    private String magneticDisturb;//磁场干扰
    private String sensorState = "0";//传感器状态
    private String valveState = "0";//阀状态
    private String readMeterState = "0";//读表状态
    private String manufacturersIdentification;//厂家标识
    private String factoryName;//厂家名称
    private String meterType;//型号  (口径+单位)
    private String realMeterReading;//旧设备表读数  (m³)

    public String printAll() {
        StringBuffer stringBuffer = new StringBuffer();
        if (!XHStringUtil.isEmpty(areaNumber, false)) {
            stringBuffer.append(" 网格号：" + areaNumber + '\n');
        }
        if (!XHStringUtil.isEmpty(sn, false)) {
            stringBuffer.append(" 物联sn号：" + sn + '\n');
        }
        stringBuffer.append(" 产品形式：" + "远传物联网端" + '\n');
        if (!XHStringUtil.isEmpty(frequency, false)) {
            stringBuffer.append(" 上送频率：" + frequency + '\n');
        }
        if (!XHStringUtil.isEmpty(firmwareVersion, false)) {
            stringBuffer.append(" 固件版本：" + firmwareVersion + '\n');
        }
        if (!XHStringUtil.isEmpty(hardwareVersion, false)) {
            stringBuffer.append(" 硬件版本：" + hardwareVersion + '\n');
        }
        if (!XHStringUtil.isEmpty(softVersion, false)) {
            stringBuffer.append(" 软件版本：" + softVersion + '\n');
        }
        if (!XHStringUtil.isEmpty(sensingSignal, false)) {
            stringBuffer.append(" 传感信号：" + LouShanYunUtils.getCGXHReadStringByCode(Long.parseLong(sensingSignal)) + '\n');
        }
        if (!XHStringUtil.isEmpty(powerState, false)) {
            stringBuffer.append(" 电源状态：" + (powerState.equals("0") ? "正常" : "异常") + '\n');
        }
        if (!XHStringUtil.isEmpty(equipmentTime, false)) {
            stringBuffer.append(" 出厂日期：" + equipmentTime + '\n');
        }
        if (!XHStringUtil.isEmpty(remark, false)) {
            stringBuffer.append(" 备注：" + remark + '\n');
        }
        if (!XHStringUtil.isEmpty(sf, false)) {
            stringBuffer.append(" 扩频：" + sf + '\n');
        }
        if (!XHStringUtil.isEmpty(channel, false)) {
            stringBuffer.append(" 信道参数：" + channel + '\n');
        }
        if (!XHStringUtil.isEmpty(rssi, false)) {
            stringBuffer.append(" 信号强度：" + rssi + '\n');
        }
        if (!XHStringUtil.isEmpty(snr, false)) {
            stringBuffer.append(" 信噪比：" + snr + '\n');
        }
        if (!XHStringUtil.isEmpty(sendingPower, false)) {
            stringBuffer.append(" 发送功率：" + sendingPower + '\n');
        }
        if (!XHStringUtil.isEmpty(joinForm, false)) {
            stringBuffer.append(" 接入类型：" + (joinForm.equals("1") ? "模拟信号" : "数字信号") + '\n');
        }
        if (!XHStringUtil.isEmpty(userId, false)) {
            stringBuffer.append(" 设备Id：" + userId + '\n');
        }
        if (!XHStringUtil.isEmpty(meterNumber, false)) {
            stringBuffer.append(" 表号：" + meterNumber + '\n');
        }
        if (!XHStringUtil.isEmpty(impulseInitial, false)) {
            stringBuffer.append(" 初始脉冲：" + impulseInitial + '\n');
        }
        if (!XHStringUtil.isEmpty(pulseConstant, false)) {
            stringBuffer.append(" 脉冲常数(个/m³)：" + pulseConstant + '\n');
        }
        if (!XHStringUtil.isEmpty(flowDirection, false)) {
            stringBuffer.append(" 流向：" + (flowDirection.equals("0") ? "正流" : "倒流") + '\n');
        }
        if (!XHStringUtil.isEmpty(disassemblyState, false)) {
            stringBuffer.append(" 拆卸：" + (disassemblyState.equals("0") ? "正常" : "拆卸") + '\n');
        }
        if (!XHStringUtil.isEmpty(magneticDisturb, false)) {
            stringBuffer.append(" 磁场干扰：" + (magneticDisturb.equals("0") ? "正常" : "强磁") + '\n');
        }
        if (!XHStringUtil.isEmpty(sensorState, false)) {
            stringBuffer.append(" 传感器状态：" + (sensorState.equals("0") ? "正常" : "异常") + '\n');
        }
        if (!XHStringUtil.isEmpty(valveState, false)) {
            stringBuffer.append(" 阀状态：" + (valveState.equals("0") ? "正常" : "异常") + '\n');
        }
        if (!XHStringUtil.isEmpty(readMeterState, false)) {
            stringBuffer.append(" 读表状态：" + (readMeterState.equals("0") ? "正常" : "异常") + '\n');
        }
        if (!XHStringUtil.isEmpty(factoryName, false)) {
            stringBuffer.append(" 厂家名称：" + factoryName + '\n');
        }
        if (!XHStringUtil.isEmpty(realMeterReading, false)) {
            stringBuffer.append(" 旧设备表读数(m³)：" + realMeterReading);
        }
        return stringBuffer.toString();
    }

    protected OnetoOneConverter(Parcel in) {
        areaNumber = in.readString();
        if (in.readByte() == 0) {
            loginId = null;
        } else {
            loginId = in.readInt();
        }
        sn = in.readString();
        productForm = in.readString();
        frequency = in.readString();
        firmwareVersion = in.readString();
        hardwareVersion = in.readString();
        softVersion = in.readString();
        sensingSignal = in.readString();
        powerType = in.readString();
        powerState = in.readString();
        equipmentPower = in.readString();
        equipmentTime = in.readString();
        equipmentType = in.readString();
        remark = in.readString();
        caliber = in.readString();
        sf = in.readString();
        channel = in.readString();
        rssi = in.readString();
        snr = in.readString();
        image = in.readString();
        imagePath = in.readString();
        sendingPower = in.readString();
        productName = in.readString();
        gatherScene = in.readString();
        paramOrUnit = in.readString();
        measuringMode = in.readString();
        joinForm = in.readString();
        userId = in.readString();
        meterNumber = in.readString();
        ADCDRMax = in.readString();
        ADCDRMin = in.readString();
        impulseInitial = in.readString();
        pulseConstant = in.readString();
        flowDirection = in.readString();
        disassemblyState = in.readString();
        magneticDisturb = in.readString();
        sensorState = in.readString();
        valveState = in.readString();
        readMeterState = in.readString();
        manufacturersIdentification = in.readString();
        factoryName = in.readString();
        meterType = in.readString();
        realMeterReading = in.readString();
    }

    public static final Creator<OnetoOneConverter> CREATOR = new Creator<OnetoOneConverter>() {
        @Override
        public OnetoOneConverter createFromParcel(Parcel in) {
            return new OnetoOneConverter(in);
        }

        @Override
        public OnetoOneConverter[] newArray(int size) {
            return new OnetoOneConverter[size];
        }
    };

    public String getRealMeterReading() {
        return realMeterReading;
    }

    public void setRealMeterReading(String realMeterReading) {
        this.realMeterReading = realMeterReading;
    }

    public String getAreaNumber() {
        return areaNumber;
    }

    public void setAreaNumber(String areaNumber) {
        this.areaNumber = areaNumber;
    }

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

    public String getProductForm() {
        return productForm;
    }

    public void setProductForm(String productForm) {
        this.productForm = productForm;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSendingPower() {
        return sendingPower;
    }

    public void setSendingPower(String sendingPower) {
        this.sendingPower = sendingPower;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getGatherScene() {
        return gatherScene;
    }

    public void setGatherScene(String gatherScene) {
        this.gatherScene = gatherScene;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMeterNumber() {
        return meterNumber;
    }

    public void setMeterNumber(String meterNumber) {
        this.meterNumber = meterNumber;
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

    public String getValveState() {
        return valveState;
    }

    public void setValveState(String valveState) {
        this.valveState = valveState;
    }

    public String getReadMeterState() {
        return readMeterState;
    }

    public void setReadMeterState(String readMeterState) {
        this.readMeterState = readMeterState;
    }

    public String getManufacturersIdentification() {
        return manufacturersIdentification;
    }

    public void setManufacturersIdentification(String manufacturersIdentification) {
        this.manufacturersIdentification = manufacturersIdentification;
    }

    public String getFactoryName() {
        return factoryName;
    }

    public void setFactoryName(String factoryName) {
        this.factoryName = factoryName;
    }

    public String getMeterType() {
        return meterType;
    }

    public void setMeterType(String meterType) {
        this.meterType = meterType;
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
    public long getBaseObjId() {
        return super.getBaseObjId();
    }

    public OnetoOneConverter() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(areaNumber);
        if (loginId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(loginId);
        }
        dest.writeString(sn);
        dest.writeString(productForm);
        dest.writeString(frequency);
        dest.writeString(firmwareVersion);
        dest.writeString(hardwareVersion);
        dest.writeString(softVersion);
        dest.writeString(sensingSignal);
        dest.writeString(powerType);
        dest.writeString(powerState);
        dest.writeString(equipmentPower);
        dest.writeString(equipmentTime);
        dest.writeString(equipmentType);
        dest.writeString(remark);
        dest.writeString(caliber);
        dest.writeString(sf);
        dest.writeString(channel);
        dest.writeString(rssi);
        dest.writeString(snr);
        dest.writeString(image);
        dest.writeString(imagePath);
        dest.writeString(sendingPower);
        dest.writeString(productName);
        dest.writeString(gatherScene);
        dest.writeString(paramOrUnit);
        dest.writeString(measuringMode);
        dest.writeString(joinForm);
        dest.writeString(userId);
        dest.writeString(meterNumber);
        dest.writeString(ADCDRMax);
        dest.writeString(ADCDRMin);
        dest.writeString(impulseInitial);
        dest.writeString(pulseConstant);
        dest.writeString(flowDirection);
        dest.writeString(disassemblyState);
        dest.writeString(magneticDisturb);
        dest.writeString(sensorState);
        dest.writeString(valveState);
        dest.writeString(readMeterState);
        dest.writeString(manufacturersIdentification);
        dest.writeString(factoryName);
        dest.writeString(meterType);
        dest.writeString(realMeterReading);
    }

    @Override
    public String toString() {
        return "OnetoOneConverter{" +
                "areaNumber='" + areaNumber + '\'' +
                ", loginId=" + loginId +
                ", sn='" + sn + '\'' +
                ", productForm='" + productForm + '\'' +
                ", frequency='" + frequency + '\'' +
                ", firmwareVersion='" + firmwareVersion + '\'' +
                ", hardwareVersion='" + hardwareVersion + '\'' +
                ", softVersion='" + softVersion + '\'' +
                ", sensingSignal='" + sensingSignal + '\'' +
                ", powerType='" + powerType + '\'' +
                ", powerState='" + powerState + '\'' +
                ", equipmentPower='" + equipmentPower + '\'' +
                ", equipmentTime='" + equipmentTime + '\'' +
                ", equipmentType='" + equipmentType + '\'' +
                ", remark='" + remark + '\'' +
                ", caliber='" + caliber + '\'' +
                ", sf='" + sf + '\'' +
                ", channel='" + channel + '\'' +
                ", rssi='" + rssi + '\'' +
                ", snr='" + snr + '\'' +
                ", image='" + image + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", sendingPower='" + sendingPower + '\'' +
                ", productName='" + productName + '\'' +
                ", gatherScene='" + gatherScene + '\'' +
                ", paramOrUnit='" + paramOrUnit + '\'' +
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
                ", readMeterState='" + readMeterState + '\'' +
                ", manufacturersIdentification='" + manufacturersIdentification + '\'' +
                ", factoryName='" + factoryName + '\'' +
                ", meterType='" + meterType + '\'' +
                ", realMeterReading='" + realMeterReading + '\'' +
                '}';
    }
}