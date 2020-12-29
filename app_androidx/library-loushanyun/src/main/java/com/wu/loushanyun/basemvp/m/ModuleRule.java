package com.wu.loushanyun.basemvp.m;

import java.util.List;

public class ModuleRule {

    /**
     * code : 0
     * msg : success
     * data : [{"id":34,"manufacturersIdentification":21,"productForm":6,"protocolVersion":6,"remark":"1.0000"},{"id":35,"manufacturersIdentification":21,"productForm":6,"protocolVersion":1,"remark":"0.0100"},{"id":36,"manufacturersIdentification":21,"productForm":6,"protocolVersion":8,"remark":"0.0100"},{"id":37,"manufacturersIdentification":21,"productForm":6,"protocolVersion":9,"remark":"10.0000"}]
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
         * id : 34
         * manufacturersIdentification : 21
         * productForm : 6
         * protocolVersion : 6
         * remark : 1.0000
         */

        private int id;
        private int manufacturersIdentification;
        private int productForm;
        private int protocolVersion;
        private String remark;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getManufacturersIdentification() {
            return manufacturersIdentification;
        }

        public void setManufacturersIdentification(int manufacturersIdentification) {
            this.manufacturersIdentification = manufacturersIdentification;
        }

        public int getProductForm() {
            return productForm;
        }

        public void setProductForm(int productForm) {
            this.productForm = productForm;
        }

        public int getProtocolVersion() {
            return protocolVersion;
        }

        public void setProtocolVersion(int protocolVersion) {
            this.protocolVersion = protocolVersion;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }
    }
}
