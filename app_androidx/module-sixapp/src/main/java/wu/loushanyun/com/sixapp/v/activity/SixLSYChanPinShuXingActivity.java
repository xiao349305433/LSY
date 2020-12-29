package wu.loushanyun.com.sixapp.v.activity;

import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.elvishew.xlog.XLog;
import com.flyco.roundview.RoundTextView;
import com.scwang.smartrefresh.layout.util.DensityUtil;
import com.sensoro.sensor.kit.entity.SensoroDevice;
import com.wu.loushanyun.base.config.K;
import com.wu.loushanyun.base.util.DataParser;
import com.wu.loushanyun.base.util.LouShanYunUtils;
import com.wu.loushanyun.base.util.MapParams;
import com.wu.loushanyun.basemvp.init.SnBlueToothListener;
import com.wu.loushanyun.basemvp.init.SnBlueToothTool;
import com.wu.loushanyun.basemvp.m.sat.SAtInstructRealizeFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.activity.BaseNoPresenterActivity;
import met.hx.com.base.base.multitype.MultiTypeAdapter;
import met.hx.com.base.baseevent.Event;
import met.hx.com.librarybase.some_utils.ToastUtils;
import met.hx.com.librarybase.some_utils.XHStringUtil;
import wu.loushanyun.com.sixapp.R;
import wu.loushanyun.com.sixapp.init.LoginParamManager;
import wu.loushanyun.com.sixapp.init.SixCode;
import wu.loushanyun.com.sixapp.init.SixUtils;
import wu.loushanyun.com.sixapp.m.EnvironmentInfInfo;
import wu.loushanyun.com.sixapp.m.GetComInfo;
import wu.loushanyun.com.sixapp.p.adapter.SNBlueViewBinder;
import wu.loushanyun.com.sixapp.p.adapter.ServiceViewBinder;
import wu.loushanyun.com.sixapp.p.runner.GetComRunner;

@Route(path = K.SixLSYChanPinShuXingActivity)
public class SixLSYChanPinShuXingActivity extends BaseNoPresenterActivity implements SnBlueToothListener {

    private Spinner tu3ServiceIdSelect;
    private EditText tu3EditDeviceId;
    private RoundTextView secondParameterRoundCreateId;
    private Spinner secondParameterCgxhSelect;
    private TextView tu3OneSetting;

    private StringBuilder stringBuilder = new StringBuilder();
    private EnvironmentInfInfo environmentInfInfo;
    protected SnBlueToothTool snBlueToothTool;
    private SensoroDevice sensoroDevice;
    private SAtInstructRealizeFactory sAtInstructRealizeFactory;
    private MultiTypeAdapter multiTypeAdapter;
    private ServiceViewBinder serviceViewBinder;
    private GetComInfo getComInfo;
    private PopupWindow myPop;

