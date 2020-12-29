package com.rokid.remote.record.model;

import java.util.List;

/**
 * Author: heshun
 * Date: 2020/4/25 5:58 PM
 * gmail: shunhe1991@gmail.com
 */
public class PhotoBean {

    String title;
    String dirPath;

    List<Photo> photoList;

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


    public List<Photo> getPhotoList() {
        return photoList;
    }

    public void setPhotoList(List<Photo> photoList) {
        this.photoList = photoList;
    }


    public static final class PhotoBeanBuilder {
        String title;
        String dirPath;
        List<Photo> photoList;

        public PhotoBeanBuilder() {
        }

        public static PhotoBeanBuilder aPhotoBean() {
            return new PhotoBeanBuilder();
        }

        public PhotoBeanBuilder withTitle(String title) {
            this.title = title;
            return this;
        }

        public PhotoBeanBuilder withDirPath(String dirPath) {
            this.dirPath = dirPath;
            return this;
        }

        public PhotoBeanBuilder withPhotoList(List<Photo> photoList) {
            this.photoList = photoList;
            return this;
        }

        public PhotoBean build() {
            PhotoBean photoBean = new PhotoBean();
            photoBean.setTitle(title);
            photoBean.setDirPath(dirPath);
            photoBean.setPhotoList(photoList);
            return photoBean;
        }
    }
}
