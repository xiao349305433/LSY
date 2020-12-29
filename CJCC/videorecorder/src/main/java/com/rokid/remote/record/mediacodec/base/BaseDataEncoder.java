package com.rokid.remote.record.mediacodec.base;

import com.rokid.remote.record.mediacodec.internal.IEncoderListener;

import java.io.IOException;

public abstract class BaseDataEncoder {

    protected volatile boolean mIsEncoding;

    protected volatile boolean mRequestStop;

    protected final Object mSync = new Object();
    public void startEncoding() {

        // 正在录制标识
        mIsEncoding = true;
        // 停止标识 置false
        mRequestStop = false;
        //


    }

    public void stopEncoding() {

        if (!mIsEncoding || mRequestStop) {
            return;
        }
        mRequestStop = true;


    }

    public void release() {

        mIsEncoding = false;


    }

    public abstract  void prepare(GBMediaParam gbMediaParam, IEncoderListener listener) throws IOException;
}
