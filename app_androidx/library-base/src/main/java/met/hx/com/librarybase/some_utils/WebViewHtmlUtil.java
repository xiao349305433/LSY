package met.hx.com.librarybase.some_utils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class WebViewHtmlUtil {

    /**
     * 返回该链接地址的html数据
     *
     * @param urlStr
     * @return
     */
    public static String doGet(String urlStr) throws Exception {
        StringBuffer sb = new StringBuffer();
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(5000);
        conn.setDoInput(true);
        conn.setDoOutput(true);

        if (conn.getResponseCode() == 200) {
            InputStream is = conn.getInputStream();
            int len = 0;
            byte[] buf = new byte[1024];

            while ((len = is.read(buf)) != -1) {
                sb.append(new String(buf, 0, len, "UTF-8"));
            }

            is.close();
        }

        return sb.toString();
    }

    public static byte[] readStream(InputStream inputStream) throws Exception {
        byte[] buffer = new byte[1024];
        int len = -1;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        while ((len = inputStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, len);
        }

        inputStream.close();
        byteArrayOutputStream.close();
        return byteArrayOutputStream.toByteArray();
    }

    public static String testGetHtml(String urlpath) throws Exception {
        URL url = new URL(urlpath);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(6 * 1000);
        conn.setRequestMethod("GET");

        if (conn.getResponseCode() == 200) {
            InputStream inputStream = conn.getInputStream();
            byte[] data = readStream(inputStream);
            String html = new String(data);
            return html;
        }
        return null;
    }

    /**
     * 过滤HTML标签
     * @param input
     * @return
     */
    public static String filterHtml(String input) {
        Pattern p = Pattern.compile("<.*?>");
        Matcher matcher = p.matcher(input);
        return matcher.replaceAll("");
    }
}
