package wu.loushanyun.com.modulechiptest.v.activity;

import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SwitchCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.elvishew.xlog.XLog;
import com.flyco.roundview.RoundTextView;
import com.gpsdk.demo.DeviceConnFactoryManager;
import com.sensoro.sensor.kit.callback.SensoroDeviceListener;
import com.sensoro.sensor.kit.entity.SensoroDevice;
import com.wu.loushanyun.base.config.K;
import com.wu.loushanyun.base.print.PrintTool;
import com.wu.loushanyun.base.util.DataParser;
import com.wu.loushanyun.base.util.LouShanYunUtils;
import com.wu.loushanyun.base.util.MapParams;
import com.wu.loushanyun.basemvp.init.SensoroBlueManager;
import com.wu.loushanyun.basemvp.v.activity.BaseBlueToothActivity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.dialog.LoadingDialogUtil;
import met.hx.com.base.base.multitype.MultiTypeAdapter;
import met.hx.com.base.baseevent.Event;
import met.hx.com.librarybase.some_utils.KeyboardUtils;
import met.hx.com.librarybase.some_utils.TimeUtils;
import met.hx.com.librarybase.some_utils.ToastUtils;
import met.hx.com.librarybase.some_utils.XHStringUtil;
import wu.loushanyun.com.libraryfive.p.adapter.SNDeviceViewBinder;
import wu.loushanyun.com.modulechiptest.R;
import wu.loushanyun.com.modulechiptest.init.ChipCode;
import wu.loushanyun.com.modulechiptest.m.NewInfo;
import wu.loushanyun.com.modulechiptest.p.runner.MChipDeviceHisDataRunner;
import wu.loushanyun.com.modulechiptest.p.runner.MChipGetNewsInfoRunner;


@Route(path = K.SecondTestActivity)
public class SecondTestActivity extends BaseBlueToothActivity {
    private ArrayList<SensoroDevice> deviceArrayList;


    private ScrollView scrollView;
    private TextView textPrint;
    private EditText editPrintNum;
    private RoundTextView textPrintCoon;
    private TextView textDangqianSn;
    private RoundTextView bluetoothConn;
    private RoundTextView buttonDayin;
    private RoundTextView bluetoothDisconn;
    private TextView textMoshi;
    private SwitchCompat switchSaveStatus;
    private Spinner fasonggonglvSelect;
    private RoundTextView fasonggonglvSetting;
    private Spinner xindaocanshuSelect;
    private RoundTextView xindaocanshuSetting;
    private Spinner kuopinyinziSelect;
    private RoundTextView kuopinyinziSetting;
    private Spinner cgxhSelect;
    private Spinner beilvSelect;
    private EditText editMaichongStart;
    private EditText editIdStart;
    private RoundTextView roundCreateId;
    private EditText editFactoryId;
    private RoundTextView maichongSetting;
    private ImageView systemStatus;
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

    private SNDeviceViewBinder snDeviceViewBinder;
    private MultiTypeAdapter multiTypeAdapterBlue;
    private SensoroDevice sensoroDeviceChoose;
    private int writeCode;

    private HashMap<String, String> hashMap;
    private int type = 0;
    private boolean canPrint = false;

    //默认参数
    private byte chaiXie = 0;//拆卸
    private byte qiangCi = 0;//强磁
    private byte chuanGanQiZhuangTai = 0;//传感器状态
    private byte daoLiu = 0;//倒流
    private byte faMenZhuangTai = 0;//阀门状态
    private byte faSongPinLv = (byte) 0x30;//发送频率   默认24小时
    private boolean isAllSetting;
    private boolean isNewID = true;//每次设置完成都让其生成一个新的id
    private boolean isJiHuo = true;
    private boolean isJiHuoAndXiuMian = false;

