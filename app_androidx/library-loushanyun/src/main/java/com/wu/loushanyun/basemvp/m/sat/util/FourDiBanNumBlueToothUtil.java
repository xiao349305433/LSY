package com.wu.loushanyun.basemvp.m.sat.util;

import com.wu.loushanyun.base.util.MapParams;

import java.util.ArrayList;
import java.util.HashMap;

import met.hx.com.librarybase.some_utils.ByteConvertUtils;

/**
 * 4号模组数字通讯蓝牙协议指令集
 */
public class FourDiBanNumBlueToothUtil {

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

    /**
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



    public static ArrayList<String> getAllChuanganxinhao() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("2EV");
        arrayList.add("3EV");
        arrayList.add("无磁正反脉冲");
        arrayList.add("0-5V");
        arrayList.add("状态切换");
        return arrayList;
    }

    /**
     * 配置传感器
     *
     * @return
     */
    public static byte[] getChuanGanSetting(String chuanganxinhao) {
        byte[] result = new byte[6];
        result[0] = (byte) 0x60;
        result[1] = (byte) 0x02;
        result[2] = 0x01;
        if ("2EV".equals(chuanganxinhao)) {
            result[3] = 0x01;
        } else if ("3EV".equals(chuanganxinhao)) {
            result[3] = 0x02;
        } else if ("无磁正反脉冲".equals(chuanganxinhao)) {
            result[3] = 0x03;
        } else if ("0-5V".equals(chuanganxinhao)) {
            result[3] = 0x04;
        } else if ("状态切换".equals(chuanganxinhao)) {
            result[3] = 0x05;
        } else {
            result[3] = 0x00;
        }
        //校验
        byte cs = 0;
        for (int i = 1; i < 4; i++) {
            cs += result[i];
        }
        result[4] = cs;
        result[5] = 0x20;
        return FourNumBlueToothUtil.getFourOEBytes(ByteConvertUtils.convertByteToHexString(result));
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
        byte[] result = new byte[12];
        result[0] = (byte) 0x60;
        result[1] = (byte) 0x08;
        result[2] = 0x02;
        result[3] = 0x00;
        for (int i = 0; i <= 3; i++) {
            result[i + 4] = (byte) (mai & 0xff);
            mai = mai >> 8;
        }
        if ("0.001".equals(beilv)) {
            result[8] = 0x00;
        } else if ("0.01".equals(beilv)) {
            result[8] = 0x01;
        } else if ("0.1".equals(beilv)) {
            result[8] = 0x02;
        } else if ("1".equals(beilv)) {
            result[8] = 0x03;
        } else if ("10".equals(beilv)) {
            result[8] = 0x04;
        } else if ("100".equals(beilv)) {
            result[8] = 0x05;
        } else {
            result[8] = 0x00;
        }
        result[9] = 0x00;
        //校验
        byte cs = 0;
        for (int i = 1; i < 10; i++) {
            cs += result[i];
        }
        result[10] = cs;
        result[11] = 0x20;
        return FourNumBlueToothUtil.getFourOEBytes(ByteConvertUtils.convertByteToHexString(result));
    }

    /**
     * 初始化线性传感
     *
     * @param maxx
     * @return
     */
    public static byte[] getxianxingSetting(String maxx) {
        long max = Long.valueOf(maxx);
        byte[] result = new byte[6];
        result[0] = (byte) 0x60;
        result[1] = (byte) 0x03;
        result[2] = 0x03;
        for (int i = 0; i <= 1; i++) {
            result[i + 3] = (byte) (max & 0xff);
            max = max >> 8;
        }
        //校验
        byte cs = 0;
        for (int i = 1; i < 4; i++) {
            cs += result[i];
        }
        result[4] = cs;
        result[5] = 0x20;
        return FourNumBlueToothUtil.getFourOEBytes(ByteConvertUtils.convertByteToHexString(result));
    }



    /**
     * 获取正向体积（double类型）
     *
     * @return
     */
    public static byte[] getleijiZhengReading() {
        byte[] result = new byte[5];
        result[0] = (byte) 0x60;
        result[1] = (byte) 0x01;
        result[2] = 0x04;
        //校验
        byte cs = 0;
        for (int i = 1; i < 3; i++) {
            cs += result[i];
        }
        result[3] = cs;
        result[4] = 0x20;
        return FourNumBlueToothUtil.getFourOEBytes(ByteConvertUtils.convertByteToHexString(result));
    }

