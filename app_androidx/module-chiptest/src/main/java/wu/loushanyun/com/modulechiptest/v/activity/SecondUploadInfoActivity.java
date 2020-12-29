package wu.loushanyun.com.modulechiptest.v.activity;

import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;

import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.elvishew.xlog.XLog;
import com.flyco.roundview.RoundTextView;
import com.gpsdk.demo.DeviceConnFactoryManager;
import com.qrcore.util.QRScannerHelper;
import com.sensoro.sensor.kit.callback.SensoroDeviceListener;
import com.sensoro.sensor.kit.entity.SensoroDevice;
import com.wu.loushanyun.base.config.K;
import com.wu.loushanyun.base.print.PrintTool;
import com.wu.loushanyun.basemvp.init.SensoroBlueManager;
import com.wu.loushanyun.basemvp.v.activity.BaseBlueToothActivity;

import java.util.ArrayList;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.multitype.MultiTypeAdapter;
import met.hx.com.base.baseevent.Event;
import met.hx.com.librarybase.some_utils.KeyboardUtils;
import met.hx.com.librarybase.some_utils.TimeUtils;
import met.hx.com.librarybase.some_utils.XHStringUtil;
import met.hx.com.librarybase.views.dialog.MDDialog;
import wu.loushanyun.com.libraryfive.p.adapter.SNDeviceViewBinder;
import wu.loushanyun.com.modulechiptest.R;
import wu.loushanyun.com.modulechiptest.init.ChipCode;
import wu.loushanyun.com.modulechiptest.m.SensoroDeviceInfo;
import wu.loushanyun.com.modulechiptest.p.adapter.SecondUploadInfoViewBinder;
import wu.loushanyun.com.modulechiptest.p.runner.MChipDeviceHisDataRunner;

@Route(path = K.SecondUploadInfoActivity)
public class SecondUploadInfoActivity extends BaseBlueToothActivity implements SecondUploadInfoViewBinder.OnPrintListener {
    private RoundTextView textSaomiao;
    private RoundTextView textPrintCoon;
    private RecyclerView recyclerview;
    private LinearLayout linearBlueTooth;
    private EditText editSearch;
    private RoundTextView resetClear;
    private RoundTextView resetClearList;
    private RecyclerView dialogBlueRecycle;
    private RoundTextView textSum;


    private TextView textShijian;

    private ArrayList<SensoroDeviceInfo> dataBeanXList;
    private MultiTypeAdapter multiTypeAdapter;
    private QRScannerHelper mScannerHelper;

    private SNDeviceViewBinder snDeviceViewBinder;
    private MultiTypeAdapter multiTypeAdapterBlue;
    private SensoroDevice sensoroDeviceChoose;
    private ArrayList<SensoroDevice> deviceArrayList;

    private MDDialog mdDialog;
    private PrintTool printTool;

    @Override
    protected void initEventListener() {
        registerEventRunner(ChipCode.MChipDeviceHisDataRunner, new MChipDeviceHisDataRunner());
    }


