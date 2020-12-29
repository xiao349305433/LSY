package com.rokid.remote.record.mediacodec.manager;

import android.media.MediaCodec;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.os.Handler;
import android.os.HandlerThread;

import androidx.annotation.NonNull;

import com.rokid.glass.videorecorder.utils.Logger;
import com.rokid.glass.videorecorder.utils.ThreadPoolHelper;
import com.rokid.remote.record.RecordDataBaseManager;
import com.rokid.remote.record.mediacodec.audio.AudioRecorder;
import com.rokid.remote.record.mediacodec.base.GBMediaParam;
import com.rokid.remote.record.mediacodec.callback.VideoCallback;
import com.rokid.remote.record.mediacodec.internal.IEncoderListener;
import com.rokid.remote.record.mediacodec.video.VideoEncoder;
import com.rokid.remote.record.mediacodec.video.VideoRecorder;
import com.rokid.remote.record.util.PathUtils;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Author: heshun
 * Date: 2020/4/15 11:05 AM
 * gmail: shunhe1991@gmail.com
 */
public class MediaMuxerManager2 {

    private volatile MediaMuxer mMediaMuxer;
    private volatile MediaMuxer mMediaMuxer2;
    private volatile MediaMuxerBean mMediaMuxerBean;
    private volatile MediaMuxerBean mMediaMuxerBean2;
    private volatile boolean switchBackMuxer = true;
    private volatile boolean needWriteMuxer = true;
    private volatile boolean needWriteMuxer2 = false;
    public static final long BLOCK_TIME = 20_000_000L * 3 * 5;//录制5分钟一段
    public static final long EARLY_TIME = 3_000_000L;
    private int mVideoIndex;
    private int audioIndex;
    private MediaFormat audioFormat;
    private MediaFormat videoFormat;
    private Handler mEventHandler;
    private AudioRecorder mAudioRecorder;
    private VideoRecorder mVideoRecorder;
    private VideoEncoder mVideoEncoder;
    private volatile boolean isVideoReady = false;
    private volatile boolean isAudioReady = false;
    private VideoCallback mCallback;
    private String mDir;

    public static class MediaMuxerBean {
        MediaMuxer mediaMuxer;
        long createTime;
        long destroyTime;
        String path;
        String dir;
    }

    private long lastPtsTime;

    public MediaMuxerManager2() {
        mMediaMuxerBean = _createMediaMuxer();
        mMediaMuxer = mMediaMuxerBean.mediaMuxer;
        HandlerThread muxerBackground = new HandlerThread("MuxerBackground");
        muxerBackground.start();
        mEventHandler = new Handler(muxerBackground.getLooper());
    }

