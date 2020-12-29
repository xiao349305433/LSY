package com.loushanyun.modulefactory.v.activity;

import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.elvishew.xlog.XLog;
import com.flyco.roundview.RoundTextView;
import com.google.gson.Gson;
import com.gpsdk.demo.DeviceConnFactoryManager;
import com.loushanyun.modulefactory.R;
import com.loushanyun.modulefactory.init.FactoryCode;
import com.loushanyun.modulefactory.init.LoginParamManager;
import com.loushanyun.modulefactory.m.FactorysSettingsModule4;
import com.loushanyun.modulefactory.p.runner.FSSaveModule4FactorySettingRunner;
import com.sensoro.sensor.kit.entity.SensoroDevice;
import com.wu.loushanyun.base.config.K;
import com.wu.loushanyun.base.print.PrintTool;
import com.wu.loushanyun.base.util.DataParser;
import com.wu.loushanyun.base.util.LouShanYunUtils;
import com.wu.loushanyun.base.util.MapParams;
import com.wu.loushanyun.basemvp.init.LouShanYunCode;
import com.wu.loushanyun.basemvp.m.ModuleRule;
import com.wu.loushanyun.basemvp.m.NewInfo;
import com.wu.loushanyun.basemvp.m.sat.SAtInstructRealizeFactory;
import com.wu.loushanyun.basemvp.m.sat.util.FourNumBlueToothUtil;
import com.wu.loushanyun.basemvp.m.sat.util.SAtInstructParams;
import com.wu.loushanyun.basemvp.p.presenter.RuleTool;
import com.wu.loushanyun.basemvp.p.runner.MChipGetNewsInfoRunner;
import com.wu.loushanyun.basemvp.v.activity.BaseSnBlueToothActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.dialog.LoadingDialogUtil;
import met.hx.com.base.base.share.Share2;
import met.hx.com.base.base.share.ShareContentType;
import met.hx.com.base.baseevent.Event;
import met.hx.com.librarybase.some_utils.ByteConvertUtils;
import met.hx.com.librarybase.some_utils.KeyboardUtils;
import met.hx.com.librarybase.some_utils.TimeUtils;
import met.hx.com.librarybase.some_utils.XHStringUtil;
import met.hx.com.librarybase.some_utils.util.AbSharedUtil;

import static com.wu.loushanyun.basemvp.init.SnBlueToothTool.BLUE_SEND_TIME_OUT;

@Route(path = K.LSYModuleNew04Activity)
public class LSYModuleNew04Activity extends BaseSnBlueToothActivity {
    private ScrollView scrollView;
    private TextView textPrint;
    private RoundTextView textPrintCoon;
    private TextView textDangqianSn;
    private RoundTextView bluetoothConn;
    private RoundTextView bluetoothDisconn;
    private RoundTextView xulieTextGetNetwork;
    private EditText editIdStart;
    private RoundTextView roundCreateId;
    private TextView editOtherVersion;
    private Spinner dianyuanSelect;
    private TextView textChanpinxingshi;
    private Spinner chuanganmoshiSelect;
    private Spinner chuanganxinhaoSelect;
    private Spinner spinnerBotelvSelect;
    private Spinner spinnerStopBit;
    private Spinner spinnerJiaoyan;
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
    private RoundTextView xulieTextClearAdd;
    private RoundTextView xulieTextOneSetting;
    private EditText editXulieWrite;
    private RoundTextView xulieRead;
    private RoundTextView xulieTextReset;
    private TextView xulieTextRead;
    private TextView xulieDataText;
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
    private RoundTextView roundTextLsy4diban;


    private SensoroDevice sensoroDeviceChoose;
    private PrintTool printTool;
    private RuleTool ruleTool;

    //默认参数
    private boolean isAllSetting;
    private boolean isAllReading;
    private boolean isJiHuo = true;
    private boolean isJiHuoAndXiuMian = false;
    private boolean isReadAllSuccess = false;

