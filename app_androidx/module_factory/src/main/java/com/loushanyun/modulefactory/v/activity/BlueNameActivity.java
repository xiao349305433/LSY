package com.loushanyun.modulefactory.v.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.elvishew.xlog.XLog;
import com.flyco.roundview.RoundTextView;
import com.loushanyun.modulefactory.R;
import com.loushanyun.modulefactory.init.LoginParamManager;
import com.loushanyun.modulefactory.p.adapter.SensoroDeviceViewBinder;
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

@Route(path = K.BlueNameActivity)
public class BlueNameActivity extends BaseNoPresenterActivity {
    private ArrayList<SensoroDevice> deviceArrayList = new ArrayList<SensoroDevice>();
    private MultiTypeAdapter myAdapter;
    private SensoroDeviceViewBinder sensoroDeviceViewBinder;
    private RecyclerView recyclerView;

    private EditText edit_search;
    private RoundTextView reset_text;

    private TextView tv_version;

    private String dumpString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tv_version = (TextView) findViewById(R.id.tv_version);
        reset_text = (RoundTextView) findViewById(R.id.reset_text);
        edit_search = (EditText) findViewById(R.id.edit_search);
        String version = getVersionName();
        tv_version.setText(version);
        initData();
        reset_text.setOnClickListener(view -> {
            edit_search.setText("");
            deviceArrayList.clear();
            myAdapter.notifyDataSetChanged();
            SensoroBlueManager.searchBlueTooth(sensoroDeviceListener);
        });
        edit_search.addTextChangedListener(new TextWatcher() {
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
    protected void onResume() {
        super.onResume();
        SensoroBlueManager.searchBlueTooth(sensoroDeviceListener);
    }

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        getAllData();
        ba.mActivityLayoutId = R.layout.activity_bluename;
        ba.mTitleText = LoginParamManager.getInstance().getProductRegister().getCompanyName();
    }

    private void getAllData() {
        dumpString=getIntent().getStringExtra("dumpString");
    }

    @Override
    protected void initView() {

    }

    @Override
    public void onRightClick(View item) {

    }

    @Override
    protected void initEventListener() {
    }

    @Override
    protected void onEventRunEnd(Event event, int code) {
    }


    /**
     * 获取android版本方法
     *
     * @return
     */
    private String getVersionName() {
        PackageManager manager = getPackageManager();
        try {
            //getPackageName()="com.it52.mobilesafe"(包名在AndroidManifest里)
            PackageInfo packageInfo = manager.getPackageInfo(getPackageName(), 0);//获取包的信息
            //int versionCode = packageInfo.versionCode;
            String versionName = packageInfo.versionName;

            return versionName;
        } catch (NameNotFoundException e) {
            // 没有找到包名的时候会报异常
            e.printStackTrace();
            return null;
        }
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


    private SensoroDeviceListener sensoroDeviceListener=new SensoroDeviceListener<SensoroDevice>() {
        @Override
        public void onNewDevice(SensoroDevice sensoroDevice) {
            XLog.i("蓝牙onNewDevice" + sensoroDevice.toString());
            if (!deviceArrayList.contains(sensoroDevice)) {
                deviceArrayList.add(sensoroDevice);
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
                if (deviceArrayList.contains(sensoroDevice)) {
                    deviceArrayList.set(deviceArrayList.indexOf(sensoroDevice), sensoroDevice);
                } else {
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
        sensoroDeviceViewBinder = new SensoroDeviceViewBinder(this,dumpString);
        myAdapter.setItems(deviceArrayList);
        myAdapter.register(SensoroDevice.class, sensoroDeviceViewBinder);
        recyclerView = (RecyclerView) findViewById(R.id.main_list);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

}
