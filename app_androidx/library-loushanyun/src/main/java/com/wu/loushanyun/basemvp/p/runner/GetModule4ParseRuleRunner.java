package com.wu.loushanyun.basemvp.p.runner;

import com.google.gson.Gson;
import com.wu.loushanyun.base.url.URLUtils;
import com.wu.loushanyun.basemvp.m.ModuleRule;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

import met.hx.com.base.base.nohttp.CommonRunner;
import met.hx.com.base.baseevent.Event;

public class GetModule4ParseRuleRunner extends CommonRunner {
    @Override
    public void onEventRun(Event event) {
        String manufacturersIdentification= (String) event.getParamAtIndex(0);
        Request<String> request= NoHttp.createStringRequest(URLUtils.getIP()+ URLUtils.MChipGetModule4ParseRule, RequestMethod.GET);
        request.add("manufacturersIdentification", manufacturersIdentification);
        request.add("pageNum", 1);
        request.add("pageSize", 20);
        Response<String> response= requestString(request);
        if(response.isSucceed()){
            String result=response.get();
            ModuleRule moduleRule=new Gson().fromJson(result,ModuleRule.class);
            event.setSuccess(true);
            event.addReturnParam(moduleRule);
        }
    }
}
