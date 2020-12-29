package wu.loushanyun.com.libraryfive.m;

import com.wu.loushanyun.basemvp.m.SaveDataMeter;

import java.io.Serializable;
import java.util.List;

public class InsideSaveThirdData implements Serializable {

    private static final long serialVersionUID = 1L;
    private String sn;
    private String lat;//经度
    private String lon;//纬度
    private String gridCode;//网格号
    private String loginId;//登录Id
    private String productName;//产品名称
    private String productForm;//产品形式
    private String businessLicense;//营业执照序号
    private String manufacturersIdentification;//厂家标识
    private String factoryName;//厂家名称
    private String meterType;//型号
    private String gatherScene;//采集场景
    private List<String> image;
    private SaveDataConverter converter;
    private List<SaveDataMeter> meters;

    public InsideSaveThirdData(YuanChuanSaveData yuanChuanSaveData, List<String> image, SaveDataConverter converter, List<SaveDataMeter> meters) {
        this.sn = yuanChuanSaveData.getSn();
        this.lat = String.valueOf(yuanChuanSaveData.getLat());
        this.lon = String.valueOf(yuanChuanSaveData.getLon());
        this.loginId = yuanChuanSaveData.getLoginId();
        this.productName = yuanChuanSaveData.getProductName();
        this.productForm = yuanChuanSaveData.getProductForm();
        this.businessLicense = yuanChuanSaveData.getBusinessLicense();
        this.manufacturersIdentification = yuanChuanSaveData.getManufacturersIdentification();
        this.factoryName = yuanChuanSaveData.getFactoryName();
        this.meterType = yuanChuanSaveData.getMeterType();
        this.gatherScene = yuanChuanSaveData.getGatherScene();
        this.image = image;
        this.converter = converter;
        this.meters = meters;
        this.gridCode = yuanChuanSaveData.getGridCode();
    }

    public String getGridCode() {
        return gridCode;
    }

    public void setGridCode(String gridCode) {
        this.gridCode = gridCode;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
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

    public String getManufacturersIdentification() {
        return manufacturersIdentification;
    }

    public void setManufacturersIdentification(String manufacturersIdentification) {
        this.manufacturersIdentification = manufacturersIdentification;
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

    public String getGatherScene() {
        return gatherScene;
    }

    public void setGatherScene(String gatherScene) {
        this.gatherScene = gatherScene;
    }

    public List<String> getImage() {
        return image;
    }

    public void setImage(List<String> image) {
        this.image = image;
    }

    public SaveDataConverter getConverter() {
        return converter;
    }

    public void setConverter(SaveDataConverter converter) {
        this.converter = converter;
    }

    public List<SaveDataMeter> getMeters() {
        return meters;
    }

    public void setMeters(List<SaveDataMeter> meters) {
        this.meters = meters;
    }

    public InsideSaveThirdData() {
    }
}
