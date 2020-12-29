package com.wu.loushanyun.basemvp.init;

import android.app.Application;

import met.hx.com.base.base.application.BaseChildApplication;

public class LouShanYunApplication extends BaseChildApplication {

    @Override
    public void onCreateAsLibrary(Application application) {
        BleBlueToothTool.init();
        SensoroBlueManager.init(application);
    }

    @Override
    public void onLowMemoryAsLibrary(Application application) {

    }

    @Override
    public void onTrimMemoryAsLibrary(Application application, int level) {

    }
}
