package wu.loushanyun.com.sixapp.m;


import java.util.List;

public class LandInfo {


    /**
     * responseTime : 1590546581103
     * message : success
     * code : 0
     * datas : [{"loginId":214,"tel":"18248417986","jobNumber":"L12345","businessName":"贵州云通曙光技术服务有限公司","id":23,"token":"8de0a06fefe544bdbe82b6b5b11d0bbb"}]
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
         * loginId : 214
         * tel : 18248417986
         * jobNumber : L12345
         * businessName : 贵州云通曙光技术服务有限公司
         * id : 23
         * token : 8de0a06fefe544bdbe82b6b5b11d0bbb
         */

        private int loginId;
        private String tel;
        private String jobNumber;
        private String businessName;
        private int id;
        private int accountId;
        private String token;

        public int getAccountId() {
            return accountId;
        }

        public void setAccountId(int accountId) {
            this.accountId = accountId;
        }

        public int getLoginId() {
            return loginId;
        }

        public void setLoginId(int loginId) {
            this.loginId = loginId;
        }

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        public String getJobNumber() {
            return jobNumber;
        }

        public void setJobNumber(String jobNumber) {
            this.jobNumber = jobNumber;
        }

        public String getBusinessName() {
            return businessName;
        }

        public void setBusinessName(String businessName) {
            this.businessName = businessName;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
