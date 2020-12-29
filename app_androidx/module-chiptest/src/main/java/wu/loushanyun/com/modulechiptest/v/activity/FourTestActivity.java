package wu.loushanyun.com.modulechiptest.v.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SwitchCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.elvishew.xlog.XLog;
import com.flyco.roundview.RoundTextView;
import com.gpsdk.demo.DeviceConnFactoryManager;
import com.sensoro.sensor.kit.entity.SensoroDevice;
import com.wu.loushanyun.base.config.K;
import com.wu.loushanyun.base.print.PrintTool;
import com.wu.loushanyun.base.util.DataParser;
import com.wu.loushanyun.base.util.LouShanYunUtils;
import com.wu.loushanyun.base.util.MapParams;
import com.wu.loushanyun.basemvp.init.SensoroBlueManager;
import com.wu.loushanyun.basemvp.m.sat.SAtInstructRealizeFactory;
import com.wu.loushanyun.basemvp.m.sat.util.SAtInstructParams;
import com.wu.loushanyun.basemvp.v.activity.BaseSnBlueToothActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.dialog.LoadingDialogUtil;
import met.hx.com.base.baseevent.Event;
import met.hx.com.librarybase.some_utils.ByteConvertUtils;
import met.hx.com.librarybase.some_utils.KeyboardUtils;
import met.hx.com.librarybase.some_utils.TimeUtils;
import met.hx.com.librarybase.some_utils.XHStringUtil;
import met.hx.com.librarybase.some_utils.util.AbSharedUtil;
import wu.loushanyun.com.modulechiptest.R;
import wu.loushanyun.com.modulechiptest.init.ChipCode;
import wu.loushanyun.com.modulechiptest.m.NewInfo;
import wu.loushanyun.com.modulechiptest.p.runner.MChipGetNewsInfoRunner;

import static com.wu.loushanyun.basemvp.init.SnBlueToothTool.BLUE_SEND_TIME_OUT;

public class FourTestActivity extends BaseSnBlueToothActivity {


    private ScrollView scrollView;
    private TextView textPrint;
    private EditText editPrintNum;
    private RoundTextView textPrintCoon;
    private TextView textToken;
    private TextView textDangqianSn;
    private RoundTextView bluetoothConn;
    private RoundTextView buttonDayin;
    private RoundTextView bluetoothDisconn;
    private SwitchCompat switchSaveStatus;
    private Spinner fasonggonglvSelect;
    private RoundTextView fasonggonglvSetting;
    private Spinner xindaocanshuSelect;
    private RoundTextView xindaocanshuSetting;
    private Spinner kuopinyinziSelect;
    private RoundTextView kuopinyinziSetting;
    private EditText editIdStart;
    private RoundTextView roundCreateId;
    private EditText editChangjiabiaoshi;
    private EditText editRxdelay;
    private RoundTextView rxdelaySetting;
    private RoundTextView maichongSetting;
    private ImageView systemStatus;
    private TextView textShowHint;
    private RoundTextView thirdTextClearWrite;
    private RoundTextView thirdTextWrite;
    private EditText thirdEditWrite;
    private RoundTextView thirdTextClearRead;
    private TextView thirdTextRead;
    private RoundTextView xulieRead;
    private RoundTextView xulieTextReset;
    private TextView xulieTextRead;
    private RoundTextView xulieTextClearRead;
    private TextView xulieTextReadThird;
    private RoundTextView xulieTextClearAdd;
    private RoundTextView xulieTextGetNetwork;
    private RoundTextView xulieTextOneSetting;
    private RecyclerView xulieRecycleview;
    private EditText editOtherVersion;
    private RoundTextView otherVersionSetting;
    private EditText editXinhaoqiangduStart;
    private EditText editXinhaoqiangduEnd;
    private EditText editXinzaobiStart;
    private EditText editXinzaobiEnd;
    private TextView textHuoqu;
    private RoundTextView buttonQiangzhifasong;
    private RoundTextView buttonDuquXinhao;
    private TextView textHuoquNew;
    private RoundTextView buttonDuquNew;
    private RoundTextView roundTextDuqu;
    private TextView textDuquInfo;
    private RoundTextView roundTextXiumianJihuo;
    private RoundTextView roundTextXiumian;
    private RoundTextView roundTextSaomiao;
    private LinearLayout linearBlueTooth;
    private EditText editSearch;
    private RoundTextView resetClear;
    private RoundTextView resetClearList;
    private RecyclerView dialogBlueRecycle;


