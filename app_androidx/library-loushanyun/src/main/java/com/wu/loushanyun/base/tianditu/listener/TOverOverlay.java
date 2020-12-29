package com.wu.loushanyun.base.tianditu.listener;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.tianditu.android.maps.GeoPoint;
import com.tianditu.android.maps.MapView;
import com.tianditu.android.maps.MapViewRender;
import com.tianditu.android.maps.Overlay;
import com.tianditu.android.maps.renderoption.DrawableOption;

import javax.microedition.khronos.opengles.GL10;

import met.hx.com.librarybase.some_utils.ImageUtils;


public class TOverOverlay extends Overlay {
    private Drawable mDrawable;
    private GeoPoint mGeoPoint;
    private DrawableOption mOption;
    private SimpleTianDiTuLocationListener simpleTianDiTuLocationListener;

    public TOverOverlay(int drawable,int drawableWidth,int drawableHeight, SimpleTianDiTuLocationListener simpleTianDiTuLocationListener) {
        this.simpleTianDiTuLocationListener = simpleTianDiTuLocationListener;
        Bitmap bitmap=ImageUtils.getBitmap(drawable);
        Bitmap bitmap1=ImageUtils.scale(bitmap,drawableWidth,drawableHeight);
        mDrawable=ImageUtils.bitmap2Drawable(bitmap1);
        mOption = new DrawableOption();
        mOption.setAnchor(0.5f, 1.0f);
    }

    public TOverOverlay(int drawable, SimpleTianDiTuLocationListener simpleTianDiTuLocationListener) {
        this.simpleTianDiTuLocationListener = simpleTianDiTuLocationListener;
        Bitmap bitmap=ImageUtils.getBitmap(drawable);
        Bitmap bitmap1=ImageUtils.scale(bitmap,50,100);
        mDrawable=ImageUtils.bitmap2Drawable(bitmap1);
        mOption = new DrawableOption();
        mOption.setAnchor(0.5f, 1.0f);
    }


    @Override
    public boolean onTap(GeoPoint point, MapView mapView) {
        mGeoPoint = point;
        simpleTianDiTuLocationListener.onTap(point, mapView);
        return true;
    }

    @Override
    public void draw(GL10 gl, MapView mapView, boolean shadow) {
        if (shadow) return;
        MapViewRender render = mapView.getMapViewRender();
        render.drawDrawable(gl, mOption, mDrawable, mGeoPoint);
    }


}