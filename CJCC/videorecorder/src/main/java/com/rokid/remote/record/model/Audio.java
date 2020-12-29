package com.rokid.remote.record.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author: heshun
 * Date: 2020/4/25 6:13 PM
 * gmail: shunhe1991@gmail.com
 */
public class Audio implements Parcelable {


    String duration;
    String dateTime;
    String path;

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public static final class AudioBuilder {
        String duration;
        String dateTime;
        String path;

        public AudioBuilder() {
        }

        public static AudioBuilder anAudio() {
            return new AudioBuilder();
        }

        public AudioBuilder withDuration(String duration) {
            this.duration = duration;
            return this;
        }

        public AudioBuilder withDateTime(String dateTime) {
            this.dateTime = dateTime;
            return this;
        }

        public AudioBuilder withPath(String path) {
            this.path = path;
            return this;
        }

        public Audio build() {
            Audio audio = new Audio();
            audio.setDuration(duration);
            audio.setDateTime(dateTime);
            audio.setPath(path);
            return audio;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.duration);
        dest.writeString(this.dateTime);
        dest.writeString(this.path);
    }

    public Audio() {
    }

    protected Audio(Parcel in) {
        this.duration = in.readString();
        this.dateTime = in.readString();
        this.path = in.readString();
    }

    public static final Creator<Audio> CREATOR = new Creator<Audio>() {
        @Override
        public Audio createFromParcel(Parcel source) {
            return new Audio(source);
        }

        @Override
        public Audio[] newArray(int size) {
            return new Audio[size];
        }
    };


    @Override
    public String toString() {
        return "Audio{" +
                "duration='" + duration + '\'' +
                ", dateTime='" + dateTime + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}
