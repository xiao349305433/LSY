package com.wu.loushanyun.basemvp.init;

import android.app.Activity;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.elvishew.xlog.XLog;
import com.wu.loushanyun.base.util.DataParser;
import com.wu.loushanyun.basemvp.m.Interface.MBleGattCallback;
import com.wu.loushanyun.basemvp.m.Interface.MBleNotifyCallback;
import com.wu.loushanyun.basemvp.m.Interface.MBleScanCallback;
import com.wu.loushanyun.basemvp.m.Interface.MBleWriteCallback;

import java.util.ArrayList;
import java.util.List;

import met.hx.com.base.base.application.AppContext;
import met.hx.com.base.base.dialog.LoadingDialogUtil;
import met.hx.com.base.base.rx.FlowableUtil;
import met.hx.com.base.baseinterface.RxTimerListener;
import met.hx.com.base.baseinterface.SimpleLifeCycle;
import met.hx.com.librarybase.some_utils.XActivityUtils;

public class BleBlueToothTool extends SimpleLifeCycle {
    public static final int PROPERTY_READ = 1;
    public static final int PROPERTY_WRITE = 2;
    public static final int PROPERTY_WRITE_NO_RESPONSE = 3;
    public static final int PROPERTY_NOTIFY = 4;
    public static final int PROPERTY_INDICATE = 5;
    public static final int TIME_OUT = 10000;//操作超时
    public static final int WRITE_TIME_DELAY = 500;//发送命令延迟
    public static final int CONNECT_TIME_DELAY = 1500;//连接蓝牙延迟
    public String loadingTag;
    public String uuid_service;
    public String uuid_write;

    public static void init() {
        BleManager.getInstance().init(AppContext.getInstance().getApplication());
        BleManager.getInstance()
                .enableLog(false)
                .setConnectOverTime(TIME_OUT)
                .setOperateTimeout(TIME_OUT);
    }

    public BleBlueToothTool(String uuid_service, String uuid_write) {
        this.uuid_service = uuid_service;
        this.uuid_write = uuid_write;
    }

