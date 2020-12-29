package com.xgg.blesdk;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothAdapter.LeScanCallback;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import java.util.List;
import java.util.UUID;

public abstract class Box {
    //设备名
    public String m_name;
    //设备地址
    public String m_address;
    //蓝牙管理器
    protected BluetoothManager mBluetoothManager;
    //蓝牙适配器m_name
    protected BluetoothAdapter mBluetoothAdapter;
    //蓝牙设备
    public BluetoothDevice mDevice = null;
    //蓝牙gatt
    protected BluetoothGatt mBluetoothGatt = null;
    //上下文
    protected Context m_context;
    //连接接口
    protected OnConnectListener i_connectlistener = null;
    protected OnGetSingleDataListener i_singledatalistener = null;
    protected OnGetMultiDataListener i_multidatalistener = null;
    //蓝牙状态
    public final int BLE_STATE_FREE = 1;                //空闲
    public final int BLE_STATE_SCANNING = 2;        //正在扫描
    public final int BLE_STATE_CONNECTING = 5;            //正在连接
    public final int BLE_STATE_DISCOVERING = 3;            //正在查找服务
    public final int BLE_STATE_READY = 4;        //就绪
    public final int BLE_STATE_SINGLECOMMING = 6;        //正在单例通信
    public final int BLE_STATE_MULTICOMMING = 7;        //正在多例通信
    protected int m_blestate = BLE_STATE_FREE;
    int TIME_OUT = 10;
    protected byte m_bytes[];
    protected int m_curcmd = 0;                //当前命令号
    protected byte m_input[];
    TimerCountConn m_timerthread = null;
    int m_timer;

    //读写的characteristic
    BluetoothGattCharacteristic m_send, m_receive;
    protected String m_str_writechara = "0000ff01-0000-1000-8000-00805f9b34fb";
    protected String m_str_readchara = "0000ff02-0000-1000-8000-00805f9b34fb";

    //光电直读的characteristic
    protected String m_str_writechara_gd = "0000ff11-0000-1000-8000-00805f9b34fb";
    protected String m_str_readchara_gd = "0000ff12-0000-1000-8000-00805f9b34fb";

    public int getState() {
        return m_blestate;
    }

    private boolean hasDialog = true;

    public boolean isHasDialog() {
        return hasDialog;
    }

    public void setHasDialog(boolean hasDialog) {
        this.hasDialog = hasDialog;
    }

    //进度条
    ProgressDialog m_dlg_pgb;

    final protected void ShowProgressBar(Context context, String title, String text) {
        if (hasDialog) {
            if (BleState.getInstance().getShowProgress() == false)
                return;
            if (context == null)
                return;
            if (m_dlg_pgb == null)
                m_dlg_pgb = ProgressDialog.show(context, title, text);
            else if (m_dlg_pgb.isShowing() == false)
                m_dlg_pgb = ProgressDialog.show(context, title, text);
            else {
                m_dlg_pgb.setTitle(title);
                m_dlg_pgb.setMessage(text);
            }
        }

    }

    final protected void HideProgressBar() {
        if (hasDialog) {
            if (m_dlg_pgb != null) {
                m_dlg_pgb.dismiss();
            }
        }

    }

    final public void Connect(int timeout, Activity activity, OnConnectListener listener) {
        i_connectlistener = listener;
        if (m_blestate != BLE_STATE_FREE) {
            BleState.getInstance().Log("zh不可重复连接");
            if (i_connectlistener != null)
                i_connectlistener.onConnectFail(OnConnectListener.RTCODE_ALREADY_CONN);
            return;
        }
        TIME_OUT = timeout;
        m_context = activity;
        //开始扫描
        m_blestate = BLE_STATE_SCANNING;
        mBluetoothManager = (BluetoothManager) activity.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = mBluetoothManager.getAdapter();

        if (mBluetoothAdapter == null) {
            BleState.getInstance().Log("获取蓝牙adapter失败");
            if (i_connectlistener != null)
                i_connectlistener.onConnectFail(OnConnectListener.RTCODE_ADAPTER_NULL);
            m_blestate = BLE_STATE_FREE;
            return;
        } else
            BleState.getInstance().Log("获取蓝牙adapter成功");

        if (!mBluetoothAdapter.isEnabled()) {
            BleState.getInstance().Log("本机蓝牙尚未打开");
            if (i_connectlistener != null)
                i_connectlistener.onConnectFail(OnConnectListener.RTCODE_BLE_CLOSED);
            m_blestate = BLE_STATE_FREE;
            return;
        } else
            BleState.getInstance().Log("本机蓝牙已打开");

        BleState.getInstance().Log("开始扫描周围的蓝牙设备");
//        m_blestate = BLE_STATE_SCANNING;
//        mBluetoothAdapter.startLeScan(mLeScanCallback);
        BleState.getInstance().Log(String.format("匹配成功", m_name));
        Message msg = handler.obtainMessage();
        msg.what = MSG_SCAN_MATCH;
        msg.sendToTarget();
        m_timerthread = new TimerCountConn();
        m_timerthread.start();

    }