    //默认参数
    private byte chaiXie = 0;//拆卸   无
    private byte qiangCi = 1;//强磁   无
    private byte chuanGanQiZhuangTai = 0;//传感器状态   无
    private byte daoLiu = 1;//倒流   无
    private byte faMenZhuangTai = 0;//阀门状态   无
    private byte faSongPinLv = (byte) 0x30;//发送频率   默认24小时


    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        ba.mTitleText = "产品属性";
        ba.mTitleBackgroundColorId = R.color.blue_bg;
        ba.mActivityLayoutId = R.layout.m_six_activity_lsy_chanpinshuxing;
    }


    @Override
    protected void initLifeCycle() {
        super.initLifeCycle();
        snBlueToothTool = new SnBlueToothTool(this);
        registerLifeCycle(snBlueToothTool);

        serviceViewBinder=new ServiceViewBinder(new ServiceViewBinder.OnServiceListener() {
            @Override
            public void onService() {

            }
        });
        registerLifeCycle(serviceViewBinder);
    }


    @Override
    protected void initView() {
        tu3ServiceIdSelect = (Spinner) findViewById(R.id.tu3_service_id_select);
        tu3EditDeviceId = (EditText) findViewById(R.id.tu3_edit_device_id);
        secondParameterRoundCreateId = (RoundTextView) findViewById(R.id.second_parameter_round_create_id);
        secondParameterCgxhSelect = (Spinner) findViewById(R.id.second_parameter_cgxh_select);
        tu3OneSetting = (TextView) findViewById(R.id.tu3_one_setting);

        sAtInstructRealizeFactory = new SAtInstructRealizeFactory();
        environmentInfInfo = getIntent().getParcelableExtra("environmentInfInfo");
        sensoroDevice = getIntent().getParcelableExtra("sensoroDevice");
        snBlueToothTool.connectBlueTooth(sensoroDevice);
        pushEvent(SixCode.MSixGetCom, LoginParamManager.getInstance().getLoginInfo().getLoginId());
        tu3OneSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDefaultFactoryParams();
            }
        });
        secondParameterRoundCreateId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tu3EditDeviceId.setText(LouShanYunUtils.getTimeID());
            }
        });





