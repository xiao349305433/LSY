package com.test1moudle.p.adapter;

/**
 * Created by huxu on 2017/12/1.
 */
public class Player {
    private String title;
    private long length;
    private String imageUrl;
    private String videoUrl;

    public Player(String title, long length,String imageUrl, String videoUrl) {
        this.title = title;
        this.length = length;
        this.imageUrl = imageUrl;
        this.videoUrl = videoUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
}