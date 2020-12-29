package com.rokid.remote.record.model;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Author: heshun
 * Date: 2020/4/22 10:55 AM
 * gmail: shunhe1991@gmail.com
 */


@StringDef({RecordState.IDLE, RecordState.AUDIO, RecordState.VIDEO, RecordState.PICTURE,
        RecordState.RECORDING_AUDIO, RecordState.RECORDING_VIDEO, RecordState.TAKING_PICTURE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RecordState {

    String IDLE = "idle";
    String AUDIO = "audio_page";
    String RECORDING_AUDIO = "recording_audio";
    String VIDEO = "video_page";
    String RECORDING_VIDEO = "recording_video";
    String PICTURE = "picture_page";
    String TAKING_PICTURE = "taking_picture";
}
