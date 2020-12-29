package com.rokid.remote.record.model;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Author: heshun
 * Date: 2020/4/25 6:03 PM
 * gmail: shunhe1991@gmail.com
 */

@StringDef({MediaType.AUDIO, MediaType.VIDEO, MediaType.PHOTO})
@Retention(RetentionPolicy.RUNTIME)
public @interface MediaType {
    public static final String AUDIO = "录音";
    public static final String VIDEO = "视频";
    public static final String PHOTO = "照片";
}
