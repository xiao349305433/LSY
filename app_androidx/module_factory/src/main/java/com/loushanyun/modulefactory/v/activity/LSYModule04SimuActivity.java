package com.loushanyun.modulefactory.v.activity;

import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import androidx.appcompat.widget.SwitchCompat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.elvishew.xlog.XLog;
import com.flyco.roundview.RoundLinearLayout;
import com.flyco.roundview.RoundTextView;
import com.google.gson.Gson;
import com.gpsdk.demo.DeviceConnFactoryManager;
import com.loushanyun.modulefactory.R;
import com.loushanyun.modulefactory.init.FactoryCode;
import com.loushanyun.modulefactory.init.LoginParamManager;
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
import com.wu.loushanyun.basemvp.m.sat.util.FourSimuBlueToothUtil;
import com.wu.loushanyun.basemvp.m.sat.util.SAtInstructParams;
import com.wu.loushanyun.basemvp.p.runner.SelectOrdernumberRunner;
import com.wu.loushanyun.basemvp.v.activity.BaseSnBlueToothActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.dialog.LoadingDialogUtil;
import met.hx.com.base.baseevent.Event;
import met.hx.com.librarybase.some_utils.TimeUtils;
import met.hx.com.librarybase.some_utils.XHStringUtil;

import static com.wu.loushanyun.basemvp.init.SnBlueToothTool.BLUE_SEND_TIME_OUT;

@Route(path = K.LSYModule04SimuActivity)
public class LSYModule04SimuActivity extends BaseSnBlueToothActivity {
    private TextView textPrint;

    private RoundTextView textPrintCoon;

    private TextView textDangqianSn;
    private RoundTextView bluetoothConn;
    private RoundTextView buttonDayin;
    private RoundTextView bluetoothDisconn;
    private SwitchCompat switchSaveStatus;


    private Spinner chanpinxingshiSelect;
    private Spinner chuanganmoshiSelect;
    private Spinner chuanganxinhaoSelect;
    private RoundTextView fourOneSetting;
    private RoundTextView fourOneReading;
    private TextView fourTextInfo;
    private ImageView systemStatus;
    private RoundTextView jiaozhunSetting;
    private Spinner beilvSelect;
    private EditText editMaichongStart;
    private RoundTextView leijiSetting;



    private RoundTextView maxChuanganSetting;
    private Spinner mesureSelect;
    private RoundTextView zhuangtaiSetting;
    private RoundTextView leijiChuanganReading;
    private TextView textLeijiInfo;
    private RoundTextView xianxingChuanganReading;
    private TextView textXianxingInfo;
    private RoundTextView zhuangtaiChuanganReading;
    private TextView textZhuangtaiInfo;
    private RoundTextView gongtongChuanganReading;
    private TextView textGongtongInfo;


    private RoundTextView roundTextXiumianJihuo;
    private RoundTextView roundTextXiumian;


    private Spinner atFasonggonglvSelect;
    private RoundTextView atFasonggonglvSetting;
    private Spinner atXindaocanshuSelect;
    private RoundTextView atXindaocanshuSetting;
    private Spinner atKuopinyinziSelect;
    private RoundTextView atKuopinyinziSetting;
    private RoundTextView atOneSetting;
    private RoundTextView atOneReading;
    private TextView atTextInfo;
    private RoundLinearLayout LeijiSettinglayout;
    private RoundLinearLayout XianxingSettinglayout;
    private RoundLinearLayout ZhuangtaiSettinglayout;


    private RoundTextView roundTextFasong;
    private Spinner manufacturerSelect;
    private EditText textNum;
    private RoundTextView textNumSaomiao;
    private Spinner dingdanSelect;
    private RoundTextView textDingdanNumSaomiao;
    private EditText editCaliber;

    private EditText editKChuangan;
    private EditText editBChuangan;
    private LinearLayout twoLayout;
    private TextView  textHuoqu;



    private boolean isAllFourSetting;
    private SensoroDevice sensoroDeviceChoose;
    private SAtInstructRealizeFactory sAtInstructRealizeFactory;
    private PrintTool printTool;

    private int type;

    private HashMap<String, String> hashMap;
    private String[] kuopinyinziArray;

    //默认参数
    private boolean isAllSetting; //是否为 第一步设置传感器参数的一键设置
    private boolean isAllReading; //是否为 第一步设置传感器参数的一键读取
    private boolean isReadAllSuccess = false; //是否为 第二步：配置模拟信号的一键读取
    private boolean isJiHuo = true; //是否交互
    private boolean isJiHuoAndXiuMian = false;//是否交互并休眠

    public static final int ChooseMoshiLeiJi=0; //累计脉冲
    public static final int ChooseXianXing=1; //线性传感
    public static final int ChooseMoshiZhuanTai=2; //状态传感

    public String pinlv_str="";//频率值
    public String wangluojiaohu_str="";//最大发送次数
    public String biaozhun="";//采样标准

