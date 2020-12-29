package com.test1moudle.v.activity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.test1moudle.R;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.activity.BaseNoPresenterActivity;
import met.hx.com.base.baseconfig.C;
import met.hx.com.base.baseevent.Event;

@Route(path = C.ScrollViewActivity)
public class ScrollViewActivity extends BaseNoPresenterActivity {
    @Override
    protected void initEventListener() {

    }

    @Override
    protected void onEventRunEnd(Event event, int code) {

    }

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        ba.mActivityLayoutId= R.layout.activity_scrolling;
        ba.mTitleText="啥叫等哈説";
    }

    @Override
    protected void initView() {

    }
}
