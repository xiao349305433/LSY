package wu.loushanyun.com.sixapp.v.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.elvishew.xlog.XLog;
import com.sensoro.sensor.kit.entity.SensoroDevice;
import com.wu.loushanyun.base.config.K;
import com.wu.loushanyun.base.util.LorawanUtils;
import com.wu.loushanyun.base.util.MapParams;
import com.wu.loushanyun.basemvp.init.SnBlueToothListener;
import com.wu.loushanyun.basemvp.init.SnBlueToothTool;
import com.wu.loushanyun.basemvp.m.sat.SAtInstructRealizeFactory;
import com.wu.loushanyun.basemvp.m.sat.util.SAtInstructParams;

import java.util.HashMap;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.activity.BaseNoPresenterActivity;
import met.hx.com.base.baseevent.Event;
import met.hx.com.librarybase.some_utils.ByteConvertUtils;
import met.hx.com.librarybase.some_utils.XHStringUtil;
import wu.loushanyun.com.sixapp.R;
import wu.loushanyun.com.sixapp.init.SixUtils;
import wu.loushanyun.com.sixapp.m.EnvironmentInfInfo;


@Route(path = K.SixChipMoKuaiPeiZhiActivity)
public class SixChipMoKuaiPeiZhiActivity   extends BaseNoPresenterActivity implements SnBlueToothListener {


//    private Spinner tu2BeilvSelect;
//    private EditText tu2EditPositiveReading;
//    private EditText tu2EditCounterReading;
//    private EditText tu2EditAllstate;
//    private EditText tu2EditLasttime;
//    private EditText tu2EditDianyuanType;
//    private EditText tu2EditDianya;
    private EditText tu2EditSoftWareVersion;
    private TextView tu3OneSetting;

