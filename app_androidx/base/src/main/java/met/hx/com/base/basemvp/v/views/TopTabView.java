package met.hx.com.base.basemvp.v.views;/*
 * Copyright (C) 2012 www.amsoft.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.CallSuper;
import androidx.annotation.LayoutRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

import met.hx.com.base.R;
import met.hx.com.librarybase.some_utils.LogUtils;
import met.hx.com.librarybase.some_utils.util.AbFragmentPagerAdapter;
import met.hx.com.librarybase.some_utils.util.AbViewPager;
import met.hx.com.librarybase.views.tablayout.XTabLayout;

/**
 * 顶部导航封装
 * @author huxu
 */
public class TopTabView extends LinearLayout {

    private  View viewDivider;
    /**
     * The context.
     */
    private Context context;


    /**
     * tab的线性布局父.
     */
    private XTabLayout mTableLayout = null;

    /**
     * The m view pager.
     */
    private AbViewPager mViewPager;


    /**
     * 内容的View.
     */
    private ArrayList<Fragment> pagerItemList = null;


    /**
     * 内容区域的适配器.
     */
    private AbFragmentPagerAdapter mFragmentPagerAdapter = null;


    public TopTabView(Context context) {
        this(context, null);
    }

    public TopTabView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.setOrientation(LinearLayout.VERTICAL);
        this.setBackgroundColor(Color.rgb(255, 255, 255));

        mTableLayout = new XTabLayout(context);
        mTableLayout.setHorizontalScrollBarEnabled(false);
        mTableLayout.setSmoothScrollingEnabled(true);
        this.addView(mTableLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        viewDivider=new View(context);
        viewDivider.setBackgroundColor(getResources().getColor(R.color.base_global_neutral_gray_c6c9d4));
        this.addView(viewDivider,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));

        //内容的View的适配
        mViewPager = new AbViewPager(context);
        //手动创建的ViewPager,必须调用setId()方法设置一个id
        mViewPager.setId(View.generateViewId());
        pagerItemList = new ArrayList<Fragment>();

        //要求必须是FragmentActivity的实例
        if (!(this.context instanceof FragmentActivity)) {
            LogUtils.e("构造AbSlidingTabView的参数context,必须是FragmentActivity的实例。");
        }

        FragmentManager mFragmentManager = ((FragmentActivity) this.context).getSupportFragmentManager();
        mFragmentPagerAdapter = new AbFragmentPagerAdapter(
                mFragmentManager, pagerItemList);
        mViewPager.setAdapter(mFragmentPagerAdapter);
        mViewPager.addOnPageChangeListener(new XTabLayout.TabLayoutOnPageChangeListener(mTableLayout));
        mTableLayout.setOnTabSelectedListener(new MyTabSelectedListener());
        mViewPager.setOffscreenPageLimit(3);
        this.addView(mViewPager, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

    }

    /**
     * 增加一组内容tab
     * @param tabList
     * @param fragments
     */
    public void addItemViews(List<XTabLayout.Tab> tabList, List<Fragment> fragments) {
        for(int i=0;i<tabList.size();i++){
            mTableLayout.addTab(tabList.get(i));
        }
        pagerItemList.addAll(fragments);
        mFragmentPagerAdapter.notifyDataSetChanged();
    }

    /**
     * 添加
     * @param tab
     * @param fragment
     */
    public void addItemView(XTabLayout.Tab tab, Fragment fragment) {
        mTableLayout.addTab(tab);
        pagerItemList.add(fragment);
        mFragmentPagerAdapter.notifyDataSetChanged();
    }

    /**
     * 添加
     * @param layoutId
     * @param fragment
     * @param onTabViewFindListener
     * @return
     */
    public XTabLayout.Tab addItemCustomView(@LayoutRes int layoutId, Fragment fragment, OnTabViewFindListener onTabViewFindListener) {
        View view = LayoutInflater.from(context).inflate(layoutId, null);
        XTabLayout.Tab tab = mTableLayout.newTab().setCustomView(view);
        mTableLayout.addTab(tab);
        onTabViewFindListener.OnTabViewFind(view,tab);
        pagerItemList.add(fragment);
        mFragmentPagerAdapter.notifyDataSetChanged();
        return tab;
    }

    private OnTopTabSelectorListener onTopTabSelectorListener;

    public void setOnTopTabSelectorListener(OnTopTabSelectorListener onTopTabSelectorListener) {
        this.onTopTabSelectorListener = onTopTabSelectorListener;
    }

    public interface OnTabViewFindListener {
        void OnTabViewFind(View view,XTabLayout.Tab tab);
    }

    public interface OnTopTabSelectorListener {
        public void onTabSelected(XTabLayout.Tab tab);
        public void onTabUnselected(XTabLayout.Tab tab);
        public void onTabReselected(XTabLayout.Tab tab);
    }

    /**
     * 移除
     * @param tab
     */
    public void removeTab(XTabLayout.Tab tab){
        pagerItemList.remove(tab.getPosition());
        mTableLayout.removeTab(tab);
        mFragmentPagerAdapter.notifyDataSetChanged();
    }

    /**
     * 全部移除
     */
    public void removeAllTab(){
        pagerItemList.clear();
        mTableLayout.removeAllTabs();
        mFragmentPagerAdapter.notifyDataSetChanged();
    }


    public class MyTabSelectedListener implements XTabLayout.OnTabSelectedListener {

        @CallSuper
        @Override
        public void onTabSelected(XTabLayout.Tab tab) {
            mViewPager.setCurrentItem(tab.getPosition());
            if(onTopTabSelectorListener!=null){
                onTopTabSelectorListener.onTabSelected(tab);
            }
        }

        @Override
        public void onTabUnselected(XTabLayout.Tab tab) {
            if(onTopTabSelectorListener!=null){
                onTopTabSelectorListener.onTabUnselected(tab);
            }
        }

        @Override
        public void onTabReselected(XTabLayout.Tab tab) {
            if(onTopTabSelectorListener!=null){
                onTopTabSelectorListener.onTabReselected(tab);
            }
        }
    }
    /**
     * 描述：获取这个View的ViewPager.
     *
     * @return the view pager
     */
    public ViewPager getViewPager() {
        return mViewPager;
    }


    public XTabLayout getTableLayout() {
        return mTableLayout;
    }


    public ArrayList<Fragment> getPagerItemList() {
        return pagerItemList;
    }

    public View getViewDivider() {
        return viewDivider;
    }

    public AbFragmentPagerAdapter getFragmentPagerAdapter() {
        return mFragmentPagerAdapter;
    }

    public void showViewDivider(boolean showViewDivider){
        if(showViewDivider){
            viewDivider.setVisibility(VISIBLE);
        }else {
            viewDivider.setVisibility(GONE);
        }
    }
}
