package wu.loushanyun.com.fivemoduleapp.init;

import android.app.Application;
import android.content.Context;
import androidx.multidex.MultiDex;

import com.wu.loushanyun.base.util.BugLyConfig;
import com.wu.loushanyun.basemvp.init.LouShanYunApplication;

import java.util.ArrayList;
import java.util.List;

import met.hx.com.base.base.application.AppContext;
import met.hx.com.base.baseinterface.ApplicationAsLibrary;
import met.hx.com.base.basemvp.init.BaseApplication;

public class MainApplication extends Application {

    private List<ApplicationAsLibrary> mChildApplicationList = new ArrayList<ApplicationAsLibrary>();

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        // you must install multiDex whatever tinker is installed!
        MultiDex.install(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AppContext.getInstance().init(this);
        BugLyConfig.initBugLy(this,"bed35a79f7");
        mChildApplicationList.add(new BaseApplication());
        mChildApplicationList.add(new LouShanYunApplication());
        for (ApplicationAsLibrary app : mChildApplicationList) {
            app.onCreateAsLibrary(this);
        }
    }


    @Override
    public void onLowMemory() {
        super.onLowMemory();
        for (ApplicationAsLibrary app : mChildApplicationList) {
            app.onLowMemoryAsLibrary(this);
        }
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        for (ApplicationAsLibrary app : mChildApplicationList) {
            app.onTrimMemoryAsLibrary(this, level);
        }
    }


}