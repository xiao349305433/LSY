package wu.loushanyun.com.moduletwoinit.v.activity;

import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.elvishew.xlog.XLog;
import com.flyco.roundview.RoundLinearLayout;
import com.qrcore.util.QRScannerHelper;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.sensoro.sensor.kit.callback.SensoroDeviceListener;
import com.sensoro.sensor.kit.entity.SensoroDevice;
import com.wu.loushanyun.base.config.K;
import com.wu.loushanyun.basemvp.init.LSY2InitTypeCode;
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
import met.hx.com.base.base.activity.BaseRefreshActivity;
import met.hx.com.base.base.dialog.LoadingDialogUtil;
import met.hx.com.base.base.multitype.MultiTypeAdapter;
import met.hx.com.base.base.rx.RxSchedulers;
import met.hx.com.base.baseevent.Event;
import met.hx.com.librarybase.some_utils.TimeUtils;
import met.hx.com.librarybase.some_utils.XHStringUtil;
import wu.loushanyun.com.libraryfive.init.LoginFiveParamManager;
import wu.loushanyun.com.moduletwoinit.R;
import wu.loushanyun.com.moduletwoinit.init.MInitTwoCode;
import wu.loushanyun.com.moduletwoinit.m.AreaDetail;
import wu.loushanyun.com.moduletwoinit.m.ConverterDeviceData;
import wu.loushanyun.com.moduletwoinit.m.OnetoOneConverter;
import wu.loushanyun.com.moduletwoinit.m.RefreshOnCloudEvent;
import wu.loushanyun.com.moduletwoinit.m.RefreshTwoLocationEvent;
import wu.loushanyun.com.moduletwoinit.p.adapter.LSYDeviceBinderView;
import wu.loushanyun.com.moduletwoinit.p.runner.GetAreaDetailRunnerNew;

@Route(path = K.LSY2LocationDeviceListActivity)
public class LSY2LocationDeviceListActivity extends BaseRefreshActivity {
    private LinearLayout baseBlueLinearBlueTooth;
    private RoundLinearLayout roundlinearErweima;
    private RoundLinearLayout relativeAll;
    private EditText baseBlueEditSearch;
    private RecyclerView baseBlueDialogBlueRecycle;

    protected MultiTypeAdapter multiTypeAdapterBlue;
    private LSYDeviceBinderView lsyDeviceBinderView;

    protected String loadingTag = this.getClass().getName();
    private String areaNumber;
    private long id;
    private ArrayList<ConverterDeviceData> dataList;
    private String areaName;

    private int jumpType;
    private QRScannerHelper mScannerHelper;

