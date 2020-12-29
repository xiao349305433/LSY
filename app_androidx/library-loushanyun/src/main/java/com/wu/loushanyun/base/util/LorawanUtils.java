package com.wu.loushanyun.base.util;

import com.elvishew.xlog.XLog;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import met.hx.com.librarybase.some_utils.ByteConvertUtils;
import met.hx.com.librarybase.some_utils.TimeUtils;

/**
 *
 */
public class LorawanUtils {


    /**
     * 读取表信息，单表s芯片植入
     * 0x22
     *
     * @return
     */
    public static byte[] getSingleReading() {
        byte[] result = new byte[5];
        result[0] = (byte) 0x68;
        result[1] = (byte) 0x01;
        result[2] = 0x22;

        //校验
        byte cs = 0;
        for (int i = 1; i < 3; i++) {
            cs += result[i];
        }
        result[3] = cs;
        result[4] = 0x16;
        return result;
    }

    /**
     * 通过表号读取表的信息，集中器S芯片植入
     * 0x33
     *
     * @return
     */
    public static byte[] getJizhongReading(int num) {
        byte[] result = new byte[6];
        result[0] = (byte) 0x68;
        result[1] = (byte) 0x02;
        result[2] = 0x33;
        result[3] = (byte) (num & 0xff);
        //校验
        byte cs = 0;
        for (int i = 1; i < 4; i++) {
            cs += result[i];
        }
        result[4] = cs;
        result[5] = 0x16;
        return result;
    }


    /**
     * 设置底数和倍率
     * 0x09
     *
     * @return
     */
    public static byte[] getbellvSetting(String dishu, String beilv) {
        long mai = Long.valueOf(dishu);
        byte[] result = new byte[10];
        result[0] = (byte) 0x68;
        result[1] = (byte) 0x07;
        result[2] = 0x09;
        for (int i = 0; i <= 4; i++) {
            result[i + 3] = (byte) (mai & 0xff);
            mai = mai >> 8;
        }
        if ("0.001".equals(beilv)) {
            result[7] = 0x00;
        } else if ("0.01".equals(beilv)) {
            result[7] = 0x01;
        } else if ("0.1".equals(beilv)) {
            result[7] = 0x02;
        } else if ("1".equals(beilv)) {
            result[7] = 0x03;
        } else if ("10".equals(beilv)) {
            result[7] = 0x04;
        } else if ("100".equals(beilv)) {
            result[7] = 0x05;
        } else {
            result[7] = 0x00;
        }


        //校验
        byte cs = 0;
        for (int i = 1; i < 8; i++) {
            cs += result[i];
        }
        result[8] = cs;
        result[9] = 0x16;
        return result;
    }


    /**
     * 设置设备ID
     * 0x08
     *
     * @return
     */
    public static byte[] getIDSetting(String ID) {
        long mai = Long.valueOf(ID);
        XLog.i("mai===="+mai);
        byte[] result = new byte[11];
        result[0] = (byte) 0x68;
        result[1] = (byte) 0x07;
        result[2] = 0x08;
        for (int i = 0; i < 6; i++) {
            result[3 + i] = (byte) ((mai & ((long) 0xff << (8 * i))) >> 8 * i);
        }
//        for (int i = 0; i < 6; i++) {
//            result[i + 3] = (byte) (mai & 0xff);
//            mai = mai >> 8;
//        }

        //校验
        byte cs = 0;
        for (int i = 1; i < 9; i++) {
            cs += result[i];
        }
        result[9] = cs;
        result[10] = 0x16;
        return result;
    }

    /**
     * 通过表设备ID读取表的信息，集中器S芯片植入
     * 0x34
     *
     * @return
     */
    public static byte[] getIDReading(int ID) {
        byte[] result = new byte[11];
        long mai = Long.valueOf(ID);
        result[0] = (byte) 0x68;
        result[1] = (byte) 0x07;
        result[2] = 0x34;
        for (int i = 0; i <= 6; i++) {
            result[i + 3] = (byte) (mai & 0xff);
            mai = mai >> 8;
        }
        //校验
        byte cs = 0;
        for (int i = 1; i < 9; i++) {
            cs += result[i];
        }
        result[9] = cs;
        result[10] = 0x16;
        return result;
    }


    /**
     * 设置出厂设置
     * 0x12
     *
     * @return
     */
    public static byte[] getChuChangWrite(String dishu, String beilv,String chuangan,String leixing,int fuwuID,int zhizaoID,String jixin) {
        long mai = Long.valueOf(dishu);
        long fuwu = Long.valueOf(fuwuID);
        byte[] result = new byte[16];
        result[0] = (byte) 0x68;
        result[1] = (byte) 0x0C;
        result[2] = 0x12;
        for (int i = 0; i <= 4; i++) {
            result[i + 3] = (byte) (mai & 0xff);
            mai = mai >> 8;
        }
        if ("0.001".equals(beilv)) {
            result[7] = 0x00;
        } else if ("0.01".equals(beilv)) {
            result[7] = 0x01;
        } else if ("0.1".equals(beilv)) {
            result[7] = 0x02;
        } else if ("1".equals(beilv)) {
            result[7] = 0x03;
        } else if ("10".equals(beilv)) {
            result[7] = 0x04;
        } else if ("100".equals(beilv)) {
            result[7] = 0x05;
        } else {
            result[7] = 0x00;
        }

        if ("0-5V".equals(chuangan)) {
            result[8] = 0x00;
        } else if ("3EV".equals(chuangan)) {
            result[8] = 0x01;
        } else if ("正反脉冲".equals(chuangan)) {
            result[8] = 0x02;
        } else if ("2EV".equals(chuangan)) {
            result[8] = 0x03;
        } else if ("TTL1一对多".equals(chuangan)) {
            result[8] = 0x04;
        } else if ("TTL2一对一".equals(chuangan)) {
            result[8] = 0x05;
        } else if ("RS485".equals(chuangan)) {
            result[8] = 0x06;
        } else if ("电子开关".equals(chuangan)) {
            result[8] = 0x07;
        } else if ("4-20MA".equals(chuangan)) {
            result[8] = 0x08;
        } else if ("EV".equals(chuangan)) {
            result[8] = 0x09;
        } else if ("TTL3".equals(chuangan)) {
            result[8] = 0x0a;
        } else if ("485一对多".equals(chuangan)) {
            result[8] = 0x0b;
        }  else if ("Mod-BUS".equals(chuangan)) {
            result[8] = 0x0c;
        } else {
            result[8] = 0x00;
        }

        if ("一对一".equals(leixing)) {
            result[9] = 0x00;
        } else if ("一对多".equals(leixing)) {
            result[9] = 0x01;
        }else {
            result[9] = 0x00;
        }
        for (int i = 0; i <= 2; i++) {
            result[i + 10] = (byte) (fuwu & 0xff);
            fuwu = fuwu >> 8;
        }
        result[12] =  (byte) (zhizaoID & 0xff);

        if ("干式".equals(jixin)) {
            result[13] = 0x00;
        } else if ("湿式".equals(jixin)) {
            result[13] = 0x01;
        }else if ("机电".equals(jixin)) {
            result[13] = 0x02;
        } else if ("超声波".equals(jixin)) {
            result[13] = 0x03;
        }

        //校验
        byte cs = 0;
        for (int i = 1; i < 14; i++) {
            cs += result[i];
        }
        result[14] = cs;
        result[15] = 0x16;
        return result;
    }


