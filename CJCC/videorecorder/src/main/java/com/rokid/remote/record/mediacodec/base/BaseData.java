package com.rokid.remote.record.mediacodec.base;


public class BaseData {
    public enum MediaType {
        videoType,
        audioType
    }

    private MediaType type = MediaType.videoType;
    private boolean isIFrame = false;
    private byte[] data;
    private long pts;


    public void setType(MediaType type){
        this.type = type;
    }

    public MediaType getType() {
        return type;
    }

    public void setIsIFrame(boolean isIFrame){
        this.isIFrame = isIFrame;
    }

    public boolean getIsIFrame(){
        return isIFrame;
    }


    public void setPts(long pts) {
        this.pts = pts;
    }

    public long getPts(){
        return pts;
    }


    public byte[] getData() {
        return data;
    }

    public  void setData (byte[] data){
        this.data = data;
    }
}
