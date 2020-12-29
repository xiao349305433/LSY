package wu.loushanyun.com.sixapp.m;

import java.util.List;

public class ManuInfo {

    /**
     * responseTime : 1591171854803
     * message : success
     * code : 0
     * datas : [{"businessName":"中国苏旺你有限公司","subBusinessName":"中国苏旺你有限公司"}]
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
         * businessName : 中国苏旺你有限公司
         * subBusinessName : 中国苏旺你有限公司
         */

        private String accountId;
        private String businessName;
        private String subBusinessName;
        private String subAccountId;

        public String getBusinessName() {
            return businessName;
        }

        public void setBusinessName(String businessName) {
            this.businessName = businessName;
        }

        public String getSubBusinessName() {
            return subBusinessName;
        }

        public void setSubBusinessName(String subBusinessName) {
            this.subBusinessName = subBusinessName;
        }

        public String getAccountId() {
            return accountId;
        }

        public void setAccountId(String accountId) {
            this.accountId = accountId;
        }

        public String getSubAccountId() {
            return subAccountId;
        }

        public void setSubAccountId(String subAccountId) {
            this.subAccountId = subAccountId;
        }
    }
}
