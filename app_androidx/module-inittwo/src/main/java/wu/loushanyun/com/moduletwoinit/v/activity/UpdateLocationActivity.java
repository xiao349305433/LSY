package wu.loushanyun.com.moduletwoinit.v.activity;

import android.content.Intent;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.flyco.roundview.RoundTextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tianditu.android.maps.GeoPoint;
import com.tianditu.android.maps.MapView;
import com.tianditu.android.maps.TGeoAddress;
import com.tianditu.android.maps.TGeoDecode;
import com.tianditu.android.maps.overlay.CircleOverlay;
import com.wu.loushanyun.base.config.K;
import com.wu.loushanyun.base.tianditu.utils.TLocationMap;
import com.wu.loushanyun.basemvp.init.LSY2InitTypeCode;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
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
import met.hx.com.base.base.glide.GlideUtil;
import met.hx.com.base.base.matisse.MatisseUtil;
import met.hx.com.base.base.rx.RxSchedulers;
import met.hx.com.base.baseevent.Event;
import met.hx.com.librarybase.some_utils.AppUtils;
import met.hx.com.librarybase.some_utils.TimeUtils;
import met.hx.com.librarybase.some_utils.ToastManager;
import met.hx.com.librarybase.some_utils.XHStringUtil;
import wu.loushanyun.com.libraryfive.PictureUtil;
import wu.loushanyun.com.libraryfive.init.LoginFiveParamManager;
import wu.loushanyun.com.moduletwoinit.R;
import wu.loushanyun.com.moduletwoinit.m.OnetoOneConverter;
import wu.loushanyun.com.moduletwoinit.m.RefreshListEvent;
import wu.loushanyun.com.moduletwoinit.m.RefreshTwoLocationEvent;
import wu.loushanyun.com.moduletwoinit.m.WuLianUploadData;

@Route(path = K.UpdateLocationActivity)
public class UpdateLocationActivity extends BaseNoPresenterActivity implements TGeoDecode.OnGeoResultListener {
    private long id;

    private MapView mapview;
    private LinearLayout linearBottom;
    private TextView textShuliang;
    private TextView textQuyuhao;
    private EditText textQuyumingcheng;
    private TextView textCenterZuobiao;
    private ImageView imageFujing;
    private ImageView iv1;
    private ImageView iv2;
    private ImageView iv3;
    private ImageView iv4;
    private RoundTextView dataSave;

    private CircleOverlay circleOverlay;
    private WuLianUploadData wuLianUploadData;

    private static final int REQUEST_CODE_CHOOSE1 = 10001;
    private static final int REQUEST_CODE_CHOOSE2 = 10002;
    private static final int REQUEST_CODE_CHOOSE3 = 10003;
    private static final int REQUEST_CODE_CHOOSE4 = 10004;

    private String[] mPhotoPath = new String[4];
    private String fileProvider = "wu.loushanyun.com.fivemoduleapp.fileprovider";

    private float radius = 5;

    @Override
    public void onGeoDecodeResult(TGeoAddress tGeoAddress, int i) {

    }

    @Override
    protected void initEventListener() {

    }

    @Override
    protected void onEventRunEnd(Event event, int code) {

    }

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        ba.mActivityLayoutId = R.layout.m_twoinit_activity_update_location;
        ba.mTitleText = "更新定位";
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(RefreshTwoLocationEvent refreshTwoLocationEvent) {
        update();
    }


