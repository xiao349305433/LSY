package com.loushanyun.modulefactory.m;

public class FindBodyData {


    /**
     * msg : 查询成功
     * code : 0
     * data : {"bodyNum":"44444455","orderber":"LSY1574229467713"}
     */

    private String msg;
    private int code;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * bodyNum : 44444455
         * orderber : LSY1574229467713
         */

        private String bodyNum;
        private String orderber;
        private String exitlogos;
        private String mLoginSupplier;

        public String getmLoginSupplier() {
            return mLoginSupplier;
        }

        public void setmLoginSupplier(String mLoginSupplier) {
            this.mLoginSupplier = mLoginSupplier;
        }

        public String getExitlogos() {
            return exitlogos;
        }

        public void setExitlogos(String exitlogos) {
            this.exitlogos = exitlogos;
        }

        public String getBodyNum() {
            return bodyNum;
        }

        public void setBodyNum(String bodyNum) {
            this.bodyNum = bodyNum;
        }

        public String getOrderber() {
            return orderber;
        }

        public void setOrderber(String orderber) {
            this.orderber = orderber;
        }
    }
}
