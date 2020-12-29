package met.hx.com.base.base;


import android.app.Activity;
import android.os.Bundle;
import android.util.SparseArray;

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
 * Created by huxu on 16/4/22.
 *
 * @author huxu
 */
public abstract class BasePresenter<V> implements FlowableListener, LifeCycleListener {

    protected Reference<Activity> mActivityRef;
    protected V mView;
    protected CompositeDisposable compositeDisposable = new CompositeDisposable();
    protected SparseArray<AMOnEventRunner> amOnEventRunners = new SparseArray<>();


    /**
     * 初始化操作，这里放一些注册接口的内容
     */
    protected abstract void initPresenter();

    /**
     * @param code   标识
     * @param runner runner
     */
    protected void registerEventRunner(int code, AMOnEventRunner runner) {
        if(amOnEventRunners!=null){
            amOnEventRunners.put(code, runner);
        }
    }

    /**
     * @param code   标识
     * @param params 请求数据
     */
    public void pushEvent(int code, Object... params) {
        if(amOnEventRunners!=null){
            if (amOnEventRunners.get(code) != null) {
                Disposable disposable = FlowableUtil.create(this, code, true, null, params);
                compositeDisposable.add(disposable);
            }
        }
    }

    public void pushEventNoProgress(int code, Object... params) {
        if(amOnEventRunners!=null){
            if (amOnEventRunners.get(code) != null) {
                Disposable disposable = FlowableUtil.create(this, code, false, null, params);
                compositeDisposable.add(disposable);
            }
        }
    }

    public void pushEventAlong(int code, Object... params) {
        if(amOnEventRunners!=null){
            if (amOnEventRunners.get(code) != null) {
                FlowableUtil.createAlong(this, code, params);
            }
        }

    }


    /**
     * @param v 初始化view
     */
    public void setView(V v) {
        initView(v);
    }

    protected void initView(V v) {
        this.mView = v;
        this.onAttached();
    }

    /**
     * 初始化view后的操作
     */
    protected abstract void onAttached();


    /**
     * @param emitter 参考 FlowableUtil类封装的rxJava2代码，这里相当于上游，做工作线程的事
     */
    @Override
    public void FlowableOnSubscribe(FlowableEmitter<Event> emitter, int code, boolean showDialog, String showLoadingText, Object[] params) {
        Event event = new Event(code, params);
        try {
            if (showDialog) {
                if (XActivityUtils.getTopActivity() != null) {
                    LoadingDialogUtil.showByEvent(showLoadingText, XActivityUtils.getTopActivity().getClass().getName());
                }
            }
            if(amOnEventRunners!=null){
                if (amOnEventRunners.get(code) != null) {
                    amOnEventRunners.get(code).runEvent(event);
                }else {
                    LogUtils.i("没有注册runner");
                }
            }
            if(showDialog){
                if (XActivityUtils.getTopActivity() != null) {
                    LoadingDialogUtil.dismissByEvent(XActivityUtils.getTopActivity().getClass().getName());
                }
            }
            if (event.isRunSuccess()) {
                emitter.onNext(event);
                emitter.onComplete();
            }
        } catch (Exception e) {
            if(showDialog){
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


    /**
     * @param event 参考 FlowableUtil类封装的rxJava2代码，这里相当于下游，做主线程的事，
     *              上游通过emitter.onNext()给下游发送消息，这里相当于主线程，可以更新UI
     */
    @Override
    public void onNext(Event event, int code) {
        onEventRunEnd(event, code);
    }

    protected abstract void onEventRunEnd(Event event, int code);

    /**
     * @param throwable 参考 FlowableUtil类封装的rxJava2代码，这里相当于下游，获取异常
     *                  上游通过emitter.onError()给下游发送消息，
     */
    @Override
    public void onError(Throwable throwable, int code) {
        LogUtils.e(throwable);
    }

    /**
     * 参考 FlowableUtil类封装的rxJava2代码，这里相当于下游，上游结束通知
     * 上游通过emitter.onComplete()给下游发送消息，
     */
    @Override
    public void onComplete(int code) {
        LogUtils.i("onComplete" + this.getClass().getName());
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


    @Override
    public void onCreate(Bundle savedInstanceState, Activity activity) {
        mActivityRef = new WeakReference<Activity>(activity);
        initPresenter();
    }

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

    @Override
    public void onDestroy() {
        if(amOnEventRunners!=null){
            amOnEventRunners.clear();
        }
        compositeDisposable.dispose();
        detachActivity();
    }
}
