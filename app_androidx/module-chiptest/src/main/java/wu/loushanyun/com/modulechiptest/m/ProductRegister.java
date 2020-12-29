package wu.loushanyun.com.modulechiptest.m;

public class ProductRegister {
    private Integer id;

    private String loginName;

    private String companyName;

    private String tel;

    private String contacts;

    private String companyPhone;

    private String provinceId;

    private String cityId;

    private String countyId;

    private String addressInfo;

    private String businessLicense;

    private String password;

    private Byte qualified;

    private String remark;

    private String serialNumber;

    private Byte manufacturersIdentification;
    private String supplier;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getCompanyPhone() {
        return companyPhone;
    }

    public void setCompanyPhone(String companyPhone) {
        this.companyPhone = companyPhone;
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

    public String getAddressInfo() {
        return addressInfo;
    }

    public void setAddressInfo(String addressInfo) {
        this.addressInfo = addressInfo;
    }

    public String getBusinessLicense() {
        return businessLicense;
    }

    public void setBusinessLicense(String businessLicense) {
        this.businessLicense = businessLicense;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Byte getQualified() {
        return qualified;
    }

    public void setQualified(Byte qualified) {
        this.qualified = qualified;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Byte getManufacturersIdentification() {
        return manufacturersIdentification;
    }

    public void setManufacturersIdentification(Byte manufacturersIdentification) {
        this.manufacturersIdentification = manufacturersIdentification;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    @Override
    public String toString() {
        return "ProductRegister{" +
                "id=" + id +
                ", loginName='" + loginName + '\'' +
                ", companyName='" + companyName + '\'' +
                ", tel='" + tel + '\'' +
                ", contacts='" + contacts + '\'' +
                ", companyPhone='" + companyPhone + '\'' +
                ", provinceId='" + provinceId + '\'' +
                ", cityId='" + cityId + '\'' +
                ", countyId='" + countyId + '\'' +
                ", addressInfo='" + addressInfo + '\'' +
                ", businessLicense='" + businessLicense + '\'' +
                ", password='" + password + '\'' +
                ", qualified=" + qualified +
                ", remark='" + remark + '\'' +
                ", serialNumber='" + serialNumber + '\'' +
                ", manufacturersIdentification=" + manufacturersIdentification +
                ", supplier='" + supplier + '\'' +
                '}';
    }
}
