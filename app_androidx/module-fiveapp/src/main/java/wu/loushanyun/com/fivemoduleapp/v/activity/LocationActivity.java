package wu.loushanyun.com.fivemoduleapp.v.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.provider.Settings;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.amap.api.location.AMapLocation;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.elvishew.xlog.XLog;
import com.flyco.roundview.RoundLinearLayout;
import com.flyco.roundview.RoundTextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tianditu.android.maps.GeoPoint;
import com.tianditu.android.maps.MapView;
import com.tianditu.android.maps.TErrorCode;
import com.tianditu.android.maps.TGeoAddress;
import com.tianditu.android.maps.TGeoDecode;
import com.tianditu.android.maps.overlay.MarkerOverlay;
import com.wu.loushanyun.base.config.K;
import com.wu.loushanyun.base.tianditu.listener.SimpleTianDiTuLocationListener;
import com.wu.loushanyun.base.tianditu.listener.TLocationOverlay;
import com.wu.loushanyun.base.tianditu.listener.TOverOverlay;
import com.wu.loushanyun.base.url.URLUtils;
import com.wu.loushanyun.basemvp.init.LSY2InitTypeCode;
import com.wu.loushanyun.basemvp.p.runner.IFiveSelectTokenRunner;
import com.zhihu.matisse.Matisse;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePal;
import org.litepal.crud.callback.UpdateOrDeleteCallback;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableOnSubscribe;
import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.activity.BaseNoPresenterActivity;
import met.hx.com.base.base.dialog.LoadingDialogUtil;
import met.hx.com.base.base.glide.GlideUtil;
import met.hx.com.base.base.map.LocationMap;
import met.hx.com.base.base.multitype.MultiTypeAdapter;
import met.hx.com.base.base.rx.RxSchedulers;
import met.hx.com.base.base.webview.BaseWebView;
import met.hx.com.base.base.webview.jsbridge.BridgeHandler;
import met.hx.com.base.base.webview.jsbridge.CallBackFunction;
import met.hx.com.base.baseevent.Event;
import met.hx.com.base.baseinterface.OnMyLocationListener;
import met.hx.com.librarybase.some_utils.AppUtils;
import met.hx.com.librarybase.some_utils.ImageUtils;
import met.hx.com.librarybase.some_utils.TimeUtils;
import met.hx.com.librarybase.some_utils.ToastManager;
import met.hx.com.librarybase.some_utils.ToastUtils;
import met.hx.com.librarybase.some_utils.XHStringUtil;
import met.hx.com.librarybase.views.dialog.MDDialog;
import wu.loushanyun.com.fivemoduleapp.R;
import wu.loushanyun.com.fivemoduleapp.m.RefreshYuanChengBiaoHao;
import wu.loushanyun.com.libraryfive.PictureUtil;
import wu.loushanyun.com.libraryfive.init.LFiveCode;
import wu.loushanyun.com.libraryfive.init.LoginFiveParamManager;
import wu.loushanyun.com.libraryfive.m.RefreshMainRepair;
import wu.loushanyun.com.libraryfive.m.RepairLocationDetail;
import wu.loushanyun.com.libraryfive.m.RepairUpdateData;
import wu.loushanyun.com.libraryfive.m.SaveDataConverter;
import wu.loushanyun.com.libraryfive.m.YuanChuanSaveData;
import wu.loushanyun.com.libraryfive.p.adapter.ImageViewBinder;
import wu.loushanyun.com.libraryfive.p.runner.GetTraceRunner;
import wu.loushanyun.com.libraryfive.p.runner.MRepairGetLocationInfoRunner;
import wu.loushanyun.com.libraryfive.p.runner.MRepairModifyThirdEquipmentRunner;
import wu.loushanyun.com.moduletwoinit.init.MInitTwoCode;

/**
 * 该界面是1号模组定位的界面，有2种方式
 * 1.从远传表号接入》...》附近新蓝牙设备》初始化界面》该界面  不带oldSN
 * 2.从服务》检修》替换远传表号接》初始化界面》该界面   带oldSN
 */
