package com.wu.loushanyun.basemvp.p.runner;

import com.google.gson.Gson;
import com.wu.loushanyun.base.url.URLUtils;
import com.wu.loushanyun.basemvp.m.CreateUserIdInfo;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

import met.hx.com.base.base.nohttp.CommonRunner;
import met.hx.com.base.baseevent.Event;


public class CreateUserIdRunner  extends CommonRunner {

    @Override
    public void onEventRun(Event event) {

        Request<String> request= NoHttp.createStringRequest(URLUtils.getIP() + URLUtils.CreateUserId, RequestMethod.POST);
        Response<String> response= requestString(request);
        if(response.isSucceed()){
            String result=response.get();
            Gson gson = new Gson();
            CreateUserIdInfo resultJson = gson.fromJson(result, CreateUserIdInfo.class);
            event.setSuccess(true);
            event.addReturnParam(resultJson);
        }
    }
}
