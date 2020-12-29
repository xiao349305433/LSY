package wu.loushanyun.com.moduletwoinit.m;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

public class WuLianUploadData extends LitePalSupport implements Serializable {
    private String time;
    private String lat;//纬度
    private String lon;//经度
    private String provinces;//省份
    private String cities;//市
    private String counties;//县城
    private String businessLicense;//营业执照序号
    private String areaNumber;//区域号
    private String areaName;//区域名称
    private Integer loginId;//登录Id
    private String jsonImage;


    @Override
    public long getBaseObjId() {
        return super.getBaseObjId();
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getProvinces() {
        return provinces;
    }

    public void setProvinces(String provinces) {
        this.provinces = provinces;
    }

    public String getCities() {
        return cities;
    }

    public void setCities(String cities) {
        this.cities = cities;
    }

    public String getCounties() {
        return counties;
    }

    public void setCounties(String counties) {
        this.counties = counties;
    }

    public String getBusinessLicense() {
        return businessLicense;
    }

    public void setBusinessLicense(String businessLicense) {
        this.businessLicense = businessLicense;
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

    public Integer getLoginId() {
        return loginId;
    }

    public void setLoginId(Integer loginId) {
        this.loginId = loginId;
    }

    public String getJsonImage() {
        return jsonImage;
    }

    public void setJsonImage(String jsonImage) {
        this.jsonImage = jsonImage;
    }


    @Override
    public String toString() {
        return "WuLianUploadData{" +
                "time='" + time + '\'' +
                ", lat='" + lat + '\'' +
                ", lon='" + lon + '\'' +
                ", provinces='" + provinces + '\'' +
                ", cities='" + cities + '\'' +
                ", counties='" + counties + '\'' +
                ", businessLicense='" + businessLicense + '\'' +
                ", areaNumber='" + areaNumber + '\'' +
                ", areaName='" + areaName + '\'' +
                ", loginId=" + loginId +
                '}';
    }

    public String printAll() {
        return " 时间：" + time + '\n' +
                " 纬度：" + lat + '\n' +
                " 经度：" + lon + '\n' +
                " 省：" + provinces + '\n' +
                " 市：" + cities + '\n' +
                " 县：" + counties + '\n' +
                " 营业执照序号：" + businessLicense + '\n' +
                " 网格号：" + areaNumber + '\n' +
                " 网格名称：" + areaName;
    }
}