    /**
     * 读取版本号
     * 0x13
     *
     * @return
     */
    public static byte[] getBanbenReading() {
        byte[] result = new byte[5];
        result[0] = (byte) 0x68;
        result[1] = (byte) 0x01;
        result[2] = 0x13;
        //校验
        byte cs = 0;
        for (int i = 1; i < 3; i++) {
            cs += result[i];
        }
        result[3] = cs;
        result[4] = 0x16;
        return result;
    }

    /**
     * 读取最后上送时间
     * 0x14
     *
     * @return
     */
    public static byte[] getLastTimeReading() {
        byte[] result = new byte[5];
        result[0] = (byte) 0x68;
        result[1] = (byte) 0x01;
        result[2] = 0x14;
        //校验
        byte cs = 0;
        for (int i = 1; i < 3; i++) {
            cs += result[i];
        }
        result[3] = cs;
        result[4] = 0x16;
        return result;
    }

    /**
     * 读取通讯状态
     * 0x15
     *
     * @return
     */
    public static byte[] getTongXuStateReading() {
        byte[] result = new byte[5];
        result[0] = (byte) 0x68;
        result[1] = (byte) 0x01;
        result[2] = 0x15;
        //校验
        byte cs = 0;
        for (int i = 1; i < 3; i++) {
            cs += result[i];
        }
        result[3] = cs;
        result[4] = 0x16;
        return result;
    }

    /**
     * 读取出厂设置
     * 0x16
     *
     * @return
     */
    public static byte[] getChuChangReading() {
        byte[] result = new byte[5];
        result[0] = (byte) 0x68;
        result[1] = (byte) 0x01;
        result[2] = 0x16;
        //校验
        byte cs = 0;
        for (int i = 1; i < 3; i++) {
            cs += result[i];
        }
        result[3] = cs;
        result[4] = 0x16;
        return result;
    }


    /**
     * 读取电池电压
     * 0x07
     *
     * @return
     */
    public static byte[] getDianchiReading() {
        byte[] result = new byte[5];
        result[0] = (byte) 0x68;
        result[1] = (byte) 0x01;
        result[2] = 0x07;
        //校验
        byte cs = 0;
        for (int i = 1; i < 3; i++) {
            cs += result[i];
        }
        result[3] = cs;
        result[4] = 0x16;
        return result;
    }

    /**
     * 读取一串表连接个数
     * 0x35
     *
     * @return
     */
    public static byte[] getALLReading() {
        byte[] result = new byte[5];
        result[0] = (byte) 0x68;
        result[1] = (byte) 0x01;
        result[2] = 0x35;
        //校验
        byte cs = 0;
        for (int i = 1; i < 3; i++) {
            cs += result[i];
        }
        result[3] = cs;
        result[4] = 0x16;
        return result;
    }


    /**
     * 手动开关阀门
     * 0 x23
     *
     * @return
     */
    public static byte[] getvavle() {
        byte[] result = new byte[5];
        result[0] = (byte) 0x68;
        result[1] = (byte) 0x01;
        result[2] = 0x23;
        //校验
        byte cs = 0;
        for (int i = 1; i < 3; i++) {
            cs += result[i];
        }
        result[3] = cs;
        result[4] = 0x16;
        return result;
    }


    /**
     * 激活
     * 0 x01
     *
     * @return
     */
    public static byte[] getjihuo(boolean jihuo) {
        byte[] result = new byte[6];
        result[0] = (byte) 0x68;
        result[1] = (byte) 0x02;
        result[2] = 0x01;
        result[3] = (byte)(jihuo?0x01:0x00);
        //校验
        byte cs = 0;
        for (int i = 1; i < 4; i++) {
            cs += result[i];
        }
        result[4] = cs;
        result[5] = 0x16;
        return result;
    }


    /**
     * 休眠
     * 0 x02
     *
     * @return
     */
    public static byte[] getxiumian() {
        byte[] result = new byte[5];
        result[0] = (byte) 0x68;
        result[1] = (byte) 0x01;
        result[2] = 0x02;
        //校验
        byte cs = 0;
        for (int i = 1; i < 3; i++) {
            cs += result[i];
        }
        result[3] = cs;
        result[4] = 0x16;
        return result;
    }



    /**
     * 0x05
     * 获取校准时间
     *
     * @return
     */
    public static byte[] getjiaozhun() {
        byte[] result = new byte[11];
        result[0] = 0x68;
        result[1] = 0x07;
        result[2] = 0x05;
        long isn = TimeUtils.getCurTimeMills();
        for (int i = 0; i < 6; i++) {
            result[i + 3] = (byte) (isn & 0xff);
            isn = isn >> 8;
        }

        //校验
        byte cs = 0;
        for (int i = 1; i < 9; i++) {
            cs += result[i];
        }
        result[9] = (byte) (cs & 0xff);
        result[10] = 0x16;
        return result;
    }

    /**
     * 设置发送频率
     * 0 x06
     *
     * @return
     */
    public static byte[] getPinLv() {
        byte[] result = new byte[5];
        result[0] = (byte) 0x68;
        result[1] = (byte) 0x01;
        result[2] = 0x06;
        //校验
        byte cs = 0;
        for (int i = 1; i < 3; i++) {
            cs += result[i];
        }
        result[3] = cs;
        result[4] = 0x16;
        return result;
    }


    /**
     * 通过0x11命令，如果返回type=1则是1号模组，如果type=2则是2号模组
     *
     * @param result
     * @return
     * @throws Exception
     */
    public static final int getModuleType(byte[] result) throws Exception {
        int type = 0;
        if (result.length == 24) {
            type = 2;
        } else if (result.length == 29) {
            type = 1;
        } else if (result.length == 26) {
            type = 4;//4号模组数字信号
        } else if (result.length == 27) {
            type = 5;//4号模组模拟信号
        } else if (result.length == 28) {
            type = 6;//Lorawan
        }
        return type;
    }

