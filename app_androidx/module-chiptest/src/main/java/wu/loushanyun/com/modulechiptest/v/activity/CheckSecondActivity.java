package wu.loushanyun.com.modulechiptest.v.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.elvishew.xlog.XLog;
import com.flyco.roundview.RoundTextView;
import com.google.gson.Gson;
import com.gpsdk.demo.DeviceConnFactoryManager;
import com.qrcore.util.QRScannerHelper;
import com.sensoro.sensor.kit.entity.SensoroDevice;
import com.wu.loushanyun.base.config.K;
import com.wu.loushanyun.base.print.PrintTool;
import com.wu.loushanyun.base.util.DataParser;
import com.wu.loushanyun.base.util.LouShanYunUtils;
import com.wu.loushanyun.base.util.MapParams;
import com.wu.loushanyun.basemvp.init.LouShanYunCode;
import com.wu.loushanyun.basemvp.init.SensoroBlueManager;
import com.wu.loushanyun.basemvp.m.sat.SAtInstructRealizeFactory;
import com.wu.loushanyun.basemvp.m.sat.util.SAtInstructParams;
import com.wu.loushanyun.basemvp.v.activity.BaseSnBlueToothActivity;

import java.math.BigDecimal;
import java.util.HashMap;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.dialog.LoadingDialogUtil;
import met.hx.com.base.base.share.Share2;
import met.hx.com.base.base.share.ShareContentType;
import met.hx.com.base.baseevent.Event;
import met.hx.com.librarybase.some_utils.TimeUtils;
import met.hx.com.librarybase.some_utils.ToastUtils;
import met.hx.com.librarybase.some_utils.XHStringUtil;
import wu.loushanyun.com.modulechiptest.R;
import wu.loushanyun.com.modulechiptest.init.ChipCode;
import wu.loushanyun.com.modulechiptest.init.LoginParamManager;
import wu.loushanyun.com.modulechiptest.m.CheckUpData;
import wu.loushanyun.com.modulechiptest.m.NewInfo;
import wu.loushanyun.com.modulechiptest.m.ResultJson;
import wu.loushanyun.com.modulechiptest.m.SelectallInfo;
import wu.loushanyun.com.modulechiptest.p.runner.GetUpDataRunner;
import wu.loushanyun.com.modulechiptest.p.runner.MChipGetNewsInfoRunner;
import wu.loushanyun.com.modulechiptest.p.runner.MChipSetRxDelayRunner;
import wu.loushanyun.com.modulechiptest.p.runner.SelectallRunner;

import static com.wu.loushanyun.basemvp.init.SnBlueToothTool.BLUE_SEND_TIME_OUT;

@Route(path = K.CheckSecondActivity)
public class CheckSecondActivity extends BaseSnBlueToothActivity {

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
    private ImageView systemStatus;
    private EditText editXinhaoqiangduStart;
    private EditText editXinhaoqiangduEnd;
    private EditText editXinzaobiStart;
    private EditText editXinzaobiEnd;
    private TextView textHuoqu;
    private RoundTextView buttonQiangzhifasong;
    private RoundTextView buttonDuquXinhao;
    private TextView textHuoquNew;
    private RoundTextView buttonDuquNew;
    private EditText textNum;
    private RoundTextView textNumSaomiao;
    private RoundTextView roundTextNoXiumianJihuo;
    private LinearLayout linearBlueTooth;
    private EditText editSearch;
    private RoundTextView resetClear;
    private RoundTextView resetClearList;
    private RecyclerView dialogBlueRecycle;

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

    private Spinner secondParameterCgxhSelect;
    private Spinner secondParameterBeilvSelect;
    private EditText secondParameterEditMaichongStart;
    private EditText secondParameterEditIdStart;
    private RoundTextView secondParameterRoundCreateId;
    private EditText secondParameterEditFactoryId;
    private RoundTextView secondParameterOneSetting;
    private RoundTextView secondParameterOneReading;
    private TextView secondParameterTextInfo;

