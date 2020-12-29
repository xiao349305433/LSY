package com.wu.loushanyun.basemvp.p.runner;

import com.wu.loushanyun.base.url.URLUtils;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONException;
import org.json.JSONObject;

import met.hx.com.base.base.nohttp.CommonRunner;
import met.hx.com.base.baseevent.Event;

public class IFiveSelectTokenRunner extends CommonRunner {
    @Override
    public void onEventRun(Event event) {
        String tradeRegisterId = (String) event.getParamAtIndex(0);
        Request<String> request = NoHttp.createStringRequest(URLUtils.getIP() + URLUtils.IFiveselectToken, RequestMethod.GET);
        request.add("tradeRegisterId", tradeRegisterId);
        Response<String> response = requestString(request);
        if (response.isSucceed()) {
            String result = response.get();
            try {
                JSONObject jsonObject = new JSONObject(result);
                int code = jsonObject.optInt("code");
                String msg = jsonObject.optString("msg");
                String data = jsonObject.optString("data");
                event.setSuccess(true);
                event.addReturnParam(code);
                event.addReturnParam(msg);
                event.addReturnParam(data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
