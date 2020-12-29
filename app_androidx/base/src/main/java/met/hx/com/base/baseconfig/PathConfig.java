package met.hx.com.base.baseconfig;

import android.os.Environment;

import java.io.File;

/**
 * Created by huxu on 2016/12/21.
 */

public class PathConfig {

    private static String ROOT = "/loushanyun";
    private static String downloadFileDir = ROOT + "/loushanyun_download/";
    private static String imagePath = ROOT + "/loushanyun_image";


    public static String getROOT() {

        return getDirPath(ROOT);
    }

    public static String getDownloadFileDir() {
        return getDirPath(downloadFileDir);
    }

    public static String getImagePath() {
        return getDirPath(imagePath);
    }

    public static String getDirPath(String path) {
        File imageDir = null;
        File extStorageDir = Environment.getExternalStorageDirectory();
        if (extStorageDir.canWrite()) {
            imageDir = new File(extStorageDir.getPath()+path);
        }
        if (imageDir != null) {
            if (!imageDir.exists()) {
                imageDir.mkdirs();
            }
            if (imageDir.canWrite()) {
                path = imageDir.getPath();
            }
        }
        return path;
    }

    /**
     * 外置存储卡的路径
     *
     * @return
     */
    public static String getExternalStorePath() {
        if (isExistExternalStore()) {
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        }
        return null;
    }

    /**
     * 是否有外存卡
     *
     * @return
     */
    public static boolean isExistExternalStore() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }
}
