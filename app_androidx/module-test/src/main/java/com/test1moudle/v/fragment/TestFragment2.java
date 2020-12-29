package com.test1moudle.v.fragment;

import android.os.Bundle;
import android.view.View;

import com.test1moudle.R;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.fragment.BaseNoPresenterFragment;
import met.hx.com.base.baseevent.Event;

public class TestFragment2 extends BaseNoPresenterFragment{
    @Override
    protected void initEventRunner() {

    }

    @Override
    public void onInitAttribute(BaseAttribute ba) {
        ba.mActivityLayoutId= R.layout.m_test_fragment_test;
        ba.mTitleText="第2个";
    }

    @Override
    public void initView(View view, Bundle bundle) {

    }

    @Override
    protected void initData(Bundle bundle) {

    }

    @Override
    protected void onEventRunEnd(Event event, int code) {

    }
}
