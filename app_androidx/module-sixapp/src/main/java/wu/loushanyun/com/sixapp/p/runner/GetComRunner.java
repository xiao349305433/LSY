package wu.loushanyun.com.sixapp.p.runner;

import com.google.gson.Gson;
import com.wu.loushanyun.base.url.URLUtils;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

import met.hx.com.base.baseevent.Event;
import wu.loushanyun.com.sixapp.m.GetBatchInfo;
import wu.loushanyun.com.sixapp.m.GetComInfo;

public class GetComRunner extends SixCommonRunner{
    @Override
    public void onEventRun(Event event) {
        int loginId= (int) event.getParamAtIndex(0);
        Request<String> request= NoHttp.createStringRequest(URLUtils.getIP() + URLUtils.MSixGetCom, RequestMethod.POST);
        request.add("loginId", loginId);
        Response<String> response= requestString(request);
        if(response.isSucceed()){
            String result=response.get();
            Gson gson = new Gson();
            GetComInfo getComInfo = gson.fromJson(result, GetComInfo.class);
            event.setSuccess(true);
            event.addReturnParam(getComInfo);
        }
    }
}
