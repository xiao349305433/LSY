package com.loushanyun.modulefactory.v.activity;

import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;

import androidx.core.widget.NestedScrollView;
import androidx.appcompat.widget.SwitchCompat;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.elvishew.xlog.XLog;
import com.flyco.roundview.RoundTextView;
import com.google.gson.Gson;
import com.gpsdk.demo.DeviceConnFactoryManager;
import com.loushanyun.modulefactory.R;
import com.loushanyun.modulefactory.init.FactoryCode;
import com.loushanyun.modulefactory.init.LoginParamManager;
import com.loushanyun.modulefactory.m.FactoryData;
import com.loushanyun.modulefactory.m.FindBodyData;
import com.loushanyun.modulefactory.p.runner.FindBodyRunner;
import com.loushanyun.modulefactory.p.runner.GetManufacturersIdentificationRunner;
import com.loushanyun.modulefactory.p.runner.UploadDataRunner;
import com.qrcore.util.QRScannerHelper;
import com.sensoro.sensor.kit.entity.SensoroDevice;
import com.wu.loushanyun.base.config.K;
import com.wu.loushanyun.base.print.PrintTool;
import com.wu.loushanyun.base.util.DataParser;
import com.wu.loushanyun.base.util.LouShanYunUtils;
import com.wu.loushanyun.base.util.MapParams;
import com.wu.loushanyun.basemvp.init.LouShanYunCode;
import com.wu.loushanyun.basemvp.m.DingDanBean;
import com.wu.loushanyun.basemvp.m.ResultJson;
import com.wu.loushanyun.basemvp.m.sat.SAtInstructRealizeFactory;
import com.wu.loushanyun.basemvp.m.sat.util.SAtInstructParams;
import com.wu.loushanyun.basemvp.p.runner.SelectOrdernumberRunner;
import com.wu.loushanyun.basemvp.v.activity.BaseSnBlueToothActivity;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.dialog.LoadingDialogUtil;
import met.hx.com.base.baseevent.Event;
import met.hx.com.librarybase.some_utils.XHStringUtil;

import static com.wu.loushanyun.basemvp.init.SnBlueToothTool.BLUE_SEND_TIME_OUT;

@Route(path = K.LSYModuleNew02Activity)
public class LSYModuleNew02Activity extends BaseSnBlueToothActivity {
    private NestedScrollView scrollView;
    private TextView textPrint;
    private EditText editPrintNum;
    private RoundTextView textPrintCoon;
    private TextView textDangqianSn;
    private RoundTextView bluetoothConn;
    private RoundTextView buttonDayin;
    private RoundTextView bluetoothDisconn;
    private SwitchCompat switchSaveStatus;
    private Spinner atFasonggonglvSelect;
    private RoundTextView atFasonggonglvSetting;
    private Spinner atXindaocanshuSelect;
    private RoundTextView atXindaocanshuSetting;
    private Spinner atKuopinyinziSelect;
    private RoundTextView atKuopinyinziSetting;
    private RoundTextView atOneSetting;
    private RoundTextView atOneReading;
    private TextView atTextInfo;
    private ImageView systemStatus;
    private Spinner secondParameterCgxhSelect;
    private Spinner secondParameterBeilvSelect;
    private EditText secondParameterEditMaichongStart;
    private EditText secondParameterEditIdStart;
    private RoundTextView secondParameterRoundCreateId;
    private RoundTextView secondParameterOneSetting;
    private RoundTextView secondParameterOneReading;
    private TextView secondParameterTextInfo;
    private EditText editXinhaoqiangduStart;
    private EditText editXinhaoqiangduEnd;
    private EditText editXinzaobiStart;
    private EditText editXinzaobiEnd;
    private TextView textHuoqu;
    private RoundTextView buttonQiangzhifasong;
    private RoundTextView buttonDuquXinhao;
    private Spinner zhizaoshangSelect;
    private EditText textNum;
    private RoundTextView textNumSaomiao;
    private Spinner dingdanSelect;
    private RoundTextView textDingdanNumSaomiao;
    private EditText textKoujing;
    private RoundTextView roundTextXiumianJihuo;
    private RoundTextView roundTextXiumian;
    private RoundTextView roundTextSave;


