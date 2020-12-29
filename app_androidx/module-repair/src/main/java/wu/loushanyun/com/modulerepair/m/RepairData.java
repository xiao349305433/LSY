package wu.loushanyun.com.modulerepair.m;

import android.os.Parcel;
import android.os.Parcelable;

public class RepairData implements Parcelable{

    /**
     * sn : 01C11117C6375B7E
     * frequencyBand : 470MHZ
     * sendingPower : 17db
     * frequency : 72小时带反馈1次
     * snr : null
     * rssi : null
     * sf :
     * channel :
     * productForm : 远传物联网端
     * version : 1
     * sensingSignal : 3EV
     * remark : 湖北省武汉市江汉区金茂大厦斜对面缨子车友酒店东南约80米
     * longitude : 114.254333
     * latitude : 30.614831
     * offLineTime : 1316
     */

    private String sn;
    private String newSn="";
    private String frequencyBand;
    private String sendingPower;
    private String frequency;
    private Object snr;
    private Object rssi;
    private String sf;
    private String channel;
    private String productForm;
    private String version;
    private String sensingSignal;
    private String remark;
    private String longitude;
    private String latitude;
    private int offLineTime;

    public RepairData() {
    }

    protected RepairData(Parcel in) {
        sn = in.readString();
        newSn = in.readString();
        frequencyBand = in.readString();
        sendingPower = in.readString();
        frequency = in.readString();
        sf = in.readString();
        channel = in.readString();
        productForm = in.readString();
        version = in.readString();
        sensingSignal = in.readString();
        remark = in.readString();
        longitude = in.readString();
        latitude = in.readString();
        offLineTime = in.readInt();
    }

    public static final Creator<RepairData> CREATOR = new Creator<RepairData>() {
        @Override
        public RepairData createFromParcel(Parcel in) {
            return new RepairData(in);
        }

        @Override
        public RepairData[] newArray(int size) {
            return new RepairData[size];
        }
    };

    @Override
    public String toString() {
        return "RepairData{" +
                "sn='" + sn + '\'' +
                ", newSn='" + newSn + '\'' +
                ", frequencyBand='" + frequencyBand + '\'' +
                ", sendingPower='" + sendingPower + '\'' +
                ", frequency='" + frequency + '\'' +
                ", snr=" + snr +
                ", rssi=" + rssi +
                ", sf='" + sf + '\'' +
                ", channel='" + channel + '\'' +
                ", productForm='" + productForm + '\'' +
                ", version='" + version + '\'' +
                ", sensingSignal='" + sensingSignal + '\'' +
                ", remark='" + remark + '\'' +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                ", offLineTime=" + offLineTime +
                '}';
    }


    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getNewSn() {
        return newSn;
    }

    public void setNewSn(String newSn) {
        this.newSn = newSn;
    }

    public String getFrequencyBand() {
        return frequencyBand;
    }

    public void setFrequencyBand(String frequencyBand) {
        this.frequencyBand = frequencyBand;
    }

    public String getSendingPower() {
        return sendingPower;
    }

    public void setSendingPower(String sendingPower) {
        this.sendingPower = sendingPower;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public Object getSnr() {
        return snr;
    }

    public void setSnr(Object snr) {
        this.snr = snr;
    }

    public Object getRssi() {
        return rssi;
    }

    public void setRssi(Object rssi) {
        this.rssi = rssi;
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

    public String getProductForm() {
        return productForm;
    }

    public void setProductForm(String productForm) {
        this.productForm = productForm;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getSensingSignal() {
        return sensingSignal;
    }

    public void setSensingSignal(String sensingSignal) {
        this.sensingSignal = sensingSignal;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public int getOffLineTime() {
        return offLineTime;
    }

    public void setOffLineTime(int offLineTime) {
        this.offLineTime = offLineTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(sn);
        dest.writeString(newSn);
        dest.writeString(frequencyBand);
        dest.writeString(sendingPower);
        dest.writeString(frequency);
        dest.writeString(sf);
        dest.writeString(channel);
        dest.writeString(productForm);
        dest.writeString(version);
        dest.writeString(sensingSignal);
        dest.writeString(remark);
        dest.writeString(longitude);
        dest.writeString(latitude);
        dest.writeInt(offLineTime);
    }
}