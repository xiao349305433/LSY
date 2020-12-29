package com.rokid.remote.record.mediacodec.manager;

import com.rokid.glass.videorecorder.utils.Logger;
import com.rokid.remote.record.mediacodec.audio.AudioRecorder;
import com.rokid.remote.record.mediacodec.callback.AudioCallback;
import com.rokid.remote.record.util.RKSingleton;

/**
 * Author: heshun
 * Date: 2020/4/22 2:22 PM
 * gmail: shunhe1991@gmail.com
 */
public class AudioRecordManager {

    private AudioCallback mAudioCallback;

    private AudioRecorder mAudioRecorder;

    private static RKSingleton<AudioRecordManager> singleton = new RKSingleton<AudioRecordManager>() {
        @Override
        public AudioRecordManager create() {
            return new AudioRecordManager();
        }
    };

    private AudioRecordManager() {
    }

    public static AudioRecordManager getInstance() {
        return singleton.get();
    }

    /**
     * 检查当前是否正在录制音频
     */
    private void checkAudioRecorder() {
        if (null != mAudioRecorder) {
            mAudioRecorder.stopRecording();
        }
    }

    /**
     * 开始录制音频
     *
     * @param callback
     */
    public void startRecording(AudioCallback callback) {
        checkAudioRecorder();
        this.mAudioCallback = callback;

        Logger.d("start recording audio");
        mAudioRecorder = new AudioRecorder();
        mAudioRecorder.prepare(null);
        mAudioRecorder.startRecording(true, new AudioCallback() {
            @Override
            public void onAudioRecordStart() {
                if (null != callback) callback.onAudioRecordStart();
            }

            @Override
            public void onAudioRecordStopped(String filePath) {
                if (null != callback) callback.onAudioRecordStopped(filePath);
            }

            @Override
            public void onAudioRecordFailed() {
                if (null != callback) callback.onAudioRecordFailed();
            }
        });

    }

    public void stopRecording() {
        Logger.d("stop recording audio");

        if (null != mAudioRecorder) mAudioRecorder.stopRecording();

        mAudioRecorder = null;
        mAudioCallback = null;
    }
}
