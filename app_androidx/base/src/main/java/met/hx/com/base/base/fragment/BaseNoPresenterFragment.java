package met.hx.com.base.base.fragment;

import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;

import androidx.annotation.Nullable;

import io.reactivex.FlowableEmitter;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.dialog.LoadingDialogUtil;
import met.hx.com.base.base.rx.FlowableUtil;
import met.hx.com.base.baseevent.AMOnEventRunner;
import met.hx.com.base.baseevent.Event;
import met.hx.com.base.baseinterface.FlowableListener;
import met.hx.com.librarybase.some_utils.LogUtils;
import met.hx.com.librarybase.some_utils.XActivityUtils;

/**
 * @param <> mvc结构的基类
 * @author huxu
 */
public abstract class BaseNoPresenterFragment extends BaseFragment implements FlowableListener {

    protected CompositeDisposable compositeDisposable = new CompositeDisposable();
    protected SparseArray<AMOnEventRunner> amOnEventRunners = new SparseArray<>();


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        initEventRunner();
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * 初始化操作，这里放一些注册接口的内容
     */
    protected abstract void initEventRunner();

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


    /**
     * @param emitter 参考 FlowableUtil类封装的rxJava2代码，这里相当于上游，做工作线程的事
     */
    @Override
    public void FlowableOnSubscribe(FlowableEmitter<Event> emitter, int code, boolean showDialog, String showLoadingText, Object[] params) {
        LogUtils.i("FlowableOnSubscribe" + emitter.requested());
        Event event = new Event(code, params);
        try {
            if (showDialog) {
                if (XActivityUtils.getTopActivity() != null) {
                    LoadingDialogUtil.showByEvent(false, 3000, showLoadingText, XActivityUtils.getTopActivity().getClass().getName());
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


    /**
     * @param event 参考 FlowableUtil类封装的rxJava2代码，这里相当于下游，做主线程的事，
     *              上游通过emitter.onNext()给下游发送消息，这里相当于主线程，可以更新UI
     */
    @Override
    public void onNext(Event event, int code) {
        onEventRunEnd(event, code);
        LogUtils.i("onNext" + this.getClass().getName());
    }


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


    public abstract void onInitAttribute(BaseAttribute ba);

    public abstract void initView(View view, Bundle bundle);

    protected abstract void onEventRunEnd(Event event, int code);

    @Override
    public void onDetach() {
        super.onDetach();
        if (amOnEventRunners != null) {
            amOnEventRunners.clear();
        }
        compositeDisposable.dispose();
    }
}
