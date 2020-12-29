package com.rokid.glass.cjcc;

public interface Constant {
    interface OfflineVoice {
        String GLOBAL_START_RECORD = "开始录像";
        String GLOBAL_STOP_RECORD = "停止录像";
    }

    interface OfflineVoice_EN {
        String GLOBAL_START_RECORD = "Start Recording";
        String GLOBAL_STOP_RECORD = "Stop Recording";
    }

    String ACTION_CAMERA_ATTACHED = "android.hardware.usb.action.USB_DEVICE_ATTACHED";
    String ACTION_CAMERA_DETACHED = "android.hardware.usb.action.USB_DEVICE_DETACHED";
}
