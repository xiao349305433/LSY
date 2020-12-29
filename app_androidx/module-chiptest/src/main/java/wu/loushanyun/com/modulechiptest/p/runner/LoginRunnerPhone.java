package wu.loushanyun.com.modulechiptest.p.runner;

import com.google.gson.Gson;
import com.wu.loushanyun.base.url.URLUtils;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

import java.util.HashMap;

import met.hx.com.base.base.nohttp.CommonRunner;
import met.hx.com.base.baseevent.Event;
import wu.loushanyun.com.modulechiptest.m.FactoryLoginData;
import wu.loushanyun.com.modulechiptest.m.LoginInfo;

public class LoginRunnerPhone extends CommonRunner {
    @Override
    public void onEventRun(Event event) {
        String name = (String) event.getParamAtIndex(0);
        String pwd = (String) event.getParamAtIndex(1);
        HashMap<String, Object> params = new HashMap();
        params.put("login", name);
        params.put("code", pwd);
        Request<String> request = NoHttp.createStringRequest(URLUtils.getIP() + URLUtils.MChipLogin, RequestMethod.POST);

        request.add(params);
        Response<String> response = requestString(request);
        if (response.isSucceed()) {
            String result = response.get();
            Gson gson = new Gson();
            LoginInfo loginInfo = gson.fromJson(result, LoginInfo.class);
            event.setSuccess(true);
            event.addReturnParam(loginInfo);
        }
    }
}
