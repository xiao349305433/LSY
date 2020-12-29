package com.test1moudle.v.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.test1moudle.R;
import com.test1moudle.p.contract.TestContract;
import com.test1moudle.p.presenter.TestPresenter;

import java.util.ArrayList;
import java.util.List;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.BasePresenter;
import met.hx.com.base.base.fragment.BaseYesPresenterFragment;
import met.hx.com.base.base.glide.GlideUtil;
import met.hx.com.base.base.player.Clarity;
import met.hx.com.base.base.player.NiceVideoPlayer;
import met.hx.com.base.base.player.NiceVideoPlayerManager;
import met.hx.com.base.base.player.TxVideoPlayerController;
import met.hx.com.base.baseconfig.C;

/**
 * Created by huxu on 2017/11/28.
 */
@Route(path = C.TestRoundFragment)
public class TestPlayerFragment extends BaseYesPresenterFragment<TestPresenter> implements TestContract.View {


    private NiceVideoPlayer mNiceVideoPlayer;

    @Override
    public void onInitAttribute(BaseAttribute ba) {
        ba.mActivityLayoutId = R.layout.m_test_g_player;
    }

    @Override
    public void initView(View view, Bundle bundle) {
        mNiceVideoPlayer = (NiceVideoPlayer) view.findViewById(R.id.nice_video_player);
        TxVideoPlayerController controller = new TxVideoPlayerController(getContext());
        controller.setTitle("Beautiful China...");
        controller.setClarity(getClarites(), 0);
        GlideUtil.display(
                getContext(), controller.imageView(),
                "http://imgsrc.baidu.com/image/c0%3Dshijue%2C0%2C0%2C245%2C40/sign=304dee3ab299a9012f38537575fc600e/91529822720e0cf3f8b77cd50046f21fbe09aa5f.jpg", R.drawable.m_test_img_default);
        mNiceVideoPlayer.setController(controller);
    }

    public List<Clarity> getClarites() {
        List<Clarity> clarities = new ArrayList<>();
        clarities.add(new Clarity("标清", "270P", "http://play.g3proxy.lecloud.com/vod/v2/MjUxLzE2LzgvbGV0di11dHMvMTQvdmVyXzAwXzIyLTExMDc2NDEzODctYXZjLTE5OTgxOS1hYWMtNDgwMDAtNTI2MTEwLTE3MDg3NjEzLWY1OGY2YzM1NjkwZTA2ZGFmYjg2MTVlYzc5MjEyZjU4LTE0OTg1NTc2ODY4MjMubXA0?b=259&mmsid=65565355&tm=1499247143&key=f0eadb4f30c404d49ff8ebad673d3742&platid=3&splatid=345&playid=0&tss=no&vtype=21&cvid=2026135183914&payff=0&pip=08cc52f8b09acd3eff8bf31688ddeced&format=0&sign=mb&dname=mobile&expect=1&tag=mobile&xformat=super"));
        clarities.add(new Clarity("高清", "480P", "http://play.g3proxy.lecloud.com/vod/v2/MjQ5LzM3LzIwL2xldHYtdXRzLzE0L3Zlcl8wMF8yMi0xMTA3NjQxMzkwLWF2Yy00MTk4MTAtYWFjLTQ4MDAwLTUyNjExMC0zMTU1NTY1Mi00ZmJjYzFkNzA1NWMyNDc4MDc5OTYxODg1N2RjNzEwMi0xNDk4NTU3OTYxNzQ4Lm1wNA==?b=479&mmsid=65565355&tm=1499247143&key=98c7e781f1145aba07cb0d6ec06f6c12&platid=3&splatid=345&playid=0&tss=no&vtype=13&cvid=2026135183914&payff=0&pip=08cc52f8b09acd3eff8bf31688ddeced&format=0&sign=mb&dname=mobile&expect=1&tag=mobile&xformat=super"));
        clarities.add(new Clarity("超清", "720P", "http://play.g3proxy.lecloud.com/vod/v2/MjQ5LzM3LzIwL2xldHYtdXRzLzE0L3Zlcl8wMF8yMi0xMTA3NjQxMzkwLWF2Yy00MTk4MTAtYWFjLTQ4MDAwLTUyNjExMC0zMTU1NTY1Mi00ZmJjYzFkNzA1NWMyNDc4MDc5OTYxODg1N2RjNzEwMi0xNDk4NTU3OTYxNzQ4Lm1wNA==?b=479&mmsid=65565355&tm=1499247143&key=98c7e781f1145aba07cb0d6ec06f6c12&platid=3&splatid=345&playid=0&tss=no&vtype=13&cvid=2026135183914&payff=0&pip=08cc52f8b09acd3eff8bf31688ddeced&format=0&sign=mb&dname=mobile&expect=1&tag=mobile&xformat=super"));
        clarities.add(new Clarity("蓝光", "1080P", "http://play.g3proxy.lecloud.com/vod/v2/MjQ5LzM3LzIwL2xldHYtdXRzLzE0L3Zlcl8wMF8yMi0xMTA3NjQxMzkwLWF2Yy00MTk4MTAtYWFjLTQ4MDAwLTUyNjExMC0zMTU1NTY1Mi00ZmJjYzFkNzA1NWMyNDc4MDc5OTYxODg1N2RjNzEwMi0xNDk4NTU3OTYxNzQ4Lm1wNA==?b=479&mmsid=65565355&tm=1499247143&key=98c7e781f1145aba07cb0d6ec06f6c12&platid=3&splatid=345&playid=0&tss=no&vtype=13&cvid=2026135183914&payff=0&pip=08cc52f8b09acd3eff8bf31688ddeced&format=0&sign=mb&dname=mobile&expect=1&tag=mobile&xformat=super"));
        return clarities;
    }


    @Override
    public void onPause() {
        NiceVideoPlayerManager.instance().suspendNiceVideoPlayer();
        super.onPause();
    }

    @Override
    public void onResume() {
        NiceVideoPlayerManager.instance().resumeNiceVideoPlayer();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        NiceVideoPlayerManager.instance().releaseNiceVideoPlayer();
        super.onDestroy();
    }

    @Override
    protected void initData(Bundle bundle) {

    }

    @Override
    public void setText(String text) {

    }

    @Override
    public void setBitmap(Bitmap bitmap, Object[] objects) {

    }

    @Override
    protected BasePresenter initPresenter() {
        return new TestPresenter();
    }
}
