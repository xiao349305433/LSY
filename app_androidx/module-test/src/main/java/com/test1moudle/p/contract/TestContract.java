package com.test1moudle.p.contract;


import android.graphics.Bitmap;

import met.hx.com.base.base.BasePresenter;
import met.hx.com.base.baseinterface.BaseView;

/**
 * Created by baixiaokang on 16/4/22.
 */
public interface TestContract {

    interface View extends BaseView {
       void setText(String text);
       void setBitmap(Bitmap bitmap,Object[] objects);
    }

    abstract class Presenter extends BasePresenter<View> {
        public abstract void test();
        public abstract Bitmap getFirstBitMap();
        public abstract void testPermission(String text);
        @Override
        public void onAttached() {

        }
    }
}

