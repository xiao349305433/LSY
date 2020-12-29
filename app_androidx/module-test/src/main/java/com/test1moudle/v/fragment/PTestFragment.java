package com.test1moudle.v.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.test1moudle.R;
import com.test1moudle.p.contract.TestContract;
import com.test1moudle.p.presenter.TestPresenter;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.BasePresenter;
import met.hx.com.base.base.fragment.BaseYesPresenterFragment;
import met.hx.com.base.baseconfig.C;

/**
 * Created by Administrator on 2017/11/21.
 */
@Route(path= C.PTestFragment)
public class PTestFragment extends BaseYesPresenterFragment<TestPresenter> implements TestContract.View{
    private TextView textV;

    @Override
    public void onInitAttribute(BaseAttribute ba) {
        ba.mActivityLayoutId= R.layout.m_test_pfragment;
    }

    @Override
    public void initView(View view, Bundle bundle) {
        textV = (TextView) view.findViewById(R.id.text_v);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mPresenter.testPermission(getArguments().getString("text"));
    }

    @Override
    public void setText(String text) {
        textV.setText(text);
    }

    @Override
    public void setBitmap(Bitmap bitmap, Object[] objects) {

    }

    @Override
    protected BasePresenter initPresenter() {
        return new TestPresenter();
    }
}
