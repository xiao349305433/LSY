package com.test1moudle.v.views;

import android.content.Context;
import android.graphics.PointF;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class MyViewPager extends ViewPager {
    private boolean noScroll = false;

    public MyViewPager(Context context) {
        super(context);
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setNoScroll(boolean noScroll) {
        this.noScroll = noScroll;
    }

    PointF downPoint = new PointF();
    OnSingleTouchListener onSingleTouchListener;

    @Override
    public boolean onTouchEvent(MotionEvent evt) {

        if (noScroll) {
            return false;
        } else {

            switch (evt.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    // 记录按下时候的坐标t
                    downPoint.x = evt.getX();
                    downPoint.y = evt.getY();
                    if (this.getChildCount() > 1) { // 有内容，多于1个时
                        // 通知其父控件，现在进行的是本控件的操作，不允许拦截
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (this.getChildCount() > 1) { // 有内容，多于1个时
                        // 通知其父控件，现在进行的是本控件的操作，不允许拦截
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    // 在up时判断是否按下和松手的坐标为一个点
                    if (PointF.length(evt.getX() - downPoint.x, evt.getY()
                            - downPoint.y) < (float) 5.0) {
                        onSingleTouch(this);
                        return true;
                    }
                    break;
            }
            return super.onTouchEvent(evt);
        }
    }

    public void onSingleTouch(View v) {
        if (onSingleTouchListener != null) {
            onSingleTouchListener.onSingleTouch(v);
        }
    }

    public interface OnSingleTouchListener {
        public void onSingleTouch(View v);
    }

    public void setOnSingleTouchListener(
            OnSingleTouchListener onSingleTouchListener) {
        this.onSingleTouchListener = onSingleTouchListener;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        if (noScroll)
            return false;
        else{
            try {
                return super.onInterceptTouchEvent(arg0);
            }catch (IllegalArgumentException ex){
                ex.printStackTrace();
            }
        }
        return false;
    }

//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        int height = 0;
//        for (int i = 0; i < getChildCount(); i++) {
//            View child = getChildAt(i);
//            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
//            int h = child.getMeasuredHeight();
//            if (h > height)height = h;
//        }
//
//        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
//
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//    }
}
