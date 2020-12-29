package wu.loushanyun.com.modulechiptest.p.runner;

import com.wu.loushanyun.base.url.URLUtils;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

import met.hx.com.base.base.nohttp.CommonRunner;
import met.hx.com.base.baseevent.Event;

public class SendNoteCodeRunner extends CommonRunner {
    @Override
    public void onEventRun(Event event) {
        String number = (String) event.getParamAtIndex(0);
        Request<String> request = NoHttp.createStringRequest(URLUtils.getIP() + URLUtils.FSSendNoteCode, RequestMethod.GET);
        request.add("mobile", number);
        Response<String> response = requestString(request);
        if (response.isSucceed()) {
            String str = response.get();
            if (str.equals("true")) {
                event.setSuccess(true);
            } else if (str.equals("false")) {
                event.setSuccess(false);
            }

        }
    }
}
