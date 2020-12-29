package wu.loushanyun.com.moduletwoinit.v.activity4;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.amap.api.location.AMapLocation;
import com.elvishew.xlog.XLog;
import com.flyco.roundview.RoundTextView;
import com.google.gson.Gson;
import com.tianditu.android.maps.GeoPoint;
import com.tianditu.android.maps.MapView;
import com.wu.loushanyun.base.config.K;
import com.wu.loushanyun.base.tianditu.listener.SimpleTianDiTuLocationListener;
import com.wu.loushanyun.base.tianditu.listener.TLocationOverlay;
import com.wu.loushanyun.base.tianditu.listener.TOverOverlay;
import com.wu.loushanyun.base.url.URLUtils;
import com.wu.loushanyun.basemvp.p.adapter.LSY4ImageViewBinder;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableOnSubscribe;
import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.activity.BaseNoPresenterActivity;
import met.hx.com.base.base.dialog.LoadingDialogUtil;
import met.hx.com.base.base.map.LocationMap;
import met.hx.com.base.base.matisse.MatisseUtil;
import met.hx.com.base.base.multitype.MultiTypeAdapter;
import met.hx.com.base.base.rx.RxSchedulers;
import met.hx.com.base.base.webview.BaseWebView;
import met.hx.com.base.base.webview.jsbridge.BridgeHandler;
import met.hx.com.base.base.webview.jsbridge.CallBackFunction;
import met.hx.com.base.baseconfig.C;
import met.hx.com.base.baseevent.Event;
import met.hx.com.base.baseinterface.OnMyLocationListener;
import met.hx.com.librarybase.some_utils.AppUtils;
import met.hx.com.librarybase.some_utils.XHStringUtil;
import met.hx.com.librarybase.views.dialog.MDDialog;
import wu.loushanyun.com.libraryfive.PictureUtil;
import wu.loushanyun.com.libraryfive.init.LoginFiveParamManager;
import wu.loushanyun.com.moduletwoinit.R;
import wu.loushanyun.com.moduletwoinit.init.MInitTwoCode;
import wu.loushanyun.com.moduletwoinit.m.InsidePublicMeter;
import wu.loushanyun.com.moduletwoinit.m.Refresh4ListEvent;
import wu.loushanyun.com.moduletwoinit.p.runner.MInitTwo4SavePublicMeterRunner;

@Route(path = K.LSY4LocationActivity)
public class LSY4LocationActivity extends BaseNoPresenterActivity implements View.OnClickListener {
    private MapView mapview;
    private TextView textSn;
    private TextView textCity;
    private RoundTextView chooseCity;
    private EditText textAddress;
    private RecyclerView recyclerImage;
    private RoundTextView dataSave;
    private BaseWebView baseWebView;

    private TOverOverlay overOverlay;


    private String provinceCode = "00";
    private String cityCode = "00";
    private String districtCode = "00";
    String province = "";
    String city = "";
    String district = "";

    private InsidePublicMeter insidePublicMeter;
    private long insidePublicMeterId;
    private MultiTypeAdapter adapter;
    private ArrayList<String> imgPathList;
    private String fileProvider = "wu.loushanyun.com.fivemoduleapp.fileprovider";

    private static final int REQUEST_CODE_1 = 10001;
    private static final int REQUEST_CODE_2 = 10002;
    private static final int RESULT_DATA = 1002;
    private boolean locationEndble = true;
    private double lat;
    private double lon;
    private TextView textMeterDetails;
    private RoundTextView roundTextSave;
    private MDDialog mdDialog;
    private LocationMap locationMap;

