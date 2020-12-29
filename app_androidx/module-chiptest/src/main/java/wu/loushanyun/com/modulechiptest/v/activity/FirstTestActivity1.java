package wu.loushanyun.com.modulechiptest.v.activity;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import androidx.cardview.widget.CardView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.elvishew.xlog.XLog;
import com.flyco.roundview.RoundLinearLayout;
import com.flyco.roundview.RoundTextView;
import com.gpsdk.demo.DeviceConnFactoryManager;
import com.sensoro.sensor.kit.entity.SensoroDevice;
import com.wu.loushanyun.base.config.K;
import com.wu.loushanyun.base.print.PrintTool;
import com.wu.loushanyun.base.util.DataParser;
import com.wu.loushanyun.base.util.LouShanYunUtils;
import com.wu.loushanyun.base.util.MapParams;
import com.wu.loushanyun.basemvp.m.sat.SAtInstructRealizeFactory;
import com.wu.loushanyun.basemvp.m.sat.util.FirstNumBlueToothUtil;
import com.wu.loushanyun.basemvp.m.sat.util.FourNumBlueToothUtil;
import com.wu.loushanyun.basemvp.m.sat.util.SAtInstructParams;
import com.wu.loushanyun.basemvp.v.activity.BaseSnBlueToothActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.dialog.LoadingDialogUtil;
import met.hx.com.base.base.share.Share2;
import met.hx.com.base.base.share.ShareContentType;
import met.hx.com.base.baseevent.Event;
import met.hx.com.librarybase.some_utils.TimeUtils;
import met.hx.com.librarybase.some_utils.XHStringUtil;
import wu.loushanyun.com.modulechiptest.R;
import wu.loushanyun.com.modulechiptest.init.ChipCode;
import wu.loushanyun.com.modulechiptest.m.NewInfo;
import wu.loushanyun.com.modulechiptest.p.runner.MChipGetNewsInfoRunner;
import wu.loushanyun.com.modulechiptest.p.runner.MChipSetRxDelayRunner;

import static com.wu.loushanyun.basemvp.init.SnBlueToothTool.BLUE_SEND_TIME_OUT;

@Route(path = K.FirstTestActivity1)
public class FirstTestActivity1 extends BaseSnBlueToothActivity {
    private TextView textPrint;
    private EditText editPrintNum;
    private RoundTextView textPrintCoon;
    private TextView textToken;
    private TextView textDangqianSn;
    private RoundTextView bluetoothConn;
    private RoundTextView buttonDayin;
    private RoundTextView bluetoothDisconn;
    private CardView cardViewProduct;
    private RoundTextView roundProductOneSetting;
    private TextView textInfo;
    private ImageView systemStatus;
    private RoundLinearLayout roundTest1;
    private RoundLinearLayout roundTest2;
    private Spinner wangluojiaohuSelect;
    private RoundTextView firstWangluojiaohuSetting;
    private RoundTextView firstOneSetting;
    private RoundTextView firstOneReading;
    private TextView firstTextInfo;
    private RoundLinearLayout roundTest3;
    private EditText editXinhaoqiangduStart;
    private EditText editXinhaoqiangduEnd;
    private EditText editXinzaobiStart;
    private EditText editXinzaobiEnd;
    private TextView textHuoqu;
    private RoundTextView buttonQiangzhifasong;
    private RoundTextView buttonDuquXinhao;
    private TextView textHuoquNew;
    private RoundTextView buttonDuquNew;
    private RoundLinearLayout roundTest4;
    private RoundTextView buttonJizhongqi;
    private RoundTextView roundTextXiumian;

    private Spinner atFasonggonglvSelect;
    private RoundTextView atFasonggonglvSetting;
    private Spinner atXindaocanshuSelect;
    private RoundTextView atXindaocanshuSetting;
    private Spinner atKuopinyinziSelect;
    private RoundTextView atKuopinyinziSetting;
    private EditText atEditRxdelay;
    private RoundTextView atRxdelaySetting;
    private RoundTextView atOneSetting;
    private RoundTextView atOneReading;
    private TextView atTextInfo;

