package met.hx.com.librarybase.some_utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2016/8/13
 *     desc  : 转换相关工具类
 * </pre>
 */
public class ConvertUtils {
    public static final int GB = 1024 * 1024 * 1024;//定义GB的计算常量
    public static final int MB = 1024 * 1024;//定义MB的计算常量
    public static final int KB = 1024;//定义KB的计算常量
    static final char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    /**
     * 每1个byte转为2个hex字符
     * <p>例如：</p>
     * bytes2HexString(new byte[] { 0, (byte) 0xa8 }) returns 00A8
     *
     * @param src byte数组
     * @return 16进制大写字符串
     */
    public static String bytes2HexString(byte[] src) {
        char[] res = new char[src.length << 1];
        for (int i = 0, j = 0; i < src.length; i++) {
            res[j++] = hexDigits[src[i] >>> 4 & 0x0f];
            res[j++] = hexDigits[src[i] & 0x0f];
        }
        return new String(res);
    }

    /**
     * 每2个hex字符转为1个byte
     * <p>例如：</p>
     * hexString2Bytes("00A8") returns { 0, (byte) 0xA8 }
     *
     * @param hexString 十六进制字符串
     * @return byte数组
     */
    public static byte[] hexString2Bytes(String hexString) {
        int len = hexString.length();
        if (len % 2 != 0) {
            throw new IllegalArgumentException("长度不是偶数");
        }
        char[] hexBytes = hexString.toUpperCase().toCharArray();
        byte[] res = new byte[len >>> 1];
        for (int i = 0; i < len; i += 2) {
            res[i >> 1] = (byte) (hex2Dec(hexBytes[i]) << 4 | hex2Dec(hexBytes[i + 1]));
        }
        return res;
    }

    /**
     * 单个hex字符转为10进制
     *
     * @param hexChar hex单个字节
     * @return 0..15
     */
    private static int hex2Dec(char hexChar) {
        if (hexChar >= '0' && hexChar <= '9') {
            return hexChar - '0';
        } else if (hexChar >= 'A' && hexChar <= 'F') {
            return hexChar - 'A' + 10;
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * charArr转byteArr
     *
     * @param chars 待转的char数组
     * @return byte数组
     */
    public static byte[] chars2Bytes(char[] chars) {
        int len = chars.length;
        byte[] bytes = new byte[len];
        for (int i = 0; i < len; i++) {
            bytes[i] = (byte) (chars[i]);
        }
        return bytes;
    }

    /**
     * byteArr转charArr
     *
     * @param bytes 待转的byte数组
     * @return char数组
     */
    public static char[] bytes2Chars(byte[] bytes) {
        int len = bytes.length;
        char[] chars = new char[len];
        for (int i = 0; i < len; i++) {
            chars[i] = (char) (bytes[i] & 0xff);
        }
        return chars;
    }

    /**
     * byteArray 转Object
     * @param data
     * @return
     * @throws StreamCorruptedException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Object byteArrayToObject(byte[] data)
            throws StreamCorruptedException, IOException,
            ClassNotFoundException {
        if (data == null) {
            return null;
        }
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        ObjectInputStream ois = new ObjectInputStream(bais);
        try {
            return ois.readObject();
        } finally {
            ois.close();
        }
    }

    public static String ByteConversionGBMBKB(int KSize) {
        if (KSize / GB >= 1) {//如果当前Byte的值大于等于1GB
            return (Math.round(KSize / (float) GB)) + "GB";//将其转换成GB
        } else if (KSize / MB >= 1) {//如果当前Byte的值大于等于1MB
            return (Math.round(KSize / (float) MB)) + "MB";
        } else if (KSize / KB >= 1){//如果当前Byte的值大于等于1KB
            return (Math.round(KSize / (float) KB)) + "KB";
        } else{
            return KSize + "Byte";//显示Byte值
        }
    }
}
