package com.wu.loushanyun.base.tianditu.utils;


import android.graphics.Point;

import com.tianditu.android.maps.GeoPoint;
import com.tianditu.android.maps.MapView;
import com.tianditu.android.maps.Projection;
import com.tianditu.android.maps.TGeoDecode;
import com.tianditu.android.maps.overlay.CircleOverlay;
import com.tianditu.android.maps.renderoption.CircleOption;

import met.hx.com.librarybase.some_utils.LogUtils;

public class TLocationMap {

    //地图基本设置
    /** private void SetData() {
     mMapView = (com.tianditu.android.maps.MapView) this.findViewById(R.id.mapview);
     mMapView.setBuiltInZoomControls(true);
     mMapView.setPlaceName(true);
     mMapView.getController().setZoom(20);
     TLocationOverlay mLocation = new TLocationOverlay(this, mMapView,new SimpleTianDiTuLocationListener(){
    @Override public void onLocationChanged(Location location, GeoPoint p) {
    super.onLocationChanged(location, p);
    if (locationEndble) {
    searchGeoDecode(p);
    locationEndble = false;
    }
    }
    });
     TOverOverlay overOverlay = new TOverOverlay(this,R.drawable.icon_overlay_nearby_center, new SimpleTianDiTuLocationListener(){
    @Override public void onTap(GeoPoint p, MapView mapView) {
    super.onTap(p, mapView);
    searchGeoDecode(p);
    }
    });
     mLocation.enableMyLocation();
     mMapView.getController().setCenter(mLocation.getMyLocation());
     mMapView.addOverlay(mLocation);
     mMapView.addOverlay(overOverlay);
     mMapView.postInvalidate();
     }
     **/
    /**
     * 获取某一个点是否在在某一圆内
     *
     * @param mapView
     * @param geoPointOld
     * @param geoPointNew
     * @param radius
     * @return
     */
    public static boolean containsPoint(MapView mapView, GeoPoint geoPointOld, GeoPoint geoPointNew, float radius) {
        LogUtils.i("老的=" + geoPointOld.toString());
        LogUtils.i("新的=" + geoPointNew.toString());
        Projection projection = mapView.getProjection();
        Point pointOld = projection.toPixels(geoPointOld, null);
        Point pointNew = projection.toPixels(geoPointNew, null);
        radius = projection.metersToEquatorPixels(radius);
        LogUtils.i("老的屏幕=" + pointOld.toString());
        LogUtils.i("新的屏幕=" + pointNew.toString());
        LogUtils.i("半径=" + radius);
        pointNew.x -= pointOld.x;
        pointNew.y -= pointOld.y;
        boolean result = radius * radius >= pointNew.x * pointNew.x + pointNew.y * pointNew.y;
        return result;
    }

    /***
     * 根据经纬度获取point
     * @param mapView
     * @param dLon
     * @param dLat
     * @return
     */
    public static GeoPoint getGeoPoint(MapView mapView, double dLon, double dLat) {
        double SPANLON = mapView.getLongitudeSpan();
        double SPANLAT = mapView.getLatitudeSpan();
        GeoPoint geoPointOld = new GeoPoint((int) ((dLat + SPANLAT / 2) * 1E6), (int) ((dLon + SPANLON / 2) * 1E6));
        return geoPointOld;
    }

    /**
     * 画圆形区域
     *
     * @param mapView
     * @param geoPointOld
     * @param radius
     * @return
     */
    public static CircleOverlay addCircle(MapView mapView, GeoPoint geoPointOld, float radius) {
        CircleOption option = new CircleOption();
        option.setFillColor(0xAAFF0000);
        option.setStrokeColor(0xFF000000);
        CircleOverlay overlay = new CircleOverlay();
        overlay.setOption(option);
        overlay.setCenter(geoPointOld);
        overlay.setRadius((int) radius);
        mapView.addOverlay(overlay);
        return overlay;
    }

    //反地理编码
    public static void searchGeoDecode(GeoPoint geoPoint, TGeoDecode.OnGeoResultListener onResult) {
        TGeoDecode mGeoDecode = new TGeoDecode(onResult);
        mGeoDecode.search(geoPoint);
    }

    public static double getLon(GeoPoint geoPoint) {
        return geoPoint.getLongitudeE6() / (double) 1000000;
    }

    public static double getLat(GeoPoint geoPoint) {
        return geoPoint.getLatitudeE6() / (double) 1000000;
    }


}
