package wu.loushanyun.com.fivemoduleapp.p.runner;

import com.google.gson.Gson;
import com.wu.loushanyun.base.url.URLUtils;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

import java.util.HashMap;

import met.hx.com.base.base.nohttp.CommonRunner;
import met.hx.com.base.baseevent.Event;
import wu.loushanyun.com.fivemoduleapp.m.ResultJson;

public class UpdateLoginPassWordRunner extends FiveCommonRunner{
    @Override
    public void onEventRun(Event event) {
        String registerPhone= (String) event.getParamAtIndex(0);
        String loginPassword= (String) event.getParamAtIndex(1);
        HashMap<String, Object> params = new HashMap();
        params.put("registPhone", registerPhone);
        params.put("loginPassword", loginPassword);
        Request<String> request= NoHttp.createStringRequest(URLUtils.getIP()+ URLUtils.IFiveUpdateLoginPwd, RequestMethod.POST);
        request.add(params);
        Response<String> response= requestString(request);
        if(response.isSucceed()){
            String result=response.get();
            ResultJson resultJson=new Gson().fromJson(result,ResultJson.class);
            if(resultJson.getCode()==0){
                event.setSuccess(true);
            }else {
                event.setSuccess(false);
            }
        }
    }
}
