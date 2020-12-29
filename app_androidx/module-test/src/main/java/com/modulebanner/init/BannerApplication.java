package met.hx.com.modulebanner.init;

import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;

import met.hx.com.base.base.application.BaseChildApplication;

/**
 * Created by huxu on 2017/11/9.
 */

public class BannerApplication extends BaseChildApplication{

    public static int H,W;
    @Override
    public void onCreateAsLibrary(Application application) {
        getScreen(application);
    }

    @Override
    public void onLowMemoryAsLibrary(Application application) {

    }

    @Override
    public void onTrimMemoryAsLibrary(Application application, int level) {

    }

    public void getScreen(Context aty) {
        DisplayMetrics dm = aty.getResources().getDisplayMetrics();
        H=dm.heightPixels;
        W=dm.widthPixels;
    }
}