@Route(path = K.LocationActivity)
public class LocationActivity extends BaseNoPresenterActivity implements TGeoDecode.OnGeoResultListener {
    private static final int REQUEST_CODE_CHOOSE1 = 10001;
    private static final int REQUEST_CODE_CHOOSE2 = 10002;
    private static final int REQUEST_CODE_CHOOSE3 = 10003;
    private static final int REQUEST_CODE_CHOOSE4 = 10004;
    public static final int RESULT_DATA = 1002;

    private MapView mapview;
    private TextView textSn;
    private TextView textAddress;
    private EditText etName;
    private EditText etInfo;
    private LinearLayout linearQuyuhao;
    private TextView textQuyuhao;
    private TextView textCity;
    private RoundTextView chooseCity;
    private TextView xinhaoqiangdu;
    private Spinner caliberSelector;
    private RecyclerView recyclerImage;
    private RoundTextView dataSave;
    private RoundLinearLayout linearTop;
    private TextView textDizhi;
    private RoundTextView roundtextSure;
    private BaseWebView baseWebView;

    private boolean locationEndble = true;
    private boolean canDingWei = false;
    private double lat;
    private double lon;
    private String provinceCode = "00";
    private String cityCode = "00";
    private String districtCode = "00";
    String province = "";
    String city = "";
    String district = "";
    private String[] mPhotoPath = new String[4];
    private ArrayList<String> image;
    private String caliber;
    private YuanChuanSaveData yuanChuanSaveData;
    private SaveDataConverter saveDataConverter;
    private MDDialog mdDialog;
    private String oldSn;

