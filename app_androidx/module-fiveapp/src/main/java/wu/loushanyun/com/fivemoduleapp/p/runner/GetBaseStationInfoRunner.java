package wu.loushanyun.com.fivemoduleapp.p.runner;

import com.wu.loushanyun.base.url.URLUtils;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import met.hx.com.base.base.nohttp.CommonRunner;
import met.hx.com.base.baseevent.Event;

public class GetBaseStationInfoRunner extends FiveCommonRunner {
    @Override
    public void onEventRun(Event event) {
        String traderegisterId= (String) event.getParamAtIndex(0);
        HashMap<String, Object> params = new HashMap();
        params.put("traderegisterId", traderegisterId);
        Request<String> request= NoHttp.createStringRequest(URLUtils.getIP()+ URLUtils.IFivegetStationInfo, RequestMethod.GET);
        request.add(params);
        Response<String> response= requestString(request);
        if(response.isSucceed()){
            try {
                JSONObject jo = new JSONObject(response.get()).optJSONObject("data");
                JSONObject joa = jo.optJSONObject("模式A");
                event.addReturnParam(joa.optString("totalNum"));//总数
                event.addReturnParam(joa.optString("offLineNum"));//离线
                event.addReturnParam(joa.optString("cellularNum"));//3G
                event.addReturnParam( joa.optString("ethernetNum"));//以太网
                JSONObject job = jo.optJSONObject("模式B");
                event.addReturnParam(job.optString("totalNum"));
                event.addReturnParam(job.optString("offLineNum"));
                event.addReturnParam(job.optString("cellularNum"));
                event.addReturnParam( job.optString("ethernetNum"));
                event.setSuccess(true);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