//        tu3EditServiceId.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                String search = s.toString().toUpperCase();
//
//                if (!XHStringUtil.isEmpty(search, false)) {
//                    serviceViewBinder.setKeyWord(search);
//                } else {
//                    serviceViewBinder.setKeyWord(null);
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });


    }


    @Override
    public void onChildConnectFailed(int i) {

    }

    @Override
    public void onChildConnectSuccess() {
        snBlueToothTool.write(DataParser.CMD_METER_INFO, "读取参数中...", true, 2000);
    }

    @Override
    public void onChildNotify(byte[] result) {
        byte code = (byte) (result[2] ^ ((byte) 0x80));
        XLog.i("蓝牙code=" + code);
        switch (code) {
            case 0x20:
                if (result[3] == 0) {
                    setDefaultParams();
                }
                break;
            case 0x21:
                if (result[3] == 0) {
                    sendMessageToast("设置成功");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            SixFixActivity.Fix_Tu3=true;
                            //配置完后再次读取数据
                            snBlueToothTool.write(DataParser.CMD_METER_INFO, "读取参数中...", true, 2000);
                            tu3EditDeviceId.setText(LouShanYunUtils.getTimeID());

                        }
                    });
                } else {
                    sendMessageToast("设置失败");
                }
                break;
            case 0x22:
                SixUtils.SecondHashMap.putAll(DataParser.getMoudleNo2(result));
                setDuQu(SixUtils.SecondHashMap);
                break;

        }
    }

    @Override
    public void onChildWriteSuccess() {

    }

    @Override
    public void onChildWriteFailure(int i) {

    }



    @Override
    protected void initEventListener() {
        registerEventRunner(SixCode.MSixGetCom, new GetComRunner());
    }

    @Override
    protected void onEventRunEnd(Event event, int code) {
        if (code == SixCode.MSixGetCom) {
             getComInfo = (GetComInfo) event.getReturnParamAtIndex(0);
            int resultcode = getComInfo.getCode();
            if (resultcode == 0) {

                ArrayList<String> arrayList=new ArrayList<>();
                for (int i = 0; i <getComInfo.getDatas().size() ; i++) {
                          arrayList.add(getComInfo.getDatas().get(i).getBusinessName());
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.l_loushanyun_item_spinner, arrayList);
                tu3ServiceIdSelect.setAdapter(arrayAdapter);

            } else if (resultcode == 1) {
                sendMessageToast(getComInfo.getMessage(),true);
            }

        }
    }



    private void setDefaultFactoryParams() {
        snBlueToothTool.write(DataParser.getFactoryParams(
                secondParameterCgxhSelect.getSelectedItem().toString().trim(), chaiXie, qiangCi, chuanGanQiZhuangTai, daoLiu, faMenZhuangTai, faSongPinLv, Integer.valueOf( getComInfo.getDatas().get(tu3ServiceIdSelect.getSelectedItemPosition()).getSubAccountId()),
                "0", "0"
        ), "设置1参数中...");
    }

    private void setDefaultParams() {
        String id = tu3EditDeviceId.getText().toString().trim();
        byte[] input2 = DataParser.getBiaoXinxiChuShiHuaCMD(
                Long.parseLong(id),
                Long.parseLong(SixUtils.SecondHashMap.get(MapParams.当前脉冲读数)),
                String.valueOf(SixUtils.SecondHashMap.get(MapParams.倍率)));
        snBlueToothTool.write(input2, "设置2参数中...");
    }



    private void setDuQu(HashMap<String, String> hashMap) {
        StringBuilder sb = new StringBuilder();
        sb.append("设备ID:  ");
        sb.append(hashMap.get(MapParams.设备ID));
        sb.append("\n脉冲常数(个/m³):  ");
        BigDecimal decimal = new BigDecimal(LouShanYunUtils.getBLReadStringByCode(Integer.parseInt(hashMap.get(MapParams.倍率))));
        sb.append(new BigDecimal(1).divide(decimal).stripTrailingZeros().toPlainString());
        sb.append("\n倍率(m³/ev):  ");
        sb.append(decimal.stripTrailingZeros().toPlainString());
        sb.append("\n脉冲数(个):  ");
        sb.append(hashMap.get(MapParams.当前脉冲读数));
        sb.append("\n初始读数(m³):  ");
        sb.append(new BigDecimal(hashMap.get(MapParams.当前脉冲读数)).multiply(decimal).stripTrailingZeros().toPlainString());
        sb.append("\n发送频率:  ");
        sb.append(LouShanYunUtils.getFSPLReadStringByCode(Long.valueOf(hashMap.get(MapParams.发送频率))));
        sb.append("\n传感信号:  ");
        sb.append(LouShanYunUtils.getCGXHReadStringByCode(Long.valueOf(hashMap.get(MapParams.传感信号))));
        sb.append("\n厂家标识:  ");
        sb.append(hashMap.get(MapParams.厂家标识));
        sb.append("\n硬件版本:  ");
        sb.append(LouShanYunUtils.getHardWareVersion(Integer.valueOf(hashMap.get(MapParams.硬件版本))));
        sb.append("\n软件版本:  ");
        sb.append(LouShanYunUtils.getSoftWareVersion(Integer.valueOf(hashMap.get(MapParams.软件版本))));
        sb.append("\n表拆卸状态:  ");
        sb.append(hashMap.get(MapParams.表拆卸状态).equals("0") ? "无" : "有");
        sb.append("\n表强磁状态:  ");
        sb.append(hashMap.get(MapParams.表强磁状态).equals("0") ? "正常" : "强磁");
        sb.append("\n表流向状态:  ");
        sb.append(hashMap.get(MapParams.表流向状态).equals("0") ? "正流" : "倒流");
        sb.append("\n表电池状态:  ");
        sb.append(hashMap.get(MapParams.表电池状态).equals("0") ? "正常" : "欠压");
        runOnUiThread(() -> {

                if (!XHStringUtil.isEmpty(hashMap.get(MapParams.传感信号), false)) {
                    if ("3".equals(hashMap.get(MapParams.传感信号))) {
                        secondParameterCgxhSelect.setSelection(0);
                    } else if ("1".equals(hashMap.get(MapParams.传感信号))) {
                        secondParameterCgxhSelect.setSelection(1);
                    } else if ("10".equals(hashMap.get(MapParams.传感信号))) {
                        secondParameterCgxhSelect.setSelection(2);
                    }
                }

                tu3EditDeviceId.setText(hashMap.get(MapParams.设备ID));
               // tu3EditServiceId.setText(hashMap.get(MapParams.厂家标识));

        });
    }
}
