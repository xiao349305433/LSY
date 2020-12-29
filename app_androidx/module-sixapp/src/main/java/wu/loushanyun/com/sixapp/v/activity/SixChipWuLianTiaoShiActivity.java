package wu.loushanyun.com.sixapp.v.activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.elvishew.xlog.XLog;
import com.sensoro.sensor.kit.entity.SensoroDevice;
import com.wu.loushanyun.base.config.K;
import com.wu.loushanyun.base.util.LorawanUtils;
import com.wu.loushanyun.base.util.LouShanYunUtils;
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
import wu.loushanyun.com.sixapp.init.SixCode;
import wu.loushanyun.com.sixapp.init.SixUtils;
import wu.loushanyun.com.sixapp.m.EnvironmentInfInfo;
import wu.loushanyun.com.sixapp.m.ResponseAnalysisInfo;
import wu.loushanyun.com.sixapp.p.runner.MSixSetRxDelayRunner;

import static com.wu.loushanyun.basemvp.init.SnBlueToothTool.BLUE_SEND_TIME_OUT;

@Route(path = K.SixChipWuLianTiaoShiActivity)
public class SixChipWuLianTiaoShiActivity extends BaseNoPresenterActivity implements SnBlueToothListener {

    private ScrollView testScroll;
    private EditText tu1EditDatatype;
    private EditText tu1EditState;
    private EditText tu1EditSn;
    private EditText atEditRssi;
    private EditText atEditSnr;
    private EditText tu1FasongpinglvEdit;
    private Spinner atFasonggonglvSelect;
    private Spinner atXindaocanshuSelect;
    private Spinner atKuopinyinziSelect;
    private EditText tu1PinduanEdit;
    private Spinner atRxDelaySelect;
    private Spinner feedbacknumSelect;
    private Switch tu1JihuoSwtich;
    private TextView tu1OneSetting;
    private TextView tvFeedbackTimes;
    private PopupWindow myPop;


