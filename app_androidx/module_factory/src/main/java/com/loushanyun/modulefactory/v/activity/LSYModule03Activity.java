package com.loushanyun.modulefactory.v.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;

import androidx.appcompat.app.AlertDialog;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ScrollView;
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
import com.loushanyun.modulefactory.m.FindBodyData;
import com.loushanyun.modulefactory.m.ThirdFactoryReturn;
import com.loushanyun.modulefactory.p.runner.FindBodyRunner;
import com.loushanyun.modulefactory.p.runner.IDCheckRunner;
import com.loushanyun.modulefactory.p.runner.LSYModule03UploadRunner;
import com.qrcore.util.QRScannerHelper;
import com.sensoro.sensor.kit.entity.SensoroDevice;
import com.wu.loushanyun.base.config.K;
import com.wu.loushanyun.base.print.PrintTool;
import com.wu.loushanyun.base.util.LouShanYunUtils;
import com.wu.loushanyun.base.util.MapParams;
import com.wu.loushanyun.basemvp.init.LouShanYunCode;
import com.wu.loushanyun.basemvp.init.SnBlueToothListener;
import com.wu.loushanyun.basemvp.init.SnBlueToothTool;
import com.wu.loushanyun.basemvp.m.CreateUserIdInfo;
import com.wu.loushanyun.basemvp.m.DingDanBean;
import com.wu.loushanyun.basemvp.m.ResultJson;
import com.wu.loushanyun.basemvp.m.st3.ST3ByteTool;
import com.wu.loushanyun.basemvp.m.st3.ST3Params;
import com.wu.loushanyun.basemvp.p.runner.CreateUserIdRunner;
import com.wu.loushanyun.basemvp.p.runner.GetChangJiaBiaoShiRunner;
import com.wu.loushanyun.basemvp.p.runner.SelectOrdernumberRunner;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.activity.BaseNoPresenterActivity;
import met.hx.com.base.baseevent.Event;
import met.hx.com.librarybase.some_utils.XHStringUtil;
import met.hx.com.librarybase.some_utils.util.AbSharedUtil;

@Route(path = K.LSYModule03Activity)
public class LSYModule03Activity extends BaseNoPresenterActivity implements SnBlueToothListener {
    private ScrollView scrollView;
    private TextView textDangqianSn;
    private TextView tvWifiStateThird;
    private RoundTextView bluetoothConn;
    private RoundTextView bluetoothDisconn;
    private Spinner secondParameterBeilvSelect;
    private TextView textSecondChangshu;
    private EditText secondParameterEditMaichongStart;
    private TextView textSecondChushizhi;
    private Spinner xinhaoSelect;
    private EditText editNum;
    private EditText editIdStart;
    private RoundTextView roundCreateId;
    private Spinner zhizaoshangSelect;
    private EditText textNum;
    private RoundTextView textNumSaomiao;
    private Spinner dingdanSelect;
    private RoundTextView textDingdanNumSaomiao;
    private EditText textKoujing;
    private RoundTextView buttonOneSettingRead;
    private TextView textSettingReadInfo;
    private RoundTextView buttonDuquYanbiao1;
    private TextView textYanbiao1;
    private RoundTextView buttonSavePrint;

    private PrintTool printTool;
    private SnBlueToothTool snBlueToothTool;
    private HashMap<String, String> hashMapProduct;
    private SensoroDevice sensoroDeviceChoose;
    private ST3ByteTool st3ByteTool;
    private boolean oneSettingRead = false;
    private QRScannerHelper mScannerHelper;

    private String meterNum;
    private String koujing;
    private String dingdanNum;

    private int saomiaoType = 0;
    private ArrayList<String> arrayListDingDan;
    private ArrayAdapter<String> arrayAdapterDingDan;

    private ArrayList<String> arrayListExitLogo;
    private ArrayAdapter<String> arrayAdapterExitLogo;
    private String exitlogo = "";


