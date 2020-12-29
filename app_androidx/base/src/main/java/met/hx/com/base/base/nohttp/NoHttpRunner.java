package met.hx.com.base.base.nohttp;

import androidx.annotation.CallSuper;
import androidx.annotation.Nullable;

import com.elvishew.xlog.XLog;
import com.yanzhenjie.nohttp.error.NetworkError;
import com.yanzhenjie.nohttp.error.NotFoundCacheError;
import com.yanzhenjie.nohttp.error.TimeoutError;
import com.yanzhenjie.nohttp.error.URLError;
import com.yanzhenjie.nohttp.error.UnKnownHostError;
import com.yanzhenjie.nohttp.rest.CacheMode;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.nohttp.rest.SyncRequestExecutor;
import com.yanzhenjie.nohttp.tools.MultiValueMap;

import org.json.JSONObject;

import java.net.ConnectException;
import java.net.Proxy;

import met.hx.com.base.BuildConfig;
import met.hx.com.base.base.application.AppContext;
import met.hx.com.base.baseevent.AMOnEventRunner;
import met.hx.com.base.baseevent.Event;
import met.hx.com.librarybase.some_utils.NetworkUtils;
import met.hx.com.librarybase.some_utils.ToastUtils;

import static com.yanzhenjie.nohttp.BasicRequest.buildCommonParams;

/**
 * Created by huxu on 2017/6/26.
 */

public abstract class NoHttpRunner implements AMOnEventRunner {


    @Override
    public Event runEvent(Event event) {
        if(NetworkUtils.isAvailable(AppContext.getInstance().getApplication())){
            onEventRun(event);
            event.setRunSuccess(true);
        }else {
            ToastUtils.showShort("当前网络不可用");
        }
        return event;
    }


    public abstract void onEventRun(Event event);

    /**
     * 请求的request。
     */
    public static final int TYPE_JAVA_RUNNER = 1000;//JAVA服务器
    private int commonType = TYPE_JAVA_RUNNER;


    public Response<String> requestString(Request<String> requestChild) {
        Response<String> response = requestString(requestChild, commonType);
        return response;
    }

    public Response<String> requestString(Request<String> requestChild, int type) {
        Response<String> response = getResponse(requestChild, type);
        return response;
    }


    public Response<JSONObject> requestJsonObject(Request<JSONObject> requestChild) {
        Response<JSONObject> response = requestJsonObject(requestChild, commonType);
        return response;
    }

    public Response<JSONObject> requestJsonObject(Request<JSONObject> requestChild, int type) {
        Response<JSONObject> response = getResponse(requestChild, type);
        return response;
    }


    @Nullable
    private <T> Response<T> getResponse(Request<T> request, int type) {
        if (request.getCacheMode() == null) {
            request.setCacheMode(CacheMode.ONLY_REQUEST_NETWORK);
        }
        if (!BuildConfig.DEBUG) {
            request.setProxy(Proxy.NO_PROXY);
        }
        addCommonParams(type,request);
        Response<T> response = null;
        switch (type) {
            case TYPE_JAVA_RUNNER:
                response = SyncRequestExecutor.INSTANCE.execute(request);
                break;
            default:
        }
        if (!request.isCanceled()) {
            request.cancel();
        }
        if (response != null) {
            sentJsonReport(type, response);
            if (!response.isSucceed()) {
                Exception exception = response.getException();
                XLog.i(exception.toString());
                if (exception instanceof NetworkError) {// 网络不好
                    ToastUtils.showShort("当前网络不可用");
                } else if (exception instanceof TimeoutError) {// 请求超时
                    ToastUtils.showShort("请求超时");
                } else if (exception instanceof UnKnownHostError) {// 找不到服务器
                    ToastUtils.showShort("找不到服务器");
                } else if (exception instanceof URLError) {// URL是错的
                    ToastUtils.showShort("请求地址错误");
                } else if (exception instanceof ConnectException) {// 无法连接服务器
                    ToastUtils.showShort("无法连接服务器");
                } else if (exception instanceof NotFoundCacheError) {// 这个异常只会在仅仅查找缓存时没有找到缓存时返回
                } else {//未知错误
                    ToastUtils.showShort("未知错误");
                }
            } else {
                //请求成功之后，解析json查看服务器是否返回错误信息
                afterSuccessResponse(response, request, type, this.getClass().getName());
            }
        }
        return response;
    }


    @CallSuper
    protected <T> void afterSuccessResponse(Response<T> response, Request<T> requestChild, int type, String className) {
        MultiValueMap multiValueMap = requestChild.getParamKeyValues();
        StringBuilder stringBuilder = buildCommonParams(multiValueMap, requestChild.getParamsEncoding());
        XLog.i("请求成功URL{" + className + "}:" + requestChild.url());
        XLog.i("请求参数字符串等于:" + stringBuilder.toString());
        XLog.i("请求成功的数据:" + response.get().toString());
    }

    //添加公共参数
    protected <T> Response<T> addCommonParams(int type, Response<T> response, Request<T> request) {
        return response;
    }

    //添加公共参数
    protected <T> void addCommonParams(int type, Request<T> request) {
    }


    //发送请求完的报告
    protected <T> void sentJsonReport(int type, Response<T> response) {
    }


}
