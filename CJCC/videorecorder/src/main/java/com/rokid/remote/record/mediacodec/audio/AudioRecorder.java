package com.rokid.remote.record.mediacodec.audio;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.media.MediaRecorder;
import android.os.Process;

import com.rokid.glass.videorecorder.utils.ByteArrayPool;
import com.rokid.glass.videorecorder.utils.Logger;
import com.rokid.remote.record.mediacodec.callback.AudioCallback;
import com.rokid.remote.record.mediacodec.internal.IEncoderListener;
import com.rokid.remote.record.util.FileManager;
import com.rokid.remote.record.util.PathUtils;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * @author : heshun
 * @version : v1.0.0
 * @date : 2020/4/14 9:43 PM
 * @description: This is AudioRecorder
 */
public class AudioRecorder extends Thread {


    private static final int sampleRateInHz = 44_100;
    private static final int bitRate = 64_000;
    private static final int channelCount = 1;
    private int minBufferSize;
    private AudioRecord mAudioRecord;
    private FileManager mFileManager;
    private MediaCodec mMediaCodec;
    private volatile boolean isRecording = false;
    private long startTime = 0;
    private IEncoderListener listener;
    private MediaFormat audioFormat;
    private boolean saveAac = false;
    private String mDir;

    public AudioRecorder() {
    }

    public void prepare(IEncoderListener listener) {


        audioFormat = MediaFormat.createAudioFormat(MediaFormat.MIMETYPE_AUDIO_AAC
                , sampleRateInHz, channelCount);
        audioFormat.setInteger(MediaFormat.KEY_AAC_PROFILE, MediaCodecInfo.CodecProfileLevel.AACObjectLC);
        audioFormat.setInteger(MediaFormat.KEY_BIT_RATE, bitRate);
        audioFormat.setInteger(MediaFormat.KEY_CHANNEL_MASK, AudioFormat.CHANNEL_IN_MONO);
        audioFormat.setInteger(MediaFormat.KEY_CHANNEL_COUNT, channelCount);
        this.listener = listener;
    }

    private AudioCallback mCallback;
    private String mFileName;

