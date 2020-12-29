package wu.loushanyun.com.moduletwoinit.v.activity;

import android.content.Intent;
import android.location.Location;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.amap.api.location.AMapLocation;
import com.elvishew.xlog.XLog;
import com.flyco.roundview.RoundLinearLayout;
import com.flyco.roundview.RoundTextView;
import com.google.gson.Gson;
import com.tianditu.android.maps.GeoPoint;
import com.tianditu.android.maps.MapView;
import com.tianditu.android.maps.TGeoAddress;
import com.tianditu.android.maps.TGeoDecode;
import com.tianditu.android.maps.overlay.CircleOverlay;
import com.wu.loushanyun.base.config.K;
import com.wu.loushanyun.base.tianditu.listener.SimpleTianDiTuLocationListener;
import com.wu.loushanyun.base.tianditu.listener.TLocationOverlay;
import com.wu.loushanyun.base.tianditu.listener.TOverOverlay;
import com.wu.loushanyun.base.tianditu.utils.TLocationMap;
import com.wu.loushanyun.base.url.URLUtils;
import com.wu.loushanyun.basemvp.init.LSY2InitTypeCode;
import com.wu.loushanyun.basemvp.p.runner.IFiveSelectTokenRunner;
import com.zhihu.matisse.Matisse;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePal;
import org.litepal.crud.callback.FindMultiCallback;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableOnSubscribe;
import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.activity.BaseNoPresenterActivity;
import met.hx.com.base.base.dialog.LoadingDialogUtil;
import met.hx.com.base.base.map.LocationMap;
import met.hx.com.base.base.multitype.MultiTypeAdapter;
import met.hx.com.base.base.rx.RxSchedulers;
import met.hx.com.base.base.webview.BaseWebView;
import met.hx.com.base.base.webview.jsbridge.BridgeHandler;
import met.hx.com.base.base.webview.jsbridge.CallBackFunction;
import met.hx.com.base.baseevent.Event;
import met.hx.com.base.baseinterface.OnMyLocationListener;
import met.hx.com.librarybase.some_utils.AppUtils;
import met.hx.com.librarybase.some_utils.TimeUtils;
import met.hx.com.librarybase.some_utils.ToastManager;
import met.hx.com.librarybase.some_utils.XHStringUtil;
import met.hx.com.librarybase.views.dialog.MDDialog;
import wu.loushanyun.com.libraryfive.PictureUtil;
import wu.loushanyun.com.libraryfive.init.LoginFiveParamManager;
import wu.loushanyun.com.libraryfive.p.adapter.ImageViewBinder;
import wu.loushanyun.com.moduletwoinit.R;
import wu.loushanyun.com.moduletwoinit.init.MInitTwoCode;
import wu.loushanyun.com.moduletwoinit.m.OnetoOneConverter;
import wu.loushanyun.com.moduletwoinit.m.RefreshTwoLocationEvent;
import wu.loushanyun.com.moduletwoinit.m.WuLianUploadData;
import wu.loushanyun.com.libraryfive.p.runner.GetTraceRunner;

@Route(path = K.CreateLocationActivity)
public class CreateLocationActivity extends BaseNoPresenterActivity implements TGeoDecode.OnGeoResultListener {
    private MapView mapview;
    private LinearLayout linearBottom;
    private TextView textShuliang;
    private TextView textQuyuhao;
    private TextView textQuyumingcheng;
    private TextView textCenterZuobiao;
    private ImageView imageFujing;
    private RecyclerView recyclerImage;
    private RoundTextView dataSave;
    private RoundLinearLayout linearTop;
    private TextView textDizhi;
    private TextView textCity;
    private RoundTextView chooseCity;
    private EditText textWeizhi;
    private RoundTextView roundtextSure;
    private BaseWebView baseWebView;

