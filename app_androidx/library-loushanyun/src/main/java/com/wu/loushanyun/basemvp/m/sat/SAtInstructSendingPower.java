package com.wu.loushanyun.basemvp.m.sat;

import com.wu.loushanyun.base.util.DataParser;
import com.wu.loushanyun.basemvp.m.sat.util.SAtInstructParams;

import java.util.HashMap;

import met.hx.com.librarybase.some_utils.ByteConvertUtils;

/**
 * 0 MaxEIRP
 * 1 MaxEIRP – 2dB
 * 2 MaxEIRP – 4dB
 * 3 MaxEIRP – 6dB
 * 4 MaxEIRP – 8dB
 * 5 MaxEIRP – 10dB
 * 6 MaxEIRP – 12dB
 * 7 MaxEIRP – 14dB
 * 8 MaxEIRP – 16dB
 * 9 MaxEIRP – 18dB
 * 10 MaxEIRP – 20dB
 */
class SAtInstructSendingPower extends BaseSAtInstruct {
    public SAtInstructSendingPower(double hardVersion) {
        super(hardVersion);
    }

    @Override
    public byte[] getOneWriteBytes(String... params) throws Exception {
        return new byte[0];
    }

    @Override
    public byte[] getTwoWriteBytes(String... params) throws Exception {
        byte[] bytes = new byte[1];
        if (hardVersion >= 1.06) {
            String drStr = params[0];
            if ("20dbm".equals(drStr)) {
                drStr = "0";
            } else if ("18dbm".equals(drStr)) {
                drStr = "1";
            } else if ("16dbm".equals(drStr)) {
                drStr = "2";
            }
            bytes = DataParser.getBytesByASCII("AT+TXP=" + drStr);
        } else {

        }
        return bytes;
    }

    @Override
    public byte[] getThreeWriteBytes(String... params) throws Exception {
        return new byte[0];
    }

    @Override
    public byte[] getFourWriteBytes(String... params) throws Exception {
        byte[] bytes = new byte[1];
        String drStr = params[0];
        if ("20dbm".equals(drStr)) {
            drStr = "0";
        } else if ("18dbm".equals(drStr)) {
            drStr = "1";
        } else if ("16dbm".equals(drStr)) {
            drStr = "2";
        }
        bytes = DataParser.getBytesByASCII("AT+TXP=" + drStr);
        return bytes;
    }

    @Override
    public byte[] getOneReadBytes(String... params) throws Exception {
        return new byte[0];
    }

    @Override
    public byte[] getTwoReadBytes(String... params) throws Exception {
        byte[] bytes;
        if (hardVersion >= 1.06) {
            bytes = DataParser.getBytesByASCII("AT+TXP?");
        } else {
            bytes = new byte[1];
        }
        return bytes;
    }

    @Override
    public byte[] getThreeReadBytes(String... params) throws Exception {
        return new byte[0];
    }

    @Override
    public byte[] getFourReadBytes(String... params) throws Exception {
        byte[] bytes;
        bytes = DataParser.getBytesByASCII("AT+TXP?");
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
            String TXPStr = result.replaceAll("\r\n", "").substring(result.indexOf("+TXP:") + 5, result.length() - "OK".length());
            if ("0".equals(TXPStr)) {
                TXPStr = "20dbm";
            } else if ("1".equals(TXPStr)) {
                TXPStr = "18dbm";
            } else if ("2".equals(TXPStr)) {
                TXPStr = "16dbm";
            } else if ("3".equals(TXPStr)) {
                TXPStr = "14dbm";
            } else if ("4".equals(TXPStr)) {
                TXPStr = "12dbm";
            } else if ("5".equals(TXPStr)) {
                TXPStr = "10dbm";
            } else if ("6".equals(TXPStr)) {
                TXPStr = "8dbm";
            } else if ("7".equals(TXPStr)) {
                TXPStr = "6dbm";
            } else if ("8".equals(TXPStr)) {
                TXPStr = "4dbm";
            } else if ("9".equals(TXPStr)) {
                TXPStr = "2dbm";
            } else if ("10".equals(TXPStr)) {
                TXPStr = "0dbm";
            }
            stringHashMap.put(SAtInstructParams.sAtInstructSendingPower, TXPStr);
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
        String TXPStr = result.replaceAll("\r\n", "").substring(result.indexOf("+TXP:") + 5, result.length() - "OK".length());
        if ("0".equals(TXPStr)) {
            TXPStr = "20dbm";
        } else if ("1".equals(TXPStr)) {
            TXPStr = "18dbm";
        } else if ("2".equals(TXPStr)) {
            TXPStr = "16dbm";
        } else if ("3".equals(TXPStr)) {
            TXPStr = "14dbm";
        } else if ("4".equals(TXPStr)) {
            TXPStr = "12dbm";
        } else if ("5".equals(TXPStr)) {
            TXPStr = "10dbm";
        } else if ("6".equals(TXPStr)) {
            TXPStr = "8dbm";
        } else if ("7".equals(TXPStr)) {
            TXPStr = "6dbm";
        } else if ("8".equals(TXPStr)) {
            TXPStr = "4dbm";
        } else if ("9".equals(TXPStr)) {
            TXPStr = "2dbm";
        } else if ("10".equals(TXPStr)) {
            TXPStr = "0dbm";
        }
        stringHashMap.put(SAtInstructParams.sAtInstructSendingPower, TXPStr);
        return stringHashMap;
    }
}
