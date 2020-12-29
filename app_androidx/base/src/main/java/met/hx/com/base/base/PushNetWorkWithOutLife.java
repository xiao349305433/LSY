package met.hx.com.base.base;

import android.util.SparseArray;

import java.io.IOException;

import io.reactivex.FlowableEmitter;
import met.hx.com.base.base.dialog.LoadingDialogUtil;
import met.hx.com.base.base.rx.FlowableUtil;
import met.hx.com.base.baseevent.AMOnEventRunner;
import met.hx.com.base.baseevent.Event;
import met.hx.com.base.baseinterface.FlowableListener;
import met.hx.com.librarybase.some_utils.LogUtils;
import met.hx.com.librarybase.some_utils.XActivityUtils;

/**
 * Created by huxu on 2017/11/13.
 * 没有生命周期的网络请求,常见在单例中请求
 */

public abstract class PushNetWorkWithOutLife implements FlowableListener {

    public PushNetWorkWithOutLife() {
        initEventRunner();
    }

    protected SparseArray<AMOnEventRunner> amOnEventRunners = new SparseArray<>();

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
                FlowableUtil.create(this, code, true, null, params);
            }
        }
    }

    public void pushEventNoProgress(int code, Object... params) {
        if (amOnEventRunners != null) {
            if (amOnEventRunners.get(code) != null) {
                FlowableUtil.create(this, code, false, null, params);
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
    }

    @Override
    public void onError(Throwable throwable, int code) {
        LogUtils.e(throwable);
    }

    @Override
    public void onComplete(int code) {
    }

    protected abstract void initEventRunner();

    protected abstract void onEventRunEnd(Event event, int code);


}
