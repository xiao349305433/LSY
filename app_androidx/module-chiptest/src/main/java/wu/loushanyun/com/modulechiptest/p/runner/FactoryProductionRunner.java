package wu.loushanyun.com.modulechiptest.p.runner;

import com.elvishew.xlog.XLog;
import com.google.gson.Gson;
import com.wu.loushanyun.base.url.URLUtils;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

import met.hx.com.base.base.nohttp.CommonRunner;
import met.hx.com.base.baseevent.Event;
import wu.loushanyun.com.modulechiptest.m.ResultJson;

public class FactoryProductionRunner extends ChipCommonRunner {
    @Override
    public void onEventRun(Event event) {
        String moduleFactory= (String) event.getParamAtIndex(0);
        long mLoginPhoneNumberApp= (long) event.getParamAtIndex(2);
        Request<String> request= NoHttp.createStringRequest(URLUtils.getIP() + URLUtils.MChipModuleFactoryProduction, RequestMethod.POST);
        request.add("moduleFactory", moduleFactory);
        request.add("mLoginPhoneNumberApp", mLoginPhoneNumberApp);
        Response<String> response= requestString(request);
        if(response.isSucceed()){
            String result=response.get();
            Gson gson = new Gson();
            ResultJson resultJson = gson.fromJson(result, ResultJson.class);
            event.setSuccess(true);
            event.addReturnParam(resultJson);
        }
    }
}