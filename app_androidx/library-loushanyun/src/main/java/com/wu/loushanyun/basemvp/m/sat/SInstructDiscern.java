package com.wu.loushanyun.basemvp.m.sat;

import com.wu.loushanyun.base.util.MapParams;

import java.util.HashMap;

class SInstructDiscern extends BaseSAtInstruct {
    public SInstructDiscern(double hardVersion) {
        super(hardVersion);
    }

    @Override
    public byte[] getOneWriteBytes(String... params) throws Exception {
        byte[] CMD_INFO_ALL = {(byte) 0x68, (byte) 0x01, (byte) 0x11, (byte) 0x12, (byte) 0x16};
        return CMD_INFO_ALL;
    }

    @Override
    public byte[] getTwoWriteBytes(String... params) throws Exception {
        byte[] CMD_INFO_ALL = {(byte) 0x68, (byte) 0x01, (byte) 0x11, (byte) 0x12, (byte) 0x16};
        return CMD_INFO_ALL;
    }

    @Override
    public byte[] getThreeWriteBytes(String... params) throws Exception {
        return new byte[0];
    }

    @Override
    public byte[] getFourWriteBytes(String... params) throws Exception {
        byte[] CMD_INFO_ALL = {(byte) 0x68, (byte) 0x01, (byte) 0x11, (byte) 0x12, (byte) 0x16};
        return CMD_INFO_ALL;
    }

    @Override
    public byte[] getOneReadBytes(String... params) throws Exception {
        return new byte[0];
    }

    @Override
    public byte[] getTwoReadBytes(String... params) throws Exception {
        return new byte[0];
    }

    @Override
    public byte[] getThreeReadBytes(String... params) throws Exception {
        return new byte[0];
    }

    @Override
    public byte[] getFourReadBytes(String... params) throws Exception {
        return new byte[0];
    }

    @Override
    public HashMap<String, String> parseOneWNotifyBytes(byte[] result) throws Exception {
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

    @Override
    public HashMap<String, String> parseOneRNotifyBytes(byte[] bytes) throws Exception {
        return null;
    }

    @Override
    public HashMap<String, String> parseTwoWNotifyBytes(byte[] result) throws Exception {
        return null;
    }

    @Override
    public HashMap<String, String> parseTwoRNotifyBytes(byte[] result) throws Exception {
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

    @Override
    public HashMap<String, String> parseThreeWNotifyBytes(byte[] bytes) throws Exception {
        return null;
    }

    @Override
    public HashMap<String, String> parseThreeRNotifyBytes(byte[] bytes) throws Exception {
        return null;
    }

    @Override
    public HashMap<String, String> parseFourWNotifyBytes(byte[] result) throws Exception {
        return null;
    }

    @Override
    public HashMap<String, String> parseFourRNotifyBytes(byte[] result) throws Exception {
        HashMap<String, String> map = new HashMap<>();
        map.put(MapParams.采集场景, String.valueOf(result[3] & 0xff));
        int i = 4;
        map.put(MapParams.发送频率, String.valueOf(result[i++] & 0xff));
        map.put(MapParams.出厂时间_年, String.valueOf(result[i++] & 0xff));
        map.put(MapParams.出厂时间_月, String.valueOf(result[i++] & 0xff));
        map.put(MapParams.出厂时间_日, String.valueOf(result[i++] & 0xff));
        map.put(MapParams.网络交互, String.valueOf(result[i++] & 0xff));
        map.put(MapParams.硬件版本, String.valueOf(result[i++] & 0xff));
        map.put(MapParams.软件版本, String.valueOf(result[i++] & 0xff));
        map.put(MapParams.产品形式, String.valueOf(result[i++] & 0xff));
        map.put(MapParams.传感模式, String.valueOf(result[i++] & 0xff));
        map.put(MapParams.传感信号, String.valueOf(result[i++] & 0xff));
        map.put(MapParams.电源类型, String.valueOf(result[i++] & 0xff));
        long tmp = 0;
        for (int j = 5; j > 0; j--) {
            tmp = (tmp * 256) + (result[j + i-1] & 0xff);
        }
        map.put(MapParams.设备ID, String.valueOf(tmp));
        map.put(MapParams.激活状态, String.valueOf(result[20] & 0xff));
        map.put(MapParams.厂家标识, String.valueOf(((result[21] & 0xff) + ((result[22] & 0xff) << 8))));
        map.put(MapParams.状态, String.valueOf(result[23] & 0xff));
        return map;
    }
}
