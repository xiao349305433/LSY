package wu.loushanyun.com.sixapp.m;

import java.util.List;

public class EnvironmentInfInfo2 {

    /**
     * responseTime : 1600326726899
     * message : success
     * code : 0
     * datas : [{"envName":"二号模组生产线1","signalIntensity":"＞-55","signalRatio":"＞0","spreadFactor":"SF9","feedback":"是","delayParameter":"5","feedbackNum":"1","channelParam":"80","sendFrequency":"24小时","demarcatePerson":"袁知博","demarcateTime":"2020-06-17 19:46:51","baseNum":"40B37F17C6BE77F5","remarks":"模式A","channel":"486.3,486.5,486.7,486.9,487.1,487.3,487.5,487.7","bottomNoise":"-94.8,-95.6,-95.4,-96.2,-98.0,-95.6,-95.8,-94.8","frequencyRange":"CN470","sendPower":"20dBm"},{"envName":"二号模组生产线2","signalIntensity":"＞-23","signalRatio":"＞0","spreadFactor":"SF9","feedback":"是","delayParameter":"5","feedbackNum":"1","channelParam":"0","sendFrequency":"24小时","demarcatePerson":"袁知博","demarcateTime":"2020-06-17 19:50:44","baseNum":"02317F17C647F0AA","remarks":"模式B","channel":"470.3,470.5,470.7,470.9,471.1,471.3,471.5,471.7","bottomNoise":"-103.2,-102.4,-100.0,-98.6,-96.2,-92.8,-95.8,-97.2","frequencyRange":"CN470","sendPower":"20dBm"}]
     */

    private long responseTime;
    private String message;
    private int code;
    private List<DatasBean> datas;

    public long getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(long responseTime) {
        this.responseTime = responseTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<DatasBean> getDatas() {
        return datas;
    }

    public void setDatas(List<DatasBean> datas) {
        this.datas = datas;
    }

    public static class DatasBean {
        /**
         * envName : 二号模组生产线1
         * signalIntensity : ＞-55
         * signalRatio : ＞0
         * spreadFactor : SF9
         * feedback : 是
         * delayParameter : 5
         * feedbackNum : 1
         * channelParam : 80
         * sendFrequency : 24小时
         * demarcatePerson : 袁知博
         * demarcateTime : 2020-06-17 19:46:51
         * baseNum : 40B37F17C6BE77F5
         * remarks : 模式A
         * channel : 486.3,486.5,486.7,486.9,487.1,487.3,487.5,487.7
         * bottomNoise : -94.8,-95.6,-95.4,-96.2,-98.0,-95.6,-95.8,-94.8
         * frequencyRange : CN470
         * sendPower : 20dBm
         */
        private int id;
        private String envName;
        private String signalIntensity;
        private String signalRatio;
        private String spreadFactor;
        private String feedback;
        private int delayParameter;
        private String feedbackNum;
        private int channelParam;
        private String sendFrequency;
        private String demarcatePerson;
        private String demarcateTime;
        private String baseNum;
        private String remarks;
        private String channel;
        private String bottomNoise;
        private String frequencyRange;
        private String sendPower;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getEnvName() {
            return envName;
        }

        public void setEnvName(String envName) {
            this.envName = envName;
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

        public String getSpreadFactor() {
            return spreadFactor;
        }

        public void setSpreadFactor(String spreadFactor) {
            this.spreadFactor = spreadFactor;
        }

        public String getFeedback() {
            return feedback;
        }

        public void setFeedback(String feedback) {
            this.feedback = feedback;
        }

        public int getDelayParameter() {
            return delayParameter;
        }

        public void setDelayParameter(int delayParameter) {
            this.delayParameter = delayParameter;
        }

        public String getFeedbackNum() {
            return feedbackNum;
        }

        public void setFeedbackNum(String feedbackNum) {
            this.feedbackNum = feedbackNum;
        }

        public int getChannelParam() {
            return channelParam;
        }

        public void setChannelParam(int channelParam) {
            this.channelParam = channelParam;
        }

        public String getSendFrequency() {
            return sendFrequency;
        }

        public void setSendFrequency(String sendFrequency) {
            this.sendFrequency = sendFrequency;
        }

        public String getDemarcatePerson() {
            return demarcatePerson;
        }

        public void setDemarcatePerson(String demarcatePerson) {
            this.demarcatePerson = demarcatePerson;
        }

        public String getDemarcateTime() {
            return demarcateTime;
        }

        public void setDemarcateTime(String demarcateTime) {
            this.demarcateTime = demarcateTime;
        }

        public String getBaseNum() {
            return baseNum;
        }

        public void setBaseNum(String baseNum) {
            this.baseNum = baseNum;
        }

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }

        public String getChannel() {
            return channel;
        }

        public void setChannel(String channel) {
            this.channel = channel;
        }

        public String getBottomNoise() {
            return bottomNoise;
        }

        public void setBottomNoise(String bottomNoise) {
            this.bottomNoise = bottomNoise;
        }

        public String getFrequencyRange() {
            return frequencyRange;
        }

        public void setFrequencyRange(String frequencyRange) {
            this.frequencyRange = frequencyRange;
        }

        public String getSendPower() {
            return sendPower;
        }

        public void setSendPower(String sendPower) {
            this.sendPower = sendPower;
        }
    }
}