    private SensoroDevice sensoroDeviceChoose;
    private PrintTool printTool;

    //默认参数text_num_saomiao
    private byte chaiXie = 0;//拆卸   无
    private byte qiangCi = 0;//强磁   无
    private byte chuanGanQiZhuangTai = 0;//传感器状态   无
    private byte daoLiu = 0;//倒流   无
    private byte faMenZhuangTai = 0;//阀门状态   无
    private byte faSongPinLv = (byte) 0x30;//发送频率   默认24小时
    private boolean isAllSetting;
    private boolean isAllReading;
    private boolean isNewID = true;//每次设置完成都让其生成一个新的id
    private boolean isJiHuo = true;
    private boolean isJiHuoAndXiuMian = false;
    private boolean isOneReadingSecond = false;

    private int type;
    private int writeCode;
    private boolean canPrint;
    private boolean isConnectSuccess;
    private SAtInstructRealizeFactory sAtInstructRealizeFactory;
    private HashMap<String, String> hashMap;
    private String[] kuopinyinziArray;
    private QRScannerHelper mScannerHelper;

    private int saomiaoType = 0;
    private ArrayList<String> arrayListDingDan;
    private ArrayAdapter<String> arrayAdapterDingDan;
    private ArrayList<String> arrayListExitLogo;
    private ArrayAdapter<String> arrayAdapterExitLogo;
    private String exitlogo = "";

