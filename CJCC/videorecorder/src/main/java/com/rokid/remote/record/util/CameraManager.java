package com.rokid.remote.record.util;

import android.content.Context;
import android.media.AudioFormat;
import android.media.MediaFormat;

import com.rokid.remote.record.mediacodec.RecordManager;
import com.rokid.remote.record.mediacodec.base.GBMediaParam;


/**
 * @author : heshun
 * @version : v1.0.0
 * @date : 2020/4/14 12:18 AM
 * @description: This is CameraManager
 */
public class CameraManager {


    static CameraManager instance = new CameraManager();


    private CameraManager() {

    }

    public static CameraManager getInstance() {
        return instance;
    }

    public void init(Context context) {

        GBMediaParam gbMediaParam = new GBMediaParam.Builder()
                .setAudioBitRate(64_000)
                .setAudioChannel(2)
                .setAudioChannelMode(AudioFormat.CHANNEL_IN_STEREO)
                .setAudioFormat(AudioFormat.ENCODING_PCM_16BIT)
                .setAudioMimeType(MediaFormat.MIMETYPE_AUDIO_AAC)
                .setAudioSampleRate(44100)
                .setVideoWidth(1280)
                .setVideoHeight(720)
                .setVideoBitRate(10 * 1024 * 1024)  //bps
                .setVideoFrameRate(30)
                .setVideoGOP(2)
                .build();
        RecordManager.getInstance().init(context, gbMediaParam);

    }

}
