// RKMediaCallback.aidl
package com.rokid.recordapi;

// Declare any non-default types here with import statements

interface RKMediaCallback {

 void onServiceReady(boolean success, String message);

    void onAudioStart();

    void onAudioStopped(String filePath);

    void onAudioFailed(int errorCode, String errorMessage);

    void onVideoStart();

    void onVideoStopped(String filePath);

    void onVideoFailed(int errorCode, String errorMessage);

    void onPictureStart();

    void onPictureStopped(String filePath);

    void onPictureFailed(int errorCode, String errorMessage);
}
