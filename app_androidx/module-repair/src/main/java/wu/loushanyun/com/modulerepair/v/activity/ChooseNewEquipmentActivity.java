package wu.loushanyun.com.modulerepair.v.activity;


import android.content.Intent;

import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.elvishew.xlog.XLog;
import com.flyco.roundview.RoundLinearLayout;
import com.flyco.roundview.RoundTextView;
import com.qrcore.util.QRScannerHelper;
import com.sensoro.sensor.kit.callback.SensoroDeviceListener;
import com.sensoro.sensor.kit.entity.SensoroDevice;
import com.wu.loushanyun.base.config.K;
import com.wu.loushanyun.base.util.DataParser;
import com.wu.loushanyun.basemvp.init.SensoroBlueManager;
import com.wu.loushanyun.basemvp.v.activity.BaseBlueToothActivity;

import java.util.ArrayList;
import java.util.HashMap;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.multitype.MultiTypeAdapter;
import met.hx.com.base.baseevent.Event;
import met.hx.com.librarybase.some_utils.LogUtils;
import met.hx.com.librarybase.some_utils.ToastManager;
import met.hx.com.librarybase.some_utils.ToastUtils;
import met.hx.com.librarybase.some_utils.XHStringUtil;
import met.hx.com.librarybase.views.MyRadioGroup;
import met.hx.com.librarybase.views.dialog.MDDialog;
import wu.loushanyun.com.libraryfive.p.adapter.SNDeviceViewBinder;
import wu.loushanyun.com.modulerepair.R;
import wu.loushanyun.com.modulerepair.m.RepairData;

@Route(path = K.ChooseNewEquipmentActivity)
public class ChooseNewEquipmentActivity extends BaseBlueToothActivity {
    private EditText editNewSn;
    private RoundTextView buttonNewSn;
    private RoundTextView buttonSaomiaoNewSn;
    private EditText editOldSn;
    private RoundTextView buttonOldSn;
    private RoundTextView buttonSaomiaoOldSn;
    private MyRadioGroup radioGroup;
    private RadioButton radioButtonDan;
    private RadioButton radioButtonDuo;
    private RoundLinearLayout roundDushu;
    private EditText editOldDushu;
    private RoundTextView buttonNext;

    private MDDialog mdDialogBlueList;
    private RecyclerView dialogBlueRecycle;
    private ArrayList<SensoroDevice> deviceArrayList;

    private static final int TYPEOLD = 1000;
    private static final int TYPENEW = 1001;
    private static final int TYPREAD = 1002;
    private int type = TYPENEW;
    private RepairData repairData;
    private QRScannerHelper mScannerHelper;
    private String oldSn;
    private String productName;
    private String realMeterReading;
    private MultiTypeAdapter multiTypeAdapterBlue;


    @Override
    protected void onChildConnectFailed(int i) {

    }

    @Override
    protected void onChildConnectSuccess() {
        readPinLv();
    }

    @Override
    protected void onChildNotify(byte[] bytes) {
        String hexString = Integer.toHexString(bytes[2] & 0xFF);
        if (hexString.equals("91")) {//解析使用类型
            if (bytes[3] == 0) {
                try {
                    HashMap<String, String> hashMap = DataParser.getInformationAll(bytes);
                    int type = DataParser.getModuleType(bytes);
                    String chanpinxingshi = "";
                    if (type == 1) {
                        chanpinxingshi = "远传表号接入";
                    } else if (type == 2) {
                        chanpinxingshi = "远传物联网端";
                    }


                } catch (Exception e) {
                    LogUtils.e(e);
                    ToastManager.getInstance().showLong("检测到新设备未进行出厂配置，请重新选择设备更换");
                }

            }
        }
        sensoroDeviceSession.disconnect();
    }

    @Override
    protected void onChildWriteSuccess() {

    }

    @Override
    protected void onChildWriteFailure(int i) {

    }

    @Override
    protected void initEventListener() {

    }

