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
import wu.loushanyun.com.modulechiptest.m.ResultJson;

public class GetUpDataRunner extends ChipCommonRunner {
    @Override
    public void onEventRun(Event event) {
        String upData = (String) event.getParamAtIndex(0);
        HashMap<String, Object> params = new HashMap();
        params.put("upData", upData);
        Request<String> request = NoHttp.createStringRequest(URLUtils.getIP() + URLUtils.MChipGetUpdata, RequestMethod.POST);
        request.add(params);
        Response<String> response = requestString(request);
        if (response.isSucceed()) {
            String result = response.get();
            Gson gson = new Gson();
            ResultJson resultJson = gson.fromJson(result, ResultJson.class);
            event.setSuccess(true);
            event.addReturnParam(resultJson);
        }
    }
}
