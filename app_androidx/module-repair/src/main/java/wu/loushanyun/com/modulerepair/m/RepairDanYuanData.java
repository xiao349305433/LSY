package wu.loushanyun.com.modulerepair.m;

import java.util.List;

public class RepairDanYuanData {

    /**
     * msg : success
     * code : 0
     * data : [{"meterNumber":1,"meterId":"180629141336","offLineHours":480},{"meterNumber":2,"meterId":"180629142503","offLineHours":480},{"meterNumber":3,"meterId":"180629142741","offLineHours":480},{"meterNumber":4,"meterId":"180629142927","offLineHours":480},{"meterNumber":5,"meterId":"180629143106","offLineHours":563}]
     */

    private String msg;
    private int code;
    private List<DataBean> data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * meterNumber : 1
         * meterId : 180629141336
         * offLineHours : 480
         */

        private int meterNumber;
        private String meterId;
        private String manufacturersIdentification;
        private int offLineHours;
        private boolean deviceGenghuanZhuangtai;
        private boolean deviceBaocunZhuangtai;

        public boolean isDeviceGenghuanZhuangtai() {
            return deviceGenghuanZhuangtai;
        }

        public void setDeviceGenghuanZhuangtai(boolean deviceGenghuanZhuangtai) {
            this.deviceGenghuanZhuangtai = deviceGenghuanZhuangtai;
        }

        public boolean isDeviceBaocunZhuangtai() {
            return deviceBaocunZhuangtai;
        }

        public void setDeviceBaocunZhuangtai(boolean deviceBaocunZhuangtai) {
            this.deviceBaocunZhuangtai = deviceBaocunZhuangtai;
        }

        public String getManufacturersIdentification() {
            return manufacturersIdentification;
        }

        public void setManufacturersIdentification(String manufacturersIdentification) {
            this.manufacturersIdentification = manufacturersIdentification;
        }

        public int getMeterNumber() {
            return meterNumber;
        }

        public void setMeterNumber(int meterNumber) {
            this.meterNumber = meterNumber;
        }

        public String getMeterId() {
            return meterId;
        }

        public void setMeterId(String meterId) {
            this.meterId = meterId;
        }

        public int getOffLineHours() {
            return offLineHours;
        }

        public void setOffLineHours(int offLineHours) {
            this.offLineHours = offLineHours;
        }
    }
}