    //4张图片的显示组件
    MultiTypeAdapter adapter = new MultiTypeAdapter();
    ArrayList<String> imgPathList = new ArrayList<>(5);
    private TOverOverlay overOverlay;
    private long repairUpdateDataId;
    private RepairLocationDetail repairLocationDetail;
    private long yuanChuanSaveDataId;
    private TextView textLocation;
    private MarkerOverlay mMarker;
    private LocationMap locationMap;

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        getAllData();
        ba.mActivityLayoutId = R.layout.m_five_activity_location;
        ba.mTitleText = "集中器定位";
    }

    @Override
    protected void initLifeCycle() {
        locationMap = new LocationMap(new OnMyLocationListener() {
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

    private void getAllData() {
        oldSn = getIntent().getStringExtra("oldSn");
        repairUpdateDataId = getIntent().getLongExtra("repairUpdateDataId", 0);
        yuanChuanSaveDataId = getIntent().getLongExtra("yuanChuanSaveDataId", 0);
        Gson gson = new Gson();
        if (!XHStringUtil.isEmpty(oldSn, false)) {
            if (repairUpdateDataId != 0) {
                RepairUpdateData repairUpdateData = LitePal.find(RepairUpdateData.class, repairUpdateDataId);
                yuanChuanSaveData = gson.fromJson(repairUpdateData.getYuanChuanSaveDataJson(), YuanChuanSaveData.class);
            } else {
                sendMessageToast("没找到本地数据，请删除后重新保存");
                finish();
            }
        } else {
            yuanChuanSaveData = LitePal.find(YuanChuanSaveData.class, yuanChuanSaveDataId);
            if (yuanChuanSaveData == null) {
                sendMessageToast("没找到本地数据，请删除后重新保存");
                finish();
            }
        }
        if (yuanChuanSaveData != null) {
            saveDataConverter = gson.fromJson(yuanChuanSaveData.getJsonSaveDataConverter(), SaveDataConverter.class);
        }
    }

    @Override
    protected void initEventListener() {
        registerEventRunner(LFiveCode.MRepairGetLocationInfoRunner, new MRepairGetLocationInfoRunner());
        registerEventRunner(LFiveCode.MRepairModifyThirdEquipmentRunner, new MRepairModifyThirdEquipmentRunner());
        registerEventRunner(MInitTwoCode.GetTraceRunner, new GetTraceRunner());
        registerEventRunner(LSY2InitTypeCode.IFiveSelectTokenRunner, new IFiveSelectTokenRunner());

    }

    @Override
    protected void onEventRunEnd(Event event, int code) {
        if (code == LFiveCode.MRepairGetLocationInfoRunner) {
            if (event.isSuccess()) {
                int codeMsg = (int) event.getReturnParamAtIndex(0);
                String msg = (String) event.getReturnParamAtIndex(1);
                if (codeMsg != 0) {
                    ToastUtils.showShort(msg);
                } else {
                    repairLocationDetail = (RepairLocationDetail) event.getReturnParamAtIndex(2);
                    setAllData(repairLocationDetail);
                }
            }
        } else if (code == LFiveCode.MRepairModifyThirdEquipmentRunner) {
            if (event.isSuccess()) {
                int codeReturn = (int) event.getReturnParamAtIndex(0);
                switch (codeReturn) {
                    //-1服务器异常 0成 功   1参数异常   2权限限制
                    case -1:
                        sendMessageToast("服务器异常");
                        break;
                    case 0:
                        sendMessageToast("更换成功");
                        EventBus.getDefault().post(new RefreshMainRepair());
                        finish();
                        break;
                    case 1:
                        sendMessageToast("无法替换未定位过的设备");
                        break;
                    case 2:
                        sendMessageToast("新设备已被定位过");
                        break;
                }
            }
        } else if (code == MInitTwoCode.GetTraceRunner) {
            if (event.isSuccess()) {
                int codeReturn = (int) event.getReturnParamAtIndex(0);
                String msg = (String) event.getReturnParamAtIndex(1);
                sendMessageToast(msg);
                if (codeReturn == 0) {
                    String gridcode = (String) event.getReturnParamAtIndex(2);
                    linearTop.setVisibility(View.GONE);
                    textQuyuhao.setText(gridcode);
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

    private void setAllData(RepairLocationDetail repairLocationDetail) {
        GeoPoint geoPoint = new GeoPoint((int) (Double.valueOf(repairLocationDetail.getLat()) * 1E6), (int) (Double.valueOf(repairLocationDetail.getLon()) * 1E6));
        searchGeoDecode(geoPoint);
        MarkerOverlay mMarker = new MarkerOverlay();
        mMarker.setPosition(geoPoint);
        Bitmap bitmap = ImageUtils.getBitmap(wu.loushanyun.com.modulerepair.R.drawable.l_loushanyun_overlay);
        Bitmap bitmap1 = ImageUtils.scale(bitmap, 50, 100);
        Drawable mDrawable = ImageUtils.bitmap2Drawable(bitmap1);
        mMarker.setIcon(mDrawable);
        mapview.removeOverlay(overOverlay);
        mapview.addOverlay(mMarker);
        etName.setText(repairLocationDetail.getProductName());
        etInfo.setText(repairLocationDetail.getRemark());
        String[] strings = repairLocationDetail.getImageUrl().split(".png");

        String[] strings1 = getResources().getStringArray(R.array.m_init_caliber);
        for (int i = 0; i < strings1.length; i++) {
            if (strings1[i].equals(repairLocationDetail.getCaliber() + "mm")) {
                caliberSelector.setSelection(i);
            }
        }
        for (int i = 0; i < strings.length; i++) {
            int finalI = i;
            XLog.i("啥的哈桑111：" + strings[finalI]);
            GlideUtil.display(this, strings[i] + ".png", R.drawable.base_chat_img, new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    mPhotoPath[finalI] = PictureUtil.bitmapToString(resource);
                    XLog.i("啥的哈桑：" + mPhotoPath[finalI]);
                }
            });
            imgPathList.add(strings[i] + ".png");
        }
        imgPathList.remove("");
        adapter.notifyDataSetChanged();
    }

    private void setAllData(YuanChuanSaveData yuanChuanSaveData) {
        Gson gson = new Gson();
        SaveDataConverter saveDataConverter = gson.fromJson(yuanChuanSaveData.getJsonSaveDataConverter(), SaveDataConverter.class);
        if (yuanChuanSaveData.getLat() != 0 && yuanChuanSaveData.getLon() != 0) {
            GeoPoint geoPoint = new GeoPoint((int) (Double.valueOf(yuanChuanSaveData.getLat()) * 1E6), (int) (Double.valueOf(yuanChuanSaveData.getLon()) * 1E6));
            searchGeoDecode(geoPoint);
            canDingWei = true;
            mMarker = new MarkerOverlay();
            mMarker.setPosition(geoPoint);
            Bitmap bitmap = ImageUtils.getBitmap(wu.loushanyun.com.modulerepair.R.drawable.l_loushanyun_overlay);
            Bitmap bitmap1 = ImageUtils.scale(bitmap, 50, 100);
            Drawable mDrawable = ImageUtils.bitmap2Drawable(bitmap1);
            mMarker.setIcon(mDrawable);
            mapview.addOverlay(mMarker);
            mapview.getController().setCenter(geoPoint);
        }

        etName.setText(yuanChuanSaveData.getProductName());
        etInfo.setText(saveDataConverter.getRemark());

        String[] strings1 = getResources().getStringArray(R.array.m_init_caliber);
        for (int i = 0; i < strings1.length; i++) {
            if (strings1[i].equals(saveDataConverter.getCaliber() + "mm")) {
                caliberSelector.setSelection(i);
            }
        }
        if (!XHStringUtil.isEmpty(yuanChuanSaveData.getJsonImagePath(), false)) {
            ArrayList<String> arrayList = gson.fromJson(yuanChuanSaveData.getJsonImagePath(), new TypeToken<ArrayList<String>>() {
            }.getType());
            imgPathList.addAll(arrayList);
            imgPathList.remove("");
            adapter.notifyDataSetChanged();
            compositeDisposable.add(Flowable.create(
                    (FlowableOnSubscribe<Boolean>) emitter ->
                    {
                        LoadingDialogUtil.showByEvent("正在解析图片", this.getClass().getName());
                        mPhotoPath[0] = PictureUtil.bitmapToString(arrayList.get(0));
                        mPhotoPath[1] = PictureUtil.bitmapToString(arrayList.get(1));
                        mPhotoPath[2] = PictureUtil.bitmapToString(arrayList.get(2));
                        mPhotoPath[3] = PictureUtil.bitmapToString(arrayList.get(3));
                        emitter.onNext(true);
                    }
                    , BackpressureStrategy.ERROR)
                    .compose(RxSchedulers.io_main())
                    .subscribe(success -> {
                        if (success) {
                            LoadingDialogUtil.dismissByEvent(this.getClass().getName());
                        }
                    }));


        }

    }

    /**
     * 保存更换的数据到本地
     *
     * @param json
     */
    private void saveUpdateData(String json) {
//TODO
        compositeDisposable.add(Flowable.create(
                (FlowableOnSubscribe<Boolean>) emitter ->
                {
                    LoadingDialogUtil.showByEvent("保存", this.getClass().getName());
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("yuanChuanSaveDataJson", json);
                    LitePal.updateAsync(RepairUpdateData.class, contentValues, repairUpdateDataId).listen(rowsAffected -> emitter.onNext(true));
                    LoadingDialogUtil.dismissByEvent(this.getClass().getName());
                }
                , BackpressureStrategy.ERROR)
                .compose(RxSchedulers.io_main())
                .subscribe(success -> {
                    if (success) {
                        ARouter.getInstance().build(K.UpLoadDataInfoActivity).withBoolean("isRepaired", true).withLong("repairUpdateDataId", repairUpdateDataId).navigation();
                    } else {
                        sendMessageToast("保存失败");
                    }
                }));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(RefreshMainRepair refreshMainRepair) {
        finish();
    }

    @Override
    protected void initView() {
        mapview = (MapView) findViewById(R.id.mapview);
        textSn = (TextView) findViewById(R.id.text_sn);
        textAddress = (TextView) findViewById(R.id.text_address);
        etName = (EditText) findViewById(R.id.et_name);
        etInfo = (EditText) findViewById(R.id.et_info);
        linearQuyuhao = (LinearLayout) findViewById(R.id.linear_quyuhao);
        textQuyuhao = (TextView) findViewById(R.id.text_quyuhao);
        textCity = (TextView) findViewById(R.id.text_city);
        chooseCity = (RoundTextView) findViewById(R.id.choose_city);
        xinhaoqiangdu = (TextView) findViewById(R.id.xinhaoqiangdu);
        caliberSelector = (Spinner) findViewById(R.id.caliberSelector);
        recyclerImage = (RecyclerView) findViewById(R.id.recycler_image);
        dataSave = (RoundTextView) findViewById(R.id.data_save);
        linearTop = (RoundLinearLayout) findViewById(R.id.linear_top);
        textDizhi = (TextView) findViewById(R.id.text_dizhi);
        roundtextSure = (RoundTextView) findViewById(R.id.roundtext_sure);
        baseWebView = (BaseWebView) findViewById(R.id.baseWebView);


        locationMap.getLocationOption().setOnceLocation(true);
        locationMap.startLocation();
        if (!XHStringUtil.isEmpty(oldSn, false)) {
            linearTop.setVisibility(View.GONE);
            linearQuyuhao.setVisibility(View.GONE);
        } else {
            linearTop.setVisibility(View.VISIBLE);
            linearQuyuhao.setVisibility(View.VISIBLE);
        }
        if (!AppUtils.isLocServiceEnable(this)) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(intent, 100);
        }
        textSn.setText("定位SN:" + yuanChuanSaveData.getSn());
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
                        textAddress.setText("地址:" + address);
                        getAddressCode(address);
                    } else {
                        sendMessageToast("获取位置信息失败");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        dataSave.setText("下一步");
        adapter.setItems(imgPathList);
        adapter.register(String.class, new ImageViewBinder(this));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerImage.setLayoutManager(layoutManager);
        recyclerImage.setAdapter(adapter);
        imgPathList.add("");
        adapter.notifyDataSetChanged();

        View view = getLayoutInflater().inflate(R.layout.m_five_md_text, null);
        textLocation = view.findViewById(R.id.text_dialog);
        mdDialog = new MDDialog.Builder(this)
                .setTitle("确定定位在该区域吗")
                .setContentView(view)
                .setNegativeButton("确定", v -> {
                    caliber = getResources().getStringArray(R.array.m_init_caliber)[caliberSelector.getSelectedItemPosition()];
                    caliber = caliber.replace("mm", "");
                    String name = etName.getText().toString().trim();
                    String info = etInfo.getText().toString().trim();
                    saveDataConverter.setCaliber(caliber);
                    saveDataConverter.setPulseConstant("0");
                    saveDataConverter.setRemark(info);
                    saveDataConverter.setProvinces(provinceCode);
                    saveDataConverter.setCities(cityCode);
                    saveDataConverter.setCounties(districtCode);
                    Gson gson = new Gson();
                    ContentValues values = new ContentValues();
                    values.put("meterType", caliber + "mm");
                    values.put("lat", lat);
                    values.put("lon", lon);
                    values.put("gridcode", textQuyuhao.getText().toString());
                    values.put("productName", name);
                    values.put("jsonImage", gson.toJson(image));
                    values.put("jsonImagePath", gson.toJson(imgPathList));
                    values.put("jsonSaveDataConverter", gson.toJson(saveDataConverter));

                    LitePal.updateAsync(YuanChuanSaveData.class, values, yuanChuanSaveDataId).listen(new UpdateOrDeleteCallback() {
                        @Override
                        public void onFinish(int rowsAffected) {
                            ARouter.getInstance().build(K.UpLoadDataInfoActivity).withLong("yuanChuanSaveDataId", yuanChuanSaveDataId).navigation();
                        }
                    });
                })
                .setPositiveButton("重新定位", v -> {
                })
                .create();

        caliberSelector.setSelection(1);
        caliberSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                caliber = getResources().getStringArray(R.array.m_init_caliber)[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        roundtextSure.setOnClickListener(v -> {
            pushEvent(LSY2InitTypeCode.IFiveSelectTokenRunner, LoginFiveParamManager.getInstance().getLoginData().getTradeRegister().getId() + "");
        });
        chooseCity.setOnClickListener(v -> {
            ARouter.getInstance().build(K.ProvincesActivity).navigation(this, RESULT_DATA);
        });
        dataSave.setOnClickListener(v -> {
            caliber = getResources().getStringArray(R.array.m_init_caliber)[caliberSelector.getSelectedItemPosition()];
            caliber = caliber.replace("mm", "");
            if (XHStringUtil.isEmpty(caliber, false)) {
                sendMessageToast("请选择口径");
                return;
            }
            String name = etName.getText().toString().trim();
            String info = etInfo.getText().toString().trim();
            if (XHStringUtil.isEmpty(name, false)) {
                sendMessageToast("请输入产品名称");
                return;
            }
            if (XHStringUtil.isEmpty(info, false)) {
                sendMessageToast("请输入备注");
                return;
            }
            image = new ArrayList<String>();
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
            if (XHStringUtil.isEmpty(oldSn, false)) {
                if (!canDingWei) {
                    if (XHStringUtil.isEmpty(textCity.getText().toString(), false)) {
                        sendMessageToast("请选择省市县编码");
                        return;
                    }
                }
                mdDialog.show();
            } else {
                yuanChuanSaveData.setTime(TimeUtils.getCurTimeString());
                yuanChuanSaveData.setMeterType(caliber + "mm");
                yuanChuanSaveData.setLat(lat);
                yuanChuanSaveData.setLon(lon);
                yuanChuanSaveData.setProductName(name);
                saveDataConverter.setCaliber(caliber);
                saveDataConverter.setRemark(info);
                saveDataConverter.setProvinces(provinceCode);
                saveDataConverter.setCities(cityCode);
                saveDataConverter.setCounties(districtCode);
                Gson gson = new Gson();
                yuanChuanSaveData.setJsonImage(gson.toJson(image));
                yuanChuanSaveData.setJsonImagePath(gson.toJson(imgPathList));
                yuanChuanSaveData.setLoginId(String.valueOf(LoginFiveParamManager.getInstance().getLoginData().getId()));
                yuanChuanSaveData.setBusinessLicense(String.valueOf(LoginFiveParamManager.getInstance().getLoginData().getTradeRegister().getBusinessLicense()));

                yuanChuanSaveData.setJsonSaveDataConverter(gson.toJson(saveDataConverter));
                saveUpdateData(new Gson().toJson(yuanChuanSaveData));
            }
        });
        SetMapData();
        if (!XHStringUtil.isEmpty(oldSn, false)) {
            pushEvent(LFiveCode.MRepairGetLocationInfoRunner, LoginFiveParamManager.getInstance().getLoginData().getTradeRegister().getId() + "", oldSn);
        } else {
            setAllData(yuanChuanSaveData);
        }
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
                if (XHStringUtil.isEmpty(oldSn, false)) {
                    if (mMarker != null) {
                        mapview.removeOverlay(mMarker);
                        mapview.postInvalidate();
                    }
                }
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshYuanChengBiaoHao(RefreshYuanChengBiaoHao refreshYuanChengBiaoHao) {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
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


    private void getAddressCode(String address) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textLocation.setText(address);
            }
        });
    }

    @Override
    public void onGeoDecodeResult(TGeoAddress tGeoAddress, int i) {
        if (i != TErrorCode.OK) {
            sendMessageToast("获取地址失败");
            return;
        }
        if (tGeoAddress == null) {
            sendMessageToast("获取地址失败");
            return;
        }
        String cityName = tGeoAddress.getCity();
        getAddressCode(cityName);
    }
}
