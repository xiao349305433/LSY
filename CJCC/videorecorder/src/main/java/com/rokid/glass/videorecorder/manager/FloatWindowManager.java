package com.rokid.glass.videorecorder.manager;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.rokid.glass.videorecorder.R;
import com.rokid.glass.videorecorder.utils.BaseLibrary;
import com.rokid.glass.videorecorder.utils.DensityUtils;
import com.rokid.glass.videorecorder.utils.Logger;
import com.rokid.glass.videorecorder.utils.ViewUtils;
import com.rokid.glass.videorecorder.view.TimerView;
import com.rokid.remote.record.RKMediaManager;


/**
 * @date 2019-08-08
 */

public class FloatWindowManager {
    private static Builder mBuilder;
    private volatile boolean mIsLiveAdd, mIsRecordAdd;
    private volatile boolean isRecording;
    private volatile boolean isLiving;

    private int mHalfWidth;
    private int mLiveX;
    private int mThreeWidth;

    private String mStartRecordStr;
    private String mStopRecordStr;

    private String mStartLiveStr;
    private String mStopLiveStr;

    private int mDelayTime = 350;
    private FloatWindowManager() {
        int width = DensityUtils.getWidth(mBuilder.context);
        int iconWidth = mBuilder.context.getResources().getDimensionPixelSize(R.dimen.glass_icon_width);
        mHalfWidth = width / 2 - iconWidth / 2;
        mThreeWidth = width / 2 - (int) (iconWidth * 1.8);
        mLiveX = width - (int) (iconWidth * 1.5);

        mStartRecordStr = mBuilder.context.getString(R.string.message_start_record);
        mStopRecordStr = mBuilder.context.getString(R.string.message_stop_record);
        mStartLiveStr = mBuilder.context.getString(R.string.message_start_live);

    }

    private static final class Holder {
        private static final FloatWindowManager INSTANCE = new FloatWindowManager();
    }

    public static FloatWindowManager getInstance() {
        return Holder.INSTANCE;
    }

    public static Builder with(final Context context) {
        mBuilder = new Builder().setContext(context);
        return mBuilder;
    }

    public void showLive() {
        if (mIsLiveAdd) {
            return;
        }

        mIsLiveAdd = true;
        mBuilder.params.x = mLiveX;
        mBuilder.windowManager.addView(mBuilder.liveView, mBuilder.params);
        mBuilder.liveTimer.setBase(SystemClock.elapsedRealtime());
        mBuilder.liveTimer.start();
    }

    public void hideLive() {
        if(!mIsLiveAdd){
            return;
        }
        if (null != mBuilder.liveTimer) {
            mBuilder.liveTimer.stop();
        }
        mBuilder.windowManager.removeView(mBuilder.liveView);
        mIsLiveAdd = false;
    }

    public void showRecord() {
        Logger.d("##### checkRecord showRecord: %s", mIsRecordAdd);
        if (mIsRecordAdd) {
            return;
        }

        mIsRecordAdd = true;
        String activityName = BaseLibrary.getInstance().getTopActivity().getClass().getName();
        Logger.d("##### checkRecord top activity %s", activityName);
        if (!TextUtils.isEmpty(activityName) && checkStatusBar(activityName)) {
            mBuilder.params.x = mHalfWidth;
        } else {
            mBuilder.params.x = mThreeWidth;
        }

        mBuilder.windowManager.addView(mBuilder.recordView, mBuilder.params);

        if (null != mBuilder.recordTimer) {
            mBuilder.recordTimer.setBase(SystemClock.elapsedRealtime());
            mBuilder.recordTimer.start();
        }
    }

    public void hideRecord() {
        if (mIsRecordAdd) {
            mBuilder.windowManager.removeView(mBuilder.recordView);
            mIsRecordAdd = false;
        }

        if (null != mBuilder.recordTimer) {
            mBuilder.recordTimer.stop();
        }
    }

    public void checkRecord() {
        Logger.d("##### checkRecord isRecording: %s", isRecording);
        if (isRecording) {
            isRecording = false;
            stopRecord();
        } else {
            isRecording = true;
            startRecord();
        }
    }