    private static final int REQUEST_CODE_CHOOSE1 = 10001;
    private static final int REQUEST_CODE_CHOOSE2 = 10002;
    private static final int REQUEST_CODE_CHOOSE3 = 10003;
    private static final int REQUEST_CODE_CHOOSE4 = 10004;
    private static final int RESULT_DATA = 1002;
    private String[] mPhotoPath = new String[4];
    private String fileProvider = "wu.loushanyun.com.fivemoduleapp.fileprovider";

    private String provinceCode = "00";
    private String cityCode = "00";
    private String districtCode = "00";
    String province = "";
    String city = "";
    String district = "";

    private boolean locationEndble = true;
    private double lat;
    private double lon;
    private GeoPoint geoPointNew;
    private GeoPoint geoPointOld;
    private boolean sureDingWei = true;
    private boolean isSaved = false;

    private float radius = 5;

    private String areaNumber;
    private String areaName;
    private String dangQianZuoBiao;
    private String areaCenterZuoBiao;
    private TGeoDecode mGeoDecode;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
//                case 0:
//                    textDangqianZuobiao.setText(dangQianZuoBiao);
//                    if (!sureDingWei) {
//                        if (TLocationMap.containsPoint(mapview, geoPointOld, geoPointNew, radius)) {
//                            sendMessageToast("该位置可以定位");
//                            canJump = true;
//                        } else {
//                            sendMessageToast("该位置不可以定位");
//                            canJump = false;
//                        }
//                    }
//                    break;
                case 1:
                    textQuyuhao.setText(areaNumber);
                    textQuyumingcheng.setText(areaName);
                    textCenterZuobiao.setText(areaCenterZuoBiao);
                    wuLianUploadData.setAreaName(areaName);
                    wuLianUploadData.setAreaNumber(areaNumber);
                    String[] strings = areaCenterZuoBiao.split(",");
                    wuLianUploadData.setLon(strings[0]);
                    wuLianUploadData.setLat(strings[1]);
                    wuLianUploadData.setProvinces(provinceCode);
                    wuLianUploadData.setCities(cityCode);
                    wuLianUploadData.setCounties(districtCode);
                    wuLianUploadData.setBusinessLicense(LoginFiveParamManager.getInstance().getLoginData().getTradeRegister().getBusinessLicense());
                    wuLianUploadData.setLoginId(LoginFiveParamManager.getInstance().getLoginData().getId());
                    mapview.removeOverlay(overOverlay);
                    break;
            }
        }
    };
    private CircleOverlay circleOverlay;
    private WuLianUploadData wuLianUploadData;
    private String[] strMessages = {"信息还未保存，是否退出"};
    private MDDialog mdDialog;
    private TOverOverlay overOverlay;

    //4张图片的显示组件
    MultiTypeAdapter adapter = new MultiTypeAdapter();
    ArrayList<String> imgPathList = new ArrayList<>(5);

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
        registerEventRunner(MInitTwoCode.GetTraceRunner, new GetTraceRunner());
        registerEventRunner(LSY2InitTypeCode.IFiveSelectTokenRunner, new IFiveSelectTokenRunner());

    }

    @Override
    protected void onEventRunEnd(Event event, int code) {
        if (code == MInitTwoCode.GetTraceRunner) {
            if (event.isSuccess()) {
                int codeReturn = (int) event.getReturnParamAtIndex(0);
                String msg = (String) event.getReturnParamAtIndex(1);
                sendMessageToast(msg);
                if (codeReturn == 0) {
                    String gridcode = (String) event.getReturnParamAtIndex(2);
                    lon = (Double) event.getReturnParamAtIndex(3);
                    lat = (Double) event.getReturnParamAtIndex(4);
                    linearTop.setVisibility(View.GONE);
                    areaNumber = gridcode;
                    areaCenterZuoBiao = String.valueOf(lon) + "," + String.valueOf(lat);
                    sureDingWei = false;
                    handler.sendEmptyMessage(1);
                }
            }
        } else if (code == LSY2InitTypeCode.IFiveSelectTokenRunner) {
            if (event.isSuccess()) {
                int codeReturn = (int) event.getReturnParamAtIndex(0);
                String msg = (String) event.getReturnParamAtIndex(1);
                String token = (String) event.getReturnParamAtIndex(2);
                if (codeReturn == 0) {
                    pushEvent(MInitTwoCode.GetTraceRunner, lon + "", lat + "", token);
                } else {
                    sendMessageToast(msg, true);
                }
            }
        }
    }

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        ba.mActivityLayoutId = R.layout.m_twoinit_activity_create_location;
        ba.mTitleText = "创建定位";
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(RefreshTwoLocationEvent refreshTwoLocationEvent) {
        compositeDisposable.add(Flowable.create((FlowableOnSubscribe<List<OnetoOneConverter>>) e -> {
            int loginid = LoginFiveParamManager.getInstance().getLoginData().getId();
            LitePal.where("areaNumber = ? and loginid = ?", wuLianUploadData.getAreaNumber(), loginid + "")
                    .findAsync(OnetoOneConverter.class).listen(new FindMultiCallback() {
                @Override
                public <T> void onFinish(List<T> t) {
                    List<OnetoOneConverter> onetoOneConverters = (List<OnetoOneConverter>) t;
                    e.onNext(onetoOneConverters);
                    Log.i("yunanhao", String.valueOf(onetoOneConverters.size()));
                }
            });
        }, BackpressureStrategy.ERROR).compose(RxSchedulers.io_main())
                .subscribe(list -> {
                    textShuliang.setText(list.size() + "");
                }));
    }

    @Override
    protected void initView() {
        mapview = (MapView) findViewById(R.id.mapview);
        linearBottom = (LinearLayout) findViewById(R.id.linear_bottom);
        textShuliang = (TextView) findViewById(R.id.text_shuliang);
        textQuyuhao = (TextView) findViewById(R.id.text_quyuhao);
        textQuyumingcheng = (TextView) findViewById(R.id.text_quyumingcheng);
        textCenterZuobiao = (TextView) findViewById(R.id.text_center_zuobiao);
        imageFujing = (ImageView) findViewById(R.id.image_fujing);
        recyclerImage = (RecyclerView) findViewById(R.id.recycler_image);
        dataSave = (RoundTextView) findViewById(R.id.data_save);
        linearTop = (RoundLinearLayout) findViewById(R.id.linear_top);
        textDizhi = (TextView) findViewById(R.id.text_dizhi);
        textCity = (TextView) findViewById(R.id.text_city);
        chooseCity = (RoundTextView) findViewById(R.id.choose_city);
        textWeizhi = (EditText) findViewById(R.id.text_weizhi);
        roundtextSure = (RoundTextView) findViewById(R.id.roundtext_sure);
        baseWebView = (BaseWebView) findViewById(R.id.baseWebView);

        locationMap.getLocationOption().setOnceLocation(true);
        locationMap.startLocation();
        if (!AppUtils.isLocServiceEnable(this)) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(intent, 100);
        }
        ;
        baseWebView.loadUrl(URLUtils.getIP() + URLUtils.TianDiTuAddress);
        baseWebView.registerHandler("submitFromWeb", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
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
        mdDialog = new MDDialog.Builder(this)
                .setMessages(strMessages)
                .setTitle("温馨提示")
                .setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }).create();

        wuLianUploadData = new WuLianUploadData();

        adapter.setItems(imgPathList);
        adapter.register(String.class, new ImageViewBinder(this));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerImage.setLayoutManager(layoutManager);
        recyclerImage.setAdapter(adapter);
        imgPathList.add("");
        adapter.notifyDataSetChanged();

        imageFujing.setOnClickListener(v -> {
            ARouter.getInstance().build(K.LSY2LocationDeviceListActivity)
                    .withString("areaNumber", areaNumber)
                    .withString("areaName", areaName)
                    .withInt("jumpType", LSY2InitTypeCode.TypeFromCreateLocation)
                    .navigation();
        });
        roundtextSure.setOnClickListener(v -> {
            if (geoPointNew == null) {
                sendMessageToast("请选择定位区域(红色区域标识定位区域)");
                return;
            }
            if (XHStringUtil.isEmpty(textCity.getText().toString(), false)) {
                sendMessageToast("请选择省市县编码");
                return;
            }
            if (XHStringUtil.isEmpty(textWeizhi.getText().toString(), false)) {
                sendMessageToast("请输入区域名称");
                return;
            }

            areaName = textWeizhi.getText().toString();
            geoPointOld = geoPointNew;
            lon = geoPointOld.getLongitudeE6() / (double) 1000000;
            lat = geoPointOld.getLatitudeE6() / (double) 1000000;
            pushEvent(LSY2InitTypeCode.IFiveSelectTokenRunner, LoginFiveParamManager.getInstance().getLoginData().getTradeRegister().getId() + "");
        });
        chooseCity.setOnClickListener(v -> {
            ARouter.getInstance().build(K.ProvincesActivity).navigation(this, RESULT_DATA);
        });
        dataSave.setOnClickListener(v -> {
            ArrayList image = new ArrayList<String>();
            for (int i = 0; i < mPhotoPath.length; i++) {
                if (!XHStringUtil.isEmpty(mPhotoPath[i], false)) {
                    String strPath = mPhotoPath[i];
                    image.add(strPath);
                }
            }
            if (image.size() < 4) {
                ToastManager.getInstance(this).show("请您添加4张图片");
                return;
            }
            Gson gson = new Gson();
            wuLianUploadData.setTime(TimeUtils.getCurTimeString());
            wuLianUploadData.setJsonImage(gson.toJson(image));
            compositeDisposable.add(Flowable.create(
                    (FlowableOnSubscribe<Boolean>) emitter ->
                    {
                        LoadingDialogUtil.showByEvent("正在保存", this.getClass().getName());
                        List<WuLianUploadData> arrayList = LitePal.findAll(WuLianUploadData.class);
                        long id = 0;
                        for (WuLianUploadData yuanChuanSaveData1 : arrayList) {
                            if (yuanChuanSaveData1.getAreaNumber().equals(wuLianUploadData.getAreaNumber())) {
                                id = yuanChuanSaveData1.getBaseObjId();
                                break;
                            }
                        }
                        if (id == 0) {
                            wuLianUploadData.assignBaseObjId(0);
                            wuLianUploadData.saveAsync().listen(success -> {
                                if (success) {
                                    emitter.onNext(true);
                                } else {
                                    emitter.onNext(false);
                                }

                            });
                        } else {
                            LitePal.delete(WuLianUploadData.class, id);
                            wuLianUploadData.assignBaseObjId(0);
                            wuLianUploadData.saveAsync().listen(success -> {
                                if (success) {
                                    emitter.onNext(true);
                                } else {
                                    emitter.onNext(false);
                                }
                            });
                        }
                        LoadingDialogUtil.dismissByEvent(this.getClass().getName());
                    }
                    , BackpressureStrategy.ERROR)
                    .compose(RxSchedulers.io_main())
                    .subscribe(success -> {
                        if (success) {
                            isSaved = true;
                            sendMessageToast("保存成功，请到网络好的地方同步");
                        } else {
                            sendMessageToast("保存失败");
                        }
                    }));
        });
        setMapData();
    }

    private void setMapData() {
        mapview.setBuiltInZoomControls(true);
        mapview.setPlaceName(true);
        mapview.getController().setZoom(mapview.getMaxZoomLevel());
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
        this.geoPointNew = geoPoint;
        lon = geoPoint.getLongitudeE6() / (double) 1000000;
        lat = geoPoint.getLatitudeE6() / (double) 1000000;
        dangQianZuoBiao = String.valueOf(lon) + "," + String.valueOf(lat);
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
//        if (mGeoDecode == null) {
//            mGeoDecode = new TGeoDecode(this);
//        }
//        mGeoDecode.search(geoPoint);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK) {
            if (intent.hasExtra("delete")) {
                //删除指定下标的图片
                String imgpath = intent.getStringExtra("path");
                if (XHStringUtil.isEmpty(imgpath, false)) {
                    return;
                }
                int len = imgPathList.size();
                for (int i = 0; i < len; i++) {
                    if (imgpath.equals(imgPathList.get(i))) {
                        if (len == 4) {
                            if (!"".equals(imgPathList.get(len - 1))) {
                                imgPathList.add("");
                            }
                        }
                        imgPathList.remove(i);
                        //将缺失的图片数据移至末尾方便下次添加新数据
                        for (int j = i + 1; j < mPhotoPath.length; j++) {
                            mPhotoPath[i] = mPhotoPath[j];
                        }
                        mPhotoPath[mPhotoPath.length - 1] = "";
                        adapter.notifyDataSetChanged();
                        return;
                    }
                }
            }
            //获取选择的图片路径集合
            List<String> paths = Matisse.obtainPathResult(intent);
            if (requestCode == REQUEST_CODE_CHOOSE1) {
                for (int i = 0; i < paths.size(); i++) {
                    //遍历将图所选图片数据和路径存入容器
                    mPhotoPath[i + 0] = PictureUtil.bitmapToString(paths.get(i));
                    if (imgPathList.size() > i + 0) {
                        imgPathList.set(i + 0, paths.get(i));
                    } else {
                        imgPathList.add(i + 0, paths.get(i));
                    }
                }
                //如果图片不足4张则在尾部增加添加的样式
                if (imgPathList.size() < 4) {
                    imgPathList.add("");
                }
                adapter.notifyDataSetChanged();
            } else if (requestCode == REQUEST_CODE_CHOOSE2) {
                for (int i = 0; i < paths.size(); i++) {
                    mPhotoPath[i + 1] = PictureUtil.bitmapToString(paths.get(i));
                    if (imgPathList.size() > i + 1) {
                        imgPathList.set(i + 1, paths.get(i));
                    } else {
                        imgPathList.add(i + 1, paths.get(i));
                    }
                }
                if (imgPathList.size() < 4) {
                    imgPathList.add("");
                }
                adapter.notifyDataSetChanged();
            } else if (requestCode == REQUEST_CODE_CHOOSE3) {
                for (int i = 0; i < paths.size(); i++) {
                    mPhotoPath[i + 2] = PictureUtil.bitmapToString(paths.get(i));
                    if (imgPathList.size() > i + 2) {
                        imgPathList.set(i + 2, paths.get(i));
                    } else {
                        imgPathList.add(i + 2, paths.get(i));
                    }
                }
                if (imgPathList.size() < 4) {
                    imgPathList.add("");
                }
                adapter.notifyDataSetChanged();
            } else if (requestCode == REQUEST_CODE_CHOOSE4) {
                for (int i = 0; i < paths.size(); i++) {
                    mPhotoPath[i + 3] = PictureUtil.bitmapToString(paths.get(i));
                    if (imgPathList.size() > i + 3) {
                        imgPathList.set(i + 3, paths.get(i));
                    } else {
                        imgPathList.add(i + 3, paths.get(i));
                    }
                }
                if (imgPathList.size() < 4) {
                    imgPathList.add("");
                }
                adapter.notifyDataSetChanged();
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

    @Override
    public void onBackPressed() {
        if (!isSaved) {
            mdDialog.show();
        } else {
            super.onBackPressed();
        }
    }

    private void getAddressCode(String address) {

        if (sureDingWei) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (circleOverlay != null) {
                        mapview.removeOverlay(circleOverlay);
                    }
                    circleOverlay = TLocationMap.addCircle(mapview, geoPointNew, radius);
                    mapview.postInvalidate();
                    areaCenterZuoBiao = String.valueOf(geoPointNew.getLongitudeE6() / (double) 1000000) + "," + String.valueOf(geoPointNew.getLatitudeE6() / (double) 1000000);
                    textDizhi.setText("地址：" + address);
                }
            });
        }
    }

    @Override
    public void onGeoDecodeResult(TGeoAddress tGeoAddress, int i) {
        getAddressCode(tGeoAddress.getCity());
    }
}
