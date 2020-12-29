package wu.loushanyun.com.modulechiptest.m;

public class SelectMoudulInfo {

    /**
     * msg : success
     * code : 0
     * data : {"id":0,"orderber":"LSY1571391853621","bodyNum":"154532","moduleId":null,"timeStamp":1571040628000,"moduleType":3,"chipNum":"fa","inspectionField":"fdg","page":0,"size":1,"mloginFactoryNum":null}
     */

    private String msg;
    private int code;
    private DataBean data;

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

    public static class DataBean {
        /**
         * id : 0
         * orderber : LSY1571391853621
         * bodyNum : 154532
         * moduleId : null
         * timeStamp : 1571040628000
         * moduleType : 3
         * chipNum : fa
         * inspectionField : fdg
         * page : 0
         * size : 1
         * mloginFactoryNum : null
         */

        private int id;
        private String orderber;
        private String bodyNum;
        private Object moduleId;
        private long timeStamp;
        private int moduleType;
        private String chipNum;
        private String inspectionField;
        private int page;
        private int size;
        private Object mloginFactoryNum;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getOrderber() {
            return orderber;
        }

        public void setOrderber(String orderber) {
            this.orderber = orderber;
        }

        public String getBodyNum() {
            return bodyNum;
        }

        public void setBodyNum(String bodyNum) {
            this.bodyNum = bodyNum;
        }

        public Object getModuleId() {
            return moduleId;
        }

        public void setModuleId(Object moduleId) {
            this.moduleId = moduleId;
        }

        public long getTimeStamp() {
            return timeStamp;
        }

        public void setTimeStamp(long timeStamp) {
            this.timeStamp = timeStamp;
        }

        public int getModuleType() {
            return moduleType;
        }

        public void setModuleType(int moduleType) {
            this.moduleType = moduleType;
        }

        public String getChipNum() {
            return chipNum;
        }

        public void setChipNum(String chipNum) {
            this.chipNum = chipNum;
        }

        public String getInspectionField() {
            return inspectionField;
        }

        public void setInspectionField(String inspectionField) {
            this.inspectionField = inspectionField;
        }

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public Object getMloginFactoryNum() {
            return mloginFactoryNum;
        }

        public void setMloginFactoryNum(Object mloginFactoryNum) {
            this.mloginFactoryNum = mloginFactoryNum;
        }
    }
}
