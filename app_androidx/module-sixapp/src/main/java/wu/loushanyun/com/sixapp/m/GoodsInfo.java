package wu.loushanyun.com.sixapp.m;

import java.util.List;

public class GoodsInfo {

    /**
     * responseTime : 1590637721165
     * message : success
     * code : 0
     * datas : [{"id":83,"deviceTypeId":"1","productForm":"10","goodsName":"总线集中采集器","goodsModel":"CENT1","goodsPicture":"http://39.98.141.127/img_smart_water/1587777416939.jpg","batteryConfig":"4","sensingSignal":"undefined","compositeCapacitor":"0","bluetooth":"undefined","caliber":"undefined","concentratorMaxNum":"32","remark":"委托湖北楚天汉仪制造"}]
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
         * id : 83
         * deviceTypeId : 1
         * productForm : 10
         * goodsName : 总线集中采集器
         * goodsModel : CENT1
         * goodsPicture : http://39.98.141.127/img_smart_water/1587777416939.jpg
         * batteryConfig : 4
         * sensingSignal : undefined
         * compositeCapacitor : 0
         * bluetooth : undefined
         * caliber : undefined
         * concentratorMaxNum : 32
         * remark : 委托湖北楚天汉仪制造
         */

        private int id;
        private String deviceTypeId;
        private String productForm;
        private String goodsName;
        private String goodsModel;
        private String goodsPicture;
        private String batteryConfig;
        private String sensingSignal;
        private String compositeCapacitor;
        private String bluetooth;
        private String caliber;
        private String concentratorMaxNum;
        private String remark;
        private String watchCase;
        private String rangeRatio;
        private String magnification;
        private String movementType;
        private String valve;
        private String productFeatures;

        public String getProductFeatures() {
            return productFeatures;
        }

        public void setProductFeatures(String productFeatures) {
            this.productFeatures = productFeatures;
        }

        public String getValve() {
            return valve;
        }

        public void setValve(String valve) {
            this.valve = valve;
        }

        public String getMovementType() {
            return movementType;
        }

        public void setMovementType(String movementType) {
            this.movementType = movementType;
        }

        public String getMagnification() {
            return magnification;
        }

        public void setMagnification(String magnification) {
            this.magnification = magnification;
        }

        public String getWatchCase() {
            return watchCase;
        }

        public void setWatchCase(String watchCase) {
            this.watchCase = watchCase;
        }

        public String getRangeRatio() {
            return rangeRatio;
        }

        public void setRangeRatio(String rangeRatio) {
            this.rangeRatio = rangeRatio;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getDeviceTypeId() {
            return deviceTypeId;
        }

        public void setDeviceTypeId(String deviceTypeId) {
            this.deviceTypeId = deviceTypeId;
        }

        public String getProductForm() {
            return productForm;
        }

        public void setProductForm(String productForm) {
            this.productForm = productForm;
        }

        public String getGoodsName() {
            return goodsName;
        }

        public void setGoodsName(String goodsName) {
            this.goodsName = goodsName;
        }

        public String getGoodsModel() {
            return goodsModel;
        }

        public void setGoodsModel(String goodsModel) {
            this.goodsModel = goodsModel;
        }

        public String getGoodsPicture() {
            return goodsPicture;
        }

        public void setGoodsPicture(String goodsPicture) {
            this.goodsPicture = goodsPicture;
        }

        public String getBatteryConfig() {
            return batteryConfig;
        }

        public void setBatteryConfig(String batteryConfig) {
            this.batteryConfig = batteryConfig;
        }

        public String getSensingSignal() {
            return sensingSignal;
        }

        public void setSensingSignal(String sensingSignal) {
            this.sensingSignal = sensingSignal;
        }

        public String getCompositeCapacitor() {
            return compositeCapacitor;
        }

        public void setCompositeCapacitor(String compositeCapacitor) {
            this.compositeCapacitor = compositeCapacitor;
        }

        public String getBluetooth() {
            return bluetooth;
        }

        public void setBluetooth(String bluetooth) {
            this.bluetooth = bluetooth;
        }

        public String getCaliber() {
            return caliber;
        }

        public void setCaliber(String caliber) {
            this.caliber = caliber;
        }

        public String getConcentratorMaxNum() {
            return concentratorMaxNum;
        }

        public void setConcentratorMaxNum(String concentratorMaxNum) {
            this.concentratorMaxNum = concentratorMaxNum;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }
    }
}
