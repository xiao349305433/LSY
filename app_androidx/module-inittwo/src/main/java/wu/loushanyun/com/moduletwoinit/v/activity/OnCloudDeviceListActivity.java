package wu.loushanyun.com.moduletwoinit.v.activity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.elvishew.xlog.XLog;
import com.sensoro.sensor.kit.callback.SensoroDeviceListener;
import com.sensoro.sensor.kit.entity.SensoroDevice;
import com.wu.loushanyun.base.config.K;
import com.wu.loushanyun.basemvp.init.SensoroBlueManager;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.activity.BaseNoPresenterActivity;
import met.hx.com.base.base.multitype.MultiTypeAdapter;
import met.hx.com.base.baseevent.Event;
import wu.loushanyun.com.moduletwoinit.R;
import wu.loushanyun.com.moduletwoinit.init.MInitTwoCode;
import wu.loushanyun.com.moduletwoinit.m.OnCloudConverterDeviceData;
import wu.loushanyun.com.moduletwoinit.m.RefreshOnCloudEvent;
import wu.loushanyun.com.moduletwoinit.p.adapter.OnCloudWuLianUploadConverterDataBinder;
import wu.loushanyun.com.moduletwoinit.p.runner.GetAreaDetailRunner;

@Route(path = K.OnCloudDeviceListActivity)
public class OnCloudDeviceListActivity extends BaseNoPresenterActivity {
    private RecyclerView recyclerview;
    private MultiTypeAdapter multiTypeAdapter;
    private ArrayList<OnCloudConverterDeviceData> dataList;
    private OnCloudWuLianUploadConverterDataBinder dataBinder;
    private String areaNumber;

    @Override
    protected void initEventListener() {
        registerEventRunner(MInitTwoCode.GetAreaDetailRunner, new GetAreaDetailRunner());

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(RefreshOnCloudEvent refreshOnCloudEvent) {
        pushEvent(MInitTwoCode.GetAreaDetailRunner, areaNumber);
    }

    @Override
    protected void onEventRunEnd(Event event, int code) {
        if (code == MInitTwoCode.GetAreaDetailRunner) {
            if (event.isSuccess()) {
                dataList.clear();
                ArrayList<OnCloudConverterDeviceData> arrayList = (ArrayList<OnCloudConverterDeviceData>) event.getReturnParamAtIndex(0);
                dataList.addAll(arrayList);
                multiTypeAdapter.notifyDataSetChanged();
                loadData();
            }
        }

    }

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        ba.mActivityLayoutId = R.layout.m_twoinit_activity_updatelocation;
        ba.mTitleText = "附近设备+云端存储设备列表";
        ba.mTitleRightLayoutId = R.layout.m_twoinit_right_progress;
        ba.mHasRightView = true;
    }

    private void getAllData() {
        areaNumber = getIntent().getStringExtra("areaNumber");
        pushEvent(MInitTwoCode.GetAreaDetailRunner, areaNumber);
    }


    @Override
    protected void initLifeCycle() {
        getAllData();
        dataBinder = new OnCloudWuLianUploadConverterDataBinder(areaNumber, getIntent().getStringExtra("areaName"));
        registerLifeCycle(dataBinder);
    }

    @Override
    protected void initView() {
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        dataList = new ArrayList<>();
        multiTypeAdapter = new MultiTypeAdapter();
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.setAdapter(multiTypeAdapter);
        multiTypeAdapter.register(OnCloudConverterDeviceData.class, dataBinder);
        multiTypeAdapter.setItems(dataList);
    }

    private void loadData() {
        SensoroBlueManager.searchBlueTooth(sensoroDeviceListener);
    }

    private SensoroDeviceListener sensoroDeviceListener = new SensoroDeviceListener<SensoroDevice>() {
        @Override
        public void onNewDevice(SensoroDevice sensoroDevice) {
            XLog.i("蓝牙 onNewDevice" + sensoroDevice.toString());
            OnCloudConverterDeviceData data = new OnCloudConverterDeviceData(sensoroDevice, sensoroDevice.getSerialNumber(), false);
            if (dataList.contains(data)) {
                OnCloudConverterDeviceData onCloudConverterDeviceData = dataList.get(dataList.indexOf(data));
                onCloudConverterDeviceData.setDevice(sensoroDevice);
            } else {
                dataList.add(data);
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    multiTypeAdapter.notifyDataSetChanged();
                }
            });
        }

        @Override
        public void onGoneDevice(SensoroDevice sensoroDevice) {
        }

        @Override
        public void onUpdateDevices(ArrayList<SensoroDevice> arrayList) {
            XLog.i("蓝牙 onUpdateDevices" + arrayList.toString());
            for (int i = 0; i < arrayList.size(); i++) {
                SensoroDevice sensoroDevice = arrayList.get(i);
                OnCloudConverterDeviceData data = new OnCloudConverterDeviceData(sensoroDevice, sensoroDevice.getSerialNumber(), false);
                if (dataList.contains(data)) {
                    OnCloudConverterDeviceData onCloudConverterDeviceData = dataList.get(dataList.indexOf(data));
                    onCloudConverterDeviceData.setDevice(sensoroDevice);
                } else {
                    dataList.add(data);
                }
            }
        }
    };
}
