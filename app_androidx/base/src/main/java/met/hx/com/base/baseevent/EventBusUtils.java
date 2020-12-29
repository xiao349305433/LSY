package met.hx.com.base.baseevent;


import org.greenrobot.eventbus.EventBus;

import met.hx.com.librarybase.some_utils.LogUtils;

/**
 * @author huxu 使用的时候注意订阅与取消，发送的事件，只要订阅者订阅了，都会产生响应
 */  
public class EventBusUtils {  
  
    private static final String TAG = "EventBusUtils";  
    private EventBusUtils() {  
    }  
  
    /** 
     * 注册EventBus 
     * 
     * @param subscriber 订阅者对象 
     */  
    public static void register(Object subscriber) {
        if (!EventBus.getDefault().isRegistered(subscriber)) {
            LogUtils.e(TAG, subscriber.getClass().getName()+"register: 注册成功");
            EventBus.getDefault().register(subscriber);  
        } else {  
            LogUtils.e(TAG, subscriber.getClass().getName()+"register: 注册失败");
        }  
    }  
  
    /** 
     * 取消注册EventBus 
     * 
     * @param subscriber 订阅者对象 
     */  
    public static void unregister(Object subscriber) {
        EventBus.getDefault().unregister(subscriber);  
    }  
  
    /**
     * 发布订阅事件
     *
     * @param event 事件对象(示例)
     */
    private  void post(Object event) {
        EventBus.getDefault().post(event);
    }

    /**
     * 发布粘性订阅事件
     *
     * @param event 事件对象(示例)
     */
    private  void postSticky(Object event) {
        EventBus.getDefault().postSticky(event);
    }

    /** 
     * 移除指定的粘性订阅事件 
     * 
     * @param eventType class的字节码，例如：String.class 
     */  
    public static <T> void removeStickyEvent(Class<T> eventType) {  
        T stickyEvent = EventBus.getDefault().getStickyEvent(eventType);  
        if (stickyEvent != null) {  
            EventBus.getDefault().removeStickyEvent((T) stickyEvent);  
        }  
    }  
  
    /** 
     * 移除所有的粘性订阅事件 
     */  
    public static void removeAllStickyEvents() {  
        EventBus.getDefault().removeAllStickyEvents();  
    }  
  
    /** 
     * 取消事件传送 
     * 
     * @param event 事件对象 
     */  
    public static void cancelEventDelivery(Object event) {  
        EventBus.getDefault().cancelEventDelivery(event);  
    }  
}  