package com.wu.loushanyun.basemvp.m.sat;

import com.elvishew.xlog.XLog;
import com.wu.loushanyun.base.util.DataParser;
import com.wu.loushanyun.basemvp.m.sat.util.SAtInstructParams;

import java.util.HashMap;

import met.hx.com.librarybase.some_utils.ByteConvertUtils;

public class SAtInstructToken extends BaseSAtInstruct {

    public SAtInstructToken(double hardVersion) {
        super(hardVersion);
    }

    @Override
    public byte[] getOneWriteBytes(String... params) throws Exception {
        return new byte[0];
    }

    @Override
    public byte[] getTwoWriteBytes(String... params) throws Exception {
        return new byte[0];
    }

    @Override
    public byte[] getThreeWriteBytes(String... params) throws Exception {
        return new byte[0];
    }

    @Override
    public byte[] getFourWriteBytes(String... params) throws Exception {
        return new byte[0];
    }

    @Override
    public byte[] getOneReadBytes(String... params) throws Exception {
        return new byte[0];
    }

    @Override
    public byte[] getTwoReadBytes(String... params) throws Exception {
        byte[] bytes = new byte[0];
        if (hardVersion >= 1.06) {
            bytes = DataParser.getBytesByASCII("AT+TOKEN?");
        } else {

        }
        return bytes;
    }

    @Override
    public byte[] getThreeReadBytes(String... params) throws Exception {
        return new byte[0];
    }

    @Override
    public byte[] getFourReadBytes(String... params) throws Exception {
        byte[] bytes = DataParser.getBytesByASCII("AT+TOKEN?");
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
        HashMap<String, String> stringHashMap = new HashMap<>();
        if (hardVersion >= 1.06) {
            String result = ByteConvertUtils.getASCIIbyByte(bytes, 4, 2).replaceAll("\r\n", "");
            String version1= result .substring(result.indexOf("+TOKEN:") + 7, result.length()- 2);
            stringHashMap.put(SAtInstructParams.sAtInstructToken, version1);
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
        String version = result.substring(result.indexOf("+TOKEN:") + 7, result.length()- 2);
        stringHashMap.put(SAtInstructParams.sAtInstructToken, version);
        return stringHashMap;
    }
}
