package com.wu.loushanyun.basemvp.m.Interface;

import com.clj.fastble.exception.BleException;

public interface MBleNotifyCallback {
    public abstract void onNotifySuccess();

    public abstract void onNotifyFailure(BleException exception);

    public abstract void onCharacteristicChanged(byte[] data);
}
