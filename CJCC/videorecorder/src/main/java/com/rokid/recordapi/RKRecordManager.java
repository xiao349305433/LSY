package com.rokid.recordapi;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;

import java.util.List;

/**
 * Author: heshun
 * Date: 2020/7/7 9:09 PM
 * gmail: shunhe1991@gmail.com
 */
public class RKRecordManager {

    private Context mContext;
//    public static final String PACKAGE_NAME = "com.rokid.remote.record";
    public static final String CLASS_NAME = "com.rokid.remote.record.RKMediaService";
    private IRKMediaService mRKMediaService;
    private OnProxyReady onProxyReady;

    private RKRecordManager() {

    }

    private static RKRecordManager instance;

    public static RKRecordManager getInstance() {
        if (null == instance) {
            synchronized (RKRecordManager.class) {
                if (null == instance) {
                    instance = new RKRecordManager();
                }
            }
        }
        return instance;
    }

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mRKMediaService = IRKMediaService.Stub.asInterface(service);
            onProxyReady.onProxyReady();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            onProxyReady.onProxyDisconnected();
            mRKMediaService = null;
            new Handler().postDelayed(()->{
                bindingService();
            }, 3000);
        }
    };

    private void bindingService() {
        Intent intent = new Intent();
        ComponentName component = new ComponentName(mContext.getPackageName(), CLASS_NAME);
        intent.setComponent(component);
        if (!mContext.bindService(intent, conn, Context.BIND_AUTO_CREATE)) {
            throw new RuntimeException("this service is not exists!");
        }
    }

    public void init(Context context, OnProxyReady onProxyReady) {
        if (null == context) throw new RuntimeException("context is invalid");

        this.onProxyReady = onProxyReady;
        this.mContext = context;

        bindingService();
    }

    public void startRecordVideo() {
        if (null != mRKMediaService){
            try {
                mRKMediaService.startRecordVideo();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopRecordVideo() {
        if (null != mRKMediaService){
            try {
                mRKMediaService.stopRecordVideo();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void startRecordAudio() {
        if (null != mRKMediaService){
            try {
                mRKMediaService.startRecordAudio();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopRecordAudio() {
        if (null != mRKMediaService) {
            try {
                mRKMediaService.stopRecordAudio();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void takePicture() {
        if (null != mRKMediaService){
            try {
                mRKMediaService.takePicture();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }


    public int queryRecordCount(String mediaType) {
        try {
            return mRKMediaService.queryRecordCount(mediaType);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int queryRecordCountWithTimestamp(String mediaType, long startTime, long endTime) {
        try {
            return mRKMediaService.queryRecordCountWithTimestamp(mediaType, startTime, endTime);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public List<RecordEntity> queryRecord(String mediaType) {
        try {
            return mRKMediaService.queryRecord(mediaType);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<RecordEntity> queryRecordWithTimestamp(String mediaType, long startTime, long endTime) {
        try {
            return mRKMediaService.queryRecordWithTimestamp(mediaType, startTime, endTime);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<RecordEntity> queryRecordWithTimestampAndPage(String mediaType, long startTime, long endTime, int page, int pageCount) {
        try {
            return mRKMediaService.queryRecordWithTimestampAndPage(mediaType, startTime, endTime, page, pageCount);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteAll(String mediaType) {
        try {
            mRKMediaService.deleteAll(mediaType);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void deleteWithTime(String mediaType, long startTime, long endTime) {
        try {
            mRKMediaService.deleteWithTime(mediaType, startTime, endTime);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void deleteWithIds(int[] ids) {
        try {
            mRKMediaService.deleteWithIds(ids);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    public static interface OnProxyReady {
        void onProxyReady();
        void onProxyDisconnected();
    }
}
