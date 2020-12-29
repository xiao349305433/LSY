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
import wu.loushanyun.com.modulechiptest.m.SelecorderInfo;

public class SelectMoudulRunner extends ChipCommonRunner {
    @Override
    public void onEventRun(Event event) {
        String moduleType = (String) event.getParamAtIndex(0);
        String orderNumber = (String) event.getParamAtIndex(1);
        String pageNum = (String) event.getParamAtIndex(2);
        String pageSize = (String) event.getParamAtIndex(3);
        HashMap<String, Object> params = new HashMap();
        params.put("moduleType", moduleType);
        params.put("orderNumber", orderNumber);
        params.put("pageNum", pageNum);
        params.put("pageSize", pageSize);
        Request<String> request = NoHttp.createStringRequest(URLUtils.getIP() + URLUtils.MChipSelectMoudul, RequestMethod.POST);
        request.add(params);
        Response<String> response = requestString(request);
        if (response.isSucceed()) {
            String result = response.get();
            Gson gson = new Gson();
            SelecorderInfo selectallInfo = gson.fromJson(result, SelecorderInfo.class);
            event.setSuccess(true);
            event.addReturnParam(selectallInfo);
        }
    }
}