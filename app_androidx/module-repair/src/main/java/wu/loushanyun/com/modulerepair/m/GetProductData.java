package wu.loushanyun.com.modulerepair.m;

import java.util.List;

public class GetProductData {

    /**
     * msg : success
     * code : 0
     * data : [{"manufacturersIdentification":17,"supplier":"贵州云通曙光科技服务有限公司"},{"manufacturersIdentification":19,"supplier":"武汉楚天汉仪科技有限公司"}]
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
         * manufacturersIdentification : 17
         * supplier : 贵州云通曙光科技服务有限公司
         */

        private int manufacturersIdentification;
        private String supplier;

        public int getManufacturersIdentification() {
            return manufacturersIdentification;
        }

        public void setManufacturersIdentification(int manufacturersIdentification) {
            this.manufacturersIdentification = manufacturersIdentification;
        }

        public String getSupplier() {
            return supplier;
        }

        public void setSupplier(String supplier) {
            this.supplier = supplier;
        }
    }
}
