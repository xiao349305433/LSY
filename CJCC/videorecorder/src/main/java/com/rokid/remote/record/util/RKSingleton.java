package com.rokid.remote.record.util;

/**
 * @author : heshun
 * @version : v1.0.0
 * @date : 2020/4/16 11:05 PM
 * @description: This is Singleton
 */
public abstract class RKSingleton<T> {

    /**obj which single instance to create*/
    T obj;

    /**need implement this method to create instance of T*/
    public abstract T create();

    /**
     * @return single instance of T
     */
    public T get() {
        if (null == obj) {
            synchronized (this) {
                if (null == obj) {
                    obj = create();
                }
            }
        }
        return obj;
    }
}
