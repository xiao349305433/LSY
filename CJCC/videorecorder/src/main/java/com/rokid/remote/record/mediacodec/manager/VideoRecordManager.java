package com.rokid.remote.record.mediacodec.manager;

import com.rokid.remote.record.mediacodec.base.GBMediaParam;
import com.rokid.remote.record.mediacodec.callback.VideoCallback;
import com.rokid.remote.record.util.RKSingleton;

/**
 * Author: heshun
 * Date: 2020/4/22 2:22 PM
 * gmail: shunhe1991@gmail.com
 */
public class VideoRecordManager {

    private MediaMuxerManager2 mMediaMuxerManager2;
    private VideoCallback mCallback;

    private static RKSingleton<VideoRecordManager> singleton = new RKSingleton<VideoRecordManager>() {
        @Override
        public VideoRecordManager create() {
            return new VideoRecordManager();
        }
    };

    private VideoRecordManager() {
    }

    public static VideoRecordManager getInstance() {
        return singleton.get();
    }

    private void checkVideoRecording() {
        if (null != mMediaMuxerManager2) {
            mMediaMuxerManager2.stop();
        }

    }

    public void startRecording(GBMediaParam param, VideoCallback callback) {
        checkVideoRecording();
        this.mCallback = callback;

        mMediaMuxerManager2 = new MediaMuxerManager2();
        mMediaMuxerManager2.prepare(param);
        mMediaMuxerManager2.start(callback);
    }

    public void stopRecording() {
        if (null == mMediaMuxerManager2) return;

        mMediaMuxerManager2.stop();
        mMediaMuxerManager2 = null;
        mCallback = null;
    }

}
