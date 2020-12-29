package wu.loushanyun.com.moduletwoinit.p.runner;

import com.google.gson.Gson;
import com.wu.loushanyun.base.url.URLUtils;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

import java.util.ArrayList;

import met.hx.com.base.base.nohttp.CommonRunner;
import met.hx.com.base.baseevent.Event;
import wu.loushanyun.com.moduletwoinit.m.AreaDetail;
import wu.loushanyun.com.moduletwoinit.m.OnCloudConverterDeviceData;

public class GetAreaDetailRunner extends CommonRunner {
    @Override
    public void onEventRun(Event event) {
        String areaNumber = (String) event.getParamAtIndex(0);
        Request<String> request = NoHttp.createStringRequest(URLUtils.getIP() + URLUtils.MInitTwogetAreaDetail, RequestMethod.GET);
        request.add("areaNumber", areaNumber);
        Response<String> response = requestString(request);
        if (response.isSucceed()) {
            Gson gson = new Gson();
            AreaDetail areaDetail = gson.fromJson(response.get(), AreaDetail.class);
            ArrayList<OnCloudConverterDeviceData> arrayList = new ArrayList<>();

            for (int i = 0; i < areaDetail.getData().size(); i++) {
                AreaDetail.DataBean dataBean = areaDetail.getData().get(i);
                OnCloudConverterDeviceData onCloudConverterDeviceData = new OnCloudConverterDeviceData(
                        dataBean.getSensingSignal(), dataBean.getManufacturersName(), dataBean.getEquipmentSN(), dataBean.getRemark(), dataBean.getEquipmentTime(), true);
                arrayList.add(onCloudConverterDeviceData);
            }
            event.addReturnParam(arrayList);
            event.setSuccess(true);
        }
    }
}
