package com.rokid.glass.cjcc;

import android.app.Application;
import android.view.WindowManager;

import com.rokid.glass.instruct.VoiceInstruction;
import com.rokid.glass.ui.autosize.AutoSizeConfig;
import com.rokid.glass.videorecorder.camera.Camera2Manager;
import com.rokid.glass.videorecorder.camera.CameraParams;
import com.rokid.glass.videorecorder.manager.FloatWindowManager;
import com.rokid.glass.videorecorder.utils.BaseLibrary;
import com.rokid.glass.videorecorder.utils.DensityUtils;
import com.rokid.remote.record.RKMediaManager;
import com.rokid.remote.record.RecordDataBaseManager;
import com.rokid.remote.record.database.RecordDatabase;

public class MyApplication extends Application {
    private int mTimerLeft, mTimerTop;

    @Override
    public void onCreate() {
        super.onCreate();

        initCameraService();
        initFloatWindow();
        AutoSizeConfig.getInstance().setExcludeFontScale(true);
        BaseLibrary.initialize(this);
        initVoiceInstruction();
    }

    private void initVoiceInstruction(){
        VoiceInstruction.init(this);
    }

    private void initCameraService(){
        Camera2Manager.getInstance().init(CameraParams.PREVIEW_WIDTH,CameraParams.PREVIEW_HEIGHT);
    }
    private void initFloatWindow() {
        int width = DensityUtils.getWidth(this);
        int iconWidth = getResources().getDimensionPixelSize(R.dimen.glass_icon_width);
        mTimerLeft = width / 2 - (int) (iconWidth * 1.8);
        mTimerTop = getResources().getDimensionPixelSize(R.dimen.glass_status_timer_top);

        FloatWindowManager
                .with(getApplicationContext())
                .setWidth(iconWidth)
                .setWindowManager((WindowManager) getSystemService(WINDOW_SERVICE))
                .setXOffset(mTimerLeft)
                .setYOffset(mTimerTop)
                .setLiveLayoutId(R.layout.layout_glass_status_live)
                .setRecordLayoutId(R.layout.layout_glass_status_record)
                .build();
//        MediaRecorderManager.getInstance().init(getApplicationContext());
        RKMediaManager.getInstance().init(getApplicationContext());
        RecordDataBaseManager.getInstance().init();
        RecordDatabase.getInstance().init(getApplicationContext());
    }
}