    @Override
    protected void onEventRunEnd(Event event, int code) {
        if (code == ChipCode.MChipDeviceHisDataRunner) {
            if (event.isSuccess()) {
                SensoroDeviceInfo sensoroDeviceInfo = (SensoroDeviceInfo) event.getReturnParamAtIndex(0);
                if ("查询成功".equals(sensoroDeviceInfo.getMsg())) {
                    if (sensoroDeviceInfo.getData().get(0) != null) {
                        int index1 = containsMeter(dataBeanXList, sensoroDeviceInfo);
                        if (index1 != -1) {
                            dataBeanXList.set(index1, sensoroDeviceInfo);
                        } else {
                            dataBeanXList.add(sensoroDeviceInfo);
                        }
                    }
                    multiTypeAdapter.notifyDataSetChanged();
                    textSum.setText(dataBeanXList.size() + "");
                }

            }
        }

    }

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
                switch (state) {
                    case DeviceConnFactoryManager.CONN_STATE_DISCONNECT:
                        textPrintCoon.setText("连接打印机(未连接)");
                        break;
                    case DeviceConnFactoryManager.CONN_STATE_CONNECTING:
                        sendMessageToast(getString(com.wu.loushanyun.R.string.str_conn_state_connecting));
                        break;
                    case DeviceConnFactoryManager.CONN_STATE_CONNECTED:
                        sendMessageToast(getString(com.wu.loushanyun.R.string.str_conn_state_connected) + "\n" + printTool.getConnDeviceInfo());
                        textPrintCoon.setText("连接打印机(已连接)");
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
        snDeviceViewBinder = new SNDeviceViewBinder(sensoroDevice -> {
            this.sensoroDeviceChoose = sensoroDevice;
            linearBlueTooth.setVisibility(View.GONE);
            pushEvent(ChipCode.MChipDeviceHisDataRunner, sensoroDeviceChoose.sn);
//            pushEvent(ChipCode.MChipDeviceHisDataRunner, "02930017C6FABA4C");
            KeyboardUtils.hideSoftInput(this);
            printTool.createChipPrint2(sensoroDevice.sn);
        });
    }

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        ba.mActivityLayoutId = R.layout.m_chip_activity_second_upload;
        ba.mTitleText = "检查自动上传";
        ba.mTitleRightText = "附近蓝牙";
        ba.mTitleLeftRightWidth = 100;
    }

    @Override
    public void onRightClick(View item) {
        linearBlueTooth.setVisibility(View.VISIBLE);
    }

    @Override
    protected void initView() {
        super.initView();
        textSaomiao = (RoundTextView) findViewById(R.id.text_saomiao);
        textPrintCoon = (RoundTextView) findViewById(R.id.text_print_coon);
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        linearBlueTooth = (LinearLayout) findViewById(R.id.linear_blue_tooth);
        editSearch = (EditText) findViewById(R.id.edit_search);
        resetClear = (RoundTextView) findViewById(R.id.reset_clear);
        resetClearList = (RoundTextView) findViewById(R.id.reset_clear_list);
        dialogBlueRecycle = (RecyclerView) findViewById(R.id.dialog_blue_recycle);
        textSum = (RoundTextView) findViewById(R.id.text_sum);

        View view = getLayoutInflater().inflate(R.layout.m_chip_dialog_shijian, null);
        textShijian = (TextView) view.findViewById(R.id.text_shijian);
        textShijian.setText("当前时间:" + TimeUtils.getCurTimeString());
        mdDialog = new MDDialog.Builder(this).setContentView(view)
                .setTitle("请确认手机时间是否准确!!!!")
                .create();
        mdDialog.show();
        multiTypeAdapter = new MultiTypeAdapter();
        dataBeanXList = new ArrayList<>();
        multiTypeAdapter.register(SensoroDeviceInfo.class, new SecondUploadInfoViewBinder(this));
        multiTypeAdapter.setItems(dataBeanXList);
        recyclerview.setAdapter(multiTypeAdapter);

        textSaomiao.setOnClickListener(v -> {
            mScannerHelper.startScanner();
        });
        textPrintCoon.setOnClickListener(view1 -> {
            printTool.showPrintDialog();
        });
        initQRScanner();
        initBlueList();
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
    }

    private void initBlueList() {
        multiTypeAdapterBlue = new MultiTypeAdapter();
        deviceArrayList = new ArrayList<>();
        multiTypeAdapterBlue.register(SensoroDevice.class, snDeviceViewBinder);
        multiTypeAdapterBlue.setItems(deviceArrayList);
        dialogBlueRecycle.setAdapter(multiTypeAdapterBlue);

    }

    protected SensoroDeviceListener sensoroDeviceListener=new SensoroDeviceListener<SensoroDevice>() {
        @Override
        public void onNewDevice(SensoroDevice sensoroDevice) {
            XLog.i("蓝牙onNewDevice"+sensoroDevice.toString());
            if(!deviceArrayList.contains(sensoroDevice)){
                deviceArrayList.add(sensoroDevice);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        multiTypeAdapterBlue.notifyDataSetChanged();
                    }
                });
            }else {
                XLog.i("蓝牙sdaasasdasdasdasd"+sensoroDevice.toString());
            }

        }

        @Override
        public void onGoneDevice(SensoroDevice sensoroDevice) {
            XLog.i("蓝牙onGoneDevice"+sensoroDevice.toString());
        }

        @Override
        public void onUpdateDevices(ArrayList<SensoroDevice> arrayList) {
            XLog.i("蓝牙onUpdateDevices"+arrayList.toString());
            for(int i=0;i<arrayList.size();i++){
                SensoroDevice sensoroDevice=arrayList.get(i);
                if(deviceArrayList.contains(sensoroDevice)){
                    deviceArrayList.set(deviceArrayList.indexOf(sensoroDevice),sensoroDevice);
                }else {
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
    protected void onChildConnectFailed(int i) {

    }

    @Override
    protected void onChildConnectSuccess() {

    }

    @Override
    protected void onChildNotify(byte[] bytes) {

    }

    @Override
    protected void onChildWriteSuccess() {

    }

    @Override
    protected void onChildWriteFailure(int i) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mScannerHelper != null) {
            mScannerHelper.onActivityResult(requestCode, resultCode, data);
        }
    }

    public int containsMeter(ArrayList<SensoroDeviceInfo> arrayList, SensoroDeviceInfo sensoroDeviceInfo) {
        int containsIndex = -1;
        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i).getData().get(0).getSn().equals(sensoroDeviceInfo.getData().get(0).getSn())) {
                containsIndex = i;
            }
        }
        return containsIndex;
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
                    String strings = result.substring(result.indexOf("/SN:") + 4);
                    XLog.i("扫描的SN" + strings);
                    pushEvent(ChipCode.MChipDeviceHisDataRunner, strings);
                }
            }
        });
    }

    @Override
    public void onPrint(String sn) {
        printTool.printBitmap(printTool.createChipPrint2(sn));
    }
}
