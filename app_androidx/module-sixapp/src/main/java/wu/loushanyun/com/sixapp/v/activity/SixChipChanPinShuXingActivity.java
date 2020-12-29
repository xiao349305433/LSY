package wu.loushanyun.com.sixapp.v.activity;

import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.elvishew.xlog.XLog;
import com.flyco.roundview.RoundTextView;
import com.sensoro.sensor.kit.entity.SensoroDevice;
import com.wu.loushanyun.base.config.K;
import com.wu.loushanyun.base.util.LorawanUtils;
import com.wu.loushanyun.base.util.LouShanYunUtils;
import com.wu.loushanyun.base.util.MapParams;
import com.wu.loushanyun.basemvp.init.SnBlueToothListener;
import com.wu.loushanyun.basemvp.init.SnBlueToothTool;
import com.wu.loushanyun.basemvp.m.sat.SAtInstructRealizeFactory;
import com.wu.loushanyun.basemvp.m.sat.util.SAtInstructParams;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.activity.BaseNoPresenterActivity;
import met.hx.com.base.baseevent.Event;
import met.hx.com.librarybase.some_utils.ByteConvertUtils;
import met.hx.com.librarybase.some_utils.FromHtmlUtil;
import met.hx.com.librarybase.some_utils.ToastUtils;
import met.hx.com.librarybase.some_utils.XHStringUtil;
import wu.loushanyun.com.sixapp.R;
import wu.loushanyun.com.sixapp.init.LoginParamManager;
import wu.loushanyun.com.sixapp.init.SixCode;
import wu.loushanyun.com.sixapp.init.SixUtils;
import wu.loushanyun.com.sixapp.m.GetComInfo;
import wu.loushanyun.com.sixapp.m.ManuInfo;
import wu.loushanyun.com.sixapp.m.ResponseAnalysisInfo;
import wu.loushanyun.com.sixapp.p.runner.GetComRunner;

import static com.wu.loushanyun.basemvp.init.SnBlueToothTool.BLUE_SEND_TIME_OUT;


@Route(path = K.SixChipChanPinShuXingActivity)
public class SixChipChanPinShuXingActivity  extends BaseNoPresenterActivity implements SnBlueToothListener {

    private Spinner tu3ServiceIdSelect;
    private Spinner tu3FactoryIdSelect;
    private EditText tu3EditDeviceId;
    private RoundTextView secondParameterRoundCreateId;
    private Spinner secondParameterCgxhSelect;
    private EditText tu3Capacitance;
    private Spinner tu3JixingSelect;
    private EditText tu3Valve;
    private TextView tu3OneSetting;

    private LinearLayout linearEnvironmental2;
    private TextView tvTiaoshiSend;
    private ImageView imgTiaoshi;
    private TextView tvEnvironmental2;



    private GetComInfo getComInfo;

    protected SnBlueToothTool snBlueToothTool;
    private SensoroDevice sensoroDevice;
    private SAtInstructRealizeFactory sAtInstructRealizeFactory;

