package com.wu.loushanyun.basemvp.m.st3;

import com.wu.loushanyun.base.util.MapParams;

import java.util.HashMap;

import met.hx.com.librarybase.some_utils.ByteConvertUtils;

public class ST3ByteTool {

    public byte HUB = (byte) 0xfd;
    private String sT3Type;


    public ST3ByteTool setsAtParams(String sT3Type) {
        this.sT3Type = sT3Type;
        return this;
    }

    public String getsT3Type() {
        return sT3Type;
    }

    /**
     * 初始化Hub号
     *
     * @return
     */
    public byte[] getThirdInitBubBytes() {
        byte[] result = new byte[4];
        result[0] = (byte) 0xc0;
        result[1] = (byte) 0xff;
        result[2] = HUB;
        //校验
        byte cs = 0;
        for (int i = 1; i < 3; i++) {
            cs += result[i];
        }
        result[3] = cs;
        return ThirdNumBlueToothUtil.getThirdOx60Bytes(ByteConvertUtils.convertByteToHexString(result));
    }

    /**
     * 置数
     *
     * @param num
     * @param id
     * @param beilv
     * @param maichong
     * @return
     */

    public byte[] getThirdZhiShuBytes(int num, long id, String beilv, int maichong) {
        byte[] result = new byte[17];
        result[0] = (byte) 0xc0;
        result[1] = HUB;
        result[2] =(byte) (num & 0xff);
        result[3] = 0x0d;
        result[4] = (byte) 0xf9;
        for (int i = 0; i < 5; i++) {
            result[i + 5] = (byte) (id & 0xff);
            id = id >> 8;
        }
        result[10] = 0x00;
        switch (beilv) {
            case "0.001":
                result[11] = 0b00000000;
                break;
            case "0.01":
                result[11] = 0b00000001;
                break;
            case "0.1":
                result[11] = 0b00000010;
                break;
            case "1":
                result[11] = 0b00000011;
                break;
            case "10":
                result[11] = 0b00000100;
                break;
            case "100":
                result[11] = 0b00000101;
                break;
        }
        for (int i = 0; i < 3; i++) {
            result[i + 12] = (byte) (maichong & 0xff);
            maichong = maichong >> 8;
        }
        result[15] = 0x00;
        //校验
        byte cs = 0;
        for (int i = 1; i < 16; i++) {
            cs += result[i];
        }
        result[16] = cs;
        return ThirdNumBlueToothUtil.getThirdOx60Bytes(ByteConvertUtils.convertByteToHexString(result));
    }



    /**
     * 表单元初始化
     *
     * @param num
     * @param id
     * @param beilv
     * @param maichong
     * @return
     */
    public byte[] getThirdInitBiaoBytes(int num, long id, String beilv, int maichong) {
        byte[] result = new byte[18];
        result[0] = (byte) 0xc0;
        result[1] = HUB;
        result[2] = 0x00;
        result[3] = 0x0e;
        result[4] = (byte) 0xfa;
        result[5] = (byte) (num & 0xff);
        for (int i = 0; i < 5; i++) {
            result[i + 6] = (byte) (id & 0xff);
            id = id >> 8;
        }
        result[11] = 0x00;
        switch (beilv) {
            case "0.001":
                result[12] = 0b00000000;
                break;
            case "0.01":
                result[12] = 0b00000001;
                break;
            case "0.1":
                result[12] = 0b00000010;
                break;
            case "1":
                result[12] = 0b00000011;
                break;
            case "10":
                result[12] = 0b00000100;
                break;
            case "100":
                result[12] = 0b00000101;
                break;
        }
        for (int i = 0; i < 3; i++) {
            result[i + 13] = (byte) (maichong & 0xff);
            maichong = maichong >> 8;
        }
        result[16] = 0x00;
        //校验
        byte cs = 0;
        for (int i = 1; i < 17; i++) {
            cs += result[i];
        }
        result[17] = cs;
        return ThirdNumBlueToothUtil.getThirdOx60Bytes(ByteConvertUtils.convertByteToHexString(result));
    }


