package wu.loushanyun.com.modulechiptest.p.runner;

import com.google.gson.Gson;
import com.wu.loushanyun.base.url.URLUtils;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

import met.hx.com.base.base.nohttp.CommonRunner;
import met.hx.com.base.baseevent.Event;
import wu.loushanyun.com.modulechiptest.m.SensoroDeviceInfo;

public class MChipDeviceHisDataRunner extends ChipCommonRunner{
    @Override
    public void onEventRun(Event event) {
        String sn= (String) event.getParamAtIndex(0);
        Request<String> request= NoHttp.createStringRequest(URLUtils.getIP()+ URLUtils.MChipDeviceHisData, RequestMethod.GET);
        request.add("sn", sn);
        request.add("pageNum", 1);
        request.add("pageSize", 5);
        Response<String> response= requestString(request);
        if(response.isSucceed()){
            String result=response.get();
            SensoroDeviceInfo sensoroDeviceInfo=new Gson().fromJson(result,SensoroDeviceInfo.class);
            event.setSuccess(true);
            event.addReturnParam(sensoroDeviceInfo);
        }
    }
}
