package wu.loushanyun.com.fivemoduleapp.v.activity;

import android.content.Intent;
import android.graphics.Color;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SwitchCompat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.elvishew.xlog.XLog;
import com.flyco.roundview.RoundLinearLayout;
import com.flyco.roundview.RoundRelativeLayout;
import com.flyco.roundview.RoundTextView;
import com.google.gson.Gson;
import com.gpsdk.demo.DeviceConnFactoryManager;
import com.sensoro.sensor.kit.entity.SensoroDevice;
import com.wu.loushanyun.base.config.K;
import com.wu.loushanyun.base.print.PrintTool;
import com.wu.loushanyun.base.util.DataParser;
import com.wu.loushanyun.base.util.LouShanYunUtils;
import com.wu.loushanyun.base.util.MapParams;
import com.wu.loushanyun.basemvp.init.LouShanYunCode;
import com.wu.loushanyun.basemvp.m.NewInfo;
import com.wu.loushanyun.basemvp.m.SaveDataMeter;
import com.wu.loushanyun.basemvp.m.sat.SAtInstructRealizeFactory;
import com.wu.loushanyun.basemvp.m.sat.util.FirstNumBlueToothUtil;
import com.wu.loushanyun.basemvp.m.sat.util.SAtInstructParams;
import com.wu.loushanyun.basemvp.p.adapter.MeterViewBinder;
import com.wu.loushanyun.basemvp.p.runner.GetChangJiaBiaoShiRunner;
import com.wu.loushanyun.basemvp.p.runner.MChipGetNewsInfoRunner;
import com.wu.loushanyun.basemvp.p.runner.MChipSetRxDelayRunner;
import com.wu.loushanyun.basemvp.v.activity.BaseSnBlueToothActivity;
import com.yanzhenjie.nohttp.Logger;

import org.greenrobot.eventbus.EventBus;
import org.litepal.LitePal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableOnSubscribe;
import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.dialog.LoadingDialogUtil;
import met.hx.com.base.base.multitype.MultiTypeAdapter;
import met.hx.com.base.base.rx.RxSchedulers;
import met.hx.com.base.baseevent.Event;
import met.hx.com.librarybase.some_utils.ByteConvertUtils;
import met.hx.com.librarybase.some_utils.TimeUtils;
import met.hx.com.librarybase.some_utils.ToastUtils;
import met.hx.com.librarybase.some_utils.XHStringUtil;
import wu.loushanyun.com.fivemoduleapp.R;
import wu.loushanyun.com.libraryfive.init.LoginFiveParamManager;
import wu.loushanyun.com.libraryfive.m.RefreshMainRepair;
import wu.loushanyun.com.libraryfive.m.RepairUpdateData;
import wu.loushanyun.com.libraryfive.m.SaveDataConverter;
import wu.loushanyun.com.libraryfive.m.YuanChuanSaveData;

import static com.wu.loushanyun.basemvp.init.SnBlueToothTool.BLUE_SEND_TIME_OUT;
import static com.wu.loushanyun.basemvp.init.SnBlueToothTool.READ_BIAO_TIME_OUT;

@Route(path = K.YuanChenBiaoHaoActivity_107)
public class YuanChenBiaoHaoActivity_107 extends BaseSnBlueToothActivity implements MeterViewBinder.OnZhiShuListener {
    private ScrollView scrollView;
    private TextView textPrint;
    private EditText editPrintNum;
    private RoundTextView textPrintCoon;
    private TextView textDangqianSn;
    private RoundTextView bluetoothConn;
    private RoundTextView buttonDayin;
    private RoundTextView bluetoothDisconn;
    private RoundLinearLayout roundTest1;
    private Spinner atFasonggonglvSelect;
    private RoundTextView atFasonggonglvSetting;
    private Spinner atXindaocanshuSelect;
    private RoundTextView atXindaocanshuSetting;
    private Spinner atKuopinyinziSelect;
    private RoundTextView atKuopinyinziSetting;
    private RoundTextView atOneSetting;
    private RoundTextView atOneReading;
    private TextView atTextInfo;
    private RoundLinearLayout roundTest2;
    private Spinner wangluojiaohuSelect;
    private RoundTextView oneModuleSetting;
    private RoundTextView oneModuleReading;
    private TextView moduleTextInfo;
    private RoundLinearLayout roundLinearDianya;
    private RoundTextView buttonDuquDianya;
    private TextView textDuquDianya;
    private RoundTextView oneDibanSetting;
    private RoundTextView oneDibanReading;
    private TextView dibanTextInfo;
    private ImageView systemStatus;
    private RoundTextView roundTextOpen;
    private RoundTextView roundTextClose;
    private RoundRelativeLayout num;
    private EditText numStart;
    private View divide;
    private EditText numEnd;
    private RoundTextView numSetting;
    private RoundRelativeLayout roundChongzhi;
    private SwitchCompat switchCheckedId;
    private TextView numReadStart;
    private TextView numReadEnd;
    private RoundRelativeLayout meterReadLayout;
    private TextView meterRead;
    private TextView meterReadMiss;
    private TextView accept;
    private TextView disAccept;
    private TextView meterMiss;
    private RecyclerView resultList;
    private RoundLinearLayout roundTest3;
    private TextView textHuoqu;
    private RoundTextView buttonQiangzhifasong;
    private RoundTextView buttonDuquXinhao;
    private TextView textHuoquNew;
    private RoundTextView buttonDuquNew;
    private RoundLinearLayout roundTest4;
    private RoundTextView buttonSave;
    private RoundTextView roundTextXiumian;
    private EditText atEditRxdelay;
    private RoundTextView atRxdelaySetting;
    private RoundTextView roundHuanjingces;

    private PrintTool printTool;
    private HashMap<String, String> hashMap;
    private String[] kuopinyinziArray;
    private SAtInstructRealizeFactory sAtInstructRealizeFactory;

    private SensoroDevice sensoroDeviceChoose;
    private int type;
    private boolean isAllAtReading;
    private boolean isAllAtSetting;
    private boolean isAllModuleReading;
    private boolean isAllDiBanReading;
    private boolean isAllDiBanSetting;
    private Double softVersion;

    private int typeJiHuo = 2;//判断底板是否激活 1已激活  0未激活


    private int meterStart;
    private int meterEnd;
    private int meterReadStart;
    private int meterReadEnd;
    private int currentNum;
    private int currentMissIndex;
    private boolean isReadMiss = false;
    private boolean showLog = true;
    Comparator comparator = (Comparator<SaveDataMeter>) (o1, o2) -> {
        int a = Integer.parseInt(o1.getMeterNumber());
        int b = Integer.parseInt(o2.getMeterNumber());
        if (a > b) {
            return 1;
        }
        if (a < b) {
            return -1;
        }
        return 0;
    };

