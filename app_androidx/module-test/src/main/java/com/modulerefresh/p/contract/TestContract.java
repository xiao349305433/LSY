package com.modulerefresh.p.contract;


import met.hx.com.base.base.BasePresenter;
import met.hx.com.base.baseinterface.BaseView;

/**
 * Created by baixiaokang on 16/4/22.
 */
public interface TestContract {

    interface View extends BaseView {
        void onGetDataSuccess(Object object);
        void onGetBottomLoadData(Object object);
        void onFailure();
    }

    abstract class Presenter extends BasePresenter<View> {
        @Override
        public void onAttached() {

        }
    }
}

