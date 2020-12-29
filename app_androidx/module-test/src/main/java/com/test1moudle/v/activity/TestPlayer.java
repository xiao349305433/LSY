package com.test1moudle.v.activity;

import android.view.View;

import com.test1moudle.R;
import com.test1moudle.p.presenter.TestPresenter;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.activity.BaseYesPresenterActivity;

/**
 * Created by huxu on 2017/5/4.
 */

public class TestPlayer extends BaseYesPresenterActivity<TestPresenter> implements View.OnClickListener{
    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        ba.mActivityLayoutId = R.layout.m_test_player;
        ba.mHasTitle = true;
        ba.mTitleText = "播放器";
    }

    @Override
    protected void initView() {
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected TestPresenter initPresenter() {
        return null;
    }
}
