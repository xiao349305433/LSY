package com.wu.loushanyun.base.tianditu.listener;

import android.location.Location;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.tianditu.android.maps.GeoPoint;
import com.tianditu.android.maps.MapView;

public interface TianDiTuLocationListener {
    public void onKeyDown(int keyCode, KeyEvent event, MapView mapView);

    public void onKeyUp(int keyCode, KeyEvent event, MapView mapView);

    public void onTap(GeoPoint p, MapView mapView);

    public void onLongPress(GeoPoint p, MapView mapView);

    public void onTouchEvent(MotionEvent event, MapView mapView);

    public void onLocationChanged(Location location,GeoPoint p);
}
