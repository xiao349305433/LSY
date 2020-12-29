package wu.loushanyun.com.moduletwoinit.v.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.elvishew.xlog.XLog;
import com.flyco.roundview.RoundLinearLayout;
import com.flyco.roundview.RoundTextView;
import com.google.gson.Gson;
import com.sensoro.sensor.kit.entity.SensoroDevice;
import com.wu.loushanyun.base.config.K;
import com.wu.loushanyun.base.util.DataParser;
import com.wu.loushanyun.base.util.LouShanYunUtils;
import com.wu.loushanyun.base.util.MapParams;
import com.wu.loushanyun.basemvp.init.LSY2InitTypeCode;
import com.wu.loushanyun.basemvp.init.SnBlueToothListener;
import com.wu.loushanyun.basemvp.init.SnBlueToothTool;
import com.wu.loushanyun.basemvp.m.sat.SAtInstructRealizeFactory;
import com.wu.loushanyun.basemvp.m.sat.util.SAtInstructParams;
import com.wu.loushanyun.basemvp.p.runner.GetChangJiaBiaoShiRunner;
import com.yanzhenjie.nohttp.Logger;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;

import org.greenrobot.eventbus.EventBus;
import org.litepal.LitePal;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableOnSubscribe;
import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.activity.BaseNoPresenterActivity;
import met.hx.com.base.base.dialog.LoadingDialogUtil;
import met.hx.com.base.base.glide.GlideUtil;
import met.hx.com.base.base.matisse.MatisseUtil;
import met.hx.com.base.base.rx.RxSchedulers;
import met.hx.com.base.baseconfig.C;
import met.hx.com.base.baseevent.Event;
import met.hx.com.librarybase.some_utils.XHStringUtil;
import wu.loushanyun.com.libraryfive.PictureUtil;
import wu.loushanyun.com.libraryfive.init.LoginFiveParamManager;
import wu.loushanyun.com.libraryfive.m.RefreshMainRepair;
import wu.loushanyun.com.libraryfive.m.RepairUpdateData;
import wu.loushanyun.com.moduletwoinit.R;
import wu.loushanyun.com.moduletwoinit.init.MInitTwoCode;
import wu.loushanyun.com.moduletwoinit.m.InsideAreaUpdate;
import wu.loushanyun.com.moduletwoinit.m.InsideSaveOnetoOneData;
import wu.loushanyun.com.moduletwoinit.m.OnetoOneConverter;
import wu.loushanyun.com.moduletwoinit.m.RefreshOnCloudEvent;
import wu.loushanyun.com.moduletwoinit.m.RefreshTwoLocationEvent;
import wu.loushanyun.com.moduletwoinit.p.runner.GetIotInfoRunner;
import wu.loushanyun.com.moduletwoinit.p.runner.InsetMeterRunner;
import wu.loushanyun.com.moduletwoinit.p.runner.UpdateMeterRunner;

import static com.wu.loushanyun.basemvp.init.SnBlueToothTool.BLUE_SEND_TIME_OUT;

@Route(path = K.LSY2InitActivity)
public class LSY2InitActivity extends BaseNoPresenterActivity implements SnBlueToothListener {
    private ScrollView scrollView;
    private TextView textDangqianSn;
    private RoundTextView bluetoothConn;
    private RoundTextView bluetoothDisconn;
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
    private Spinner secondParameterBeilvSelect;
    private TextView textSecondChangshu;
    private EditText secondParameterEditMaichongStart;
    private TextView textSecondChushizhi;
    private RoundTextView secondParameterOneSetting;
    private RoundTextView secondParameterOneReading;
    private TextView secondParameterTextInfo;
    private TextView textHuoqu;
    private RoundTextView buttonQiangzhifasong;
    private RoundLinearLayout roundTextFour;
    private Spinner caliber;
    private EditText inputMessage;
    private RoundLinearLayout roundTextFive;
    private ImageView ivSelector;
    private RoundTextView roundTextJihuoXiumianSave;