    private ArrayList<SaveDataMeter> meterArrayList;
    private ArrayList<SaveDataMeter> meterMissArrayList;
    private MeterViewBinder meterViewBinder;
    private MultiTypeAdapter multiTypeAdapter;
    private int meterMisssNum = 0;
    private SaveDataMeter resetSaveDataMeter;
    private boolean isJiZhongQiOpened = false;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private boolean hasSetTime;
    private boolean isSave;

    private String oldSn;
    private boolean isOnlyRead = false;
    private String factoryName = "";

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        getAllData();
        super.onInitAttribute(ba);
        ba.mTitleText = "物联网集中器1.07以上程序初始化";
        if (sensoroDeviceChoose != null) {
            ba.mHasRightView = false;
        } else {
            ba.mHasRightView = true;
        }
    }

    private void getAllData() {
        oldSn = getIntent().getStringExtra("oldSn");
        sensoroDeviceChoose = getIntent().getParcelableExtra("sensoroDevice");
        isOnlyRead = getIntent().getBooleanExtra("isOnlyRead", false);//是否是只读取检查
    }

    @Override
    protected void initLifeCycle() {
        super.initLifeCycle();
        meterViewBinder = new MeterViewBinder(this);
        meterViewBinder.setOnChaoBiaoListener(new MeterViewBinder.OnChaoBiaoListener() {
            @Override
            public void onChaoBiao(SaveDataMeter saveDataMeter) {
            }
        });
        registerLifeCycle(meterViewBinder);
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
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        textPrint = (TextView) findViewById(R.id.text_print);
        editPrintNum = (EditText) findViewById(R.id.edit_print_num);
        textPrintCoon = (RoundTextView) findViewById(R.id.text_print_coon);
        textDangqianSn = (TextView) findViewById(R.id.text_dangqian_sn);
        bluetoothConn = (RoundTextView) findViewById(R.id.bluetooth_conn);
        buttonDayin = (RoundTextView) findViewById(R.id.button_dayin);
        bluetoothDisconn = (RoundTextView) findViewById(R.id.bluetooth_disconn);
        roundTest1 = (RoundLinearLayout) findViewById(R.id.round_test1);
        atFasonggonglvSelect = (Spinner) findViewById(R.id.at_fasonggonglv_select);
        atFasonggonglvSetting = (RoundTextView) findViewById(R.id.at_fasonggonglv_setting);
        atXindaocanshuSelect = (Spinner) findViewById(R.id.at_xindaocanshu_select);
        atXindaocanshuSetting = (RoundTextView) findViewById(R.id.at_xindaocanshu_setting);
        atKuopinyinziSelect = (Spinner) findViewById(R.id.at_kuopinyinzi_select);
        atKuopinyinziSetting = (RoundTextView) findViewById(R.id.at_kuopinyinzi_setting);
        atOneSetting = (RoundTextView) findViewById(R.id.at_one_setting);
        atOneReading = (RoundTextView) findViewById(R.id.at_one_reading);
        atTextInfo = (TextView) findViewById(R.id.at_text_info);
        roundTest2 = (RoundLinearLayout) findViewById(R.id.round_test2);
        wangluojiaohuSelect = (Spinner) findViewById(R.id.wangluojiaohu_select);
        oneModuleSetting = (RoundTextView) findViewById(R.id.one_module_setting);
        oneModuleReading = (RoundTextView) findViewById(R.id.one_module_reading);
        moduleTextInfo = (TextView) findViewById(R.id.module_text_info);
        roundLinearDianya = (RoundLinearLayout) findViewById(R.id.round_linear_dianya);
        buttonDuquDianya = (RoundTextView) findViewById(R.id.button_duqu_dianya);
        textDuquDianya = (TextView) findViewById(R.id.text_duqu_dianya);
        oneDibanSetting = (RoundTextView) findViewById(R.id.one_diban_setting);
        oneDibanReading = (RoundTextView) findViewById(R.id.one_diban_reading);
        dibanTextInfo = (TextView) findViewById(R.id.diban_text_info);
        systemStatus = (ImageView) findViewById(R.id.system_status);
        roundTextOpen = (RoundTextView) findViewById(R.id.round_text_open);
        roundTextClose = (RoundTextView) findViewById(R.id.round_text_close);
        num = (RoundRelativeLayout) findViewById(R.id.num);
        numStart = (EditText) findViewById(R.id.num_start);
        divide = (View) findViewById(R.id.divide);
        numEnd = (EditText) findViewById(R.id.num_end);
        numSetting = (RoundTextView) findViewById(R.id.num_setting);
        roundChongzhi = (RoundRelativeLayout) findViewById(R.id.round_chongzhi);
        switchCheckedId = (SwitchCompat) findViewById(R.id.switch_checked_id);
        numReadStart = (TextView) findViewById(R.id.num_read_start);
        numReadEnd = (TextView) findViewById(R.id.num_read_end);
        meterReadLayout = (RoundRelativeLayout) findViewById(R.id.meter_read_layout);
        meterRead = (TextView) findViewById(R.id.meter_read);
        meterReadMiss = (TextView) findViewById(R.id.meter_read_miss);
        accept = (TextView) findViewById(R.id.accept);
        disAccept = (TextView) findViewById(R.id.dis_accept);
        meterMiss = (TextView) findViewById(R.id.meter_miss);
        resultList = (RecyclerView) findViewById(R.id.result_list);
        roundTest3 = (RoundLinearLayout) findViewById(R.id.round_test3);
        textHuoqu = (TextView) findViewById(R.id.text_huoqu);
        buttonQiangzhifasong = (RoundTextView) findViewById(R.id.button_qiangzhifasong);
        buttonDuquXinhao = (RoundTextView) findViewById(R.id.button_duqu_xinhao);
        textHuoquNew = (TextView) findViewById(R.id.text_huoqu_new);
        buttonDuquNew = (RoundTextView) findViewById(R.id.button_duqu_new);
        roundTest4 = (RoundLinearLayout) findViewById(R.id.round_test4);
        buttonSave = (RoundTextView) findViewById(R.id.button_save);
        roundTextXiumian = (RoundTextView) findViewById(R.id.round_text_xiumian);
        atEditRxdelay = (EditText) findViewById(R.id.at_edit_rxdelay);
        atRxdelaySetting = (RoundTextView) findViewById(R.id.at_rxdelay_setting);
        roundHuanjingces = (RoundTextView) findViewById(R.id.round_huanjingces);


        if (isOnlyRead) {
            atOneSetting.setVisibility(View.GONE);
            oneModuleSetting.setVisibility(View.GONE);
            oneDibanSetting.setVisibility(View.GONE);
            buttonSave.setVisibility(View.GONE);
            oneDibanReading.setVisibility(View.VISIBLE);
        } else {
            atOneSetting.setVisibility(View.VISIBLE);
            oneModuleSetting.setVisibility(View.VISIBLE);
            oneDibanSetting.setVisibility(View.VISIBLE);
            buttonSave.setVisibility(View.VISIBLE);
            oneDibanReading.setVisibility(View.GONE);
        }
        hashMap = new HashMap<>();
        kuopinyinziArray = getResources().getStringArray(R.array.l_loushanyun_kuopinyinzi);


        meterArrayList = new ArrayList<>();
        meterMissArrayList = new ArrayList<>();
        hashMap = new HashMap<>();
        multiTypeAdapter = new MultiTypeAdapter();
        multiTypeAdapter.register(SaveDataMeter.class, meterViewBinder);
        multiTypeAdapter.setItems(meterArrayList);
        resultList.setLayoutManager(new LinearLayoutManager(this));
        resultList.setAdapter(multiTypeAdapter);
        resultList.setNestedScrollingEnabled(false);


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
        ArrayAdapter<String> arrayAdapterJiaoHu = new ArrayAdapter<String>(this, R.layout.l_loushanyun_item_spinner, FirstNumBlueToothUtil.getAllWangLuoJiaoHu());
        wangluojiaohuSelect.setAdapter(arrayAdapterJiaoHu);
        wangluojiaohuSelect.setSelection(2);
        atKuopinyinziSelect.setSelection(3);
        initTestClick();
        if (sensoroDeviceChoose != null) {
            snBlueToothTool.connectBlueTooth(sensoroDeviceChoose);
        }
    }

    @Override
    public void initBlueList() {
        if (sensoroDeviceChoose == null) {
            super.initBlueList();
        }

    }

    private void initTestClick() {
        atXindaocanshuSetting.setOnClickListener(this::OnBlueToothClick);
        atKuopinyinziSetting.setOnClickListener(this::OnBlueToothClick);
        atFasonggonglvSetting.setOnClickListener(this::OnBlueToothClick);
        atOneSetting.setOnClickListener(this::OnBlueToothClick);
        atOneReading.setOnClickListener(this::OnBlueToothClick);
        atRxdelaySetting.setOnClickListener(this::OnBlueToothClick);

        buttonQiangzhifasong.setOnClickListener(this::OnBlueToothClick);
        buttonDuquXinhao.setOnClickListener(this::OnBlueToothClick);
        roundTextXiumian.setOnClickListener(this::OnBlueToothClick);
        oneModuleReading.setOnClickListener(this::OnBlueToothClick);
        oneModuleSetting.setOnClickListener(this::OnBlueToothClick);
        oneDibanReading.setOnClickListener(this::OnBlueToothClick);
        oneDibanSetting.setOnClickListener(this::OnBlueToothClick);
        systemStatus.setOnClickListener(this::OnBlueToothClick);
        buttonSave.setOnClickListener(this::OnBlueToothClick);


        numSetting.setOnClickListener(this::OnBlueToothClick);
        roundChongzhi.setOnClickListener(this::OnBlueToothClick);
        meterRead.setOnClickListener(this::OnBlueToothClick);
        meterReadMiss.setOnClickListener(this::OnBlueToothClick);
        systemStatus.setOnClickListener(this::OnBlueToothClick);
        roundTextOpen.setOnClickListener(this::OnBlueToothClick);
        roundTextClose.setOnClickListener(this::OnBlueToothClick);

        buttonDuquDianya.setOnClickListener(this::OnBlueToothClick);

        buttonDayin.setOnClickListener(this::OnClick);
        textPrintCoon.setOnClickListener(this::OnClick);
        buttonDuquNew.setOnClickListener(this::OnClick);
        roundHuanjingces.setOnClickListener(this::OnClick);
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
                pushEvent(LouShanYunCode.MChipGetNewsInfoRunner, sensoroDeviceChoose.sn, "1", "5");
                break;
            case R.id.round_huanjingces:
                ARouter.getInstance().build(K.EquipmentListActivity).withParcelable("sensoroDevice", sensoroDeviceChoose).navigation();
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
                isAllAtSetting = false;
                break;
            case R.id.at_kuopinyinzi_setting:
                try {
                    snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructSpreadingFactor).getFourWriteBytes(atKuopinyinziSelect.getSelectedItem().toString().trim()), "设置扩频因子");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                isAllAtSetting = false;
                break;
            case R.id.at_fasonggonglv_setting:
                try {
                    snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructSendingPower).getFourWriteBytes(atFasonggonglvSelect.getSelectedItem().toString().trim()), "设置发送功率");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                isAllAtSetting = false;
                break;
            case R.id.at_one_reading:
                try {
                    snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructChannel).getFourReadBytes(), "读取信道中...");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                isAllAtReading = true;
                atTextInfo.setText("");
                break;
            case R.id.at_rxdelay_setting:
                try {
                    if (Integer.valueOf(atEditRxdelay.getText().toString().trim()) > 5 || Integer.valueOf(atEditRxdelay.getText().toString().trim()) < 1) {
                        sendMessageToast("只能设置1-5范围内");
                        return;
                    }
                    pushEvent(LouShanYunCode.MChipSetRxDelayRunner, sensoroDeviceChoose.sn, atEditRxdelay.getText().toString().trim());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.at_one_setting:
                try {
                    snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructChannel).getFourWriteBytes(atXindaocanshuSelect.getSelectedItem().toString().trim()), "设置信道");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                isAllAtSetting = true;
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
                    isAllAtReading = false;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
