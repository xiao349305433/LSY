package met.hx.com.base.baseinterface;

import androidx.annotation.NonNull;
/**
 * 定时器监听
 */
public interface RxTimerListener {

    /**
     * @param number 参考 FlowableUtil类封装的rxJava2代码，这里相当于下游，做主线程的事，
     *              上游通过emitter.onNext()给下游发送消息，这里相当于主线程，可以更新UI
     */
    void onNext(@NonNull Long number);
    /**
     * @param throwable 参考 FlowableUtil类封装的rxJava2代码，这里相当于下游，获取异常
     *                  上游通过emitter.onError()给下游发送消息，
     */
    void onError(Throwable throwable);
    /**
     * 参考 FlowableUtil类封装的rxJava2代码，这里相当于下游，上游结束通知
     * 上游通过emitter.onComplete()给下游发送消息，
     */
    void onComplete();
}