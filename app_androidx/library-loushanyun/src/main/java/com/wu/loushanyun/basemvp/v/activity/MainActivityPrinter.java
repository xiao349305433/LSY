package com.wu.loushanyun.basemvp.v.activity;

import android.Manifest;
import android.app.PendingIntent;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Handler;
import android.os.Message;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.gprinter.command.EscCommand;
import com.gprinter.command.LabelCommand;
import com.gpsdk.demo.BluetoothDeviceList;
import com.gpsdk.demo.ConnMoreDevicesActivity;
import com.gpsdk.demo.Constant;
import com.gpsdk.demo.DeviceConnFactoryManager;
import com.gpsdk.demo.PrinterCommand;
import com.gpsdk.demo.SerialPortList;
import com.gpsdk.demo.ThreadFactoryBuilder;
import com.gpsdk.demo.ThreadPool;
import com.gpsdk.demo.UsbDeviceList;
import com.gpsdk.demo.Utils;
import com.gpsdk.demo.WifiParameterConfigDialog;
import com.wu.loushanyun.R;

import java.util.ArrayList;
import java.util.Vector;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.activity.BaseNoPresenterActivity;
import met.hx.com.base.baseevent.Event;

/**
 * Created by Administrator
 *
 * @author 猿史森林
 *         Date: 2017/8/2
 *         Class description:
 */
public class MainActivityPrinter extends BaseNoPresenterActivity {
    private static final String TAG = "MainActivityPrinter";
    public static final String 物联SN = "SN：";
    public static final String 设备ID = "ID：";
    public static final String 传感 = "传感信号：";
    public static final String 出厂时间 = "出厂时间：";
    public static final String 厂家名称 = "厂家名称：";
    ArrayList<String> per = new ArrayList<>();
    private UsbManager usbManager;
    private int counts;
    private static final int REQUEST_CODE = 0x004;

    /**
     * 连接状态断开
     */
    private static final int CONN_STATE_DISCONN = 0x007;
    /**
     * 使用打印机指令错误
     */
    private static final int PRINTER_COMMAND_ERROR = 0x008;


    /**
     * ESC查询打印机实时状态指令
     */
    private byte[] esc = {0x10, 0x04, 0x02};


    /**
     * TSC查询打印机状态指令
     */
    private byte[] tsc = {0x1b, '!', '?'};

