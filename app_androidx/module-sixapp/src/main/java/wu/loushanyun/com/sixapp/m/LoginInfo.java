package wu.loushanyun.com.sixapp.m;

public class LoginInfo {


    /**
     * code : 0
     * msg : 登录成功
     * data : {"id":1,"mloginFactoryNum":"1","mloginCode":"1","mloginSupplier":"1","mloginPhoneNumber":15907170807,"mloginHomeType":1,"mloginContacts":"1","mloginRemark":"1","mloginCreateTime":1569749208000,"mloginType":"1,2","mloginEmail":"1","mloginAccountStatus":1}
     */

    private int code;
    private String msg;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 1
         * mloginFactoryNum : 1
         * mloginCode : 1
         * mloginSupplier : 1
         * mloginPhoneNumber : 15907170807
         * mloginHomeType : 1
         * mloginContacts : 1
         * mloginRemark : 1
         * mloginCreateTime : 1569749208000
         * mloginType : 1,2
         * mloginEmail : 1
         * mloginAccountStatus : 1
         */

        private int id;
        private String mloginFactoryNum;
        private String mloginCode;
        private String mloginSupplier;
        private long mloginPhoneNumber;
        private int mloginHomeType;
        private String mloginContacts;
        private String mloginRemark;
        private long mloginCreateTime;
        private String mloginType;
        private String mloginEmail;
        private int mloginAccountStatus;
        private String appToken;

        public String getAppToken() {
            return appToken;
        }

        public void setAppToken(String appToken) {
            this.appToken = appToken;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getMloginFactoryNum() {
            return mloginFactoryNum;
        }

        public void setMloginFactoryNum(String mloginFactoryNum) {
            this.mloginFactoryNum = mloginFactoryNum;
        }

        public String getMloginCode() {
            return mloginCode;
        }

        public void setMloginCode(String mloginCode) {
            this.mloginCode = mloginCode;
        }

        public String getMloginSupplier() {
            return mloginSupplier;
        }

        public void setMloginSupplier(String mloginSupplier) {
            this.mloginSupplier = mloginSupplier;
        }

        public long getMloginPhoneNumber() {
            return mloginPhoneNumber;
        }

        public void setMloginPhoneNumber(long mloginPhoneNumber) {
            this.mloginPhoneNumber = mloginPhoneNumber;
        }

        public int getMloginHomeType() {
            return mloginHomeType;
        }

        public void setMloginHomeType(int mloginHomeType) {
            this.mloginHomeType = mloginHomeType;
        }

        public String getMloginContacts() {
            return mloginContacts;
        }

        public void setMloginContacts(String mloginContacts) {
            this.mloginContacts = mloginContacts;
        }

        public String getMloginRemark() {
            return mloginRemark;
        }

        public void setMloginRemark(String mloginRemark) {
            this.mloginRemark = mloginRemark;
        }

        public long getMloginCreateTime() {
            return mloginCreateTime;
        }

        public void setMloginCreateTime(long mloginCreateTime) {
            this.mloginCreateTime = mloginCreateTime;
        }

        public String getMloginType() {
            return mloginType;
        }

        public void setMloginType(String mloginType) {
            this.mloginType = mloginType;
        }

        public String getMloginEmail() {
            return mloginEmail;
        }

        public void setMloginEmail(String mloginEmail) {
            this.mloginEmail = mloginEmail;
        }

        public int getMloginAccountStatus() {
            return mloginAccountStatus;
        }

        public void setMloginAccountStatus(int mloginAccountStatus) {
            this.mloginAccountStatus = mloginAccountStatus;
        }
    }
}
