package met.hx.com.base.base.application;

import android.app.Application;

import met.hx.com.base.baseinterface.ApplicationAsLibrary;
import met.hx.com.base.basemvp.init.BaseApplication;

/**
 * Created by Nirui on 17/2/27.
 */

public abstract class BaseChildApplication extends Application implements ApplicationAsLibrary {

  @Override public void onCreate() {
    super.onCreate();
    //作为单独运行时初始化的东西

    AppContext.getInstance().init(this);
    new BaseApplication().onCreateAsLibrary(this);
    onCreateAsLibrary(this);
  }

  @Override public void onLowMemory() {
    super.onLowMemory();
    onLowMemoryAsLibrary(this);
  }

  @Override public void onTrimMemory(int level) {
    super.onTrimMemory(level);
    onTrimMemoryAsLibrary(this, level);
  }

  @Override public abstract void onCreateAsLibrary(Application application) ;

  @Override public abstract void onLowMemoryAsLibrary(Application application);

  @Override public abstract void onTrimMemoryAsLibrary(Application application, int level) ;

}