    private void update() {
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

    private void getAllData() {
        id = getIntent().getLongExtra("id", 0);
        if (id != 0) {
            wuLianUploadData = LitePal.find(WuLianUploadData.class, id);
        } else {
            sendMessageToast("没找到本地数据，请删除后重新保存");
            finish();
        }
    }

    @Override
    protected void initView() {
        getAllData();
        update();
        mapview = (MapView) findViewById(R.id.mapview);
        linearBottom = (LinearLayout) findViewById(R.id.linear_bottom);
        textShuliang = (TextView) findViewById(R.id.text_shuliang);
        textQuyuhao = (TextView) findViewById(R.id.text_quyuhao);
        textQuyumingcheng = (EditText) findViewById(R.id.text_quyumingcheng);
        textCenterZuobiao = (TextView) findViewById(R.id.text_center_zuobiao);
        imageFujing = (ImageView) findViewById(R.id.image_fujing);
        iv1 = (ImageView) findViewById(R.id.iv1);
        iv2 = (ImageView) findViewById(R.id.iv2);
        iv3 = (ImageView) findViewById(R.id.iv3);
        iv4 = (ImageView) findViewById(R.id.iv4);
        dataSave = (RoundTextView) findViewById(R.id.data_save);

        if(!AppUtils.isLocServiceEnable(this)){
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(intent, 100);
        };
        textQuyuhao.setText(wuLianUploadData.getAreaNumber());
        textQuyumingcheng.setText(wuLianUploadData.getAreaName());
        textCenterZuobiao.setText(wuLianUploadData.getLon() + "," + wuLianUploadData.getLat());
        Gson gson = new Gson();
        ArrayList<String> arrayList = gson.fromJson(wuLianUploadData.getJsonImage(), new TypeToken<ArrayList<String>>() {
        }.getType());
        mPhotoPath[0] = arrayList.get(0);
        mPhotoPath[1] = arrayList.get(1);
        mPhotoPath[2] = arrayList.get(2);
        mPhotoPath[3] = arrayList.get(3);
        GlideUtil.display(this, iv1, Base64.decode(arrayList.get(0).getBytes(), Base64.DEFAULT));
        GlideUtil.display(this, iv2, Base64.decode(arrayList.get(1).getBytes(), Base64.DEFAULT));
        GlideUtil.display(this, iv3, Base64.decode(arrayList.get(2).getBytes(), Base64.DEFAULT));
        GlideUtil.display(this, iv4, Base64.decode(arrayList.get(3).getBytes(), Base64.DEFAULT));

        iv1.setOnClickListener(view -> {
            MatisseUtil.choosePicture(this, MimeType.ofImage(), 1, REQUEST_CODE_CHOOSE1, fileProvider);
        });
        iv2.setOnClickListener(view -> {
            MatisseUtil.choosePicture(this, MimeType.ofImage(), 1, REQUEST_CODE_CHOOSE2, fileProvider);
        });
        iv3.setOnClickListener(view -> {
            MatisseUtil.choosePicture(this, MimeType.ofImage(), 1, REQUEST_CODE_CHOOSE3, fileProvider);
        });
        iv4.setOnClickListener(view -> {
            MatisseUtil.choosePicture(this, MimeType.ofImage(), 1, REQUEST_CODE_CHOOSE4, fileProvider);
        });
        imageFujing.setOnClickListener(v -> {
            ARouter.getInstance().build(K.LSY2LocationDeviceListActivity)
                    .withString("areaNumber", wuLianUploadData.getAreaNumber())
                    .withString("areaName", wuLianUploadData.getAreaName())
                    .withLong("id", id)
                    .withInt("jumpType", LSY2InitTypeCode.TypeFromUpdateLocation)
                    .navigation();
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
            wuLianUploadData.setAreaName(textQuyumingcheng.getText().toString().trim());
            wuLianUploadData.setTime(TimeUtils.getCurTimeString());
            wuLianUploadData.setJsonImage(gson.toJson(image));
            wuLianUploadData.setBusinessLicense(LoginFiveParamManager.getInstance().getLoginData().getTradeRegister().getBusinessLicense());
            wuLianUploadData.setLoginId(LoginFiveParamManager.getInstance().getLoginData().getId());
            compositeDisposable.add(Flowable.create(
                    (FlowableOnSubscribe<Boolean>) emitter ->
                    {
                        LoadingDialogUtil.showByEvent("正在保存", this.getClass().getName());
                        List<WuLianUploadData> arrayList1 = LitePal.findAll(WuLianUploadData.class);
                        long id = 0;
                        for (WuLianUploadData wuLianUploadData1 : arrayList1) {
                            if (wuLianUploadData1.getAreaNumber().equals(wuLianUploadData.getAreaNumber())) {
                                id = wuLianUploadData1.getBaseObjId();
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
                            sendMessageToast("保存成功，请到网络好的地方同步");
                            EventBus.getDefault().post(new RefreshListEvent());
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
        GeoPoint geoPoint = TLocationMap.getGeoPoint(mapview, Double.valueOf(wuLianUploadData.getLon().trim()), Double.valueOf(wuLianUploadData.getLat().trim()));
        circleOverlay = TLocationMap.addCircle(mapview, geoPoint, radius);
        mapview.getController().setCenter(geoPoint);
        mapview.postInvalidate();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_CHOOSE1) {
                GlideUtil.display(this, iv1, Matisse.obtainPathResult(intent).get(0));
                mPhotoPath[0] = PictureUtil.bitmapToString(Matisse.obtainPathResult(intent).get(0));
            } else if (requestCode == REQUEST_CODE_CHOOSE2) {
                GlideUtil.display(this, iv2, Matisse.obtainPathResult(intent).get(0));
                mPhotoPath[1] = PictureUtil.bitmapToString(Matisse.obtainPathResult(intent).get(0));
            } else if (requestCode == REQUEST_CODE_CHOOSE3) {
                GlideUtil.display(this, iv3, Matisse.obtainPathResult(intent).get(0));
                mPhotoPath[2] = PictureUtil.bitmapToString(Matisse.obtainPathResult(intent).get(0));
            } else if (requestCode == REQUEST_CODE_CHOOSE4) {
                GlideUtil.display(this, iv4, Matisse.obtainPathResult(intent).get(0));
                mPhotoPath[3] = PictureUtil.bitmapToString(Matisse.obtainPathResult(intent).get(0));
            }
        }

    }


}