    private int totalNum = 3;
    final int MSG_CONN = 1, MSG_CONNSUCCESS1 = 2;
    final int MSG_DIALOG = 5, MSG_SHOWVIEW = 6;
    final int MSG_CONNSUCCESS2 = 7, MSG_READSUCCESS = 8;
    final int MSG_READSUCCESSTONEXT = 9, MSG_ADDNUMBERSEND = 10;
    final int MSG_SENDTEXT = 12;
    private ArrayList<ResponseAnalysisInfo> aList = new ArrayList<>();
    private int sendNumber = 0;
    private int reciviceNumber = 0;

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        ba.mTitleText = "性能验证";
        ba.mTitleBackgroundColorId = R.color.blue_bg;
        ba.mActivityLayoutId = R.layout.m_six_activity_chip_chanpinshuxing;

    }


    @Override
    protected void initLifeCycle() {
        super.initLifeCycle();
        snBlueToothTool = new SnBlueToothTool(this);
        registerLifeCycle(snBlueToothTool);
    }

    @Override
    protected void initView() {
//        tu3ServiceIdSelect = (Spinner) findViewById(R.id.tu3_service_id_select);
//        tu3FactoryIdSelect = (Spinner) findViewById(R.id.tu3_Factory_id_select);
//        tu3EditDeviceId = (EditText) findViewById(R.id.tu3_edit_device_id);
//        secondParameterRoundCreateId = (RoundTextView) findViewById(R.id.second_parameter_round_create_id);
//        secondParameterCgxhSelect = (Spinner) findViewById(R.id.second_parameter_cgxh_select);
//        tu3Capacitance = (EditText) findViewById(R.id.tu3_capacitance);
//        tu3JixingSelect = (Spinner) findViewById(R.id.tu3_jixing_select);
//        tu3Valve = (EditText) findViewById(R.id.tu3_valve);
//        tu3OneSetting = (TextView) findViewById(R.id.tu3_one_setting);

        linearEnvironmental2 = (LinearLayout) findViewById(R.id.linear_environmental2);
        tvTiaoshiSend = (TextView) findViewById(R.id.tv_tiaoshi_send);
        imgTiaoshi = (ImageView) findViewById(R.id.img_tiaoshi);
        tvEnvironmental2 = (TextView) findViewById(R.id.tv_environmental2);


//        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(this, R.layout.l_loushanyun_item_spinner, LorawanUtils.getAllJiXin());
//        tu3JixingSelect.setAdapter(arrayAdapter1);
     //   pushEvent(SixCode.MSixGetCom, LoginParamManager.getInstance().getLoginInfo().getLoginId());

        sAtInstructRealizeFactory = new SAtInstructRealizeFactory();
        sensoroDevice = getIntent().getParcelableExtra("sensoroDevice");
        snBlueToothTool.connectBlueTooth(sensoroDevice);
        tvTiaoshiSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvEnvironmental2.setText("");

                aList.clear();
                sendNumber=0;
                snBlueToothTool.write(LorawanUtils.getQiangZhi()
                        , "发送0x04命令", true, BLUE_SEND_TIME_OUT);
            }
        });
//
//        tu3OneSetting.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (XHStringUtil.isEmpty(tu3EditDeviceId.getText().toString(), true)) {
//                    sendMessageToast("请填写设备ID");
//                    return;
//                }
//                snBlueToothTool.write(LorawanUtils.getIDSetting(tu3EditDeviceId.getText().toString()), "正在设置设备ID");
//            }
//        });

