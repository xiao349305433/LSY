package wu.loushanyun.com.fivemoduleapp.v.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.flyco.roundview.RoundTextView;
import com.wu.loushanyun.base.config.K;
import com.wu.loushanyun.base.url.URLUtils;
import com.wu.loushanyun.base.url.UrlController;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.fragment.BaseNoPresenterFragment;
import met.hx.com.base.base.glide.GlideUtil;
import met.hx.com.base.baseevent.Event;
import wu.loushanyun.com.fivemoduleapp.BuildConfig;
import wu.loushanyun.com.fivemoduleapp.R;
import wu.loushanyun.com.libraryfive.init.LoginFiveParamManager;

public class SecondWelcomeFragment extends BaseNoPresenterFragment {
    private ImageView imageView;
    private RoundTextView textDemo;
    private RoundTextView textShop;

    @Override
    protected void initEventRunner() {

    }

    @Override
    public void onInitAttribute(BaseAttribute ba) {
        ba.mActivityLayoutId = R.layout.m_five_welcome_second_fragment;
    }

    @Override
    public void initView(View view, Bundle bundle) {
        imageView = (ImageView) view.findViewById(R.id.image_view);
        textDemo = (RoundTextView) view.findViewById(R.id.text_demo);
        textShop = (RoundTextView) view.findViewById(R.id.text_shop);
        GlideUtil.display(getContext(), imageView, R.drawable.yindaoye);
        if(BuildConfig.DEBUG){
            textDemo.setVisibility(View.VISIBLE);
            textShop.setVisibility(View.GONE);
        }else {
            textDemo.setVisibility(View.GONE);
            textShop.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void initData(Bundle bundle) {
        textDemo.setOnClickListener(view -> {
            UrlController.getInstance().setIP(URLUtils.HttpHead, URLUtils.HostTEST);
            login();
        });
        textShop.setOnClickListener(view -> {
            UrlController.getInstance().setIP(URLUtils.HttpHead, URLUtils.HostOFFICIAL);
            login();
        });
    }

    private void login() {
        if (LoginFiveParamManager.getInstance().compareTime()) {
            if (LoginFiveParamManager.getInstance().getLoginData() != null) {
                ARouter.getInstance().build(K.HomeActivity1).navigation();
                sendMessageToast("免密登录成功，请注意账号与业务是否匹配！！不匹配请切换登录账号");
            } else {
                ARouter.getInstance().build(K.LoginActivityFivePhone).navigation();
            }
        } else {
            ARouter.getInstance().build(K.LoginActivityFivePhone).navigation();
        }
    }

    @Override
    protected void onEventRunEnd(Event event, int code) {

    }
}
