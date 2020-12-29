package wu.loushanyun.com.modulechiptest.v.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.elvishew.xlog.XLog;
import com.flyco.roundview.RoundTextView;
import com.sensoro.sensor.kit.entity.SensoroDevice;
import com.wu.loushanyun.base.config.K;
import com.wu.loushanyun.base.util.DataParser;
import com.wu.loushanyun.basemvp.v.activity.BaseSnBlueToothActivity;

import java.util.ArrayList;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.baseevent.Event;
import met.hx.com.librarybase.some_utils.ByteConvertUtils;
import met.hx.com.librarybase.some_utils.KeyboardUtils;
import met.hx.com.librarybase.some_utils.XHStringUtil;
import met.hx.com.librarybase.some_utils.util.AbSharedUtil;
import wu.loushanyun.com.modulechiptest.R;

@Route(path = K.ATTestActivity)
public class ATTestActivity extends BaseSnBlueToothActivity {
    private TextView textDangqianSn;
    private RoundTextView bluetoothConn;
    private RoundTextView bluetoothDisconn;
    private TextView textShowHint;
    private RoundTextView textClearWrite;
    private RoundTextView textWrite;
    private EditText editWrite;
    private RoundTextView textClearRead;
    private RoundTextView textChangeRead;
    private EditText textRead;


    protected SensoroDevice sensoroDeviceChoose;

    private StringBuffer stringBuffer;

    private ArrayList<byte[]> arrayList;
    private boolean isASCII = false;

    @Override
    protected int onChildLayout() {
        return R.layout.m_chip_actvity_at;
    }

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        super.onInitAttribute(ba);
        ba.mTitleText = "AT指令验证";
    }

    @Override
    protected void initView() {
        super.initView();
        textDangqianSn = (TextView) findViewById(R.id.text_dangqian_sn);
        bluetoothConn = (RoundTextView) findViewById(R.id.bluetooth_conn);
        bluetoothDisconn = (RoundTextView) findViewById(R.id.bluetooth_disconn);
        textShowHint = (TextView) findViewById(R.id.text_show_hint);
        textClearWrite = (RoundTextView) findViewById(R.id.text_clear_write);
        textWrite = (RoundTextView) findViewById(R.id.text_write);
        editWrite = (EditText) findViewById(R.id.edit_write);
        textClearRead = (RoundTextView) findViewById(R.id.text_clear_read);
        textChangeRead = (RoundTextView) findViewById(R.id.text_change_read);
        textRead = (EditText) findViewById(R.id.text_read);

        stringBuffer = new StringBuffer();
        arrayList = new ArrayList<>();
        editWrite.setText(AbSharedUtil.getString(this, "writeAtStr"));
        textShowHint.setText("请输入AT指令");
        bluetoothConn.setOnClickListener(this::onClick);
        bluetoothDisconn.setOnClickListener(this::onClick);
        textClearWrite.setOnClickListener(this::onClick);
        textWrite.setOnClickListener(this::onClick);
        textRead.setOnClickListener(this::onClick);
        textClearRead.setOnClickListener(this::onClick);
        textChangeRead.setOnClickListener(this::onClick);
    }

    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.bluetooth_conn:
                if (snBlueToothTool.isConnected()) {
                    sendMessageToast("当前已连接设备");
                    return;
                }
                snBlueToothTool.connectBlueTooth(sensoroDeviceChoose);
                break;
            case R.id.bluetooth_disconn:
                snBlueToothTool.disconnectBlueTooth();
                break;
            case R.id.text_clear_write:
                editWrite.setText("");
                break;
            case R.id.text_clear_read:
                stringBuffer = new StringBuffer();
                arrayList.clear();
                textRead.setText(stringBuffer.toString());
                break;
            case R.id.text_change_read:
                if (!isASCII) {

                    arrayList.add(  ByteConvertUtils.convertHexStringToBytes(textRead.getText().toString()));
                    StringBuffer stringBuffer = new StringBuffer();
                    for (int i = 0; i < arrayList.size(); i++) {
                        stringBuffer.append(ByteConvertUtils.getASCIIbyByte(arrayList.get(i), 3, 2));
                    }
                    textRead.setText(stringBuffer.toString());
                    isASCII = true;
                } else {
                    textRead.setText(stringBuffer.toString());
                    isASCII = false;
                }
                break;
            case R.id.text_write:
                KeyboardUtils.hideSoftInput(this);
                try {
                    String writeStr = editWrite.getText().toString().replace(" ", "").trim();
                    if (XHStringUtil.isEmpty(writeStr, false)) {
                        sendMessageToast("请输入指令");
                        return;
                    }
                    writeStr=writeStr+"\r\n";
                    AbSharedUtil.putString(this, "writeAtStr", writeStr);
                    byte[] writeBytes = DataParser.getBytesByASCII(writeStr);

                    textRead.setText(ByteConvertUtils.getASCIIbyByte(writeBytes, 3, 2));
                    snBlueToothTool.write(writeBytes,"正在发送",true,1000);
                } catch (Exception e) {
                    XLog.e(e);
                }
                break;
        }
    }

    @Override
    protected void onClickBlueListListener(SensoroDevice sensoroDevice) {
        super.onClickBlueListListener(sensoroDevice);
        this.sensoroDeviceChoose = sensoroDevice;
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
            }
        });
    }

    @Override
    public void onChildNotify(byte[] bytes) {
        stringBuffer.append(DataParser.byteToString(bytes) + "\n");
        arrayList.add(bytes);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textRead.setText(stringBuffer.toString());
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

    }

    @Override
    protected void onEventRunEnd(Event event, int code) {

    }
}
