package com.wu.loushanyun.basemvp.v.activity;

import androidx.annotation.CallSuper;
import androidx.annotation.LayoutRes;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.elvishew.xlog.XLog;
import com.flyco.roundview.RoundTextView;
import com.sensoro.sensor.kit.callback.SensoroDeviceListener;
import com.sensoro.sensor.kit.entity.SensoroDevice;
import com.wu.loushanyun.R;
import com.wu.loushanyun.basemvp.init.SensoroBlueManager;
import com.wu.loushanyun.basemvp.init.SnBlueToothListener;
import com.wu.loushanyun.basemvp.init.SnBlueToothTool;
import com.wu.loushanyun.basemvp.p.adapter.SNDeviceViewBinder;

import java.util.ArrayList;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.activity.BaseNoPresenterActivity;
import met.hx.com.base.base.multitype.MultiTypeAdapter;
import met.hx.com.librarybase.some_utils.KeyboardUtils;
import met.hx.com.librarybase.some_utils.XHStringUtil;

public abstract class BaseBleBlueToothActivity extends BaseNoPresenterActivity implements SnBlueToothListener {
    private FrameLayout baseBlueFrame;

    private LinearLayout baseBlueLinearBlueTooth;
    private EditText baseBlueEditSearch;
    private RoundTextView baseBlueResetClear;
    private RoundTextView baseBlueResetClearList;
    private RecyclerView baseBlueDialogBlueRecycle;

    private SNDeviceViewBinder snDeviceViewBinder;
    private MultiTypeAdapter multiTypeAdapterBlue;
    private SnBlueToothTool snBlueToothTool;
    private ArrayList<SensoroDevice> deviceArrayList;

    @Override
    protected void initLifeCycle() {
        snBlueToothTool = new SnBlueToothTool(this);
        registerLifeCycle(snBlueToothTool);
        snDeviceViewBinder = new SNDeviceViewBinder(sensoroDevice -> {
            onClickBlueListListener(sensoroDevice);
        });
    }


    @Override
    protected void onLoadingDismissTimeOut() {
        sendMessageToast("操作超时");
    }


    @CallSuper
    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        ba.mActivityLayoutId = R.layout.l_loushanyun_base_ble_blue_aactivity;
        ba.mTitleRightText = "附近蓝牙";
        ba.mTitleLeftRightWidth = 100;
    }

    @Override
    public void onRightClick(View item) {
        baseBlueLinearBlueTooth.setVisibility(View.VISIBLE);
    }

    @Override
    protected void initView() {
        baseBlueFrame = (FrameLayout) findViewById(R.id.base_blue_frame);

        if (onChildLayout() != 0) {
            View mainView = getLayoutInflater().inflate(onChildLayout(), baseBlueFrame);
        }
        if (hasSearchLayout()) {
            View blueListView = getLayoutInflater().inflate(R.layout.l_loushanyun_bluelist_layout, baseBlueFrame);
            baseBlueLinearBlueTooth = (LinearLayout) blueListView.findViewById(R.id.base_blue_linear_blue_tooth);
            baseBlueEditSearch = (EditText) blueListView.findViewById(R.id.base_blue_edit_search);
            baseBlueResetClear = (RoundTextView) blueListView.findViewById(R.id.base_blue_reset_clear);
            baseBlueResetClearList = (RoundTextView) blueListView.findViewById(R.id.base_blue_reset_clear_list);
            baseBlueDialogBlueRecycle = (RecyclerView) blueListView.findViewById(R.id.base_blue_dialog_blue_recycle);
            baseBlueLinearBlueTooth.setVisibility(View.GONE);
            initBlueList();
        }

    }

    private void initBlueList() {
        baseBlueEditSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String search = s.toString().toUpperCase();
                if (!XHStringUtil.isEmpty(search, false)) {
                    snDeviceViewBinder.setKeyWord(search);
                } else {
                    snDeviceViewBinder.setKeyWord(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        baseBlueResetClear.setOnClickListener(v -> {
            baseBlueEditSearch.setText("");
        });
        baseBlueResetClearList.setOnClickListener(v -> {
            SensoroBlueManager.searchBlueTooth(sensoroDeviceListener);
            deviceArrayList.clear();
            multiTypeAdapterBlue.notifyDataSetChanged();
        });
        multiTypeAdapterBlue = new MultiTypeAdapter();
        deviceArrayList = new ArrayList<>();
        multiTypeAdapterBlue.register(SensoroDevice.class, snDeviceViewBinder);
        multiTypeAdapterBlue.setItems(deviceArrayList);
        baseBlueDialogBlueRecycle.setAdapter(multiTypeAdapterBlue);

    }

    private SensoroDeviceListener sensoroDeviceListener=new SensoroDeviceListener<SensoroDevice>() {
        @Override
        public void onNewDevice(SensoroDevice sensoroDevice) {
            XLog.i("蓝牙onNewDevice" + sensoroDevice.toString());
            if (!deviceArrayList.contains(sensoroDevice)) {
                deviceArrayList.add(sensoroDevice);
            } else {
                deviceArrayList.set(deviceArrayList.indexOf(sensoroDevice), sensoroDevice);
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    multiTypeAdapterBlue.notifyDataSetChanged();
                }
            });
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
                    multiTypeAdapterBlue.notifyDataSetChanged();
                }
            });
        }
    };
    /**
     * 子类的布局
     * @return
     */
    @LayoutRes
    protected abstract int onChildLayout();

    /**
     * SN的点击事件
     * @param sensoroDevice
     */
    protected void onClickBlueListListener(SensoroDevice sensoroDevice) {
        baseBlueLinearBlueTooth.setVisibility(View.GONE);
        snBlueToothTool.connectBlueTooth(sensoroDevice);
        KeyboardUtils.hideSoftInput(this);
    }

    /**
     * 是否需要搜蓝牙的功能
     * @return
     */
    protected boolean hasSearchLayout() {
        return true;
    }


}
