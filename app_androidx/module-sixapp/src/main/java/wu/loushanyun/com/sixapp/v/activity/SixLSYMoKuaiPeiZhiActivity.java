package wu.loushanyun.com.sixapp.v.activity;

import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.elvishew.xlog.XLog;
import com.sensoro.sensor.kit.entity.SensoroDevice;
import com.wu.loushanyun.base.config.K;
import com.wu.loushanyun.base.util.DataParser;
import com.wu.loushanyun.base.util.LorawanUtils;
import com.wu.loushanyun.base.util.LouShanYunUtils;
import com.wu.loushanyun.base.util.MapParams;
import com.wu.loushanyun.basemvp.init.SnBlueToothListener;
import com.wu.loushanyun.basemvp.init.SnBlueToothTool;
import com.wu.loushanyun.basemvp.m.sat.SAtInstructRealizeFactory;

import java.math.BigDecimal;
import java.util.HashMap;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.activity.BaseNoPresenterActivity;
import met.hx.com.base.baseevent.Event;
import met.hx.com.librarybase.some_utils.ToastUtils;
import met.hx.com.librarybase.some_utils.XHStringUtil;
import wu.loushanyun.com.sixapp.R;
import wu.loushanyun.com.sixapp.init.SixUtils;
import wu.loushanyun.com.sixapp.m.EnvironmentInfInfo;

@Route(path = K.SixLSYMoKuaiPeiZhiActivity)
public class SixLSYMoKuaiPeiZhiActivity extends BaseNoPresenterActivity implements SnBlueToothListener {


    private EditText tu2EditSoftWareVersion;
    private EditText tu2EditHardWareVersion;

    private StringBuilder stringBuilder = new StringBuilder();
    private EnvironmentInfInfo environmentInfInfo;
    protected SnBlueToothTool snBlueToothTool;
    private SensoroDevice sensoroDevice;
    private SAtInstructRealizeFactory sAtInstructRealizeFactory;


    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        ba.mTitleText = "模块配置";
        ba.mTitleBackgroundColorId = R.color.blue_bg;
        ba.mActivityLayoutId = R.layout.m_six_activity_lsy_mokuaipeizhi;
    }

    @Override
    protected void initLifeCycle() {
        super.initLifeCycle();
        snBlueToothTool = new SnBlueToothTool(this);
        registerLifeCycle(snBlueToothTool);
    }

    @Override
    protected void initView() {

        tu2EditSoftWareVersion = (EditText) findViewById(R.id.tu2_edit_SoftWareVersion);
        tu2EditHardWareVersion = (EditText) findViewById(R.id.tu2_edit_HardWareVersion);
        sAtInstructRealizeFactory = new SAtInstructRealizeFactory();
        environmentInfInfo = getIntent().getParcelableExtra("environmentInfInfo");
        sensoroDevice = getIntent().getParcelableExtra("sensoroDevice");
      //  snBlueToothTool.connectBlueTooth(sensoroDevice);

        tu2EditSoftWareVersion.setText(LouShanYunUtils.getHardWareVersion(Integer.valueOf(SixUtils.SecondHashMap.get(MapParams.硬件版本))));
        tu2EditHardWareVersion.setText(LouShanYunUtils.getSoftWareVersion(Integer.valueOf(SixUtils.SecondHashMap.get(MapParams.软件版本))));
        SixFixActivity.Fix_Tu2=true;
//        tu2BeilvSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                try {
//                    if (!XHStringUtil.isEmpty(tu2EditMaichonggeshu.getText().toString(), false)) {
//                        long maiChongDishu = Long.valueOf(tu2EditMaichonggeshu.getText().toString());
//                        String beiLv = (String) tu2BeilvSelect.getSelectedItem();
//                        BigDecimal a1 = new BigDecimal(beiLv);
//                        BigDecimal b1 = new BigDecimal(maiChongDishu);
////                        double result1 = a1.multiply(b1).doubleValue();// 相乘结果
//                        tu2EditMaichongdushu.setText(a1.multiply(b1).stripTrailingZeros().toPlainString() + "m³");
//                    } else {
//                        tu2EditMaichongdushu.setText("");
//                    }
//                } catch (Exception e) {
//                    tu2EditMaichongdushu.setText("");
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//
//        tu2EditMaichonggeshu.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                try {
//                    if (!XHStringUtil.isEmpty(s.toString(), false)) {
//                        long maiChongDishu = Long.valueOf(s.toString());
//                        String beiLv = (String) tu2BeilvSelect.getSelectedItem();
//                        BigDecimal a1 = new BigDecimal(beiLv);
//                        BigDecimal b1 = new BigDecimal(maiChongDishu);
////                        double result1 = a1.multiply(b1).doubleValue();// 相乘结果
//                        tu2EditMaichongdushu.setText(a1.multiply(b1).stripTrailingZeros().toPlainString() + "m³");
//                    } else {
//                        tu2EditMaichongdushu.setText("");
//                    }
//                } catch (Exception e) {
//                    tu2EditMaichongdushu.setText("");
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//
//
//
//        tu2OneSetting.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                setDefaultParams();
//            }
//        });

    }


    @Override
    public void onChildConnectFailed(int i) {

    }

    @Override
    public void onChildConnectSuccess() {
        snBlueToothTool.write(DataParser.CMD_METER_INFO, "读取中...", true, 6000);
    }


    @Override
    public void onChildNotify(byte[] result) {
        byte code = (byte) (result[2] ^ ((byte) 0x80));
        XLog.i("蓝牙code=" + code);
        switch (code) {

            case 0x20:
                if (result[3] == 0) {
                  //  setDefaultParams();
                }
                break;
            case 0x21:
                if (result[3] == 0) {
                    sendMessageToast("设置成功");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            snBlueToothTool.write(DataParser.CMD_METER_INFO, "读取中...", true, 6000);
                        }
                    });
                } else {
                    sendMessageToast("设置失败");
                }
                break;
            case 0x22:
                SixUtils.SecondHashMap.putAll(DataParser.getMoudleNo2(result));
             //   setDuQu(SixUtils.SecondHashMap);
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

    }

    @Override
    protected void onEventRunEnd(Event event, int code) {

    }





