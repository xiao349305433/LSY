package com.modulerefresh.v.activity;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.modulerefresh.p.adapter.TestRefreshViewBinder;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.test1moudle.R;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.activity.BaseRefreshActivity;
import met.hx.com.base.base.multitype.Items;
import met.hx.com.base.base.multitype.MultiTypeAdapter;
import met.hx.com.base.baseconfig.C;
import met.hx.com.base.baseevent.Event;
import met.hx.com.librarybase.some_utils.KeyboardUtils;
import met.hx.com.librarybase.some_utils.LogUtils;
import met.hx.com.librarybase.some_utils.SoftKeyBoardListener;

import static android.view.Gravity.CENTER;

/**
 * Created by huxu on 2017/11/15.
 */
@Route(path = C.REFRESHTEST0)
public class TestRefreshActivity extends BaseRefreshActivity {
    private MultiTypeAdapter adapter;
    private int i = 0;
    private int j = 0;
    private TestRefreshViewBinder testRefreshViewBinder;
    private Items items;
    private EditText editText;

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
    protected void initTransitionView() {

    }

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        ba.mActivityLayoutId = R.layout.m_test_refresh_test;
        ba.mTitleText = "刷新框架使用";
        ba.mTitleRightText = "暂无";
    }

    @Override
    public void onRightClick(View item) {
        super.onRightClick(item);
        showEmptyData(null,CENTER,20);
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        super.onLoadmore(refreshlayout);
        j=j+1;
        LogUtils.i("第"+j);
        items.add(i++);
        items.add(i++);
        items.add(i++);
        items.add(i++);
        items.add(i++);
        items.add(i++);
        items.add(i++);
        adapter.notifyDataSetChanged();
        mSmartRefresh.finishLoadmore(3000);
        /**
         * 没有更多数据
         */
        mSmartRefresh.setLoadmoreFinished(true);//没有更多数据之后
        mSmartRefresh.finishLoadmore();
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        super.onRefresh(refreshlayout);
        /**
         * 恢复没有更多数据的原始状态
         */
        mSmartRefresh.setLoadmoreFinished(false);
        /**
         * 隐藏暂无布局
         */
        refreshView.hideEmptyData();

        items.clear();
        i = 0;
        items.add(i++);
        items.add(i++);
        items.add(i++);
        items.add(i++);
        adapter.notifyDataSetChanged();
    }


    @Override
    protected void initView() {
        super.initView();
        editText= (EditText) findViewById(R.id.edit0);
        editText.setFocusable(false);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyboardUtils.showSoftInput(editText);
            }
        });
        SoftKeyBoardListener.setListener(TestRefreshActivity.this, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                Toast.makeText(TestRefreshActivity.this, "键盘显示 高度" + height, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void keyBoardHide(int height) {
                Toast.makeText(TestRefreshActivity.this, "键盘隐藏 高度" + height, Toast.LENGTH_SHORT).show();
            }
        });
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycle);
        adapter = new MultiTypeAdapter();
        items = new Items();
        adapter.register(Integer.class, testRefreshViewBinder);
        recyclerView.setAdapter(adapter);
        adapter.setItems(items);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected int initRefreshLayoutId() {
        return 0;
    }

    @Override
    protected int getRefreshViewId() {
        return R.id.refresh;
    }


    @Override
    protected void initEventListener() {

    }

    @Override
    protected void onEventRunEnd(Event event, int code) {

    }
}
