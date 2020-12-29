package com.rokid.remote.record.mediacodec;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.RemoteException;

import com.rokid.glass.videorecorder.utils.Logger;
import com.rokid.recordapi.RKMediaCallback;
import com.rokid.remote.record.mediacodec.base.GBMediaParam;
import com.rokid.remote.record.mediacodec.callback.AudioCallback;
import com.rokid.remote.record.mediacodec.callback.VideoCallback;
import com.rokid.remote.record.mediacodec.manager.AudioRecordManager;
import com.rokid.remote.record.mediacodec.manager.PictureManager;
import com.rokid.remote.record.mediacodec.manager.VideoRecordManager;

public class RecordManager {

    private Context mContext;
    private GBMediaParam mGBMediaParam;

    private static volatile RecordManager mInstance = new RecordManager();
    private static Handler mBackGroundHandler;

    private RecordManager() {
        HandlerThread handlerThread = new HandlerThread("RecordBackground");
        handlerThread.start();
        mBackGroundHandler = new Handler(handlerThread.getLooper());
    }

    public static RecordManager getInstance() {
        return mInstance;
    }


    public void init(Context context, GBMediaParam gbMediaParam) {
        this.mContext = context;
        this.mGBMediaParam = gbMediaParam;
    }

    private Runnable _audioRecordTask;

    public synchronized void startAudioRecording(AudioCallback callback) {
        Logger.i("startAudioRecording ============================");
        _audioRecordTask = () -> {
            AudioRecordManager.getInstance().stopRecording();

            AudioRecordManager.getInstance().startRecording(callback);
        };
        mBackGroundHandler.post(_audioRecordTask);
    }

    public synchronized void stopAudioRecording() {
        Logger.i("stopAudioRecording ============================");
        if (null != _audioRecordTask) {
            mBackGroundHandler.removeCallbacks(_audioRecordTask);
            _audioRecordTask = null;
        }
        mBackGroundHandler.post(() -> {
            AudioRecordManager.getInstance().stopRecording();
        });
    }

    private Runnable _videoRecordTask;

    public synchronized void startVideoRecording(VideoCallback callback) {
        Logger.i("startVideoRecording ============================");
        _videoRecordTask = () -> {
            VideoRecordManager.getInstance().stopRecording();
            VideoRecordManager.getInstance().startRecording(mGBMediaParam, callback);
        };

        mBackGroundHandler.post(_videoRecordTask);
    }

    public synchronized void stopVideoRecording() {
        Logger.i("stopVideoRecording ============================");

        if (null != _videoRecordTask) {
            mBackGroundHandler.removeCallbacks(_videoRecordTask);
            _videoRecordTask = null;
        }
        mBackGroundHandler.post(() -> {
            VideoRecordManager.getInstance().stopRecording();
        });
    }

    public synchronized void takePhoto(RKMediaCallback callback) {
        mBackGroundHandler.post(() -> {
            try {
                callback.onPictureStart();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            PictureManager.getInstance().takePicture(callback);
        });
    }
}
