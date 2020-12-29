package com.wu.loushanyun.basemvp.p.runner;

import com.google.gson.Gson;
import com.wu.loushanyun.base.url.URLUtils;
import com.wu.loushanyun.basemvp.m.ResultJson;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

import met.hx.com.base.base.nohttp.CommonRunner;
import met.hx.com.base.baseevent.Event;

public class SelectOrdernumberRunner extends CommonRunner {
    @Override
    public void onEventRun(Event event) {
        Request<String> request = NoHttp.createStringRequest(URLUtils.getIP() + URLUtils.FSelectOrdernumber, RequestMethod.POST);
        String tablenumber = (String) event.getParamAtIndex(0);
        String exitlogo = (String) event.getParamAtIndex(1);
        byte manufacturersIdentification = (byte) event.getParamAtIndex(2);
        request.add("tablenumber", tablenumber);
        request.add("exitlogo", exitlogo);
        request.add("manufacturersIdentification", String.valueOf(manufacturersIdentification));
        Response<String> response = requestString(request);
        if (response.isSucceed()) {

            event.addReturnParam(response.get().toString());
            event.setSuccess(true);
        }
    }
}