//    private void setDefaultParams() {
//        String id = tu2EditMaichonggeshu.getText().toString().trim();
//        byte[] input2 = DataParser.getBiaoXinxiChuShiHuaCMD(
//                Long.parseLong(id),
//                Long.parseLong(String.valueOf(tu2EditMaichonggeshu.getText())),
//                String.valueOf(tu2BeilvSelect.getSelectedItem()));
//        snBlueToothTool.write(input2, "设置2参数中...");
//    }


//    private void setDuQu(HashMap<String, String> hashMap) {
//        StringBuilder sb = new StringBuilder();
//        sb.append("设备ID:  ");
//        sb.append(hashMap.get(MapParams.设备ID));
//        sb.append("\n脉冲常数(个/m³):  ");
//        BigDecimal decimal = new BigDecimal(LouShanYunUtils.getBLReadStringByCode(Integer.parseInt(hashMap.get(MapParams.倍率))));
//        sb.append(new BigDecimal(1).divide(decimal).stripTrailingZeros().toPlainString());
//        sb.append("\n倍率(m³/ev):  ");
//        sb.append(decimal.stripTrailingZeros().toPlainString());
//        sb.append("\n脉冲数(个):  ");
//        sb.append(hashMap.get(MapParams.当前脉冲读数));
//        sb.append("\n初始读数(m³):  ");
//       String dushu= new BigDecimal(hashMap.get(MapParams.当前脉冲读数)).multiply(decimal).stripTrailingZeros().toPlainString();
//        sb.append(dushu);
//        sb.append("\n发送频率:  ");
//        sb.append(LouShanYunUtils.getFSPLReadStringByCode(Long.valueOf(hashMap.get(MapParams.发送频率))));
//        sb.append("\n传感信号:  ");
//        sb.append(LouShanYunUtils.getCGXHReadStringByCode(Long.valueOf(hashMap.get(MapParams.传感信号))));
//        sb.append("\n厂家标识:  ");
//        sb.append(hashMap.get(MapParams.厂家标识));
//        sb.append("\n硬件版本:  ");
//        sb.append(LouShanYunUtils.getHardWareVersion(Integer.valueOf(hashMap.get(MapParams.硬件版本))));
//        sb.append("\n软件版本:  ");
//        sb.append(LouShanYunUtils.getSoftWareVersion(Integer.valueOf(hashMap.get(MapParams.软件版本))));
//        sb.append("\n表拆卸状态:  ");
//        sb.append(hashMap.get(MapParams.表拆卸状态).equals("0") ? "无" : "有");
//        sb.append("\n表强磁状态:  ");
//        sb.append(hashMap.get(MapParams.表强磁状态).equals("0") ? "正常" : "强磁");
//        sb.append("\n表流向状态:  ");
//        sb.append(hashMap.get(MapParams.表流向状态).equals("0") ? "正流" : "倒流");
//        sb.append("\n表电池状态:  ");
//        sb.append(hashMap.get(MapParams.表电池状态).equals("0") ? "正常" : "欠压");
//        runOnUiThread(() -> {
//            tu2EditDianchizhuangtai.setText(hashMap.get(MapParams.表电池状态).equals("0") ? "正常" : "欠压");
//
//            tu2EditMaichongdushu.setText(new BigDecimal(hashMap.get(MapParams.当前脉冲读数)).multiply(decimal).stripTrailingZeros().toPlainString());
//            tu2EditMaichonggeshu.setText(hashMap.get(MapParams.当前脉冲读数));
//                if (!XHStringUtil.isEmpty(hashMap.get(MapParams.倍率), false)) {
//                    if ("0".equals(hashMap.get(MapParams.倍率))) {
//                        tu2BeilvSelect.setSelection(0);
//                    } else if ("1".equals(hashMap.get(MapParams.倍率))) {
//                        tu2BeilvSelect.setSelection(1);
//                    } else if ("2".equals(hashMap.get(MapParams.倍率))) {
//                        tu2BeilvSelect.setSelection(2);
//                    } else if ("3".equals(hashMap.get(MapParams.倍率))) {
//                        tu2BeilvSelect.setSelection(3);
//                    } else if ("4".equals(hashMap.get(MapParams.倍率))) {
//                        tu2BeilvSelect.setSelection(4);
//                    } else if ("5".equals(hashMap.get(MapParams.倍率))) {
//                        tu2BeilvSelect.setSelection(5);
//                    }
//                }
//        });
//    }

}