    private SelectallInfo.DataBean dataBean;
    private SensoroDevice sensoroDeviceChoose;

    //默认参数
    private byte chaiXie = 0;//拆卸   无
    private byte qiangCi = 1;//强磁   无
    private byte chuanGanQiZhuangTai = 0;//传感器状态   无
    private byte daoLiu = 1;//倒流   无
    private byte faMenZhuangTai = 0;//阀门状态   无
    private byte faSongPinLv = (byte) 0x30;//发送频率   默认24小时
    private boolean isAllSetting;
    private boolean isAllReading;
    private boolean isNewID = true;//每次设置完成都让其生成一个新的id
    private boolean isJiHuo = true;
    private boolean isJiHuoAndXiuMian = false;
    private int saomiaoType = 0;

    private int type;
    private int writeCode;
    private boolean canPrint;
    private SAtInstructRealizeFactory sAtInstructRealizeFactory;
    private HashMap<String, String> hashMap;
    private String[] kuopinyinziArray;
    private QRScannerHelper mScannerHelper;
    private boolean hasSetting = false;
    private boolean hasAtSetting = false;

    private PrintTool printTool;

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
    protected int onChildLayout() {
        return R.layout.m_chip_activity_checksecond;
    }


    @Override
    protected void onClickBlueListListener(SensoroDevice sensoroDevice) {
        super.onClickBlueListListener(sensoroDevice);
        this.sensoroDeviceChoose = sensoroDevice;
    }


    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        super.onInitAttribute(ba);
        ba.mTitleText = "2号模组质检";
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
        systemStatus = (ImageView) findViewById(R.id.system_status);
        editXinhaoqiangduStart = (EditText) findViewById(R.id.edit_xinhaoqiangdu_start);
        editXinhaoqiangduEnd = (EditText) findViewById(R.id.edit_xinhaoqiangdu_end);
        editXinzaobiStart = (EditText) findViewById(R.id.edit_xinzaobi_start);
        editXinzaobiEnd = (EditText) findViewById(R.id.edit_xinzaobi_end);
        textHuoqu = (TextView) findViewById(R.id.text_huoqu);
        buttonQiangzhifasong = (RoundTextView) findViewById(R.id.button_qiangzhifasong);
        buttonDuquXinhao = (RoundTextView) findViewById(R.id.button_duqu_xinhao);
        textHuoquNew = (TextView) findViewById(R.id.text_huoqu_new);
        buttonDuquNew = (RoundTextView) findViewById(R.id.button_duqu_new);
        textNum = (EditText) findViewById(R.id.text_num);
        textNumSaomiao = (RoundTextView) findViewById(R.id.text_num_saomiao);
        roundTextNoXiumianJihuo = (RoundTextView) findViewById(R.id.round_text_no_xiumian_jihuo);
        linearBlueTooth = (LinearLayout) findViewById(R.id.linear_blue_tooth);
        editSearch = (EditText) findViewById(R.id.edit_search);
        resetClear = (RoundTextView) findViewById(R.id.reset_clear);
        resetClearList = (RoundTextView) findViewById(R.id.reset_clear_list);
        dialogBlueRecycle = (RecyclerView) findViewById(R.id.dialog_blue_recycle);


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

        secondParameterCgxhSelect = (Spinner) findViewById(R.id.second_parameter_cgxh_select);
        secondParameterBeilvSelect = (Spinner) findViewById(R.id.second_parameter_beilv_select);
        secondParameterEditMaichongStart = (EditText) findViewById(R.id.second_parameter_edit_maichong_start);
        secondParameterEditIdStart = (EditText) findViewById(R.id.second_parameter_edit_id_start);
        secondParameterRoundCreateId = (RoundTextView) findViewById(R.id.second_parameter_round_create_id);
        secondParameterEditFactoryId = (EditText) findViewById(R.id.second_parameter_edit_factory_id);
        secondParameterOneSetting = (RoundTextView) findViewById(R.id.second_parameter_one_setting);
        secondParameterOneReading = (RoundTextView) findViewById(R.id.second_parameter_one_reading);
        secondParameterTextInfo = (TextView) findViewById(R.id.second_parameter_text_info);

