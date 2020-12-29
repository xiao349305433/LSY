package wu.loushanyun.com.sixapp.m;

import java.util.List;

public class GetBatchInfo {


    /**
     * responseTime : 1593507200877
     * message : success
     * code : 0
     * datas : [{"batchNumber":"1","productForm":"3"},{"batchNumber":"2","productForm":"3"},{"batchNumber":"1","productForm":"4"},{"batchNumber":"1","productForm":"10"}]
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
         * batchNumber : 1
         * productForm : 3
         */

        private String batchNumber;
        private String productForm;

        public String getBatchNumber() {
            return batchNumber;
        }

        public void setBatchNumber(String batchNumber) {
            this.batchNumber = batchNumber;
        }

        public String getProductForm() {
            return productForm;
        }

        public void setProductForm(String productForm) {
            this.productForm = productForm;
        }
    }
}
