package com.wu.loushanyun.base.tianditu.listener;

import android.content.Context;
import android.location.Location;

import com.tianditu.android.maps.MapView;
import com.tianditu.android.maps.MyLocationOverlay;

public class TLocationOverlay extends MyLocationOverlay {
    private SimpleTianDiTuLocationListener simpleTianDiTuLocationListener;

    public TLocationOverlay(Context context, MapView mapView, SimpleTianDiTuLocationListener simpleTianDiTuLocationListener) {
        super(context, mapView);
        this.simpleTianDiTuLocationListener = simpleTianDiTuLocationListener;
    }

    @Override
        public void onLocationChanged(Location location) {
            super.onLocationChanged(location);
            simpleTianDiTuLocationListener.onLocationChanged(location,getMyLocation());
        }

    }