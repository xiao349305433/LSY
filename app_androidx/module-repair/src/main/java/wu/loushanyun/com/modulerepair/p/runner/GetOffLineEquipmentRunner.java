package wu.loushanyun.com.modulerepair.p.runner;

import com.google.gson.Gson;
import com.wu.loushanyun.base.url.URLUtils;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

import met.hx.com.base.base.nohttp.CommonRunner;
import met.hx.com.base.baseevent.Event;
import wu.loushanyun.com.modulerepair.m.RepairDataMsg;

public class GetOffLineEquipmentRunner extends CommonRunner {
    @Override
    public void onEventRun(Event event) {
        String gatherScene = (String) event.getParamAtIndex(0);
        String productForm = (String) event.getParamAtIndex(1);
        String tradeRegistId = (String) event.getParamAtIndex(2);
        String offLineStartHour = (String) event.getParamAtIndex(3);
        String offLineEndtHour = (String) event.getParamAtIndex(4);
        String manufacturersIdentification = (String) event.getParamAtIndex(5);
        String pageNum = (String) event.getParamAtIndex(6);
        String pageSize = (String) event.getParamAtIndex(7);
        String lon = (String) event.getParamAtIndex(8);
        String lat = (String) event.getParamAtIndex(9);
        String order = (String) event.getParamAtIndex(10);
        String distance = (String) event.getParamAtIndex(11);
        Request<String> request = NoHttp.createStringRequest(URLUtils.getIP() + URLUtils.MRepairGetOffLineEquipment, RequestMethod.GET);
        request.add("gatherScene", gatherScene);
        request.add("productForm", productForm);
        request.add("tradeRegistId", tradeRegistId);
        if (!"-1".equalsIgnoreCase(offLineStartHour)) {
            request.add("offLineStartHour", offLineStartHour);
        }
        if (!"-1".equalsIgnoreCase(offLineEndtHour)) {
            request.add("offLineEndtHour", offLineEndtHour);
        }
        if (!"-1".equalsIgnoreCase(manufacturersIdentification)) {
            request.add("manufacturersIdentification", manufacturersIdentification);
        }
        if (!"-1".equalsIgnoreCase(lon)) {
            request.add("lon", lon);
        }
        if (!"-1".equalsIgnoreCase(lat)) {
            request.add("lat", lat);
        }
        if (!"-1".equalsIgnoreCase(order)) {
            request.add("order", order);
        }
        if (!"-1".equalsIgnoreCase(distance)) {
            request.add("distance", distance);
        }
        request.add("pageNum", pageNum);
        request.add("pageSize", pageSize);
        Response<String> response = requestString(request);
        if (response.isSucceed()) {
            Gson gson = new Gson();
            RepairDataMsg repairDataMsg = gson.fromJson(response.get(), RepairDataMsg.class);
            event.addReturnParam(repairDataMsg);
            event.setSuccess(true);
        }
    }
}
