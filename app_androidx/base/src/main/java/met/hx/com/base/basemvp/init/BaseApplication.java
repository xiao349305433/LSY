package met.hx.com.base.basemvp.init;


import android.app.Application;


import met.hx.com.base.base.application.BaseChildApplication;
import met.hx.com.base.baseconfig.CrashHandler;

/**
 * Created by huxu on 2017/4/14.
 */

public class BaseApplication extends BaseChildApplication {



    @Override
    public void onCreateAsLibrary(Application application) {
        CrashHandler.getInstance().init(application);
    }

    @Override
    public void onLowMemoryAsLibrary(Application application) {

    }

    @Override
    public void onTrimMemoryAsLibrary(Application application, int level) {

    }
}
