package wu.loushanyun.com.sixapp.m;

import java.util.List;

public class EnvironmentNameInfo {


    /**
     * responseTime : 1590572554948
     * message : success
     * code : 0
     * datas : ["57","58","63","64"]
     */

    private long responseTime;
    private String message;
    private int code;
    private List<String> datas;

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

    public List<String> getDatas() {
        return datas;
    }

    public void setDatas(List<String> datas) {
        this.datas = datas;
    }
}
