package wu.loushanyun.com.moduletwoinit.v.activity4;


import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.elvishew.xlog.XLog;
import com.flyco.roundview.RoundLinearLayout;
import com.flyco.roundview.RoundTextView;
import com.sensoro.sensor.kit.entity.SensoroDevice;
import com.wu.loushanyun.base.config.K;
import com.wu.loushanyun.base.util.DataParser;
import com.wu.loushanyun.base.util.LouShanYunUtils;
import com.wu.loushanyun.base.util.MapParams;
import com.wu.loushanyun.basemvp.init.LouShanYunCode;
import com.wu.loushanyun.basemvp.m.NewInfo;
import com.wu.loushanyun.basemvp.m.sat.SAtInstructRealizeFactory;
import com.wu.loushanyun.basemvp.m.sat.util.FourNumBlueToothUtil;
import com.wu.loushanyun.basemvp.m.sat.util.SAtInstructParams;
import com.wu.loushanyun.basemvp.p.runner.MChipGetNewsInfoRunner;
import com.wu.loushanyun.basemvp.v.activity.BaseSnBlueToothActivity;

import org.litepal.LitePal;

import java.util.HashMap;
import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableOnSubscribe;
import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.dialog.LoadingDialogUtil;
import met.hx.com.base.base.rx.RxSchedulers;
import met.hx.com.base.base.share.Share2;
import met.hx.com.base.base.share.ShareContentType;
import met.hx.com.base.baseevent.Event;
import met.hx.com.librarybase.some_utils.ByteConvertUtils;
import met.hx.com.librarybase.some_utils.KeyboardUtils;
import met.hx.com.librarybase.some_utils.TimeUtils;
import met.hx.com.librarybase.some_utils.XHStringUtil;
import met.hx.com.librarybase.some_utils.util.AbSharedUtil;
import wu.loushanyun.com.libraryfive.init.LoginFiveParamManager;
import wu.loushanyun.com.moduletwoinit.R;
import wu.loushanyun.com.moduletwoinit.m.InsidePublicMeter;

import static com.wu.loushanyun.basemvp.init.SnBlueToothTool.BLUE_SEND_TIME_OUT;

@Route(path = K.LSY4InitActivity)
public class LSY4InitActivity extends BaseSnBlueToothActivity {
    private ScrollView scrollView;
    private TextView textDangqianSn;
    private RoundTextView bluetoothConn;
    private RoundTextView bluetoothDisconn;
    private Spinner wangluojiaohuSelect;
    private Spinner pinlvSelect;
    private RoundTextView fourOneSetting;
    private RoundTextView fourOneReading;
    private TextView fourTextInfo;
    private ImageView systemStatus;
    private TextView textShowHint;
    private RoundTextView thirdTextClearWrite;
    private RoundTextView thirdTextWrite;
    private EditText thirdEditWrite;
    private RoundTextView thirdTextClearRead;
    private TextView thirdTextRead;
    private TextView textBanben;
    private RoundTextView xulieRead;
    private TextView xulieTextRead;
    private TextView xulieDataText;
    private RoundLinearLayout roundTextFour;
    private Spinner caliber;
    private TextView editOtherRate;
    private Spinner danwei;
    private EditText inputMessage;
    private Spinner atFasonggonglvSelect;
    private RoundTextView atFasonggonglvSetting;
    private Spinner atXindaocanshuSelect;
    private RoundTextView atXindaocanshuSetting;
    private Spinner atKuopinyinziSelect;
    private RoundTextView atKuopinyinziSetting;
    private RoundTextView atOneSetting;
    private RoundTextView atOneReading;
    private TextView atTextInfo;
    private EditText editXinhaoqiangduStart;
    private EditText editXinhaoqiangduEnd;
    private EditText editXinzaobiStart;
    private EditText editXinzaobiEnd;
    private TextView textHuoqu;
    private RoundTextView buttonQiangzhifasong;
    private RoundTextView buttonDuquXinhao;
    private TextView textHuoquNew;
    private RoundTextView buttonDuquNew;
    private RoundTextView roundTextXiumianSave;

    private SensoroDevice sensoroDeviceChoose;
    private SAtInstructRealizeFactory sAtInstructRealizeFactory;
    private int type;
    private HashMap<String, String> hashMap;

    //默认参数
    private boolean isAllSetting;
    private boolean isAllReading;
    private boolean isJiHuo = true;
    private boolean isReadAllSuccess = false;
    private boolean fourSettingReadDataSuccess = false;
    private static final String THIRDSHARE = "thirdDataShare";

    private StringBuffer stringBufferThird;
    private boolean isAllFourSetting;
    private String rxDelay = "5";
    private int xuLieLength;
    private int xuLieIndex;
    private final static int WTIME = 5000;

    private StringBuffer stringBufferXuLie;
    private StringBuffer stringBufferXuLieData;
    private String[] stringsXuLie;
    private int stringsXuLieIndex;
    private boolean isJiHuoAndXiuMian = false;

