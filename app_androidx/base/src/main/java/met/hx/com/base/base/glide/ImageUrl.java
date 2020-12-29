package met.hx.com.base.base.glide;

import android.text.TextUtils;

/**
 * Created by huxu on 2017/11/15.
 */

public class ImageUrl {
    /*
     * 网络图片显示时的大中小类型。
     */
    public static final String PICTURE_BIG = "big";
    public static final String PICTURE_MIDDLE = "middle";
    public static final String PICTURE_SMALL = "small";

    public static String PIC_HTTPURL = "";

    public static void setPicURL(String url) {
        PIC_HTTPURL = url;
    }
    public static String getPicURL() {
        return PIC_HTTPURL;
    }
    public static String packingUrlBySizeSpecification(String url, String type) {
        if (TextUtils.isEmpty(type)) {
            return url;
        }
        if (!PICTURE_BIG.equals(type)) {
            int i = url.indexOf("Uploads/");
            if (i != -1) {
                StringBuilder sb = new StringBuilder();
                if (PICTURE_MIDDLE.equals(type)) {
                    sb.append(url).insert(i + "Uploads/".length(), "Uploads_middle/");
                    url = sb.toString();
                } else if (PICTURE_SMALL.equals(type)) {
                    sb.append(url).insert(i + "Uploads/".length(), "Uploads_small/");
                    url = sb.toString();
                }
            }
        }
        return url;
    }

    public static String getCompleteUrl(String url, String imgPrefix) {
        if (url.length() > 5) {
            if (!url.contains("http")) {
                if (url.contains("storage") || url.contains("sdcard") || url.contains("system")) {

                } else {
                    url = imgPrefix + url;
                }
            } else {
                //完整地址而且排除有两个前缀的情况
                url = url.substring(url.lastIndexOf("http"), url.length());
            }
        }
        return url;
    }
}
