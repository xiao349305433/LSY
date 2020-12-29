package com.loushanyun.modulefactory.p.runner;

import com.google.gson.Gson;
import com.loushanyun.modulefactory.m.ErrorCode;
import com.wu.loushanyun.base.url.URLUtils;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;


import met.hx.com.base.base.nohttp.CommonRunner;
import met.hx.com.base.baseevent.Event;

public class LSYModule03UploadRunner extends CommonRunner{
    @Override
    public void onEventRun(Event event) {
        String pama= String.valueOf(event.getParamAtIndex(0));
        Request<String> request= NoHttp.createStringRequest( URLUtils.getIP()+ URLUtils.FSSaveThirdModel, RequestMethod.POST);
        request.add("modelInfo", pama);
        Response<String> response= requestString(request);
        if(response.isSucceed()){
            String result=response.get();
            Gson gson = new Gson();
            ErrorCode aLoginAnalysis = gson.fromJson(result, ErrorCode.class);
            event.addReturnParam(aLoginAnalysis.getCode());
            event.addReturnParam(aLoginAnalysis.getMsg());
        }
    }
}
