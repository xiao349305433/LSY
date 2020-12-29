package wu.loushanyun.com.modulechiptest.v.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import androidx.appcompat.widget.SwitchCompat;
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
import com.gpsdk.demo.DeviceConnFactoryManager;
import com.sensoro.sensor.kit.entity.SensoroDevice;
import com.wu.loushanyun.base.config.K;
import com.wu.loushanyun.base.print.PrintTool;
import com.wu.loushanyun.base.util.DataParser;
import com.wu.loushanyun.base.util.LouShanYunUtils;
import com.wu.loushanyun.base.util.MapParams;
import com.wu.loushanyun.basemvp.m.ModuleRule;
import com.wu.loushanyun.basemvp.m.sat.SAtInstructRealizeFactory;
import com.wu.loushanyun.basemvp.m.sat.util.FourNumBlueToothUtil;
import com.wu.loushanyun.basemvp.m.sat.util.SAtInstructParams;
import com.wu.loushanyun.basemvp.p.presenter.RuleTool;
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
import wu.loushanyun.com.modulechiptest.R;
import wu.loushanyun.com.modulechiptest.init.ChipCode;
import wu.loushanyun.com.modulechiptest.m.NewInfo;
import wu.loushanyun.com.modulechiptest.p.runner.MChipGetNewsInfoRunner;
import wu.loushanyun.com.modulechiptest.p.runner.MChipSetRxDelayRunner;

import static com.wu.loushanyun.basemvp.init.SnBlueToothTool.BLUE_SEND_TIME_OUT;

@Route(path = K.FourTestActivity)
public class FourTestActivity1 extends BaseSnBlueToothActivity {
    private ScrollView scrollView;
    private TextView textPrint;
    private EditText editPrintNum;
    private RoundTextView textPrintCoon;
    private TextView textToken;
    private TextView textDangqianSn;
    private RoundTextView bluetoothConn;
    private RoundTextView buttonDayin;
    private RoundTextView bluetoothDisconn;
    private SwitchCompat switchSaveStatus;
    private RoundTextView xulieTextGetNetwork;
    private EditText editIdStart;
    private RoundTextView roundCreateId;
    private EditText editChangjiabiaoshi;
    private EditText editDisanfang;
    private Spinner wangluojiaohuSelect;
    private RoundTextView atWangluojiaohuSetting;
    private TextView editOtherVersion;
    private Spinner pinlvSelect;
    private RoundTextView atPinlvSetting;
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
    private EditText editXinhaoqiangduStart;
    private EditText editXinhaoqiangduEnd;
    private EditText editXinzaobiStart;
    private EditText editXinzaobiEnd;
    private TextView textHuoqu;
    private RoundTextView buttonQiangzhifasong;
    private RoundTextView buttonDuquXinhao;
    private TextView textHuoquNew;
    private RoundTextView buttonDuquNew;
    private RoundTextView roundTextXiumianJihuo;
    private RoundTextView roundTextXiumian;
    private RoundTextView roundTextSaomiao;

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

    private SensoroDevice sensoroDeviceChoose;
    private PrintTool printTool;
    private RuleTool ruleTool;

    //默认参数
    private boolean isAllSetting;
    private boolean isAllReading;
    private boolean isNewID = true;//每次设置完成都让其生成一个新的id
    private boolean isJiHuo = true;
    private boolean isJiHuoAndXiuMian = false;
    private boolean isReadAllSuccess = false;

    private int type;
    private int writeCode;
    private boolean canPrint;
    private SAtInstructRealizeFactory sAtInstructRealizeFactory;
    private HashMap<String, String> hashMap;
    private String[] kuopinyinziArray;

    private StringBuffer stringBufferThird;
    private boolean isAllFourSetting;
    private int xuLieLength;
    private int xuLieIndex;
    private int xuLieDataIndex;
    private int waittingTime;
    private final static int WTIME = 5000;

    private StringBuffer stringBufferXuLie;
    private StringBuffer stringBufferXuLieData;
    private String[] stringsXuLie;
    private int stringsXuLieIndex;

    private static final String XULIESHARE = "xuLieShare";
    private static final String THIRDSHARE = "thirdDataShare";
    private boolean isAllSettingXuLie = false;

