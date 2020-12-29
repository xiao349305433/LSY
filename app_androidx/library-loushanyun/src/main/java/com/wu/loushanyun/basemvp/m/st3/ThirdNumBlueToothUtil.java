package com.wu.loushanyun.basemvp.m.st3;

import java.util.ArrayList;

import met.hx.com.librarybase.some_utils.ToastUtils;
import met.hx.com.librarybase.some_utils.XHStringUtil;

/**
 * 4号模组数字通讯蓝牙协议指令集
 */
public class ThirdNumBlueToothUtil {



    public static byte[] getByteFromHexString(String writeStr) {
        try {
            if (XHStringUtil.isEmpty(writeStr, false)) {
                ToastUtils.showShort("请输入指令");
                return null;
            }
            ArrayList<String> arrayList = new ArrayList<>();
            if (writeStr.length() % 2 == 0) {
                for (int i = 0; i < writeStr.length(); i++) {
                    if (i % 2 == 0) {
                        arrayList.add(writeStr.substring(i, i + 2));
                    }
                }
            } else {
                String lastStr = "0" + writeStr.substring(writeStr.length() - 1, writeStr.length());
                writeStr = writeStr.substring(0, writeStr.length() - 1) + lastStr;
                for (int i = 0; i < writeStr.length(); i++) {
                    if (i % 2 == 0) {
                        arrayList.add(writeStr.substring(i, i + 2));
                    }

                }
            }
            byte[] bytes = new byte[arrayList.size()];
            for (int i = 0; i < arrayList.size(); i++) {
                int value = Integer.parseInt(arrayList.get(i), 16);
                bytes[i] = (byte) value;
            }
            return bytes;
        } catch (Exception e) {
            ToastUtils.showShort("解析错误");
            return null;
        }
    }

    public static byte[] getThirdOx60Bytes(String writeStr) {
        return getByteFromHexString("680060" + writeStr + "16");
    }




}
