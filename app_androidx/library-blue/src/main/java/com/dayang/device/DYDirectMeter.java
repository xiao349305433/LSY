package com.dayang.device;

import com.xgg.blesdk.Utils;

import org.json.JSONObject;

public class DYDirectMeter extends DYMeter {

    public DYDirectMeter() {
        m_id = 1;
    }

    @Override
    public byte[] getQueryBytes(String sn) {
        String tmp = "FEFEFE68190002030100000000000000010000000000000020160117204112AD16";
        int length = tmp.length() / 2;
        char[] hexChars = tmp.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (Utils.charToByte(hexChars[pos]) << 4 | Utils.charToByte(hexChars[pos + 1]));
        }

        try {
//	    	int isn = Integer.parseInt(sn);
            long isn = Long.parseLong(sn);
            for (int i = 0; i < 8; i++) {
                d[i + 8] = (byte) (isn & 0xff);
                d[i + 16] = (byte) (isn & 0xff);
                isn = isn >> 8;
            }
        } catch (Exception e) {

        }


        length = d[5] * 256 + d[4];
        byte cs = 0;
        for (int i = 4; i < length + 6; i++) {
            cs += d[i];
        }
        d[length + 6] = cs;
        return d;
    }

    @Override
    public JSONObject getQueryObject(byte output[]) {
        JSONObject json = new JSONObject();
        //先检查返回状态码
        try {
            if (output == null) {
                json.put("状态", "未能读取到数据");
                return json;
            }
            if (output[7] == 0)        //操作成功
            {
                json.put("rtcode", "ok");


                long tmp = (int) output[9];
                json.put("表号", tmp);

                tmp = 0;
                for (int i = 7; i > 0; i--) {
                    tmp = (tmp * 256) + (int) (output[i + 9] & 0xff);
                }
                json.put("ID", tmp);


                tmp = 0;
                for (int i = 6; i > 0; i--) {
                    tmp = tmp * 256 + (int) (output[i + 16] & 0xff);
                }
                json.put("正脉冲数", tmp);

                tmp = 0;
                for (int i = 4; i > 0; i--) {
                    tmp = tmp * 256 + (int) (output[i + 22] & 0xff);
                }
                json.put("反脉冲数", tmp);

                tmp = (int) output[27];
                if (tmp == 0)
                    json.put("倍率", "0.001");
                else {
                    if (tmp == 1)
                        json.put("倍率", "0.01");
                    if (tmp == 2)
                        json.put("倍率", "0.1");
                    if (tmp == 3)
                        json.put("倍率", "1");
                    if (tmp == 4)
                        json.put("倍率", "10");
                    if (tmp == 5)
                        json.put("倍率", "100");
                    if (tmp == 6)
                        json.put("倍率", "1000");
                    if (tmp == 7)
                        json.put("倍率", "0.0001");
                }

                tmp = 0;
                for (int i = 4; i > 0; i--) {
                    tmp = tmp * 256 + (int) (output[i + 27] & 0xff);
                }
                json.put("冻结水量", tmp);

                tmp = 0;
                for (int i = 2; i > 0; i--) {
                    tmp = tmp * 256 + (int) (output[i + 29] & 0xff);
                }
                json.put("剩余水量", tmp + "吨");


//				for (int i = 8; i > 0; i--)
//					{
//						tmp = (tmp *256) + (int)(output[i+32] & 0xff);
//					}
                tmp = (int) output[34] & 0xff;
                json.put("状态", getZTReadStringByCode(String.valueOf(tmp)));

                tmp = (int) output[35];
                json.put("冻结时间", tmp + "天");

                return json;
            } else if (output[7] == 1)    //操作失败
            {
                json.put("rtcode", "1");
                json.put("desc", "操作失败");
                return json;
            } else if (output[7] == 2)    //无效命令字
            {
                json.put("rtcode", "2");
                json.put("desc", "无效命令字");
                return json;
            } else if (output[7] == 3)    //操作超时
            {
                json.put("rtcode", "3");
                json.put("desc", "操作超时");
                return json;
            } else if (output[7] == 4)    //无效参数
            {
                json.put("rtcode", "4");
                json.put("desc", "无效参数");
                return json;
            }
        } catch (Exception e) {
            return json;
        }
        return null;
    }
    /**
     * 解析出2进制字符串判断状态，3号表单元
     *
     * @param tmpString
     * @return
     */
    public static String getZTReadStringByCode(String tmpString) {
        StringBuffer result = new StringBuffer();
        result.append("正常，");
        try {
            int tem = (Integer.valueOf(tmpString)) & 0xff;
            byte b = (byte) tem;
            String binary = conver2BinaryStr(b);
            if (binary.charAt(4) == '1') {
                if (result.toString().contains("正常，")) {
                    result.delete(0, result.length());
                }
                result.append("欠压，");
            }
            if (binary.charAt(2) == '1') {
                if (result.toString().contains("正常，")) {
                    result.delete(0, result.length());
                }
                result.append("倒流，");
            }
            if (binary.charAt(7) == '1') {
                if (result.toString().contains("正常，")) {
                    result.delete(0, result.length());
                }
                result.append("强磁，");
            }
            if (binary.charAt(6) == '1') {
                if (result.toString().contains("正常，")) {
                    result.delete(0, result.length());
                }
                result.append("拆卸，");
            }

        } catch (Exception e) {
            result.append("，");
        }
        return result.toString().substring(0, result.length() - 1);
    }
    /**
     * @param b
     * @param length 把二进制解析成几位
     * @return
     */
    public static String conver2BinaryStr(byte b, int length) {
        String s = Long.toString(b & 0xff, 2);
        if (s.length() < length) {
            String s1 = "";
            for (int j = 0; j < length - s.length(); j++) {
                s1 += "0";
            }
            s = s1 + s;
        }
        return s;
    }
    /**
     * byte转成2进制,按照8位解析
     *
     * @param b
     * @return
     */
    public static String conver2BinaryStr(byte b) {
        return conver2BinaryStr(b, 8);
    }
}


