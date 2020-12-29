package wu.loushanyun.com.fivemoduleapp.p.runner;

import com.google.gson.Gson;
import com.wu.loushanyun.base.url.URLUtils;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

import java.util.HashMap;

import wu.loushanyun.com.fivemoduleapp.m.ResponseAnalysis;
import met.hx.com.base.base.nohttp.CommonRunner;
import met.hx.com.base.baseevent.Event;

public class LoginRunner extends FiveCommonRunner{
    @Override
    public void onEventRun(Event event) {
        String name= (String) event.getParamAtIndex(0);
        String pwd= (String) event.getParamAtIndex(1);
        HashMap<String, Object> params = new HashMap();
        params.put("loginNameOrPhone", name);
        params.put("loginPwd", pwd);
        Request<String> request= NoHttp.createStringRequest(URLUtils.getIP()+ URLUtils.ILLoginURL, RequestMethod.POST);
        request.add(params);
        Response<String> response= requestString(request);
        if(response.isSucceed()){
            String result=response.get();
            Gson gson = new Gson();
            ResponseAnalysis productregister = gson.fromJson(result, ResponseAnalysis.class);
            event.setSuccess(true);
            event.addReturnParam(productregister);
        }
    }
}
