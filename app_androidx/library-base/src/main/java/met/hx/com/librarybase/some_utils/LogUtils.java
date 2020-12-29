package met.hx.com.librarybase.some_utils;

import android.text.TextUtils;
import android.util.Log;

@Deprecated
public class LogUtils {
    public static boolean flag = true;
    private static String mTag ;

    /**
     * @param tag 自定义标识
     * @param msg 信息
     */
    public static void e(String tag, String msg) {
        e(tag, msg, null);
    }

    /**
     * @param msg 信息
     */
    public static void e(String msg) {
        e(null, msg);
    }

    public static void e(Throwable e) {
        e(null, "", e);
    }

    /**
     * @param tag 自定义标识
     * @param msg 信息
     * @param tr  异常
     */
    public static void e(String tag, String msg, Throwable tr) {
        if (flag) {
            if (msg != null) {
                setTag(tag);
                if (tr == null) {
                    Log.e(mTag, msg + ".");
                } else {
                    Log.e(mTag, msg + ".", tr);
                }
            }
        }
    }

    /**
     * @param tag 自定义标识
     * @param msg 信息
     */
    public static void d(String tag, String msg) {
        d(tag, msg, null);
    }

    /**
     * @param msg 信息
     */
    public static void d(String msg) {
        d(null, msg);
    }

    /**
     * @param tag 自定义标识
     * @param msg 信息
     * @param tr  异常
     */
    public static void d(String tag, String msg, Throwable tr) {
        if (flag) {
            if (msg != null) {
                setTag(tag);
                if (tr == null) {
                    Log.d(mTag, msg + ".");
                } else {
                    Log.d(mTag, msg + ".", tr);
                }

            }
        }
    }

    /**
     * @param tag 自定义标识
     * @param msg 信息
     */
    public static void i(String tag, String msg) {
        i(tag, msg, null);
    }

    /**
     * @param msg 信息
     */
    public static void i(String msg) {
        i(null, msg);
    }

    /**
     * @param tag 自定义标识
     * @param msg 信息
     * @param tr  异常
     */
    public static void i(String tag, String msg, Throwable tr) {
        if (flag) {
            if (msg != null) {
                setTag(tag);
                if (tr == null) {
                    Log.i(mTag, msg + ".");
                } else {
                    Log.i(mTag, msg + ".", tr);
                }
            }
        }
    }

    /**
     * @param tag 自定义标识
     * @param msg 信息
     */
    public static void w(String tag, String msg) {
        w(tag, msg, null);
    }

    /**
     * @param msg 信息
     */
    public static void w(String msg) {
        w(null, msg);
    }

    /**
     * @param tag 自定义标识
     * @param msg 信息
     * @param tr  异常
     */
    public static void w(String tag, String msg, Throwable tr) {
        if (flag) {
            if (msg != null) {
                setTag(tag);
                if (tr == null) {
                    Log.w(mTag, msg + ".");
                } else {
                    Log.w(mTag, msg + ".", tr);
                }
            }
        }
    }

    /**
     * 点击Log跳转到指定源码位置
     *
     * @param tag
     * @param msg
     */
    public static void showLog(String tag, String msg) {
        if (flag && !TextUtils.isEmpty(msg)) {
            if (TextUtils.isEmpty(tag)) tag = mTag;
            StackTraceElement[] stackTraceElement = Thread.currentThread().getStackTrace();
            int currentIndex = -1;
            for (int i = 0; i < stackTraceElement.length; i++) {
                if (stackTraceElement[i].getMethodName().compareTo("showLog") == 0) {
                    currentIndex = i + 1;
                    break;
                }
            }
            if (currentIndex >= 0) {
                String fullClassName = stackTraceElement[currentIndex].getClassName();
                String className = fullClassName.substring(fullClassName
                        .lastIndexOf(".") + 1);
                String methodName = stackTraceElement[currentIndex].getMethodName();
                String lineNumber = String
                        .valueOf(stackTraceElement[currentIndex].getLineNumber());

                Log.i(tag, msg + "\n  ---->at " + className + "." + methodName + "("
                        + className + ".java:" + lineNumber + ")");
            } else {
                Log.i(tag, msg);
            }
        }
    }

    /**
     * @param tag 自定义标识
     * @param msg 信息
     */
    public static void v(String tag, String msg) {
        v(tag, msg, null);
    }

    /**
     * @param msg 信息
     */
    public static void v(String msg) {
        v(null, msg);
    }

    /**
     * @param tag 自定义标识
     * @param msg 信息
     * @param tr  异常
     */
    public static void v(String tag, String msg, Throwable tr) {
        if (flag) {
            if (msg != null) {
                setTag(tag);
                if (tr == null) {
                    Log.v(mTag, msg + ".");
                } else {
                    Log.v(mTag, msg + ".", tr);
                }
            }
        }
    }

    private static void setTag(String tag) {
        if (!XHStringUtil.isEmpty(tag, false)) {
            mTag = tag;
        } else {
            mTag = "LSY:";
        }
    }
}
