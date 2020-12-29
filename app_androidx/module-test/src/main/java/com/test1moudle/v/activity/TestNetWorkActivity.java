package com.test1moudle.v.activity;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.modulerefresh.p.adapter.TestRefreshViewBinder;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.test1moudle.R;
import com.test1moudle.p.presenter.NetContract;
import com.test1moudle.p.presenter.NetPresenter;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.activity.BaseYesPresenterActivity;
import met.hx.com.base.base.multitype.Items;
import met.hx.com.base.base.multitype.MultiTypeAdapter;
import met.hx.com.base.baseconfig.C;
import met.hx.com.base.basemvp.v.views.RefreshView;

@Route(path = C.Net)
public class TestNetWorkActivity extends BaseYesPresenterActivity<NetPresenter> implements NetContract.View{

    private RefreshView refreshView;
    private MultiTypeAdapter adapter;
    private TestRefreshViewBinder testRefreshViewBinder;
    private Items items;
    private RecyclerView recyclerView;

    @Override
    protected void initLifeCycle() {
        super.initLifeCycle();
        testRefreshViewBinder=new TestRefreshViewBinder();
        registerLifeCycle(testRefreshViewBinder);
    }

    @Override
    protected NetPresenter initPresenter() {
        return new NetPresenter();
    }

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        ba.mActivityLayoutId = R.layout.m_test_fragment_refresh;
        ba.mTitleText = "测试网络请求性能";
        ba.mTitleRightText = "请求";
    }

    @Override
    public void onRightClick(View item) {
        super.onRightClick(item);
        mPresenter.test();
    }

    @Override
    protected void initView() {
        refreshView= (RefreshView) findViewById(R.id.refresh);
        recyclerView= (RecyclerView) findViewById(R.id.recycle);
        SmartRefreshLayout smartRefreshLayout=refreshView.getmSmartRefresh();
        adapter=new MultiTypeAdapter();
        items=new Items();
        adapter.register(Integer.class,testRefreshViewBinder);
        items.add(100000);
        adapter.setItems(items);
        recyclerView.setAdapter(adapter);
        smartRefreshLayout.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadmore();

            }

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh();
            }
        });
    }

    @Override
    public void test(Integer code) {
        items.add(code);
        adapter.notifyDataSetChanged();
        recyclerView.smoothScrollToPosition(adapter.getItemCount()-1);
    }
}
