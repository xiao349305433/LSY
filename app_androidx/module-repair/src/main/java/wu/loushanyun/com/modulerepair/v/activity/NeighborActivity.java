package wu.loushanyun.com.modulerepair.v.activity;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.elvishew.xlog.XLog;
import com.sensoro.sensor.kit.callback.SensoroDeviceListener;
import com.sensoro.sensor.kit.entity.SensoroDevice;
import com.wu.loushanyun.base.config.K;
import com.wu.loushanyun.base.util.DataParser;
import com.wu.loushanyun.basemvp.init.LSY2InitTypeCode;
import com.wu.loushanyun.basemvp.init.SensoroBlueManager;
import com.wu.loushanyun.basemvp.v.activity.BaseBlueToothActivity;

import java.util.ArrayList;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.multitype.MultiTypeAdapter;
import met.hx.com.base.baseevent.Event;
import met.hx.com.librarybase.some_utils.ToastManager;
import wu.loushanyun.com.modulerepair.R;
import wu.loushanyun.com.modulerepair.p.adapter.SensoroDeviceViewBinder;

@Route(path = K.NeighborActivity)
public class NeighborActivity extends BaseBlueToothActivity {
    private MultiTypeAdapter myAdapter;
    private ArrayList<SensoroDevice> deviceList;
    private SensoroDeviceViewBinder sensoroDeviceViewBinder;
    private RecyclerView recyclerView;
    private SensoroDevice sensoroDevice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    private void initData() {
        deviceList = new ArrayList<>();
        myAdapter = new MultiTypeAdapter();
        sensoroDeviceViewBinder = new SensoroDeviceViewBinder();
        myAdapter.setItems(deviceList);
        myAdapter.register(SensoroDevice.class, sensoroDeviceViewBinder);
        recyclerView = (RecyclerView) findViewById(R.id.data_list);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        sensoroDeviceViewBinder.setOnSensoroDeviceClickListener(new SensoroDeviceViewBinder.OnSensoroDeviceClickListener() {
            @Override
            public void onSensoroDeviceClick(SensoroDevice sensoroDevice) {
                NeighborActivity.this.sensoroDevice = sensoroDevice;
                connectBlueTooth(sensoroDevice);
            }
        });
    }

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        ba.mActivityLayoutId = R.layout.m_repair_activity_neighbor;
        ba.mTitleText = "现场检查";
    }

    @Override
    public void onResume() {
        super.onResume();
        SensoroBlueManager.searchBlueTooth(sensoroDeviceListener);
    }

    private SensoroDeviceListener sensoroDeviceListener=new SensoroDeviceListener<SensoroDevice>() {
        @Override
        public void onNewDevice(SensoroDevice sensoroDevice) {
            XLog.i("蓝牙onNewDevice" + sensoroDevice.toString());
            if (!deviceList.contains(sensoroDevice)) {
                deviceList.add(sensoroDevice);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        myAdapter.notifyDataSetChanged();
                    }
                });
            } else {
                XLog.i("蓝牙sdaasasdasdasdasd" + sensoroDevice.toString());
            }

        }

        @Override
        public void onGoneDevice(SensoroDevice sensoroDevice) {
            XLog.i("蓝牙onGoneDevice" + sensoroDevice.toString());
        }

        @Override
        public void onUpdateDevices(ArrayList<SensoroDevice> arrayList) {
            XLog.i("蓝牙onUpdateDevices" + arrayList.toString());
            for (int i = 0; i < arrayList.size(); i++) {
                SensoroDevice sensoroDevice = arrayList.get(i);
                if (deviceList.contains(sensoroDevice)) {
                    deviceList.set(deviceList.indexOf(sensoroDevice), sensoroDevice);
                } else {
                    deviceList.add(sensoroDevice);
                }
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    myAdapter.notifyDataSetChanged();
                }
            });
        }
    };


    @Override
    protected void onChildConnectFailed(int i) {

    }

    @Override
    protected void onChildConnectSuccess() {
        readPinLv();
    }

    @Override
    protected void onChildNotify(byte[] bytes) {
        String hexString = Integer.toHexString(bytes[2] & 0xFF);
        if (hexString.equals("91")) {//解析使用类型
            if (bytes[3] == 0) {
                try {
                    int type = DataParser.getModuleType(bytes);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (type == 1) {
                                ARouter.getInstance().build(K.YuanChenBiaoHaoActivity).withParcelable("sensoroDevice", sensoroDevice).withBoolean("isOnlyRead", true).navigation();
                            } else if (type == 2) {
                                ARouter.getInstance().build(K.LSY2InitActivity).withInt("jumpType", LSY2InitTypeCode.TypeFromRead).withParcelable("sensoroDevice", sensoroDevice).navigation();
                            } else {
                                sendMessageToast("暂不支持该设备");
                            }
                        }
                    });
                } catch (Exception e) {
                    ToastManager.getInstance().showLong("检测到新设备未进行出厂配置，请重新选择设备更换");
                }

            }
        }
    }

    private void readPinLv() {
        byte[] bytes = DataParser.CMD_INFO_ALL;
        write(bytes, "正在识别设备");
    }

    @Override
    protected void onChildWriteSuccess() {

    }

    @Override
    protected void onChildWriteFailure(int i) {

    }


    @Override
    protected void initEventListener() {

    }

    @Override
    protected void onEventRunEnd(Event event, int code) {

    }
}
