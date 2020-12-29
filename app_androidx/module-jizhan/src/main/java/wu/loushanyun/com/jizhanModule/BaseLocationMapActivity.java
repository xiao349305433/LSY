package wu.loushanyun.com.jizhanModule;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.amap.api.location.AMapLocation;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.elvishew.xlog.XLog;
import com.flyco.roundview.RoundRelativeLayout;
import com.flyco.roundview.RoundTextView;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.tianditu.android.maps.GeoPoint;
import com.tianditu.android.maps.MapView;
import com.tianditu.android.maps.TGeoAddress;
import com.tianditu.android.maps.TGeoDecode;
import com.tianditu.android.maps.TGeoDecode.OnGeoResultListener;
import com.wu.loushanyun.base.config.K;
import com.wu.loushanyun.base.tianditu.listener.SimpleTianDiTuLocationListener;
import com.wu.loushanyun.base.tianditu.listener.TLocationOverlay;
import com.wu.loushanyun.base.tianditu.listener.TOverOverlay;
import com.wu.loushanyun.base.url.URLUtils;
import com.wu.loushanyun.basemvp.m.sat.SAtInstructRealizeFactory;
import com.zhihu.matisse.Matisse;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.activity.BaseNoPresenterActivity;
import met.hx.com.base.base.glide.GlideUtil;
import met.hx.com.base.base.map.LocationMap;
import met.hx.com.base.base.multitype.MultiTypeAdapter;
import met.hx.com.base.base.webview.BaseWebView;
import met.hx.com.base.base.webview.jsbridge.BridgeHandler;
import met.hx.com.base.base.webview.jsbridge.CallBackFunction;
import met.hx.com.base.baseevent.Event;
import met.hx.com.base.baseinterface.OnMyLocationListener;
import met.hx.com.librarybase.some_utils.LogUtils;
import met.hx.com.librarybase.some_utils.ToastManager;
import met.hx.com.librarybase.some_utils.XHStringUtil;
import wu.loushanyun.com.libraryfive.PictureUtil;
import wu.loushanyun.com.libraryfive.ResponseAnalysis;
import wu.loushanyun.com.libraryfive.init.LoginFiveParamManager;
import wu.loushanyun.com.libraryfive.m.RefreshStation;
import wu.loushanyun.com.libraryfive.p.adapter.ImageViewBinder;


/**
 * AMapV2地图中介绍如何显示一个基本地图
 */
