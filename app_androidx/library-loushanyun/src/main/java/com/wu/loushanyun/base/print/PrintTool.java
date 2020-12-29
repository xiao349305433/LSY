package com.wu.loushanyun.base.print;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.elvishew.xlog.XLog;
import com.gprinter.command.EscCommand;
import com.gprinter.command.LabelCommand;
import com.gpsdk.demo.DeviceConnFactoryManager;
import com.gpsdk.demo.Utils;
import com.qrcore.util.QRUtil;
import com.wu.loushanyun.R;
import com.wu.loushanyun.basemvp.m.PrintBean;
import com.wu.loushanyun.basemvp.p.adapter.PrintBeanViewBinder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableOnSubscribe;
import met.hx.com.base.base.multitype.MultiTypeAdapter;
import met.hx.com.base.base.rx.RxSchedulers;
import met.hx.com.base.baseinterface.SimpleLifeCycle;
import met.hx.com.librarybase.some_utils.ToastUtils;
import met.hx.com.librarybase.some_utils.util.AbSharedUtil;
import met.hx.com.librarybase.views.CustomPopWindow;
import met.hx.com.librarybase.views.dialog.MDDialog;

import static android.hardware.usb.UsbManager.ACTION_USB_DEVICE_DETACHED;
import static com.gpsdk.demo.Constant.ACTION_USB_PERMISSION;
import static com.gpsdk.demo.DeviceConnFactoryManager.ACTION_QUERY_PRINTER_STATE;
import static com.gpsdk.demo.DeviceConnFactoryManager.CONN_STATE_FAILED;

public class PrintTool extends SimpleLifeCycle {
    public static final boolean canPrinted = false;


    public static final int PRINT_WIFI = 1;//WIFI打印机
    public static final int PRINT_USB = 2;//便携式打印机

    private int printId;
    private boolean isConnected;

    private PrintListener printListener;
    private MDDialog mdDialogUsb;
    private int printType;
    private CustomPopWindow printPopWindow;
    private MDDialog printDialog;


    public interface PrintListener {

        void onUsbPermission(Intent intent);

        void onUsbDeviceDetached(Intent intent);

        void onAclDisconnected(Intent intent);

        void onConnectionState(Intent intent);

        void onConnectionStateDisconnect(Intent intent);

        void onConnectionStateConnecting(Intent intent);

        void onConnectionStateConnected(Intent intent);

        void onConnectionStateFailed(Intent intent);

        void onQueryPrinterState(Intent intent);
    }

    public PrintTool(int printId, PrintListener printListener) {
        this.printId = printId;
        this.printListener = printListener;
    }

    public void showPrintPopWindow(View view) {
        View contentView = View.inflate(getActivity(), R.layout.l_loushanyun_print_pop, null);
        printPopWindow = new CustomPopWindow.PopupWindowBuilder(getActivity())
                .setView(contentView)//显示的布局，还可以通过设置一个View
                .setAnimationStyle(R.style.base_showPopupAnimation)
                .setFocusable(false)
                .size(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT) //设置显示的大小，不设置就默认包裹内容
                .create();//创建PopupWindow
        RecyclerView recyclePrinter = (RecyclerView) contentView.findViewById(R.id.recycle_printer);
        ArrayList<PrintBean> arrayList = new ArrayList<>();
        arrayList.add(new PrintBean(R.drawable.l_loushanyun_wifi_black_24dp, "WIFI连接"));
        arrayList.add(new PrintBean(R.drawable.l_loushanyun_usb_black_24dp, "USB连接"));
        MultiTypeAdapter multiTypeAdapter = new MultiTypeAdapter(arrayList);
        multiTypeAdapter.register(PrintBean.class, new PrintBeanViewBinder(getActivity(), new PrintBeanViewBinder.onPrinteListener() {
            @Override
            public void onClickPrint(PrintBean printBean) {
                if ("WIFI连接".equals(printBean.getTextName())) {
                    connectWifi();
                    printPopWindow.dissmiss();
                } else if ("USB连接".equals(printBean.getTextName())) {
                    connectUsb();
                    printPopWindow.dissmiss();
                }
            }
        }));
        recyclePrinter.setAdapter(multiTypeAdapter);
        printPopWindow.showAsDropDown(view);
    }