    final public void Connect_gd(int timeout, Activity activity, OnConnectListener listener) {
        i_connectlistener = listener;
        if (m_blestate != BLE_STATE_FREE) {
            BleState.getInstance().Log("zh不可重复连接");
            if (i_connectlistener != null)
                i_connectlistener.onConnectFail(OnConnectListener.RTCODE_ALREADY_CONN);
            return;
        }
        TIME_OUT = timeout;
        m_context = activity;
        //开始扫描
        m_blestate = BLE_STATE_SCANNING;
        mBluetoothManager = (BluetoothManager) activity.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = mBluetoothManager.getAdapter();

        if (mBluetoothAdapter == null) {
            BleState.getInstance().Log("获取蓝牙adapter失败");
            if (i_connectlistener != null)
                i_connectlistener.onConnectFail(OnConnectListener.RTCODE_ADAPTER_NULL);
            m_blestate = BLE_STATE_FREE;
            return;
        } else
            BleState.getInstance().Log("获取蓝牙adapter成功");

        if (!mBluetoothAdapter.isEnabled()) {
            BleState.getInstance().Log("本机蓝牙尚未打开");
            if (i_connectlistener != null)
                i_connectlistener.onConnectFail(OnConnectListener.RTCODE_BLE_CLOSED);
            m_blestate = BLE_STATE_FREE;
            return;
        } else
            BleState.getInstance().Log("本机蓝牙已打开");

        BleState.getInstance().Log("开始扫描周围的蓝牙设备");
        m_blestate = BLE_STATE_SCANNING;
        mBluetoothAdapter.startLeScan(mLeScanCallback);
        m_timerthread = new TimerCountConn();
        m_timerthread.start();


    }

    final public boolean Disconnect() {
        m_blestate = BLE_STATE_FREE;
        if (mBluetoothGatt != null) {
            mBluetoothGatt.disconnect();
        }

        BleState.getInstance().Log("断开连接");
        return true;
    }

