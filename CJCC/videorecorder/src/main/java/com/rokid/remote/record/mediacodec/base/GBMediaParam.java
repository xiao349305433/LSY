package com.rokid.remote.record.mediacodec.base;
import android.media.AudioFormat;

public class GBMediaParam {

    private String videoMimeType = "video/avc";
    private int videoWidth = 1280;
    private int videoHeight = 720;
    private int videoGOP = 2;
    private int videoFrameRate = 24;
    private int videoBitRate = 5*500*1000; // 2Mbps



    private String audioMimeType = "audio/mp4a-latm";
    private int audioChannel = 1;
    private int audioSampleRate = 44100;
    private int audioBitRate = 64000;
    private int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
    private int audioChannelMode = AudioFormat.CHANNEL_IN_MONO;

    public String getVideoMimeType() {
        return videoMimeType;
    }

    public void setVideoMimeType(String videoMimeType) {
        this.videoMimeType = videoMimeType;
    }

    public int getVideoHeight() {
        return videoHeight;
    }

    public void setVideoHeight(int videoHeight) {
        this.videoHeight = videoHeight;
    }

    public int getVideoWidth() {
        return videoWidth;
    }

    public void setVideoWidth(int videoWidth) {
        this.videoWidth = videoWidth;
    }

    public int getVideoGOP() {
        return videoGOP;
    }

    public void setVideoGOP(int videoGOP) {
        this.videoGOP = videoGOP;
    }

    public int getVideoFrameRate() {
        return videoFrameRate;
    }

    public void setVideoFrameRate(int videoFrameRate) {
        this.videoFrameRate = videoFrameRate;
    }

    public int getVideoBitRate() {
        return videoBitRate;
    }

    public void setVideoBitRate(int videoBitRate) {
        this.videoBitRate = videoBitRate;
    }



    public int getAudioChannel() {
        return audioChannel;
    }

    public void setAudioChannel(int audioChannel) {
        this.audioChannel = audioChannel;
    }

    public int getAudioChannelMode() {
        return audioChannelMode;
    }

    public void setAudioChannelMode(int audioChannelMode) {
        this.audioChannelMode = audioChannelMode;
    }

    public int getAudioFormat() {
        return audioFormat;
    }
    public void setAudioFormat(int audioFormat) {
        this.audioFormat = audioFormat;
    }

    public int getAudioSampleRate() {
        return audioSampleRate;
    }
    public void setAudioSampleRate(int audioSampleRate) {
        this.audioSampleRate = audioSampleRate;
    }

    public String getAudioMimeType() {
        return audioMimeType;
    }
    public void setAudioMimeType(String audioMimeType) {
        this.audioMimeType = audioMimeType;
    }
    public int getAudioBitRate() {
        return audioBitRate;
    }
    public void setAudioBitRate(int audioBitRate) {
        this.audioBitRate = audioBitRate;
    }


    public static class Builder{
        public GBMediaParam mGBMediaParam;

        public Builder() { mGBMediaParam =  new GBMediaParam();}

        public Builder setVideoFrameRate(int videoFrameRate){
            this.mGBMediaParam.videoFrameRate = videoFrameRate;
            return this;
        }
        public Builder setVideoBitRate(int videoBitRate){
            this.mGBMediaParam.videoBitRate = videoBitRate;
            return this;
        }
        public Builder setVideoGOP(int videoGOP){
            this.mGBMediaParam.videoGOP = videoGOP;
            return this;
        }
        public Builder setVideoHeight(int videoHeight){
            this.mGBMediaParam.videoHeight = videoHeight;
            return this;
        }
        public Builder setVideoWidth(int videoWidth){
            this.mGBMediaParam.videoWidth = videoWidth;
            return this;
        }


        public Builder setAudioChannel(int audioChannel) {
            this.mGBMediaParam.audioChannel = audioChannel;
            return this;
        }

        public Builder setAudioChannelMode(int audioChannelMode) {
            this.mGBMediaParam.audioChannelMode = audioChannelMode;
            return this;
        }

        public Builder setAudioFormat(int audioFormat) {
            this.mGBMediaParam.audioFormat = audioFormat;
            return this;
        }

        public Builder setAudioSampleRate(int audioSampleRate) {
            this.mGBMediaParam.audioSampleRate = audioSampleRate;
            return this;
        }

        public Builder setAudioMimeType(String audioMimeType) {
            this.mGBMediaParam.audioMimeType = audioMimeType;
            return this;
        }

        public Builder setAudioBitRate(int audioBitRate) {
            this.mGBMediaParam.audioBitRate = audioBitRate;
            return this;
        }


        public GBMediaParam build(){
            return this.mGBMediaParam;
        }

    }

    @Override
    public String toString() {
        return "GBMediaParam{" +
                "videoMimeType='" + videoMimeType + '\'' +
                ", videoWidth=" + videoWidth +
                ", videoHeight=" + videoHeight +
                ", videoGOP=" + videoGOP +
                ", videoFrameRate=" + videoFrameRate +
                ", videoBitRate=" + videoBitRate +
                ", audioMimeType='" + audioMimeType + '\'' +
                ", audioChannel=" + audioChannel +
                ", audioSampleRate=" + audioSampleRate +
                ", audioBitRate=" + audioBitRate +
                ", audioFormat=" + audioFormat +
                ", audioChannelMode=" + audioChannelMode +
                '}';
    }
}
