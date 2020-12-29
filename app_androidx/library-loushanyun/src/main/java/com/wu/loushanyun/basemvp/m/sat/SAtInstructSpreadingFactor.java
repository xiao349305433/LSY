package com.wu.loushanyun.basemvp.m.sat;

import com.wu.loushanyun.base.util.DataParser;
import com.wu.loushanyun.basemvp.m.sat.util.SAtInstructParams;

import java.util.HashMap;

import met.hx.com.librarybase.some_utils.ByteConvertUtils;

/**
 * 扩频因子
 */
class SAtInstructSpreadingFactor extends BaseSAtInstruct {

    public SAtInstructSpreadingFactor(double hardVersion) {
        super(hardVersion);
    }

    /**
     * DR 配置 Bit rate[bit/s]
     * 0 SF12 / 125 kHz 250
     * 1 SF11 / 125 kHz 440
     * 2 SF10 / 125 kHz 980
     * 3 SF9 / 125 kHz 1760
     * 4 SF8 / 125 kHz 3215
     * 5 SF7 / 125 kHz 547
     *
     * @param params
     * @return
     */

    @Override
    public byte[] getOneWriteBytes(String... params) {
        return new byte[0];
    }

    @Override
    public byte[] getTwoWriteBytes(String... params) {
        byte[] bytes = new byte[1];
        if (hardVersion >= 1.06) {
            String drStr = params[0];
            if ("SF7".equals(drStr)) {
                drStr = "5";
            } else if ("SF8".equals(drStr)) {
                drStr = "4";
            } else if ("SF9".equals(drStr)) {
                drStr = "3";
            } else if ("SF10".equals(drStr)) {
                drStr = "2";
            } else if ("SF11".equals(drStr)) {
                drStr = "1";
            } else if ("SF12".equals(drStr)) {
                drStr = "0";
            }
            bytes = DataParser.getBytesByASCII("AT+DR=" + drStr);
        } else {

        }
        return bytes;
    }

    @Override
    public byte[] getThreeWriteBytes(String... params) {
        return new byte[0];
    }

    @Override
    public byte[] getFourWriteBytes(String... params) {
        byte[] bytes = new byte[1];
        String drStr = params[0];
        if ("SF7".equals(drStr)) {
            drStr = "5";
        } else if ("SF8".equals(drStr)) {
            drStr = "4";
        } else if ("SF9".equals(drStr)) {
            drStr = "3";
        } else if ("SF10".equals(drStr)) {
            drStr = "2";
        } else if ("SF11".equals(drStr)) {
            drStr = "1";
        } else if ("SF12".equals(drStr)) {
            drStr = "0";
        }
        bytes = DataParser.getBytesByASCII("AT+DR=" + drStr);
        return bytes;
    }

    @Override
    public byte[] getOneReadBytes(String... params) {
        return new byte[0];
    }

    @Override
    public byte[] getTwoReadBytes(String... params) {
        byte[] bytes = new byte[0];
        if (hardVersion >= 1.06) {
            bytes = DataParser.getBytesByASCII("AT+DR?");
        } else {

        }
        return bytes;
    }

    @Override
    public byte[] getThreeReadBytes(String... params) {
        return new byte[0];
    }

    @Override
    public byte[] getFourReadBytes(String... params) {
        byte[] bytes = DataParser.getBytesByASCII("AT+DR?");
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
            String drStr = result.substring(result.indexOf("+DR:") + 4, result.length() - "OK".length());
            if ("5".equals(drStr)) {
                drStr = "SF7";
            } else if ("4".equals(drStr)) {
                drStr = "SF8";
            } else if ("3".equals(drStr)) {
                drStr = "SF9";
            } else if ("2".equals(drStr)) {
                drStr = "SF10";
            } else if ("1".equals(drStr)) {
                drStr = "SF11";
            } else if ("0".equals(drStr)) {
                drStr = "SF12";
            }
            stringHashMap.put(SAtInstructParams.sAtInstructSpreadingFactor, drStr);
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
        String drStr = result.substring(result.indexOf("+DR:") + 4, result.length() - "OK".length());
        if ("5".equals(drStr)) {
            drStr = "SF7";
        } else if ("4".equals(drStr)) {
            drStr = "SF8";
        } else if ("3".equals(drStr)) {
            drStr = "SF9";
        } else if ("2".equals(drStr)) {
            drStr = "SF10";
        } else if ("1".equals(drStr)) {
            drStr = "SF11";
        } else if ("0".equals(drStr)) {
            drStr = "SF12";
        }
        stringHashMap.put(SAtInstructParams.sAtInstructSpreadingFactor, drStr);
        return stringHashMap;

    }
}
