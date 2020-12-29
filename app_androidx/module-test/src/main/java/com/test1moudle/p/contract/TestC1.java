package com.test1moudle.p.contract;

import met.hx.com.base.base.BasePresenter;
import met.hx.com.base.baseinterface.BaseView;

/**
 * Created by huxu on 2018/1/19.
 */

public interface TestC1 {
    interface View extends BaseView {
    }

    abstract class Presenter extends BasePresenter<TestC1.View> {
    }
}
