package met.hx.com.base.base;

import android.app.Activity;
import android.os.Bundle;
import android.util.SparseArray;

import androidx.annotation.CallSuper;

import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

import io.reactivex.FlowableEmitter;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import met.hx.com.base.base.dialog.LoadingDialogUtil;
import met.hx.com.base.base.rx.FlowableUtil;
import met.hx.com.base.baseevent.AMOnEventRunner;
import met.hx.com.base.baseevent.Event;
import met.hx.com.base.baseinterface.FlowableListener;
import met.hx.com.base.baseinterface.LifeCycleListener;
import met.hx.com.librarybase.some_utils.LogUtils;
import met.hx.com.librarybase.some_utils.XActivityUtils;

/**
 * Created by huxu on 2017/11/13.
 * 网络请求，使用时注册生命周期回调
 */

public abstract class PushNetWork implements FlowableListener, LifeCycleListener {

    protected SparseArray<AMOnEventRunner> amOnEventRunners = new SparseArray<>();
    protected CompositeDisposable compositeDisposable = new CompositeDisposable();
    protected Reference<Activity> mActivityRef;

    /**
     * @param code   标识
     * @param runner runner
     */
    public void registerEventRunner(int code, AMOnEventRunner runner) {
        if (amOnEventRunners != null) {
            amOnEventRunners.put(code, runner);
        }
    }

    /**
     * @param code   标识
     * @param params 请求数据
     */
    public void pushEvent(int code, Object... params) {
        if (amOnEventRunners != null) {
            if (amOnEventRunners.get(code) != null) {
                Disposable disposable = FlowableUtil.create(this, code, true, null, params);
                compositeDisposable.add(disposable);
            }
        }
    }

    public void pushEventNoProgress(int code, Object... params) {
        if (amOnEventRunners != null) {
            if (amOnEventRunners.get(code) != null) {
                Disposable disposable = FlowableUtil.create(this, code, false, null, params);
                compositeDisposable.add(disposable);
            }
        }
    }

    public void pushEventAlong(int code, Object... params) {
        if (amOnEventRunners != null) {
            if (amOnEventRunners.get(code) != null) {
                FlowableUtil.createAlong(this, code, params);
            }
        }
    }


    @Override
    public void FlowableOnSubscribe(FlowableEmitter<Event> emitter, int code, boolean showDialog, String showLoadingText, Object[] params) throws IOException {
        LogUtils.i("c" + emitter.requested());
        Event event = new Event(code, params);
        try {
            if (showDialog) {
                if (XActivityUtils.getTopActivity() != null) {
                    LoadingDialogUtil.showByEvent(showLoadingText, XActivityUtils.getTopActivity().getClass().getName());
                }
            }
            if (amOnEventRunners != null) {
                if (amOnEventRunners.get(code) != null) {
                    amOnEventRunners.get(code).runEvent(event);
                } else {
                    LogUtils.i("没有注册runner");
                }
            }
            if (showDialog) {
                if (XActivityUtils.getTopActivity() != null) {
                    LoadingDialogUtil.dismissByEvent(XActivityUtils.getTopActivity().getClass().getName());
                }
            }

            if (event.isRunSuccess()) {
                emitter.onNext(event);
                emitter.onComplete();
            }
        } catch (Exception e) {
            if (showDialog) {
                if (XActivityUtils.getTopActivity() != null) {
                    LoadingDialogUtil.dismissByEvent(XActivityUtils.getTopActivity().getClass().getName());
                }
            }
            if (emitter != null) {
                if (e != null) {
                    emitter.onError(e);
                }
            }
        }
    }

    @Override
    public void onNext(Event event, int code) {
        onEventRunEnd(event, code);
        LogUtils.i("onNext" + this.getClass().getName());
    }

    @Override
    public void onError(Throwable throwable, int code) {
        LogUtils.e(throwable);
    }

    @Override
    public void onComplete(int code) {
    }


    /**
     * @return 是否可用
     */
    public boolean isActivityAttached() {
        return mActivityRef != null && mActivityRef.get() != null;
    }

    /**
     * @return 获取
     */
    public Activity getActivity() {
        if (mActivityRef == null) {
            return null;
        }
        return mActivityRef.get();
    }

    /**
     * 销毁
     */
    private void detachActivity() {
        if (isActivityAttached()) {
            mActivityRef.clear();
            mActivityRef = null;
        }
    }

    @CallSuper
    @Override
    public void onCreate(Bundle savedInstanceState, Activity activity) {
        mActivityRef = new WeakReference<Activity>(activity);
        initEventRunner();
        LogUtils.i("onCreate" + this.getClass().getName());
    }

    /**
     * 动态初始化activity
     *
     * @param activity
     */
    protected void initActivityRef(Activity activity) {
        if (!isActivityAttached()) {
            LogUtils.i("注册了线程");
            this.mActivityRef = new WeakReference<Activity>(activity);
            initEventRunner();
        }
    }

    protected abstract void initEventRunner();

    protected abstract void onEventRunEnd(Event event, int code);

    @Override
    public void onStart() {

    }

    @Override
    public void onRestart() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

    }

    @CallSuper
    @Override
    public void onDestroy() {
        if (amOnEventRunners != null) {
            amOnEventRunners.clear();
        }
        compositeDisposable.dispose();
        detachActivity();
    }


}
