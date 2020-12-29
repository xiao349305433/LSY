package wu.loushanyun.com.modulerepair.v.activity;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Location;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.tianditu.android.maps.GeoPoint;
import com.tianditu.android.maps.MapView;
import com.tianditu.android.maps.overlay.CircleOverlay;
import com.tianditu.android.maps.overlay.MarkerOverlay;
import com.wu.loushanyun.base.config.K;
import com.wu.loushanyun.base.tianditu.listener.SimpleTianDiTuLocationListener;
import com.wu.loushanyun.base.tianditu.listener.TLocationOverlay;
import com.wu.loushanyun.base.tianditu.utils.TLocationMap;

import java.util.ArrayList;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.activity.BaseNoPresenterActivity;
import met.hx.com.base.base.multitype.MultiTypeAdapter;
import met.hx.com.base.baseevent.Event;
import met.hx.com.librarybase.some_utils.ImageUtils;
import met.hx.com.librarybase.some_utils.LogUtils;
import wu.loushanyun.com.libraryfive.init.LFiveCode;
import wu.loushanyun.com.libraryfive.init.LoginFiveParamManager;
import wu.loushanyun.com.libraryfive.m.RepairLocationDetail;
import wu.loushanyun.com.libraryfive.p.adapter.ImageBinder;
import wu.loushanyun.com.libraryfive.p.runner.MRepairGetLocationInfoRunner;
import wu.loushanyun.com.modulerepair.R;

@Route(path = K.LocationDetailActivity)
public class LocationDetailActivity extends BaseNoPresenterActivity {
    private String oldSn;
    private String productForm;

    private MapView mapview;
    private LinearLayout linearBottom;
    private LinearLayout linearChanpin;
    private TextView textChanpin;
    private LinearLayout linearQuyu;
    private TextView textQuyumingcheng;
    private TextView textRemark;
    private RecyclerView recyclerImage;

    private RepairLocationDetail repairLocationDetail;
    private CircleOverlay circleOverlay;
    private ImageBinder imageBinder;
    private MultiTypeAdapter multiTypeAdapter;
    private ArrayList<String> arrayList;

    @Override
    protected void initEventListener() {
        registerEventRunner(LFiveCode.MRepairGetLocationInfoRunner, new MRepairGetLocationInfoRunner());

    }

    @Override
    protected void onEventRunEnd(Event event, int code) {
        if (code == LFiveCode.MRepairGetLocationInfoRunner) {
            if (event.isSuccess()) {
                int codeReturn= (int) event.getReturnParamAtIndex(0);
                String msg= (String) event.getReturnParamAtIndex(1);
                sendMessageToast(msg);
                if(codeReturn==0){
                    repairLocationDetail = (RepairLocationDetail) event.getReturnParamAtIndex(2);
                    setAllData(repairLocationDetail);
                }
            }
        }

    }

    private void setAllData(RepairLocationDetail repairLocationDetail) {
        mapview.setBuiltInZoomControls(true);
        mapview.setPlaceName(true);
        mapview.getController().setZoom(mapview.getMaxZoomLevel());
        TLocationOverlay mLocation = new TLocationOverlay(this, mapview, new SimpleTianDiTuLocationListener() {
            @Override
            public void onLocationChanged(Location location, GeoPoint p) {
                super.onLocationChanged(location, p);
            }
        });
        GeoPoint geoPoint = new GeoPoint((int) (Double.valueOf(repairLocationDetail.getLat()) * 1E6), (int) (Double.valueOf(repairLocationDetail.getLon()) * 1E6));
        circleOverlay = TLocationMap.addCircle(mapview, geoPoint, 5);
        MarkerOverlay mMarker = new MarkerOverlay();
        mMarker.setPosition(geoPoint);
        Bitmap bitmap = ImageUtils.getBitmap(R.drawable.l_loushanyun_overlay);
        Bitmap bitmap1 = ImageUtils.scale(bitmap, 50, 100);
        Drawable mDrawable = ImageUtils.bitmap2Drawable(bitmap1);
        mMarker.setIcon(mDrawable);
        mLocation.enableMyLocation();
        mapview.addOverlay(mMarker);
        mapview.addOverlay(mLocation);
        mapview.getController().setCenter(geoPoint);
        mapview.postInvalidate();

        textChanpin.setText(repairLocationDetail.getProductName());
        textQuyumingcheng.setText(repairLocationDetail.getAreaName());
        textRemark.setText(repairLocationDetail.getRemark());
        String[] strings = repairLocationDetail.getImageUrl().split(".png");
        for (int i = 0; i < strings.length; i++) {
            arrayList.add(strings[i] + ".png");
        }
        multiTypeAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        getAllData();
        ba.mActivityLayoutId = R.layout.m_repair_activity_location_detail;
        ba.mTitleText = "SN:" + oldSn + "-定位详情";
    }

    private void getAllData() {
        oldSn = getIntent().getStringExtra("oldSn");
        productForm = getIntent().getStringExtra("productForm");
        LogUtils.i("产品形式"+productForm);
    }

    @Override
    protected void initView() {

        mapview = (MapView) findViewById(R.id.mapview);
        linearBottom = (LinearLayout) findViewById(R.id.linear_bottom);
        linearChanpin = (LinearLayout) findViewById(R.id.linear_chanpin);
        textChanpin = (TextView) findViewById(R.id.text_chanpin);
        linearQuyu = (LinearLayout) findViewById(R.id.linear_quyu);
        textQuyumingcheng = (TextView) findViewById(R.id.text_quyumingcheng);
        textRemark = (TextView) findViewById(R.id.text_remark);
        recyclerImage = (RecyclerView) findViewById(R.id.recycler_image);

        GridLayoutManager layoutManage = new GridLayoutManager(this, 4);
        recyclerImage.setLayoutManager(layoutManage);
        imageBinder = new ImageBinder(this);
        arrayList = new ArrayList<>();
        multiTypeAdapter = new MultiTypeAdapter();
        multiTypeAdapter.register(String.class, imageBinder);
        multiTypeAdapter.setItems(arrayList);
        recyclerImage.setAdapter(multiTypeAdapter);
        pushEvent(LFiveCode.MRepairGetLocationInfoRunner, LoginFiveParamManager.getInstance().getLoginData().getTradeRegister().getId() + "", oldSn);
        if(productForm.equals("远传表号接入")){
            linearChanpin.setVisibility(View.VISIBLE);
            linearQuyu.setVisibility(View.GONE);
        }else if(productForm.equals("远传物联网端")){
            linearQuyu.setVisibility(View.VISIBLE);
            linearChanpin.setVisibility(View.GONE);
        }
    }

}
