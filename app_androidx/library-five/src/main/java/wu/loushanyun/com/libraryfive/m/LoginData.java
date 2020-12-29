package wu.loushanyun.com.libraryfive.m;

public class LoginData {

    /**
     * id : 98
     * loginName : huxuhuxu
     * loginPwd : 2EwWcXrsyozvY5E/33+Hiw==
     * registerPhone : 15527919058
     * openId : 0
     * role : 3
     * nameOrNumber : 胡旭
     * authorizationTime : Jun 11, 2018 9:00:52 PM
     * tradeRegister : {"id":57,"name":"娄山云武汉事业部","contacts":"武汉事业部","phone":"010-12346578","businessLicense":"123456789456414","companyNetwork":"39.100.145.211","companyImage":"LouShanCloud_enterpriseLogo/c1d38515-d8c8-492a-835d-84377d9f8335.png","companyProfile":"娄山云","sendAddress":"whsyb@lsy.com","address":"天街5栋917-920","qualified":1,"serialNumber":"1803281717A","enterpriseTypeId":1,"provinceId":"42","cityId":"01","countyId":"03"}
     * token :
     */

    private int id;
    private String loginName;
    private String loginPwd;
    private String registerPhone;
    private String openId;
    private int role;
    private String nameOrNumber;
    private String authorizationTime;
    private TradeRegisterBean tradeRegister;
    private String token;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getLoginPwd() {
        return loginPwd;
    }

    public void setLoginPwd(String loginPwd) {
        this.loginPwd = loginPwd;
    }

    public String getRegisterPhone() {
        return registerPhone;
    }

    public void setRegisterPhone(String registerPhone) {
        this.registerPhone = registerPhone;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getNameOrNumber() {
        return nameOrNumber;
    }

    public void setNameOrNumber(String nameOrNumber) {
        this.nameOrNumber = nameOrNumber;
    }

    public String getAuthorizationTime() {
        return authorizationTime;
    }

    public void setAuthorizationTime(String authorizationTime) {
        this.authorizationTime = authorizationTime;
    }

    public TradeRegisterBean getTradeRegister() {
        return tradeRegister;
    }

    public void setTradeRegister(TradeRegisterBean tradeRegister) {
        this.tradeRegister = tradeRegister;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public static class TradeRegisterBean {
        /**
         * id : 57
         * name : 娄山云武汉事业部
         * contacts : 武汉事业部
         * phone : 010-12346578
         * businessLicense : 123456789456414
         * companyNetwork : 39.100.145.211
         * companyImage : LouShanCloud_enterpriseLogo/c1d38515-d8c8-492a-835d-84377d9f8335.png
         * companyProfile : 娄山云
         * sendAddress : whsyb@lsy.com
         * address : 天街5栋917-920
         * qualified : 1
         * serialNumber : 1803281717A
         * enterpriseTypeId : 1
         * provinceId : 42
         * cityId : 01
         * countyId : 03
         */

        private int id;
        private String name;
        private String contacts;
        private String phone;
        private String businessLicense;
        private String companyNetwork;
        private String companyImage;
        private String companyProfile;
        private String sendAddress;
        private String address;
        private int qualified;
        private String serialNumber;
        private int enterpriseTypeId;
        private String provinceId;
        private String cityId;
        private String countyId;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getContacts() {
            return contacts;
        }

        public void setContacts(String contacts) {
            this.contacts = contacts;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getBusinessLicense() {
            return businessLicense;
        }

        public void setBusinessLicense(String businessLicense) {
            this.businessLicense = businessLicense;
        }

        public String getCompanyNetwork() {
            return companyNetwork;
        }

        public void setCompanyNetwork(String companyNetwork) {
            this.companyNetwork = companyNetwork;
        }

        public String getCompanyImage() {
            return companyImage;
        }

        public void setCompanyImage(String companyImage) {
            this.companyImage = companyImage;
        }

        public String getCompanyProfile() {
            return companyProfile;
        }

        public void setCompanyProfile(String companyProfile) {
            this.companyProfile = companyProfile;
        }

        public String getSendAddress() {
            return sendAddress;
        }

        public void setSendAddress(String sendAddress) {
            this.sendAddress = sendAddress;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getQualified() {
            return qualified;
        }

        public void setQualified(int qualified) {
            this.qualified = qualified;
        }

        public String getSerialNumber() {
            return serialNumber;
        }

        public void setSerialNumber(String serialNumber) {
            this.serialNumber = serialNumber;
        }

        public int getEnterpriseTypeId() {
            return enterpriseTypeId;
        }

        public void setEnterpriseTypeId(int enterpriseTypeId) {
            this.enterpriseTypeId = enterpriseTypeId;
        }

        public String getProvinceId() {
            return provinceId;
        }

        public void setProvinceId(String provinceId) {
            this.provinceId = provinceId;
        }

        public String getCityId() {
            return cityId;
        }

        public void setCityId(String cityId) {
            this.cityId = cityId;
        }

        public String getCountyId() {
            return countyId;
        }

        public void setCountyId(String countyId) {
            this.countyId = countyId;
        }
    }
}
