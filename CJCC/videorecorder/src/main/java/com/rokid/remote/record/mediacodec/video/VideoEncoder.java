package com.rokid.remote.record.mediacodec.video;

import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.os.Bundle;

import com.rokid.glass.videorecorder.utils.Logger;
import com.rokid.remote.record.YuvOsdUtils;
import com.rokid.remote.record.mediacodec.base.BaseData;
import com.rokid.remote.record.mediacodec.base.BaseDataEncoder;
import com.rokid.remote.record.mediacodec.base.BaseDataWrapper;
import com.rokid.remote.record.mediacodec.base.GBMediaParam;
import com.rokid.remote.record.mediacodec.internal.IEncoderListener;
import com.rokid.remote.record.mediacodec.internal.IRecorderListener;
import com.rokid.remote.record.util.FileManager;

import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.concurrent.LinkedBlockingQueue;


public class VideoEncoder extends BaseDataEncoder implements IRecorderListener {

    private MediaCodec.BufferInfo info;
    private MediaCodec mVideoEncoder;
    private VideoEncodeThread mVideoEncodeThread;
    private LinkedBlockingQueue<BaseData> mVideoDataQueue = new LinkedBlockingQueue<>();
    private FileManager videoFileManager;
    private FileManager yuvFileManager;
    private Object sync = new Object();
    IEncoderListener listener;
    private byte[] NV12Data = null;

    private GBMediaParam mGBMediaParam;
    private MediaFormat videoFormat;

