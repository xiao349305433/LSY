package wu.loushanyun.com.modulerepair.v.activity;

import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.elvishew.xlog.XLog;
import com.flyco.roundview.RoundTextView;
import com.qrcore.util.QRScannerHelper;
import com.sensoro.sensor.kit.callback.SensoroDeviceListener;
import com.sensoro.sensor.kit.entity.SensoroDevice;
import com.wu.loushanyun.base.config.K;
import com.wu.loushanyun.base.util.DataParser;
import com.wu.loushanyun.base.util.LouShanYunUtils;
import com.wu.loushanyun.base.util.MapParams;
import com.wu.loushanyun.basemvp.init.SensoroBlueManager;
import com.wu.loushanyun.basemvp.m.SaveDataMeter;
import com.wu.loushanyun.basemvp.m.sat.SAtInstructRealizeFactory;
import com.wu.loushanyun.basemvp.m.sat.util.SAtInstructParams;
import com.wu.loushanyun.basemvp.p.adapter.MeterViewBinder;
import com.wu.loushanyun.basemvp.v.activity.BaseBlueToothActivity;

import org.litepal.LitePal;

import java.util.ArrayList;
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
import met.hx.com.librarybase.some_utils.ToastUtils;
import met.hx.com.librarybase.some_utils.XHStringUtil;
import met.hx.com.librarybase.views.dialog.MDDialog;
import wu.loushanyun.com.libraryfive.init.LoginFiveParamManager;
import wu.loushanyun.com.libraryfive.p.adapter.SNDeviceViewBinder;
import wu.loushanyun.com.modulerepair.R;
import wu.loushanyun.com.modulerepair.init.MRepairCode;
import wu.loushanyun.com.modulerepair.m.InsideModifyThirdMeterInfo;
import wu.loushanyun.com.modulerepair.m.RepairDanYuanData;
import wu.loushanyun.com.modulerepair.p.adapter.RepairDanYuanViewBinder;
import wu.loushanyun.com.modulerepair.p.runner.RepairDanYuanRunner;

@Route(path = K.ReplaceThreeActivity)
public class ReplaceThreeActivity extends BaseBlueToothActivity implements RepairDanYuanViewBinder.OnRepairDanYuanListener {
    private EditText editSn;
    private RoundTextView buttonChooseSn;
    private RoundTextView buttonSaomiaoSn;
    private RoundTextView buttonHuoqu;
    private EditText editBiaohao;
    private RoundTextView buttonChooseBiaohao;
    private EditText editOldId;
    private EditText editOldDushu;
    private RoundTextView buttonChushihua;
    private EditText editNewNum;
    private EditText editNewId;
    private TextView textDangqianSn;
    private RoundTextView buttonConnect;
    private RoundTextView buttonDisconnect;
    private RoundTextView buttonDuqu;
    private RoundTextView buttonLouchao;
    private RecyclerView resultDuquList;
    private RoundTextView buttonBaocun;

    private MDDialog mdDialogBlueList;
    private RecyclerView dialogBlueRecycle;
    private ArrayList<SensoroDevice> deviceArrayList;

    private QRScannerHelper mScannerHelper;

    private MDDialog mdDialogRepairMeter;
    private TextView textTitle;
    private RecyclerView recyclerview;
    private ArrayList<RepairDanYuanData.DataBean> dataBeanArrayList;
    private MultiTypeAdapter multiTypeAdapterRepair;
    private static final int RESULT_YANBIAO = 1000;
    private static final int RESULT_CHUSHIHUA = 1001;
    private int position;
    private boolean isScanner;
    private boolean canSave;
    private String oldSn;

    private MeterViewBinder meterViewBinder;
    private MultiTypeAdapter meterMultiTypeAdapter;
    private ArrayList<SaveDataMeter> saveDataMeterArrayList;

    private String sendingPower;//发送功率
    private String sf;//扩频
    private String snr;//信噪比
    private String rssi;//信号强度
    private String channel;//信道
    private double softVersion;
    private SAtInstructRealizeFactory sAtInstructRealizeFactory;
    private MultiTypeAdapter multiTypeAdapterBlue;

