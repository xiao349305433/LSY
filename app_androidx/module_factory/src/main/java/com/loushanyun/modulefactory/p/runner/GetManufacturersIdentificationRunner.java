package com.loushanyun.modulefactory.p.runner;

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

public class GetManufacturersIdentificationRunner extends CommonRunner{
    @Override
    public void onEventRun(Event event) {
        long manufacturersIdentification= (long) event.getParamAtIndex(0);
        HashMap<String, Object> params = new HashMap();
        params.put("manufacturersIdentification", String.valueOf(manufacturersIdentification));
        Request<String> request= NoHttp.createStringRequest(URLUtils.getIP()+ URLUtils.FSGetManufacturersIdentification, RequestMethod.GET);
        request.add(params);
        Response<String> response= requestString(request);
        if(response.isSucceed()){
            String result=response.get();
            try {
                JSONObject jsonObject=new JSONObject(result);
                int code=jsonObject.optInt("code");
                if(code==0){
                    String companyName=jsonObject.optString("companyName");
                    event.setSuccess(true);
                    event.addReturnParam(companyName);
                }else {
                    event.setSuccess(false);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }
}