    /**
     * 配置表单元
     *
     * @param num
     * @param chuanganleiixng
     * @param year
     * @param month
     * @param day
     * @param changjia
     * @return
     */
    public byte[] getThirdSettingBiaoBytes(int num, String chuanganleiixng, int year, int month, int day, long changjia) {
        byte[] result = new byte[16];
        result[0] = (byte) 0xc0;
        result[1] = HUB;
        result[2] = (byte) (num & 0xff);
        result[3] = 0x0c;
        result[4] = (byte) 0xfa;
        switch (chuanganleiixng) {
            case "3EV":
                result[5] = 0x01;
                break;
            case "2EV":
                result[5] = 0x03;
                break;
            case "无磁正反脉冲":
                result[5] = 0x0a;
                break;
        }
        result[6] = (byte) (year & 0xff);
        result[7] = (byte) (month & 0xff);
        result[8] = (byte) (day & 0xff);
        for (int i = 0; i < 6; i++) {
            result[i + 9] = (byte) (changjia & 0xff);
            changjia = changjia >> 8;
        }
        //校验
        byte cs = 0;
        for (int i = 1; i < 15; i++) {
            cs += result[i];
        }
        result[15] = cs;
        return ThirdNumBlueToothUtil.getThirdOx60Bytes(ByteConvertUtils.convertByteToHexString(result));
    }

    /**
     * 抄表
     *
     * @param num
     * @return
     */
    public byte[] getThirdReadBiaoBytes(int num) {
        byte[] result = new byte[6];
        result[0] = (byte) 0xc0;
        result[1] = HUB;
        result[2] = (byte) (num & 0xff);
        result[3] = 0x02;
        result[4] = (byte) 0xfb;
        //校验
        byte cs = 0;
        for (int i = 1; i < 5; i++) {
            cs += result[i];
        }
        result[5] = cs;
        return ThirdNumBlueToothUtil.getThirdOx60Bytes(ByteConvertUtils.convertByteToHexString(result));
    }

    /**
     * 解析抄表数据
     *
     * @param result
     * @return
     */
    public HashMap<String, String> parseThirdReadBiaoBytes(byte[] result) throws Exception {
        HashMap<String, String> map = new HashMap<>();
        map.put(MapParams.表号, String.valueOf(result[4] & 0xff));
        long temp = 0;
        for (int i = 4; i >= 0; i--) {
            temp = temp * 256 + (long) (result[i + 7] & 0xff);
        }
        map.put(MapParams.设备ID, String.valueOf(temp));
        int beilv = result[13] & 0xff;
        switch (beilv) {
            //0.001
            case 0b00000000:
                map.put(MapParams.倍率, "0");
                break;
            //0.01
            case 0b00000001:
                map.put(MapParams.倍率, "1");
                break;
            //0.1
            case 0b00000010:
                map.put(MapParams.倍率, "2");
                break;
            //1
            case 0b00000011:
                map.put(MapParams.倍率, "3");
                break;
            //10
            case 0b00000100:
                map.put(MapParams.倍率, "4");
                break;
            //100
            case 0b00000101:
                map.put(MapParams.倍率, "5");
                break;
        }
        temp = 0L;
        for (int i = 2; i >= 0; i--) {
            temp = temp * 256 + (long) (result[i + 14] & 0xff);
        }
        map.put(MapParams.脉冲数, String.valueOf(temp));
        map.put(MapParams.状态, String.valueOf(result[17] & 0xff));
        map.put(MapParams.HUB号, String.valueOf(result[18] & 0xff));
        return map;
    }

    /**
     * 读取配置信息
     *
     * @param num
     * @return
     */
    public byte[] getThirdReadSettingBytes(int num) {
        byte[] result = new byte[6];
        result[0] = (byte) 0xc0;
        result[1] = HUB;
        result[2] = (byte) (num & 0xff);
        result[3] = 0x02;
        result[4] = (byte) 0xfc;
        //校验
        byte cs = 0;
        for (int i = 1; i < 5; i++) {
            cs += result[i];
        }
        result[5] = cs;
        return ThirdNumBlueToothUtil.getThirdOx60Bytes(ByteConvertUtils.convertByteToHexString(result));
    }