/**-------------------------------------------------------模组设置读取------------------------------------------------------------------------------------*/
            case R.id.one_module_reading:
                try {
                    isAllModuleReading = false;
                    snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sInstructDiscern).getFourWriteBytes(), "识别模块中...", true, 6000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                moduleTextInfo.setText("");
                break;
            case R.id.one_module_setting:
                snBlueToothTool.write(FirstNumBlueToothUtil.getFouWangluoJiaoHuBytes(wangluojiaohuSelect.getSelectedItem().toString().replaceAll("最大发送", "").replaceAll("次", "")), "正在设置网络交互");
                break;
/**-------------------------------------------------------底板设置读取------------------------------------------------------------------------------------*/
            case R.id.one_diban_reading:
                snBlueToothTool.write(DataParser.CMD_READ_TIME, "正在读取时间");
                dibanTextInfo.setText("");
                break;
            case R.id.one_diban_setting:
                snBlueToothTool.write(DataParser.getByteForSettingTime(), "正在设置时间");
                break;
            case R.id.system_status:
                isSave = false;
                if (typeJiHuo == 1) {
                    snBlueToothTool.write(DataParser.getSystemStatusSettingCMD(false), "关闭激活中");
                } else if (typeJiHuo == 0) {
                    snBlueToothTool.write(DataParser.getSystemStatusSettingCMD(true), "开启激活中");
                }
                break;
            case R.id.button_duqu_dianya:
                byte[] DY = {(byte) 0x68, (byte) 0x01, (byte) 0x43, (byte) 0x44, (byte) 0x16};
                snBlueToothTool.write(DY, "正在读取电池电压");
                break;
