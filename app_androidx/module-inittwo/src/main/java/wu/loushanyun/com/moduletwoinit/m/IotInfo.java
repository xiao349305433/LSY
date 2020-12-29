package wu.loushanyun.com.moduletwoinit.m;

public class IotInfo {

    /**
     * msg : success
     * code : 0
     * data : {"disassemblyState":0,"joinForm":2,"pulseConstant":100,"caliber":40,"impulseInitial":66,"channel":"模式A","remark":"JJ破记录了","frequency":"72小时","sensingSignal":"2EV","initialData":"0.66","manufacturersName":21,"sf":"SF9","rate":0.01,"backflowState":0,"imageUrl":"http://www.loushanyun.net/static/LouShanCloudAllImage/123456789456414/locationImages/2018year/07month/11day/102d1404-39a9-437a-8424-4dbd2602a7f1.png","magneticInterferenceState":0,"productionTime":1530547200000,"sensorState":0}
     */

    private String msg;
    private int code;
    private DataBean data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * disassemblyState : 0
         * joinForm : 2
         * pulseConstant : 100
         * caliber : 40
         * impulseInitial : 66
         * channel : 模式A
         * remark : JJ破记录了
         * frequency : 72小时
         * sensingSignal : 2EV
         * initialData : 0.66
         * manufacturersName : 21
         * sf : SF9
         * rate : 0.01
         * backflowState : 0
         * imageUrl : http://www.loushanyun.net/static/LouShanCloudAllImage/123456789456414/locationImages/2018year/07month/11day/102d1404-39a9-437a-8424-4dbd2602a7f1.png
         * magneticInterferenceState : 0
         * productionTime : 1530547200000
         * sensorState : 0
         */

        private int disassemblyState;
        private int joinForm;
        private double pulseConstant;
        private int caliber;
        private int impulseInitial;
        private String channel;
        private String remark;
        private String frequency;
        private String sensingSignal;
        private String initialData;
        private int manufacturersName;
        private String sf;
        private double rate;
        private int backflowState;
        private String imageUrl;
        private int magneticInterferenceState;
        private long productionTime;
        private int sensorState;

        public int getDisassemblyState() {
            return disassemblyState;
        }

        public void setDisassemblyState(int disassemblyState) {
            this.disassemblyState = disassemblyState;
        }

        public int getJoinForm() {
            return joinForm;
        }

        public void setJoinForm(int joinForm) {
            this.joinForm = joinForm;
        }


        public double getPulseConstant() {
            return pulseConstant;
        }

        public void setPulseConstant(double pulseConstant) {
            this.pulseConstant = pulseConstant;
        }

        public int getCaliber() {
            return caliber;
        }

        public void setCaliber(int caliber) {
            this.caliber = caliber;
        }

        public int getImpulseInitial() {
            return impulseInitial;
        }

        public void setImpulseInitial(int impulseInitial) {
            this.impulseInitial = impulseInitial;
        }

        public String getChannel() {
            return channel;
        }

        public void setChannel(String channel) {
            this.channel = channel;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getFrequency() {
            return frequency;
        }

        public void setFrequency(String frequency) {
            this.frequency = frequency;
        }

        public String getSensingSignal() {
            return sensingSignal;
        }

        public void setSensingSignal(String sensingSignal) {
            this.sensingSignal = sensingSignal;
        }

        public String getInitialData() {
            return initialData;
        }

        public void setInitialData(String initialData) {
            this.initialData = initialData;
        }

        public int getManufacturersName() {
            return manufacturersName;
        }

        public void setManufacturersName(int manufacturersName) {
            this.manufacturersName = manufacturersName;
        }

        public String getSf() {
            return sf;
        }

        public void setSf(String sf) {
            this.sf = sf;
        }

        public double getRate() {
            return rate;
        }

        public void setRate(double rate) {
            this.rate = rate;
        }

        public int getBackflowState() {
            return backflowState;
        }

        public void setBackflowState(int backflowState) {
            this.backflowState = backflowState;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public int getMagneticInterferenceState() {
            return magneticInterferenceState;
        }

        public void setMagneticInterferenceState(int magneticInterferenceState) {
            this.magneticInterferenceState = magneticInterferenceState;
        }

        public long getProductionTime() {
            return productionTime;
        }

        public void setProductionTime(long productionTime) {
            this.productionTime = productionTime;
        }

        public int getSensorState() {
            return sensorState;
        }

        public void setSensorState(int sensorState) {
            this.sensorState = sensorState;
        }
    }
}
