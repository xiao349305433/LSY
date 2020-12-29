package wu.loushanyun.com.moduletwoinit.p.runner;

import com.wu.loushanyun.base.url.URLUtils;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONException;
import org.json.JSONObject;

import met.hx.com.base.base.nohttp.CommonRunner;
import met.hx.com.base.baseevent.Event;

public class UpdateAreaInfoRunner extends CommonRunner{
    @Override
    public void onEventRun(Event event) {
        String updateAreaInfo = (String) event.getParamAtIndex(0);
        Request<String> request = NoHttp.createStringRequest(URLUtils.getIP() + URLUtils.MInitTwoUpdateAreaInfo, RequestMethod.POST);
        request.add("updateAreaInfo", updateAreaInfo);
        Response<String> response = requestString(request);
        if (response.isSucceed()) {
            try {
                JSONObject jsonObject = new JSONObject(response.get());
                int code = jsonObject.optInt("code");
                event.addReturnParam(code);
                event.addReturnParam(jsonObject.optString("msg"));
                event.setSuccess(true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
