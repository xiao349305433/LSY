package com.wu.loushanyun.basemvp.m.sat.util;

import java.util.ArrayList;
import java.util.HashMap;

import met.hx.com.librarybase.some_utils.ByteConvertUtils;
import met.hx.com.librarybase.some_utils.ToastUtils;
import met.hx.com.librarybase.some_utils.XHStringUtil;

/**
 * 4号模组数字通讯蓝牙协议指令集
 */
public class FourNumBlueToothUtil {
    public static byte[] getByteFromHexString(String writeStr) {
        try {
            if (XHStringUtil.isEmpty(writeStr, false)) {
                ToastUtils.showShort("请输入指令");
                return null;
            }
            ArrayList<String> arrayList = new ArrayList<>();
            if (writeStr.length() % 2 == 0) {
                for (int i = 0; i < writeStr.length(); i++) {
                    if (i % 2 == 0) {
                        arrayList.add(writeStr.substring(i, i + 2));
                    }
                }
            } else {
                String lastStr = "0" + writeStr.substring(writeStr.length() - 1, writeStr.length());
                writeStr = writeStr.substring(0, writeStr.length() - 1) + lastStr;
                for (int i = 0; i < writeStr.length(); i++) {
                    if (i % 2 == 0) {
                        arrayList.add(writeStr.substring(i, i + 2));
                    }

                }
            }
            byte[] bytes = new byte[arrayList.size()];
            for (int i = 0; i < arrayList.size(); i++) {
                int value = Integer.parseInt(arrayList.get(i), 16);
                bytes[i] = (byte) value;
            }
            return bytes;
        } catch (Exception e) {
            ToastUtils.showShort("解析错误");
            return null;
        }
    }

    public static byte[] getFourOEBytes(String writeStr) {
        return getByteFromHexString("68000e" + writeStr + "16");
    }

    public static byte[] getFourOFBytes(String params) {
        return getByteFromHexString("68000f" + params + "16");
    }

    public static byte[] getFourCKWriteBytes(String botelv, String stopBit, String jiaoyan) {
        StringBuffer str = new StringBuffer();
        str.append("680025");
        long isn = Long.parseLong(botelv.replaceAll("bps", ""));
        byte[] bytes = new byte[3];
        for (int i = 2; i >= 0; i--) {
            bytes[i] = (byte) (isn & 0xff);
            isn = isn >> 8;
        }
        str.append(ByteConvertUtils.convertByteToHexString(bytes));
        if ("1位".equals(stopBit)) {
            str.append("00");
        } else if ("1.5位".equals(stopBit)) {
            str.append("02");
        } else if ("2位".equals(stopBit)) {
            str.append("01");
        } else {
            str.append("00");
        }
        if ("无校验".equals(jiaoyan)) {
            str.append("00");
        } else if ("偶校验".equals(jiaoyan)) {
            str.append("02");
        } else if ("奇校验".equals(jiaoyan)) {
            str.append("01");
        } else {
            str.append("00");
        }
        str.append("16");
        return getByteFromHexString(str.toString());
    }

    public static byte[] getFourCKReadBytes() {
        StringBuffer str = new StringBuffer();
        str.append("68002616");
        return getByteFromHexString(str.toString());
    }

    public static byte[] getFour20Bytes(String params) {
        byte[] result = new byte[6];
        result[0] = 0x68;
        result[1] = 0x00;
        result[2] = 0x20;
        long isn = Long.parseLong(params);
        for (int i = 0; i < 2; i++) {
            result[i + 3] = (byte) (isn & 0xff);
            isn = isn >> 8;
        }
        result[5] = 0x16;
        return result;
    }

    public static byte[] getFour21Bytes() {
        byte[] result = new byte[4];
        result[0] = 0x68;
        result[1] = 0x00;
        result[2] = 0x21;
        result[3] = 0x16;
        return result;
    }