    private int type;
    private boolean canPrint;
    private SAtInstructRealizeFactory sAtInstructRealizeFactory;
    private HashMap<String, String> hashMap;
    private String[] kuopinyinziArray;

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

    private static final String XULIESHARE = "xuLieShare";
    private static final String THIRDSHARE = "thirdDataShare";
    private boolean isAllSettingXuLie = false;

    private boolean fourSettingReadDataSuccess = false;

    private ModuleRule.DataBean dataBean;

    @Override
    protected int onChildLayout() {
        return R.layout.m_factory_activity_four_1;
    }

    @Override
    protected int getThemeColor() {
        return getResources().getColor(R.color.base_Q1);
    }

    @Override
    protected void initLifeCycle() {
        super.initLifeCycle();
        ruleTool = new RuleTool(new RuleTool.OnRuleCallBack() {
            @Override
            public void onRuleBack(String result) {
                editXulieWrite.setText(result);
                String xulie = editXulieWrite.getText().toString();
                if (XHStringUtil.isEmpty(xulie, false)) {
                    sendMessageToast("请在网站上自定义协议");
                    return;
                }
                AbSharedUtil.putString(LSYModuleNew04Activity.this, XULIESHARE, xulie);
                stringsXuLie = xulie.split("\n");
            }

            @Override
            public void onVersionBack(ModuleRule.DataBean dataBean) {
                LSYModuleNew04Activity.this.dataBean = dataBean;
                editOtherVersion.setText(dataBean.getProtocolVersion() + "");
                textChanpinxingshi.setText(LouShanYunUtils.getCPXSReadStringByCode(dataBean.getProductForm()));
            }

        }, getResources().getColor(R.color.base_Q1));
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
        registerLifeCycle(ruleTool);
    }

