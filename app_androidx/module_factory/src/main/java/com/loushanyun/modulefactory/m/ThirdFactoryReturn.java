package com.loushanyun.modulefactory.m;

import java.math.BigDecimal;

import met.hx.com.librarybase.some_utils.TimeUtils;

public class ThirdFactoryReturn {


    /**
     * msg : 该设备ID已存在
     * code : -1
     * thirdfactorysettings : {"id":768,"userid":"190429114648","sensingsignal":3,"rate":0.001,"status":"正常","firmwareversion":"1.07","productiontime":1556467200000,"date":null,"manufacturersidentification":21,"useable":0,"impulseInitial":5,"rechargeType":0}
     */

    private String msg;
    private int code;
    private ThirdfactorysettingsBean thirdfactorysettings;

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

    public ThirdfactorysettingsBean getThirdfactorysettings() {
        return thirdfactorysettings;
    }

    public void setThirdfactorysettings(ThirdfactorysettingsBean thirdfactorysettings) {
        this.thirdfactorysettings = thirdfactorysettings;
    }

    public static class ThirdfactorysettingsBean {
        /**
         * id : 768
         * userid : 190429114648
         * sensingsignal : 3
         * rate : 0.001
         * status : 正常
         * firmwareversion : 1.07
         * productiontime : 1556467200000
         * date : null
         * manufacturersidentification : 21
         * useable : 0
         * impulseInitial : 5.0
         * rechargeType : 0
         */
        public String printThird() {
            StringBuilder sb = new StringBuilder();
            sb.append("上传的设备ID:  ");
            sb.append(userid);
            sb.append("\n传感信号: ");
            if (sensingsignal == 1) {
                sb.append("3EV");
            } else if (sensingsignal == 3) {
                sb.append("2EV");
            } else {
                sb.append("无磁正反脉冲");
            }
            sb.append("\n脉冲常数(个/m³):  ");
            BigDecimal decimal = new BigDecimal(String.valueOf(rate));
            sb.append(new BigDecimal(1).divide(decimal).stripTrailingZeros().toPlainString());
            sb.append("\n倍率(m³/ev):  ");
            sb.append(rate);
            sb.append("\n读数:  ");
            sb.append(impulseInitial);
            sb.append("\n固件版本:  ");
            sb.append(firmwareversion);
            sb.append("\n生厂日期:  ");
            sb.append(TimeUtils.milliseconds2String(productiontime));
            return sb.toString();
        }

        private int id;
        private String userid;
        private int sensingsignal;
        private double rate;
        private String status;
        private String firmwareversion;
        private long productiontime;
        private Object date;
        private int manufacturersidentification;
        private int useable;
        private double impulseInitial;
        private int rechargeType;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public int getSensingsignal() {
            return sensingsignal;
        }

        public void setSensingsignal(int sensingsignal) {
            this.sensingsignal = sensingsignal;
        }

        public double getRate() {
            return rate;
        }

        public void setRate(double rate) {
            this.rate = rate;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getFirmwareversion() {
            return firmwareversion;
        }

        public void setFirmwareversion(String firmwareversion) {
            this.firmwareversion = firmwareversion;
        }

        public long getProductiontime() {
            return productiontime;
        }

        public void setProductiontime(long productiontime) {
            this.productiontime = productiontime;
        }

        public Object getDate() {
            return date;
        }

        public void setDate(Object date) {
            this.date = date;
        }

        public int getManufacturersidentification() {
            return manufacturersidentification;
        }

        public void setManufacturersidentification(int manufacturersidentification) {
            this.manufacturersidentification = manufacturersidentification;
        }

        public int getUseable() {
            return useable;
        }

        public void setUseable(int useable) {
            this.useable = useable;
        }

        public double getImpulseInitial() {
            return impulseInitial;
        }

        public void setImpulseInitial(double impulseInitial) {
            this.impulseInitial = impulseInitial;
        }

        public int getRechargeType() {
            return rechargeType;
        }

        public void setRechargeType(int rechargeType) {
            this.rechargeType = rechargeType;
        }
    }
}