        atEditRxdelay.setText("5");
        atFasonggonglvSetting.setVisibility(View.GONE);
        atXindaocanshuSetting.setVisibility(View.GONE);
        atKuopinyinziSetting.setVisibility(View.GONE);
        atRxdelaySetting.setVisibility(View.GONE);
        atFasonggonglvSelect.setEnabled(false);
        atKuopinyinziSelect.setEnabled(false);
        atEditRxdelay.setEnabled(false);
        secondParameterEditIdStart.setEnabled(false);
        atKuopinyinziSelect.setSelection(3);
        secondParameterRoundCreateId.setVisibility(View.GONE);
        dataBean = getIntent().getParcelableExtra("order");
        secondParameterEditMaichongStart.setEnabled(false);
        secondParameterEditIdStart.setText(LouShanYunUtils.getTimeID());
        secondParameterEditFactoryId.setEnabled(false);
        secondParameterEditFactoryId.setText(dataBean.getManufacturersIdentification() + "");
        secondParameterCgxhSelect.setSelection(2);
        hashMap = new HashMap<>();
        kuopinyinziArray = getResources().getStringArray(R.array.m_chip_kuopinyinzi);
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
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String search = s.toString().toUpperCase();
                if (!XHStringUtil.isEmpty(search, false)) {
                    snDeviceViewBinder.setKeyWord(search);
                } else {
                    snDeviceViewBinder.setKeyWord(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        resetClear.setOnClickListener(v -> {
            editSearch.setText("");
        });
        resetClearList.setOnClickListener(v -> {
            SensoroBlueManager.searchBlueTooth(sensoroDeviceListener);
            deviceArrayList.clear();
            multiTypeAdapterBlue.notifyDataSetChanged();
        });
        secondParameterCgxhSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String data = (String) secondParameterCgxhSelect.getItemAtPosition(position);//从spinner中获取被选择的数据
                //绑定值
                if (data.equals("3EV")) {
                    qiangCi = 1;
                    daoLiu = 1;
                } else if (data.equals("无磁正反脉冲")) {
                    qiangCi = 0;
                    daoLiu = 1;
                    editXinhaoqiangduStart.setText("-90");
                } else if (data.equals("2EV")) {
                    qiangCi = 1;
                    daoLiu = 0;
                    editXinhaoqiangduStart.setText("-80");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        initQRScanner();
        initTestClick();
        sAtInstructRealizeFactory = new SAtInstructRealizeFactory();

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(CheckSecondActivity.this, R.layout.l_loushanyun_item_spinner, sAtInstructRealizeFactory.getAllMoShi());
        atXindaocanshuSelect.setAdapter(arrayAdapter);

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

        secondParameterOneSetting.setOnClickListener(this::OnBlueToothClick);
        secondParameterOneReading.setOnClickListener(this::OnBlueToothClick);
        secondParameterRoundCreateId.setOnClickListener(this::OnBlueToothClick);

        buttonDuquXinhao.setOnClickListener(this::OnBlueToothClick);
        buttonQiangzhifasong.setOnClickListener(this::OnBlueToothClick);
        textNumSaomiao.setOnClickListener(this::OnBlueToothClick);

        systemStatus.setOnClickListener(this::OnBlueToothClick);
        roundTextNoXiumianJihuo.setOnClickListener(this::OnBlueToothClick);
        buttonDayin.setOnClickListener(this::OnBlueToothClick);
        buttonDuquNew.setOnClickListener(this::OnBlueToothClick);

        buttonDayin.setOnClickListener(this::OnClick);
        textPrintCoon.setOnClickListener(this::OnClick);
        buttonDuquNew.setOnClickListener(this::OnClick);
        textToken.setOnClickListener(this::OnClick);
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
            case R.id.text_print_coon:
                printTool.showPrintDialog();
                break;
            case R.id.button_duqu_new:
                pushEvent(ChipCode.MChipGetNewsInfoRunner, sensoroDeviceChoose.sn, "1", "5");
                break;
            case R.id.text_token:
                String text = "SN:" + sensoroDeviceChoose.sn + ";Token:" + hashMap.get(SAtInstructParams.sAtInstructToken);
                //获取剪贴板管理器：
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 创建普通字符型ClipData
                ClipData mClipData = ClipData.newPlainText("Label", text);
                // 将ClipData内容放到系统剪贴板里。
                cm.setPrimaryClip(mClipData);
                sendMessageToast("已复制到剪切板");
                new Share2.Builder(CheckSecondActivity.this)
                        .setContentType(ShareContentType.TEXT)
                        .setTitle("打印SN")
                        .setTextContent(text)
                        .setOnActivityResult(1000)
                        .build()
                        .shareBySystem();
                break;
        }
    }

    private void initSnAgreement(double hardVersion) {
        sAtInstructRealizeFactory.initHardVersion(hardVersion);
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
        if (type != 2) {
            sendMessageToast("识别不是2号模组，请选择正确的模组");
            return;
        }
        writeCode = 0;
        canPrint = false;
        switch (view.getId()) {
            case R.id.text_num_saomiao:
                saomiaoType = 0;
                start();
                break;
            case R.id.at_xindaocanshu_setting:
                try {

                    snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructChannel).getTwoWriteBytes(atXindaocanshuSelect.getSelectedItem().toString().trim()), "设置信道");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                isAllSetting = false;
                break;
            case R.id.at_kuopinyinzi_setting:
                try {
                    snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructSpreadingFactor).getTwoWriteBytes(atKuopinyinziSelect.getSelectedItem().toString().trim()), "设置扩频因子");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                isAllSetting = false;
                break;
            case R.id.at_fasonggonglv_setting:
                try {
                    snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructSendingPower).getTwoWriteBytes(atFasonggonglvSelect.getSelectedItem().toString().trim()), "设置发送功率");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                isAllSetting = false;
                break;
            case R.id.at_rxdelay_setting:
                try {
                    if (Integer.valueOf(atEditRxdelay.getText().toString().trim()) > 5 || Integer.valueOf(atEditRxdelay.getText().toString().trim()) < 1) {
                        sendMessageToast("只能设置1-5范围内");
                        return;
                    }
                    pushEvent(ChipCode.MChipSetRxDelayRunner, sensoroDeviceChoose.sn, atEditRxdelay.getText().toString().trim());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.at_one_reading:
                try {
//                    if (!hasAtSetting) {
//                        sendMessageToast("请先设置再读取");
//                        return;
//                    }
                    snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructChannel).getTwoReadBytes(), "读取信道中...");
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
                    snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructChannel).getTwoWriteBytes(atXindaocanshuSelect.getSelectedItem().toString().trim()), "设置信道");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                isAllSetting = true;
                break;