    private PrintTool printTool;


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
        snDeviceViewBinder = new SNDeviceViewBinder(sensoroDevice -> {
            this.sensoroDeviceChoose = sensoroDevice;
            linearBlueTooth.setVisibility(View.GONE);
            connectBlueTooth(sensoroDevice);
            clearAllText();
            KeyboardUtils.hideSoftInput(this);
        });
    }

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        ba.mActivityLayoutId = R.layout.m_chip_activity_second;
        ba.mTitleText = "2号模组测试";
        ba.mTitleRightText = "附近蓝牙";
        ba.mTitleLeftRightWidth = 100;
    }

    @Override
    public void onRightClick(View item) {
        linearBlueTooth.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onChildConnectFailed(int i) {

    }

    @Override
    protected void onChildConnectSuccess() {
        runOnUiThread(() -> {
            textMoshi.setText("当前设备信道：未读到");
            textDuquInfo.setText("");
            textHuoqu.setText("");
            this.sensoroDevice = sensoroDeviceChoose;
            textDangqianSn.setText("当前连接的SN设备:" + sensoroDevice.getSerialNumber().toUpperCase());
            write(DataParser.CMD_INFO_ALL, "识别模块中...", true, 6000);
            int printNum = Integer.valueOf(editPrintNum.getText().toString());
            for (int i = 0; i < printNum; i++) {
                printTool.printBitmap(printTool.createChipPrint2(sensoroDevice.sn));
            }
        });
    }

    private void close() {
        write(new byte[]{(byte) 0x68, (byte) 0x02, (byte) 0x25, (byte) 0x00, (byte) 0x27, (byte) 0x16}, "正在让设备进入休眠状态");
    }

    @Override
    protected void onChildNotify(byte[] result) {
        byte code = (byte) (result[2] ^ ((byte) 0x80));
        XLog.i("蓝牙code=" + code);
        switch (code) {
            case 0x05:
                if (result[3] == 0) {
                    sendMessageToast("设置成功");
                    readXinDao();
                } else {
                    sendMessageToast("设置失败");
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
                    if (type == 2) {
                        write(DataParser.CMD_METER_INFO, "读取中...", true, 6000);
                    } else {
                        sendMessageToast("识别不是2号模组，请选择正确的模组");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    sendMessageToast("识别失败");
                }
                break;
            case 0x14:
                if (result[3] == 0) {
                    clearAllText();
                    if (result[4] == 1) {
                        runOnUiThread(() -> {
                            textMoshi.setText("当前设备模式：模式B");
                            xindaocanshuSelect.setSelection(1);
                        });
                    } else if (result[4] == 0) {
                        runOnUiThread(() -> {
                            textMoshi.setText("当前设备模式：模式A");
                            xindaocanshuSelect.setSelection(0);
                        });
                    } else if (result[4] == 0x11) {
                        runOnUiThread(() -> {
                            textMoshi.setText("当前设备模式：模式16");
                            xindaocanshuSelect.setSelection(2);
                        });
                    } else if (result[4] == 0x21) {
                        runOnUiThread(() -> {
                            textMoshi.setText("当前设备模式：模式32");
                            xindaocanshuSelect.setSelection(3);
                        });
                    } else if (result[4] == 0x31) {
                        runOnUiThread(() -> {
                            textMoshi.setText("当前设备模式：模式48");
                            xindaocanshuSelect.setSelection(4);
                        });
                    }
                } else {
                    sendMessageToast("读取失败");
                }
                break;
            case 0x40:
                if (result[3] == 0) {
                    LoadingDialogUtil.showByEvent(true, 8000, "正在读取", loadingTag);
                } else {
                }
                break;
            case 0x04:
                if (result[3] == 0) {
                    sendMessageToast("设置成功");
                } else {
                    sendMessageToast("设置失败");
                }
                break;
            case 0x20:
                if (result[3] == 0) {
                    setDefaultParams();
                }
                break;
            case 0x02:
                if (result[3] == 0) {
                    sendMessageToast("设置发送功率成功");
                    if (isAllSetting) {
                        setDefaultFactoryParams();
                    }

                }
                break;
            case 0x21:
                if (result[3] == 0) {
                    sendMessageToast("设置成功");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            editIdStart.setText(LouShanYunUtils.getTimeID());
                            write(DataParser.CMD_METER_INFO, "读取中...", true, 6000);
                        }
                    });
                } else {
                    sendMessageToast("设置失败");
                }
                break;
            case 0x12:
                if (result[3] == 0) {
                    int rssi = result[4];
                    int snr = result[5];
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
//                        SpannableString ss = new SpannableString(sb);
//                        ForegroundColorSpan colorSpan = new ForegroundColorSpan(0xff941e23);
//                        ss.setSpan(colorSpan, 3, sensoroDevice.getSerialNumber().length()+3, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                        textHuoqu.setText(sb);
                        pushEventNoProgress(ChipCode.MChipGetNewsInfoRunner, sensoroDevice.sn, "1", "5");
                    });
                }

                break;
            case 0x22:
                hashMap = DataParser.getMoudleNo2(result);
                setDuQu(hashMap);
                byte[] d = {0x68, 0x01, 0x15, 0x16, 0x16};
                write(d, "读取发送功率");
                break;
            case 0x15:
                int gonglv = result[4] & 0xff;
                hashMap.put(MapParams.发送功率, String.valueOf(gonglv));
                setDuQu(hashMap);
                byte[] d1 = {0x68, 0x01, 0x16, 0x17, 0x16};
                write(d1, "读取固件版本号");
                break;
            case 0x16:
                String banben = DataParser.getASCIIbyByte(result);
                hashMap.put(MapParams.固件版本号, banben);
                write(DataParser.CMD_SYSTEM_STATUS, "正在读取设备激活状态");
                setDuQu(hashMap);
                break;
            case 0x26:
                if (result[3] == 0) {
                    if ("1".equals(String.valueOf(result[4] & 0xff))) {
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
                } else {
                    runOnUiThread(() -> systemStatus.setVisibility(View.GONE));
                }
                break;
            case 0x24:
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
            case 0x27:
                if (result[3] == 0) {
                    runOnUiThread(() -> textHuoqu.setText("强制发送成功"));
                    sendMessageToast("强制发送成功");
                    write(DataParser.CMD_RSSI_SNR, "读取信号强度和信噪比");
                } else {
                    runOnUiThread(() -> textHuoqu.setText("强制发送失败(反馈)"));
                    sendMessageToast("强制发送失败(反馈)");
                }
                break;
            default:
                sendMessageToast("命令与模块不对应，请重新选择模块");
        }

    }

    @Override
    protected void onLoadingDismissTimeOut() {
        if (writeCode == 0x27) {
            runOnUiThread(() -> textHuoqu.setText("强制发送失败(超时)"));
        } else if (writeCode == 0x40) {
            runOnUiThread(() -> textHuoqu.setText("强制发送失败(超时)"));
        } else {
            super.onLoadingDismissTimeOut();
        }
    }

    private void setDuQu(HashMap<String, String> hashMap) {
        StringBuilder sb = new StringBuilder();
        sb.append("发送功率:  ");
        if (!XHStringUtil.isEmpty(hashMap.get(MapParams.发送功率), false)) {
            sb.append(hashMap.get(MapParams.发送功率) + "dbm");
        }
        sb.append("\n设备ID:  ");
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
        sb.append("\n信道参数:  ");
        if ("0".equalsIgnoreCase(hashMap.get(MapParams.信道参数))) {
            sb.append("模式A");
        } else if ("1".equalsIgnoreCase(hashMap.get(MapParams.信道参数))) {
            sb.append("模式B");
        } else if ("17".equalsIgnoreCase(hashMap.get(MapParams.信道参数))) {
            sb.append("模式16");
        } else if ("33".equalsIgnoreCase(hashMap.get(MapParams.信道参数))) {
            sb.append("模式32");
        } else if ("49".equalsIgnoreCase(hashMap.get(MapParams.信道参数))) {
            sb.append("模式48");
        }
        sb.append("\n扩频因子:  ");
        sb.append(LouShanYunUtils.getKPYZReadStringByCode(hashMap.get(MapParams.扩频因子)));
        sb.append("\n厂家标识:  ");
        sb.append(hashMap.get(MapParams.厂家标识));
        sb.append("\n硬件版本:  ");
        sb.append(LouShanYunUtils.getHardWareVersion(Integer.valueOf(hashMap.get(MapParams.硬件版本))));
        sb.append("\n软件版本:  ");
        sb.append(LouShanYunUtils.getSoftWareVersion(Integer.valueOf(hashMap.get(MapParams.软件版本))));
        sb.append("\nLSY-IOT版本号:  ");
        sb.append(hashMap.get(MapParams.固件版本号));
        sb.append("\n表拆卸状态:  ");
        sb.append(hashMap.get(MapParams.表拆卸状态).equals("0") ? "无" : "有");
        sb.append("\n表强磁状态:  ");
        sb.append(hashMap.get(MapParams.表强磁状态).equals("0") ? "无" : "有");
        sb.append("\n表流向状态:  ");
        sb.append(hashMap.get(MapParams.表流向状态).equals("0") ? "正流" : "倒流");
        sb.append("\n表电池状态:  ");
        sb.append(hashMap.get(MapParams.表电池状态).equals("0") ? "正常" : "欠压");
        runOnUiThread(() -> {
            if (!switchSaveStatus.isChecked()) {
                if (!XHStringUtil.isEmpty(hashMap.get(MapParams.发送功率), false)) {
                    if ("20".equals(hashMap.get(MapParams.发送功率))) {
                        fasonggonglvSelect.setSelection(0);
                    } else if ("18".equals(hashMap.get(MapParams.发送功率))) {
                        fasonggonglvSelect.setSelection(1);
                    } else if ("16".equals(hashMap.get(MapParams.发送功率))) {
                        fasonggonglvSelect.setSelection(2);
                    }
                }
                if (!XHStringUtil.isEmpty(hashMap.get(MapParams.传感信号), false)) {
                    if ("3".equals(hashMap.get(MapParams.传感信号))) {
                        cgxhSelect.setSelection(0);
                    } else if ("1".equals(hashMap.get(MapParams.传感信号))) {
                        cgxhSelect.setSelection(1);
                    } else if ("10".equals(hashMap.get(MapParams.传感信号))) {
                        cgxhSelect.setSelection(2);
                    }
                }
                if (!XHStringUtil.isEmpty(hashMap.get(MapParams.扩频因子), false)) {
                    if ("0".equals(hashMap.get(MapParams.扩频因子))) {
                        kuopinyinziSelect.setSelection(0);
                    } else if ("1".equals(hashMap.get(MapParams.扩频因子))) {
                        kuopinyinziSelect.setSelection(1);
                    } else if ("2".equals(hashMap.get(MapParams.扩频因子))) {
                        kuopinyinziSelect.setSelection(2);
                    } else if ("3".equals(hashMap.get(MapParams.扩频因子))) {
                        kuopinyinziSelect.setSelection(3);
                    } else if ("4".equals(hashMap.get(MapParams.扩频因子))) {
                        kuopinyinziSelect.setSelection(4);
                    } else if ("5".equals(hashMap.get(MapParams.扩频因子))) {
                        kuopinyinziSelect.setSelection(5);
                    }
                }
                if (!XHStringUtil.isEmpty(hashMap.get(MapParams.倍率), false)) {
                    if ("0".equals(hashMap.get(MapParams.倍率))) {
                        beilvSelect.setSelection(0);
                    } else if ("1".equals(hashMap.get(MapParams.倍率))) {
                        beilvSelect.setSelection(1);
                    } else if ("2".equals(hashMap.get(MapParams.倍率))) {
                        beilvSelect.setSelection(2);
                    } else if ("3".equals(hashMap.get(MapParams.倍率))) {
                        beilvSelect.setSelection(3);
                    } else if ("4".equals(hashMap.get(MapParams.倍率))) {
                        beilvSelect.setSelection(4);
                    } else if ("5".equals(hashMap.get(MapParams.倍率))) {
                        beilvSelect.setSelection(5);
                    }
                }

                if ("0".equalsIgnoreCase(hashMap.get(MapParams.信道参数))) {
                    xindaocanshuSelect.setSelection(0);
                    textMoshi.setText("当前设备模式：模式A");
                } else if ("1".equalsIgnoreCase(hashMap.get(MapParams.信道参数))) {
                    xindaocanshuSelect.setSelection(1);
                    textMoshi.setText("当前设备模式：模式B");
                } else if ("17".equalsIgnoreCase(hashMap.get(MapParams.信道参数))) {
                    xindaocanshuSelect.setSelection(2);
                    textMoshi.setText("当前设备模式：模式16");
                } else if ("33".equalsIgnoreCase(hashMap.get(MapParams.信道参数))) {
                    xindaocanshuSelect.setSelection(3);
                    textMoshi.setText("当前设备模式：模式32");
                } else if ("49".equalsIgnoreCase(hashMap.get(MapParams.信道参数))) {
                    xindaocanshuSelect.setSelection(4);
                    textMoshi.setText("当前设备模式：模式48");
                }
                editIdStart.setText(hashMap.get(MapParams.设备ID));
            }
            textDuquInfo.setText(sb.toString());
            scrollView.fullScroll(ScrollView.FOCUS_DOWN);
        });
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

    private void readXinDao() {
        write(DataParser.CMD_CHANNEL, "读取中..");
    }

    @Override
    protected void onChildWriteSuccess() {

    }

    @Override
    protected void onChildWriteFailure(int i) {

    }

    @Override
    protected void initEventListener() {
        registerEventRunner(ChipCode.MChipDeviceHisDataRunner, new MChipDeviceHisDataRunner());
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
        textMoshi = (TextView) findViewById(R.id.text_moshi);
        switchSaveStatus = (SwitchCompat) findViewById(R.id.switch_save_status);
        fasonggonglvSelect = (Spinner) findViewById(R.id.fasonggonglv_select);
        fasonggonglvSetting = (RoundTextView) findViewById(R.id.fasonggonglv_setting);
        xindaocanshuSelect = (Spinner) findViewById(R.id.xindaocanshu_select);
        xindaocanshuSetting = (RoundTextView) findViewById(R.id.xindaocanshu_setting);
        kuopinyinziSelect = (Spinner) findViewById(R.id.kuopinyinzi_select);
        kuopinyinziSetting = (RoundTextView) findViewById(R.id.kuopinyinzi_setting);
        cgxhSelect = (Spinner) findViewById(R.id.cgxh_select);
        beilvSelect = (Spinner) findViewById(R.id.beilv_select);
        editMaichongStart = (EditText) findViewById(R.id.edit_maichong_start);
        editIdStart = (EditText) findViewById(R.id.edit_id_start);
        roundCreateId = (RoundTextView) findViewById(R.id.round_create_id);
        editFactoryId = (EditText) findViewById(R.id.edit_factory_id);
        maichongSetting = (RoundTextView) findViewById(R.id.maichong_setting);
        systemStatus = (ImageView) findViewById(R.id.system_status);
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


        initBlueList();
        editIdStart.setText(LouShanYunUtils.getTimeID());
        cgxhSelect.setSelection(2);
        bluetoothConn.setOnClickListener(v -> {
            if (textDangqianSn.getText().toString().contains("当前连接的SN设备")) {
                sendMessageToast("当前已连接设备");
                return;
            }
            connectBlueTooth(sensoroDevice);
            clearAllText();
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
        cgxhSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String data = (String) cgxhSelect.getItemAtPosition(position);//从spinner中获取被选择的数据
                //绑定值
                if (data.equals("3EV")) {
//                    qiangCi = 1;
//                    daoLiu = 1;
                } else if (data.equals("无磁正反脉冲")) {
//                    qiangCi = 0;
//                    daoLiu = 1;
                    editXinhaoqiangduStart.setText("-90");
                } else if (data.equals("2EV")) {
//                    qiangCi = 1;
//                    daoLiu = 0;
                    editXinhaoqiangduStart.setText("-80");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        initTestClick();
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
        roundCreateId.setOnClickListener(this::OnBlueToothClick);
        systemStatus.setOnClickListener(this::OnBlueToothClick);
        roundTextXiumianJihuo.setOnClickListener(this::OnBlueToothClick);
        roundTextSaomiao.setOnClickListener(this::OnClick);
        buttonDayin.setOnClickListener(this::OnClick);
        textPrintCoon.setOnClickListener(this::OnClick);
        buttonDuquNew.setOnClickListener(this::OnClick);
    }

    private void OnClick(View view) {
        switch (view.getId()) {
            case R.id.round_text_saomiao:
                ARouter.getInstance().build(K.SecondUploadInfoActivity).navigation();
                break;
            case R.id.button_dayin:
                if (!XHStringUtil.isEmpty(sensoroDevice.sn, false)) {
                    LoadingDialogUtil.showByEvent("正在打印", this.loadingTag);
                    printTool.printBitmap(printTool.createChipPrint2(sensoroDevice.sn));
                    LoadingDialogUtil.dismissByEvent(this.loadingTag);
                }
                break;
            case R.id.text_print_coon:
                printTool.showPrintDialog();
                break;
            case R.id.button_duqu_new:
                pushEvent(ChipCode.MChipGetNewsInfoRunner, sensoroDevice.sn, "1", "5");
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
        if (type != 2) {
            sendMessageToast("识别不是2号模组，请选择正确的模组");
            return;
        }
        writeCode = 0;
        canPrint = false;
        switch (view.getId()) {
            case R.id.xindaocanshu_setting:
                int model =
                        LouShanYunUtils.getXDCSWriteStringByCode02(xindaocanshuSelect.getSelectedItem().toString().trim());
                byte[] input = {0x68, 0x02, 0x05, (byte) model, (byte) (0x07 + model), 0x16};
                write(input, "设置信道中...");
                break;
            case R.id.round_text_duqu:
                textDuquInfo.setText("");
                write(DataParser.CMD_METER_INFO, "读取中...", true, 2000);
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
            case R.id.round_create_id:
                editIdStart.setText(LouShanYunUtils.getTimeID());
                break;
            case R.id.fasonggonglv_setting:
                int xinhaoqiangduData = Integer.parseInt(getResources().getStringArray(
                        R.array.m_chip_xinhaoqiangdu)[fasonggonglvSelect
                        .getSelectedItemPosition()].replaceAll("dbm", ""));
                byte[] d = {0x68, 0x02, 0x02, (byte) xinhaoqiangduData, (byte) (0x04 + (byte) xinhaoqiangduData), 0x16};
                write(d, "设置发送功率");
                isAllSetting = false;
                break;
            case R.id.kuopinyinzi_setting:
                int kpyz = LouShanYunUtils.getKPYZWriteCodeByString(String.valueOf(kuopinyinziSelect.getSelectedItem()));
                byte[] input1 = {0x68, 0x02, 0x04, (byte) kpyz, (byte) (0x06 + kpyz), 0x16};
                write(input1, "设置扩频因子中...");
                break;
            case R.id.maichong_setting:
                if (XHStringUtil.isEmpty(editMaichongStart.getText().toString(), false)) {
                    sendMessageToast("请填入脉冲数");
                    return;
                }
                if (XHStringUtil.isEmpty(editIdStart.getText().toString(), false)) {
                    sendMessageToast("请填入设备ID");
                    return;
                }
                int xinhaoqiangduData1 = Integer.parseInt(getResources().getStringArray(
                        R.array.m_chip_xinhaoqiangdu)[fasonggonglvSelect
                        .getSelectedItemPosition()].replaceAll("dbm", ""));
                byte[] d2 = {0x68, 0x02, 0x02, (byte) xinhaoqiangduData1, (byte) (0x04 + (byte) xinhaoqiangduData1), 0x16};
                write(d2, "设置发送功率");
                isAllSetting = true;
                canPrint = true;
                clearAllText();
                break;
            case R.id.button_duqu_xinhao:
                write(DataParser.CMD_RSSI_SNR, "读取信号强度和信噪比");
                break;
            case R.id.round_text_xiumian_jihuo:
                isJiHuoAndXiuMian = true;
                isJiHuo = false;
                openJiHuo();
                break;
            case R.id.button_qiangzhifasong:
                clearAllText();
                writeCode = 0x27;
                String phone = "15527919058";
                String ph1 = phone.substring(0, 1);
                String ph2 = phone.substring(1, 2);
                String ph3 = phone.substring(2, 3);
                String ph4 = phone.substring(3, 4);
                String ph5 = phone.substring(4, 5);
                String ph6 = phone.substring(5, 6);
                String ph7 = phone.substring(6, 7);
                String ph8 = phone.substring(7, 8);
                String ph9 = phone.substring(8, 9);
                String ph10 = phone.substring(9, 10);
                String ph11 = phone.substring(10, 11);
                byte[] d1 = new byte[16];
                d1[0] = (byte) 0x68;
                d1[1] = (byte) 0x0c;//有效数据
                d1[2] = (byte) 0x27;//命令
                d1[3] = (byte) Integer.parseInt(ph1);
                d1[4] = (byte) Integer.parseInt(ph2);
                d1[5] = (byte) Integer.parseInt(ph3);
                d1[6] = (byte) Integer.parseInt(ph4);
                d1[7] = (byte) Integer.parseInt(ph5);
                d1[8] = (byte) Integer.parseInt(ph6);
                d1[9] = (byte) Integer.parseInt(ph7);
                d1[10] = (byte) Integer.parseInt(ph8);
                d1[11] = (byte) Integer.parseInt(ph9);
                d1[12] = (byte) Integer.parseInt(ph10);
                d1[13] = (byte) Integer.parseInt(ph11);
                byte cs = 0;
                for (int i = 1; i < 14; i++) {
                    cs += d1[i];
                }
                d1[14] = cs;//校验和
                d1[15] = (byte) 0x16;
                if (sensoroDeviceSession != null) {
                    write(d1, "正在发送命令，请稍后……", true, BLUE_SEND_TIME_OUT);
                }
                break;
//            case R.id.button_huoqu:
//                clearAllText();
//                textHuoqu.setText("");
//                try {
//                    LoadingDialogUtil.show(loadingProgress, "连接服务器中，请稍后……");
//                    String url = URLUtils.getSocketIP() + "/1/" + sensoroDevice.getSerialNumber().toUpperCase();
//                    client = new WebSocketClient(new URI(url), new Draft_17()) {
//
//                        @Override
//                        public void onClose(int arg0, String arg1, boolean arg2) {
//                            LoadingDialogUtil.dismissByEvent(loadingTag);
//                        }
//
//                        @Override
//                        public void onError(Exception arg0) {
//                            XLog.e(arg0);
//                            LoadingDialogUtil.dismissByEvent(loadingTag);
//                            sendMessageToast("服务器出错，网络状况不佳");
//                            runOnUiThread(() -> client.close());
//                        }
//
//                        @Override
//                        public void onMessage(final String arg0) {
//                            Log.i("yunanhao", "onMessage:" + arg0);
//                            runOnUiThread(() -> textHuoqu.setText("强制发送成功"));
//                            sendMessageToast("强制发送成功");
//                            client.close();
////                            handler.sendEmptyMessageDelayed(100,1000);
//                        }
//
//                        @Override
//                        public void onOpen(ServerHandshake arg0) {
//                            String phone = "15527919058";
//                            String regex = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[013678])|(18[0,5-9]))\\d{8}$";
//                            Pattern p = Pattern.compile(regex);
//                            Matcher m = p.matcher(phone);
//                            boolean isMatch = m.matches();
//                            if (isMatch) {
//                                String ph1 = phone.substring(0, 1);
//                                String ph2 = phone.substring(1, 2);
//                                String ph3 = phone.substring(2, 3);
//                                String ph4 = phone.substring(3, 4);
//                                String ph5 = phone.substring(4, 5);
//                                String ph6 = phone.substring(5, 6);
//                                String ph7 = phone.substring(6, 7);
//                                String ph8 = phone.substring(7, 8);
//                                String ph9 = phone.substring(8, 9);
//                                String ph10 = phone.substring(9, 10);
//                                String ph11 = phone.substring(10, 11);
//                                byte[] d = new byte[16];
//                                d[0] = (byte) 0x68;
//                                d[1] = (byte) 0x0c;//有效数据
//                                d[2] = (byte) 0x40;//命令
//                                d[3] = (byte) Integer.parseInt(ph1);
//                                d[4] = (byte) Integer.parseInt(ph2);
//                                d[5] = (byte) Integer.parseInt(ph3);
//                                d[6] = (byte) Integer.parseInt(ph4);
//                                d[7] = (byte) Integer.parseInt(ph5);
//                                d[8] = (byte) Integer.parseInt(ph6);
//                                d[9] = (byte) Integer.parseInt(ph7);
//                                d[10] = (byte) Integer.parseInt(ph8);
//                                d[11] = (byte) Integer.parseInt(ph9);
//                                d[12] = (byte) Integer.parseInt(ph10);
//                                d[13] = (byte) Integer.parseInt(ph11);
//                                byte cs = 0;
//                                for (int i = 1; i < 14; i++) {
//                                    cs += d[i];
//                                }
//                                d[14] = cs;//校验和
//                                d[15] = (byte) 0x16;
//                                if (sensoroDeviceSession != null) {
//                                    write(d, "正在发送命令，请稍后……");
//                                }
//                            } else {
//                                sendMessageToast("请输入正确的手机号");
//                            }
//                        }
//                    };
//                    client.connect();
//                } catch (Exception e) {
//                    Log.i("yunanhao", e.getMessage());
//                }
//                break;
        }
    }


    private void setDefaultFactoryParams() {
//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        if (XHStringUtil.isEmpty(editFactoryId.getText().toString(), false)) {
            ToastUtils.showShort("请输入厂家标识");
            return;
        }
        write(DataParser.getFactoryParams(
                cgxhSelect.getSelectedItem().toString().trim(), chaiXie, qiangCi, chuanGanQiZhuangTai, daoLiu, faMenZhuangTai, faSongPinLv, Integer.valueOf(editFactoryId.getText().toString()),
                kuopinyinziSelect.getSelectedItem().toString().trim(), xindaocanshuSelect.getSelectedItem().toString().trim()

        ), "设置1参数中...");
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
        if (sensoroDeviceSession != null) {
            write(d, "正在停用无线上传");
        }
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
        if (sensoroDeviceSession != null) {
            write(d, "正在激活无线上传");
        }
    }

    private void setDefaultParams() {
        String id = editIdStart.getText().toString().trim();
        byte[] input2 = DataParser.getBiaoXinxiChuShiHuaCMD(
                Long.parseLong(id),
                Long.parseLong(String.valueOf(editMaichongStart.getText())),
                String.valueOf(beilvSelect.getSelectedItem()));
//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        write(input2, "设置2参数中...");
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
    protected SensoroDeviceListener sensoroDeviceListener=new SensoroDeviceListener<SensoroDevice>() {
        @Override
        public void onNewDevice(SensoroDevice sensoroDevice) {
            XLog.i("蓝牙onNewDevice" + sensoroDevice.toString());
            if (!deviceArrayList.contains(sensoroDevice)) {
                deviceArrayList.add(sensoroDevice);
            } else {
                deviceArrayList.set(deviceArrayList.indexOf(sensoroDevice), sensoroDevice);
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    multiTypeAdapterBlue.notifyDataSetChanged();
                }
            });
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
