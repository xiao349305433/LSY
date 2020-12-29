package com.test1moudle.v.activity;

import androidx.fragment.app.Fragment;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.test1moudle.R;
import com.test1moudle.p.presenter.TestPresenter;
import com.test1moudle.v.fragment.TestFragment;

import java.util.ArrayList;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.BasePresenter;
import met.hx.com.base.base.activity.PBaseBottomTabActivity;
import met.hx.com.base.baseconfig.C;
import met.hx.com.librarybase.some_utils.ToastManager;
import met.hx.com.librarybase.some_utils.util.AbViewUtil;

/**
 * Created by huxu on 2017/11/11.
 */
@Route(path = C.TAB)
public class TestTabActivity extends PBaseBottomTabActivity<TestPresenter> {


    @Override
    protected void onChildInitAttribute(BaseAttribute ba) {
        ba.mTitleText = "测试Tab";
    }

    @Override
    protected int addTabMenuId() {
        return R.menu.m_test_menu_navigation_center_icon_only;
    }

    @Override
    protected boolean canScroll() {
        return true;
    }

    @Override
    protected boolean showCenter() {
        return true;
    }

    @Override
    protected void initView() {
        super.initView();
        ImageView imageView= (ImageView) findViewById(R.id.image_view);
        imageView.getLayoutParams().height= AbViewUtil.getViewHeight(mBaseBottomTabNavigation);
    }

    @Override
    protected void onCenterClick(View v) {
        ToastManager.getInstance(this).showToast("我是中间按钮");
        ArrayList<Fragment> list = new ArrayList<>();
        list.add(new TestFragment());
        list.add(new TestFragment());
        list.add(new TestFragment());
        list.add(new TestFragment());
        list.add(new TestFragment());
        setMenuAndList(R.menu.m_test_menu_navigation_with_view_pager,list);
        mBottomTabPresenter.test();
    }


    @Override
    protected ArrayList<Fragment> addFragment() {
        ArrayList<Fragment> list = new ArrayList<>();
        list.add(new TestFragment());
        list.add(new TestFragment());
        list.add(new TestFragment());
        list.add(new TestFragment());
        return list;
    }


    @Override
    protected int getCenterLayout() {
        return R.layout.m_test_magecenter;
    }

    @Override
    protected BasePresenter initPresenter() {
        return new TestPresenter();
    }
}