    @Override
    protected int onChildLayout() {
        return R.layout.m_twoinit_activity_lsy4_init;
    }

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        super.onInitAttribute(ba);
        ba.mTitleText = "4号模组初始化";
    }

    @Override
    protected void initView() {
        super.initView();
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        textDangqianSn = (TextView) findViewById(R.id.text_dangqian_sn);
        bluetoothConn = (RoundTextView) findViewById(R.id.bluetooth_conn);
        bluetoothDisconn = (RoundTextView) findViewById(R.id.bluetooth_disconn);
        wangluojiaohuSelect = (Spinner) findViewById(R.id.wangluojiaohu_select);
        pinlvSelect = (Spinner) findViewById(R.id.pinlv_select);
        fourOneSetting = (RoundTextView) findViewById(R.id.four_one_setting);
        fourOneReading = (RoundTextView) findViewById(R.id.four_one_reading);
        fourTextInfo = (TextView) findViewById(R.id.four_text_info);
        systemStatus = (ImageView) findViewById(R.id.system_status);
        textShowHint = (TextView) findViewById(R.id.text_show_hint);
        thirdTextClearWrite = (RoundTextView) findViewById(R.id.third_text_clear_write);
        thirdTextWrite = (RoundTextView) findViewById(R.id.third_text_write);
        thirdEditWrite = (EditText) findViewById(R.id.third_edit_write);
        thirdTextClearRead = (RoundTextView) findViewById(R.id.third_text_clear_read);
        thirdTextRead = (TextView) findViewById(R.id.third_text_read);
        textBanben = (TextView) findViewById(R.id.text_banben);
        xulieRead = (RoundTextView) findViewById(R.id.xulie_read);
        xulieTextRead = (TextView) findViewById(R.id.xulie_text_read);
        xulieDataText = (TextView) findViewById(R.id.xulie_data_text);
        roundTextFour = (RoundLinearLayout) findViewById(R.id.round_text_four);
        caliber = (Spinner) findViewById(R.id.caliber);
        editOtherRate = (TextView) findViewById(R.id.edit_other_rate);
        danwei = (Spinner) findViewById(R.id.danwei);
        inputMessage = (EditText) findViewById(R.id.input_message);
        atFasonggonglvSelect = (Spinner) findViewById(R.id.at_fasonggonglv_select);
        atFasonggonglvSetting = (RoundTextView) findViewById(R.id.at_fasonggonglv_setting);
        atXindaocanshuSelect = (Spinner) findViewById(R.id.at_xindaocanshu_select);
        atXindaocanshuSetting = (RoundTextView) findViewById(R.id.at_xindaocanshu_setting);
        atKuopinyinziSelect = (Spinner) findViewById(R.id.at_kuopinyinzi_select);
        atKuopinyinziSetting = (RoundTextView) findViewById(R.id.at_kuopinyinzi_setting);
        atOneSetting = (RoundTextView) findViewById(R.id.at_one_setting);
        atOneReading = (RoundTextView) findViewById(R.id.at_one_reading);
        atTextInfo = (TextView) findViewById(R.id.at_text_info);
        editXinhaoqiangduStart = (EditText) findViewById(R.id.edit_xinhaoqiangdu_start);
        editXinhaoqiangduEnd = (EditText) findViewById(R.id.edit_xinhaoqiangdu_end);
        editXinzaobiStart = (EditText) findViewById(R.id.edit_xinzaobi_start);
        editXinzaobiEnd = (EditText) findViewById(R.id.edit_xinzaobi_end);
        textHuoqu = (TextView) findViewById(R.id.text_huoqu);
        buttonQiangzhifasong = (RoundTextView) findViewById(R.id.button_qiangzhifasong);
        buttonDuquXinhao = (RoundTextView) findViewById(R.id.button_duqu_xinhao);
        textHuoquNew = (TextView) findViewById(R.id.text_huoqu_new);
        buttonDuquNew = (RoundTextView) findViewById(R.id.button_duqu_new);
        roundTextXiumianSave = (RoundTextView) findViewById(R.id.round_text_xiumian_save);


        initTestClick();
        sAtInstructRealizeFactory = new SAtInstructRealizeFactory();
        hashMap = new HashMap<>();
        stringBufferThird = new StringBuffer();
        bluetoothConn.setOnClickListener(v -> {
            if (textDangqianSn.getText().toString().contains("当前连接的SN设备")) {
                sendMessageToast("当前已连接设备");
                return;
            }
            snBlueToothTool.connectBlueTooth(sensoroDeviceChoose);
        });
        bluetoothDisconn.setOnClickListener(v -> {
            snBlueToothTool.disconnect();
            textDangqianSn.setText("当前未连接SN设备");
        });
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(LSY4InitActivity.this, R.layout.l_loushanyun_item_spinner, sAtInstructRealizeFactory.getAllMoShi());
        atXindaocanshuSelect.setAdapter(arrayAdapter);
        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(LSY4InitActivity.this, R.layout.l_loushanyun_item_spinner, FourNumBlueToothUtil.getAllPinLv());
        pinlvSelect.setAdapter(arrayAdapter1);
        ArrayAdapter<String> arrayAdapterJiaoHu = new ArrayAdapter<String>(LSY4InitActivity.this, R.layout.l_loushanyun_item_spinner, FourNumBlueToothUtil.getAllWangLuoJiaoHu());
        wangluojiaohuSelect.setAdapter(arrayAdapterJiaoHu);
        pinlvSelect.setSelection(2);
        atKuopinyinziSelect.setSelection(3);
    }

    private void initTestClick() {
        atXindaocanshuSetting.setOnClickListener(this::OnBlueToothClick);
        atKuopinyinziSetting.setOnClickListener(this::OnBlueToothClick);
        atFasonggonglvSetting.setOnClickListener(this::OnBlueToothClick);
        atOneSetting.setOnClickListener(this::OnBlueToothClick);
        atOneReading.setOnClickListener(this::OnBlueToothClick);

        thirdTextClearRead.setOnClickListener(this::OnBlueToothClick);
        thirdTextClearWrite.setOnClickListener(this::OnBlueToothClick);
        thirdTextRead.setOnClickListener(this::OnBlueToothClick);
        thirdTextWrite.setOnClickListener(this::OnBlueToothClick);
        fourOneReading.setOnClickListener(this::OnBlueToothClick);
        fourOneSetting.setOnClickListener(this::OnBlueToothClick);
        buttonQiangzhifasong.setOnClickListener(this::OnBlueToothClick);
        buttonDuquXinhao.setOnClickListener(this::OnBlueToothClick);
        xulieRead.setOnClickListener(this::OnBlueToothClick);
        xulieTextRead.setOnClickListener(this::OnBlueToothClick);
        xulieDataText.setOnClickListener(this::OnBlueToothClick);
        roundTextXiumianSave.setOnClickListener(this::OnBlueToothClick);

        buttonDuquNew.setOnClickListener(this::OnClick);

    }

    /**
     * 需要判断蓝牙连接状态的点击事件
     *
     * @param view
     */
    private void OnBlueToothClick(View view) {
        if (!snBlueToothTool.isConnected()) {
            sendMessageToast("未连接蓝牙");
            return;
        }
        if (type != 4) {
            sendMessageToast("识别不是4号模组，请选择正确的模组");
            return;
        }
        int i = view.getId();
        if (i == R.id.at_xindaocanshu_setting) {
            try {
                snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructChannel).getFourWriteBytes(atXindaocanshuSelect.getSelectedItem().toString().trim()), "设置信道");
            } catch (Exception e) {
                e.printStackTrace();
            }
            isAllSetting = false;

        } else if (i == R.id.at_kuopinyinzi_setting) {
            try {
                snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructSpreadingFactor).getFourWriteBytes(atKuopinyinziSelect.getSelectedItem().toString().trim()), "设置扩频因子");
            } catch (Exception e) {
                e.printStackTrace();
            }
            isAllSetting = false;

        } else if (i == R.id.at_fasonggonglv_setting) {
            try {
                snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructSendingPower).getFourWriteBytes(atFasonggonglvSelect.getSelectedItem().toString().trim()), "设置发送功率");
            } catch (Exception e) {
                e.printStackTrace();
            }
            isAllSetting = false;

        } else if (i == R.id.at_one_reading) {
            try {
                snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructChannel).getFourReadBytes(), "读取信道中...");
            } catch (Exception e) {
                e.printStackTrace();
            }
            isAllReading = true;
            atTextInfo.setText("");

        } else if (i == R.id.at_one_setting) {
            try {
                snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructChannel).getFourWriteBytes(atXindaocanshuSelect.getSelectedItem().toString().trim()), "设置信道");
            } catch (Exception e) {
                e.printStackTrace();
            }
            isAllSetting = true;

