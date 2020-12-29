package com.rokid.remote.record.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author: heshun
 * Date: 2020/4/25 6:13 PM
 * gmail: shunhe1991@gmail.com
 */
public class Video implements Parcelable {

    private String path;
    private String formatTime;
    private String duration;
    private long durationValue;
    private long createTime;


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFormatTime() {
        return formatTime;
    }

    public void setFormatTime(String formatTime) {
        this.formatTime = formatTime;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public long getDurationValue() {
        return durationValue;
    }

    public void setDurationValue(long durationValue) {
        this.durationValue = durationValue;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }


    public static final class VideoBuilder {
        private String path;
        private String formatTime;
        private String duration;
        private long durationValue;
        private long createTime;

        public VideoBuilder() {
        }

        public static VideoBuilder aVideo() {
            return new VideoBuilder();
        }

        public VideoBuilder withPath(String path) {
            this.path = path;
            return this;
        }

        public VideoBuilder withFormatTime(String formatTime) {
            this.formatTime = formatTime;
            return this;
        }

        public VideoBuilder withDuration(String duration) {
            this.duration = duration;
            return this;
        }

        public VideoBuilder withDurationValue(long durationValue) {
            this.durationValue = durationValue;
            return this;
        }

        public VideoBuilder withCreateTime(long createTime) {
            this.createTime = createTime;
            return this;
        }

        public Video build() {
            Video video = new Video();
            video.setPath(path);
            video.setFormatTime(formatTime);
            video.setDuration(duration);
            video.setDurationValue(durationValue);
            video.setCreateTime(createTime);
            return video;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.path);
        dest.writeString(this.formatTime);
        dest.writeString(this.duration);
        dest.writeLong(this.durationValue);
        dest.writeLong(this.createTime);
    }

    public Video() {
    }

    public Video(Parcel in) {
        this.path = in.readString();
        this.formatTime = in.readString();
        this.duration = in.readString();
        this.durationValue = in.readLong();
        this.createTime = in.readLong();
    }

    public static final Creator<Video> CREATOR = new Creator<Video>() {
        @Override
        public Video createFromParcel(Parcel source) {
            return new Video(source);
        }

        @Override
        public Video[] newArray(int size) {
            return new Video[size];
        }
    };
}
