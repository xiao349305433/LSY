package com.test1moudle.v.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.test1moudle.R;



import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.activity.BaseNoPresenterActivity;
import met.hx.com.base.base.map.LocationMap;
import met.hx.com.base.baseconfig.C;
import met.hx.com.base.baseevent.Event;
import met.hx.com.base.baseinterface.OnMyLocationListener;
import met.hx.com.librarybase.some_utils.LogUtils;

/**
 * Created by huxu on 2017/11/24.
 */
@Route(path = C.LocationActivity)
public class LocationActivity extends BaseNoPresenterActivity implements OnMyLocationListener{
    private Button mMTestButton;
    private Button mMTestButton2;
    private LocationMap locationMap;
    private TextView textLocation;
    private MapView mapView;
    private AMap aMap;;
    private  MyLocationStyle myLocationStyle;;
    private UiSettings mUiSettings;


    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle= msg.getData();
            bundle.getString("text");
            textLocation.setText(bundle.getString("text"));
        }
    };

    @Override
    protected void initLifeCycle() {
        super.initLifeCycle();
        locationMap=new LocationMap(this);
        registerLifeCycle(locationMap);
    }

    @Override
    protected void initEventListener() {

    }

    @Override
    protected void onEventRunEnd(Event event, int code) {

    }

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        ba.mActivityLayoutId= R.layout.m_test_activity_location;
        ba.mTitleText="定位+连续定位";
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mapView= (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        if (aMap == null) {
            aMap = mapView.getMap();
        }

        setUpMap();
    }


    @Override
    protected void initView() {
        mMTestButton = (Button) findViewById(R.id.m_test_button);
        mMTestButton2 = (Button) findViewById(R.id.m_test_button2);
        textLocation = (TextView) findViewById(R.id.text_location);


        mMTestButton.setOnClickListener(v -> {
            locationMap.getLocationOption().setOnceLocation(true);
            locationMap.startLocation();
        });
        mMTestButton2.setOnClickListener(v -> {
            locationMap.getLocationOption().setOnceLocation(false);
            locationMap.startLocation();
        });
    }


    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {

        // 如果要设置定位的默认状态，可以在此处进行设置
        myLocationStyle = new MyLocationStyle();
    //    aMap.setMyLocationStyle(myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE));
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);


        aMap.setMyLocationStyle(myLocationStyle);
        myLocationStyle.showMyLocation(false);
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false

        aMap.getMyLocation();

        aMap.setMinZoomLevel(Float.valueOf(10));//设置最小缩放比例
        aMap.moveCamera(CameraUpdateFactory.zoomTo(Float.valueOf(13)));
        aMap.setMaxZoomLevel(Float.valueOf(19));//设置最大缩放比例

        mUiSettings = aMap.getUiSettings();
        mUiSettings.setScrollGesturesEnabled(true);//设置地图是否可以手势滑动
        mUiSettings.setZoomGesturesEnabled(false);//设置地图是否可以手势缩放大小
        mUiSettings.setTiltGesturesEnabled(false);//设置地图是否可以倾斜
        mUiSettings.setRotateGesturesEnabled(false);//设置地图是否可以旋转
        mUiSettings.setScaleControlsEnabled(true);//设置比例尺
        mUiSettings.setZoomControlsEnabled(true); //设置控制缩放按钮
        mUiSettings.setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示

    }





    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        LatLng latLng=new LatLng( aMapLocation.getLatitude(),aMapLocation.getLongitude());
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));

        Message message=new Message();
        Bundle bundle=new Bundle();
        bundle.putString("text",aMapLocation.toString());
        message.setData(bundle);
        handler.sendMessage(message);
        LogUtils.i("定位",aMapLocation.toString());
    }
}