    @Override
    protected int onChildLayout() {
        return R.layout.m_factory_activity_lsy_2;
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

        scrollView = (NestedScrollView) findViewById(R.id.scrollView);
        textPrint = (TextView) findViewById(R.id.text_print);
        editPrintNum = (EditText) findViewById(R.id.edit_print_num);
        textPrintCoon = (RoundTextView) findViewById(R.id.text_print_coon);
        textDangqianSn = (TextView) findViewById(R.id.text_dangqian_sn);
        bluetoothConn = (RoundTextView) findViewById(R.id.bluetooth_conn);
        buttonDayin = (RoundTextView) findViewById(R.id.button_dayin);
        bluetoothDisconn = (RoundTextView) findViewById(R.id.bluetooth_disconn);
        switchSaveStatus = (SwitchCompat) findViewById(R.id.switch_save_status);
        atFasonggonglvSelect = (Spinner) findViewById(R.id.at_fasonggonglv_select);
        atFasonggonglvSetting = (RoundTextView) findViewById(R.id.at_fasonggonglv_setting);
        atXindaocanshuSelect = (Spinner) findViewById(R.id.at_xindaocanshu_select);
        atXindaocanshuSetting = (RoundTextView) findViewById(R.id.at_xindaocanshu_setting);
        atKuopinyinziSelect = (Spinner) findViewById(R.id.at_kuopinyinzi_select);
        atKuopinyinziSetting = (RoundTextView) findViewById(R.id.at_kuopinyinzi_setting);
        atOneSetting = (RoundTextView) findViewById(R.id.at_one_setting);
        atOneReading = (RoundTextView) findViewById(R.id.at_one_reading);
        atTextInfo = (TextView) findViewById(R.id.at_text_info);
        systemStatus = (ImageView) findViewById(R.id.system_status);
        secondParameterCgxhSelect = (Spinner) findViewById(R.id.second_parameter_cgxh_select);
        secondParameterBeilvSelect = (Spinner) findViewById(R.id.second_parameter_beilv_select);
        secondParameterEditMaichongStart = (EditText) findViewById(R.id.second_parameter_edit_maichong_start);
        secondParameterEditIdStart = (EditText) findViewById(R.id.second_parameter_edit_id_start);
        secondParameterRoundCreateId = (RoundTextView) findViewById(R.id.second_parameter_round_create_id);
        secondParameterOneSetting = (RoundTextView) findViewById(R.id.second_parameter_one_setting);
        secondParameterOneReading = (RoundTextView) findViewById(R.id.second_parameter_one_reading);
        secondParameterTextInfo = (TextView) findViewById(R.id.second_parameter_text_info);
        editXinhaoqiangduStart = (EditText) findViewById(R.id.edit_xinhaoqiangdu_start);
        editXinhaoqiangduEnd = (EditText) findViewById(R.id.edit_xinhaoqiangdu_end);
        editXinzaobiStart = (EditText) findViewById(R.id.edit_xinzaobi_start);
        editXinzaobiEnd = (EditText) findViewById(R.id.edit_xinzaobi_end);
        textHuoqu = (TextView) findViewById(R.id.text_huoqu);
        buttonQiangzhifasong = (RoundTextView) findViewById(R.id.button_qiangzhifasong);
        buttonDuquXinhao = (RoundTextView) findViewById(R.id.button_duqu_xinhao);
        zhizaoshangSelect = (Spinner) findViewById(R.id.zhizaoshang_select);
        textNum = (EditText) findViewById(R.id.text_num);
        textNumSaomiao = (RoundTextView) findViewById(R.id.text_num_saomiao);
        dingdanSelect = (Spinner) findViewById(R.id.dingdan_select);
        textDingdanNumSaomiao = (RoundTextView) findViewById(R.id.text_dingdan_num_saomiao);
        textKoujing = (EditText) findViewById(R.id.text_koujing);
        roundTextXiumianJihuo = (RoundTextView) findViewById(R.id.round_text_xiumian_jihuo);
        roundTextXiumian = (RoundTextView) findViewById(R.id.round_text_xiumian);
        roundTextSave = (RoundTextView) findViewById(R.id.round_text_save);


        secondParameterEditIdStart.setText(LouShanYunUtils.getTimeID());
        secondParameterCgxhSelect.setSelection(2);
        hashMap = new HashMap<>();
        kuopinyinziArray = getResources().getStringArray(R.array.l_loushanyun_kuopinyinzi);
        bluetoothConn.setOnClickListener(v -> {
            if (textDangqianSn.getText().toString().contains("当前连接的SN设备")) {
                sendMessageToast("当前已连接设备");
                return;
            }
            snBlueToothTool.connectBlueTooth(sensoroDeviceChoose);
            clearAllText();
            clearText();
        });
        bluetoothDisconn.setOnClickListener(v -> {
            snBlueToothTool.disconnect();
            textDangqianSn.setText("当前未连接SN设备");
        });
        secondParameterCgxhSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String data = (String) secondParameterCgxhSelect.getItemAtPosition(position);//从spinner中获取被选择的数据
                //绑定值
                if (data.equals("3EV")) {
                } else if (data.equals("无磁正反脉冲")) {
                    editXinhaoqiangduStart.setText("-90");
                } else if (data.equals("2EV")) {
                    editXinhaoqiangduStart.setText("-80");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        initTestClick();
        initQRScanner();
        atKuopinyinziSelect.setSelection(3);
        sAtInstructRealizeFactory = new SAtInstructRealizeFactory();

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(LSYModuleNew02Activity.this, R.layout.l_loushanyun_item_spinner, sAtInstructRealizeFactory.getAllMoShi());
        atXindaocanshuSelect.setAdapter(arrayAdapter);

                XLog.i("arrayListExitLogo ====="+LoginParamManager.getInstance().getProductRegister().getManufacturersIdentification());
        arrayListExitLogo = LouShanYunCode.getAllZhiZaoShang(LoginParamManager.getInstance().getProductRegister().getManufacturersIdentification());
        arrayAdapterExitLogo = new ArrayAdapter<String>(this, R.layout.l_loushanyun_item_spinner, arrayListExitLogo);
        zhizaoshangSelect.setAdapter(arrayAdapterExitLogo);


        arrayListDingDan = new ArrayList<>();
        arrayAdapterDingDan = new ArrayAdapter<String>(this, R.layout.l_loushanyun_item_spinner, arrayListDingDan);
        dingdanSelect.setAdapter(arrayAdapterDingDan);
    }


    private void clearAllText() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textHuoqu.setText("");
            }
        });

    }

    private void clearText() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textNum.setText("");
                textKoujing.setText("");
            }
        });

    }

    private void initTestClick() {
        atXindaocanshuSetting.setOnClickListener(this::OnBlueToothClick);
        atKuopinyinziSetting.setOnClickListener(this::OnBlueToothClick);
        atFasonggonglvSetting.setOnClickListener(this::OnBlueToothClick);
        atOneSetting.setOnClickListener(this::OnBlueToothClick);
        atOneReading.setOnClickListener(this::OnBlueToothClick);

        secondParameterOneSetting.setOnClickListener(this::OnBlueToothClick);
        secondParameterOneReading.setOnClickListener(this::OnBlueToothClick);
        secondParameterRoundCreateId.setOnClickListener(this::OnBlueToothClick);

        buttonDuquXinhao.setOnClickListener(this::OnBlueToothClick);
        buttonQiangzhifasong.setOnClickListener(this::OnBlueToothClick);
        roundTextXiumian.setOnClickListener(this::OnBlueToothClick);
        textNumSaomiao.setOnClickListener(this::OnBlueToothClick);
        textDingdanNumSaomiao.setOnClickListener(this::OnBlueToothClick);
        systemStatus.setOnClickListener(this::OnClick);
        roundTextXiumianJihuo.setOnClickListener(this::OnBlueToothClick);
        roundTextSave.setOnClickListener(this::OnBlueToothClick);
        buttonDayin.setOnClickListener(this::OnClick);
        textPrintCoon.setOnClickListener(this::OnClick);
    }

    private void initSnAgreement(double hardVersion) {
        sAtInstructRealizeFactory.initHardVersion(hardVersion);
    }

    /**
     * 在onCreate中调用
     */
    private void initQRScanner() {
        mScannerHelper = new QRScannerHelper(this);
        mScannerHelper.setCallBack(new QRScannerHelper.OnScannerCallBack() {
            @Override
            public void onScannerBack(String result) {
                if (saomiaoType == 0) {
                    result = result.substring(1);
                    textNum.setText(result);
                    pushEvent(LouShanYunCode.SelectOrdernumberRunner, result,  LouShanYunCode.getBiaoShi(zhizaoshangSelect.getSelectedItem().toString())==""?exitlogo:LouShanYunCode.getBiaoShi(zhizaoshangSelect.getSelectedItem().toString())
                            , LoginParamManager.getInstance().getProductRegister().getManufacturersIdentification());
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
        if (Double.valueOf(LouShanYunUtils.getHardWareVersion(Integer.valueOf(hashMap.get(MapParams.硬件版本)))) < 1.07) {
            sendMessageToast("识别2号模组版本小于1.07请升级固件后再试");
            return;
        }
        if (isJiHuo) {
            sendMessageToast("请点击右上角激活按钮，设置成未激活状态，再做配置操作", true);
            return;
        }
        writeCode = 0;
        canPrint = false;
        switch (view.getId()) {
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
            case R.id.at_one_reading:
                try {
                    snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructChannel).getTwoReadBytes(), "读取信道中...");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                isAllReading = true;
                atTextInfo.setText("");
                break;
            case R.id.at_one_setting:
                try {
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
                secondParameterTextInfo.setText("");
                isOneReadingSecond = true;
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
                setDefaultFactoryParams();
                secondParameterTextInfo.setText("");
                break;
/**-------------------------------------------------------------------------------------------------------------------------------------------*/
            case R.id.round_text_xiumian:
                close();
                break;
            case R.id.button_duqu_xinhao:
                try {
                    snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructRSSI).getTwoReadBytes(), "读取信号强度");
                    isAllReading = false;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.round_text_xiumian_jihuo:
                isJiHuoAndXiuMian = true;
                isJiHuo = false;
                openJiHuo();
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
            case R.id.text_num_saomiao:
                saomiaoType = 0;
                start();
                break;
            case R.id.text_dingdan_num_saomiao:
                if (XHStringUtil.isEmpty(textNum.getText().toString(), false)) {
                    sendMessageToast("请输入表身号");
                    return;
                }
                pushEvent(LouShanYunCode.SelectOrdernumberRunner, textNum.getText().toString(),  LouShanYunCode.getBiaoShi(zhizaoshangSelect.getSelectedItem().toString())==""?exitlogo:LouShanYunCode.getBiaoShi(zhizaoshangSelect.getSelectedItem().toString())
                        , LoginParamManager.getInstance().getProductRegister().getManufacturersIdentification());
                break;
            case R.id.round_text_save:
                boolean isHigher = LouShanYunUtils.isHigherGuJian(hashMap.get(SAtInstructParams.sAtInstructFirmwareVersion));

                if (XHStringUtil.isEmpty(atTextInfo.getText().toString(), false)) {
                    sendMessageToast("请配置和读取模组上送参数");
                    return;
                }
                if (!isHigher) {
                    sendMessageToast("固件版本过低，不能进行保存");
                    return;
                }
                if (XHStringUtil.isEmpty(secondParameterTextInfo.getText().toString(), false)) {
                    sendMessageToast("请配置和读取水表参数");
                    return;
                }
//                if (XHStringUtil.isEmpty(textHuoqu.getText().toString(), false)) {
//                    sendMessageToast("请进行强制发送测试与基站的通信是否良好");
//                    return;
//                }
                if (isJiHuo) {
                    sendMessageToast("请点击右上角激活按钮，设置成未激活状态，否则无法保存");
                    scrollView.scrollTo(0, 0);
                    return;
                }
                if (XHStringUtil.isEmpty(textNum.getText().toString(), false)) {
                    sendMessageToast("请扫码获取或填写表身号");
                    return;
                }
                if (arrayListDingDan.size() <= 0) {
                    sendMessageToast("请获取订单号");
                    return;
                }
                if (XHStringUtil.isEmpty(textKoujing.getText().toString(), false)) {
                    sendMessageToast("请填写水表口径");
                    return;
                }
                String batteryState = hashMap.get(MapParams.表电池状态);
                String magneticInterferenceState = hashMap.get(MapParams.表强磁状态);
                String disassemblyState = hashMap.get(MapParams.表拆卸状态);
                String backflowState = hashMap.get(MapParams.表流向状态);
                int thirdPowerState = 2;
                int circumscribedPowerState = 2;
                int measuringMode = 0;
                String productForm = LouShanYunUtils.getCPXSUploadIntByValue(LouShanYunUtils.getCPXSReadStringByCode(Long.valueOf(hashMap.get(MapParams.产品形式)))) + "";
                String gonglv = hashMap.get(SAtInstructParams.sAtInstructSendingPower);
                String string = "2";//基站民用2  公用1
                String string5 = "";
                String string2 = "20" + hashMap.get(MapParams.出厂时间_年) + "-" + hashMap.get(MapParams.出厂时间_月) + "-" + hashMap.get(MapParams.出厂时间_日);
                String string3 = LouShanYunUtils.getHardWareVersion(Integer.valueOf(hashMap.get(MapParams.硬件版本)));
                String string4 = LouShanYunUtils.getSoftWareVersion(Integer.valueOf(hashMap.get(MapParams.软件版本)));
                String sf = hashMap.get(SAtInstructParams.sAtInstructSpreadingFactor);
                String channel = hashMap.get(SAtInstructParams.sAtInstructChannel);
                String impulseInitial = hashMap.get(MapParams.当前脉冲读数);
                BigDecimal decimal = new BigDecimal(LouShanYunUtils.getBLReadStringByCode(Integer.parseInt(hashMap.get(MapParams.倍率))));
                String pulseConstant = new BigDecimal(1).divide(decimal).stripTrailingZeros().toPlainString();
                int cjbs_wlwd = LoginParamManager.getInstance().getProductRegister().getManufacturersIdentification();
                JSONObject factoryData = new FactoryData().getFactoryData(sensoroDeviceChoose.getSerialNumber(),
                        string, 2, 1, 3, "470MHZ",
                        gonglv, string2, "无", string3, string4, batteryState,
                        magneticInterferenceState, disassemblyState, backflowState, 0, 0,
                        thirdPowerState, circumscribedPowerState, LouShanYunUtils.getCGXHUploadIntByValue(LouShanYunUtils.getCGXHReadStringByCode(hashMap.get(MapParams.传感信号))), measuringMode,
                        productForm, string5, 1, cjbs_wlwd, 0, sf, channel, impulseInitial, pulseConstant,
                        textKoujing.getText().toString(), textNum.getText().toString(), dingdanSelect.getSelectedItem().toString(), LouShanYunCode.getBiaoShi(zhizaoshangSelect.getSelectedItem().toString())==""?exitlogo:LouShanYunCode.getBiaoShi(zhizaoshangSelect.getSelectedItem().toString()));
                pushEvent(FactoryCode.UploadDataRunner, factoryData.toString());
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mScannerHelper != null) {
            mScannerHelper.onActivityResult(requestCode, resultCode, data);
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
            case R.id.system_status:
                isJiHuoAndXiuMian = false;
                if (isJiHuo) {
                    closeJiHuo();
                } else {
                    openJiHuo();
                }
                break;
        }
    }

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        super.onInitAttribute(ba);
        ba.mTitleText = "2号模组出厂配置";
    }

    @Override
    public void onChildConnectFailed(int i) {

    }

    @Override
    public void onChildConnectSuccess() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                atTextInfo.setText("");
                secondParameterTextInfo.setText("");
                textHuoqu.setText("");
                hashMap.clear();
                isConnectSuccess = true;
                secondParameterEditIdStart.setText(LouShanYunUtils.getTimeID());
                textDangqianSn.setText("当前连接的SN设备:" + sensoroDeviceChoose.getSerialNumber().toUpperCase());
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
                        if (rssi > rssiStart && rssi < rssiEnd && snr > snrStart && snr < snrEnd) {
                            status = "合格";
                        } else {
                            status = "不合格";
                        }
                        runOnUiThread(() -> {
                            String sb = "信号强度：" + rssi + "\n" + "信噪比：" + snr + "\n" + "状态：" + status;
                            textHuoqu.setText(sb);
//                            pushEventNoProgress(ChipCode.MChipGetNewsInfoRunner, sensoroDeviceChoose.sn, "1", "5");
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (resultAt.equals(SAtInstructParams.sAtInstructSpreadingFactor + "-R")) {
                    try {
                        hashMap.putAll(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructSpreadingFactor).parseTwoRNotifyBytes(result));
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
//                                textToken.setText("当前设备Token和SN(点击复制到剪贴板):\n" + "SN:" + sensoroDeviceChoose.sn + "\nToken:" + hashMap.get(SAtInstructParams.sAtInstructToken));
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
                        snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructChannel).getTwoReadBytes(), "读取信道中...");
                        isAllReading = true;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                atTextInfo.setText("");
                            }
                        });
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
                    initSnAgreement(Double.valueOf(LouShanYunUtils.getHardWareVersion(Integer.valueOf(hashMap.get(MapParams.硬件版本)))));
                    if (type == 2) {
//                        snBlueToothTool.write(DataParser.CMD_METER_INFO, "读取参数中...", true, 2000);
//                        isOneReadingSecond = false;
                        snBlueToothTool.write(DataParser.CMD_SYSTEM_STATUS, "正在读取设备激活状态");
                        pushEvent(FactoryCode.FindBodyRunner, sensoroDeviceChoose.sn, 2);
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
                            isOneReadingSecond = true;
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
                try {
                    if (!isOneReadingSecond) {
                        snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructChannel).getTwoReadBytes(), "读取信道中...");
                        isAllReading = true;
                    }
                    if (hashMap.get(MapParams.厂家标识).equals(String.valueOf(LoginParamManager.getInstance().getProductRegister().getManufacturersIdentification()))) {
                        setDuQu(hashMap, null);
                    } else {
                        sendMessageToast("获取到该设备的厂家标识不是本账号的厂家，请设置厂家标识信息！！");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 0x26:
                if (result[3] == 0) {
                    if ("1".equals(String.valueOf(result[4] & 0xff))) {
                        runOnUiThread(() -> {
                            isJiHuo = true;
                            //已激活
                            systemStatus.setVisibility(View.VISIBLE);
                            systemStatus.setImageDrawable(getResources().getDrawable(R.drawable.l_loushanyun_active_enable));
                            if (isConnectSuccess) {
                                closeJiHuo();
                            }
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
                            if (isConnectSuccess) {
                                isConnectSuccess = false;
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
//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        snBlueToothTool.write(DataParser.getFactoryParams(
                secondParameterCgxhSelect.getSelectedItem().toString().trim(), chaiXie, qiangCi, chuanGanQiZhuangTai, daoLiu, faMenZhuangTai, faSongPinLv, Integer.valueOf(LoginParamManager.getInstance().getProductRegister().getManufacturersIdentification()),
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
        sb.append("\n扩频因子:  ");
        sb.append(hashMap.get(SAtInstructParams.sAtInstructSpreadingFactor));
        sb.append("\nLSY-IOT版本号:  ");
        sb.append(hashMap.get(SAtInstructParams.sAtInstructFirmwareVersion));
        runOnUiThread(() -> {
//            if (!switchSaveStatus.isChecked()) {
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
//            }
            atTextInfo.setText(sb.toString());
        });
    }

    private void setDuQu(HashMap<String, String> hashMap, String manufacturersIdentification) {
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
        sb.append("\n产品形式:  ");
        sb.append(LouShanYunUtils.getCPXSReadStringByCode(Long.valueOf(hashMap.get(MapParams.产品形式))));
        sb.append("\n采集场景:  ");
        sb.append(LouShanYunUtils.getCJCJReadStringByCode(hashMap.get(MapParams.采集场景)));
        sb.append("\n信号类型:  ");
        sb.append(hashMap.get(MapParams.信号类型).equals("0") ? "模拟信号" : "数字信号");
        sb.append("\n厂家标识:  ");
        if (!XHStringUtil.isEmpty(manufacturersIdentification, false)) {
            sb.append(manufacturersIdentification);
        }
        sb.append("\n出厂日期:  ");
        sb.append("20" + hashMap.get(MapParams.出厂时间_年) + "-" + hashMap.get(MapParams.出厂时间_月) + "-" + hashMap.get(MapParams.出厂时间_日));
        sb.append("\n硬件版本:  ");
        sb.append(LouShanYunUtils.getHardWareVersion(Integer.valueOf(hashMap.get(MapParams.硬件版本))));
        sb.append("\n软件版本:  ");
        sb.append(LouShanYunUtils.getSoftWareVersion(Integer.valueOf(hashMap.get(MapParams.软件版本))));
        sb.append("\n表拆卸状态:  ");
        sb.append(hashMap.get(MapParams.表拆卸状态).equals("0") ? "无" : "拆卸");
        sb.append("\n表强磁状态:  ");
        sb.append(hashMap.get(MapParams.表强磁状态).equals("0") ? "无" : "强磁");
        sb.append("\n表流向状态:  ");
        sb.append(hashMap.get(MapParams.表流向状态).equals("0") ? "正流" : "倒流");
        sb.append("\n表电池状态:  ");
        sb.append(hashMap.get(MapParams.表电池状态).equals("0") ? "正常" : "欠压");
        runOnUiThread(() -> {
//            if (!switchSaveStatus.isChecked()) {
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
//                secondParameterEditIdStart.setText(hashMap.get(MapParams.设备ID));
//            }
            secondParameterTextInfo.setText(sb.toString());
//            scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            if (XHStringUtil.isEmpty(manufacturersIdentification, false)) {
                pushEventNoProgress(FactoryCode.GetManufacturersIdentificationRunner, Long.valueOf(hashMap.get(MapParams.厂家标识)));
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
    protected int getThemeColor() {
        return getResources().getColor(R.color.base_Q1);
    }

    @Override
    protected void initEventListener() {
        registerEventRunner(FactoryCode.UploadDataRunner, new UploadDataRunner());
        registerEventRunner(FactoryCode.GetManufacturersIdentificationRunner, new GetManufacturersIdentificationRunner());
        registerEventRunner(LouShanYunCode.SelectOrdernumberRunner, new SelectOrdernumberRunner());
        registerEventRunner(FactoryCode.FindBodyRunner, new FindBodyRunner());
    }

    @Override
    protected void onEventRunEnd(Event event, int code) {
        if (code == FactoryCode.UploadDataRunner) {
            int codeReturn = (int) event.getReturnParamAtIndex(0);
            if (codeReturn == 0) {
                close();
            }
            sendMessageToast((String) event.getReturnParamAtIndex(1), true);
        } else if (code == FactoryCode.GetManufacturersIdentificationRunner) {
            if (event.isSuccess()) {
                String str = (String) event.getReturnParamAtIndex(0);
                if (!XHStringUtil.isEmpty(str, false)) {
                    setDuQu(hashMap, str);
                }
            }
        } else if (code == LouShanYunCode.SelectOrdernumberRunner) {
            if (event.isSuccess()) {
                ResultJson resultJson=new Gson().fromJson(event.getReturnParamAtIndex(0).toString(),ResultJson.class);
                if (resultJson.getCode() == 0) {
                    DingDanBean dingDanBean=new Gson().fromJson(event.getReturnParamAtIndex(0).toString(),DingDanBean.class);
                    arrayListDingDan.clear();
                    for (int i = 0; i < dingDanBean.getData().size(); i++) {
                        arrayListDingDan.add(dingDanBean.getData().get(i).getOrdernumber());
                    }
                    arrayAdapterDingDan.notifyDataSetChanged();
                } else {
                    sendMessageToast(resultJson.getMsg());
                }

            }
        } else if (code == FactoryCode.FindBodyRunner) {
            if (event.isSuccess()) {
                FindBodyData findBodyData = (FindBodyData) event.getReturnParamAtIndex(0);
                if (findBodyData.getCode() == 0) {
                    arrayListExitLogo.clear();
                    arrayListExitLogo.add(findBodyData.getData().getmLoginSupplier());
                    arrayAdapterExitLogo.notifyDataSetChanged();
                    zhizaoshangSelect.setSelection(0);
                    exitlogo=findBodyData.getData().getExitlogos();
                    textNum.setEnabled(false);
                    textNumSaomiao.setEnabled(false);
                    textNum.setText(findBodyData.getData().getBodyNum());
                    arrayListDingDan.add(findBodyData.getData().getOrderber());
                    arrayAdapterDingDan.notifyDataSetChanged();
                    dingdanSelect.setSelection(0);
                } else {
                    sendMessageToast(findBodyData.getMsg());
                    textNumSaomiao.setEnabled(true);
                    textNum.setEnabled(true);

                }

            }
        }
    }
}
