package wu.loushanyun.com.libraryfive.m;

import org.litepal.crud.LitePalSupport;

import met.hx.com.librarybase.some_utils.TimeUtils;

public class RepairUpdateData extends LitePalSupport {
    private String newSn;
    private String loginid;
    private String oldSn;
    private String time;
    private String productName;
    private String yuanChuanSaveDataJson;
    private String wuLianWangDataJson;


    public RepairUpdateData(String newSn, String loginid, String oldSn, String productName, String yuanChuanSaveDataJson) {
        this.newSn = newSn;
        this.loginid = loginid;
        this.oldSn = oldSn;
        this.time = TimeUtils.getCurTimeString();
        this.productName = productName;
        this.yuanChuanSaveDataJson = yuanChuanSaveDataJson;
    }

    public RepairUpdateData(String newSn, String loginid, String oldSn, String productName) {
        this.newSn = newSn;
        this.loginid = loginid;
        this.oldSn = oldSn;
        this.time = TimeUtils.getCurTimeString();
        this.productName = productName;
    }

    @Override
    public String toString() {
        return "RepairUpdateData{" +
                "newSn='" + newSn + '\'' +
                ", loginid='" + loginid + '\'' +
                ", oldSn='" + oldSn + '\'' +
                ", time='" + time + '\'' +
                ", productName='" + productName + '\'' +
                ", yuanChuanSaveDataJson='" + yuanChuanSaveDataJson + '\'' +
                ", wuLianWangDataJson='" + wuLianWangDataJson + '\'' +
                '}';
    }

    @Override
    public long getBaseObjId() {
        return super.getBaseObjId();
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNewSn() {
        return newSn;
    }

    public void setNewSn(String newSn) {
        this.newSn = newSn;
    }

    public String getWuLianWangDataJson() {
        return wuLianWangDataJson;
    }

    public void setWuLianWangDataJson(String wuLianWangDataJson) {
        this.wuLianWangDataJson = wuLianWangDataJson;
    }

    public String getLoginid() {
        return loginid;
    }

    public void setLoginid(String loginid) {
        this.loginid = loginid;
    }

    public String getOldSn() {
        return oldSn;
    }

    public void setOldSn(String oldSn) {
        this.oldSn = oldSn;
    }

    public String getYuanChuanSaveDataJson() {
        return yuanChuanSaveDataJson;
    }

    public void setYuanChuanSaveDataJson(String yuanChuanSaveDataJson) {
        this.yuanChuanSaveDataJson = yuanChuanSaveDataJson;
    }
}
