package com.wu.loushanyun.basemvp.init;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.elvishew.xlog.XLog;
import com.sensoro.sensor.kit.SensoroDeviceManager;
import com.sensoro.sensor.kit.callback.SensoroDeviceListener;
import com.sensoro.sensor.kit.utils.Logger;

import met.hx.com.base.BuildConfig;
import met.hx.com.base.base.application.AppContext;

public class SensoroBlueManager {
    private static final Handler HANDLER = new Handler(Looper.getMainLooper());
    private static SensoroDeviceManager sensoroDeviceManager = SensoroDeviceManager.getInstance(AppContext.getInstance().getApplication());
    private static Application.ActivityLifecycleCallbacks mCallbacks = new Application.ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(Activity activity, Bundle bundle) {
        }

        @Override
        public void onActivityStarted(Activity activity) {
        }

        @Override
        public void onActivityResumed(Activity activity) {
        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            SensoroBlueManager.stopSearch();
            SensoroBlueManager.getSensoroDeviceManager().setSensoroDeviceListener(null);
        }
    };
    /**
     * 初始化
     */
    public static void init(Application application) {
        sensoroDeviceManager.setScanTypeMode(SensoroDeviceManager.SCAN_API_TYPE_OLD);
        sensoroDeviceManager.setBackgroundMode(false);
        sensoroDeviceManager.setForegroundScanPeriod(3 * 1000);
        sensoroDeviceManager.setForegroundBetweenScanPeriod(7 * 1000);
        sensoroDeviceManager.setOutOfRangeDelay(7 * 1000);
        sensoroDeviceManager.setBackgroundMode(false);
        Logger.setEnable(BuildConfig.DEBUG);
        application.registerActivityLifecycleCallbacks(mCallbacks);
    }

    /**
     * 扫描
     */
    public static SensoroDeviceManager searchBlueTooth(SensoroDeviceListener sensoroDeviceListener) {
        HANDLER.post(new Runnable() {
            @Override
            public void run() {
                sensoroDeviceManager.stopService();
                XLog.i("停止搜索蓝牙");
                try {
                    SensoroBlueManager.getSensoroDeviceManager().setSensoroDeviceListener(sensoroDeviceListener);
                    sensoroDeviceManager.startService();
                    XLog.i("开始搜索蓝牙");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return sensoroDeviceManager;
    }

    /**
     * 停止扫描
     */
    public static void stopSearch() {
        XLog.i("停止搜索蓝牙");
        SensoroDeviceManager.getInstance(AppContext.getInstance().getApplication()).stopService();
    }

    public static SensoroDeviceManager getSensoroDeviceManager() {
        return sensoroDeviceManager;
    }
}
