package com.rokid.remote.record.mediacodec.video;

/**
 * Author: zhuohf
 * Version: V0.1 2019/9/12
 */
public class VideoData {

    public byte[] videoData;
    public int width;
    public int height;

    public VideoData(byte[] videoData, int width, int height) {
        this.videoData = videoData;
        this.width = width;
        this.height = height;
    }
}
