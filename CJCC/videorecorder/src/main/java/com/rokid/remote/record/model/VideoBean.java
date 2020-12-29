package com.rokid.remote.record.model;

import java.util.List;

/**
 * Author: heshun
 * Date: 2020/4/25 5:55 PM
 * gmail: shunhe1991@gmail.com
 */
public class VideoBean {
    String title;
    String dirPath;

    List<Video> videoList;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDirPath() {
        return dirPath;
    }

    public void setDirPath(String dirPath) {
        this.dirPath = dirPath;
    }

    public List<Video> getVideoList() {
        return videoList;
    }

    public void setVideoList(List<Video> videoList) {
        this.videoList = videoList;
    }


    public static final class VideoBeanBuilder {
        String title;
        String dirPath;
        List<Video> videoList;

        public VideoBeanBuilder() {
        }

        public static VideoBeanBuilder aVideoBean() {
            return new VideoBeanBuilder();
        }

        public VideoBeanBuilder withTitle(String title) {
            this.title = title;
            return this;
        }

        public VideoBeanBuilder withDirPath(String dirPath) {
            this.dirPath = dirPath;
            return this;
        }

        public VideoBeanBuilder withVideoList(List<Video> videoList) {
            this.videoList = videoList;
            return this;
        }

        public VideoBean build() {
            VideoBean videoBean = new VideoBean();
            videoBean.setTitle(title);
            videoBean.setDirPath(dirPath);
            videoBean.setVideoList(videoList);
            return videoBean;
        }
    }
}
