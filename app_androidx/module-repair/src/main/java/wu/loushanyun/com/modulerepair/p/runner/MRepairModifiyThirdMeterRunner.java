package wu.loushanyun.com.modulerepair.p.runner;

import com.wu.loushanyun.base.url.URLUtils;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONException;
import org.json.JSONObject;

import met.hx.com.base.base.nohttp.CommonRunner;
import met.hx.com.base.baseevent.Event;

public class MRepairModifiyThirdMeterRunner extends CommonRunner {
    @Override
    public void onEventRun(Event event) {
        String newMeterInfo = (String) event.getParamAtIndex(0);
        Request<String> request = NoHttp.createStringRequest(URLUtils.getIP() + URLUtils.MRepairModifiyThirdMeter, RequestMethod.POST);
        request.add("newMeterInfo", newMeterInfo);
        Response<String> response = requestString(request);
        if (response.isSucceed()) {
            try {
                JSONObject jsonObject =new JSONObject(response.get());
                int code = jsonObject.optInt("code");
                event.addReturnParam(code);
                event.addReturnParam(jsonObject.opt("msg"));
                event.setSuccess(true);
            } catch (JSONException e) {
                e.printStackTrace();
                event.setSuccess(false);
            }

        }
    }
}