    public void showPrintDialog() {
        View contentView = View.inflate(getActivity(), R.layout.l_loushanyun_print_pop, null);
        printDialog = new MDDialog.Builder(getActivity())
                .setContentView(contentView)
                .setTitle("请选择打印机类型")
                .setShowButtons(false)
                .setShowTitle(false).create();
        RecyclerView recyclePrinter = (RecyclerView) contentView.findViewById(R.id.recycle_printer);
        ArrayList<PrintBean> arrayList = new ArrayList<>();
        arrayList.add(new PrintBean(R.drawable.l_loushanyun_wifi_black_24dp, "WIFI连接"));
        arrayList.add(new PrintBean(R.drawable.l_loushanyun_usb_black_24dp, "USB连接"));
        MultiTypeAdapter multiTypeAdapter = new MultiTypeAdapter(arrayList);
        multiTypeAdapter.register(PrintBean.class, new PrintBeanViewBinder(getActivity(), new PrintBeanViewBinder.onPrinteListener() {
            @Override
            public void onClickPrint(PrintBean printBean) {
                if ("WIFI连接".equals(printBean.getTextName())) {
                    connectWifi();
                    printDialog.dismiss();
                } else if ("USB连接".equals(printBean.getTextName())) {
                    connectUsb();
                    printDialog.dismiss();
                }
            }
        }));
        recyclePrinter.setAdapter(multiTypeAdapter);
        printDialog.show();
    }

    public void dissmissPrintPopWindow() {
        if (printPopWindow != null) {
            printPopWindow.dissmiss();
        }
    }

    public void dissmissPrintDialog() {
        if (printDialog != null) {
            printDialog.dismiss();
        }
    }

    /**
     * wifi 默认端口号
     */
    public static final int WIFI_DEFAULT_PORT = 9100;
    /**
     * wifi IP键值对 key
     */
    public static final String WIFI_DEFAULT_IP = "WIFI_DEFAULT_IP";

