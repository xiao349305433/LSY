package com.wu.loushanyun.basemvp.m.Interface;

import com.clj.fastble.data.BleDevice;

import java.util.List;

public interface MBleScanCallback {
    void onScanStarted(boolean success);

    void onScanning(BleDevice bleDevice);

    void onLeScan(BleDevice bleDevice);

    public abstract void onScanFinished(List<BleDevice> scanResultList);

}