    public void getSingleData(int timeout, Activity activity, byte input[], OnGetSingleDataListener listener) {
        i_singledatalistener = listener;
        i_multidatalistener = null;
        m_input = input;
        //先检查状态，看是否已经连接
        if (m_blestate != BLE_STATE_READY) {
            m_blestate = BLE_STATE_FREE;
            BleState.getInstance().Log("需要先连接");
            if (listener != null)
                listener.onCommFail(OnGetSingleDataListener.RTCODE_GATT_NULL);
            return;
        }
        if (mBluetoothGatt == null || m_send == null || m_receive == null) {
            m_blestate = BLE_STATE_FREE;
            BleState.getInstance().Log("需要先连接");
            if (listener != null)
                listener.onCommFail(OnGetSingleDataListener.RTCODE_GATT_NULL);
            return;
        }
        //若已连接，组织发送字节
        TIME_OUT = timeout;
        m_context = activity;
        m_blestate = BLE_STATE_SINGLECOMMING;
        TimerCount timer = new TimerCount();
        timer.start();
        setCharacteristicNotification(m_receive, true);
        Trans trans = new Trans();
        try {
            Thread.sleep(60);
            trans.start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void getSingleData_gd(int timeout, Activity activity, byte input[], OnGetSingleDataListener listener) {
        i_singledatalistener = listener;
        i_multidatalistener = null;
        m_input = input;
        //先检查状态，看是否已经连接
        if (m_blestate != BLE_STATE_READY) {
            m_blestate = BLE_STATE_FREE;
            BleState.getInstance().Log("需要先连接");
            if (listener != null)
                listener.onCommFail(OnGetSingleDataListener.RTCODE_GATT_NULL);
            return;
        }
        if (mBluetoothGatt == null || m_send == null || m_receive == null) {
            m_blestate = BLE_STATE_FREE;
            BleState.getInstance().Log("需要先连接");
            if (listener != null)
                listener.onCommFail(OnGetSingleDataListener.RTCODE_GATT_NULL);
            return;
        }
        //若已连接，组织发送字节
        TIME_OUT = timeout;
        m_context = activity;
        m_blestate = BLE_STATE_SINGLECOMMING;
        TimerCount timer = new TimerCount();
        timer.start();
        setCharacteristicNotification_gd(m_receive, true);
        Trans_gd trans = new Trans_gd();
        trans.start();
    }

    public void getSingleData1(int timeout, Activity activity, byte input[], OnGetSingleDataListener listener) {
        i_singledatalistener = listener;
        i_multidatalistener = null;
        m_input = input;
        //先检查状态，看是否已经连接
        if (m_blestate != BLE_STATE_READY) {
            m_blestate = BLE_STATE_FREE;
            BleState.getInstance().Log("需要先连接");
            if (listener != null)
                listener.onCommFail(OnGetSingleDataListener.RTCODE_GATT_NULL);
            return;
        }
        if (mBluetoothGatt == null || m_send == null || m_receive == null) {
            m_blestate = BLE_STATE_FREE;
            BleState.getInstance().Log("需要先连接");
            if (listener != null)
                listener.onCommFail(OnGetSingleDataListener.RTCODE_GATT_NULL);
            return;
        }
        //若已连接，组织发送字节
        TIME_OUT = timeout;
        m_context = activity;
        m_blestate = BLE_STATE_SINGLECOMMING;
        TimerCount timer = new TimerCount();
        timer.start();
        setCharacteristicNotification(m_receive, true);
        Trans1 trans = new Trans1();
        try {
            Thread.sleep(10);
            trans.start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void getSingleData2(int timeout, Activity activity, byte input[], OnGetSingleDataListener listener) {
        i_singledatalistener = listener;
        i_multidatalistener = null;
        m_input = input;
        //先检查状态，看是否已经连接
        if (m_blestate != BLE_STATE_READY) {
            m_blestate = BLE_STATE_FREE;
            BleState.getInstance().Log("需要先连接");
            if (listener != null)
                listener.onCommFail(OnGetSingleDataListener.RTCODE_GATT_NULL);
            return;
        }
        if (mBluetoothGatt == null || m_send == null || m_receive == null) {
            m_blestate = BLE_STATE_FREE;
            BleState.getInstance().Log("需要先连接");
            if (listener != null)
                listener.onCommFail(OnGetSingleDataListener.RTCODE_GATT_NULL);
            return;
        }
        //若已连接，组织发送字节
        TIME_OUT = timeout;
        m_context = activity;
        m_blestate = BLE_STATE_SINGLECOMMING;
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == MSG_UPDATE_TIMER) {
                    if (timeout != 4)
                        ShowProgressBar(m_context, "运行中...", String.format("采集已进行%d秒", msg.arg1));
                } else if (msg.what == MSG_UPDATE_TIMER_CONN) {
                    if (timeout != 4)
                        ShowProgressBar(m_context, "蓝牙连接中...", String.format("已进行%d秒", msg.arg1));
                } else if (msg.what == MSG_TIMER_EXPIRES) {
                    //蓝牙结束
                    //依本设备蓝牙状态而定
                    if (m_blestate == BLE_STATE_SCANNING) {
                        m_blestate = BLE_STATE_FREE;
                        mBluetoothAdapter.stopLeScan(mLeScanCallback);
                        BleState.getInstance().Log("蓝牙连接超时");
                        i_connectlistener.onConnectTimeOut();
                    } else if (m_blestate == BLE_STATE_SINGLECOMMING) {
                        m_blestate = BLE_STATE_READY;
                        i_singledatalistener.onCommTimeOut();
                    } else if (m_blestate == BLE_STATE_MULTICOMMING) {
                        m_blestate = BLE_STATE_READY;
                        i_multidatalistener.onCommEnd();
                    }
                    HideProgressBar();
                } else if (msg.what == MSG_SCAN_MATCH) {
                    //停止查找设备
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    BleState.getInstance().Log(String.format("zh停止查找设备"));
                    //开始连接
                    if (m_blestate != BLE_STATE_CONNECTING) {
                        BleState.getInstance().Log(String.format("zh尝试连接[%s]", mDevice.getName()));
                        mBluetoothGatt = mDevice.connectGatt(m_context, false, mGattCallback);
                        m_blestate = BLE_STATE_CONNECTING;

                    }
                } else if (msg.what == MSG_DISCOVER_SERVICE) {
                    //连接上，开始查找服务
                    if (mBluetoothGatt != null)
                        mBluetoothGatt.discoverServices();
                    else {
                        BleState.getInstance().Log(String.format("获取gatt失败"));
                        i_connectlistener.onConnectFail(OnConnectListener.RTCODE_GATT_NULL);
                    }
                } else if (msg.what == MSG_CONN_SUCCESS) {
                    BleState.getInstance().Log(String.format("连接成功，回调"));
                    HideProgressBar();
                    if (i_connectlistener != null)
                        i_connectlistener.onConnectSuccess();
                } else if (msg.what == MSG_READ_SUCCESS) {
                    if (m_blestate == BLE_STATE_SINGLECOMMING) {
                        m_blestate = BLE_STATE_READY;
                        if (i_singledatalistener != null)
                            i_singledatalistener.onCommSuccess(m_bytes);
                        HideProgressBar();
                        clearAllBytes();
                    } else if (m_blestate == BLE_STATE_MULTICOMMING)        //尝试读取多个数据
                    {
                        if (i_multidatalistener != null) {
                            if (m_bytes != null) {
                                byte rt[] = new byte[m_bytes.length];
                                System.arraycopy(m_bytes, 0, rt, 0, m_bytes.length);
                                i_multidatalistener.onCommSuccess(rt);
                            }
                        }
                        if (clearBytes() == 1) {
                            Message newmsg = obtainMessage();
                            newmsg.what = MSG_NEED_TREAT;
                            newmsg.sendToTarget();
                        }
                    }
                } else if (msg.what == MSG_READ_FAIL) {
                    BleState.getInstance().Log("收到的字节不符合协议，删除");
                    showbytes();
                    clearAllBytes();
                    if (m_blestate == BLE_STATE_SINGLECOMMING) {
                        m_blestate = BLE_STATE_READY;
                        if (i_singledatalistener != null)
                            i_singledatalistener.onCommFail(OnGetSingleDataListener.RTCODE_FAIL);
                        HideProgressBar();
                    } else if (m_blestate == BLE_STATE_MULTICOMMING) {

                    }
                } else if (msg.what == MSG_NEED_TREAT) {
                    int checkinput = CheckInput();
                    if (checkinput < 0)        //校验失败
                    {
                        //该处理了
                        Message newmsg = obtainMessage();
                        newmsg.what = MSG_READ_FAIL;
                        newmsg.sendToTarget();
                    } else if (checkinput == 0)        //尚未接收完全
                    {
                        BleState.getInstance().Log("现在有" + m_bytes.length + "个字节，等下一帧");
                    } else if (checkinput == 1) {
                        //该处理了
                        Message newmsg = obtainMessage();
                        newmsg.what = MSG_READ_SUCCESS;
                        newmsg.sendToTarget();
                    }
                } else if (msg.what == MSG_DISCONNECT) {
                    if (i_connectlistener != null) {
                        i_connectlistener.onDisconnect();
                    }
                }
            }
        };
        new Thread() {
            public void run() {
                try {
                    m_timer = 0;
                    while (m_timer <= TIME_OUT) {
                        Thread.sleep(1000);
                        if (m_blestate == BLE_STATE_READY)
                            break;
                        Message msg = handler.obtainMessage();
                        msg.what = MSG_UPDATE_TIMER;
                        msg.arg1 = m_timer;
                        msg.sendToTarget();
                        m_timer++;
                    }
                    if (m_blestate == BLE_STATE_READY)
                        return;
                    Message msg = handler.obtainMessage();
                    msg.what = MSG_TIMER_EXPIRES;
                    msg.sendToTarget();
                } catch (Exception e) {

                }
            }
        }
                .start();
        setCharacteristicNotification(m_receive, true);
        Trans trans = new Trans();
        try {
            Thread.sleep(60);
            trans.start();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void getMultiData(int timeout, Activity activity, byte input[], OnGetMultiDataListener listener) {
        i_singledatalistener = null;
        i_multidatalistener = listener;
        m_input = input;
        //先检查状态，看是否已经连接
        if (m_blestate != BLE_STATE_READY) {
            m_blestate = BLE_STATE_FREE;
            BleState.getInstance().Log("需要先连接");
            if (listener != null)
                listener.onCommFail(OnGetMultiDataListener.RTCODE_GATT_NULL);
            return;
        }
        if (mBluetoothGatt == null || m_send == null || m_receive == null) {
            m_blestate = BLE_STATE_FREE;
            BleState.getInstance().Log("需要先连接");
            if (listener != null)
                listener.onCommFail(OnGetMultiDataListener.RTCODE_GATT_NULL);
            return;
        }
        //若已连接，组织发送字节
        TIME_OUT = timeout;
        m_context = activity;
        m_blestate = BLE_STATE_MULTICOMMING;
        TimerCount timer = new TimerCount();
        timer.start();
        setCharacteristicNotification(m_receive, true);
        Trans trans = new Trans();
        try {
            Thread.sleep(60);
            trans.start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public interface OnConnectListener {
        public void onConnectSuccess();

        public void onConnectTimeOut();

        public void onConnectFail(int rtcode);

        public void onDisconnect();

        public final int RTCODE_SUCCESS = 1;
        public final int RTCODE_TIMEROUT = 2;
        public final int RTCODE_ADAPTER_NULL = 3;
        public final int RTCODE_BLE_CLOSED = 4;
        public final int RTCODE_GATT_NULL = 5;
        public final int RTCODE_SERVICE_FAIL = 6;
        public final int RTCODE_ALREADY_CONN = 7;
        public final int RTCODE_WRITE_CHARA_FAIL = 8;
        public final int RTCODE_READ_CHARA_FAIL = 9;
        public final int RTCODE_STOP = 10;
    }

    public interface OnGetSingleDataListener {
        public void onCommSuccess(byte result[]);

        public void onCommTimeOut();

        public void onCommFail(int rtcode);

        public void onCommStop();

        public final int RTCODE_SUCCESS = 1;
        public final int RTCODE_TIMEROUT = 2;
        public final int RTCODE_GATT_NULL = 5;
        public final int RTCODE_FAIL = 6;
        public final int RTCODE_STOP = 10;
    }

    public interface OnGetMultiDataListener {
        public void onCommSuccess(byte result[]);

        public void onCommTimeOut();

        public void onCommEnd();

        public void onCommFail(int rtcode);

        public final int RTCODE_SUCCESS = 1;
        public final int RTCODE_TIMEROUT = 2;
        public final int RTCODE_GATT_NULL = 5;
        public final int RTCODE_FAIL = 6;
        public final int RTCODE_END = 7;
    }

    //进度条线程
    class TimerCount extends Thread {
        public void run() {
            try {
                m_timer = 0;
                while (m_timer <= TIME_OUT) {
                    Thread.sleep(1000);
                    if (m_blestate == BLE_STATE_READY)
                        break;
                    Message msg = handler.obtainMessage();
                    msg.what = MSG_UPDATE_TIMER;
                    msg.arg1 = m_timer;
                    msg.sendToTarget();
                    m_timer++;
                }
                if (m_blestate == BLE_STATE_READY)
                    return;
                Message msg = handler.obtainMessage();
                msg.what = MSG_TIMER_EXPIRES;
                msg.sendToTarget();
            } catch (Exception e) {

            }
        }
    }

    ;

    //进度条线程
    class TimerCountConn extends Thread {

        public void run() {
            try {
                m_timer = 0;
                while (m_timer <= TIME_OUT) {
                    Thread.sleep(1000);
                    if (m_blestate == BLE_STATE_READY)
                        break;
                    Message msg = handler.obtainMessage();
                    msg.what = MSG_UPDATE_TIMER_CONN;
                    msg.arg1 = m_timer;
                    msg.sendToTarget();
                    m_timer++;
                }
                if (m_blestate == BLE_STATE_READY)
                    return;
                Message msg = handler.obtainMessage();
                msg.what = MSG_TIMER_EXPIRES;
                msg.sendToTarget();
            } catch (Exception e) {

            }
        }

    }

    ;

    class Trans extends Thread {
        public byte[] b = m_input;

        public void run() {
            try {
                int length = b.length;
                int repeat = 0;
                byte[] tmp = new byte[20];
                while (length - repeat * 20 > 0) {
                    if (length - repeat * 20 >= 20) {
                        for (int i = 0; i < 20; i++) {
                            tmp[i] = b[i + repeat * 20];
                        }
                    } else {
                        for (int i = 0; i < length - repeat * 20; i++) {
                            tmp[i] = b[i + repeat * 20];
                        }
                    }
                    repeat++;
                    m_send.setValue(tmp);
                    mBluetoothGatt.writeCharacteristic(m_send);
                    BleState.getInstance().Log(String.format("zh发送%d字节" + "Trans", tmp.length));
                    Thread.sleep(40);
                }        //end of while
                Thread.sleep(100);
            } catch (Exception e) {

            }
        }
    }

    class Trans1 extends Thread {
        public byte[] b = m_input;

        public void run() {
            try {
                int length = b.length;
                int repeat = 0;
                byte[] tmp = new byte[20];
                while (length - repeat * 20 > 0) {
                    if (length - repeat * 20 >= 20) {
                        for (int i = 0; i < 20; i++) {
                            tmp[i] = b[i + repeat * 20];
                        }
                    } else {
                        for (int i = 0; i < length - repeat * 20; i++) {
                            tmp[i] = b[i + repeat * 20];
                        }
                    }
                    repeat++;
                    m_send.setValue(tmp);
                    mBluetoothGatt.writeCharacteristic(m_send);
                    BleState.getInstance().Log(String.format("zh发送%d字节" + "Trans1", tmp.length));
                    Thread.sleep(40);
                }        //end of while
                Thread.sleep(100);
            } catch (Exception e) {

            }
        }
    }

    class Trans_gd extends Thread {
        public byte[] b = m_input;

        public void run() {
            try {
                int length = b.length;
                int repeat = 0;
                byte[] tmp = new byte[24];
                while (length - repeat * 24 > 0) {
                    if (length - repeat * 24 >= 24) {
                        for (int i = 0; i < 24; i++) {
                            tmp[i] = b[i + repeat * 24];
                        }
                    } else {
                        for (int i = 0; i < length - repeat * 24; i++) {
                            tmp[i] = b[i + repeat * 24];
                        }
                    }
                    repeat++;
                    m_send.setValue(tmp);
                    mBluetoothGatt.writeCharacteristic(m_send);
                    BleState.getInstance().Log(String.format("zh发送%d字节", tmp.length));
                    Thread.sleep(10);
                }        //end of while
            } catch (Exception e) {

            }
        }
    }

    protected final int MSG_UPDATE_TIMER = 1;
    protected final int MSG_UPDATE_TIMER_CONN = 10;
    protected final int MSG_TIMER_EXPIRES = 2;
    protected final int MSG_SCAN_MATCH = 3;            //扫描到匹配的设备
    protected final int MSG_DISCOVER_SERVICE = 4;        //开始查找服务，取读写的charactor
    protected final int MSG_CONN_SUCCESS = 5;        //连接成功
    protected final int MSG_READ_SUCCESS = 6;        //读到了数据，且校验成功
    protected final int MSG_READ_FAIL = 7;        //读到了错误的数据
    protected final int MSG_NEED_TREAT = 8;        //缓存中有数据需要处理
    protected final int MSG_DISCONNECT = 9;        //断开事件
    protected Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG_UPDATE_TIMER) {
                ShowProgressBar(m_context, "运行中...", String.format("采集已进行%d秒", msg.arg1));
            } else if (msg.what == MSG_UPDATE_TIMER_CONN) {
                ShowProgressBar(m_context, "蓝牙连接中...", String.format("已进行%d秒", msg.arg1));
            } else if (msg.what == MSG_TIMER_EXPIRES) {
                //蓝牙结束
                //依本设备蓝牙状态而定
                if (m_blestate == BLE_STATE_SCANNING) {
                    m_blestate = BLE_STATE_FREE;
//					mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    BleState.getInstance().Log("蓝牙连接超时");
                    i_connectlistener.onConnectTimeOut();
                } else if (m_blestate == BLE_STATE_SINGLECOMMING) {
                    m_blestate = BLE_STATE_READY;
                    i_singledatalistener.onCommTimeOut();
                } else if (m_blestate == BLE_STATE_MULTICOMMING) {
                    m_blestate = BLE_STATE_READY;
                    i_multidatalistener.onCommEnd();
                }
                HideProgressBar();
            } else if (msg.what == MSG_SCAN_MATCH) {
//				//停止查找设备
//				mBluetoothAdapter.stopLeScan(mLeScanCallback);
//				BleState.getInstance().Log(String.format("zh停止查找设备"));
                //开始连接
                if (m_blestate != BLE_STATE_CONNECTING) {
                    BleState.getInstance().Log(String.format("zh尝试连接[%s]", m_name));
                    mBluetoothGatt = mDevice.connectGatt(m_context, false, mGattCallback);
                    m_blestate = BLE_STATE_CONNECTING;

                }
            } else if (msg.what == MSG_DISCOVER_SERVICE) {
                //连接上，开始查找服务
                if (mBluetoothGatt != null)
                    mBluetoothGatt.discoverServices();
                else {
                    BleState.getInstance().Log(String.format("获取gatt失败"));
                    i_connectlistener.onConnectFail(OnConnectListener.RTCODE_GATT_NULL);
                }
            } else if (msg.what == MSG_CONN_SUCCESS) {
                BleState.getInstance().Log(String.format("连接成功，回调"));
                HideProgressBar();
                if (i_connectlistener != null)
                    i_connectlistener.onConnectSuccess();
            } else if (msg.what == MSG_READ_SUCCESS) {
                if (m_blestate == BLE_STATE_SINGLECOMMING) {
                    m_blestate = BLE_STATE_READY;
                    if (i_singledatalistener != null)
                        i_singledatalistener.onCommSuccess(m_bytes);
                    HideProgressBar();
                    clearAllBytes();
                } else if (m_blestate == BLE_STATE_MULTICOMMING)        //尝试读取多个数据
                {
                    if (i_multidatalistener != null) {
                        if (m_bytes != null) {
                            byte rt[] = new byte[m_bytes.length];
                            System.arraycopy(m_bytes, 0, rt, 0, m_bytes.length);
                            i_multidatalistener.onCommSuccess(rt);
                        }
                    }
                    if (clearBytes() == 1) {
                        Message newmsg = handler.obtainMessage();
                        newmsg.what = MSG_NEED_TREAT;
                        newmsg.sendToTarget();
                    }
                }
            } else if (msg.what == MSG_READ_FAIL) {
                BleState.getInstance().Log("收到的字节不符合协议，删除");
                showbytes();
                clearAllBytes();
                if (m_blestate == BLE_STATE_SINGLECOMMING) {
                    m_blestate = BLE_STATE_READY;
                    if (i_singledatalistener != null)
                        i_singledatalistener.onCommFail(OnGetSingleDataListener.RTCODE_FAIL);
                    HideProgressBar();
                } else if (m_blestate == BLE_STATE_MULTICOMMING) {

                }
            } else if (msg.what == MSG_NEED_TREAT) {
                int checkinput = CheckInput();
                if (checkinput < 0)        //校验失败
                {
                    //该处理了
                    Message newmsg = handler.obtainMessage();
                    newmsg.what = MSG_READ_FAIL;
                    newmsg.sendToTarget();
                } else if (checkinput == 0)        //尚未接收完全
                {
                    BleState.getInstance().Log("现在有" + m_bytes.length + "个字节，等下一帧");
                } else if (checkinput == 1) {
                    //该处理了
                    Message newmsg = handler.obtainMessage();
                    newmsg.what = MSG_READ_SUCCESS;
                    newmsg.sendToTarget();
                }
            } else if (msg.what == MSG_DISCONNECT) {
                if (i_connectlistener != null) {
                    i_connectlistener.onDisconnect();
                }
            }
        }
    };

    //蓝牙扫描结果回调
    protected LeScanCallback mLeScanCallback = new LeScanCallback() {
        @SuppressWarnings("deprecation")
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {

            String btname = device.getName();
//				String btaddress  = device.getAddress();
            BleState.getInstance().Log(String.format("扫描到名为%s的设备", btname));
            if (btname != null && btname.equals(m_name) && m_blestate == BLE_STATE_SCANNING) {
                //停止查找设备
                mBluetoothAdapter.stopLeScan(mLeScanCallback);
                BleState.getInstance().Log(String.format("zh停止查找设备"));

                mDevice = device;
//					BleState.getInstance().Log(String.format("匹配成功",btname));
                Message msg = handler.obtainMessage();
                msg.what = MSG_SCAN_MATCH;
                msg.sendToTarget();
            }/*if (btaddress.equals(m_address) && m_blestate == BLE_STATE_SCANNING)
				{
					mDevice = device;
					BleState.getInstance().Log(String.format("匹配成功",btname));
					Message msg = handler.obtainMessage();
					msg.what = MSG_SCAN_MATCH;
					msg.sendToTarget();
				}*/ else {
                BleState.getInstance().Log(String.format("放弃名为%s的设备", btname));
            }
        }
    };

    //蓝牙状态变化回调
    protected BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                BleState.getInstance().Log(String.format("连接[%s]成功，开始查找服务", mDevice.getName()));
                m_blestate = BLE_STATE_DISCOVERING;
                Message msg = handler.obtainMessage();
                msg.what = MSG_DISCOVER_SERVICE;
                msg.sendToTarget();
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                BleState.getInstance().Log(("zhdisconnect成功"));
                mBluetoothGatt.close();
                mBluetoothGatt = null;
                if (mDevice != null)
                    mDevice = null;
                if (mBluetoothAdapter != null) {
                    mBluetoothManager = null;
                    mBluetoothAdapter = null;
                }
                Message msg = handler.obtainMessage();
                msg.what = MSG_DISCONNECT;
                msg.sendToTarget();
                System.gc();
                m_blestate = BLE_STATE_FREE;
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                BleState.getInstance().Log(String.format("查找服务成功"));
                String uuid = "";
                List<BluetoothGattService> gattServices = mBluetoothGatt.getServices();
                m_send = null;
                m_receive = null;
                for (BluetoothGattService gattService : gattServices) {
                    List<BluetoothGattCharacteristic> gattCharacteristics = gattService.getCharacteristics();
                    for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                        uuid = gattCharacteristic.getUuid().toString();
                        if (uuid.equals(m_str_writechara)) {
                            m_send = gattCharacteristic;
                            BleState.getInstance().Log(String.format("发送Chara" + m_str_writechara));
                        }
                        if (uuid.equals(m_str_readchara)) {
                            int charaProp = gattCharacteristic.getProperties();
                            if ((charaProp | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
                                m_receive = gattCharacteristic;
                                BleState.getInstance().Log(String.format("接收Chara" + m_str_writechara));
                            }
                        }
                        if (uuid.equals(m_str_writechara_gd)) {
                            int charaProp = gattCharacteristic.getProperties();
                            if ((charaProp | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
                                m_send = gattCharacteristic;
                                BleState.getInstance().Log(String.format("接收Chara" + m_str_writechara));
                            }
                        }
                        if (uuid.equals(m_str_readchara_gd)) {
                            int charaProp = gattCharacteristic.getProperties();
                            if ((charaProp | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
                                m_receive = gattCharacteristic;
                                BleState.getInstance().Log(String.format("接收Chara" + m_str_writechara));
                            }
                        }
                    }        //end of for characteristics
                }        //end of for service
                if (m_send == null) {
                    m_blestate = BLE_STATE_FREE;
                    BleState.getInstance().Log("无法获得write characteristic，请注意该蓝牙芯片的设置");
                    if (i_connectlistener != null)
                        i_connectlistener.onConnectFail(OnConnectListener.RTCODE_WRITE_CHARA_FAIL);
                    return;
                }
                if (m_receive == null) {
                    m_blestate = BLE_STATE_FREE;
                    BleState.getInstance().Log("无法获得read characteristic，请注意该蓝牙芯片的设置");
                    if (i_connectlistener != null)
                        i_connectlistener.onConnectFail(OnConnectListener.RTCODE_READ_CHARA_FAIL);
                    return;
                }
                if (m_send != null && m_receive != null) {
                    m_blestate = BLE_STATE_READY;
                    Message msg = handler.obtainMessage();
                    msg.what = MSG_CONN_SUCCESS;
                    msg.sendToTarget();
                }
            } else {
                //查找服务失败
                BleState.getInstance().Log(String.format("获取service失败"));
                i_connectlistener.onConnectFail(OnConnectListener.RTCODE_SERVICE_FAIL);
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            byte[] receivedata = characteristic.getValue();
            if (receivedata == null || receivedata.length <= 0)
                return;
            BleState.getInstance().Log("收到notify数据");
            addBytes(receivedata);

            showbytes();

            Message msg = handler.obtainMessage();
            msg.what = MSG_NEED_TREAT;
            msg.sendToTarget();
        }
    };

    //打开某个characteristic的收听功能
    void setCharacteristicNotification(BluetoothGattCharacteristic characteristic, boolean enabled) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            return;
        }
        mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);
        //根据UUID来发送指令
        BluetoothGattDescriptor descriptor = characteristic.getDescriptor(UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"));
//		BluetoothGattDescriptor descriptor = characteristic.getDescriptor(UUID.fromString("0000ff12-0000-1000-8000-00805f9b34fb"));
        if (descriptor != null) {
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            mBluetoothGatt.writeDescriptor(descriptor);
        }
    }

