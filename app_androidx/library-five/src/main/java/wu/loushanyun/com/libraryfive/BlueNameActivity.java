package wu.loushanyun.com.libraryfive;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
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
import wu.loushanyun.com.libraryfive.p.adapter.SNDeviceViewBinder;

@Route(path = K.BlueNameActivityFive)
public class BlueNameActivity extends BaseNoPresenterActivity {

    private ArrayList<SensoroDevice> deviceArrayList = new ArrayList<SensoroDevice>();
    private MultiTypeAdapter myAdapter;
    private RecyclerView recyclerView;
    private TextView update_to_net;

    private EditText edit_search;
    private RoundTextView reset_text;
    private SNDeviceViewBinder snDeviceViewBinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        update_to_net = (TextView) findViewById(R.id.update_to_net);
        update_to_net.setOnClickListener(v -> ARouter.getInstance().build(K.UpLoadDataActivity).navigation());
        reset_text = (RoundTextView) findViewById(R.id.reset_text);
        edit_search = (EditText) findViewById(R.id.edit_search);
        initData();
        reset_text.setOnClickListener(view -> {
            SensoroBlueManager.searchBlueTooth(sensoroDeviceListener);
            deviceArrayList.clear();
            edit_search.setText("");
        });
        edit_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String search = charSequence.toString().toUpperCase();
                if (!XHStringUtil.isEmpty(search, false)) {
                    snDeviceViewBinder.setKeyWord(search);
                } else {
                    snDeviceViewBinder.setKeyWord(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        ba.mActivityLayoutId = R.layout.l_five_activity_bluename;
        ba.mTitleText = "设备列表";
    }

    @Override
    public void onRightClick(View item) {
        finish();
    }

    @Override
    protected void initView() {
        SensoroBlueManager.searchBlueTooth(sensoroDeviceListener);
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


    private SensoroDeviceListener sensoroDeviceListener=new SensoroDeviceListener<SensoroDevice>() {
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
                    XLog.i("蓝牙刷新");
                    myAdapter.notifyDataSetChanged();
                }
            });
        }
    };


    @Override
    protected void initLifeCycle() {
        super.initLifeCycle();
        snDeviceViewBinder = new SNDeviceViewBinder(sensoroDevice -> {
            Bundle bundle = new Bundle();
            bundle.putParcelable("sensoroDevice", sensoroDevice);
            ARouter.getInstance().build(K.YuanChenBiaoHaoActivity).with(bundle).navigation();
        });
    }

    private void initData() {
        myAdapter = new MultiTypeAdapter();
        myAdapter.setItems(deviceArrayList);
        myAdapter.register(SensoroDevice.class, snDeviceViewBinder);
        recyclerView = (RecyclerView) findViewById(R.id.main_list);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


}
