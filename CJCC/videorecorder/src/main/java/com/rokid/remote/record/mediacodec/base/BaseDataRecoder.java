package com.rokid.remote.record.mediacodec.base;

import com.rokid.remote.record.mediacodec.internal.IRecorderListener;

import java.io.IOException;

public abstract class BaseDataRecoder {

    protected volatile boolean mIsCapturing;

    protected volatile boolean mRequestStop;

    protected final Object mSync = new Object();



    public void startRecording(){
        // 正在录制标识
        mIsCapturing = true;
        // 停止标识 置false
        mRequestStop = false;
        //
    }

    public void stopRecording(){
        if (!mIsCapturing || mRequestStop) {
            return;
        }
        mRequestStop = true;
    }


    public void release(){
        mIsCapturing = false;

    }

    public abstract  void prepare(GBMediaParam gbMediaParam, IRecorderListener listener) throws IOException;

}
