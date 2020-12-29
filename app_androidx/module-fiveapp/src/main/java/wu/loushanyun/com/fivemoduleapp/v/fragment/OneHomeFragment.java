package wu.loushanyun.com.fivemoduleapp.v.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.flyco.roundview.RoundLinearLayout;
import com.flyco.roundview.RoundRelativeLayout;
import com.flyco.roundview.RoundTextView;
import com.lsy.equipment.list.EquipmentListActivity;
import com.wu.loushanyun.base.config.K;
import com.wu.loushanyun.base.url.URLUtils;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.fragment.BaseNoPresenterFragment;
import met.hx.com.base.baseevent.Event;
import wu.loushanyun.com.fivemoduleapp.R;
import wu.loushanyun.com.fivemoduleapp.v.activity.StationHomeActivity;
import wu.loushanyun.com.libraryfive.BlueNameActivity;
import wu.loushanyun.com.libraryfive.init.LoginFiveParamManager;

public class OneHomeFragment extends BaseNoPresenterFragment {
    private TextView textOne;
    private TextView textShezhi;
    private TextView textTwo;
    private TextView textThree;
    private TextView textJiZhan;
    private TextView textBenDi;
    private TextView textJieRu;
    private TextView textKanCha;
    private TextView textGengHuan;
    private RoundRelativeLayout linearBenDi;
    private RoundTextView roundTextChushihua;
    private TextView textChushihua;
    private RoundLinearLayout linearJieRu;
    private RoundTextView roundYuanChengBiaohao;
    private RoundTextView roundWulianwangduan;
    private RoundTextView roundGongyeshuibiao;
    private RoundTextView roundLiuliangji;
    private RoundTextView roundYaliji;
    private RoundTextView roundShuizhifenxi;
    private RoundTextView roundGuanjinggai;
    private RoundRelativeLayout linearKanCha;
    private RoundTextView roundTextFujin;
    private RoundTextView roundTextShebeixinxi;
    private RoundTextView roundTextXianchangjiancha;
    private RoundTextView roundTextXianchanggenghuan;
    private RoundTextView roundTextGenghuantongbu;
    private RoundTextView roundTextGenghuanThree;
    private RoundTextView roundTextTongbuThree;
    private RoundRelativeLayout linearGengHuan;
    private LinearLayout lineaGenghuan;
    private RoundTextView roundGenghuanSn;
    private RoundTextView roundGenghuanId;
    private RoundTextView roundGenghuanGongyeshuibiao;
    private RoundTextView roundGenghuanLiuliang;
    private RoundTextView roundGenghuanYali;
    private RoundTextView roundGenghuanShuizhifenxi;
    private RoundTextView roundGenghuanJinggai;
    private ImageView imageGenghuan;
    private TextView textGuanli;


    @Override
    protected void initEventRunner() {

    }

    @Override
    public void onInitAttribute(BaseAttribute ba) {
        ba.mActivityLayoutId = R.layout.m_five_fragment_home_one;
    }

    @Override
    public void initView(View view, Bundle bundle) {
        textOne = (TextView) view.findViewById(R.id.text_one);
        textShezhi = (TextView) view.findViewById(R.id.text_shezhi);
        textTwo = (TextView) view.findViewById(R.id.text_two);
        textThree = (TextView) view.findViewById(R.id.text_three);
        textJiZhan = (TextView) view.findViewById(R.id.text_jiZhan);
        textBenDi = (TextView) view.findViewById(R.id.text_benDi);
        textJieRu = (TextView) view.findViewById(R.id.text_jieRu);
        textKanCha = (TextView) view.findViewById(R.id.text_kanCha);
        textGengHuan = (TextView) view.findViewById(R.id.text_gengHuan);
        linearBenDi = (RoundRelativeLayout) view.findViewById(R.id.linear_benDi);
        roundTextChushihua = (RoundTextView) view.findViewById(R.id.round_text_chushihua);
        textChushihua = (TextView) view.findViewById(R.id.text_chushihua);
        linearJieRu = (RoundLinearLayout) view.findViewById(R.id.linear_jieRu);
        roundYuanChengBiaohao = (RoundTextView) view.findViewById(R.id.round_yuan_cheng_biaohao);
        roundWulianwangduan = (RoundTextView) view.findViewById(R.id.round_wulianwangduan);
        roundGongyeshuibiao = (RoundTextView) view.findViewById(R.id.round_gongyeshuibiao);
        roundLiuliangji = (RoundTextView) view.findViewById(R.id.round_liuliangji);
        roundYaliji = (RoundTextView) view.findViewById(R.id.round_yaliji);
        roundShuizhifenxi = (RoundTextView) view.findViewById(R.id.round_shuizhifenxi);
        roundGuanjinggai = (RoundTextView) view.findViewById(R.id.round_guanjinggai);
        linearKanCha = (RoundRelativeLayout) view.findViewById(R.id.linear_kanCha);
        roundTextFujin = (RoundTextView) view.findViewById(R.id.round_text_fujin);
        roundTextShebeixinxi = (RoundTextView) view.findViewById(R.id.round_text_shebeixinxi);
        roundTextXianchangjiancha = (RoundTextView) view.findViewById(R.id.round_text_xianchangjiancha);
        roundTextXianchanggenghuan = (RoundTextView) view.findViewById(R.id.round_text_xianchanggenghuan);
        roundTextGenghuantongbu = (RoundTextView) view.findViewById(R.id.round_text_genghuantongbu);
        roundTextGenghuanThree = (RoundTextView) view.findViewById(R.id.round_text_genghuan_three);
        roundTextTongbuThree = (RoundTextView) view.findViewById(R.id.round_text_tongbu_three);
        linearGengHuan = (RoundRelativeLayout) view.findViewById(R.id.linear_gengHuan);
        lineaGenghuan = (LinearLayout) view.findViewById(R.id.linea_genghuan);
        roundGenghuanSn = (RoundTextView) view.findViewById(R.id.round_genghuan_sn);
        roundGenghuanId = (RoundTextView) view.findViewById(R.id.round_genghuan_id);
        roundGenghuanGongyeshuibiao = (RoundTextView) view.findViewById(R.id.round_genghuan_gongyeshuibiao);
        roundGenghuanLiuliang = (RoundTextView) view.findViewById(R.id.round_genghuan_liuliang);
        roundGenghuanYali = (RoundTextView) view.findViewById(R.id.round_genghuan_yali);
        roundGenghuanShuizhifenxi = (RoundTextView) view.findViewById(R.id.round_genghuan_shuizhifenxi);
        roundGenghuanJinggai = (RoundTextView) view.findViewById(R.id.round_genghuan_jinggai);
        imageGenghuan = (ImageView) view.findViewById(R.id.image_genghuan);
        textGuanli = (TextView) view.findViewById(R.id.text_guanli);

    }