    private boolean isAllSetting;
    private boolean isAllReading;
    private boolean isJiHuo;
    private EnvironmentInfInfo environmentInfInfo;
    protected SnBlueToothTool snBlueToothTool;
    private SensoroDevice sensoroDevice;
    private SAtInstructRealizeFactory sAtInstructRealizeFactory;


    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        ba.mTitleText = "物联调试";
        ba.mTitleBackgroundColorId = R.color.blue_bg;
        ba.mHasRightView = true;
        ba.mActivityLayoutId = R.layout.m_six_activity_chip_wuliantiaoshi;
        ba.mTitleRightImageIcon = R.mipmap.test_wxhj;
    }


    @Override
    protected void initLifeCycle() {
        super.initLifeCycle();
        snBlueToothTool = new SnBlueToothTool(this);
        registerLifeCycle(snBlueToothTool);
    }



    @Override
    protected void initView() {

        testScroll = (ScrollView) findViewById(R.id.test_scroll);
        tu1EditDatatype = (EditText) findViewById(R.id.tu1_edit_datatype);
        tu1EditState = (EditText) findViewById(R.id.tu1_edit_state);
        tu1EditSn = (EditText) findViewById(R.id.tu1_edit_sn);
        atEditRssi = (EditText) findViewById(R.id.at_edit_rssi);
        atEditSnr = (EditText) findViewById(R.id.at_edit_snr);
        tu1FasongpinglvEdit = (EditText) findViewById(R.id.tu1_fasongpinglv_edit);
        atFasonggonglvSelect = (Spinner) findViewById(R.id.at_fasonggonglv_select);
        atXindaocanshuSelect = (Spinner) findViewById(R.id.at_xindaocanshu_select);
        atKuopinyinziSelect = (Spinner) findViewById(R.id.at_kuopinyinzi_select);
        tu1PinduanEdit = (EditText) findViewById(R.id.tu1_pinduan_edit);
        atRxDelaySelect = (Spinner) findViewById(R.id.at_rxDelay_select);
        feedbacknumSelect = (Spinner) findViewById(R.id.feedbacknum_select);
        feedbacknumSelect.setEnabled(false);
        tu1JihuoSwtich = (Switch) findViewById(R.id.tu1_jihuo_swtich);
        tu1OneSetting = (TextView) findViewById(R.id.tu1_one_setting);
        tvFeedbackTimes = (TextView) findViewById(R.id.tv_feedback_times);

        sAtInstructRealizeFactory = new SAtInstructRealizeFactory();
       // environmentInfInfo = getIntent().getParcelableExtra("environmentInfInfo");
        sensoroDevice = getIntent().getParcelableExtra("sensoroDevice");
        snBlueToothTool.connectBlueTooth(sensoroDevice);


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.l_loushanyun_item_spinner, sAtInstructRealizeFactory.getAllMoShi());
        atXindaocanshuSelect.setAdapter(arrayAdapter);
        atXindaocanshuSelect.setEnabled(false);
        ArrayAdapter<String> arrayAdapterJiaoHu = new ArrayAdapter<String>(this, R.layout.l_loushanyun_item_spinner, LorawanUtils.getAllLuoJiaoHu());
        feedbacknumSelect.setAdapter(arrayAdapterJiaoHu);
        atKuopinyinziSelect.setEnabled(false);
        atFasonggonglvSelect.setEnabled(false);
        atRxDelaySelect.setEnabled(false);


        tu1OneSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAllSetting=true;
                try {
                    setChange();
                    snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructChannel).getTwoWriteBytes(atXindaocanshuSelect.getSelectedItem().toString().trim()), "设置信道");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public void onRightClick(View item) {
        super.onRightClick(item);
        showpop(item);
    }

    private void showpop(View item) {
        View view = LayoutInflater.from(this).inflate(R.layout.m_six_pop_wuliantiaoshi, null, false);
        TextView popTvEname = view.findViewById(R.id.pop_tv_ename);
        TextView popTvAT = view.findViewById(R.id.pop_tv_at);
        popTvEname.setText(SixUtils.DatasBean.getEnvName() + "\n" + SixUtils.DatasBean.getDemarcateTime());
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("信道参数："+SixUtils.DatasBean.getRemarks()+"\n");
        stringBuilder.append("扩频因子："+SixUtils.DatasBean.getSpreadFactor()+"\n");
        stringBuilder.append("Rxdelay："+SixUtils.DatasBean.getDelayParameter()+"S\n");
        stringBuilder.append("发送频率："+SixUtils.DatasBean.getSendFrequency()+"\n");
          stringBuilder.append("发送功率："+SixUtils.DatasBean.getSendPower()+"\n");
        stringBuilder.append("频段："+SixUtils.DatasBean.getFrequencyRange()+"\n");
        stringBuilder.append("信号强度："+SixUtils.DatasBean.getSignalIntensity()+"\n");
        stringBuilder.append("信噪比："+SixUtils.DatasBean.getSignalRatio()+"\n");
        stringBuilder.append("信噪比："+SixUtils.DatasBean.getSignalRatio()+"\n");
        stringBuilder.append("反馈次数："+SixUtils.DatasBean.getFeedbackNum()+"\n");
        popTvAT.setText(stringBuilder.toString());
        myPop = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        myPop.setBackgroundDrawable(new ColorDrawable());
        myPop.setOutsideTouchable(true);
        myPop.showAsDropDown(item, (int) item.getX(), (int) item.getY());
    }


    @Override
    public void onChildConnectFailed(int i) {

    }

    @Override
    public void onChildConnectSuccess() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tu1EditSn.setText(sensoroDevice.sn);
            }
        });

        initSnAgreement(Double.valueOf("1.07"));
        try {
            snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructChannel).getTwoReadBytes(), "读取信道中...");
       //     snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructToken).getTwoReadBytes(), "读取Token...");
            isAllReading=true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    public void onChildNotify(byte[] result) {
        byte code = (byte) (result[2] ^ ((byte) 0x80));
        XLog.i("蓝牙code=" + code);
        switch (code) {
            case 0x50:
                String resultAt = sAtInstructRealizeFactory.getSAtTypeString(result);
                XLog.i("蓝牙" + resultAt);
                if (resultAt.equals(SAtInstructParams.sAtInstructChannel + "-R")) {
                    try {
                        SixUtils.LorawanHashMap.putAll(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructChannel).parseTwoRNotifyBytes(result));
                        if (isAllReading) {
                            snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructSendingPower).getTwoReadBytes(), "读取发送功率");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (resultAt.equals(SAtInstructParams.sAtInstructSendingPower + "-R")) {
                    try {
                        SixUtils.LorawanHashMap.putAll(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructSendingPower).parseTwoRNotifyBytes(result));
                        if (isAllReading) {
                            snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructFirmwareVersion).getTwoReadBytes(), "读取固件版本");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (resultAt.equals(SAtInstructParams.sAtInstructFirmwareVersion + "-R")) {
                    try {
                        SixUtils.LorawanHashMap.putAll(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructFirmwareVersion).parseTwoRNotifyBytes(result));
                        if (isAllReading) {
                            snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructSpreadingFactor).getTwoReadBytes(), "读取扩频因子");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (resultAt.equals(SAtInstructParams.sAtInstructSpreadingFactor + "-R")) {
                    try {
                        SixUtils.LorawanHashMap.putAll(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructSpreadingFactor).parseTwoRNotifyBytes(result));
                        if (isAllReading) {
                            snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructRxDelay).getTwoReadBytes(), "读取RxDelay");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (resultAt.equals(SAtInstructParams.sAtInstructRxDelay + "-R")) {
                    try {
                        SixUtils.LorawanHashMap.putAll(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructRxDelay).parseTwoRNotifyBytes(result));
                        if (isAllReading) {
                            setAtDuShu( SixUtils.LorawanHashMap);
                            //      snBlueToothTool.write(DataParser.CMD_SYSTEM_STATUS, "正在读取设备激活状态");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (resultAt.equals(SAtInstructParams.sAtInstructToken + "-R")) {
                    try {
                        SixUtils.LorawanHashMap.putAll(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructToken).parseTwoRNotifyBytes(result));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //        mTextToken.setText("当前设备Token和SN(点击复制到剪贴板):\n" + "SN:" + sensoroDevice.sn + "\nToken:" + hashMap.get(SAtInstructParams.sAtInstructToken));
                                try {
                                    snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructChannel).getTwoReadBytes(), "读取信道中...");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
//                                isAllReading = true;
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (resultAt.equals(SAtInstructParams.sAtInstructChannel + "-W")) {
                    try {
                        sendMessageToast("信道设置成功");
                        if (isAllSetting) {
                            try {
                                snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructSendingPower).getTwoWriteBytes(atFasonggonglvSelect.getSelectedItem().toString().trim()), "设置发送功率");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (resultAt.equals(SAtInstructParams.sAtInstructSendingPower + "-W")) {
                    try {
                        sendMessageToast("设置发送功率成功");
                        if (isAllSetting) {
                            try {
                                snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructSpreadingFactor).getTwoWriteBytes(atKuopinyinziSelect.getSelectedItem().toString().trim()), "设置扩频因子");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (resultAt.equals(SAtInstructParams.sAtInstructSpreadingFactor + "-W")) {
                    try {
                        sendMessageToast("设置扩频因子成功");
                        pushEvent(SixCode.MChipSetRxDelayRunner, sensoroDevice.sn, atRxDelaySelect.getSelectedItem().toString().replace("S", "").trim());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (resultAt.equals(SAtInstructParams.sAtInstructRxDelay + "-W")) {
                    try {
                        sendMessageToast("设置RxDelay成功");
                        snBlueToothTool.write(LorawanUtils.getLorawanTimes(feedbacknumSelect.getSelectedItem().toString().replaceAll("次", "")), "正在设置反馈次数");
                        //   hasAtSetting = true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (resultAt.equals(SAtInstructParams.sAtInstructRSSI + "-R")) {
                    try {
                        SixUtils.LorawanHashMap.putAll(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructRSSI).parseFourRNotifyBytes(result));
                        String[] strings =  SixUtils.LorawanHashMap.get(SAtInstructParams.sAtInstructRSSI).split(",");
                        int rssi = Integer.valueOf(strings[0]);
                        int snr = Integer.valueOf(strings[1]);

                        runOnUiThread(() -> {
                            atEditRssi.setText(rssi+"");
                            atEditSnr.setText(snr+"");
                       //     snBlueToothTool.write(LorawanUtils.getjihuo(tu1JihuoSwtich.isChecked()), "正在设置设备激活或停用");
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (resultAt.equals(SAtInstructParams.sAtInstructForcedSend + "+SEND2")) {
                    try {
                        sendMessageToast("强制发送成功");
                        snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructRSSI).getTwoReadBytes(), "读取信号强度");
                        isAllReading = false;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (resultAt.equals(SAtInstructParams.sAtInstructForcedSend + "+SEND3")) {
                    try {
                        sendMessageToast("强制发送失败");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    sendMessageToast(resultAt);
                }
                break;

            case 0x01:
                if (result[3] == 0) {
                    sendMessageToast("系统停用");
                } else if (result[3] == 1) {
                    sendMessageToast("系统激活");
                }
                break;
            case 0x02:
                if (result[3] == 0) {
                    sendMessageToast("休眠成功");
                } else {
                    sendMessageToast("休眠失败");
                }
                break;
            case 0x03:
                if (result[3] == 0) {
                    sendMessageToast("设置成功");
                    try {
                        SixFixActivity.Fix_Tu1=true;
                        //在读取一遍数据
                    //    snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructChannel).getTwoReadBytes(), "读取信道中...");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    isAllReading=true;
                } else {
                    sendMessageToast("设置失败");
                }
                break;
            case 0x04:
                if (result[3] == 0) {
                    sendMessageToast("0x04命令发送成功");
                    try {
                        SixUtils.LorawanHashMap.putAll(LorawanUtils.getQiangZhiSend(result));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvFeedbackTimes.setText("本次上送的发送次数： "+SixUtils.LorawanHashMap.get(MapParams.发送次数));
                                try {
                                    snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructRSSI).getTwoReadBytes(), "读取信号强度");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    sendMessageToast("强制发送失败");

                }
                break;
            case 0x05:
                if (result[3] == 0) {
                    sendMessageToast("校准时间成功");
                    try {
                        snBlueToothTool.write(LorawanUtils.getLastTimeReading(), "正在读取最后上送时间");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    sendMessageToast("校准时间失败");
                }
                break;
            case 0x06:
                if (result[3] == 0) {
                    sendMessageToast("设置频率成功");
                } else if (result[3] == 1) {
                    sendMessageToast("设置频率失败");
                } else if (result[3] == 2) {
                    sendMessageToast("无效命令字");
                } else if (result[3] == 3) {
                    sendMessageToast("无效参数");
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


                } else if (result[3] == 1) {
                    sendMessageToast("读取电池电压失败");
                } else if (result[3] == 2) {
                    sendMessageToast("无效命令字");
                } else if (result[3] == 3) {
                    sendMessageToast("无效参数");
                }
                break;
            case 0x08:
                if (result[3] == 0) {
                    sendMessageToast("设置设备ID成功");
                } else if (result[3] == 1) {
                    sendMessageToast("设置设备ID失败");
                } else if (result[3] == 2) {
                    sendMessageToast("无效命令字");
                } else if (result[3] == 3) {
                    sendMessageToast("无效参数");
                }
                break;
            case 0x09:
                if (result[3] == 0) {
                    sendMessageToast("设置倍率成功");
                } else if (result[3] == 1) {
                    sendMessageToast("设置频率失败");
                } else if (result[3] == 2) {
                    sendMessageToast("无效命令字");
                } else if (result[3] == 3) {
                    sendMessageToast("无效参数");
                }
                break;

            case 0x12:
                if (result[3] == 0) {
                    sendMessageToast("设置出厂设置成功");
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

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tu1EditDatatype.setText(SixUtils.LorawanHashMap.get(MapParams.数据性质));


                        }
                    });



                } else if (result[3] == 1) {
                    sendMessageToast("读取最后上送时间失败");
                } else if (result[3] == 2) {
                    sendMessageToast("无效命令字");
                } else if (result[3] == 3) {
                    sendMessageToast("无效参数");
                }
                break;
            case 0x15:
                if (result[3] == 0) {
                    sendMessageToast("读取通讯状态成功");
                    if (result[4] == 0x00) {
                        SixUtils.LorawanHashMap.put(MapParams.通讯状态, "正常");
                    } else {
                        SixUtils.LorawanHashMap.put(MapParams.通讯状态, "测试");
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //   mTextTongxunzhuangtaiInfo.setText("通讯状态  :"+hashMap.get(MapParams.通讯状态));
                        }
                    });

                } else if (result[3] == 1) {
                    sendMessageToast("读取通讯状态失败");
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

                snBlueToothTool.write(LorawanUtils.getBanbenReading(), "正在读取版本号");

            }
        });
    }

    private void setdata() {
        //设置信道数据
        if (!SixUtils.DatasBean.getRemarks().equals(atXindaocanshuSelect.getSelectedItem().toString())) {
            atXindaocanshuSelect.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    ((TextView) atXindaocanshuSelect.getSelectedView()).setTextColor(Color.RED);
                    //    stringBuilder.append("信道参数："+environmentInfInfo.getDatas().get(0).getRemarks());
                }
            });
        }
        //设置扩频因子
        if (!SixUtils.DatasBean.getSpreadFactor().equals(atKuopinyinziSelect.getSelectedItem().toString())) {
            atKuopinyinziSelect.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    ((TextView) atKuopinyinziSelect.getSelectedView()).setTextColor(Color.RED);
                    //    stringBuilder.append("扩频因子："+environmentInfInfo.getDatas().get(0).getSpreadFactor());
                }
            });
        }


        //设置发送功率
        if (!SixUtils.DatasBean.getSendPower().equals(atFasonggonglvSelect.getSelectedItem().toString())) {
            atFasonggonglvSelect.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    ((TextView) atFasonggonglvSelect.getSelectedView()).setTextColor(Color.RED);
                    //    stringBuilder.append("信道参数："+environmentInfInfo.getDatas().get(0).getRemarks());
                }
            });
        }

        //设置Rxdelay
        if (SixUtils.DatasBean.getDelayParameter() - 1 != atRxDelaySelect.getSelectedItemPosition()) {
            atRxDelaySelect.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    ((TextView) atRxDelaySelect.getSelectedView()).setTextColor(Color.RED);
                }
            });
        }
    }




    private void setAtDuShu(HashMap<String, String> hashMap) {
        StringBuilder sb = new StringBuilder();
        sb.append("发送功率:  ");
        sb.append(hashMap.get(SAtInstructParams.sAtInstructSendingPower));
        sb.append("\n信道参数:  ");
        sb.append(hashMap.get(SAtInstructParams.sAtInstructChannel));
        sb.append("\nRxDelay:  ");
        sb.append(hashMap.get(SAtInstructParams.sAtInstructRxDelay) + "s");
        sb.append("\n扩频因子:  ");
        sb.append(hashMap.get(SAtInstructParams.sAtInstructSpreadingFactor));
        sb.append("\nLSY-IOT版本号:  ");
        sb.append(hashMap.get(SAtInstructParams.sAtInstructFirmwareVersion));
        runOnUiThread(() -> {

            if(!XHStringUtil.isEmpty(hashMap.get(MapParams.最大发送次数),false)){
                if(hashMap.get(MapParams.最大发送次数).equals("1")){
                    feedbacknumSelect.setSelection(0);
                }else if(hashMap.get(MapParams.最大发送次数).equals("2")){
                    feedbacknumSelect.setSelection(1);
                }else if(hashMap.get(MapParams.最大发送次数).equals("3")){
                    feedbacknumSelect.setSelection(2);
                }
                XLog.i("最大发送次数=="+hashMap.get(MapParams.最大发送次数));
            }




            if (!XHStringUtil.isEmpty(hashMap.get(MapParams.发送功率), false)) {
                if ("20dbm".equals(hashMap.get(SAtInstructParams.sAtInstructSendingPower))) {
                    atFasonggonglvSelect.setSelection(0);
                } else if ("18dbm".equals(hashMap.get(SAtInstructParams.sAtInstructSendingPower))) {
                    atFasonggonglvSelect.setSelection(1);
                } else if ("16dbm".equals(hashMap.get(SAtInstructParams.sAtInstructSendingPower))) {
                    atFasonggonglvSelect.setSelection(2);
                }
            }
            String[] kuopinyinziArray = getResources().getStringArray(R.array.l_loushanyun_kuopinyinzi);
            if (!XHStringUtil.isEmpty(hashMap.get(SAtInstructParams.sAtInstructSpreadingFactor), false)) {
                for (int i = 0; i < kuopinyinziArray.length; i++) {
                    if (kuopinyinziArray[i].equals(hashMap.get(SAtInstructParams.sAtInstructSpreadingFactor))) {
                        atKuopinyinziSelect.setSelection(i);
                    }
                }
            }




            //修正发送功率
            String[] fasonggonglv = getResources().getStringArray(R.array.l_loushanyun_xinhaoqiangdu);
            for (int i = 0; i < fasonggonglv.length; i++) {
                if (SixUtils.DatasBean.getSendPower().equals(fasonggonglv[i])) {
                    atFasonggonglvSelect.setSelection(i);
                }
            }
            atXindaocanshuSelect.setSelection(sAtInstructRealizeFactory.getMoShiIndex(hashMap.get(SAtInstructParams.sAtInstructChannel)));
            atRxDelaySelect.setSelection(Integer.valueOf(hashMap.get(SAtInstructParams.sAtInstructRxDelay))-1);
            setdata();
            snBlueToothTool.write(LorawanUtils.getjiaozhun(), "正在校准模组时间");

            //    snBlueToothTool.write(LorawanUtils.getChuChangReading(), "正在读取出厂设置");

//            mAtTextInfo.setText(sb.toString());
        });
    }




    //修正数据
    private void setChange() {
        //修正信道模式
        int len = sAtInstructRealizeFactory.getAllMoShi().size();
        atXindaocanshuSelect.setSelection(SixUtils.DatasBean.getChannelParam());
        atXindaocanshuSelect.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ((TextView) atXindaocanshuSelect.getSelectedView()).setTextColor(Color.BLACK);
            }
        });
    //修正反馈次数
        if(!XHStringUtil.isEmpty(SixUtils.DatasBean.getFeedbackNum(),false)){
            if(SixUtils.DatasBean.getFeedbackNum().equals("1次")){
                feedbacknumSelect.setSelection(0);
            }else if(SixUtils.DatasBean.getFeedbackNum().equals("2次")){
                feedbacknumSelect.setSelection(1);
            }else if(SixUtils.DatasBean.getFeedbackNum().equals("3次")){
                feedbacknumSelect.setSelection(2);
            }

        }

        //修正扩频因子
        String[] kuopingyinzi = getResources().getStringArray(R.array.l_loushanyun_kuopinyinzi);
        for (int i = 0; i < kuopingyinzi.length; i++) {
            if (SixUtils.DatasBean.getSpreadFactor().equals(kuopingyinzi[i])) {
                atKuopinyinziSelect.setSelection(i);
                atKuopinyinziSelect.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        ((TextView) atKuopinyinziSelect.getSelectedView()).setTextColor(Color.BLACK);
                    }
                });
            }
        }

        //修正发送功率
        String[] fasonggonglv = getResources().getStringArray(R.array.l_loushanyun_xinhaoqiangdu);
        for (int i = 0; i < fasonggonglv.length; i++) {
            if (SixUtils.DatasBean.getSendPower().equals(fasonggonglv[i])) {
                atFasonggonglvSelect.setSelection(i);
                atFasonggonglvSelect.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        ((TextView) atFasonggonglvSelect.getSelectedView()).setTextColor(Color.BLACK);
                    }
                });
            }
        }


        //修正Rxdelay
        String[] RxDelays = getResources().getStringArray(R.array.l_loushanyun_rxdelay);
        atRxDelaySelect.setSelection(SixUtils.DatasBean.getDelayParameter() - 1);