    @Override
    protected void initLifeCycle() {
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
                        tvWifiStateThird.setText("打印机未连接");
                        break;
                    case DeviceConnFactoryManager.CONN_STATE_CONNECTING:
                        sendMessageToast(getString(com.wu.loushanyun.R.string.str_conn_state_connecting));
                        break;
                    case DeviceConnFactoryManager.CONN_STATE_CONNECTED:
                        sendMessageToast(getString(com.wu.loushanyun.R.string.str_conn_state_connected) + "\n" + printTool.getConnDeviceInfo());
                        tvWifiStateThird.setText("打印机已连接");
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
        snBlueToothTool = new SnBlueToothTool(this);
        snBlueToothTool.setWriteTimeDelay(300);
        registerLifeCycle(snBlueToothTool);
    }

    @Override
    protected void initEventListener() {
        registerEventRunner(FactoryCode.IDCheckRunner, new IDCheckRunner());
        registerEventRunner(FactoryCode.LSYModule03UploadRunner, new LSYModule03UploadRunner());
        registerEventRunner(LouShanYunCode.GetChangJiaBiaoShiRunner, new GetChangJiaBiaoShiRunner());
        registerEventRunner(LouShanYunCode.SelectOrdernumberRunner, new SelectOrdernumberRunner());
        registerEventRunner(FactoryCode.FindBodyRunner, new FindBodyRunner());
        registerEventRunner(LouShanYunCode.MChipCreateUserId, new CreateUserIdRunner());

    }


