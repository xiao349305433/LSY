package com.rokid.remote.record;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.rokid.glass.videorecorder.utils.Logger;
import com.rokid.glass.videorecorder.utils.MD5Utils;
import com.rokid.recordapi.RecordEntity;
import com.rokid.remote.record.database.RecordDatabase;
import com.rokid.remote.record.util.PathUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Author: heshun
 * Date: 2020/7/7 7:36 PM
 * gmail: shunhe1991@gmail.com
 */
public class RecordDataBaseManager {

    public static final String RECORD_DATABASE_EVENT = "record_event";

    private static RecordDataBaseManager instance = new RecordDataBaseManager();

    private Handler recordHandler;

    private RecordDataBaseManager() {
        HandlerThread handlerThread = new HandlerThread("record_database");
        handlerThread.start();
        recordHandler = new Handler(handlerThread.getLooper());
    }

    public static RecordDataBaseManager getInstance() {
        return instance;
    }

    public void insertData(String path) {
        Logger.d("insertData------>path = " + path);
        File file = new File(path);
        if (!file.exists()) return;
        final long createTime = file.lastModified();
        RecordEntity recordEntity = new RecordEntity();
        recordEntity.setFileName(file.getName());
        recordEntity.setFilePath(path);
        recordEntity.setCreateTime(createTime);
        if (path.contains(PathUtils.FILE_TYPE.JPEG.prefix)) {
            recordEntity.setMediaType(PathUtils.FILE_TYPE.JPEG.prefix);
            RecordDatabase.getInstance().recordDao().addRecord(recordEntity);
            return;
        }

        String durationStr = getMediaFileDuration(file.getAbsolutePath());
        if (TextUtils.isEmpty(durationStr)) {
            Logger.d("record media file duration by getMediaFileDuration but durationStr is empty.");
            durationStr = getMediaDuration(file.getAbsolutePath());
        }
        Logger.d("record media file duration:", durationStr);

        if (TextUtils.isEmpty(durationStr)) {
            //异步线程中通过mediaplayer获取duration 比较稳定，主线程中直接用mediaplayer获取duration会出现prepare失败，如果此处为空，则获取时异常，需要慎重处理
            durationStr = "0";
//            return;
        }
        long duration = Long.parseLong(durationStr) / 1000;
        recordEntity.setDuration(duration);

        if (path.contains(PathUtils.FILE_TYPE.VIDEO.prefix)) {
            String thumbnailPath = saveAndGetThumbnailBitmap(path, 1280, 720, MediaStore.Video.Thumbnails.MINI_KIND);
            recordEntity.setThumbnailPath(thumbnailPath);
            recordEntity.setMediaType(PathUtils.FILE_TYPE.VIDEO.prefix);

        } else if (path.contains(PathUtils.FILE_TYPE.AUDIO.prefix)) {
            recordEntity.setMediaType(PathUtils.FILE_TYPE.AUDIO.prefix);

        } else {
            Logger.w("what? unknown data!!!", path);
        }

        RecordDatabase.getInstance().recordDao().addRecord(recordEntity);

    }

    public void init() {
    }

    private String getMediaFileDuration(String filePath) {
        MediaMetadataRetriever mmr = null;
        try {
            mmr = new MediaMetadataRetriever();
            mmr.setDataSource(new File(filePath).getAbsolutePath());
            final String durationStr = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            return durationStr;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != mmr) {
                mmr.release();
            }
        }
        return null;
    }

    private String getMediaDuration(String path){
        MediaPlayer mp = new MediaPlayer();
        try {
            mp.reset();
            mp.setDataSource(path);
            mp.prepare();
            Logger.i("获取Duration失败 通过MediaPlayer获取");
            String duration = String.valueOf(mp.getDuration());
            mp.release();
            return duration;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private String saveAndGetThumbnailBitmap(String path, int width, int height, int kind) {
//        Bitmap bitmap = null;
//        try {
//            bitmap = ThumbnailUtils.createAudioThumbnail(new File(path), new Size(width, height), null);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
////        bitmap = ThumbnailUtils.createAudioThumbnail(path, kind);
//        if (bitmap == null) {
//            bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
//        }

        Bitmap bitmap = null;
        //MediaMetadataRetriever 是android中定义好的一个类，提供了统一
        //的接口，用于从输入的媒体文件中取得帧和元数据；
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            //根据文件路径获取缩略图
            retriever.setDataSource(path);
            //获得第一帧图片
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } finally {
            retriever.release();
        }
        String parent = new File(path).getParent();
        String thumbnailPath = parent + File.separator + MD5Utils.getMD5String(path);
        saveBitmap2File(bitmap, thumbnailPath, 100);
        return thumbnailPath;
    }

    public static void saveBitmap2File(Bitmap bitmap, String path, int quality) {
        if (bitmap == null || bitmap.isRecycled()) return;
        OutputStream os = null;
        try {
            //写大图到缓存目录
            File tmpImgFile = new File(path);
            os = new FileOutputStream(tmpImgFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, os);
            os.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null)
                    os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
