package com.wu.loushanyun.basemvp.m.sat.util;

import com.wu.loushanyun.base.util.MapParams;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import met.hx.com.librarybase.some_utils.ByteConvertUtils;
import met.hx.com.librarybase.some_utils.TimeUtils;

/**
 * 4号模组模拟通讯蓝牙协议指令集
 */
public class FourSimuBlueToothUtil {
    public static ArrayList<String> getAllWangLuoJiaoHu() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("最大发送1次");
        arrayList.add("最大发送2次");
        arrayList.add("最大发送3次");
        return arrayList;
    }

    public static ArrayList<String> getAllDianYuanLeiXing() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("物联电池");
        arrayList.add("外接电源");
        return arrayList;
    }

    //读取_识别模块信息（命令0x11）
    public static final HashMap<String, String> getInformationAll(byte[] result) throws Exception {
        HashMap<String, String> map = new HashMap<>();
        map.put(MapParams.采集场景, String.valueOf(result[4] & 0xff));
        StringBuilder sb = new StringBuilder();
        String temp;
        int i;
        for (i = 5; i < 13; i++) {
            temp = Integer.toHexString(result[i] & 0xff);
            if (temp.length() == 1)
                sb.append(0);
            sb.append(temp);
        }
        map.put(MapParams.物联SN, sb.toString().toUpperCase());
        map.put(MapParams.工作模式, String.valueOf(result[i++] & 0xff));
        map.put(MapParams.发送频率, String.valueOf(result[i++] & 0xff));
        map.put(MapParams.无线频率, String.valueOf(result[i++] & 0xff));
        map.put(MapParams.发送功率, String.valueOf(result[i++] & 0xff));
        map.put(MapParams.出厂时间_年, String.valueOf(result[i++] & 0xff));
        map.put(MapParams.出厂时间_月, String.valueOf(result[i++] & 0xff));
        map.put(MapParams.出厂时间_日, String.valueOf(result[i++] & 0xff));
        map.put(MapParams.网络交互, String.valueOf(result[i++] & 0xff));
        map.put(MapParams.硬件版本, String.valueOf(result[i++] & 0xff));
        map.put(MapParams.软件版本, String.valueOf(result[i++] & 0xff));
        map.put(MapParams.厂家标识, String.valueOf(((result[i++] & 0xff) + ((result[i++] & 0xff) << 8)) & 0xffff));
        return map;
    }

    public static byte[] getFouWangluoJiaoHuBytes(String jiaohu) {
        byte[] result = new byte[6];
        result[0] = 0x68;
        result[1] = 0x02;
        result[2] = 0x04;
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

    public static ArrayList<String> getDianYuanLeiXing() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("物联电池");
        arrayList.add("外接电源");
        return arrayList;
    }

    public static ArrayList<String> getChuanGanMoShi() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("累计脉冲");
        arrayList.add("线性传感");
        arrayList.add("状态切换");
        return arrayList;
    }

    public static byte getChuanGanMoShiByte(String moShi) {
        byte result = 0;
        if ("累计脉冲".equals(moShi)) {
            result = 0x01;
        } else if ("状态切换".equals(moShi)) {
            result = 0x02;
        } else if ("数字通讯".equals(moShi)) {
            result = 0x03;
        } else if ("线性传感".equals(moShi)) {
            result = 0x04;
        }
        return result;
    }

    /**
     * 获取传感信号
     *
     * @param type type=0  累计脉冲
     *             type=1  线性传感
     *             type=2  状态传感
     *             type=3  全部信号
     * @return
     */
    public static ArrayList<String> getChuanGanXinHaoShuZi(int type) {
        ArrayList<String> arrayList = new ArrayList<>();
        switch (type) {
            case 0:
                arrayList.add("3EV");
                arrayList.add("2EV");
                arrayList.add("正反脉冲");
                break;
            case 1:
                arrayList.add("0-5V");
                break;
            case 2:
                arrayList.add("状态传感");
                break;
            case 3:
                arrayList.add("0-5V");
                arrayList.add("3EV");
                arrayList.add("正反脉冲");
                arrayList.add("2EV");
                arrayList.add("状态传感");
                break;
        }

        return arrayList;
    }


    public static String getFourFSPLReadStringByString(String code) {
        String result = "";
        if ("0".equals(code)) {
            result = "15分钟上传一次";
        } else if ("1".equals(code)) {
            result = "6小时上传一次";
        } else if ("2".equals(code)) {
            result = "60秒上传一次";
        } else if ("3".equals(code)) {
            result = "24小时上传一次";
        } else if ("4".equals(code)) {
            result = "5分钟上送一次";
        } else if ("5".equals(code)) {
            result = "30分钟上传一次";
        } else if ("6".equals(code)) {
            result = "60分钟上传一次";
        } else if ("7".equals(code)) {
            result = "3小时上传一次";
        } else if ("8".equals(code)) {
            result = "72小时上传一次";
        } else if ("9".equals(code)) {
            result = "12小时一次";
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
        } else if (value.equals("状态传感")) {
            result = 7;
        } else if (value.equals("4-20MA")) {
            result = 8;
        } else if (value.equals("单EV")) {
            result = 9;
        } else if (value.equals("无磁正反脉冲")) {
            result = 0x0a;
        } else if (value.equals("RS485一对多")) {
            result = 0x0b;
        } else if (value.equals("MOD-BUS")) {
            result = 0x0c;
        }
        return result;
    }

    /**
     * 解析出2进制字符串判断状态，3号表单元
     *
     * @param tmpString
     * @return
     */
    public static HashMap<String, String> getZTReadStringByCode(String tmpString) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("电池状态", "正常");
        hashMap.put("强磁状态", "正常");
        hashMap.put("拆卸状态", "正常");
        hashMap.put("流向状态", "正常");
        hashMap.put("阀门故障状态", "正常");
        try {
            int tem = (Integer.valueOf(tmpString)) & 0xff;
            byte b = (byte) tem;
            String binary = ByteConvertUtils.conver2BinaryStr(b);
            if (binary.charAt(4) == '1') {
                hashMap.put("电池状态", "欠压");
            }
            if (binary.charAt(2) == '1') {
                hashMap.put("流向状态", "倒流");
            }
            if (binary.charAt(7) == '1') {
                hashMap.put("强磁状态", "强磁");
            }
            if (binary.charAt(6) == '1') {
                hashMap.put("拆卸状态", "拆卸");
            }

        } catch (Exception e) {
        }
        return hashMap;
    }


    public static byte[] getFourPinlvBytes(String pinlv) {
        byte[] result = new byte[6];
        result[0] = 0x68;
        result[1] = 0x03;
        result[2] = 0x07;
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


    /**
     * 初始化累计脉冲
     *
     * @param maichong
     * @param beilv
     * @return
     */
    public static byte[] getLeiJiSetting(String maichong, String beilv) {
        long mai = Long.valueOf(maichong);
        byte[] result = new byte[14];
        result[0] = (byte) 0x68;
        result[1] = (byte) 0x10;
        result[2] = 0x21;

        for (int i = 0; i <= 8; i++) {
            result[i + 3] = (byte) (mai & 0xff);
            mai = mai >> 8;
        }
        if ("0.001".equals(beilv)) {
            result[11] = 0x00;
        } else if ("0.01".equals(beilv)) {
            result[11] = 0x01;
        } else if ("0.1".equals(beilv)) {
            result[11] = 0x02;
        } else if ("1".equals(beilv)) {
            result[11] = 0x03;
        } else if ("10".equals(beilv)) {
            result[11] = 0x04;
        } else if ("100".equals(beilv)) {
            result[11] = 0x05;
        } else {
            result[11] = 0x00;
        }

        //校验
        byte cs = 0;
        for (int i = 1; i < 10; i++) {
            cs += result[i];
        }
        result[12] = cs;
        result[13] = 0x16;
        return result;
    }

    /**
     * 初始化线性传感
     *
     * @param
     * @return
     */
    public static byte[] getxianxingSetting(String biaozhun, String Kvlaue, String Bvlaue) {

        float k = Float.valueOf(Kvlaue);
        float b = Float.valueOf(Bvlaue);
        byte[] result = new byte[14];
        result[0] = (byte) 0x68;
        result[1] = (byte) 0x05;
        result[2] = 0x22;


        result[3] = ByteConvertUtils.convertHexStringToBytes(ByteConvertUtils.intToHex(Integer.valueOf(biaozhun)))[0];

        byte[] bytesk = ByteConvertUtils.convertHexStringToBytes(ByteConvertUtils.floatToHexStr(k));
        for (int i = 0; i < bytesk.length; i++) {
            result[i + 4] = bytesk[bytesk.length - 1 - i];
        }

        byte[] bytesb = ByteConvertUtils.convertHexStringToBytes(ByteConvertUtils.floatToHexStr(b));
        for (int i = 0; i < bytesb.length; i++) {
            result[i + 8] = bytesb[bytesk.length - 1 - i];
        }


        //校验
        byte cs = 0;
        for (int i = 1; i < 4; i++) {
            cs += result[i];
        }
        result[12] = cs;
        result[13] = 0x16;
        return result;
    }


    /**
     * 初始化状态传感
     *
     * @param biaozhun
     * @return
     */
    public static byte[] getzhuantaiSetting(String biaozhun) {
        byte[] result = new byte[6];
        result[0] = (byte) 0x68;
        result[1] = (byte) 0x02;
        result[2] = 0x23;
        if ("高电平为0  低电平为1".equals(biaozhun)) {
            result[3] = 0x00;
        } else if ("高电平为1  低电平为0".equals(biaozhun)) {
            result[3] = 0x01;
        } else if ("高电平为1  低电平为1".equals(biaozhun)) {
            result[3] = 0x02;
        } else if ("高电平为0  低电平为0".equals(biaozhun)) {
            result[3] = 0x03;
        }
        byte cs = 0;
        for (int i = 1; i < 4; i++) {
            cs += result[i];
        }
        result[4] = cs;
        result[5] = 0x16;

        return result;
    }


    /**
     * 获取累计传感数据
     *
     * @return
     */
    public static byte[] getleijiZhengReading() {
        byte[] result = new byte[5];
        result[0] = (byte) 0x68;
        result[1] = (byte) 0x01;
        result[2] = 0x30;
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
     * 获取线性传感数据
     *
     * @return
     */
    public static byte[] getXianxingYaliReading() {
        byte[] result = new byte[5];
        result[0] = (byte) 0x68;
        result[1] = (byte) 0x01;
        result[2] = 0x31;
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
     * 获取状态传感数据
     *
     * @return
     */
    public static byte[] getZhuangtaiReading() {
        byte[] result = new byte[5];
        result[0] = (byte) 0x68;
        result[1] = (byte) 0x01;
        result[2] = 0x32;
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
     * 获取共有特性传感数据
     *
     * @return
     */
    public static byte[] getChuanganxinhaoReading() {
        byte[] result = new byte[5];
        result[0] = (byte) 0x68;
        result[1] = (byte) 0x01;
        result[2] = 0x33;
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
     * 获取状态传感测量标准
     *
     * @return
     */
    public static ArrayList<String> getAllManufacturer() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("武汉楚天汉仪有限公司");

        return arrayList;
    }

    /**
     * 获取状态传感测量标准
     *
     * @return
     */
    public static ArrayList<String> getAllZhuangTaiBiaoZhun() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("高电平为0  低电平为1");
        arrayList.add("高电平为1  低电平为0");
        arrayList.add("高电平为1  低电平为1");
        arrayList.add("高电平为0  低电平为0");
        return arrayList;
    }

    /**
     * 获取线性传感测量标准
     *
     * @return
     */
    public static ArrayList<String> getAllXianXingBiaoZhun() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("1分钟");
        arrayList.add("3分钟");
        arrayList.add("15分钟");
        arrayList.add("30分钟");
        return arrayList;
    }


    /**
     * 获取累计传感倍率
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

    /**
     * 0x00	压力计（线性传感）
     * 0x01	工业水表 （2EV 3EV 正反脉冲）（累积脉冲）
     * 0x06	流量计 （TTL转MOD-BUS）
     * 0x07	井盖 （状态传感）
     * 0x08	水淹传感器（状态传感）
     * 0x09	电源开关传感器（状态传感）
     * <p>
     * type=0  累计脉冲
     * type=1  线性传感
     * type=2  状态传感
     * type=3   全部
     *
     * @return
     */
    public static ArrayList<String> getAllChanPinXingShi(int type) {
        ArrayList<String> arrayList = new ArrayList<>();
        switch (type) {
            case 0:
                arrayList.add("工业水表（累积脉冲）");
                arrayList.add("流量计（TTL转MOD-BUS）");
                break;
            case 1:
                arrayList.add("压力计（线性传感）");
                break;
            case 2:
                arrayList.add("井盖 （状态传感）");
                arrayList.add("水淹传感器（状态传感）");
                arrayList.add("电源开关传感器（状态传感）");
                break;
            case 3:
                arrayList.add("压力计（线性传感）");
                arrayList.add("工业水表（累积脉冲）");
                arrayList.add("流量计（TTL转MOD-BUS）");
                arrayList.add("井盖 （状态传感）");
                arrayList.add("水淹传感器（状态传感）");
                arrayList.add("电源开关传感器（状态传感）");
                break;

        }

        return arrayList;
    }

    //采集场景
    public static final String getCJCJReadStringByCode(String num) {
        if ("0".equalsIgnoreCase(num))
            return "基站公用";
        else if ("1".equalsIgnoreCase(num)) return "基站民用";
        return "";
    }

    public static String getChanPinXingShi(String code) {
        String cpxs = "";
        if ("0".equals(code)) {
            cpxs = "压力计（线性传感）";
        } else if ("1".equals(code)) {
            cpxs = "工业水表（累积脉冲）";
        } else if ("6".equals(code)) {
            cpxs = "流量计（TTL转MOD-BUS）";
        } else if ("7".equals(code)) {
            cpxs = "井盖 （状态传感）";
        } else if ("8".equals(code)) {
            cpxs = "水淹传感器（状态传感）";
        } else if ("9".equals(code)) {
            cpxs = "电源开关传感器（状态传感）";
        }
        return cpxs;
    }

    public static int getCPXSIntByValue(String value) {
        int result = 0;
        if (value.equals("压力计（线性传感）")) {
            result = 0x00;
        } else if (value.equals("工业水表（累积脉冲）")) {
            result = 0x01;
        } else if (value.equals("流量计（TTL转MOD-BUS）")) {
            result = 0x06;
        } else if (value.equals("井盖 （状态传感）")) {
            result = 0x07;
        } else if (value.equals("水淹传感器（状态传感）")) {
            result = 0x08;
        } else if (value.equals("电源开关传感器（状态传感）")) {
            result = 0x09;
        }
        return result;
    }


    public static byte[] getFourCKReadBytes() {
        StringBuffer str = new StringBuffer();
        str.append("68002616");
        return FourNumBlueToothUtil.getByteFromHexString(str.toString());
    }

    /**
     * 获取校准时间
     *
     * @return
     */
    public static byte[] getFourjiaozhun() {
        byte[] result = new byte[10];
        result[0] = 0x68;
        result[1] = 0x06;
        result[2] = 0x20;
        long isn = TimeUtils.getCurTimeMills() / 1000;
        for (int i = 0; i < 5; i++) {
            result[i + 3] = (byte) (isn & 0xff);
            isn = isn >> 8;
        }

        //校验
        byte cs = 0;
        for (int i = 1; i < 4; i++) {
            cs += result[i];
        }
        result[8] = (byte) (cs & 0xff);
        result[9] = 0x16;
        return result;
    }


    /**
     * 获取精确到秒的时间戳
     *
     * @param date
     * @return
     */
    public static int getSecondTimestampTwo(Date date) {
        if (null == date) {
            return 0;
        }
        String timestamp = String.valueOf(date.getTime() / 1000);
        return Integer.valueOf(timestamp);
    }

    /**
     * 解析累计传感相关信息
     *
     * @param result
     * @return
     */
    public static HashMap<String, String> parseFourLeijiNotifyBytes(byte[] result) {
        HashMap<String, String> map = new HashMap<>();
        int i = 4;
        long temp1 = 0;
        for (int j = 8; j > 0; j--) {
            temp1 = (temp1 * 256) + (result[j + i - 1] & 0xff);
        }
        i = i + 8;
        map.put(MapParams.累计脉冲数, String.valueOf(temp1));

        long tmp2 = 0;
        for (int j = 4; j > 0; j--) {
            tmp2 = (tmp2 * 256) + (result[j + i - 1] & 0xff);
        }
        i = i + 4;
        map.put(MapParams.反向脉冲数, String.valueOf(tmp2));
        map.put(MapParams.倍率, String.valueOf(result[i++] & 0xff));

        StringBuffer stringBuffer = new StringBuffer();
        for (int j = 3; j >= 0; j--) {
            stringBuffer.append(ByteConvertUtils.conver2BinaryStr(result[j + 17]));
        }
        map.put(MapParams.瞬时流量, String.valueOf(ByteConvertUtils.formatNumber(stringBuffer.toString(), 2)));

        i = i + 4;
        // TODO 传感器状态位没解析
        String str = ByteConvertUtils.conver2BinaryStr(result[i++]);
        map.put(MapParams.断线状态, str.substring(7, 8));
        map.put(MapParams.流向状态, str.substring(6, 7));
        map.put(MapParams.强磁状态, str.substring(5, 6));
        map.put(MapParams.累计脉冲正负标识, str.substring(4, 5));


        long tmp4 = 0;
        for (int j = 5; j > 0; j--) {
            tmp4 = (tmp4 * 256) + (result[j + i - 1] & 0xff);
        }
        i = i + 5;
        map.put(MapParams.最后一个正脉冲时间, String.valueOf(TimeUtils.milliseconds2String(tmp4 * 1000)));

        return map;
    }


    /**
     * 解析线性传感相关信息
     *
     * @param result
     * @return
     */
    public static HashMap<String, String> parseFourXianXingNotifyBytes(byte[] result) {
        HashMap<String, String> map = new HashMap<>();
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 3; i >= 0; i--) {
            stringBuffer.append(ByteConvertUtils.conver2BinaryStr(result[i + 4]));
        }
        map.put(MapParams.最高压力值, String.valueOf(ByteConvertUtils.formatNumber(stringBuffer.toString(), 2)));
        stringBuffer = new StringBuffer();
        for (int i = 3; i >= 0; i--) {
            stringBuffer.append(ByteConvertUtils.conver2BinaryStr(result[i + 8]));
        }
        map.put(MapParams.平均压力值, String.valueOf(ByteConvertUtils.formatNumber(stringBuffer.toString(), 2)));
        stringBuffer = new StringBuffer();
        for (int i = 3; i >= 0; i--) {
            stringBuffer.append(ByteConvertUtils.conver2BinaryStr(result[i + 12]));
        }
        map.put(MapParams.最低压力值, String.valueOf(ByteConvertUtils.formatNumber(stringBuffer.toString(), 2)));

        map.put(MapParams.采样频率, String.valueOf(result[16] & 0xff));
        stringBuffer = new StringBuffer();
        for (int i = 3; i >= 0; i--) {
            stringBuffer.append(ByteConvertUtils.conver2BinaryStr(result[i + 17]));
        }
        map.put(MapParams.输出电压, String.valueOf(ByteConvertUtils.formatNumber(stringBuffer.toString(), 2)));
        String str = ByteConvertUtils.conver2BinaryStr(result[21]);
        map.put(MapParams.输出电压状态, str.substring(7, 8));
        map.put(MapParams.断线状态, str.substring(6, 7));
        long tmp = 0;
        for (int i = 4; i >= 0; i--) {
            tmp = (tmp * 256) + (result[i + 22] & 0xff);
        }
        map.put(MapParams.最后一次采样时间, String.valueOf(TimeUtils.milliseconds2String(tmp * 1000)));
        tmp = 0;
        for (int i = 1; i >= 0; i--) {
            tmp = (tmp * 256) + (result[i + 26] & 0xff);
        }

        stringBuffer = new StringBuffer();
        for (int i = 3; i >= 0; i--) {
            String temp = Integer.toHexString(result[i+ 27] & 0xff);
            if(temp.length()==1){
                temp="0"+temp;
            }
            stringBuffer.append(temp);
        }
        map.put(MapParams.K斜率, ByteConvertUtils.hextoFloat(stringBuffer.toString()));

        stringBuffer = new StringBuffer();
        for (int i = 3; i >= 0; i--) {
            String temp = Integer.toHexString(result[i+ 31] & 0xff);
            if(temp.length()==1){
                temp="0"+temp;
            }
            stringBuffer.append(temp);
        }
        map.put(MapParams.B截距, ByteConvertUtils.hextoFloat(stringBuffer.toString()));


        return map;
    }

    /**
     * 解析状态传感相关信息
     *
     * @param result
     * @return
     */
    public static HashMap<String, String> parseFourZhuangTaiNotifyBytes(byte[] result) {
        HashMap<String, String> map = new HashMap<>();
        map.put(MapParams.传感状态位, String.valueOf(result[4] & 0xff));
        int tmp1 = 0;
        for (int j = 3; j >= 0; j--) {
            tmp1 = (tmp1 * 256) + (result[j + 5] & 0xff);
        }
        map.put(MapParams.当前状态累计时长, String.valueOf(tmp1));

        int tmp2 = 0;
        for (int j = 3; j >= 0; j--) {
            tmp2 = (tmp2 * 256) + (result[j + 9] & 0xff);
        }
        map.put(MapParams.上一状态时长, String.valueOf(tmp2));
        map.put(MapParams.判断正常和异常, String.valueOf(result[13] & 0xff));
        long tmp3 = 0;
        for (int j = 4; j >= 0; j--) {
            tmp3 = (tmp3 * 256) + (result[j + 14] & 0xff);
        }
        map.put(MapParams.最后一次动作切换的起始时间, String.valueOf(TimeUtils.milliseconds2String(tmp3 * 1000)));
        return map;
    }

    /**
     * 解析共同传感器
     *
     * @param result
     * @return
     */

    public static HashMap<String, String> parseFourGongTongNotifyBytes(byte[] result) {
        HashMap<String, String> map = new HashMap<>();
        map.put(MapParams.电源状态, String.valueOf(result[4] & 0xff));
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 3; i >= 0; i--) {
            stringBuffer.append(ByteConvertUtils.conver2BinaryStr(result[i + 5]));
        }
        map.put(MapParams.电池电压, String.valueOf(ByteConvertUtils.formatNumber(stringBuffer.toString(), 2)));
        long tmp = 0;
        for (int i = 4; i >= 0; i--) {
            tmp = (tmp * 256) + (result[i + 9] & 0xff);
        }
        map.put(MapParams.模组当前时间戳, String.valueOf(tmp));
        return map;
    }


    /**
     * 通过code获取倍率，解析时使用
     *
     * @param code
     * @return
     */
    public static String getBLReadStringByCode(String code) {
        String result = "100000";
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
     * 通过code获取断线状态，解析时使用
     *
     * @param code
     * @return
     */
    public static String getDXZTReadStringByCode(String code) {
        String result = "0";
        if ("0".equals(code)) {
            result = "正常";
        } else if ("1".equals(code)) {
            result = "断线";
        }
        return result;
    }

    /**
     * 通过code获取流向状态，解析时使用
     *
     * @param code
     * @return
     */
    public static String getLXZTReadStringByCode(String code) {
        String result = "0";
        if ("0".equals(code)) {
            result = "正常";
        } else if ("1".equals(code)) {
            result = "倒流";
        }
        return result;
    }

    /**
     * 通过code获取强磁状态，解析时使用
     *
     * @param code
     * @return
     */
    public static String getQCZTReadStringByCode(String code) {
        String result = "0";
        if ("0".equals(code)) {
            result = "正常";
        } else if ("1".equals(code)) {
            result = "强磁";
        }
        return result;
    }

    /**
     * 通过code获取累计脉冲正负标识，解析时使用
     *
     * @param code
     * @return
     */
    public static String getLJMCZFBSReadStringByCode(String code) {
        String result = "0";
        if ("0".equals(code)) {
            result = "正";
        } else if ("1".equals(code)) {
            result = "负";
        }
        return result;
    }

    /**
     * 通过code获取采样频率，解析时使用
     *
     * @param code
     * @return
     */
    public static String getCYPLReadStringByCode(String code) {

        String result = Integer.parseInt(code, 10) + "";
//        if ("0".equals(code)) {
//            result = "1分钟采样";
//        } else if ("1".equals(code)) {
//            result = "5分钟采样";
//        } else if ("2".equals(code)) {
//            result = "15分钟采样";
//        }else if ("3".equals(code)) {
//            result = "30分钟采样";
//        }
        return result;
    }

    /**
     * 通过code获取输出电压状态，解析时使用
     *
     * @param code
     * @return
     */
    public static String getSCDYZTReadStringByCode(String code) {
        String result = "0";
        if ("0".equals(code)) {
            result = "正常";
        } else if ("1".equals(code)) {
            result = "低于指定输出电压(异常)";
        }
        return result;
    }

    /**
     * 通过code获取线性传感 断线状态，解析时使用
     *
     * @param code
     * @return
     */
    public static String getXXDXZTReadStringByCode(String code) {
        String result = "0";
        if ("0".equals(code)) {
            result = "正常";
        } else if ("1".equals(code)) {
            result = "断线(异常)";
        }
        return result;
    }

    /**
     * 通过code获取判断正常和异常 ，解析时使用
     *
     * @param code
     * @return
     */
    public static String getPDZCHYCReadStringByCode(String code) {
        String result = "0";
        if ("0".equals(code)) {
            result = "高电平为0  低电平为1";
        } else if ("1".equals(code)) {
            result = "高电平为1  低电平为0";
        } else if ("2".equals(code)) {
            result = "高电平为1  低电平为1";
        } else if ("3".equals(code)) {
            result = "高电平为0  低电平为0";
        }
        return result;
    }

    /**
     * 通过code获取电源状态 ，解析时使用
     *
     * @param code
     * @return
     */
    public static String getDYZTReadStringByCode(String code) {
        String result = "0";
        if ("0".equals(code)) {
            result = "正常";
        } else if ("1".equals(code)) {
            result = "异常";
        }
        return result;
    }

}