    public boolean startRecording(boolean saveAac, AudioCallback callback) {

        this.saveAac = saveAac;
        this.mCallback = callback;

        if (saveAac) {

            PathUtils.FileInfo fileInfo = PathUtils.createFile(PathUtils.FILE_TYPE.AUDIO);
            mFileName = fileInfo.getPath();
            File file = new File(mFileName);
            mDir = file.getParent();
            Logger.i("file path ", mFileName, mDir);
            mFileManager = new FileManager(mFileName);
        }

        try {
            mMediaCodec = MediaCodec.createEncoderByType(MediaFormat.MIMETYPE_AUDIO_AAC);
            mMediaCodec.configure(audioFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
            mMediaCodec.start();

            minBufferSize = AudioRecord.getMinBufferSize(sampleRateInHz, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
            mAudioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, sampleRateInHz, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, minBufferSize);

            if (AudioRecord.STATE_INITIALIZED != mAudioRecord.getState()) {
                mAudioRecord.release();
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        start();
        return true;
    }

    public void stopRecording() {

        isRecording = false;
        if (null != mFileManager) {
            mFileManager.closeFile();
        }

        if (null != mCallback) {
            mCallback.onAudioRecordStopped(mDir);
            mCallback = null;
        }
    }

    private long getPTSUs() {
        long result = System.nanoTime() / 1_000L;

        if (result < startTime) {
            result = startTime - result + result;
        }

        return result;
    }

    private boolean isMux = false;

    @Override
    public void run() {

        if (null != mCallback) mCallback.onAudioRecordStart();

        Process.setThreadPriority(Process.THREAD_PRIORITY_AUDIO);
        isRecording = true;
        mAudioRecord.startRecording();
        MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();

        byte[] data = new byte[minBufferSize];
        ByteBuffer buffer = null;
        if (startTime == 0) {
            startTime = System.nanoTime() / 1_000L;
        }

        while (isRecording) {
            int readBytes = mAudioRecord.read(data, 0, data.length);

            if (!isRecording) {
                break;
            }

            if (readBytes <= 0) {
                continue;
            }

            int index = mMediaCodec.dequeueInputBuffer(0);
            if (isMux && index == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {

                Logger.w("something wrong");
            }


            if (index >= 0) {
                ByteBuffer inputBuffer = mMediaCodec.getInputBuffer(index);
                inputBuffer.clear();
                inputBuffer.put(data, 0, readBytes);

                mMediaCodec.queueInputBuffer(index, 0, readBytes, getPTSUs(), 0);
            }

            if (!isRecording) {
                break;
            }


            index = mMediaCodec.dequeueOutputBuffer(info, 0);
            if (index == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                if (listener != null) {
                    audioFormat = mMediaCodec.getOutputFormat();
                    listener.onPreparedMux(audioFormat, false);
                }
                isMux = true;
            }

            if (!isMux) {
                continue;
            }
            while (index >= 0 && isRecording) {
                info.presentationTimeUs = getPTSUs();
                ByteBuffer outputBuffer = mMediaCodec.getOutputBuffer(index);
                byte[] buf = ByteArrayPool.defaultPool().getBuf(info.size);
                Arrays.fill(buf, (byte) 0);
                outputBuffer.get(buf, 0, info.size);
                if (saveAac) {
                    //添加ADTS头
                    int outDataSize = info.size + 7;
                    byte[] outData = ByteArrayPool.defaultPool().getBuf(outDataSize);
                    Arrays.fill(outData, (byte) 0);
                    addADTStoPacket(sampleRateInHz, outData, outDataSize);
                    System.arraycopy(buf, 0, outData, 7, info.size);


                    mFileManager.saveFileData(outData, 0, outDataSize);
                    ByteArrayPool.defaultPool().returnBuf(outData);
                }

                if (listener != null) {

                    if (buffer == null || buffer.capacity() < info.size) {
                        buffer = ByteBuffer.allocate(info.size);
                    }
                    buffer.clear();
                    buffer.put(buf, 0, info.size);
//                    Logger.d("======================================= AUDIO presentationTimeUs:", String.valueOf(info.presentationTimeUs));
                    listener.onReceivedData(buffer, info);
                    startTime = info.presentationTimeUs;
                    ByteArrayPool.defaultPool().returnBuf(buf);
                }

                mMediaCodec.releaseOutputBuffer(index, false);
                index = mMediaCodec.dequeueOutputBuffer(info, 0);

            }

        }

        mAudioRecord.stop();
        mAudioRecord.release();
        mAudioRecord = null;

        mMediaCodec.stop();
        mMediaCodec.release();
        mMediaCodec = null;

        if (saveAac) {
            mFileManager.closeFile();
        }

        startTime = 0;
        isRecording = false;

        if (null != mCallback) mCallback.onAudioRecordStopped(mDir);
        mCallback = null;
    }


    /**
     * 添加ADTS头，如果要与视频流合并就不用添加，单独AAC文件就需要添加，否则无法正常播放
     */
    public static void addADTStoPacket(int sampleRateType, byte[] packet, int packetLen) {
        int profile = 2; // AAC LC
        int chanCfg = 2; // CPE

        packet[0] = (byte) 0xFF;
        packet[1] = (byte) 0xF9;
        packet[2] = (byte) (((profile - 1) << 6) + (sampleRateType << 2) + (chanCfg >> 2));
        packet[3] = (byte) (((chanCfg & 3) << 6) + (packetLen >> 11));
        packet[4] = (byte) ((packetLen & 0x7FF) >> 3);
        packet[5] = (byte) (((packetLen & 7) << 5) + 0x1F);
        packet[6] = (byte) 0xFC;
    }


    public MediaFormat getAudioFormat() {
        return audioFormat;
    }
}
