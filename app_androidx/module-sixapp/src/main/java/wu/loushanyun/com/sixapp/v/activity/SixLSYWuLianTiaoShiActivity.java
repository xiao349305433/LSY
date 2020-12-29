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

import androidx.appcompat.widget.SwitchCompat;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.elvishew.xlog.XLog;
import com.google.gson.Gson;
import com.lsy.domain.ResponseAnalysisInfo1;
import com.sensoro.sensor.kit.entity.SensoroDevice;
import com.wu.loushanyun.base.config.K;
import com.wu.loushanyun.base.util.DataParser;
import com.wu.loushanyun.base.util.LouShanYunUtils;
import com.wu.loushanyun.base.util.MapParams;
import com.wu.loushanyun.basemvp.init.SnBlueToothListener;
import com.wu.loushanyun.basemvp.init.SnBlueToothTool;
import com.wu.loushanyun.basemvp.m.sat.SAtInstructRealizeFactory;
import com.wu.loushanyun.basemvp.m.sat.util.SAtInstructParams;
import com.wu.loushanyun.basemvp.p.runner.MChipSetRxDelayRunner;

import java.util.HashMap;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.activity.BaseNoPresenterActivity;
import met.hx.com.base.baseevent.Event;
import met.hx.com.librarybase.some_utils.ToastUtils;
import met.hx.com.librarybase.some_utils.XHStringUtil;
import wu.loushanyun.com.sixapp.R;
import wu.loushanyun.com.sixapp.init.SixCode;
import wu.loushanyun.com.sixapp.init.SixUtils;
import wu.loushanyun.com.sixapp.m.EnvironmentInfInfo;
import wu.loushanyun.com.sixapp.p.runner.MSixSetRxDelayRunner;

import static com.wu.loushanyun.basemvp.init.SnBlueToothTool.BLUE_SEND_TIME_OUT;


@Route(path = K.SixLSYWuLianTiaoShiActivity)
public class SixLSYWuLianTiaoShiActivity extends BaseNoPresenterActivity implements SnBlueToothListener {


    private ScrollView testScroll;
    private EditText tu1EditSn;
    private EditText atEditRssi;
    private EditText atEditSnr;
    private EditText tu1FasongpinglvEdit;
    private Spinner atFasonggonglvSelect;
    private Spinner atXindaocanshuSelect;
    private Spinner atKuopinyinziSelect;
    private EditText tu1PinduanEdit;
    private Spinner atRxDelaySelect;
    private Switch tu1JihuoSwtich;
    private TextView atOneSetting;
    private PopupWindow myPop;

  //  private StringBuilder stringBuilder = new StringBuilder();
    private EnvironmentInfInfo environmentInfInfo;
    protected SnBlueToothTool snBlueToothTool;
    private SensoroDevice sensoroDevice;
    private SAtInstructRealizeFactory sAtInstructRealizeFactory;
    private boolean isAllSetting;
    private boolean isAllReading;
    private boolean isJiHuo;

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        ba.mTitleText = "物联调试";
        ba.mTitleBackgroundColorId = R.color.blue_bg;
        ba.mHasRightView = true;
        ba.mActivityLayoutId = R.layout.m_six_activity_lsy_wuliantiaoshi;
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
        tu1EditSn = (EditText) findViewById(R.id.tu1_edit_sn);
        atEditRssi = (EditText) findViewById(R.id.at_edit_rssi);
        atEditSnr = (EditText) findViewById(R.id.at_edit_snr);
        tu1FasongpinglvEdit = (EditText) findViewById(R.id.tu1_fasongpinglv_edit);
        atFasonggonglvSelect = (Spinner) findViewById(R.id.at_fasonggonglv_select);
        atFasonggonglvSelect.setEnabled(false);
        atXindaocanshuSelect = (Spinner) findViewById(R.id.at_xindaocanshu_select);
        atXindaocanshuSelect.setEnabled(false);
        atKuopinyinziSelect = (Spinner) findViewById(R.id.at_kuopinyinzi_select);
        atKuopinyinziSelect.setEnabled(false);
        tu1PinduanEdit = (EditText) findViewById(R.id.tu1_pinduan_edit);
        atRxDelaySelect = (Spinner) findViewById(R.id.at_RxDelay_select);
        atRxDelaySelect.setEnabled(false);
        tu1JihuoSwtich = (Switch) findViewById(R.id.tu1_jihuo_swtich);
        atOneSetting = (TextView) findViewById(R.id.at_one_setting);

        sAtInstructRealizeFactory = new SAtInstructRealizeFactory();
     //   environmentInfInfo = getIntent().getParcelableExtra("environmentInfInfo");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(SixLSYWuLianTiaoShiActivity.this, R.layout.l_loushanyun_item_spinner, sAtInstructRealizeFactory.getAllMoShi());
        atXindaocanshuSelect.setAdapter(arrayAdapter);

