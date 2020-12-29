package wu.loushanyun.com.moduletwoinit.p.runner;

import com.google.gson.Gson;
import com.wu.loushanyun.base.url.URLUtils;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

import met.hx.com.base.base.nohttp.CommonRunner;
import met.hx.com.base.baseevent.Event;
import wu.loushanyun.com.moduletwoinit.m.AreaDetail;

public class GetAreaDetailRunnerNew extends CommonRunner {
    @Override
    public void onEventRun(Event event) {
        String areaNumber = (String) event.getParamAtIndex(0);
        Request<String> request = NoHttp.createStringRequest(URLUtils.getIP() + URLUtils.MInitTwogetAreaDetail, RequestMethod.GET);
        request.add("areaNumber", areaNumber);
        Response<String> response = requestString(request);
        if (response.isSucceed()) {
            Gson gson = new Gson();
            AreaDetail areaDetail = gson.fromJson(response.get(), AreaDetail.class);
            event.addReturnParam(areaDetail);
            event.setSuccess(true);
        }
    }
}
