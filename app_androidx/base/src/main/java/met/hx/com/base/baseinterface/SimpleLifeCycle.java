package met.hx.com.base.baseinterface;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.CallSuper;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by huxu on 2017/12/15.
 * @author huxu
 */

public class SimpleLifeCycle implements LifeCycleListener{
    protected Reference<Activity> mActivityRef;
    protected CompositeDisposable compositeDisposable = new CompositeDisposable();
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
    public void detachActivity() {
        if (isActivityAttached()) {
            mActivityRef.clear();
            mActivityRef = null;
        }
    }
    @CallSuper
    @Override
    public void onCreate(Bundle savedInstanceState, Activity activity) {
        mActivityRef = new WeakReference<Activity>(activity);
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
    @CallSuper
    @Override
    public void onDestroy() {
        compositeDisposable.dispose();
        detachActivity();
    }
}