    /**
     * 0x03
     * 设置最大发送次数，由单片机控制重发
     *
     * @return
     */
    public static byte[] getLorawanTimes(String jiaohu) {
        byte[] result = new byte[6];
        result[0] = 0x68;
        result[1] = 0x02;
        result[2] = 0x03;
        result[3] = (byte) (Integer.valueOf(jiaohu) & 0xff);
        //校验
        byte cs = 0;
        for (int i = 1; i < 4; i++) {
            cs += result[i];
        }
        result[4] = (byte) (cs & 0xff);
        result[5] = 0x16;
        return result;
    }

    /**
     * 0x04
     * 强制发送
     *
     * @return
     */
    public static byte[] getQiangZhi(){
        byte[] result = new byte[5];
        result[0] = (byte) 0x68;
        result[1] = (byte) 0x01;
        result[2] = 0x04;

        //校验
        byte cs = 0;
        for (int i = 1; i < 3; i++) {
            cs += result[i];
        }
        result[3] = cs;
        result[4] = 0x16;
        return result;


    }
    /**
     * 0x06
     * 设置发送频率
     *
     * @return
     */
    public static byte[] getPinlvBytes(String pinlv) {
        byte[] result = new byte[6];
        result[0] = 0x68;
        result[1] = 0x02;
        result[2] = 0x06;
        if ("1分钟".equals(pinlv)) {
            result[3] = 0x02;
        } else if ("5分钟".equals(pinlv)) {
            result[3] = 0x04;
        } else if ("15分钟".equals(pinlv)) {
            result[3] = 0x00;
        } else if ("30分钟".equals(pinlv)) {
            result[3] = 0x05;
        } else if ("1小时".equals(pinlv)) {
            result[3] = 0x06;
        } else if ("3小时".equals(pinlv)) {
            result[3] = 0x07;
        } else if ("12小时".equals(pinlv)) {
            result[3] = 0x09;
        } else if ("24小时".equals(pinlv)) {
            result[3] = 0x03;
        } else if ("72小时".equals(pinlv)) {
            result[3] = 0x08;
        } else if ("6小时".equals(pinlv)) {
            result[3] = 0x01;
        }
        //校验
        byte cs = 0;
        for (int i = 1; i < 4; i++) {
            cs += result[i];
        }
        result[4] = (byte) (cs & 0xff);
        result[5] = 0x16;
        return result;
    }


