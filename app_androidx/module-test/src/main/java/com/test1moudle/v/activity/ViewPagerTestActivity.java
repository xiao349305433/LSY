package com.test1moudle.v.activity;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.activity.BaseBottomTabActivity;
import met.hx.com.base.baseevent.Event;

import com.test1moudle.R;
import com.test1moudle.v.fragment.TestFragment1;
import com.test1moudle.v.fragment.TestFragment2;
import com.test1moudle.v.fragment.TestFragment3;
import com.test1moudle.v.fragment.TestFragment4;
import com.test1moudle.v.fragment.TestFragment5;

public class ViewPagerTestActivity extends BaseBottomTabActivity{
    @Override
    protected void onChildInitAttribute(BaseAttribute ba) {
        ba.mHasTitle=false;
    }

    @Override
    protected ArrayList<Fragment> addFragment() {
        ArrayList<Fragment> arrayList=new ArrayList<>();
        arrayList.add(new TestFragment1());
        arrayList.add(new TestFragment2());
        arrayList.add(new TestFragment3());
        arrayList.add(new TestFragment4());
        arrayList.add(new TestFragment5());
        return arrayList;
    }

    @Override
    protected int addTabMenuId() {
        return R.menu.m_test_menu_navigation;
    }

    @Override
    protected void initEventListener() {

    }

    @Override
    protected void onEventRunEnd(Event event, int code) {

    }
}
