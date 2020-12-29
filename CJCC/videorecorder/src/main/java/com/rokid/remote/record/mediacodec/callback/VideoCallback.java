package com.rokid.remote.record.mediacodec.callback;

/**
 * Author: heshun
 * Date: 2020/4/22 2:40 PM
 * gmail: shunhe1991@gmail.com
 */
public interface VideoCallback {

    void onVideoStart();

    void onVideoStopped(String filePath);

    void onVideoFailed(int errorCode, String errorMessage);
}
