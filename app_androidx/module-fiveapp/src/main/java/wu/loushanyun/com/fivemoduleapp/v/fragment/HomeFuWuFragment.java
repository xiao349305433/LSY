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

public class HomeFuWuFragment extends BaseNoPresenterFragment {
    private RoundLinearLayout linearBenDi;
    private TextView roundTextFujin;
    private TextView roundTextShebeixinxi;
    private TextView roundTextXianchangjiancha;
    private TextView roundTextXianchanggenghuan;
    private TextView roundTextGenghuantongbu;
    private TextView roundTextGenghuanThree;
    private TextView roundTextTongbuThree;
    @Override
    protected void initEventRunner() {

    }

    @Override
    public void onInitAttribute(BaseAttribute ba) {
        ba.mActivityLayoutId= R.layout.m_five_fragment_fuwu;
    }

    @Override
    public void initView(View view, Bundle bundle) {
        linearBenDi = (RoundLinearLayout) view.findViewById(R.id.linear_benDi);
        roundTextFujin = (TextView) view.findViewById(R.id.round_text_fujin);
        roundTextShebeixinxi = (TextView) view.findViewById(R.id.round_text_shebeixinxi);
        roundTextXianchangjiancha = (TextView) view.findViewById(R.id.round_text_xianchangjiancha);
        roundTextXianchanggenghuan = (TextView) view.findViewById(R.id.round_text_xianchanggenghuan);
        roundTextGenghuantongbu = (TextView) view.findViewById(R.id.round_text_genghuantongbu);
        roundTextGenghuanThree = (TextView) view.findViewById(R.id.round_text_genghuan_three);
        roundTextTongbuThree = (TextView) view.findViewById(R.id.round_text_tongbu_three);


    }

    @Override
    protected void initData(Bundle bundle) {
        roundTextFujin.setOnClickListener(view -> {
            ARouter.getInstance().build(K.EquipmentListActivity).navigation();
        });
        roundTextShebeixinxi.setOnClickListener(view -> {
            ARouter.getInstance().build(K.MainRepairActivity).navigation();
        });
        roundTextXianchangjiancha.setOnClickListener(view -> {
            ARouter.getInstance().build(K.NeighborActivity).navigation();
        });
        roundTextXianchanggenghuan.setOnClickListener(view -> {
            ARouter.getInstance().build(K.ChooseNewEquipmentActivity).navigation();
        });
        roundTextGenghuanThree.setOnClickListener(view -> {
            ARouter.getInstance().build(K.ReplaceThreeActivity).navigation();
        });
        roundTextTongbuThree.setOnClickListener(view -> {
            ARouter.getInstance().build(K.UploadReplaceDataActivity).navigation();
        });
        roundTextGenghuantongbu.setOnClickListener(view -> {
            ARouter.getInstance().build(K.UploadChangeDataActivity).navigation();
        });
    }

    @Override
    protected void onEventRunEnd(Event event, int code) {

    }
}
