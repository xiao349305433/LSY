package com.wu.loushanyun.basemvp.p.runner;

import com.wu.loushanyun.base.url.URLUtils;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import met.hx.com.base.base.nohttp.CommonRunner;
import met.hx.com.base.baseevent.Event;

public class ArrearsVerificationRunner extends CommonRunner {
    @Override
    public void onEventRun(Event event) {
        String id = (String) event.getParamAtIndex(0);
        HashMap<String, Object> params = new HashMap();
        params.put("id", id);
        Request<String> request = NoHttp.createStringRequest(URLUtils.getIP() + URLUtils.IFiveArrearsVerification, RequestMethod.GET);
        request.add(params);
        Response<String> response = requestString(request);
        if (response.isSucceed()) {
            String result = response.get();
            try {
                JSONObject jsonObject = new JSONObject(result);
                int code = jsonObject.optInt("code");
                String msg = jsonObject.optString("msg");
                event.setSuccess(true);
                event.addReturnParam(code);
                event.addReturnParam(msg);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }
}