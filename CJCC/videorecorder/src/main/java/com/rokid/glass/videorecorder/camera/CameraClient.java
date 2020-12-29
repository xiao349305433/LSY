package com.rokid.glass.videorecorder.camera;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({CameraClient.FACE, CameraClient.VIDEO, CameraClient.GB28181})
@Retention(RetentionPolicy.SOURCE)
public @interface CameraClient {
    int FACE = 0x001; // 识别类模块
    int VIDEO = 0x002; // 录像模块
    int GB28181 = 0x003; // GB28181视频监控
}