        sensoroDevice = getIntent().getParcelableExtra("sensoroDevice");
        snBlueToothTool.connectBlueTooth(sensoroDevice);

        atOneSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    setChange();
                    snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructChannel).getTwoWriteBytes(atXindaocanshuSelect.getSelectedItem().toString().trim()), "设置信道");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                isAllSetting = true;
            }
        });

    }


    @Override
    public void onChildConnectFailed(int i) {

    }



    @Override
    public void onChildConnectSuccess() {
        try {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tu1EditSn.setText(sensoroDevice.sn);
                }
            });
            initSnAgreement(Double.valueOf(LouShanYunUtils.getHardWareVersion(Integer.valueOf(SixUtils.SecondHashMap.get(MapParams.硬件版本)))));
            snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructToken).getTwoReadBytes(), "读取Token...");
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
                        SixUtils.SecondHashMap.putAll(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructChannel).parseTwoRNotifyBytes(result));
                        if (isAllReading) {
                            snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructSendingPower).getTwoReadBytes(), "读取发送功率");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (resultAt.equals(SAtInstructParams.sAtInstructSendingPower + "-R")) {
                    try {
                        SixUtils.SecondHashMap.putAll(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructSendingPower).parseTwoRNotifyBytes(result));
                        if (isAllReading) {
                            snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructFirmwareVersion).getTwoReadBytes(), "读取固件版本");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (resultAt.equals(SAtInstructParams.sAtInstructFirmwareVersion + "-R")) {
                    try {
                        SixUtils.SecondHashMap.putAll(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructFirmwareVersion).parseTwoRNotifyBytes(result));
                        if (isAllReading) {
                            snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructSpreadingFactor).getTwoReadBytes(), "读取扩频因子");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (resultAt.equals(SAtInstructParams.sAtInstructRSSI + "-R")) {
                    try {
                        SixUtils.SecondHashMap.putAll(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructRSSI).parseTwoRNotifyBytes(result));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String[] strings =  SixUtils.SecondHashMap.get(SAtInstructParams.sAtInstructRSSI).split(",");
                                int rssi = Integer.valueOf(strings[0]);
                                int snr = Integer.valueOf(strings[1]);

                                atEditRssi.setText(rssi+"");
                                atEditSnr.setText(snr+"");


                                try {
                                    //再读取一遍数据
                              //      snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructToken).getTwoReadBytes(), "读取Token...");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (resultAt.equals(SAtInstructParams.sAtInstructSpreadingFactor + "-R")) {
                    try {
                        SixUtils.SecondHashMap.putAll(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructSpreadingFactor).parseTwoRNotifyBytes(result));
                        if (isAllReading) {
                            snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructRxDelay).getTwoReadBytes(), "读取RxDelay");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (resultAt.equals(SAtInstructParams.sAtInstructRxDelay + "-R")) {
                    try {
                        SixUtils.SecondHashMap.putAll(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructRxDelay).parseTwoRNotifyBytes(result));
                        if (isAllReading) {
                            snBlueToothTool.write(DataParser.CMD_SYSTEM_STATUS, "正在读取设备激活状态");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (resultAt.equals(SAtInstructParams.sAtInstructToken + "-R")) {
                    try {
                        SixUtils.SecondHashMap.putAll(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructToken).parseTwoRNotifyBytes(result));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //        textToken.setText("当前设备Token和SN(点击复制到剪贴板):\n" + "SN:" + sensoroDeviceChoose.sn + "\nToken:" + hashMap.get(SAtInstructParams.sAtInstructToken));
                                try {
                                    snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructChannel).getTwoReadBytes(), "读取信道中...");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                isAllReading = true;
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
                } else if (resultAt.equals(SAtInstructParams.sAtInstructSpreadingFactor + "-W")) {
                    try {
                        sendMessageToast("设置扩频因子成功");
                        pushEvent(SixCode.MChipSetRxDelayRunner, sensoroDevice.sn, atRxDelaySelect.getSelectedItem().toString().replace("S","").trim());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (resultAt.equals(SAtInstructParams.sAtInstructRxDelay + "-W")) {
                    try {
                        sendMessageToast("设置RxDelay成功");
                        if(tu1JihuoSwtich.isChecked()){
                                openJiHuo();
                        }else {
                            closeJiHuo();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    sendMessageToast(resultAt);
                }
                break;
            case 0x26:
                if (result[3] == 0) {
                    if ("1".equals(String.valueOf(result[4] & 0xff))) {

                        runOnUiThread(() -> {
                            isJiHuo = true;
                            tu1JihuoSwtich.setChecked(true);
                            //已激活

                        });
                    } else {
                        runOnUiThread(() -> {
                            //未激活
                            isJiHuo = false;
                            tu1JihuoSwtich.setChecked(false);

                        });

                    }
                    setAtDuShu(SixUtils.SecondHashMap);
                } else {
//                    runOnUiThread(() -> systemStatus.setVisibility(View.GONE));
                }
                break;
            case 0x24:
                if (result[3] == 0) {
                    if (isJiHuo) {
                        runOnUiThread(() -> {
                            //改成未激活
                            isJiHuo = false;
                        });
                    } else {
                        runOnUiThread(() -> {
                            //改成已激活
                            isJiHuo = true;
                        });
                    }

                    SixFixActivity.Fix_Tu1=true;

//                    try {
//                        snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructForcedSend).getTwoReadBytes("15527919058")
//                                        , "强制发送", true, BLUE_SEND_TIME_OUT);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
                }


                break;
        }
    }

    private void closeJiHuo() {
        byte[] d = new byte[6];
        d[0] = (byte) 0x68;
        d[1] = (byte) 0x02;//有效数据
        d[2] = (byte) 0x24;//命令,设置成未激活
        d[3] = (byte) 0x00;//
        byte cs = 0;
        for (int i = 1; i < 4; i++) {
            cs += d[i];
        }
        d[4] = cs;//校验和
        d[5] = (byte) 0x16;
        snBlueToothTool.write(d, "正在停用无线上传");
    }

    private void openJiHuo() {
        byte[] d = new byte[6];
        d[0] = (byte) 0x68;
        d[1] = (byte) 0x02;//有效数据
        d[2] = (byte) 0x24;//命令,设置成未激活
        d[3] = (byte) 0x01;//
        byte cs = 0;
        for (int i = 1; i < 4; i++) {
            cs += d[i];
        }
        d[4] = cs;//校验和
        d[5] = (byte) 0x16;
        snBlueToothTool.write(d, "正在激活无线上传");
    }


    @Override
    public void onChildWriteSuccess() {

    }

    @Override
    public void onChildWriteFailure(int i) {

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


    @Override
    public void onRightClick(View item) {
        super.onRightClick(item);
        showpop(item);
    }

    private void initSnAgreement(double hardVersion) {
        sAtInstructRealizeFactory.initHardVersion(hardVersion);
    }

    private void setAtDuShu(HashMap<String, String> hashMap) {
//        StringBuilder sb = new StringBuilder();
//        sb.append("发送功率:  ");
//        sb.append(hashMap.get(SAtInstructParams.sAtInstructSendingPower));
//        sb.append("\n信道参数:  ");
//        sb.append(hashMap.get(SAtInstructParams.sAtInstructChannel));
//        sb.append("\nRxDelay:  ");
//        sb.append(hashMap.get(SAtInstructParams.sAtInstructRxDelay) + "s");
//        sb.append("\n扩频因子:  ");
//        sb.append(hashMap.get(SAtInstructParams.sAtInstructSpreadingFactor));
//        sb.append("\nLSY-IOT版本号:  ");
//        sb.append(hashMap.get(SAtInstructParams.sAtInstructFirmwareVersion));
        runOnUiThread(() -> {

            if (!XHStringUtil.isEmpty(hashMap.get(MapParams.发送功率), false)) {
                if ("20dbm".equals(hashMap.get(SAtInstructParams.sAtInstructSendingPower))) {
                    atFasonggonglvSelect.setSelection(0);
                } else if ("18dbm".equals(hashMap.get(SAtInstructParams.sAtInstructSendingPower))) {
                    atFasonggonglvSelect.setSelection(1);
                } else if ("16dbm".equals(hashMap.get(SAtInstructParams.sAtInstructSendingPower))) {
                    atFasonggonglvSelect.setSelection(2);
                }
            }

            if (!XHStringUtil.isEmpty(hashMap.get(SAtInstructParams.sAtInstructSpreadingFactor), false)) {
                String[] kuopinyinziArray = getResources().getStringArray(R.array.l_loushanyun_kuopinyinzi);
                for (int i = 0; i < kuopinyinziArray.length; i++) {
                    if (kuopinyinziArray[i].equals(hashMap.get(SAtInstructParams.sAtInstructSpreadingFactor))) {
                        atKuopinyinziSelect.setSelection(i);
                    }
                }
            }
            XLog.i("sAtInstructRxDelay===="+hashMap.get(SAtInstructParams.sAtInstructRxDelay));
            atRxDelaySelect.setSelection(Integer.valueOf(hashMap.get(SAtInstructParams.sAtInstructRxDelay))-1);
            atXindaocanshuSelect.setSelection(sAtInstructRealizeFactory.getMoShiIndex(hashMap.get(SAtInstructParams.sAtInstructChannel)));
            tu1FasongpinglvEdit.setText(SixUtils.DatasBean.getSendFrequency());
            setdata();

        });
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
        popTvAT.setText(stringBuilder.toString());
        myPop = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        myPop.setBackgroundDrawable(new ColorDrawable());
        myPop.setOutsideTouchable(true);
        myPop.showAsDropDown(item, (int) item.getX(), (int) item.getY());
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


}