    public static byte[] getFour22Bytes() {
        byte[] result = new byte[4];
        result[0] = 0x68;
        result[1] = 0x00;
        result[2] = 0x22;
        result[3] = 0x16;
        return result;
    }

    public static byte[] getFour23Bytes() {
        byte[] result = new byte[4];
        result[0] = 0x68;
        result[1] = 0x00;
        result[2] = 0x23;
        result[3] = 0x16;
        return result;
    }

    public static byte[] getFour24Bytes() {
        byte[] result = new byte[4];
        result[0] = 0x68;
        result[1] = 0x00;
        result[2] = 0x24;
        result[3] = 0x16;
        return result;
    }

    public static ArrayList<String> getAllPinLv() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("1分钟");
        arrayList.add("5分钟");
        arrayList.add("15分钟");
        arrayList.add("30分钟");
        arrayList.add("1小时");
        arrayList.add("3小时");
        arrayList.add("12小时");
        arrayList.add("24小时");
        arrayList.add("72小时");
        return arrayList;
    }

    public static ArrayList<String> getAllBoTeLv() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("1200bps");
        arrayList.add("2400bps");
        arrayList.add("4800bps");
        arrayList.add("9600bps");
        arrayList.add("14400bps");
        arrayList.add("38400bps");
        arrayList.add("56000bps");
        arrayList.add("115200bps");
        arrayList.add("128000bps");
        return arrayList;
    }

    public static ArrayList<String> getAllStopBit() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("1位");
        arrayList.add("1.5位");
        arrayList.add("2位");
        return arrayList;
    }

    public static ArrayList<String> getAllWangLuoJiaoHu() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("最大发送1次");
        arrayList.add("最大发送2次");
        arrayList.add("最大发送3次");
        return arrayList;
    }

    public static ArrayList<String> getAllKuoPinYinZi() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("SF7");
        arrayList.add("SF8");
        arrayList.add("SF9");
        arrayList.add("SF10");
        arrayList.add("SF11");
        arrayList.add("SF12");
        return arrayList;
    }

    public static ArrayList<String> getAllJiaoYan() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("无校验");
        arrayList.add("偶校验");
        arrayList.add("奇校验");
        return arrayList;
    }

    public static ArrayList<String> getDianYuanLeiXing() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("物联电池");
        arrayList.add("外接电源");
        return arrayList;
    }

    public static ArrayList<String> getChuanGanMoShi() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("数字通讯");
        arrayList.add("累计脉冲");
        arrayList.add("状态切换");
        return arrayList;
    }

    public static byte getChuanGanMoShiByte(String moShi) {
        byte result = 0;
        if ("数字通讯".equals(moShi)) {
            result = 0x03;
        } else if ("累计脉冲".equals(moShi)) {
            result = 0x01;
        } else if ("状态切换".equals(moShi)) {
            result = 0x02;
        }
        return result;
    }

    public static ArrayList<String> getChuanGanXinHaoShuZi() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("RS485");
        arrayList.add("TTL2");
        arrayList.add("MOD-BUS");
        return arrayList;
    }

    public static ArrayList<String> getChuanGanXinHaoLeiJi() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("3EV");
        arrayList.add("正反脉冲");
        arrayList.add("2EV");
        arrayList.add("无磁正反脉冲");
        return arrayList;
    }

    public static ArrayList<String> getChuanGanXinHaoZhuangTai() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("开关状态");
        return arrayList;
    }

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
        } else if (value.equals("开关状态")) {
            result = 7;
        } else if (value.equals("4-20MA")) {
            result = 8;
        } else if (value.equals("单EV")) {
            result = 9;
        } else if (value.equals("无磁正反脉冲")) {
            result = 0x0a;
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
        } else {
            result[3] = 0x04;
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

    public static byte[] getFouWangluoJiaoHuBytes(String jiaohu) {
        byte[] result = new byte[6];
        result[0] = 0x68;
        result[1] = 0x03;
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

}
