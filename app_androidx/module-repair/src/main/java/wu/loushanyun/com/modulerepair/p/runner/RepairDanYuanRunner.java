package wu.loushanyun.com.modulerepair.p.runner;

import com.google.gson.Gson;
import com.wu.loushanyun.base.url.URLUtils;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

import met.hx.com.base.base.nohttp.CommonRunner;
import met.hx.com.base.baseevent.Event;
import wu.loushanyun.com.modulerepair.m.RepairDanYuanData;

public class RepairDanYuanRunner extends CommonRunner{
    @Override
    public void onEventRun(Event event) {
        String sn = (String) event.getParamAtIndex(0);
        Request<String> request = NoHttp.createStringRequest(URLUtils.getIP() + URLUtils.MRepairGetOffLineThirdEquipment, RequestMethod.GET);
        request.add("sn", sn);
        Response<String> response = requestString(request);
        if (response.isSucceed()) {
            Gson gson = new Gson();
            RepairDanYuanData repairDanYuanData = gson.fromJson(response.get(), RepairDanYuanData.class);
            event.addReturnParam(repairDanYuanData);
            event.setSuccess(true);
        }
    }
}
