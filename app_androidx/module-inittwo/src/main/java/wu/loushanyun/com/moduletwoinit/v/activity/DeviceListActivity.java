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
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableOnSubscribe;
import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.activity.BaseNoPresenterActivity;
import met.hx.com.base.base.dialog.LoadingDialogUtil;
import met.hx.com.base.base.multitype.MultiTypeAdapter;
import met.hx.com.base.base.rx.RxSchedulers;
import met.hx.com.base.baseevent.Event;
import wu.loushanyun.com.libraryfive.init.LoginFiveParamManager;
import wu.loushanyun.com.moduletwoinit.R;
import wu.loushanyun.com.moduletwoinit.m.ConverterDeviceData;
import wu.loushanyun.com.moduletwoinit.m.OnetoOneConverter;
import wu.loushanyun.com.moduletwoinit.m.RefreshTwoLocationEvent;
import wu.loushanyun.com.moduletwoinit.m.WuLianUploadData;
import wu.loushanyun.com.moduletwoinit.p.adapter.WuLianUploadConverterDataBinder;

@Route(path = K.WuLianWangDeviceListActivity)
public class DeviceListActivity extends BaseNoPresenterActivity {
    private RecyclerView recyclerview;
    private MultiTypeAdapter multiTypeAdapter;
    private ArrayList<ConverterDeviceData> dataList;
    private WuLianUploadConverterDataBinder dataBinder;

    private String areaNumber;
    private long id;

    @Override
    protected void initEventListener() {

    }

    @Override
    protected void onEventRunEnd(Event event, int code) {

    }

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        getAllData();
        ba.mActivityLayoutId = R.layout.m_twoinit_activity_updatelocation;
        ba.mTitleText = "附近设备+本地存储设备列表";
    }

    private void getAllData() {
        id = getIntent().getLongExtra("id", 0);
        areaNumber = getIntent().getStringExtra("areaNumber");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(RefreshTwoLocationEvent refreshTwoLocationEvent) {
        loadData();
    }

    @Override
    protected void initView() {
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        dataList = new ArrayList<>();
        multiTypeAdapter = new MultiTypeAdapter();
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.setAdapter(multiTypeAdapter);
        dataBinder = new WuLianUploadConverterDataBinder(areaNumber);
        multiTypeAdapter.register(ConverterDeviceData.class, dataBinder);
        multiTypeAdapter.setItems(dataList);
        loadData();
    }


    private void loadData() {
        compositeDisposable.add(Flowable.create(
                (FlowableOnSubscribe<List<ConverterDeviceData>>) emitter -> {
                    LoadingDialogUtil.showByEvent("正在加载", this.getClass().getName());
                    int loginid = LoginFiveParamManager.getInstance().getLoginData().getId();
                    WuLianUploadData uploadData = LitePal.find(WuLianUploadData.class, id);
                    List<OnetoOneConverter> arrayList = LitePal.where("areaNumber = ? and loginid = ?", uploadData.getAreaNumber(), loginid + "")
                            .find(OnetoOneConverter.class);
                    List<ConverterDeviceData> deviceDataArrayList = new ArrayList<>();
                    for (OnetoOneConverter onetoOneConverter : arrayList) {
                        ConverterDeviceData converterDeviceData = new ConverterDeviceData();
                        converterDeviceData.setConverter(onetoOneConverter);
                        converterDeviceData.setSerialnumber(onetoOneConverter.getSn());
                        deviceDataArrayList.add(converterDeviceData);
                    }
                    emitter.onNext(deviceDataArrayList);
                    LoadingDialogUtil.dismissByEvent(this.getClass().getName());

                }
                , BackpressureStrategy.ERROR)
                .compose(RxSchedulers.io_main())
                .subscribe(list -> {
                    dataList.clear();
                    dataList.addAll(list);

                    multiTypeAdapter.notifyDataSetChanged();
                }));

        SensoroBlueManager.searchBlueTooth(sensoroDeviceListener);
    }
    private SensoroDeviceListener sensoroDeviceListener=new SensoroDeviceListener<SensoroDevice>() {
        @Override
        public void onNewDevice(SensoroDevice sensoroDevice) {
            XLog.i("蓝牙 onNewDevice"+sensoroDevice.toString());
            ConverterDeviceData data = new ConverterDeviceData();
            data.setDevice(sensoroDevice);
            data.setCanClick(true);
            data.setSerialnumber(sensoroDevice.getSerialNumber());
            if(dataList.contains(data)){
                ConverterDeviceData converterDeviceData=dataList.get(dataList.indexOf(data));
                converterDeviceData.setDevice(data.getDevice());
                converterDeviceData.setCanClick(data.isCanClick());
                converterDeviceData.setSerialnumber(data.getSerialnumber());
            }else {
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
            XLog.i("蓝牙 onUpdateDevices"+arrayList.toString());
            for(int i=0;i<arrayList.size();i++){
                SensoroDevice sensoroDevice=arrayList.get(i);
                ConverterDeviceData data = new ConverterDeviceData();
                data.setDevice(sensoroDevice);
                data.setCanClick(true);
                data.setSerialnumber(sensoroDevice.getSerialNumber());
                if(dataList.contains(data)){
                    ConverterDeviceData converterDeviceData=dataList.get(dataList.indexOf(data));
                    converterDeviceData.setDevice(data.getDevice());
                    converterDeviceData.setCanClick(data.isCanClick());
                    converterDeviceData.setSerialnumber(data.getSerialnumber());
                }else {
                    dataList.add(data);
                }
            }
        }
    };
}
