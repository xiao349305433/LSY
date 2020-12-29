package com.loushanyun.modulefactory.v.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.loushanyun.modulefactory.R;
import com.wu.loushanyun.base.config.K;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.fragment.BaseNoPresenterFragment;
import met.hx.com.base.base.glide.GlideUtil;
import met.hx.com.base.baseevent.Event;

public class SecondWelcomeFragment extends BaseNoPresenterFragment {
    private ImageView imageView;
    private TextView tvInNew;

    @Override
    protected void initEventRunner() {

    }

    @Override
    public void onInitAttribute(BaseAttribute ba) {
        ba.mActivityLayoutId = R.layout.m_factory_welcome_second_fragment;
    }

    @Override
    public void initView(View view, Bundle bundle) {
        imageView = (ImageView) view.findViewById(R.id.image_view);
        tvInNew = (TextView) view.findViewById(R.id.tvInNew);

        GlideUtil.display(getContext(), imageView, R.drawable.yindaoye2);
    }

    @Override
    protected void initData(Bundle bundle) {
        tvInNew.setOnClickListener(view -> {
            ARouter.getInstance().build(K.LoginActivityPhone).navigation();
        });
    }


    @Override
    protected void onEventRunEnd(Event event, int code) {

    }
}
