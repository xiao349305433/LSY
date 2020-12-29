package com.modulerefresh.v.activity;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.modulerefresh.p.adapter.TestRefreshViewBinder;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.test1moudle.R;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.BasePresenter;
import met.hx.com.base.base.activity.BaseYesPresenterActivity;
import met.hx.com.base.base.multitype.Items;
import met.hx.com.base.base.multitype.MultiTypeAdapter;
import met.hx.com.base.baseconfig.C;
import met.hx.com.base.basemvp.v.views.RefreshView;
import met.hx.com.librarybase.some_utils.LogUtils;

/**
 * Created by huxu on 2017/11/22.
 */
@Route(path = C.REFRESHTEST1)
public class RefreshTestActivity extends BaseYesPresenterActivity {
    private RefreshView refreshView;
    private MultiTypeAdapter adapter;
    private int i = 0;
    private int j = 0;
    private TestRefreshViewBinder testRefreshViewBinder;

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
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        ba.mActivityLayoutId = R.layout.m_test_fragment_refresh;
        ba.mTitleText = "刷新group";
        ba.mTitleRightText = "跳转";
    }

    @Override
    public void onRightClick(View item) {
        super.onRightClick(item);
        ARouter.getInstance().build(C.REFRESH).navigation();
//        refreshView.showEmptyData(null);
    }

    @Override
    protected void initView() {
        refreshView=(RefreshView)findViewById(R.id.refresh);
        //可以动态添加自定义布局，也可以包裹一个布局
//        refreshView.addRefreshContentView(R.layout.m_test_refresh_recycleview);
        RecyclerView recyclerView= (RecyclerView) findViewById(R.id.recycle);
        SmartRefreshLayout smartRefreshLayout=refreshView.getmSmartRefresh();
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
        adapter.register(Integer.class,testRefreshViewBinder);
        recyclerView.setAdapter(adapter);
        adapter.setItemsAndNotify(items);
        smartRefreshLayout.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                LogUtils.i("社活生生的",i+",");
                items.add(i++);
                items.add(i++);
                items.add(i++);
                items.add(i++);
                items.add(i++);
                adapter.notifyDataSetChanged();
                smartRefreshLayout.finishLoadmore();
                if(j==3){
                    smartRefreshLayout.setLoadmoreFinished(true);
                }
                j++;
            }

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                items.clear();
                i=0;
                j=0;
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
                adapter.notifyDataSetChanged();
                smartRefreshLayout.finishRefresh();
            }
        });
    }
}