    /**
     * WIFI连接
     */
    public void connectWifi() {
        printType = PrintTool.PRINT_WIFI;
        //获取wifi服务
        WifiManager wifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        //判断wifi是否开启
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        String strIp = AbSharedUtil.getString(getActivity(), WIFI_DEFAULT_IP);
        if (strIp == null) {
            strIp = intToIp(ipAddress);
        }
        View contentView = View.inflate(getActivity(), com.gpsdk.demo.R.layout.dialog_wifi_config, null);
        final EditText etWifiIpConfig = (EditText) contentView.findViewById(com.gpsdk.demo.R.id.et_wifi_ip);
        final EditText etWifiPortConfig = (EditText) contentView.findViewById(com.gpsdk.demo.R.id.et_wifi_port);
        etWifiIpConfig.setText(strIp);
        etWifiPortConfig.setText(String.valueOf(WIFI_DEFAULT_PORT));
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).setView(contentView)
                .setNegativeButton(getActivity().getString(com.gpsdk.demo.R.string.str_confirm), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                final String strIp = etWifiIpConfig.getText().toString().trim();
                                final String strPort = etWifiPortConfig.getText().toString().trim();
                                AbSharedUtil.putString(getActivity(), WIFI_DEFAULT_IP, strIp);
                                //初始化端口信息
                                new DeviceConnFactoryManager.Build()
                                        //设置端口连接方式
                                        .setConnMethod(DeviceConnFactoryManager.CONN_METHOD.WIFI)
                                        //设置端口IP地址
                                        .setIp(strIp)
                                        //设置端口ID（主要用于连接多设备）
                                        .setId(printId)
                                        //设置连接的热点端口号
                                        .setPort(WIFI_DEFAULT_PORT)
                                        .build();
                                compositeDisposable.add(Flowable.create(
                                        (FlowableOnSubscribe<Integer>) emitter ->
                                        {
                                            DeviceConnFactoryManager.getDeviceConnFactoryManagers()[printId].openPort();
                                            emitter.onNext(printId);
                                        }
                                        , BackpressureStrategy.ERROR)
                                        .compose(RxSchedulers.io_main())
                                        .subscribe(object -> XLog.i(printId)));
                            }
                        }
                )
                .setPositiveButton(getActivity().getString(com.gpsdk.demo.R.string.str_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
        alertDialog.setCanceledOnTouchOutside(false);
    }

    /**
     * usb连接
     */
    public void connectUsb() {
        printType = PrintTool.PRINT_USB;
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_usb_list, null);
        mdDialogUsb = new MDDialog.Builder(getActivity()).setContentView(view)
                .setShowButtons(false).setShowTitle(false).create();
        mdDialogUsb.show();
        ListView lvUsbDevice = (ListView) view.findViewById(R.id.lvUsbDevices);
        ArrayAdapter mUsbDeviceArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.usb_device_name_item);
        lvUsbDevice.setAdapter(mUsbDeviceArrayAdapter);
        UsbManager usbManager = (UsbManager) getActivity().getSystemService(Context.USB_SERVICE);
        // Get the list of attached devices
        HashMap<String, UsbDevice> devices = usbManager.getDeviceList();
        Iterator<UsbDevice> deviceIterator = devices.values().iterator();
        int count = devices.size();
        if (count > 0) {
            while (deviceIterator.hasNext()) {
                UsbDevice device = deviceIterator.next();
                String devicename = device.getDeviceName();
                if (checkUsbDevicePidVid(device)) {
                    mUsbDeviceArrayAdapter.add(devicename);
                }
            }
        } else {
            String noDevices = getActivity().getResources().getText(com.gpsdk.demo.R.string.none_usb_device).toString();
            mUsbDeviceArrayAdapter.add(noDevices);
        }

        lvUsbDevice.setOnItemClickListener((adapterView, view1, i, l) -> {
            String info = ((TextView) view1).getText().toString();
            String noDevices = getActivity().getResources().getText(com.gpsdk.demo.R.string.none_usb_device).toString();
            if (!info.equals(noDevices)) {
                String usbName = info;
                //通过USB设备名找到USB设备
                UsbDevice usbDevice = Utils.getUsbDeviceFromName(getActivity(), usbName);
                //判断USB设备是否有权限
                if (usbManager.hasPermission(usbDevice)) {
                    new DeviceConnFactoryManager.Build()
                            .setId(printId)
                            .setConnMethod(DeviceConnFactoryManager.CONN_METHOD.USB)
                            .setUsbDevice(usbDevice)
                            .setContext(getActivity())
                            .build().openPort();
                } else {//请求权限
                    PendingIntent mPermissionIntent = PendingIntent.getBroadcast(getActivity(), 0, new Intent(ACTION_USB_PERMISSION), 0);
                    usbManager.requestPermission(usbDevice, mPermissionIntent);
                }
            }
        });
    }

    boolean checkUsbDevicePidVid(UsbDevice dev) {
        int pid = dev.getProductId();
        int vid = dev.getVendorId();
        return ((vid == 34918 && pid == 256) || (vid == 1137 && pid == 85)
                || (vid == 6790 && pid == 30084)
                || (vid == 26728 && pid == 256) || (vid == 26728 && pid == 512)
                || (vid == 26728 && pid == 256) || (vid == 26728 && pid == 768)
                || (vid == 26728 && pid == 1024) || (vid == 26728 && pid == 1280)
                || (vid == 26728 && pid == 1536));
    }

    @Override
    public void onCreate(Bundle savedInstanceState, Activity activity) {
        super.onCreate(savedInstanceState, activity);
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        filter.addAction(ACTION_USB_DEVICE_DETACHED);
        filter.addAction(ACTION_QUERY_PRINTER_STATE);
        filter.addAction(DeviceConnFactoryManager.ACTION_CONN_STATE);
        activity.registerReceiver(receiver, filter);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case ACTION_USB_PERMISSION:
                    printListener.onUsbPermission(intent);
                    break;
                //Usb连接断开、蓝牙连接断开广播
                case ACTION_USB_DEVICE_DETACHED:
                    printListener.onUsbDeviceDetached(intent);
                case BluetoothDevice.ACTION_ACL_DISCONNECTED:
                    printListener.onAclDisconnected(intent);
                    break;
                case DeviceConnFactoryManager.ACTION_CONN_STATE:
                    printListener.onConnectionState(intent);
                    int state = intent.getIntExtra(DeviceConnFactoryManager.STATE, -1);
                    int deviceId = intent.getIntExtra(DeviceConnFactoryManager.DEVICE_ID, -1);
                    switch (state) {
                        case DeviceConnFactoryManager.CONN_STATE_DISCONNECT:
                            if (printId == deviceId) {
                                isConnected = false;
                                printListener.onConnectionStateDisconnect(intent);
                                XLog.i("断开");
                            }
                            break;
                        case DeviceConnFactoryManager.CONN_STATE_CONNECTING:
                            printListener.onConnectionStateConnecting(intent);
                            XLog.i("正在连接");
                            ToastUtils.showShort("正在连接");
                            break;
                        case DeviceConnFactoryManager.CONN_STATE_CONNECTED:
                            isConnected = true;
                            printListener.onConnectionStateConnected(intent);
                            XLog.i("连接成功");
                            ToastUtils.showShort("连接成功");
                            break;
                        case CONN_STATE_FAILED:
                            isConnected = false;
                            printListener.onConnectionStateFailed(intent);
                            XLog.i("断开连接");
                            ToastUtils.showShort("断开连接");
                            break;
                        default:
                            break;
                    }
                    break;
                case ACTION_QUERY_PRINTER_STATE:
                    printListener.onQueryPrinterState(intent);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onDestroy() {
        if (printPopWindow != null) {
            printPopWindow.dissmiss();
        }
        DeviceConnFactoryManager.closeAllPort();
        getActivity().unregisterReceiver(receiver);
        super.onDestroy();
    }

    private String intToIp(int i) {
        return (i & 0xFF) + "." +
                ((i >> 8) & 0xFF) + "." +
                ((i >> 16) & 0xFF) + "." +
                (i >> 24 & 0xFF);
    }

    public boolean isConnected() {
        return isConnected;
    }

    public String getConnDeviceInfo() {
        String str = "";
        DeviceConnFactoryManager deviceConnFactoryManager = DeviceConnFactoryManager.getDeviceConnFactoryManagers()[printId];
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

    public void printBitmap(Bitmap bitmap) {
        if (!isConnected()) {
            ToastUtils.showLong("请连接打印机");
            return;
        }
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
        tsc.addReference(0, 0);
        // 撕纸模式开启
        tsc.addTear(EscCommand.ENABLE.ON);
        // 清除打印缓冲区
        tsc.addCls();
        tsc.addBitmap(0, 0, bitmap.getWidth(), bitmap);
        // 打印标签后 蜂鸣器响
        tsc.addPrint(1, 1);
        tsc.addSound(1, 100);
        tsc.addCashdrwer(LabelCommand.FOOT.F5, 255, 255);
        Vector<Byte> datas = tsc.getCommand();
        // 发送数据
        if (DeviceConnFactoryManager.getDeviceConnFactoryManagers()[printId] == null) {
            ToastUtils.showLong("打印机连接错误，请重新连接");
            return;
        }
        DeviceConnFactoryManager.getDeviceConnFactoryManagers()[printId].sendDataImmediately(datas);
    }

    public Bitmap createPrint3(String id, String timeYear, String timeMonth, String timeDay, String factoryName, String chuanganxinhao) {
        int i = 1;
        int gap = 46;
        int paddingTop = 20;
        int width = 600;
        int height = 380;
        int paddingLeft = 36;
        int textSize = 36;
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        canvas.drawRect(0, 0, bitmap.getWidth(), bitmap.getHeight(), paint);
        paint.setColor(Color.BLACK);
        paint.setTextSize(textSize);
        canvas.drawText("ID:" + id, paddingLeft, (gap * i++) + paddingTop, paint);
        canvas.drawText("抄表电流:5-10mA", paddingLeft, (gap * i++) + paddingTop, paint);
        canvas.drawText("无传感静态电流:10-15μA", paddingLeft, (gap * i++) + paddingTop, paint);
        canvas.drawText("传感信号:" + chuanganxinhao, paddingLeft, (gap * i++) + paddingTop, paint);
        factoryName = "厂家名称:" + factoryName;
        if (factoryName.length() > 14) {
            canvas.drawText(factoryName.substring(0, 14), paddingLeft, (gap * i++) + paddingTop, paint);
            canvas.drawText(factoryName.substring(14, factoryName.length()), paddingLeft, (gap * i++) + paddingTop, paint);
        } else {
            canvas.drawText(factoryName, paddingLeft, (gap * i++) + paddingTop, paint);
        }
        canvas.drawText("出厂时间:" + timeYear + '/' + timeMonth + '/' + timeDay, paddingLeft, (gap * i++) + paddingTop, paint);
        Bitmap bitmapZxing = QRUtil.createQRBitmap(id, 150);
        canvas.drawBitmap(bitmapZxing, bitmap.getWidth() - bitmapZxing.getWidth(), paddingTop + 10, paint);
        canvas.save();
        canvas.restore();

        return bitmap;
    }

    public Bitmap createPrint2(String sn, String chuangan, String factoryName, String id, String timeYear, String timeMonth, String timeDay) {
        String time = "出厂时间:" + timeYear + '/' + timeMonth + '/' + timeDay;
        int i = 1;
        int gap = 46;
        int width = 600;
        int height = 380;
        int paddingLeft = 36;
        int paddingTop = 20;
        int textSize = 36;
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        canvas.drawRect(0, 0, bitmap.getWidth(), bitmap.getHeight(), paint);
        paint.setColor(Color.BLACK);
        paint.setTextSize(textSize);
        canvas.drawText("ID:" + id, paddingLeft, (gap * i++) + paddingTop, paint);
        canvas.drawText("SN:" + sn, paddingLeft, (gap * i++) + paddingTop, paint);
        canvas.drawText("无传感静态电流:12-20μA", paddingLeft, (gap * i++) + paddingTop, paint);
        canvas.drawText("传感信号:" + chuangan, paddingLeft, (gap * i++) + paddingTop, paint);
        factoryName = "厂家名称:" + factoryName;
        if (factoryName.length() > 14) {
            canvas.drawText(factoryName.substring(0, 14), paddingLeft, (gap * i++) + paddingTop, paint);
            canvas.drawText(factoryName.substring(14, factoryName.length()), paddingLeft, (gap * i++) + paddingTop, paint);
        } else {
            canvas.drawText(factoryName, paddingLeft, (gap * i++) + paddingTop, paint);
        }
        canvas.drawText("出厂时间:" + timeYear + '/' + timeMonth + '/' + timeDay, paddingLeft, (gap * i++) + paddingTop, paint);
        Bitmap bitmapIDSN = QRUtil.createQRBitmap("TYPE:2/ID:" + id + "/SN:" + sn, 150);
        canvas.drawBitmap(bitmapIDSN, bitmap.getWidth() - bitmapIDSN.getWidth(), paddingTop + 10, paint);
        canvas.save();
        canvas.restore();
        return bitmap;
    }

    public Bitmap createChipPrint2(String sn) {
        int width = 600;
        int height = 380;
        int textSize = 45;
        int paddingTop = 20;
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        canvas.drawRect(0, 0, bitmap.getWidth(), bitmap.getHeight(), paint);
        paint.setColor(Color.BLACK);
        paint.setTextSize(textSize);
        Bitmap bitmapIDSN = QRUtil.createQRBitmap("TYPE:2/ID:" + "/SN:" + sn, 240);
        canvas.drawBitmap(bitmapIDSN, (bitmap.getWidth() / 2) - bitmapIDSN.getWidth() / 2, paddingTop, paint);
        canvas.drawText(sn, (bitmap.getWidth() / 2) - ((sn.length() - 7) * textSize) / 2, bitmapIDSN.getHeight() + textSize + paddingTop, paint);
        canvas.save();
        canvas.restore();
        return bitmap;
    }

    public Bitmap createChipPrint1(String sn) {
        int width = 600;
        int height = 380;
        int textSize = 45;
        int paddingTop = 20;
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        canvas.drawRect(0, 0, bitmap.getWidth(), bitmap.getHeight(), paint);
        paint.setColor(Color.BLACK);
        paint.setTextSize(textSize);
        Bitmap bitmapIDSN = QRUtil.createQRBitmap("TYPE:1/ID:" + "/SN:" + sn, 240);
        canvas.drawBitmap(bitmapIDSN, (bitmap.getWidth() / 2) - bitmapIDSN.getWidth() / 2, paddingTop, paint);
        canvas.drawText(sn, (bitmap.getWidth() / 2) - ((sn.length() - 7) * textSize) / 2, bitmapIDSN.getHeight() + textSize + paddingTop, paint);
        canvas.save();
        canvas.restore();
        return bitmap;
    }

    public void print3(String id, String timeYear, String timeMonth, String timeDay, String factoryName, String chuanganxinhao) {
        String time = "出厂时间:" + timeYear + '/' + timeMonth + '/' + timeDay;
        if (!isConnected()) {
            ToastUtils.showLong("请连接打印机");
            return;
        }
        if (printType == PrintTool.PRINT_WIFI) {
            printBitmap(createPrint3(id, timeYear, timeMonth, timeDay, factoryName, chuanganxinhao));
        } else if (printType == PrintTool.PRINT_USB) {
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
            tsc.addReference(0, 18);
            // 撕纸模式开启
            tsc.addTear(EscCommand.ENABLE.ON);
            // 清除打印缓冲区
            tsc.addCls();
            // 绘制简体中文
            int i = 0;
            tsc.addText(0, 32 * i++, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE,
                    LabelCommand.ROTATION.ROTATION_0,
                    LabelCommand.FONTMUL.MUL_1,
                    LabelCommand.FONTMUL.MUL_1,
                    "ID:" + id);
            tsc.addText(0, 32 * i++, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE,
                    LabelCommand.ROTATION.ROTATION_0,
                    LabelCommand.FONTMUL.MUL_1,
                    LabelCommand.FONTMUL.MUL_1,
                    time);
            tsc.addText(0, 32 * i++, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE,
                    LabelCommand.ROTATION.ROTATION_0,
                    LabelCommand.FONTMUL.MUL_1,
                    LabelCommand.FONTMUL.MUL_1,
                    "抄表电流：5-10mA");
            tsc.addText(0, 32 * i++, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE,
                    LabelCommand.ROTATION.ROTATION_0,
                    LabelCommand.FONTMUL.MUL_1,
                    LabelCommand.FONTMUL.MUL_1,
                    "无磁传感静态电流：10-15μA");
            tsc.addText(0, 32 * i++, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE,
                    LabelCommand.ROTATION.ROTATION_0,
                    LabelCommand.FONTMUL.MUL_1,
                    LabelCommand.FONTMUL.MUL_1,
                    "传感信号:" + chuanganxinhao);
            tsc.addText(0, 32 * i++, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE,
                    LabelCommand.ROTATION.ROTATION_0,
                    LabelCommand.FONTMUL.MUL_1,
                    LabelCommand.FONTMUL.MUL_1,
                    factoryName);
            // 绘制二维码
            tsc.addQRCode(260, 0,
                    LabelCommand.EEC.LEVEL_L, 4,
                    LabelCommand.ROTATION.ROTATION_0,
                    id);
//        tsc.addText(290, 96, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE,2
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
            if (DeviceConnFactoryManager.getDeviceConnFactoryManagers()[printId] == null) {
                ToastUtils.showLong("打印机连接错误，请重新连接");
                return;
            }
            DeviceConnFactoryManager.getDeviceConnFactoryManagers()[printId].sendDataImmediately(datas);
        }

    }


    public void print2(String sn, String chuangan, String factoryName, String id, String timeYear, String timeMonth, String timeDay) {
        String time = "出厂时间:" + timeYear + '/' + timeMonth + '/' + timeDay;
        if (!isConnected()) {
            ToastUtils.showLong("请连接打印机");
            return;
        }
        if (printType == PrintTool.PRINT_WIFI) {
            printBitmap(createPrint2(sn, chuangan, factoryName, id, timeYear, timeMonth, timeDay));
        } else if (printType == PrintTool.PRINT_USB) {
            printBitmap(createPrint2(sn, chuangan, factoryName, id, timeYear, timeMonth, timeDay));
        }

    }
}
