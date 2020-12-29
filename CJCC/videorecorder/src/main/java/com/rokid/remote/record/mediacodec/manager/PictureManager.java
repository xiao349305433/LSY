package com.rokid.remote.record.mediacodec.manager;

import com.rokid.glass.videorecorder.camera.Camera2Manager;
import com.rokid.recordapi.RKMediaCallback;
import com.rokid.remote.record.util.RKSingleton;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Author: heshun
 * Date: 2020/4/22 2:45 PM
 * gmail: shunhe1991@gmail.com
 */
public class PictureManager {
    private static RKSingleton<PictureManager> singleton = new RKSingleton<PictureManager>() {
        @Override
        public PictureManager create() {
            return new PictureManager();
        }
    };

    private PictureManager() {
    }

    public static PictureManager getInstance() {
        return singleton.get();
    }

    private boolean result = false;
    static String pattern = "yyyy-MM-dd HH:mm:ss:SSS";
    private static SimpleDateFormat dateFormat = new SimpleDateFormat(pattern, Locale.CHINA);

    public void takePicture(RKMediaCallback mediaCallback) {
        Camera2Manager.getInstance().takePicture();
    }
}
