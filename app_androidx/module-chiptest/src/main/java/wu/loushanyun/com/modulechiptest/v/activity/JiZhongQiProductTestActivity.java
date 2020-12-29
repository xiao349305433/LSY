package wu.loushanyun.com.modulechiptest.v.activity;

import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.elvishew.xlog.XLog;
import com.flyco.roundview.RoundTextView;
import com.gpsdk.demo.DeviceConnFactoryManager;
import com.sensoro.sensor.kit.entity.SensoroDevice;
import com.wu.loushanyun.base.config.K;
import com.wu.loushanyun.base.print.PrintTool;
import com.wu.loushanyun.base.util.DataParser;
import com.wu.loushanyun.base.util.LouShanYunUtils;
import com.wu.loushanyun.base.util.MapParams;
import com.wu.loushanyun.basemvp.m.sat.SAtInstructRealizeFactory;
import com.wu.loushanyun.basemvp.m.sat.util.SAtInstructParams;
import com.wu.loushanyun.basemvp.v.activity.BaseSnBlueToothActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.dialog.LoadingDialogUtil;
import met.hx.com.base.baseevent.Event;
import met.hx.com.librarybase.some_utils.XHStringUtil;
import wu.loushanyun.com.modulechiptest.R;

@Route(path = K.JiZhongQiProductTestActivity)
public class JiZhongQiProductTestActivity extends BaseSnBlueToothActivity {
    private ScrollView scrollView;
    private TextView textPrint;
    private EditText editPrintNum;
    private RoundTextView textPrintCoon;
    private TextView textToken;
    private TextView textDangqianSn;
    private RoundTextView bluetoothConn;
    private RoundTextView buttonDayin;
    private RoundTextView bluetoothDisconn;
    private RoundTextView atOneSetting;
    private TextView textInfo;
    private ImageView systemStatus;

    private PrintTool printTool;
    private SensoroDevice sensoroDeviceChoose;
    private SAtInstructRealizeFactory sAtInstructRealizeFactory;
    private int type;
    private HashMap<String, String> hashMap;
    private boolean isAllSetting;
    private boolean isFirstConnect;
    private int typeJiHuo = 2;//判断底板是否激活 1已激活  0未激活
    private int meterReadStart;
    private int meterReadEnd;

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
    protected int onChildLayout() {
        return R.layout.m_chip_activity_jizhongqi_product;
    }

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        super.onInitAttribute(ba);
        ba.mTitleText = "集中器初始化参数配置";
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
        atOneSetting = (RoundTextView) findViewById(R.id.at_one_setting);
        textInfo = (TextView) findViewById(R.id.text_info);
        systemStatus = (ImageView) findViewById(R.id.system_status);