    /**
     * 解析配置数据
     *
     * @param result
     * @return
     */
    public HashMap<String, String> parseThirdReadSettingBytes(byte[] result) throws Exception {
        HashMap<String, String> map = new HashMap<>();
        map.put(MapParams.表号, String.valueOf(result[4] & 0xff));
        long temp = 0;
        for (int i = 4; i >= 0; i--) {
            temp = temp * 256 + (long) (result[i + 7] & 0xff);
        }
        map.put(MapParams.设备ID, String.valueOf(temp));
        int beilv = result[13] & 0xff;
        switch (beilv) {
            //0.001
            case 0b00000000:
                map.put(MapParams.倍率, "0");
                break;
            //0.01
            case 0b00000001:
                map.put(MapParams.倍率, "1");
                break;
            //0.1
            case 0b00000010:
                map.put(MapParams.倍率, "2");
                break;
            //1
            case 0b00000011:
                map.put(MapParams.倍率, "3");
                break;
            //10
            case 0b00000100:
                map.put(MapParams.倍率, "4");
                break;
            //100
            case 0b00000101:
                map.put(MapParams.倍率, "5");
                break;
        }
        map.put(MapParams.状态, String.valueOf(result[14] & 0xff));
        int xinhao = result[15] & 0xff;
        switch (xinhao) {
            case 0x03:
                map.put(MapParams.传感类型, "3");
                break;
            case 0x01:
                map.put(MapParams.传感类型, "1");
                break;
            case 0x0a:
                map.put(MapParams.传感类型, "10");
                break;
        }
        map.put(MapParams.软件版本, String.valueOf(result[16] & 0xff));
        temp = 0;
        for (int i = 5; i >= 0; i--) {
            temp = temp * 256 + (long) (result[i + 17] & 0xff);
        }
        map.put(MapParams.企业代码, String.valueOf(temp));
        map.put(MapParams.出厂时间_年, String.valueOf(result[23] & 0xff));
        map.put(MapParams.出厂时间_月, String.valueOf(result[24] & 0xff));
        map.put(MapParams.出厂时间_日, String.valueOf(result[25] & 0xff));
        map.put(MapParams.HUB号, String.valueOf(result[26] & 0xff));
        return map;
    }

    /**
     * 验表
     *
     * @return
     */
    public byte[] getThirdReadYanBiaoBytes() {
        byte[] result = new byte[6];
        result[0] = (byte) 0xc0;
        result[1] = HUB;
        result[2] = (byte) (0 & 0xff);
        result[3] = 0x02;
        result[4] = (byte) 0xfb;
        //校验
        byte cs = 0;
        for (int i = 1; i < 5; i++) {
            cs += result[i];
        }
        result[5] = cs;
        return ThirdNumBlueToothUtil.getThirdOx60Bytes(ByteConvertUtils.convertByteToHexString(result));
    }

    /**
     * 验表
     *
     * @param result
     * @return
     */
    public HashMap<String, String> parseThirdReadYanBiaoBytes(byte[] result) throws Exception {
        HashMap<String, String> map = new HashMap<>();
        map.put(MapParams.表号, String.valueOf(result[4] & 0xff));
        long temp = 0;
        for (int i = 4; i >= 0; i--) {
            temp = temp * 256 + (long) (result[i + 7] & 0xff);
        }
        map.put(MapParams.设备ID, String.valueOf(temp));
        int beilv = result[13] & 0xff;
        switch (beilv) {
            //0.001
            case 0b00000000:
                map.put(MapParams.倍率, "0");
                break;
            //0.01
            case 0b00000001:
                map.put(MapParams.倍率, "1");
                break;
            //0.1
            case 0b00000010:
                map.put(MapParams.倍率, "2");
                break;
            //1
            case 0b00000011:
                map.put(MapParams.倍率, "3");
                break;
            //10
            case 0b00000100:
                map.put(MapParams.倍率, "4");
                break;
            //100
            case 0b00000101:
                map.put(MapParams.倍率, "5");
                break;
        }
        temp = 0L;
        for (int i = 2; i >= 0; i--) {
            temp = temp * 256 + (long) (result[i + 14] & 0xff);
        }
        map.put(MapParams.脉冲数, String.valueOf(temp));
        map.put(MapParams.状态, String.valueOf(result[17] & 0xff));
        map.put(MapParams.HUB号, String.valueOf(result[18] & 0xff));
        return map;
    }
}