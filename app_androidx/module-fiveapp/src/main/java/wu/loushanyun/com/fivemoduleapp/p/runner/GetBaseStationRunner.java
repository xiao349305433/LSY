package wu.loushanyun.com.fivemoduleapp.p.runner;

import com.google.gson.Gson;
import com.wu.loushanyun.base.url.URLUtils;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import met.hx.com.base.base.nohttp.CommonRunner;
import met.hx.com.base.baseevent.Event;
import wu.loushanyun.com.fivemoduleapp.m.BaseStation;

public class GetBaseStationRunner extends FiveCommonRunner {
    @Override
    public void onEventRun(Event event) {
        String traderegisterId= (String) event.getParamAtIndex(0);
        HashMap<String, Object> params = new HashMap();
        params.put("traderegisterId", traderegisterId);
        Request<String> request= NoHttp.createStringRequest(URLUtils.getIP()+ URLUtils.IFivegetStationList, RequestMethod.GET);
        request.add(params);
        Response<String> response= requestString(request);
        if(response.isSucceed()){
            try {
                JSONObject jo = new JSONObject(response.get());
                JSONArray array = jo.optJSONArray("data");
                event.addReturnParam(array.length());
                for (int i = 0; i < array.length(); i++) {
                    event.addReturnParam(new Gson().fromJson(array.getString(i), BaseStation.class));
                }
                event.setSuccess(true);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
