package com.test1moudle.v.activity;

import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.test1moudle.R;
import com.test1moudle.v.fragment.TestNoPresenterFragment;
import com.wu.loushanyun.base.config.K;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.activity.BaseNoPresenterActivity;
import met.hx.com.base.base.glide.GlideUtil;
import met.hx.com.base.baseevent.Event;
import met.hx.com.base.basemvp.v.views.TopTabView;
import met.hx.com.librarybase.some_utils.SizeUtils;
import met.hx.com.librarybase.views.tablayout.XTabLayout;

@Route(path = K.ScrollingActivity)
public class ScrollingActivity extends BaseNoPresenterActivity {
    private TopTabView topTabView;
    private XTabLayout.Tab tab1;

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        ba.mActivityLayoutId=R.layout.m_test_activity_scrolling;
        ba.toolBarId=R.id.toolbar;

    }

    @Override
    protected void initView() {

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        ImageView image_scrolling_top = (ImageView) findViewById(R.id.image_scrolling_top);
        GlideUtil.display(this,image_scrolling_top,R.drawable.material_design_3);

        topTabView = (TopTabView) findViewById(R.id.topTab);
        XTabLayout tabLayout=topTabView.getTableLayout();
//        tabLayout.setSelectedTabIndicatorHeight(SizeUtils.dp2px(2));
        tabLayout.setSelectedIndicatorWidth(SizeUtils.dp2px(18));
//        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.base_QW));
        topTabView.addItemCustomView(R.layout.m_test_tablayout_item, new TestNoPresenterFragment(), (view1, tab) -> {
            setView(view1, "最近聊天", getTextDrawable(R.drawable.m_test_friend), true);
        });
        topTabView.addItemCustomView(R.layout.m_test_tablayout_item, new TestNoPresenterFragment(), (view1, tab) -> {
            setView(view1, "好友", getTextDrawable(R.drawable.m_test_group), true);
        });
        tab1=topTabView.addItemCustomView(R.layout.m_test_tablayout_item, new TestNoPresenterFragment(), (view1, tab) -> {
            setView(view1, "人脉圈", getTextDrawable(R.drawable.m_test_ring), true);
        });
    }

    @Override
    protected void initEventListener() {

    }

    @Override
    protected void onEventRunEnd(Event event, int code) {

    }

    private void setView(View customView, String text, Drawable mTabIcon, boolean showDivisionLine) {
        TextView item = customView.findViewById(R.id.tablayout_item);
        item.setText(text);
        item.setCompoundDrawables(mTabIcon, null, null, null);
        item.setCompoundDrawablePadding(5);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Configuration configuration = getResources().getConfiguration();
        if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

            CollapsingToolbarLayout collapsing_toolbar_layout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
            collapsing_toolbar_layout.setExpandedTitleTextColor(ColorStateList.valueOf(Color.TRANSPARENT));
        } else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

}
