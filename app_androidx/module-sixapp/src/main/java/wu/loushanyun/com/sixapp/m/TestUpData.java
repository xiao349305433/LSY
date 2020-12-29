package wu.loushanyun.com.sixapp.m;

public class TestUpData {

    /*
    *
    id	int	主键id
test_time	varchar	质检时间
manufacturer_id	varchar	制造商账号id
isStandard	varchar	是否合格 0：不合格 1：合格
is_distribution	varchar	是否分配
is_deliver	varchar	是否发货
batch_number	varchar	批次号
order_number	varchar	订单号
collect_scene	varchar	采集场景 1: "公用",2: "民用",3: "第三方公用",4: "第三方家用"
product_form	varchar
产品形式 0: "压力传感器",1: "工业水表（信号输出）",2: "工业类电子类计量表",3: "远传物联网端（1对一物联网水表）",4: "远传表号接入",5: "11位手机号标志",6: "流量计",7: "井盖开关预警产品",8: "浸水判断预警产品",9: "电源开关类产品",10: "集中器"
data_source	varchar	数据源 0:APP,1: XLS文件,2:消息推送
chip_number	varchar	芯片号
body_number	varchar	表身号
goods_name	varchar	产品名称
model	varchar	型号
caliber	varchar	口径
lot_type	varchar	物联设备类型 1: "LORAWAN_S",2: "NB",3: "其他"
job_number	varchar	工号
product_features	varchar	产品特性
device_id	varchar	设备ID
lot_sn	varchar	物联SN
signal_intensity	varchar	信号强度
signal_ratio	varchar	信噪比
battery_status	varchar	电池状态
spread_factor	varchar	扩频因子
channel_param	varchar	信道参数
send_power	varchar	发送功率
factory_dication	varchar	出厂读数
magnification	varchar	倍率/脉冲常数
software_version	varchar	软件版本号
hardware_version	varchar	硬件版本号
sense_signal	varchar	传感信号
send_frequency	varchar	发送频率
signal_type	varchar	信号类型
battery	varchar	电池
complex_capacity	varchar	复合电容
bluetooth	varchar	蓝牙
watchcase	varchar	表壳
range_ratio	varchar	量程比
movement_type	varchar	机芯类型
valve	varchar	阀
max_connect_number	varchar	最大连接数
delay_parameter	varchar	延时参数
 businessName  服务商名称
subBusinessName  制造商名称
     */


    private int id;
    private Long testTime;
    private String manufacturerId;
    private String isStandard; //是否合格
    private String is_distribution;
    private String is_deliver;
    private String batchNumber;
    private String orderNumber;
    private String collectScene;
    private String productForm;
    private int goodsId;
    private String dataSource;//数据源 0:APP 1: XLS文件 2:消息推送
    private String chipNumber; //芯片号  产品特性1（Lorawan芯片）+设备ID+SN 产品特性2（模组）+设备ID+SN
    private String bodyNumber;
    private String goodsName;
    private String model;
    private String caliber;
    private String lotType;
    private String jobNumber;
    private String productFeatures;
    private String deviceId;
    private String lotSn;
    private String signalIntensity;
    private String signalRatio;
    private String batteryStatus;
    private String spreadFactor;
    private String channelParam;
    private String sendPower;
    private String factoryDication;
    private String magnification;
    private String softwareVersion;
    private String hardwareVersion;
    private String senseSignal;
    private String sendFrequency;
    private String signalType;
    private String battery;//18505,24615
    private String complexCapacity;
    private String bluetooth;
    private String watchcase;
    private String rangeRatio;
    private String movement_type;
    private String valve;
    private String max_connect_number; //64
    private int delayParameter;
    private int loginId;
    private int orderId;
    private int authId;
    private String businessName ;
    private String subBusinessName ;
    private int envId;
    private int testId;
    private String feedback;
    private String feedbackNum;

    public String getFeedbackNum() {
        return feedbackNum;
    }

