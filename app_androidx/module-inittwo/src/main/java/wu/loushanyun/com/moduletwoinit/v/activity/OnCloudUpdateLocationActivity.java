package wu.loushanyun.com.moduletwoinit.v.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.flyco.roundview.RoundTextView;
import com.google.gson.Gson;
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

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.activity.BaseNoPresenterActivity;
import met.hx.com.base.base.glide.GlideUtil;
import met.hx.com.base.base.matisse.MatisseUtil;
import met.hx.com.base.baseevent.Event;
import met.hx.com.librarybase.some_utils.ToastManager;
import met.hx.com.librarybase.some_utils.XHStringUtil;
import wu.loushanyun.com.libraryfive.PictureUtil;
import wu.loushanyun.com.libraryfive.init.LoginFiveParamManager;
import wu.loushanyun.com.moduletwoinit.R;
import wu.loushanyun.com.moduletwoinit.init.MInitTwoCode;
import wu.loushanyun.com.moduletwoinit.m.AreaInfo;
import wu.loushanyun.com.moduletwoinit.m.InsideAreaUpdate;
import wu.loushanyun.com.moduletwoinit.m.RefreshOnCloudEvent;
import wu.loushanyun.com.moduletwoinit.p.runner.GetAreaInfoRunner;
import wu.loushanyun.com.moduletwoinit.p.runner.UpdateAreaInfoRunner;

@Route(path = K.OnCloudUpdateLocationActivity)
public class OnCloudUpdateLocationActivity extends BaseNoPresenterActivity implements TGeoDecode.OnGeoResultListener {
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
    private AreaInfo areaInfo;

    private static final int REQUEST_CODE_CHOOSE1 = 10001;
    private static final int REQUEST_CODE_CHOOSE2 = 10002;
    private static final int REQUEST_CODE_CHOOSE3 = 10003;
    private static final int REQUEST_CODE_CHOOSE4 = 10004;

    private String[] mPhotoPath = new String[4];
    private String fileProvider = "wu.loushanyun.com.fivemoduleapp.fileprovider";

    private float radius = 5;
    private String areaNumber;

    @Override
    public void onGeoDecodeResult(TGeoAddress tGeoAddress, int i) {

    }

    @Override
    protected void initEventListener() {
        registerEventRunner(MInitTwoCode.GetAreaInfoRunner, new GetAreaInfoRunner());
        registerEventRunner(MInitTwoCode.UpdateAreaInfoRunner, new UpdateAreaInfoRunner());

    }

    @Override
    protected void onEventRunEnd(Event event, int code) {
        if (code == MInitTwoCode.GetAreaInfoRunner) {
            if (event.isSuccess()) {
                areaInfo = (AreaInfo) event.getReturnParamAtIndex(0);
                if (areaInfo.getCode() == 0) {
                    setData();
                }
            }
        } else if (code == MInitTwoCode.UpdateAreaInfoRunner) {
            if (event.isSuccess()) {
                int codeR = (int) event.getReturnParamAtIndex(0);
                String msg = (String) event.getReturnParamAtIndex(1);
                if (codeR == 0) {
                    sendMessageToast("上传成功",true);
                } else {
                    sendMessageToast(msg,true);
                }
            }
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(RefreshOnCloudEvent refreshOnCloudEvent) {
        pushEvent(MInitTwoCode.GetAreaInfoRunner, areaNumber);
    }

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        ba.mActivityLayoutId = R.layout.m_twoinit_activity_update_location;
        ba.mTitleText = "更新.云定位";
    }

    private void getAllData() {
        areaNumber = getIntent().getStringExtra("areaNumber");
    }

    @Override
    protected void initView() {
        getAllData();
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
        pushEvent(MInitTwoCode.GetAreaInfoRunner, areaNumber);

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
            if (areaInfo==null){
                sendMessageToast("暂无数据");
                return;
            }
            if (areaInfo.getData() == null) {
                sendMessageToast("未找到对应的区域号");
                return;
            }
            if (TextUtils.isEmpty(areaInfo.getData().getAreaNumber())) {
                sendMessageToast("未找到对应的区域号");
                return;
            }
            ARouter.getInstance().build(K.LSY2LocationDeviceListActivity)
                    .withString("areaNumber", areaInfo.getData().getAreaNumber())
                    .withString("areaName", areaInfo.getData().getAreaName())
                    .withInt("jumpType", LSY2InitTypeCode.TypeFromOnCloudUpdateLocation)
                    .navigation();
        });
        dataSave.setOnClickListener(v -> {
            Gson gson = new Gson();

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
            InsideAreaUpdate insideAreaUpdate = new InsideAreaUpdate();
            insideAreaUpdate.setAreaName(textQuyumingcheng.getText().toString());
            insideAreaUpdate.setAreaNumber(areaNumber);
            insideAreaUpdate.setBusinessLicense(LoginFiveParamManager.getInstance().getLoginData().getTradeRegister().getBusinessLicense());
            insideAreaUpdate.setImageBytes(image);
            insideAreaUpdate.setTradeRegisterId(LoginFiveParamManager.getInstance().getLoginData().getTradeRegister().getId());
            pushEvent(MInitTwoCode.UpdateAreaInfoRunner, gson.toJson(insideAreaUpdate));

        });
        setMapData();
    }

    private void setData() {
        textShuliang.setText(areaInfo.getData().getNums() + "");
        textQuyuhao.setText(areaInfo.getData().getAreaNumber());
        textQuyumingcheng.setText(areaInfo.getData().getAreaName());
        textCenterZuobiao.setText(areaInfo.getData().getLongitude() + "," + areaInfo.getData().getLatitude());
        Gson gson = new Gson();
        String[] strings = areaInfo.getData().getImageUrl().split(",");
        if (strings.length > 0) {
            GlideUtil.display(this, strings[0], R.drawable.m_twoinit_zx, new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    iv1.setImageBitmap(resource);
                    mPhotoPath[0] = PictureUtil.bitmapToString(resource);
                }
            });
            GlideUtil.display(this, strings[1], R.drawable.m_twoinit_zx, new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    iv2.setImageBitmap(resource);
                    mPhotoPath[1] = PictureUtil.bitmapToString(resource);
                }
            });
            GlideUtil.display(this, strings[2], R.drawable.m_twoinit_zx, new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    iv3.setImageBitmap(resource);
                    mPhotoPath[2] = PictureUtil.bitmapToString(resource);
                }
            });
            GlideUtil.display(this, strings[3], R.drawable.m_twoinit_zx, new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    iv4.setImageBitmap(resource);
                    mPhotoPath[3] = PictureUtil.bitmapToString(resource);
                }
            });
        }
        GeoPoint geoPoint = new GeoPoint((int) (Double.valueOf(areaInfo.getData().getLatitude()) * 1E6), (int) (Double.valueOf(areaInfo.getData().getLongitude()) * 1E6));
        circleOverlay = TLocationMap.addCircle(mapview, geoPoint, radius);
        mapview.getController().setCenter(geoPoint);
        mapview.postInvalidate();
    }

    private void setMapData() {
        mapview.setBuiltInZoomControls(true);
        mapview.setPlaceName(true);
        mapview.getController().setZoom(mapview.getMaxZoomLevel());
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
