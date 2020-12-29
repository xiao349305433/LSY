package wu.loushanyun.com.sixapp.p.runner;

import com.google.gson.Gson;
import com.wu.loushanyun.base.url.URLUtils;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

import java.util.HashMap;

import met.hx.com.base.baseevent.Event;
import wu.loushanyun.com.sixapp.m.EnvNameInfo;
import wu.loushanyun.com.sixapp.m.ResultJson;

public class ContractProRunner extends SixCommonRunner{
    @Override
    public void onEventRun(Event event) {
        String lotSn= (String) event.getParamAtIndex(0);
        int productForm= (int) event.getParamAtIndex(1);
        int authId= (int) event.getParamAtIndex(2);
        int accountId= (int) event.getParamAtIndex(3);
        Request<String> request= NoHttp.createStringRequest(URLUtils.getIP() + URLUtils.MSixContractPro, RequestMethod.POST);
        request.add("lotSn", lotSn);
        request.add("productForm", productForm);
        request.add("authId", authId);
        request.add("accountId", accountId);
        Response<String> response= requestString(request);
        if(response.isSucceed()){
            String result=response.get();
            Gson gson = new Gson();
            ContractProInfo getBatchInfo = gson.fromJson(result, ContractProInfo.class);
            event.setSuccess(true);
            event.addReturnParam(getBatchInfo);
        }



    }
}
