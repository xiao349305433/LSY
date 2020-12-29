package wu.loushanyun.com.sixapp.p.runner;

import com.google.gson.Gson;
import com.wu.loushanyun.base.url.URLUtils;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

import met.hx.com.base.baseevent.Event;
import wu.loushanyun.com.sixapp.m.GetComInfo;
import wu.loushanyun.com.sixapp.m.GetModelInfo;

public class GetModelRunner extends SixCommonRunner{
    @Override
    public void onEventRun(Event event) {
        int accountId= (int) event.getParamAtIndex(0);
        Request<String> request= NoHttp.createStringRequest(URLUtils.getIP() + URLUtils.MSixGetModel, RequestMethod.POST);
        request.add("accountId", accountId);
        Response<String> response= requestString(request);
        if(response.isSucceed()){
            String result=response.get();
            Gson gson = new Gson();
            GetModelInfo getComInfo = gson.fromJson(result, GetModelInfo.class);
            event.setSuccess(true);
            event.addReturnParam(getComInfo);
        }
    }
}
