package com.rokid.remote.record.mediacodec.internal;


public interface IMuxerListener {
    void onReceivedData(byte[] data, int length);

}