    @Override
    protected void initView() {
        super.initView();
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        textPrint = (TextView) findViewById(R.id.text_print);
        textPrintCoon = (RoundTextView) findViewById(R.id.text_print_coon);
        textDangqianSn = (TextView) findViewById(R.id.text_dangqian_sn);
        bluetoothConn = (RoundTextView) findViewById(R.id.bluetooth_conn);
        bluetoothDisconn = (RoundTextView) findViewById(R.id.bluetooth_disconn);
        xulieTextGetNetwork = (RoundTextView) findViewById(R.id.xulie_text_get_network);
        editIdStart = (EditText) findViewById(R.id.edit_id_start);
        roundCreateId = (RoundTextView) findViewById(R.id.round_create_id);
        editOtherVersion = (TextView) findViewById(R.id.edit_other_version);
        dianyuanSelect = (Spinner) findViewById(R.id.dianyuan_select);
        textChanpinxingshi = (TextView) findViewById(R.id.text_chanpinxingshi);
        chuanganmoshiSelect = (Spinner) findViewById(R.id.chuanganmoshi_select);
        chuanganxinhaoSelect = (Spinner) findViewById(R.id.chuanganxinhao_select);
        spinnerBotelvSelect = (Spinner) findViewById(R.id.spinner_botelv_select);
        spinnerStopBit = (Spinner) findViewById(R.id.spinner_stop_bit);
        spinnerJiaoyan = (Spinner) findViewById(R.id.spinner_jiaoyan);
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
        xulieTextClearAdd = (RoundTextView) findViewById(R.id.xulie_text_clear_add);
        xulieTextOneSetting = (RoundTextView) findViewById(R.id.xulie_text_one_setting);
        editXulieWrite = (EditText) findViewById(R.id.edit_xulie_write);
        xulieRead = (RoundTextView) findViewById(R.id.xulie_read);
        xulieTextReset = (RoundTextView) findViewById(R.id.xulie_text_reset);
        xulieTextRead = (TextView) findViewById(R.id.xulie_text_read);
        xulieDataText = (TextView) findViewById(R.id.xulie_data_text);
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
        roundTextLsy4diban = (RoundTextView) findViewById(R.id.round_text_lsy4diban);


        thirdEditWrite.setText(AbSharedUtil.getString(this, THIRDSHARE));
        stringBufferXuLie = new StringBuffer();
        stringBufferXuLieData = new StringBuffer();
        hashMap = new HashMap<>();
        stringBufferThird = new StringBuffer();
        kuopinyinziArray = getResources().getStringArray(R.array.l_loushanyun_kuopinyinzi);
        editIdStart.setText(LouShanYunUtils.getTimeID());
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
        initTestClick();
        atKuopinyinziSelect.setSelection(3);
        sAtInstructRealizeFactory = new SAtInstructRealizeFactory();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(LSYModuleNew04Activity.this, R.layout.l_loushanyun_item_spinner, sAtInstructRealizeFactory.getAllMoShi());
        atXindaocanshuSelect.setAdapter(arrayAdapter);
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(LSYModuleNew04Activity.this, R.layout.l_loushanyun_item_spinner, FourNumBlueToothUtil.getAllBoTeLv());
        spinnerBotelvSelect.setAdapter(arrayAdapter2);
        spinnerBotelvSelect.setSelection(3);
        ArrayAdapter<String> arrayAdapter3 = new ArrayAdapter<String>(LSYModuleNew04Activity.this, R.layout.l_loushanyun_item_spinner, FourNumBlueToothUtil.getAllStopBit());
        spinnerStopBit.setAdapter(arrayAdapter3);
        ArrayAdapter<String> arrayAdapter4 = new ArrayAdapter<String>(LSYModuleNew04Activity.this, R.layout.l_loushanyun_item_spinner, FourNumBlueToothUtil.getAllJiaoYan());
        spinnerJiaoyan.setAdapter(arrayAdapter4);
        ArrayAdapter<String> arrayAdapterDYLX = new ArrayAdapter<String>(LSYModuleNew04Activity.this, R.layout.l_loushanyun_item_spinner, FourNumBlueToothUtil.getDianYuanLeiXing());
        ArrayAdapter<String> arrayAdapterMoShi = new ArrayAdapter<String>(LSYModuleNew04Activity.this, R.layout.l_loushanyun_item_spinner, FourNumBlueToothUtil.getChuanGanMoShi());
        ArrayAdapter<String> arrayAdapterShuZi = new ArrayAdapter<String>(LSYModuleNew04Activity.this, R.layout.l_loushanyun_item_spinner, FourNumBlueToothUtil.getChuanGanXinHaoShuZi());
        ArrayAdapter<String> arrayAdapterLeiJi = new ArrayAdapter<String>(LSYModuleNew04Activity.this, R.layout.l_loushanyun_item_spinner, FourNumBlueToothUtil.getChuanGanXinHaoLeiJi());
        ArrayAdapter<String> arrayAdapterZhuangTai = new ArrayAdapter<String>(LSYModuleNew04Activity.this, R.layout.l_loushanyun_item_spinner, FourNumBlueToothUtil.getChuanGanXinHaoZhuangTai());
        dianyuanSelect.setAdapter(arrayAdapterDYLX);
        chuanganmoshiSelect.setAdapter(arrayAdapterMoShi);
        chuanganxinhaoSelect.setAdapter(arrayAdapterShuZi);
        chuanganmoshiSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    chuanganxinhaoSelect.setAdapter(arrayAdapterShuZi);
                } else if (position == 1) {
                    chuanganxinhaoSelect.setAdapter(arrayAdapterLeiJi);
                } else if (position == 2) {
                    chuanganxinhaoSelect.setAdapter(arrayAdapterZhuangTai);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void clearAllText() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textHuoqu.setText("");
            }
        });

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
        xulieTextReset.setOnClickListener(this::OnBlueToothClick);
        xulieTextClearAdd.setOnClickListener(this::OnBlueToothClick);
        xulieTextOneSetting.setOnClickListener(this::OnBlueToothClick);
        xulieTextRead.setOnClickListener(this::OnBlueToothClick);
        xulieDataText.setOnClickListener(this::OnBlueToothClick);
        roundTextXiumianSave.setOnClickListener(this::OnBlueToothClick);

        xulieTextGetNetwork.setOnClickListener(this::OnClick);
        textPrintCoon.setOnClickListener(this::OnClick);
        buttonDuquNew.setOnClickListener(this::OnClick);
        roundTextLsy4diban.setOnClickListener(this::OnClick);

    }

    private void initSnAgreement(double hardVersion) {
        sAtInstructRealizeFactory.initHardVersion(hardVersion);
    }

    private byte[] getDuiJieBytes() {
        byte[] result = new byte[15];
        result[0] = 0x68;
        result[1] = 0x0b;
        result[2] = 0x0a;
        result[3] = (byte) LouShanYunUtils.getCPXSUploadIntByValue(textChanpinxingshi.getText().toString());
        result[4] = 0x01;//水务
        result[5] = (byte) ("物联电池".equals(dianyuanSelect.getSelectedItem().toString()) ? 0x00 : 0x01);
        result[6] = FourNumBlueToothUtil.getChuanGanMoShiByte(chuanganmoshiSelect.getSelectedItem().toString());
        result[7] = FourNumBlueToothUtil.getChuanGanXinHaoByString(chuanganxinhaoSelect.getSelectedItem().toString());
        long isn = Long.parseLong(editIdStart.getText().toString());
        for (int i = 0; i < 5; i++) {
            result[i + 8] = (byte) (isn & 0xff);
            isn = isn >> 8;
        }
        byte cs = 0;
        for (int i = 1; i < 14; i++) {
            cs += result[i];
        }
        result[13] = (byte) (cs & 0xff);
        result[14] = 0x16;
        return result;
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
        canPrint = false;
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
                    snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructChannel).getFourWriteBytes(atXindaocanshuSelect.getSelectedItem().toString().trim()), "设置信道");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                isAllSetting = true;
                break;
