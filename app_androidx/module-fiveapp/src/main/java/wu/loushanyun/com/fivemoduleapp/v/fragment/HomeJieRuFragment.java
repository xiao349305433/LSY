package wu.loushanyun.com.fivemoduleapp.v.fragment;

import android.content.Intent;
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
import wu.loushanyun.com.libraryfive.BlueNameActivity;

public class HomeJieRuFragment extends BaseNoPresenterFragment {
    private RoundLinearLayout linearBenDi;
    private TextView roundYuanChengBiaohao;
    private TextView roundWulianwangduan;
    private TextView roundLiuliangji;
    private TextView roundLiuliangjiUpload;

    @Override
    protected void initEventRunner() {

    }

    @Override
    public void onInitAttribute(BaseAttribute ba) {
        ba.mActivityLayoutId = R.layout.m_five_fragment_jieru;
    }

    @Override
    public void initView(View view, Bundle bundle) {
        linearBenDi = (RoundLinearLayout) view.findViewById(R.id.linear_benDi);
        roundYuanChengBiaohao = (TextView) view.findViewById(R.id.round_yuan_cheng_biaohao);
        roundWulianwangduan = (TextView) view.findViewById(R.id.round_wulianwangduan);
        roundLiuliangji = (TextView) view.findViewById(R.id.round_liuliangji);
        roundLiuliangjiUpload = (TextView) view.findViewById(R.id.round_liuliangji_upload);
    }

    @Override
    protected void initData(Bundle bundle) {
        roundLiuliangji.setOnClickListener(view -> {
            ARouter.getInstance().build(K.LSY4InitActivity).navigation();
        });
        roundYuanChengBiaohao.setOnClickListener(view -> {
            startActivity(new Intent(getActivity(), BlueNameActivity.class));
        });
        roundWulianwangduan.setOnClickListener(view -> {
            ARouter.getInstance().build(K.MTwoMainActivity).navigation();
        });
        roundLiuliangjiUpload.setOnClickListener(view -> {
            ARouter.getInstance().build(K.LSY4ListActivity).navigation();
        });
    }

    @Override
    protected void onEventRunEnd(Event event, int code) {

    }
}
