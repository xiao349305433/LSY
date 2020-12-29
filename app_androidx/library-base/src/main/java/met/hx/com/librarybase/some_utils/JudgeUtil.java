package met.hx.com.librarybase.some_utils;

import android.text.TextUtils;


import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.regex.Pattern;

public class JudgeUtil {
    public static String Judge(String text) {
        String msg = "";
        if (text.startsWith("0.0000")) {

            msg = "0.0001";
        } else {

            Float f = Float.parseFloat(text);
            if (f < 0) {
                if (text.length() > 7) {
                    BigDecimal bigDecimal = new BigDecimal(f);
                    float d = bigDecimal.setScale(4, RoundingMode.HALF_UP)
                            .floatValue();
                    msg = d + "";
                } else {
                    msg = text;
                }
            } else {

                BigDecimal bigDecimal = new BigDecimal(f);
                float d = bigDecimal.setScale(4, RoundingMode.HALF_UP)
                        .floatValue();
                msg = d + "";
            }
        }

        return msg;

    }

    public static Float transfolat(float f1, int n) {
        float f2 = 0;
        if (f1 > 0) {
            f2 = transfolats(f1, n);
        }
        return f2;

    }

    public static Float transfolats(float f1, int n) {
        BigDecimal bigDecimal = new BigDecimal(f1);
        float f2 = bigDecimal.setScale(n, RoundingMode.HALF_UP).floatValue();
        return f2;

    }

    public static String transfolatstring(String f1, int n) {
        if (TextUtils.isEmpty(f1)) {
            return "0";
        }
        BigDecimal bigDecimal = new BigDecimal(f1);
        float f2 = bigDecimal.setScale(n, RoundingMode.HALF_UP).floatValue();
        String num = numFormat(f2 + "");
        return num;
    }

    public static float transfolats(String f1, int n) {
        if (TextUtils.isEmpty(f1)) {
            return 0f;
        }
        BigDecimal bigDecimal = new BigDecimal(f1);
        float f2 = bigDecimal.setScale(n, RoundingMode.HALF_UP).floatValue();
        return f2;
    }

    public static String transfolatCheckLastFor(String f1, int n) {
        if (TextUtils.isEmpty(f1)) {
            return "0";
        } else if (f1.endsWith(".0000") || f1.endsWith(".000") || f1.endsWith(".00") || f1.endsWith(".0")) {
            return f1.split("\\.")[0];
        } else {
            return transfolatstring(f1, n);
        }


    }

    public static String transdouble(double f1, int n) {
        String f2 = "";
        if (f1 != 0) {
            BigDecimal bigDecimal = new BigDecimal(f1);
            f2 = bigDecimal.setScale(n, RoundingMode.HALF_UP).toString();
        } else if (f1 == 0) {
            f2 = "0";
        }
        return TextUtils.isEmpty(f2) ? "0.00" : f2;

    }

    public static double transdoubles(double f1, int n) {
        double f2 = 0;
        if (f1 != 0) {
            BigDecimal bigDecimal = new BigDecimal(f1);
            f2 = bigDecimal.setScale(n, RoundingMode.HALF_UP).doubleValue();
        } else if (f1 == 0) {
            f2 = 0;
        }
        return f2;

    }
    public static double transdoubles(double f1, int n,RoundingMode mode,int len) {
        String s=f1+"";
        if(s.contains(".")){
            //len 为直接返回的最大长度,例如len=2
            //如果只有两位小数比如 0.02 直接返回,因为0.02经过BigDecimal可能会变成0.020000000001等
            //如果mode为up f1会变成0.03影响结果
            String s1=s.split("\\.")[1];
            if (s1.length()<=len){
                return f1;
            }
        }
        double f2 = 0;
        if (f1 != 0) {
            BigDecimal bigDecimal = new BigDecimal(f1);
            f2 = bigDecimal.setScale(n, mode).doubleValue();
        } else if (f1 == 0) {
            f2 = 0;
        }
        return f2;

    }

    public static double transdoubles(String f1, int n) {
        double f2 = 0;
        if (TextUtils.isEmpty(f1)) {
            BigDecimal bigDecimal = new BigDecimal(f1);
            f2 = bigDecimal.setScale(n, RoundingMode.HALF_UP).doubleValue();
        } else {
            f2 = 0;
        }
        return f2;

    }

    public static String numFormat(String num) {
        NumberFormat format = NumberFormat.getNumberInstance();
        format.setMaximumFractionDigits(2);
        format.setMinimumFractionDigits(2);
        format.setGroupingUsed(false);//不显示科学计数法
        return format.format(Double.parseDouble(num));
    }

    public static String numFormat(double num) {
        NumberFormat format = NumberFormat.getNumberInstance();
        format.setMaximumFractionDigits(2);
        format.setMinimumFractionDigits(2);
        format.setGroupingUsed(false);//不显示科学计数法
        return format.format(num);
    }

    /**
     * 判断文件是否存在
     *
     * @param path 路径
     * @return返回
     * @author txx
     */
    public static boolean isFileExist(String path) {
        File file = new File(path);
        if (file.exists()) {
            return true;
        } else {

            return false;
        }

    }


    /**
     * @param money_text 输入的金额
     * @return 格式是否正确
     * @author txx
     */
    public static boolean isMoneyFormatRight(String money_text) {
        if (TextUtils.isEmpty(money_text)) {
            return false;
        }
        if (money_text.equals(".")) {
            return false;
        }
        if (money_text.startsWith(".") || money_text.endsWith(".") || money_text.startsWith("00")) {
            return false;
        }
        if (Float.parseFloat(money_text) == 0) {
            return false;
        }
        return true;

    }

    /**
     * 判断是不是数字(包含小数点)
     *
     * @return
     */
    public static boolean isNumber(String str) {
        Pattern pattern = Pattern.compile("-?[0-9]*.?[0-9]*");
//        Pattern pattern1 = Pattern.compile("[0-9]");
        return pattern.matcher(str).matches();
    }

}
