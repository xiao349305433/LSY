package wu.loushanyun.com.sixapp.p.runner;

import com.google.gson.Gson;
import com.wu.loushanyun.base.url.URLUtils;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

import met.hx.com.base.baseevent.Event;
import wu.loushanyun.com.sixapp.m.ProInfo;
import wu.loushanyun.com.sixapp.m.ResultJson;

public class ProInfRunner extends SixCommonRunner{
    @Override
    public void onEventRun(Event event) {
        int loginId= (int) event.getParamAtIndex(0);
        int authId= (int) event.getParamAtIndex(1);
        String batchNumber= (String) event.getParamAtIndex(2);
        Request<String> request= NoHttp.createStringRequest(URLUtils.getIP() + URLUtils.MSixProInf, RequestMethod.POST);
        request.add("loginId", loginId);
        request.add("authId", authId);
        request.add("batchNumber", batchNumber);
        Response<String> response= requestString(request);
        if(response.isSucceed()){
            String result=response.get();
            Gson gson = new Gson();
            ProInfo resultJson = gson.fromJson(result, ProInfo.class);
            event.setSuccess(true);
            event.addReturnParam(resultJson);
        }
    }
}
