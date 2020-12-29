package com.rokid.glass.videorecorder.camera;

public interface ICameraDataCallback {
    void onCameraDataCallback(byte[] data, int width, int height);
}
