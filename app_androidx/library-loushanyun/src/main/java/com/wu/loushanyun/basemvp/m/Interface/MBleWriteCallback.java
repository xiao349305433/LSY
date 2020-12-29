package com.wu.loushanyun.basemvp.m.Interface;

import com.clj.fastble.exception.BleException;

public interface MBleWriteCallback {
    public abstract void onWriteSuccess(int current, int total, byte[] justWrite);

    public abstract void onWriteFailure(BleException exception);
}