    @Override
    public void prepare(GBMediaParam gbMediaParam, IEncoderListener listener) {
        this.listener = listener;
        this.mGBMediaParam = gbMediaParam;
        NV12Data = new byte[(int) (gbMediaParam.getVideoWidth() * gbMediaParam.getVideoHeight() * 3 / 2)];
        videoFormat = MediaFormat.createVideoFormat(mGBMediaParam.getVideoMimeType(), mGBMediaParam.getVideoWidth(), mGBMediaParam.getVideoHeight());
        videoFormat.setInteger(MediaFormat.KEY_BIT_RATE, mGBMediaParam.getVideoBitRate());
        videoFormat.setInteger(MediaFormat.KEY_FRAME_RATE, mGBMediaParam.getVideoFrameRate());
        videoFormat.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface); //COLOR_FormatSurface
        videoFormat.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, mGBMediaParam.getVideoGOP());//关键帧间隔时间 单位s--- 设置到GOP
        Logger.e("video format param", gbMediaParam.toString());
    }


    private void init() {
        try {
            info = new MediaCodec.BufferInfo();
            mVideoEncoder = MediaCodec.createEncoderByType(mGBMediaParam.getVideoMimeType());

            if (videoFormat == null) {
                videoFormat = MediaFormat.createVideoFormat(mGBMediaParam.getVideoMimeType(), mGBMediaParam.getVideoWidth(), mGBMediaParam.getVideoHeight());
                videoFormat.setInteger(MediaFormat.KEY_BIT_RATE, mGBMediaParam.getVideoBitRate());
                videoFormat.setInteger(MediaFormat.KEY_FRAME_RATE, mGBMediaParam.getVideoFrameRate());
                videoFormat.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface); //COLOR_FormatYUV420Flexible
                videoFormat.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, mGBMediaParam.getVideoGOP());//关键帧间隔时间 单位s--- 设置到GOP
            }
            mVideoEncoder.configure(videoFormat,
                    null /* surface */,
                    null /* crypto */,
                    MediaCodec.CONFIGURE_FLAG_ENCODE);
        } catch (Exception e) {
            Logger.e("initMediaCodec: 创建编码器错误 e=" + e.getMessage());
            e.printStackTrace();
        }
        mVideoEncoder.start();

    }

    public MediaFormat getVideoFormat() {
        return videoFormat;
    }

    @Override
    public void startEncoding() {
        super.startEncoding();
        if (mVideoEncoder == null) {
            init();
        }
        if (mVideoEncodeThread == null) {
            mVideoEncodeThread = new VideoEncodeThread();
            mVideoEncodeThread.start();
        }

    }

    @Override
    public void stopEncoding() {
        super.stopEncoding();
        if (mVideoEncodeThread != null) {
            mVideoEncodeThread.interrupt();
            mVideoEncodeThread = null;
        }
        if (mVideoEncoder != null) {

            mVideoEncoder.stop();
            mVideoEncoder.release();
            mVideoEncoder = null;
        }
        if (info != null) {
            info = null;
        }
        if (mVideoDataQueue != null) {
            mVideoDataQueue.clear();
            mVideoDataQueue = null;
        }
        if (NV12Data != null) {
            NV12Data = null;
        }
        if (listener != null) {
            listener = null;
        }
        if (mGBMediaParam != null) {
            mGBMediaParam = null;
        }
    }

    @Override
    public void release() {
        super.release();

        if (mVideoEncodeThread != null) {
            mVideoEncodeThread = null;
        }
        if (mVideoEncoder != null) {
            mVideoEncoder.stop();
            mVideoEncoder.release();
            mVideoEncoder = null;
        }
        if (videoFileManager != null) {
            videoFileManager.closeFile();
            videoFileManager = null;
//            yuvFileManager.closeFile();
        }

    }

    private boolean isMux = false;
    private long syncTime = 0;

    public class VideoEncodeThread extends Thread {
        @Override
        public void run() {
            super.run();
            Thread.currentThread().setName("VideoEncodeThread");

            ByteBuffer byteBuffer = null;

            while (mIsEncoding && !mRequestStop && !Thread.interrupted()) {
                try {

                    //主动触发关键帧
                    if (syncTime == 0) {
                        syncTime = System.currentTimeMillis();
                    } else if (System.currentTimeMillis() - syncTime >= 2000) {
                        Bundle bundle = new Bundle();
                        bundle.putInt(MediaCodec.PARAMETER_KEY_REQUEST_SYNC_FRAME, 0);
                        mVideoEncoder.setParameters(bundle);
                        syncTime = System.currentTimeMillis();
                    }

                    if (null == mVideoDataQueue) {//队列invalid
                        continue;
                    }
                    BaseDataWrapper mBaseData = ((BaseDataWrapper) mVideoDataQueue.poll());

                    if (null == mBaseData || mBaseData.getData().length <= 0 || mBaseData.getLength() <= 0) {//无效数据
                        continue;
                    }

                    int inputBufferid = mVideoEncoder.dequeueInputBuffer(0);
                    if (inputBufferid >= 0) {
                        ByteBuffer inputBuffer = mVideoEncoder.getInputBuffer(inputBufferid);
                        inputBuffer.clear();
                        inputBuffer.put(mBaseData.getData(), 0, mBaseData.getLength());
                        // 将填充好的输入缓冲器的索引提交给编码器，注意第四个参数是缓冲区的时间戳，微秒单位，后一帧的时间应该大于前一帧

                        //Logger.d("encode time " +time);
                        mVideoEncoder.queueInputBuffer(inputBufferid, 0, mBaseData.getLength(), getPTSUs(), 0);

                    } else {
                        Logger.e("查询编码器可用输入缓冲区索引错误 dequeueInputIndex=" + inputBufferid);
                        continue;
                    }

                    int encoderStatus = mVideoEncoder.dequeueOutputBuffer(info, 0);
//                    Logger.w("video encoder status:", String.valueOf(encoderStatus));


                    if (encoderStatus < 0) {

                        if (encoderStatus == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                            if (isMux) continue;
                            isMux = true;
                            videoFormat = mVideoEncoder.getOutputFormat();
                            if (listener != null) listener.onPreparedMux(videoFormat, true);
                        }

                    } else if (encoderStatus >= 0) {

                        if (!isMux) {
                            continue;
                        }


                        ByteBuffer outputBuffer = mVideoEncoder.getOutputBuffer(encoderStatus);
                        info.presentationTimeUs = getPTSUs();

                        int remaining = outputBuffer.remaining();
                        byteBuffer = outputBuffer;

                        if ((info.flags & MediaCodec.BUFFER_FLAG_CODEC_CONFIG) != 0) {
                            // The codec config data was pulled out and fed to the muxer when we got
                            // the INFO_OUTPUT_FORMAT_CHANGED status.  Ignore it.
                            info.size = 0;
                        }

                        if (info.size != 0) {

                            if ((info.flags & MediaCodec.BUFFER_FLAG_KEY_FRAME) != 0) {
                                Logger.e("I Frame ===");
                            }

                            if (listener != null) {
//                                Logger.i("======================================= VIDEO presentationTimeUs:", String.valueOf(info.presentationTimeUs));
                                listener.onReceivedData(outputBuffer, info);
                            }
                            startTime = info.presentationTimeUs;
                        }
                        mVideoEncoder.releaseOutputBuffer(encoderStatus, false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public void signalEndOfInputStream() {
        encode(null);
    }


    private void encode(byte[] data) {
        //
        if (mIsEncoding) {

            // 查询编码器可用输入缓冲区索引

            int inputBufferid = mVideoEncoder.dequeueInputBuffer(-1);
            if (inputBufferid >= 0) {
                ByteBuffer inputBuffer = mVideoEncoder.getInputBuffer(inputBufferid);
                long time = System.currentTimeMillis();
                inputBuffer.clear();
                if (data != null) {
                    inputBuffer.put(data, 0, data.length);
                } else {
                    mVideoEncoder.queueInputBuffer(inputBufferid, 0, 0, time, MediaCodec.BUFFER_FLAG_END_OF_STREAM);
                    return;
                }

                // 将填充好的输入缓冲器的索引提交给编码器，注意第四个参数是缓冲区的时间戳，微秒单位，后一帧的时间应该大于前一帧

                Logger.d("encode time " + time);
                mVideoEncoder.queueInputBuffer(inputBufferid, 0, data.length, time, 0);

            } else {
                Logger.e("查询编码器可用输入缓冲区索引错误 dequeueInputIndex=" + inputBufferid);

            }

        }
    }

    private long startTime = 0;

    private long getPTSUs() {
        long result = System.nanoTime() / 1000L;

        if (result < startTime)
            result = startTime - result + result;
        return result;
    }

    static String pattern = "yyyy-MM-dd HH:mm:ss:SSS";
    private static SimpleDateFormat dateFormat = new SimpleDateFormat(pattern, Locale.CHINA);

    private byte[] outData;
    public void onReceivedData(byte[] data) {
        synchronized (sync) {
            if (mIsEncoding && !mRequestStop) {
                try {
                    if (NV12Data != null && mVideoDataQueue != null) {
                        //添加水印
                        if (null == outData || outData.length < data.length) {
                            outData = new byte[data.length];
                        }
                        YuvOsdUtils.NV21ToNV12(data,mGBMediaParam.getVideoWidth(),mGBMediaParam.getVideoHeight());
                        GregorianCalendar mCalendar = new GregorianCalendar();
                        YuvOsdUtils.addOsd(data, outData, dateFormat.format(mCalendar.getTime()));
                        BaseData encodeData = new BaseDataWrapper();//TODO 循环创建对象，需要使用对象池
                        encodeData.setData(outData);
                        ((BaseDataWrapper) encodeData).setLength(outData.length);
                        mVideoDataQueue.put(encodeData);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }


    }

    @Override
    public void onError() {
        stopEncoding();
        release();
        if (null != listener) {
            listener.onError();
        }
    }

}