/**-------------------------------------------------------------------------------------------------------------------------------------------*/
        } else if (i == R.id.four_one_setting) {
            isAllFourSetting = true;
            if (isAllFourSetting) {
                snBlueToothTool.write(FourNumBlueToothUtil.getFouWangluoJiaoHuBytes(wangluojiaohuSelect.getSelectedItem().toString().replaceAll("最大发送", "").replaceAll("次", "")), "正在设置网络交互");
            }
        } else if (i == R.id.four_one_reading) {
            isReadAllSuccess = true;
            try {
                snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sInstructDiscern).getFourWriteBytes(), "识别模块中...", true, 6000);
            } catch (Exception e) {
                e.printStackTrace();
            }

/**-------------------------------------------------------------------------------------------------------------------------------------------*/
        } else if (i == R.id.system_status) {
            if (isJiHuo) {
                closeJiHuo();
            } else {
                openJiHuo();
            }

        } else if (i == R.id.round_text_xiumian_save) {
            boolean isHigher = LouShanYunUtils.isHigherGuJian(hashMap.get(SAtInstructParams.sAtInstructFirmwareVersion));
            if (XHStringUtil.isEmpty(fourTextInfo.getText().toString(), false)) {
                sendMessageToast("请配置和读取4号模组参数");
                return;
            }
            if (!fourSettingReadDataSuccess) {
                sendMessageToast("请读取自定义协议序列数据");
                return;
            }
            if (XHStringUtil.isEmpty(xulieTextRead.getText().toString(), false)) {
                sendMessageToast("请读取自定义协议序列数据");
                return;
            }
            if (XHStringUtil.isEmpty(textHuoqu.getText().toString(), false)) {
                sendMessageToast("请进行强制发送测试与基站的通信是否良好");
                return;
            }
            if (XHStringUtil.isEmpty(atTextInfo.getText().toString(), false)) {
                sendMessageToast("请配置和读取模组上送参数");
                return;
            }
            if (XHStringUtil.isEmpty(inputMessage.getText().toString(), false)) {
                sendMessageToast("请输入备注信息");
                return;
            }
            if (XHStringUtil.isEmpty(editOtherRate.getText().toString(), false)) {
                sendMessageToast("请输入水表倍率");
                return;
            }
            if (!isHigher) {
                sendMessageToast("固件版本过低，不能进行保存");
                return;
            }
            int loginid = LoginFiveParamManager.getInstance().getLoginData().getId();
            InsidePublicMeter insidePublicMeter = new InsidePublicMeter();
            insidePublicMeter.setSn(sensoroDeviceChoose.sn);
            insidePublicMeter.setLoginId(loginid + "");
            insidePublicMeter.setFrequency(hashMap.get(MapParams.发送频率));
            insidePublicMeter.setCaliber(String.valueOf(caliber.getSelectedItem()).replaceAll("MM",""));
            insidePublicMeter.setRemark(String.valueOf(inputMessage.getText()));
            insidePublicMeter.setSendingPower(hashMap.get(SAtInstructParams.sAtInstructSendingPower));
            insidePublicMeter.setSf(hashMap.get(SAtInstructParams.sAtInstructSpreadingFactor));
            String[] strings = hashMap.get(SAtInstructParams.sAtInstructRSSI).split(",");
            int rssi = Integer.valueOf(strings[0]);
            int snr = Integer.valueOf(strings[1]);
            insidePublicMeter.setSnr(snr + "");
            insidePublicMeter.setRssi(rssi + "");
            insidePublicMeter.setRate(editOtherRate.getText().toString());
            insidePublicMeter.setParamOrUnit("m³".equals(danwei.getSelectedItem().toString()) ? "2" : "1");
            insidePublicMeter.setNetworkRetryNumber(hashMap.get(MapParams.网络交互));
            insidePublicMeter.setChannel(hashMap.get(SAtInstructParams.sAtInstructChannel));
            insidePublicMeter.setJiHuoTime(TimeUtils.getCurTimeMills() + "");
            insidePublicMeter.setSensingSignalModule(hashMap.get(MapParams.传感模式));
            if ("3".equals(hashMap.get(MapParams.传感模式))) {
                String xulie = xulieTextRead.getText().toString().replaceAll("\n", ",");
                insidePublicMeter.setParseData(xulie);
            } else if ("2".equals(hashMap.get(MapParams.传感模式))) {
            } else if ("1".equals(hashMap.get(MapParams.传感模式))) {
            }
            compositeDisposable.add(Flowable.create(
                    (FlowableOnSubscribe<Boolean>) emitter ->
                    {
                        LoadingDialogUtil.showByEvent("正在保存", this.getClass().getName());
                        long id = 0;


                        List<InsidePublicMeter> arrayList = LitePal.where("sn = ? and loginId = ?", sensoroDeviceChoose.sn, loginid + "")
                                .find(InsidePublicMeter.class);
                        if (arrayList.size() > 0) {
                            id = arrayList.get(0).getBaseObjId();
                        }
                        if (id == 0) {
                            insidePublicMeter.assignBaseObjId(0);
                            insidePublicMeter.saveAsync().listen(success -> {
                                if (success) {
                                    emitter.onNext(true);
                                } else {
                                    emitter.onNext(false);
                                }

                            });
                        } else {
                            LitePal.delete(InsidePublicMeter.class, id);
                            insidePublicMeter.assignBaseObjId(0);
                            insidePublicMeter.saveAsync().listen(success -> {
                                if (success) {
                                    emitter.onNext(true);
                                } else {
                                    emitter.onNext(false);
                                }
                            });
                        }
                        LoadingDialogUtil.dismissByEvent(this.getClass().getName());
                    }
                    , BackpressureStrategy.ERROR)
                    .compose(RxSchedulers.io_main())
                    .subscribe(success -> {
                        if (success) {
                            isJiHuoAndXiuMian = true;
                            isJiHuo = false;
                            openJiHuo();
                            sendMessageToast("保存成功，请到网络好的地方同步");
                        } else {
                            sendMessageToast("保存失败");
                        }
                    }));


        } else if (i == R.id.button_qiangzhifasong) {
            clearAllText();
            try {
                snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructForcedSend).getTwoReadBytes(LoginFiveParamManager.getInstance().getLoginData().getRegisterPhone())
                        , "强制发送", true, BLUE_SEND_TIME_OUT);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (i == R.id.button_duqu_xinhao) {
            try {
                snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructRSSI).getTwoReadBytes(), "读取信号强度");
                isAllReading = false;
            } catch (Exception e) {
                e.printStackTrace();
            }

