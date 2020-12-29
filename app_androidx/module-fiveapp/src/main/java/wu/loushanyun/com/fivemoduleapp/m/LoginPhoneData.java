package wu.loushanyun.com.fivemoduleapp.m;

import java.util.List;

import wu.loushanyun.com.libraryfive.m.LoginData;

public class LoginPhoneData {


    /**
     * code : 0
     * msg : Login successful
     * data : [{"id":98,"loginName":"huxuhuxu","loginPwd":"2EwWcXrsyozvY5E/33+Hiw==","registerPhone":"15527919058","openId":"0","role":3,"nameOrNumber":"胡旭","unit":null,"appUse":null,"authorizationTime":1528722052000,"tradeRegister":{"id":57,"name":"娄山云武汉事业部","contacts":"武汉事业部","phone":"010-12346578","businessLicense":"123456789456414","companyNetwork":"39.100.145.211","companyImage":"LouShanCloud_enterpriseLogo/c1d38515-d8c8-492a-835d-84377d9f8335.png","companyProfile":"娄山云","sendAddress":"whsyb@lsy.com","address":"天街5栋917-920","qualified":1,"serialNumber":"1806291534A","registerTime":null,"enterpriseTypeId":1,"provinceId":"42","cityId":"01","countyId":"03","tradeType":null,"billingtype":null,"thId":null,"enabledState":null},"token":"","tokenValidTime":null,"tradeType":1,"loginprivilege":null,"userPrivileges":null}]
     */

    private int code;
    private String msg;
    private List<LoginData> data;

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

    public List<LoginData> getData() {
        return data;
    }

    public void setData(List<LoginData> data) {
        this.data = data;
    }

}
