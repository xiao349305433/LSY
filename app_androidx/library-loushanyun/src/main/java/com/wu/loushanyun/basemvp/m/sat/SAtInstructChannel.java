package com.wu.loushanyun.basemvp.m.sat;

import com.wu.loushanyun.base.util.DataParser;
import com.wu.loushanyun.base.util.LouShanYunUtils;
import com.wu.loushanyun.basemvp.m.sat.util.SAtInstructParams;

import java.util.HashMap;

import met.hx.com.librarybase.some_utils.ByteConvertUtils;

/**
 * 设置/读取 信道参数
 */
class SAtInstructChannel extends BaseSAtInstruct {

    /**
     * 通过模式获取信道命令
     *
     * @param moShi
     */
    public String getChannelMask(String moShi) {
        moShi = moShi.replaceAll("模式", "");
        if ("A".equals(moShi)) {
            moShi = "80";
        } else if ("B".equals(moShi)) {
            moShi = "0";
        }
        int moShiNum = Integer.valueOf(moShi);
        String strings = new String();
        for (int i = 0; i < 96; i++) {
            if (i >= moShiNum && i < moShiNum + 8) {
                strings += "1";
            } else {
                strings += "0";
            }
        }
        String strings1 = new String();
        for(int i=0;i<strings.length()/16;i++){
            strings1+=new StringBuilder(strings.substring(i*16,i*16+16)).reverse().toString();
        }
        String s = new String();
        for (int i = 0; i < strings1.length() / 4; i++) {
            s += Integer.toHexString(Integer.parseInt(strings1.substring(i * 4, i * 4 + 4), 2) & 0xf);
        }
        String s1 = new String();
        for (int i = 0; i < s.length() / 4; i++) {
            s1 += new StringBuilder(s.substring(i * 4, i * 4 + 4)).toString().toUpperCase() + ",";
        }
        s1 = s1.substring(0, s1.length() - 1);
        return s1;

    }


    public static void main(String[] args) {
//        System.out.print(getChannelMoshi(getChannelMask("模式29")));
//        for(int i=0;i<=88;i++){
//            System.out.print("模式"+i+"："+getChannelMask("模式"+i+"\n"));
//            System.out.print("解析出来:"+getChannelMoshi(getChannelMask("模式"+i))+"\n");
//        }

    }

    /**
     * 通过信道命令获取模式
     *
     * @param channelMask
     */
    public String getChannelMoshi(String channelMask) {
        String moshi = "模式";
        String[] a = channelMask.split(",");
        for (int i = 0; i < a.length; i++) {
            a[i] = new StringBuilder(a[i]).toString();
        }
        String s = new String();
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[i].length(); j++) {
                byte b = (byte) (Integer.parseInt(a[i].charAt(j) + "", 16) & 0xff);
                s += ByteConvertUtils.conver2BinaryStr(b, 4);
            }
        }
        String s1 = new String();
        for (int i = 0; i < s.length()/16; i++) {
            s1+=new StringBuilder(s.substring(i*16,i*16+16)).reverse().toString();
        }
        for (int i = 0; i < s1.length(); i++) {
            if (s1.charAt(i) == '1') {
                if (i == 80) {
                    moshi += "A";
                } else if (i == 0) {
                    moshi += "B";
                } else {
                    moshi += i;
                }
                break;
            }
        }
        return moshi;
    }


    public SAtInstructChannel(double hardVersion) {
        super(hardVersion);
    }

    @Override
    public byte[] getOneWriteBytes(String... params) {
        return new byte[0];
    }

    /**
     * @param params params[0]为1.06的参数，params[1]为1.05的参数
     * @return
     */
    @Override
    public byte[] getTwoWriteBytes(String... params) {
        byte[] bytes;
        if (hardVersion >= 1.06) {
            bytes = DataParser.getBytesByASCII("AT+CHM=" + getChannelMask(params[0]));
        } else {
            int model = LouShanYunUtils.getXDCSWriteStringByCode02(params[1]);
            bytes = new byte[]{0x68, 0x02, 0x05, (byte) model, (byte) (0x07 + model), 0x16};
        }

        return bytes;
    }

    @Override
    public byte[] getThreeWriteBytes(String... params) {
        return new byte[0];
    }

    @Override
    public byte[] getFourWriteBytes(String... params) {
        byte[] bytes;
        bytes = DataParser.getBytesByASCII("AT+CHM=" + getChannelMask(params[0]));
        return bytes;
    }

    @Override
    public byte[] getOneReadBytes(String... params) {
        return new byte[0];
    }

    @Override
    public byte[] getTwoReadBytes(String... params) {
        byte[] bytes = DataParser.getBytesByASCII("AT+CHM?");
        return bytes;
    }

    @Override
    public byte[] getThreeReadBytes(String... params) {
        return new byte[0];
    }

    @Override
    public byte[] getFourReadBytes(String... params) {
        byte[] bytes = DataParser.getBytesByASCII("AT+CHM?");
        return bytes;
    }

    @Override
    public HashMap<String, String> parseOneWNotifyBytes(byte[] bytes) throws Exception {
        return null;
    }

    @Override
    public HashMap<String, String> parseOneRNotifyBytes(byte[] bytes) throws Exception {
        HashMap<String, String> stringHashMap = new HashMap<>();
        if (hardVersion >= 1.06) {
            String result = ByteConvertUtils.getASCIIbyByte(bytes, 4, 2).replaceAll("\r\n", "");
            String channel = getChannelMoshi(result.substring(result.indexOf("+CHM:") + 5, result.length() - "OK".length()));
            stringHashMap.put(SAtInstructParams.sAtInstructChannel, channel);
        }
        return stringHashMap;
    }

    @Override
    public HashMap<String, String> parseTwoWNotifyBytes(byte[] bytes) throws Exception {
        return null;
    }

    @Override
    public HashMap<String, String> parseTwoRNotifyBytes(byte[] bytes) throws Exception {
        HashMap<String, String> stringHashMap = new HashMap<>();
        if (hardVersion >= 1.06) {
            String result = ByteConvertUtils.getASCIIbyByte(bytes, 4, 2).replaceAll("\r\n", "");
            String channel = getChannelMoshi(result.substring(result.indexOf("+CHM:") + 5, result.length() - "OK".length()));
            stringHashMap.put(SAtInstructParams.sAtInstructChannel, channel);
        }
        return stringHashMap;
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
    public HashMap<String, String> parseFourWNotifyBytes(byte[] bytes) throws Exception {
        return null;
    }

    @Override
    public HashMap<String, String> parseFourRNotifyBytes(byte[] bytes) throws Exception {
        HashMap<String, String> stringHashMap = new HashMap<>();
        String result = ByteConvertUtils.getASCIIbyByte(bytes, 4, 2).replaceAll("\r\n", "");
        String channel = getChannelMoshi(result.substring(result.indexOf("+CHM:") + 5, result.length() - "OK".length()));
        stringHashMap.put(SAtInstructParams.sAtInstructChannel, channel);
        return stringHashMap;
    }
}
