package com.loushanyun.modulefactory.p.runner;

import com.wu.loushanyun.base.url.URLUtils;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONException;
import org.json.JSONObject;

import met.hx.com.base.base.nohttp.CommonRunner;
import met.hx.com.base.baseevent.Event;
import met.hx.com.librarybase.some_utils.LogUtils;

public class UploadDataRunner extends CommonRunner{
    @Override
    public void onEventRun(Event event) {
        String jsonUpload= (String) event.getParamAtIndex(0);
        LogUtils.i("上传的："+jsonUpload);
        Request<String> request= NoHttp.createStringRequest(URLUtils.getIP()+ URLUtils.FSFactorySetting, RequestMethod.POST);
        request.add("factorySetting", jsonUpload);
        Response<String> response= requestString(request);
        if(response.isSucceed()){
            int code = 0;
            try {
                code = new JSONObject(response.get()).optInt("code");
                String msg = new JSONObject(response.get()).optString("msg");
                event.addReturnParam(code);
                event.addReturnParam(msg);
                event.setSuccess(true);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