    public void setFeedbackNum(String feedbackNum) {
        this.feedbackNum = feedbackNum;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public int getTestId() {
        return testId;
    }

    public void setTestId(int testId) {
        this.testId = testId;
    }

    public int getEnvId() {
        return envId;
    }

    public void setEnvId(int envId) {
        this.envId = envId;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getSubBusinessName() {
        return subBusinessName;
    }

    public void setSubBusinessName(String subBusinessName) {
        this.subBusinessName = subBusinessName;
    }

    public int getLoginId() {
        return loginId;
    }

    public void setLoginId(int loginId) {
        this.loginId = loginId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getAuthId() {
        return authId;
    }

    public void setAuthId(int authId) {
        this.authId = authId;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }


    public Long getTestTime() {
        return testTime;
    }

    public void setTestTime(Long testTime) {
        this.testTime = testTime;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }



    public String getBodyNumber() {
        return bodyNumber;
    }

    public void setBodyNumber(String bodyNumber) {
        this.bodyNumber = bodyNumber;
    }

    public String getChipNumber() {
        return chipNumber;
    }

    public void setChipNumber(String chipNumber) {
        this.chipNumber = chipNumber;
    }


    public String getRangeRatio() {
        return rangeRatio;
    }

    public void setRangeRatio(String rangeRatio) {
        this.rangeRatio = rangeRatio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Long getTest_time() {
        return testTime;
    }

    public void setTest_time(Long testTime) {
        this.testTime = testTime;
    }

    public String getManufacturerId() {
        return manufacturerId;
    }

    public void setManufacturerId(String manufacturerId) {
        this.manufacturerId = manufacturerId;
    }

    public String getIsStandard() {
        return isStandard;
    }

    public void setIsStandard(String isStandard) {
        this.isStandard = isStandard;
    }

    public String getIs_distribution() {
        return is_distribution;
    }

    public void setIs_distribution(String is_distribution) {
        this.is_distribution = is_distribution;
    }

    public String getIs_deliver() {
        return is_deliver;
    }

    public void setIs_deliver(String is_deliver) {
        this.is_deliver = is_deliver;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }



    public String getCollectScene() {
        return collectScene;
    }

    public void setCollectScene(String collectScene) {
        this.collectScene = collectScene;
    }

    public String getProductForm() {
        return productForm;
    }

    public void setProductForm(String productForm) {
        this.productForm = productForm;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }


    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getCaliber() {
        return caliber;
    }

    public void setCaliber(String caliber) {
        this.caliber = caliber;
    }

    public String getLotType() {
        return lotType;
    }

    public void setLotType(String lotType) {
        this.lotType = lotType;
    }

    public String getJobNumber() {
        return jobNumber;
    }

    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
    }

    public String getProductFeatures() {
        return productFeatures;
    }

    public void setProductFeatures(String productFeatures) {
        this.productFeatures = productFeatures;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getLotSn() {
        return lotSn;
    }

    public void setLotSn(String lotSn) {
        this.lotSn = lotSn;
    }

    public String getSignalIntensity() {
        return signalIntensity;
    }

    public void setSignalIntensity(String signalIntensity) {
        this.signalIntensity = signalIntensity;
    }

    public String getSignalRatio() {
        return signalRatio;
    }

    public void setSignalRatio(String signalRatio) {
        this.signalRatio = signalRatio;
    }

    public String getBatteryStatus() {
        return batteryStatus;
    }

    public void setBatteryStatus(String batteryStatus) {
        this.batteryStatus = batteryStatus;
    }

    public String getSpreadFactor() {
        return spreadFactor;
    }

    public void setSpreadFactor(String spreadFactor) {
        this.spreadFactor = spreadFactor;
    }

    public String getChannelParam() {
        return channelParam;
    }

    public void setChannelParam(String channelParam) {
        this.channelParam = channelParam;
    }

    public String getSendPower() {
        return sendPower;
    }

    public void setSendPower(String sendPower) {
        this.sendPower = sendPower;
    }

    public String getFactoryDication() {
        return factoryDication;
    }

    public void setFactoryDication(String factoryDication) {
        this.factoryDication = factoryDication;
    }

    public String getMagnification() {
        return magnification;
    }

    public void setMagnification(String magnification) {
        this.magnification = magnification;
    }

    public String getSoftwareVersion() {
        return softwareVersion;
    }

    public void setSoftwareVersion(String softwareVersion) {
        this.softwareVersion = softwareVersion;
    }

    public String getHardwareVersion() {
        return hardwareVersion;
    }

    public void setHardwareVersion(String hardwareVersion) {
        this.hardwareVersion = hardwareVersion;
    }

    public String getSenseSignal() {
        return senseSignal;
    }

    public void setSenseSignal(String senseSignal) {
        this.senseSignal = senseSignal;
    }

    public String getSendFrequency() {
        return sendFrequency;
    }

    public void setSendFrequency(String sendFrequency) {
        this.sendFrequency = sendFrequency;
    }

    public String getSignalType() {
        return signalType;
    }

    public void setSignalType(String signalType) {
        this.signalType = signalType;
    }

    public String getBattery() {
        return battery;
    }

    public void setBattery(String battery) {
        this.battery = battery;
    }

    public String getComplexCapacity() {
        return complexCapacity;
    }

    public void setComplexCapacity(String complexCapacity) {
        this.complexCapacity = complexCapacity;
    }

    public String getBluetooth() {
        return bluetooth;
    }

    public void setBluetooth(String bluetooth) {
        this.bluetooth = bluetooth;
    }

    public String getWatchcase() {
        return watchcase;
    }

    public void setWatchcase(String watchcase) {
        this.watchcase = watchcase;
    }



    public String getMovement_type() {
        return movement_type;
    }

    public void setMovement_type(String movement_type) {
        this.movement_type = movement_type;
    }

    public String getValve() {
        return valve;
    }

    public void setValve(String valve) {
        this.valve = valve;
    }

    public String getMax_connect_number() {
        return max_connect_number;
    }

    public void setMax_connect_number(String max_connect_number) {
        this.max_connect_number = max_connect_number;
    }

    public int getDelayParameter() {
        return delayParameter;
    }

    public void setDelayParameter(int delayParameter) {
        this.delayParameter = delayParameter;
    }
}
