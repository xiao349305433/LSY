package com.rokid.remote.record.mediacodec.internal;

import android.media.MediaCodec;
import android.media.MediaFormat;

import androidx.annotation.NonNull;

import java.nio.ByteBuffer;

public interface IEncoderListener {

    void onReceivedData(ByteBuffer data, @NonNull MediaCodec.BufferInfo info);

    void onPreparedMux(MediaFormat mediaFormat, boolean isVideo);

    void onError();
}
