package met.hx.com.base.base.rx;

import java.util.concurrent.TimeUnit;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import met.hx.com.base.baseevent.Event;
import met.hx.com.base.baseinterface.FlowableListener;
import met.hx.com.base.baseinterface.RxTimerListener;

/**
 * Created by huxu on 2017/10/30.
 *
 * @author huxu
 */

public class FlowableUtil {
    public static Disposable create(FlowableListener followableListener, int code, boolean showDialog, String showLoadingText, Object[] params) {
        return Flowable.create(
                (FlowableOnSubscribe<Event>) emitter ->
                        followableListener.FlowableOnSubscribe(emitter, code, showDialog, showLoadingText,params)
                , BackpressureStrategy.ERROR)
                .compose(RxSchedulers.io_main())
                .subscribe(object -> followableListener.onNext(object, code),
                        throwable -> followableListener.onError(throwable, code),
                        () -> followableListener.onComplete(code));
    }

    /**
     * 只发送不接收
     */
    public static Disposable createAlong(FlowableListener followableListener, int code, Object[] params) {
        return Flowable.create(
                (FlowableOnSubscribe<Event>) emitter ->
                        followableListener.FlowableOnSubscribe(emitter, code, false, null,params)
                , BackpressureStrategy.ERROR)
                .subscribeOn(Schedulers.newThread()).subscribe();
    }

    /**
     * milliseconds毫秒后执行next操作
     *
     * @param milliseconds
     * @param rxTimerListener
     */
    public static Disposable createRxTimer(long milliseconds, RxTimerListener rxTimerListener) {
        return Flowable.timer(milliseconds, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    rxTimerListener.onNext(aLong);
                }, throwable -> {
                    rxTimerListener.onError(throwable);
                }, () -> {
                    rxTimerListener.onComplete();
                });

    }

    /**
     * 每隔milliseconds毫秒后执行next操作
     *
     * @param milliseconds
     * @param rxTimerListener
     */
    public static Disposable intervalRxTimer(long milliseconds, RxTimerListener rxTimerListener) {
        return Flowable.interval(milliseconds, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    rxTimerListener.onNext(aLong);
                }, throwable -> {
                    rxTimerListener.onError(throwable);
                }, () -> {
                    rxTimerListener.onComplete();
                });
    }

}