    private MediaMuxerBean _createMediaMuxer() {
        PathUtils.FileInfo fileInfo = PathUtils.createFile(PathUtils.FILE_TYPE.VIDEO);
        try {
            mDir = fileInfo.getDir();
            MediaMuxer mediaMuxer = new MediaMuxer(fileInfo.getPath(), MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
            MediaMuxerBean mediaMuxerBean = new MediaMuxerBean();
            mediaMuxerBean.mediaMuxer = mediaMuxer;
            mediaMuxerBean.path = fileInfo.getPath();
            return mediaMuxerBean;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void receiveAudio(ByteBuffer data, MediaCodec.BufferInfo info) {
        if (!isMuxerStart) return;

//        Logger.i("receiveAudio mux");
        synchronized (this) {
            if (null != mMediaMuxer && needWriteMuxer) {
                mMediaMuxer.writeSampleData(audioIndex, data, info);
            }
            if (null != mMediaMuxer2 && needWriteMuxer2) {
                mMediaMuxer2.writeSampleData(audioIndex, data, info);
            }


        }
//        Logger.i("writeAudio done");
    }


    public void receiveVideo(ByteBuffer data, MediaCodec.BufferInfo info) {

        if (!isMuxerStart) return;

//        Logger.i("writeVideo start");
        synchronized (this) {

            if (null != mMediaMuxer && needWriteMuxer) {
                mMediaMuxer.writeSampleData(mVideoIndex, data, info);


            }

            if (null != mMediaMuxer2 && needWriteMuxer2) {
                mMediaMuxer2.writeSampleData(mVideoIndex, data, info);
            }

            checkPtsTime(info.presentationTimeUs);
        }
//        Logger.i("writeVideo done");


    }

    private void checkPtsTime(long currentTime) {

        if (lastPtsTime == 0) {
            lastPtsTime = currentTime;
            mMediaMuxerBean.createTime = currentTime;
            mMediaMuxerBean.destroyTime = currentTime + BLOCK_TIME;
        }


        if (!switchBackMuxer) {
            if (null != mMediaMuxerBean2 && null != mMediaMuxer2) {
                if (mMediaMuxerBean2.destroyTime - currentTime <= EARLY_TIME) {
                    mEventHandler.post(() -> {
                        synchronized (this) {
                            mMediaMuxerBean = _createMediaMuxer();
                            mMediaMuxer = mMediaMuxerBean.mediaMuxer;
                            mMediaMuxer.addTrack(audioFormat);
                            mMediaMuxer.addTrack(videoFormat);
                            mMediaMuxer.start();
                            mMediaMuxerBean.createTime = currentTime;
                            mMediaMuxerBean.destroyTime = currentTime + BLOCK_TIME;
                            needWriteMuxer = true;
                        }
                    });

                    switchBackMuxer = !switchBackMuxer;
                }
            }
        } else {
            if (null != mMediaMuxerBean && null != mMediaMuxer) {
                if (mMediaMuxerBean.destroyTime - currentTime <= EARLY_TIME) {
                    mEventHandler.post(() -> {
                        synchronized (this) {
                            mMediaMuxerBean2 = _createMediaMuxer();
                            mMediaMuxer2 = mMediaMuxerBean2.mediaMuxer;
                            mMediaMuxer2.addTrack(audioFormat);
                            mMediaMuxer2.addTrack(videoFormat);
                            mMediaMuxer2.start();
                            mMediaMuxerBean2.createTime = currentTime;
                            mMediaMuxerBean2.destroyTime = currentTime + BLOCK_TIME;
                            needWriteMuxer2 = true;
                        }

                    });

                    switchBackMuxer = !switchBackMuxer;
                }
            }
        }

        if (null != mMediaMuxerBean && currentTime >= mMediaMuxerBean.destroyTime) {
            mEventHandler.post(() -> {
                synchronized (this) {
                    if (!isMuxerStart) {
                        return;
                    }
                    needWriteMuxer = false;
                }

                if (null != mMediaMuxer) {
                    final String path = mMediaMuxerBean.path;
                    ThreadPoolHelper.getInstance().threadExecute(new Runnable() {
                        @Override
                        public void run() {
                            RecordDataBaseManager.getInstance().insertData(path);
                        }
                    });

                    mMediaMuxerBean = null;
                    mMediaMuxer.stop();
                    mMediaMuxer.release();
                    mMediaMuxer = null;
                    Logger.i("======================== Muxer 销毁");
                }

            });
        } else if (null != mMediaMuxerBean2 && currentTime >= mMediaMuxerBean2.destroyTime) {


            mEventHandler.post(() -> {
                synchronized (this) {
                    if (!isMuxerStart) {
                        return;
                    }
                    needWriteMuxer2 = false;
                }

                if (null != mMediaMuxer2) {
                    final String path = mMediaMuxerBean2.path;
                    ThreadPoolHelper.getInstance().threadExecute(new Runnable() {
                        @Override
                        public void run() {
                            RecordDataBaseManager.getInstance().insertData(path);
                        }
                    });
                    mMediaMuxerBean2 = null;
                    mMediaMuxer2.stop();
                    mMediaMuxer2.release();
                    mMediaMuxer2 = null;
                    Logger.i("======================== Muxer2 销毁");
                }

            });
        }
    }


    public void preparedMux(MediaFormat mediaFormat, boolean isVideo) {
        if (isVideo) {
            videoFormat = mediaFormat;
            mVideoIndex = mMediaMuxer.addTrack(mediaFormat);
            isVideoReady = true;
        } else {
            audioFormat = mediaFormat;
            audioIndex = mMediaMuxer.addTrack(mediaFormat);
            isAudioReady = true;
        }

        if (isVideoReady && isAudioReady) {
            Logger.i("prepareMux all ready!!! VideoIndex:", String.valueOf(mVideoIndex),
                    "AudioIndex:", String.valueOf(audioIndex));
            Logger.i("audioFormat:", audioFormat.toString());
            Logger.i("videoFormat:", videoFormat.toString());
            mMediaMuxer.start();
            mMediaMuxerBean.createTime = System.nanoTime() / 1_000L;
            mMediaMuxerBean.destroyTime = mMediaMuxerBean.createTime + BLOCK_TIME;
            isMuxerStart = true;

            if (null != mCallback) mCallback.onVideoStart();
        }
    }


    public void prepare(GBMediaParam param) {
        if (null != mAudioRecorder) mAudioRecorder.stopRecording();
        if (null != mVideoRecorder) mVideoRecorder.stopRecording();
        if (null != mVideoEncoder) mVideoEncoder.stopEncoding();

        mAudioRecorder = new AudioRecorder();
        mAudioRecorder.prepare(new IEncoderListener() {
            @Override
            public void onReceivedData(ByteBuffer data, @NonNull MediaCodec.BufferInfo info) {
                receiveAudio(data, info);
            }

            @Override
            public void onPreparedMux(MediaFormat mediaFormat, boolean isVideo) {
                Logger.i("prepare audio");
                preparedMux(mediaFormat, false);
            }

            @Override
            public void onError() {
                stop();
            }
        });

        mVideoRecorder = new VideoRecorder();
        mVideoEncoder = new VideoEncoder();
        mVideoEncoder.prepare(param, new IEncoderListener() {
            @Override
            public void onReceivedData(ByteBuffer data, @NonNull MediaCodec.BufferInfo info) {
                receiveVideo(data, info);
            }

            @Override
            public void onPreparedMux(MediaFormat mediaFormat, boolean isVideo) {
                Logger.i("prepare video");
                preparedMux(mediaFormat, true);
            }

            @Override
            public void onError() {
                stop();
            }
        });
        mVideoRecorder.prepare(param, mVideoEncoder);
    }

    private volatile boolean isMuxerStart = false;

    public void start(VideoCallback callback) {
        if (isMuxerStart) {
            Logger.w("has muxer started!");
            return;
        }

        this.mCallback = callback;

        synchronized (this) {
            if (null != mAudioRecorder) {
                mAudioRecorder.startRecording(false, null);
            }

            if (null != mVideoRecorder) {
                mVideoRecorder.startRecording();
            }

            if (null != mVideoEncoder) {
                mVideoEncoder.startEncoding();
            }

            if (isAudioReady && isVideoReady && !isMuxerStart) {
                mMediaMuxer.start();
                isMuxerStart = true;
            }
        }

    }

    public void stop() {
        if (!isMuxerStart) {
            Logger.w("has muxer stopped");
            return;
        }

        try {
            isMuxerStart = false;
            synchronized (this) {
                Logger.i("stop media muxer=======================");

                if (null != mAudioRecorder) {
                    mAudioRecorder.stopRecording();
                }

                if (null != mVideoRecorder) {
                    mVideoRecorder.stopRecording();
                    mVideoRecorder.release();
                }

                if (null != mVideoEncoder) {
                    mVideoEncoder.stopEncoding();
                    mVideoEncoder.release();
                }

                if (null != mMediaMuxer) {
                    final String path = mMediaMuxerBean.path;
                    ThreadPoolHelper.getInstance().threadExecute(new Runnable() {
                        @Override
                        public void run() {
                            RecordDataBaseManager.getInstance().insertData(path);
                        }
                    });
                    mMediaMuxerBean = null;
                    mMediaMuxer.stop();
                    mMediaMuxer.release();
                    mMediaMuxer = null;
                }


                if (null != mMediaMuxer2) {
                    final String path = mMediaMuxerBean2.path;
                    ThreadPoolHelper.getInstance().threadExecute(new Runnable() {
                        @Override
                        public void run() {
                            RecordDataBaseManager.getInstance().insertData(path);
                        }
                    });
                    mMediaMuxerBean2 = null;
                    mMediaMuxer2.stop();
                    mMediaMuxer2.release();
                    mMediaMuxer2 = null;
                }

                if (null != mCallback) {
                    mCallback.onVideoStopped(mDir);
                }
                mCallback = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
