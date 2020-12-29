package com.test1moudle.v.fragment;

import android.os.Bundle;
import android.view.View;

import com.test1moudle.R;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.fragment.BaseNoPresenterFragment;
import met.hx.com.base.baseevent.Event;

/**
 * Created by huxu on 2017/3/31.
 */

public class TestNoPresenterFragment extends BaseNoPresenterFragment {


    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected void initEventRunner() {

    }

    @Override
    protected void onEventRunEnd(Event event, int code) {

    }

    @Override
    public void onInitAttribute(BaseAttribute ba) {
        ba.mActivityLayoutId= R.layout.m_test_h;

    }

    @Override
    public void initView(View view, Bundle bundle) {

    }
}