    /**
     * 获取负向体积（double类型）
     *
     * @return
     */
    public static byte[] getleijiFuReading() {
        byte[] result = new byte[5];
        result[0] = (byte) 0x60;
        result[1] = (byte) 0x01;
        result[2] = 0x05;
        //校验
        byte cs = 0;
        for (int i = 1; i < 3; i++) {
            cs += result[i];
        }
        result[3] = cs;
        result[4] = 0x20;
        return FourNumBlueToothUtil.getFourOEBytes(ByteConvertUtils.convertByteToHexString(result));
    }


    /**
     * 获取正向脉冲数
     *
     * @return
     */
    public static byte[] getleijiZhengNumReading() {
        byte[] result = new byte[5];
        result[0] = (byte) 0x60;
        result[1] = (byte) 0x01;
        result[2] = 0x06;
        //校验
        byte cs = 0;
        for (int i = 1; i < 3; i++) {
            cs += result[i];
        }
        result[3] = cs;
        result[4] = 0x20;
        return FourNumBlueToothUtil.getFourOEBytes(ByteConvertUtils.convertByteToHexString(result));
    }

    /**
     * 获取反向脉冲数
     *
     * @return
     */
    public static byte[] getleijiFuNumReading() {
        byte[] result = new byte[5];
        result[0] = (byte) 0x60;
        result[1] = (byte) 0x01;
        result[2] = 0x07;
        //校验
        byte cs = 0;
        for (int i = 1; i < 3; i++) {
            cs += result[i];
        }
        result[3] = cs;
        result[4] = 0x20;
        return FourNumBlueToothUtil.getFourOEBytes(ByteConvertUtils.convertByteToHexString(result));
    }

    /**
     * 获取倍率
     *
     * @return
     */
    public static byte[] getleijiBeilvReading() {
        byte[] result = new byte[5];
        result[0] = (byte) 0x60;
        result[1] = (byte) 0x01;
        result[2] = 0x08;
        //校验
        byte cs = 0;
        for (int i = 1; i < 3; i++) {
            cs += result[i];
        }
        result[3] = cs;
        result[4] = 0x20;
        return FourNumBlueToothUtil.getFourOEBytes(ByteConvertUtils.convertByteToHexString(result));
    }

    /**
     * 获取瞬时流量
     *
     * @return
     */
    public static byte[] getleijiLiuLiangReading() {
        byte[] result = new byte[5];
        result[0] = (byte) 0x60;
        result[1] = (byte) 0x01;
        result[2] = 0x09;
        //校验
        byte cs = 0;
        for (int i = 1; i < 3; i++) {
            cs += result[i];
        }
        result[3] = cs;
        result[4] = 0x20;
        return FourNumBlueToothUtil.getFourOEBytes(ByteConvertUtils.convertByteToHexString(result));
    }

    /**
     * 获取拆卸状态
     *
     * @return
     */
    public static byte[] getleijiChaixieReading() {
        byte[] result = new byte[5];
        result[0] = (byte) 0x60;
        result[1] = (byte) 0x01;
        result[2] = 0x0a;
        //校验
        byte cs = 0;
        for (int i = 1; i < 3; i++) {
            cs += result[i];
        }
        result[3] = cs;
        result[4] = 0x20;
        return FourNumBlueToothUtil.getFourOEBytes(ByteConvertUtils.convertByteToHexString(result));
    }

    /**
     * 获取流向状态
     *
     * @return
     */
    public static byte[] getleijiLiuXiangReading() {
        byte[] result = new byte[5];
        result[0] = (byte) 0x60;
        result[1] = (byte) 0x01;
        result[2] = 0x0b;
        //校验
        byte cs = 0;
        for (int i = 1; i < 3; i++) {
            cs += result[i];
        }
        result[3] = cs;
        result[4] = 0x20;
        return FourNumBlueToothUtil.getFourOEBytes(ByteConvertUtils.convertByteToHexString(result));
    }