/**-------------------------------------------------------读表相关------------------------------------------------------------------------------------*/
            case R.id.round_text_open:
                snBlueToothTool.write(DataParser.openJiZhongQi(), "正在打开集中器");
                break;
            case R.id.round_text_close:
                snBlueToothTool.write(DataParser.closeJiZhongQi(), "正在关闭集中器");
                break;
            case R.id.num_setting:
                if (XHStringUtil.isEmpty(numStart.getText().toString(), false)) {
                    sendMessageToast("请输入起表号");
                    return;
                }
                if (XHStringUtil.isEmpty(numEnd.getText().toString(), false)) {
                    sendMessageToast("请输入止表号");
                    return;
                }
                meterStart = Integer.valueOf(numStart.getText().toString());
                meterEnd = Integer.valueOf(numEnd.getText().toString());
                if (hashMap != null) {
                    //起末表号的设置
                    HashMap<String, String> parmas = new HashMap<>();
                    parmas.put(MapParams.总线起止表号_起, String.valueOf(meterStart));
                    parmas.put(MapParams.总线起止表号_止, String.valueOf(meterEnd));
                    parmas.put(MapParams.仪表通信号, "0");
                    parmas.put(MapParams.初始化表计状态, "0");
                    parmas.put(MapParams.倍率, "0");
                    parmas.put(MapParams.安装脉冲底数, "0");
                    parmas.put(MapParams.口径, "0");
                    parmas.put(MapParams.发送频率, "0");
                    parmas.put(MapParams.保留字节, "0");
                    parmas.put(MapParams.设备ID, "0");
                    snBlueToothTool.write(DataParser.getDiBanBiaoJiChuShiHuaCMD(parmas), "设置表号");
                }
                break;
            case R.id.round_chongzhi:
                meterArrayList.clear();
                meterMissArrayList.clear();
                meterMisssNum = 0;
                accept.setText(String.valueOf(meterArrayList.size()));
                disAccept.setText(String.valueOf(meterReadEnd - meterArrayList.size()));
                meterMiss.setText(String.valueOf(meterMisssNum));
                meterRead.setEnabled(true);
                meterReadLayout.getDelegate().setBackgroundColor(getResources().getColor(R.color.l_loushanyun_Q));
                multiTypeAdapter.notifyDataSetChanged();
                break;
            case R.id.meter_read:
                //读表
                if (!isJiZhongQiOpened) {
                    sendMessageToast("请打开集中器");
                    return;
                }
                meterRead.setEnabled(false);
                meterReadLayout.getDelegate().setBackgroundColor(getResources().getColor(R.color.base_global_neutral_gray_c6c9d4));
                currentNum = meterReadStart;
                meterArrayList.clear();
                meterMissArrayList.clear();
                multiTypeAdapter.notifyDataSetChanged();
                isReadMiss = false;
                meterMisssNum = 0;
                accept.setText(String.valueOf(meterArrayList.size()));
                disAccept.setText(String.valueOf(meterReadEnd - meterArrayList.size()));
                meterMiss.setText(String.valueOf(meterMisssNum));
                readBiao(currentNum);
                break;
            case R.id.meter_read_miss:
                if (!isJiZhongQiOpened) {
                    sendMessageToast("请打开集中器");
                    return;
                }
                if (meterMissArrayList.size() == 0) {
                    sendMessageToast("没有漏抄的表");
                    return;
                }
                isReadMiss = true;
                currentMissIndex = 0;
                currentNum = Integer.valueOf(meterMissArrayList.get(currentMissIndex).getMeterNumber());
                readBiao(currentNum);
                break;
