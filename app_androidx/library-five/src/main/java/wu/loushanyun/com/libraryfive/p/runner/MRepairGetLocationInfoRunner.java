package wu.loushanyun.com.libraryfive.p.runner;

import com.elvishew.xlog.XLog;
import com.google.gson.Gson;
import com.wu.loushanyun.base.url.URLUtils;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONException;
import org.json.JSONObject;

import met.hx.com.base.base.nohttp.CommonRunner;
import met.hx.com.base.baseevent.Event;
import wu.loushanyun.com.libraryfive.m.RepairLocationDetail;

public class MRepairGetLocationInfoRunner extends CommonRunner {
    @Override
    public void onEventRun(Event event) {
        String traderegisterId = (String) event.getParamAtIndex(0);
        String sn = (String) event.getParamAtIndex(1);
        Request<String> request = NoHttp.createStringRequest(URLUtils.getIP() + URLUtils.MRepairGetLocationInfo, RequestMethod.GET);
        request.add("traderegisterId", traderegisterId);
        request.add("sn", sn);
        Response<String> response = requestString(request);
        if (response.isSucceed()) {
            try {
                JSONObject jsonObject = new JSONObject(response.get());
                int code = jsonObject.optInt("code");
                String msg = jsonObject.optString("msg");
                XLog.i("code="+code);
                if (code != 0) {
                    event.addReturnParam(code);
                    event.addReturnParam(msg);
                } else {
                    JSONObject data = jsonObject.getJSONObject("data");
                    Gson gson = new Gson();
                    RepairLocationDetail repairLocationDetail = gson.fromJson(data.toString(), RepairLocationDetail.class);
                    event.addReturnParam(code);
                    event.addReturnParam(msg);
                    event.addReturnParam(repairLocationDetail);
                }
                event.setSuccess(true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