    private PrintTool printTool;
    private HashMap<String, String> hashMap;
    private String[] kuopinyinziArray;
    private SAtInstructRealizeFactory sAtInstructRealizeFactory;

    private SensoroDevice sensoroDeviceChoose;
    private boolean isAllReading;
    private int type;
    private boolean isAllSetting;
    private boolean isAllFirstSetting;
    private boolean isAllFirstReading;
    private Double softVersion;

    private int jumpType;
    private int typeJiHuo = 2;//判断底板是否激活 1已激活  0未激活

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        super.onInitAttribute(ba);
        getAllData();
        ba.mTitleText = "1号模组1.07以上程序";
    }

    private void getAllData() {
        jumpType = getIntent().getIntExtra("jumpType", ChipCode.TypeFromTest);
    }

    @Override
    protected void initLifeCycle() {
        super.initLifeCycle();
        printTool = new PrintTool(1, new PrintTool.PrintListener() {
            @Override
            public void onUsbPermission(Intent intent) {
                synchronized (this) {
                    UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if (device != null) {
                            System.out.println("permission ok for device " + device);
                        }
                    } else {
                        System.out.println("permission denied for device " + device);
                    }
                }
            }

            @Override
            public void onUsbDeviceDetached(Intent intent) {
            }

            @Override
            public void onAclDisconnected(Intent intent) {
            }

            @Override
            public void onConnectionState(Intent intent) {
                int state = intent.getIntExtra(DeviceConnFactoryManager.STATE, -1);
                int deviceId = intent.getIntExtra(DeviceConnFactoryManager.DEVICE_ID, -1);
                switch (state) {
                    case DeviceConnFactoryManager.CONN_STATE_DISCONNECT:
                        textPrint.setText("打印机未连接");
                        break;
                    case DeviceConnFactoryManager.CONN_STATE_CONNECTING:
                        sendMessageToast(getString(com.wu.loushanyun.R.string.str_conn_state_connecting));
                        break;
                    case DeviceConnFactoryManager.CONN_STATE_CONNECTED:
                        sendMessageToast(getString(com.wu.loushanyun.R.string.str_conn_state_connected) + "\n" + printTool.getConnDeviceInfo());
                        textPrint.setText("打印机已连接");
                        break;
                    case DeviceConnFactoryManager.CONN_STATE_FAILED:
                        sendMessageToast(getString(com.wu.loushanyun.R.string.str_conn_fail));
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onConnectionStateDisconnect(Intent intent) {
            }

            @Override
            public void onConnectionStateConnecting(Intent intent) {
            }

            @Override
            public void onConnectionStateConnected(Intent intent) {
            }

            @Override
            public void onConnectionStateFailed(Intent intent) {
            }

            @Override
            public void onQueryPrinterState(Intent intent) {
            }
        });
        registerLifeCycle(printTool);
    }

    @Override
    protected void initView() {
        super.initView();
        textPrint = (TextView) findViewById(R.id.text_print);
        editPrintNum = (EditText) findViewById(R.id.edit_print_num);
        textPrintCoon = (RoundTextView) findViewById(R.id.text_print_coon);
        textToken = (TextView) findViewById(R.id.text_token);
        textDangqianSn = (TextView) findViewById(R.id.text_dangqian_sn);
        bluetoothConn = (RoundTextView) findViewById(R.id.bluetooth_conn);
        buttonDayin = (RoundTextView) findViewById(R.id.button_dayin);
        bluetoothDisconn = (RoundTextView) findViewById(R.id.bluetooth_disconn);
        cardViewProduct = (CardView) findViewById(R.id.card_view_product);
        roundProductOneSetting = (RoundTextView) findViewById(R.id.round_product_one_setting);
        textInfo = (TextView) findViewById(R.id.text_info);
        systemStatus = (ImageView) findViewById(R.id.system_status);
        roundTest1 = (RoundLinearLayout) findViewById(R.id.round_test1);
        roundTest2 = (RoundLinearLayout) findViewById(R.id.round_test2);
        wangluojiaohuSelect = (Spinner) findViewById(R.id.wangluojiaohu_select);
        firstWangluojiaohuSetting = (RoundTextView) findViewById(R.id.first_wangluojiaohu_setting);
        firstOneSetting = (RoundTextView) findViewById(R.id.first_one_setting);
        firstOneReading = (RoundTextView) findViewById(R.id.first_one_reading);
        firstTextInfo = (TextView) findViewById(R.id.first_text_info);
        roundTest3 = (RoundLinearLayout) findViewById(R.id.round_test3);
        editXinhaoqiangduStart = (EditText) findViewById(R.id.edit_xinhaoqiangdu_start);
        editXinhaoqiangduEnd = (EditText) findViewById(R.id.edit_xinhaoqiangdu_end);
        editXinzaobiStart = (EditText) findViewById(R.id.edit_xinzaobi_start);
        editXinzaobiEnd = (EditText) findViewById(R.id.edit_xinzaobi_end);
        textHuoqu = (TextView) findViewById(R.id.text_huoqu);
        buttonQiangzhifasong = (RoundTextView) findViewById(R.id.button_qiangzhifasong);
        buttonDuquXinhao = (RoundTextView) findViewById(R.id.button_duqu_xinhao);
        textHuoquNew = (TextView) findViewById(R.id.text_huoqu_new);
        buttonDuquNew = (RoundTextView) findViewById(R.id.button_duqu_new);
        roundTest4 = (RoundLinearLayout) findViewById(R.id.round_test4);
        buttonJizhongqi = (RoundTextView) findViewById(R.id.button_jizhongqi);
        roundTextXiumian = (RoundTextView) findViewById(R.id.round_text_xiumian);

        atFasonggonglvSelect = (Spinner) findViewById(R.id.at_fasonggonglv_select);
        atFasonggonglvSetting = (RoundTextView) findViewById(R.id.at_fasonggonglv_setting);
        atXindaocanshuSelect = (Spinner) findViewById(R.id.at_xindaocanshu_select);
        atXindaocanshuSetting = (RoundTextView) findViewById(R.id.at_xindaocanshu_setting);
        atKuopinyinziSelect = (Spinner) findViewById(R.id.at_kuopinyinzi_select);
        atKuopinyinziSetting = (RoundTextView) findViewById(R.id.at_kuopinyinzi_setting);
        atEditRxdelay = (EditText) findViewById(R.id.at_edit_rxdelay);
        atRxdelaySetting = (RoundTextView) findViewById(R.id.at_rxdelay_setting);
        atOneSetting = (RoundTextView) findViewById(R.id.at_one_setting);
        atOneReading = (RoundTextView) findViewById(R.id.at_one_reading);
        atTextInfo = (TextView) findViewById(R.id.at_text_info);
        atEditRxdelay.setText("1");

        if (jumpType == ChipCode.TypeFromFactory) {
            cardViewProduct.setVisibility(View.VISIBLE);
            roundTest1.setVisibility(View.GONE);
            roundTest2.setVisibility(View.GONE);
            roundTest3.setVisibility(View.GONE);
            roundTest4.setVisibility(View.GONE);
        } else {
            cardViewProduct.setVisibility(View.GONE);
            roundTest1.setVisibility(View.VISIBLE);
            roundTest2.setVisibility(View.VISIBLE);
            roundTest3.setVisibility(View.VISIBLE);
            roundTest4.setVisibility(View.VISIBLE);
        }
        hashMap = new HashMap<>();
        kuopinyinziArray = getResources().getStringArray(R.array.m_chip_kuopinyinzi);

        bluetoothConn.setOnClickListener(v -> {
            if (textDangqianSn.getText().toString().contains("当前连接的SN设备")) {
                sendMessageToast("当前已连接设备");
                return;
            }
            snBlueToothTool.connectBlueTooth(sensoroDeviceChoose);
            textHuoqu.setText("");
            atTextInfo.setText("");
        });
        bluetoothDisconn.setOnClickListener(v -> {
            snBlueToothTool.disconnect();
            textDangqianSn.setText("当前未连接SN设备");
        });
        sAtInstructRealizeFactory = new SAtInstructRealizeFactory();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.l_loushanyun_item_spinner, sAtInstructRealizeFactory.getAllMoShi());
        atXindaocanshuSelect.setAdapter(arrayAdapter);
        ArrayAdapter<String> arrayAdapterJiaoHu = new ArrayAdapter<String>(this, R.layout.l_loushanyun_item_spinner, FourNumBlueToothUtil.getAllWangLuoJiaoHu());
        wangluojiaohuSelect.setAdapter(arrayAdapterJiaoHu);
        atKuopinyinziSelect.setSelection(3);
        initTestClick();
    }

    private void initTestClick() {
        atXindaocanshuSetting.setOnClickListener(this::OnBlueToothClick);
        atKuopinyinziSetting.setOnClickListener(this::OnBlueToothClick);
        atFasonggonglvSetting.setOnClickListener(this::OnBlueToothClick);
        atRxdelaySetting.setOnClickListener(this::OnBlueToothClick);
        atOneSetting.setOnClickListener(this::OnBlueToothClick);
        atOneReading.setOnClickListener(this::OnBlueToothClick);

        buttonQiangzhifasong.setOnClickListener(this::OnBlueToothClick);
        buttonDuquXinhao.setOnClickListener(this::OnBlueToothClick);
        roundTextXiumian.setOnClickListener(this::OnBlueToothClick);
        roundProductOneSetting.setOnClickListener(this::OnBlueToothClick);
        firstOneSetting.setOnClickListener(this::OnBlueToothClick);
        firstOneReading.setOnClickListener(this::OnBlueToothClick);
        firstWangluojiaohuSetting.setOnClickListener(this::OnBlueToothClick);
        buttonJizhongqi.setOnClickListener(this::OnBlueToothClick);

        buttonDayin.setOnClickListener(this::OnClick);
        textPrintCoon.setOnClickListener(this::OnClick);
        buttonDuquNew.setOnClickListener(this::OnClick);
        textToken.setOnClickListener(this::OnClick);
        textToken.setOnClickListener(this::OnClick);
    }


    private void OnClick(View view) {
        switch (view.getId()) {
            case R.id.button_dayin:
                if (!XHStringUtil.isEmpty(sensoroDeviceChoose.sn, false)) {
                    LoadingDialogUtil.showByEvent("正在打印", this.loadingTag);
                    printTool.printBitmap(printTool.createChipPrint2(sensoroDeviceChoose.sn));
                    LoadingDialogUtil.dismissByEvent(this.loadingTag);
                }
                break;
            case R.id.text_print_coon:
                printTool.showPrintDialog();
                break;
            case R.id.button_duqu_new:
                pushEvent(ChipCode.MChipGetNewsInfoRunner, sensoroDeviceChoose.sn, "1", "5");
                break;
            case R.id.text_token:
                String text = sensoroDeviceChoose.sn + ";" + hashMap.get(SAtInstructParams.sAtInstructToken);
                //获取剪贴板管理器
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 创建普通字符型ClipData
                ClipData mClipData = ClipData.newPlainText("Label", text);
                // 将ClipData内容放到系统剪贴板里。
                cm.setPrimaryClip(mClipData);
                sendMessageToast("已复制到剪切板");
                new Share2.Builder(this)
                        .setContentType(ShareContentType.TEXT)
                        .setTitle("打印SN")
                        .setTextContent(text)
                        .setOnActivityResult(1000)
                        .build()
                        .shareBySystem();
                break;
        }
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
        if (Double.valueOf(LouShanYunUtils.getHardWareVersion(Integer.valueOf(hashMap.get(MapParams.硬件版本)))) < 1.07) {
            sendMessageToast("识别1号模组版本小于1.07请升级固件后再试");
            return;
        }
        if (type != 1) {
            sendMessageToast("识别不是1号模组，请选择正确的模组");
            return;
        }
        switch (view.getId()) {
            case R.id.at_xindaocanshu_setting:
                try {
                    snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructChannel).getFourWriteBytes(atXindaocanshuSelect.getSelectedItem().toString().trim()), "设置信道");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                isAllSetting = false;
                break;
            case R.id.at_kuopinyinzi_setting:
                try {
                    snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructSpreadingFactor).getFourWriteBytes(atKuopinyinziSelect.getSelectedItem().toString().trim()), "设置扩频因子");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                isAllSetting = false;
                break;
            case R.id.at_fasonggonglv_setting:
                try {
                    snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructSendingPower).getFourWriteBytes(atFasonggonglvSelect.getSelectedItem().toString().trim()), "设置发送功率");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                isAllSetting = false;
                break;
            case R.id.at_rxdelay_setting:
                if (Integer.valueOf(atEditRxdelay.getText().toString().trim()) > 5 || Integer.valueOf(atEditRxdelay.getText().toString().trim()) < 1) {
                    sendMessageToast("只能设置1-5范围内");
                    return;
                }
                pushEvent(ChipCode.MChipSetRxDelayRunner, sensoroDeviceChoose.sn, atEditRxdelay.getText().toString().trim());
                break;
            case R.id.at_one_reading:
                try {
                    snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructChannel).getFourReadBytes(), "读取信道中...");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                isAllReading = true;
                atTextInfo.setText("");
                break;
            case R.id.at_one_setting:
                try {
                    if (Integer.valueOf(atEditRxdelay.getText().toString().trim()) > 5 || Integer.valueOf(atEditRxdelay.getText().toString().trim()) < 1) {
                        sendMessageToast("只能设置1-5范围内");
                        return;
                    }
                    snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructChannel).getFourWriteBytes(atXindaocanshuSelect.getSelectedItem().toString().trim()), "设置信道");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                isAllSetting = true;
                break;
