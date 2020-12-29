package wu.loushanyun.com.modulechiptest.m;

import java.util.List;

public class OrderListInfo {

    /**
     * msg : 查询成功
     * code : 0
     * data : {"orderNumber":"LSY1573530301935","detailAddress":"贵州省遵义市汇川区城上城1座103","receiveName":"童欢","receiveNumber":null,"acceptPhone":"11","tradingStatus":0,"deliveryTime":1573548455000,"trackingCompany":"11","trackingNumber":"11","modules":[{"goodsImg":"img/order_m01.png","goodsName":"LSY-M01","goodsIntroduction":"适用于物联网集中器1对多TTL接口采集一对一485、Mod-bus协议采集工业仪表","moduleordernum":10,"goodsId":1,"boxname":"娄山云1号"},{"goodsImg":"img/order_m02.png","goodsName":"LSY-M02","goodsIntroduction":"适用于网络端一对一接入2ev/3ev/无磁正反脉冲传感的智能设备","moduleordernum":10,"goodsId":2,"boxname":"娄山云2号"},{"goodsImg":"img/order_m03.png","goodsName":"LSY-M03","goodsIntroduction":"适用于总线网络接入2ev/3ev/无磁正反脉冲传感的智能表具","moduleordernum":10,"goodsId":3,"boxname":"娄山云3号"}],"manufacturerContacter":null,"productnumber":30,"orderTime":1573530301000}
     * instype : no
     */

    private String msg;
    private int code;
    private DataBean data;
    private String instype;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getInstype() {
        return instype;
    }

    public void setInstype(String instype) {
        this.instype = instype;
    }

    public static class DataBean {
        /**
         * orderNumber : LSY1573530301935
         * detailAddress : 贵州省遵义市汇川区城上城1座103
         * receiveName : 童欢
         * receiveNumber : null
         * acceptPhone : 11
         * tradingStatus : 0
         * deliveryTime : 1573548455000
         * trackingCompany : 11
         * trackingNumber : 11
         * modules : [{"goodsImg":"img/order_m01.png","goodsName":"LSY-M01","goodsIntroduction":"适用于物联网集中器1对多TTL接口采集一对一485、Mod-bus协议采集工业仪表","moduleordernum":10,"goodsId":1,"boxname":"娄山云1号"},{"goodsImg":"img/order_m02.png","goodsName":"LSY-M02","goodsIntroduction":"适用于网络端一对一接入2ev/3ev/无磁正反脉冲传感的智能设备","moduleordernum":10,"goodsId":2,"boxname":"娄山云2号"},{"goodsImg":"img/order_m03.png","goodsName":"LSY-M03","goodsIntroduction":"适用于总线网络接入2ev/3ev/无磁正反脉冲传感的智能表具","moduleordernum":10,"goodsId":3,"boxname":"娄山云3号"}]
         * manufacturerContacter : null
         * productnumber : 30
         * orderTime : 1573530301000
         */

        private String orderNumber;
        private String detailAddress;
        private String receiveName;
        private Object receiveNumber;
        private String acceptPhone;
        private int tradingStatus;
        private long deliveryTime;
        private String trackingCompany;
        private String trackingNumber;
        private Object manufacturerContacter;
        private int productnumber;
        private long orderTime;
        private List<ModulesBean> modules;

        public String getOrderNumber() {
            return orderNumber;
        }

        public void setOrderNumber(String orderNumber) {
            this.orderNumber = orderNumber;
        }

        public String getDetailAddress() {
            return detailAddress;
        }

        public void setDetailAddress(String detailAddress) {
            this.detailAddress = detailAddress;
        }

        public String getReceiveName() {
            return receiveName;
        }

        public void setReceiveName(String receiveName) {
            this.receiveName = receiveName;
        }

        public Object getReceiveNumber() {
            return receiveNumber;
        }

        public void setReceiveNumber(Object receiveNumber) {
            this.receiveNumber = receiveNumber;
        }

        public String getAcceptPhone() {
            return acceptPhone;
        }

        public void setAcceptPhone(String acceptPhone) {
            this.acceptPhone = acceptPhone;
        }

        public int getTradingStatus() {
            return tradingStatus;
        }

        public void setTradingStatus(int tradingStatus) {
            this.tradingStatus = tradingStatus;
        }

        public long getDeliveryTime() {
            return deliveryTime;
        }

        public void setDeliveryTime(long deliveryTime) {
            this.deliveryTime = deliveryTime;
        }

        public String getTrackingCompany() {
            return trackingCompany;
        }

        public void setTrackingCompany(String trackingCompany) {
            this.trackingCompany = trackingCompany;
        }

        public String getTrackingNumber() {
            return trackingNumber;
        }

        public void setTrackingNumber(String trackingNumber) {
            this.trackingNumber = trackingNumber;
        }

        public Object getManufacturerContacter() {
            return manufacturerContacter;
        }

        public void setManufacturerContacter(Object manufacturerContacter) {
            this.manufacturerContacter = manufacturerContacter;
        }

        public int getProductnumber() {
            return productnumber;
        }

        public void setProductnumber(int productnumber) {
            this.productnumber = productnumber;
        }

        public long getOrderTime() {
            return orderTime;
        }

        public void setOrderTime(long orderTime) {
            this.orderTime = orderTime;
        }

        public List<ModulesBean> getModules() {
            return modules;
        }

        public void setModules(List<ModulesBean> modules) {
            this.modules = modules;
        }

        public static class ModulesBean {
            /**
             * goodsImg : img/order_m01.png
             * goodsName : LSY-M01
             * goodsIntroduction : 适用于物联网集中器1对多TTL接口采集一对一485、Mod-bus协议采集工业仪表
             * moduleordernum : 10
             * goodsId : 1
             * boxname : 娄山云1号
             */

            private String goodsImg;
            private String goodsName;
            private String goodsIntroduction;
            private int moduleordernum;
            private int goodsId;
            private String boxname;

            public String getGoodsImg() {
                return goodsImg;
            }

            public void setGoodsImg(String goodsImg) {
                this.goodsImg = goodsImg;
            }

            public String getGoodsName() {
                return goodsName;
            }

            public void setGoodsName(String goodsName) {
                this.goodsName = goodsName;
            }

            public String getGoodsIntroduction() {
                return goodsIntroduction;
            }

            public void setGoodsIntroduction(String goodsIntroduction) {
                this.goodsIntroduction = goodsIntroduction;
            }

            public int getModuleordernum() {
                return moduleordernum;
            }

            public void setModuleordernum(int moduleordernum) {
                this.moduleordernum = moduleordernum;
            }

            public int getGoodsId() {
                return goodsId;
            }

            public void setGoodsId(int goodsId) {
                this.goodsId = goodsId;
            }

            public String getBoxname() {
                return boxname;
            }

            public void setBoxname(String boxname) {
                this.boxname = boxname;
            }
        }
    }
}