//        if (!XHStringUtil.isEmpty(environmentInfInfo.getDatas().get(0).getDelayParameter(), false)) {
//
//            for (int i = 0; i < RxDelays.length; i++) {
//                if (environmentInfInfo.getDatas().get(0).getSpreadFactor().equals(RxDelays[i])) {
//                    atRxDelaySelect.setSelection(i);
//                    atRxDelaySelect.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//                        @Override
//                        public void onGlobalLayout() {
//                            ((TextView) atRxDelaySelect.getSelectedView()).setTextColor(Color.BLACK);
//                        }
//                    });
//                }
//            }

        atRxDelaySelect.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ((TextView) atRxDelaySelect.getSelectedView()).setTextColor(Color.BLACK);
            }
        });
//        }

        //设置Rxdelay

        //    atRxDelaySelect.setText(environmentInfInfo.getDatas().get(0).getDelayParameter());


    }

    @Override
    public void onChildWriteSuccess() {

    }

    @Override
    public void onChildWriteFailure(int i) {

    }


    private void initSnAgreement(double hardVersion) {
        sAtInstructRealizeFactory.initHardVersion(hardVersion);
    }


    @Override
    protected void initEventListener() {
        registerEventRunner(SixCode.MChipSetRxDelayRunner, new MSixSetRxDelayRunner());
    }

    @Override
    protected void onEventRunEnd(Event event, int code) {
        if (code == SixCode.MChipSetRxDelayRunner) {
            if (event.isSuccess()) {
                int codeReturn = (int) event.getReturnParamAtIndex(0);
                String msg = (String) event.getReturnParamAtIndex(1);
                if (codeReturn == 0) {
                    try {
                        snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructRxDelay).getFourWriteBytes(atRxDelaySelect.getSelectedItem().toString().replace("S","").trim()), "设置RxDelay");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    sendMessageToast(msg);
                }
            }
        }
    }




}
