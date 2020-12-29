package met.hx.com.base.base.application;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;
import com.yanzhenjie.nohttp.Logger;


import org.litepal.LitePal;

import met.hx.com.base.BuildConfig;
import met.hx.com.base.base.nohttp.NoHttpConfig;
import met.hx.com.base.base.refresh.SmartRefresh;
import met.hx.com.base.baseconfig.TextStyleConfig;
import met.hx.com.base.baseconfig.XlogConfig;
import met.hx.com.librarybase.some_utils.LogUtils;
import met.hx.com.librarybase.some_utils.Utils;


/**
 * Created by Nirui on 17/2/21.
 *
 * @author huxu
 */

public class AppContext {
    private Application mAppContext;
    private volatile static AppContext instance;
    public static AppContext getInstance() {
        if (instance == null) {
            synchronized (AppContext.class) {
                if (instance == null) {
                    instance = new AppContext();
                }
            }
        }
        return instance;
    }

    public void init(Application application) {
        if (mAppContext == null) {
            mAppContext = application;
            initApplication(application);
        } else {
            throw new IllegalStateException("set context duplicate");
        }
    }

    public Application getApplication() {
        if (mAppContext == null) {
            throw new IllegalStateException("forget init?");
        } else {
            return mAppContext;
        }
    }

    /**
     * 整个项目需要公用的初始化的东西
     */
    public void initApplication(Application application) {
        Utils.init(application);
        if (BuildConfig.DEBUG) {
            Logger.setDebug(true);// 开启NoHttp的调试模式, 配置后可看到请求过程、日志和错误信息。
            Logger.setTag("NoHttp:");// 设置NoHttp打印Log的tag。
            LogUtils.flag=true;
            ARouter.openLog();     // 打印日志
            ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        } else {
            Logger.setDebug(false);
            LogUtils.flag=false;
        }
        XlogConfig.initXlog();
        ARouter.init(application);
        TextStyleConfig.initTextStyle(application);
        NoHttpConfig.initNoHttp(application);
        SmartRefresh.initRefresh();
        LitePal.initialize(application);
    }

}
