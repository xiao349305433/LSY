package com.rokid.remote.record.util;

import android.media.MediaCodec;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.os.Handler;
import android.os.HandlerThread;


import com.rokid.glass.videorecorder.utils.Logger;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author : heshun
 * @version : v1.0.0
 * @date : 2020/4/17 10:02 PM
 * @description: This is MuxerHelper
 */
public class MuxerHelper {

    private Handler mHandler;
    private MediaFormat mVideoFormat;
    private MediaFormat mAudioFormat;
    private MediaMuxer mMediaMuxer;
    private MediaMuxer prevMediaMuxer;
    private int mAudioIndex;
    private int mVideoIndex;
    private ReadWriteLock lock = new ReentrantReadWriteLock();
    private Lock write;
    private Lock read;
    private volatile boolean hasStopped;
    private volatile boolean hasAudioReady;
    private volatile boolean hasVideoReady;
    private volatile long prevPtsTime;

    public MuxerHelper(){
        HandlerThread muxerThread = new HandlerThread("Muxer");
        muxerThread.start();
        mHandler = new Handler(muxerThread.getLooper());
        write = this.lock.writeLock();
        read = this.lock.readLock();
    }


    public void startMuxer(){
        hasStopped = false;
    }

    private Runnable _startMuxer = ()-> {
        if (hasVideoReady && hasAudioReady && !hasStopped) {
            write.lock();
            try{
                Logger.i("create mediaMuxer start");
                prevMediaMuxer = mMediaMuxer;
                mMediaMuxer = new MediaMuxer(PathUtils.generateFileName(PathUtils.FILE_TYPE.VIDEO), MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
                Logger.i("create mediaMuxer done");

                mAudioIndex = mMediaMuxer.addTrack(mAudioFormat);
                mVideoIndex = mMediaMuxer.addTrack(mVideoFormat);
                mMediaMuxer.start();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                write.unlock();
                if (null != prevMediaMuxer) {
                    prevMediaMuxer.stop();
                    prevMediaMuxer.release();
                    prevMediaMuxer = null;
                }

            }
        }
    };

    public void checkForMuxerStart(){
        if (hasVideoReady && hasAudioReady) {
            mHandler.post(_startMuxer);
        }
    }

    public void checkPtsTime(long currentTime){
        if (prevPtsTime == 0 ) {
            prevPtsTime = currentTime;
        }
    }

    public void prepareAudio(MediaFormat audioFormat){
        mAudioFormat = audioFormat;
        mMediaMuxer.addTrack(audioFormat);
        hasAudioReady = true;
        checkForMuxerStart();
    }

    public void prepareVideo(MediaFormat videoFormat){
        mVideoFormat = videoFormat;
        mMediaMuxer.addTrack(videoFormat);
        hasVideoReady = true;
        checkForMuxerStart();
    }


    private void _stopMuxer(){
        write.lock();
        try{
            mMediaMuxer.stop();
            mMediaMuxer.release();
        }finally {
            write.unlock();
        }
    }

    public void stopMuxer(){
        hasStopped = true;
        mHandler.post(() -> _stopMuxer());
    }

    public void writeAudioSampleData(ByteBuffer data, MediaCodec.BufferInfo info){

        read.lock();
        try {
            if (null != mMediaMuxer && !hasStopped) {
                mMediaMuxer.writeSampleData(mAudioIndex, data, info);
            }
        }finally {
            read.unlock();
        }
    }

    public void writeVideoSampleData(ByteBuffer data, MediaCodec.BufferInfo info){
        read.lock();
        try {
            if (null != mMediaMuxer && !hasStopped) {
                mMediaMuxer.writeSampleData(mAudioIndex, data, info);
            }
        }finally {
            read.unlock();
        }
    }

}