    private static final int CONN_MOST_DEVICES = 0x11;
    private static final int CONN_PRINTER = 0x12;
    private PendingIntent mPermissionIntent;
    private String[] permissions = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.BLUETOOTH
    };
    private String usbName;
    private TextView tvConnState;
    private ThreadPool threadPool;
    /**
     * 判断打印机所使用指令是否是ESC指令
     */
    private int id = 0;
    private EditText etPrintCounts;

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        ba.mActivityLayoutId = R.layout.l_loushanyun_activity_printer_main;
        ba.mTitleText = "无线打印";
    }

    @Override
    protected void initView() {
        usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        checkPermission();
        requestPermission();
        tvConnState = (TextView) findViewById(R.id.tv_connState);
        etPrintCounts = (EditText) findViewById(R.id.et_print_counts);
    }

    @Override
    protected void initEventListener() {

    }

    @Override
    protected void onEventRunEnd(Event event, int code) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(Constant.ACTION_USB_PERMISSION);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        filter.addAction(DeviceConnFactoryManager.ACTION_QUERY_PRINTER_STATE);
        filter.addAction(DeviceConnFactoryManager.ACTION_CONN_STATE);
        registerReceiver(receiver, filter);
    }

    private void checkPermission() {
        for (String permission : permissions) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, permission)) {
                per.add(permission);
            }
        }
    }

    private void requestPermission() {
        if (per.size() > 0) {
            String[] p = new String[per.size()];
            ActivityCompat.requestPermissions(this, per.toArray(p), REQUEST_CODE);
        }
    }

    /**
     * 蓝牙连接
     */
    public void btnBluetoothConn(View view) {
        startActivityForResult(new Intent(this, BluetoothDeviceList.class), Constant.BLUETOOTH_REQUEST_CODE);
    }

    /**
     * 连接多设备
     *
     * @param view
     */
    public void btnMoreDevices(View view) {
        startActivityForResult(new Intent(this, ConnMoreDevicesActivity.class), CONN_MOST_DEVICES);
    }

    /**
     * 串口连接
     *
     * @param view
     */
    public void btnSerialPortConn(View view) {
        startActivityForResult(new Intent(this, SerialPortList.class), Constant.SERIALPORT_REQUEST_CODE);
    }

    /**
     * USB连接
     *
     * @param view
     */
    public void btnUsbConn(View view) {
        startActivityForResult(new Intent(this, UsbDeviceList.class), Constant.USB_REQUEST_CODE);
    }

    /**
     * WIFI连接
     *
     * @param view
     */
    public void btnWifiConn(View view) {
        WifiParameterConfigDialog wifiParameterConfigDialog = new WifiParameterConfigDialog(this, mHandler);
        wifiParameterConfigDialog.show();
    }

    public void btnReceiptPrint(View view) {
        if (DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id] == null ||
                !DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].getConnState()) {
            Utils.toast(this, getString(R.string.str_cann_printer));
            return;
        }
        threadPool = ThreadPool.getInstantiation();
        threadPool.addTask(new Runnable() {
            @Override
            public void run() {
                if (DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].getCurrentPrinterCommand() == PrinterCommand.ESC) {
                    sendReceiptWithResponse();
                } else {
                    mHandler.obtainMessage(PRINTER_COMMAND_ERROR).sendToTarget();
                }
            }
        });
    }

    public void btnLabelPrint(View view) {
        threadPool = ThreadPool.getInstantiation();
        threadPool.addTask(new Runnable() {
            @Override
            public void run() {
                if (DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id] == null ||
                        !DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].getConnState()) {
                    mHandler.obtainMessage(CONN_PRINTER).sendToTarget();
                    return;
                }
                if (DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].getCurrentPrinterCommand() == PrinterCommand.TSC) {
                    sendLabel();
                } else {
                    mHandler.obtainMessage(PRINTER_COMMAND_ERROR).sendToTarget();
                }
            }
        });
    }

    public void btnDisConn(View view) {
        mHandler.obtainMessage(CONN_STATE_DISCONN).sendToTarget();
    }

    /**
     * 打印机状态查询
     *
     * @param view
     */
    public void btnPrinterState(View view) {
        //打印机状态查询
        if (DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id] == null ||
                !DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].getConnState()) {
            Utils.toast(this, getString(R.string.str_cann_printer));
            return;
        }
        ThreadPool.getInstantiation().addTask(new Runnable() {
            @Override
            public void run() {
                Vector<Byte> data = new Vector<>(esc.length);
                if (DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].getCurrentPrinterCommand() == PrinterCommand.ESC) {

                    for (int i = 0; i < esc.length; i++) {
                        data.add(esc[i]);
                    }
                    DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].sendDataImmediately(data);
                }

                if (DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].getCurrentPrinterCommand() == PrinterCommand.TSC) {
                    for (int i = 0; i < tsc.length; i++) {
                        data.add(tsc[i]);
                    }
                    DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].sendDataImmediately(data);
                }
            }
        });
    }

    /**
     * 连续打印
     *
     * @param view
     */
    public void btnReceiptAndLabelContinuityPrint(View view) {
        counts = Integer.parseInt(etPrintCounts.getText().toString().trim());
        sendContinuityPrint();
    }

    private void sendContinuityPrint() {
        ThreadPool.getInstantiation().addTask(new Runnable() {
            @Override
            public void run() {
                if (DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id] != null
                        && DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].getConnState()) {
                    ThreadFactoryBuilder threadFactoryBuilder = new ThreadFactoryBuilder("MainActivity_sendContinuity_Timer");
                    ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(1, threadFactoryBuilder);
                    scheduledExecutorService.schedule(threadFactoryBuilder.newThread(new Runnable() {
                        @Override
                        public void run() {
                            counts--;
                            if (DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].getCurrentPrinterCommand() == PrinterCommand.ESC) {
                                sendReceiptWithResponse();
                            } else {
                                //标签模式可直接使用LabelCommand.addPrint()方法进行打印
                                sendLabel();
                            }
                        }
                    }), 1000, TimeUnit.MILLISECONDS);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {

                /*蓝牙连接*/
                case Constant.BLUETOOTH_REQUEST_CODE: {
                    /*获取蓝牙mac地址*/
                    String macAddress = data.getStringExtra(BluetoothDeviceList.EXTRA_DEVICE_ADDRESS);
                    //初始化话DeviceConnFactoryManager
                    new DeviceConnFactoryManager.Build()
                            .setId(id)
                            //设置连接方式
                            .setConnMethod(DeviceConnFactoryManager.CONN_METHOD.BLUETOOTH)
                            //设置连接的蓝牙mac地址
                            .setMacAddress(macAddress)
                            .build();
                    //打开端口
                    DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].openPort();
                    break;
                }
                /*USB连接*/
                case Constant.USB_REQUEST_CODE: {
                    //获取USB设备名
                    usbName = data.getStringExtra(UsbDeviceList.USB_NAME);
                    //通过USB设备名找到USB设备
                    UsbDevice usbDevice = Utils.getUsbDeviceFromName(MainActivityPrinter.this, usbName);
                    //判断USB设备是否有权限
                    if (usbManager.hasPermission(usbDevice)) {
                        usbConn(usbDevice);
                    } else {//请求权限
                        mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(Constant.ACTION_USB_PERMISSION), 0);
                        usbManager.requestPermission(usbDevice, mPermissionIntent);
                    }
                    break;
                }
                /*串口连接*/
                case Constant.SERIALPORT_REQUEST_CODE:
                    //获取波特率
                    int baudrate = data.getIntExtra(Constant.SERIALPORTBAUDRATE, 0);
                    //获取串口号
                    String path = data.getStringExtra(Constant.SERIALPORTPATH);

                    if (baudrate != 0 && !TextUtils.isEmpty(path)) {
                        //初始化DeviceConnFactoryManager
                        new DeviceConnFactoryManager.Build()
                                //设置连接方式
                                .setConnMethod(DeviceConnFactoryManager.CONN_METHOD.SERIAL_PORT)
                                .setId(id)
                                //设置波特率
                                .setBaudrate(baudrate)
                                //设置串口号
                                .setSerialPort(path)
                                .build();
                        //打开端口
                        DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].openPort();
                    }
                    break;
                case CONN_MOST_DEVICES:
                    id = data.getIntExtra("id", -1);
                    if (DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id] != null &&
                            DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].getConnState()) {
                        tvConnState.setText(getString(R.string.str_conn_state_connected) + "\n" + getConnDeviceInfo());
                    } else {
                        tvConnState.setText(getString(R.string.str_conn_state_disconnect));
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * usb连接
     *
     * @param usbDevice
     */
    private void usbConn(UsbDevice usbDevice) {
        new DeviceConnFactoryManager.Build()
                .setId(id)
                .setConnMethod(DeviceConnFactoryManager.CONN_METHOD.USB)
                .setUsbDevice(usbDevice)
                .setContext(this)
                .build();
        DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].openPort();
    }

    /**
     * 发送标签
     *
     */
    void sendLabel() {
        String type = getIntent().getStringExtra("type");
        if ("1".equals(type)){
            sendMessageToast("1号暂无打印功能");
        }else if ("2".equals(type)){
            print2();
        }else if ("3".equals(type)){
            print3();
        }else {
            sendMessageToast("不支持的打印类型");
        }
    }

    /**
     * 发送票据
     */
    void sendReceiptWithResponse() {
        EscCommand esc = new EscCommand();
        esc.addInitializePrinter();
        esc.addPrintAndFeedLines((byte) 3);
        // 设置打印居中
        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);
        // 设置为倍高倍宽
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.ON, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF);
        // 打印文字
        esc.addText("Sample\n");
        esc.addPrintAndLineFeed();

        /* 打印文字 */
        // 取消倍高倍宽
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);
        // 设置打印左对齐
        esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);
        // 打印文字
        esc.addText("Print text\n");
        // 打印文字
        esc.addText("Welcome to use SMARNET printer!\n");

        /* 打印繁体中文 需要打印机支持繁体字库 */
        String message = "佳博智匯票據打印機\n";
        esc.addText(message, "GB2312");
        esc.addPrintAndLineFeed();

        /* 绝对位置 具体详细信息请查看GP58编程手册 */
        esc.addText("智汇");
        esc.addSetHorAndVerMotionUnits((byte) 7, (byte) 0);
        esc.addSetAbsolutePrintPosition((short) 6);
        esc.addText("网络");
        esc.addSetAbsolutePrintPosition((short) 10);
        esc.addText("设备");
        esc.addPrintAndLineFeed();

        /* 打印图片 */
        // 打印文字
        esc.addText("Print bitmap!\n");
        Bitmap b = BitmapFactory.decodeResource(getResources(),
                R.mipmap.gprinter);
        // 打印图片
        esc.addOriginRastBitImage(b, 384, 0);

        /* 打印一维条码 */
        // 打印文字
        esc.addText("Print code128\n");
        esc.addSelectPrintingPositionForHRICharacters(EscCommand.HRI_POSITION.BELOW);
        // 设置条码可识别字符位置在条码下方
        // 设置条码高度为60点
        esc.addSetBarcodeHeight((byte) 60);
        // 设置条码单元宽度为1
        esc.addSetBarcodeWidth((byte) 1);
        // 打印Code128码
        esc.addCODE128(esc.genCodeB("SMARNET"));
        esc.addPrintAndLineFeed();

        /*
         * QRCode命令打印 此命令只在支持QRCode命令打印的机型才能使用。 在不支持二维码指令打印的机型上，则需要发送二维条码图片
         */
        // 打印文字
        esc.addText("Print QRcode\n");
        // 设置纠错等级
        esc.addSelectErrorCorrectionLevelForQRCode((byte) 0x31);
        // 设置qrcode模块大小
        esc.addSelectSizeOfModuleForQRCode((byte) 3);
        // 设置qrcode内容
        esc.addStoreQRCodeData("www.smarnet.cc");
        esc.addPrintQRCode();// 打印QRCode
        esc.addPrintAndLineFeed();

        // 设置打印左对齐
        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);
        //打印文字
        esc.addText("Completed!\r\n");

        // 开钱箱
        esc.addGeneratePlus(LabelCommand.FOOT.F5, (byte) 255, (byte) 255);
        esc.addPrintAndFeedLines((byte) 8);
        // 加入查询打印机状态，打印完成后，此时会接收到GpCom.ACTION_DEVICE_STATUS广播
        esc.addQueryPrinterStatus();
        Vector<Byte> datas = esc.getCommand();
        // 发送数据
        DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].sendDataImmediately(datas);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case Constant.ACTION_USB_PERMISSION:
                    synchronized (this) {
                        UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                        if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                            if (device != null) {
                                System.out.println("permission ok for device " + device);
                                usbConn(device);
                            }
                        } else {
                            System.out.println("permission denied for device " + device);
                        }
                    }
                    break;
                //Usb连接断开、蓝牙连接断开广播
                case UsbManager.ACTION_USB_DEVICE_DETACHED:
                case BluetoothDevice.ACTION_ACL_DISCONNECTED:
                    mHandler.obtainMessage(CONN_STATE_DISCONN).sendToTarget();
                    break;
                case DeviceConnFactoryManager.ACTION_CONN_STATE:
                    int state = intent.getIntExtra(DeviceConnFactoryManager.STATE, -1);
                    int deviceId = intent.getIntExtra(DeviceConnFactoryManager.DEVICE_ID, -1);
                    switch (state) {
                        case DeviceConnFactoryManager.CONN_STATE_DISCONNECT:
                            if (id == deviceId) {
                                tvConnState.setText(getString(R.string.str_conn_state_disconnect));
                            }
                            break;
                        case DeviceConnFactoryManager.CONN_STATE_CONNECTING:
                            tvConnState.setText(getString(R.string.str_conn_state_connecting));
                            break;
                        case DeviceConnFactoryManager.CONN_STATE_CONNECTED:
                            tvConnState.setText(getString(R.string.str_conn_state_connected) + "\n" + getConnDeviceInfo());
                            break;
                        case DeviceConnFactoryManager.CONN_STATE_FAILED:
                            Utils.toast(MainActivityPrinter.this, getString(R.string.str_conn_fail));
                            tvConnState.setText(getString(R.string.str_conn_state_disconnect));
                            break;
                        default:
                            break;
                    }
                    break;
                case DeviceConnFactoryManager.ACTION_QUERY_PRINTER_STATE:
                    if(counts >0) {
                        sendContinuityPrint();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CONN_STATE_DISCONN:
                    if (DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id] != null) {
                        DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].closePort(id);
                    }
                    break;
                case PRINTER_COMMAND_ERROR:
                    Utils.toast(MainActivityPrinter.this, getString(R.string.str_choice_printer_command));
                    break;
                case CONN_PRINTER:
                    Utils.toast(MainActivityPrinter.this, getString(R.string.str_cann_printer));
                    break;
                case Constant.MESSAGE_UPDATE_PARAMETER:
                    String strIp = msg.getData().getString("Ip");
                    String strPort = msg.getData().getString("Port");
                    //初始化端口信息
                    new DeviceConnFactoryManager.Build()
                            //设置端口连接方式
                            .setConnMethod(DeviceConnFactoryManager.CONN_METHOD.WIFI)
                            //设置端口IP地址
                            .setIp(strIp)
                            //设置端口ID（主要用于连接多设备）
                            .setId(id)
                            //设置连接的热点端口号
                            .setPort(Integer.parseInt(strPort))
                            .build();
                    threadPool = ThreadPool.getInstantiation();
                    threadPool.addTask(new Runnable() {
                        @Override
                        public void run() {
                            DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].openPort();
                        }
                    });
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(receiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG,"onDestroy()");
        DeviceConnFactoryManager.closeAllPort();
        if (threadPool != null) {
            threadPool.stopThreadPool();
        }
    }

    private String getConnDeviceInfo() {
        String str = "";
        DeviceConnFactoryManager deviceConnFactoryManager = DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id];
        if (deviceConnFactoryManager != null
                && deviceConnFactoryManager.getConnState()) {
            if ("USB".equals(deviceConnFactoryManager.getConnMethod().toString())) {
                str += "USB\n";
                str += "USB Name: " + deviceConnFactoryManager.usbDevice().getDeviceName();
            } else if ("WIFI".equals(deviceConnFactoryManager.getConnMethod().toString())) {
                str += "WIFI\n";
                str += "IP: " + deviceConnFactoryManager.getIp() + "\t";
                str += "Port: " + deviceConnFactoryManager.getPort();
            } else if ("BLUETOOTH".equals(deviceConnFactoryManager.getConnMethod().toString())) {
                str += "BLUETOOTH\n";
                str += "MacAddress: " + deviceConnFactoryManager.getMacAddress();
            } else if ("SERIAL_PORT".equals(deviceConnFactoryManager.getConnMethod().toString())) {
                str += "SERIAL_PORT\n";
                str += "Path: " + deviceConnFactoryManager.getSerialPortPath() + "\t";
                str += "Baudrate: " + deviceConnFactoryManager.getBaudrate();
            }
        }
        return str;
    }

    private void print2(){
        LabelCommand tsc = new LabelCommand();
        // 设置标签尺寸，按照实际尺寸设置
        tsc.addSize(50, 30);
        // 设置标签间隙，按照实际尺寸设置，如果为无间隙纸则设置为0
        tsc.addGap(2);
        // 设置打印方向
        tsc.addDirection(LabelCommand.DIRECTION.BACKWARD, LabelCommand.MIRROR.NORMAL);
        // 开启带Response的打印，用于连续打印
        tsc.addQueryPrinterStatus(LabelCommand.RESPONSE_MODE.ON);
        // 设置原点坐标
        tsc.addReference(0, 16);
        // 撕纸模式开启
        tsc.addTear(EscCommand.ENABLE.ON);
        // 清除打印缓冲区
        tsc.addCls();
        int i = 0;
        // 绘制简体中文
        tsc.addText(0, 32*i++, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE,
                LabelCommand.ROTATION.ROTATION_0,
                LabelCommand.FONTMUL.MUL_1,
                LabelCommand.FONTMUL.MUL_1,
                getIntent().getStringExtra(MainActivityPrinter.物联SN));
        tsc.addText(0, 32*i++, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE,
                LabelCommand.ROTATION.ROTATION_0,
                LabelCommand.FONTMUL.MUL_1,
                LabelCommand.FONTMUL.MUL_1,
                getIntent().getStringExtra(MainActivityPrinter.传感));
        tsc.addText(0, 32*i++, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE,
                LabelCommand.ROTATION.ROTATION_0,
                LabelCommand.FONTMUL.MUL_1,
                LabelCommand.FONTMUL.MUL_1,
                "无传感器静态12-20μA");
        String name = getIntent().getStringExtra(MainActivityPrinter.厂家名称);
        if (name.length()>9){
            name = name.substring(0,9)+"...";
        }
        tsc.addText(0, 32*i++, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE,
                LabelCommand.ROTATION.ROTATION_0,
                LabelCommand.FONTMUL.MUL_1,
                LabelCommand.FONTMUL.MUL_1,
                name);
        tsc.addText(0, 32*i++, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE,
                LabelCommand.ROTATION.ROTATION_0,
                LabelCommand.FONTMUL.MUL_1,
                LabelCommand.FONTMUL.MUL_1,
                getIntent().getStringExtra(MainActivityPrinter.设备ID));
//        tsc.addText(0, 32*i++, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE,
//                LabelCommand.ROTATION.ROTATION_0,
//                LabelCommand.FONTMUL.MUL_1,
//                LabelCommand.FONTMUL.MUL_1,
//                "工作电流：60-100mA");
        tsc.addText(0, 32*i++, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE,
                LabelCommand.ROTATION.ROTATION_0,
                LabelCommand.FONTMUL.MUL_1,
                LabelCommand.FONTMUL.MUL_1,
                getIntent().getStringExtra(MainActivityPrinter.出厂时间));
        // 绘制二维码
        tsc.addQRCode(260, 0,

                LabelCommand.EEC.LEVEL_L, 4,
                LabelCommand.ROTATION.ROTATION_0,
                getIntent().getStringExtra(MainActivityPrinter.物联SN).replaceAll(MainActivityPrinter.物联SN,""));
        tsc.addQRCode(260, 110,
                LabelCommand.EEC.LEVEL_L, 4,
                LabelCommand.ROTATION.ROTATION_0,
                getIntent().getStringExtra(MainActivityPrinter.设备ID).replaceAll(MainActivityPrinter.设备ID,""));
//        tsc.addText(290, 96, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE,
//                LabelCommand.ROTATION.ROTATION_0,
//                LabelCommand.FONTMUL.MUL_1,
//                LabelCommand.FONTMUL.MUL_1,
//                "SN");
//        tsc.addBar(260,96,100,1);
//        tsc.addText(390, 96, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE,
//                LabelCommand.ROTATION.ROTATION_0,
//                LabelCommand.FONTMUL.MUL_1,
//                LabelCommand.FONTMUL.MUL_1,
//                "ID");
        // 打印标签
        tsc.addPrint(1, 1);
        // 打印标签后 蜂鸣器响
        tsc.addSound(1, 100);
        tsc.addCashdrwer(LabelCommand.FOOT.F5, 255, 255);
        Vector<Byte> datas = tsc.getCommand();
        // 发送数据
        if (DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id] == null) {
            return;
        }
        DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].sendDataImmediately(datas);
    }

    private void print3(){
        LabelCommand tsc = new LabelCommand();
        // 设置标签尺寸，按照实际尺寸设置
        tsc.addSize(50, 30);
        // 设置标签间隙，按照实际尺寸设置，如果为无间隙纸则设置为0
        tsc.addGap(2);
        // 设置打印方向
        tsc.addDirection(LabelCommand.DIRECTION.BACKWARD, LabelCommand.MIRROR.NORMAL);
        // 开启带Response的打印，用于连续打印
        tsc.addQueryPrinterStatus(LabelCommand.RESPONSE_MODE.ON);
        // 设置原点坐标
        tsc.addReference(0, 16);
        // 撕纸模式开启
        tsc.addTear(EscCommand.ENABLE.ON);
        // 清除打印缓冲区
        tsc.addCls();
        // 绘制简体中文
        int i = 0;
        tsc.addText(0, 32*i++, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE,
                LabelCommand.ROTATION.ROTATION_0,
                LabelCommand.FONTMUL.MUL_1,
                LabelCommand.FONTMUL.MUL_1,
                getIntent().getStringExtra(MainActivityPrinter.设备ID));
//        tsc.addText(0, 32*i++, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE,
//                LabelCommand.ROTATION.ROTATION_0,
//                LabelCommand.FONTMUL.MUL_1,
//                LabelCommand.FONTMUL.MUL_1,
//                getIntent().getStringExtra(MainActivityPrinter.传感));
        tsc.addText(0, 32*i++, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE,
                LabelCommand.ROTATION.ROTATION_0,
                LabelCommand.FONTMUL.MUL_1,
                LabelCommand.FONTMUL.MUL_1,
                getIntent().getStringExtra(MainActivityPrinter.出厂时间));
        tsc.addText(0, 32*i++, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE,
                LabelCommand.ROTATION.ROTATION_0,
                LabelCommand.FONTMUL.MUL_1,
                LabelCommand.FONTMUL.MUL_1,
                "工作电流：5-10mA");
        tsc.addText(0, 32*i++, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE,
                LabelCommand.ROTATION.ROTATION_0,
                LabelCommand.FONTMUL.MUL_1,
                LabelCommand.FONTMUL.MUL_1,
                "无传感器静态：10-15μA");
        tsc.addText(0, 32*i++, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE,
                LabelCommand.ROTATION.ROTATION_0,
                LabelCommand.FONTMUL.MUL_1,
                LabelCommand.FONTMUL.MUL_1,
                getIntent().getStringExtra(MainActivityPrinter.厂家名称));
        // 绘制二维码
        tsc.addQRCode(260, 0,
                LabelCommand.EEC.LEVEL_L, 4,
                LabelCommand.ROTATION.ROTATION_0,
                getIntent().getStringExtra(MainActivityPrinter.设备ID).replaceAll(MainActivityPrinter.设备ID,""));
        tsc.addText(290, 96, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE,
                LabelCommand.ROTATION.ROTATION_0,
                LabelCommand.FONTMUL.MUL_1,
                LabelCommand.FONTMUL.MUL_1,
                "ID");
        // 打印标签
        tsc.addPrint(1, 1);
        // 打印标签后 蜂鸣器响
        tsc.addSound(1, 100);
        tsc.addCashdrwer(LabelCommand.FOOT.F5, 255, 255);
        Vector<Byte> datas = tsc.getCommand();
        // 发送数据
        if (DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id] == null) {
            return;
        }
        DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].sendDataImmediately(datas);
    }
}