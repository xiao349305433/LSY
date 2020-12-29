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
import wu.loushanyun.com.moduletwoinit.m.AreaList;
import wu.loushanyun.com.moduletwoinit.m.WuLianUploadData;

public class GetAreaListRunner extends CommonRunner {
    @Override
    public void onEventRun(Event event) {
        String tradeRegisterId = (String) event.getParamAtIndex(0);
        String pageNum = (String) event.getParamAtIndex(1);
        String pageSize = (String) event.getParamAtIndex(2);
        Request<String> request = NoHttp.createStringRequest(URLUtils.getIP() + URLUtils.MInitTwogetAreaList, RequestMethod.GET);
        request.add("tradeRegisterId", tradeRegisterId);
        request.add("pageNum", pageNum);
        request.add("pageSize", pageSize);
        Response<String> response = requestString(request);
        if (response.isSucceed()) {
            Gson gson = new Gson();
            AreaList areaList = gson.fromJson(response.get(), AreaList.class);
            ArrayList<WuLianUploadData> wuLianUploadDataArrayList=new ArrayList<>();
            for(int i=0;i<areaList.getData().size();i++){
                WuLianUploadData wuLianUploadData=new WuLianUploadData();
                AreaList.DataBean dataBean=areaList.getData().get(i);
                wuLianUploadData.setAreaNumber(dataBean.getAreaNumber());
                wuLianUploadData.setAreaName(dataBean.getAreaName());
                wuLianUploadData.setTime(dataBean.getSaveTime()+"");
                wuLianUploadDataArrayList.add(wuLianUploadData);
            }
            event.addReturnParam(areaList.getCode());
            event.addReturnParam(wuLianUploadDataArrayList);
            event.setSuccess(true);
        }
    }
}