/**-------------------------------------------------------保存相关------------------------------------------------------------------------------------*/
            case R.id.button_save:
                if (XHStringUtil.isEmpty(atTextInfo.getText().toString(), false)) {
                    sendMessageToast("请读取模组信道等参数");
                    return;
                }
                if (XHStringUtil.isEmpty(moduleTextInfo.getText().toString(), false)) {
                    sendMessageToast("请读取模组配置参数");
                    return;
                }
                if (XHStringUtil.isEmpty(dibanTextInfo.getText().toString(), false)) {
                    sendMessageToast("请读取底板配置参数");
                    return;
                }
                if (!hasSetTime) {
                    sendMessageToast("请校准时间");
                    return;
                }
                if (meterMisssNum != 0) {
                    sendMessageToast("存在漏抄的表，无法保存");
                    return;
                }
                if (meterArrayList.size() == 0) {
                    sendMessageToast("该设备未读取总线表单元信息，请读取验证之后再保存");
                    return;
                }
                if (XHStringUtil.isEmpty(textHuoqu.getText().toString(), false)) {
                    sendMessageToast("请进行强制发送");
                    return;
                }
                isSave = true;
                typeJiHuo = 0;
                snBlueToothTool.write(DataParser.getSystemStatusSettingCMD(true), "开启激活中");
                break;

        }
    }


    private void openJiZHongQiSuccess() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                roundTextOpen.getDelegate().setBackgroundColor(Color.GRAY);
                roundTextClose.getDelegate().setBackgroundColor(getResources().getColor(R.color.l_loushanyun_Q));
                isJiZhongQiOpened = true;
            }
        });


    }

    private void closeJiZHongQiSuccess() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                roundTextClose.getDelegate().setBackgroundColor(Color.GRAY);
                roundTextOpen.getDelegate().setBackgroundColor(getResources().getColor(R.color.l_loushanyun_Q));
                isJiZhongQiOpened = false;
            }
        });

    }

    private void readBiao(int num) {
        if (switchCheckedId.isChecked()) {
            String str = getRepeadStr(meterArrayList);
            if (!XHStringUtil.isEmpty(str, false)) {
                ToastUtils.showLong(str);
            } else {
                snBlueToothTool.write(DataParser.getDanBiaoDuQuXinXiCMD(num), "正在读" + num + "号表", true, READ_BIAO_TIME_OUT);
            }
        } else {
            snBlueToothTool.write(DataParser.getDanBiaoDuQuXinXiCMD(num), "正在读" + num + "号表", true, READ_BIAO_TIME_OUT);
        }
    }

    private String getRepeadStr(ArrayList<SaveDataMeter> meterArrayList) {
        StringBuffer result = new StringBuffer();
        ArrayList<SaveDataMeter> array = new ArrayList<>();
        for (int i = 0; i < meterArrayList.size(); i++) {
            SaveDataMeter out = meterArrayList.get(i);
            for (int j = i + 1; j < meterArrayList.size(); j++) {
                SaveDataMeter inside = meterArrayList.get(j);
                if (!XHStringUtil.isEmpty(out.getUserId(), false) && !XHStringUtil.isEmpty(inside.getUserId(), false)) {
                    if (i != j && out.getUserId().equals(inside.getUserId())) {
                        if (!array.contains(out)) {
                            array.add(out);
                        }
                        array.add(inside);
                        continue;
                    }
                }
            }
            if (array.size() != 0) {
                break;
            }
        }
        if (array.size() != 0) {
            for (int i = 0; i < array.size(); i++) {
                SaveDataMeter saveDataMeter = array.get(i);
                if (i == array.size() - 1) {
                    result.append(saveDataMeter.getMeterNumber() + "号表的id重复了，都是" + saveDataMeter.getUserId());
                } else {
                    result.append(saveDataMeter.getMeterNumber() + "号表,");
                }
            }
        }
        return result.toString();
    }

    @Override
    protected void onClickBlueListListener(SensoroDevice sensoroDevice) {
        super.onClickBlueListListener(sensoroDevice);
        this.sensoroDeviceChoose = sensoroDevice;
    }

    @Override
    protected int onChildLayout() {
        return R.layout.m_five_activity_first1;
    }

    @Override
    public void onChildConnectFailed(int i) {

    }

    @Override
    protected int getThemeColor() {
        return getResources().getColor(com.wu.loushanyun.R.color.l_loushanyun_Q);
    }

    @Override
    public void onChildConnectSuccess() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textHuoqu.setText("");
                atTextInfo.setText("");
                moduleTextInfo.setText("");
                dibanTextInfo.setText("");
                textDangqianSn.setText("当前连接的SN设备:" + sensoroDeviceChoose.getSerialNumber().toUpperCase());
                try {
                    isAllModuleReading = true;
                    snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sInstructDiscern).getFourWriteBytes(), "识别模块中...", true, 6000);
                } catch (Exception e) {
                    e.printStackTrace();
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
                        if (isAllAtReading) {
                            snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructSendingPower).getFourReadBytes(), "读取发送功率");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (resultAt.equals(SAtInstructParams.sAtInstructSendingPower + "-R")) {
                    try {
                        hashMap.putAll(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructSendingPower).parseFourRNotifyBytes(result));
                        if (isAllAtReading) {
                            snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructFirmwareVersion).getFourReadBytes(), "读取固件版本");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (resultAt.equals(SAtInstructParams.sAtInstructFirmwareVersion + "-R")) {
                    try {
                        hashMap.putAll(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructFirmwareVersion).parseFourRNotifyBytes(result));
                        if (isAllAtReading) {
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
                        runOnUiThread(() -> {
                            String sb = "信号强度：" + rssi + "\n" + "信噪比：" + snr;
                            textHuoqu.setText(sb);
                           // pushEventNoProgress(LouShanYunCode.MChipGetNewsInfoRunner, sensoroDeviceChoose.sn, "1", "5");
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (resultAt.equals(SAtInstructParams.sAtInstructSpreadingFactor + "-R")) {
                    try {
                        hashMap.putAll(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructSpreadingFactor).parseFourRNotifyBytes(result));
                        if (isAllAtReading) {
                            snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructRxDelay).getFourReadBytes(), "读取RxDelay");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (resultAt.equals(SAtInstructParams.sAtInstructRxDelay + "-R")) {
                    try {
                        hashMap.putAll(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructRxDelay).parseFourRNotifyBytes(result));
                        if (isAllAtReading) {
                            setAtDuShu(hashMap);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (resultAt.equals(SAtInstructParams.sAtInstructRxDelay + "-W")) {
                    try {
                        sendMessageToast("设置RxDelay成功");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (resultAt.equals(SAtInstructParams.sAtInstructChannel + "-W")) {
                    try {
                        sendMessageToast("信道设置成功");
                        if (isAllAtSetting) {
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
                        if (isAllAtSetting) {
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
                        isAllAtReading = false;
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
                        try {
                            snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructChannel).getFourReadBytes(), "读取信道中...");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        isAllAtReading = true;
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
            case 0x04:
                if (result[3] == 0) {
                    try {
                        snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sInstructDiscern).getFourWriteBytes(), "识别模块中...", true, 6000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    sendMessageToast("设置网络交互成功");
                } else {
                    sendMessageToast("设置网络交互失败");
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
            case 0x25:
                if (result[3] == 0) {
                    sendMessageToast("设置时间成功");
                    hasSetTime = true;
                    snBlueToothTool.write(DataParser.CMD_READ_TIME, "正在读取时间");
                } else {
                    sendMessageToast("设置时间失败");
                }
                break;
            case 0x26:
                if (result[3] == 0) {
                    try {
                        hashMap.put("底板时间", DataParser.getTimeFromByte(result));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            isSave = false;
                            if (isOnlyRead) {
                                snBlueToothTool.write(DataParser.getDiBanBiaoJiDuQuCMD(), "正在读取激活状态");
                            } else {
                                snBlueToothTool.write(DataParser.getSystemStatusSettingCMD(false), "关闭激活中");
                            }
                        }
                    });
                } else {
                    sendMessageToast("读取失败");
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
            case 0x11:
                try {
                    type = DataParser.getModuleType(result);
                    hashMap.putAll(FirstNumBlueToothUtil.getInformationAll(result));
                    softVersion = Double.valueOf(LouShanYunUtils.getSoftWareVersion(Integer.valueOf(hashMap.get(MapParams.软件版本))));
                    initSnAgreement(softVersion);
                    if (type == 1) {

                        if (softVersion >= 1.08) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    roundLinearDianya.setVisibility(View.VISIBLE);
                                }
                            });
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    roundLinearDianya.setVisibility(View.GONE);
                                }
                            });
                        }
                        if (softVersion >= 1.07) {

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
                setDuQu(null);
                break;
            //读取附加参数
            case 0x31:
                hashMap.putAll(FirstNumBlueToothUtil.getDiBanBiaoJiInfo(result));
                try {
                    meterReadStart = Integer.parseInt(hashMap.get(MapParams.总线起止表号_起));
                    meterReadEnd = Integer.parseInt(hashMap.get(MapParams.总线起止表号_止));
                    if (result[3] == 0) {
                        XLog.i("蓝牙系统状态:" + hashMap.get(MapParams.系统状态));
                        if ("1".equalsIgnoreCase(hashMap.get(MapParams.系统状态))) {
                            typeJiHuo = 1;
                            if (isSave) {
                                save();
                                setDiBanDuShu();
                            }
                        } else {
                            typeJiHuo = 0;
                        }
                        if (!isSave) {
                            snBlueToothTool.write(DataParser.CMD_INFO_BASE, "正在读取底板信息");
                        }
                    }
                } catch (Exception e) {

                }
                break;
            //设置激活停用
            case 0x23:
                if (result[3] == 0) {
                    snBlueToothTool.write(DataParser.getDiBanBiaoJiDuQuCMD(), "正在读取激活状态");
                } else {
                    sendMessageToast("激活状态设置失败");
                }
                break;
            //读取底板基本信息
            case 0x30:
                hashMap.putAll(FirstNumBlueToothUtil.getInformationBase(result));
                setDiBanDuShu();
                break;
            case 0x41:
                if (result[3] == 0) {
                    sendMessageToast("打开集中器成功");
                    openJiZHongQiSuccess();
                } else {
                    sendMessageToast("打开集中器失败");
                }
                break;
            case 0x42:
                if (result[3] == 0) {
                    sendMessageToast("关闭集中器成功");
                    closeJiZHongQiSuccess();
                } else {
                    sendMessageToast("关闭集中器失败");
                }
                break;
            case 0x43:
                if (result[3] == 0) {
                    StringBuffer stringBuffer = new StringBuffer();
                    for (int i = 3; i >= 0; i--) {
                        stringBuffer.append(ByteConvertUtils.conver2BinaryStr(result[i + 4]));
                    }
                    hashMap.put(MapParams.电池电压, String.valueOf(ByteConvertUtils.formatNumber(stringBuffer.toString(), 2)));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textDuquDianya.setText(hashMap.get(MapParams.电池电压) + "V");
                        }
                    });
                }
                break;
            case 0x28:
                if (result[3] == 0) {
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put(MapParams.表号, resetSaveDataMeter.getMeterNumber());
                    hashMap.put(MapParams.HUB号, resetSaveDataMeter.getHub());
                    SaveDataMeter item = new SaveDataMeter(hashMap, resetSaveDataMeter.getParamOrUnit());
                    int index1 = LouShanYunUtils.containsMeter(meterArrayList, item);
                    if (index1 != -1) {
                        meterArrayList.set(index1, item);
                    } else {
                        meterArrayList.add(item);
                    }
                    int index = LouShanYunUtils.containsMeter(meterMissArrayList, item);
                    if (index != -1) {
                        meterMissArrayList.set(index, item);
                    } else {
                        meterMissArrayList.add(item);
                        meterMisssNum++;
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            int hasReadNum = meterArrayList.size();
                            int notReadNum = meterReadEnd - meterArrayList.size();
                            accept.setText(String.valueOf(hasReadNum));
                            disAccept.setText(String.valueOf(notReadNum));
                            meterMiss.setText(String.valueOf(meterMisssNum));
                            Collections.sort(meterArrayList, comparator);
                            Collections.sort(meterMissArrayList, comparator);
                            multiTypeAdapter.notifyDataSetChanged();
                        }
                    });
                    sendMessageToast("置数成功");
                } else if (result[3] == 1) {
                    sendMessageToast("置数失败，表单元和集中器通讯异常");
                } else if (result[3] == 2) {
                    sendMessageToast("置数失败，集中器和底板通讯异常");
                }
                break;
            //读取单表
            case 0x32:
                SaveDataMeter item;
                HashMap<String, String> hashMap = new HashMap<>();
                if (result[3] == 0) {
                    hashMap.putAll(DataParser.getDanBiaoDuQuXinXi(result, softVersion));
                } else if (result[3] == 1) {
                    hashMap.put(MapParams.表号, String.valueOf(currentNum));
                } else if (result[3] == 2) {
                    hashMap.put(MapParams.表号, String.valueOf(currentNum));
                    hashMap.put(MapParams.HUB号, String.valueOf(result[4] & 0xff));
                }
                item = new SaveDataMeter(hashMap, "2");

                int index1 = LouShanYunUtils.containsMeter(meterArrayList, item);
                if (index1 != -1) {
                    meterArrayList.set(index1, item);
                } else {
                    meterArrayList.add(item);
                }
                if (result[3] != 0) {
                    int index = LouShanYunUtils.containsMeter(meterMissArrayList, item);
                    if (index != -1) {
                        meterMissArrayList.set(index, item);
                    } else {
                        meterMissArrayList.add(item);
                        meterMisssNum++;
                    }
                } else {
                    if (meterMissArrayList.size() > currentMissIndex) {
                        if (meterMissArrayList.get(currentMissIndex).getMeterNumber().equals(item.getMeterNumber())) {
                            meterMissArrayList.get(currentMissIndex).setMeterNumber("-1");//证明抄到表了，置为-1
                            meterMisssNum--;
                        }
                    }
                }
                if (isReadMiss) {
                    currentMissIndex++;
                }
                runOnUiThread(() -> {
                    int hasReadNum = meterArrayList.size();
                    int notReadNum = meterReadEnd - meterArrayList.size();
                    accept.setText(String.valueOf(hasReadNum));
                    disAccept.setText(String.valueOf(notReadNum));
                    meterMiss.setText(String.valueOf(meterMisssNum));
                    Collections.sort(meterArrayList, comparator);
                    multiTypeAdapter.notifyDataSetChanged();
                    Collections.sort(meterMissArrayList, comparator);

                    if (isReadMiss) {
                        if (currentMissIndex < meterMissArrayList.size()) {
                            if (!("-1".equals(meterMissArrayList.get(currentMissIndex).getMeterNumber()))) {
                                currentNum = Integer.valueOf(meterMissArrayList.get(currentMissIndex).getMeterNumber());
                                readBiao(currentNum);
                            }
                        } else {
                            for (int i = meterMissArrayList.size() - 1; i >= 0; i--) {
                                if ("-1".equals(meterMissArrayList.get(i).getMeterNumber())) {
                                    meterMissArrayList.remove(i);
                                }
                            }
                        }
                        if (meterMisssNum == 0) {
                            snBlueToothTool.write(DataParser.closeJiZhongQi(), "正在关闭集中器");
                        }
                    } else {
                        if (notReadNum == 0) {
                            snBlueToothTool.write(DataParser.closeJiZhongQi(), "正在关闭集中器");
                        }
                        scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                        if (currentNum < meterReadEnd) {
                            currentNum++;
                            readBiao(currentNum);
                        }
                    }

                });
                break;
        }
    }

    private void save() {
        YuanChuanSaveData yuanChuanSaveData = new YuanChuanSaveData();
        yuanChuanSaveData.setSn(sensoroDeviceChoose.sn.toUpperCase());

        yuanChuanSaveData.setProductForm("" + LouShanYunUtils.getCPXSUploadIntByValue(LouShanYunUtils.getCPXSReadStringByCode(hashMap.get(MapParams.产品形式))));
        yuanChuanSaveData.setManufacturersIdentification(hashMap.get(MapParams.厂家标识));
        yuanChuanSaveData.setGatherScene("" + LouShanYunUtils.getCJCJUploadCodeByString(LouShanYunUtils.getCJCJReadStringByCode(hashMap.get(MapParams.采集场景))));
        yuanChuanSaveData.setFactoryName(factoryName);
        SaveDataConverter saveDataConverter = new SaveDataConverter();
        saveDataConverter.setStartMeterNumber(hashMap.get(MapParams.总线起止表号_起));
        saveDataConverter.setFrequency(LouShanYunUtils.getFSPLReadStringByCode(Long.parseLong(hashMap.get(MapParams.发送频率))).replaceAll("上传一次", ""));
        saveDataConverter.setEndMeterNumber(hashMap.get(MapParams.总线起止表号_止));
        saveDataConverter.setSoftVersion(LouShanYunUtils.getSoftWareVersion(Integer.valueOf(hashMap.get(MapParams.软件版本))));
        saveDataConverter.setHardwareVersion(LouShanYunUtils.getHardWareVersion(Integer.valueOf(hashMap.get(MapParams.硬件版本))));
        saveDataConverter.setEquipmentPower("0");
        saveDataConverter.setPowerType("" + LouShanYunUtils.getDYLXUploadCodeByString(LouShanYunUtils.getDianYuanLeiXin(hashMap.get(MapParams.电源类型))));
        saveDataConverter.setPowerState(hashMap.get(MapParams.底板状态_自备电池状态));
        saveDataConverter.setSensingSignal("" + LouShanYunUtils.getCGXHUploadIntByValue(LouShanYunUtils.getCGXHReadStringByCode(Long.valueOf(hashMap.get(MapParams.传感信号)))));
        saveDataConverter.setEquipmentTime("20" + hashMap.get(MapParams.出厂时间_年) + "-" + (hashMap.get(MapParams.出厂时间_月).length() == 1 ? "0" + hashMap.get(MapParams.出厂时间_月) : hashMap.get(MapParams.出厂时间_月)) + "-" + (hashMap.get(MapParams.出厂时间_日).length() == 1 ? "0" + hashMap.get(MapParams.出厂时间_日) : hashMap.get(MapParams.出厂时间_日)));
        saveDataConverter.setEquipmentType("0");
        saveDataConverter.setPulseConstant("0");
        String[] strings = hashMap.get(SAtInstructParams.sAtInstructRSSI).split(",");
        int rssi = Integer.valueOf(strings[0]);
        int snr = Integer.valueOf(strings[1]);
        hashMap.put(MapParams.信号强度, rssi + "");
        hashMap.put(MapParams.信噪比, snr + "");
        saveDataConverter.setRssi(rssi + "");
        saveDataConverter.setSnr(snr + "");
        saveDataConverter.setSendingPower(hashMap.get(SAtInstructParams.sAtInstructSendingPower));
        saveDataConverter.setSf(hashMap.get(SAtInstructParams.sAtInstructSpreadingFactor));
        saveDataConverter.setChannel(hashMap.get(SAtInstructParams.sAtInstructChannel));
        Gson gson = new Gson();
        yuanChuanSaveData.setJsonSaveDataConverter(gson.toJson(saveDataConverter));
        yuanChuanSaveData.setJsonMeter(gson.toJson(meterArrayList));
        yuanChuanSaveData.setAttrMapJson(DataParser.getJSONObject(hashMap));
        yuanChuanSaveData.setLoginId(String.valueOf(LoginFiveParamManager.getInstance().getLoginData().getId()));
        yuanChuanSaveData.setBusinessLicense(String.valueOf(LoginFiveParamManager.getInstance().getLoginData().getTradeRegister().getBusinessLicense()));
        yuanChuanSaveData.setTime(TimeUtils.getCurTimeString());

        if (!XHStringUtil.isEmpty(oldSn, false)) {
            saveUpdateData(gson.toJson(yuanChuanSaveData));
        } else {
            saveUpdateDataNew(yuanChuanSaveData);
        }
        Logger.i("传过去的数据==" + yuanChuanSaveData.toString());
    }

    private void saveUpdateDataNew(YuanChuanSaveData yuanChuanSaveData) {
        compositeDisposable.add(Flowable.create(
                (FlowableOnSubscribe<Boolean>) emitter ->
                {
                    LoadingDialogUtil.showByEvent("正在保存", this.getClass().getName());
                    long id = 0;
                    int loginid = LoginFiveParamManager.getInstance().getLoginData().getId();
                    List<YuanChuanSaveData> arrayList = LitePal.where("sn = ? and loginid = ?", yuanChuanSaveData.getSn(), loginid + "")
                            .find(YuanChuanSaveData.class);
                    if (arrayList.size() > 0) {
                        id = arrayList.get(0).getBaseObjId();
                    }
                    if (id == 0) {
                        Logger.i("只保存=" + id);
                        yuanChuanSaveData.assignBaseObjId(0);
                        yuanChuanSaveData.saveAsync().listen(success -> {
                            if (success) {
                                emitter.onNext(true);
                            } else {
                                emitter.onNext(false);
                            }

                        });
                    } else {
                        Logger.i("删除然后保存=" + id);
                        LitePal.delete(YuanChuanSaveData.class, id);
                        yuanChuanSaveData.assignBaseObjId(0);
                        yuanChuanSaveData.saveAsync().listen(success -> {
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
                        sendMessageToast("保存成功，请到网络好的地方同步");
                        snBlueToothTool.write(DataParser.getSystemSleepCMD(), "正在休眠");
                    } else {
                        sendMessageToast("保存失败");
                    }
                }));
    }

    /**
     * 保存更换的数据到本地
     *
     * @param json
     */
    private void saveUpdateData(String json) {
//TODO
        compositeDisposable.add(Flowable.create(
                (FlowableOnSubscribe<Boolean>) emitter ->
                {
                    LoadingDialogUtil.showByEvent("正在保存", this.getClass().getName());
                    long id = 0;
                    int loginid = LoginFiveParamManager.getInstance().getLoginData().getId();
                    RepairUpdateData repairUpdateData = new RepairUpdateData(sensoroDeviceChoose.getSerialNumber(), loginid + "", oldSn, "远传表号接入", json);
                    List<RepairUpdateData> arrayList = LitePal.where("oldSn = ? and loginid = ?", oldSn, loginid + "")
                            .find(RepairUpdateData.class);
                    XLog.i("数据==" + arrayList.toString());
                    if (arrayList.size() > 0) {
                        id = arrayList.get(0).getBaseObjId();
                    }
                    if (id == 0) {
                        repairUpdateData.assignBaseObjId(0);
                        repairUpdateData.saveAsync().listen(success -> {
                            if (success) {
                                emitter.onNext(true);
                            } else {
                                emitter.onNext(false);
                            }

                        });
                    } else {
                        LitePal.delete(RepairUpdateData.class, id);
                        repairUpdateData.assignBaseObjId(0);
                        repairUpdateData.saveAsync().listen(success -> {
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
                        sendMessageToast("保存成功，请到网络好的地方同步");
                        snBlueToothTool.write(DataParser.getSystemSleepCMD(), "正在休眠");
                        EventBus.getDefault().post(new RefreshMainRepair());
                    } else {
                        sendMessageToast("保存失败");
                    }
                }));
    }

    private void setDiBanDuShu() {
        StringBuilder sb = new StringBuilder();
        sb.append("底板采集场景:  ");
        sb.append(LouShanYunUtils.getCJCJReadStringByCode(hashMap.get(MapParams.底板采集场景)));
        sb.append("\n设备ID:  ");
        sb.append(hashMap.get(MapParams.设备ID));
        sb.append("\n产品形式:  ");
        sb.append(LouShanYunUtils.getCPXSReadStringByCode(Long.valueOf(hashMap.get(MapParams.产品形式))));
        sb.append("\n传感信号:  ");
        sb.append(LouShanYunUtils.getCGXHReadStringByCode(Long.valueOf(hashMap.get(MapParams.传感信号))));
        sb.append("\n底板时间:  ");
        sb.append(hashMap.get("底板时间"));
        sb.append("\n电源类型:  ");
        sb.append(LouShanYunUtils.getDYLXReadStringByCode(Integer.valueOf(hashMap.get(MapParams.电源类型))));
        sb.append("\n底板出厂时间:  ");
        sb.append("20" + hashMap.get(MapParams.底板出厂时间_年) + "-" + hashMap.get(MapParams.底板出厂时间_月) + "-" + hashMap.get(MapParams.底板出厂时间_日));
        sb.append("\n底板硬件版本:  ");
        sb.append(LouShanYunUtils.getHardWareVersion(Integer.valueOf(hashMap.get(MapParams.底板硬件版本))));
        sb.append("\n底板软件版本:  ");
        sb.append(LouShanYunUtils.getSoftWareVersion(Integer.valueOf(hashMap.get(MapParams.底板软件版本))));
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                numReadStart.setText(String.valueOf(meterReadStart));
                numReadEnd.setText(String.valueOf(meterReadEnd));
                dibanTextInfo.setText(sb.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
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
                    }
                });
            }
        });
    }

    private void initSnAgreement(double hardVersion) {
        sAtInstructRealizeFactory.initHardVersion(hardVersion);
    }

    private void setDuQu(String manufacturersIdentification) {
        StringBuilder sb = new StringBuilder();
        sb.append("发送频率:  ");
        sb.append(LouShanYunUtils.getFSPLReadStringByCode(Long.valueOf(hashMap.get(MapParams.发送频率))));
        sb.append("\n无线频率:  ");
        sb.append("0".equals(hashMap.get(MapParams.无线频率)) ? "433 MHz" : "470 MHz");
        sb.append("\n网络交互:  ");
        sb.append(LouShanYunUtils.getWLJHReadStringByCode(hashMap.get(MapParams.网络交互)));
        sb.append("\n模组采集场景:  ");
        sb.append(LouShanYunUtils.getCJCJReadStringByCode(hashMap.get(MapParams.采集场景)));
        sb.append("\n模组硬件版本:  ");
        sb.append(LouShanYunUtils.getHardWareVersion(Integer.valueOf(hashMap.get(MapParams.硬件版本))));
        sb.append("\n模组软件版本:  ");
        sb.append(LouShanYunUtils.getSoftWareVersion(Integer.valueOf(hashMap.get(MapParams.软件版本))));
        sb.append("\n厂家标识:  ");
        if (!XHStringUtil.isEmpty(manufacturersIdentification, false)) {
            sb.append(manufacturersIdentification);
        }
        sb.append("\n工作模式:  ");
        sb.append(hashMap.get(MapParams.工作模式).equals("1") ? "从机模式" : "主机模式");
        sb.append("\n模组出厂时间:  ");
        sb.append("20" + hashMap.get(MapParams.出厂时间_年) + "-" + hashMap.get(MapParams.出厂时间_月) + "-" + hashMap.get(MapParams.出厂时间_日));
        runOnUiThread(() -> {
            moduleTextInfo.setText(sb.toString());
            if (XHStringUtil.isEmpty(manufacturersIdentification, false)) {
                pushEventNoProgress(LouShanYunCode.GetChangJiaBiaoShiRunner, hashMap.get(MapParams.厂家标识));
            }
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
        registerEventRunner(LouShanYunCode.MChipGetNewsInfoRunner, new MChipGetNewsInfoRunner());
        registerEventRunner(LouShanYunCode.GetChangJiaBiaoShiRunner, new GetChangJiaBiaoShiRunner());
        registerEventRunner(LouShanYunCode.MChipSetRxDelayRunner, new MChipSetRxDelayRunner());
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
        } else if (code == LouShanYunCode.GetChangJiaBiaoShiRunner) {
            if (event.isSuccess()) {
                String name = (String) event.getReturnParamAtIndex(0);
                if (XHStringUtil.isEmpty(name, true)) {
                    return;
                }
                factoryName = name;
                setDuQu(name);
            }
        } else if (code == LouShanYunCode.MChipSetRxDelayRunner) {
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
    public void onZhiShu(SaveDataMeter saveDataMeter) {
        HashMap<String, String> hashMap = saveDataMeter.getMeterMap();
        String[] zhuangtai = new String[8];
        for (int i = 0; i < zhuangtai.length; i++) {
            zhuangtai[i] = "0";
        }
//        if ("1".equals(hashMap.get(MapParams.表强磁状态))) {
//            zhuangtai[7] = "1";
//        }
////        zhuangtai[7] = "1";
//        if ("1".equals(hashMap.get(MapParams.表电池状态))) {
//            zhuangtai[4] = "1";
//        }
////        zhuangtai[4] = "1";
//        if ("1".equals(hashMap.get(MapParams.表流向状态))) {
//            zhuangtai[2] = "1";
//        }
////        zhuangtai[2] = "1";
//        if ("1".equals(hashMap.get(MapParams.表拆卸状态))) {
//            zhuangtai[6] = "1";
//        }
////        zhuangtai[6] = "1";
        String s = "";
        for (int i = 0; i < zhuangtai.length; i++) {
            s = s + zhuangtai[i];
        }
        byte b = (byte) (ByteConvertUtils.binaryToDecimal(s) & 0xff);
        hashMap.put(MapParams.状态, String.valueOf(b));
        snBlueToothTool.write(DataParser.getZhiShuData(hashMap), "正在置数");
        this.resetSaveDataMeter = saveDataMeter;
    }
}
