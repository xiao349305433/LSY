package wu.loushanyun.com.moduletwoinit.m;

import java.util.List;



public class InsideAreaUpdate {

    private String areaNumber;//区域号
    private String areaName;//区域名称
    private List<String> imageBytes;//图片Bytes
    private Integer tradeRegisterId;
    private String businessLicense;//企业营业执照号码

    private String sn;
    private String pulseConstant;//脉冲常数
    private String impulseInitial;//初始脉冲
    private String channel;//信道参数
    private String sf;//扩频因子
    private String remark;//备注
    private String caliber;//口径
    private String loginId;//登录用户Id
    private String meterId;//设备id


    public String getMeterId() {
        return meterId;
    }

    public void setMeterId(String meterId) {
        this.meterId = meterId;
    }

    public String getAreaNumber() {
        return areaNumber;
    }

    public void setAreaNumber(String areaNumber) {
        this.areaNumber = areaNumber;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public List<String> getImageBytes() {
        return imageBytes;
    }

    public void setImageBytes(List<String> imageBytes) {
        this.imageBytes = imageBytes;
    }

    public Integer getTradeRegisterId() {
        return tradeRegisterId;
    }

    public void setTradeRegisterId(Integer tradeRegisterId) {
        this.tradeRegisterId = tradeRegisterId;
    }

    public String getBusinessLicense() {
        return businessLicense;
    }

    public void setBusinessLicense(String businessLicense) {
        this.businessLicense = businessLicense;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getPulseConstant() {
        return pulseConstant;
    }

    public void setPulseConstant(String pulseConstant) {
        this.pulseConstant = pulseConstant;
    }

    public String getImpulseInitial() {
        return impulseInitial;
    }

    public void setImpulseInitial(String impulseInitial) {
        this.impulseInitial = impulseInitial;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getSf() {
        return sf;
    }

    public void setSf(String sf) {
        this.sf = sf;
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
        this.caliber = caliber;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }
}
