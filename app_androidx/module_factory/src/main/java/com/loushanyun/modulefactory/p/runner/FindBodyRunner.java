package com.loushanyun.modulefactory.p.runner;

import com.google.gson.Gson;
import com.loushanyun.modulefactory.m.FindBodyData;
import com.wu.loushanyun.base.url.URLUtils;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

import met.hx.com.base.base.nohttp.CommonRunner;
import met.hx.com.base.baseevent.Event;

public class FindBodyRunner extends CommonRunner {
    @Override
    public void onEventRun(Event event) {
        String chipNum= (String) event.getParamAtIndex(0);
        int moduleType= (int) event.getParamAtIndex(1);
        Request<String> request= NoHttp.createStringRequest(URLUtils.getIP()+ URLUtils.Findbody, RequestMethod.POST);
        request.add("chipNum", chipNum);
        request.add("moduleType", moduleType);
        Response<String> response= requestString(request);
        if(response.isSucceed()){
            String result=response.get();
            FindBodyData findBodyData=new Gson().fromJson(result,FindBodyData.class);
            event.addReturnParam(findBodyData);
            event.setSuccess(true);
    }
    }
}