    //打开某个characteristic的收听功能
    void setCharacteristicNotification_gd(BluetoothGattCharacteristic characteristic, boolean enabled) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            return;
        }
        mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);
        //根据UUID来发送指令
//		BluetoothGattDescriptor descriptor = characteristic.getDescriptor(UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"));
//		BluetoothGattDescriptor descriptor = characteristic.getDescriptor(UUID.fromString("0000ff12-0000-1000-8000-00805f9b34fb"));
        BluetoothGattDescriptor descriptor = characteristic.getDescriptor(UUID.fromString("224ADC47-D9A1-4257-AC0B-C817996A54C5"));
        if (descriptor != null) {
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            mBluetoothGatt.writeDescriptor(descriptor);
        }
    }

    //将收到的数据添加到m_bytes里
    void addBytes(byte[] b) {
        if (b == null)
            return;
        if (m_bytes == null) {
            m_bytes = new byte[b.length];
            System.arraycopy(b, 0, m_bytes, 0, b.length);
        } else {
            int length_o = m_bytes.length;
            int length_n = b.length;
            byte rt[] = new byte[length_o + length_n];
            System.arraycopy(m_bytes, 0, rt, 0, length_o);
            System.arraycopy(b, 0, rt, length_o, length_n);
            m_bytes = rt;
        }
    }

    //清除接收区
    protected void clearAllBytes() {
        m_bytes = null;
    }

    //清除接收区符合格式的那一部分
    abstract public int clearBytes();

    //检查收到的数据是否合乎规则
    //返回值
    //-1：不合规则，全删除
    //0:未收完，不确定是否合乎规则
    //1:合规则，可以进入下一步了
    //－2:校验错误
    //-3：收到的命令号与当前命令号不一致
    abstract protected int CheckInput();

    public void stop() {
        if (m_timerthread != null)
            m_timer = TIME_OUT + 1;
    }

    void showbytes() {
        if (m_bytes == null) {
            BleState.getInstance().Log("没有接收缓冲区");
            return;
        }
        String redata = "";
        for (int i = 0; i < m_bytes.length; i++) {
            redata = redata + Integer.toHexString(m_bytes[i] & 0xFF);
            redata = redata + " ";
        }
        BleState.getInstance().Log(redata);
    }
}
