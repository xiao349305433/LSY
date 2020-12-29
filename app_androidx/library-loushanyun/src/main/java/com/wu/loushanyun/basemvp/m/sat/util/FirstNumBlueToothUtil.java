package com.wu.loushanyun.basemvp.m.sat.util;

import com.wu.loushanyun.base.util.MapParams;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 4号模组数字通讯蓝牙协议指令集
 */
public class FirstNumBlueToothUtil {
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

    //现场初始化操作 读取_附加参数的命令（命令0x31）  底板表计信息
    public static final HashMap<String, String> getDiBanBiaoJiInfo(byte[] result) {
        HashMap<String, String> map = new HashMap<>();
        map.put(MapParams.总线起止表号_起, String.valueOf(result[4] & 0xff));
        map.put(MapParams.总线起止表号_止, String.valueOf(result[5] & 0xff));
//        map.put(MapParams.仪表通信号, String.valueOf(result[6] & 0xff));
//        map.put(MapParams.当前表计状态, String.valueOf(result[7] & 0xff));
//        map.put(MapParams.倍率, String.valueOf(result[8] & 0xff));
//        long temp = 0L;
//        for (int i = 4; i > 0; i--) {
//            temp = temp * 256 + (long) (result[i + 9] & 0xff);
//        }
//        if (((result[9] & 0xff) != 0)) {
//            temp = -temp;
//        }
//        map.put(MapParams.安装脉冲底数, String.valueOf(temp));
//        map.put(MapParams.口径, String.valueOf(result[14] & 0xff));
//        map.put(MapParams.发送频率, String.valueOf(result[15] & 0xff));
//        map.put(MapParams.保留字节, String.valueOf(result[16] & 0xff));
//        temp = 0L;
//        for (int i = 5; i > 0; i--) {
//            temp = temp * 256 + (long) (result[i + 16] & 0xff);
//        }
//        map.put(MapParams.设备ID, String.valueOf(temp));
//        map.put(MapParams.时间_秒, String.valueOf(result[22] & 0xff));
//        map.put(MapParams.时间_分, String.valueOf(result[23] & 0xff));
        map.put(MapParams.系统状态, String.valueOf(result[24] & 0xff));
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

    //读取_基本信息 解析数据 0x30
    public static HashMap<String, String> getInformationBase(byte[] result) {
        HashMap<String, String> map = new HashMap<>();
        long tmp = 0;
        for (int i = 5; i > 0; i--) {
            tmp = (tmp * 256) + (long) (result[i + 3] & 0xff);
        }
        map.put(MapParams.设备ID, String.valueOf(tmp));
        map.put(MapParams.底板采集场景, String.valueOf(result[9] & 0xff));
        map.put(MapParams.产品形式, String.valueOf(result[10] & 0xff));
        map.put(MapParams.传感信号, String.valueOf(result[11] & 0xff));
        map.put(MapParams.电源类型, String.valueOf(result[13] & 0xff));
        map.put(MapParams.底板出厂时间_年, String.valueOf(result[14] & 0xff));
        map.put(MapParams.底板出厂时间_月, String.valueOf(result[15] & 0xff));
        map.put(MapParams.底板出厂时间_日, String.valueOf(result[16] & 0xff));
        map.put(MapParams.底板硬件版本, String.valueOf(result[17] & 0xff));
        map.put(MapParams.底板软件版本, String.valueOf(result[18] & 0xff));
//        map.put(MapParams.工作模式, String.valueOf(result[19] & 0xff));
//        map.put(MapParams.信号类型, String.valueOf(result[20] & 0xff));
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
}
