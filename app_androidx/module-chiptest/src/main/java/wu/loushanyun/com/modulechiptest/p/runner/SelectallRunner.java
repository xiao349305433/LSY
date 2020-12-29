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
import wu.loushanyun.com.modulechiptest.m.LoginInfo;
import wu.loushanyun.com.modulechiptest.m.ResultJson;
import wu.loushanyun.com.modulechiptest.m.SelectallInfo;

public class SelectallRunner extends ChipCommonRunner {
    @Override
    public void onEventRun(Event event) {
        String mLoginFactoryNum = (String) event.getParamAtIndex(0);
        int inspectiontype = (int) event.getParamAtIndex(1);
        int timesort = (int) event.getParamAtIndex(2);
        int moduleType = (int) event.getParamAtIndex(3);
        int pageNum = (int) event.getParamAtIndex(4);
        int pageSize = (int) event.getParamAtIndex(5);
        HashMap<String, Object> params = new HashMap();
        params.put("mLoginFactoryNum", mLoginFactoryNum);
        if(inspectiontype>=0){
            params.put("inspectiontype", inspectiontype);
        }
        params.put("timesort", timesort);
        params.put("moduleType", moduleType);
        params.put("pageNum", pageNum);
        params.put("pageSize", pageSize);
        Request<String> request = NoHttp.createStringRequest(URLUtils.getIP() + URLUtils.MChipSelectall, RequestMethod.POST);
        request.add(params);
        Response<String> response = requestString(request);
        if (response.isSucceed()) {
            String result = response.get();
            Gson gson = new Gson();
            SelectallInfo selectallInfo = gson.fromJson(result, SelectallInfo.class);
            event.setSuccess(true);
            event.addReturnParam(selectallInfo);
        }
    }
}
