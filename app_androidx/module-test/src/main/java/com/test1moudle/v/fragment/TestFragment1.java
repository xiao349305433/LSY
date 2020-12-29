package com.test1moudle.v.fragment;

import android.os.Bundle;
import android.view.View;


import com.test1moudle.R;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.fragment.BaseNoPresenterFragment;
import met.hx.com.base.baseevent.Event;
import met.hx.com.base.basemvp.v.views.TopTabView;
import met.hx.com.librarybase.some_utils.SizeUtils;
import met.hx.com.librarybase.views.tablayout.XTabLayout;

public class TestFragment1 extends BaseNoPresenterFragment{
    private TopTabView topTabView;
    private XTabLayout.Tab tab1;

    @Override
    protected void initEventRunner() {

    }

    @Override
    public void onInitAttribute(BaseAttribute ba) {
        ba.mActivityLayoutId= R.layout.m_test_fragment_test;
        ba.mTitleText="第1个";
    }

    @Override
    public void initView(View view, Bundle bundle) {
        topTabView = (TopTabView) view.findViewById(R.id.topTab);
        XTabLayout tabLayout=topTabView.getTableLayout();
//        tabLayout.setSelectedTabIndicatorHeight(SizeUtils.dp2px(2));
        tabLayout.setSelectedIndicatorWidth(SizeUtils.dp2px(18));
//        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.base_QW));
        topTabView.addItemCustomView(R.layout.m_test_tablayout_item, new TestNoPresenterFragment(), (view1, tab) -> {
//            setView(view1, "最近聊天", getTextDrawable(R.drawable.m_test_friend), true);
        });
        topTabView.addItemCustomView(R.layout.m_test_tablayout_item, new TestNoPresenterFragment(), (view1, tab) -> {
//            setView(view1, "好友", getTextDrawable(R.drawable.m_test_group), true);
        });
        tab1=topTabView.addItemCustomView(R.layout.m_test_tablayout_item, new TestNoPresenterFragment(), (view1, tab) -> {
//            setView(view1, "人脉圈", getTextDrawable(R.drawable.m_test_ring), true);
        });
    }

    @Override
    protected void initData(Bundle bundle) {

    }

    @Override
    protected void onEventRunEnd(Event event, int code) {

    }
}
