package wu.loushanyun.com.moduletwoinit.m;

import java.util.List;

public class AreaDetail {

    /**
     * msg : success
     * code : 0
     * data : [{"sensingSignal":"2EV","manufacturersName":"贵州云通曙光科技服务有限公司","remark":"6","equipmentSN":"01C11117C6451A4A","equipmentTime":1530633600000},{"sensingSignal":"2EV","manufacturersName":"贵州云通曙光科技服务有限公司","remark":"3","equipmentSN":"01C11117C6870C02","equipmentTime":1530633600000},{"sensingSignal":"2EV","manufacturersName":"贵州云通曙光科技服务有限公司","remark":"","equipmentSN":"01C11117C64694C9","equipmentTime":1530633600000}]
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
         * sensingSignal : 2EV
         * manufacturersName : 贵州云通曙光科技服务有限公司
         * remark : 6
         * equipmentSN : 01C11117C6451A4A
         * equipmentTime : 1530633600000
         */

        private String sensingSignal;
        private String manufacturersName;
        private String remark;
        private String equipmentSN;
        private long equipmentTime;

        public String getSensingSignal() {
            return sensingSignal;
        }

        public void setSensingSignal(String sensingSignal) {
            this.sensingSignal = sensingSignal;
        }

        public String getManufacturersName() {
            return manufacturersName;
        }

        public void setManufacturersName(String manufacturersName) {
            this.manufacturersName = manufacturersName;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getEquipmentSN() {
            return equipmentSN;
        }

        public void setEquipmentSN(String equipmentSN) {
            this.equipmentSN = equipmentSN;
        }

        public long getEquipmentTime() {
            return equipmentTime;
        }

        public void setEquipmentTime(long equipmentTime) {
            this.equipmentTime = equipmentTime;
        }
    }
}
