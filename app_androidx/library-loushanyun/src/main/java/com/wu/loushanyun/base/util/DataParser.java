package com.wu.loushanyun.base.util;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

import met.hx.com.librarybase.some_utils.ByteConvertUtils;
import met.hx.com.librarybase.some_utils.LogUtils;

public class DataParser {
    public static final byte[] head = {(byte) 0xfe, (byte) 0xfe, (byte) 0xfe, (byte) 0x68};
    public static final byte[] len = {(byte) 0x00, (byte) 0x00};
    public static final byte[] data = {(byte) 0x00, (byte) 0x00};
    public static final byte[] cmd = {(byte) 0x00};
    public static final byte[] sum = {(byte) 0x00};
    public static final byte[] end = {(byte) 0x16};

    public static final byte[] CMD_CHECK = {(byte) 0xfe, (byte) 0xfe, (byte) 0xfe, (byte) 0x68, (byte) 0x03,
            (byte) 0x00, (byte) 0x02, (byte) 0x03, (byte) 0x00, (byte) 0x08, (byte) 0x16};
    //读取底板的基本信息（命令0x30）
    public static final byte[] CMD_INFO_BASE = {(byte) 0x68, (byte) 0x01, (byte) 0x30, (byte) 0x31, 0x16};
    //读取识别模块所有信息（命令0x11）
    public static final byte[] CMD_INFO_ALL = {(byte) 0x68, (byte) 0x01, (byte) 0x11, (byte) 0x12, (byte) 0x16};
    //读取当前信号强度 RSSI SNR
    public static final byte[] CMD_RSSI_SNR = {(byte) 0x68, (byte) 0x01, (byte) 0x12, (byte) 0x13, (byte) 0x16};
    //读取模块信道（命令0x14）
    public static final byte[] CMD_CHANNEL = {(byte) 0x68, (byte) 0x01, (byte) 0x14, (byte) 0x15, (byte) 0x16};
    //查询激活状态命令（命令0x26）
    public static final byte[] CMD_SYSTEM_STATUS = {(byte) 0x68, (byte) 0x01, (byte) 0x26, (byte) 0x27, (byte) 0x16};
    //查询激活状态命令（命令0x31）
    public static final byte[] CMD_SYSTEM_STATUS_NORMAL = {(byte) 0x68, (byte) 0x01, (byte) 0x31, (byte) 0x32, (byte) 0x16};
    //读取_单具现场参数信息（命令0x33）
    public static final byte[] CMD_METER_INFO_CURRENT = {(byte) 0x68, (byte) 0x02, (byte) 0x33, (byte) 0x06, (byte) 0x3b, (byte) 0x16};
    //读取_单具信息（命令0x22）
    public static final byte[] CMD_METER_INFO = {(byte) 0x68, (byte) 0x01, (byte) 0x22, (byte) 0x23, (byte) 0x16};
    //读取_底板时间（命令0x26）
    public static final byte[] CMD_READ_TIME = {(byte) 0x68, (byte) 0x01, (byte) 0x26, (byte) 0x27, (byte) 0x16};

    public static final HashMap<String, String> check(HashMap<String, String> map, byte[] result) throws Exception {
        map.put(MapParams.表类型, String.valueOf(result[8]));
        map.put(MapParams.表号, String.valueOf(result[9]));
        long tmp = 0;
        for (int i = 7; i > 0; i--) {
            tmp = (tmp * 256) + (long) (result[i + 9] & 0xff);
        }
        map.put(MapParams.设备ID, String.valueOf(tmp));
        tmp = 0;
        for (int i = 6; i > 0; i--) {
            tmp = tmp * 256 + (long) (result[i + 16] & 0xff);
        }
        map.put(MapParams.正脉冲数, String.valueOf(tmp));
        tmp = 0;
        for (int i = 4; i > 0; i--) {
            tmp = tmp * 256 + (long) (result[i + 22] & 0xff);
        }
        map.put(MapParams.反脉冲数, String.valueOf(tmp));
        map.put(MapParams.倍率, getRateFromByte(result[27]));
        tmp = 0;
        for (int i = 4; i > 0; i--) {
            tmp = tmp * 256 + (long) (result[i + 27] & 0xff);
        }
        map.put(MapParams.冻结水量, String.valueOf(tmp));
        tmp = 0;
        for (int i = 2; i > 0; i--) {
            tmp = tmp * 256 + (long) (result[i + 31] & 0xff);
        }
        map.put(MapParams.剩余水量, String.valueOf(tmp));
        map.put(MapParams.状态, String.valueOf(result[34]));
        map.put(MapParams.冻结日, String.valueOf(result[35]));
        tmp = 0;
        for (int i = 6; i > 0; i--) {
            tmp = tmp * 256 + (long) (result[i + 35] & 0xff);
        }
        map.put(MapParams.当前时间, String.valueOf(tmp));
        map.put(MapParams.电压, String.valueOf(result[42]));
        Log.i("test", map.toString());
        return map;
    }