    //默认参数
    private byte chaiXie = 0;//拆卸   无
    private byte qiangCi = 0;//强磁   无
    private byte chuanGanQiZhuangTai = 0;//传感器状态   无
    private byte daoLiu = 0;//倒流   无
    private byte faMenZhuangTai = 0;//阀门状态   无
    private byte faSongPinLv = (byte) 0x30;//发送频率   默认24小时
    private boolean isAllSetting;
    private boolean isAllReading;
    private boolean isJiHuo = true;
    private boolean isJiHuoAndXiuMian = false;
    private boolean isOneReadingSecond = false;
    private boolean isConnectSuccess = true;

    private int type;
    private int writeCode;
    private boolean canPrint;
    private SAtInstructRealizeFactory sAtInstructRealizeFactory;
    private HashMap<String, String> hashMap;
    private String[] kuopinyinziArray;
    private String[] mTwoinitCaliber;

    protected SnBlueToothTool snBlueToothTool;
    private SensoroDevice sensoroDeviceChoose;
    private OnetoOneConverter onetoOneConverter;

    private int jumpType;
    private String areaNumber;
    private String oldSn;
    private String realMeterReading;

    @Override
    protected void initLifeCycle() {
        super.initLifeCycle();
        snBlueToothTool = new SnBlueToothTool(this);
        registerLifeCycle(snBlueToothTool);
    }

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        getAllData();
        ba.mTitleText = "物联网端初始化";
        ba.mActivityLayoutId = R.layout.m_twoinit_activity_lsy_2;
    }

    private void getAllData() {
        sensoroDeviceChoose = getIntent().getExtras().getParcelable("sensoroDevice");
        areaNumber = getIntent().getStringExtra("areaNumber");
        realMeterReading = getIntent().getStringExtra("realMeterReading");
        jumpType = getIntent().getIntExtra("jumpType", LSY2InitTypeCode.TypeFromCreateLocation);
        onetoOneConverter = new OnetoOneConverter();
        if (jumpType == LSY2InitTypeCode.TypeFromCreateLocation) {

        } else if (jumpType == LSY2InitTypeCode.TypeFromUpdateLocation) {
            compositeDisposable.add(Flowable.create(
                    (FlowableOnSubscribe<ArrayList<Object>>) emitter -> {
                        int loginid = LoginFiveParamManager.getInstance().getLoginData().getId();
                        List<OnetoOneConverter> arrayList = LitePal.where("sn = ? and loginid = ?", sensoroDeviceChoose.sn, loginid + "")
                                .find(OnetoOneConverter.class);
                        ArrayList<Object> arrayList1 = new ArrayList<>();
                        if (arrayList.size() != 0) {
                            arrayList1.add(arrayList.get(0));
                            emitter.onNext(arrayList1);
                            emitter.onComplete();
                        } else {
                            emitter.onComplete();
                        }
                    }
                    , BackpressureStrategy.ERROR)
                    .compose(RxSchedulers.io_main())
                    .subscribe(list -> {
                        OnetoOneConverter onetoOneConverter = (OnetoOneConverter) list.get(0);
                        setDefaultText(onetoOneConverter);
                    }));

        } else if (jumpType == LSY2InitTypeCode.TypeFromOnCloudUpdateLocation) {
            pushEventNoProgress(MInitTwoCode.GetIotInfoRunner, sensoroDeviceChoose.sn);
        } else if (jumpType == LSY2InitTypeCode.TypeFromReplace) {
            oldSn = getIntent().getStringExtra("oldSn");
        }
    }

    @Override
    protected void initView() {
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        textDangqianSn = (TextView) findViewById(R.id.text_dangqian_sn);
        bluetoothConn = (RoundTextView) findViewById(R.id.bluetooth_conn);
        bluetoothDisconn = (RoundTextView) findViewById(R.id.bluetooth_disconn);
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
        secondParameterBeilvSelect = (Spinner) findViewById(R.id.second_parameter_beilv_select);
        textSecondChangshu = (TextView) findViewById(R.id.text_second_changshu);
        secondParameterEditMaichongStart = (EditText) findViewById(R.id.second_parameter_edit_maichong_start);
        textSecondChushizhi = (TextView) findViewById(R.id.text_second_chushizhi);
        secondParameterOneSetting = (RoundTextView) findViewById(R.id.second_parameter_one_setting);
        secondParameterOneReading = (RoundTextView) findViewById(R.id.second_parameter_one_reading);
        secondParameterTextInfo = (TextView) findViewById(R.id.second_parameter_text_info);
        textHuoqu = (TextView) findViewById(R.id.text_huoqu);
        buttonQiangzhifasong = (RoundTextView) findViewById(R.id.button_qiangzhifasong);
        roundTextFour = (RoundLinearLayout) findViewById(R.id.round_text_four);
        caliber = (Spinner) findViewById(R.id.caliber);
        inputMessage = (EditText) findViewById(R.id.input_message);
        roundTextFive = (RoundLinearLayout) findViewById(R.id.round_text_five);
        ivSelector = (ImageView) findViewById(R.id.iv_selector);
        roundTextJihuoXiumianSave = (RoundTextView) findViewById(R.id.round_text_jihuo_xiumian_save);


        if (jumpType == LSY2InitTypeCode.TypeFromRead) {
            roundTextFour.setVisibility(View.GONE);
            roundTextFive.setVisibility(View.GONE);
        } else {
            roundTextFour.setVisibility(View.VISIBLE);
            roundTextFive.setVisibility(View.VISIBLE);
        }
        hashMap = new HashMap<>();
        kuopinyinziArray = getResources().getStringArray(R.array.l_loushanyun_kuopinyinzi);
        mTwoinitCaliber = getResources().getStringArray(R.array.m_twoinit_caliber);
        ivSelector.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onetoOneConverter.getImagePath() != null) {
                    ArrayList list = new ArrayList(1);
                    list.add(onetoOneConverter.getImagePath());
                    ARouter.getInstance().build(C.FullViewPictureActivity)
                            .withStringArrayList("paths", list)
                            .withBoolean("hasDelete", false)
                            .navigation(LSY2InitActivity.this);
                }
                return true;
            }
        });
        initTestClick();
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
        sAtInstructRealizeFactory = new SAtInstructRealizeFactory();

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(LSY2InitActivity.this, R.layout.l_loushanyun_item_spinner, sAtInstructRealizeFactory.getAllMoShi());
        atXindaocanshuSelect.setAdapter(arrayAdapter);
        snBlueToothTool.connectBlueTooth(sensoroDeviceChoose);
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

        secondParameterOneSetting.setOnClickListener(this::OnBlueToothClick);
        secondParameterOneReading.setOnClickListener(this::OnBlueToothClick);
        buttonQiangzhifasong.setOnClickListener(this::OnBlueToothClick);
        roundTextJihuoXiumianSave.setOnClickListener(this::OnBlueToothClick);

        systemStatus.setOnClickListener(this::OnClick);
        bluetoothConn.setOnClickListener(this::OnClick);
        bluetoothDisconn.setOnClickListener(this::OnClick);
        ivSelector.setOnClickListener(this::OnClick);
    }

    private void OnClick(View view) {
        int i = view.getId();
        if (i == R.id.system_status) {
            isJiHuoAndXiuMian = false;
            if (isJiHuo) {
                closeJiHuo();
            } else {
                openJiHuo();
            }
        } else if (i == R.id.bluetooth_conn) {
            if (textDangqianSn.getText().toString().contains("当前连接的SN设备")) {
                sendMessageToast("当前已连接设备");
                return;
            }
            snBlueToothTool.connectBlueTooth(sensoroDeviceChoose);
            clearAllText();
        } else if (i == R.id.bluetooth_disconn) {
            snBlueToothTool.disconnect();
            textDangqianSn.setText("当前未连接SN设备");
        } else if (i == R.id.iv_selector) {
            MatisseUtil.choosePicture(this, MimeType.ofImage(), 1, 99, "wu.loushanyun.com.fivemoduleapp.fileprovider");
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
        if (Double.valueOf(LouShanYunUtils.getHardWareVersion(Integer.valueOf(hashMap.get(MapParams.硬件版本)))) < 1.06) {
            sendMessageToast("识别2号模组版本小于1.06请升级固件后再试");
            return;
        }
        writeCode = 0;
        canPrint = false;
        int i = view.getId();
        if (i == R.id.at_xindaocanshu_setting) {
            try {
                snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructChannel).getTwoWriteBytes(atXindaocanshuSelect.getSelectedItem().toString().trim()), "设置信道");
            } catch (Exception e) {
                e.printStackTrace();
            }
            isAllSetting = false;

        } else if (i == R.id.at_kuopinyinzi_setting) {
            try {
                snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructSpreadingFactor).getTwoWriteBytes(atKuopinyinziSelect.getSelectedItem().toString().trim()), "设置扩频因子");
            } catch (Exception e) {
                e.printStackTrace();
            }
            isAllSetting = false;

        } else if (i == R.id.at_fasonggonglv_setting) {
            try {
                snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructSendingPower).getTwoWriteBytes(atFasonggonglvSelect.getSelectedItem().toString().trim()), "设置发送功率");
            } catch (Exception e) {
                e.printStackTrace();
            }
            isAllSetting = false;

        } else if (i == R.id.at_one_reading) {
            try {
                snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructChannel).getTwoReadBytes(), "读取信道中...");
            } catch (Exception e) {
                e.printStackTrace();
            }
            isAllReading = true;
            atTextInfo.setText("");

        } else if (i == R.id.at_one_setting) {
            try {
                snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructChannel).getTwoWriteBytes(atXindaocanshuSelect.getSelectedItem().toString().trim()), "设置信道");
            } catch (Exception e) {
                e.printStackTrace();
            }
            isAllSetting = true;