    /**
     * 获取压力读数
     *
     * @return
     */
    public static byte[] getXianxingYaliReading() {
        byte[] result = new byte[5];
        result[0] = (byte) 0x60;
        result[1] = (byte) 0x01;
        result[2] = 0x0c;
        //校验
        byte cs = 0;
        for (int i = 1; i < 3; i++) {
            cs += result[i];
        }
        result[3] = cs;
        result[4] = 0x20;
        return FourNumBlueToothUtil.getFourOEBytes(ByteConvertUtils.convertByteToHexString(result));
    }

    /**
     * 获取压力值
     *
     * @return
     */
    public static byte[] getXianxingYaliZhiReading() {
        byte[] result = new byte[5];
        result[0] = (byte) 0x60;
        result[1] = (byte) 0x01;
        result[2] = 0x11;
        //校验
        byte cs = 0;
        for (int i = 1; i < 3; i++) {
            cs += result[i];
        }
        result[3] = cs;
        result[4] = 0x20;
        return FourNumBlueToothUtil.getFourOEBytes(ByteConvertUtils.convertByteToHexString(result));
    }

    /**
     * 获取最大值
     *
     * @return
     */
    public static byte[] getXianxingMaxZhiReading() {
        byte[] result = new byte[5];
        result[0] = (byte) 0x60;
        result[1] = (byte) 0x01;
        result[2] = 0x12;
        //校验
        byte cs = 0;
        for (int i = 1; i < 3; i++) {
            cs += result[i];
        }
        result[3] = cs;
        result[4] = 0x20;
        return FourNumBlueToothUtil.getFourOEBytes(ByteConvertUtils.convertByteToHexString(result));
    }

    /**
     * 获取压力传感器状态
     *
     * @return
     */
    public static byte[] getXianxingYaliZtReading() {
        byte[] result = new byte[5];
        result[0] = (byte) 0x60;
        result[1] = (byte) 0x01;
        result[2] = 0x0d;
        //校验
        byte cs = 0;
        for (int i = 1; i < 3; i++) {
            cs += result[i];
        }
        result[3] = cs;
        result[4] = 0x20;
        return FourNumBlueToothUtil.getFourOEBytes(ByteConvertUtils.convertByteToHexString(result));
    }

    /**
     * 获取状态切换数据
     *
     * @return
     */
    public static byte[] getZhuangtaiReading() {
        byte[] result = new byte[5];
        result[0] = (byte) 0x60;
        result[1] = (byte) 0x01;
        result[2] = 0x0e;
        //校验
        byte cs = 0;
        for (int i = 1; i < 3; i++) {
            cs += result[i];
        }
        result[3] = cs;
        result[4] = 0x20;
        return FourNumBlueToothUtil.getFourOEBytes(ByteConvertUtils.convertByteToHexString(result));
    }

    /**
     * 获取电源状态
     *
     * @return
     */
    public static byte[] getDianYuanZhuangtaiReading() {
        byte[] result = new byte[5];
        result[0] = (byte) 0x60;
        result[1] = (byte) 0x01;
        result[2] = 0x10;
        //校验
        byte cs = 0;
        for (int i = 1; i < 3; i++) {
            cs += result[i];
        }
        result[3] = cs;
        result[4] = 0x20;
        return FourNumBlueToothUtil.getFourOEBytes(ByteConvertUtils.convertByteToHexString(result));
    }

    /**
     * 获取传感信号
     *
     * @return
     */
    public static byte[] getChuanganxinhaoReading() {
        byte[] result = new byte[5];
        result[0] = (byte) 0x60;
        result[1] = (byte) 0x01;
        result[2] = 0x13;
        //校验
        byte cs = 0;
        for (int i = 1; i < 3; i++) {
            cs += result[i];
        }
        result[3] = cs;
        result[4] = 0x20;
        return FourNumBlueToothUtil.getFourOEBytes(ByteConvertUtils.convertByteToHexString(result));
    }