public class BaseLocationMapActivity extends BaseNoPresenterActivity implements
        OnClickListener, OnGeoResultListener {
    private static final int REQUEST_CODE_CHOOSE1 = 10001;
    private static final int REQUEST_CODE_CHOOSE2 = 10002;
    private static final int REQUEST_CODE_CHOOSE3 = 10003;
    private static final int REQUEST_CODE_CHOOSE4 = 10004;
    private static final int RESULT_DATA = 1002;

    private String[] mPhotoPath = new String[4];

    private Toast toast;
    private ProgressDialog m_dlg_pgb; // loading

    private double lat;
    private double lon;
    private String sn, baseName, tags, status, remark;
    int baseId;


    private TGeoDecode mGeoDecode;

    private String provinceCode = "00";
    private String cityCode = "00";
    private String districtCode = "00";
    String province = "";
    String city = "";
    String district = "";

    private boolean locationEndble = true;
    private String[] imageUrls;
    private String imageUrl;


    private ArrayList<String> image;
    private String channel;
    private boolean canDingWei = false;

    MultiTypeAdapter adapter = new MultiTypeAdapter();
    ArrayList<String> imgPathList = new ArrayList<>(5);

    private MapView mapview;
    private TextView textAddress;
    private TextView tvBaseStatus;
    private Spinner atXindaocanshuSelect;
    private RoundRelativeLayout rlLocationJzmc;
    private TextView tvBaseNamex;
    private EditText tvBaseName;
    private RoundRelativeLayout llLocationTags;
    private TextView tvJingdu;
    private EditText etLocationTags;
    private TextView textCity;
    private RoundTextView chooseCity;
    private TextView tvBeizhu;
    private EditText etLocationRemark;
    private RecyclerView recyclerImage;
    private RoundTextView btSave;
    private BaseWebView baseWebView;
    private SAtInstructRealizeFactory sAtInstructRealizeFactory;
    private LocationMap locationMap;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        sn = intent.getStringExtra("sn");
        baseName = intent.getStringExtra("baseName");
        imageUrl = intent.getStringExtra("imageUrl");
        tags = intent.getStringExtra("tags");
        status = intent.getStringExtra("status");
        remark = intent.getStringExtra("remark");
        channel = intent.getStringExtra("channel");
        baseId = intent.getIntExtra("baseId", 0);
        lon = intent.getDoubleExtra("longitude", 0.0);
        lat = intent.getDoubleExtra("latitude", 0.0);
        if (!XHStringUtil.isEmpty(imageUrl, false)) {
            if (!imageUrl.equals("0")) {
                imageUrls = imageUrl.split(",");
            }
        }
        SetView();
        SetData();
        locationMap.getLocationOption().setOnceLocation(true);
        locationMap.startLocation();
    }

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        ba.mActivityLayoutId = R.layout.m_join_base_location_map_activity;
        ba.mTitleText = "基站详情";
    }


    @Override
    protected void initView() {

    }

    @Override
    protected void initEventListener() {

    }

    @Override
    protected void onEventRunEnd(Event event, int code) {

    }

    private void SetView() {


        mapview = (MapView) findViewById(R.id.mapview);
        textAddress = (TextView) findViewById(R.id.text_address);
        tvBaseStatus = (TextView) findViewById(R.id.tv_base_status);
        atXindaocanshuSelect = (Spinner) findViewById(R.id.at_xindaocanshu_select);
        rlLocationJzmc = (RoundRelativeLayout) findViewById(R.id.rl_location_jzmc);
        tvBaseNamex = (TextView) findViewById(R.id.tv_base_namex);
        tvBaseName = (EditText) findViewById(R.id.tv_base_name);
        llLocationTags = (RoundRelativeLayout) findViewById(R.id.ll_location_tags);
        tvJingdu = (TextView) findViewById(R.id.tv_jingdu);
        etLocationTags = (EditText) findViewById(R.id.et_location_tags);
        textCity = (TextView) findViewById(R.id.text_city);
        chooseCity = (RoundTextView) findViewById(R.id.choose_city);
        tvBeizhu = (TextView) findViewById(R.id.tv_beizhu);
        etLocationRemark = (EditText) findViewById(R.id.et_location_remark);
        recyclerImage = (RecyclerView) findViewById(R.id.recycler_image);
        btSave = (RoundTextView) findViewById(R.id.bt_save);
        baseWebView = (BaseWebView) findViewById(R.id.baseWebView);

        sAtInstructRealizeFactory = new SAtInstructRealizeFactory();

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(BaseLocationMapActivity.this, R.layout.l_loushanyun_item_spinner, sAtInstructRealizeFactory.getAllMoShi());
        atXindaocanshuSelect.setAdapter(arrayAdapter);


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
        if (!XHStringUtil.isEmpty(imageUrl, false) && !imageUrl.equals("0") && imageUrls != null) {
            for (int i = 0; i < imageUrls.length; i++) {
                int finalI = i;
                GlideUtil.display(this, imageUrls[i], R.drawable.base_chat_img, new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        imgPathList.add(imageUrls[finalI]);
                        mPhotoPath[finalI] = PictureUtil.bitmapToString(resource);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                        if (imgPathList.size() == 0) {
                            imgPathList.add("");
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
            }

        } else {
            imgPathList.add("");
        }

        adapter.setItems(imgPathList);
        adapter.register(String.class, new ImageViewBinder(this));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerImage.setLayoutManager(layoutManager);
        recyclerImage.setAdapter(adapter);
//        adapter.notifyDataSetChanged();


        tvBaseStatus.setText(status);
        tvBaseName.setText(baseName);
        etLocationTags.setText(tags);
        etLocationRemark.setText(remark);
        btSave.setOnClickListener(this);
        chooseCity.setOnClickListener(v -> {
            ARouter.getInstance().build(K.ProvincesActivity).navigation(this, RESULT_DATA);
        });
        if (!XHStringUtil.isEmpty(channel.trim(), false)) {
            atXindaocanshuSelect.setSelection(sAtInstructRealizeFactory.getMoShiIndex(channel));
        }

    }

    private void SetData() {

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
        TOverOverlay overOverlay = new TOverOverlay(R.drawable.l_loushanyun_overlay, new SimpleTianDiTuLocationListener() {
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
                XLog.i("发送成功 " + data);
            }

        });
//        // 开始搜索
//        if (mGeoDecode == null)
//            mGeoDecode = new TGeoDecode(this);
//        mGeoDecode.search(geoPoint);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapview.onDestroy();
    }

    private void getAddressCode(String address) {
    }

    @Override
    public void onClick(View arg0) {
        if (arg0 == btSave) {
            if (XHStringUtil.isEmpty(textCity.getText().toString(), false)) {
                sendMessageToast("请选择省市县编码");
                return;
            }
            if (etLocationRemark.getText().toString().equals("")) {
                Toast.makeText(this, "请您输入备注信息！", Toast.LENGTH_SHORT).show();
                return;
            }
            image = new ArrayList<String>();
            // 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
            for (int i = 0; i < mPhotoPath.length; i++) {
                if (!XHStringUtil.isEmpty(mPhotoPath[i], false)) {
                    String strPath = mPhotoPath[i];

                    image.add(strPath);
                } else {
                    sendMessageToast("第" + (i + 1) + "张图片地址不存在");
                }
            }
            if (image.size() < 4) {
                ToastManager.getInstance(this).show("请您添加4张图片");
                return;
            }
            baseName = tvBaseName.getText().toString().trim();
            tags = etLocationTags.getText().toString().trim();
            String remark = etLocationRemark.getText().toString().trim();
            upload(sn, baseName, tags, remark, baseId);
        }
    }


    /**
     * 设备定位上传
     */
    private void upload(String sn, String baseName, String tags, String remark, Integer baseId) {
        if (image != null && image.size() == 4) {

            if (LoginFiveParamManager.getInstance().getLoginData() != null) {
                int loginId = LoginFiveParamManager.getInstance().getLoginData().getId();
                m_dlg_pgb = ProgressDialog.show(this, "上传", "上传中，请稍等");
                JSONObject jObject = new JSONObject();
                try {
                    jObject.put("longitude", lon);// 经度
                    jObject.put("latitude", lat);// 纬度
                    int trId = LoginFiveParamManager.getInstance().getLoginData().getTradeRegister().getId();
                    jObject.put("sn", sn);
                    jObject.put("baseName", baseName);
                    jObject.put("tags", tags);
                    jObject.put("remark", remark);
                    jObject.put("baseId", baseId);
                    jObject.put("province", provinceCode);
                    jObject.put("city", cityCode);
                    jObject.put("county", districtCode);
                    jObject.put("channel", atXindaocanshuSelect.getSelectedItem().toString().trim());
                    jObject.put("image", image.get(0));
                    jObject.put("image1", image.get(1));
                    jObject.put("image2", image.get(2));
                    jObject.put("image3", image.get(3));

                    jObject.put("loginId", loginId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                HttpUtils utils = new HttpUtils();
                RequestParams params = new RequestParams();

                params.addBodyParameter("baseLocationData", jObject.toString());

                String url = URLUtils.getIP() + URLUtils.IFiveBaseStationLocation;
                LogUtils.i("请求的url" + url);
                LogUtils.i("请求的参数" + jObject.toString());

                utils.send(HttpMethod.POST, url, params,
                        new RequestCallBack<String>() {
                            // 访问成功
                            @SuppressWarnings("rawtypes")
                            @Override
                            public void onSuccess(ResponseInfo responseInfo) {
                                String result = (String) responseInfo.result;
                                System.out.println("返回结果:" + result);

                                if (m_dlg_pgb != null) {
                                    m_dlg_pgb.dismiss();
                                }

                                Gson gson = new Gson();
                                ResponseAnalysis aLoginAnalysis = gson.fromJson(result, ResponseAnalysis.class);
                                int err_code = aLoginAnalysis.getErr_code();
                                if (err_code == 0) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(BaseLocationMapActivity.this);
                                    builder.setTitle("上传状态");
                                    Object object = aLoginAnalysis.getData().get(0);
                                    builder.setMessage("保存成功记录：" + object + "条数据");
                                    builder.setPositiveButton(
                                            "确定",
                                            (dialog, which) -> {
                                                finish();
                                                EventBus.getDefault().post(new RefreshStation());
                                            });

                                    AlertDialog alertDialog = builder.create();

                                    alertDialog.setCancelable(false);// 设置这个对话框不能被用户按[返回键]而取消掉,但测试发现如果用户按了
                                    // KeyEvent.KEYCODE_SEARCH,对话框还是会Dismiss掉
                                    // 由于设置alertDialog.setCancelable(false);
                                    // 发现如果用户按了KeyEvent.KEYCODE_SEARCH,对话框还是会Dismiss掉,这里的setOnKeyListener作用就是屏蔽用户按下KeyEvent.KEYCODE_SEARCH
                                    alertDialog
                                            .setOnKeyListener(new DialogInterface.OnKeyListener() {
                                                @Override
                                                public boolean onKey(
                                                        DialogInterface dialog,
                                                        int keyCode,
                                                        KeyEvent event) {
                                                    if (keyCode == KeyEvent.KEYCODE_SEARCH) {
                                                        return true;
                                                    } else {
                                                        return false; // 默认返回
                                                        // false
                                                    }
                                                }
                                            });
                                    alertDialog.show();
                                } else {
                                    if (m_dlg_pgb != null) {
                                        m_dlg_pgb.dismiss();
                                    }
                                    ShowToast("保存失败");
                                }
                            }

                            // 访问失败
                            @Override
                            public void onFailure(HttpException error,
                                                  String msg) {
                                if (m_dlg_pgb != null) {
                                    m_dlg_pgb.dismiss();
                                }
                                ShowToast("服务器连接错误");
                                error.printStackTrace();
                            }
                        });
            } else {
                ShowToast("登录用户不存在");
            }
        } else {
            toast = Toast.makeText(BaseLocationMapActivity.this,
                    "请先点击图片添加照片", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
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

    public void ShowToast(String text) {
        Toast makeText = Toast.makeText(BaseLocationMapActivity.this, text,
                Toast.LENGTH_SHORT);
        makeText.setGravity(Gravity.CENTER, 0, 0);
        makeText.show();
    }


    @Override
    public void onGeoDecodeResult(TGeoAddress addr, int errCode) {
        getAddressCode(addr.getCity());
    }

}