    protected SnBlueToothTool snBlueToothTool;
    private SensoroDevice sensoroDevice;


    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        ba.mTitleText = "模块配置";
        ba.mTitleBackgroundColorId = R.color.blue_bg;
        ba.mActivityLayoutId = R.layout.m_six_activity_chip_mokuaipeizhi;
    }


    @Override
    protected void initLifeCycle() {
        super.initLifeCycle();
        snBlueToothTool = new SnBlueToothTool(this);
        registerLifeCycle(snBlueToothTool);
    }

    @Override
    protected void initView() {
//
//        tu2BeilvSelect = (Spinner) findViewById(R.id.tu2_beilv_select);
//        tu2EditPositiveReading = (EditText) findViewById(R.id.tu2_edit_positive_reading);
//        tu2EditCounterReading = (EditText) findViewById(R.id.tu2_edit_Counter_reading);
//        tu2EditAllstate = (EditText) findViewById(R.id.tu2_edit_allstate);
//        tu2EditLasttime = (EditText) findViewById(R.id.tu2_edit_lasttime);
//        tu2EditDianyuanType = (EditText) findViewById(R.id.tu2_edit_dianyuan_type);
//        tu2EditDianya = (EditText) findViewById(R.id.tu2_edit_dianya);
        tu2EditSoftWareVersion = (EditText) findViewById(R.id.tu2_edit_SoftWareVersion);
        tu3OneSetting = (TextView) findViewById(R.id.tu3_one_setting);

        sensoroDevice = getIntent().getParcelableExtra("sensoroDevice");
        snBlueToothTool.connectBlueTooth(sensoroDevice);

        tu2EditSoftWareVersion.setText(SixUtils.LorawanHashMap.get(MapParams.产品固件版本号));
        tu3OneSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snBlueToothTool.write(LorawanUtils.getjiaozhun(), "正在校准模组时间");
              //  snBlueToothTool.write(LorawanUtils.getChuChangWrite(SixUtils.LorawanHashMap.get(MapParams.脉冲底数),LorawanUtils.getBiaoBeilvCodeByString(tu2BeilvSelect.getSelectedItem().toString()) ,SixUtils.LorawanHashMap.get(MapParams.传感信号), "一对一",Integer.valueOf(SixUtils.LorawanHashMap.get(MapParams.服务商ID)), Integer.valueOf(SixUtils.LorawanHashMap.get(MapParams.制造商ID)), SixUtils.LorawanHashMap.get(MapParams.机芯类型)), "正在设置出厂设置");
            }
        });
    }


    @Override
    public void onChildConnectFailed(int i) {

    }

    @Override
    public void onChildConnectSuccess() {
        try {
         //   snBlueToothTool.write(LorawanUtils.getChuChangReading(), "正在读取出厂设置");
        } catch (Exception e) {
            e.printStackTrace();
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


    @Override
    public void onChildNotify(byte[] result) {
        byte code = (byte) (result[2] ^ ((byte) 0x80));
        XLog.i("蓝牙code=" + code);
        switch (code) {


            case 0x05:
                if (result[3] == 0) {
                    sendMessageToast("校准时间成功");
                    SixFixActivity.Fix_Tu2=true;
                    try {
                      //  snBlueToothTool.write(LorawanUtils.getSingleReading(), "正在读取表信息");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    sendMessageToast("校准时间失败");
                }
                break;


            case 0x07:
                if (result[3] == 0) {
                    sendMessageToast("读取电池电压成功");
                    StringBuffer stringBuffer = new StringBuffer();
                    StringBuffer stringBuffer2 = new StringBuffer();
                    for (int i = 3; i >= 0; i--) {
                        stringBuffer2.append(Integer.toHexString(result[i + 4]));
                        stringBuffer.append(ByteConvertUtils.conver2BinaryStr(result[i + 4]));

                    }
                    XLog.i("stringBuffer2===" + stringBuffer2.toString());
                    XLog.i("stringBuffer2Int=====" + Integer.valueOf(stringBuffer.toString()));
                    int zhi = Integer.parseInt(stringBuffer2.toString(), 16);
                    SixUtils.LorawanHashMap.put(MapParams.电池电压, String.valueOf((float) zhi / 100));

                    snBlueToothTool.write(LorawanUtils.getLastTimeReading(), "正在读取最后上送时间");


                } else if (result[3] == 1) {
                    sendMessageToast("读取电池电压失败");
                } else if (result[3] == 2) {
                    sendMessageToast("无效命令字");
                } else if (result[3] == 3) {
                    sendMessageToast("无效参数");
                }
                break;


            case 0x12:
                if (result[3] == 0) {
                    sendMessageToast("设置出厂设置成功");
                    SixFixActivity.Fix_Tu2=true;
                } else if (result[3] == 1) {
                    sendMessageToast("设置出厂设置失败");
                } else if (result[3] == 2) {
                    sendMessageToast("无效命令字");
                } else if (result[3] == 3) {
                    sendMessageToast("无效参数");
                }
                break;
            case 0x13:
                if (result[3] == 0) {
                    sendMessageToast("读取版本号成功");
                    SixUtils.LorawanHashMap.put(MapParams.产品固件版本号, String.valueOf(result[4]));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tu2EditSoftWareVersion.setText(SixUtils.LorawanHashMap.get(MapParams.产品固件版本号));
                        }
                    });

                } else if (result[3] == 1) {
                    sendMessageToast("读取版本号失败");
                } else if (result[3] == 2) {
                    sendMessageToast("无效命令字");
                } else if (result[3] == 3) {
                    sendMessageToast("无效参数");
                }
                break;
            case 0x14:
                if (result[3] == 0) {
                    sendMessageToast("读取最后上送时间成功");
                    SixUtils.LorawanHashMap.put(MapParams.数据性质, LorawanUtils.getShuJuTypeByCode(String.valueOf(result[4])));

                    long tmp;
                    tmp = 0;
                    for (int i = 6; i > 0; i--) {
                        tmp = (tmp * 256) + (result[i + 4] & 0xff);
                    }
                    SixUtils.LorawanHashMap.put(MapParams.最后一次采样时间, String.valueOf(tmp / 1000));
                    snBlueToothTool.write(LorawanUtils.getSingleReading(), "正在读取表信息");
                } else if (result[3] == 1) {
                    sendMessageToast("读取最后上送时间失败");
                } else if (result[3] == 2) {
                    sendMessageToast("无效命令字");
                } else if (result[3] == 3) {
                    sendMessageToast("无效参数");
                }
                break;

            case 0x16:
                if (result[3] == 0) {
                    sendMessageToast("读取出厂设置成功");
                    try {
                        SixUtils.LorawanHashMap.putAll(LorawanUtils.getReadChuChangSetting(result));
                        setChuChangText(SixUtils.LorawanHashMap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else if (result[3] == 1) {
                    sendMessageToast("读取出厂设置失败");
                } else if (result[3] == 2) {
                    sendMessageToast("无效命令字");
                } else if (result[3] == 3) {
                    sendMessageToast("无效参数");
                }
                break;

            case 0x22:
                if (result[3] == 0) {
                    try {
                        SixUtils.LorawanHashMap.putAll(LorawanUtils.getLorawanSingle(result));
                        setSingleText(SixUtils.LorawanHashMap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else if (result[3] == 1) {
                    sendMessageToast("读取失败");
                }
                break;
            case 0x23:
                if (result[3] == 0) {
                    sendMessageToast("操作成功");
                    SixUtils.LorawanHashMap.put(MapParams.阀门状态, String.valueOf(result[4] & 0xff) == "0" ? "开" : "关");
                    //    mTextValveInfo.setText(hashMap.get(MapParams.阀门状态));

                } else if (result[3] == 1) {
                    sendMessageToast("操作失败");
                } else if (result[3] == 2) {
                    sendMessageToast("无效命令字");
                } else if (result[3] == 3) {
                    sendMessageToast("无效参数");
                } else if (result[3] == 4) {
                    sendMessageToast("没有阀门，无法配置");
                }
                break;


        }
    }


    private void setChuChangText(HashMap<String, String> hashMap) {
        StringBuilder sb = new StringBuilder();
        sb.append("脉冲底数:  ");
        sb.append(hashMap.get(MapParams.脉冲底数));
        sb.append("\n倍率:  ");
        sb.append(hashMap.get(MapParams.倍率));
        sb.append("\n传感信号:  ");
        sb.append(hashMap.get(MapParams.传感信号));
        sb.append("\n接入类型:  ");
        sb.append(hashMap.get(MapParams.接入类型));
        sb.append("\n服务商ID:  ");
        sb.append(hashMap.get(MapParams.服务商ID));
        sb.append("\n制造商ID:  ");
        sb.append(hashMap.get(MapParams.制造商ID));
        sb.append("\n机芯:  ");
        sb.append(hashMap.get(MapParams.机芯类型));
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                snBlueToothTool.write(LorawanUtils.getDianchiReading(), "正在读取电池电压");

                //   mTextChuchuangInfo.setText(sb.toString());
            }
        });
    }

    private void setSingleText(HashMap<String, String> hashMap) {
        StringBuilder sb = new StringBuilder();
        sb.append("复合电容:  ");
        sb.append(LorawanUtils.getFuHedianrongStringByCode(hashMap.get(MapParams.复合电容)));
        sb.append("\n欠压状态:  ");
        sb.append(LorawanUtils.getQianYaStateStringByCode(hashMap.get(MapParams.欠压状态)));
        sb.append("\n流向状态:  ");
        sb.append(LorawanUtils.getLiuXiangstateStringByCode(hashMap.get(MapParams.流向状态)));
        sb.append("\n拆卸状态:  ");
        sb.append(LorawanUtils.getCaiXieStateStringByCode(hashMap.get(MapParams.拆卸状态)));
        sb.append("\n阀门有无:  ");
        sb.append(LorawanUtils.getValveStringByCode(hashMap.get(MapParams.阀门有无)));
        sb.append("\n阀门状态:  ");
        sb.append(LorawanUtils.getValvestateStringByCode(hashMap.get(MapParams.阀门状态)));
        sb.append("\n磁干扰:  ");
        sb.append(LorawanUtils.getCiStateStringByCode(hashMap.get(MapParams.磁干扰)));
        sb.append("\n模拟信号:  ");
        sb.append(LorawanUtils.getXinHaoStateStringByCode(hashMap.get(MapParams.模拟信号)));
        sb.append("\n机芯类型:  ");
        sb.append(LorawanUtils.getJiXinStringByCode(hashMap.get(MapParams.机芯类型)));
        sb.append("\n最后计数时间:  ");
        sb.append(LorawanUtils.getLastTimeStringByCode(hashMap.get(MapParams.最后计数时间)));
        sb.append("\n正计数:  ");
        sb.append(hashMap.get(MapParams.正计数));
        sb.append("\n反计数:  ");
        sb.append(hashMap.get(MapParams.反计数));
        sb.append("\n倍率:  ");//TODO 如是数字信号，配置小数点位 这里还未完善
        sb.append(LorawanUtils.getBiaoBeilvStringByCode(hashMap.get(MapParams.倍率)));
        sb.append("\n预警电压值:  ");
        sb.append(hashMap.get(MapParams.预警电压值));


        if (hashMap.get(MapParams.模拟信号).equals("1")) {
            sb.append("\n六点的读数:  ");
            sb.append(hashMap.get(MapParams.六点的读数));
            sb.append("\n十二点的读数:  ");
            sb.append(hashMap.get(MapParams.十二点的读数));
            sb.append("\n十八点的读数:  ");
            sb.append(hashMap.get(MapParams.十八点的读数));
            sb.append("\n二十四点的读数:  ");
            sb.append(hashMap.get(MapParams.二十四点的读数));
        } else {
            sb.append("\n零点至三点的脉冲数:  ");
            sb.append(hashMap.get(MapParams.零点至三点的脉冲数));
            sb.append("\n三点至六点的脉冲数:  ");
            sb.append(hashMap.get(MapParams.三点至六点的脉冲数));
            sb.append("\n六点至九点的脉冲数:  ");
            sb.append(hashMap.get(MapParams.六点至九点的脉冲数));
            sb.append("\n九点至十二点的脉冲数:  ");
            sb.append(hashMap.get(MapParams.九点至十二点的脉冲数));
            sb.append("\n十二点至十五点的脉冲数:  ");
            sb.append(hashMap.get(MapParams.十二点至十五点的脉冲数));
            sb.append("\n十五点至十八点的脉冲数:  ");
            sb.append(hashMap.get(MapParams.十五点至十八点的脉冲数));
            sb.append("\n十八点至二十一点的脉冲数:  ");
            sb.append(hashMap.get(MapParams.十八点至二十一点的脉冲数));
            sb.append("\n二十一点至二十四点的脉冲数:  ");
            sb.append(hashMap.get(MapParams.二十一点至二十四点的脉冲数));
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

//                if (!XHStringUtil.isEmpty(hashMap.get(MapParams.倍率), false)) {
//                    tu2BeilvSelect.setSelection(Integer.valueOf(hashMap.get(MapParams.倍率)));
//                }
//
//                if(!XHStringUtil.isEmpty(hashMap.get(MapParams.电源类型), false)){
//                    tu2EditDianyuanType.setText(LorawanUtils.getDianYuanTypeStringByCode(hashMap.get(MapParams.电源类型)));
//                }
//
//
//                if(!XHStringUtil.isEmpty(hashMap.get(MapParams.正计数), false)){
//                    tu2EditCounterReading.setText(hashMap.get(MapParams.正计数));
//                }
//                if(!XHStringUtil.isEmpty(hashMap.get(MapParams.反计数), false)){
//                    tu2EditPositiveReading.setText(hashMap.get(MapParams.反计数));
//                }
//
//                tu2EditAllstate.setText("流向："+LorawanUtils.getLiuXiangstateStringByCode(hashMap.get(MapParams.流向状态))+
//                        " 电池："+LorawanUtils.getQianYaStateStringByCode(hashMap.get(MapParams.欠压状态))+
//                        " 阀门："+LorawanUtils.getValvestateStringByCode(hashMap.get(MapParams.阀门状态)));
//
//                if(!XHStringUtil.isEmpty(hashMap.get(MapParams.最后计数时间), false)){
//                    tu2EditLasttime.setText(LorawanUtils.getLastTimeStringByCode(hashMap.get(MapParams.最后计数时间)));
//                }
//
//                if(!XHStringUtil.isEmpty(hashMap.get(MapParams.预警电压), false)){
//                    tu2EditDianya.setText(hashMap.get(MapParams.预警电压));
//                }





            }
        });
    }

}
