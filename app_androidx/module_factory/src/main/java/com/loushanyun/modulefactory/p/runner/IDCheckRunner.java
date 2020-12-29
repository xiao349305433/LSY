package com.loushanyun.modulefactory.p.runner;

import com.google.gson.Gson;
import com.loushanyun.modulefactory.m.ThirdFactoryReturn;
import com.wu.loushanyun.base.url.URLUtils;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

import met.hx.com.base.base.nohttp.CommonRunner;
import met.hx.com.base.baseevent.Event;

public class IDCheckRunner extends CommonRunner {
    @Override
    public void onEventRun(Event event) {
        String meterId = String.valueOf(event.getParamAtIndex(0));
        String manufacturersIdentification = String.valueOf(event.getParamAtIndex(1));
        Request<String> request = NoHttp.createStringRequest(URLUtils.getIP() + URLUtils.createMeterId, RequestMethod.GET);
        request.add("meterId", meterId);
        request.add("manufacturersIdentification", manufacturersIdentification);
        Response<String> response = requestString(request);
        if (response.isSucceed()) {
            String result = response.get();
            ThirdFactoryReturn thirdFactoryReturn=new Gson().fromJson(result,ThirdFactoryReturn.class);
            event.addReturnParam(thirdFactoryReturn);
            event.setSuccess(true);
        }
    }
}