    @Override
    protected void initEventListener() {
        registerEventRunner(MRepairCode.RepairDanYuanRunner, new RepairDanYuanRunner());

    }

    @Override
    protected void onEventRunEnd(Event event, int code) {
        if (code == MRepairCode.RepairDanYuanRunner) {
            if (event.isSuccess()) {
                dataBeanArrayList.clear();
                RepairDanYuanData repairDanYuanData = (RepairDanYuanData) event.getReturnParamAtIndex(0);
                dataBeanArrayList.addAll(repairDanYuanData.getData());
                if (dataBeanArrayList.size() <= 0) {
                    ToastUtils.showShort("该设备下无表单元可换");
                } else {
                    mdDialogRepairMeter.show();
                    multiTypeAdapterRepair.notifyDataSetChanged();
                }

            }
        }

    }

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        ba.mActivityLayoutId = R.layout.m_repair_activity_replace;
        ba.mTitleText = "3号表单元更换";
        ba.mTitleRightText = "附近设备";
        ba.mTitleLeftRightWidth = 100;
    }

    @Override
    public void onRightClick(View item) {
        editSn.setText("");
        mdDialogBlueList.show();
    }

    @Override
    protected void initView() {
        super.initView();
        editSn = (EditText) findViewById(R.id.edit_sn);
        buttonChooseSn = (RoundTextView) findViewById(R.id.button_choose_sn);
        buttonSaomiaoSn = (RoundTextView) findViewById(R.id.button_saomiao_sn);
        buttonHuoqu = (RoundTextView) findViewById(R.id.button_huoqu);
        editBiaohao = (EditText) findViewById(R.id.edit_biaohao);
        buttonChooseBiaohao = (RoundTextView) findViewById(R.id.button_choose_biaohao);
        editOldId = (EditText) findViewById(R.id.edit_old_id);
        editOldDushu = (EditText) findViewById(R.id.edit_old_dushu);
        buttonChushihua = (RoundTextView) findViewById(R.id.button_chushihua);
        editNewNum = (EditText) findViewById(R.id.edit_new_num);
        editNewId = (EditText) findViewById(R.id.edit_new_id);
        textDangqianSn = (TextView) findViewById(R.id.text_dangqian_sn);
        buttonConnect = (RoundTextView) findViewById(R.id.button_connect);
        buttonDisconnect = (RoundTextView) findViewById(R.id.button_disconnect);
        buttonDuqu = (RoundTextView) findViewById(R.id.button_duqu);
        buttonLouchao = (RoundTextView) findViewById(R.id.button_louchao);
        resultDuquList = (RecyclerView) findViewById(R.id.result_duqu_list);
        buttonBaocun = (RoundTextView) findViewById(R.id.button_baocun);


        initBlueList();
        initRepairDanYuanDialog();
        meterViewBinder = new MeterViewBinder(this);
        meterMultiTypeAdapter = new MultiTypeAdapter();
        saveDataMeterArrayList = new ArrayList<>();
        meterMultiTypeAdapter.register(SaveDataMeter.class, meterViewBinder);
        meterMultiTypeAdapter.setItems(saveDataMeterArrayList);
        resultDuquList.setAdapter(meterMultiTypeAdapter);
        resultDuquList.setNestedScrollingEnabled(false);
        sAtInstructRealizeFactory = new SAtInstructRealizeFactory();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.l_loushanyun_item_spinner, sAtInstructRealizeFactory.getAllMoShi());

        mScannerHelper = new QRScannerHelper(this);
        mScannerHelper.setCallBack(result -> {
            if (!XHStringUtil.isEmpty(result, false)) {
                if (result.contains("TYPE:")) {
                    result = result.substring(result.indexOf("/SN:")).replace("/SN:", "");
                }
                editSn.setText(result.toUpperCase());
                setDialogTitleText();
                pushEvent(MRepairCode.RepairDanYuanRunner, result);
            }
        });
        buttonSaomiaoSn.setOnClickListener(v -> {
            isScanner = true;
            mScannerHelper.startScanner();
        });
        buttonConnect.setOnClickListener(this::OnClick);
        buttonDisconnect.setOnClickListener(this::OnClick);
        buttonBaocun.setOnClickListener(this::OnClick);
        buttonChooseSn.setOnClickListener(this::OnClick);
        buttonHuoqu.setOnClickListener(this::OnClick);
        buttonChooseBiaohao.setOnClickListener(this::OnClick);
        buttonChushihua.setOnClickListener(this::OnClick);

        buttonDuqu.setOnClickListener(this::onBlueClick);
        buttonLouchao.setOnClickListener(this::onBlueClick);
    }

    private void initSnAgreement(double hardVersion) {
        sAtInstructRealizeFactory.initHardVersion(hardVersion);
    }

    private void onBlueClick(View view) {
        if (XHStringUtil.isEmpty(oldSn, false)) {
            sendMessageToast("蓝牙未连接");
            return;
        }
        if (view.getId() == R.id.button_louchao) {
            readMeter();
        } else if (view.getId() == R.id.button_duqu) {
            write(DataParser.CMD_RSSI_SNR, "读取信号强度");
        }
    }

    private void OnClick(View view) {
        if (view.getId() == R.id.button_connect) {
            String newSN = editSn.getText().toString();
            if (XHStringUtil.isEmpty(newSN, false)) {
                ToastUtils.showShort("没有选择sn设备");
                return;
            }
            String newId = editNewId.getText().toString();
            if (XHStringUtil.isEmpty(newId, false)) {
                ToastUtils.showShort("没有选择新设备");
                return;
            }
            if (dataBeanArrayList.get(position).getMeterId().equals(newId)) {
                ToastUtils.showShort("新旧设备id相同,无法替换");
                return;
            }
            if (sensoroDevice != null) {
                connectBlueTooth(sensoroDevice);
            } else {
                if (!XHStringUtil.isEmpty(newSN, false)) {
                    SensoroDevice sensoroDevice = null;
                    for (int i = 0; i < deviceArrayList.size(); i++) {
                        if (newSN.equalsIgnoreCase(deviceArrayList.get(i).getSerialNumber())) {
                            sensoroDevice = deviceArrayList.get(i);
                        }
                    }
                    if (sensoroDevice != null) {
                        ReplaceThreeActivity.this.sensoroDevice = sensoroDevice;
                        connectBlueTooth(sensoroDevice);
                    } else {
                        ToastUtils.showShort("附近未搜索到该蓝牙，请确认设备是否打开");
                    }
                }
            }
        } else if (view.getId() == R.id.button_disconnect) {
            oldSn = "";
            textDangqianSn.setText("当前未连接SN设备");
        } else if (view.getId() == R.id.button_chushihua) {
            if (XHStringUtil.isEmpty(editOldDushu.getText().toString(), false)) {
                sendMessageToast("请填写旧机械表读数");
                return;
            }
            isScanner = false;
            if (dataBeanArrayList.size() > 0) {
                ARouter.getInstance().build(K.ThirdTestNewActivity).withInt("biaoHao", dataBeanArrayList.get(position).getMeterNumber())
                        .withInt("fromType", 1).navigation(this, RESULT_CHUSHIHUA);
            } else {
                ToastUtils.showShort("请选择需要替换的旧设备");
            }
        } else if (view.getId() == R.id.button_choose_biaohao) {
            if (dataBeanArrayList.size() <= 0) {
                ToastUtils.showShort("该设备下无表单元可换");
            } else {
                mdDialogRepairMeter.show();
            }
        } else if (view.getId() == R.id.button_huoqu) {
            String sn = editSn.getText().toString();
            if (XHStringUtil.isEmpty(sn, false)) {
                ToastUtils.showShort("请选择SN设备");
                return;
            }
            setDialogTitleText();
            pushEvent(MRepairCode.RepairDanYuanRunner, sn);
        } else if (view.getId() == R.id.button_choose_sn) {
            editSn.setText("");
            SensoroBlueManager.searchBlueTooth(sensoroDeviceListener);
            mdDialogBlueList.show();
        } else if (view.getId() == R.id.button_baocun) {
            if (!canSave) {
                ToastUtils.showShort("新设备未被集中器抄到数据，无法保存");
                return;
            }
            SaveDataMeter saveDataMeter = saveDataMeterArrayList.get(0);
            String newId = editNewId.getText().toString();
            String userId = saveDataMeter.getUserId();
            if (!userId.equals(newId)) {
                sendMessageToast("请把新表接到集中器之后再抄表");
                return;
            }
            saveUpdateData(saveDataMeterArrayList.get(0));
        }

    }


    /**
     * 保存更换的数据到本地
     */
    private void saveUpdateData(SaveDataMeter saveDataMeter) {
        compositeDisposable.add(Flowable.create(
                (FlowableOnSubscribe<Boolean>) emitter ->
                {
                    LoadingDialogUtil.showByEvent("正在保存", this.getClass().getName());
                    long id = 0;
                    int loginid = LoginFiveParamManager.getInstance().getLoginData().getId();
                    InsideModifyThirdMeterInfo insideModifyThirdMeterInfo = new InsideModifyThirdMeterInfo();
                    insideModifyThirdMeterInfo.setNewMeterId(saveDataMeter.getUserId());
                    insideModifyThirdMeterInfo.setDisassemblyState(saveDataMeter.getDisassemblyState());
                    insideModifyThirdMeterInfo.setFlowDirection(saveDataMeter.getFlowDirection());
                    insideModifyThirdMeterInfo.setImpulseInitial(saveDataMeter.getImpulseInitial());
                    insideModifyThirdMeterInfo.setLoginId(loginid + "");
                    insideModifyThirdMeterInfo.setMagneticDisturb(saveDataMeter.getMagneticDisturb());
                    insideModifyThirdMeterInfo.setMeterNumber(saveDataMeter.getMeterNumber());
                    insideModifyThirdMeterInfo.setPowerState(saveDataMeter.getPowerState());
                    insideModifyThirdMeterInfo.setPulseConstant(saveDataMeter.getPulseConstant());
                    insideModifyThirdMeterInfo.setReadMeterState(saveDataMeter.getReadMeterState());
                    insideModifyThirdMeterInfo.setSensorState(saveDataMeter.getSensorState());
                    insideModifyThirdMeterInfo.setOldMeterId(dataBeanArrayList.get(position).getMeterId());
                    insideModifyThirdMeterInfo.setSn(oldSn);
                    insideModifyThirdMeterInfo.setSendingPower(sendingPower);
                    insideModifyThirdMeterInfo.setSf(sf);
                    insideModifyThirdMeterInfo.setSnr(snr);
                    insideModifyThirdMeterInfo.setRealMeterReading(editOldDushu.getText().toString());
                    insideModifyThirdMeterInfo.setRssi(rssi);
                    insideModifyThirdMeterInfo.setChannel(channel);
                    List<InsideModifyThirdMeterInfo> arrayList =
                            LitePal.where("sn = ? and loginid = ? and meterNumber=?", oldSn, loginid + "", saveDataMeter.getMeterNumber())
                                    .find(InsideModifyThirdMeterInfo.class);
                    XLog.i("数据==" + arrayList.toString());
                    XLog.i("s数据==" + insideModifyThirdMeterInfo.toString());
                    if (arrayList.size() > 0) {
                        id = arrayList.get(0).getBaseObjId();
                    }
                    if (id == 0) {
                        insideModifyThirdMeterInfo.saveAsync().listen(success -> {
                            if (success) {
                                emitter.onNext(true);
                            } else {
                                emitter.onNext(false);
                            }
                        });
                    } else {
                        LitePal.delete(InsideModifyThirdMeterInfo.class, id);
                        insideModifyThirdMeterInfo.saveAsync().listen(success -> {
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
                        sendMessageToast(saveDataMeter.getMeterNumber() + "号表保存成功，请到网络好的地方同步");
                        dataBeanArrayList.get(position).setDeviceBaocunZhuangtai(true);
                        multiTypeAdapterRepair.notifyDataSetChanged();
                        clearAllData();
                    } else {
                        sendMessageToast("保存失败");
                    }
                }));
    }

    private void clearAllData() {
        editNewNum.setText("");
        editNewId.setText("");
        editBiaohao.setText("");
        oldSn = "";
    }

    private void initRepairDanYuanDialog() {
        View view = getLayoutInflater().inflate(R.layout.m_repair_dialog_old_meter, null);
        mdDialogRepairMeter = new MDDialog.Builder(this).setShowButtons(false)
                .setShowTitle(false)
                .setContentView(view)
                .create();
        textTitle = (TextView) view.findViewById(R.id.text_title);
        recyclerview = (RecyclerView) view.findViewById(R.id.recyclerview);
        multiTypeAdapterRepair = new MultiTypeAdapter();
        dataBeanArrayList = new ArrayList<>();
        multiTypeAdapterRepair.register(RepairDanYuanData.DataBean.class, new RepairDanYuanViewBinder(this, this));
        multiTypeAdapterRepair.setItems(dataBeanArrayList);
        recyclerview.setAdapter(multiTypeAdapterRepair);
    }

    private SensoroDeviceListener sensoroDeviceListener=new SensoroDeviceListener<SensoroDevice>() {
        @Override
        public void onNewDevice(SensoroDevice sensoroDevice) {
            XLog.i("蓝牙onNewDevice2231231" + sensoroDevice.toString());
            if (!deviceArrayList.contains(sensoroDevice)) {
                deviceArrayList.add(sensoroDevice);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        multiTypeAdapterBlue.notifyDataSetChanged();
                    }
                });
            } else {
                XLog.i("蓝牙sdaasasdasdasdasd" + sensoroDevice.toString());
            }

        }

        @Override
        public void onGoneDevice(SensoroDevice sensoroDevice) {
            XLog.i("蓝牙onGoneDevice1111" + sensoroDevice.toString());
        }

        @Override
        public void onUpdateDevices(ArrayList<SensoroDevice> arrayList) {
            XLog.i("蓝牙onUpdateDevices1111122" + arrayList.toString());
            for (int i = 0; i < arrayList.size(); i++) {
                SensoroDevice sensoroDevice = arrayList.get(i);
                if (deviceArrayList.contains(sensoroDevice)) {
                    deviceArrayList.set(deviceArrayList.indexOf(sensoroDevice), sensoroDevice);
                } else {
                    deviceArrayList.add(sensoroDevice);
                }
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    multiTypeAdapterBlue.notifyDataSetChanged();
                }
            });
        }
    };

    private void initBlueList() {
        View view = getLayoutInflater().inflate(R.layout.m_repair_main_dialog_blue_list, null);
        mdDialogBlueList = new MDDialog.Builder(this).setShowButtons(false)
                .setShowTitle(false)
                .setContentView(view)
                .create();
        dialogBlueRecycle = (RecyclerView) view.findViewById(R.id.dialog_blue_recycle);
        multiTypeAdapterBlue = new MultiTypeAdapter();
        deviceArrayList = new ArrayList<>();
        multiTypeAdapterBlue.register(SensoroDevice.class, new SNDeviceViewBinder(new SNDeviceViewBinder.OnSensoroDeviceListener() {
            @Override
            public void onSensoroDevice(SensoroDevice sensoroDevice) {
                mdDialogBlueList.dismiss();
                editSn.setText(sensoroDevice.getSerialNumber());
                setDialogTitleText();
                pushEvent(MRepairCode.RepairDanYuanRunner, sensoroDevice.getSerialNumber());
            }
        }));
        multiTypeAdapterBlue.setItems(deviceArrayList);
        dialogBlueRecycle.setAdapter(multiTypeAdapterBlue);

    }

    private void setDialogTitleText() {
        textTitle.setText("SN:" + editSn.getText().toString() + "下所有未上送过数据的单元如下：\n" +
                "请通过验表功能来获取可更换状态！！\n验表失败即可更换！！");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        if (isScanner) {
            if (mScannerHelper != null) {
                mScannerHelper.onActivityResult(requestCode, resultCode, data);
            }
        } else {
            if (resultCode != RESULT_OK) {
                return;
            }
            if (requestCode == RESULT_YANBIAO) {
                boolean deviceGenghuanZhuangtai = data.getBooleanExtra("deviceGenghuanZhuangtai", false);
                dataBeanArrayList.get(position).setDeviceGenghuanZhuangtai(deviceGenghuanZhuangtai);
                multiTypeAdapterRepair.notifyDataSetChanged();
            } else if (requestCode == RESULT_CHUSHIHUA) {
                String resultBiaoHao = data.getStringExtra("resultBiaoHao");
                String resultID = data.getStringExtra("resultID");
                editNewId.setText(resultID);
                editNewNum.setText(resultBiaoHao);
            }
        }

    }

    @Override
    public void onRepairYanBiaoClick(RepairDanYuanData.DataBean oldDanYuanData, int position) {
        this.position = position;
        isScanner = false;
        ARouter.getInstance().build(K.ThirdTestNewActivity).withInt("biaoHao", oldDanYuanData.getMeterNumber())
                .withInt("fromType", 0).navigation(this, RESULT_YANBIAO);
    }

    @Override
    public void onRepairGengHuanClick(RepairDanYuanData.DataBean oldDanYuanData, int position) {
        this.position = position;
        editBiaohao.setText(oldDanYuanData.getMeterNumber() + "");
        editOldId.setText(oldDanYuanData.getMeterId() + "");
        mdDialogRepairMeter.dismiss();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    protected void onChildConnectFailed(int i) {

    }

    @Override
    protected void onChildConnectSuccess() {
        this.oldSn = sensoroDevice.getSerialNumber();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textDangqianSn.setText("当前连接的SN设备:" + sensoroDevice.getSerialNumber().toUpperCase());
                write(DataParser.CMD_INFO_ALL, "识别模块中...", true, 6000);
            }
        });
    }

    private void readMeter() {
        if (softVersion >= 1.05) {
            write(DataParser.openJiZhongQi(), "正在打开集中器");
        } else {
            int biaoHao = dataBeanArrayList.get(position).getMeterNumber();
            write(DataParser.getDanBiaoDuQuXinXiCMD(biaoHao), "正在读" + biaoHao + "号表");
        }
    }

    @Override
    protected void onChildNotify(byte[] bytes) {
        byte code = (byte) (bytes[2] ^ ((byte) 0x80));
        switch (code) {
            case 0x50:
                String resultAt = sAtInstructRealizeFactory.getSAtTypeString(bytes);
                HashMap<String, String> hashMap1 = new HashMap<>();
                if (resultAt.equals(SAtInstructParams.sAtInstructRSSI + "-R")) {
                    try {
                        hashMap1.putAll(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructRSSI).parseFourRNotifyBytes(bytes));
                        String[] strings = hashMap1.get(SAtInstructParams.sAtInstructRSSI).split(",");
                        rssi = strings[0];
                        snr = strings[1];
                        try {
                            write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructChannel).getFourReadBytes(), "读取信道中...");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (resultAt.equals(SAtInstructParams.sAtInstructChannel + "-R")) {
                    try {
                        hashMap1.putAll(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructChannel).parseFourRNotifyBytes(bytes));
                        channel=hashMap1.get(SAtInstructParams.sAtInstructChannel);
                        write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructSendingPower).getFourReadBytes(), "读取发送功率");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (resultAt.equals(SAtInstructParams.sAtInstructSendingPower + "-R")) {
                    try {
                        hashMap1.putAll(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructSendingPower).parseFourRNotifyBytes(bytes));
                        sendingPower=hashMap1.get(SAtInstructParams.sAtInstructSendingPower);
                        write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructSpreadingFactor).getFourReadBytes(), "读取扩频因子");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (resultAt.equals(SAtInstructParams.sAtInstructSpreadingFactor + "-R")) {
                    try {
                        hashMap1.putAll(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructSpreadingFactor).parseFourRNotifyBytes(bytes));
                        sf=hashMap1.get(SAtInstructParams.sAtInstructSpreadingFactor);
                        readMeter();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    sendMessageToast(resultAt);
                }
                break;
            case 0x11:
                try {
                    HashMap<String, String> map = DataParser.getInformationAll(bytes);
                    int typeMoZu = DataParser.getModuleType(bytes);
                    if (typeMoZu == 1) {
                        softVersion = Double.valueOf(LouShanYunUtils.getSoftWareVersion(Integer.valueOf(map.get(MapParams.软件版本))));
                        initSnAgreement(softVersion);
                        if (softVersion >= 1.07) {
                            write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructRSSI).getTwoReadBytes(), "读取信号强度");
                        } else {
                            sendingPower = map.get(MapParams.发送功率) + "dbm";
                            sf = LouShanYunUtils.getKPYZReadStringByCode(map.get(MapParams.扩频因子));
                            channel = "0".equalsIgnoreCase(map.get(MapParams.信道参数)) ? "模式A" : "模式B";
                            readMeter();
                        }
                    } else {
                        sendMessageToast("识别不是1号模组");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case 0x12:
                rssi = ByteConvertUtils.parseByteToSignedString(bytes[4]) + "";
                snr = ByteConvertUtils.parseByteToSignedString(bytes[5]) + "";
                write(DataParser.CMD_INFO_ALL, "读取基本信息");
                break;
            case 0x41:
                if (bytes[3] == 0) {
                    sendMessageToast("打开集中器成功");
                    int biaoHao = dataBeanArrayList.get(position).getMeterNumber();
                    write(DataParser.getDanBiaoDuQuXinXiCMD(biaoHao), "正在读" + biaoHao + "号表");
                } else {
                    sendMessageToast("打开集中器失败");
                }
                break;
            case 0x42:
                if (bytes[3] == 0) {
                    sendMessageToast("关闭集中器成功");
                } else {
                    sendMessageToast("关闭集中器失败");
                }
                break;
            case 0x32:
                SaveDataMeter item;
                HashMap<String, String> hashMap = new HashMap<>();
                if (bytes[3] == 0) {
                    hashMap.putAll(DataParser.getDanBiaoDuQuXinXi(bytes, softVersion));
                    canSave = true;
                    if (softVersion >= 1.05) {
                        write(DataParser.closeJiZhongQi(), "正在关闭集中器");
                    }
                } else if (bytes[3] == 1) {
                    canSave = false;
                    hashMap.put(MapParams.表号, String.valueOf(dataBeanArrayList.get(position).getMeterNumber()));
                } else if (bytes[3] == 2) {
                    canSave = false;
                    hashMap.put(MapParams.表号, String.valueOf(dataBeanArrayList.get(position).getMeterNumber()));
                    hashMap.put(MapParams.HUB号, String.valueOf(bytes[4] & 0xff));
                }
                item = new SaveDataMeter(hashMap, "2");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (saveDataMeterArrayList.size() == 0) {
                            saveDataMeterArrayList.add(item);
                        } else {
                            saveDataMeterArrayList.set(0, item);
                        }
                        meterMultiTypeAdapter.notifyDataSetChanged();
                    }
                });

                break;
        }
    }

    @Override
    protected void onChildWriteSuccess() {

    }

    @Override
    protected void onChildWriteFailure(int i) {

    }

}
