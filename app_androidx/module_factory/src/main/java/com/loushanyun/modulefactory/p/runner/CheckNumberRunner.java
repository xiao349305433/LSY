package com.loushanyun.modulefactory.p.runner;

import com.wu.loushanyun.base.url.URLUtils;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

import met.hx.com.base.base.nohttp.CommonRunner;
import met.hx.com.base.baseevent.Event;

public class CheckNumberRunner extends CommonRunner{
    @Override
    public void onEventRun(Event event) {
        String tel= (String) event.getParamAtIndex(0);
        Request<String> request= NoHttp.createStringRequest(URLUtils.getIP()+ URLUtils.FSCheckNumberURL, RequestMethod.POST);
        request.add("tel", tel);
        Response<String> response= requestString(request);
        if(response.isSucceed()){
            String result=response.get();
            event.addReturnParam(result);
            event.setSuccess(true);
        }
    }
}
