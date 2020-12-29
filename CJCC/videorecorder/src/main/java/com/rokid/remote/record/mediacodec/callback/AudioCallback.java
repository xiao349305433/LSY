package com.rokid.remote.record.mediacodec.callback;

/**
 * Author: heshun
 * Date: 2020/4/22 2:16 PM
 * gmail: shunhe1991@gmail.com
 */
public interface AudioCallback {

    void onAudioRecordStart();

    void onAudioRecordStopped(String filePath);

    void onAudioRecordFailed();
}