/**-------------------------------------------------------------------------------------------------------------------------------------------*/
            case R.id.four_one_setting:
                if (XHStringUtil.isEmpty(editOtherVersion.getText().toString(), false)) {
                    sendMessageToast("还没有请求网络获取自定义版本信息");
                    return;
                }
                isAllFourSetting = true;
                byte[] input2 = {0x68, 0x02, 0x00, 0x00, 0x02, 0x16};
                snBlueToothTool.write(input2, "设置采集场景中...");
                fourTextInfo.setText("");
                break;
            case R.id.four_one_reading:
                isReadAllSuccess = true;
                try {
                    snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sInstructDiscern).getFourWriteBytes(), "识别模块中...", true, 6000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
/**-------------------------------------------------------------------------------------------------------------------------------------------*/
            case R.id.round_text_xiumian:
                close();
                break;
            case R.id.system_status:
                isJiHuoAndXiuMian = false;
                if (isJiHuo) {
                    closeJiHuo();
                } else {
                    openJiHuo();
                }
                break;
            case R.id.round_text_xiumian_save:
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
                if (!isHigher) {
                    sendMessageToast("固件版本过低，不能进行保存");
                    return;
                }
                FactorysSettingsModule4 factorysSettingsModule4 = new FactorysSettingsModule4();
                factorysSettingsModule4.setSn(sensoroDeviceChoose.getSerialNumber());
                factorysSettingsModule4.setUseScene("基站公用".equals(LouShanYunUtils.getCJCJReadStringByCode(hashMap.get(MapParams.采集场景))) ? "1" : "2");
                factorysSettingsModule4.setProductionTime("20" + hashMap.get(MapParams.出厂时间_年) + "-" + hashMap.get(MapParams.出厂时间_月) + "-" + hashMap.get(MapParams.出厂时间_日));
                factorysSettingsModule4.setHardwareVersion(LouShanYunUtils.getHardWareVersion(Integer.valueOf(hashMap.get(MapParams.硬件版本))));
                factorysSettingsModule4.setSoftVersion(LouShanYunUtils.getSoftWareVersion(Integer.valueOf(hashMap.get(MapParams.软件版本))));
                factorysSettingsModule4.setProductForm(LouShanYunUtils.getCPXSUploadIntByValue(LouShanYunUtils.getCPXSReadStringByCode(hashMap.get(MapParams.产品形式))) + "");
                factorysSettingsModule4.setSensingSignalModule("" + hashMap.get(MapParams.传感模式));
                factorysSettingsModule4.setSensingSignal(LouShanYunUtils.getCGXHUploadIntByValue(LouShanYunUtils.getCGXHReadStringByCode(hashMap.get(MapParams.传感信号))) + "");
                factorysSettingsModule4.setPowerType(hashMap.get(MapParams.电源类型));
                factorysSettingsModule4.setMeterId(hashMap.get(MapParams.设备ID));
                factorysSettingsModule4.setManufacturersIdentification(hashMap.get(MapParams.厂家标识));
                factorysSettingsModule4.setFirmwareVersion(hashMap.get(SAtInstructParams.sAtInstructFirmwareVersion));
                if ("3".equals(hashMap.get(MapParams.传感模式))) {
                    factorysSettingsModule4.setProtocolVersion(textBanben.getText().toString().replaceAll("自定义协议版本号：", ""));
                    factorysSettingsModule4.setRate("");
//                    factorysSettingsModule4.setBatteryStatus("正常".equals(FourNumBlueToothUtil.getZTReadStringByCode(hashMap.get(MapParams.状态)).get("电池状态")) ? "0" : "1");
                    factorysSettingsModule4.setBatteryStatus("正常");
                    String xulie = xulieTextRead.getText().toString().replaceAll("\n", ",");
                    factorysSettingsModule4.setParseData(xulie);
                    factorysSettingsModule4.setBps(hashMap.get(MapParams.波特率) + "bps");
                } else if ("2".equals(hashMap.get(MapParams.传感模式))) {

                } else if ("1".equals(hashMap.get(MapParams.传感模式))) {

                }
                pushEvent(FactoryCode.FSSaveModule4FactorySettingRunner, new Gson().toJson(factorysSettingsModule4));
                break;
            case R.id.button_qiangzhifasong:
                clearAllText();
                try {
                    snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructForcedSend).getTwoReadBytes(LoginParamManager.getInstance().getProductRegister().getCompanyPhone())
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
/**------------------------------------------------------------数字通讯-------------------------------------------------------------------------------*/
            case R.id.third_text_write:
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
                break;
            case R.id.third_text_clear_write:
                thirdEditWrite.setText("");
                break;
            case R.id.third_text_clear_read:
                stringBufferThird = new StringBuffer();
                thirdTextRead.setText("");
                break;
            case R.id.xulie_text_reset:
                isAllSettingXuLie = false;
                snBlueToothTool.write(FourNumBlueToothUtil.getFour21Bytes(), "正在重置自定义序列");
                break;
            case R.id.xulie_read:
                if (XHStringUtil.isEmpty(editOtherVersion.getText().toString(), false)) {
                    sendMessageToast("请先选择协议");
                    return;
                }
                snBlueToothTool.write(FourNumBlueToothUtil.getFour22Bytes(), "读取第三方自定义协议序列是否已经配置");
                stringBufferXuLie = new StringBuffer();
                stringBufferXuLieData = new StringBuffer();
                xulieDataText.setText("");
                xulieTextRead.setText("");
                break;
            case R.id.xulie_text_clear_add:
                editXulieWrite.setText("");
                break;
            case R.id.xulie_text_one_setting:
                String xulie = editXulieWrite.getText().toString();
                if (XHStringUtil.isEmpty(xulie, false)) {
                    sendMessageToast("请输入字符");
                    return;
                }
                AbSharedUtil.putString(this, XULIESHARE, xulie);
                stringsXuLie = xulie.split("\n");
                stringsXuLieIndex = 0;
                isAllSettingXuLie = true;
                snBlueToothTool.write(FourNumBlueToothUtil.getFour21Bytes(), "正在重置自定义序列");
                break;
            case R.id.xulie_text_read:
                if (!XHStringUtil.isEmpty(xulieTextRead.getText().toString(), false)) {
                    new Share2.Builder(LSYModuleNew04Activity.this)
                            .setContentType(ShareContentType.TEXT)
                            .setTitle("第三方自定义序列")
                            .setTextContent(xulieTextRead.getText().toString())
                            .setOnActivityResult(1000)
                            .build()
                            .shareBySystem();
                }
                break;
            case R.id.xulie_data_text:
                if (!XHStringUtil.isEmpty(xulieDataText.getText().toString(), false)) {
                    new Share2.Builder(LSYModuleNew04Activity.this)
                            .setContentType(ShareContentType.TEXT)
                            .setTitle("第三方自定义序列数据")
                            .setTextContent(xulieDataText.getText().toString())
                            .setOnActivityResult(1000)
                            .build()
                            .shareBySystem();
                }
                break;

        }
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
        switch (view.getId()) {
            case R.id.button_dayin:
                if (!XHStringUtil.isEmpty(sensoroDeviceChoose.sn, false)) {
                    LoadingDialogUtil.showByEvent("正在打印", this.loadingTag);
                    printTool.printBitmap(printTool.createChipPrint2(sensoroDeviceChoose.sn));
                    LoadingDialogUtil.dismissByEvent(this.loadingTag);
                }
                break;
            case R.id.xulie_text_get_network:
                ruleTool.showRuleDialog(LoginParamManager.getInstance().getProductRegister().getManufacturersIdentification());
                break;
            case R.id.text_print_coon:
                printTool.showPrintDialog();
                break;
            case R.id.button_duqu_new:
                pushEvent(LouShanYunCode.MChipGetNewsInfoRunner, sensoroDeviceChoose.sn, "1", "5");
                break;
            case R.id.round_text_lsy4diban:
                ARouter.getInstance().build(K.FourDiBanActivity).navigation();
                break;
        }
    }

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        super.onInitAttribute(ba);
        ba.mTitleText = "4号模组新程序";
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
                fourSettingReadDataSuccess = false;
                xulieTextRead.setText("");
                atTextInfo.setText("");
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
            case 0x00:
                if (result[3] == 0) {
                    if (isAllFourSetting) {
                        snBlueToothTool.write(FourNumBlueToothUtil.getFourPinlvBytes("15分钟"), "设置发送频率15分钟");
                    }
                } else {
                    sendMessageToast("设置采集场景失败");
                }
                break;
            case 0x03:
                if (result[3] == 0) {
                    if (isAllFourSetting) {
                        snBlueToothTool.write(FourNumBlueToothUtil.getFouWangluoJiaoHuBytes("1"), "正在设置网络交互");
                    }
                } else {
                    sendMessageToast("设置出厂日期失败");
                }
                break;
            case 0x04:
                if (result[3] == 0) {
                    if (isAllFourSetting) {
                        int biaoshi = Integer.valueOf(LoginParamManager.getInstance().getProductRegister().getManufacturersIdentification());
                        byte[] d = new byte[7];
                        d[0] = (byte) 0x68;
                        d[1] = (byte) 0x03;//有效数据
                        d[2] = (byte) 0x06;//命令
                        d[3] = (byte) (biaoshi & 0xff);
                        d[4] = (byte) (biaoshi & 0xff00);
                        byte cs = 0;
                        for (int i = 1; i < 5; i++) {
                            cs += d[i];
                        }
                        d[5] = cs;//校验和
                        d[6] = (byte) 0x16;
                        snBlueToothTool.write(d, "正在设置厂家标识");
                    }
                } else {
                    sendMessageToast("设置网络交互失败");
                }
                break;
            case 0x06:
                if (result[3] == 0) {
                    if (isAllFourSetting) {
                        snBlueToothTool.write(getDuiJieBytes(), "正在设置对接信息");
                    }
                } else {
                    sendMessageToast("设置厂家标识失败");
                }
                break;
            case 0x0a:
                if (result[3] == 0) {
                    if (isAllFourSetting) {
                        snBlueToothTool.write(FourNumBlueToothUtil.getFourCKWriteBytes(spinnerBotelvSelect.getSelectedItem().toString()
                                , spinnerStopBit.getSelectedItem().toString(), spinnerJiaoyan.getSelectedItem().toString()), "正在设置串口通信参数");
                    }
                } else {
                    sendMessageToast("设置对接信息失败");
                }
                break;
            case 0x25:
                if (result[3] == 0) {
                    if (isAllFourSetting) {
                        snBlueToothTool.write(FourNumBlueToothUtil.getFour20Bytes(editOtherVersion.getText().toString()), "正在设置协议版本号");
                    }
                } else {
                    sendMessageToast("设置串口通信参数失败");
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
                    if (hashMap.get(MapParams.厂家标识).equals(String.valueOf(LoginParamManager.getInstance().getProductRegister().getManufacturersIdentification()))) {
                        setDuQu(hashMap);
                    } else {
                        sendMessageToast("获取到该设备的厂家标识不是本账号的厂家，请设置厂家标识信息！！");
                    }
                } else {
                    sendMessageToast("读取串口通信参数失败");
                }
                break;
            case 0x20:
                if (result[3] == 0) {
                    closeJiHuo();
                    sendMessageToast("设置协议版本号成功");
                } else {
                    sendMessageToast("设置协议版本号失败");
                }
                break;
            case 0x07:
                if (result[3] == 0) {
                    if (isAllFourSetting) {
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
                        if (isReadAllSuccess) {
                            snBlueToothTool.write(FourNumBlueToothUtil.getFourCKReadBytes(), "正在读取4号模组串口信息");
                        }
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
            case 0x21:
                if (result[3] == 0) {
                    sendMessageToast("重置成功");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            xulieDataText.setText("");
                            xulieTextRead.setText("");
                            if (isAllSettingXuLie) {
                                snBlueToothTool.write(FourNumBlueToothUtil.getFourOFBytes(stringsXuLie[stringsXuLieIndex]), "正在配置第" + (stringsXuLieIndex + 1) + "个");
                            }
                        }
                    });
                } else {
                    sendMessageToast("重置失败");
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
                            if (finalNum == Integer.valueOf(editOtherVersion.getText().toString())) {
                                if (stringsXuLie != null) {
                                    if (xuLieLength == stringsXuLie.length) {
                                        fourSettingReadDataSuccess = true;
                                        snBlueToothTool.write(FourNumBlueToothUtil.getFour24Bytes(), "正在读取自定义序列");
                                    } else {
                                        fourSettingReadDataSuccess = false;
                                        sendMessageToast("协议配置数量不正确，请重新配置");
                                    }
                                } else {
                                    sendMessageToast("请先配置协议序列");
                                }
                            } else {
                                sendMessageToast("协议版本号不对应！请设置协议版本号再读取", true);
                            }

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


    private void close() {
        snBlueToothTool.write(new byte[]{(byte) 0x68, (byte) 0x01, (byte) 0x0d, (byte) 0x0e, (byte) 0x16}, "正在让设备进入休眠状态");
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
    public void onChildWriteSuccess() {

    }

    @Override
    public void onChildWriteFailure(int i) {

    }

    @Override
    protected void initEventListener() {
        registerEventRunner(LouShanYunCode.MChipGetNewsInfoRunner, new MChipGetNewsInfoRunner());
        registerEventRunner(FactoryCode.FSSaveModule4FactorySettingRunner, new FSSaveModule4FactorySettingRunner());
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
        } else if (code == FactoryCode.FSSaveModule4FactorySettingRunner) {
            if (event.isSuccess()) {
                int resCode = (int) event.getReturnParamAtIndex(0);
                String msg = (String) event.getReturnParamAtIndex(1);
                sendMessageToast(msg, true);
                if (resCode == 0) {
                    close();
                }
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (ruleTool != null) {
            ruleTool.onActivityResult(requestCode, resultCode, data);
        }
    }
}
