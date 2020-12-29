package wu.loushanyun.com.fivemoduleapp.v.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.flyco.roundview.RoundLinearLayout;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.fragment.BaseNoPresenterFragment;
import met.hx.com.base.baseevent.Event;
import wu.loushanyun.com.fivemoduleapp.R;
import wu.loushanyun.com.fivemoduleapp.v.activity.StationHomeActivity;

public class HomeJiZhanFragment extends BaseNoPresenterFragment {
    private RoundLinearLayout linearBenDi;
    private TextView roundTextJizhan;

    @Override
    protected void initEventRunner() {

    }

    @Override
    public void onInitAttribute(BaseAttribute ba) {
        ba.mActivityLayoutId = R.layout.m_five_fragment_jizhan;
    }

    @Override
    public void initView(View view, Bundle bundle) {
        linearBenDi = (RoundLinearLayout) view.findViewById(R.id.linear_benDi);
        roundTextJizhan = (TextView) view.findViewById(R.id.round_text_jizhan);
    }

    @Override
    protected void initData(Bundle bundle) {
        roundTextJizhan.setOnClickListener(view -> {
            startActivity(new Intent(getActivity(), StationHomeActivity.class));
        });
    }

    @Override
    protected void onEventRunEnd(Event event, int code) {

    }
}
