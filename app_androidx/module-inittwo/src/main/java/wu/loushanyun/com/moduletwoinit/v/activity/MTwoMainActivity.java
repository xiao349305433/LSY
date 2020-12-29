package wu.loushanyun.com.moduletwoinit.v.activity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.flyco.roundview.RoundTextView;
import com.wu.loushanyun.base.config.K;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.activity.BaseNoPresenterActivity;
import met.hx.com.base.baseevent.Event;
import wu.loushanyun.com.moduletwoinit.R;

@Route(path = K.MTwoMainActivity)
public class MTwoMainActivity extends BaseNoPresenterActivity {
    private RoundTextView roundTextChuangjiandingwei;
    private RoundTextView roundTextGengxindingwei;
    private RoundTextView roundTextTongbu;


    @Override
    protected void initEventListener() {

    }

    @Override
    protected void onEventRunEnd(Event event, int code) {

    }

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        ba.mActivityLayoutId = R.layout.m_twoinit_main;
        ba.mTitleText = "物联网端接入";

    }

    @Override
    protected void initView() {
        roundTextChuangjiandingwei = (RoundTextView) findViewById(R.id.round_text_chuangjiandingwei);
        roundTextGengxindingwei = (RoundTextView) findViewById(R.id.round_text_gengxindingwei);
        roundTextTongbu = (RoundTextView) findViewById(R.id.round_text_tongbu);

        roundTextChuangjiandingwei.setOnClickListener(v -> {
            ARouter.getInstance().build(K.CreateLocationActivity).navigation();
        });
        roundTextGengxindingwei.setOnClickListener(v -> {
            ARouter.getInstance().build(K.OnCloudWuLianListActivity).navigation();
        });
        roundTextTongbu.setOnClickListener(v -> {
            ARouter.getInstance().build(K.WuLianListActivity).navigation();
        });

    }
}