    @Override
    protected int onChildLayout() {
        return R.layout.m_chip_activity_four_1;
    }


    @Override
    protected void initLifeCycle() {
        super.initLifeCycle();
        ruleTool = new RuleTool(new RuleTool.OnRuleCallBack() {
            @Override
            public void onRuleBack(String result) {
                editXulieWrite.setText(result);
            }

            @Override
            public void onVersionBack(ModuleRule.DataBean dataBean) {
                editOtherVersion.setText(dataBean.getProtocolVersion() + "");
                textChanpinxingshi.setText(LouShanYunUtils.getCPXSReadStringByCode(dataBean.getProductForm()));
            }

        }, getResources().getColor(R.color.l_loushanyun_Q));
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
        editPrintNum = (EditText) findViewById(R.id.edit_print_num);
        textPrintCoon = (RoundTextView) findViewById(R.id.text_print_coon);
        textToken = (TextView) findViewById(R.id.text_token);
        textDangqianSn = (TextView) findViewById(R.id.text_dangqian_sn);
        bluetoothConn = (RoundTextView) findViewById(R.id.bluetooth_conn);
        buttonDayin = (RoundTextView) findViewById(R.id.button_dayin);
        bluetoothDisconn = (RoundTextView) findViewById(R.id.bluetooth_disconn);
        switchSaveStatus = (SwitchCompat) findViewById(R.id.switch_save_status);
        xulieTextGetNetwork = (RoundTextView) findViewById(R.id.xulie_text_get_network);
        editIdStart = (EditText) findViewById(R.id.edit_id_start);
        roundCreateId = (RoundTextView) findViewById(R.id.round_create_id);
        editChangjiabiaoshi = (EditText) findViewById(R.id.edit_changjiabiaoshi);
        editDisanfang = (EditText) findViewById(R.id.edit_disanfang);
        wangluojiaohuSelect = (Spinner) findViewById(R.id.wangluojiaohu_select);
        atWangluojiaohuSetting = (RoundTextView) findViewById(R.id.at_wangluojiaohu_setting);
        editOtherVersion = (TextView) findViewById(R.id.edit_other_version);
        pinlvSelect = (Spinner) findViewById(R.id.pinlv_select);
        atPinlvSetting = (RoundTextView) findViewById(R.id.at_pinlv_setting);
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
        editXinhaoqiangduStart = (EditText) findViewById(R.id.edit_xinhaoqiangdu_start);
        editXinhaoqiangduEnd = (EditText) findViewById(R.id.edit_xinhaoqiangdu_end);
        editXinzaobiStart = (EditText) findViewById(R.id.edit_xinzaobi_start);
        editXinzaobiEnd = (EditText) findViewById(R.id.edit_xinzaobi_end);
        textHuoqu = (TextView) findViewById(R.id.text_huoqu);
        buttonQiangzhifasong = (RoundTextView) findViewById(R.id.button_qiangzhifasong);
        buttonDuquXinhao = (RoundTextView) findViewById(R.id.button_duqu_xinhao);
        textHuoquNew = (TextView) findViewById(R.id.text_huoqu_new);
        buttonDuquNew = (RoundTextView) findViewById(R.id.button_duqu_new);
        roundTextXiumianJihuo = (RoundTextView) findViewById(R.id.round_text_xiumian_jihuo);
        roundTextXiumian = (RoundTextView) findViewById(R.id.round_text_xiumian);
        roundTextSaomiao = (RoundTextView) findViewById(R.id.round_text_saomiao);


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

        editXulieWrite.setText(AbSharedUtil.getString(this, XULIESHARE));
        thirdEditWrite.setText(AbSharedUtil.getString(this, THIRDSHARE));
        atEditRxdelay.setText("5");
        stringBufferXuLie = new StringBuffer();
        stringBufferXuLieData = new StringBuffer();
        hashMap = new HashMap<>();
        stringBufferThird = new StringBuffer();
        kuopinyinziArray = getResources().getStringArray(R.array.m_chip_kuopinyinzi);
        editIdStart.setText(LouShanYunUtils.getTimeID());
        bluetoothConn.setOnClickListener(v -> {
            if (textDangqianSn.getText().toString().contains("当前连接的SN设备")) {
                sendMessageToast("当前已连接设备");
                return;
            }
            snBlueToothTool.connectBlueTooth(sensoroDeviceChoose);
            clearAllText();
        });
        bluetoothDisconn.setOnClickListener(v -> {
            snBlueToothTool.disconnect();
            textDangqianSn.setText("当前未连接SN设备");
        });
        initTestClick();
        sAtInstructRealizeFactory = new SAtInstructRealizeFactory();
        ArrayAdapter<String> arrayAdapterJiaoHu = new ArrayAdapter<String>(FourTestActivity1.this, R.layout.l_loushanyun_item_spinner, FourNumBlueToothUtil.getAllWangLuoJiaoHu());
        wangluojiaohuSelect.setAdapter(arrayAdapterJiaoHu);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(FourTestActivity1.this, R.layout.l_loushanyun_item_spinner, sAtInstructRealizeFactory.getAllMoShi());
        atXindaocanshuSelect.setAdapter(arrayAdapter);
        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(FourTestActivity1.this, R.layout.l_loushanyun_item_spinner, FourNumBlueToothUtil.getAllPinLv());
        pinlvSelect.setAdapter(arrayAdapter1);
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(FourTestActivity1.this, R.layout.l_loushanyun_item_spinner, FourNumBlueToothUtil.getAllBoTeLv());
        spinnerBotelvSelect.setAdapter(arrayAdapter2);
        ArrayAdapter<String> arrayAdapter3 = new ArrayAdapter<String>(FourTestActivity1.this, R.layout.l_loushanyun_item_spinner, FourNumBlueToothUtil.getAllStopBit());
        spinnerStopBit.setAdapter(arrayAdapter3);
        ArrayAdapter<String> arrayAdapter4 = new ArrayAdapter<String>(FourTestActivity1.this, R.layout.l_loushanyun_item_spinner, FourNumBlueToothUtil.getAllJiaoYan());
        spinnerJiaoyan.setAdapter(arrayAdapter4);
        ArrayAdapter<String> arrayAdapterDYLX = new ArrayAdapter<String>(FourTestActivity1.this, R.layout.l_loushanyun_item_spinner, FourNumBlueToothUtil.getDianYuanLeiXing());
        ArrayAdapter<String> arrayAdapterMoShi = new ArrayAdapter<String>(FourTestActivity1.this, R.layout.l_loushanyun_item_spinner, FourNumBlueToothUtil.getChuanGanMoShi());
        ArrayAdapter<String> arrayAdapterShuZi = new ArrayAdapter<String>(FourTestActivity1.this, R.layout.l_loushanyun_item_spinner, FourNumBlueToothUtil.getChuanGanXinHaoShuZi());
        ArrayAdapter<String> arrayAdapterLeiJi = new ArrayAdapter<String>(FourTestActivity1.this, R.layout.l_loushanyun_item_spinner, FourNumBlueToothUtil.getChuanGanXinHaoLeiJi());
        ArrayAdapter<String> arrayAdapterZhuangTai = new ArrayAdapter<String>(FourTestActivity1.this, R.layout.l_loushanyun_item_spinner, FourNumBlueToothUtil.getChuanGanXinHaoZhuangTai());
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
        atRxdelaySetting.setOnClickListener(this::OnBlueToothClick);
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
        roundTextXiumian.setOnClickListener(this::OnBlueToothClick);
        roundTextXiumianJihuo.setOnClickListener(this::OnBlueToothClick);
        systemStatus.setOnClickListener(this::OnBlueToothClick);
        xulieRead.setOnClickListener(this::OnBlueToothClick);
        xulieTextReset.setOnClickListener(this::OnBlueToothClick);
        xulieTextClearAdd.setOnClickListener(this::OnBlueToothClick);
        xulieTextOneSetting.setOnClickListener(this::OnBlueToothClick);
        xulieTextRead.setOnClickListener(this::OnBlueToothClick);
        xulieDataText.setOnClickListener(this::OnBlueToothClick);
        atWangluojiaohuSetting.setOnClickListener(this::OnBlueToothClick);
        atPinlvSetting.setOnClickListener(this::OnBlueToothClick);

        xulieTextGetNetwork.setOnClickListener(this::OnClick);
        roundTextSaomiao.setOnClickListener(this::OnClick);
        buttonDayin.setOnClickListener(this::OnClick);
        textPrintCoon.setOnClickListener(this::OnClick);
        buttonDuquNew.setOnClickListener(this::OnClick);
        textToken.setOnClickListener(this::OnClick);

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
        writeCode = 0;
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
            case R.id.four_one_setting:
                if (XHStringUtil.isEmpty(editOtherVersion.getText().toString(), false)) {
                    sendMessageToast("还没有请求网络获取自定义版本信息");
                    return;
                }
                isAllFourSetting = true;
                if (editDisanfang.getText().toString().equals("0")) {
                    byte[] input2 = {0x68, 0x00, 0x27, 0x00, 0x16};
                    snBlueToothTool.write(input2, "设置模组中...");
                } else if (editDisanfang.getText().toString().equals("1")) {
                    byte[] input2 = {0x68, 0x00, 0x27, 0x01, 0x16};
                    snBlueToothTool.write(input2, "设置模组中...");
                }
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
            case R.id.round_text_xiumian_jihuo:
                isJiHuoAndXiuMian = true;
                isJiHuo = false;
                openJiHuo();
                break;
            case R.id.at_wangluojiaohu_setting:
                isAllFourSetting = false;
                snBlueToothTool.write(FourNumBlueToothUtil.getFouWangluoJiaoHuBytes(wangluojiaohuSelect.getSelectedItem().toString().replaceAll("最大发送", "").replaceAll("次", "")), "正在设置网络交互");
                break;
            case R.id.at_pinlv_setting:
                isAllFourSetting = false;
                snBlueToothTool.write(FourNumBlueToothUtil.getFourPinlvBytes(pinlvSelect.getSelectedItem().toString().trim()), "设置发送频率" + pinlvSelect.getSelectedItem().toString().trim());
                break;
            case R.id.button_qiangzhifasong:
                clearAllText();
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
                    new Share2.Builder(FourTestActivity1.this)
                            .setContentType(ShareContentType.TEXT)
                            .setTitle("第三方自定义序列")
                            .setTextContent(xulieTextRead.getText().toString())
                            .setOnActivityResult(1000)
                            .build()
                            .shareBySystem();
                }
                break;
            case R.id.third_text_read:
                if (!XHStringUtil.isEmpty(thirdTextRead.getText().toString(), false)) {
                    new Share2.Builder(FourTestActivity1.this)
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
                    new Share2.Builder(FourTestActivity1.this)
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
            case R.id.round_text_saomiao:
                ARouter.getInstance().build(K.SecondUploadInfoActivity).navigation();
                break;
            case R.id.button_dayin:
                if (!XHStringUtil.isEmpty(sensoroDeviceChoose.sn, false)) {
                    LoadingDialogUtil.showByEvent("正在打印", this.loadingTag);
                    printTool.printBitmap(printTool.createChipPrint2(sensoroDeviceChoose.sn));
                    LoadingDialogUtil.dismissByEvent(this.loadingTag);
                }
                break;
            case R.id.xulie_text_get_network:
                ruleTool.showRuleDialog(21);
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
                new Share2.Builder(FourTestActivity1.this)
                        .setContentType(ShareContentType.TEXT)
                        .setTitle("打印SN")
                        .setTextContent(text)
                        .setOnActivityResult(1000)
                        .build()
                        .shareBySystem();
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
                textDangqianSn.setText("当前连接的SN设备:" + sensoroDeviceChoose.getSerialNumber().toUpperCase());
                try {
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
                    if (isAllFourSetting) {
                        snBlueToothTool.write(FourNumBlueToothUtil.getFourPinlvBytes(pinlvSelect.getSelectedItem().toString().trim()), "设置发送频率" + pinlvSelect.getSelectedItem().toString().trim());
                    }
                } else {
                    sendMessageToast("设置采集场景失败");
                }
                break;
            case 0x03:
                if (result[3] == 0) {
                    if (isAllFourSetting) {
                        snBlueToothTool.write(FourNumBlueToothUtil.getFouWangluoJiaoHuBytes(wangluojiaohuSelect.getSelectedItem().toString().replaceAll("最大发送", "").replaceAll("次", "")), "正在设置网络交互");
                    }
                } else {
                    sendMessageToast("设置出厂日期失败");
                }
                break;
            case 0x04:
                if (result[3] == 0) {
                    if (isAllFourSetting) {
                        int biaoshi = Integer.valueOf(editChangjiabiaoshi.getText().toString());
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
                    } else {
                        sendMessageToast("设置网络交互成功");
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
                    byte[] input2 = {0x68, 0x00, 0x28, 0x16};
                    snBlueToothTool.write(input2, "读取模组中...");
                } else {
                    sendMessageToast("读取串口通信参数失败");
                }
                break;
            case 0x28:
                if (result[3] == 0) {
                    hashMap.put(MapParams.模组配置参数, String.valueOf(result[4] & 0xff));
                    setDuQu(hashMap);
                } else {
                    sendMessageToast("读取模组配置参数失败");
                }
                break;
            case 0x20:
                if (result[3] == 0) {
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
                    } else {
                        sendMessageToast("设置发送频率成功");
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
                        if (!isReadAllSuccess) {
                            snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructToken).getFourReadBytes(), "读取Token...");
                        } else {
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
                            if (isJiHuoAndXiuMian) {
                                close();
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
                    stringBufferThird.append(ByteConvertUtils.byteToString(result, 4, 1) + "\n");
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
                } else {
                    sendMessageToast("第" + (stringsXuLieIndex + 1) + "条设置失败");
                }
                stringsXuLieIndex++;
                if (stringsXuLieIndex < stringsXuLie.length) {
                    snBlueToothTool.write(FourNumBlueToothUtil.getFourOFBytes(stringsXuLie[stringsXuLieIndex]), "正在配置第" + (stringsXuLieIndex + 1) + "个");
                } else {
                    sendMessageToast("配置成功");
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
                    waittingTime = WTIME * xuLieLength;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textBanben.setText("自定义协议版本号：" + finalNum);
                            if (!XHStringUtil.isEmpty(editOtherVersion.getText().toString(), false)) {
                                if (finalNum == Integer.valueOf(editOtherVersion.getText().toString())) {
                                    snBlueToothTool.write(FourNumBlueToothUtil.getFour24Bytes(), "正在读取自定义序列");
                                } else {
                                    sendMessageToast("协议版本号不对应！请设置协议版本号再读取", true);
                                }
                            } else {
                                sendMessageToast("请先选择协议版本号");
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
                    stringBufferXuLie.append(ByteConvertUtils.byteToString(result, 4, 1) + "\n");
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
                    stringBufferXuLieData.append(ByteConvertUtils.byteToString(result, 4, 1) + "\n");
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
            case 0x27:
                if (result[3] == 0) {
                    byte[] input2 = {0x68, 0x02, 0x00, 0x00, 0x02, 0x16};
                    snBlueToothTool.write(input2, "设置采集场景中...");
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
        sb.append("\n是否配置的是第三方底板:  ");
        sb.append(hashMap.get(MapParams.模组配置参数).equals("0") ? "是" : "否");
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
            if (!switchSaveStatus.isChecked()) {
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
                    for (int i = 0; i < kuopinyinziArray.length; i++) {
                        if (kuopinyinziArray[i].equals(hashMap.get(SAtInstructParams.sAtInstructSpreadingFactor))) {
                            atKuopinyinziSelect.setSelection(i);
                        }
                    }
                }
                atXindaocanshuSelect.setSelection(sAtInstructRealizeFactory.getMoShiIndex(hashMap.get(SAtInstructParams.sAtInstructChannel)));
            }
            atTextInfo.setText(sb.toString());
        });
    }

    private void close() {
        snBlueToothTool.write(new byte[]{(byte) 0x68, (byte) 0x01, (byte) 0x0d, (byte) 0x0e, (byte) 0x16}, "正在让设备进入休眠状态");
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (ruleTool != null) {
            ruleTool.onActivityResult(requestCode, resultCode, data);
        }
    }
}