    /**
     * 解析正向体积 double
     *
     * @param bytes
     * @return
     */
    public static HashMap<String, String> getParseZhengReading(byte[] bytes) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(MapParams.错误码, String.valueOf(bytes[3] & 0xff));
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 7; i >= 0; i--) {
            stringBuffer.append(ByteConvertUtils.conver2BinaryStr(bytes[i + 4]));
        }
        hashMap.put(MapParams.正向体积, String.valueOf(ByteConvertUtils.formatNumber(stringBuffer.toString(), 3)));
        return hashMap;
    }

    /**
     * 解析反向体积 double
     *
     * @param bytes
     * @return
     */
    public static HashMap<String, String> getParseFuReading(byte[] bytes) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(MapParams.错误码, String.valueOf(bytes[3] & 0xff));
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 7; i >= 0; i--) {
            stringBuffer.append(ByteConvertUtils.conver2BinaryStr(bytes[i + 4]));
        }
        hashMap.put(MapParams.反向体积, String.valueOf(ByteConvertUtils.formatNumber(stringBuffer.toString(), 3)));
        return hashMap;
    }

    /**
     * 解析反向脉冲数
     *
     * @param bytes
     * @return
     */
    public static HashMap<String, String> getParseFuNumReading(byte[] bytes) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(MapParams.错误码, String.valueOf(bytes[3] & 0xff));
        long tmp = 0;
        for (int i = 3; i >= 0; i--) {
            tmp = (tmp * 256) + (bytes[i + 4] & 0xff);
        }
        hashMap.put(MapParams.反向脉冲数, String.valueOf(tmp));
        return hashMap;
    }

    /**
     * 解析正向脉冲数
     *
     * @param bytes
     * @return
     */
    public static HashMap<String, String> getParseZhengNumReading(byte[] bytes) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(MapParams.错误码, String.valueOf(bytes[3] & 0xff));
        long tmp = 0;
        for (int i = 3; i >= 0; i--) {
            tmp = (tmp * 256) + (bytes[i + 4] & 0xff);
        }
        hashMap.put(MapParams.正向脉冲数, String.valueOf(tmp));
        return hashMap;
    }

    /**
     * 解析倍率
     *
     * @param bytes
     * @return
     */
    public static HashMap<String, String> getParseBeiLVReading(byte[] bytes) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(MapParams.错误码, String.valueOf(bytes[3] & 0xff));
        int beilv = bytes[4] & 0xff;
        if (beilv == 0) {
            hashMap.put(MapParams.倍率, "0.001");
        } else if (beilv == 1) {
            hashMap.put(MapParams.倍率, "0.01");
        } else if (beilv == 2) {
            hashMap.put(MapParams.倍率, "0.1");
        } else if (beilv == 3) {
            hashMap.put(MapParams.倍率, "1");
        } else if (beilv == 4) {
            hashMap.put(MapParams.倍率, "10");
        } else if (beilv == 5) {
            hashMap.put(MapParams.倍率, "100");
        }
        return hashMap;
    }

    /**
     * 瞬时流量
     *
     * @param bytes
     * @return
     */
    public static HashMap<String, String> getParseShunShiReading(byte[] bytes) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(MapParams.错误码, String.valueOf(bytes[3] & 0xff));
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 7; i >= 0; i--) {
            stringBuffer.append(ByteConvertUtils.conver2BinaryStr(bytes[i + 4]));
        }
        hashMap.put(MapParams.瞬时流量, String.valueOf(ByteConvertUtils.formatNumber(stringBuffer.toString(), 3)));
        return hashMap;
    }

    /**
     * 拆卸状态
     *
     * @param bytes
     * @return
     */
    public static HashMap<String, String> getParseChaixieReading(byte[] bytes) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(MapParams.错误码, String.valueOf(bytes[3] & 0xff));
        hashMap.put(MapParams.表拆卸状态, String.valueOf(bytes[4] & 0xff));
        return hashMap;
    }

    /**
     * 流向状态
     *
     * @param bytes
     * @return
     */
    public static HashMap<String, String> getParseLiuXiangReading(byte[] bytes) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(MapParams.错误码, String.valueOf(bytes[3] & 0xff));
        hashMap.put(MapParams.流向状态, String.valueOf(bytes[4] & 0xff));
        return hashMap;
    }

    /**
     * 压力读数
     *
     * @param bytes
     * @return
     */
    public static HashMap<String, String> getParseYaLiReading(byte[] bytes) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(MapParams.错误码, String.valueOf(bytes[3] & 0xff));
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 7; i >= 0; i--) {
            stringBuffer.append(ByteConvertUtils.conver2BinaryStr(bytes[i + 4]));
        }
        hashMap.put(MapParams.压力读数, String.valueOf(ByteConvertUtils.formatNumber(stringBuffer.toString(), 3)));
        return hashMap;
    }

    /**
     * 压力值
     *
     * @param bytes
     * @return
     */
    public static HashMap<String, String> getParseYaLiZhiReading(byte[] bytes) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(MapParams.错误码, String.valueOf(bytes[3] & 0xff));
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 7; i >= 0; i--) {
            stringBuffer.append(ByteConvertUtils.conver2BinaryStr(bytes[i + 4]));
        }
        hashMap.put(MapParams.压力值, String.valueOf(ByteConvertUtils.formatNumber(stringBuffer.toString(), 3)));
        return hashMap;
    }

    /**
     * 最大值
     *
     * @param bytes
     * @return
     */
    public static HashMap<String, String> getParseYaLiMaxZhiReading(byte[] bytes) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(MapParams.错误码, String.valueOf(bytes[3] & 0xff));
        long tmp = 0;
        for (int i = 1; i >= 0; i--) {
            tmp = (tmp * 256) + (bytes[i + 4] & 0xff);
        }
        hashMap.put(MapParams.最大值, String.valueOf(tmp));
        return hashMap;
    }

    /**
     * 压力状态
     *
     * @param bytes
     * @return
     */
    public static HashMap<String, String> getParseYaliZhuangtaiReading(byte[] bytes) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(MapParams.错误码, String.valueOf(bytes[3] & 0xff));
        hashMap.put(MapParams.压力传感器状态, String.valueOf(bytes[4] & 0xff));
        return hashMap;
    }

    /**
     * 电池状态
     *
     * @param bytes
     * @return
     */
    public static HashMap<String, String> getParseDianyuanZhuangtaiReading(byte[] bytes) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(MapParams.错误码, String.valueOf(bytes[3] & 0xff));
        hashMap.put(MapParams.电池状态, String.valueOf(bytes[4] & 0xff));
        return hashMap;
    }

    /**
     * 传感信号
     * 0x00: 无（默认）
     * 0x01: 2EV（累积脉冲传感）
     * 0x02: 3EV（累积脉冲传感）
     * 0x03: 无磁正反脉冲（累积脉冲传感）
     * 0x04：0-5V（线性传感）
     * 0x05：状态切换（状态传感0开1关）
     *
     * @param bytes
     * @return
     */
    public static HashMap<String, String> getParseChuanganqiReading(byte[] bytes) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(MapParams.错误码, String.valueOf(bytes[3] & 0xff));
        int chuanganqi = bytes[4] & 0xff;
        if (chuanganqi == 0) {
            hashMap.put(MapParams.传感信号, "无");
        } else if (chuanganqi == 1) {
            hashMap.put(MapParams.传感信号, "2EV（累积脉冲传感）");
        } else if (chuanganqi == 2) {
            hashMap.put(MapParams.传感信号, "3EV（累积脉冲传感）");
        } else if (chuanganqi == 3) {
            hashMap.put(MapParams.传感信号, "无磁正反脉冲（累积脉冲传感）");
        } else if (chuanganqi == 4) {
            hashMap.put(MapParams.传感信号, "0-5V（线性传感）");
        } else if (chuanganqi == 5) {
            hashMap.put(MapParams.传感信号, "状态切换（状态传感0开1关）");
        }
        return hashMap;
    }

    /**
     * 状态切换 时长
     *
     * @param bytes
     * @return
     */
    public static HashMap<String, String> getParseShiChangZhuangtaiReading(byte[] bytes) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(MapParams.错误码, String.valueOf(bytes[3] & 0xff));
        long tmp = 0;
        for (int i = 3; i >= 0; i--) {
            tmp = (tmp * 256) + (bytes[i + 5] & 0xff);
        }
        hashMap.put(MapParams.状态时长, String.valueOf(tmp));
        hashMap.put(MapParams.状态位, String.valueOf(bytes[4] & 0xff));
        return hashMap;
    }
}
