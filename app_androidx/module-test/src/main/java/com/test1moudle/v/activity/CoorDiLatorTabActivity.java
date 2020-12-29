package com.test1moudle.v.activity;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.test1moudle.R;
import com.test1moudle.v.fragment.TestNoPresenterFragment;

import java.util.ArrayList;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.activity.BaseNoPresenterActivity;
import met.hx.com.base.baseconfig.C;
import met.hx.com.base.baseevent.Event;
import met.hx.com.base.basemvp.v.views.CoordinatorTabLayout;
import met.hx.com.librarybase.some_utils.util.AbFragmentPagerAdapter;

@Route(path = C.CoorDiLatorTabActivity)
public class CoorDiLatorTabActivity extends BaseNoPresenterActivity {
    private CoordinatorTabLayout coordinatortablayout;
    private ViewPager vp;
    private int[] mImageArray;
    private int[] mColorArray;
    private ArrayList<Fragment> mFragments;
    private final String[] mTitles = {"Android", "iOS", "Web", "Other"};

    @Override
    protected void initEventListener() {

    }

    @Override
    protected void onEventRunEnd(Event event, int code) {

    }

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        ba.mActivityLayoutId = R.layout.m_test_activity_coorcilator;
        ba.toolBarId = R.id.toolbar;
    }

    @Override
    protected void initView() {
        coordinatortablayout = (CoordinatorTabLayout) findViewById(R.id.coordinatortablayout);
        vp = (ViewPager) findViewById(R.id.vp);

        initFragments();
        initViewPager();
        mImageArray = new int[]{
                R.drawable.bg_android,
                R.drawable.bg_ios,
                R.drawable.bg_js,
                R.drawable.bg_other};
        mColorArray = new int[]{
                android.R.color.holo_blue_light,
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_green_light};

        coordinatortablayout
                .setTitle("Demo")
                .setBackEnable(true)
                .setImageArray(mImageArray, mColorArray)
                .setupWithViewPager(vp);

    }

    private void initFragments() {
        mFragments = new ArrayList<>();
        for (String title : mTitles) {
            mFragments.add(new TestNoPresenterFragment());
        }
    }

    private void initViewPager() {
        AbFragmentPagerAdapter mFragmentPagerAdapter = new AbFragmentPagerAdapter(
                getSupportFragmentManager(), mFragments, mTitles);
        vp.setAdapter(mFragmentPagerAdapter);
        vp.setOffscreenPageLimit(4);
    }
}
