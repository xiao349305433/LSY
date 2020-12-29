package wu.loushanyun.com.moduletwoinit.p.runner;

import com.google.gson.Gson;
import com.wu.loushanyun.base.url.URLUtils;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

import met.hx.com.base.base.nohttp.CommonRunner;
import met.hx.com.base.baseevent.Event;
import met.hx.com.librarybase.some_utils.TimeUtils;
import wu.loushanyun.com.moduletwoinit.m.IotInfo;
import wu.loushanyun.com.moduletwoinit.m.OnetoOneConverter;

public class GetIotInfoRunner extends CommonRunner{
    /**
     * disassemblyState : 0
     * joinForm : 2
     * pulseConstant : 100
     * caliber : 40
     * impulseInitial : 66
     * channel : 模式A
     * remark : JJ破记录了
     * frequency : 72小时
     * sensingSignal : 2EV
     * initialData : 0.66
     * manufacturersName : 21
     * sf : SF9
     * rate : 0.01
     * backflowState : 0
     * imageUrl : http://www.loushanyun.net/static/LouShanCloudAllImage/123456789456414/locationImages/2018year/07month/11day/102d1404-39a9-437a-8424-4dbd2602a7f1.png
     * magneticInterferenceState : 0
     * productionTime : 1530547200000
     * sensorState : 0
     */
    @Override
    public void onEventRun(Event event) {
        String sn = (String) event.getParamAtIndex(0);
        Request<String> request = NoHttp.createStringRequest(URLUtils.getIP() + URLUtils.MInitTwogetIotInfo, RequestMethod.GET);
        request.add("sn", sn);
        Response<String> response = requestString(request);
        if (response.isSucceed()) {
            Gson gson = new Gson();
            IotInfo areaInfo = gson.fromJson(response.get(), IotInfo.class);
            OnetoOneConverter onetoOneConverter=new OnetoOneConverter();
            onetoOneConverter.setSn(sn);
            onetoOneConverter.setDisassemblyState(areaInfo.getData().getDisassemblyState()+"");
            onetoOneConverter.setJoinForm(areaInfo.getData().getJoinForm()+"");
            onetoOneConverter.setPulseConstant(areaInfo.getData().getPulseConstant()+"");
            onetoOneConverter.setCaliber(areaInfo.getData().getCaliber()+"");
            onetoOneConverter.setImpulseInitial(areaInfo.getData().getImpulseInitial()+"");
            onetoOneConverter.setChannel(areaInfo.getData().getChannel()+"");
            onetoOneConverter.setRemark(areaInfo.getData().getRemark()+"");
            onetoOneConverter.setSensingSignal(areaInfo.getData().getSensingSignal()+"");
            onetoOneConverter.setFrequency(areaInfo.getData().getFrequency()+"");
            onetoOneConverter.setSensingSignal(areaInfo.getData().getSensingSignal()+"");
            onetoOneConverter.setManufacturersIdentification(areaInfo.getData().getManufacturersName()+"");
            onetoOneConverter.setSf(areaInfo.getData().getSf()+"");
            onetoOneConverter.setFlowDirection(areaInfo.getData().getBackflowState()+"");
            onetoOneConverter.setImagePath(areaInfo.getData().getImageUrl()+"");
            onetoOneConverter.setMagneticDisturb(areaInfo.getData().getMagneticInterferenceState()+"");
            onetoOneConverter.setEquipmentTime(TimeUtils.milliseconds2String(areaInfo.getData().getProductionTime()));
            onetoOneConverter.setSensorState(areaInfo.getData().getSensorState()+"");
            event.addReturnParam(areaInfo.getCode());
            event.addReturnParam(onetoOneConverter);
            event.setSuccess(true);
        }
    }
}