    @Override
    protected void initLifeCycle() {
        super.initLifeCycle();
        getAllData();
        lsyDeviceBinderView = new LSYDeviceBinderView(areaNumber, jumpType, id,areaName);
        registerLifeCycle(lsyDeviceBinderView);
    }

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        super.onInitAttribute(ba);
        ba.mTitleText = areaName + "附近设备";
    }

    private void getAllData() {
        id = getIntent().getLongExtra("id", 0);
        areaNumber = getIntent().getStringExtra("areaNumber");
        areaName = getIntent().getStringExtra("areaName");
        jumpType = getIntent().getIntExtra("jumpType", LSY2InitTypeCode.TypeFromCreateLocation);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(RefreshTwoLocationEvent refreshTwoLocationEvent) {
        loadData();
    }

    @Override
    protected int initRefreshLayoutId() {
        return R.layout.m_twoinit_activity_lsy_device_list;
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        SensoroBlueManager.searchBlueTooth(sensoroDeviceListener);
        refreshlayout.finishRefresh(500);
        loadData();
        dataList.clear();
        multiTypeAdapterBlue.notifyDataSetChanged();
    }


    @Override
    protected void initView() {
        super.initView();
        baseBlueLinearBlueTooth = (LinearLayout) findViewById(R.id.base_blue_linear_blue_tooth);
        roundlinearErweima = (RoundLinearLayout) findViewById(R.id.roundlinear_erweima);
        relativeAll = (RoundLinearLayout) findViewById(R.id.relative_all);
        baseBlueEditSearch = (EditText) findViewById(R.id.base_blue_edit_search);
        baseBlueDialogBlueRecycle = (RecyclerView) findViewById(R.id.base_blue_dialog_blue_recycle);


        roundlinearErweima.setOnClickListener(v -> {
            mScannerHelper.startScanner();
        });
        initBlueList();
        loadData();
        initQRScanner();
        SensoroBlueManager.searchBlueTooth(sensoroDeviceListener);
    }

    /**
     * 在onCreate中调用
     */
    private void initQRScanner() {
        mScannerHelper = new QRScannerHelper(this);
        mScannerHelper.setCallBack(new QRScannerHelper.OnScannerCallBack() {
            @Override
            public void onScannerBack(String result) {
                if (!XHStringUtil.isEmpty(result, false)) {
                    if(result.contains("TYPE:")){
                        result=result.substring(result.indexOf("/SN:")).replace("/SN:","");
                    }
                    lsyDeviceBinderView.setKeyWord(result);
                    baseBlueEditSearch.setText(result);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mScannerHelper != null) {
            mScannerHelper.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void loadData() {
        if (jumpType == LSY2InitTypeCode.TypeFromOnCloudUpdateLocation) {
            pushEvent(MInitTwoCode.GetAreaDetailRunnerNew, areaNumber);
        } else if (jumpType == LSY2InitTypeCode.TypeFromCreateLocation || jumpType == LSY2InitTypeCode.TypeFromUpdateLocation) {
            compositeDisposable.add(Flowable.create(
                    (FlowableOnSubscribe<List<ConverterDeviceData>>) emitter -> {
                        LoadingDialogUtil.showByEvent("正在加载", loadingTag);
                        int loginid = LoginFiveParamManager.getInstance().getLoginData().getId();
                        List<OnetoOneConverter> arrayList = LitePal.where("areaNumber = ? and loginid = ?", areaNumber, loginid + "")
                                .find(OnetoOneConverter.class);
                        List<ConverterDeviceData> deviceDataArrayList = new ArrayList<>();
                        for (OnetoOneConverter onetoOneConverter : arrayList) {
                            ConverterDeviceData converterDeviceData = new ConverterDeviceData();
                            converterDeviceData.setConverter(onetoOneConverter);
                            converterDeviceData.setSerialnumber(onetoOneConverter.getSn());
                            deviceDataArrayList.add(converterDeviceData);
                        }
                        emitter.onNext(deviceDataArrayList);
                        LoadingDialogUtil.dismissByEvent(loadingTag);
                    }
                    , BackpressureStrategy.ERROR)
                    .compose(RxSchedulers.io_main())
                    .subscribe(list -> {
                        dataList.clear();
                        dataList.addAll(list);
                        multiTypeAdapterBlue.notifyDataSetChanged();
                    }));
        }

    }

    private void initBlueList() {
        mSmartRefresh.setEnableLoadmore(false);
        baseBlueEditSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String search = s.toString().toUpperCase();
                if (!XHStringUtil.isEmpty(search, false)) {
                    lsyDeviceBinderView.setKeyWord(search);
                } else {
                    lsyDeviceBinderView.setKeyWord(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        multiTypeAdapterBlue = new MultiTypeAdapter();
        dataList = new ArrayList<>();
        multiTypeAdapterBlue.register(ConverterDeviceData.class, lsyDeviceBinderView);
        multiTypeAdapterBlue.setItems(dataList);
        baseBlueDialogBlueRecycle.setAdapter(multiTypeAdapterBlue);

    }
    private SensoroDeviceListener sensoroDeviceListener=new SensoroDeviceListener<SensoroDevice>() {
        @Override
        public void onNewDevice(SensoroDevice sensoroDevice) {
            XLog.i("蓝牙 onNewDevice" + sensoroDevice.toString());
            ConverterDeviceData data = new ConverterDeviceData();
            data.setDevice(sensoroDevice);
            data.setCanClick(true);
            data.setSerialnumber(sensoroDevice.getSerialNumber());
            if (dataList.contains(data)) {
                ConverterDeviceData converterDeviceData = dataList.get(dataList.indexOf(data));
                converterDeviceData.setDevice(data.getDevice());
                converterDeviceData.setCanClick(data.isCanClick());
                converterDeviceData.setSerialnumber(data.getSerialnumber());
            } else {
                dataList.add(data);
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
            XLog.i("蓝牙 onUpdateDevices" + arrayList.toString());
            for (int i = 0; i < arrayList.size(); i++) {
                SensoroDevice sensoroDevice = arrayList.get(i);
                ConverterDeviceData data = new ConverterDeviceData();
                data.setDevice(sensoroDevice);
                data.setCanClick(true);
                data.setSerialnumber(sensoroDevice.getSerialNumber());
                if (dataList.contains(data)) {
                    ConverterDeviceData converterDeviceData = dataList.get(dataList.indexOf(data));
                    converterDeviceData.setDevice(data.getDevice());
                    converterDeviceData.setCanClick(data.isCanClick());
                    converterDeviceData.setSerialnumber(data.getSerialnumber());
                } else {
                    dataList.add(data);
                }
            }
        }
    };


    @Override
    protected void initEventListener() {
        registerEventRunner(MInitTwoCode.GetAreaDetailRunnerNew, new GetAreaDetailRunnerNew());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(RefreshOnCloudEvent refreshOnCloudEvent) {
        pushEvent(MInitTwoCode.GetAreaDetailRunnerNew, areaNumber);
    }

    @Override
    protected void onEventRunEnd(Event event, int code) {
        if (code == MInitTwoCode.GetAreaDetailRunnerNew) {
            if (event.isSuccess()) {
                AreaDetail areaDetail = (AreaDetail) event.getReturnParamAtIndex(0);
                if(areaDetail.getCode()==0){
                    dataList.clear();
                    for (int i = 0; i < areaDetail.getData().size(); i++) {
                        AreaDetail.DataBean dataBean = areaDetail.getData().get(i);
                        OnetoOneConverter onetoOneConverter = new OnetoOneConverter();
                        onetoOneConverter.setSensingSignal(dataBean.getSensingSignal());
                        onetoOneConverter.setFactoryName(dataBean.getManufacturersName());
                        onetoOneConverter.setSn(dataBean.getEquipmentSN());
                        onetoOneConverter.setRemark( dataBean.getRemark());
                        onetoOneConverter.setEquipmentTime(TimeUtils.milliseconds2String(dataBean.getEquipmentTime()));
                        ConverterDeviceData converterDeviceData = new ConverterDeviceData();
                        converterDeviceData.setConverter(onetoOneConverter);
                        converterDeviceData.setSerialnumber(onetoOneConverter.getSn());
                        dataList.add(converterDeviceData);
                    }
                    multiTypeAdapterBlue.notifyDataSetChanged();
                }else {
                    sendMessageToast(areaDetail.getMsg());
                }

            }
        }

    }
}
