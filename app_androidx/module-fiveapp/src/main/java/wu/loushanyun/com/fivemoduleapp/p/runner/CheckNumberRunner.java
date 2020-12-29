package wu.loushanyun.com.fivemoduleapp.p.runner;

import com.google.gson.Gson;
import com.wu.loushanyun.base.url.URLUtils;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

import met.hx.com.base.base.nohttp.CommonRunner;
import met.hx.com.base.baseevent.Event;
import wu.loushanyun.com.fivemoduleapp.m.ResultJson;

public class CheckNumberRunner extends FiveCommonRunner{
    @Override
    public void onEventRun(Event event) {
        String tel= (String) event.getParamAtIndex(0);
        Request<String> request= NoHttp.createStringRequest(URLUtils.getIP()+ URLUtils.IFiveCheckUserExist, RequestMethod.POST);
        request.add("registPhone", tel);
        Response<String> response= requestString(request);
        if(response.isSucceed()){
            String result=response.get();
            ResultJson resultJson=new Gson().fromJson(result,ResultJson.class);
            event.addReturnParam(resultJson);
            event.setSuccess(true);
        }
    }
}
