package wu.loushanyun.com.fivemoduleapp.v.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.flyco.roundview.RoundLinearLayout;
import com.wu.loushanyun.base.config.K;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.fragment.BaseNoPresenterFragment;
import met.hx.com.base.baseevent.Event;
import wu.loushanyun.com.fivemoduleapp.R;

public class HomeBenDiFragment extends BaseNoPresenterFragment {
    private RoundLinearLayout linearBenDi;
    private TextView textChushihua;
    private TextView roundTextChushihua;
    @Override
    protected void initEventRunner() {

    }

    @Override
    public void onInitAttribute(BaseAttribute ba) {
        ba.mActivityLayoutId= R.layout.m_five_fragment_bendi;

    }

    @Override
    public void initView(View view, Bundle bundle) {
        linearBenDi = (RoundLinearLayout) view.findViewById(R.id.linear_benDi);
        textChushihua = (TextView) view.findViewById(R.id.text_chushihua);
        roundTextChushihua = (TextView) view.findViewById(R.id.round_text_chushihua);
    }

    @Override
    protected void initData(Bundle bundle) {
        roundTextChushihua.setOnClickListener(view -> {
            ARouter.getInstance().build(K.ThirdTestNewActivity).navigation();
        });
    }

    @Override
    protected void onEventRunEnd(Event event, int code) {

    }
}
