package wu.loushanyun.com.modulechiptest.m;

import java.util.List;

public class FactoryLoginData {


    /**
     * msg : Login successful
     * code : 0
     * data : [{"id":99,"loginName":"hhhuuuxxx","companyName":"hhhhhh","tel":"15527919058","contacts":"hhhhh","companyPhone":"18627798893","provinceId":"湖北省","cityId":"武汉市","countyId":"江汉区","addressInfo":"888888","businessLicense":"static/LouShanCloud_enterpriseLogo/c4d12436-5c29-4999-a50c-1ae3044b3168.png","password":"Yell0ZX/Cj/KuA0Cz7tFGw==","qualified":1,"remark":null,"serialNumber":"1805281912B","manufacturersIdentification":21,"registTime":1527477474000,"role":1,"unit":null,"supplier":null,"credits":"100000","enabledState":0},{"id":100,"loginName":"sfcsqy","companyName":"收费测试企业","tel":"15527919058","contacts":"徐相彬","companyPhone":"18294858391","provinceId":"湖北省","cityId":"武汉市","countyId":"江汉区","addressInfo":"武汉天街小区2栋1203","businessLicense":"static/LouShanCloud_enterpriseLogo/038e130b-ac07-48bb-ae23-084548f4da7d.png","password":"P6487JMoXMe7ddH/b+pmng==","qualified":1,"remark":null,"serialNumber":"1806010256B","manufacturersIdentification":22,"registTime":1527763888000,"role":1,"unit":null,"supplier":null,"credits":"1000000","enabledState":0},{"id":135,"loginName":"xiongwanxiong","companyName":"hhhhhh","tel":"15527919058","contacts":"熊万熊","companyPhone":"18627798893","provinceId":"湖北省","cityId":"武汉市","countyId":"江汉区","addressInfo":"888888","businessLicense":"static/LouShanCloud_enterpriseLogo/c4d12436-5c29-4999-a50c-1ae3044b3168.png","password":"dCx0E37seE/cVCfDxTdd/g==","qualified":1,"remark":"测试","serialNumber":"1805281912B","manufacturersIdentification":21,"registTime":1530868881000,"role":2,"unit":"娄山云","supplier":null,"credits":"100000","enabledState":0}]
     */

    private String msg;
    private String code;
    private List<ProductRegister> data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<ProductRegister> getData() {
        return data;
    }

    public void setData(List<ProductRegister> data) {
        this.data = data;
    }

}