    @Override
    public void onRightClick(View item) {
        if ("printerOpened".equals(AbSharedUtil.getString(LSYModule03Activity.this, "printerUsedStatus"))) {
            printTool.showPrintPopWindow(item);
        } else {
            sendMessageToast("请在首页打开打印机开关");
        }
    }

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        getAllData();
        ba.mActivityLayoutId = R.layout.m_factory_activity_lsy3;
        ba.mTitleText = "3号模组出厂配置";
        ba.mTitleRightImageIcon = R.drawable.dy;
    }

    private void getAllData() {
        sensoroDeviceChoose = this.getIntent().getParcelableExtra("sensoroDevice");
    }

    @Override
    protected void initView() {
        scrollView = (ScrollView) findViewById(R.id.scroll_view);
        textDangqianSn = (TextView) findViewById(R.id.text_dangqian_sn);
        tvWifiStateThird = (TextView) findViewById(R.id.tv_wifiState_third);
        bluetoothConn = (RoundTextView) findViewById(R.id.bluetooth_conn);
        bluetoothDisconn = (RoundTextView) findViewById(R.id.bluetooth_disconn);
        secondParameterBeilvSelect = (Spinner) findViewById(R.id.second_parameter_beilv_select);
        textSecondChangshu = (TextView) findViewById(R.id.text_second_changshu);
        secondParameterEditMaichongStart = (EditText) findViewById(R.id.second_parameter_edit_maichong_start);
        textSecondChushizhi = (TextView) findViewById(R.id.text_second_chushizhi);
        xinhaoSelect = (Spinner) findViewById(R.id.xinhao_select);
        editNum = (EditText) findViewById(R.id.edit_num);
        editIdStart = (EditText) findViewById(R.id.edit_id_start);
        roundCreateId = (RoundTextView) findViewById(R.id.round_create_id);
        zhizaoshangSelect = (Spinner) findViewById(R.id.zhizaoshang_select);
        textNum = (EditText) findViewById(R.id.text_num);
        textNumSaomiao = (RoundTextView) findViewById(R.id.text_num_saomiao);
        dingdanSelect = (Spinner) findViewById(R.id.dingdan_select);
        textDingdanNumSaomiao = (RoundTextView) findViewById(R.id.text_dingdan_num_saomiao);
        textKoujing = (EditText) findViewById(R.id.text_koujing);
        buttonOneSettingRead = (RoundTextView) findViewById(R.id.button_one_setting_read);
        textSettingReadInfo = (TextView) findViewById(R.id.text_setting_read_info);
        buttonDuquYanbiao1 = (RoundTextView) findViewById(R.id.button_duqu_yanbiao_1);
        textYanbiao1 = (TextView) findViewById(R.id.text_yanbiao_1);
        buttonSavePrint = (RoundTextView) findViewById(R.id.button_save_print);


//        textSettingReadInfo.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
//            }
//        });

        arrayListExitLogo = LouShanYunCode.getAllZhiZaoShang(LoginParamManager.getInstance().getProductRegister().getManufacturersIdentification());
        arrayAdapterExitLogo = new ArrayAdapter<String>(this, R.layout.l_loushanyun_item_spinner, arrayListExitLogo);
        zhizaoshangSelect.setAdapter(arrayAdapterExitLogo);


        arrayListDingDan = new ArrayList<>();
        arrayAdapterDingDan = new ArrayAdapter<String>(this, R.layout.l_loushanyun_item_spinner, arrayListDingDan);
        dingdanSelect.setAdapter(arrayAdapterDingDan);

        secondParameterBeilvSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String beilv = secondParameterBeilvSelect.getSelectedItem().toString();
                BigDecimal decimal = new BigDecimal(beilv);
                textSecondChangshu.setText(new BigDecimal(1).divide(decimal).stripTrailingZeros().toPlainString());
                String maichong = secondParameterEditMaichongStart.getText().toString();
                textSecondChushizhi.setText(new BigDecimal(maichong).multiply(decimal).stripTrailingZeros().toPlainString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        secondParameterEditMaichongStart.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String maichong = s.toString();
                if (!XHStringUtil.isEmpty(maichong, false)) {
                    String beilv = secondParameterBeilvSelect.getSelectedItem().toString();
                    BigDecimal decimal = new BigDecimal(beilv);
                    textSecondChushizhi.setText(new BigDecimal(maichong).multiply(decimal).stripTrailingZeros().toPlainString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        snBlueToothTool.connectBlueTooth(sensoroDeviceChoose);
        hashMapProduct = new HashMap<>();
        bluetoothConn.setOnClickListener(v -> {
            if (textDangqianSn.getText().toString().contains("当前连接的SN设备")) {
                sendMessageToast("当前已连接设备");
                return;
            }
            snBlueToothTool.connectBlueTooth(sensoroDeviceChoose);
            clearAllText();
            clearId();
        });
        bluetoothDisconn.setOnClickListener(v -> {
            snBlueToothTool.disconnect();
            textDangqianSn.setText("当前未连接SN设备");
        });
        st3ByteTool = new ST3ByteTool();
        initTestClick();
        initQRScanner();

    }


    private void initTestClick() {
        buttonDuquYanbiao1.setOnClickListener(this::OnBlueToothClick);
        roundCreateId.setOnClickListener(this::OnBlueToothClick);
        buttonOneSettingRead.setOnClickListener(this::OnBlueToothClick);
        buttonSavePrint.setOnClickListener(this::OnBlueToothClick);
        textNumSaomiao.setOnClickListener(this::OnBlueToothClick);
        textDingdanNumSaomiao.setOnClickListener(this::OnBlueToothClick);
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
                        result = result.substring(1);
                        textNum.setText(result);
                        pushEvent(LouShanYunCode.SelectOrdernumberRunner, result, LouShanYunCode.getBiaoShi(zhizaoshangSelect.getSelectedItem().toString()) == "" ? exitlogo : LouShanYunCode.getBiaoShi(zhizaoshangSelect.getSelectedItem().toString())
                                , LoginParamManager.getInstance().getProductRegister().getManufacturersIdentification());
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

    private void OnBlueToothClick(View view) {
        if (!snBlueToothTool.isConnected()) {
            sendMessageToast("未连接蓝牙");
            return;
        }
        switch (view.getId()) {
            case R.id.round_create_id:
                oneSettingRead = false;
                snBlueToothTool.write(st3ByteTool.setsAtParams(ST3Params.sT3ReadYanBiao).getThirdReadYanBiaoBytes(), "验表");
                editIdStart.setText("");
                break;
            case R.id.button_one_setting_read:
                if (Integer.valueOf(secondParameterEditMaichongStart.getText().toString()) > 16777215) {
                    sendMessageToast("最大只能设置到16777215");
                    return;
                }
                if (XHStringUtil.isEmpty(editIdStart.getText().toString(), false)) {
                    sendMessageToast("请获取设备ID");
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
                meterNum = textNum.getText().toString();
                dingdanNum = dingdanSelect.getSelectedItem().toString();
                koujing = textKoujing.getText().toString() + "MM";

                oneSettingRead = true;
                snBlueToothTool.write(st3ByteTool.setsAtParams(ST3Params.sT3InitHub).getThirdInitBubBytes(), "初始化Bub号");
                break;
            case R.id.button_duqu_yanbiao_1:
                oneSettingRead = false;
                snBlueToothTool.write(st3ByteTool.setsAtParams(ST3Params.sT3ReadMaiChong).getThirdReadBiaoBytes(
                        Integer.valueOf(editNum.getText().toString())), "抄表");
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
                pushEvent(LouShanYunCode.SelectOrdernumberRunner, textNum.getText().toString(), LouShanYunCode.getBiaoShi(zhizaoshangSelect.getSelectedItem().toString()) == "" ? exitlogo : LouShanYunCode.getBiaoShi(zhizaoshangSelect.getSelectedItem().toString())
                        , LoginParamManager.getInstance().getProductRegister().getManufacturersIdentification());
                break;
            case R.id.button_save_print:
                if ("printerOpened".equals(AbSharedUtil.getString(LSYModule03Activity.this, "printerUsedStatus"))) {

                    if (!printTool.isConnected()) {
                        sendMessageToast("请连接打印机");
                        return;
                    }
                }
                if (XHStringUtil.isEmpty(textSettingReadInfo.getText().toString(), false)) {
                    sendMessageToast("请先进行设置和读取参数");
                    return;
                }
                if (XHStringUtil.isEmpty(textYanbiao1.getText().toString(), false)) {
                    sendMessageToast("请抄表测试水表计数功能");
                    return;
                }
                if (Double.valueOf(LouShanYunUtils.getSoftWareVersion(Integer.valueOf(hashMapProduct.get(MapParams.软件版本)))) < 1.07) {
                    sendMessageToast("只能出厂1.07以上的版本");
                    return;
                }
                String sensoroType = LouShanYunUtils.getCGXHReadStringByCode(hashMapProduct.get(MapParams.传感类型));
                int sensoroCode;
                if (sensoroType.equals("3EV")) {
                    sensoroCode = 1;
                } else if (sensoroType.equals("2EV")) {
                    sensoroCode = 3;
                } else {
                    sensoroCode = 2;
                }
                JSONObject modelInfo = new JSONObject();
                try {
                    modelInfo.put("userid", hashMapProduct.get(MapParams.设备ID));
                    modelInfo.put("sensingsignal", String.valueOf(sensoroCode));
                    modelInfo.put("rate", LouShanYunUtils.getBLReadStringByCode(Integer.parseInt(hashMapProduct.get(MapParams.倍率))));
                    modelInfo.put("status", LouShanYunUtils.getZTReadStringByCode(hashMapProduct.get(MapParams.状态)));
                    modelInfo.put("firmwareversion", LouShanYunUtils.getSoftWareVersion(Integer.valueOf(hashMapProduct.get(MapParams.软件版本))));
                    modelInfo.put("date", "20" + hashMapProduct.get(MapParams.出厂时间_年) + "-" + hashMapProduct.get(MapParams.出厂时间_月) + "-" + hashMapProduct.get(MapParams.出厂时间_日));
                    modelInfo.put("manufacturersidentification", hashMapProduct.get(MapParams.企业代码));
                    float impulseInitial = Float.valueOf(hashMapProduct.get(MapParams.脉冲数));
                    modelInfo.put("impulseInitial", impulseInitial);
                    modelInfo.put("caliber", koujing);
                    modelInfo.put("ordernumber", dingdanNum);
                    modelInfo.put("exitlogo", LouShanYunCode.getBiaoShi(zhizaoshangSelect.getSelectedItem().toString()) == "" ? exitlogo : LouShanYunCode.getBiaoShi(zhizaoshangSelect.getSelectedItem().toString()));
                    if (!XHStringUtil.isEmpty(textNum.getText().toString(), false)) {
                        modelInfo.put("tablenumber", meterNum);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                pushEvent(FactoryCode.LSYModule03UploadRunner, modelInfo.toString());
                break;
        }
    }

    @Override
    protected void onEventRunEnd(Event event, int event_code) {
        if (event_code == FactoryCode.IDCheckRunner) {
            ThirdFactoryReturn thirdFactoryReturn = (ThirdFactoryReturn) event.getReturnParamAtIndex(0);
            String strCode = String.valueOf(thirdFactoryReturn.getCode());
            String readId = (String) event.getParamAtIndex(0);
            XLog.i("readId===="+readId);
            // 0 设备未出厂 -1 该设备已存在
            if ("0".equals(strCode)) {
                clearAllText();
                pushEvent(FactoryCode.FindBodyRunner, readId, 3, 0);
            } else if ("-1".equals(strCode)) {
                if (thirdFactoryReturn.getThirdfactorysettings() == null) {
                    clearAllText();
                    pushEvent(FactoryCode.FindBodyRunner, readId, 3, 1);
                } else {
                    new AlertDialog.Builder(this)
                            .setTitle("提示！！！")
                            .setMessage("该设备已出厂配置过了，是否更改其参数？\n该ID原数据如下\n验表的设备ID：" + readId + "\n" + thirdFactoryReturn.getThirdfactorysettings().printThird())
                            .setPositiveButton("是(覆盖该ID下的原数据)", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    editIdStart.setText(readId);
                                    clearAllText();
                                }
                            })
                            .setNegativeButton("否", null)
                            .setCancelable(false)
                            .show();
                }

            } else if ("-2".equals(strCode)) {
                new AlertDialog.Builder(this)
                        .setTitle("提示！！！")
                        .setMessage("该设备ID已被使用在其他厂家账号下，请登录到正确的账号进行出厂配置")
                        .setPositiveButton("好的", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setCancelable(false)
                        .show();
            }
        } else if (event_code == LouShanYunCode.GetChangJiaBiaoShiRunner) {
            if (event.isSuccess()) {
                String name = (String) event.getReturnParamAtIndex(0);
                if (XHStringUtil.isEmpty(name, true)) {
                    return;
                }
                setTextSettingReadInfo(hashMapProduct, name);
            }
        } else if (event_code == LouShanYunCode.SelectOrdernumberRunner) {
            if (event.isSuccess()) {

                ResultJson resultJson = new Gson().fromJson(event.getReturnParamAtIndex(0).toString(), ResultJson.class);
                if (resultJson.getCode() == 0) {
                    DingDanBean dingDanBean = new Gson().fromJson(event.getReturnParamAtIndex(0).toString(), DingDanBean.class);
                    arrayListDingDan.clear();
                    for (int i = 0; i < dingDanBean.getData().size(); i++) {
                        arrayListDingDan.add(dingDanBean.getData().get(i).getOrdernumber());
                    }
                    arrayAdapterDingDan.notifyDataSetChanged();
                } else {
                    sendMessageToast(resultJson.getMsg());
                }

            }
        } else if (event_code == FactoryCode.LSYModule03UploadRunner) {
            String rcode = String.valueOf(event.getReturnParamAtIndex(0));
            String result = String.valueOf(event.getReturnParamAtIndex(1));
            if ("0".equals(rcode)) {
                sendMessageToast("保存成功");
                print();
                clearId();
                return;
            }
            sendMessageToast(result, true);
        } else if (event_code == FactoryCode.FindBodyRunner) {
            if (event.isSuccess()) {
                FindBodyData findBodyData = (FindBodyData) event.getReturnParamAtIndex(0);
                //是否需要重新获取ID
                XLog.i("readId===="+new Gson().toJson(event.getParamAtIndex(1)));
                int needCreateId = (int) event.getParamAtIndex(2);
                String readId = (String) event.getParamAtIndex(0);
                if (findBodyData.getCode() == 0) {
                    arrayListExitLogo.clear();
                    arrayListExitLogo.add(findBodyData.getData().getmLoginSupplier());
                    arrayAdapterExitLogo.notifyDataSetChanged();
                    zhizaoshangSelect.setSelection(0);
                    exitlogo = findBodyData.getData().getExitlogos();
                    textNum.setEnabled(false);
                    textNumSaomiao.setEnabled(false);
                    textNum.setText(findBodyData.getData().getBodyNum());
                    arrayListDingDan.add(findBodyData.getData().getOrderber());
                    arrayAdapterDingDan.notifyDataSetChanged();
                    dingdanSelect.setSelection(0);
                    editIdStart.setText(readId);
                } else {
                    sendMessageToast(findBodyData.getMsg());
                    textNumSaomiao.setEnabled(true);
                    textNum.setEnabled(true);
                    if (needCreateId == 1) {
                        pushEvent(LouShanYunCode.MChipCreateUserId);
                    } else {
                        editIdStart.setText(readId);
                        textNum.setEnabled(true);
                    }

                }

            }
        } else if (event_code == LouShanYunCode.MChipCreateUserId) {
            if (event.isSuccess()) {
                CreateUserIdInfo createUserIdInfo = (CreateUserIdInfo) event.getReturnParamAtIndex(0);
                if (createUserIdInfo.getCode() == 0) {
                    editIdStart.setText(createUserIdInfo.getUserId());
                    textNum.setEnabled(true);
                    sendMessageToast(createUserIdInfo.getMsg());
//                    UserId=createUserIdInfo.getUserId();
//                    snBlueToothTool.write(st3ByteTool.setsAtParams(ST3Params.sT3InitHub).getThirdInitBubBytes(), "初始化Bub号");
                }

            }
        }
    }

    private void print() {
        if ("printerOpened".equals(AbSharedUtil.getString(LSYModule03Activity.this, "printerUsedStatus"))) {
            if (printTool.isConnected()) {
                printTool.print3(hashMapProduct.get(MapParams.设备ID),
                        "20" + hashMapProduct.get(MapParams.出厂时间_年),
                        hashMapProduct.get(MapParams.出厂时间_月), hashMapProduct.get(MapParams.出厂时间_日),
                        LoginParamManager.getInstance().getProductRegister().getCompanyName(), LouShanYunUtils.getCGXHReadStringByCode(hashMapProduct.get(MapParams.传感类型)));
            } else {
                sendMessageToast("请连接打印机");
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mScannerHelper != null) {
            mScannerHelper.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void clearId() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                editIdStart.setText("");
                textNum.setText("");
                textKoujing.setText("");
            }
        });

    }

    private void clearAllText() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textSettingReadInfo.setText("");
                textYanbiao1.setText("");
                hashMapProduct.clear();
                textNum.setText("");
            }
        });

    }

    @Override
    public void onChildConnectFailed(int i) {

    }

    @Override
    public void onChildConnectSuccess() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textDangqianSn.setText("当前连接的SN设备:" + sensoroDeviceChoose.getSerialNumber().toUpperCase());
                clearAllText();
                clearId();
            }
        });
    }

    @Override
    public void onChildNotify(byte[] result) {
        byte code = (byte) (result[2] ^ ((byte) 0x80));
        XLog.i("蓝牙code=" + code);
        switch (code) {
            case 0x60:
                if (result[3] == 0) {
                    if (st3ByteTool.getsT3Type().equals(ST3Params.sT3InitHub)) {
                        if (result[4] == 1 && result[5] == 4 && result[6] == (byte) 0xfe && result[7] == (byte) 0xd0) {
                            sendMessageToast("Hub号设置成功");
                            if (oneSettingRead) {
                                snBlueToothTool.write(st3ByteTool.setsAtParams(ST3Params.sT3InitBiao).getThirdInitBiaoBytes(
                                        Integer.valueOf(editNum.getText().toString())
                                        , Long.valueOf(editIdStart.getText().toString())
                                        , secondParameterBeilvSelect.getSelectedItem().toString()
                                        , Integer.valueOf(secondParameterEditMaichongStart.getText().toString())
                                ), "初始化表单元");
                            }
                        }
                    } else if (st3ByteTool.getsT3Type().equals(ST3Params.sT3InitBiao)) {
                        if (result[5] == 4 && result[6] == (byte) 0xfc) {
                            sendMessageToast("设置成功");
                            if (oneSettingRead) {
                                //生成时间戳
                                Date date1 = new Date();// new Date()为获取当前系统时间，也可使用当前时间戳
                                SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
                                String dateF = df1.format(date1);
                                String[] split = dateF.split("-");
                                String year = split[0].substring(split[0].length() - 2, split[0].length());
                                String month = split[1].toString();
                                String day = split[2].toString();
                                snBlueToothTool.write(st3ByteTool.setsAtParams(ST3Params.sT3SettingInfo).getThirdSettingBiaoBytes(
                                        Integer.valueOf(editNum.getText().toString())
                                        , xinhaoSelect.getSelectedItem().toString()
                                        , Integer.valueOf(year)
                                        , Integer.valueOf(month)
                                        , Integer.valueOf(day)
                                        , LoginParamManager.getInstance().getProductRegister().getManufacturersIdentification()
                                ), "配置表单元");
                            }
                        }
                    } else if (st3ByteTool.getsT3Type().equals(ST3Params.sT3SettingInfo)) {
                        if (result[5] == 4 && result[6] == (byte) 0xfb) {
                            sendMessageToast("设置成功");
                            if (oneSettingRead) {
                                snBlueToothTool.write(st3ByteTool.setsAtParams(ST3Params.sT3ReadMaiChong).getThirdReadBiaoBytes(
                                        Integer.valueOf(editNum.getText().toString())), "抄表");
                            }
                        } else {
                            sendMessageToast("设置失败");
                        }
                    } else if (st3ByteTool.getsT3Type().equals(ST3Params.sT3ReadMaiChong)) {
                        if (result[5] == (byte) 0x0e && result[6] == (byte) 0xfc) {
                            sendMessageToast("抄表成功");
                            if (oneSettingRead) {
                                snBlueToothTool.write(st3ByteTool.setsAtParams(ST3Params.sT3ReadInfo).getThirdReadSettingBytes(
                                        Integer.valueOf(editNum.getText().toString())), "读取配置信息");
                                try {
                                    hashMapProduct.putAll(st3ByteTool.parseThirdReadBiaoBytes(result));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                try {
                                    setTextReadbiao(st3ByteTool.parseThirdReadBiaoBytes(result));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            sendMessageToast("抄表失败");
                        }
                    } else if (st3ByteTool.getsT3Type().equals(ST3Params.sT3ReadInfo)) {
                        if (result[5] == 0x16 && result[6] == (byte) 0xfd) {
                            sendMessageToast("读取配置信息成功");
                            if (oneSettingRead) {
                                try {
                                    hashMapProduct.putAll(st3ByteTool.parseThirdReadSettingBytes(result));
                                    setTextSettingReadInfo(hashMapProduct, null);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            sendMessageToast("读取配置信息失败");
                        }
                    } else if (st3ByteTool.getsT3Type().equals(ST3Params.sT3ReadYanBiao)) {
                        if (result[5] == (byte) 0x0e && result[6] == (byte) 0xfc) {
                            try {
                                setYanBiao(st3ByteTool.parseThirdReadYanBiaoBytes(result));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            sendMessageToast("验表失败");
                        }
                    }
                } else {
                    sendMessageToast("操作失败");
                }

                break;
        }
    }

    private void setYanBiao(HashMap<String, String> hashMap) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(hashMap.get(MapParams.设备ID));
                if (!"0".equals(stringBuilder.toString())) {
                    pushEvent(FactoryCode.IDCheckRunner, hashMap.get(MapParams.设备ID), LoginParamManager.getInstance().getProductRegister().getManufacturersIdentification());
                } else {
                    pushEvent(LouShanYunCode.MChipCreateUserId);
                }


            }
        });

    }


    private void setTextReadbiao(HashMap<String, String> hashMap) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("脉冲常数(个/m³):  ");
        BigDecimal decimal = new BigDecimal(LouShanYunUtils.getBLReadStringByCode(Integer.parseInt(hashMap.get(MapParams.倍率))));
        stringBuilder.append(new BigDecimal(1).divide(decimal).stripTrailingZeros().toPlainString());
        stringBuilder.append("\n倍率(m³/ev):  ");
        stringBuilder.append(decimal.stripTrailingZeros().toPlainString());
        stringBuilder.append("\n脉冲数(个):  ");
        stringBuilder.append(hashMap.get(MapParams.脉冲数));
        stringBuilder.append("\n读数(m³):  ");
        stringBuilder.append(new BigDecimal(hashMap.get(MapParams.脉冲数)).multiply(decimal).stripTrailingZeros().toPlainString());
        stringBuilder.append("\n状态：");
        stringBuilder.append(LouShanYunUtils.getZTReadStringByCode(hashMap.get(MapParams.状态)));
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textYanbiao1.setText(stringBuilder.toString());
            }
        });
    }

    private void setTextSettingReadInfo(HashMap<String, String> hashMap, String manufacturersIdentification) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("表号：");
        stringBuilder.append(hashMap.get(MapParams.表号));
        stringBuilder.append("\n表身号：");
        stringBuilder.append(meterNum);
        stringBuilder.append("\n口径：");
        stringBuilder.append(koujing);
        stringBuilder.append("\n设备ID：");
        stringBuilder.append(hashMap.get(MapParams.设备ID));
        stringBuilder.append("\n脉冲常数(个/m³):  ");
        BigDecimal decimal = new BigDecimal(LouShanYunUtils.getBLReadStringByCode(Integer.parseInt(hashMap.get(MapParams.倍率))));
        stringBuilder.append(new BigDecimal(1).divide(decimal).stripTrailingZeros().toPlainString());
        stringBuilder.append("\n倍率(m³/ev):  ");
        stringBuilder.append(decimal.stripTrailingZeros().toPlainString());
        stringBuilder.append("\n初始脉冲数(个):  ");
        stringBuilder.append(hashMap.get(MapParams.脉冲数));
        stringBuilder.append("\n初始读数(m³):  ");
        stringBuilder.append(new BigDecimal(hashMap.get(MapParams.脉冲数)).multiply(decimal).stripTrailingZeros().toPlainString());
        stringBuilder.append("\nHUB号：");
        stringBuilder.append(hashMap.get(MapParams.HUB号));
        stringBuilder.append("\n生产时间:  ");
        stringBuilder.append("20" + hashMap.get(MapParams.出厂时间_年) + "-" + hashMap.get(MapParams.出厂时间_月) + "-" + hashMap.get(MapParams.出厂时间_日));
        stringBuilder.append("\n传感类型：");
        stringBuilder.append(LouShanYunUtils.getCGXHReadStringByCode(hashMap.get(MapParams.传感类型)));
        stringBuilder.append("\n软件版本：");
        stringBuilder.append(LouShanYunUtils.getSoftWareVersion(Integer.valueOf(hashMap.get(MapParams.软件版本))));
        stringBuilder.append("\n厂家标识：");
        if (!XHStringUtil.isEmpty(manufacturersIdentification, false)) {
            stringBuilder.append(manufacturersIdentification);
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textSettingReadInfo.setText(stringBuilder.toString());
                if (XHStringUtil.isEmpty(manufacturersIdentification, false)) {
                    pushEventNoProgress(LouShanYunCode.GetChangJiaBiaoShiRunner, hashMap.get(MapParams.企业代码));
                }
            }
        });
    }

    @Override
    public void onChildWriteSuccess() {

    }

    @Override
    public void onChildWriteFailure(int i) {

    }
}