    private SensoroDevice sensoroDeviceChoose;
    private PrintTool printTool;

    //默认参数
    private boolean isAllSetting;
    private boolean isAllReading;
    private boolean isNewID = true;//每次设置完成都让其生成一个新的id
    private boolean isJiHuo = true;
    private boolean isJiHuoAndXiuMian = false;
    private boolean isReadAllSuccess = false;

    private int type;
    private int writeCode;
    private boolean canPrint;
    private SAtInstructRealizeFactory sAtInstructRealizeFactory;
    private HashMap<String, String> hashMap;
    private String[] kuopinyinziArray;

    private StringBuffer stringBufferThird;

    @Override
    protected int onChildLayout() {
        return R.layout.m_chip_activity_four;
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
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        textPrint = (TextView) findViewById(R.id.text_print);
        editPrintNum = (EditText) findViewById(R.id.edit_print_num);
        textPrintCoon = (RoundTextView) findViewById(R.id.text_print_coon);
        textToken = (TextView) findViewById(R.id.text_token);
        textDangqianSn = (TextView) findViewById(R.id.text_dangqian_sn);
        bluetoothConn = (RoundTextView) findViewById(R.id.bluetooth_conn);
        buttonDayin = (RoundTextView) findViewById(R.id.button_dayin);
        bluetoothDisconn = (RoundTextView) findViewById(R.id.bluetooth_disconn);
        switchSaveStatus = (SwitchCompat) findViewById(R.id.switch_save_status);
        fasonggonglvSelect = (Spinner) findViewById(R.id.fasonggonglv_select);
        fasonggonglvSetting = (RoundTextView) findViewById(R.id.fasonggonglv_setting);
        xindaocanshuSelect = (Spinner) findViewById(R.id.xindaocanshu_select);
        xindaocanshuSetting = (RoundTextView) findViewById(R.id.xindaocanshu_setting);
        kuopinyinziSelect = (Spinner) findViewById(R.id.kuopinyinzi_select);
        kuopinyinziSetting = (RoundTextView) findViewById(R.id.kuopinyinzi_setting);
        editIdStart = (EditText) findViewById(R.id.edit_id_start);
        roundCreateId = (RoundTextView) findViewById(R.id.round_create_id);
        editChangjiabiaoshi = (EditText) findViewById(R.id.edit_changjiabiaoshi);
        editRxdelay = (EditText) findViewById(R.id.edit_rxdelay);
        rxdelaySetting = (RoundTextView) findViewById(R.id.rxdelay_setting);
        maichongSetting = (RoundTextView) findViewById(R.id.maichong_setting);
        systemStatus = (ImageView) findViewById(R.id.system_status);
        textShowHint = (TextView) findViewById(R.id.text_show_hint);
        thirdTextClearWrite = (RoundTextView) findViewById(R.id.third_text_clear_write);
        thirdTextWrite = (RoundTextView) findViewById(R.id.third_text_write);
        thirdEditWrite = (EditText) findViewById(R.id.third_edit_write);
        thirdTextClearRead = (RoundTextView) findViewById(R.id.third_text_clear_read);
        thirdTextRead = (TextView) findViewById(R.id.third_text_read);
        xulieRead = (RoundTextView) findViewById(R.id.xulie_read);
        xulieTextReset = (RoundTextView) findViewById(R.id.xulie_text_reset);
        xulieTextRead = (TextView) findViewById(R.id.xulie_text_read);
        xulieTextClearRead = (RoundTextView) findViewById(R.id.xulie_text_clear_read);
        xulieTextReadThird = (TextView) findViewById(R.id.xulie_text_read_third);
        xulieTextClearAdd = (RoundTextView) findViewById(R.id.xulie_text_clear_add);
        xulieTextGetNetwork = (RoundTextView) findViewById(R.id.xulie_text_get_network);
        xulieTextOneSetting = (RoundTextView) findViewById(R.id.xulie_text_one_setting);
        xulieRecycleview = (RecyclerView) findViewById(R.id.xulie_recycleview);
        editOtherVersion = (EditText) findViewById(R.id.edit_other_version);
        otherVersionSetting = (RoundTextView) findViewById(R.id.other_version_setting);
        editXinhaoqiangduStart = (EditText) findViewById(R.id.edit_xinhaoqiangdu_start);
        editXinhaoqiangduEnd = (EditText) findViewById(R.id.edit_xinhaoqiangdu_end);
        editXinzaobiStart = (EditText) findViewById(R.id.edit_xinzaobi_start);
        editXinzaobiEnd = (EditText) findViewById(R.id.edit_xinzaobi_end);
        textHuoqu = (TextView) findViewById(R.id.text_huoqu);
        buttonQiangzhifasong = (RoundTextView) findViewById(R.id.button_qiangzhifasong);
        buttonDuquXinhao = (RoundTextView) findViewById(R.id.button_duqu_xinhao);
        textHuoquNew = (TextView) findViewById(R.id.text_huoqu_new);
        buttonDuquNew = (RoundTextView) findViewById(R.id.button_duqu_new);
        roundTextDuqu = (RoundTextView) findViewById(R.id.round_text_duqu);
        textDuquInfo = (TextView) findViewById(R.id.text_duqu_info);
        roundTextXiumianJihuo = (RoundTextView) findViewById(R.id.round_text_xiumian_jihuo);
        roundTextXiumian = (RoundTextView) findViewById(R.id.round_text_xiumian);
        roundTextSaomiao = (RoundTextView) findViewById(R.id.round_text_saomiao);
        linearBlueTooth = (LinearLayout) findViewById(R.id.linear_blue_tooth);
        editSearch = (EditText) findViewById(R.id.edit_search);
        resetClear = (RoundTextView) findViewById(R.id.reset_clear);
        resetClearList = (RoundTextView) findViewById(R.id.reset_clear_list);
        dialogBlueRecycle = (RecyclerView) findViewById(R.id.dialog_blue_recycle);





        hashMap = new HashMap<>();
        stringBufferThird =new StringBuffer();
        kuopinyinziArray = getResources().getStringArray(R.array.m_chip_kuopinyinzi);
        editIdStart.setText(LouShanYunUtils.getTimeID());
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
            deviceArrayList.clear();
            multiTypeAdapterBlue.notifyDataSetChanged();
        });
        initTestClick();
        sAtInstructRealizeFactory = new SAtInstructRealizeFactory();

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(FourTestActivity.this, R.layout.l_loushanyun_item_spinner, sAtInstructRealizeFactory.getAllMoShi());
        xindaocanshuSelect.setAdapter(arrayAdapter);
    }


    private void clearAllText() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textDuquInfo.setText("");
                textHuoqu.setText("");
            }
        });

    }

    private void initTestClick() {
        xindaocanshuSetting.setOnClickListener(this::OnBlueToothClick);
        kuopinyinziSetting.setOnClickListener(this::OnBlueToothClick);
        maichongSetting.setOnClickListener(this::OnBlueToothClick);
        roundTextDuqu.setOnClickListener(this::OnBlueToothClick);
        buttonDuquXinhao.setOnClickListener(this::OnBlueToothClick);
        fasonggonglvSetting.setOnClickListener(this::OnBlueToothClick);
        buttonQiangzhifasong.setOnClickListener(this::OnBlueToothClick);
        roundTextXiumian.setOnClickListener(this::OnBlueToothClick);
        systemStatus.setOnClickListener(this::OnBlueToothClick);
        roundTextXiumianJihuo.setOnClickListener(this::OnBlueToothClick);
        rxdelaySetting.setOnClickListener(this::OnBlueToothClick);

        thirdTextClearRead.setOnClickListener(this::OnBlueToothClick);
        thirdTextClearWrite.setOnClickListener(this::OnBlueToothClick);
        thirdTextRead.setOnClickListener(this::OnBlueToothClick);
        thirdTextWrite.setOnClickListener(this::OnBlueToothClick);

        roundTextSaomiao.setOnClickListener(this::OnClick);
        buttonDayin.setOnClickListener(this::OnClick);
        textPrintCoon.setOnClickListener(this::OnClick);
        buttonDuquNew.setOnClickListener(this::OnClick);
        textToken.setOnClickListener(this::OnClick);
    }

    private void initSnAgreement(double hardVersion) {
        sAtInstructRealizeFactory.initHardVersion(hardVersion);
    }

    private byte[] getDuiJieBytes() {
        byte[] result = new byte[15];
        result[0] = 0x68;
        result[1] = 0x0b;
        result[2] = 0x0a;
        result[3] = 0x06;//流量计
        result[4] = 0x01;//水务
        result[5] = 0x00;//物联电池
        result[6] = 0x03;//数字通讯
        result[7] = 0x06;//RS485 一对一
        long isn = Long.parseLong(editIdStart.getText().toString());
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
        if (type != 4) {
            sendMessageToast("识别不是4号模组，请选择正确的模组");
            return;
        }
        writeCode = 0;
        canPrint = false;
        switch (view.getId()) {
            case R.id.xindaocanshu_setting:
                try {
                    snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructChannel).getFourWriteBytes(xindaocanshuSelect.getSelectedItem().toString().trim()), "设置信道");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                isAllSetting = false;
                break;
            case R.id.round_text_duqu:
                textDuquInfo.setText("");
                isReadAllSuccess = false;
                isAllReading = true;
                try {
                    snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructChannel).getFourReadBytes(), "读取信道中...");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.rxdelay_setting:
                try {
                    snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructRxDelay).getFourWriteBytes(editRxdelay.getText().toString().trim()), "设置RxDelay");
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
            case R.id.round_text_xiumian:
                close();
                break;
            case R.id.fasonggonglv_setting:
                try {
                    snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructSendingPower).getFourWriteBytes(fasonggonglvSelect.getSelectedItem().toString().trim()), "设置发送功率");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                isAllSetting = false;
                break;
            case R.id.kuopinyinzi_setting:
                try {
                    snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructSpreadingFactor).getFourWriteBytes(kuopinyinziSelect.getSelectedItem().toString().trim()), "设置扩频因子");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                isAllSetting = false;
                break;
            case R.id.maichong_setting:
                try {
                    snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructChannel).getFourWriteBytes(xindaocanshuSelect.getSelectedItem().toString().trim()), "设置信道");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                isAllSetting = true;
                clearAllText();
                break;
            case R.id.button_duqu_xinhao:
                try {
                    snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructRSSI).getFourReadBytes(), "读取信号强度");
                    isAllReading = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.round_text_xiumian_jihuo:
                isJiHuoAndXiuMian = true;
                isJiHuo = false;
                break;
            case R.id.button_qiangzhifasong:
                clearAllText();
                try {
                    snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructForcedSend).getFourReadBytes("15527919058")
                            , "强制发送", true, BLUE_SEND_TIME_OUT);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.third_text_write:
                KeyboardUtils.hideSoftInput(this);
                try{
                    String writeStr = thirdEditWrite.getText().toString().replace(" ","").trim();
                    if (XHStringUtil.isEmpty(writeStr, false)) {
                        sendMessageToast("请输入指令");
                        return;
                    }
                    AbSharedUtil.putString(this, "writeStr", writeStr);
                    ArrayList<String> arrayList = new ArrayList<>();
                    arrayList.add("68");
                    arrayList.add("00");
                    arrayList.add("0e");
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
                    arrayList.add("16");
                    byte[] bytes = new byte[arrayList.size()];
                    for (int i = 0; i < arrayList.size(); i++) {
                        int value = Integer.parseInt(arrayList.get(i), 16);
                        bytes[i] = (byte) value;
                        XLog.i("第" + i + "个=" + bytes[i]);
                    }
                    snBlueToothTool.write(bytes, "正在发送");
                }catch (Exception e){
                    sendMessageToast("解析错误");
                }
                break;
            case R.id.third_text_clear_write:
                thirdEditWrite.setText("");
                break;
            case R.id.third_text_clear_read:
                stringBufferThird=new StringBuffer();
                thirdTextRead.setText("");
                break;
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
            case R.id.round_text_saomiao:
                ARouter.getInstance().build(K.SecondUploadInfoActivity).navigation();
                break;
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
                pushEvent(ChipCode.MChipGetNewsInfoRunner, sensoroDeviceChoose.sn, "1", "5");
                break;
            case R.id.text_token:
                String text = sensoroDeviceChoose.sn + ";" + hashMap.get(SAtInstructParams.sAtInstructToken);
                //获取剪贴板管理器
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 创建普通字符型ClipData
                ClipData mClipData = ClipData.newPlainText("Label", text);
                // 将ClipData内容放到系统剪贴板里。
                cm.setPrimaryClip(mClipData);
                sendMessageToast("已复制到剪切板");
                break;
        }
    }

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        super.onInitAttribute(ba);
        ba.mTitleText = "4号模组新程序";
    }

    @Override
    public void onChildConnectFailed(int i) {

    }

    @Override
    public void onChildConnectSuccess() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textDuquInfo.setText("");
                textHuoqu.setText("");
                textDangqianSn.setText("当前连接的SN设备:" + sensoroDeviceChoose.getSerialNumber().toUpperCase());
                try {
                    snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sInstructDiscern).getFourWriteBytes(), "识别模块中...", true, 6000);
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
                            snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructSendingPower).getFourReadBytes(), "读取发送功率");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (resultAt.equals(SAtInstructParams.sAtInstructChannel + "-W")) {
                    try {
                        sendMessageToast("信道设置成功");
                        if (isAllSetting) {
                            try {
                                snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructSendingPower).getFourWriteBytes(fasonggonglvSelect.getSelectedItem().toString().trim()), "设置发送功率");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (resultAt.equals(SAtInstructParams.sAtInstructSendingPower + "-R")) {
                    try {
                        hashMap.putAll(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructSendingPower).parseTwoRNotifyBytes(result));
                        if (isAllReading) {
                            snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructFirmwareVersion).getFourReadBytes(), "读取固件版本");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (resultAt.equals(SAtInstructParams.sAtInstructSendingPower + "-W")) {
                    try {
                        sendMessageToast("设置发送功率成功");
                        if (isAllSetting) {
                            try {
                                snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructSpreadingFactor).getFourWriteBytes(kuopinyinziSelect.getSelectedItem().toString().trim()), "设置扩频因子");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (resultAt.equals(SAtInstructParams.sAtInstructFirmwareVersion + "-R")) {
                    try {
                        hashMap.putAll(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructFirmwareVersion).parseTwoRNotifyBytes(result));
                        if (isAllReading) {
                            snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructSpreadingFactor).getFourReadBytes(), "读取扩频因子");
                        }
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
                            pushEventNoProgress(ChipCode.MChipGetNewsInfoRunner, sensoroDeviceChoose.sn, "1", "5");
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (resultAt.equals(SAtInstructParams.sAtInstructSpreadingFactor + "-R")) {
                    try {
                        hashMap.putAll(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructSpreadingFactor).parseTwoRNotifyBytes(result));
                        if (isAllReading) {
                            snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructRxDelay).getFourReadBytes(), "读取RxDelay");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (resultAt.equals(SAtInstructParams.sAtInstructSpreadingFactor + "-W")) {
                    try {
                        sendMessageToast("设置扩频因子成功");
                        if (isAllSetting) {
                            byte[] input2 = {0x68, 0x02, 0x00, 0x40, 0x42, 0x16};
                            snBlueToothTool.write(input2, "设置采集场景中...");
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
                } else if (resultAt.equals(SAtInstructParams.sAtInstructRxDelay + "-R")) {
                    try {
                        hashMap.putAll(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructRxDelay).parseTwoRNotifyBytes(result));
                        if (isAllReading) {
                            try {
                                isReadAllSuccess = true;
                                snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sInstructDiscern).getFourWriteBytes(), "读取中...", true, 6000);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (resultAt.equals(SAtInstructParams.sAtInstructToken + "-R")) {
                    try {
                        hashMap.putAll(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructToken).parseFourRNotifyBytes(result));
                        isAllSetting = false;
                        isAllReading = true;
                        try {
                            snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructChannel).getFourReadBytes(), "读取信道中...");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                textToken.setText("当前设备Token和SN(点击复制到剪贴板):\n" + "SN:" + sensoroDeviceChoose.sn + "\nToken:" + hashMap.get(SAtInstructParams.sAtInstructToken));
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    sendMessageToast(resultAt);
                }
                break;
            case 0x00:
                if (result[3] == 0) {
                    if (isAllSetting) {
                        byte[] bytes = {0x68, 0x03, 0x07, 0x04, 0x0e, 0x16};
                        snBlueToothTool.write(bytes, "设置发送频率，5分钟");
                    }
                } else {
                    sendMessageToast("设置采集场景失败");
                }
                break;
            case 0x03:
                if (result[3] == 0) {
                    if (isAllSetting) {
                        byte[] cmd = {0x68, 0x02, 0x04, 0x01, 0x07, 0x16};
                        snBlueToothTool.write(cmd, "正在设置网络最大发送次数");
                    }
                } else {
                    sendMessageToast("设置出厂日期失败");
                }
                break;
            case 0x04:
                if (result[3] == 0) {
                    if (isAllSetting) {
                        int biaoshi = Integer.valueOf(editChangjiabiaoshi.getText().toString());
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
                    }
                } else {
                    sendMessageToast("设置网络交互失败");
                }
                break;
            case 0x06:
                if (result[3] == 0) {
                    if (isAllSetting) {
                        snBlueToothTool.write(getDuiJieBytes(), "正在对接信息");
                    }
                } else {
                    sendMessageToast("设置厂家标识失败");
                }
                break;
            case 0x0a:
                if (result[3] == 0) {
                    sendMessageToast("设置对接信息成功");
                } else {
                    sendMessageToast("设置对接信息失败");
                }
                break;
            case 0x07:
                if (result[3] == 0) {
                    if (isAllSetting) {
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
                    }
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
                    initSnAgreement(Double.valueOf(LouShanYunUtils.getHardWareVersion(Integer.valueOf("6"))));
                    if (type == 4) {
                        hashMap.putAll(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sInstructDiscern).parseFourRNotifyBytes(result));
                        if (!isReadAllSuccess) {
                            snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructToken).getFourReadBytes(), "读取Token...");
                        } else {
                            setDuQu(hashMap);
                        }
                    } else {
                        sendMessageToast("识别不是4号模组，请选择正确的模组");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    sendMessageToast("识别失败");
                }
                break;
            case 0x0c:
                if (result[3] == 0) {
                    if (isJiHuo) {
                        runOnUiThread(() -> {
                            //已激活
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
            case 0x0e:
                if (result[3] == 0) {
                    stringBufferThird.append(ByteConvertUtils.byteToString(result,4,result.length-1)+"\n");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            thirdTextRead.setText(stringBufferThird);
                        }
                    });
                }else {
                    sendMessageToast("发送第三方命令失败");
                }
                break;
            default:
                sendMessageToast("命令与模块不对应，请重新选择模块");
        }
    }


    private void close() {
        snBlueToothTool.write(new byte[]{(byte) 0x68, (byte) 0x02, (byte) 0x25, (byte) 0x00, (byte) 0x27, (byte) 0x16}, "正在让设备进入休眠状态");
    }


    private void setDuQu(HashMap<String, String> hashMap) {
        StringBuilder sb = new StringBuilder();
        sb.append("发送功率:  ");
        sb.append(hashMap.get(SAtInstructParams.sAtInstructSendingPower));
        sb.append("\nRxDelay:  ");
        sb.append(hashMap.get(SAtInstructParams.sAtInstructRxDelay) + "s");
        sb.append("\n发送频率:  ");
        sb.append(LouShanYunUtils.getFourFSPLReadStringByString(hashMap.get(MapParams.发送频率)));
        sb.append("\n信道参数:  ");
        sb.append(hashMap.get(SAtInstructParams.sAtInstructChannel));
        sb.append("\n扩频因子:  ");
        sb.append(hashMap.get(SAtInstructParams.sAtInstructSpreadingFactor));
        sb.append("\n厂家标识:  ");
        sb.append(hashMap.get(MapParams.厂家标识));
        sb.append("\n出厂时间:  ");
        sb.append("20" + hashMap.get(MapParams.出厂时间_年) + "-" + hashMap.get(MapParams.出厂时间_月) + "-" + hashMap.get(MapParams.出厂时间_日));
        sb.append("\n设备ID:  ");
        sb.append(hashMap.get(MapParams.设备ID));
        sb.append("\n采集场景:  ");
        sb.append(LouShanYunUtils.getCJCJReadStringByCode(hashMap.get(MapParams.采集场景)));
        sb.append("\n产品形式:  ");
        sb.append(LouShanYunUtils.getCPXSReadStringByCode(hashMap.get(MapParams.产品形式)));
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
        sb.append("\nLSY-IOT版本号:  ");
        sb.append(hashMap.get(SAtInstructParams.sAtInstructFirmwareVersion));
        runOnUiThread(() -> {
            if ("0".equals(hashMap.get(MapParams.激活状态))) {
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
            if (!switchSaveStatus.isChecked()) {
                if (!XHStringUtil.isEmpty(hashMap.get(MapParams.发送功率), false)) {
                    if ("20dbm".equals(hashMap.get(SAtInstructParams.sAtInstructSendingPower))) {
                        fasonggonglvSelect.setSelection(0);
                    } else if ("18dbm".equals(hashMap.get(SAtInstructParams.sAtInstructSendingPower))) {
                        fasonggonglvSelect.setSelection(1);
                    } else if ("16dbm".equals(hashMap.get(SAtInstructParams.sAtInstructSendingPower))) {
                        fasonggonglvSelect.setSelection(2);
                    }
                }
                if (!XHStringUtil.isEmpty(hashMap.get(SAtInstructParams.sAtInstructSpreadingFactor), false)) {
                    for (int i = 0; i < kuopinyinziArray.length; i++) {
                        if (kuopinyinziArray[i].equals(hashMap.get(SAtInstructParams.sAtInstructSpreadingFactor))) {
                            kuopinyinziSelect.setSelection(i);
                        }
                    }
                }
                if (!XHStringUtil.isEmpty(hashMap.get(SAtInstructParams.sAtInstructChannel), false)) {
                    xindaocanshuSelect.setSelection(sAtInstructRealizeFactory.getMoShiIndex(hashMap.get(SAtInstructParams.sAtInstructChannel)));
                }

            }
            textDuquInfo.setText(sb.toString());
            scrollView.fullScroll(ScrollView.FOCUS_DOWN);
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
        registerEventRunner(ChipCode.MChipGetNewsInfoRunner, new MChipGetNewsInfoRunner());
    }

    @Override
    protected void onEventRunEnd(Event event, int code) {
        if (code == ChipCode.MChipGetNewsInfoRunner) {
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
        }

    }
}
