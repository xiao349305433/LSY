package wu.loushanyun.com.sixapp.m;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class EnvironmentInfInfo implements Parcelable {

    /**
     * responseTime : 1589874408545
     * message : success
     * code : 0
     * datas : [{"envName":"集中器生产线1","signalIntensity":"-52","signalRatio":"0","spreadFactor":"SF9","feedback":"是","delayParameter":"5","feedbackNum":"1","channelParam":"80","sendFrequency":"24小时","demarcatePerson":"袁知博","demarcateTime":"2020-05-17 18:43:48","baseNum":"40B37F17C6BE77F5","remarks":"模式A","channel":"486.3,486.5,486.7,486.9,487.1,487.3,487.5,487.7","bottomNoise":"-99.4,-98.8,-98.2,-99.6,-98.4,-98.0,-99.4,-98.6","frequencyRange":"CN470"}]
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

    public static class DatasBean implements Parcelable {
        /**
         * envName : 集中器生产线1
         * signalIntensity : -52
         * signalRatio : 0
         * spreadFactor : SF9
         * feedback : 是
         * delayParameter : 5
         * feedbackNum : 1
         * channelParam : 80
         * sendFrequency : 24小时
         * demarcatePerson : 袁知博
         * demarcateTime : 2020-05-17 18:43:48
         * baseNum : 40B37F17C6BE77F5
         * remarks : 模式A
         * channel : 486.3,486.5,486.7,486.9,487.1,487.3,487.5,487.7
         * bottomNoise : -99.4,-98.8,-98.2,-99.6,-98.4,-98.0,-99.4,-98.6
         * frequencyRange : CN470
         */

        private String envName;
        private String signalIntensity;
        private String signalRatio;
        private String spreadFactor;
        private String feedback;
        private int delayParameter;
        private int feedbackNum;
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

        public String getSendPower() {
            return sendPower;
        }

        public void setSendPower(String sendPower) {
            this.sendPower = sendPower;
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

        public int getFeedbackNum() {
            return feedbackNum;
        }

        public void setFeedbackNum(int feedbackNum) {
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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.envName);
            dest.writeString(this.signalIntensity);
            dest.writeString(this.signalRatio);
            dest.writeString(this.spreadFactor);
            dest.writeString(this.feedback);
            dest.writeInt(this.delayParameter);
            dest.writeInt(this.feedbackNum);
            dest.writeInt(this.channelParam);
            dest.writeString(this.sendFrequency);
            dest.writeString(this.demarcatePerson);
            dest.writeString(this.demarcateTime);
            dest.writeString(this.baseNum);
            dest.writeString(this.remarks);
            dest.writeString(this.channel);
            dest.writeString(this.bottomNoise);
            dest.writeString(this.frequencyRange);
            dest.writeString(this.sendPower);
        }

        public DatasBean() {
        }

        protected DatasBean(Parcel in) {
            this.envName = in.readString();
            this.signalIntensity = in.readString();
            this.signalRatio = in.readString();
            this.spreadFactor = in.readString();
            this.feedback = in.readString();
            this.delayParameter = in.readInt();
            this.feedbackNum = in.readInt();
            this.channelParam = in.readInt();
            this.sendFrequency = in.readString();
            this.demarcatePerson = in.readString();
            this.demarcateTime = in.readString();
            this.baseNum = in.readString();
            this.remarks = in.readString();
            this.channel = in.readString();
            this.bottomNoise = in.readString();
            this.frequencyRange = in.readString();
            this.sendPower = in.readString();
        }

        public static final Creator<DatasBean> CREATOR = new Creator<DatasBean>() {
            @Override
            public DatasBean createFromParcel(Parcel source) {
                return new DatasBean(source);
            }

            @Override
            public DatasBean[] newArray(int size) {
                return new DatasBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.responseTime);
        dest.writeString(this.message);
        dest.writeInt(this.code);
        dest.writeList(this.datas);
    }

    public EnvironmentInfInfo() {
    }

    protected EnvironmentInfInfo(Parcel in) {
        this.responseTime = in.readLong();
        this.message = in.readString();
        this.code = in.readInt();
        this.datas = new ArrayList<DatasBean>();
        in.readList(this.datas, DatasBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<EnvironmentInfInfo> CREATOR = new Parcelable.Creator<EnvironmentInfInfo>() {
        @Override
        public EnvironmentInfInfo createFromParcel(Parcel source) {
            return new EnvironmentInfInfo(source);
        }

        @Override
        public EnvironmentInfInfo[] newArray(int size) {
            return new EnvironmentInfInfo[size];
        }
    };
}
