package com.rokid.remote.record;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;

import com.rokid.glass.videorecorder.utils.Logger;
import com.rokid.recordapi.IRKMediaService;
import com.rokid.recordapi.RKMediaCallback;
import com.rokid.remote.record.util.RKSingleton;

import java.lang.ref.WeakReference;

/**
 * Author: heshun
 * Date: 2020/4/22 4:15 PM
 * gmail: shunhe1991@gmail.com
 */
public class RKMediaManager extends RKMediaCallback.Stub {

    private static WeakReference<Context> mContextRef;
    private IRKMediaService proxy;
    private volatile boolean isProxyReady;
    private volatile boolean isBind;

    private static RKSingleton<RKMediaManager> singleton = new RKSingleton<RKMediaManager>() {
        @Override
        public RKMediaManager create() {
            return new RKMediaManager();
        }
    };

    private Handler mRetryHandler = new Handler(Looper.getMainLooper());
    private Runnable _rebind = () -> {
        if (!bindMediaService()) {
            isBind = false;
            retryBindService();
        }
    };
    private RKMediaCallback mediaCallback;

    private void retryBindService() {
        mRetryHandler.removeCallbacks(_rebind);
        mRetryHandler.postDelayed(_rebind, 5000);
    }

    private boolean bindMediaService() {
        Context context = mContextRef.get();
        if (null == context) {
            throw new RuntimeException("context is invalid, can not bind");
        }

        if (!isBind){

            isBind = true;
            Intent service = new Intent();
            ComponentName componentName = new ComponentName(context.getPackageName(), RKMediaService.class.getName());
            service.setComponent(componentName);
            return context.bindService(service, mediaConnection, Context.BIND_AUTO_CREATE);
        }
        return true;
    }

    private RKMediaManager() {
    }

    private ServiceConnection mediaConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Logger.w("service bind ", name.getPackageName(), name.getClassName());
            proxy = IRKMediaService.Stub.asInterface(service);
            initMediaService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Logger.w("service unbind ", name.getPackageName(), name.getClassName());
            proxy = null;
            isProxyReady = false;
        }
    };

    private void initMediaService() {
        if (null != proxy) {
            try {
                proxy.addMediaCallback(this);
                proxy.init();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public static RKMediaManager getInstance() {
        return singleton.get();
    }

    public void destroy() {
        try {
            proxy.removeMediaCallback(this);
            Context context = mContextRef.get();
            if (null == context) return;
            if (isBind){

                isBind = false;
                context.unbindService(mediaConnection);
            }
            mediaCallback = null;
            isProxyReady = false;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void init(Context context) {
        if (isProxyReady) return;

        mContextRef = new WeakReference<>(context);
        if (!bindMediaService()) {
            isBind = false;
            retryBindService();
        }
    }


    @Override
    public void onServiceReady(boolean success, String message) throws RemoteException {
        if (null != mediaCallback) mediaCallback.onServiceReady(success, message);
        isProxyReady = true;
    }

    @Override
    public void onAudioStart() throws RemoteException {
        if (null != mediaCallback) mediaCallback.onAudioStart();
    }

    @Override
    public void onAudioStopped(String filePath) throws RemoteException {
        if (null != mediaCallback) mediaCallback.onAudioStopped(filePath);
    }

    @Override
    public void onAudioFailed(int errorCode, String errorMessage) throws RemoteException {
        if (null != mediaCallback) mediaCallback.onAudioFailed(errorCode, errorMessage);
    }

    @Override
    public void onVideoStart() throws RemoteException {
        if (null != mediaCallback) mediaCallback.onVideoStart();
    }

    @Override
    public void onVideoStopped(String filePath) throws RemoteException {
        if (null != mediaCallback) mediaCallback.onVideoStopped(filePath);
    }

    @Override
    public void onVideoFailed(int errorCode, String errorMessage) throws RemoteException {
        if (null != mediaCallback) mediaCallback.onVideoFailed(errorCode, errorMessage);
    }

    @Override
    public void onPictureStart() throws RemoteException {
        if (null != mediaCallback) mediaCallback.onPictureStart();
    }

    @Override
    public void onPictureStopped(String filePath) throws RemoteException {
        if (null != mediaCallback) mediaCallback.onPictureStopped(filePath);
    }

    @Override
    public void onPictureFailed(int errorCode, String errorMessage) throws RemoteException {
        if (null != mediaCallback) mediaCallback.onPictureFailed(errorCode, errorMessage);
    }

    public void startRecordVideo() {
        if (null == proxy) {
            Logger.w("media proxy is invalid");
            return;
        }
        try {
            proxy.startRecordVideo();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void stopRecordVideo() {
        if (null == proxy) {
            Logger.w("media proxy is invalid");
            return;
        }
        try {
            proxy.stopRecordVideo();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void startRecordAudio() {
        if (null == proxy) {
            Logger.w("media proxy is invalid");
            return;
        }
        try {
            proxy.startRecordAudio();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void stopRecordAudio() {
        if (null == proxy) {
            Logger.w("media proxy is invalid");
            return;
        }
        try {
            proxy.stopRecordAudio();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void takePicture() {
        if (null == proxy) {
            Logger.w("media proxy is invalid");
            return;
        }
        try {
            proxy.takePicture();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void addMediaCallback(RKMediaCallback callback) {
        mediaCallback = callback;
        if (isProxyReady) {
            try {
                callback.onServiceReady(true, null);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void removeMediaCallback(RKMediaCallback callback) {
        mediaCallback = callback;
    }

    public boolean isProxyReady() {
        return this.isProxyReady;
    }

    public String getCurrentState() {
        if (null == proxy) {
            Logger.w("media proxy is invalid");
            return null;
        }

        try {
            return proxy.getCurrentState();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public long getStartTime() {
        if (null == proxy) {
            Logger.w("media proxy is invalid");
            return 0;
        }

        try {
            return proxy.getRecordStartTime();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
