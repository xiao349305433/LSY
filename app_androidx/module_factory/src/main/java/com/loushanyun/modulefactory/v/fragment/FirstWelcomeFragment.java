package com.loushanyun.modulefactory.v.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.loushanyun.modulefactory.R;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.fragment.BaseNoPresenterFragment;
import met.hx.com.base.baseevent.Event;

public class FirstWelcomeFragment extends BaseNoPresenterFragment{

    private ImageView imageView;
    @Override
    protected void initEventRunner() {

    }

    @Override
    public void onInitAttribute(BaseAttribute ba) {
        ba.mActivityLayoutId= R.layout.m_factory_welcome_first_fragment;

    }

    @Override
    public void initView(View view, Bundle bundle) {
        imageView = (ImageView)view.findViewById(R.id.image_view);
//        GlideUtil.display(getContext(),imageView,R.drawable.ydy);
    }

    @Override
    protected void initData(Bundle bundle) {

    }

    @Override
    protected void onEventRunEnd(Event event, int code) {

    }
}