/**------------------------------------------------------------数字通讯-------------------------------------------------------------------------------*/
        } else if (i == R.id.third_text_write) {
            KeyboardUtils.hideSoftInput(this);
            try {
                String writeStr = thirdEditWrite.getText().toString().replace(" ", "").trim();
                if (XHStringUtil.isEmpty(writeStr, false)) {
                    sendMessageToast("请输入指令");
                    return;
                }
                AbSharedUtil.putString(this, THIRDSHARE, writeStr);
                snBlueToothTool.write(FourNumBlueToothUtil.getFourOEBytes(writeStr), "正在发送");
            } catch (Exception e) {
                sendMessageToast("解析错误");
            }

        } else if (i == R.id.third_text_clear_write) {
            thirdEditWrite.setText("");

        } else if (i == R.id.third_text_clear_read) {
            stringBufferThird = new StringBuffer();
            thirdTextRead.setText("");

        } else if (i == R.id.xulie_read) {
            snBlueToothTool.write(FourNumBlueToothUtil.getFour22Bytes(), "读取第三方自定义协议序列是否已经配置");
            stringBufferXuLie = new StringBuffer();
            stringBufferXuLieData = new StringBuffer();
            xulieDataText.setText("");
            xulieTextRead.setText("");

        } else if (i == R.id.xulie_text_read) {
            if (!XHStringUtil.isEmpty(xulieTextRead.getText().toString(), false)) {
                new Share2.Builder(LSY4InitActivity.this)
                        .setContentType(ShareContentType.TEXT)
                        .setTitle("第三方自定义序列")
                        .setTextContent(xulieTextRead.getText().toString())
                        .setOnActivityResult(1000)
                        .build()
                        .shareBySystem();
            }

        } else if (i == R.id.xulie_data_text) {
            if (!XHStringUtil.isEmpty(xulieDataText.getText().toString(), false)) {
                new Share2.Builder(LSY4InitActivity.this)
                        .setContentType(ShareContentType.TEXT)
                        .setTitle("第三方自定义序列数据")
                        .setTextContent(xulieDataText.getText().toString())
                        .setOnActivityResult(1000)
                        .build()
                        .shareBySystem();
            }

        }
    }

    private void close() {
        snBlueToothTool.write(new byte[]{(byte) 0x68, (byte) 0x01, (byte) 0x0d, (byte) 0x0e, (byte) 0x16}, "正在让设备进入休眠状态");
    }

    private void clearAllText() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textHuoqu.setText("");
            }
        });

    }


    private void closeJiHuo() {
        byte[] d = new byte[6];
        d[0] = (byte) 0x68;
        d[1] = (byte) 0x02;//有效数据
        d[2] = (byte) 0x0c;//命令,设置成未激活
        d[3] = (byte) 0x00;//停用
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
        d[2] = (byte) 0x0c;//命令,设置成未激活
        d[3] = (byte) 0x01;//激活
        byte cs = 0;
        for (int i = 1; i < 4; i++) {
            cs += d[i];
        }
        d[4] = cs;//校验和
        d[5] = (byte) 0x16;
        snBlueToothTool.write(d, "正在激活无线上传");
    }

    private void OnClick(View view) {
        int i = view.getId();
        if (i == R.id.button_duqu_new) {
            pushEvent(LouShanYunCode.MChipGetNewsInfoRunner, sensoroDeviceChoose.sn, "1", "5");
        }
    }

    @Override
    public void onChildConnectFailed(int i) {

    }

    @Override
    public void onChildConnectSuccess() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textHuoqu.setText("");
                xulieTextRead.setText("");
                atTextInfo.setText("");
                fourTextInfo.setText("");
                fourSettingReadDataSuccess = false;
                hashMap.clear();
                textDangqianSn.setText("当前连接的SN设备:" + sensoroDeviceChoose.getSerialNumber().toUpperCase());
                try {
                    snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sInstructDiscern).getFourWriteBytes(), "识别模块中...", true, 6000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    protected void onClickBlueListListener(SensoroDevice sensoroDevice) {
        super.onClickBlueListListener(sensoroDevice);
        this.sensoroDeviceChoose = sensoroDevice;
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
                        hashMap.putAll(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructChannel).parseFourRNotifyBytes(result));
                        if (isAllReading) {
                            snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructSendingPower).getFourReadBytes(), "读取发送功率");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (resultAt.equals(SAtInstructParams.sAtInstructSendingPower + "-R")) {
                    try {
                        hashMap.putAll(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructSendingPower).parseFourRNotifyBytes(result));
                        if (isAllReading) {
                            snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructFirmwareVersion).getFourReadBytes(), "读取固件版本");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (resultAt.equals(SAtInstructParams.sAtInstructFirmwareVersion + "-R")) {
                    try {
                        hashMap.putAll(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructFirmwareVersion).parseFourRNotifyBytes(result));
                        if (isAllReading) {
                            snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructSpreadingFactor).getFourReadBytes(), "读取扩频因子");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (resultAt.equals(SAtInstructParams.sAtInstructRSSI + "-R")) {
                    try {
                        hashMap.putAll(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructRSSI).parseFourRNotifyBytes(result));
                        String[] strings = hashMap.get(SAtInstructParams.sAtInstructRSSI).split(",");
                        int rssi = Integer.valueOf(strings[0]);
                        int snr = Integer.valueOf(strings[1]);
                        int rssiStart = Integer.valueOf(editXinhaoqiangduStart.getText().toString());
                        int rssiEnd = Integer.valueOf(editXinhaoqiangduEnd.getText().toString());
                        int snrStart = Integer.valueOf(editXinzaobiStart.getText().toString());
                        int snrEnd = Integer.valueOf(editXinzaobiEnd.getText().toString());
                        String status;
                        if (rssi > rssiStart && rssi < rssiEnd && snr > snrStart && snr < snrEnd) {
                            status = "合格";
                        } else {
                            status = "不合格";
                        }
                        runOnUiThread(() -> {
                            String sb = "信号强度：" + rssi + "\n" + "信噪比：" + snr + "\n" + "状态：" + status;
                            textHuoqu.setText(sb);
                            pushEventNoProgress(LouShanYunCode.MChipGetNewsInfoRunner, sensoroDeviceChoose.sn, "1", "5");
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (resultAt.equals(SAtInstructParams.sAtInstructSpreadingFactor + "-R")) {
                    try {
                        hashMap.putAll(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructSpreadingFactor).parseFourRNotifyBytes(result));
                        if (isAllReading) {
                            snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructRxDelay).getFourReadBytes(), "读取RxDelay");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (resultAt.equals(SAtInstructParams.sAtInstructRxDelay + "-R")) {
                    try {
                        hashMap.putAll(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructRxDelay).parseFourRNotifyBytes(result));
                        if (isAllReading) {
                            setAtDuShu(hashMap);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (resultAt.equals(SAtInstructParams.sAtInstructChannel + "-W")) {
                    try {
                        sendMessageToast("信道设置成功");
                        clearAllAtInfo();
                        if (isAllSetting) {
                            try {
                                snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructSendingPower).getFourWriteBytes(atFasonggonglvSelect.getSelectedItem().toString().trim()), "设置发送功率");
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
                        clearAllAtInfo();
                        if (isAllSetting) {
                            try {
                                snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructSpreadingFactor).getFourWriteBytes(atKuopinyinziSelect.getSelectedItem().toString().trim()), "设置扩频因子");
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
                        snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructRSSI).getFourReadBytes(), "读取信号强度");
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
                        clearAllAtInfo();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (resultAt.equals(SAtInstructParams.sAtInstructRxDelay + "-W")) {
                    try {
                        sendMessageToast("设置RxDelay成功");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    sendMessageToast(resultAt);
                }
                break;
            case 0x26:
                if (result[3] == 0) {
                    sendMessageToast("读取串口通信参数成功");
                    long tmp = 0;
                    for (int j = 0; j < 3; j++) {
                        tmp = (tmp * 256) + (result[j + 4] & 0xff);
                    }
                    hashMap.put(MapParams.波特率, String.valueOf(tmp));
                    hashMap.put(MapParams.停止位, String.valueOf(result[result.length - 3] & 0xff));
                    hashMap.put(MapParams.校验方式, String.valueOf(result[result.length - 2] & 0xff));
                    setDuQu(hashMap);
                } else {
                    sendMessageToast("读取串口通信参数失败");
                }
                break;
            case 0x04:
                if (result[3] == 0) {
                    if (isAllFourSetting) {
                        snBlueToothTool.write(FourNumBlueToothUtil.getFourPinlvBytes(pinlvSelect.getSelectedItem().toString().trim()), "设置发送频率" + pinlvSelect.getSelectedItem().toString().trim());
                    }
                } else {
                    sendMessageToast("设置网络交互失败");
                }
                break;
            case 0x07:
                if (result[3] == 0) {
                    if (isAllFourSetting) {
                        isReadAllSuccess = true;
                        try {
                            snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sInstructDiscern).getFourWriteBytes(), "识别模块中...", true, 6000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    sendMessageToast("设置发送频率失败");
                }
                break;
            case 0x0d:
                isJiHuoAndXiuMian = false;
                if (result[3] == 0) {
                    sendMessageToast("休眠成功");
                } else {
                    sendMessageToast("休眠失败");
                }
                break;
            case 0x11:
                try {
                    type = DataParser.getModuleType(result);
                    initSnAgreement(Double.valueOf(LouShanYunUtils.getHardWareVersion(Integer.valueOf("6"))));
                    if (type == 4) {
                        hashMap.putAll(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sInstructDiscern).parseFourRNotifyBytes(result));
                        snBlueToothTool.write(FourNumBlueToothUtil.getFourCKReadBytes(), "正在读取4号模组串口信息");
                    } else {
                        sendMessageToast("识别不是4号模组，请选择正确的模组");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    sendMessageToast("识别失败");
                }
                break;
            case 0x0c:
                if (result[3] == 0) {
                    if (isJiHuo) {
                        runOnUiThread(() -> {
                            //未激活
                            systemStatus.setVisibility(View.VISIBLE);
                            systemStatus.setImageDrawable(getResources().getDrawable(R.drawable.l_loushanyun_active_disable));
                            isJiHuo = false;
                            try {
                                isReadAllSuccess = true;
                                snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sInstructDiscern).getFourWriteBytes(), "识别模块中...", true, 6000);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                    } else {
                        runOnUiThread(() -> {
                            //已激活
                            systemStatus.setVisibility(View.VISIBLE);
                            systemStatus.setImageDrawable(getResources().getDrawable(R.drawable.l_loushanyun_active_enable));
                            isJiHuo = true;
                            if (isJiHuoAndXiuMian) {
                                close();
                            }
                        });
                    }
                }
                break;
            case 0x0e:
                if (result[3] == 0) {
                    stringBufferThird.append(ByteConvertUtils.byteToString(result, 4, 1, false) + "\n");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            thirdTextRead.setText(stringBufferThird.toString());
                        }
                    });
                } else {
                    sendMessageToast("发送第三方命令失败");
                }
                break;
            case 0x0f:
                if (result[3] == 0) {
                    sendMessageToast("第" + (stringsXuLieIndex + 1) + "条设置成功");
                    stringsXuLieIndex++;
                    if (stringsXuLieIndex < stringsXuLie.length) {
                        snBlueToothTool.write(FourNumBlueToothUtil.getFourOFBytes(stringsXuLie[stringsXuLieIndex]), "正在配置第" + (stringsXuLieIndex + 1) + "个");
                    } else {
                        sendMessageToast("配置成功");
                    }
                } else {
                    sendMessageToast("第" + (stringsXuLieIndex + 1) + "条设置失败");
                }
                break;
            case 0x22:
                if (result[3] == 0) {
                    xuLieLength = result[4] & 0xff;
                    int num = 0;
                    for (int j = 2; j > 0; j--) {
                        num = (num * 256) + (result[j + 4] & 0xff);
                    }
                    int finalNum = num;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textBanben.setText("自定义协议版本号：" + finalNum);
                            fourSettingReadDataSuccess = true;
                            snBlueToothTool.write(FourNumBlueToothUtil.getFour24Bytes(), "正在读取自定义序列");
                        }
                    });
                } else {
                    sendMessageToast("第三方协议序列未配置");
                }
                break;
            case 0x23:
                xuLieIndex++;
                if (result[3] == 0) {
                    stringBufferXuLie.append(ByteConvertUtils.byteToString(result, 4, 1, false) + "\n");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            xulieTextRead.setText(stringBufferXuLie.toString());
                        }
                    });
                } else {
                    sendMessageToast("读取失败");
                }
                if (xuLieIndex == xuLieLength) {
                    sendMessageToast("读取自定义序列数据完成");
                    xuLieIndex = 0;
                } else {
                    LoadingDialogUtil.showByEvent("读取自定义序列数据中...", loadingTag);
                }
                break;
            case 0x24:
                xuLieIndex++;
                if (result[3] == 0) {
                    stringBufferXuLieData.append(ByteConvertUtils.byteToString(result, 4, 1, false) + "\n");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            xulieDataText.setText(stringBufferXuLieData.toString());
                        }
                    });
                } else {
                    sendMessageToast("读取失败");
                }
                if (xuLieIndex == xuLieLength) {
                    sendMessageToast("读取自定义序列完成");
                    snBlueToothTool.write(FourNumBlueToothUtil.getFour23Bytes(), "正在通过自定义序列读取第三方数据");
                    xuLieIndex = 0;
                } else {
                    LoadingDialogUtil.showByEvent("读取自定义序列中...", loadingTag);
                }
                break;
            default:
                sendMessageToast("命令与模块不对应，请重新选择模块");
        }
    }

    private void initSnAgreement(double hardVersion) {
        sAtInstructRealizeFactory.initHardVersion(hardVersion);
    }

    private void clearAllAtInfo() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                atTextInfo.setText("");
                textHuoqu.setText("");
            }
        });

    }

    @Override
    protected void initEventListener() {
        registerEventRunner(LouShanYunCode.MChipGetNewsInfoRunner, new MChipGetNewsInfoRunner());
    }

    @Override
    protected void onEventRunEnd(Event event, int code) {
        if (code == LouShanYunCode.MChipGetNewsInfoRunner) {
            if (event.isSuccess()) {
                NewInfo newInfo = (NewInfo) event.getReturnParamAtIndex(0);
                textHuoquNew.setText("");
                StringBuffer stringBuffer = new StringBuffer();
                for (int i = 0; i < newInfo.getData().size(); i++) {
                    if (i < 2) {
                        stringBuffer.append("时间:" + TimeUtils.milliseconds2String(newInfo.getData().get(i).getSendTime()) + "；");
                        stringBuffer.append("信号强度:" + newInfo.getData().get(i).getRssi() + "；");
                        stringBuffer.append("信噪比:" + newInfo.getData().get(i).getSnr() + "；\n");
                    }
                }
                textHuoquNew.setText(stringBuffer.toString());
            }
        }

    }

    private void setDuQu(HashMap<String, String> hashMap) {
        StringBuilder sb = new StringBuilder();
        sb.append("厂家标识:  ");
        sb.append(hashMap.get(MapParams.厂家标识));
        sb.append("\n状态:  ");
        sb.append(FourNumBlueToothUtil.getZTReadStringByCode(hashMap.get(MapParams.状态)).toString());
        sb.append("\n出厂时间:  ");
        sb.append("20" + hashMap.get(MapParams.出厂时间_年) + "-" + hashMap.get(MapParams.出厂时间_月) + "-" + hashMap.get(MapParams.出厂时间_日));
        sb.append("\n设备ID:  ");
        sb.append(hashMap.get(MapParams.设备ID));
        sb.append("\n采集场景:  ");
        sb.append(LouShanYunUtils.getCJCJReadStringByCode(hashMap.get(MapParams.采集场景)));
        sb.append("\n网络交互:  ");
        sb.append(hashMap.get(MapParams.网络交互));
        sb.append("\n产品形式:  ");
        sb.append(LouShanYunUtils.getCPXSReadStringByCode(hashMap.get(MapParams.产品形式)));
        sb.append("\n传感模式:  ");
        sb.append(LouShanYunUtils.getCGMSReadStringByCode(hashMap.get(MapParams.传感模式)));
        sb.append("\n传感信号:  ");
        sb.append(LouShanYunUtils.getCGXHReadStringByCode(hashMap.get(MapParams.传感信号)));
        sb.append("\n电源类型:  ");
        sb.append(LouShanYunUtils.getDYLXReadStringByCode(hashMap.get(MapParams.电源类型)));
        sb.append("\n硬件版本:  ");
        sb.append(LouShanYunUtils.getHardWareVersion(Integer.valueOf(hashMap.get(MapParams.硬件版本))));
        sb.append("\n软件版本:  ");
        sb.append(LouShanYunUtils.getSoftWareVersion(Integer.valueOf(hashMap.get(MapParams.软件版本))));
        sb.append("\n发送频率:  ");
        sb.append(LouShanYunUtils.getFourFSPLReadStringByString(hashMap.get(MapParams.发送频率)));
        sb.append("\n波特率:  ");
        sb.append(hashMap.get(MapParams.波特率) + "bps");
        sb.append("\n停止位:  ");
        sb.append(LouShanYunUtils.getFourTZWReadStringByString(hashMap.get(MapParams.停止位)));
        sb.append("\n校验方式:  ");
        sb.append(LouShanYunUtils.getFourJYFSReadStringByString(hashMap.get(MapParams.校验方式)));
        runOnUiThread(() -> {
            if ("1".equals(hashMap.get(MapParams.激活状态))) {
                runOnUiThread(() -> {
                    isJiHuo = true;
                    //已激活
                    systemStatus.setVisibility(View.VISIBLE);
                    systemStatus.setImageDrawable(getResources().getDrawable(R.drawable.l_loushanyun_active_enable));
                });
            } else {
                runOnUiThread(() -> {
                    //未激活
                    isJiHuo = false;
                    systemStatus.setVisibility(View.VISIBLE);
                    systemStatus.setImageDrawable(getResources().getDrawable(R.drawable.l_loushanyun_active_disable));
                });
            }
            fourTextInfo.setText(sb.toString());
        });
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
        sb.append("\n固件版本号:  ");
        sb.append(hashMap.get(SAtInstructParams.sAtInstructFirmwareVersion));
        runOnUiThread(() -> {
//            if (!switchSaveStatus.isChecked()) {
//                if (!XHStringUtil.isEmpty(hashMap.get(MapParams.发送功率), false)) {
//                    if ("20dbm".equals(hashMap.get(SAtInstructParams.sAtInstructSendingPower))) {
//                        atFasonggonglvSelect.setSelection(0);
//                    } else if ("18dbm".equals(hashMap.get(SAtInstructParams.sAtInstructSendingPower))) {
//                        atFasonggonglvSelect.setSelection(1);
//                    } else if ("16dbm".equals(hashMap.get(SAtInstructParams.sAtInstructSendingPower))) {
//                        atFasonggonglvSelect.setSelection(2);
//                    }
//                }
//
//                if (!XHStringUtil.isEmpty(hashMap.get(SAtInstructParams.sAtInstructSpreadingFactor), false)) {
//                    for (int i = 0; i < kuopinyinziArray.length; i++) {
//                        if (kuopinyinziArray[i].equals(hashMap.get(SAtInstructParams.sAtInstructSpreadingFactor))) {
//                            atKuopinyinziSelect.setSelection(i);
//                        }
//                    }
//                }
//                atXindaocanshuSelect.setSelection(sAtInstructRealizeFactory.getMoShiIndex(hashMap.get(SAtInstructParams.sAtInstructChannel)));
//            }
            atTextInfo.setText(sb.toString());
        });
    }

    @Override
    public void onChildWriteSuccess() {

    }

    @Override
    public void onChildWriteFailure(int i) {

    }

}
