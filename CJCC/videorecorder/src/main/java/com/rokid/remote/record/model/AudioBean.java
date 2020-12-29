package com.rokid.remote.record.model;

import java.util.List;

/**
 * Author: heshun
 * Date: 2020/4/25 5:58 PM
 * gmail: shunhe1991@gmail.com
 */
public class AudioBean {

    String title;
    String dirPath;
    List<Audio> audioList;

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

    public List<Audio> getAudioList() {
        return audioList;
    }

    public void setAudioList(List<Audio> audioList) {
        this.audioList = audioList;
    }


    public static final class AudioBeanBuilder {
        String title;
        String dirPath;
        List<Audio> audioList;

        public AudioBeanBuilder() {
        }

        public static AudioBeanBuilder anAudioBean() {
            return new AudioBeanBuilder();
        }

        public AudioBeanBuilder withTitle(String title) {
            this.title = title;
            return this;
        }

        public AudioBeanBuilder withDirPath(String dirPath) {
            this.dirPath = dirPath;
            return this;
        }

        public AudioBeanBuilder withAudioList(List<Audio> audioList) {
            this.audioList = audioList;
            return this;
        }

        public AudioBean build() {
            AudioBean audioBean = new AudioBean();
            audioBean.setTitle(title);
            audioBean.setDirPath(dirPath);
            audioBean.setAudioList(audioList);
            return audioBean;
        }
    }
}