/**-------------------------------------------------------------------------------------------------------------------------------------------*/
            case R.id.second_parameter_round_create_id:
                secondParameterEditIdStart.setText(LouShanYunUtils.getTimeID());
                break;
            case R.id.second_parameter_one_reading:
                if (!hasSetting) {
                    sendMessageToast("请先设置再读取");
                    return;
                }
                secondParameterTextInfo.setText("");
                snBlueToothTool.write(DataParser.CMD_METER_INFO, "读取参数中...", true, 2000);
                break;

            case R.id.second_parameter_one_setting:
                if (XHStringUtil.isEmpty(secondParameterEditMaichongStart.getText().toString(), false)) {
                    sendMessageToast("请填入脉冲数");
                    return;
                }
                if (XHStringUtil.isEmpty(secondParameterEditIdStart.getText().toString(), false)) {
                    sendMessageToast("请填入设备ID");
                    return;
                }
                hasSetting = true;
                setDefaultFactoryParams();
                secondParameterTextInfo.setText("");
                break;
            /**-------------------------------------------------------------------------------------------------------------------------------------------*/
            case R.id.system_status:
                isJiHuoAndXiuMian = false;
                if (isJiHuo) {
                    closeJiHuo();
                } else {
                    openJiHuo();
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

            case R.id.round_text_no_xiumian_jihuo:
                isJiHuoAndXiuMian = true;
                isJiHuo = true;
                updata(hashMap);
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
            case R.id.button_duqu_new:
                pushEvent(ChipCode.MChipGetNewsInfoRunner, sensoroDeviceChoose.sn, "1", "5");
                break;
        }
    }

    /**
     * 在onCreate中调用
     */
    private void initQRScanner() {
        mScannerHelper = new QRScannerHelper(this);
        mScannerHelper.setCallBack(new QRScannerHelper.OnScannerCallBack() {
            @Override
            public void onScannerBack(String result) {
                if (!XHStringUtil.isEmpty(result, false)) {
                    if (saomiaoType == 0) {
                        textNum.setText(result);
                    }
                }

            }
        });
    }

    /**
     * 开启扫描界面
     */
    public void start() {
        mScannerHelper.startScanner();
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
    public void onChildConnectSuccess() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hasSetting = false;
                hasAtSetting = false;
                atTextInfo.setText("");
                secondParameterTextInfo.setText("");
                textHuoqu.setText("");
                textNum.setText("");
                textDangqianSn.setText("当前连接的SN设备:" + sensoroDeviceChoose.getSerialNumber().toUpperCase());
                hashMap.clear();
                secondParameterEditIdStart.setText(LouShanYunUtils.getTimeID());
                try {
                    snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sInstructDiscern).getTwoWriteBytes(), "识别模块中...", true, 6000);
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
                        hashMap.putAll(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructChannel).parseTwoRNotifyBytes(result));
                        if (isAllReading) {
                            snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructSendingPower).getTwoReadBytes(), "读取发送功率");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (resultAt.equals(SAtInstructParams.sAtInstructSendingPower + "-R")) {
                    try {
                        hashMap.putAll(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructSendingPower).parseTwoRNotifyBytes(result));
                        if (isAllReading) {
                            snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructFirmwareVersion).getTwoReadBytes(), "读取固件版本");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (resultAt.equals(SAtInstructParams.sAtInstructFirmwareVersion + "-R")) {
                    try {
                        hashMap.putAll(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructFirmwareVersion).parseTwoRNotifyBytes(result));
                        if (isAllReading) {
                            snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructSpreadingFactor).getTwoReadBytes(), "读取扩频因子");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (resultAt.equals(SAtInstructParams.sAtInstructRSSI + "-R")) {
                    try {
                        hashMap.putAll(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructRSSI).parseTwoRNotifyBytes(result));
                        String[] strings = hashMap.get(SAtInstructParams.sAtInstructRSSI).split(",");
                        int rssi = Integer.valueOf(strings[0]);
                        int snr = Integer.valueOf(strings[1]);
                        int rssiStart = Integer.valueOf(editXinhaoqiangduStart.getText().toString());
                        int rssiEnd = Integer.valueOf(editXinhaoqiangduEnd.getText().toString());
                        int snrStart = Integer.valueOf(editXinzaobiStart.getText().toString());
                        int snrEnd = Integer.valueOf(editXinzaobiEnd.getText().toString());
                        String status;
                        if (rssi >= rssiStart && rssi <= rssiEnd && snr >= snrStart && snr <= snrEnd) {
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
                        hashMap.putAll(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructSpreadingFactor).parseTwoRNotifyBytes(result));
                        if (isAllReading) {
                            snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructRxDelay).getTwoReadBytes(), "读取RxDelay");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (resultAt.equals(SAtInstructParams.sAtInstructRxDelay + "-R")) {
                    try {
                        hashMap.putAll(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructRxDelay).parseTwoRNotifyBytes(result));
                        if (isAllReading) {
                            setAtDuShu(hashMap);
                            snBlueToothTool.write(DataParser.CMD_SYSTEM_STATUS, "正在读取设备激活状态");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (resultAt.equals(SAtInstructParams.sAtInstructToken + "-R")) {
                    try {
                        hashMap.putAll(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructToken).parseTwoRNotifyBytes(result));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                textToken.setText("当前设备Token和SN(点击复制到剪贴板):\n" + "SN:" + sensoroDeviceChoose.sn + "\nToken:" + hashMap.get(SAtInstructParams.sAtInstructToken));
//                                try {
//                                    snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructChannel).getTwoReadBytes(), "读取信道中...");
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
                        pushEvent(ChipCode.MChipSetRxDelayRunner, sensoroDeviceChoose.sn, atEditRxdelay.getText().toString().trim());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (resultAt.equals(SAtInstructParams.sAtInstructRxDelay + "-W")) {
                    try {
                        sendMessageToast("设置RxDelay成功");
                        hasAtSetting = true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    sendMessageToast(resultAt);
                }
                break;
            case 0x25:
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
                    hashMap.putAll(DataParser.getInformationAll(result));
                    XLog.i("版本：" + hashMap.get(MapParams.硬件版本));
                    initSnAgreement(Double.valueOf(LouShanYunUtils.getHardWareVersion(Integer.valueOf(hashMap.get(MapParams.硬件版本)))));
                    if (type == 2) {
                        snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructToken).getTwoReadBytes(), "读取Token...");
                    } else {
                        sendMessageToast("识别不是2号模组，请选择正确的模组");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    sendMessageToast("识别失败");
                }
                break;
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
                            secondParameterEditIdStart.setText(LouShanYunUtils.getTimeID());
                            snBlueToothTool.write(DataParser.CMD_METER_INFO, "读取中...", true, 6000);
                        }
                    });
                } else {
                    sendMessageToast("设置失败");
                }
                break;
            case 0x22:
                hashMap.putAll(DataParser.getMoudleNo2(result));
                setDuQu(hashMap);
                break;
            case 0x26:
                if (result[3] == 0) {
                    if ("1".equals(String.valueOf(result[4] & 0xff))) {
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
                } else {
                    runOnUiThread(() -> systemStatus.setVisibility(View.GONE));
                }
                break;
            case 0x24:
                if (result[3] == 0) {
                    if (isJiHuo) {
                        runOnUiThread(() -> {
                            //改成未激活
                            systemStatus.setVisibility(View.VISIBLE);
                            systemStatus.setImageDrawable(getResources().getDrawable(R.drawable.l_loushanyun_active_disable));
                            isJiHuo = false;
                            if (isJiHuoAndXiuMian) {
                                close();
                            }
                        });
                    } else {
                        runOnUiThread(() -> {
                            //改成已激活
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
            default:
                sendMessageToast("命令与模块不对应，请重新选择模块");
        }
    }

    private void setDefaultFactoryParams() {

        if (XHStringUtil.isEmpty(secondParameterEditFactoryId.getText().toString(), false)) {
            ToastUtils.showShort("请输入厂家标识");
            return;
        }
        snBlueToothTool.write(DataParser.getFactoryParams(
                secondParameterCgxhSelect.getSelectedItem().toString().trim(), chaiXie, qiangCi, chuanGanQiZhuangTai, daoLiu, faMenZhuangTai, faSongPinLv, Integer.valueOf(secondParameterEditFactoryId.getText().toString()),
                "0", "0"
        ), "设置1参数中...");
    }


    private void close() {
        snBlueToothTool.write(new byte[]{(byte) 0x68, (byte) 0x02, (byte) 0x25, (byte) 0x00, (byte) 0x27, (byte) 0x16}, "正在让设备进入休眠状态");
    }

    private void setDefaultParams() {
        String id = secondParameterEditIdStart.getText().toString().trim();
        byte[] input2 = DataParser.getBiaoXinxiChuShiHuaCMD(
                Long.parseLong(id),
                Long.parseLong(String.valueOf(secondParameterEditMaichongStart.getText())),
                String.valueOf(secondParameterBeilvSelect.getSelectedItem()));
        snBlueToothTool.write(input2, "设置2参数中...");
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
            if (!switchSaveStatus.isChecked()) {
                if (!XHStringUtil.isEmpty(hashMap.get(MapParams.传感信号), false)) {
                    if ("3".equals(hashMap.get(MapParams.传感信号))) {
                        secondParameterCgxhSelect.setSelection(0);
                    } else if ("1".equals(hashMap.get(MapParams.传感信号))) {
                        secondParameterCgxhSelect.setSelection(1);
                    } else if ("10".equals(hashMap.get(MapParams.传感信号))) {
                        secondParameterCgxhSelect.setSelection(2);
                    }
                }

                if (!XHStringUtil.isEmpty(hashMap.get(MapParams.倍率), false)) {
                    if ("0".equals(hashMap.get(MapParams.倍率))) {
                        secondParameterBeilvSelect.setSelection(0);
                    } else if ("1".equals(hashMap.get(MapParams.倍率))) {
                        secondParameterBeilvSelect.setSelection(1);
                    } else if ("2".equals(hashMap.get(MapParams.倍率))) {
                        secondParameterBeilvSelect.setSelection(2);
                    } else if ("3".equals(hashMap.get(MapParams.倍率))) {
                        secondParameterBeilvSelect.setSelection(3);
                    } else if ("4".equals(hashMap.get(MapParams.倍率))) {
                        secondParameterBeilvSelect.setSelection(4);
                    } else if ("5".equals(hashMap.get(MapParams.倍率))) {
                        secondParameterBeilvSelect.setSelection(5);
                    }
                }
                secondParameterEditIdStart.setText(hashMap.get(MapParams.设备ID));
            }
            secondParameterTextInfo.setText(sb.toString());
            scrollView.fullScroll(ScrollView.FOCUS_DOWN);
        });
    }


    private void updata(HashMap<String, String> hashMap) {
        StringBuilder sb = new StringBuilder();
        if (hashMap.get(MapParams.传感信号) == null) {
            sendMessageToast(" 请进行第二步一键读取");
            return;
        }
        if (hashMap.get(SAtInstructParams.sAtInstructSendingPower) == null) {
            sendMessageToast(" 请进行第一步一键读取");
            return;
        }

        sb.append("软件版本:");
        sb.append(LouShanYunUtils.getSoftWareVersion(Integer.valueOf(hashMap.get(MapParams.软件版本))));
        sb.append(";硬件版本:");
        sb.append(LouShanYunUtils.getHardWareVersion(Integer.valueOf(hashMap.get(MapParams.硬件版本))));
        sb.append(";上送频率:");
        sb.append(LouShanYunUtils.getFSPLReadStringByCode(Long.valueOf(hashMap.get(MapParams.发送频率))));
        sb.append(";发送功率:");
        sb.append(hashMap.get(SAtInstructParams.sAtInstructSendingPower));
        sb.append(";扩频因子:");
        sb.append(hashMap.get(SAtInstructParams.sAtInstructSpreadingFactor));
        sb.append(";RxDelay:");
        sb.append(hashMap.get(SAtInstructParams.sAtInstructRxDelay) + "s");
        sb.append(";信道参数:");
        sb.append(hashMap.get(SAtInstructParams.sAtInstructChannel));
        sb.append(";产品形式:");
        sb.append("远传物联网端");

        sb.append(";LORAWAN参数配置读取:");
        if (hashMap.get(SAtInstructParams.sAtInstructSendingPower) != null && hashMap.get(SAtInstructParams.sAtInstructSpreadingFactor) != null && hashMap.get(SAtInstructParams.sAtInstructRxDelay) != null && hashMap.get(SAtInstructParams.sAtInstructChannel) != null) {
            sb.append("正常");
        } else {
            sendMessageToast(" LORAWAN参数配置异常");
            return;
        }
        sb.append(";传感信号:");
        sb.append(LouShanYunUtils.getCGXHReadStringByCode(Long.valueOf(hashMap.get(MapParams.传感信号))));

        BigDecimal decimal = new BigDecimal(LouShanYunUtils.getBLReadStringByCode(Integer.parseInt(hashMap.get(MapParams.倍率))));
        sb.append(";倍率:  ");
        sb.append(decimal.stripTrailingZeros().toPlainString());
        sb.append(";初始脉冲数:");
        sb.append("5");
        sb.append(";最终脉冲数:");
        //TODO测试用需改回
        int finallynum = Integer.parseInt(hashMap.get(MapParams.当前脉冲读数));
        if (finallynum - 5 < 2) {
            sendMessageToast("请吹两个脉冲数");
            return;
        }
        sb.append(hashMap.get(MapParams.当前脉冲读数));

        // sb.append(new BigDecimal(hashMap.get(MapParams.当前脉冲读数)).multiply(decimal).stripTrailingZeros().toPlainString());
        if (hashMap.get(SAtInstructParams.sAtInstructRSSI) == null) {
            sendMessageToast("请完成第三步强制发送");
            return;
        }

        String[] strings = hashMap.get(SAtInstructParams.sAtInstructRSSI).split(",");
        if (Integer.valueOf(strings[0]) != 0 && Integer.valueOf(strings[1]) != 0) {
            sb.append(";通讯功能:正常");
        } else {
            sendMessageToast("通讯功能异常");
            sb.append(";通讯功能:异常");
            return;
        }
        sb.append(";信号强度:" + Integer.valueOf(strings[0]));
        sb.append(";信噪比:" + Integer.valueOf(strings[1]));
        sb.append(";计数状态:");
        sb.append("正常");
        sb.append(";流向状态:");
        sb.append(hashMap.get(MapParams.表流向状态).equals("0") ? "正流" : "倒流");
        sb.append(";电池状态:");
        sb.append(hashMap.get(MapParams.表电池状态).equals("0") ? "正常" : "欠压");


        CheckUpData checkUpData = new CheckUpData();
        checkUpData.setOreder(dataBean.getOrderNumber());
        if (XHStringUtil.isEmpty(textNum.getText().toString(), false)) {
            sendMessageToast("请输入表身号");
            return;
        }
        checkUpData.setBodynum(textNum.getText().toString());
        checkUpData.setmLoginFactoryNum(LoginParamManager.getInstance().getLoginInfo().getData().getMloginFactoryNum());
        checkUpData.setModuleType(2);
        checkUpData.setInspectionField(sb.toString());
        checkUpData.setChipNum(sensoroDeviceChoose.sn);
        XLog.i("LSYsb====" + new Gson().toJson(checkUpData));
        pushEvent(ChipCode.MChipGetUpdata, new Gson().toJson(checkUpData));
    }


    @Override
    public void onChildConnectFailed(int i) {

    }

    @Override
    public void onChildWriteSuccess() {

    }

    @Override
    public void onChildWriteFailure(int i) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mScannerHelper != null && data != null) {
            mScannerHelper.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void initEventListener() {
        registerEventRunner(ChipCode.MChipGetUpdata, new GetUpDataRunner());
        registerEventRunner(ChipCode.MChipSetRxDelayRunner, new MChipSetRxDelayRunner());
        registerEventRunner(ChipCode.MChipGetNewsInfoRunner, new MChipGetNewsInfoRunner());
    }

    @Override
    protected void onEventRunEnd(Event event, int code) {
        if (code == ChipCode.MChipSetRxDelayRunner) {
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
        } else if (code == ChipCode.MChipGetUpdata) {
            ResultJson resultJson = (ResultJson) event.getReturnParamAtIndex(0);
            int resultcode = resultJson.getCode();
            if (resultcode == 0) {
                closeJiHuo();
            } else if (resultcode == 1) {

            }
            sendMessageToast(resultJson.getMsg(),true);
        } else if (code == ChipCode.MChipGetNewsInfoRunner) {
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
}
