package com.rokid.remote.record.util;


import android.os.Environment;

import com.rokid.glass.videorecorder.utils.Logger;
import com.rokid.remote.record.model.MediaType;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.UUID;

/**
 * Author: heshun
 * Date: 2020/4/16 8:46 PM
 * gmail: shunhe1991@gmail.com
 */
public class PathUtils {


    public enum FILE_TYPE {
        VIDEO(MediaType.VIDEO, "VIDEO", ".mp4"),
        AUDIO(MediaType.AUDIO, "AUDIO", ".aac"),
        JPEG(MediaType.PHOTO, "PHOTO", ".jpg"),
        H264(MediaType.PHOTO, "VIDEO", ".h264");
        public String prefix;
        public String suffix;
        public String type;

        FILE_TYPE(String type, String prefix, String suffix) {
            this.type = type;
            this.prefix = prefix;
            this.suffix = suffix;
        }
    }

    private static String absolutePath;
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.CHINA);
    private static SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);


    static {
//        absolutePath = ContextUtil.getApplicationContext().getFilesDir().getAbsolutePath();
        absolutePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/RokidRecording";
    }

    public static String mkdirs(FILE_TYPE fileType) {
        StringBuilder sb = new StringBuilder();

        String fileDir = sb.append(absolutePath)
                .append(File.separator)
                .append(fileType.prefix)
                .append(File.separator)
                .append(date.format(new GregorianCalendar().getTime()))
                .toString();
        File file = new File(fileDir);
        if (file.exists() && file.isDirectory()) {
            return fileDir;
        }
        if (!file.mkdirs()) Logger.e("what happened, mkdirs failed:", fileDir);
        return fileDir;
    }

    public static String generateFileName(FILE_TYPE fileType) {
        StringBuilder sb = new StringBuilder();
        String uuid = UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
        //生成后缀
        long suffix = Math.abs(uuid.hashCode() % 100000000);
        String parentDir = mkdirs(fileType);
        String ret = sb.append(parentDir).append(File.separator)
                .append(fileType.prefix)
                .append("-")
                .append(simpleDateFormat.format(new GregorianCalendar().getTime()))
                .append("-")
                .append(suffix)
                .append(fileType.suffix)
                .toString();
        Logger.i("save file path", ret);
        return ret;
    }

    public static FileInfo createFile(FILE_TYPE fileType) {
        StringBuilder sb = new StringBuilder();
        String uuid = UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
        //生成后缀
        long suffix = Math.abs(uuid.hashCode() % 100000000);
        String parentDir = mkdirs(fileType);
        String fileName = sb.append(fileType.prefix)
                .append("-")
                .append(simpleDateFormat.format(new GregorianCalendar().getTime()))
                .append("-")
                .append(suffix)
                .append(fileType.suffix)
                .toString();
        sb = new StringBuilder();
        String path = sb.append(parentDir).append(File.separator).append(fileName).toString();

        Logger.i("save file path", path);
        return new FileInfo(parentDir, fileName, path);
    }

    public static class FileInfo {
        private String dir;
        private String name;
        private String path;


        public FileInfo(String dir, String name, String path) {
            this.dir = dir;
            this.name = name;
            this.path = path;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getDir() {
            return dir;
        }

        public void setDir(String dir) {
            this.dir = dir;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}
