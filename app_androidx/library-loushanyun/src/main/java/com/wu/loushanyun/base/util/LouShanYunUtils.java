package com.wu.loushanyun.base.util;

import com.wu.loushanyun.basemvp.m.SaveDataMeter;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import met.hx.com.base.base.multitype.Items;
import met.hx.com.librarybase.some_utils.ByteConvertUtils;
import met.hx.com.librarybase.some_utils.LogUtils;

public class LouShanYunUtils {

    /**
     * 设置脉冲底数+初始值
     *
     * @param beiLv
     * @param maiChongDishu
     * @return
     */
    public static String getBasicNumber(String beiLv, String maiChongDishu) {
        String string;
        try {
            BigDecimal a1 = new BigDecimal(beiLv);
            BigDecimal b1 = new BigDecimal(maiChongDishu);
            double result1 = a1.multiply(b1).doubleValue();// 相乘结果
            string = maiChongDishu + "个" + " / " + String.valueOf(result1) + "m³";
        } catch (Exception e) {
            string = "";
            LogUtils.e(e);
        }
        return string;
    }

    /**
     * 判断S芯片固件版本是否高于2.0.2
     *
     * @param gjBanBen
     * @return
     */
    public static boolean isHigherGuJian(String gjBanBen) {
        try {
            String[] strings = gjBanBen.split("\\.");
            if (strings.length >= 1) {
                if (Integer.valueOf(strings[0]) < 2) {
                    return false;
                } else if (Integer.valueOf(strings[0]) == 2) {
                    if (strings.length >= 2) {
                        if (Integer.valueOf(strings[1]) == 0) {
                            if (strings.length >= 3) {
                                if (Integer.valueOf(strings[2]) < 2) {
                                    return false;
                                }
                            } else {
                                return false;
                            }
                        }
                    } else {
                        return false;
                    }

                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 设置倍率+脉冲常数
     *
     * @param MultiplyingPower
     * @return
     */
    public static String getMultiplyingPower(String MultiplyingPower) {
        String result = "";
        try {
            double d1 = 1.0 / Double.valueOf(MultiplyingPower);
            if (d1 < 1) {
                result = MultiplyingPower + " / " + d1;
            } else {
                result = MultiplyingPower + " / " + (int) d1;
            }
        } catch (Exception e) {
            result = "";
            LogUtils.e(e);
        }
        return result;
    }

    /**
     * 脉冲常数
     *
     * @param MultiplyingPower
     * @return
     */
    public static String getPulseConstant(String MultiplyingPower) {
        String result = "";
        Double d = 1.0;
        try {
            result = result + Double.valueOf((d / Double.valueOf(MultiplyingPower))) + "个/m³";
        } catch (Exception e) {
            result = "";
            LogUtils.e(e);
        }
        return result;
    }

    public static String getTimeID() {
        //生成时间戳
        String year;
        String month;
        String day;
        String hours;
        String minutes;
        String seconds;
        Date date1 = new Date();// new Date()为获取当前系统时间，也可使用当前时间戳
        SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        String dateF = df1.format(date1);
        String[] split = dateF.split("-");
        year = split[0].substring(split[0].length() - 2, split[0].length());
        month = split[1].toString();
        day = split[2].toString();

        SimpleDateFormat df2 = new SimpleDateFormat("HH:mm:ss");//设置日期格式
        String dateF1 = df2.format(date1);// new Date()为获取当前系统时间，也可使用当前时间戳
        String[] split1 = dateF1.split(":");
        hours = split1[0].substring(split1[0].length() - 2, split1[0].length());
        minutes = split1[1].toString();
        seconds = split1[2].toString();
        return year + month + day + hours + minutes + seconds;
    }

    /**
     * 通过code获取产品形式，解析时使用
     * 产品形式
     *
     * @param code
     * @returnH
     */

    public static String getCPXSReadStringByCode(long code) {
        String result = "";
        if (code == 0) {
            result = "压力计";
        } else if (code == 1) {
            result = "公用水表";
        } else if (code == 2) {
            result = "待定";
        } else if (code == 3) {
            result = "远传物联网端";
        } else if (code == 4) {
            result = "远传表号接入";
        } else if (code == 5) {
            result = "11位手机号标志";
        } else if (code == 6) {
            result = "流量计";
        } else if (code == 7) {
            result = "管井盖";
        }
        return result;
    }

    /**
     * 通过code获取产品形式，解析时使用
     * 产品形式
     *
     * @param code
     * @return
     */

    public static String getCPXSReadStringByCode(String code) {
        String result = "";
        if ("0".equalsIgnoreCase(code)) {
            result = "压力计";
        } else if ("1".equalsIgnoreCase(code)) {
            result = "公用水表";
        } else if ("2".equalsIgnoreCase(code)) {
            result = "待定";
        } else if ("3".equalsIgnoreCase(code)) {
            result = "远传物联网端";
        } else if ("4".equalsIgnoreCase(code)) {
            result = "远传表号接入";
        } else if ("5".equalsIgnoreCase(code)) {
            result = "11位手机号标志";
        } else if ("6".equalsIgnoreCase(code)) {
            result = "流量计";
        } else if ("7".equalsIgnoreCase(code)) {
            result = "管井盖";
        }
        return result;
    }

    public static String getCGMSReadStringByCode(String code) {
        String result = "";
        if ("1".equalsIgnoreCase(code)) {
            result = "累积脉冲";
        } else if ("2".equalsIgnoreCase(code)) {
            result = "状态切换";
        } else if ("3".equalsIgnoreCase(code)) {
            result = "数字通讯";
        } else if ("4".equalsIgnoreCase(code)) {
            result = "线性传感";
        }
        return result;
    }

    //采集场景
    public static String getCJCJReadStringByCode(int code) {
        String result = "";
        if (code == 0x00) {
            result = "基站公用";
        } else if (code == 0x40) {
            result = "基站民用";
        }
        return result;
    }

    public static String getXDCSReadStringByCode(String code) {
        String result = "";
        if (code.equals("0")) {
            result = "模式A";
        } else if (code.equals("1")) {
            result = "模式B";
        }
        return result;
    }

    public static String getXDCSReadStringByCode02(String code) {
        String result = "";
        if (code.equals("0")) {
            result = "模式A";
        } else if (code.equals("1")) {
            result = "模式B";
        } else if (code.equals("17")) {
            result = "模式16";
        } else if (code.equals("33")) {
            result = "模式32";
        } else if (code.equals("49")) {
            result = "模式48";
        }
        return result;
    }

    public static int getXDCSWriteStringByCode(String code) {
        int result = 0x00;
        if (code.equals("模式A")) {
            result = 0x00;
        } else if (code.equals("模式B")) {
            result = 0x01;
        }
        return result;
    }

    public static int getXDCSWriteStringByCode02(String code) {
        int result = 0x00;
        if (code.equals("模式A")) {
            result = 0x00;
        } else if (code.equals("模式B")) {
            result = 0x01;
        } else if (code.equals("模式16")) {
            result = 0x11;
        } else if (code.equals("模式32")) {
            result = 0x21;
        } else if (code.equals("模式48")) {
            result = 0x31;
        }
        return result;
    }

    //采集场景
    public static final String getCJCJReadStringByCode(String num) {
        if ("0".equalsIgnoreCase(num))
            return "基站公用";
        else if ("64".equalsIgnoreCase(num)) return "基站民用";
        return "";
    }

    //采集场景 上传时使用
    public static final int getCJCJUploadCodeByString(String num) {
        if ("基站公用".equalsIgnoreCase(num)) {
            return 1;
        } else if ("基站民用".equalsIgnoreCase(num)) {
            return 2;
        }
        return 0;
    }

    //通过发送频率获取产品形式
    public static String getCPXSReadStringByFSPL(String pinLv) {
        String result = "";
        if (pinLv == "15分钟上传一次") {
            result = "";
        } else if (pinLv == "24小时上传一次") {
            result = "远传表号接入";
        } else if (pinLv == "48小时上传一次") {
            result = "";
        } else if (pinLv == "72小时上传一次") {
            result = "远传物联网端";
        }
        return result;
    }

    public static String getFSPLReadStringByCode(long code) {
        String result = "";
        if (code == 0x00) {
            result = "15分钟上传一次";
        } else if (code == 0x10) {
            result = "待定";
        } else if (code == 0x20) {
            result = "待定";
        } else if (code == 0x30) {
            result = "24小时上传一次";
        } else if (code == 0x40) {
            result = "48小时上传一次";
        } else if (code == 0x50) {
            result = "待定";
        } else if (code == 0x60) {
            result = "待定";
        } else if (code == 0x70) {
            result = "待定";
        } else if (code == 0x80) {
            result = "72小时上传一次";
        }
        return result;
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

    public static String getFourTZWReadStringByString(String code) {
        String result = "";
        if ("0".equals(code)) {
            result = "1位停止位";
        } else if ("1".equals(code)) {
            result = "2位停止位";
        } else if ("2".equals(code)) {
            result = "1.5位停止位";
        }
        return result;
    }

    public static String getFourJYFSReadStringByString(String code) {
        String result = "";
        if ("0".equals(code)) {
            result = "无校验";
        } else if ("1".equals(code)) {
            result = "偶校验";
        } else if ("2".equals(code)) {
            result = "奇校验";
        }
        return result;
    }

    public static int getFSPLUploadIntByValue(String value) {
        int result = 0;
        if (value.equals("15分钟")) {
            result = 0;
        } else if (value.equals("24小时")) {
            result = 3;
        } else if (value.equals("72小时")) {
            result = 8;
        }
        return result;
    }

    public static String getMKReadStringByCode(long code) {
        String result = "";
        if (code == 0) {
            result = "无效参数";
        } else if (code == 2) {
            result = "脉宽大于等于200ms";
        } else if (code == 3) {
            result = "脉宽大于等于300ms";
        } else if (code == 4) {
            result = "脉宽大于等于400ms";
        } else if (code == 5) {
            result = "脉宽大于等于500ms";
        } else if (code == 6) {
            result = "脉宽大于等于600ms";
        } else if (code == 7) {
            result = "脉宽大于等于700ms";
        } else if (code == 8) {
            result = "脉宽大于等于800ms";
        }
        return result;
    }


    /**
     * 通过value获取序号，上传时使用
     * 产品形式
     *
     * @param value
     * @return
     */
    public static int getCPXSUploadIntByValue(String value) {
        int result = 0;
        if (value.equals("压力计")) {
            result = 0;
        } else if (value.equals("公用水表")) {
            result = 1;
        } else if (value.equals("远传物联网端")) {
            result = 3;
        } else if (value.equals("远传表号接入")) {
            result = 4;
        } else if (value.equals("流量计")) {
            result = 6;
        } else if (value.equals("管井盖")) {
            result = 7;
        } else if (value.equals("远程设备ID接入")) {
            result = 10;
        } else if (value.equals("机械")) {
            result = 11;
        }
        return result;
    }

    /**
     * 传感信号
     *
     * @param tmp
     * @return
     */
    public static String getCGXHReadStringByCode(long tmp) {
        String result = "";
        if (tmp == 0) {
            result = "0-5V";
        } else if (tmp == 1) {
            result = "3EV";
        } else if (tmp == 2) {
            result = "正反脉冲";
        } else if (tmp == 3) {
            result = "2EV";
        } else if (tmp == 4) {
            result = "TTL1";
        } else if (tmp == 5) {
            result = "TTL2";
        } else if (tmp == 6) {
            result = "RS485";
        } else if (tmp == 7) {
            result = "开关";
        } else if (tmp == 8) {
            result = "4-20MA";
        } else if (tmp == 9) {
            result = "单EV";
        } else if (tmp == 10) {
            result = "无磁正反脉冲";
        } else if (tmp == 12) {
            result = "MOD-BUS";
        }
        return result;
    }

    public static String getCGXHReadStringByCode(String tmp) {
        String result = "";
        if ("0".equals(tmp)) {
            result = "0-5V";
        } else if ("1".equals(tmp)) {
            result = "3EV";
        } else if ("2".equals(tmp)) {
            result = "正反脉冲";
        } else if ("3".equals(tmp)) {
            result = "2EV";
        } else if ("4".equals(tmp)) {
            result = "TTL1";
        } else if ("5".equals(tmp)) {
            result = "TTL2";
        } else if ("6".equals(tmp)) {
            result = "RS485";
        } else if ("7".equals(tmp)) {
            result = "开关状态传感";
        } else if ("8".equals(tmp)) {
            result = "4-20MA";
        } else if ("9".equals(tmp)) {
            result = "单EV";
        } else if ("10".equals(tmp)) {
            result = "无磁正反脉冲";
        } else if ("11".equals(tmp)) {
            result = "RS485一对多";
        } else if ("12".equals(tmp)) {
            result = "MOD-BUS";
        }
        return result;
    }

    public static String getCGXHReadStringByString(String tmp) {
        String result = "";
        if (tmp == "0") {
            result = "0-5V";
        } else if (tmp == "1") {
            result = "3EV";
        } else if (tmp == "2") {
            result = "正反脉冲";
        } else if (tmp == "3") {
            result = "2EV";
        } else if (tmp == "4") {
            result = "TTL1";
        } else if (tmp == "5") {
            result = "TTL2";
        } else if (tmp == "6") {
            result = "RS485";
        } else if (tmp == "7") {
            result = "开关";
        } else if (tmp == "8") {
            result = "4-20MA";
        } else if (tmp == "9") {
            result = "单EV";
        } else if (tmp == "10") {
            result = "无磁正反脉冲";
        } else if (tmp == "12") {
            result = "MOD-BUS";
        }
        return result;
    }

    public static String getCSNRReadStringByCode(long tmp) {
        String result = "";
        if (tmp == 0) {
            result = "无效参数";
        } else if (tmp == 1) {
            result = "累积脉冲";
        } else if (tmp == 2) {
            result = "线性积算";
        } else if (tmp == 3) {
            result = "状态判断";
        } else if (tmp == 4) {
            result = "用量";
        } else if (tmp == 5) {
            result = "压力";
        } else if (tmp == 6) {
            result = "水位";
        } else if (tmp == 6) {
            result = "水质";
        }
        return result;
    }

    /**
     * 传感信号
     *
     * @param value
     * @return
     */
    public static int getCGXHUploadIntByValue(String value) {
        int result = 0;
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
        } else if (value.equals("开关")) {
            result = 7;
        } else if (value.equals("4-20MA")) {
            result = 8;
        } else if (value.equals("单EV")) {
            result = 9;
        } else if (value.equals("无磁正反脉冲")) {
            result = 10;
        } else if (value.equals("MOD-BUS")) {
            result = 12;
        }
        return result;
    }


    public static int getCSNRUploadIntByValue(String value) {
        int result = 0;
        if (value.equals("无效参数")) {
            result = 0;
        } else if (value.equals("累积脉冲")) {
            result = 1;
        } else if (value.equals("线性积算")) {
            result = 2;
        } else if (value.equals("状态判断")) {
            result = 3;
        } else if (value.equals("用量")) {
            result = 4;
        } else if (value.equals("压力")) {
            result = 5;
        } else if (value.equals("水位")) {
            result = 6;
        } else if (value.equals("水质")) {
            result = 7;
        }
        return result;
    }

    /**
     * 倍率（读取）
     *
     * @param tmp
     * @return
     */
    public static String getBLReadStringByCode(long tmp) {
        String result = "";
        if (tmp == 0) {
            result = "0.001";
        } else if (tmp == 1) {
            result = "0.01";
        } else if (tmp == 2) {
            result = "0.1";
        } else if (tmp == 3) {
            result = "1";
        } else if (tmp == 4) {
            result = "10";
        } else if (tmp == 5) {
            result = "100";
        }
        return result;
    }

    public static String getBLReadStringByCode(String tmp) {
        String result = "100000";
        if ("0".equals(tmp)) {
            result = "0.001";
        } else if ("1".equals(tmp)) {
            result = "0.01";
        } else if ("2".equals(tmp)) {
            result = "0.1";
        } else if ("3".equals(tmp)) {
            result = "1";
        } else if ("4".equals(tmp)) {
            result = "10";
        } else if ("5".equals(tmp)) {
            result = "100";
        }
        return result;
    }

    /**
     * 倍率解析(上传)
     *
     * @param value
     * @return
     */
    public static int getIntByValueSelection(String value) {
        int result = 0;
        if (value.equals("0.001")) {
            result = 0;
        } else if (value.equals("0.01")) {
            result = 1;
        } else if (value.equals("0.1")) {
            result = 2;
        } else if (value.equals("1")) {
            result = 3;
        } else if (value.equals("10")) {
            result = 4;
        } else if (value.equals("100")) {
            result = 5;
        }
        return result;
    }

    /**
     * 倍率(上传)
     *
     * @param value
     * @return
     */
    public static int getBLUploadIntByValue(String value) {
        int result = 0;
        if (value.equals("0.001")) {
            result = 1;
        } else if (value.equals("0.01")) {
            result = 2;
        } else if (value.equals("0.1")) {
            result = 3;
        } else if (value.equals("1")) {
            result = 4;
        } else if (value.equals("10")) {
            result = 5;
        } else if (value.equals("100")) {
            result = 6;
        }
        return result;
    }

    public static int getBLWriteIntByValue(String value) {
        int result = 0x00;
        if (value.equals("0.001")) {
            result = 0x00;
        } else if (value.equals("0.01")) {
            result = 0x01;
        } else if (value.equals("0.1")) {
            result = 0x02;
        } else if (value.equals("1")) {
            result = 0x03;
        } else if (value.equals("10")) {
            result = 0x04;
        } else if (value.equals("100")) {
            result = 0x05;
        }
        return result;
    }

    /**
     * 工作模式
     *
     * @param tmp
     * @return
     */
    public static String getGZMSReadStringByCode(long tmp) {
        String result = "";
        if (tmp == 0) {
            result = "无效参数";
        } else if (tmp == 1) {
            result = "从机模式";
        } else if (tmp == 2) {
            result = "主机模式";
        }
        return result;
    }


    /**
     * 参数内容
     *
     * @param tmp
     * @return
     */
    public static String getCSLRReadStringByCode(long tmp) {
        String result = "";
        if (tmp == 0) {
            result = "无效参数";
        } else if (tmp == 1) {
            result = "累积脉冲";
        } else if (tmp == 2) {
            result = "线性积算";
        } else if (tmp == 3) {
            result = "状态判断";
        } else if (tmp == 4) {
            result = "用量";
        } else if (tmp == 5) {
            result = "压力";
        } else if (tmp == 6) {
            result = "水位";
        } else if (tmp == 7) {
            result = "水质";
        }
        return result;
    }


    /**
     * 汇中表诊断码
     *
     * @param tmp
     * @return
     */
    public static String getHZBZDMReadStringByCode(long tmp) {
        String result = "";
        if (tmp == 0) {
            result = "正常。";
        } else if (tmp == 1) {
            result = "电池电压低于 3.37V， 需要更换电池。";
        } else if (tmp == 2) {
            result = "空管或者换能器故障无测量信号。";
        } else if (tmp == 3) {
            result = "代码 01 和代码 02 同时发生。";
        } else if (tmp == 4) {
            result = "电池电压低于 3.3V,必须更换电池。";
        } else if (tmp == 5) {
            result = "传感器和换能器之间通讯故障， 无通讯。";
        } else if (tmp == 6) {
            result = "E2PROM 损坏。";
        }
        return result;
    }


    public static String getDYLXReadStringByCode(int i) {
        String result = "";
        int temp = 0b10000000;
        if ((i & temp) == temp) {
            result = "物联电池";
        } else if ((i & (temp >>> 1)) == (temp >>> 1)) {
            result = "物联电池";
        } else if ((i & (temp >>> 2)) == (temp >>> 2)) {
            result = "外接电池";
        }
        return result;
    }

    public static String getDYLXReadStringByCode(String i) {
        String result = "";
        if ("0".equals(i)) {
            result = "物联电池";
        } else if ("1".equals(i)) {
            result = "外接电源";
        }
        return result;
    }

    public static int getDYLXUploadCodeByString(String value) {
        int result = 0;
        if (value.equals("物联电池")) {
            result = 0;
        } else if (value.equals("外接电池")) {
            result = 1;
        } else if (value.equals("第三方电池")) {
            result = 2;
        }
        return result;
    }

    public static String getWLJHReadStringByCode(String value) {
        String result = "";
        if ("0".equals(value)) {
            result = "不带网络反馈";
        } else {
            result = "带网络反馈，重试" + value + "次";
        }
        return result;
    }

    public static String getZBDCZTReadStringByCode(int tmp) {
        String result = "";
        if (tmp == 0) {
            result = "正常";
        } else if (tmp == 1) {
            result = "欠压";
        } else if (tmp == 2) {
            result = "空";
        }
        return result;
    }

    public static String getSBQCZTReadStringByCode(int tmp) {
        String result = "";
        if (tmp == 0) {
            result = "正常";
        } else if (tmp == 1) {
            result = "强磁";
        } else if (tmp == 2) {
            result = "空";
        }
        return result;
    }

    public static String getSBCXZTReadStringByCode(int tmp) {
        String result = "";
        if (tmp == 0) {
            result = "正常";
        } else if (tmp == 1) {
            result = "拆卸";
        } else if (tmp == 2) {
            result = "空";
        }
        return result;
    }

    public static String getSBDLZTReadStringByCode(int tmp) {
        String result = "";
        if (tmp == 0) {
            result = "正常";
        } else if (tmp == 1) {
            result = "倒流";
        } else if (tmp == 2) {
            result = "空";
        }
        return result;
    }

    public static String getDSFDCZTReadStringByCode(int tmp) {
        String result = "";
        if (tmp == 0) {
            result = "正常";
        } else if (tmp == 1) {
            result = "欠压";
        } else if (tmp == 2) {
            result = "空";
        }
        return result;
    }





    /**
     * 是否同一个表号的对象,返回-1证明不存在
     *
     * @param arrayList
     * @param saveDataMeter
     * @return
     */
    public static int containsMeter(ArrayList<SaveDataMeter> arrayList, SaveDataMeter saveDataMeter) {
        int containsIndex = -1;
        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i).getMeterNumber().equals(saveDataMeter.getMeterNumber())) {
                containsIndex = i;
            }
        }
        return containsIndex;
    }

    /**
     * 解析出2进制字符串判断状态，3号表单元
     *
     * @param tmpString
     * @return
     */
    public static String getZTReadStringByCode(String tmpString) {
        StringBuffer result = new StringBuffer();
        result.append("正常，");
        try {
            int tem = (Integer.valueOf(tmpString)) & 0xff;
            byte b = (byte) tem;
            String binary = ByteConvertUtils.conver2BinaryStr(b);
            if (binary.charAt(4) == '1') {
                if (result.toString().contains("正常，")) {
                    result.delete(0, result.length());
                }
                result.append("欠压，");
            }
            if (binary.charAt(2) == '1') {
                if (result.toString().contains("正常，")) {
                    result.delete(0, result.length());
                }
                result.append("倒流，");
            }
            if (binary.charAt(7) == '1') {
                if (result.toString().contains("正常，")) {
                    result.delete(0, result.length());
                }
                result.append("强磁，");
            }
            if (binary.charAt(6) == '1') {
                if (result.toString().contains("正常，")) {
                    result.delete(0, result.length());
                }
                result.append("拆卸，");
            }

        } catch (Exception e) {
            result.append("，");
        }
        return result.toString().substring(0, result.length() - 1);
    }

    public static String getWJ220VZTReadStringByCode(int tmp) {
        String result = "";
        if (tmp == 0) {
            result = "正常";
        } else if (tmp == 1) {
            result = "异常";
        } else if (tmp == 2) {
            result = "空";
        }
        return result;
    }

    //无线频率
    public static final String getWuXianPinLv(String num) {
        if ("0".equalsIgnoreCase(num)) return "433 MHz";
        else if ("1".equalsIgnoreCase(num)) return "470 MHz";
        return "";
    }

    //扩频因子DR
    public static final String getKPYZReadStringByCode(String num) {
        if ("0".equalsIgnoreCase(num))
            return "SF12";
        else if ("1".equalsIgnoreCase(num)) return "SF11";
        else if ("2".equalsIgnoreCase(num)) return "SF10";
        else if ("3".equalsIgnoreCase(num)) return "SF9";
        else if ("4".equalsIgnoreCase(num)) return "SF8";
        else if ("5".equalsIgnoreCase(num)) return "SF7";
        return "";
    }

    //扩频因子DR
    public static final int getKPYZWriteCodeByString(String kuopinyinzi) {
        switch (kuopinyinzi) {
            case "SF12":
                return 0;
            case "SF11":
                return 1;
            case "SF10":
                return 2;
            case "SF9":
                return 3;
            case "SF8":
                return 4;
            case "SF7":
                return 5;
            default:
                return 0;
        }
    }

    //电源类型
    public static final String getDianYuanLeiXin(String dianyuan) {
        if ((Integer.parseInt(dianyuan) & 0B1000_0000) == 0B1000_0000) return "物联电池";
        else return "外接电源";
    }

    /**
     * 板子只有一个字节表示
     * 根据输入的数值返回硬件版本号 返回1+a.bc
     * a为输入数除以100的商
     * b为输入数取100的余数再除以10的商
     * c为输入数取10的余数
     */
    public static final String getHardWareVersion(int number) {
        number &= 0xff;//取两个byte
        int a = 1;
        a += number / 100;//默认值为1 如果数值大于100则取商+1
        int b = (number % 100) / 10;//默认值为0 如果数值大于10则取商
        int c = number % 10;//默认值为0 如果数值大于10则取余
        StringBuilder sb = new StringBuilder();
        sb.append(a);
        sb.append('.');
        sb.append(b);
        sb.append(c);
        return sb.toString();
    }

    public static void main(String[] args) {
//        for (int i = 0; i < 257; i++) {
//            System.out.print(getHardWareVersion(i) + "\n");
//        }
        long maichong = Long.parseLong("1");
        for (int i = 0; i < 3; i++) {
            System.out.println((byte) (maichong & 0xff));
            maichong = maichong >> 8;
        }
    }

    /**
     * 板子只有1个字节表示
     * 根据输入的数值返回硬件版本号 返回1+a.bc
     * a为输入数除以100的商
     * b为输入数取100的余数再除以10的商
     * c为输入数取10的余数
     */
    public static final String getSoftWareVersion(int number) {
        number &= 0xff;//取两个byte
        int a = 1;
        a += number / 100;//默认值为1 如果数值大于100则取商+1
        int b = (number % 100) / 10;//默认值为0 如果数值大于10则取商
        int c = number % 10;//默认值为0 如果数值大于10则取余
        StringBuilder sb = new StringBuilder();
        sb.append(a);
        sb.append('.');
        sb.append(b);
        sb.append(c);
        return sb.toString();
    }

}
