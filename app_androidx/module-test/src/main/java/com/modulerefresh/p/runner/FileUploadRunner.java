package com.modulerefresh.p.runner;

import com.elvishew.xlog.XLog;
import com.google.gson.Gson;
import com.wu.loushanyun.base.url.URLUtils;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.CacheMode;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import met.hx.com.base.base.nohttp.CommonRunner;
import met.hx.com.base.baseevent.Event;
import met.hx.com.librarybase.some_utils.FileUtils;

import static android.provider.ContactsContract.StatusUpdates.CONTENT_TYPE;

/**
 * PHP平台登陆
 */
public class FileUploadRunner extends CommonRunner {

    @Override
    public void onEventRun(Event event) {
        String path = (String) event.getParamAtIndex(0);
        XLog.i(path);
        File file = FileUtils.getFileByPath(path);
//        String url="http://139.9.6.118/upload";
        String url="http://www.loushancloud.com/upload";
        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.POST);

        String time = LoraUtil.getX_NONCE();
        String content = time+"POST" + url + "{}";

        byte[] SIGNATURE = LoraUtil.hmacsha256(content.getBytes(),"cbeeca023a2182d7a57220f968974a14c380dd4e5541e930789d2a22e49f04d2".getBytes());
        byte[] base64 = Base64.encodeBase64(SIGNATURE);
        String base64Str = new String(base64);
        request.addHeader("LSY-ACCESS-ID", "6bfa142590a0a1d49541522dabe58076");
        request.addHeader("LSY-ACCESS-DATA", time);
        request.addHeader("LSY-ACCESS-SIGNATURE", base64Str);
        request.add("file", file);
        request.add("pathType", 1);
        request.add("fileName", "sdadsadsa");

        Response<String> response = requestString(request);
        if (response.isSucceed()) {
            event.addReturnParam(response.get());
            event.setSuccess(true);
        }
    }


}