/**-------------------------------------------------------------------------------------------------------------------------------------------*/
        } else if (i == R.id.second_parameter_one_reading) {
            secondParameterTextInfo.setText("");
            isOneReadingSecond = true;
            snBlueToothTool.write(DataParser.CMD_METER_INFO, "读取参数中...", true, 2000);

        } else if (i == R.id.second_parameter_one_setting) {
            if (XHStringUtil.isEmpty(secondParameterEditMaichongStart.getText().toString(), false)) {
                sendMessageToast("请填入脉冲数");
                return;
            }
            setDefaultFactoryParams();
            secondParameterTextInfo.setText("");

/**-------------------------------------------------------------------------------------------------------------------------------------------*/
        } else if (i == R.id.round_text_jihuo_xiumian_save) {
            boolean isHigher = LouShanYunUtils.isHigherGuJian(hashMap.get(SAtInstructParams.sAtInstructFirmwareVersion));
            if (XHStringUtil.isEmpty(atTextInfo.getText().toString(), false)) {
                sendMessageToast("请配置和读取模组上送参数");
                return;
            }
            if (!isHigher) {
                sendMessageToast("LSY-IOT版本号过低，不能进行保存");
                return;
            }
            if (XHStringUtil.isEmpty(secondParameterTextInfo.getText().toString(), false)) {
                sendMessageToast("请配置和读取水表参数");
                return;
            }
            if (XHStringUtil.isEmpty(textHuoqu.getText().toString(), false)) {
                sendMessageToast("请进行强制发送测试与基站的通信是否良好");
                return;
            }
            if (XHStringUtil.isEmpty(onetoOneConverter.getImage(), true)) {
                sendMessageToast("请选择一张图片");
                return;
            }
            if (XHStringUtil.isEmpty(inputMessage.getText().toString(), false)) {
                sendMessageToast("请添加备注");
                return;
            }
            isJiHuoAndXiuMian = true;
            isJiHuo = false;
            openJiHuo();
        } else if (i == R.id.button_qiangzhifasong) {
            clearAllText();
            try {
                snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructForcedSend).getTwoReadBytes(LoginFiveParamManager.getInstance().getLoginData().getRegisterPhone())
                        , "强制发送", true, BLUE_SEND_TIME_OUT);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private void save() {
        onetoOneConverter.setSn(sensoroDeviceChoose.sn);
        onetoOneConverter.setJoinForm("0".equals(hashMap.get(MapParams.信号类型)) ? "1" : "2");
        onetoOneConverter.setFrequency(LouShanYunUtils.getFSPLReadStringByCode(Long.parseLong(hashMap.get(MapParams.发送频率))).replaceAll("上传一次", ""));
        onetoOneConverter.setHardwareVersion(LouShanYunUtils.getHardWareVersion(Integer.valueOf(hashMap.get(MapParams.硬件版本))));
        onetoOneConverter.setSoftVersion(LouShanYunUtils.getSoftWareVersion(Integer.valueOf(hashMap.get(MapParams.软件版本))));
        onetoOneConverter.setFirmwareVersion(hashMap.get(SAtInstructParams.sAtInstructFirmwareVersion));
        onetoOneConverter.setSensingSignal(hashMap.get(MapParams.传感信号));
        onetoOneConverter.setPowerState(hashMap.get(MapParams.表电池状态));
        StringBuilder sb = new StringBuilder();
        sb.append("20");
        if (hashMap.get(MapParams.出厂时间_年).length() == 1) {
            sb.append(0);
        }
        sb.append(hashMap.get(MapParams.出厂时间_年));
        sb.append("-");
        if (hashMap.get(MapParams.出厂时间_月).length() == 1) {
            sb.append(0);
        }
        sb.append(hashMap.get(MapParams.出厂时间_月));
        sb.append("-");
        if (hashMap.get(MapParams.出厂时间_日).length() == 1) {
            sb.append(0);
        }
        sb.append(hashMap.get(MapParams.出厂时间_日));
        onetoOneConverter.setEquipmentTime(sb.toString());
        onetoOneConverter.setRemark(String.valueOf(inputMessage.getText()));
        onetoOneConverter.setUserId(hashMap.get(MapParams.设备ID));
        onetoOneConverter.setFlowDirection(hashMap.get(MapParams.表流向状态));
        onetoOneConverter.setDisassemblyState(hashMap.get(MapParams.表拆卸状态));
        onetoOneConverter.setMagneticDisturb(hashMap.get(MapParams.表强磁状态));
        onetoOneConverter.setPowerState(hashMap.get(MapParams.表电池状态));
        onetoOneConverter.setLoginId(LoginFiveParamManager.getInstance().getLoginData().getId());
        onetoOneConverter.setManufacturersIdentification(hashMap.get(MapParams.厂家标识));
        onetoOneConverter.setPulseConstant(new BigDecimal(1)
                .divide(new BigDecimal(LouShanYunUtils.getBLReadStringByCode(hashMap.get(MapParams.倍率))))
                .stripTrailingZeros().toPlainString());//倒数
        onetoOneConverter.setImpulseInitial(hashMap.get(MapParams.当前脉冲读数));
        onetoOneConverter.setAreaNumber(areaNumber);
        onetoOneConverter.setSf(hashMap.get(SAtInstructParams.sAtInstructSpreadingFactor));//扩频因子
        onetoOneConverter.setChannel(hashMap.get(SAtInstructParams.sAtInstructChannel));//信道参数
        onetoOneConverter.setCaliber(String.valueOf(caliber.getSelectedItem()).replaceAll("\\D+", ""));
        onetoOneConverter.setMeterType(String.valueOf(caliber.getSelectedItem()));
        String[] strings = hashMap.get(SAtInstructParams.sAtInstructRSSI).split(",");
        int rssi = Integer.valueOf(strings[0]);
        int snr = Integer.valueOf(strings[1]);
        onetoOneConverter.setRssi(rssi + "");
        onetoOneConverter.setSnr(snr + "");
        onetoOneConverter.setSendingPower(hashMap.get(SAtInstructParams.sAtInstructSendingPower));//发送功率
        if (jumpType == LSY2InitTypeCode.TypeFromOnCloudUpdateLocation) {
            String areaNumber = getIntent().getExtras().getString("areaNumber");//区域号
            String areaName = getIntent().getExtras().getString("areaName");//区域名称
            String imageBytes = onetoOneConverter.getImage();//图片Bytes
            String businessLicense = LoginFiveParamManager.getInstance().getLoginData().getTradeRegister().getBusinessLicense();//企业营业执照号码
            String sn = onetoOneConverter.getSn();
            String pulseConstant = onetoOneConverter.getPulseConstant();//脉冲常数
            String impulseInitial = onetoOneConverter.getImpulseInitial();//初始脉冲
            String channel = onetoOneConverter.getChannel();//信道参数
            String sf = onetoOneConverter.getSf();//扩频因子
            String remark = String.valueOf(inputMessage.getText());//备注
            String $caliber = String.valueOf(caliber.getSelectedItem()).replaceAll("\\D+", "");//口径
            String loginId = String.valueOf(LoginFiveParamManager.getInstance().getLoginData().getId());//登录用户Id
            InsideAreaUpdate insideAreaUpdate = new InsideAreaUpdate();
            insideAreaUpdate.setAreaNumber(areaNumber);
            insideAreaUpdate.setAreaName(areaName);
            insideAreaUpdate.setMeterId(onetoOneConverter.getUserId());
            ArrayList<String> stringArrayList = new ArrayList<>();
            stringArrayList.add(imageBytes);
            insideAreaUpdate.setImageBytes(stringArrayList);
            insideAreaUpdate.setBusinessLicense(businessLicense);
            insideAreaUpdate.setSn(sn);
            insideAreaUpdate.setSf(sf);
            insideAreaUpdate.setPulseConstant(pulseConstant);
            insideAreaUpdate.setImpulseInitial(impulseInitial);
            insideAreaUpdate.setChannel(channel);
            insideAreaUpdate.setRemark(remark);
            insideAreaUpdate.setCaliber($caliber);
            insideAreaUpdate.setLoginId(loginId);
            pushEvent(MInitTwoCode.UpdateMeterRunner, new Gson().toJson(insideAreaUpdate));
        } else if (jumpType == LSY2InitTypeCode.TypeFromOnCloudInsideLocation) {
            InsideSaveOnetoOneData data = new InsideSaveOnetoOneData();
            List<OnetoOneConverter> onetoOneConverterList = new ArrayList<>();
            onetoOneConverterList.add(onetoOneConverter);
            data.setConverter(onetoOneConverterList);
            if (areaNumber != null) {
                data.setAreaNumber(areaNumber);
            }
            data.setBusinessLicense(LoginFiveParamManager.getInstance().getLoginData().getTradeRegister().getBusinessLicense());
            data.setLoginId(LoginFiveParamManager.getInstance().getLoginData().getId());
            pushEvent(MInitTwoCode.InsetMeterRunner, new Gson().toJson(data));
        } else if (jumpType == LSY2InitTypeCode.TypeFromCreateLocation || jumpType == LSY2InitTypeCode.TypeFromUpdateLocation) {
            compositeDisposable.add(Flowable.create(
                    (FlowableOnSubscribe<Boolean>) emitter ->
                    {
                        LoadingDialogUtil.showByEvent("正在保存", this.getClass().getName());
                        int loginid = LoginFiveParamManager.getInstance().getLoginData().getId();
                        List<OnetoOneConverter> arrayList = LitePal.where("sn = ? and loginid = ?", onetoOneConverter.getSn(), loginid + "")
                                .find(OnetoOneConverter.class);
                        long baseId = 0;
                        if (arrayList.size() > 0) {
                            baseId = arrayList.get(0).getBaseObjId();
                        }
                        if (baseId == 0) {
                            Logger.i("只保存=" + baseId);
                            onetoOneConverter.assignBaseObjId(0);
                            onetoOneConverter.saveAsync().listen(success -> {
                                if (success) {
                                    emitter.onNext(true);
                                } else {
                                    emitter.onNext(false);
                                }

                            });
                        } else {
                            Logger.i("删除然后保存=" + baseId);
                            LitePal.delete(OnetoOneConverter.class, baseId);
                            onetoOneConverter.assignBaseObjId(0);
                            onetoOneConverter.saveAsync().listen(success -> {
                                if (success) {
                                    emitter.onNext(true);
                                } else {
                                    emitter.onNext(false);
                                }
                            });
                        }
                        LoadingDialogUtil.dismissByEvent(this.getClass().getName());
                    }, BackpressureStrategy.ERROR)
                    .compose(RxSchedulers.io_main())
                    .subscribe(success -> {
                        if (success) {
                            sendMessageToast("保存成功");
                            close();
                            EventBus.getDefault().post(new RefreshTwoLocationEvent());
                        } else {
                            sendMessageToast("保存失败");
                        }
                    }));
        } else if (jumpType == LSY2InitTypeCode.TypeFromReplace) {
            onetoOneConverter.setRealMeterReading(realMeterReading);
            InsideSaveOnetoOneData data = new InsideSaveOnetoOneData();
            List<OnetoOneConverter> onetoOneConverterList = new ArrayList<>();
            onetoOneConverterList.add(onetoOneConverter);
            data.setConverter(onetoOneConverterList);
            data.setBusinessLicense(LoginFiveParamManager.getInstance().getLoginData().getTradeRegister().getBusinessLicense());
            data.setLoginId(LoginFiveParamManager.getInstance().getLoginData().getId());
            compositeDisposable.add(Flowable.create(
                    (FlowableOnSubscribe<Boolean>) emitter ->
                    {
                        LoadingDialogUtil.showByEvent("正在保存", this.getClass().getName());
                        long id = 0;
                        int loginid = LoginFiveParamManager.getInstance().getLoginData().getId();
                        RepairUpdateData repairUpdateData = new RepairUpdateData(sensoroDeviceChoose.getSerialNumber(), loginid + "", oldSn, "远传物联网端");
                        repairUpdateData.setWuLianWangDataJson(new Gson().toJson(data));
                        List<RepairUpdateData> arrayList = LitePal.where("oldSn = ? and loginid = ?", oldSn, loginid + "")
                                .find(RepairUpdateData.class);
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
                            EventBus.getDefault().post(new RefreshMainRepair());
                            close();
                            sendMessageToast("保存成功，请到网络好的地方同步");
                        } else {
                            sendMessageToast("保存失败");
                        }
                    }));
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
                if (jumpType == LSY2InitTypeCode.TypeFromRead) {
                    isConnectSuccess = false;
                } else {
                    isConnectSuccess = true;
                }
                textDangqianSn.setText("当前连接的SN设备:" + sensoroDeviceChoose.getSerialNumber().toUpperCase());
                try {
                    snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sInstructDiscern).getTwoWriteBytes(), "识别模块中...", true, 6000);
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
                        int rssiStart = Integer.valueOf("-90");
                        int rssiEnd = Integer.valueOf("127");
                        int snrStart = Integer.valueOf("0");
                        int snrEnd = Integer.valueOf("127");
                        String status;
                        if (rssi > rssiStart && rssi < rssiEnd && snr > snrStart && snr < snrEnd) {
                            status = "合格";
                        } else {
                            status = "不合格";
                        }
                        runOnUiThread(() -> {
                            String sb = "信号强度：" + rssi + "\n" + "信噪比：" + snr + "\n" + "状态：" + status;
                            textHuoqu.setText(sb);
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (resultAt.equals(SAtInstructParams.sAtInstructSpreadingFactor + "-R")) {
                    try {
                        hashMap.putAll(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructSpreadingFactor).parseTwoRNotifyBytes(result));
                        if (isAllReading) {
                            snBlueToothTool.write(DataParser.CMD_SYSTEM_STATUS, "正在读取设备激活状态");
                            setAtDuShu(hashMap);
                        }
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
                        snBlueToothTool.write(DataParser.CMD_METER_INFO, "读取参数中...", true, 2000);
                        isOneReadingSecond = false;
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
                    setDuQu(hashMap, null);
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
                                save();
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
        snBlueToothTool.write(DataParser.getFactoryParams(
                LouShanYunUtils.getCGXHReadStringByCode(Long.valueOf(hashMap.get(MapParams.传感信号))),
                chaiXie,
                qiangCi,
                chuanGanQiZhuangTai,
                daoLiu,
                faMenZhuangTai,
                faSongPinLv,
                Integer.valueOf(hashMap.get(MapParams.厂家标识)),
                "0", "0"
        ), "设置1参数中...");
    }

    private void close() {
        snBlueToothTool.write(new byte[]{(byte) 0x68, (byte) 0x02, (byte) 0x25, (byte) 0x00, (byte) 0x27, (byte) 0x16}, "正在让设备进入休眠状态", true, 2000);
    }

    private void setDefaultParams() {
        String id = hashMap.get(MapParams.设备ID);
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
        sb.append("\n表流向状态:  ");
        sb.append(hashMap.get(MapParams.表流向状态).equals("0") ? "正流" : "倒流");
        sb.append("\n表电池状态:  ");
        sb.append(hashMap.get(MapParams.表电池状态).equals("0") ? "正常" : "欠压");
        runOnUiThread(() -> {
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
            secondParameterEditMaichongStart.setText(hashMap.get(MapParams.当前脉冲读数));
            secondParameterTextInfo.setText(sb.toString());
            if (XHStringUtil.isEmpty(manufacturersIdentification, false)) {
                pushEventNoProgress(MInitTwoCode.GetChangJiaBiaoShiRunner, hashMap.get(MapParams.厂家标识));
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
        registerEventRunner(MInitTwoCode.GetChangJiaBiaoShiRunner, new GetChangJiaBiaoShiRunner());
        registerEventRunner(MInitTwoCode.UpdateMeterRunner, new UpdateMeterRunner());
        registerEventRunner(MInitTwoCode.GetIotInfoRunner, new GetIotInfoRunner());
        registerEventRunner(MInitTwoCode.InsetMeterRunner, new InsetMeterRunner());
    }

    @Override
    protected void onEventRunEnd(Event event, int code) {
        if (code == MInitTwoCode.GetChangJiaBiaoShiRunner) {
            if (event.isSuccess()) {
                String name = (String) event.getReturnParamAtIndex(0);
                if (XHStringUtil.isEmpty(name, true)) {
                    return;
                }
                onetoOneConverter.setFactoryName(name);
                setDuQu(hashMap, name);
            }
        } else if (code == MInitTwoCode.UpdateMeterRunner) {
            if (event.isSuccess()) {
                int codeReturn = (int) event.getReturnParamAtIndex(0);
                String msg = (String) event.getReturnParamAtIndex(1);
                if (codeReturn == 0) {
                    sendMessageToast("上传同步成功");
                    close();
                    EventBus.getDefault().post(new RefreshOnCloudEvent());
                } else {
                    sendMessageToast(msg, true);
                }
            }
        } else if (code == MInitTwoCode.InsetMeterRunner) {
            if (event.isSuccess()) {
                int codeReturn = (int) event.getReturnParamAtIndex(0);
                String msg = (String) event.getReturnParamAtIndex(1);
                sendMessageToast(msg, true);
                switch (codeReturn) {
                    case 0:
                        close();
                        EventBus.getDefault().post(new RefreshOnCloudEvent());
                        break;
                }
            }

        } else if (code == MInitTwoCode.GetIotInfoRunner) {
            if (event.isSuccess()) {
                OnetoOneConverter onetoOneConverter = (OnetoOneConverter) event.getReturnParamAtIndex(1);
                if (onetoOneConverter != null) {
                    setDefaultText(onetoOneConverter);
                }
            }
        }
    }

    private void setDefaultText(OnetoOneConverter converter) {
        if (converter != null) {
            inputMessage.setText(converter.getRemark());
            onetoOneConverter.setRemark(converter.getRemark());
            if (!XHStringUtil.isEmpty(converter.getCaliber() + "MM", false)) {
                for (int i = 0; i < mTwoinitCaliber.length; i++) {
                    if (mTwoinitCaliber[i].equals(converter.getCaliber() + "MM")) {
                        caliber.setSelection(i);
                    }
                }
            }
            onetoOneConverter.setCaliber(converter.getCaliber());
            GlideUtil.display(this, converter.getImagePath(), R.drawable.m_twoinit_zx, new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    GlideUtil.display(LSY2InitActivity.this, ivSelector, converter.getImagePath());
                    converter.setImage(PictureUtil.bitmapToString(resource));
                    onetoOneConverter.setImagePath(converter.getImagePath());
                    onetoOneConverter.setImage(converter.getImage());
                }
            });

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 99) {
                String imagePath = Matisse.obtainPathResult(intent).get(0);
                GlideUtil.display(this, ivSelector, imagePath);
                String data = PictureUtil.bitmapToString(imagePath);
                onetoOneConverter.setImage(data);
                onetoOneConverter.setImagePath(imagePath);
            }
        }
    }
}