    @Override
    protected void initData(Bundle bundle) {
        if (URLUtils.getHost().equals(URLUtils.HostTEST)) {
            textThree.setText("DEMO版本");
        } else if (URLUtils.getHost().equals(URLUtils.HostOFFICIAL)) {
            textThree.setText("商用版本");
        }

        if (LoginFiveParamManager.getInstance().getLoginData() != null) {
            String s = LoginFiveParamManager.getInstance().getLoginData().getTradeRegister().getName();
            textTwo.setText(s);
            int role = LoginFiveParamManager.getInstance().getLoginData().getRole();
            if (role == 1) {
                textGuanli.setText("系统管理员");
            } else if (role == 2) {
                textGuanli.setText("员工");
            } else if (role == 3) {
                textGuanli.setText("抄表员");
            }
        }

        textJiZhan.setOnClickListener(view -> {
            startActivity(new Intent(getActivity(), StationHomeActivity.class));
        });
        roundWulianwangduan.setOnClickListener(view -> {
            ARouter.getInstance().build(K.MTwoMainActivity).navigation();
        });
        textBenDi.setOnClickListener(view -> {
            linearBenDi.setVisibility(View.VISIBLE);
            linearJieRu.setVisibility(View.GONE);
            linearKanCha.setVisibility(View.GONE);
            linearGengHuan.setVisibility(View.GONE);
        });
        textJieRu.setOnClickListener(view -> {
            linearBenDi.setVisibility(View.GONE);
            linearJieRu.setVisibility(View.VISIBLE);
            linearKanCha.setVisibility(View.GONE);
            linearGengHuan.setVisibility(View.GONE);
        });
        textKanCha.setOnClickListener(view -> {
            linearBenDi.setVisibility(View.GONE);
            linearJieRu.setVisibility(View.GONE);
            linearKanCha.setVisibility(View.VISIBLE);
            linearGengHuan.setVisibility(View.GONE);
        });
        textGengHuan.setOnClickListener(view -> {
            linearBenDi.setVisibility(View.GONE);
            linearJieRu.setVisibility(View.GONE);
            linearKanCha.setVisibility(View.GONE);
            linearGengHuan.setVisibility(View.VISIBLE);
        });
        roundTextChushihua.setOnClickListener(view -> {
            ARouter.getInstance().build(K.ThirdTestNewActivity).navigation();
        });
        roundTextFujin.setOnClickListener(view -> {
            startActivity(new Intent(getActivity(), EquipmentListActivity.class));
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
        roundLiuliangji.setOnClickListener(view -> {
//            ARouter.getInstance().build(K.LSY4InitActivity).navigation();
        });

        roundTextGenghuantongbu.setOnClickListener(view -> {
            ARouter.getInstance().build(K.UploadChangeDataActivity).navigation();
        });
        roundYuanChengBiaohao.setOnClickListener(view -> {
            startActivity(new Intent(getActivity(), BlueNameActivity.class));
        });
        textShezhi.setOnClickListener(v -> {
            ARouter.getInstance().build(K.WelcomeActivity).withInt("position", 1).navigation();
            LoginFiveParamManager.getInstance().clear();
            getActivity().finish();
        });
    }

    @Override
    protected void onEventRunEnd(Event event, int code) {

    }
}
