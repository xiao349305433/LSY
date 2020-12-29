package com.wu.loushanyun.basemvp.m.Interface;

import android.bluetooth.BluetoothGatt;

import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;

public interface MBleGattCallback {
    public void onStartConnect();

    public void onConnectFail(BleDevice bleDevice, BleException exception);

    public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status);

    public void onDisConnected(boolean isActiveDisConnected, BleDevice device, BluetoothGatt gatt, int status);
}
