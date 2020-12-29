package com.rokid.glass.videorecorder.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.rokid.glass.videorecorder.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @date 2019-10-16
 */

public class BreathingView extends AppCompatImageView {
    private Context mContext;
    private ImageView mBreathing;
    private Timer mTimer;
    private boolean isOpen = true;
    private int mIndex = 0;

    private final int BREATH_INTERVAL_TIME = 1000;

    public BreathingView(Context context) {
        this(context, null);
    }

    public BreathingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BreathingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    doFadeIn();
                    break;
                case 2:
                    doFadeOut();
                    break;
            }
        }
    };

    private void init(final Context context) {
        mBreathing = this;
        this.mContext = context;
    }

    private void startTimer() {
        mTimer = new Timer(true);
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (isOpen) {
                    if (mIndex % 2 == 0) {
                        mIndex = 0;
                    }

                    mIndex++;
                    mHandler.sendEmptyMessage(mIndex);
                }
            }
        };

        mTimer.schedule(task, 0, BREATH_INTERVAL_TIME);

    }

    private void doFadeIn() {
        mBreathing.clearAnimation();
        mBreathing.setAnimation(getFadeIn());
    }

    private void doFadeOut() {
        mBreathing.clearAnimation();
        mBreathing.setAnimation(getFadeOut());
    }

    private Animation getFadeIn() {
        Animation fadeIn = AnimationUtils.loadAnimation(mContext,
                R.anim.breathing_fade_in);
        fadeIn.setDuration(BREATH_INTERVAL_TIME);
        fadeIn.setStartOffset(100);
        return fadeIn;
    }

    private Animation getFadeOut() {
        Animation fadeOut = AnimationUtils.loadAnimation(mContext,
                R.anim.breathing_fade_out);
        fadeOut.setDuration(BREATH_INTERVAL_TIME);
        fadeOut.setStartOffset(100);
        return fadeOut;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        isOpen = true;
        startTimer();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isOpen = false;
        if (null != mTimer) {
            mTimer.cancel();
        }

        mTimer = null;
    }
}