    private QRScannerHelper mScannerHelper;
    private int saomiaoType = 0;
    private ArrayList<String> arrayListDingDan;
    private ArrayAdapter<String> arrayAdapterDingDan;
    private ArrayAdapter<String> arrayAdapterCPXS;
    private  ArrayAdapter<String> arrayAdapterShuZi;
    @Override
    protected int onChildLayout() {
        return R.layout.m_factory_activity_four_simu;
    }


    @Override
    protected void initView() {
        super.initView();
        atFasonggonglvSelect = (Spinner) findViewById(R.id.at_fasonggonglv_select);
        atFasonggonglvSetting = (RoundTextView) findViewById(R.id.at_fasonggonglv_setting);
        atXindaocanshuSelect = (Spinner) findViewById(R.id.at_xindaocanshu_select);
        atXindaocanshuSetting = (RoundTextView) findViewById(R.id.at_xindaocanshu_setting);
        atKuopinyinziSelect = (Spinner) findViewById(R.id.at_kuopinyinzi_select);
        atKuopinyinziSetting = (RoundTextView) findViewById(R.id.at_kuopinyinzi_setting);

        atOneSetting = (RoundTextView) findViewById(R.id.at_one_setting);
        atOneReading = (RoundTextView) findViewById(R.id.at_one_reading);
        atTextInfo = (TextView) findViewById(R.id.at_text_info);
        textPrint = (TextView) findViewById(R.id.text_print);
        textPrintCoon = (RoundTextView) findViewById(R.id.text_print_coon);
        textDangqianSn = (TextView) findViewById(R.id.text_dangqian_sn);
        bluetoothConn = (RoundTextView) findViewById(R.id.bluetooth_conn);
        buttonDayin = (RoundTextView) findViewById(R.id.button_dayin);
        bluetoothDisconn = (RoundTextView) findViewById(R.id.bluetooth_disconn);
        switchSaveStatus = (SwitchCompat) findViewById(R.id.switch_save_status);


        chanpinxingshiSelect = (Spinner) findViewById(R.id.chanpinxingshi_select);
        chuanganmoshiSelect = (Spinner) findViewById(R.id.chuanganmoshi_select);
        chuanganxinhaoSelect = (Spinner) findViewById(R.id.chuanganxinhao_select);
        fourOneSetting = (RoundTextView) findViewById(R.id.four_one_setting);
        fourOneReading = (RoundTextView) findViewById(R.id.four_one_reading);
        fourTextInfo = (TextView) findViewById(R.id.four_text_info);
        systemStatus = (ImageView) findViewById(R.id.system_status);
        jiaozhunSetting = (RoundTextView) findViewById(R.id.jiaozhun_setting);
        beilvSelect = (Spinner) findViewById(R.id.beilv_select);
        editMaichongStart = (EditText) findViewById(R.id.edit_maichong_start);
        leijiSetting = (RoundTextView) findViewById(R.id.leiji_setting);

        maxChuanganSetting = (RoundTextView) findViewById(R.id.max_chuangan_setting);
        mesureSelect = (Spinner) findViewById(R.id.mesure_select);
        zhuangtaiSetting = (RoundTextView) findViewById(R.id.zhuangtai_setting);
        leijiChuanganReading = (RoundTextView) findViewById(R.id.leiji_chuangan_reading);
        textLeijiInfo = (TextView) findViewById(R.id.text_leiji_info);
        xianxingChuanganReading = (RoundTextView) findViewById(R.id.xianxing_chuangan_reading);
        textXianxingInfo = (TextView) findViewById(R.id.text_xianxing_info);
        zhuangtaiChuanganReading = (RoundTextView) findViewById(R.id.zhuangtai_chuangan_reading);
        textZhuangtaiInfo = (TextView) findViewById(R.id.text_zhuangtai_info);
        gongtongChuanganReading = (RoundTextView) findViewById(R.id.gongtong_chuangan_reading);
        textGongtongInfo = (TextView) findViewById(R.id.text_gongtong_info);


        roundTextXiumianJihuo = (RoundTextView) findViewById(R.id.round_text_xiumian_jihuo);
        roundTextXiumian = (RoundTextView) findViewById(R.id.round_text_xiumian);
        LeijiSettinglayout = (RoundLinearLayout) findViewById(R.id.Leiji_Settinglayout);
        XianxingSettinglayout = (RoundLinearLayout) findViewById(R.id.Xianxing_Settinglayout);
        ZhuangtaiSettinglayout = (RoundLinearLayout) findViewById(R.id.Zhuangtai_Settinglayout);

        roundTextFasong = (RoundTextView) findViewById(R.id.round_text_fasong);
        manufacturerSelect = (Spinner) findViewById(R.id.manufacturer_select);
        textNum = (EditText) findViewById(R.id.text_num);
        textNumSaomiao = (RoundTextView) findViewById(R.id.text_num_saomiao);
        dingdanSelect = (Spinner) findViewById(R.id.dingdan_select);
        textDingdanNumSaomiao = (RoundTextView) findViewById(R.id.text_dingdan_num_saomiao);
        editCaliber = (EditText) findViewById(R.id.edit_caliber);
        editBChuangan= (EditText) findViewById(R.id.edit_B_chuangan);
        editKChuangan= (EditText) findViewById(R.id.edit_K_chuangan);
        twoLayout= (LinearLayout) findViewById(R.id.two_layout);
        textHuoqu= (TextView) findViewById(R.id.text_huoqu);
        initQRScanner();
        initTestClick();

        sAtInstructRealizeFactory = new SAtInstructRealizeFactory();
        hashMap = new HashMap<>();
        kuopinyinziArray = getResources().getStringArray(R.array.kuopinyinzi);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(LSYModule04SimuActivity.this, R.layout.l_loushanyun_item_spinner, sAtInstructRealizeFactory.getAllMoShi());
        atXindaocanshuSelect.setAdapter(arrayAdapter);

        ArrayAdapter<String> arrayAdapterMoShi = new ArrayAdapter<String>(LSYModule04SimuActivity.this, R.layout.l_loushanyun_item_spinner, FourSimuBlueToothUtil.getChuanGanMoShi());
        chuanganmoshiSelect.setAdapter(arrayAdapterMoShi);
        arrayAdapterShuZi = new ArrayAdapter<String>(LSYModule04SimuActivity.this, R.layout.l_loushanyun_item_spinner, FourSimuBlueToothUtil.getChuanGanXinHaoShuZi(ChooseMoshiLeiJi));
        chuanganxinhaoSelect.setAdapter(arrayAdapterShuZi);
        ArrayAdapter<String> arrayAdapterBeiLv = new ArrayAdapter<String>(this, R.layout.l_loushanyun_item_spinner, FourSimuBlueToothUtil.getAllBeiLv());
        beilvSelect.setAdapter(arrayAdapterBeiLv);
        ArrayAdapter<String> arrayAdapterZhuangTaiBiaoZhun = new ArrayAdapter<String>(this, R.layout.l_loushanyun_item_spinner, FourSimuBlueToothUtil.getAllZhuangTaiBiaoZhun());
        mesureSelect.setAdapter(arrayAdapterZhuangTaiBiaoZhun);

        arrayAdapterCPXS = new ArrayAdapter<String>(this, R.layout.l_loushanyun_item_spinner, FourSimuBlueToothUtil.getAllChanPinXingShi(ChooseMoshiLeiJi));
        chanpinxingshiSelect.setAdapter(arrayAdapterCPXS);
        ArrayAdapter<String> arrayAdaptermanufacturer = new ArrayAdapter<String>(this, R.layout.l_loushanyun_item_spinner,  LouShanYunCode.getAllZhiZaoShang(LoginParamManager.getInstance().getProductRegister().getManufacturersIdentification()));
        manufacturerSelect.setAdapter(arrayAdaptermanufacturer);

        arrayListDingDan = new ArrayList<>();
        arrayAdapterDingDan = new ArrayAdapter<String>(this, R.layout.l_loushanyun_item_spinner, arrayListDingDan);
        dingdanSelect.setAdapter(arrayAdapterDingDan);

        atKuopinyinziSelect.setSelection(3);

        //默认选择累计传感
        chuanganmoshiSelect.setSelection(ChooseMoshiLeiJi);


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


    }


