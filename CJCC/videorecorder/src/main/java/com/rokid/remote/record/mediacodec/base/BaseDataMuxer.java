package com.rokid.remote.record.mediacodec.base;

import android.content.Context;

import com.rokid.remote.record.mediacodec.internal.IMuxerListener;

import java.io.IOException;

public abstract class BaseDataMuxer {

    protected volatile boolean isMuxing;

    protected volatile boolean isRequestStop;



    public void startMuxing() {

        isMuxing = true;
        isRequestStop = false;

    }
    public void stopMuxing() {

        isMuxing = false;
        isRequestStop = true;
    }

    public void release() {

    }


    public abstract void prepare(Context context, IMuxerListener listener) throws IOException;


}
