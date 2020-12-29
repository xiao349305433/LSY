/*
 * Copyright (c) 2016 Jacksgong(blog.dreamtobe.cn).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package met.hx.com.base.base.webview;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;

import java.lang.ref.WeakReference;

import met.hx.com.base.baseinterface.ISmoothTarget;

/**
 * Created by Jacksgong on 2/2/16.
 * <p/>
 * handle the case of the internal of the percent between the current and the last is
 * too large to smooth for the target
 *
 * @see ISmoothTarget
 */
public class SmoothHandler extends Handler {
    final WeakReference<ISmoothTarget> targetWeakReference;

    private float aimPercent;
    private float minInternalPercent = 0.03f; // 3%
    private float smoothInternalPercent = 0.01f; // 1%
    private int smoothIncreaseDelayMillis = 1; // 1ms

    private final String TAG = "SmoothHandler";
    public static boolean NEED_LOG = false;


    /**
     * generally use for the progress widget
     *
     * @param targetWeakReference the weak reference of the smooth target
     */
    public SmoothHandler(WeakReference<ISmoothTarget> targetWeakReference) {
        this(targetWeakReference, Looper.getMainLooper());
    }

    public SmoothHandler(WeakReference<ISmoothTarget> targetWeakReference, Looper looper) {
        super(looper);
        this.targetWeakReference = targetWeakReference;
        this.aimPercent = targetWeakReference.get().getPercent();
        clear();
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if (this.targetWeakReference == null || this.targetWeakReference.get() == null) {
            return;
        }

        final ISmoothTarget target = targetWeakReference.get();

        final float currentPercent = target.getPercent();
        final float desiredPercentDelta = calculatePercent(currentPercent);
        setPercent2Target(Math.min(currentPercent + desiredPercentDelta, aimPercent));
        final float realPercentDelta = target.getPercent() - currentPercent;


        if (target.getPercent() >= this.aimPercent || target.getPercent() >= 1 ||
                (target.getPercent() == 0 && this.aimPercent == 0)) {
            if (NEED_LOG) {
                Log.d(TAG, String.format("finish aimPercent(%f) durationMillis(%d)",
                        this.aimPercent, this.tempDurationMillis));
            }
            clear();
            return;
        }

        sendEmptyMessageDelayed(0, calculateDelay(realPercentDelta, desiredPercentDelta));
    }

    private void clear() {
        resetTempDelay();
        this.ignoreCommit = false;
        removeMessages(0);
    }

    private boolean ignoreCommit = false;

    /**
     * Must be invoked by some method which will change the percent for monitor all changes
     * about percent.
     *
     * @param percent the percent will be effect by the target.
     */
    public void commitPercent(float percent) {
        if (this.ignoreCommit) {
            this.ignoreCommit = false;
            return;
        }
        this.aimPercent = percent;
    }

    private void setPercent2Target(final float percent) {
        if (targetWeakReference == null || targetWeakReference.get() == null) {
            return;
        }

        this.ignoreCommit = true;
        targetWeakReference.get().setPercent(percent);
        this.ignoreCommit = false;
    }

    public void loopSmooth(float percent) {
        loopSmooth(percent, -1);
    }

    /**
     * If the provider percent(the aim percent) more than {@link #minInternalPercent}, it will
     * be split to the several {@link #smoothInternalPercent}.
     *
     * @param percent        The aim percent.
     * @param durationMillis Temporary duration for {@code percent}. If lesson than 0, it will be
     *                       ignored.
     */
    public void loopSmooth(float percent, long durationMillis) {
        if (this.targetWeakReference == null || this.targetWeakReference.get() == null) {
            return;
        }

        if (NEED_LOG) {
            Log.d(TAG,
                    String.format("start loopSmooth lastAimPercent(%f), aimPercent(%f)" +
                            " durationMillis(%d)", aimPercent, percent, durationMillis));
        }

        final ISmoothTarget target = targetWeakReference.get();

        setPercent2Target(this.aimPercent);
        clear();

        this.aimPercent = percent;

        if (this.aimPercent - target.getPercent() > minInternalPercent) {
            if (durationMillis >= 0) {


                tempStartTimestamp = SystemClock.uptimeMillis();
                tempDurationMillis = durationMillis;
                tempRemainDurationMillis = durationMillis;
            }
            sendEmptyMessage(0);
        } else {
            setPercent2Target(percent);
        }
    }

    private void resetTempDelay() {
        tempLastConsumeMillis = smoothIncreaseDelayMillis;
        tempStartTimestamp = -1;
        tempDurationMillis = -1;
        tempRemainDurationMillis = -1;
        tempWarnedAccuracyProblem = false;
    }

    private float calculatePercent(final float currentPercent) {
        if (tempDurationMillis < 0) {
            return smoothInternalPercent;
        }

        float internalPercent;

        final long usedDuration = SystemClock.uptimeMillis() - tempStartTimestamp;
        final long lastRemainDurationMillis = tempRemainDurationMillis;

        tempRemainDurationMillis = tempDurationMillis - usedDuration;
        tempLastConsumeMillis = Math.max(lastRemainDurationMillis - tempRemainDurationMillis, 1);

        final long splitByDelay = Math.max(tempRemainDurationMillis / tempLastConsumeMillis, 1);
        final float percentDelta = this.aimPercent - currentPercent;

        internalPercent = percentDelta / splitByDelay;

        return internalPercent;
    }

    private long calculateDelay(final float realPercentDelta, final float desiredPercentDelta) {
        if (tempDurationMillis < 0) {
            return smoothIncreaseDelayMillis;
        }

        if (realPercentDelta - desiredPercentDelta <= ALLOWED_PRECISION_ERROR) {
            return smoothIncreaseDelayMillis;
        }

        //Accuracy Problem in target smooth progress.
        if (!tempWarnedAccuracyProblem) {
            tempWarnedAccuracyProblem = true;
            Log.w(TAG,
                    String.format("Occur Accuracy Problem in %s, (real percent delta is %f, but" +
                                    " desired percent delta is %f), so we use delay to handle the" +
                                    " temporary duration, as result the processing will not smooth",
                            targetWeakReference.get(), realPercentDelta, desiredPercentDelta));
        }

        long remedyDelayMillis;
        final float delta = realPercentDelta - desiredPercentDelta;
        remedyDelayMillis = (long) ((delta / desiredPercentDelta) * tempLastConsumeMillis);
        return remedyDelayMillis + smoothIncreaseDelayMillis;
    }

    private long tempStartTimestamp;
    private long tempDurationMillis;
    private long tempRemainDurationMillis;
    private long tempLastConsumeMillis;
    private boolean tempWarnedAccuracyProblem;
    public static float ALLOWED_PRECISION_ERROR = 0.00001f;
}