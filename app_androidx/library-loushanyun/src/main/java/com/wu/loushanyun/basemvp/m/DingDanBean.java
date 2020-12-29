package com.wu.loushanyun.basemvp.m;

import java.util.List;

public class DingDanBean {

    /**
     * code : 0
     * msg : success
     * data : [{"id":null,"tablenumber":null,"ordernumber":"LSY123456781","exitlogo":null},{"id":null,"tablenumber":null,"ordernumber":"LSY123456789","exitlogo":null}]
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
         * id : null
         * tablenumber : null
         * ordernumber : LSY123456781
         * exitlogo : null
         */

        private Object id;
        private Object tablenumber;
        private String ordernumber;
        private Object exitlogo;

        public Object getId() {
            return id;
        }

        public void setId(Object id) {
            this.id = id;
        }

        public Object getTablenumber() {
            return tablenumber;
        }

        public void setTablenumber(Object tablenumber) {
            this.tablenumber = tablenumber;
        }

        public String getOrdernumber() {
            return ordernumber;
        }

        public void setOrdernumber(String ordernumber) {
            this.ordernumber = ordernumber;
        }

        public Object getExitlogo() {
            return exitlogo;
        }

        public void setExitlogo(Object exitlogo) {
            this.exitlogo = exitlogo;
        }
    }
}
