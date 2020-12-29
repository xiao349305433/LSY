package com.wu.loushanyun.basemvp.m;

import java.util.List;

public class ModuleRuleDetail {

    /**
     * code : 0
     * msg : success
     * data : [{"id":240,"parseId":34,"startBit":17,"endBit":88,"field":"read","chineseName":"正向体积","command":"01 03 00 18 00 04 C4 0E","byteMode":1,"orderNumber":1,"maxByte":13},{"id":241,"parseId":34,"startBit":17,"endBit":88,"field":"reverseVolume","chineseName":"反向体积","command":"01 03 00 20 00 04 45 C3","byteMode":1,"orderNumber":2,"maxByte":13},{"id":242,"parseId":34,"startBit":17,"endBit":40,"field":"flow","chineseName":"水流流向","command":"01 03 00 33 00 01 74 05","byteMode":1,"orderNumber":3,"maxByte":7},{"id":243,"parseId":34,"startBit":17,"endBit":88,"field":"currentFlow","chineseName":"当前流量","command":"01 03 00 29 00 04 95 C1","byteMode":1,"orderNumber":4,"maxByte":13}]
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
         * id : 240
         * parseId : 34
         * startBit : 17
         * endBit : 88
         * field : read
         * chineseName : 正向体积
         * command : 01 03 00 18 00 04 C4 0E
         * byteMode : 1
         * orderNumber : 1
         * maxByte : 13
         */

        private int id;
        private int parseId;
        private int startBit;
        private int endBit;
        private String field;
        private String chineseName;
        private String command;
        private int byteMode;
        private int orderNumber;
        private int maxByte;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getParseId() {
            return parseId;
        }

        public void setParseId(int parseId) {
            this.parseId = parseId;
        }

        public int getStartBit() {
            return startBit;
        }

        public void setStartBit(int startBit) {
            this.startBit = startBit;
        }

        public int getEndBit() {
            return endBit;
        }

        public void setEndBit(int endBit) {
            this.endBit = endBit;
        }

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        public String getChineseName() {
            return chineseName;
        }

        public void setChineseName(String chineseName) {
            this.chineseName = chineseName;
        }

        public String getCommand() {
            return command;
        }

        public void setCommand(String command) {
            this.command = command;
        }

        public int getByteMode() {
            return byteMode;
        }

        public void setByteMode(int byteMode) {
            this.byteMode = byteMode;
        }

        public int getOrderNumber() {
            return orderNumber;
        }

        public void setOrderNumber(int orderNumber) {
            this.orderNumber = orderNumber;
        }

        public int getMaxByte() {
            return maxByte;
        }

        public void setMaxByte(int maxByte) {
            this.maxByte = maxByte;
        }
    }
}
