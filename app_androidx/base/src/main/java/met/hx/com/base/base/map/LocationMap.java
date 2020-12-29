package met.hx.com.base.base.map;

import android.Manifest;
import android.app.Activity;
import android.os.Bundle;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.tbruyelle.rxpermissions2.RxPermissions;

import met.hx.com.base.baseinterface.OnMyGeocodeSearchedListener;
import met.hx.com.base.baseinterface.OnMyLocationListener;
import met.hx.com.base.baseinterface.OnMyRegeocodeSearchedListener;
import met.hx.com.base.baseinterface.SimpleLifeCycle;
import met.hx.com.librarybase.some_utils.LogUtils;

/**
 * Created by huxu on 2017/11/24.
 */

public class LocationMap extends SimpleLifeCycle implements GeocodeSearch.OnGeocodeSearchListener {
    public AMapLocationClient locationClient;
    public AMapLocationClientOption locationOption;

    public OnMyLocationListener myLocationListener;
    /**
     * 高德地图-生成静态地图图片url, 其中location的值在使用时请自行替换
     */
    private RxPermissions rxPermissions;
    private GeocodeSearch geocoderSearch;
    private OnMyGeocodeSearchedListener onMyGeocodeSearchedListener;
    private OnMyRegeocodeSearchedListener onMyRegeocodeSearchedListener;


    /**
     * 需要定位
     *
     * @param myLocationListener
     */
    public LocationMap(OnMyLocationListener myLocationListener) {
        this.myLocationListener = myLocationListener;
    }

    /**
     * 定位以及查询的监听
     * @param myLocationListener
     * @param var1
     */
    public LocationMap(OnMyLocationListener myLocationListener, GeocodeSearch.OnGeocodeSearchListener var1) {
        this.myLocationListener = myLocationListener;
        if (geocoderSearch == null) {
            geocoderSearch = new GeocodeSearch(getActivity());
            geocoderSearch.setOnGeocodeSearchListener(var1);
        }
    }

    /**
     * 通过坐标获取位置的监听
     */
    public LocationMap(OnMyRegeocodeSearchedListener onMyRegeocodeSearchedListener) {
        this.onMyRegeocodeSearchedListener = onMyRegeocodeSearchedListener;
    }

    /**
     * 通过位置获取坐标的监听
     */
    public LocationMap(OnMyGeocodeSearchedListener onMyGeocodeSearchedListener) {
        this.onMyGeocodeSearchedListener = onMyGeocodeSearchedListener;
    }

    /**
     * 双监听
     */
    public LocationMap(GeocodeSearch.OnGeocodeSearchListener var1) {
        if (geocoderSearch == null) {
            geocoderSearch = new GeocodeSearch(getActivity());
            geocoderSearch.setOnGeocodeSearchListener(var1);
        }
    }

    public void setOnMyGeocodeSearchedListener(OnMyGeocodeSearchedListener onMyGeocodeSearchedListener) {
        this.onMyGeocodeSearchedListener = onMyGeocodeSearchedListener;
    }

    public void setOnMyRegeocodeSearchedListener(OnMyRegeocodeSearchedListener onMyRegeocodeSearchedListener) {
        this.onMyRegeocodeSearchedListener = onMyRegeocodeSearchedListener;
    }

    private void registerSearch() {
        if (geocoderSearch == null) {
            geocoderSearch = new GeocodeSearch(getActivity());
            geocoderSearch.setOnGeocodeSearchListener(this);
        }
    }

    /**
     * 1）可以在回调中解析result，获取坐标信息。
     * 2）返回结果成功或者失败的响应码。1000为成功，其他为失败（详细信息参见网站开发指南-实用工具-错误码对照表）
     *
     * @param query // name表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode
     */
    public void queryGeocode(GeocodeQuery query) {
        registerSearch();
        if (geocoderSearch != null) {
            geocoderSearch.getFromLocationNameAsyn(query);
        }
    }

    /**
     * 1）可以在回调中解析result，获取地址、adcode等等信息。
     * 2）返回结果成功或者失败的响应码。1000为成功，其他为失败（详细信息参见网站开发指南-实用工具-错误码对照表）
     *
     * @param query // 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
     */
    public void queryRegeocode(RegeocodeQuery query) {
        registerSearch();
        if (geocoderSearch != null) {
            geocoderSearch.getFromLocationAsyn(query);
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState, Activity activity) {
        super.onCreate(savedInstanceState, activity);
        rxPermissions = new RxPermissions(getActivity());
        if (myLocationListener != null) {
            initLocation();
        }
    }

    /**
     * 默认的定位参数(默认单次定位，多次定位可重新设置)
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    public AMapLocationClientOption getDefaultOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(true);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
        return mOption;
    }

    public AMapLocationClient initLocation() {
        //初始化client
        locationClient = new AMapLocationClient(getActivity().getApplicationContext());
        locationOption = getDefaultOption();
        //设置定位参数
        locationClient.setLocationOption(locationOption);
        // 设置定位监听
        locationClient.setLocationListener(location -> {
            if (null != location) {
                if (location.getErrorCode() == 0) {
                    XLocationManager.getInstance().setLocation(location);
                    LogUtils.i("定位成功", location.toString());
                } else {
                    LogUtils.e("定位失败", location.getErrorInfo());
                }
            } else {
                LogUtils.i("定位失败");
            }
            if (myLocationListener != null) {
                myLocationListener.onLocationChanged(location);
            }
        });
        return locationClient;
    }

    /**
     * 开始定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    public void startLocation() {
        if (locationClient != null) {
            rxPermissions.request(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                    .subscribe(aBoolean -> {
                        if (aBoolean) {
                            // 设置定位参数
                            locationClient.setLocationOption(locationOption);
                            // 启动定位
                            locationClient.startLocation();
                        }
                    });

        } else {
            LogUtils.i("没有注册生命周期");
        }

    }

    public AMapLocationClient getLocationClient() {
        return locationClient;
    }

    public AMapLocationClientOption getLocationOption() {
        return locationOption;
    }

    /**
     * 停止定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    public void stopLocation() {
        // 停止定位
        locationClient.stopLocation();
    }

    /**
     * 销毁定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    public void destroyLocation() {
        if (null != locationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            locationClient.onDestroy();
            locationClient = null;
            locationOption = null;
            myLocationListener=null;
            onMyGeocodeSearchedListener=null;
            onMyRegeocodeSearchedListener=null;
        }
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onRestart() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestroy() {
        destroyLocation();
        super.onDestroy();
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
        if (onMyRegeocodeSearchedListener != null) {
            onMyRegeocodeSearchedListener.OnMyRegeocodeSearched(regeocodeResult, i);
        }

    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
        if (onMyGeocodeSearchedListener != null) {
            onMyGeocodeSearchedListener.OnMyGeocodeSearched(geocodeResult, i);
        }

    }
}
