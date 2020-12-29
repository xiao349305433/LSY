package com.test1moudle.p.presenter;


import met.hx.com.base.base.BasePresenter;
import met.hx.com.base.baseinterface.BaseView;

/**
 * Created by baixiaokang on 16/4/22.
 */
public interface MainContract {

    interface View extends BaseView {
        void test(Integer code);
    }

    abstract class Presenter extends BasePresenter<View> {
        public abstract void test(String path);
        @Override
        public void onAttached() {

        }
    }
}

