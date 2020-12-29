package com.rokid.recordapi;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.rokid.remote.record.util.Constants;

/**
 * Author: heshun
 * Date: 2020/7/7 10:48 AM
 * gmail: shunhe1991@gmail.com
 */
@Entity(tableName = Constants.MEDIA_RECORD_TABLE_NAME)
public class RecordEntity implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "create_time")
    private long createTime;

    @ColumnInfo(name = "media_type")
    private String mediaType;

    @ColumnInfo(name = "file_path")
    private String filePath;

    @ColumnInfo(name = "file_name")
    private String fileName;

    @ColumnInfo(name = "thumbnail_path")
    private String thumbnailPath;//缩略图

    private long duration;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeLong(this.createTime);
        dest.writeString(this.mediaType);
        dest.writeString(this.filePath);
        dest.writeString(this.fileName);
        dest.writeString(this.thumbnailPath);
        dest.writeLong(this.duration);
    }

    public RecordEntity readFromParcel(Parcel in){
        return new RecordEntity(in);
    }

    public RecordEntity() {
    }

    protected RecordEntity(Parcel in) {
        this.id = in.readInt();
        this.createTime = in.readLong();
        this.mediaType = in.readString();
        this.filePath = in.readString();
        this.fileName = in.readString();
        this.thumbnailPath = in.readString();
        this.duration = in.readLong();
    }

    public static final Creator<RecordEntity> CREATOR = new Creator<RecordEntity>() {
        @Override
        public RecordEntity createFromParcel(Parcel source) {
            return new RecordEntity(source);
        }

        @Override
        public RecordEntity[] newArray(int size) {
            return new RecordEntity[size];
        }
    };
}