    public static final byte[] getInitCMD(HashMap<String, String> map) {
        Log.i("yunanhao", map.toString());
        byte[] result = new byte[27];
        //4个为固定head
        result[0] = (byte) 0xfe;
        result[1] = (byte) 0xfe;
        result[2] = (byte) 0xfe;
        result[3] = (byte) 0x68;
        //有效长度
        result[4] = (byte) 0x13;
        result[5] = (byte) 0x00;
        //命令
        result[6] = (byte) 0x09;
        //抄表类型
        result[7] = (byte) 0x03;
        //6位ID
        long id = Long.parseLong(map.get(MapParams.设备ID));
        for (int i = 0; i < 6; i++) {
            result[8 + i] = (byte) ((id & ((long) 0xff << (8 * i))) >> 8 * i);
        }
        Log.i("yunanhao", "id=" + id);
        //表号
        result[14] = (byte) Integer.parseInt(map.get(MapParams.表号));
        //0x55指令
        result[15] = (byte) 0x55;
        //设置脉冲底数
        result[16] = (byte) 0x04;
        int rate = Integer.parseInt(map.get(MapParams.脉冲底数));
        for (int i = 0; i < 4; i++) {
            result[17 + i] = (byte) ((rate & (0xff << (8 * i))) >> 8 * i);
        }
        LogUtils.i(MapParams.脉冲底数 + rate);
        //设置倍率
        result[21] = (byte) 0x02;
        result[22] = getByteFromRate(map.get(MapParams.倍率));
        //设置状态
        result[23] = (byte) 0x07;
        result[24] = (byte) 0x00;
        //校验
        byte cs = 0;
        for (int i = 4; i < 25; i++) {
            cs += result[i];
        }
        result[25] = (byte) (cs & 0xff);
        //结束
        result[26] = (byte) 0x16;
        Log.i("yunanhao", "发送：" + DataParser.byteToString(result));
        return result;
    }

    //设置底板时间（命令0x25）
    public static byte[] getByteForSettingTime() {
        //生成时间戳
        int year;
        int month;
        int day;
        int hours;
        int minutes;
        int seconds;
        Date date1 = new Date();// new Date()为获取当前系统时间，也可使用当前时间戳
        SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");//设置日期格式
        String dateF = df1.format(date1);
        String[] split = dateF.split("-");
        year = Integer.valueOf(split[0].substring(split[0].length() - 2, split[0].length()));
        month = Integer.valueOf(split[1]);
        day = Integer.valueOf(split[2]);
        hours = Integer.valueOf(split[3]);
        minutes = Integer.valueOf(split[4]);
        seconds = Integer.valueOf(split[5]);
        byte[] result = new byte[11];
        result[0] = 0x68;
        result[1] = 0x07;
        result[2] = 0x25;
        result[3] = (byte) (year & 0xff);
        result[4] = (byte) (month & 0xff);
        result[5] = (byte) (day & 0xff);
        result[6] = (byte) (hours & 0xff);
        result[7] = (byte) (minutes & 0xff);
        result[8] = (byte) (seconds & 0xff);
        //校验
        byte cs = 0;
        for (int i = 1; i <= 8; i++) {
            cs += result[i];
        }
        result[9] = (byte) (cs & 0xff);
        result[10] = 0x16;
        return result;
    }

    public static String getTimeFromByte(byte[] result) throws Exception {
        //生成时间戳
        String year = String.valueOf(result[4] & 0xff);
        String month = String.valueOf(result[5] & 0xff);
        if (month.length() == 1) {
            month = "0" + month;
        }
        String day = String.valueOf(result[6] & 0xff);
        if (day.length() == 1) {
            day = "0" + day;
        }
        String hours = String.valueOf(result[7] & 0xff);
        if (hours.length() == 1) {
            hours = "0" + hours;
        }
        String minutes = String.valueOf(result[8] & 0xff);
        if (minutes.length() == 1) {
            minutes = "0" + minutes;
        }
        String seconds = String.valueOf(result[9] & 0xff);
        if (seconds.length() == 1) {
            seconds = "0" + seconds;
        }
        return "20" + year + "-" + month + "-" + day + ";" + hours + ":" + minutes + ":" + seconds;
    }

