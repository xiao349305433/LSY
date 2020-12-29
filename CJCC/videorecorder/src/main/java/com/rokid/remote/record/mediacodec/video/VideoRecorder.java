package com.rokid.remote.record.mediacodec.video;

import com.rokid.glass.videorecorder.camera.Camera2Manager;
import com.rokid.glass.videorecorder.camera.CameraClient;
import com.rokid.glass.videorecorder.camera.PreviewCallback;
import com.rokid.remote.record.mediacodec.base.BaseDataRecoder;
import com.rokid.remote.record.mediacodec.base.GBMediaParam;
import com.rokid.remote.record.mediacodec.internal.IRecorderListener;


public class VideoRecorder extends BaseDataRecoder {


    private int mWidth = 1280;
    private int mHeight = 720;
    private IRecorderListener listener;
    private Camera2Manager mCamera2Manager;

    @Override
    public void prepare(GBMediaParam gbMediaParam, final IRecorderListener listener) {
        this.listener = listener;
        this.mWidth = gbMediaParam.getVideoWidth();
        this.mHeight = gbMediaParam.getVideoHeight();
        mCamera2Manager = Camera2Manager.getInstance();
        mCamera2Manager.registerPreviewCallBack(CameraClient.VIDEO, new PreviewCallback() {
            @Override
            public void onPreviewTaken(byte[] data) {
                if (mIsCapturing && !mRequestStop) {
                    if (listener != null) {
                        listener.onReceivedData(data);
                    }
                }
            }
        });
    }


    @Override
    public void startRecording() {
        super.startRecording();
    }

    @Override
    public void stopRecording() {
        super.stopRecording();
        if (listener != null) {
            listener = null;
        }
    }


    @Override
    public void release() {
        super.release();
        listener = null;
    }
}
