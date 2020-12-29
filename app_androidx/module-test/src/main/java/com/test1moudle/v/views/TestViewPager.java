package com.test1moudle.v.views;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.test1moudle.R;
import com.test1moudle.p.presenter.TestPresenter;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.activity.BaseYesPresenterActivity;
import met.hx.com.base.baseconfig.C;

/**
 * Created by huxu on 2017/10/12.
 */
@Route(path = C.TESTVIEW)
public class TestViewPager extends BaseYesPresenterActivity<TestPresenter> {

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        ba.mActivityLayoutId = R.layout.m_test_testviewpager;
        ba.mHasTitle = true;
        ba.mTitleText = "哈哈";
    }

    @Override
    protected void initView() {
    }

    @Override
    protected TestPresenter initPresenter() {
        return new TestPresenter();
    }
}