    //设置底板的基本信息
    public static final byte[] getInformationSettingCMD(HashMap<String, String> map) {
        byte[] result = new byte[30];
        result[0] = 0x68;
        result[1] = 0x1a;
        result[2] = 0x20;
        //5位设备ID
        long id = Long.parseLong(map.get(MapParams.设备ID));
        for (int i = 0; i < 5; i++) {
            result[3 + i] = (byte) ((id & ((long) 0xff << (8 * i))) >> 8 * i);
        }
        //采集场景
        result[8] = (byte) Integer.parseInt(map.get(MapParams.采集场景));
        //产品形式
        result[9] = (byte) Integer.parseInt(map.get(MapParams.产品形式));
        //传感信号
        result[10] = (byte) Integer.parseInt(map.get(MapParams.传感信号));
        //保留字节
        result[11] = (byte) Integer.parseInt(map.get(MapParams.保留字节));
        //电源类型
        result[12] = (byte) Integer.parseInt(map.get(MapParams.电源类型));
        //出厂时间 年
        result[13] = (byte) Integer.parseInt(map.get(MapParams.出厂时间_年));
        //出厂时间 月
        result[14] = (byte) Integer.parseInt(map.get(MapParams.出厂时间_月));
        //出厂时间 日
        result[15] = (byte) Integer.parseInt(map.get(MapParams.出厂时间_日));
        //工作模式
        result[16] = (byte) Integer.parseInt(map.get(MapParams.工作模式));
        //信号类型
        result[17] = (byte) Integer.parseInt(map.get(MapParams.信号类型));
        //参数内容
        result[18] = (byte) Integer.parseInt(map.get(MapParams.参数内容));
        //脉宽
        result[19] = (byte) Integer.parseInt(map.get(MapParams.脉宽));
        //压力值标定 初始值
        result[20] = (byte) Integer.parseInt(map.get(MapParams.压力值标定_初始值));
        //压力值标定 最大值
        result[21] = (byte) Integer.parseInt(map.get(MapParams.压力值标定_最大值));
        //底板状态 设备强磁状态
        result[22] = (byte) Integer.parseInt(map.get(MapParams.底板状态_设备强磁状态));
        //底板状态 设备拆卸状态
        result[23] = (byte) Integer.parseInt(map.get(MapParams.底板状态_设备拆卸状态));
        //底板状态 水表倒流状态
        result[24] = (byte) Integer.parseInt(map.get(MapParams.底板状态_水表倒流状态));
        //底板状态 自备电池状态
        result[25] = (byte) Integer.parseInt(map.get(MapParams.底板状态_自备电池状态));
        //底板状态 第三方电池状态
        result[26] = (byte) Integer.parseInt(map.get(MapParams.底板状态_第三方电池状态));
        //底板状态 外接电源220V状态
        result[27] = (byte) Integer.parseInt(map.get(MapParams.底板状态_外接电源220V状态));
        //校验
        byte cs = 0;
        for (int i = 1; i < 28; i++) {
            cs += result[i];
        }
        result[28] = (byte) (cs & 0xff);
        //结束
        result[29] = (byte) 0x16;
        return result;
    }

    //设置_系统激活或停用（命令0x23）
    public static final byte[] getSystemStatusSettingCMD(boolean isActive) {
        byte[] result = {0x68, 0x02, 0x23, 0x00, 0x25, 0x16};//系统停用
        if (isActive) {
            //系统激活
            result[3] = 0x01;
            result[4] = 0x26;
        }
        return result;
    }

    //设置_手动进入休眠（命令0x24）
    public static final byte[] getSystemSleepCMD() {
        byte[] result = {0x68, 0x01, 0x24, 0x25, 0x16};
        return result;
    }

    public static final byte[] getZhiShuData(HashMap<String, String> map) {
        byte[] result = new byte[17];
        result[0] = 0x68;
        result[1] = 0x0d;
        result[2] = 0x28;
        result[3] = (byte) (Integer.valueOf(map.get(MapParams.HUB号)) & 0xff);
        result[4] = (byte) (Integer.valueOf(map.get(MapParams.表号)) & 0xff);
        long isn = Long.parseLong(map.get(MapParams.设备ID));
        for (int i = 0; i < 5; i++) {
            result[i + 5] = (byte) (isn & 0xff);
            isn = isn >> 8;
        }
        result[10] = (byte) (Integer.valueOf(map.get(MapParams.倍率)) & 0xff);
        long maichong = Long.parseLong(map.get(MapParams.脉冲数));
        for (int i = 0; i < 3; i++) {
            result[i + 11] = (byte) (maichong & 0xff);
            maichong = maichong >> 8;
        }
        result[14] = (byte) (Integer.valueOf(map.get(MapParams.状态)) & 0xff);
        byte cs = 0;
        for (int i = 1; i < 15; i++) {
            cs += result[i];
        }
        result[15] = (byte) (cs & 0xff);
        result[16] = 0x16;
        return result;
    }