        sAtInstructRealizeFactory = new SAtInstructRealizeFactory();
        hashMap = new HashMap<>();
        bluetoothConn.setOnClickListener(v -> {
            if (textDangqianSn.getText().toString().contains("当前连接的SN设备")) {
                sendMessageToast("当前已连接设备");
                return;
            }
            snBlueToothTool.connectBlueTooth(sensoroDeviceChoose);
            textInfo.setText("");
        });
        bluetoothDisconn.setOnClickListener(v -> {
            snBlueToothTool.disconnect();
            textDangqianSn.setText("当前未连接SN设备");
        });
        buttonDayin.setOnClickListener(this::OnClick);
        textPrintCoon.setOnClickListener(this::OnClick);
        atOneSetting.setOnClickListener(this::OnBueCllick);
    }

    private void OnBueCllick(View view) {
        if (!snBlueToothTool.isConnected()) {
            sendMessageToast("未连接蓝牙");
            return;
        }
        if (type != 1) {
            sendMessageToast("识别不是1号模组，请选择正确的模组");
            return;
        }
        isAllSetting = false;
        switch (view.getId()) {
            case R.id.at_one_setting:
                oneSetting();
                break;
        }
    }

    private void oneSetting() {
        isAllSetting = true;
        byte[] input2 = {0x68, 0x02, 0x00, 0x40, 0x42, 0x16};
        snBlueToothTool.write(input2, "设置采集场景中...");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textInfo.setText("");
            }
        });

    }

    @Override
    public void onChildConnectFailed(int i) {

    }

    @Override
    protected void onClickBlueListListener(SensoroDevice sensoroDevice) {
        super.onClickBlueListListener(sensoroDevice);
        this.sensoroDeviceChoose = sensoroDevice;
    }

    private void OnClick(View view) {
        switch (view.getId()) {
            case R.id.button_dayin:
                if (!XHStringUtil.isEmpty(sensoroDeviceChoose.sn, false)) {
                    LoadingDialogUtil.showByEvent("正在打印", this.loadingTag);
                    printTool.printBitmap(printTool.createChipPrint1(sensoroDeviceChoose.sn));
                    LoadingDialogUtil.dismissByEvent(this.loadingTag);
                }
                break;
            case R.id.text_print_coon:
                printTool.showPrintDialog();
                break;
        }
    }

    @Override
    public void onChildConnectSuccess() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                isFirstConnect = true;
                textDangqianSn.setText("当前连接的SN设备:" + sensoroDeviceChoose.getSerialNumber().toUpperCase());
                try {
                    snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sInstructDiscern).getFourWriteBytes(), "识别模块中...", true, 6000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                int printNum = Integer.valueOf(editPrintNum.getText().toString());
                for (int i = 0; i < printNum; i++) {
                    printTool.printBitmap(printTool.createChipPrint1(sensoroDeviceChoose.sn));
                }
            }
        });
    }

    @Override
    public void onChildNotify(byte[] result) {
        byte code = (byte) (result[2] ^ ((byte) 0x80));
        XLog.i("蓝牙code=" + code);
        switch (code) {
            //设置基本信息
            case 0x20:
                if (result[3] == 0) {
                    if (isAllSetting) {
                        //起末表号的设置
                        HashMap<String, String> parmas = new HashMap<>();
                        parmas.put(MapParams.总线起止表号_起, String.valueOf(1));
                        parmas.put(MapParams.总线起止表号_止, String.valueOf(64));
                        parmas.put(MapParams.仪表通信号, "0");
                        parmas.put(MapParams.初始化表计状态, "0");
                        parmas.put(MapParams.倍率, "0");
                        parmas.put(MapParams.安装脉冲底数, "0");
                        parmas.put(MapParams.口径, "0");
                        parmas.put(MapParams.发送频率, "48");
                        parmas.put(MapParams.保留字节, "0");
                        parmas.put(MapParams.设备ID, LouShanYunUtils.getTimeID());
                        snBlueToothTool.write(DataParser.getDiBanBiaoJiChuShiHuaCMD(parmas), "设置表号");
                    }

                } else {
                    sendMessageToast("设置失败");
                }
                break;
            //设置附加参数
            case 0x21:
                if (result[3] == 0) {
                    if (isAllSetting) {
                        snBlueToothTool.write(DataParser.getSystemStatusSettingCMD(false), "关闭激活中");
                    }
                } else {
                    sendMessageToast("设置失败");
                }
                break;
            //设置激活停用
            case 0x23:
                if (result[3] == 0) {
                    if (typeJiHuo == 1) {
                        typeJiHuo = 0;
                    } else if (typeJiHuo == 0) {
                        typeJiHuo = 1;
                    }
                    isFirstConnect = false;
                    snBlueToothTool.write(DataParser.CMD_INFO_ALL, "识别模块中...", true, 6000);
                }
                break;
            //读取底板基本信息
            case 0x30:
                if (isAllSetting) {
                    hashMap.putAll(DataParser.getInformationBase(result));
                    snBlueToothTool.write(DataParser.getDiBanBiaoJiDuQuCMD(), "正在读取激活状态");
                }
                break;
            //读取附加参数
            case 0x31:
                hashMap.putAll(DataParser.getDiBanBiaoJiInfo(result));
                try {
                    meterReadStart = Integer.parseInt(hashMap.get(MapParams.总线起止表号_起));
                    meterReadEnd = Integer.parseInt(hashMap.get(MapParams.总线起止表号_止));
                    if (result[3] == 0) {
                        if ("1".equalsIgnoreCase(hashMap.get(MapParams.系统状态))) {
                            typeJiHuo = 1;
                        } else {
                            typeJiHuo = 0;
                        }
                    }
                    setDiBanDuShu();
                    if (isAllSetting) {
                        snBlueToothTool.write(DataParser.getSystemSleepCMD(), "正在休眠");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                snBlueToothTool.setConnected(false);
                                textDangqianSn.setText("当前未连接SN设备");
                            }
                        });
                    }
                } catch (Exception e) {

                }
                break;
            case 0x11:
                try {
                    type = DataParser.getModuleType(result);
                    hashMap.putAll(DataParser.getInformationAll(result));
                    if (type == 1) {
                        if (isFirstConnect) {
                            oneSetting();
                        } else {
                            if (isAllSetting) {
                                setModuleDuQu();
                                snBlueToothTool.write(DataParser.CMD_INFO_BASE, "正在读取底板信息");
                            }
                        }
                    } else {
                        sendMessageToast("识别不是1号模组，请选择正确的模组");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    sendMessageToast("识别失败");
                }
                break;
            case 0x00:
                if (result[3] == 0) {
                    if (isAllSetting) {
                        byte[] bytes = {0x68, 0x02, 0x01, 0x30, 0x33, 0x16};
                        snBlueToothTool.write(bytes, "设置发送频率，24小时");
                    } else {

                    }

                } else {
                    sendMessageToast("设置采集场景失败");
                }
                break;
            case 0x01:
                if (result[3] == 0) {
                    if (isAllSetting) {
                        int xinhaoqiangduData = Integer.parseInt(getResources().getStringArray(
                                R.array.m_chip_xinhaoqiangdu)[0].replaceAll("dbm", ""));
                        snBlueToothTool.write(DataParser.setFaSongGongLv(xinhaoqiangduData), "正在设置发送功率");
                    } else {

                    }

                } else {
                    sendMessageToast("设置发送频率失败");
                }
                break;
            case 0x02:
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
                    } else {
                        sendMessageToast("设置成功");
                    }
                } else {
                    sendMessageToast("设置发送功率失败");
                }
                break;
            case 0x03:
                if (result[3] == 0) {
                    if (isAllSetting) {
                        byte[] cmd = {0x68, 0x02, 0x04, 0x01, 0x07, 0x16};
                        snBlueToothTool.write(cmd, "正在设置网络交互");
                    } else {

                    }

                } else {
                    sendMessageToast("设置出厂日期失败");
                }
                break;
            case 0x04:
                if (result[3] == 0) {
                    if (isAllSetting) {
                        byte[] cmd = {0x68, 0x02, 0x05, 0x01, 0x08, 0x16};
                        snBlueToothTool.write(cmd, "正在设置工作模式");
                    } else {

                    }

                } else {
                    sendMessageToast("设置网络交互失败");
                }
                break;
            case 0x05:
                if (result[3] == 0) {
                    if (isAllSetting) {
                        byte[] d = new byte[7];
                        d[0] = (byte) 0x68;
                        d[1] = (byte) 0x03;//有效数据
                        d[2] = (byte) 0x06;//命令
                        d[3] = (byte) (0 & 0xff);
                        d[4] = (byte) (0 & 0xff00);
                        byte cs = 0;
                        for (int i = 1; i < 5; i++) {
                            cs += d[i];
                        }
                        d[5] = cs;//校验和
                        d[6] = (byte) 0x16;
                        snBlueToothTool.write(d, "正在设置厂家标识");
                    } else {

                    }

                } else {
                    sendMessageToast("设置工作模式失败");
                }
                break;
            case 0x06:
                if (result[3] == 0) {
                    if (isAllSetting) {
                        int kpyz = LouShanYunUtils.getKPYZWriteCodeByString("SF9");
                        byte[] input1 = {0x68, 0x02, 0x07, (byte) kpyz, (byte) (0x09 + kpyz), 0x16};
                        snBlueToothTool.write(input1, "设置扩频因子中...");
                    } else {

                    }

                } else {
                    sendMessageToast("设置厂家标识失败");
                }
                break;
            case 0x07:
                if (result[3] == 0) {
                    if (isAllSetting) {
                        String moshi = "模式A";
                        if (moshi.equals("模式A")) {
                            byte[] d = DataParser.setXinDaoCanShu(true);//模式A
                            snBlueToothTool.write(d, "设置模式中");
                        } else if (moshi.equals("模式B")) {
                            byte[] d = DataParser.setXinDaoCanShu(false);//模式B
                            snBlueToothTool.write(d, "设置模式中");
                        }
                    } else {
                        sendMessageToast("设置成功");
                    }

                } else {
                    sendMessageToast("设置失败");
                }
                break;
            case 0x08:
                if (result[3] == 0) {
                    if (isAllSetting) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        String time = sdf.format(new Date());
                        String[] split = time.split("-");
                        String year = split[0].substring(split[0].length() - 2, split[0].length());
                        String month = split[1].toString();
                        String day = split[2].toString();
                        HashMap<String, String> map = new HashMap<>();
                        map.put(MapParams.设备ID, LouShanYunUtils.getTimeID());
                        map.put(MapParams.采集场景, String.valueOf(0x40));
                        map.put(MapParams.产品形式, String.valueOf(0x04));
                        map.put(MapParams.传感信号, String.valueOf(0x04));
                        map.put(MapParams.保留字节, String.valueOf(0x00));
                        map.put(MapParams.电源类型, String.valueOf(0b10000000));
                        map.put(MapParams.出厂时间_年, year);
                        map.put(MapParams.出厂时间_月, month);
                        map.put(MapParams.出厂时间_日, day);
                        map.put(MapParams.工作模式, String.valueOf(0x01));
                        map.put(MapParams.信号类型, String.valueOf(0x01));
                        map.put(MapParams.参数内容, String.valueOf(0x00));//无此属性
                        map.put(MapParams.脉宽, String.valueOf(0x00));//无此属性
                        map.put(MapParams.压力值标定_初始值, String.valueOf(0x00));//无此属性
                        map.put(MapParams.压力值标定_最大值, String.valueOf(0x00));//无此属性
                        map.put(MapParams.底板状态_设备强磁状态, String.valueOf(0x00));//无此属性
                        map.put(MapParams.底板状态_设备拆卸状态, String.valueOf(0x00));//无此属性
                        map.put(MapParams.底板状态_水表倒流状态, String.valueOf(0x00));//无此属性
                        map.put(MapParams.底板状态_自备电池状态, String.valueOf(0x00));
                        map.put(MapParams.底板状态_第三方电池状态, String.valueOf(0x00));
                        map.put(MapParams.底板状态_外接电源220V状态, String.valueOf(0x00));
                        snBlueToothTool.write(DataParser.getInformationSettingCMD(map), "正在设置参数");
                    }
                } else {
                    sendMessageToast("设置失败");
                }
                break;
            case 0x40:
                if (result[3] == 0) {
                    LoadingDialogUtil.showByEvent(true, 10000, "正在接收", loadingTag);
                }
                break;
            case 0x24:
                if (result[3] == 0) {
                    sendMessageToast("休眠成功");
                } else {
                    sendMessageToast("休眠失败");
                }
                break;
        }

    }

    private void setDiBanDuShu() {
        StringBuilder sb = new StringBuilder();
        sb.append("模组信息如下：");
        sb.append("\n" + textInfo.getText().toString());
        sb.append("\n底板信息如下：");
        sb.append("\n采集场景:  ");
        sb.append(LouShanYunUtils.getCJCJReadStringByCode(hashMap.get(MapParams.采集场景)));
        sb.append("\n产品形式:  ");
        sb.append(LouShanYunUtils.getCPXSReadStringByCode(Long.valueOf(hashMap.get(MapParams.产品形式))));
        sb.append("\n传感信号:  ");
        sb.append(LouShanYunUtils.getCGXHReadStringByCode(Long.valueOf(hashMap.get(MapParams.传感信号))));
        sb.append("\n电源类型:  ");
        sb.append(LouShanYunUtils.getDYLXReadStringByCode(Integer.valueOf(hashMap.get(MapParams.电源类型))));
        sb.append("\n底板出厂时间:  ");
        sb.append("20" + hashMap.get(MapParams.出厂时间_年) + "-" + hashMap.get(MapParams.出厂时间_月) + "-" + hashMap.get(MapParams.出厂时间_日));
        sb.append("\n底板硬件版本:  ");
        sb.append(LouShanYunUtils.getHardWareVersion(Integer.valueOf(hashMap.get(MapParams.硬件版本))));
        sb.append("\n底板软件版本:  ");
        sb.append(LouShanYunUtils.getSoftWareVersion(Integer.valueOf(hashMap.get(MapParams.软件版本))));
        sb.append("\n起始表号:  ");
        sb.append(String.valueOf(meterReadStart));
        sb.append("\n终止表号:  ");
        sb.append(String.valueOf(meterReadEnd));
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textInfo.setText(sb.toString());
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

    private void setModuleDuQu() {
        StringBuilder sb = new StringBuilder();
        sb.append("发送功率:  ");
        if (!XHStringUtil.isEmpty(hashMap.get(MapParams.发送功率), false)) {
            sb.append(hashMap.get(MapParams.发送功率) + "dbm");
        }
        sb.append("\n发送频率:  ");
        sb.append(LouShanYunUtils.getFSPLReadStringByCode(Long.valueOf(hashMap.get(MapParams.发送频率))));
        sb.append("\n网络交互:  ");
        sb.append(LouShanYunUtils.getWLJHReadStringByCode(hashMap.get(MapParams.网络交互)));
        sb.append("\n信道参数:  ");
        sb.append("0".equalsIgnoreCase(hashMap.get(MapParams.信道参数)) ? "信道A" : "信道B");
        sb.append("\n扩频因子:  ");
        sb.append(LouShanYunUtils.getKPYZReadStringByCode(hashMap.get(MapParams.扩频因子)));
        sb.append("\n采集场景:  ");
        sb.append(LouShanYunUtils.getCJCJReadStringByCode(hashMap.get(MapParams.采集场景)));
        sb.append("\n模组硬件版本:  ");
        sb.append(LouShanYunUtils.getHardWareVersion(Integer.valueOf(hashMap.get(MapParams.硬件版本))));
        sb.append("\n模组软件版本:  ");
        sb.append(LouShanYunUtils.getSoftWareVersion(Integer.valueOf(hashMap.get(MapParams.软件版本))));
        sb.append("\n模组出厂时间:  ");
        sb.append("20" + hashMap.get(MapParams.出厂时间_年) + "-" + hashMap.get(MapParams.出厂时间_月) + "-" + hashMap.get(MapParams.出厂时间_日));
        runOnUiThread(() -> {
            textInfo.setText(sb.toString());
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
