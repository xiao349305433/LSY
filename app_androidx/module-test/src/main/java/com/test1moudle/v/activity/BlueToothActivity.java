package com.test1moudle.v.activity;

import android.bluetooth.BluetoothGatt;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.test1moudle.R;
import com.test1moudle.p.adapter.BlueToothBinder;
import com.wu.loushanyun.base.util.DataParser;
import com.wu.loushanyun.basemvp.init.BleBlueToothTool;
import com.wu.loushanyun.basemvp.m.Interface.MBleGattCallback;
import com.wu.loushanyun.basemvp.m.Interface.MBleNotifyCallback;
import com.wu.loushanyun.basemvp.m.Interface.MBleScanCallback;
import com.wu.loushanyun.basemvp.m.Interface.MBleWriteCallback;

import java.util.ArrayList;
import java.util.List;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.activity.BaseNoPresenterActivity;
import met.hx.com.base.base.multitype.MultiTypeAdapter;
import met.hx.com.base.baseconfig.C;
import met.hx.com.base.baseevent.Event;

@Route(path = C.BlueToothActivity)
public class BlueToothActivity extends BaseNoPresenterActivity implements MBleScanCallback, MBleGattCallback, MBleWriteCallback, MBleNotifyCallback {
    private MultiTypeAdapter multiTypeAdapter;
    private List<BleDevice> bleDevices;

    private RecyclerView recyclerview;
    private BleBlueToothTool bleBlueToothTool;
    private BleDevice bleDevice;


    private String SUUID = "0000ffe0-0000-1000-8000-00805f9b34fb";
    //    private String WUUID="0000ffe0-0000-1000-8000-00805f9b34fb";
    private String WUUID = "0000ffe1-0000-1000-8000-00805f9b34fb";

    @Override
    protected void initLifeCycle() {
        super.initLifeCycle();
        bleBlueToothTool = new BleBlueToothTool(SUUID,WUUID);
        registerLifeCycle(bleBlueToothTool);
    }

    @Override
    protected void initEventListener() {

    }

    @Override
    protected void onEventRunEnd(Event event, int code) {

    }

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        ba.mActivityLayoutId = R.layout.m_test_activity_demo1;
        ba.mHasTitle = true;
        ba.mTitleText = "附近蓝牙列表";
    }

    @Override
    protected void initView() {
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        multiTypeAdapter = new MultiTypeAdapter();
        bleDevices = new ArrayList<>();
        multiTypeAdapter.register(BleDevice.class, new BlueToothBinder(new BlueToothBinder.OnClickTextListener() {
            @Override
            public void onClickText(BleDevice item) {
                BlueToothActivity.this.bleDevice = item;
                bleBlueToothTool.connect(item, BlueToothActivity.this,BlueToothActivity.this);
            }

            @Override
            public void onClickButton(BleDevice item) {
                bleBlueToothTool.write(bleDevice, DataParser.CMD_CHECK, BlueToothActivity.this);
            }
        }));
        multiTypeAdapter.setItems(bleDevices);
        recyclerview.setAdapter(multiTypeAdapter);
        bleBlueToothTool.startScan(this);
    }

    @Override
    public void onStartConnect() {

    }

    @Override
    public void onConnectFail(BleDevice bleDevice, BleException exception) {

    }

    @Override
    public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {

    }

    @Override
    public void onDisConnected(boolean isActiveDisConnected, BleDevice device, BluetoothGatt gatt, int status) {

    }

    @Override
    public void onNotifySuccess() {

    }

    @Override
    public void onNotifyFailure(BleException exception) {

    }

    @Override
    public void onCharacteristicChanged(byte[] data) {

    }

    @Override
    public void onScanStarted(boolean success) {

    }

    @Override
    public void onScanning(BleDevice bleDevice) {
        bleDevices.add(bleDevice);
        multiTypeAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLeScan(BleDevice bleDevice) {

    }

    @Override
    public void onScanFinished(List<BleDevice> scanResultList) {

    }

    @Override
    public void onWriteSuccess(int current, int total, byte[] justWrite) {

    }

    @Override
    public void onWriteFailure(BleException exception) {

    }
}
