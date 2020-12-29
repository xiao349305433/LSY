package wu.loushanyun.com.modulechiptest.m;

import java.util.List;
/**
 * 查询设备历史数据的返回实体类
 * 
 */
public class SensoroDeviceInfo {


    /**
     * err_code : 0
     * msg : 查询成功
     * total : 261
     * data : [{"msgId":"5bebdef0334d9500115bcf74","createdTime":"1542184688801","sn":"01C11117C639AAC2","data":{"customer":"001a030a00f18b6802261802000018020000180200000101801209011f"}}]
     */

    private int err_code;
    private String msg;
    private int total;
    private List<DataBeanX> data;

    public int getErr_code() {
        return err_code;
    }

    public void setErr_code(int err_code) {
        this.err_code = err_code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<DataBeanX> getData() {
        return data;
    }

    public void setData(List<DataBeanX> data) {
        this.data = data;
    }

    public static class DataBeanX {
        /**
         * msgId : 5bebdef0334d9500115bcf74
         * createdTime : 1542184688801
         * sn : 01C11117C639AAC2
         * data : {"customer":"001a030a00f18b6802261802000018020000180200000101801209011f"}
         */

        private String msgId;
        private String createdTime;
        private String sn;
        private DataBean data;

        public String getMsgId() {
            return msgId;
        }

        public void setMsgId(String msgId) {
            this.msgId = msgId;
        }

        public String getCreatedTime() {
            return createdTime;
        }

        public void setCreatedTime(String createdTime) {
            this.createdTime = createdTime;
        }

        public String getSn() {
            return sn;
        }

        public void setSn(String sn) {
            this.sn = sn;
        }

        public DataBean getData() {
            return data;
        }

        public void setData(DataBean data) {
            this.data = data;
        }

        public static class DataBean {
            /**
             * customer : 001a030a00f18b6802261802000018020000180200000101801209011f
             */

            private String customer;

            public String getCustomer() {
                return customer;
            }

            public void setCustomer(String customer) {
                this.customer = customer;
            }
        }
    }
}