    @Override
    protected void onEventRunEnd(Event event, int code) {

    }

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        getAllData();
        ba.mActivityLayoutId = R.layout.m_repair_activity_choose;
        ba.mTitleText = "现场更换";
        ba.mTitleRightText = "附近设备";
        ba.mTitleLeftRightWidth = 100;
    }

    private void getAllData() {
        oldSn = getIntent().getStringExtra("oldSn");
        productName = getIntent().getStringExtra("productName");
    }

    @Override
    public void onRightClick(View item) {
        mdDialogBlueList.show();
        SensoroBlueManager.searchBlueTooth(sensoroDeviceListener);
    }

    private void readPinLv() {
        byte[] bytes = DataParser.CMD_INFO_ALL;
        write(bytes, "正在识别更换设备与目标设备是否相符");
    }

    @Override
    protected void initView() {
        super.initView();
        editNewSn = (EditText) findViewById(R.id.edit_new_sn);
        buttonNewSn = (RoundTextView) findViewById(R.id.button_new_sn);
        buttonSaomiaoNewSn = (RoundTextView) findViewById(R.id.button_saomiao_new_sn);
        editOldSn = (EditText) findViewById(R.id.edit_old_sn);
        buttonOldSn = (RoundTextView) findViewById(R.id.button_old_sn);
        buttonSaomiaoOldSn = (RoundTextView) findViewById(R.id.button_saomiao_old_sn);
        radioGroup = (MyRadioGroup) findViewById(R.id.radio_group);
        radioButtonDan = (RadioButton) findViewById(R.id.radio_button_dan);
        radioButtonDuo = (RadioButton) findViewById(R.id.radio_button_duo);
        roundDushu = (RoundLinearLayout) findViewById(R.id.round_dushu);
        editOldDushu = (EditText) findViewById(R.id.edit_old_dushu);
        buttonNext = (RoundTextView) findViewById(R.id.button_next);

        repairData = new RepairData();
        if (!XHStringUtil.isEmpty(oldSn, false)) {
            editOldSn.setText(oldSn);
        }
        if (!XHStringUtil.isEmpty(productName, false)) {
            repairData.setProductForm(productName);
            if (productName.equals("远传表号接入")) {
                radioButtonDan.setChecked(true);
            } else if (productName.equals("远传物联网端")) {
                radioButtonDuo.setChecked(true);
            }
        }
        mScannerHelper = new QRScannerHelper(this);
        mScannerHelper.setCallBack(result -> {
            XLog.i("扫码" + result);
            if (!XHStringUtil.isEmpty(result, false)) {
                result = result.toUpperCase().substring(result.indexOf("SN:") + 3);
                if (type == TYPENEW) {
                    editNewSn.setText(result);
                } else if (type == TYPEOLD) {
                    editOldSn.setText(result);
                }
            }
        });
        initBlueList();

        buttonNewSn.setOnClickListener(view -> {
            ToastUtils.showShort("请选择新设备");
            type = TYPENEW;
            mdDialogBlueList.show();
            SensoroBlueManager.searchBlueTooth(sensoroDeviceListener);
        });
        buttonSaomiaoNewSn.setOnClickListener(view -> {
            type = TYPENEW;
            mScannerHelper.startScanner();
        });
        buttonOldSn.setOnClickListener(view -> {
            ToastUtils.showShort("请选择旧设备");
            type = TYPEOLD;
            mdDialogBlueList.show();
            SensoroBlueManager.searchBlueTooth(sensoroDeviceListener);
        });
        buttonSaomiaoOldSn.setOnClickListener(view -> {
            type = TYPEOLD;
            mScannerHelper.startScanner();
        });
        buttonNext.setOnClickListener(view -> {
            if (radioButtonDan.isChecked()) {
                repairData.setProductForm("远传表号接入");
            } else {
                repairData.setProductForm("远传物联网端");
            }
            String newSN = editNewSn.getText().toString().trim();
            String oldSN = editOldSn.getText().toString().trim();
            if (XHStringUtil.isEmpty(oldSN, false)) {
                ToastUtils.showShort("请选择旧设备");
                return;
            }
            if (XHStringUtil.isEmpty(newSN, false)) {
                ToastUtils.showShort("请选择新设备");
                return;
            }
            if (!radioButtonDan.isChecked() && !radioButtonDuo.isChecked()) {
                ToastUtils.showShort("请选择旧设备的产品形式");
                return;
            }
            if ("远传物联网端".equals(repairData.getProductForm())) {
                if (XHStringUtil.isEmpty(editOldDushu.getText().toString(), false)) {
                    sendMessageToast("情输入旧机械表的读数");
                    return;
                }
                realMeterReading = editOldDushu.getText().toString();
            }
            repairData.setSn(oldSN);
            repairData.setNewSn(newSN);
            if (sensoroDevice != null) {
                ARouter.getInstance().build(K.FieldDetectionActivity)
                        .withParcelable("sensoroDevice", sensoroDevice)
                        .withParcelable("repairData", repairData)
                        .withString("realMeterReading", realMeterReading)
                        .navigation();
            } else {
                sendMessageToast("未搜到新设备的蓝牙");
            }
        });
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (radioButtonDan.isChecked()) {
                repairData.setProductForm("远传表号接入");
                roundDushu.setVisibility(View.GONE);
            } else if (radioButtonDuo.isChecked()) {
                roundDushu.setVisibility(View.VISIBLE);
                repairData.setProductForm("远传物联网端");
            }
        });
        SensoroBlueManager.searchBlueTooth(sensoroDeviceListener);
    }


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
                if (type == TYPENEW) {
                    ChooseNewEquipmentActivity.this.sensoroDevice = sensoroDevice;
                    editNewSn.setText(sensoroDevice.getSerialNumber());
                } else if (type == TYPEOLD) {
                    editOldSn.setText(sensoroDevice.getSerialNumber());
                } else if (type == TYPREAD) {
                    ChooseNewEquipmentActivity.this.sensoroDevice = sensoroDevice;
                    connectBlueTooth(sensoroDevice);
                }
            }
        }));
        multiTypeAdapterBlue.setItems(deviceArrayList);
        dialogBlueRecycle.setAdapter(multiTypeAdapterBlue);

    }

    private SensoroDeviceListener sensoroDeviceListener = new SensoroDeviceListener<SensoroDevice>() {
        @Override
        public void onNewDevice(SensoroDevice sensoroDevice) {
            XLog.i("蓝牙onNewDevice" + sensoroDevice.toString());
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
            XLog.i("蓝牙onGoneDevice" + sensoroDevice.toString());
        }

        @Override
        public void onUpdateDevices(ArrayList<SensoroDevice> arrayList) {
            XLog.i("蓝牙onUpdateDevices" + arrayList.toString());
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mScannerHelper != null) {
            mScannerHelper.onActivityResult(requestCode, resultCode, data);
        }
    }
}
