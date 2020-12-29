package wu.loushanyun.com.moduletwoinit.v.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.elvishew.xlog.XLog;
import com.flyco.roundview.RoundTextView;
import com.sensoro.sensor.kit.callback.SensoroDeviceListener;
import com.sensoro.sensor.kit.entity.SensoroDevice;
import com.wu.loushanyun.base.config.K;
import com.wu.loushanyun.basemvp.init.SensoroBlueManager;

import java.util.ArrayList;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.activity.BaseNoPresenterActivity;
import met.hx.com.base.base.multitype.MultiTypeAdapter;
import met.hx.com.base.baseevent.Event;
import met.hx.com.librarybase.some_utils.XHStringUtil;
import wu.loushanyun.com.moduletwoinit.R;
import wu.loushanyun.com.moduletwoinit.p.adapter.SensoroDeviceViewBinder;

@Route(path = K.BlueNameActivityTwo)
public class BlueNameActivity extends BaseNoPresenterActivity {

    private ArrayList<SensoroDevice> deviceArrayList = new ArrayList<SensoroDevice>();
    private MultiTypeAdapter myAdapter;
    private SensoroDeviceViewBinder sensoroDeviceViewBinder;
    private RecyclerView recyclerView;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    editSearch.setText("");
                    break;
            }
        }
    };

    private EditText editSearch;
    private RoundTextView resetText;

    private String areaNumber;
    private String areaName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        editSearch = (EditText) findViewById(R.id.edit_search);
        resetText = (RoundTextView) findViewById(R.id.reset_text);
        initData();
        initSDK();
        resetText.setOnClickListener(view -> {
            handler.sendEmptyMessage(1);
        });
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String search = charSequence.toString().toUpperCase();
                if (!XHStringUtil.isEmpty(search, false)) {
                    sensoroDeviceViewBinder.setKeyWrold(search);
                } else {
                    sensoroDeviceViewBinder.setKeyWrold(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        getAllData();
        ba.mActivityLayoutId = R.layout.m_twoinit_activity_bluename;
        ba.mTitleText = areaName+"附近设备";
    }

    private void getAllData() {
        areaNumber=getIntent().getStringExtra("areaNumber");
        areaName=getIntent().getStringExtra("areaName");
    }


    @Override
    protected void initView() {

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void initEventListener() {

    }

    @Override
    protected void onEventRunEnd(Event event, int code) {

    }


    public boolean containsDevice(SensoroDevice sensoroDevice) {
        boolean isContains = false;
        for (int i = 0; i < deviceArrayList.size(); i++) {
            SensoroDevice tempDevice = deviceArrayList.get(i);
            if (tempDevice.getSerialNumber().equalsIgnoreCase(sensoroDevice.getSerialNumber())) {
                isContains = true;
            }
        }
        return isContains;
    }

    private void initSDK() {
        SensoroBlueManager.searchBlueTooth(sensoroDeviceListener);
    }
    private  SensoroDeviceListener sensoroDeviceListener=new SensoroDeviceListener<SensoroDevice>() {
        @Override
        public void onNewDevice(SensoroDevice sensoroDevice) {
            XLog.i("蓝牙onNewDevice"+sensoroDevice.toString());
            if(!deviceArrayList.contains(sensoroDevice)){
                deviceArrayList.add(sensoroDevice);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        myAdapter.notifyDataSetChanged();
                    }
                });
            }else {
                XLog.i("蓝牙sdaasasdasdasdasd"+sensoroDevice.toString());
            }

        }

        @Override
        public void onGoneDevice(SensoroDevice sensoroDevice) {
            XLog.i("蓝牙onGoneDevice"+sensoroDevice.toString());
        }

        @Override
        public void onUpdateDevices(ArrayList<SensoroDevice> arrayList) {
            XLog.i("蓝牙onUpdateDevices"+arrayList.toString());
            for(int i=0;i<arrayList.size();i++){
                SensoroDevice sensoroDevice=arrayList.get(i);
                if(deviceArrayList.contains(sensoroDevice)){
                    deviceArrayList.set(deviceArrayList.indexOf(sensoroDevice),sensoroDevice);
                }else {
                    deviceArrayList.add(sensoroDevice);
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

    private void initData() {
        myAdapter = new MultiTypeAdapter();
        sensoroDeviceViewBinder = new SensoroDeviceViewBinder(areaNumber);
        myAdapter.setItems(deviceArrayList);
        myAdapter.register(SensoroDevice.class, sensoroDeviceViewBinder);
        recyclerView = (RecyclerView) findViewById(R.id.main_list);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }



}
