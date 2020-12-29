package met.hx.com.librarybase.some_utils;

import com.elvishew.xlog.XLog;

import org.jetbrains.annotations.TestOnly;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class ByteConvertUtils {

    /**
     * 16进制转成ASCII
     *
     * @param hex
     * @return
     */
    public static String convert16ToASCII(String hex) {
        StringBuilder sb = new StringBuilder();
        StringBuilder temp = new StringBuilder();

        //49204c6f7665204a617661 split into two characters 49, 20, 4c...
        for (int i = 0; i < hex.length() - 1; i += 2) {

            //grab the hex in pairs
            String output = hex.substring(i, (i + 2));
            //convert hex to decimal
            int decimal = Integer.parseInt(output, 16);
            //convert the decimal to character
            sb.append((char) decimal);

            temp.append(decimal);
        }
        return sb.toString();
    }

    /**
     * ASCII转成16进制
     *
     * @param str
     * @return
     */
    public static String convertASCIITo16(String str) {
        char[] chars = str.toCharArray();
        StringBuffer hex = new StringBuffer();
        for (int i = 0; i < chars.length; i++) {
            String hexString = Integer.toHexString((int) chars[i]);
            if (hexString.length() == 1) {
                hexString = '0' + hexString;
            }
            hex.append(hexString);
        }
        return hex.toString();
    }

    /**
     * 十进制转十六进制
     *
     * @param n
     * @return
     */
    public static String intToHex(int n) {
        String s = Integer.toHexString(n);
        if (s.length() == 1) {
            s = "0" + s;
        }
        return s;
    }

    /**
     * byte数组转换为二进制字符串,每个字节以","隔开
     **/
    public static String conver2BinaryStr(byte[] b) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            String s = Long.toString(b[i] & 0xff, 2);
            if (s.length() < 8) {
                String s1 = "";
                for (int j = 0; j < 8 - s.length(); j++) {
                    s1 += "0";
                }
                s = s1 + s;
            }
            result.append(s + ",");
        }
        return result.toString().substring(0, result.length() - 1);
    }


    /**
     * te by转成2进制,按照8位解析
     *
     * @param b
     * @return
     */
    public static String conver2BinaryStr(byte b) {
        return conver2BinaryStr(b, 8);
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
     * 第一位代表符号位解析byte
     *
     * @param b
     * @return
     */
    public static int parseByteToSignedString(byte b) {
        String allResult = conver2BinaryStr(b);
        String result = allResult.substring(1, 8);
        if (allResult.charAt(0) == '0') {
            return binaryToDecimal(result);
        } else {
            return -binaryToDecimal(result);
        }
    }

    /**
     * @Description: 二进制转换成十进制
     */
    public static int binaryToDecimal(String binarySource) {
        BigInteger bi = new BigInteger(binarySource, 2);    //转换为BigInteger类型
        return Integer.parseInt(bi.toString());        //转换成十进制
    }

    /**
     * byte数组转成16进制字符串
     *
     * @param result
     * @return
     */
    public static String convertByteToHexString(byte[] result) {
        StringBuilder sb = new StringBuilder();
        String temp;
        for (int i = 0; i < result.length; i++) {
            temp = Integer.toHexString(result[i] & 0xff);
            if (temp.length() == 1) {
                sb.append(0);
            }
            sb.append(temp);
        }
        return sb.toString();
    }

    /**
     * byte数组转ASCII
     *
     * @param result
     * @return
     */
    public static String convertByteToASCII(byte[] result) {
        return convert16ToASCII(convertByteToHexString(result));
    }

    /**
     * 十六进制串转化为byte数组
     *
     * @return the array of byte
     */
    public static final byte[] convertHexStringToBytes(String hex) throws IllegalArgumentException {
        if (hex.length() % 2 != 0) {
            throw new IllegalArgumentException();
        }
        char[] arr = hex.toCharArray();
        byte[] b = new byte[hex.length() / 2];
        for (int i = 0, j = 0, l = hex.length(); i < l; i++, j++) {
            String swap = "" + arr[i++] + arr[i];
            int byteint = Integer.parseInt(swap, 16) & 0xFF;
            b[j] = new Integer(byteint).byteValue();
        }
        return b;
    }

    /**
     * 十六进制串转化为byte数组
     *
     * @return the array of byte
     */

    public static final byte[] convertSymbleHexStringToBytes(String hex) throws IllegalArgumentException {

        char[] arr = hex.toCharArray();
        int blength = hex.replace("-", "").length() / 2;
        byte[] b = new byte[blength];
        String[] strs = new String[blength];
        for (int i = 0, j = 0; j < blength; i++, j++) {
            if (arr[i] == '-') {
                String swap = "-" + arr[i + 1] + arr[i + 2];
                strs[j] = swap;
                b[j] = new BigInteger(swap, 16).byteValue();
                i = i + 2;
            } else {
                String swap = "" + arr[i] + arr[i + 1];
                strs[j] = swap;
                b[j] = new BigInteger(swap, 16).byteValue();
                i = i + 1;
            }
        }
        for (int i = 0; i < strs.length; i++) {
            System.out.print(b[i]);
        }

//        for (int i = 0, j = 0, l = hex.length(); i < l; i++, j++) {
//            String swap = "" + arr[i++] + arr[i];
//            int byteint = Integer.parseInt(swap, 16) & 0xFF;
//            b[j] = new Integer(byteint).byteValue();
//        }
        return b;
    }

    /**
     * @param result 需要解析的数组
     * @param start  需要去掉头部的长度
     * @param end    需要去掉尾部的长度
     *               掐头去尾得到需要解析的数组
     * @return
     */
    public static String getASCIIbyByte(byte[] result, int start, int end) {
        String temp = "";
        if (result != null) {
            if (result.length > (start + end)) {
                byte[] bytes = new byte[result.length - (start + end)];
                for (int i = start; i <= result.length - 1 - end; i++) {
                    bytes[i - start] = result[i];
                }
                temp = ByteConvertUtils.convertByteToASCII(bytes);
            }
        }
        return temp;
    }

    public static void main(String[] args) {

         ArrayList<byte[]> arrayList=new ArrayList<>();
        StringBuilder stringBuffer=new StringBuilder();
        ;
        arrayList.add(convertHexStringToBytes("6806d0004f4b0d0a8716"));
        for (int i = 0; i < arrayList.size(); i++) {
            stringBuffer.append(ByteConvertUtils.getASCIIbyByte(arrayList.get(i), 3, 2));
        }
        System.out.println(stringBuffer.toString());


        //   convertSymbleHexStringToBytes("62-86-12-15");

//        String s = "3E1E9E9F";
//        Float f = 8888899999.25f;
//        String kkk = Integer.toHexString(Float.floatToIntBits(f));
//        System.out.println(kkk);
//        byte[] result = convertHexStringToBytes(kkk);
//        for (int i = 0; i < result.length; i++) {
//            System.out.println(result[i]);
//        }
//        float k = Float.valueOf("-33222.144418881");
//        byte[] bytesk = ByteConvertUtils.convertHexStringToBytes(ByteConvertUtils.floatToHexStr(k));
//        System.out.println(byteToString(bytesk));
//        StringBuffer stringBuffer = new StringBuffer();
//        for (int i = 0; i < bytesk.length; i++) {
//            String temp = Integer.toHexString(bytesk[i] & 0xff);
//            if (temp.length() == 1) {
//                temp = "0" + temp;
//            }
//            stringBuffer.append(temp);
//        }
//
//        System.out.println();
//
//        double d = Double.valueOf("-3322.1232213");
//        byte[] bytesd = ByteConvertUtils.convertHexStringToBytes(ByteConvertUtils.doubletoHex(d));
//        System.out.println(byteToString(bytesd));
//        StringBuffer stringBufferd = new StringBuffer();
//        for (int i = 0; i < bytesd.length; i++) {
//            String temp = Integer.toHexString(bytesd[i] & 0xff);
//            if (temp.length() == 1) {
//                temp = "0" + temp;
//            }
//            stringBufferd.append(temp);
//        }
//        System.out.println(stringBufferd.toString());
//
//        System.out.println(ByteConvertUtils.hextoDouble(stringBufferd.toString()));
//        Byte[] result=new Byte[]{(byte)7d& 0xff,(byte)01& 0xff,00,00};
//
//        StringBuffer stringBuffer = new StringBuffer();
//        for (int i = 3; i >= 0; i--) {
//            stringBuffer.append(ByteConvertUtils.conver2BinaryStr(result[i + 0]));
//        }
//       String dianchi= String.valueOf(ByteConvertUtils.formatNumber(stringBuffer.toString(), 2));
//        System.out.println(dianchi);
//        DecimalFormat df = new DecimalFormat("#.00000000000");
//        double f=Double.valueOf(ByteConvertUtils.hextoDouble("0000017d"));
//        float f2=Float.valueOf(ByteConvertUtils.hextoFloat("0000017d"));
////        String result = df.format(f);
//        System.out.println(String.valueOf(f));
//        int zhi=Integer.parseInt("0000017d",16);

//        System.out.println(String.valueOf((float)zhi/100));
////
//
//        Float value = Float.intBitsToFloat(new BigInteger(kkk, 16).intValue());
//        //   Float value = Float.intBitsToFloat(Integer.valueOf(kkk.trim(), 16));
//        System.out.println(value);


//        double d = formatNumber("123456789.123",3);
//        String kkk =   ByteConvertUtils.doubletoHex(d);
//       String kk = Long.toHexString(Double.doubleToLongBits(d));
//            System.out.println(kkk);
//              byte[] result = convertHexStringToBytes(kkk);
//        for (int i = 0; i < result.length; i++) {
//            System.out.println(result[i]);
//        }


//        float f = Float.valueOf("888888996756756756756756776999.7812371293123");
//        String kkk = Integer.toHexString(Float.floatToIntBits(f));
//        String kkkshow="";
//        char[] arr = kkk.toCharArray();
//        for (int i = 0; i <arr.length/2 ; i++) {
//              kkkshow=  kkk.substring(2*i,2*i+2)+kkkshow;
//        }
//        System.out.println(kkkshow);


//        System.out.println(hextoDouble(doubletoHex(d)));
//
//        for (int i = 0; i <4 ; i++) {
//            System.out.println( bytesk[i]);
//        }
//        long isn = Long.parseLong("181122203030");
//        byte[] result = new byte[6];
//        for (int i = 0; i < 5; i++) {
//            result[i] = (byte) (isn & 0xff);
//            isn = isn >> 8;
//        }
//        System.out.println(byteToString(result));

//            String hex = "322e302e32";
//            byte[] bytes1 = {0x68, 0x07, (byte) 0x96, 0x00, 0x32, 0x2e, 0x30, 0x2e, 0x32, (byte) 0x8d, 0x16};
//            byte[] bytes = {0x32, 0x2e, 0x30, 0x2e, 0x32};
//        System.out.println(convertByteToHexString(bytes));  //默认长度为0
//        System.out.println(convertByteToASCII(bytes));  //默认长度为0
//        System.out.println(getASCIIbyByte(bytes1, 4, 2));  //默认长度为0
//
//        byte b = (byte) 234;
//        System.out.println(b & 0xff);
//            System.out.println(conver2BinaryStr(bytes1));

//        try {
        String parseBytes = "101a04bfffffffffffffffff3fffffffffffffffff0102ff08000000fc".substring(4, 56);//获取上送的26字节
        try {
//            parseIoTEquipment("", parseBytes);
            parseCivilMeter(parseBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
////                String str = new BigInteger("002a2bb97d96", 16).toString(10);
////                System.out.println(str);
////            String s = convertASCIITo16("AT+RX1DL=5");
////            String s = convertASCIITo16("OK");
////            System.out.println(s);
////            System.out.println(convert16ToASCII(s));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        System.out.println(convertASCIITo16("9600"));
//        System.out.println(convert16ToASCII(convertASCIITo16("9600")));
//        int isn = 9600;
//        byte[] bytes = new byte[3];
//        for (int i = 2; i > 0; i--) {
//            bytes[i] = (byte) (isn & 0xff);
//            isn = isn >> 8;
//        }
//        System.out.print(ByteConvertUtils.convertByteToHexString(bytes) + "\n");
//        System.out.print(result.reverse().toString());
//        long tmp = 0;
//        for (int j = 0; j < 3; j++) {
//            tmp = (tmp * 256) + (bytes[j] & 0xff);
//        }
//        System.out.print(tmp);
//        System.out.println(Double.longBitsToDouble(Long.valueOf("B81E85EB85E4FC40",16).longValue()));


//        String areaCode = "012345";
//        String provinceCode = areaCode.substring(0, 2);
//        System.out.print(areaCode.substring(0, 2));
//        System.out.print(areaCode.substring(2, 4));
//        System.out.print(areaCode.substring(4, 6));
//
//        String dataPar="0,0.001|1,0.01|2,0.1|3,1|4,10|5,100";
//        String[] strings=dataPar.split("|");
//        HashMap<String,String> hashMap=new HashMap<>();
//        for(int i=0;i<strings.length;i++){
//            String[] strings1=strings[i].split(",");
//            hashMap.put(strings1[0],strings1[1]);
//        }


    }


    /**
     * 将16进制转换为相应的数字
     *
     * @param bitString  0100000001010000011000100101111001000000000000000000000000000000
     * @param numberType
     * @return
     */
    public static String formatNumber(String bitString, int numberType) {
        if (numberType == 1) {
        } else if (numberType == 2) {
            new BigInteger(bitString, 2);
            return binarytoFloat(bitString);
        } else if (numberType == 3) {
            return binarytoDouble(bitString);
        }
        return "";
    }

    /**
     * 将float转换为16进制字符串
     *
     * @param f
     * @return
     */
    public static String floatToHexStr(float f) {
        return Integer.toHexString(Float.floatToIntBits(f));
    }

    /**
     * 十六进制转float
     *
     * @param hex
     * @return
     */
    public static String hextoFloat(String hex) {
        float f = Float.intBitsToFloat(new BigInteger(hex.trim(), 16).intValue());
        return String.valueOf(f);
    }

    public static String binarytoFloat(String binary) {
        XLog.i("原始数据=" + binary);
        float f = Float.intBitsToFloat(new BigInteger(binary.trim(), 2).intValue());
        return String.valueOf(f);
    }


    /**
     * 十六进制转double
     *
     * @param hex
     * @return
     */
    public static String hextoDouble(String hex) {
        long longHex = parseUnsignedHex(hex);
        double d = Double.longBitsToDouble(longHex);
        return String.valueOf(d);
    }

    public static long parseUnsignedHex(String text) {
        if (text.length() == 16) {
            return (parseUnsignedHex(text.substring(0, 1)) << 60)
                    | parseUnsignedHex(text.substring(1));
        }
        return Long.parseLong(text, 16);
    }


    public static String binarytoDouble(String binary) {
        XLog.i("原始数据1=" + binary);
        Double d = Double.longBitsToDouble(Long.parseLong(binary, 2));
        return String.valueOf(d);
    }

    /**
     * 十六进制转double
     *
     * @param d
     * @return
     */
    public static String doubletoHex(double d) {
        //return   Long.toHexString(Double.doubleToLongBits(0.1d)).length();
        return Long.toHexString(Double.doubleToLongBits(d));

    }


    public static final String byteToString(byte[] result) {
        StringBuilder sb = new StringBuilder();
        String temp;
        for (int i = 0; i < result.length; i++) {
            temp = Integer.toHexString(result[i] & 0xff);
            sb.append("(");
            sb.append(i);
            sb.append(")");
            if (temp.length() == 1) {
                sb.append(0);
            }
            sb.append(temp);
            if (i + 1 < result.length) {
                sb.append(' ');
            }
        }
        return sb.toString();
    }

    public static final String byteToString(byte[] result, boolean hasSerialNumber) {
        StringBuilder sb = new StringBuilder();
        String temp;
        for (int i = 0; i < result.length; i++) {
            temp = Integer.toHexString(result[i] & 0xff);

            if (hasSerialNumber) {
                sb.append("(");
                sb.append(i);
                sb.append(")");
            }
            if (temp.length() == 1) {
                sb.append(0);
            }
            sb.append(temp);
            if (i + 1 < result.length) {
                sb.append(' ');
            }
        }
        return sb.toString();
    }

    public static final String byteToString(byte[] result, int start, int end) {
        StringBuilder sb = new StringBuilder();
        String temp;
        for (int i = start; i <= result.length - 1 - end; i++) {
            temp = Integer.toHexString(result[i] & 0xff);
            sb.append("(");
            sb.append(i - start);
            sb.append(")");
            if (temp.length() == 1) {
                sb.append(0);
            }
            sb.append(temp);
            if (i + 1 < result.length) {
                sb.append(' ');
            }
        }
        return sb.toString();
    }

    /**
     * @param result
     * @param start
     * @param end
     * @param hasSerialNumber 是否有序号
     * @return
     */
    public static final String byteToString(byte[] result, int start, int end, boolean hasSerialNumber) {
        StringBuilder sb = new StringBuilder();
        String temp;
        for (int i = start; i <= result.length - 1 - end; i++) {
            temp = Integer.toHexString(result[i] & 0xff);
            if (hasSerialNumber) {
                sb.append("(");
                sb.append(i - start);
                sb.append(")");
            }
            if (temp.length() == 1) {
                sb.append(0);
            }
            sb.append(temp);
            if (i + 1 < result.length) {
                sb.append(' ');
            }
        }
        return sb.toString();
    }

    private static void parseIoTEquipment(String sn, String parseBytes) throws Exception {
        String char1_productForm = parseBytes.substring(0, 2);//产品形式
        String char2 = parseBytes.substring(2, 4);//传感信号
        String char3 = parseBytes.substring(4, 6);//设备状态
        String char4to8 = parseBytes.substring(6, 16);//设备ID
        String char9to12 = parseBytes.substring(16, 24);//脉冲数据记录1
        String char13to16 = parseBytes.substring(24, 32);//脉冲数据记录2
        String char17to20 = parseBytes.substring(32, 40);//脉冲数据记录1
        String char21 = parseBytes.substring(40, 42);//硬件版本号
        String char22 = parseBytes.substring(42, 44);//固件版本
        String char23to26 = parseBytes.substring(44, 52);//倒流值

        //设备状态
        String status = strToBinstr(char3);
        Byte valueSign1 = Byte.valueOf(status.substring(7, 8), 2);//第一组脉冲值的正负号
        Byte equipmentPowerState = Byte.valueOf(status.substring(6, 7), 2);//物联电池状态
        Byte valueSign2 = Byte.valueOf(status.substring(5, 6), 2);//第二组脉冲值的正负号
        Byte flowDirectionState = Byte.valueOf(status.substring(4, 5), 2);//流向状态
        Byte disassemblyState = Byte.valueOf(status.substring(3, 4), 2);//拆卸状态
        Byte magneticDisturbState = Byte.valueOf(status.substring(2, 3), 2);//磁场干扰
        Byte valueSign3 = Byte.valueOf(status.substring(1, 2), 2);//第三组脉冲值的正负号
        //待定Bit位  Byte.valueOf(status.substring(0, 1), 2);

        //产品形式
        Byte productForm = Byte.valueOf(char1_productForm, 16);

        //传感信号
        Byte sensingSignal = Byte.valueOf(char2, 16);


        //设备Id
        String meterId = Long.valueOf(char4to8.substring(8, 10) + char4to8.substring(6, 8) +
                char4to8.substring(4, 6) + char4to8.substring(2, 4) + char4to8.substring(0, 2), 16).toString();

        //实际脉冲数据1
        Integer pulseValue1 = Integer.parseInt(char9to12.substring(6, 8) + char9to12.substring(4, 6) +
                char9to12.substring(2, 4) + char9to12.substring(0, 2), 16);

        //实际脉冲数据2
        Integer pulseValue2 = Integer.parseInt(char13to16.substring(6, 8) + char13to16.substring(4, 6) +
                char13to16.substring(2, 4) + char13to16.substring(0, 2), 16);

        //实际脉冲数据3
        Integer pulseValue3 = Integer.parseInt(char17to20.substring(6, 8) + char17to20.substring(4, 6) +
                char17to20.substring(2, 4) + char17to20.substring(0, 2), 16);

        //硬件版本号
        //Float hardwareVersion = Float.intBitsToFloat(Integer.parseInt(char21,16));

        //软件版本号
        //Float softVersion = Float.intBitsToFloat(Integer.parseInt(char22,16));


        //倒流值
        Integer backflowValue = Integer.parseInt(char23to26.substring(6, 8) + char23to26.substring(4, 6) +
                char23to26.substring(2, 4) + char23to26.substring(0, 2), 16);

    }

    // 将字符串转换成二进制字符串//，以空格相隔
    private static String strToBinstr(String str) {
        char[] strChar = str.toCharArray();
        String result = "", tmp;
        for (int i = 0; i < strChar.length; i++) {// 加"0000"是为了
            // 当字符串为0时得到的结果为0从而变成00000，继而substring(tmp.length()
            // - 4)得到后面4个0000
            tmp = "0000"
                    + Integer.toBinaryString(Integer.parseInt(
                    String.valueOf(strChar[i]), 16));
            result += tmp.substring(tmp.length() - 4);
        }
        return result;
    }


    private static void parseCivilMeter(String parseBytes) throws Exception {
        String char1_productForm = parseBytes.substring(0, 2);//产品形式

        String char3to7 = parseBytes.substring(4, 14);//第一块表ID
        String char2 = parseBytes.substring(2, 4);//第一块表状态
        String char8to10 = parseBytes.substring(14, 20);//第一块表脉冲数
        String char11 = parseBytes.substring(20, 22);//第二块表状态
        String char12to16 = parseBytes.substring(22, 32);//第二块表ID
        String char17to19 = parseBytes.substring(32, 38);//第二块表脉冲数
        String char20 = parseBytes.substring(38, 40);//第一块表表号
        String char21 = parseBytes.substring(40, 42);//第二块表表号
        String char22 = parseBytes.substring(42, 44);//第一、二块表的脉冲常数
        String char23 = parseBytes.substring(44, 46);//硬件版本号
//                String  char24  =  parseBytes.substring(46,  48);//上送频率  +  月
//                String  char25  =  parseBytes.substring(48,  50);//日
//                String  char26  =  parseBytes.substring(50,  52);//年


        //第一块表表号
        Integer num1_meterNum = Integer.valueOf(char20, 16);

        //第二块表表号
        Integer num2_meterNum = Integer.valueOf(char21, 16);

        boolean hashMeter2 = false;//本次数据是否含有2号表,  默认没有


        byte powerType = 0;//电源类型:默认  0
        byte equipmentPowerState = -1;//物联电池状态
        byte gatherState = 0;//采集状态

        byte num1_magneticDisturbState = -1;//第一块表的磁场干扰
        byte num1_disassemblyState = -1;//第一块表的拆卸
        byte num1_readStatus = -1;//第一块表的读表状态
        byte num1_clientPowerState = -1;//第一块表的电源状态
        byte num1_flowDirectionState = -1;//流向状态（>1.04）
        byte num1_gatherState = 0;//第一块表的采集状态（>1.04）
        //byte  gatherScene  =  -1;//采集场景

        byte num2_magneticDisturbState = -1;//第二块表的磁场干扰
        byte num2_disassemblyState = -1;//第二块表的拆卸
        byte num2_readStatus = -1;//第二块表的读表状态
        byte num2_clientPowerState = -1;//第二块表的电源状态
        byte num2_flowDirectionState = -1;//流向状态（>1.04）
        byte num2_gatherState = 0;//第二块表的采集状态（>1.04）

        //解析第二字节
        String status1 = strToBinstr(char2);
        String status2 = strToBinstr(char11);
        double hardVersion = Double.parseDouble(getSoftWareVersion(Integer.parseInt(char23, 16)));

        if (hardVersion < 1.04) {
            hashMeter2 = (char21.equalsIgnoreCase("ff") ||
                    char12to16.equalsIgnoreCase("ffffffffff")) ? false : true;
            gatherState = Byte.valueOf(status1.substring(0, 1), 2);
            if (gatherState != 0) {//设备故障
                return;
            }
            //1.04版本以下，两个表的采集状态相同
            num1_gatherState = gatherState;
            num2_gatherState = gatherState;
        } else if (hardVersion >= 1.04) {
            hashMeter2 = Byte.valueOf(status1.substring(0, 1), 2) == 0 ? false : true;
            num1_gatherState = Byte.valueOf(status1.substring(2, 3), 2);
            num2_gatherState = Byte.valueOf(status2.substring(2, 3), 2);
            if (num1_gatherState != 0) {
                return;
            }
            if (hashMeter2 && num2_gatherState != 0) {
                return;
            }
        }


        if (hardVersion < 1.04) {
            num1_magneticDisturbState = Byte.valueOf(status1.substring(7, 8), 2);
            num1_disassemblyState = Byte.valueOf(status1.substring(6, 7), 2);
            num1_readStatus = Byte.valueOf(status1.substring(5, 6), 2);
            num1_clientPowerState = Byte.valueOf(status1.substring(4, 5), 2);
            equipmentPowerState = Byte.valueOf(status1.substring(2, 3), 2);
            //gatherScene = Byte.valueOf(status1.substring(1, 2), 2);
        } else if (hardVersion >= 1.04) {
            num1_magneticDisturbState = Byte.valueOf(status1.substring(7, 8), 2);
            num1_disassemblyState = Byte.valueOf(status1.substring(6, 7), 2);
            num1_readStatus = Byte.valueOf(status1.substring(5, 6), 2);
            num1_clientPowerState = Byte.valueOf(status1.substring(4, 5), 2);
            num1_flowDirectionState = Byte.valueOf(status1.substring(3, 4), 2);
            equipmentPowerState = Byte.valueOf(status1.substring(1, 2), 2);
        }


        if (hashMeter2) {//存在第二块表
            if (hardVersion < 1.04) {
                num2_magneticDisturbState = Byte.valueOf(status2.substring(7, 8), 2);
                num2_disassemblyState = Byte.valueOf(status2.substring(6, 7), 2);
                num2_readStatus = Byte.valueOf(status2.substring(5, 6), 2);
                num2_clientPowerState = Byte.valueOf(status2.substring(4, 5), 2);
                //String softVersion = Integer.valueOf(status2.substring(0, 4), 2).toString();//软件版本
            } else if (hardVersion >= 1.04) {
                num2_magneticDisturbState = Byte.valueOf(status2.substring(7, 8), 2);
                num2_disassemblyState = Byte.valueOf(status2.substring(6, 7), 2);
                num2_readStatus = Byte.valueOf(status2.substring(5, 6), 2);
                num2_clientPowerState = Byte.valueOf(status2.substring(4, 5), 2);
                num2_flowDirectionState = Byte.valueOf(status2.substring(3, 4), 2);
            }
        }

        //产品形式
        Byte productForm = Byte.valueOf(char1_productForm, 16);

        //第一、二块表的脉冲常数
        String pulseConstant = strToBinstr(char22);


    }

    public static final String getSoftWareVersion(int number) {
        number &= 0xff;//取两个byte
        int a = 1;
        a += number / 100;//默认值为1 如果数值大于100则取商+1
        int b = (number % 100) / 10;//默认值为0 如果数值大于10则取商
        int c = number % 10;//默认值为0 如果数值大于10则取余
        StringBuilder sb = new StringBuilder();
        sb.append(a);
        sb.append('.');
        sb.append(b);
        sb.append(c);
        return sb.toString();
    }
}
