package wu.loushanyun.com.sixapp.p.runner;

public class ContractProInfo {


    /**
     * responseTime : 1600397911770
     * message : success
     * code : 0
     * data : {"testId":268,"productForm":"4","batchNumber":"020091801","goodsId":0,"envId":0}
     */

    private long responseTime;
    private String message;
    private int code;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * testId : 268
         * productForm : 4
         * batchNumber : 020091801
         * goodsId : 0
         * envId : 0
         */

        private int testId;
        private String productForm;
        private String batchNumber;
        private int goodsId;
        private int envId;

        public int getTestId() {
            return testId;
        }

        public void setTestId(int testId) {
            this.testId = testId;
        }

        public String getProductForm() {
            return productForm;
        }

        public void setProductForm(String productForm) {
            this.productForm = productForm;
        }

        public String getBatchNumber() {
            return batchNumber;
        }

        public void setBatchNumber(String batchNumber) {
            this.batchNumber = batchNumber;
        }

        public int getGoodsId() {
            return goodsId;
        }

        public void setGoodsId(int goodsId) {
            this.goodsId = goodsId;
        }

        public int getEnvId() {
            return envId;
        }

        public void setEnvId(int envId) {
            this.envId = envId;
        }
    }
}