//        secondParameterRoundCreateId.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                tu3EditDeviceId.setText(LouShanYunUtils.getTimeID());
//            }
//        });
    }

    @Override
    public void onChildConnectFailed(int i) {

    }

    @Override
    public void onChildConnectSuccess() {
        try {
            initSnAgreement(Double.valueOf("1.07"));

            ToastUtils.showLong("连接成功");
            snBlueToothTool.write(LorawanUtils.getQiangZhi()
                    , "发送0x04命令", true, BLUE_SEND_TIME_OUT);
          //  snBlueToothTool.write(LorawanUtils.getChuChangReading(), "正在读取出厂设置");
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
              if (resultAt.equals(SAtInstructParams.sAtInstructRSSI + "-R")) {
                    try {
                        SixUtils.LorawanHashMap.putAll(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructRSSI).parseFourRNotifyBytes(result));
                        String[] strings = SixUtils.LorawanHashMap.get(SAtInstructParams.sAtInstructRSSI).split(",");
                        int rssi = Integer.valueOf(strings[0]);
                        int snr = Integer.valueOf(strings[1]);
                        aList.get(aList.size() - 1).setRssi(rssi + "");
                        aList.get(aList.size() - 1).setSnr(snr + "");
                        reciviceNumber++;
                        handler.sendEmptyMessage(MSG_ADDNUMBERSEND);
                        if (sendNumber == totalNum) {
                            handler.sendEmptyMessage(MSG_SHOWVIEW);
                        } else {
                            send2();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (resultAt.equals(SAtInstructParams.sAtInstructForcedSend + "+SEND2")) {
                    try {
                        sendMessageToast("强制发送成功");
                        snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructRSSI).getTwoReadBytes(), "读取信号强度");

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

            case 0x04:
                if (result[3] == 0) {
                    sendMessageToast("0x04命令发送成功");
                    try {
                        SixUtils.LorawanHashMap.putAll(LorawanUtils.getQiangZhiSend(result));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ResponseAnalysisInfo responseAnalysisInfo = new ResponseAnalysisInfo();
                                responseAnalysisInfo.setMaxTimes(SixUtils.LorawanHashMap.get(MapParams.最大发送次数));
                                responseAnalysisInfo.setSendTimes(SixUtils.LorawanHashMap.get(MapParams.发送次数));
                                aList.add(responseAnalysisInfo);

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
                    ResponseAnalysisInfo responseAnalysisInfo = new ResponseAnalysisInfo();
                    responseAnalysisInfo.setRssi("-");
                    responseAnalysisInfo.setSnr("-");
                    responseAnalysisInfo.setMaxTimes("0");
                    responseAnalysisInfo.setSendTimes("0");
                    aList.add(responseAnalysisInfo);
                    handler.sendEmptyMessage(MSG_SENDTEXT);//发送失败

                    if (sendNumber == totalNum) {
                        handler.sendEmptyMessage(MSG_SHOWVIEW);
                    } else {
                        send2();
                    }
                }
                break;

            case 0x08:
                if (result[3] == 0) {
                    sendMessageToast("设置设备ID成功");
                    SixFixActivity.Fix_Tu3=true;
                    snBlueToothTool.write(LorawanUtils.getChuChangWrite(SixUtils.LorawanHashMap.get(MapParams.脉冲底数),LorawanUtils.getBiaoBeilvCodeByString(SixUtils.LorawanHashMap.get(MapParams.倍率)) ,SixUtils.LorawanHashMap.get(MapParams.传感信号), "一对一",Integer.valueOf( getComInfo.getDatas().get(tu3ServiceIdSelect.getSelectedItemPosition()).getSubAccountId()), Integer.valueOf( getComInfo.getDatas().get(tu3FactoryIdSelect.getSelectedItemPosition()).getSubAccountId()), tu3JixingSelect.getSelectedItem().toString()), "正在设置出厂设置");

                } else if (result[3] == 1) {
                    sendMessageToast("设置设备ID失败");
                } else if (result[3] == 2) {
                    sendMessageToast("无效命令字");
                } else if (result[3] == 3) {
                    sendMessageToast("无效参数");
                }
                break;


        }
    }


    private void initSnAgreement(double hardVersion) {
        sAtInstructRealizeFactory.initHardVersion(hardVersion);
    }
    /**
     * 通过handler发送消息来执行相应的操作
     */
    protected Handler handler = new Handler() {


        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG_CONN)//蓝牙连接的方法
            {

            } else if (msg.what == MSG_CONNSUCCESS1) {
                send2();
            } else if (msg.what == MSG_SENDTEXT) {
                //发送失败
                setlisttv();
            } else if (msg.what == MSG_ADDNUMBERSEND) {
                //发送命令成功数
                setlisttv();
            } else if (msg.what == MSG_CONNSUCCESS2) {
                //识别设备第一次发送
            } else if (msg.what == MSG_READSUCCESSTONEXT) {
                //全部发送完后，显示数据
            } else if (msg.what == MSG_DIALOG) {
                //显示dialog
            } else if (msg.what == MSG_SHOWVIEW) {
                //全部发送完后
                //  showview();

            }
        }
    };


    private void setlisttv() {
        String rssi = "-105";
        String snr = "0";
        XLog.i("LSY rssi=====" + rssi);
        XLog.i("LSY snr=====" + snr);
        int chenggongTimes = 0;
        int fasongchenggongTimes = 0;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < aList.size(); i++) {
            ResponseAnalysisInfo aLoginAnalysis = aList.get(i);
            sb.append("强制" + FromHtmlUtil.get3Prehtml() + (i + 1) + "次");
            if (!aLoginAnalysis.getRssi().equals("-")) {
                fasongchenggongTimes++;
                chenggongTimes = chenggongTimes + Integer.valueOf(aLoginAnalysis.getSendTimes());
                sb.append(FromHtmlUtil.get3Prehtml() + "成功");
                sb.append(FromHtmlUtil.get3Prehtml() + "信噪比：" + ((Integer.parseInt(aLoginAnalysis.getSnr()) > Integer.parseInt(snr)) ? aLoginAnalysis.getSnr() : FromHtmlUtil.getRedhtml(aLoginAnalysis.getSnr()))
                );
                sb.append(FromHtmlUtil.get3Prehtml() + "信号强度：" + ((Integer.parseInt(aLoginAnalysis.getRssi()) > Integer.parseInt(rssi)) ? aLoginAnalysis.getRssi() : FromHtmlUtil.getRedhtml(aLoginAnalysis.getRssi()))
                );
                sb.append(FromHtmlUtil.get3Prehtml() + "最大发送次数: " + aLoginAnalysis.getMaxTimes());
                sb.append(FromHtmlUtil.get3Prehtml() + "发送次数: " + aLoginAnalysis.getSendTimes());
                aLoginAnalysis.setIssendok(true);
                aLoginAnalysis.setSnrisok((Integer.parseInt(aLoginAnalysis.getSnr()) > Integer.parseInt(snr)) ? true : false);
                aLoginAnalysis.setRssiisok((Integer.parseInt(aLoginAnalysis.getRssi()) > Integer.parseInt(rssi)) ? true : false);
                if (aLoginAnalysis.isRssiisok() == false || aLoginAnalysis.isSnrisok() == false) {
                    aLoginAnalysis.setIsok(false);
                } else {
                    aLoginAnalysis.setIsok(true);
                }

            } else {
                sb.append(FromHtmlUtil.get3Prehtml() + FromHtmlUtil.getRedhtml("失败"));
                sb.append(FromHtmlUtil.get3Prehtml() + "信噪比：-");
                sb.append(FromHtmlUtil.get3Prehtml() + "信号强度：-");
                sb.append(FromHtmlUtil.get3Prehtml() + "最大发送次数:-");
                sb.append(FromHtmlUtil.get3Prehtml() + "发送次数:-");
                aLoginAnalysis.setIssendok(false);
                aLoginAnalysis.setRssiisok(false);
                aLoginAnalysis.setSnrisok(false);
                aLoginAnalysis.setIsok(false);
            }
            sb.append(FromHtmlUtil.getBrhtml());

        }

        if (aList.size() == 4) {
            DecimalFormat df = new DecimalFormat("0.00%");//格式化小数
            String num = df.format(((float) fasongchenggongTimes / chenggongTimes));
            sb.append("发送成功率" + num);

            if (((float) fasongchenggongTimes / chenggongTimes) >= 0.5) {
                imgTiaoshi.setImageResource(R.mipmap.ic_ytg_right);
                    SixFixActivity.Fix_Tu3=true;
            } else if (((float) fasongchenggongTimes / chenggongTimes) < 0.25) {
                imgTiaoshi.setImageResource(R.mipmap.ic_wtg);
            } else {
                imgTiaoshi.setImageResource(R.mipmap.ic_wtg);
            }


        }

        tvEnvironmental2.setText(Html.fromHtml(sb.toString()));

    }


    /**
     * 多次强制发送
     */
    private void send2() {
        if (sensoroDevice != null) {
            try {
                sendNumber++;
                //  handler.sendEmptyMessage(MSG_SENDTEXT);
                snBlueToothTool.write(LorawanUtils.getQiangZhi()
                        , "发送0x04命令", true, BLUE_SEND_TIME_OUT);
            } catch (Exception e) {
                e.printStackTrace();
            }
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
      //  registerEventRunner(SixCode.MSixGetCom, new GetComRunner());
    }

    @Override
    protected void onEventRunEnd(Event event, int code) {
//        if (code == SixCode.MSixGetCom) {
//            getComInfo = (GetComInfo) event.getReturnParamAtIndex(0);
//            int resultcode = getComInfo.getCode();
//            if (resultcode == 0) {
//
//                ArrayList<String> arrayList1=new ArrayList<>();
//                ArrayList<String> arrayList2=new ArrayList<>();
//                for (int i = 0; i <getComInfo.getDatas().size() ; i++) {
//                    arrayList1.add(getComInfo.getDatas().get(i).getBusinessName());
//                    arrayList2.add(getComInfo.getDatas().get(i).getSubBusinessName());
//                }
//                ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(this, R.layout.l_loushanyun_item_spinner, arrayList1);
//                tu3ServiceIdSelect.setAdapter(arrayAdapter1);
//                ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(this, R.layout.l_loushanyun_item_spinner, arrayList2);
//                tu3FactoryIdSelect.setAdapter(arrayAdapter2);
//
//            } else if (resultcode == 1) {
//                sendMessageToast(getComInfo.getMessage(),true);
//            }
//            snBlueToothTool.connectBlueTooth(sensoroDevice);
//        }
    }




}
