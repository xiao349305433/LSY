package wu.loushanyun.com.libraryfive.m;

import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * 远传表号基础信息类
 * */
public class YuanChuanSaveData extends LitePalSupport implements Parcelable {
    private String sn;
    private double lat;//经度
    private double lon;//纬度
    private String loginId;//登录Id
    private String productName;//产品名称
    private String productForm;//产品形式
    private String businessLicense;//营业执照序号
    private String gridCode;//网格号

    private String time;//保存时间 自用
    private String attrMapJson;//属性集合 自用

    private String jsonImage;
    private String jsonImagePath;//本地图片路径
    //读表
    private String jsonMeter;
    //配置
    private String jsonSaveDataConverter;
    private String manufacturersIdentification;//厂家标识
    private String gatherScene;//采集场景
    private String factoryName;//厂家名称
    private String meterType;//型号==口径+(mm)

    public YuanChuanSaveData() {
    }


    @Override
    public long getBaseObjId() {
        return super.getBaseObjId();
    }

    protected YuanChuanSaveData(Parcel in) {
        sn = in.readString();
        lat = in.readDouble();
        lon = in.readDouble();
        loginId = in.readString();
        productName = in.readString();
        productForm = in.readString();
        businessLicense = in.readString();
        gridCode = in.readString();
        time = in.readString();
        attrMapJson = in.readString();
        jsonImage = in.readString();
        jsonImagePath = in.readString();
        jsonMeter = in.readString();
        jsonSaveDataConverter = in.readString();
        manufacturersIdentification = in.readString();
        gatherScene = in.readString();
        factoryName = in.readString();
        meterType = in.readString();
    }

    public static final Creator<YuanChuanSaveData> CREATOR = new Creator<YuanChuanSaveData>() {
        @Override
        public YuanChuanSaveData createFromParcel(Parcel in) {
            return new YuanChuanSaveData(in);
        }

        @Override
        public YuanChuanSaveData[] newArray(int size) {
            return new YuanChuanSaveData[size];
        }
    };

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductForm() {
        return productForm;
    }

    public void setProductForm(String productForm) {
        this.productForm = productForm;
    }

    public String getBusinessLicense() {
        return businessLicense;
    }

    public void setBusinessLicense(String businessLicense) {
        this.businessLicense = businessLicense;
    }

    public String getGridCode() {
        return gridCode;
    }

    public void setGridCode(String gridCode) {
        this.gridCode = gridCode;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAttrMapJson() {
        return attrMapJson;
    }

    public void setAttrMapJson(String attrMapJson) {
        this.attrMapJson = attrMapJson;
    }

    public String getJsonImage() {
        return jsonImage;
    }

    public void setJsonImage(String jsonImage) {
        this.jsonImage = jsonImage;
    }

    public String getJsonImagePath() {
        return jsonImagePath;
    }

    public void setJsonImagePath(String jsonImagePath) {
        this.jsonImagePath = jsonImagePath;
    }

    public String getJsonMeter() {
        return jsonMeter;
    }

    public void setJsonMeter(String jsonMeter) {
        this.jsonMeter = jsonMeter;
    }

    public String getJsonSaveDataConverter() {
        return jsonSaveDataConverter;
    }

    public void setJsonSaveDataConverter(String jsonSaveDataConverter) {
        this.jsonSaveDataConverter = jsonSaveDataConverter;
    }

    public String getManufacturersIdentification() {
        return manufacturersIdentification;
    }

    public void setManufacturersIdentification(String manufacturersIdentification) {
        this.manufacturersIdentification = manufacturersIdentification;
    }

    public String getGatherScene() {
        return gatherScene;
    }

    public void setGatherScene(String gatherScene) {
        this.gatherScene = gatherScene;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(sn);
        dest.writeDouble(lat);
        dest.writeDouble(lon);
        dest.writeString(loginId);
        dest.writeString(productName);
        dest.writeString(productForm);
        dest.writeString(businessLicense);
        dest.writeString(gridCode);
        dest.writeString(time);
        dest.writeString(attrMapJson);
        dest.writeString(jsonImage);
        dest.writeString(jsonImagePath);
        dest.writeString(jsonMeter);
        dest.writeString(jsonSaveDataConverter);
        dest.writeString(manufacturersIdentification);
        dest.writeString(gatherScene);
        dest.writeString(factoryName);
        dest.writeString(meterType);
    }
}