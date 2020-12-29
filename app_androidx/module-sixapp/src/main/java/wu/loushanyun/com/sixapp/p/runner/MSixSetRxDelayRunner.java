package wu.loushanyun.com.sixapp.p.runner;

import com.wu.loushanyun.base.url.URLUtils;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONException;
import org.json.JSONObject;

import met.hx.com.base.baseevent.Event;

public class MSixSetRxDelayRunner extends SixCommonRunner {
    @Override
    public void onEventRun(Event event) {
        Request<String> request = NoHttp.createStringRequest("http://39.100.145.211" + URLUtils.MChipSetRxDelay, RequestMethod.POST);
        String sn = (String) event.getParamAtIndex(0);
        String rxDelay = (String) event.getParamAtIndex(1);
        request.add("sn", sn);
        request.add("rxDelay", rxDelay);
        Response<String> response = requestString(request);
        if (response.isSucceed()) {
            try {
                JSONObject jsonObject = new JSONObject(response.get());
                int code = jsonObject.optInt("code");
                String msg = jsonObject.optString("msg");
                event.addReturnParam(code);
                event.addReturnParam(msg);
                event.setSuccess(true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
