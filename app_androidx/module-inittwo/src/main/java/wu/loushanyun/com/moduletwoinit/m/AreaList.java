package wu.loushanyun.com.moduletwoinit.m;

import java.util.List;

public class AreaList {


    /**
     * msg : success
     * code : 0
     * data : [{"areaName":"华旭车间2","areaNumber":"106N842559A28N216646","saveTime":1531275992000}]
     */

    private String msg;
    private int code;
    private List<DataBean> data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * areaName : 华旭车间2
         * areaNumber : 106N842559A28N216646
         * saveTime : 1531275992000
         */

        private String areaName;
        private String areaNumber;
        private long saveTime;

        public String getAreaName() {
            return areaName;
        }

        public void setAreaName(String areaName) {
            this.areaName = areaName;
        }

        public String getAreaNumber() {
            return areaNumber;
        }

        public void setAreaNumber(String areaNumber) {
            this.areaNumber = areaNumber;
        }

        public long getSaveTime() {
            return saveTime;
        }

        public void setSaveTime(long saveTime) {
            this.saveTime = saveTime;
        }
    }
}
