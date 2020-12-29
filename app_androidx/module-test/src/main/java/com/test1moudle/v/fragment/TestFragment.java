package com.test1moudle.v.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.util.DensityUtil;
import com.test1moudle.R;

import met.hx.com.base.base.BasePresenter;
import met.hx.com.base.base.fragment.PBaseRefreshFragment;
import met.hx.com.base.basemvp.v.views.TopTabView;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.librarybase.views.tablayout.XTabLayout;

import static com.google.android.material.tabs.TabLayout.MODE_SCROLLABLE;

/**
 * Created by huxu on 2017/11/11.
 */

public class TestFragment extends PBaseRefreshFragment {

    @Override
    protected void initData(Bundle savedInstanceState) {

    }


    @Override
    public void onInitAttribute(BaseAttribute ba) {
        super.onInitAttribute(ba);
        ba.mHasTitle = false;
    }

    @Override
    public void initView(View view, Bundle bundle) {
        super.initView(view, bundle);
        TopTabView topTabView = view.findViewById(R.id.topTab);
        XTabLayout tabLayout=topTabView.getTableLayout();
        tabLayout.setTabMode(MODE_SCROLLABLE);
        topTabView.addItemCustomView(R.layout.m_test_tablayout_item, new TestNoPresenterFragment(), (view1, tab) -> {
            setView(view1, "最近聊天", getTextDrawable(R.drawable.m_test_friend), true);
        });
        topTabView.addItemCustomView(R.layout.m_test_tablayout_item, new TestNoPresenterFragment(), (view1, tab) -> {
            setView(view1, "好友", getTextDrawable(R.drawable.m_test_group), true);
        });
        topTabView.addItemCustomView(R.layout.m_test_tablayout_item, new TestNoPresenterFragment(), (view1, tab) -> {
            setView(view1, "人脉圈", getTextDrawable(R.drawable.m_test_ring), true);
        });
        topTabView.addItemCustomView(R.layout.m_test_tablayout_item, new TestNoPresenterFragment(), (view1, tab) -> {
            setView(view1, "人脉圈", getTextDrawable(R.drawable.m_test_ring), true);
        });
        topTabView.addItemCustomView(R.layout.m_test_tablayout_item, new TestNoPresenterFragment(), (view1, tab) -> {
            setView(view1, "人脉圈", getTextDrawable(R.drawable.m_test_ring), true);
        });
    }

    private void setView(View customView, String text, Drawable mTabIcon, boolean showDivisionLine) {
        TextView item = customView.findViewById(R.id.tablayout_item);
        item.setText(text);
        item.setCompoundDrawables(mTabIcon, null, null, null);
        item.setCompoundDrawablePadding( DensityUtil.dp2px(5));
    }

    @Override
    protected int initRefreshLayoutId() {
        return R.layout.m_test_fragment;
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }
}
