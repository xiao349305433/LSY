package wu.loushanyun.com.modulechiptest.m;

import java.util.List;

public class SelecorderInfo {


    /**
     * code : 0
     * msg : success
     * data : [{"id":49,"orderNumber":"LSY1571713027271","modulinspectionnum":0,"moduleordernum":1,"goodsId":1,"picaddress":"null","inspectiontype":0,"page":null,"size":null,"mloginFactoryNum":6},{"id":50,"orderNumber":"LSY1571713027271","modulinspectionnum":0,"moduleordernum":1,"goodsId":4,"picaddress":"null","inspectiontype":0,"page":null,"size":null,"mloginFactoryNum":6}]
     */

    private int code;
    private String msg;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 49
         * orderNumber : LSY1571713027271
         * modulinspectionnum : 0
         * moduleordernum : 1
         * goodsId : 1
         * picaddress : null
         * inspectiontype : 0
         * page : null
         * size : null
         * mloginFactoryNum : 6
         */

        private int id;
        private String orderNumber;
        private int modulinspectionnum;
        private int moduleordernum;
        private int goodsId;
        private String picaddress;
        private int inspectiontype; //质检状态（0：未质检，1：正在质检，2：已质检）
        private Object page;
        private Object size;
        private int mloginFactoryNum;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getOrderNumber() {
            return orderNumber;
        }

        public void setOrderNumber(String orderNumber) {
            this.orderNumber = orderNumber;
        }

        public int getModulinspectionnum() {
            return modulinspectionnum;
        }

        public void setModulinspectionnum(int modulinspectionnum) {
            this.modulinspectionnum = modulinspectionnum;
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

        public String getPicaddress() {
            return picaddress;
        }

        public void setPicaddress(String picaddress) {
            this.picaddress = picaddress;
        }

        public int getInspectiontype() {
            return inspectiontype;
        }

        public void setInspectiontype(int inspectiontype) {
            this.inspectiontype = inspectiontype;
        }

        public Object getPage() {
            return page;
        }

        public void setPage(Object page) {
            this.page = page;
        }

        public Object getSize() {
            return size;
        }

        public void setSize(Object size) {
            this.size = size;
        }

        public int getMloginFactoryNum() {
            return mloginFactoryNum;
        }

        public void setMloginFactoryNum(int mloginFactoryNum) {
            this.mloginFactoryNum = mloginFactoryNum;
        }
    }
}
