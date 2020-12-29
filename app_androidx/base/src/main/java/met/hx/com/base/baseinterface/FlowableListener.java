package met.hx.com.base.baseinterface;

import java.io.IOException;

import io.reactivex.FlowableEmitter;
import met.hx.com.base.baseevent.Event;

/**
 * Created by huxu on 2017/10/30.
 * @author huxu
 */

public interface FlowableListener {
     /**
      * @param emitter 参考 FlowableUtil类封装的rxJava2代码，这里相当于上游，做工作线程的事
      */
     void FlowableOnSubscribe(FlowableEmitter<Event> emitter, int code,boolean showDialog,String showLoadingText, Object[] params) throws IOException;
     /**
      * @param event 参考 FlowableUtil类封装的rxJava2代码，这里相当于下游，做主线程的事，
      *              上游通过emitter.onNext()给下游发送消息，这里相当于主线程，可以更新UI
      */
     void onNext(Event event, int code);
     /**
      * @param throwable 参考 FlowableUtil类封装的rxJava2代码，这里相当于下游，获取异常
      *                  上游通过emitter.onError()给下游发送消息，
      */
     void onError(Throwable throwable,int code);
     /**
      * 参考 FlowableUtil类封装的rxJava2代码，这里相当于下游，上游结束通知
      * 上游通过emitter.onComplete()给下游发送消息，
      */
     void onComplete(int code);
}
