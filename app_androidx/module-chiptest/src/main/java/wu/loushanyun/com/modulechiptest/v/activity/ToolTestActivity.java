package wu.loushanyun.com.modulechiptest.v.activity;

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
import com.sensoro.sensor.kit.callback.SensoroDeviceListener;
import com.sensoro.sensor.kit.entity.SensoroDevice;
import com.wu.loushanyun.base.config.K;
import com.wu.loushanyun.base.util.DataParser;
import com.wu.loushanyun.base.util.MapParams;
import com.wu.loushanyun.basemvp.init.SensoroBlueManager;
import com.wu.loushanyun.basemvp.v.activity.BaseBlueToothActivity;

import java.util.ArrayList;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.multitype.MultiTypeAdapter;
import met.hx.com.base.baseevent.Event;
import met.hx.com.librarybase.some_utils.ByteConvertUtils;
import met.hx.com.librarybase.some_utils.KeyboardUtils;
import met.hx.com.librarybase.some_utils.XHStringUtil;
import met.hx.com.librarybase.some_utils.util.AbSharedUtil;
import wu.loushanyun.com.libraryfive.p.adapter.SNDeviceViewBinder;
import wu.loushanyun.com.modulechiptest.R;

@Route(path = K.ToolTestActivity)
public class ToolTestActivity extends BaseBlueToothActivity {
    private ArrayList<SensoroDevice> deviceArrayList;
    private TextView textDangqianSn;
    private RoundTextView bluetoothConn;
    private RoundTextView bluetoothDisconn;
    private EditText editWrite;
    private RoundTextView textClearWrite;
    private RoundTextView textWrite;
    private TextView textRead;
    private RoundTextView resetClearRead;
    private LinearLayout linearBlueTooth;
    private EditText editSearch;
    private RoundTextView resetClear;
    private RoundTextView resetClearList;
    private RecyclerView dialogBlueRecycle;

    private RoundTextView mTextClearInt;
    private RoundTextView mTextAnalyzeInt;
    private EditText mEditWriteInt;

    private RoundTextView mTextClearFloatSymbol;
    private RoundTextView mTextAnalyzeFloatSymbol;
    private EditText mEditWriteFloatSymbol;
    private RoundTextView mTextClearDoubleSymbol;
    private RoundTextView mTextAnalyzeDoubleSymbol;
    private EditText mEditWriteDoubleSymbol;


    private SNDeviceViewBinder snDeviceViewBinder;
    private MultiTypeAdapter multiTypeAdapterBlue;
    private boolean analyzeDouble;
    private boolean analyzeFloat;
    private boolean analyzeDoubleSymble;
    private boolean analyzeFloatSymble;
    private boolean analyzeInt;
    private String writeIntValue;
    private String writeFloatValue;
    private String writeDoubleValue;
    private String writeFloatSymbleValue;
    private String writeDoubleSymbleValue;