    //读取_基本信息 解析数据 0x30
    public static final HashMap<String, String> getInformationBase(byte[] result) {
        HashMap<String, String> map = new HashMap<>();
        long tmp = 0;
        for (int i = 5; i > 0; i--) {
            tmp = (tmp * 256) + (long) (result[i + 3] & 0xff);
        }
        map.put(MapParams.设备ID, String.valueOf(tmp));
        map.put(MapParams.采集场景, String.valueOf(result[9] & 0xff));
        map.put(MapParams.产品形式, String.valueOf(result[10] & 0xff));
        map.put(MapParams.传感信号, String.valueOf(result[11] & 0xff));
        map.put(MapParams.保留字节, String.valueOf(result[12] & 0xff));
        map.put(MapParams.电源类型, String.valueOf(result[13] & 0xff));
        map.put(MapParams.出厂时间_年, String.valueOf(result[14] & 0xff));
        map.put(MapParams.出厂时间_月, String.valueOf(result[15] & 0xff));
        map.put(MapParams.出厂时间_日, String.valueOf(result[16] & 0xff));
        map.put(MapParams.硬件版本, String.valueOf(result[17] & 0xff));
        map.put(MapParams.软件版本, String.valueOf(result[18] & 0xff));
        map.put(MapParams.工作模式, String.valueOf(result[19] & 0xff));
        map.put(MapParams.信号类型, String.valueOf(result[20] & 0xff));
        map.put(MapParams.参数内容, String.valueOf(result[21] & 0xff));
        map.put(MapParams.脉宽, String.valueOf(result[22] & 0xff));
        map.put(MapParams.压力值标定_初始值, String.valueOf(result[23] & 0xff));
        map.put(MapParams.压力值标定_最大值, String.valueOf(result[24] & 0xff));
        map.put(MapParams.底板状态_设备强磁状态, String.valueOf(result[25] & 0xff));
        map.put(MapParams.底板状态_设备拆卸状态, String.valueOf(result[26] & 0xff));
        map.put(MapParams.底板状态_水表倒流状态, String.valueOf(result[27] & 0xff));
        map.put(MapParams.底板状态_自备电池状态, String.valueOf(result[28] & 0xff));
        map.put(MapParams.底板状态_第三方电池状态, String.valueOf(result[29] & 0xff));
        map.put(MapParams.底板状态_外接电源220V状态, String.valueOf(result[30] & 0xff));
        return map;
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
        if (result[1] != 0x15) {//远程水表物联网有效长度为15 下同
            map.put(MapParams.工作模式, String.valueOf(result[i++] & 0xff));
        }
        map.put(MapParams.发送频率, String.valueOf(result[i++] & 0xff));
        map.put(MapParams.无线频率, String.valueOf(result[i++] & 0xff));
        map.put(MapParams.发送功率, String.valueOf(result[i++] & 0xff));
        map.put(MapParams.出厂时间_年, String.valueOf(result[i++] & 0xff));
        map.put(MapParams.出厂时间_月, String.valueOf(result[i++] & 0xff));
        map.put(MapParams.出厂时间_日, String.valueOf(result[i++] & 0xff));
        map.put(MapParams.网络交互, String.valueOf(result[i++] & 0xff));
        map.put(MapParams.硬件版本, String.valueOf(result[i++] & 0xff));
        map.put(MapParams.软件版本, String.valueOf(result[i++] & 0xff));
        if (result[1] != 0x15) {
            map.put(MapParams.厂家标识, String.valueOf(((result[i++] & 0xff) + ((result[i++] & 0xff) << 8)) & 0xffff));
            map.put(MapParams.扩频因子, String.valueOf(result[i++] & 0xff));
            map.put(MapParams.信道参数, String.valueOf(result[i++] & 0xff));
        }
        return map;
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
        }else if (result.length == 27) {
            type = 5;//4号模组模拟信号
        }else if(result.length == 28){
            type = 6;//Lorawan
        }
        return type;
    }

    // 68 01 41 42 16
    public static final byte[] openJiZhongQi() {
        byte[] result = new byte[5];
        result[0] = 0x68;
        result[1] = 0x01;
        result[2] = 0x41;
        result[3] = 0x42;
        result[4] = 0x16;
        return result;
    }

    public static final byte[] closeJiZhongQi() {
        byte[] result = new byte[5];
        result[0] = 0x68;
        result[1] = 0x01;
        result[2] = 0x42;
        result[3] = 0x43;
        result[4] = 0x16;
        return result;
    }

    //表信息初始化设置（命令0x21）
    public static final byte[] getBiaoXinxiChuShiHuaCMD(long id, long maichong, String beilv) {
        Log.i("yunanhao", "id:" + id + ",maichong" + maichong + ",beilv" + beilv);
        byte[] result = new byte[17];
        result[0] = 0x68;
        result[1] = 0x0d;
        result[2] = 0x21;
        result[3] = 0x00;
        for (int i = 0; i < 5; i++) {
            result[4 + i] = (byte) ((id & ((long) 0xff << (8 * i))) >> 8 * i);
        }
        if (maichong < 0) {
            result[9] = 0x01;
        } else {
            result[9] = 0x00;
        }
        result[10] = (byte) (maichong & 0xff);
        result[11] = (byte) ((maichong & 0xff00) >> 8);
        result[12] = (byte) ((maichong & 0xff0000) >> 16);
        result[13] = (byte) ((maichong & 0xff000000) >> 24);
        switch (beilv) {
            case "0.001":
                result[14] = 0;
                break;
            case "0.01":
                result[14] = 1;
                break;
            case "0.1":
                result[14] = 2;
                break;
            case "1":
                result[14] = 3;
                break;
            case "10":
                result[14] = 4;
                break;
            case "100":
                result[14] = 5;
                break;
        }
        result[15] = (byte) 0x00;
        //校验
        byte cs = 0;
        for (int i = 1; i < 15; i++) {
            cs += result[i];
        }
        result[15] = (byte) (cs & 0xff);
        result[16] = 0x16;
        return result;
    }

    //2号设置出厂状态  0x20命令
    public static byte[] getFactoryParams(String cgxh, byte chaiXie, byte qiangCi,
                                          byte chuanGanQiZhuangTai, byte daoLiu,
                                          byte faMenZhuangTai, byte faSongPinLv, int cjbs_wlwd,
                                          String spreadingFactor, String channel) {
        //生成时间戳
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        String time = df.format(new Date());// new Date()为获取当前系统时间，也可使用当前时间戳
        String[] split = time.split("-");
        String year = split[0].substring(split[0].length() - 2, split[0].length());
        String month = split[1].toString();
        String day = split[2].toString();
        //开始封装设置数据
        byte[] d = new byte[21];
        d[0] = (byte) 0x68;
        d[1] = (byte) 0x11;//有效数据
        d[2] = (byte) 0x20;//命令
        d[3] = (byte) 0x03;//产品形式,物联网端
        d[4] = (byte) 0x00;//信号类型，全部是模拟信号
        if (cgxh.equals("单EV")) {
            d[5] = (byte) 0x09;//传感信号 。针对2ev 0x03，无磁正反脉冲0x0a
        } else if (cgxh.equals("2EV")) {
            d[5] = (byte) 0x03;//传感信号 。针对2ev 0x03，无磁正反脉冲0x0a
        } else if (cgxh.equals("3EV")) {
            d[5] = (byte) 0x01;//传感信号 。针对2ev 0x03，无磁正反脉冲0x0a
        } else if (cgxh.equals("无磁正反脉冲")) {
            d[5] = (byte) 0x0a;//传感信号 。针对2ev 0x03，无磁正反脉冲0x0a
        } else {
            d[5] = (byte) 0x05;//传感信号 。针对2ev 0x03，无磁正反脉冲0x0a
        }
        d[6] = chaiXie;//拆卸
        d[7] = qiangCi;//强磁
        d[8] = chuanGanQiZhuangTai;//传感器状态
        d[9] = daoLiu;//倒流
        d[10] = faMenZhuangTai;//阀门状态
        d[11] = faSongPinLv; // 发送频率 固定 24小时上传一次

        d[12] = (byte) Integer.parseInt(year);//出厂时间设置
        d[13] = (byte) Integer.parseInt(month);
        d[14] = (byte) Integer.parseInt(day);
        d[15] = (byte) (cjbs_wlwd & 0xff);
        d[16] = (byte) ((cjbs_wlwd & 0xff00) >> 8);
        d[17] = (byte) LouShanYunUtils.getKPYZWriteCodeByString(spreadingFactor);//扩频因子
        d[18] = (byte) LouShanYunUtils.getXDCSWriteStringByCode02(channel);//信道
        byte cs = 0;
        for (int i = 1; i < 19; i++) {
            cs += d[i];
        }
        d[19] = cs;//校验和
        d[20] = (byte) 0x16;
        return d;
    }


    //2号休眠 0x25命令
    public static final byte[] close() {
        byte[] result = new byte[6];
        result[0] = 0x68;
        result[1] = 0x02;
        result[2] = 0x25;
        result[3] = 0x00;
        result[4] = 0x27;
        result[5] = 0x16;
        return result;
    }





    //现场初始化操作 设置_附加参数（命令0x21）
    public static final byte[] getDiBanBiaoJiChuShiHuaCMD(HashMap<String, String> map) {
        byte[] result = new byte[23];
        result[0] = 0x68;
        result[1] = 0x13;
        result[2] = 0x21;
        result[3] = (byte) Integer.parseInt(map.get(MapParams.总线起止表号_起));
        result[4] = (byte) Integer.parseInt(map.get(MapParams.总线起止表号_止));
        result[5] = (byte) Integer.parseInt(map.get(MapParams.仪表通信号));
        result[6] = (byte) Integer.parseInt(map.get(MapParams.初始化表计状态));
        result[7] = (byte) Integer.parseInt(map.get(MapParams.倍率));
        long temp = Long.parseLong(map.get(MapParams.安装脉冲底数));
        if (temp < 0) {
            result[8] = 0x01;
        } else {
            result[8] = 0x00;
        }
        result[9] = (byte) (temp & 0xff);
        result[10] = (byte) ((temp & 0xff00) >> 8);
        result[11] = (byte) ((temp & 0xff0000) >> 16);
        result[12] = (byte) ((temp & 0xff000000) >> 24);

        result[13] = (byte) Integer.parseInt(map.get(MapParams.口径));
        result[14] = (byte) Integer.parseInt(map.get(MapParams.发送频率));
        result[15] = (byte) Integer.parseInt(map.get(MapParams.保留字节));
        long id = Long.parseLong(map.get(MapParams.设备ID));
        for (int i = 0; i < 5; i++) {
            result[16 + i] = (byte) ((id & ((long) 0xff << (8 * i))) >> 8 * i);
        }
        //校验
        byte cs = 0;
        for (int i = 1; i < 21; i++) {
            cs += result[i];
        }
        result[21] = (byte) (cs & 0xff);
        result[22] = 0x16;
        return result;
    }

    //现场初始化操作 读取_附加参数的命令（命令0x31）
    public static final byte[] getDiBanBiaoJiDuQuCMD() {
        byte[] result = {0x68, 0x01, 0x31, 0x32, 0x16};
        return result;
    }

    //现场初始化操作 读取_附加参数的命令（命令0x31）  底板表计信息
    public static final HashMap<String, String> getDiBanBiaoJiInfo(byte[] result) {
        HashMap<String, String> map = new HashMap<>();
        map.put(MapParams.总线起止表号_起, String.valueOf(result[4] & 0xff));
        map.put(MapParams.总线起止表号_止, String.valueOf(result[5] & 0xff));
        map.put(MapParams.仪表通信号, String.valueOf(result[6] & 0xff));
        map.put(MapParams.当前表计状态, String.valueOf(result[7] & 0xff));
        map.put(MapParams.倍率, String.valueOf(result[8] & 0xff));
        long temp = 0L;
        for (int i = 4; i > 0; i--) {
            temp = temp * 256 + (long) (result[i + 9] & 0xff);
        }
        if (((result[9] & 0xff) != 0)) {
            temp = -temp;
        }
        map.put(MapParams.安装脉冲底数, String.valueOf(temp));
        map.put(MapParams.口径, String.valueOf(result[14] & 0xff));
        map.put(MapParams.发送频率, String.valueOf(result[15] & 0xff));
        map.put(MapParams.保留字节, String.valueOf(result[16] & 0xff));
        temp = 0L;
        for (int i = 5; i > 0; i--) {
            temp = temp * 256 + (long) (result[i + 16] & 0xff);
        }
        map.put(MapParams.设备ID, String.valueOf(temp));
        map.put(MapParams.时间_秒, String.valueOf(result[22] & 0xff));
        map.put(MapParams.时间_分, String.valueOf(result[23] & 0xff));
        map.put(MapParams.系统状态, String.valueOf(result[24] & 0xff));
        return map;
    }

    // 测试_基站连通状态（命令0x40）
    public static final byte[] getCeShiJiZhanLianTongCMD(String phone) {
        byte[] result = new byte[16];
        result[0] = 0x68;
        result[1] = 0x0c;
        result[2] = 0x40;
        for (int i = 0; i < phone.length(); i++) {
            result[i + 3] = (byte) Integer.parseInt(String.valueOf(phone.charAt(i)));
        }
        //校验
        byte cs = 0;
        for (int i = 1; i < 14; i++) {
            cs += result[i];
        }
        result[14] = (byte) (cs & 0xff);
        result[15] = 0x16;
        return result;
    }

    // 测试_安装完成状态（命令0x42）
    public static final byte[] getCeShiAnZhuangWanChenCMD() {
        byte[] result = {0x68, 0x01, 0x42, 0x43, 0x16};
        return result;
    }

    //读取_单表计信息（命令0x32）
    public static final byte[] getDanBiaoDuQuXinXiCMD(int biaohao) {
        byte[] result = {0x68, 0x02, 0x32, (byte) (biaohao & 0xff), (byte) (0x34 + (byte) (biaohao & 0xff)), 0x16};
        return result;
    }

    //读取_单表计信息（命令0x32）
    public static final HashMap<String, String> getDanBiaoDuQuXinXi(byte[] result, double softVersion) {
        HashMap<String, String> map = new HashMap<>();
        map.put(MapParams.表号, String.valueOf(result[4] & 0xff));
        long temp = 0;
        for (int i = 5; i > 0; i--) {
            temp = temp * 256 + (long) (result[i + 4] & 0xff);
        }
        map.put(MapParams.设备ID, String.valueOf(temp));
        map.put(MapParams.倍率, String.valueOf(result[10] & 0xff));
        temp = 0L;
        for (int i = 4; i > 0; i--) {
            temp = temp * 256 + (long) (result[i + 11] & 0xff);
        }
        if (((result[11] & 0xff) != 0)) {
            temp = -temp;
        }
        map.put(MapParams.脉冲数, String.valueOf(temp));
        map.put(MapParams.表强磁状态, String.valueOf(result[16] & 0xff));
        map.put(MapParams.表电池状态, String.valueOf(result[17] & 0xff));
        map.put(MapParams.表流向状态, String.valueOf(result[18] & 0xff));
        map.put(MapParams.表拆卸状态, String.valueOf(result[19] & 0xff));
        map.put(MapParams.汇中表诊断码, String.valueOf(result[20] & 0xff));
        if (softVersion >= 1.04) {
            map.put(MapParams.HUB号, String.valueOf(result[21] & 0xff));
        } else {
            map.put(MapParams.HUB号, "-1");
        }
        return map;
    }


    // 读取_单具现场参数信息（命令0x33） 解析返回
    public static final HashMap<String, String> getDanBiaoXianChangXinXi(byte[] result) {
        HashMap<String, String> map = new HashMap<>();
        map.put(MapParams.表号, String.valueOf(result[4] & 0xff));
        long temp = 0;
        for (int i = 5; i > 0; i--) {
            temp = temp * 256 + (long) (result[i + 4] & 0xff);
        }
        map.put(MapParams.设备ID, String.valueOf(temp));
        map.put(MapParams.倍率, String.valueOf(result[10] & 0xff));
        temp = 0L;
        for (int i = 4; i > 0; i--) {
            temp = temp * 256 + (long) (result[i + 11] & 0xff);
        }
        if (((result[11] & 0xff) != 0)) {
            temp = -temp;
        }
        map.put(MapParams.脉冲数, String.valueOf(temp));
        map.put(MapParams.表强磁状态, String.valueOf(result[16] & 0xff));
        map.put(MapParams.表电池状态, String.valueOf(result[17] & 0xff));
        map.put(MapParams.表流向状态, String.valueOf(result[18] & 0xff));
        map.put(MapParams.表拆卸状态, String.valueOf(result[19] & 0xff));
        map.put(MapParams.汇中表诊断码, String.valueOf(result[20] & 0xff));
        return map;
    }

    //设置信道（命令0x08）为true则设置为信道A否则设置为信道B
    public static final byte[] setXinDaoCanShu(boolean isXinDaoA) {
        if (isXinDaoA) {
            byte[] result = {0x68, 0x02, 0x08, 0x00, 0x0a, 0x16};
            return result;
        } else {
            byte[] result = {0x68, 0x02, 0x08, 0x01, 0x0b, 0x16};
            return result;
        }
    }

    //设置信道（命令0x05）为true则设置为信道A否则设置为信道B,物联网端设置信道
    public static final byte[] setXinDaoCanShuWu(boolean isXinDaoA) {
        if (isXinDaoA) {
            byte[] result = {0x68, 0x02, 0x05, 0x00, 0x07, 0x16};
            return result;
        } else {
            byte[] result = {0x68, 0x02, 0x05, 0x01, 0x08, 0x16};
            return result;
        }
    }

    // 设置_扩频因子（命令0x07）
    public static final byte[] setKuoPinYinZi(int kuopinyinzi) {
        byte[] result = {0x68, 0x02, 0x07, (byte) (kuopinyinzi & 0xff), (byte) (0x09 + (byte) (kuopinyinzi & 0xff)), 0x16};
        return result;
    }

    //设置_发送功率（命令0x02）
    public static final byte[] setFaSongGongLv(int fasonggonglv) {
        byte[] result = {0x68, 0x02, 0x02, (byte) (fasonggonglv & 0xff), (byte) (0x04 + (byte) (fasonggonglv & 0xff)), 0x16};
        return result;
    }

    //读取表配置信息 （命令0x0b）
    public static final byte[] getMeterConfigInfoCMD(int meter_type, int meter_code) {
        byte[] result = {(byte) 0xfe, (byte) 0xfe, (byte) 0xfe, (byte) 0x68, (byte) 0x03, (byte) 0x00, (byte) 0x0b, (byte) (meter_type & 0xff), (byte) (meter_code & 0xff), (byte) 0x12, (byte) 0x16};
        result[result.length - 2] = 0;
        result[result.length - 2] += result[4];
        result[result.length - 2] += result[5];
        result[result.length - 2] += result[6];
        result[result.length - 2] += result[7];
        result[result.length - 2] += result[8];
        return result;
    }

    //读取表配置信息 （命令0x0b）解析返回结果
    public static final HashMap<String, String> getMeterConfigInfo(byte[] result) {
        HashMap<String, String> map = new HashMap<>();
        map.put(MapParams.表号, String.valueOf(result[8] & 0xff));
        long temp = 0;
        for (int i = 7; i > 0; i--) {
            temp = temp * 256 + (long) (result[8 + i] & 0xff);
        }
        map.put(MapParams.设备ID, String.valueOf(temp));
        map.put(MapParams.倍率, String.valueOf(result[16] & 0xff));
        map.put(MapParams.状态, String.valueOf(result[17] & 0xff));
        map.put(MapParams.传感类型, String.valueOf(result[18] & 0xff));
        map.put(MapParams.软件版本, String.valueOf(result[19] & 0xff));
        temp = 0;
        for (int i = 6; i > 0; i--) {
            temp = temp * 256 + (long) (result[19 + i] & 0xff);
        }
        map.put(MapParams.企业代码, String.valueOf(temp));
        map.put(MapParams.生产时间_年, String.valueOf(result[26] & 0xff));
        map.put(MapParams.生产时间_月, String.valueOf(result[27] & 0xff));
        map.put(MapParams.生产时间_日, String.valueOf(result[28] & 0xff));
        return map;
    }

    //2号模组 远传物联网端0x22
    public static final HashMap<String, String> getMoudleNo2(byte[] result) {
        HashMap<String, String> hashMap = new HashMap<>();
        if (result[3] == 0) {
            long tmp;
            hashMap.put(MapParams.产品形式, String.valueOf(result[4] & 0xff));
            hashMap.put(MapParams.信号类型, String.valueOf(result[5] & 0xff));
            hashMap.put(MapParams.传感信号, String.valueOf(result[6] & 0xff));
            hashMap.put(MapParams.表拆卸状态, String.valueOf(result[7] & 0xff));
            hashMap.put(MapParams.表强磁状态, String.valueOf(result[8] & 0xff));
            hashMap.put(MapParams.表流向状态, String.valueOf(result[9] & 0xff));
            hashMap.put(MapParams.发送频率, String.valueOf(result[10] & 0xff));
            hashMap.put(MapParams.出厂时间_年, String.valueOf(result[11] & 0xff));
            hashMap.put(MapParams.出厂时间_月, String.valueOf(result[12] & 0xff));
            hashMap.put(MapParams.出厂时间_日, String.valueOf(result[13] & 0xff));
            hashMap.put(MapParams.厂家标识, String.valueOf(((result[14] & 0xff) + ((result[15] & 0xff) << 8))));
            hashMap.put(MapParams.扩频因子, String.valueOf(result[16] & 0xff));
            hashMap.put(MapParams.信道参数, String.valueOf(result[17] & 0xff));
            tmp = 0;
            for (int i = 5; i > 0; i--) {
                tmp = (tmp * 256) + (result[i + 18] & 0xff);
            }
            hashMap.put(MapParams.设备ID, String.valueOf(tmp));
            tmp = 0;
            for (int i = 4; i > 0; i--) {
                tmp = (tmp * 256) + (result[i + 24] & 0xff);
            }
            if (((result[24] & 0xff) != 0)) {
                tmp = -tmp;
            }
            hashMap.put(MapParams.当前脉冲读数, String.valueOf(tmp));
            hashMap.put(MapParams.倍率, String.valueOf(result[29] & 0xff));
            hashMap.put(MapParams.表电池状态, String.valueOf(result[30] & 0xff));
            StringBuilder sb = new StringBuilder();
            String temp;
            int i;
            for (i = 31; i < 39; i++) {
                temp = Integer.toHexString(result[i] & 0xff);
                if (temp.length() == 1) {
                    sb.append(0);
                }
                sb.append(temp);
            }
            hashMap.put(MapParams.物联SN, sb.toString().toUpperCase());
            hashMap.put(MapParams.无线频率, String.valueOf(result[39] & 0xff));
            hashMap.put(MapParams.硬件版本, String.valueOf(result[40] & 0xff));
            hashMap.put(MapParams.软件版本, String.valueOf(result[41] & 0xff));
        }
        return hashMap;
    }

    public static final String getRateFromByte(byte b) {
        if (b == 0)
            return "0.001";
        if (b == 1)
            return "0.01";
        if (b == 2)
            return "0.1";
        if (b == 3)
            return "1";
        if (b == 4)
            return "10";
        if (b == 5)
            return "100";
        if (b == 6)
            return "1000";
        if (b == 7)
            return "0.0001";
        return null;
    }

    public static final byte getByteFromRate(String rate) {
        if ("0.0001".equals(rate))
            return 0x07;
        if ("0.001".equals(rate))
            return 0x00;
        if ("0.01".equals(rate))
            return 0x01;
        if ("0.1".equals(rate))
            return 0x02;
        if ("1".equals(rate))
            return 0x03;
        if ("10".equals(rate))
            return 0x04;
        if ("100".equals(rate))
            return 0x05;
        if ("1000".equals(rate))
            return 0x06;
        return 0x00;
    }

    public static final String byteToString(byte result) {
        StringBuilder sb = new StringBuilder();
        String temp;

            temp = Integer.toHexString(result & 0xff);
            if (temp.length() == 1) {
                sb.append(0);
            }
            sb.append(temp);

        return sb.toString();
    }


    public static final String byteToString(byte[] result) {
        StringBuilder sb = new StringBuilder();
        String temp;
        for (int i = 0; i < result.length; i++) {
            temp = Integer.toHexString(result[i] & 0xff);
            sb.append("(");
            sb.append(i);
            sb.append(")");
            if (temp.length() == 1) {
                sb.append(0);
            }
            sb.append(temp);
            if (i + 1 < result.length) {
                sb.append(' ');
            }
        }
        return sb.toString();
    }

    public static final String getJSONObject(HashMap<String, String> map) {
        Set<String> keyset = map.keySet();
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (String key : keyset) {
            sb.append("\"");
            sb.append(key);
            sb.append("\":");
            sb.append("\"");
            sb.append(map.get(key));
            sb.append("\",");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append("}");
        return sb.toString();
    }

    /**
     * 通过去掉回复命令的 协议头:68,有效长度，命令字，错误码：00 和 校验和，结束符:16
     * 得到16进制的数据解析出ASCII字符串
     *
     * @param result
     * @return
     */
    public static String getASCIIbyByte(byte[] result) {
        return ByteConvertUtils.getASCIIbyByte(result, 4, 2);
    }

    /**
     * 0x50命令 通过单片机与s芯片交互
     *
     * @param writeStr
     * @return
     */
    public static byte[] getBytesByASCII(String writeStr) {
        if (!writeStr.contains("\r\n")) {
            writeStr += "\r\n";
        }
        byte[] bytes = ByteConvertUtils.convertHexStringToBytes(ByteConvertUtils.convertASCIITo16(writeStr));
        byte[] writeBytes = new byte[bytes.length + 5];
        writeBytes[0] = 0x68;
        writeBytes[1] = (byte) ((bytes.length + 1) & 0xff);
        writeBytes[2] = 0x50;
        for (int i = 0; i < bytes.length; i++) {
            writeBytes[i + 3] = bytes[i];
        }
        byte cs = 0;
        for (int i = 1; i < writeBytes.length - 2; i++) {
            cs += writeBytes[i];
        }
        writeBytes[writeBytes.length - 2] = (byte) (cs & 0xff);
        writeBytes[writeBytes.length - 1] = 0x16;
        return writeBytes;
    }

    /**
     * 0x60命令 通过单片机与TTL模块交互
     *
     * @param writeStr
     * @return
     */
    public static byte[] getTTLBytesByASCII(String writeStr) {
        if (!writeStr.contains("\r\n")) {
            writeStr += "\r\n";
        }
        byte[] bytes = ByteConvertUtils.convertHexStringToBytes(ByteConvertUtils.convertASCIITo16(writeStr));
        byte[] writeBytes = new byte[bytes.length + 5];
        writeBytes[0] = 0x68;
        writeBytes[1] = (byte) ((bytes.length + 1) & 0xff);
        writeBytes[2] = 0x60;
        for (int i = 0; i < bytes.length; i++) {
            writeBytes[i + 3] = bytes[i];
        }
        byte cs = 0;
        for (int i = 1; i < writeBytes.length - 2; i++) {
            cs += writeBytes[i];
        }
        writeBytes[writeBytes.length - 2] = (byte) (cs & 0xff);
        writeBytes[writeBytes.length - 1] = 0x16;
        return writeBytes;
    }
}
