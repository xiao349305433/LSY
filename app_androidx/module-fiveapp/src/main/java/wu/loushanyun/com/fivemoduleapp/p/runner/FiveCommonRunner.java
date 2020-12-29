package wu.loushanyun.com.fivemoduleapp.p.runner;

import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.wu.loushanyun.base.config.K;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

import met.hx.com.base.base.nohttp.NoHttpRunner;
import met.hx.com.librarybase.some_utils.ToastUtils;
import wu.loushanyun.com.fivemoduleapp.m.ResultJson;
import wu.loushanyun.com.libraryfive.init.LoginFiveParamManager;

public abstract class FiveCommonRunner extends NoHttpRunner {
    @Override
    protected <T> void addCommonParams(int type, Request<T> request) {

        switch (type) {
            case TYPE_JAVA_RUNNER:
                //加入公共参数的逻辑
                if(LoginFiveParamManager.getInstance().getLoginData()!=null){
                    request.addHeader("AndroidToken", LoginFiveParamManager.getInstance().getLoginData().getToken());
                }

                break;
            default:
        }
    }



    @Override
    protected <T> void afterSuccessResponse(Response<T> response, Request<T> requestChild, int type, String className) {
        super.afterSuccessResponse(response, requestChild, type, className);
        switch (type) {
            case TYPE_JAVA_RUNNER:
                ResultJson resultJson=new Gson().fromJson(response.get().toString(),ResultJson.class);
                if( resultJson.getCode()==999){
                    ToastUtils.showShort(resultJson.getMsg());
                    ARouter.getInstance().build(K.LoginActivityFive).navigation();
                }
                //跳转到登陆的逻辑
                break;
            default:
        }
    }
}
