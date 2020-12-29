package wu.loushanyun.com.modulechiptest.m;

public class ThirdProduceInfo {
    private int num;
    private String UserId;
    private String tishi = "";
    private String sendStatus;
    private boolean IsSend;
    private int MLoginFactoryNum;
    private String moduleId;
    private String moduleProductionTime;
    private String hardwareVersion;
    private String softVersion;
    private boolean IsSave;

    public boolean isSave() {
        return IsSave;
    }

    public void setSave(boolean save) {
        IsSave = save;
    }

    public int getMLoginFactoryNum() {
        return MLoginFactoryNum;
    }

    public void setMLoginFactoryNum(int MLoginFactoryNum) {
        this.MLoginFactoryNum = MLoginFactoryNum;
    }

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public String getModuleProductionTime() {
        return moduleProductionTime;
    }

    public void setModuleProductionTime(String moduleProductionTime) {
        this.moduleProductionTime = moduleProductionTime;
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

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(String sendStatus) {
        this.sendStatus = sendStatus;
    }

    public boolean isSend() {
        return IsSend;
    }

    public void setSend(boolean send) {
        IsSend = send;
    }

    public boolean isUpData() {
        return IsUpData;
    }

    public void setUpData(boolean upData) {
        IsUpData = upData;
    }

    private boolean IsUpData;

}
