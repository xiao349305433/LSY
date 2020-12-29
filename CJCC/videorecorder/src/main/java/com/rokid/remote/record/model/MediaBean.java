package com.rokid.remote.record.model;


import android.text.TextUtils;

import com.rokid.remote.record.util.PathUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: heshun
 * Date: 2020/4/24 1:49 PM
 * gmail: shunhe1991@gmail.com
 */
public class MediaBean {


    private int selectIndex;
    private String selectType;


    public static final String VIDEO_SUFFIX = ".mp4";
    public static final String AUDIO_SUFFIX = ".aac";
    public static final String PHOTO_SUFFIX = ".jpg";
    public static final String VIDEO_PREFIX = "VIDEO";
    public static final String AUDIO_PREFIX = "AUDIO";
    public static final String PHOTO_PREFIX = "PHOTO";

    public static final Map<String, MediaFileInfo> mMediaInfoMap;

    static {
        mMediaInfoMap = new HashMap<>(8);
        mMediaInfoMap.put(MediaType.VIDEO, new MediaFileInfo()
                .setMediaType(MediaType.VIDEO).setPrefix(VIDEO_PREFIX).setSuffix(VIDEO_SUFFIX));
        mMediaInfoMap.put(MediaType.PHOTO, new MediaFileInfo()
                .setMediaType(MediaType.PHOTO).setPrefix(PHOTO_PREFIX).setSuffix(PHOTO_SUFFIX));
        mMediaInfoMap.put(MediaType.AUDIO, new MediaFileInfo()
                .setMediaType(MediaType.AUDIO).setPrefix(AUDIO_PREFIX).setSuffix(AUDIO_SUFFIX));
    }

    public String getTitle(int position) {
        return position == 0 ? MediaType.VIDEO : (position == 1 ? MediaType.PHOTO : MediaType.AUDIO);
    }

    public int getCount(int position) {
        int count = 0;
        if (position == 0 && null != videos && null != videos.videoList)
            count = videos.videoList.size();
        if (position == 1 && null != photos && null != photos.photoList)
            count = photos.photoList.size();
        if (position == 2 && null != audios && null != audios.audioList)
            count = audios.audioList.size();
        return count;
    }

    public static class MediaFileInfo {
        String mediaType;
        String suffix;
        String prefix;

        public String getMediaType() {
            return mediaType;
        }

        public MediaFileInfo setMediaType(String mediaType) {
            this.mediaType = mediaType;
            return this;
        }

        public String getSuffix() {
            return suffix;
        }

        public MediaFileInfo setSuffix(String suffix) {
            this.suffix = suffix;
            return this;
        }

        public String getPrefix() {
            return prefix;
        }

        public MediaFileInfo setPrefix(String prefix) {
            this.prefix = prefix;
            return this;
        }
    }


    public static String mediaPath(@MediaType String type) {
        if (TextUtils.isEmpty(type) || !mMediaInfoMap.containsKey(type)) {
            return null;
        }

        PathUtils.FILE_TYPE file_type;
        if (type.equals(PathUtils.FILE_TYPE.VIDEO.type)) {
            file_type = PathUtils.FILE_TYPE.VIDEO;
        } else if (type.equals(PathUtils.FILE_TYPE.AUDIO.type)) {
            file_type = PathUtils.FILE_TYPE.AUDIO;
        } else {
            file_type = PathUtils.FILE_TYPE.JPEG;
        }
        return PathUtils.mkdirs(file_type);
    }

    private VideoBean videos;
    private PhotoBean photos;
    private AudioBean audios;

    public int getSelectIndex() {
        return selectIndex;
    }

    public void setSelectIndex(int selectIndex) {
        this.selectIndex = selectIndex;
    }

    public String getSelectType() {
        return selectType;
    }

    public void setSelectType(String selectType) {
        this.selectType = selectType;
    }


    public VideoBean getVideos() {
        return videos;
    }

    public void setVideos(VideoBean videos) {
        this.videos = videos;
    }

    public PhotoBean getPhotos() {
        return photos;
    }

    public void setPhotos(PhotoBean photos) {
        this.photos = photos;
    }

    public AudioBean getAudios() {
        return audios;
    }

    public void setAudios(AudioBean audios) {
        this.audios = audios;
    }


    public static final class MediaBeanBuilder {
        private int selectIndex;
        private String selectType;
        private VideoBean videos;
        private PhotoBean photos;
        private AudioBean audios;

        private MediaBeanBuilder() {
        }

        public static MediaBeanBuilder aMediaBean() {
            return new MediaBeanBuilder();
        }

        public MediaBeanBuilder withSelectIndex(int selectIndex) {
            this.selectIndex = selectIndex;
            return this;
        }

        public MediaBeanBuilder withSelectType(String selectType) {
            this.selectType = selectType;
            return this;
        }

        public MediaBeanBuilder withVideos(VideoBean videos) {
            this.videos = videos;
            return this;
        }

        public MediaBeanBuilder withPhotos(PhotoBean photos) {
            this.photos = photos;
            return this;
        }

        public MediaBeanBuilder withAudios(AudioBean audios) {
            this.audios = audios;
            return this;
        }

        public MediaBean build() {
            MediaBean mediaBean = new MediaBean();
            mediaBean.setSelectIndex(selectIndex);
            mediaBean.setSelectType(selectType);
            mediaBean.setVideos(videos);
            mediaBean.setPhotos(photos);
            mediaBean.setAudios(audios);
            return mediaBean;
        }
    }
}
