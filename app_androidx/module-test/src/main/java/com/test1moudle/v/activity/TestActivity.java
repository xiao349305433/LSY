package com.test1moudle.v.activity;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.test1moudle.R;
import com.test1moudle.p.presenter.TestPresenter;
import com.test1moudle.v.fragment.PTestFragment;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.activity.BaseYesPresenterActivity;
import met.hx.com.base.baseconfig.C;

/**
 * Created by huxu on 2017/5/4.
 */
@Route(path = C.TESTPLAYER)
public class TestActivity extends BaseYesPresenterActivity<TestPresenter> {
    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        ba.mActivityLayoutId = R.layout.m_test_testp;
        ba.mHasTitle = false;
        ba.mTitleText = "测试";
    }

    @Override
    protected void initView() {
        Bundle bundle1=new Bundle();
        bundle1.putString("text","第一");
        addLocalFragment(new PTestFragment(),R.id.frame_1,bundle1);
        Bundle bundle2=new Bundle();
        bundle2.putString("text","第2");
        addLocalFragment(new PTestFragment(),R.id.frame_2,bundle2);
        Bundle bundle3=new Bundle();
        bundle3.putString("text","第3");
        addLocalFragment(new PTestFragment(),R.id.frame_3,bundle3);
        Bundle bundle4=new Bundle();
        bundle4.putString("text","第4");
        addLocalFragment(new PTestFragment(),R.id.frame_4,bundle4);
    }

    @Override
    protected TestPresenter initPresenter() {
        return null;
    }
}