    public void updateRecord() {
        if (isRecording) {
            String activityName = BaseLibrary.getInstance().getTopActivity().getClass().getName();
            Logger.d("##### checkRecord top activity %s", activityName);
            if (!TextUtils.isEmpty(activityName) && checkStatusBar(activityName)) {
                mBuilder.params.x = mHalfWidth;
            } else {
                mBuilder.params.x = mThreeWidth;
            }
            mBuilder.windowManager.updateViewLayout(mBuilder.recordView, mBuilder.params);
        }
    }

    /**
     * stop all record
     */
    public void stopAllRecord() {
        isRecording = false;
        hideRecord();
        stopRecord();
    }

    /**
     * start record
     */
    public void startRecord() {
        RKMediaManager.getInstance().startRecordVideo();
        showRecord();
    }

    /**
     * stop record
     */
    private void stopRecord() {
        RKMediaManager.getInstance().stopRecordVideo();
        hideRecord();
    }

    public void destory() {
        removeLiveView();
    }

    private void removeLiveView() {
        if (mIsLiveAdd && null != mBuilder.liveView) {
            mBuilder.windowManager.removeView(mBuilder.liveView);
            mIsLiveAdd = false;
        }
    }

    private void removeRecordView() {
        if (mIsRecordAdd && null != mBuilder.recordView) {
            mBuilder.windowManager.removeView(mBuilder.recordView);
            mIsRecordAdd = false;
        }
    }

    private boolean checkStatusBar(final String activityName) {
        return activityName.equals("com.rokid.glass.gallery.ui.GalleryActivity");
    }

    public static class Builder {
        private Context context;
        private WindowManager windowManager;
        private WindowManager.LayoutParams params;
        private int liveLayoutId;
        private View liveView;
        private int recordLayoutId;
        private View recordView;
        private int width = ViewGroup.LayoutParams.WRAP_CONTENT;
        private int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        private int gravity = Gravity.TOP | Gravity.START;
        private int xOffset;
        private int yOffset;

        private TimerView liveTimer;
        private TimerView recordTimer;

        public Builder setContext(Context context) {
            this.context = context;
            return this;
        }

        public Builder setWindowManager(WindowManager windowManager) {
            this.windowManager = windowManager;
            return this;
        }

        public Builder setWidth(int width) {
            this.width = width;
            return this;
        }

        public Builder setHeight(int height) {
            this.height = height;
            return this;
        }

        public Builder setGravity(int gravity) {
            this.gravity = gravity;
            return this;
        }

        public Builder setXOffset(int xOffset) {
            this.xOffset = xOffset;
            return this;
        }

        public Builder setYOffset(int yOffset) {
            this.yOffset = yOffset;
            return this;
        }

        public Builder setLiveLayoutId(int liveLayoutId) {
            this.liveLayoutId = liveLayoutId;
            return this;
        }

        public Builder setRecordLayoutId(int recordLayoutId) {
            this.recordLayoutId = recordLayoutId;
            return this;
        }

        public void build() {
            if (null == windowManager || null == context) {
                throw new IllegalArgumentException("Window manager not set!");
            }

            if (liveView == null && liveLayoutId == 0) {
                throw new IllegalArgumentException("View has not been set!");
            }

            if (liveView == null) {
                liveView = ViewUtils.inflate(context, liveLayoutId);
                liveTimer = liveView.findViewById(R.id.status_bar_timer);
            }

            if (recordView == null) {
                recordView = ViewUtils.inflate(context, recordLayoutId);
                recordTimer = recordView.findViewById(R.id.status_bar_timer);
            }

            params = new WindowManager.LayoutParams();
            if (Build.VERSION.SDK_INT >= 26) {
                params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            } else {
                params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
            }

            params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR |
                    WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
            params.width = width;
            params.height = height;
            if (gravity == 0) {
                params.gravity = Gravity.TOP | Gravity.START;
            } else {
                params.gravity = gravity;
            }
            params.format = PixelFormat.RGBA_8888;
            params.x = xOffset;
            params.y = yOffset;

            FloatWindowManager manager = getInstance();
        }
    }

    /**
     * 是否正在录像
     * @return
     */
    public boolean isRecording(){
        return isRecording;
    }
}