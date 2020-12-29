package wu.loushanyun.com.libraryfive.p.runner;

import com.elvishew.xlog.XLog;
import com.google.gson.Gson;
import com.wu.loushanyun.base.url.URLUtils;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;


import met.hx.com.base.base.nohttp.CommonRunner;
import met.hx.com.base.baseevent.Event;
import wu.loushanyun.com.libraryfive.m.GridNumBean;

public class GetTraceRunner extends CommonRunner {
    @Override
    public void onEventRun(Event event) {
        String x = (String) event.getParamAtIndex(0);
        String y = (String) event.getParamAtIndex(1);
//        String token = "6c1dccbf-89d2-45b4-85e8-cab5dbbe25d3";
        String token = (String) event.getParamAtIndex(2);
        String k = "[{longitude:" + x + ",latitude:" + y + "}]";
        XLog.i("和萨达是的哈加快速度"+k);
        Request<String> request = NoHttp.createStringRequest(URLUtils.IPAPI, RequestMethod.POST);
        request.setDefineRequestBody(k,"application/json");
        request.addHeader("Authorization", token);
        XLog.i("和萨达是的哈加快速度"+token);
        Response<String> response = requestString(request);
        if (response.isSucceed()) {
            GridNumBean gridNumBean=new Gson().fromJson(response.get(),GridNumBean.class);
            if(gridNumBean.getStatus()==200){
                event.addReturnParam(0);
                event.addReturnParam(gridNumBean.getMsg());
                event.addReturnParam(gridNumBean.getData().get(0).getGridcode());
                event.addReturnParam(gridNumBean.getData().get(0).getCenter().getLongitude());
                event.addReturnParam(gridNumBean.getData().get(0).getCenter().getLatitude());
            }else {
                event.addReturnParam(1);
                event.addReturnParam(gridNumBean.getMsg());
            }
            event.setSuccess(true);
        }
    }


}
