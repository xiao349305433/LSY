package com.rokid.glass.videorecorder.camera;

public interface VideoInterface {

    void startRec(String path);

    void stopRec();

    long getRecStartTime();

    void setType(int type);

    String getFileName();
}