/**-------------------------------------------------------------------------------------------------------------------------------------------*/
            case R.id.round_text_xiumian:
                snBlueToothTool.write(DataParser.getSystemSleepCMD(), "正在休眠");
                break;
            case R.id.button_qiangzhifasong:
                textHuoqu.setText("");
                try {
                    snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructForcedSend).getTwoReadBytes("15527919058")
                            , "强制发送", true, BLUE_SEND_TIME_OUT);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.button_duqu_xinhao:
                try {
                    snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructRSSI).getTwoReadBytes(), "读取信号强度");
                    isAllReading = false;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
/**-------------------------------------------------------------------------------------------------------------------------------------------*/
            case R.id.first_one_setting:
                isAllFirstSetting = true;
                byte[] input2 = {0x68, 0x02, 0x00, 0x40, 0x42, 0x16};
                snBlueToothTool.write(input2, "设置采集场景中...");
                firstTextInfo.setText("");
                break;
            case R.id.first_wangluojiaohu_setting:
                isAllFirstSetting = false;
                snBlueToothTool.write(FirstNumBlueToothUtil.getFouWangluoJiaoHuBytes(wangluojiaohuSelect.getSelectedItem().toString().replaceAll("最大发送", "").replaceAll("次", "")), "正在设置网络交互");
                firstTextInfo.setText("");
                break;
            case R.id.first_one_reading:
                try {
                    isAllFirstReading = false;
                    snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sInstructDiscern).getFourWriteBytes(), "识别模块中...", true, 6000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                firstTextInfo.setText("");
                break;
            case R.id.button_jizhongqi:
                ARouter.getInstance().build(K.FirstJiZhongQiActivity).withParcelable("sensoroDevice", sensoroDeviceChoose).navigation();
                break;
