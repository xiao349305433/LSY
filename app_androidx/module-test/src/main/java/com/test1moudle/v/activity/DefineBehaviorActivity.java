package com.test1moudle.v.activity;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.modulerefresh.p.adapter.TestRefreshViewBinder;
import com.test1moudle.R;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.activity.BaseNoPresenterActivity;
import met.hx.com.base.base.multitype.Items;
import met.hx.com.base.base.multitype.MultiTypeAdapter;
import met.hx.com.base.baseconfig.C;
import met.hx.com.base.baseevent.Event;

@Route(path = C.DefineBehaviorActivity)
public class DefineBehaviorActivity extends BaseNoPresenterActivity {
    private AppBarLayout appBar;
    private CollapsingToolbarLayout toolbarLayout;
    private RecyclerView myList;
    private TextView title;


    private MultiTypeAdapter adapter;
    private TestRefreshViewBinder testRefreshViewBinder;
    private int i = 0;
    @Override
    protected void initEventListener() {

    }
    @Override
    protected void initLifeCycle() {
        super.initLifeCycle();
        /**
         * 网络请求的在这注册生命周期，不然无法请求
         */
        testRefreshViewBinder = new TestRefreshViewBinder();
        registerLifeCycle(testRefreshViewBinder);
    }
    @Override
    protected void onEventRunEnd(Event event, int code) {

    }

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        ba.mActivityLayoutId= R.layout.m_test_activity_define_behavior;

    }

    @Override
    protected void initView() {
        appBar = (AppBarLayout) findViewById(R.id.app_bar);
        toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        myList = (RecyclerView) findViewById(R.id.my_list);
        title = (TextView) findViewById(R.id.title);
        myList.setLayoutManager(new LinearLayoutManager(this));
        adapter=new MultiTypeAdapter();
        Items items=new Items();
        items.add(i++);
        items.add(i++);
        items.add(i++);
        items.add(i++);
        items.add(i++);
        items.add(i++);
        items.add(i++);
        items.add(i++);
        items.add(i++);
        items.add(i++);
        items.add(i++);
        items.add(i++);
        items.add(i++);
        items.add(i++);
        items.add(i++);
        items.add(i++);
        adapter.register(Integer.class,testRefreshViewBinder);
        myList.setAdapter(adapter);
        adapter.setItemsAndNotify(items);
    }
}