    public static ArrayList<String> getAllWangLuoJiaoHu() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("最大发送1次");
        arrayList.add("最大发送2次");
        arrayList.add("最大发送3次");
        return arrayList;
    }
        //反馈次数
    public static ArrayList<String> getAllLuoJiaoHu() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("1次");
        arrayList.add("2次");
        arrayList.add("3次");
        return arrayList;
    }


    public static ArrayList<String> getAllPinLv() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("1分钟");
        arrayList.add("5分钟");
        arrayList.add("15分钟");
        arrayList.add("30分钟");
        arrayList.add("1小时");
        arrayList.add("3小时");
        arrayList.add("6小时");
        arrayList.add("12小时");
        arrayList.add("24小时");
        arrayList.add("72小时");
        return arrayList;
    }

    public static ArrayList<String> getJieRu() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("一对一");
        arrayList.add("一对多");
        return arrayList;
    }


    public static ArrayList<String> getAllChuanGan() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("0-5V");
        arrayList.add("3EV");
        arrayList.add("正反脉冲");
        arrayList.add("2EV");
        arrayList.add("TTL1一对多");
        arrayList.add("TTL2一对一");
        arrayList.add("RS485");
        arrayList.add("电子开关");
        arrayList.add("4-20MA");
        arrayList.add("EV");
        arrayList.add("TTL3");
        arrayList.add("485一对多");
        arrayList.add("Mod-BUS");
        return arrayList;
    }


    /**
     * 获取倍率
     *
     * @return
     */
    public static ArrayList<String> getAllBeiLv() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("0.001");
        arrayList.add("0.01");
        arrayList.add("0.1");
        arrayList.add("1");
        arrayList.add("10");
        arrayList.add("100");
        return arrayList;
    }

    /**
     * 获取机芯
     *
     * @return
     */
    public static ArrayList<String> getAllJiXin() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("干式");
        arrayList.add("湿式");
        arrayList.add("机电");
        arrayList.add("超声波");
        return arrayList;
    }





    //读取_识别模块信息（命令0x34）
    public static final HashMap<String, String> getLorawanID(byte[] result) throws Exception {
        HashMap<String, String> map = new HashMap<>();

        String binary = ByteConvertUtils.conver2BinaryStr(result[4]);
        map.put(MapParams.表磁场外干扰, String.valueOf(binary.charAt(0)));
        map.put(MapParams.表拆卸状态, String.valueOf(binary.charAt(1)));
        map.put(MapParams.表读表状态, String.valueOf(binary.charAt(2)));
        map.put(MapParams.表电源状态, String.valueOf(binary.charAt(3)));
        map.put(MapParams.表倒流状态, String.valueOf(binary.charAt(4)));
        map.put(MapParams.寻址方式, String.valueOf(binary.charAt(5)));

        map.put(MapParams.连接的表个数, String.valueOf(result[5] & 0xff));
        long tmp;
        tmp = 0;
        for (int i = 6; i > 0; i--) {
            tmp = (tmp * 256) + (result[i + 7] & 0xff);
        }
        map.put(MapParams.表ID, String.valueOf(tmp));

        tmp = 0;
        for (int i = 4; i > 0; i--) {
            tmp = (tmp * 256) + (result[i + 12] & 0xff);
        }
        map.put(MapParams.表计数, String.valueOf(tmp));

        map.put(MapParams.表号, String.valueOf(result[16] & 0xff));
        map.put(MapParams.表倍率, String.valueOf(result[17] & 0xff));
        tmp = 0;
        for (int i = 3; i > 0; i--) {
            tmp = (tmp * 256) + (result[i + 12] & 0xff);
        }
        map.put(MapParams.表累积倒流数, String.valueOf(tmp));

        map.put(MapParams.表传感信号, String.valueOf(result[21] & 0xff));
        return map;
    }

    //读取电池电压（命令0x07）
    public static final HashMap<String, String> getLorawanDianchi(byte[] result) throws Exception {
        HashMap<String, String> map = new HashMap<>();
        map.put(MapParams.电池电压, String.valueOf(result[4] & 0xff));
        return map;
    }



    //读取_识别模块信息（命令0x35）
    public static final HashMap<String, String> getLorawanALL(byte[] result) throws Exception {
        HashMap<String, String> map = new HashMap<>();
        map.put(MapParams.连接数, String.valueOf(result[4] & 0xff));
        return map;
    }


    //读取_识别模块信息（命令0x11）
    public static final HashMap<String, String> getLorawanInfoAll(byte[] result) throws Exception {
        HashMap<String, String> map = new HashMap<>();
        map.put(MapParams.产品形式, String.valueOf(result[4] & 0xff));
        map.put(MapParams.平台类型, String.valueOf(result[5] & 0xff));
        map.put(MapParams.传感模式, String.valueOf(result[6] & 0xff));

        String binary = ByteConvertUtils.conver2BinaryStr(result[7]);
        map.put(MapParams.电源类型, String.valueOf(binary.charAt(0)));
        map.put(MapParams.物联电池状态, String.valueOf(binary.charAt(1)));
        map.put(MapParams.采集场景, String.valueOf(binary.charAt(2)));
        map.put(MapParams.硬件匹配, String.valueOf(binary.charAt(3)));
        map.put(MapParams.接入模式, String.valueOf(binary.charAt(4)));
        map.put(MapParams.通讯状态, String.valueOf(binary.charAt(5)));
        map.put(MapParams.蓝牙, String.valueOf(binary.charAt(6)));

        map.put(MapParams.传感信号, String.valueOf(result[8] & 0xff));
        map.put(MapParams.厂家标识, String.valueOf(((result[9] & 0xff) + ((result[10] & 0xff) << 8))));
        map.put(MapParams.制造企业ID, String.valueOf(result[11] & 0xff));
        map.put(MapParams.产品固件版本号, String.valueOf(result[12] & 0xff));
        map.put(MapParams.发送频率, String.valueOf(result[13] & 0xff));
        long tmp;
//        tmp = 0;
////        for (int i = 5; i > 0; i--) {
////            tmp = (tmp * 256) + (result[i + 14] & 0xff);
////        }
////        map.put(MapParams.设备ID, String.valueOf(tmp));
        map.put(MapParams.预警电压, String.valueOf(result[14] & 0xff + result[15] & 0xff));
        //   map.put(MapParams.电池配置, String.valueOf(result[16]& 0xff));
        //   map.put(MapParams.网络反馈, String.valueOf(result[22]& 0xff));
        map.put(MapParams.公共字节区协议版本号, String.valueOf(result[16] & 0xff));
        map.put(MapParams.第三方功能区协议版本号, String.valueOf(result[17] & 0xff));
        map.put(MapParams.系统激活停用状态, String.valueOf(result[18] & 0xff));
        map.put(MapParams.最大发送次数, String.valueOf(result[19] & 0xff));
        tmp = 0;
        for (int i = 6; i > 0; i--) {
            tmp = tmp * 256 + (long) (result[i + 20] & 0xff);
        }
        map.put(MapParams.系统时间, String.valueOf(tmp));

        return map;
    }



    //读取_出厂设置（命令0x16）
    public static final HashMap<String, String> getReadChuChangSetting(byte[] result) throws Exception {
        HashMap<String, String> map = new HashMap<>();
        long tmp=0;
        tmp = 0;
        for (int i = 4; i > 0; i--) {
            tmp = (tmp * 256) + (result[i + 3] & 0xff);
        }
//        for (int i = 4; i >= 0; i--) {
//            stringBuffer.append(result[i + 4]);
//        }

        //TODO 脉冲底数有问题
        map.put(MapParams.脉冲底数, String.valueOf(tmp));
        map.put(MapParams.倍率,LorawanUtils.getBiaoBeilvStringByCode(String.valueOf(result[8])));
        map.put(MapParams.传感信号,LorawanUtils.getChuanGanXinHaoByCode(String.valueOf(result[9])));
        map.put(MapParams.接入类型,LorawanUtils.getJieRuTypeByCode(String.valueOf(result[10])));
//        tmp = 0;
//        for (int i = 2; i > 0; i--) {
//            tmp = (tmp * 256) + (result[i + 10] & 0xff);
//        }
        map.put(MapParams.服务商ID,String.valueOf(((result[11] & 0xff) + ((result[12] & 0xff) << 8))));
        map.put(MapParams.制造商ID,String.valueOf(result[11] & 0xff));
        map.put(MapParams.机芯类型,LorawanUtils.getJiXinStringByCode(String.valueOf(result[14])));

       // map.put(MapParams.连接数, String.valueOf(result[4] & 0xff));
        return map;
    }


    //读取表信息 单表s芯片植入（命令0x22）
    public static final HashMap<String, String> getLorawanSingle(byte[] result) throws Exception {
        HashMap<String, String> map = new HashMap<>();
        long tmp;
        tmp = 0;
        for (int i = 6; i > 0; i--) {
            tmp = (tmp * 256) + (result[i + 3] & 0xff);
        }
        map.put(MapParams.设备ID, String.valueOf(tmp));

        String binary = ByteConvertUtils.conver2BinaryStr(result[10]);
        map.put(MapParams.复合电容, String.valueOf(binary.charAt(7)));
        map.put(MapParams.欠压状态, String.valueOf(binary.charAt(6)));
        map.put(MapParams.流向状态, String.valueOf(binary.charAt(5)));
        map.put(MapParams.拆卸状态, String.valueOf(binary.charAt(4)));
        map.put(MapParams.阀门有无, String.valueOf(binary.charAt(3)));
        map.put(MapParams.阀门状态, String.valueOf(binary.charAt(2)));
        map.put(MapParams.磁干扰, String.valueOf(binary.charAt(1)));
        map.put(MapParams.模拟信号, String.valueOf(binary.charAt(0)));
        //当result[11]长度只有1时，需要增加一个0
    String hex11= Integer.toHexString(result[11] & 0xff);
    hex11=hex11.length()==2?hex11:"0"+hex11;
        map.put(MapParams.机芯类型,  hex11.substring(0, 1));

        map.put(MapParams.最后计数时间, hex11.substring(1, 2) +   DataParser.byteToString(result[12])+   DataParser.byteToString(result[13]) +   DataParser.byteToString(result[14]));

        tmp = 0;
        for (int i = 4; i > 0; i--) {
            tmp = (tmp * 256) + (result[i + 14] & 0xff);
        }
        map.put(MapParams.正计数, String.valueOf(tmp));
        tmp = 0;
        for (int i = 4; i > 0; i--) {
            tmp = (tmp * 256) + (result[i + 18] & 0xff);
        }
        map.put(MapParams.反计数, String.valueOf(tmp));
        map.put(MapParams.倍率, String.valueOf(result[23] & 0xff));
        map.put(MapParams.预警电压值, String.valueOf(result[24] + "." + result[25]));
//        if(MapParams.模拟信号=="0"){
//            tmp = 0;
//            for (int i = 4; i > 0; i--) {
//                tmp = (tmp * 256) + (result[i + 25] & 0xff);
//            }
//            map.put(MapParams.六点的读数, String.valueOf(tmp));
//            tmp = 0;
//            for (int i = 4; i > 0; i--) {
//                tmp = (tmp * 256) + (result[i + 29] & 0xff);
//            }
//            map.put(MapParams.十二点的读数, String.valueOf(tmp));
//            tmp = 0;
//            for (int i = 4; i > 0; i--) {
//                tmp = (tmp * 256) + (result[i + 33] & 0xff);
//            }
//            map.put(MapParams.十八点的读数, String.valueOf(tmp));
//            tmp = 0;
//            for (int i = 4; i > 0; i--) {
//                tmp = (tmp * 256) + (result[i + 37] & 0xff);
//            }
//            map.put(MapParams.二十四点的读数, String.valueOf(tmp));
//        }else {
//        tmp = 0;
//        for (int i = 0; i <2 ; i++) {
//            tmp = (tmp * 256) + (result[i + 26] & 0xff);
//        }
//        map.put(MapParams.零点至三点的脉冲数, String.valueOf(tmp));
//        tmp = 0;
//        for (int i = 0; i <2 ; i++) {
//            tmp = (tmp * 256) + (result[i + 28] & 0xff);
//        }
//        map.put(MapParams.三点至六点的脉冲数, String.valueOf(tmp));
//        tmp = 0;
//        for (int i = 0; i <2 ; i++) {
//            tmp = (tmp * 256) + (result[i + 30] & 0xff);
//        }
//        map.put(MapParams.六点至九点的脉冲数, String.valueOf(tmp));
//        tmp = 0;
//        for (int i = 0; i <2 ; i++) {
//            tmp = (tmp * 256) + (result[i + 32] & 0xff);
//        }
//        map.put(MapParams.九点至十二点的脉冲数, String.valueOf(tmp));
//        tmp = 0;
//        for (int i = 0; i <2 ; i++) {
//            tmp = (tmp * 256) + (result[i + 34] & 0xff);
//        }
//        map.put(MapParams.十二点至十五点的脉冲数, String.valueOf(tmp));
//        tmp = 0;
//        for (int i = 0; i <2 ; i++) {
//            tmp = (tmp * 256) + (result[i + 36] & 0xff);
//        }
//        map.put(MapParams.十五点至十八点的脉冲数, String.valueOf(tmp));
//        tmp = 0;
//        for (int i = 0; i <2 ; i++) {
//            tmp = (tmp * 256) + (result[i + 38] & 0xff);
//        }
//        map.put(MapParams.十八点至二十一点的脉冲数, String.valueOf(tmp));
//        tmp = 0;
//        for (int i = 0; i <2 ; i++) {
//            tmp = (tmp * 256) + (result[i + 40] & 0xff);
//        }
//        map.put(MapParams.二十一点至二十四点的脉冲数, String.valueOf(tmp));

//
//            map.put(MapParams.零点至三点的脉冲数, String.valueOf(((result[26] & 0xff) + ((result[27] & 0xff) ))));
//        map.put(MapParams.三点至六点的脉冲数, String.valueOf(((result[28] & 0xff) + ((result[29] & 0xff) ))));
//        map.put(MapParams.六点至九点的脉冲数, String.valueOf(((result[30] & 0xff) + ((result[31] & 0xff) ))));
//        map.put(MapParams.九点至十二点的脉冲数, String.valueOf(((result[32] & 0xff) + ((result[33] & 0xff) ))));
//        map.put(MapParams.十二点至十五点的脉冲数, String.valueOf(((result[34] & 0xff) + ((result[35] & 0xff) ))));
//        map.put(MapParams.十五点至十八点的脉冲数, String.valueOf(((result[36] & 0xff) + ((result[37] & 0xff) ))));
//        map.put(MapParams.十八点至二十一点的脉冲数, String.valueOf(((result[38] & 0xff) + ((result[39] & 0xff) ))));
//        map.put(MapParams.二十一点至二十四点的脉冲数, String.valueOf(((result[40] & 0xff) + ((result[41] & 0xff) ))));


        map.put(MapParams.零点至三点的脉冲数, getnum(result[26],result[27]));
        map.put(MapParams.三点至六点的脉冲数, getnum(result[28],result[29]));
        map.put(MapParams.六点至九点的脉冲数, getnum(result[30],result[31]));
        map.put(MapParams.九点至十二点的脉冲数, getnum(result[32],result[33]));
        map.put(MapParams.十二点至十五点的脉冲数, getnum(result[34],result[35]));
        map.put(MapParams.十五点至十八点的脉冲数, getnum(result[36],result[37]));
        map.put(MapParams.十八点至二十一点的脉冲数, getnum(result[38],result[39]));
        map.put(MapParams.二十一点至二十四点的脉冲数, getnum(result[40],result[41]));

//            map.put(MapParams.零点至三点的脉冲数, String.valueOf(((result[26] & 0xff) + ((result[27] & 0xff) << 8))));
//        map.put(MapParams.三点至六点的脉冲数, String.valueOf(((result[28] & 0xff) + ((result[29] & 0xff) << 8))));
//        map.put(MapParams.六点至九点的脉冲数, String.valueOf(((result[30] & 0xff) + ((result[31] & 0xff) << 8))));
//        map.put(MapParams.九点至十二点的脉冲数, String.valueOf(((result[32] & 0xff) + ((result[33] & 0xff) << 8))));
//        map.put(MapParams.十二点至十五点的脉冲数, String.valueOf(((result[34] & 0xff) + ((result[35] & 0xff) << 8))));
//        map.put(MapParams.十五点至十八点的脉冲数, String.valueOf(((result[36] & 0xff) + ((result[37] & 0xff) << 8))));
//        map.put(MapParams.十八点至二十一点的脉冲数, String.valueOf(((result[38] & 0xff) + ((result[39] & 0xff) << 8))));
//        map.put(MapParams.二十一点至二十四点的脉冲数, String.valueOf(((result[40] & 0xff) + ((result[41] & 0xff) << 8))));
//        }
        return map;
    }

    //读取表信息 单表s芯片植入（命令0x33）
    public static final HashMap<String, String> getLorawanJiZhong(byte[] result) throws Exception {
        HashMap<String, String> map = new HashMap<>();
        String binary = ByteConvertUtils.conver2BinaryStr(result[4]);
        map.put(MapParams.第一块表磁场外干扰, String.valueOf(binary.charAt(0)));
        map.put(MapParams.第一块表拆卸状态, String.valueOf(binary.charAt(1)));
        map.put(MapParams.第一块表读表状态, String.valueOf(binary.charAt(2)));
        map.put(MapParams.第一块表电源状态, String.valueOf(binary.charAt(3)));
        map.put(MapParams.第一块表倒流状态, String.valueOf(binary.charAt(4)));
        map.put(MapParams.寻址方式, String.valueOf(binary.charAt(5)));
        map.put(MapParams.是否有第二块表, String.valueOf(binary.charAt(6)));

        map.put(MapParams.连接数, String.valueOf(result[5] & 0xff));
        long tmp;
        tmp = 0;
        for (int i = 4; i > 0; i--) {
            tmp = (tmp * 256) + (result[i + 6] & 0xff);
        }
        map.put(MapParams.第一块表ID, String.valueOf(tmp));
        tmp = 0;
        for (int i = 4; i > 0; i--) {
            tmp = (tmp * 256) + (result[i + 10] & 0xff);
        }
        map.put(MapParams.第一块表计数, String.valueOf(tmp));
        String binary1 = ByteConvertUtils.conver2BinaryStr(result[14]);
        map.put(MapParams.第二块表磁场外干扰, String.valueOf(binary1.charAt(0)));
        map.put(MapParams.第二块表拆卸状态, String.valueOf(binary1.charAt(1)));
        map.put(MapParams.第二块表读表状态, String.valueOf(binary1.charAt(2)));
        map.put(MapParams.第二块表电源状态, String.valueOf(binary1.charAt(3)));
        map.put(MapParams.第二块表倒流状态, String.valueOf(binary1.charAt(4)));
        tmp = 0;
        for (int i = 4; i > 0; i--) {
            tmp = (tmp * 256) + (result[i + 15] & 0xff);
        }
        map.put(MapParams.第二块表ID, String.valueOf(tmp));
        tmp = 0;
        for (int i = 4; i > 0; i--) {
            tmp = (tmp * 256) + (result[i + 19] & 0xff);
        }
        map.put(MapParams.第二块表计数, String.valueOf(tmp));
        map.put(MapParams.第一块表表号, String.valueOf(result[23] & 0xff));
        map.put(MapParams.第二块表表号, String.valueOf(result[24] & 0xff));
        map.put(MapParams.第一块表倍率, String.valueOf(result[25] & 0xff).substring(0, 1));
        map.put(MapParams.第二块表倍率, String.valueOf(result[25] & 0xff).substring(1, 2));
        tmp = 0;
        for (int i = 3; i > 0; i--) {
            tmp = (tmp * 256) + (result[i + 26] & 0xff);
        }
        map.put(MapParams.第一块表累积倒流数, String.valueOf(tmp));
        tmp = 0;
        for (int i = 3; i > 0; i--) {
            tmp = (tmp * 256) + (result[i + 29] & 0xff);
        }
        map.put(MapParams.第二块表累积倒流数, String.valueOf(tmp));
        map.put(MapParams.第一块表传感信号, String.valueOf(result[32] & 0xff));
        map.put(MapParams.第二块表传感信号, String.valueOf(result[33] & 0xff));
        return map;
    }


    //读取表信息 单表s芯片植入（命令0x04）
    public static final HashMap<String, String> getQiangZhiSend(byte[] result) throws Exception {
        HashMap<String, String> map = new HashMap<>();


        map.put(MapParams.强制发送成功, String.valueOf(result[4] & 0xff));
        map.put(MapParams.最大发送次数, String.valueOf(result[5] & 0xff));
        map.put(MapParams.发送次数, String.valueOf(result[6] & 0xff));
        return map;
    }

    /**
     * 通过code获取强制发送是否成功，解析时使用
     *
     * @param code
     * @return
     */
    public static String getQiangzhifasongStringByCode(String code) {
        String result = "";
        if ("0".equals(code)) {
            result = "成功";
        } else if ("1".equals(code)) {
            result = "失败";
        }
        return result;
    }

    /**
     * 通过code获取表磁场外干扰，解析时使用
     *
     * @param code
     * @return
     */
    public static String getBiaoCiChangGanRaoStringByCode(String code) {
        String result = "";
        if ("0".equals(code)) {
            result = "正常";
        } else if ("1".equals(code)) {
            result = "强磁";
        }
        return result;
    }


    /**
     * 通过code获取表磁场外干扰，解析时使用
     *
     * @param code
     * @return
     */
    public static String getDianYuanTypeStringByCode(String code) {
        String result = "";
        if ("0".equals(code)) {
            result = "物联电源";
        } else if ("1".equals(code)) {
            result = "外接电源";
        }
        return result;
    }



    /**
     * 通过code获取表拆卸状态，解析时使用
     *
     * @param code
     * @return
     */
    public static String getBiaoChaiXieStateStringByCode(String code) {
        String result = "";
        if ("0".equals(code)) {
            result = "正常";
        } else if ("1".equals(code)) {
            result = "拆卸";
        }
        return result;
    }

    /**
     * 通过code获取表读表状态，解析时使用
     *
     * @param code
     * @return
     */
    public static String getBiaoDuBiaoStateStringByCode(String code) {
        String result = "";
        if ("0".equals(code)) {
            result = "成功";
        } else if ("1".equals(code)) {
            result = "失败";
        }
        return result;
    }

    /**
     * 通过code获取表电源状态，解析时使用
     *
     * @param code
     * @return
     */
    public static String getBiaoDianYuanStateStringByCode(String code) {
        String result = "";
        if ("0".equals(code)) {
            result = "正常";
        } else if ("1".equals(code)) {
            result = "欠压";
        }
        return result;
    }

    /**
     * 通过code获取表倒流状态，解析时使用
     *
     * @param code
     * @return
     */
    public static String getBiaoDaoLiuStateStringByCode(String code) {
        String result = "";
        if ("0".equals(code)) {
            result = "正常";
        } else if ("1".equals(code)) {
            result = "倒流";
        }
        return result;
    }

    /**
     * 通过code获取寻址方式，解析时使用
     *
     * @param code
     * @return
     */
    public static String getXuanZhiFangShiStringByCode(String code) {
        String result = "";
        if ("0".equals(code)) {
            result = "表号寻址";
        } else if ("1".equals(code)) {
            result = "表ID寻址方式";
        }
        return result;
    }

    /**
     * 接入类型
     * @param code
     * @return
     */

    public static String getJieRuTypeByCode(String code){
        String result = "";
        if ("0".equals(code)) {
            result = "一对一";
        } else if ("1".equals(code)) {
            result = "一对多";
        }
        return result;
    }


    public static String getShuJuTypeByCode(String code){
        String result = "";
        if ("0".equals(code)) {
            result = "测试";
        } else if ("1".equals(code)) {
            result = "正式";
        }
        return result;
    }


    /**
     * 通过String获取寻址方式，解析时使用
     *
     * @param str
     * @return
     */
    public static String getBiaoBeilvCodeByString(String str) {
        String result = "";
        if ("0.001".equals(str)) {
            result = "0";
        } else if ("0.01".equals(str)) {
            result = "1";
        } else if ("0.1".equals(str)) {
            result = "2";
        } else if ("1".equals(str)) {
            result = "3";
        } else if ("10".equals(str)) {
            result = "4";
        } else if ("100".equals(str)) {
            result = "5";
        }
        return result;
    }

    /**
     * 通过code获取寻址方式，解析时使用
     *
     * @param code
     * @return
     */
    public static String getBiaoBeilvStringByCode(String code) {
        String result = "";
        if ("0".equals(code)) {
            result = "0.001";
        } else if ("1".equals(code)) {
            result = "0.01";
        } else if ("2".equals(code)) {
            result = "0.1";
        } else if ("3".equals(code)) {
            result = "1";
        } else if ("4".equals(code)) {
            result = "10";
        } else if ("5".equals(code)) {
            result = "100";
        }
        return result;
    }



    /**
     * 传感信号说明
     *
     * @param code
     * @return
     */
    public static String getChuanGanXinHaoByCode(String code) {
        String result ="";
        if (code.equals("0")) {
            result = "0-5V";
        } else if (code.equals("1")) {
            result = "3EV";
        } else if (code.equals("2")) {
            result = "正反脉冲";
        } else if (code.equals("3")) {
            result = "2EV";
        } else if (code.equals("4")) {
            result = "TTL1";
        } else if (code.equals("5")) {
            result = "TTL2";
        } else if (code.equals("6")) {
            result = "RS485";
        } else if (code.equals("7")) {
            result = "电子开关";
        } else if (code.equals("8")) {
            result = "4-20MA";
        } else if (code.equals("9")) {
            result = "单EV";
        } else if (code.equals("0x0a")) {
            result = "TTL3";
        } else if (code.equals("0x0b")) {
            result = "RS485一对多";
        } else if (code.equals("0x0c")) {
            result = "MOD-BUS";
        }
        return result;
    }


    /**
     * 传感信号说明
     *
     * @param value
     * @return
     */
    public static byte getChuanGanXinHaoByString(String value) {
        byte result = 0;
        if (value.equals("0-5V")) {
            result = 0;
        } else if (value.equals("3EV")) {
            result = 1;
        } else if (value.equals("正反脉冲")) {
            result = 2;
        } else if (value.equals("2EV")) {
            result = 3;
        } else if (value.equals("TTL1")) {
            result = 4;
        } else if (value.equals("TTL2")) {
            result = 5;
        } else if (value.equals("RS485")) {
            result = 6;
        } else if (value.equals("电子开关")) {
            result = 7;
        } else if (value.equals("4-20MA")) {
            result = 8;
        } else if (value.equals("单EV")) {
            result = 9;
        } else if (value.equals("TTL3")) {
            result = 0x0a;
        } else if (value.equals("RS485一对多")) {
            result = 0x0b;
        } else if (value.equals("MOD-BUS")) {
            result = 0x0c;
        }
        return result;
    }


    /**
     * 通过code获取复合电容，解析时使用
     *
     * @param code
     * @return
     */
    public static String getFuHedianrongStringByCode(String code) {
        String result = "";
        if ("0".equals(code)) {
            result = "无";
        } else if ("1".equals(code)) {
            result = "有";
        }
        return result;
    }

    /**
     * 通过code获取欠压状态，解析时使用
     *
     * @param code
     * @return
     */
    public static String getQianYaStateStringByCode(String code) {
        String result = "";
        if ("0".equals(code)) {
            result = "正常";
        } else if ("1".equals(code)) {
            result = "欠压";
        }
        return result;
    }

    /**
     * 通过code获取流向状态，解析时使用
     *
     * @param code
     * @return
     */
    public static String getLiuXiangstateStringByCode(String code) {
        String result = "";
        if ("0".equals(code)) {
            result = "正常";
        } else if ("1".equals(code)) {
            result = "倒流";
        }
        return result;
    }

    /**
     * 通过code获取拆卸状态，解析时使用
     *
     * @param code
     * @return
     */
    public static String getCaiXieStateStringByCode(String code) {
        String result = "";
        if ("0".equals(code)) {
            result = "正常";
        } else if ("1".equals(code)) {
            result = "拆卸";
        }else {
            result = "正常";
        }
        return result;
    }

    /**
     * 通过code获取阀门有无，解析时使用
     *
     * @param code
     * @return
     */
    public static String getValveStringByCode(String code) {
        String result = "";
        if ("0".equals(code)) {
            result = "有";
        } else if ("1".equals(code)) {
            result = "无";
        }else {
            result = "有";
        }
        return result;
    }

    /**
     * 通过code获取阀门状态，解析时使用
     *
     * @param code
     * @return
     */
    public static String getValvestateStringByCode(String code) {
        String result = "";
        if ("0".equals(code)) {
            result = "开";
        } else if ("1".equals(code)) {
            result = "关";
        }
        return result;
    }

    /**
     * 通过code获取磁干扰，解析时使用
     *
     * @param code
     * @return
     */
    public static String getCiStateStringByCode(String code) {
        String result = "";
        if ("0".equals(code)) {
            result = "正常";
        } else if ("1".equals(code)) {
            result = "强磁";
        }
        return result;
    }

    /**
     * 通过code获取模拟信号，解析时使用
     *
     * @param code
     * @return
     */
    public static String getXinHaoStateStringByCode(String code) {
        String result = "";
        if ("1".equals(code)) {
            result = "数字";
        } else if ("0".equals(code)) {
            result = "模拟";
        }
        return result;
    }


    /**
     * 通过code获取机芯类型，解析时使用
     *
     * @param code
     * @return
     */
    public static String getJiXinStringByCode(String code) {
        String result = "";
        if ("0".equals(code)) {
            result = "干式";
        } else if ("1".equals(code)) {
            result = "湿式";
        } else if ("2".equals(code)) {
            result = "机电";
        } else if ("3".equals(code)) {
            result = "超声波";
        }
        return result;
    }

    /**
     * 通过code获取机芯类型，解析时使用
     *
     * @param str
     * @return
     */
    public static int getJiXinCodeByString(String str) {
        int result = 0;
        if ("干式".equals(str)) {
            result = 0;
        } else if ("湿式".equals(str)) {
            result = 1;
        } else if ("机电".equals(str)) {
            result = 2;
        } else if ("超声波".equals(str)) {
            result =3;
        }
        return result;
    }


    /**
     * 通过code获取最后计数时间，解析时使用
     *
     * @param code
     * @return
     */
    public static String getLastTimeStringByCode(String code) {

        String year = String.valueOf(Integer.parseInt(code.substring(0, 2),16));
        String month = String.valueOf(Integer.parseInt(code.substring(2, 3),16));
        String day = String.valueOf(Integer.parseInt(code.substring(3, 5),16));

        String hour =  String.valueOf(Integer.parseInt(code.substring(5, 7),16));

        String result = "";
        result = year + "年" + month + "月" + day + "日" + hour + "时";
        return result;
    }

    /**
     * 通过code获取表磁场外干扰，解析时使用
     *
     * @param code
     * @return
     */
    public static String getCiGanraoStringByCode(String code) {
        String result = "";
        if ("0".equals(code)) {
            result = "正常";
        } else if ("1".equals(code)) {
            result = "强磁";
        }
        return result;
    }


    /**
     * 通过code获取表读表状态，解析时使用
     *
     * @param code
     * @return
     */
    public static String getDuquStateStringByCode(String code) {
        String result = "";
        if ("0".equals(code)) {
            result = "成功";
        } else if ("1".equals(code)) {
            result = "失败";
        }
        return result;
    }

    /**
     * 通过code获取表电源状态，解析时使用
     *
     * @param code
     * @return
     */
    public static String getDianYuanStateStringByCode(String code) {
        String result = "";
        if ("0".equals(code)) {
            result = "正常";
        } else if ("1".equals(code)) {
            result = "欠压";
        }
        return result;
    }

    /**
     * 通过code获取表倒流状态，解析时使用
     *
     * @param code
     * @return
     */
    public static String getLiuXiangStateStringByCode(String code) {
        String result = "";
        if ("0".equals(code)) {
            result = "正流";
        } else if ("1".equals(code)) {
            result = "倒流";
        }
        return result;
    }

    /**
     * 通过code获取寻址方式，解析时使用
     *
     * @param code
     * @return
     */
    public static String getFindWayStringByCode(String code) {
        String result = "";
        if ("0".equals(code)) {
            result = "表号寻址";
        } else if ("1".equals(code)) {
            result = "表ID寻址";
        }
        return result;
    }


    /**
     * 通过code获取是否有第二块表，解析时使用
     *
     * @param code
     * @return
     */
    public static String getIsCunZaiTwoStringByCode(String code) {
        String result = "";
        if ("0".equals(code)) {
            result = "无";
        } else if ("1".equals(code)) {
            result = "有";
        }
        return result;
    }




    private static String getnum(byte b,byte c){
        String result = "";
        int tmp;

        XLog.i("b==="+DataParser.byteToString(b));
        XLog.i("c==="+DataParser.byteToString(c));
//          tmp=b & 0xff;
//          tmp=tmp*256+c& 0xff;
//          String num=String.valueOf(tmp);

        tmp=  Integer.valueOf(DataParser.byteToString(b)+DataParser.byteToString(c),16);

        if(tmp>32768){
            tmp=32768-tmp;
        }

//        String str1="";
//        String str2="";
//       // String str1=  Long.toString(Integer.valueOf(tmp), 2).length()==16?"-":"";

//
//
//
//        if( Long.toString(Integer.valueOf(tmp), 2).length()==16){
//             str1="-";
//            str2= Long.toString(Integer.valueOf(tmp), 2).substring(1);
//        }else {
//             str1="";
//             str2= Long.toString(Integer.valueOf(tmp), 2);
//        }
//
//        int num=0;
//        if(!str2.isEmpty()){
//             num= ByteConvertUtils.binaryToDecimal(str2 );
//        }
//        result=str1+num;
        return String.valueOf(tmp);
    }

    public static void main(String[] args) {

        String  str="0.1m³/ev(10)";

      ;
//           byte [] bytes=ByteConvertUtils.convertHexStringToBytes("8888");
//        ;
  //  System.out.print(   );
//
//
//           byte b= bytes[0];
////        System.out.print( "b==="+ b);
////           byte c= bytes[1];
////        System.out.print( "c==="+ c);
////
//      int tmp;

//        for (int i = 0; i < 6; i++) {
//            result[3 + i] = (byte) ((mai & ((long) 0xff << (8 * i))) >> 8 * i);
//        }
//
//        long tmp;
//        tmp = 0;
//        for (int i = 6; i > 0; i--) {
//            tmp = (tmp * 256) + (result[i + 3] & 0xff);
//        }
//        map.put(MapParams.设备ID, String.valueOf(tmp));
//


    //    System.out.print( tmp);
//        tmp=b & 0xff;
//        tmp=tmp*256+c& 0xff;
//        String num=String.valueOf(tmp);
//
//     System.out.print(  num);

//
//        byte b= bytes[0];
//        byte c= bytes[1];
//        int tmp;
//        tmp=b & 0xff;
//        tmp=tmp*256+c& 0xff;
//
//    //  int tt=  ByteConvertUtils.binaryToDecimal( Long.toString(Integer.parseInt("FFFF"), 2).substring(1));
//
//
//       String tmp="32768";
//
//        String str1="";
//        String str2="";
//        if( Long.toString(Integer.valueOf(tmp), 2).length()==16){
//            str1="-";
//            str2= Long.toString(Integer.valueOf(tmp), 2).substring(1);
//        }else {
//            str1="";
//            str2= Long.toString(Integer.valueOf(tmp), 2);
//        }
//
//        int num=0;
//        if(!str2.isEmpty()){
//            num= ByteConvertUtils.binaryToDecimal(str2 );
//        }


//
//        int tmp=32770;
//        if(tmp>=32768){
//            tmp=32768-tmp;
//        }
//
//        System.out.print(  String.valueOf(tmp));

      //  result=str1+num;

//        String str1=  Long.toString(Integer.valueOf(tmp), 2).length()==16?"-":"";
//     //   XLog.i("num===="+String.valueOf(((b & 0xff) + (c & 0xff))));
//        String str2= Long.toString(Integer.valueOf(tmp), 2).substring(1);
//        int num=0;
//        if(!str2.isEmpty()){
//            num= ByteConvertUtils.binaryToDecimal(str2 );
//        }


      //  System.out.print(  Long.toString(65535 , 2).substring(1));

//      byte[] result={11,80};
//        int tmp;
//        tmp = 0;
//        for (int i = 0; i <2 ; i++) {
//            tmp = (tmp * 256) + (result[i ] & 0xff);
//        }
//      System.out.print(result[0 ] & 0xff);
//      String str1=DataParser.byteToString(result[0] ).substring(0, 1).equals("0")?"":"-";
//              String str2= DataParser.byteToString(result[0] ).substring(1, 2) ;
//        String str3= DataParser.byteToString(result[1] );
             //   System.out.print(str1+str2+str3);

//
//      //  String binary = ByteConvertUtils.conver2BinaryStr();
//        String s = Long.toString(91 & 0xff, 2);
//        byte b=0x7f;
//        Integer.toHexString(b & 0xff);
//        DecimalFormat df = new DecimalFormat("0.00%");//格式化小数
//        String num = df.format(((float)3/4));
//
//       // String num =String.valueOf((float)3/4)*100));
//     //   System.out.print(  Integer.toHexString(b & 0xff));
//        System.out.print( s);
//
//        String s = "1471609" ;
//
//        String year = String.valueOf(Integer.parseInt(s.substring(0, 2),16));
//        String month = String.valueOf(Integer.parseInt(s.substring(2, 3),16));
//        String day = String.valueOf(Integer.parseInt(s.substring(3, 5),16));
//
//        String hour =  String.valueOf(Integer.parseInt(s.substring(5, 7),16));

       // String hour = String.valueOf() & 0xff);
    //    System.out.print(  year + "年" + month + "月" + day + "日" + hour + "时");
    }


}