    public void printAllUUID(BluetoothGatt gatt) {
        XLog.i("连接成功：" + gatt.toString());
        for (BluetoothGattService service : gatt.getServices()) {
            XLog.i("---1--服务UUID-----：" + service.getUuid());
            XLog.i("     服务Type：" + service.getType());
            for (BluetoothGattCharacteristic characteristic : service.getCharacteristics()) {
                XLog.i("     ---2--服务下的UUID-----：" + characteristic.getUuid());
                XLog.i("         --服务下的TYPE-----：" + characteristic.getWriteType());
                final List<Integer> propList = new ArrayList<>();
                List<String> propNameList = new ArrayList<>();
                int charaProp = characteristic.getProperties();
                if ((charaProp & BluetoothGattCharacteristic.PROPERTY_READ) > 0) {
                    propList.add(PROPERTY_READ);
                    propNameList.add("Read");
                }
                if ((charaProp & BluetoothGattCharacteristic.PROPERTY_WRITE) > 0) {
                    propList.add(PROPERTY_WRITE);
                    propNameList.add("Write");
                }
                if ((charaProp & BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE) > 0) {
                    propList.add(PROPERTY_WRITE_NO_RESPONSE);
                    propNameList.add("Write No Response");
                }
                if ((charaProp & BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
                    propList.add(PROPERTY_NOTIFY);
                    propNameList.add("Notify");
                }
                if ((charaProp & BluetoothGattCharacteristic.PROPERTY_INDICATE) > 0) {
                    propList.add(PROPERTY_INDICATE);
                    propNameList.add("Indicate");
                }
                XLog.i("         --服务下的特性-----：" + propNameList.toString());
                for (BluetoothGattDescriptor bluetoothGattDescriptor : characteristic.getDescriptors()) {
                    XLog.i("          服务下的UUID下的UUid：" + bluetoothGattDescriptor.getUuid());
                }
            }
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState, Activity activity) {
        super.onCreate(savedInstanceState, activity);
        loadingTag = activity.getClass().getName();
    }

    /**
     * 开始扫描
     *
     * @param bleScanCallback
     */
    public void startScan(MBleScanCallback bleScanCallback) {
        BleManager.getInstance().scan(new BleScanCallback() {
            @Override
            public void onScanFinished(List<BleDevice> scanResultList) {
//                XLog.i("蓝牙模块 onScanFinished");
                bleScanCallback.onScanFinished(scanResultList);
            }

            @Override
            public void onScanStarted(boolean success) {
//                XLog.i("蓝牙模块 onScanStarted");
                bleScanCallback.onScanStarted(success);
            }

            @Override
            public void onScanning(BleDevice bleDevice) {
//                XLog.i("蓝牙模块 onScanning");
                bleScanCallback.onScanning(bleDevice);
            }

            @Override
            public void onLeScan(BleDevice bleDevice) {
//                XLog.i("蓝牙模块 onLeScan");
                bleScanCallback.onLeScan(bleDevice);
            }
        });
    }

    /**
     * 停止扫描
     */
    public void stopScan() {
        BleManager.getInstance().cancelScan();
    }

    /**
     * 连接蓝牙
     *
     * @param bleDevice
     * @param bleGattCallback
     * @param bleNotifyCallback
     */
    public void connect(final BleDevice bleDevice, MBleGattCallback bleGattCallback, MBleNotifyCallback bleNotifyCallback) {
        connect(TIME_OUT, bleDevice, bleGattCallback, bleNotifyCallback);
    }

    /**
     * 连接蓝牙
     *
     * @param milliseconds      超时时间
     * @param bleDevice
     * @param bleGattCallback
     * @param bleNotifyCallback
     */
    public void connect(int milliseconds, final BleDevice bleDevice, MBleGattCallback bleGattCallback, MBleNotifyCallback bleNotifyCallback) {
        stopScan();
        BleManager.getInstance().setConnectOverTime(milliseconds);
        LoadingDialogUtil.showByEvent(true, milliseconds, bleDevice.getName() + "蓝牙连接中", loadingTag);
        compositeDisposable.add(FlowableUtil.createRxTimer(WRITE_TIME_DELAY, new RxTimerListener() {
            @Override
            public void onNext(@NonNull Long number) {
                BleManager.getInstance().connect(bleDevice, new BleGattCallback() {
                    @Override
                    public void onStartConnect() {
                        if (isTopTaskAcitivity()) {
                            XLog.i("蓝牙模块 onStartConnect");
                            bleGattCallback.onStartConnect();
                        }
                    }

                    @Override
                    public void onConnectFail(BleDevice bleDevice, BleException exception) {
                        if (isTopTaskAcitivity()) {
                            LoadingDialogUtil.dismissByEvent(loadingTag);
                            XLog.i("蓝牙模块 onConnectFail");
                            bleGattCallback.onConnectFail(bleDevice, exception);
                        }
                    }

                    @Override
                    public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
                        if (isTopTaskAcitivity()) {
                            LoadingDialogUtil.dismissByEvent(loadingTag);
                            XLog.i("蓝牙模块 onConnectSuccess");
                            bleGattCallback.onConnectSuccess(bleDevice, gatt, status);
                            notifyBlue(bleDevice, uuid_service, uuid_write, bleNotifyCallback);
                        }

                    }

                    @Override
                    public void onDisConnected(boolean isActiveDisConnected, BleDevice device, BluetoothGatt gatt, int status) {
                        if (isTopTaskAcitivity()) {
                            LoadingDialogUtil.dismissByEvent(loadingTag);
                            XLog.i("蓝牙模块 onDisConnected");
                            bleGattCallback.onDisConnected(isActiveDisConnected, device, gatt, status);
                        }
                    }
                });
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onComplete() {

            }
        }));

    }

    /**
     * 写入命令
     *
     * @param bleDevice
     * @param data
     * @param bleWriteCallback
     */
    public void write(BleDevice bleDevice, byte[] data, MBleWriteCallback bleWriteCallback) {
        write(TIME_OUT, bleDevice, uuid_service, uuid_write, data, bleWriteCallback);
    }


    /**
     * 写入命令
     *
     * @param milliseconds     超时时间
     * @param bleDevice
     * @param uuid_service
     * @param uuid_write
     * @param data
     * @param bleWriteCallback
     */
    public void write(int milliseconds, BleDevice bleDevice, String uuid_service, String uuid_write, byte[] data, MBleWriteCallback bleWriteCallback) {
        BleManager.getInstance().setOperateTimeout(milliseconds);
        LoadingDialogUtil.showByEvent(true, milliseconds, bleDevice.getName() + "写入中", loadingTag);

        compositeDisposable.add(FlowableUtil.createRxTimer(CONNECT_TIME_DELAY, new RxTimerListener() {
            @Override
            public void onNext(@NonNull Long number) {
                BleManager.getInstance().write(bleDevice, uuid_service, uuid_write, data, new BleWriteCallback() {
                    @Override
                    public void onWriteSuccess(int current, int total, byte[] justWrite) {
                        if (isTopTaskAcitivity()) {
                            XLog.i("蓝牙模块 onWriteSuccess" + DataParser.byteToString(justWrite));
                            bleWriteCallback.onWriteSuccess(current, total, justWrite);
                        }
                    }

                    @Override
                    public void onWriteFailure(BleException exception) {
                        if (isTopTaskAcitivity()) {
                            LoadingDialogUtil.dismissByEvent(loadingTag);
                            XLog.i("蓝牙模块 onWriteFailure");
                            bleWriteCallback.onWriteFailure(exception);
                        }
                    }
                });
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onComplete() {

            }
        }));


    }

    public void notifyBlue(BleDevice bleDevice, String uuid_service, String uuid_write, MBleNotifyCallback bleWriteCallback) {
        BleManager.getInstance().notify(bleDevice, uuid_service, uuid_write, new BleNotifyCallback() {
            @Override
            public void onNotifySuccess() {
                if (isTopTaskAcitivity()) {
                    XLog.i("蓝牙模块 onNotifySuccess");
                    bleWriteCallback.onNotifySuccess();
                }
            }

            @Override
            public void onNotifyFailure(BleException exception) {
                if (isTopTaskAcitivity()) {
                    XLog.i("蓝牙模块 onNotifyFailure");
                    bleWriteCallback.onNotifyFailure(exception);
                }
            }

            @Override
            public void onCharacteristicChanged(byte[] data) {
                if (isTopTaskAcitivity()) {
                    LoadingDialogUtil.dismissByEvent(loadingTag);
                    XLog.i("蓝牙模块 onCharacteristicChanged" + DataParser.byteToString(data));
                    bleWriteCallback.onCharacteristicChanged(data);
                }
            }
        });

    }

    /**
     * 判断当前显示的activity是否在栈顶
     *
     * @return
     */
    public boolean isTopTaskAcitivity() {
        if (loadingTag.equals(XActivityUtils.getTopActivity().getClass().getName())) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BleManager.getInstance().disconnectAllDevice();
        BleManager.getInstance().destroy();
    }
}
