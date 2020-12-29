package wu.loushanyun.com.modulerepair.p.runner;

import com.google.gson.Gson;
import com.wu.loushanyun.base.url.URLUtils;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.CacheMode;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

import met.hx.com.base.base.nohttp.CommonRunner;
import met.hx.com.base.baseevent.Event;
import wu.loushanyun.com.modulerepair.m.GetProductData;

public class GetProducterRunner extends CommonRunner{
    @Override
    public void onEventRun(Event event) {
        String tradeRegistId = (String) event.getParamAtIndex(0);
        Request<String> request = NoHttp.createStringRequest(URLUtils.getIP() + URLUtils.MRepairGetProducter, RequestMethod.GET);
        request.setCacheMode(CacheMode.REQUEST_NETWORK_FAILED_READ_CACHE);
        request.add("tradeRegistId", tradeRegistId);
        Response<String> response = requestString(request);
        if (response.isSucceed()) {
            Gson gson = new Gson();
            GetProductData areaInfo = gson.fromJson(response.get(), GetProductData.class);
            event.addReturnParam(areaInfo);
            event.setSuccess(true);
        }

    }
}