    @Override
    protected void initLifeCycle() {
        locationMap=new LocationMap(new OnMyLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                district = aMapLocation.getDistrict();
                city = aMapLocation.getCity();
                province = aMapLocation.getProvince();
                String areaCode = aMapLocation.getAdCode();
                try {
                    provinceCode = areaCode.substring(0, 2);
                } catch (Exception e) {
                    provinceCode = "00";
                }
                try {
                    cityCode = areaCode.substring(2, 4);
                } catch (Exception e) {
                    cityCode = "00";
                }
                try {
                    districtCode = areaCode.substring(4, 6);
                } catch (Exception e) {
                    districtCode = "00";
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textCity.setText(province + city + district + areaCode);
                    }
                });
            }
        });
        registerLifeCycle(locationMap);
    }
    @Override
    protected void initEventListener() {
        registerEventRunner(MInitTwoCode.MInitTwo4SavePublicMeterRunner, new MInitTwo4SavePublicMeterRunner());

    }

    @Override
    protected void onEventRunEnd(Event event, int code) {
        if (code == MInitTwoCode.MInitTwo4SavePublicMeterRunner) {
            if (event.isSuccess()) {
                int codeReturn = (int) event.getReturnParamAtIndex(0);
                String msg = (String) event.getReturnParamAtIndex(1);
                sendMessageToast(msg, true);
                switch (codeReturn) {
                    case 0:
                        EventBus.getDefault().post(new Refresh4ListEvent());
                        break;
                }
            }
        }

    }

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        getAllData();
        ba.mActivityLayoutId = R.layout.m_twoinit_activity_lsy4_location;
        ba.mTitleText = "4号模组定位";
    }

    private void getAllData() {
        insidePublicMeterId = getIntent().getLongExtra("insidePublicMeterId", 0);
        insidePublicMeter = LitePal.find(InsidePublicMeter.class, insidePublicMeterId);
        if (insidePublicMeter == null) {
            sendMessageToast("没找到本地数据，请删除后重新保存");
            finish();
        }
    }

    @Override
    protected void initView() {
        mapview = (MapView) findViewById(R.id.mapview);
        textSn = (TextView) findViewById(R.id.text_sn);
        textCity = (TextView) findViewById(R.id.text_city);
        chooseCity = (RoundTextView) findViewById(R.id.choose_city);
        textAddress = (EditText) findViewById(R.id.text_address);
        recyclerImage = (RecyclerView) findViewById(R.id.recycler_image);
        dataSave = (RoundTextView) findViewById(R.id.data_save);
        baseWebView = (BaseWebView) findViewById(R.id.baseWebView);

        locationMap.getLocationOption().setOnceLocation(true);
        locationMap.startLocation();
        textSn.setText(insidePublicMeter.getSn());
        if (!AppUtils.isLocServiceEnable(this)) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(intent, 100);
        }
        ;
        View view = getLayoutInflater().inflate(R.layout.m_twoinit_lsy4_dialog, null);
        textMeterDetails = (TextView) view.findViewById(R.id.text_meter_details);
        roundTextSave = (RoundTextView) view.findViewById(R.id.round_text_save);
        mdDialog = new MDDialog.Builder(this)
                .setContentView(view)
                .setShowButtons(false)
                .setShowTitle(false).create();
        dataSave.setOnClickListener(this);
        roundTextSave.setOnClickListener(this);
        baseWebView.loadUrl(URLUtils.getIP() + URLUtils.TianDiTuAddress);
        baseWebView.registerHandler("submitFromWeb", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                XLog.i("接收的数据" + data);
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    int status = jsonObject.optInt("status");
                    if (status == 0) {
                        String address = jsonObject.optString("address");
                        getAddressCode(address);
                    } else {
                        sendMessageToast("获取位置信息失败");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        SetMapData();
        adapter = new MultiTypeAdapter();
        imgPathList = new ArrayList<>();
        imgPathList.add("");
        adapter.setItems(imgPathList);
        adapter.register(String.class, new LSY4ImageViewBinder(this, new LSY4ImageViewBinder.OnClickImageListener() {
            @Override
            public void OnClickImage(String path, int position) {
                if (XHStringUtil.isEmpty(path, false)) {
                    MatisseUtil.choosePicture(LSY4LocationActivity.this, MimeType.ofImage(), 5 - imgPathList.size(), REQUEST_CODE_1, fileProvider);
                } else {
                    ArrayList<String> arrayList = new ArrayList<>();
                    arrayList.addAll(imgPathList);
                    if (arrayList.contains("")) {
                        arrayList.remove("");
                    }
                    ARouter.getInstance().build(C.FullViewPictureActivity)
                            .withStringArrayList("paths", arrayList)
                            .withBoolean("hasDelete", true)
                            .withInt("num", position)
                            .navigation(LSY4LocationActivity.this, REQUEST_CODE_2);
                }
            }
        }));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerImage.setLayoutManager(layoutManager);
        recyclerImage.setAdapter(adapter);

        chooseCity.setOnClickListener(v -> {
            ARouter.getInstance().build(K.ProvincesActivity).navigation(this, RESULT_DATA);
        });
    }

    private void SetMapData() {
        mapview.setBuiltInZoomControls(true);
        mapview.setPlaceName(true);
        mapview.getController().setZoom(20);
        TLocationOverlay mLocation = new TLocationOverlay(this, mapview, new SimpleTianDiTuLocationListener() {
            @Override
            public void onLocationChanged(Location location, GeoPoint p) {
                super.onLocationChanged(location, p);
                if (locationEndble) {
                    searchGeoDecode(p);
                    locationEndble = false;
                }
            }
        });
        overOverlay = new TOverOverlay(R.drawable.l_loushanyun_overlay, new SimpleTianDiTuLocationListener() {
            @Override
            public void onTap(GeoPoint p, MapView mapView) {
                super.onTap(p, mapView);
                searchGeoDecode(p);
            }
        });
        mLocation.enableMyLocation();
        mapview.getController().setCenter(mLocation.getMyLocation());
        mapview.addOverlay(mLocation);
        mapview.addOverlay(overOverlay);
        mapview.postInvalidate();
    }

    //反地理编码
    private void searchGeoDecode(GeoPoint geoPoint) {
        lon = geoPoint.getLongitudeE6() / (double) 1000000;
        lat = geoPoint.getLatitudeE6() / (double) 1000000;
        StringBuffer data = new StringBuffer();
        data.append("{\"Lng\":");
        data.append(lon);
        data.append(",\"Lat\":");
        data.append(lat);
        data.append("}");
        baseWebView.callHandler("functionInJs", data.toString(), new CallBackFunction() {

            @Override
            public void onCallBack(String data) {
                // TODO Auto-generated method stub
                XLog.i("发送成功 " + data);
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_1) {
                //获取选择的图片路径集合
                List<String> paths = Matisse.obtainPathResult(intent);
                if (paths != null) {
                    if (paths.size() > 0) {
                        imgPathList.remove(imgPathList.size() - 1);
                        imgPathList.addAll(paths);
                        //如果图片不足4张则在尾部增加添加的样式
                        if (imgPathList.size() < 4) {
                            imgPathList.add("");
                        }
                        adapter.notifyDataSetChanged();
                    }
                }
            } else if (requestCode == REQUEST_CODE_2) {
                if (intent != null) {
                    Bundle bundle = intent.getExtras();
                    int num = bundle.getInt("num");
                    boolean delete = bundle.getBoolean("delete");
                    if (delete) {
                        imgPathList.remove(num);
                        if (imgPathList.size() < 4) {
                            if (!imgPathList.contains("")) {
                                imgPathList.add("");
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }

                }
            } else if (requestCode == RESULT_DATA && intent != null) {
                district = intent.getStringExtra("area");
                city = intent.getStringExtra("city");
                province = intent.getStringExtra("province");
                String areaCode = intent.getStringExtra("areaCode");
                try {
                    provinceCode = areaCode.substring(0, 2);
                } catch (Exception e) {
                    provinceCode = "00";
                }
                try {
                    cityCode = areaCode.substring(2, 4);
                } catch (Exception e) {
                    cityCode = "00";
                }
                try {
                    districtCode = areaCode.substring(4, 6);
                } catch (Exception e) {
                    districtCode = "00";
                }
                textCity.setText(province + city + district + areaCode);
            }

        }
    }

    private void getAddressCode(String address) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textAddress.setText(address);
            }
        });
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.data_save) {
            if (imgPathList.contains("")) {
                sendMessageToast("请添加4张图片");
                return;
            }
            if (XHStringUtil.isEmpty(textCity.getText().toString(), false)) {
                sendMessageToast("请选择省市县编码");
                return;
            }
            compositeDisposable.add(Flowable.create(
                    (FlowableOnSubscribe<Boolean>) emitter ->
                    {
                        LoadingDialogUtil.showByEvent("", this.getClass().getName());
                        insidePublicMeter.setLat(String.valueOf(lat));
                        insidePublicMeter.setLon(String.valueOf(lon));
                        insidePublicMeter.setProvinces(String.valueOf(provinceCode));
                        insidePublicMeter.setCities(String.valueOf(cityCode));
                        insidePublicMeter.setCounties(String.valueOf(districtCode));
                        insidePublicMeter.setBusinessLicense(LoginFiveParamManager.getInstance().getLoginData().getTradeRegister().getBusinessLicense());
                        insidePublicMeter.setAddress(province + city + district + textAddress.getText().toString());
                        insidePublicMeter.setImagePath(imgPathList);
                        ArrayList<String> arrayList = new ArrayList<>();
                        for (int j = 0; j < imgPathList.size(); j++) {
                            arrayList.add(PictureUtil.bitmapToString(imgPathList.get(j)));
                        }
                        insidePublicMeter.setImage(arrayList);
                        textMeterDetails.setText(insidePublicMeter.printInfo());
                        LoadingDialogUtil.dismissByEvent(this.getClass().getName());
                        emitter.onNext(true);
                    }
                    , BackpressureStrategy.ERROR)
                    .compose(RxSchedulers.io_main())
                    .subscribe(success -> {
                        if (success) {
                            mdDialog.show();
                        }
                    }));


        } else if (i == R.id.round_text_save) {
            pushEvent(MInitTwoCode.MInitTwo4SavePublicMeterRunner, new Gson().toJson(insidePublicMeter));
            mdDialog.dismiss();
        }
    }
}
