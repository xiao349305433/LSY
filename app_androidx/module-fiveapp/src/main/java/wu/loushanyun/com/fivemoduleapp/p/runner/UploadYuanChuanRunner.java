package wu.loushanyun.com.fivemoduleapp.p.runner;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wu.loushanyun.base.url.URLUtils;
import com.wu.loushanyun.basemvp.m.SaveDataMeter;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import met.hx.com.base.base.nohttp.CommonRunner;
import met.hx.com.base.baseevent.Event;
import wu.loushanyun.com.libraryfive.m.InsideSaveThirdData;
import wu.loushanyun.com.libraryfive.m.SaveDataConverter;
import wu.loushanyun.com.libraryfive.m.YuanChuanSaveData;

public class UploadYuanChuanRunner extends FiveCommonRunner {
    @Override
    public void onEventRun(Event event) {
        YuanChuanSaveData yuanChuanSaveData = (YuanChuanSaveData) event.getParamAtIndex(0);
        Gson gson = new Gson();
        ArrayList<String> arrayListImage = gson.fromJson(yuanChuanSaveData.getJsonImage(), new TypeToken<ArrayList<String>>() {
        }.getType());
        ArrayList<SaveDataMeter> arrayListSaveDataMeter = gson.fromJson(yuanChuanSaveData.getJsonMeter(), new TypeToken<ArrayList<SaveDataMeter>>() {
        }.getType());

        SaveDataConverter saveDataConverter = gson.fromJson(yuanChuanSaveData.getJsonSaveDataConverter(), SaveDataConverter.class);

        InsideSaveThirdData insideSaveThirdData = new InsideSaveThirdData(yuanChuanSaveData, arrayListImage, saveDataConverter, arrayListSaveDataMeter);
        HashMap<String, Object> params = new HashMap();
        String json = gson.toJson(insideSaveThirdData);
        params.put("saveData", json);
        Request<String> request = NoHttp.createStringRequest(URLUtils.getIP() + URLUtils.BaseStationSheBeiInstall, RequestMethod.POST);
        request.add(params);
        Response<String> response = requestString(request);
        if (response.isSucceed()) {
            try {
                JSONObject jsonObject=new JSONObject(response.get());
                int code = jsonObject.optInt("code");
                event.addReturnParam(code);
                event.addReturnParam(jsonObject.opt("msg"));
                event.setSuccess(true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}
