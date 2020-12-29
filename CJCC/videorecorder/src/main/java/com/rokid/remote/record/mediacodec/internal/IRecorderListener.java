package com.rokid.remote.record.mediacodec.internal;


public interface IRecorderListener {
    void onReceivedData(byte[] data);

    void onError();
}