/**-------------------------------------------------------------------------------------------------------------------------------------------*/
            case R.id.round_product_one_setting:
                snBlueToothTool.write(DataParser.getSystemStatusSettingCMD(false), "关闭激活中");
                textInfo.setText("");
                break;
        }
    }

    @Override
    protected void onClickBlueListListener(SensoroDevice sensoroDevice) {
        super.onClickBlueListListener(sensoroDevice);
        this.sensoroDeviceChoose = sensoroDevice;
    }

    @Override
    protected int onChildLayout() {
        return R.layout.m_chip_activity_first1;
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
                textDangqianSn.setText("当前连接的SN设备:" + sensoroDeviceChoose.getSerialNumber().toUpperCase());
                try {
                    isAllFirstReading = true;
                    snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sInstructDiscern).getFourWriteBytes(), "识别模块中...", true, 6000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                int printNum = Integer.valueOf(editPrintNum.getText().toString());
                for (int i = 0; i < printNum; i++) {
                    printTool.printBitmap(printTool.createChipPrint2(sensoroDeviceChoose.sn));
                }
            }
        });

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
                                pushEventNoProgress(ChipCode.MChipGetNewsInfoRunner, sensoroDeviceChoose.sn, "1", "5");
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
                    } else if (resultAt.equals(SAtInstructParams.sAtInstructToken + "-R")) {
                        try {
                            hashMap.putAll(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructToken).parseFourRNotifyBytes(result));
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    textToken.setText("当前设备Token和SN(点击复制到剪贴板):\n" + "SN:" + sensoroDeviceChoose.sn + "\nToken:" + hashMap.get(SAtInstructParams.sAtInstructToken));
//                                try {
//                                    snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructChannel).getFourReadBytes(), "读取信道中...");
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
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
                            pushEvent(ChipCode.MChipSetRxDelayRunner, sensoroDeviceChoose.sn, atEditRxdelay.getText().toString().trim());
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
                case 0x00:
                    if (result[3] == 0) {
                        if (isAllFirstSetting) {
                            byte[] bytes = {0x68, 0x02, 0x01, 0x30, 0x33, 0x16};
                            snBlueToothTool.write(bytes, "设置发送频率，24小时");
                        } else {
                            sendMessageToast("设置采集场景成功");
                        }
                    } else {
                        sendMessageToast("设置采集场景失败");
                    }
                    break;
                case 0x01:
                    if (result[3] == 0) {
                        if (isAllFirstSetting) {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            String time = sdf.format(new Date());
                            String[] split = time.split("-");
                            String year = split[0].substring(split[0].length() - 2, split[0].length());
                            String month = split[1].toString();
                            String day = split[2].toString();
                            byte[] bytes = {0x68, 0x04, 0x03,
                                    (byte) (Integer.parseInt(year) & 0xff),
                                    (byte) (Integer.parseInt(month) & 0xff),
                                    (byte) (Integer.parseInt(day) & 0xff),
                                    0x07, 0x16};
                            bytes[bytes.length - 2] += bytes[3];
                            bytes[bytes.length - 2] += bytes[4];
                            bytes[bytes.length - 2] += bytes[5];
                            snBlueToothTool.write(bytes, "正在设置模组出厂日期");
                        } else {
                            sendMessageToast("设置发送频率成功");
                        }
                    } else {
                        sendMessageToast("设置发送频率失败");
                    }
                    break;
                case 0x03:
                    if (result[3] == 0) {
                        if (isAllFirstSetting) {
                            snBlueToothTool.write(FirstNumBlueToothUtil.getFouWangluoJiaoHuBytes(wangluojiaohuSelect.getSelectedItem().toString().replaceAll("最大发送", "").replaceAll("次", "")), "正在设置网络交互");
                        } else {
                            sendMessageToast("设置出厂日期成功");
                        }
                    } else {
                        sendMessageToast("设置出厂日期失败");
                    }
                    break;
                case 0x04:
                    if (result[3] == 0) {
                        if (isAllFirstSetting) {
                            byte[] cmd = {0x68, 0x02, 0x05, 0x01, 0x08, 0x16};
                            snBlueToothTool.write(cmd, "正在设置工作模式");
                        } else {
                            try {
                                snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sInstructDiscern).getFourWriteBytes(), "识别模块中...", true, 6000);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            sendMessageToast("设置网络交互成功");
                        }
                    } else {
                        sendMessageToast("设置网络交互失败");
                    }
                    break;
                case 0x05:
                    if (result[3] == 0) {
                        if (isAllFirstSetting) {
                            byte[] d = new byte[7];
                            d[0] = (byte) 0x68;
                            d[1] = (byte) 0x03;//有效数据
                            d[2] = (byte) 0x06;//命令
                            d[3] = (byte) (98 & 0xff);
                            d[4] = (byte) (98 & 0xff00);
                            byte cs = 0;
                            for (int i = 1; i < 5; i++) {
                                cs += d[i];
                            }
                            d[5] = cs;//校验和
                            d[6] = (byte) 0x16;
                            snBlueToothTool.write(d, "正在设置厂家标识");
                        } else {
                            sendMessageToast("设置工作模式成功");
                        }

                    } else {
                        sendMessageToast("设置工作模式失败");
                    }
                    break;
                case 0x06:
                    if (result[3] == 0) {
                        if (isAllFirstSetting) {
                            sendMessageToast("设置成功");
                        } else {
                            sendMessageToast("设置厂家标识成功");
                        }
                    } else {
                        sendMessageToast("设置厂家标识失败");
                    }
                    break;
                //设置休眠
                case 0x24:
                    if (result[3] == 0) {
                        sendMessageToast("休眠成功");
                    } else {
                        sendMessageToast("休眠失败");
                    }
                    break;
                case 0x11:
                    try {
                        type = DataParser.getModuleType(result);
                        hashMap.putAll(DataParser.getInformationAll(result));
                        softVersion = Double.valueOf(LouShanYunUtils.getSoftWareVersion(Integer.valueOf(hashMap.get(MapParams.软件版本))));
                        initSnAgreement(softVersion);
                        if (type == 1) {
                            if (softVersion >= 1.07) {
                                if (jumpType == ChipCode.TypeFromFactory) {
                                    snBlueToothTool.write(DataParser.getSystemStatusSettingCMD(false), "关闭激活中");
                                    textInfo.setText("");
                                } else {
                                    if (isAllFirstReading) {
                                        snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructToken).getFourReadBytes(), "读取Token...");
                                    }
                                }
                            } else {
                                sendMessageToast("请将固件升级到1.07版本");
                            }
                        } else {
                            sendMessageToast("识别不是1号模组，请选择正确的模组");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        sendMessageToast("识别失败");
                    }
                    setDuQu();
                    break;
                //设置激活停用
                case 0x23:
                    if (result[3] == 0) {
                        //起末表号的设置
                        HashMap<String, String> parmas = new HashMap<>();
                        parmas.put(MapParams.总线起止表号_起, String.valueOf(1));
                        parmas.put(MapParams.总线起止表号_止, String.valueOf(64));
                        parmas.put(MapParams.仪表通信号, "0");
                        parmas.put(MapParams.初始化表计状态, "0");
                        parmas.put(MapParams.倍率, "0");
                        parmas.put(MapParams.安装脉冲底数, "0");
                        parmas.put(MapParams.口径, "0");
                        parmas.put(MapParams.发送频率, "48");
                        parmas.put(MapParams.保留字节, "0");
                        parmas.put(MapParams.设备ID, LouShanYunUtils.getTimeID());
                        snBlueToothTool.write(DataParser.getDiBanBiaoJiChuShiHuaCMD(parmas), "设置表号");
                    }
                    break;
                //设置附加参数
                case 0x21:
                    if (result[3] == 0) {
                        snBlueToothTool.write(DataParser.getDiBanBiaoJiDuQuCMD(), "正在读取激活状态");
                    } else {
                        sendMessageToast("设置失败");
                    }
                    break;
                //读取附加参数
                case 0x31:
                    hashMap.putAll(DataParser.getDiBanBiaoJiInfo(result));
                    try {
                        int meterReadStart = Integer.parseInt(hashMap.get(MapParams.总线起止表号_起));
                        int meterReadEnd = Integer.parseInt(hashMap.get(MapParams.总线起止表号_止));
                        if (result[3] == 0) {
                            if ("1".equalsIgnoreCase(hashMap.get(MapParams.系统状态))) {
                                typeJiHuo = 1;
                            } else {
                                typeJiHuo = 0;
                            }
                            snBlueToothTool.write(DataParser.getSystemSleepCMD(), "正在休眠");
                            StringBuilder sb = new StringBuilder();
                            sb.append("\n硬件版本:  ");
                            sb.append(LouShanYunUtils.getHardWareVersion(Integer.valueOf(hashMap.get(MapParams.硬件版本))));
                            sb.append("\n软件版本:  ");
                            sb.append(LouShanYunUtils.getSoftWareVersion(Integer.valueOf(hashMap.get(MapParams.软件版本))));
                            sb.append("\n起始表号:  ");
                            sb.append(String.valueOf(meterReadStart));
                            sb.append("\n终止表号:  ");
                            sb.append(String.valueOf(meterReadEnd));
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    textInfo.setText(sb.toString());
                                    if (typeJiHuo == 1) {
                                        //已激活
                                        systemStatus.setVisibility(View.VISIBLE);
                                        systemStatus.setImageDrawable(getResources().getDrawable(R.drawable.l_loushanyun_active_enable));
                                    } else if (typeJiHuo == 0) {
                                        //未激活
                                        systemStatus.setVisibility(View.VISIBLE);
                                        systemStatus.setImageDrawable(getResources().getDrawable(R.drawable.l_loushanyun_active_disable));
                                    } else {
                                        systemStatus.setVisibility(View.GONE);
                                    }
                                    snBlueToothTool.setConnected(false);
                                    textDangqianSn.setText("当前未连接SN设备");
                                }
                            });
                        }

                    } catch (Exception e) {

                    }
                    break;
        }
    }

    private void initSnAgreement(double hardVersion) {
        sAtInstructRealizeFactory.initHardVersion(hardVersion);
    }

    private void setDuQu() {
        StringBuilder sb = new StringBuilder();
        sb.append("发送频率:  ");
        sb.append(LouShanYunUtils.getFSPLReadStringByCode(Long.valueOf(hashMap.get(MapParams.发送频率))));
        sb.append("\n网络交互:  ");
        sb.append(LouShanYunUtils.getWLJHReadStringByCode(hashMap.get(MapParams.网络交互)));
        sb.append("\n采集场景:  ");
        sb.append(LouShanYunUtils.getCJCJReadStringByCode(hashMap.get(MapParams.采集场景)));
        sb.append("\n硬件版本:  ");
        sb.append(LouShanYunUtils.getHardWareVersion(Integer.valueOf(hashMap.get(MapParams.硬件版本))));
        sb.append("\n软件版本:  ");
        sb.append(LouShanYunUtils.getSoftWareVersion(Integer.valueOf(hashMap.get(MapParams.软件版本))));
        sb.append("\n厂家标识:  ");
        sb.append(hashMap.get(MapParams.厂家标识));
        sb.append("\n模组出厂时间:  ");
        sb.append("20" + hashMap.get(MapParams.出厂时间_年) + "-" + hashMap.get(MapParams.出厂时间_月) + "-" + hashMap.get(MapParams.出厂时间_日));
        runOnUiThread(() -> {
            firstTextInfo.setText(sb.toString());
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
        sb.append("\nLSY-IOT版本号:  ");
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

    @Override
    protected void initEventListener() {
        registerEventRunner(ChipCode.MChipGetNewsInfoRunner, new MChipGetNewsInfoRunner());
        registerEventRunner(ChipCode.MChipSetRxDelayRunner, new MChipSetRxDelayRunner());
    }

    @Override
    protected void onEventRunEnd(Event event, int code) {
        if (code == ChipCode.MChipGetNewsInfoRunner) {
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
        } else if (code == ChipCode.MChipSetRxDelayRunner) {
            if (event.isSuccess()) {
                int codeReturn = (int) event.getReturnParamAtIndex(0);
                String msg = (String) event.getReturnParamAtIndex(1);
                if (codeReturn == 0) {
                    try {
                        snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructRxDelay).getFourWriteBytes(atEditRxdelay.getText().toString().trim()), "设置RxDelay");
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
