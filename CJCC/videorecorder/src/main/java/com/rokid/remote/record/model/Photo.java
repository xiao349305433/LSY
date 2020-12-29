package com.rokid.remote.record.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author: heshun
 * Date: 2020/4/25 6:14 PM
 * gmail: shunhe1991@gmail.com
 */
public class Photo implements Parcelable {

    String path;
    String dateTime;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }


    public static final class PhotoBuilder {
        String path;
        String dateTime;

        public PhotoBuilder() {
        }

        public static PhotoBuilder aPhoto() {
            return new PhotoBuilder();
        }

        public PhotoBuilder withPath(String path) {
            this.path = path;
            return this;
        }

        public PhotoBuilder withDateTime(String dateTime) {
            this.dateTime = dateTime;
            return this;
        }

        public Photo build() {
            Photo photo = new Photo();
            photo.setPath(path);
            photo.setDateTime(dateTime);
            return photo;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.path);
        dest.writeString(this.dateTime);
    }

    public Photo() {
    }

    protected Photo(Parcel in) {
        this.path = in.readString();
        this.dateTime = in.readString();
    }

    public static final Creator<Photo> CREATOR = new Creator<Photo>() {
        @Override
        public Photo createFromParcel(Parcel source) {
            return new Photo(source);
        }

        @Override
        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };
}
