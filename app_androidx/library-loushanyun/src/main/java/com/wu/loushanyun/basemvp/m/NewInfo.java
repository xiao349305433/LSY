package com.wu.loushanyun.basemvp.m;

import java.util.List;

public class NewInfo {


    /**
     * code : 0
     * msg : 查询成功
     * data : [{"rssi":-59,"snr":13,"sendTime":1544423325000},{"rssi":-59,"snr":13,"sendTime":1544423325000},{"rssi":-59,"snr":13,"sendTime":1544423325000},{"rssi":-61,"snr":13.3,"sendTime":1544422155000},{"rssi":-61,"snr":13.3,"sendTime":1544422155000}]
     */

    private int code;
    private String msg;
    private List<DataBean> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * rssi : -59
         * snr : 13
         * sendTime : 1544423325000
         */

        private double rssi;
        private double snr;
        private long sendTime;

        public double getRssi() {
            return rssi;
        }

        public void setRssi(int rssi) {
            this.rssi = rssi;
        }

        public double getSnr() {
            return snr;
        }

        public void setSnr(int snr) {
            this.snr = snr;
        }

        public long getSendTime() {
            return sendTime;
        }

        public void setSendTime(long sendTime) {
            this.sendTime = sendTime;
        }
    }
}
