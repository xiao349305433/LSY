package wu.loushanyun.com.fivemoduleapp.p.runner;

import com.wu.loushanyun.base.url.URLUtils;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

import met.hx.com.base.base.nohttp.CommonRunner;
import met.hx.com.base.baseevent.Event;

public class CheckNoteCodeRunner extends CommonRunner{
    @Override
    public void onEventRun(Event event) {
        String code= (String) event.getParamAtIndex(0);
        Request<String> request= NoHttp.createStringRequest(URLUtils.getIP()+ URLUtils.FSCheckNoteCode, RequestMethod.GET);
        request.add("code",code);
        Response<String> response= requestString(request);
        if(response.isSucceed()){
            String str=response.get();
            if(str.equals("true")){
                event.setSuccess(true);
            }else if(str.equals("false")){
                event.setSuccess(false);
            }
        }
    }
}