    @Override
    protected void onEventRunEnd(Event event, int code) {

    }

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        ba.mActivityLayoutId = R.layout.m_chip_activity_tool;
        ba.mTitleText = "开发人员工具";
        ba.mTitleRightText = "附近蓝牙";
        ba.mTitleLeftRightWidth = 100;
    }

    @Override
    public void onRightClick(View item) {
        linearBlueTooth.setVisibility(View.VISIBLE);
        SensoroBlueManager.searchBlueTooth(sensoroDeviceListener);
    }

    @Override
    protected void onChildConnectFailed(int i) {

    }

    @Override
    protected void onChildConnectSuccess() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textDangqianSn.setText("当前连接的SN设备:" + sensoroDevice.getSerialNumber().toUpperCase());
            }
        });

    }

    @Override
    protected void onChildNotify(byte[] bytes) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textRead.setText(DataParser.byteToString(bytes));
            }
        });
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
    protected void initLifeCycle() {
        snDeviceViewBinder = new SNDeviceViewBinder(sensoroDevice -> {
            this.sensoroDevice = sensoroDevice;
            linearBlueTooth.setVisibility(View.GONE);
            connectBlueTooth(sensoroDevice);
            KeyboardUtils.hideSoftInput(this);
        });
    }

    @Override
    protected void initView() {
        super.initView();
        textDangqianSn = (TextView) findViewById(R.id.text_dangqian_sn);
        bluetoothConn = (RoundTextView) findViewById(R.id.bluetooth_conn);
        bluetoothDisconn = (RoundTextView) findViewById(R.id.bluetooth_disconn);
        editWrite = (EditText) findViewById(R.id.edit_write);
        textClearWrite = (RoundTextView) findViewById(R.id.text_clear_write);
        textWrite = (RoundTextView) findViewById(R.id.text_write);
        textRead = (TextView) findViewById(R.id.text_read);
        resetClearRead = (RoundTextView) findViewById(R.id.reset_clear_read);
        linearBlueTooth = (LinearLayout) findViewById(R.id.linear_blue_tooth);
        editSearch = (EditText) findViewById(R.id.edit_search);
        resetClear = (RoundTextView) findViewById(R.id.reset_clear);
        resetClearList = (RoundTextView) findViewById(R.id.reset_clear_list);
        dialogBlueRecycle = (RecyclerView) findViewById(R.id.dialog_blue_recycle);
        mTextClearInt = (RoundTextView) findViewById(R.id.text_clear_int);
        mTextAnalyzeInt = (RoundTextView) findViewById(R.id.text_analyze_int);
        mEditWriteInt = (EditText) findViewById(R.id.edit_write_int);

        mTextClearFloatSymbol = (RoundTextView) findViewById(R.id.text_clear_float_symbol);
        mTextAnalyzeFloatSymbol = (RoundTextView) findViewById(R.id.text_analyze_float_symbol);
        mEditWriteFloatSymbol = (EditText) findViewById(R.id.edit_write_float_symbol);
        mTextClearDoubleSymbol = (RoundTextView) findViewById(R.id.text_clear_double_symbol);
        mTextAnalyzeDoubleSymbol = (RoundTextView) findViewById(R.id.text_analyze_double_symbol);
        mEditWriteDoubleSymbol = (EditText) findViewById(R.id.edit_write_double_symbol);


        editWrite.setText(AbSharedUtil.getString(this, "writeStr"));
        initBlueList();
        bluetoothConn.setOnClickListener(v -> {
            if (textDangqianSn.getText().toString().contains("当前连接的SN设备")) {
                sendMessageToast("当前已连接设备");
                return;
            }
            connectBlueTooth(sensoroDevice);
        });
        bluetoothDisconn.setOnClickListener(v -> {
            sensoroDeviceSession.disconnect();
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
        initClick();
    }

    /**
     * 各种协议点击事件
     */
    private void initClick() {
        textClearWrite.setOnClickListener(this::OnBlueToothClick);
        textWrite.setOnClickListener(this::OnBlueToothClick);
        resetClearRead.setOnClickListener(this::OnBlueToothClick);
        mTextClearInt.setOnClickListener(this::OnClick);
        mTextClearFloatSymbol.setOnClickListener(this::OnClick);
        mTextClearDoubleSymbol.setOnClickListener(this::OnClick);
        mTextAnalyzeInt.setOnClickListener(this::OnClick);
        mTextAnalyzeFloatSymbol.setOnClickListener(this::OnClick);
        mTextAnalyzeDoubleSymbol.setOnClickListener(this::OnClick);

    }

    private void OnClick(View view) {
        switch (view.getId()) {
            case R.id.text_clear_int:
                mEditWriteInt.setText("");
                analyzeInt=false;
                break;
            case R.id.text_analyze_int:
                if (!analyzeInt) {
                    analyzeInt = true;
                    String writeInt = mEditWriteInt.getText().toString().replace(" ", "").trim().toLowerCase();
                    writeIntValue = writeInt;
                    if (XHStringUtil.isEmpty(writeInt, false)) {
                        sendMessageToast("请输入指令");
                        return;
                    }
                    if (writeInt.length() % 2 != 0) {
                        sendMessageToast("请输入正确指令");
                        return;
                    }
                    byte[] byteint = ByteConvertUtils.convertHexStringToBytes(writeInt);

                    long tmp;
                    tmp = 0;
                    for (int i = byteint.length; i > 0; i--) {
                        tmp = (tmp * 256) + (byteint[i - 1] & 0xff);
                    }
                    mEditWriteInt.setText(String.valueOf(tmp));
                } else {
                    mEditWriteInt.setText(writeIntValue);
                    analyzeInt = false;
                }
                break;
            case R.id.text_clear_float_symbol:
                mEditWriteFloatSymbol.setText("");
                analyzeFloatSymble=false;
                break;
            case R.id.text_analyze_float_symbol:
                if (!analyzeFloatSymble) {
                    String writeFloatSymbol = mEditWriteFloatSymbol.getText().toString().replace(" ", "").trim().toLowerCase();
                    writeFloatSymbleValue=writeFloatSymbol;
                    if (XHStringUtil.isEmpty(writeFloatSymbol, false)) {
                        sendMessageToast("请输入指令");
                        return;
                    }

                    float f = Float.valueOf(writeFloatSymbol);
                    byte[] bytesk = ByteConvertUtils.convertHexStringToBytes(ByteConvertUtils.floatToHexStr(f));

                    mEditWriteFloatSymbol.setText(ByteConvertUtils.byteToString(bytesk,false));
                    analyzeFloatSymble = true;
                } else {
                    mEditWriteFloatSymbol.setText(writeFloatSymbleValue);
                    analyzeFloatSymble = false;
                }

                break;
            case R.id.text_clear_double_symbol:
                mEditWriteDoubleSymbol.setText("");
                analyzeDoubleSymble = false;
                break;
            case R.id.text_analyze_double_symbol:
                if (!analyzeDoubleSymble) {
                    String writeDoubleSymbol = mEditWriteDoubleSymbol.getText().toString().replace(" ", "").trim().toLowerCase();
                    writeDoubleSymbleValue=writeDoubleSymbol;
                    if (XHStringUtil.isEmpty(writeDoubleSymbol, false)) {
                        sendMessageToast("请输入指令");
                        return;
                    }
                    double d = Double.parseDouble(writeDoubleSymbol);
                    byte[] bytesd = ByteConvertUtils.convertHexStringToBytes(ByteConvertUtils.doubletoHex(d));
                    mEditWriteDoubleSymbol.setText(ByteConvertUtils.byteToString(bytesd,false));
                    analyzeDoubleSymble = true;

                } else {
                    mEditWriteDoubleSymbol.setText(writeDoubleSymbleValue);
                    analyzeDoubleSymble = false;
                }
                break;

        }

    }


    /**
     * 需要判断蓝牙连接状态的点击事件
     *
     * @param view
     */
    private void OnBlueToothClick(View view) {
        if (!isConnect()) {
            sendMessageToast("未连接蓝牙");
            return;
        }
        switch (view.getId()) {
            case R.id.text_clear_write:
                editWrite.setText("");
                break;
            case R.id.text_write:
                KeyboardUtils.hideSoftInput(this);
                try {
                    String writeStr = editWrite.getText().toString().replace(" ", "").trim().toLowerCase();
                    if (XHStringUtil.isEmpty(writeStr, false)) {
                        sendMessageToast("请输入指令");
                        return;
                    }
                    AbSharedUtil.putString(this, "writeStr", writeStr);
                    ArrayList<String> arrayList = new ArrayList<>();
                    if (writeStr.length() % 2 == 0) {
                        for (int i = 0; i < writeStr.length(); i++) {
                            if (i % 2 == 0) {
                                arrayList.add(writeStr.substring(i, i + 2));
                            }
                        }
                    } else {
                        String lastStr = "0" + writeStr.substring(writeStr.length() - 1, writeStr.length());
                        writeStr = writeStr.substring(0, writeStr.length() - 1) + lastStr;
                        for (int i = 0; i < writeStr.length(); i++) {
                            if (i % 2 == 0) {
                                arrayList.add(writeStr.substring(i, i + 2));
                            }

                        }
                    }
                    byte[] bytes = new byte[arrayList.size()];
                    for (int i = 0; i < arrayList.size(); i++) {
                        int value = Integer.parseInt(arrayList.get(i), 16);
                        bytes[i] = (byte) value;
                        XLog.i("第" + i + "个=" + bytes[i]);
                    }
                    write(bytes, "正在发送", true, 200);
                } catch (Exception e) {
                    sendMessageToast("解析错误");
                }
                break;
            case R.id.reset_clear_read:
                textRead.setText("");
                break;
        }
    }

    private boolean isConnect() {
        if (textDangqianSn.getText().toString().contains("当前连接的SN设备")) {
            return true;
        } else {
            return false;
        }
    }

    private void initBlueList() {
        multiTypeAdapterBlue = new MultiTypeAdapter();
        deviceArrayList = new ArrayList<>();
        multiTypeAdapterBlue.register(SensoroDevice.class, snDeviceViewBinder);
        multiTypeAdapterBlue.setItems(deviceArrayList);
        dialogBlueRecycle.setAdapter(multiTypeAdapterBlue);

    }

    protected SensoroDeviceListener sensoroDeviceListener = new SensoroDeviceListener<SensoroDevice>() {
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
}
