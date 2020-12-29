package com.gpsdk.demo;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;


/**
 * Created by Administrator
 *
 * @author 猿史森林
 * Date: 2017/11/28
 * Class description:
 */
public class App extends Application {

    @SuppressLint("StaticFieldLeak")
    private static Application sApplication;

    public static Context getContext() {
        if (sApplication != null) return sApplication;
        throw new NullPointerException("u should init first");
    }

    public static void init(Application application) {
        App.sApplication = application;
    }
}
