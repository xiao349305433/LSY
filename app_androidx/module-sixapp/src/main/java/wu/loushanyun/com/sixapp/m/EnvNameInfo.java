package wu.loushanyun.com.sixapp.m;

import java.util.List;

public class EnvNameInfo {

    /**
     * responseTime : 1593761587516
     * message : success
     * code : 0
     * datas : [{"envName":"1","demarcateTime":"2020-06-17 19:22:36"}]
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
         * envName : 1
         * demarcateTime : 2020-06-17 19:22:36
         */

        private String envName;
        private String demarcateTime;

        public String getEnvName() {
            return envName;
        }

        public void setEnvName(String envName) {
            this.envName = envName;
        }

        public String getDemarcateTime() {
            return demarcateTime;
        }

        public void setDemarcateTime(String demarcateTime) {
            this.demarcateTime = demarcateTime;
        }
    }
}
