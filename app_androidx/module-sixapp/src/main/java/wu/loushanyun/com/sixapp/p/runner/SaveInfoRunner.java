package wu.loushanyun.com.sixapp.p.runner;

import com.google.gson.Gson;
import com.wu.loushanyun.base.url.URLUtils;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

import java.util.HashMap;

import met.hx.com.base.baseevent.Event;
import wu.loushanyun.com.sixapp.m.ResultJson;

public class SaveInfoRunner extends SixCommonRunner{
    @Override
    public void onEventRun(Event event) {
        String testdata= (String) event.getParamAtIndex(0);
        HashMap<String, Object> params = new HashMap();
        params.put("testUpData", testdata);
        Request<String> request= NoHttp.createStringRequest(URLUtils.getIP() + URLUtils.MSixSaveInf, RequestMethod.POST);
        request.add(params);
      //  request.addHeader("Content-Type","application/json;charset=UTF-8");
        Response<String> response= requestString(request);
        if(response.isSucceed()){
            String result=response.get();
            Gson gson = new Gson();
            ResultJson resultJson = gson.fromJson(result, ResultJson.class);
            event.setSuccess(true);
            event.addReturnParam(resultJson);
        }
    }
}
