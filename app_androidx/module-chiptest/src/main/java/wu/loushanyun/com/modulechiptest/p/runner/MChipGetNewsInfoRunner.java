package wu.loushanyun.com.modulechiptest.p.runner;

import com.google.gson.Gson;
import com.wu.loushanyun.base.url.URLUtils;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

import met.hx.com.base.base.nohttp.CommonRunner;
import met.hx.com.base.baseevent.Event;
import wu.loushanyun.com.modulechiptest.m.NewInfo;

public class MChipGetNewsInfoRunner extends ChipCommonRunner {
    @Override
    public void onEventRun(Event event) {
        Request<String> request = NoHttp.createStringRequest(URLUtils.getIP() + URLUtils.MChipGetNewsInfo, RequestMethod.GET);
        String sn = (String) event.getParamAtIndex(0);
        String pageNum = (String) event.getParamAtIndex(1);
        String pageSize = (String) event.getParamAtIndex(2);
        request.add("sn", sn);
        request.add("pageNum", pageNum);
        request.add("pageSize", pageSize);
        Response<String> response = requestString(request);
        if (response.isSucceed()) {
            NewInfo newInfo=new Gson().fromJson(response.get(),NewInfo.class);
            event.addReturnParam(newInfo);
            event.setSuccess(true);

        }
    }
}
