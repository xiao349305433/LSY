package wu.loushanyun.com.sixapp.p.runner;

import com.google.gson.Gson;
import com.wu.loushanyun.base.url.URLUtils;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

import met.hx.com.base.baseevent.Event;
import wu.loushanyun.com.sixapp.m.GoodsInfo;
import wu.loushanyun.com.sixapp.m.ResultJson;

public class GoodsInfRunner extends SixCommonRunner{
    @Override
    public void onEventRun(Event event) {
        int goodsId= (int) event.getParamAtIndex(0);
        int loginId= (int) event.getParamAtIndex(1);
        int authId= (int) event.getParamAtIndex(2);
        String batchNumber= (String) event.getParamAtIndex(3);
        int orderId= (int) event.getParamAtIndex(4);
        Request<String> request= NoHttp.createStringRequest(URLUtils.getIP() + URLUtils.MSixGoodsInf, RequestMethod.POST);
        request.add("goodsId", goodsId);
        request.add("loginId", loginId);
        request.add("authId", authId);
        request.add("batchNumber", batchNumber);
        request.add("orderId", orderId);
        Response<String> response= requestString(request);
        if(response.isSucceed()){
            String result=response.get();
            Gson gson = new Gson();
            GoodsInfo resultJson = gson.fromJson(result, GoodsInfo.class);
            event.setSuccess(true);
            event.addReturnParam(resultJson);
        }
    }
}
