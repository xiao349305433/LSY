package met.hx.com.librarybase.some_utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import met.hx.com.librarybase.R;

/**
 * 工具类
 */
public final class DateAlertDialogUtils {
    /** 一年时间的毫秒值 */
    public static long oneYearMillis = 365l * 24 * 60 * 60 * 1000;
    private static Toast toast = null;

    /** Toast内容 单例模式 */
    public static void showToast(Context context, String text) {
        if (toast == null) {
            toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        } else {
            toast.setText(text);
        }
        toast.show();
    }


    public static void showDialog(){
//        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setTitle("请选择车辆类型");
//        builder.setItems(new String[]{"小型车", "大型车"}, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                switch (i) {
//                    case 0:
//                        carType.setText("小型车");
//                        carType.setTextColor(getResources().getColor(R.color.black_s));
//                        break;
//                    case 1:
//                        carType.setText("大型车");
//                        carType.setTextColor(getResources().getColor(R.color.black_s));
//                        break;
//                }
//            }
//        });
//        builder.show();
    }

    /**
     * 日期对话框
     * @param context
     * @param intervalTime 间隔时间，正值，为0时表示间隔1年
     * @param tvList 日期显示位置
     * @return 日期的字符串形式集合
     */
    public static void showDateDialog(Context context, long intervalTime
            , final TextView... tvList) {

        if (intervalTime < 0) {
            throw new IllegalArgumentException("intervalTime 的值必须大于等于0！");
        }
        if (tvList.length <= 0) {
            throw new IllegalArgumentException("tvList 的值不能为空！");
        }
        intervalTime = ((intervalTime == 0) ? oneYearMillis : intervalTime);
        final long finalIntervalTime = intervalTime;
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dateView = LayoutInflater.from(context).inflate(R.layout.base_utils_date_dialog, null);
        final DatePicker datePicker = (DatePicker) dateView.findViewById(R.id.date_picker);
        datePicker.setCalendarViewShown(false);
        datePicker.init(year, month, day, null);
        builder.setView(dateView);
        builder.setTitle("选择日期信息");
        builder.setPositiveButton("确 定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //日期格式
                List<String> dateList = new ArrayList<String>();
                Calendar calendar = Calendar.getInstance();
                calendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                String dateStr = formatDate("yyyy-MM-dd", calendar.getTime());
                tvList[0].setText(dateStr);
                dateList.add(dateStr);
                if ((finalIntervalTime != 0) && (tvList.length > 1)) {
                    long seconds = calendar.getTimeInMillis();
                    long oneYear = finalIntervalTime + seconds;
                    String time = formatDate("yyyy年MM月dd日", new Date(oneYear));
                    tvList[1].setText("00起 至" + time + " 24时止");
                    dateList.add(formatDate("yyyy-MM-dd", new Date(oneYear)));
                }
                tvList[0].setTag(dateList);
                dialog.cancel();
            }
        });
        builder.setNegativeButton("取 消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.create().show();

    }

    /**
     * 格式化日期
     * @param pattern 给定日期格式
     * @param date 需要格式化的日期
     * @return 格式化后日期的字符串形式
     */
    public static String formatDate(String pattern, Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(date);
    }

    /**
     * 以当前时间为开始日期，设置日期
     * @param intervalTime 时间间隔，正值，为0时表示间隔1年
     * @param tvList 日期显示位置
     * @return 截止日期的毫秒值
     */
    public static long setDefaultDate(long intervalTime, TextView... tvList) {
        if (intervalTime < 0) {
            throw new IllegalArgumentException("intervalTime 的值必须大于等于0！");
        }
        if (tvList.length <= 0) {
            throw new IllegalArgumentException("tvList 的值不能为空！");
        }
        long currentTime = System.currentTimeMillis();
        intervalTime = (intervalTime == 0) ? oneYearMillis : intervalTime;
        long OneYearAgo = currentTime + intervalTime;
        tvList[0].setText(formatDate("yyyy-MM-dd", new Date(currentTime)));
        if (tvList.length > 1) {
            tvList[1].setText("00起 至" + formatDate("yyyy年MM月dd日", new Date(OneYearAgo)) + " 24时止");
        }
        return OneYearAgo;
    }

    public static String setEndDate(long intervalTime, String beginDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = dateFormat.parse(beginDate);
            long endMills = date.getTime() + intervalTime;
            String f = "00起 至" + formatDate("yyyy年MM月dd日", new Date(endMills)) + " 24时止";
            return f;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
