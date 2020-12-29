package wu.loushanyun.com.modulerepair.m;

import java.util.List;

public class RepairDataMsg {


    /**
     * msg : success
     * code : 0
     * data : [{"sn":"01C11117C6FE8375","frequencyBand":"470MHZ","sendingPower":"17db","frequency":"72小时","snr":null,"rssi":null,"sf":"SF11","channel":"模式A","productForm":3,"version":"0100","sensingSignal":3,"remark":"SF11","longitude":"106.843544","latitude":"28.217384","offLineTime":"-1"}]
     */

    private String msg;
    private int code;
    private List<RepairData> data;

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

    public List<RepairData> getData() {
        return data;
    }

    public void setData(List<RepairData> data) {
        this.data = data;
    }

}
