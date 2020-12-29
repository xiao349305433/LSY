package com.rokid.glass.videorecorder.utils;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import java.lang.ref.WeakReference;

/**
 * Author: zhuohf
 * Version: V0.1 2018/2/19
 */
public class BaseLibrary {

    private static volatile BaseLibrary mInstance;
    private volatile Activity mTopActivity;

    private WeakReference<Application> applicationWeak;

    public static BaseLibrary getInstance() {
        if (null == mInstance) {
            synchronized (BaseLibrary.class) {
                if (null == mInstance) {
                    mInstance = new BaseLibrary();
                }
            }
        }
        return mInstance;
    }

    public static void initialize(Application application) {
        Logger.d("Start to init the XBase lib.");

        // Init Config
        getInstance().applicationWeak = new WeakReference<>(application);
        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                getInstance().setTopActivity(activity);
            }

            @Override
            public void onActivityStarted(Activity activity) {
                getInstance().setTopActivity(activity);
            }

            @Override
            public void onActivityResumed(Activity activity) {
                getInstance().setTopActivity(activity);
            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }

    public Activity getTopActivity() {
        return mTopActivity;
    }
    public BaseLibrary setTopActivity(Activity topActivity) {
        this.mTopActivity = topActivity;
        return this;
    }

    public Application getContext() {
        if (null == applicationWeak || null == applicationWeak.get()) {
            applicationWeak = new WeakReference<>(getApplicationContext());
        }

        return applicationWeak.get();
    }

    public static Application getApplicationContext() {
        try {
            Application application = (Application) Class.forName("android.app.ActivityThread")
                    .getMethod("currentApplication").invoke(null, (Object[]) null);
            if (application != null) {
                return application;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Application application = (Application) Class.forName("android.app.AppGlobals")
                    .getMethod("getInitialApplication").invoke(null, (Object[]) null);
            if (application != null) {
                return application;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


}
