package com.wu.loushanyun.basemvp.m.sat;

import com.wu.loushanyun.base.util.DataParser;
import com.wu.loushanyun.basemvp.m.sat.util.SAtInstructParams;

import java.util.HashMap;

import met.hx.com.librarybase.some_utils.ByteConvertUtils;

/**
 * 固件版本
 */
class SAtInstructFirmwareVersion extends BaseSAtInstruct {


    public SAtInstructFirmwareVersion(double hardVersion) {
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
        byte[] bytes = DataParser.getBytesByASCII("AT+V?");
        return bytes;
    }

    @Override
    public byte[] getThreeReadBytes(String... params) {
        return new byte[0];
    }

    @Override
    public byte[] getFourReadBytes(String... params) {
        byte[] bytes = DataParser.getBytesByASCII("AT+V?");
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
            String version = result.substring(result.indexOf("+V:") + 3, result.length() - "OK".length());
            stringHashMap.put(SAtInstructParams.sAtInstructFirmwareVersion, version);
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
        String version = result.substring(result.indexOf("+V:") + 3, result.length() - "OK".length());
        stringHashMap.put(SAtInstructParams.sAtInstructFirmwareVersion, version);
        return stringHashMap;
    }
}