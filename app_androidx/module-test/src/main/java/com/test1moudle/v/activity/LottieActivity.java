package com.test1moudle.v.activity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.test1moudle.R;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.activity.BaseNoPresenterActivity;
import met.hx.com.base.baseconfig.C;
import met.hx.com.base.baseevent.Event;

@Route(path = C.LottieActivity)
public class LottieActivity extends BaseNoPresenterActivity {
    @Override
    protected void initEventListener() {

    }

    @Override
    protected void onEventRunEnd(Event event, int code) {

    }

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        ba.mActivityLayoutId = R.layout.m_test_first;
        ba.mTitleText = "动画";

    }

    @Override
    protected void initView() {

    }
}
