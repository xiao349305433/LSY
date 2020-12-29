package met.hx.com.base.base.nohttp;

import android.content.Context;

import com.yanzhenjie.nohttp.InitializationConfig;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.cache.DBCacheStore;
import com.yanzhenjie.nohttp.cookie.DBCookieStore;

/**
 * Created by huxu on 2017/6/27.网络请求框架
 */

public class NoHttpConfig {
    public static final int TIME_OUT=20*1000;
    public static final int READ_TIME_OUT=20*1000;
    public static void initNoHttp(Context context) {
        InitializationConfig initializationConfig=InitializationConfig.newBuilder(context)
                .connectionTimeout(TIME_OUT)
                .readTimeout(READ_TIME_OUT)
                .cacheStore(
                        // 保存到数据库
                        new DBCacheStore(context).setEnable(true) // 如果不使用缓存，设置false禁用。
                        //new DiskCacheStore(context) // 保存在context.getCahceDir()文件夹中。
                )
                .cookieStore(new DBCookieStore(context).setEnable(true)) // 如果不维护cookie，设置false禁用。)
//                .networkExecutor(new URLConnectionNetworkExecutor())
                .networkExecutor(new OkHttpNetworkExecutor())//需要依赖com.squareup.okhttp3:okhttp-urlconnection:3.8.1
                // 全局通用Header，add是添加，多次调用add不会覆盖上次add。
//                .addHeader()
                // 全局通用Param，add是添加，多次调用add不会覆盖上次add。
//                .addParam()
//                .sslSocketFactory() // 全局SSLSocketFactory。
//                .hostnameVerifier() // 全局HostnameVerifier。
                .retry(2) // 全局重试次数，配置后每个请求失败都会重试x次。
                .build();
        NoHttp.initialize(initializationConfig);
    }



}
