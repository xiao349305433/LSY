package met.hx.com.librarybase.some_utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GetTradeNoUtil {
    public static String getTard(String keys) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();
        String key = format.format(date);

        java.util.Random r = new java.util.Random();
        String s = r.nextInt() + "";
        if (s.contains("-")) {
            s = s.replace("-", "").trim();
        }
        key += s;
        key = key.substring(0, 19);
        key = keys + key;
        return key;

    }

    public static String getTardeNo_Bao(String keys) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        Date date = new Date();
        String key = format.format(date);

        java.util.Random r = new java.util.Random();
        String s = r.nextInt() + "";
        if (s.contains("-")) {
            s = s.replace("-", "").trim();
        }
        key += s;
        key = key.substring(0, 18);
        key = keys + key;
        return key;

    }

    public static String getTard() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();
        String key = format.format(date);

        java.util.Random r = new java.util.Random();
        String s = r.nextInt() + "";
        if (s.contains("-")) {
            s = s.replace("-", "").trim();
        }
        key += s;
        key = key.substring(0, 19);
        return key;

    }

    public static String getTard(String keys, int n) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();
        String key = format.format(date);

        java.util.Random r = new java.util.Random();
        String s = r.nextInt() + "";
        if (s.contains("-")) {
            s = s.replace("-", "").trim();
        }
        key += s;
        key = key.substring(0, n);
        key = keys + key;
        return key;

    }

}
