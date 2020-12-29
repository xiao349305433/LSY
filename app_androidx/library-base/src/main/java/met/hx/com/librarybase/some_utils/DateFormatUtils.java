package met.hx.com.librarybase.some_utils;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressLint("SimpleDateFormat")
public final class DateFormatUtils {

    public static final SimpleDateFormat dfMdHm = new SimpleDateFormat("M月d日  HH:mm");

    public static final SimpleDateFormat dfYMdHm = new SimpleDateFormat("y年M月d日 HH:mm");
    public static final SimpleDateFormat dfYMdHmss = new SimpleDateFormat("y年M月d日 HH:mm:ss");

    public static final SimpleDateFormat dfYMdHm_ = new SimpleDateFormat("y_M_d_ HH:mm");
    public static final SimpleDateFormat df_ymdhm_middle = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    public static final SimpleDateFormat dfSlash = new SimpleDateFormat("y/M/d HH:mm");

    public static final SimpleDateFormat dfSlashYMd = new SimpleDateFormat("y/M/d");

    public static final SimpleDateFormat dfSlashMd = new SimpleDateFormat("MM/dd HH:mm");

    public static final SimpleDateFormat dfYMd = new SimpleDateFormat("y-M-d");// 2016-5-9

    public static final SimpleDateFormat dfYMMdd = new SimpleDateFormat("yyyy-MM-dd"); // 2016-05-09

    public static final SimpleDateFormat dfYMdHms = new SimpleDateFormat("yyyy-MM-dd-HHmmssSSS");
    public static final SimpleDateFormat dfYMdH = new SimpleDateFormat("yyyy-MM-dd-HH");

    private DateFormatUtils() {
    }

    public static String formatMdHm(long time) {
        time = time * 1000;
        try {
            if (DateUtils.isInCurrentYear(time)) {
                return dfMdHm.format(new Date(time));
            } else {
                return dfYMdHm.format(new Date(time));
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return "";
    }
    public static String formatMdHmss(long time) {
        try {
            if (DateUtils.isInCurrentYear(time)) {
                return dfYMdHmss.format(new Date(time));
            } else {
                return dfYMdHmss.format(new Date(time));
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String format(long time, SimpleDateFormat df) {
        time = time * 1000;
        try {
            return df.format(new Date(time));
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 时间格式化
     * author tangshasha
     */

    public static String format2(long time, SimpleDateFormat df) {
        try {
            return df.format(new Date(time));
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getTime(String str, String type) {
        SimpleDateFormat sdf = new SimpleDateFormat(type);
        try {
            return sdf.format(new Date(Long.parseLong(str) * 1000));
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getTime(long str, String type) {
        SimpleDateFormat sdf = new SimpleDateFormat(type);
        try {
            return sdf.format(new Date(str * 1000));
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 将日期字符串解析为时间戳
     * @param str 日期
     * @param dateFormat 日期格式
     * @return
     */
    public static long parseToTime(String str, SimpleDateFormat dateFormat) {
        try {
            Date date = dateFormat.parse(str);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
