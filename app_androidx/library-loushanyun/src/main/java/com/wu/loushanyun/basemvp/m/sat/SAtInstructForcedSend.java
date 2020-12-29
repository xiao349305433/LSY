package com.wu.loushanyun.basemvp.m.sat;

import com.wu.loushanyun.base.util.DataParser;

import java.util.HashMap;

/**
 * 强制发送
 */
class SAtInstructForcedSend extends BaseSAtInstruct {


    public SAtInstructForcedSend(double hardVersion) {
        super(hardVersion);
    }

    @Override
    public byte[] getOneWriteBytes(String... params) {
        return new byte[0];
    }

    @Override
    public byte[] getTwoWriteBytes(String... params) {
        return new byte[0];
    }

    @Override
    public byte[] getThreeWriteBytes(String... params) {
        return new byte[0];
    }

    @Override
    public byte[] getFourWriteBytes(String... params) {
        return new byte[0];
    }

    @Override
    public byte[] getOneReadBytes(String... params) {
        return new byte[0];
    }

    @Override
    public byte[] getTwoReadBytes(String... params) {
        byte[] bytes = null;
        if (hardVersion >= 1.06) {
            String phone = params[0];
            byte[] d1 = new byte[16];
            for (int i = 0; i < d1.length - 5; i++) {
                d1[i] = (byte) Integer.parseInt(phone.substring(i, i + 1));
            }
            bytes = DataParser.getBytesByASCII("AT+SENDB=1,5,1,000c050" + phone);
        //    bytes = DataParser.getBytesByASCII("AT+SENDB=1,5,1,FFFFFFFFFF6400FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF" + phone);
        } else {
            bytes = new byte[16];
            String phone = params[0];
            bytes[0] = (byte) 0x68;
            bytes[1] = (byte) 0x0c;//有效数据
            bytes[2] = (byte) 0x27;//命令
            for (int i = 3; i < bytes.length - 2; i++) {
                bytes[i] = (byte) Integer.parseInt(phone.substring(i - 3, i - 2));
            }
            byte cs = 0;
            for (int i = 1; i < 14; i++) {
                cs += bytes[i];
            }
            bytes[14] = cs;//校验和
            bytes[15] = (byte) 0x16;
        }
        return bytes;
    }

    @Override
    public byte[] getThreeReadBytes(String... params) {
        return new byte[0];
    }

    @Override
    public byte[] getFourReadBytes(String... params) {
        byte[] bytes = null;
        String phone = params[0];
        byte[] d1 = new byte[16];
        for (int i = 0; i < d1.length - 5; i++) {
            d1[i] = (byte) Integer.parseInt(phone.substring(i, i + 1));
        }

      //  bytes = DataParser.getBytesByASCII("AT+SENDB=1,5,1,FFFFFFFFFF6400FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF" + phone);
       bytes = DataParser.getBytesByASCII("AT+SENDB=1,5,1,000c050" + phone);
        return bytes;
    }

    @Override
    public HashMap<String, String> parseOneWNotifyBytes(byte[] bytes) throws Exception {
        return null;
    }

    @Override
    public HashMap<String, String> parseOneRNotifyBytes(byte[] bytes) throws Exception {
        return null;
    }

    @Override
    public HashMap<String, String> parseTwoWNotifyBytes(byte[] bytes) throws Exception {
        return null;
    }

    @Override
    public HashMap<String, String> parseTwoRNotifyBytes(byte[] bytes) throws Exception {
        return null;
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
        return null;
    }
}