    //修改蓝牙搜索页面的颜色
    @Override
    protected int getThemeColor() {
        return getResources().getColor(R.color.base_Q1);
    }

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        super.onInitAttribute(ba);
      switch (getIntent().getIntExtra(EquipmentListActivity.TYPE,0)) {
          case EquipmentListActivity.MO_NORMORL:
              ba.mTitleText = "4号模组标准版配置";
              pinlv_str="6小时";
              wangluojiaohu_str="最大发送1次";
              biaozhun="12";
              break;
          case EquipmentListActivity.MO_HIGH:
              ba.mTitleText = "4号模组高级版配置";
              pinlv_str="15分钟";
              wangluojiaohu_str="最大发送2次";
              biaozhun="5";
              break;
      }

    }

    @Override
    protected void onClickBlueListListener(SensoroDevice sensoroDevice) {
        super.onClickBlueListListener(sensoroDevice);
        this.sensoroDeviceChoose = sensoroDevice;
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

    private void initTestClick() {
        atXindaocanshuSetting.setOnClickListener(this::OnBlueToothClick);
        atKuopinyinziSetting.setOnClickListener(this::OnBlueToothClick);
        atFasonggonglvSetting.setOnClickListener(this::OnBlueToothClick);
        atOneSetting.setOnClickListener(this::OnBlueToothClick);
        atOneReading.setOnClickListener(this::OnBlueToothClick);
        fourOneReading.setOnClickListener(this::OnBlueToothClick);
        fourOneSetting.setOnClickListener(this::OnBlueToothClick);
        roundTextXiumian.setOnClickListener(this::OnBlueToothClick);
        roundTextXiumianJihuo.setOnClickListener(this::OnBlueToothClick);
        jiaozhunSetting.setOnClickListener(this::OnBlueToothClick);
        leijiSetting.setOnClickListener(this::OnBlueToothClick);
        maxChuanganSetting.setOnClickListener(this::OnBlueToothClick);
        zhuangtaiSetting.setOnClickListener(this::OnBlueToothClick);
        leijiChuanganReading.setOnClickListener(this::OnBlueToothClick);
        xianxingChuanganReading.setOnClickListener(this::OnBlueToothClick);
        zhuangtaiChuanganReading.setOnClickListener(this::OnBlueToothClick);
        gongtongChuanganReading.setOnClickListener(this::OnBlueToothClick);
        textNumSaomiao.setOnClickListener(this::OnBlueToothClick);
        roundTextFasong.setOnClickListener(this::OnBlueToothClick);
        systemStatus.setOnClickListener(this::OnBlueToothClick);
        textDingdanNumSaomiao.setOnClickListener(this::OnBlueToothClick);
        textPrintCoon.setOnClickListener(this::OnClick);
    }



    private void OnClick(View view) {
        switch (view.getId()) {
            case R.id.text_print_coon:
                printTool.showPrintDialog();
                break;
            case R.id.button_dayin:
                if (!XHStringUtil.isEmpty(sensoroDeviceChoose.sn, false)) {
                    LoadingDialogUtil.showByEvent("正在打印", this.loadingTag);
                    printTool.printBitmap(printTool.createChipPrint2(sensoroDeviceChoose.sn));
                    LoadingDialogUtil.dismissByEvent(this.loadingTag);
                }
                break;

            case R.id.button_duqu_new:
                pushEvent(FactoryCode.MChipGetNewsInfoRunner, sensoroDeviceChoose.sn, "1", "5");
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
        // TODO 这里需要取消注释的
        if (type != 5) {
            sendMessageToast("识别不是4号模拟信号模组，请选择正确的模组");
            return;
        }

        switch (view.getId()) {
            case R.id.at_fasonggonglv_setting:
                try {
                    snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructSendingPower).getFourWriteBytes(atFasonggonglvSelect.getSelectedItem().toString().trim()), "设置发送功率");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                isAllSetting = false;
                break;
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

            case R.id.four_one_setting:
                isAllFourSetting = true;
                byte[] input2 = {0x68, 0x02, 0x00, 0x00, 0x02, 0x16};
                snBlueToothTool.write(input2, "设置采集场景中...");
                fourTextInfo.setText("");
                twoLayout.setVisibility(View.GONE);
                break;
            case R.id.four_one_reading:
                isReadAllSuccess = true;
                try {
                    systemStatus.setVisibility(View.GONE);//一键读取的时候先设置隐藏是否激活
                    snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sInstructDiscern).getFourWriteBytes(), "识别模块中...", true, 6000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case R.id.round_text_xiumian:
                close();
                break;

            case R.id.jiaozhun_setting:
                snBlueToothTool.write(FourSimuBlueToothUtil.getFourjiaozhun(), "正在校准模组时间");
                break;
            case R.id.leiji_setting:
                if (XHStringUtil.isEmpty(editMaichongStart.getText().toString(), true)) {
                    sendMessageToast("请填写初始脉冲数");
                    return;
                }
                snBlueToothTool.write(FourSimuBlueToothUtil.getLeiJiSetting(editMaichongStart.getText().toString(), beilvSelect.getSelectedItem().toString()), "初始化累计脉冲");
                break;
            case R.id.max_chuangan_setting: // TODO 输入值大小的限制还没有设定

                snBlueToothTool.write(FourSimuBlueToothUtil.getxianxingSetting(biaozhun, editKChuangan.getText().toString(), editBChuangan.getText().toString()), "初始化线性传感");
                break;
            case R.id.zhuangtai_setting:
                snBlueToothTool.write(FourSimuBlueToothUtil.getzhuantaiSetting(mesureSelect.getSelectedItem().toString()), "初始化状态传感器");
                break;
            case R.id.leiji_chuangan_reading:
                snBlueToothTool.write(FourSimuBlueToothUtil.getleijiZhengReading(), "读取累计传感数据");
                clearAllText();
                break;
            case R.id.xianxing_chuangan_reading:
                snBlueToothTool.write(FourSimuBlueToothUtil.getXianxingYaliReading(), "读取线性传感数据");
                clearAllText();
                break;
            case R.id.zhuangtai_chuangan_reading:
                snBlueToothTool.write(FourSimuBlueToothUtil.getZhuangtaiReading(), "读取状态传感数据");
                clearAllText();
                break;
            case R.id.gongtong_chuangan_reading:
                snBlueToothTool.write(FourSimuBlueToothUtil.getChuanganxinhaoReading(), "读取共有特性数据");
                clearAllText();
                break;
            case R.id.round_text_fasong:
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
            case R.id.system_status:
                isJiHuoAndXiuMian = false;
                if (isJiHuo) {
                    closeJiHuo();
                } else {
                    openJiHuo();
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
                pushEvent(LouShanYunCode.SelectOrdernumberRunner, textNum.getText().toString(), LouShanYunCode.getBiaoShi(manufacturerSelect.getSelectedItem().toString())
                        ,LoginParamManager.getInstance().getProductRegister().getManufacturersIdentification());
                break;
            case R.id.round_text_xiumian_jihuo:

                boolean isHigher = LouShanYunUtils.isHigherGuJian(hashMap.get(SAtInstructParams.sAtInstructFirmwareVersion));

                if (XHStringUtil.isEmpty(atTextInfo.getText().toString(), false)) {
                    sendMessageToast("请配置和读取模组上送参数");
                    return;
                }
                if (!isHigher) {
                    sendMessageToast("固件版本过低，不能进行保存");
                    return;
                }
//                if (XHStringUtil.isEmpty(secondParameterTextInfo.getText().toString(), false)) {
//                    sendMessageToast("请配置和读取水表参数");
//                    return;
//                }
//                if (XHStringUtil.isEmpty(textHuoqu.getText().toString(), false)) {
//                    sendMessageToast("请进行强制发送测试与基站的通信是否良好");
//                    return;
//                }
//                if (isJiHuo) {
//                    sendMessageToast("请点击右上角激活按钮，设置成未激活状态，否则无法保存");
//                    scrollView.scrollTo(0, 0);
//                    return;
//                }
                if (XHStringUtil.isEmpty(textNum.getText().toString(), false)) {
                    sendMessageToast("请扫码获取或填写表身号");
                    return;
                }
                if (arrayListDingDan.size() <= 0) {
                    sendMessageToast("请获取订单号");
                    return;
                }
                if (XHStringUtil.isEmpty(editCaliber.getText().toString(), false)) {
                    sendMessageToast("请填写水表口径");
                    return;
                }


                isJiHuoAndXiuMian = true;
                isJiHuo = false;
                openJiHuo();
                break;

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
                //   textHuoqu.setText("");
                textDangqianSn.setText("当前连接的SN设备:" + sensoroDeviceChoose.getSerialNumber().toUpperCase());
                try {
                    snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sInstructDiscern).getFourWriteBytes(), "识别模块中...", true, 6000);
//                    systemStatus.setVisibility(View.VISIBLE);
//                    systemStatus.setImageDrawable(getResources().getDrawable(R.drawable.l_loushanyun_active_disable));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                    printTool.printBitmap(printTool.createChipPrint2(sensoroDeviceChoose.sn));

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
                }  else if (resultAt.equals(SAtInstructParams.sAtInstructSpreadingFactor + "-R")) {
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
                        //        textToken.setText("当前设备Token和SN(点击复制到剪贴板):\n" + "SN:" + sensoroDeviceChoose.sn + "\nToken:" + hashMap.get(SAtInstructParams.sAtInstructToken));
                                try {
                                    snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructChannel).getFourReadBytes(), "读取信道中...");
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
                }else if (resultAt.equals(SAtInstructParams.sAtInstructRSSI + "-R")) {
                    try {
                        hashMap.putAll(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructRSSI).parseTwoRNotifyBytes(result));
                        String[] strings = hashMap.get(SAtInstructParams.sAtInstructRSSI).split(",");
                        int rssi = Integer.valueOf(strings[0]);
                        int snr = Integer.valueOf(strings[1]);

                        runOnUiThread(() -> {
                            String sb = "信号强度：" + rssi + "\n" + "信噪比：" + snr ;
                            textHuoqu.setText(sb);
//                            pushEventNoProgress(ChipCode.MChipGetNewsInfoRunner, sensoroDeviceChoose.sn, "1", "5");
                        });

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
                } else if (resultAt.equals(SAtInstructParams.sAtInstructRxDelay + "-W")) {
                    try {
                        sendMessageToast("设置RxDelay成功");


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
                }else {
                    sendMessageToast(resultAt);
                }
                break;

            case 0x11:
                try {
                    type = DataParser.getModuleType(result);

                    //TODO 先用type=4 的模组 进行测试
                    initSnAgreement(Double.valueOf(LouShanYunUtils.getHardWareVersion(Integer.valueOf("6"))));
                    if (type == 5) {
                        hashMap.putAll(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sInstructDiscern).parseFourRNotifyBytes(result));
                        snBlueToothTool.write(FourSimuBlueToothUtil.getFourjiaozhun(), "正在校准模组时间");
                        if (!isReadAllSuccess) {
//                            snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructToken).getFourReadBytes(), "读取Token...");
                        } else {
                            //     snBlueToothTool.write(FourSimuBlueToothUtil.getFourCKReadBytes(), "正在读取4号模拟信号模组串口信息");
                            setDuQu(hashMap);
                        }

                    } else {
                        sendMessageToast("识别不是4号模拟信号模组，请选择正确的模组");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    sendMessageToast("识别失败");
                }
                break;
            case 0x00:
                if (result[3] == 0) {
                    if (isAllFourSetting) {
                        snBlueToothTool.write(FourSimuBlueToothUtil.getFourPinlvBytes(pinlv_str), "设置发送频率" + pinlv_str);
                    }
                } else {
                    sendMessageToast("设置采集场景失败");
                }
                break;
            case 0x03:
                if (result[3] == 0) {
                    if (isAllFourSetting) {
                        snBlueToothTool.write(FourSimuBlueToothUtil.getFouWangluoJiaoHuBytes(wangluojiaohu_str.replaceAll("最大发送", "").replaceAll("次", "")), "正在设置网络交互");
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
            case 0x0a:
                if (result[3] == 0) {
                    if (isAllFourSetting) {
                        sendMessageToast("设置对接信息成功");

                        isJiHuo = true;
                        closeJiHuo();
                    }
                } else {
                    sendMessageToast("设置对接信息失败");
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
            case 0x0d:
                isJiHuoAndXiuMian = false;
                if (result[3] == 0) {
                    sendMessageToast("休眠成功");
                } else {
                    sendMessageToast("休眠失败");
                }
                break;

            case 0x20:
                if (result[3] == 0) {
                    sendMessageToast("校准成功");
                } else if (result[3] == 1) {
                    sendMessageToast("校准失败");
                }

                break;
            case 0x21:
                if (result[3] == 0) {
                    sendMessageToast("配置成功");
                } else if (result[3] == 1) {
                    sendMessageToast("配置失败");
                } else if (result[3] == 2) {
                    sendMessageToast("未配置累计脉冲相关的传感器");
                }
                break;
            case 0x22:
                if (result[3] == 0) {
                    sendMessageToast("配置成功");
                } else if (result[3] == 1) {
                    sendMessageToast("配置失败");
                } else if (result[3] == 2) {
                    sendMessageToast("未配置线性传感相关的传感器");
                }
                break;
            case 0x23:
                if (result[3] == 0) {
                    sendMessageToast("配置成功");
                } else if (result[3] == 1) {
                    sendMessageToast("配置失败");
                } else if (result[3] == 2) {
                    sendMessageToast("未配置线性传感相关的传感器");
                }
                break;
            case 0x30:
                if (result[3] == 0) {
                    hashMap.putAll(FourSimuBlueToothUtil.parseFourLeijiNotifyBytes(result));
                    setLeiJiText(hashMap);
                } else if (result[3] == 1) {
                    sendMessageToast("读取失败");
                } else if (result[3] == 2) {
                    sendMessageToast("未配置累计脉冲相关的传感器");
                }

                break;
            case 0x31:
                if (result[3] == 0) {
                    hashMap.putAll(FourSimuBlueToothUtil.parseFourXianXingNotifyBytes(result));
                    setXianXingText(hashMap);
                } else if (result[3] == 1) {
                    sendMessageToast("读取失败");
                } else if (result[3] == 2) {
                    sendMessageToast("未配置线性相关的传感器");
                }

                break;
            case 0x32:
                if (result[3] == 0) {
                    hashMap.putAll(FourSimuBlueToothUtil.parseFourZhuangTaiNotifyBytes(result));
                    setZhuangtaiText(hashMap);
                } else if (result[3] == 1) {
                    sendMessageToast("读取失败");
                } else if (result[3] == 2) {
                    sendMessageToast("未配置状态相关的传感器");
                }
                break;
            case 0x33:
                if (result[3] == 0) {
                    //这个未写完
                    hashMap.putAll(FourSimuBlueToothUtil.parseFourGongTongNotifyBytes(result));
                    setGongTongText(hashMap);
                } else if (result[3] == 1) {
                    sendMessageToast("读取失败");
                } else if (result[3] == 2) {
                    sendMessageToast("未配置任何相关的传感器");
                }
                break;


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

        registerEventRunner(LouShanYunCode.SelectOrdernumberRunner, new SelectOrdernumberRunner());
    }

    @Override
    protected void onEventRunEnd(Event event, int code) {
        if (code == LouShanYunCode.SelectOrdernumberRunner) {
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
                if (saomiaoType == 0) {
                    result = result.substring(1);
                    textNum.setText(result);
                    pushEvent(LouShanYunCode.SelectOrdernumberRunner, result, LouShanYunCode.getBiaoShi(manufacturerSelect.getSelectedItem().toString())
                            ,LoginParamManager.getInstance().getProductRegister().getManufacturersIdentification());
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


        //一键读取后选择传感模式
    private void setMoshi(int position){
        switch (position){
            case 1://选择了累计脉冲
                arrayAdapterCPXS.clear();
                arrayAdapterCPXS.addAll(FourSimuBlueToothUtil.getAllChanPinXingShi(ChooseMoshiLeiJi));
                arrayAdapterShuZi.clear();
                arrayAdapterShuZi.addAll(FourSimuBlueToothUtil.getChuanGanXinHaoShuZi(ChooseMoshiLeiJi));
                chanpinxingshiSelect.setSelection(0);
                chuanganxinhaoSelect.setSelection(0);
                LeijiSettinglayout.setVisibility(View.VISIBLE);
                XianxingSettinglayout.setVisibility(View.GONE);
                ZhuangtaiSettinglayout.setVisibility(View.GONE);
                break;
            case 2://选择了状态传感
                arrayAdapterCPXS.clear();
                arrayAdapterCPXS.addAll(FourSimuBlueToothUtil.getAllChanPinXingShi(ChooseMoshiZhuanTai));
                arrayAdapterShuZi.clear();
                arrayAdapterShuZi.addAll(FourSimuBlueToothUtil.getChuanGanXinHaoShuZi(ChooseMoshiZhuanTai));
                chanpinxingshiSelect.setSelection(0);
                chuanganxinhaoSelect.setSelection(0);
                LeijiSettinglayout.setVisibility(View.GONE);
                XianxingSettinglayout.setVisibility(View.GONE);
                ZhuangtaiSettinglayout.setVisibility(View.VISIBLE);
                break;
            case 4://选择了线性传感
                arrayAdapterCPXS.clear();
                arrayAdapterCPXS.addAll(FourSimuBlueToothUtil.getAllChanPinXingShi(ChooseXianXing));
                arrayAdapterShuZi.clear();
                arrayAdapterShuZi.addAll(FourSimuBlueToothUtil.getChuanGanXinHaoShuZi(ChooseXianXing));
                chanpinxingshiSelect.setSelection(0);
                chuanganxinhaoSelect.setSelection(0);
                LeijiSettinglayout.setVisibility(View.GONE);
                XianxingSettinglayout.setVisibility(View.VISIBLE);
                ZhuangtaiSettinglayout.setVisibility(View.GONE);
                break;
        }

    }

    private void setDuQu(HashMap<String, String> hashMap) {
        StringBuilder sb = new StringBuilder();
        sb.append("厂家标识:  ");
        sb.append(hashMap.get(MapParams.厂家标识));
        sb.append("\n出厂时间:  ");
        sb.append("20" + hashMap.get(MapParams.出厂时间_年) + "-" + hashMap.get(MapParams.出厂时间_月) + "-" + hashMap.get(MapParams.出厂时间_日));
        sb.append("\n设备ID:  ");
        sb.append(hashMap.get(MapParams.设备ID));
        sb.append("\n采集场景:  ");
        sb.append(FourSimuBlueToothUtil.getCJCJReadStringByCode(hashMap.get(MapParams.采集场景)));
        sb.append("\n网络交互:  ");
        sb.append(hashMap.get(MapParams.网络交互));
        sb.append("\n产品形式:  ");
        sb.append(FourSimuBlueToothUtil.getChanPinXingShi(hashMap.get(MapParams.产品形式)));
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
            twoLayout.setVisibility(View.VISIBLE);
            setMoshi(Integer.valueOf(hashMap.get(MapParams.传感模式)));
        });
    }

    /**
     * 读取累计脉冲传感相关数据
     */
    private void setLeiJiText(HashMap<String, String> hashMap) {
        StringBuffer sb = new StringBuffer();
        sb.append("累计脉冲数:  ");
        sb.append(!XHStringUtil.isEmpty(hashMap.get(MapParams.累计脉冲数), false) ? hashMap.get(MapParams.累计脉冲数) : "");
        sb.append("\n累计脉冲正负标识:  ");
        sb.append(FourSimuBlueToothUtil.getLJMCZFBSReadStringByCode(hashMap.get(MapParams.累计脉冲正负标识)));
        sb.append("\n反向脉冲数:  ");
        sb.append(!XHStringUtil.isEmpty(hashMap.get(MapParams.反向脉冲数), false) ? hashMap.get(MapParams.反向脉冲数) : "");
        sb.append("\n倍率:  ");
        sb.append(FourSimuBlueToothUtil.getBLReadStringByCode(hashMap.get(MapParams.倍率)));
        sb.append("\n瞬时流量:  ");
        sb.append(!XHStringUtil.isEmpty(hashMap.get(MapParams.瞬时流量), false) ? hashMap.get(MapParams.瞬时流量) : "");
        sb.append("\n断线状态:  ");
        sb.append(FourSimuBlueToothUtil.getDXZTReadStringByCode(hashMap.get(MapParams.断线状态)));
        sb.append("\n流向状态:  ");
        sb.append(FourSimuBlueToothUtil.getLXZTReadStringByCode(hashMap.get(MapParams.流向状态)));
        sb.append("\n强磁状态:  ");
        sb.append(FourSimuBlueToothUtil.getQCZTReadStringByCode(hashMap.get(MapParams.强磁状态)));
        sb.append("\n最后一个正脉冲时间:  ");
        sb.append(!XHStringUtil.isEmpty(hashMap.get(MapParams.最后一个正脉冲时间), false) ? hashMap.get(MapParams.最后一个正脉冲时间) : "");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textLeijiInfo.setText(sb.toString());
            }
        });
    }


    /**
     * 读取线性传感相关数据
     */
    private void setXianXingText(HashMap<String, String> hashMap) {
        StringBuffer sb = new StringBuffer();
        sb.append("最高压力值:  ");
        sb.append(!XHStringUtil.isEmpty(hashMap.get(MapParams.最高压力值), false) ? hashMap.get(MapParams.最高压力值) : "");
        sb.append("\n平均压力值:  ");
        sb.append(!XHStringUtil.isEmpty(hashMap.get(MapParams.平均压力值), false) ? hashMap.get(MapParams.平均压力值) : "");
        sb.append("\n最低压力值:  ");
        sb.append(!XHStringUtil.isEmpty(hashMap.get(MapParams.最低压力值), false) ? hashMap.get(MapParams.最低压力值) : "");
        sb.append("\n采样频率:  ");
        sb.append(FourSimuBlueToothUtil.getCYPLReadStringByCode(hashMap.get(MapParams.采样频率)));
        sb.append("\n输出电压:  ");
        sb.append(!XHStringUtil.isEmpty(hashMap.get(MapParams.输出电压), false) ? hashMap.get(MapParams.输出电压) : "");
        sb.append("\n输出电压状态:  ");
        sb.append(FourSimuBlueToothUtil.getSCDYZTReadStringByCode(hashMap.get(MapParams.输出电压状态)));
        sb.append("\n断线状态:  ");
        sb.append(FourSimuBlueToothUtil.getDXZTReadStringByCode(hashMap.get(MapParams.断线状态)));
        sb.append("\n最后一次采样时间:  ");
        sb.append(!XHStringUtil.isEmpty(hashMap.get(MapParams.最后一次采样时间), false) ? hashMap.get(MapParams.最后一次采样时间) : "");
        sb.append("\nK(斜率):  ");
        sb.append(!XHStringUtil.isEmpty(hashMap.get(MapParams.K斜率), false) ? hashMap.get(MapParams.K斜率) : "");
        sb.append("\nB(截距):  ");
        sb.append(!XHStringUtil.isEmpty(hashMap.get(MapParams.B截距), false) ? hashMap.get(MapParams.B截距) : "");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textXianxingInfo.setText(sb.toString());
            }
        });
    }

    /**
     * 读取状态传感相关数据
     */
    private void setZhuangtaiText(HashMap<String, String> hashMap) {
        StringBuffer sb = new StringBuffer();
        sb.append("传感状态位:  ");
        sb.append(!XHStringUtil.isEmpty(hashMap.get(MapParams.传感状态位), false) ? hashMap.get(MapParams.传感状态位) : "");
        sb.append("\n当前状态累计时长:  ");
        sb.append(!XHStringUtil.isEmpty(hashMap.get(MapParams.当前状态累计时长), false) ? hashMap.get(MapParams.当前状态累计时长) : "");
        sb.append("\n上一状态时长:  ");
        sb.append(!XHStringUtil.isEmpty(hashMap.get(MapParams.上一状态时长), false) ? hashMap.get(MapParams.上一状态时长) : "");
        sb.append("\n判断正常和异常:  ");
        sb.append(FourSimuBlueToothUtil.getPDZCHYCReadStringByCode(hashMap.get(MapParams.判断正常和异常)));
        sb.append("\n最后一次动作切换的起始时间:  ");
        sb.append(!XHStringUtil.isEmpty(hashMap.get(MapParams.最后一次动作切换的起始时间), false) ? hashMap.get(MapParams.最后一次动作切换的起始时间) : "");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textZhuangtaiInfo.setText(sb.toString());
            }
        });
    }


    /**
     * 读取传感共有特性的相关数据
     */
    private void setGongTongText(HashMap<String, String> hashMap) {
        StringBuffer sb = new StringBuffer();
        sb.append("电源状态:  ");
        sb.append(FourSimuBlueToothUtil.getDYZTReadStringByCode(hashMap.get(MapParams.电源状态)));
        sb.append("\n电池电压:  ");
        sb.append(hashMap.get(MapParams.电池电压));
        sb.append("\n模组当前时间戳:  ");
        sb.append(hashMap.get(MapParams.模组当前时间戳));
        sb.append("\n模组当前时间:  ");
        sb.append(TimeUtils.milliseconds2String(Long.valueOf(hashMap.get(MapParams.模组当前时间戳)) * 1000));
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textGongtongInfo.setText(sb.toString());
            }
        });
    }

    /**
     * 设备休眠
     */
    private void close() {
        snBlueToothTool.write(new byte[]{(byte) 0x68, (byte) 0x01, (byte) 0x0d, (byte) 0x0e, (byte) 0x16}, "正在让设备进入休眠状态");
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
    /**
     * 设备激活
     */
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

    private void clearAllText() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //  textHuoqu.setText("");
            }
        });

    }


    private void initSnAgreement(double hardVersion) {
        sAtInstructRealizeFactory.initHardVersion(hardVersion);
    }

    /**
     * 设置第一步的传感器配置参数默认值
     *
     * @param hashMap
     */
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

    /**
     * 获取对接信息
     *
     * @return
     */
    private byte[] getDuiJieBytes() {
        byte[] result = new byte[15];
        result[0] = 0x68;
        result[1] = 0x0b;
        result[2] = 0x0a;
        result[3] = (byte) FourSimuBlueToothUtil.getCPXSIntByValue(chanpinxingshiSelect.getSelectedItem().toString());
        result[4] = 0x01;//水务
        result[5] = (byte) (0x00);//设置电源类型 物联电池 0x00
        result[6] = FourSimuBlueToothUtil.getChuanGanMoShiByte(chuanganmoshiSelect.getSelectedItem().toString());
        result[7] = FourSimuBlueToothUtil.getChuanGanXinHaoByString(chuanganxinhaoSelect.getSelectedItem().toString());
        long isn = Long.parseLong(LouShanYunUtils.getTimeID());//生成ID
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

}

