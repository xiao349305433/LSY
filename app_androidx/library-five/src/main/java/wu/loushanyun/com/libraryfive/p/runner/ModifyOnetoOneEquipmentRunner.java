package wu.loushanyun.com.libraryfive.p.runner;

import com.wu.loushanyun.base.url.URLUtils;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONException;
import org.json.JSONObject;

import met.hx.com.base.base.nohttp.CommonRunner;
import met.hx.com.base.baseevent.Event;

public class ModifyOnetoOneEquipmentRunner extends CommonRunner{
    @Override
    public void onEventRun(Event event) {
        String oldSn = (String) event.getParamAtIndex(0);
        String newSnData = (String) event.getParamAtIndex(1);
        Request<String> request = NoHttp.createStringRequest(URLUtils.getIP() + URLUtils.MRepairModifyOnetoOneEquipment, RequestMethod.POST);
        request.add("oldSn", oldSn);
        request.add("newSnData", newSnData);
        Response<String> response = requestString(request);
        if (response.isSucceed()) {
            try {
                JSONObject jsonObject=new JSONObject(response.get());
                int code =jsonObject.optInt("code");
                String msg=jsonObject.optString("msg");
                event.addReturnParam(code);
                event.addReturnParam(msg);
                event.setSuccess(true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
