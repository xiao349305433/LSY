package com.test1moudle.v.activity;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.amap.api.location.AMapLocation;
import com.test1moudle.R;
import com.test1moudle.v.fragment.TestNoPresenterFragment;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.activity.BaseNoPresenterActivity;
import met.hx.com.base.base.map.LocationMap;
import met.hx.com.base.baseconfig.C;
import met.hx.com.base.baseevent.Event;
import met.hx.com.base.baseinterface.OnMyLocationListener;
import met.hx.com.base.basemvp.v.views.TopTabView;
import met.hx.com.librarybase.some_utils.SizeUtils;
import met.hx.com.librarybase.views.tablayout.XTabLayout;

/**
 * @author zsy
 * @date 2017-11-18
 */
@Route(path = C.TestTopTab)
public class TestTopTabActivity extends BaseNoPresenterActivity implements OnMyLocationListener{

    private TopTabView topTabView;
    private XTabLayout.Tab tab1;
    private LocationMap locationMap;

    @Override
    protected void initEventListener() {

    }

    @Override
    protected void onEventRunEnd(Event event, int code) {

    }

    @Override
    protected void initLifeCycle() {
        super.initLifeCycle();
        locationMap=new LocationMap(this);
        registerLifeCycle(locationMap);
    }

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        ba.mActivityLayoutId= R.layout.m_test_activity_toptab;
        ba.mTitleText="演示顶部导航";
        ba.mTitleRightText="移除";
    }

    @Override
    protected void initView() {
        locationMap.startLocation();
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
    public void onRightClick(View item) {
        topTabView.removeAllTab();
    }

    private void setView(View customView, String text, Drawable mTabIcon, boolean showDivisionLine) {
        TextView item = customView.findViewById(R.id.tablayout_item);
        item.setText(text);
        item.setCompoundDrawables(mTabIcon, null, null, null);
        item.setCompoundDrawablePadding(5);
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {

    }
}
