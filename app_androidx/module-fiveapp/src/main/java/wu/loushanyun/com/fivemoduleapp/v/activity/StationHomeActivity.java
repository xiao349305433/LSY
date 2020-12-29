package wu.loushanyun.com.fivemoduleapp.v.activity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.flyco.roundview.RoundRelativeLayout;
import com.wu.loushanyun.base.url.URLUtils;

import met.hx.com.base.baseconfig.C;
import wu.loushanyun.com.fivemoduleapp.R;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.activity.BaseNoPresenterActivity;
import met.hx.com.base.baseevent.Event;
import wu.loushanyun.com.fivemoduleapp.p.runner.GetBaseStationInfoRunner;
import wu.loushanyun.com.libraryfive.init.LoginFiveParamManager;
import wu.loushanyun.com.module_initthree.init.InitCode;

public class StationHomeActivity extends BaseNoPresenterActivity implements View.OnClickListener {
    //基站总数
    private TextView tvNum;
    //模式AB的各个信息
    private TextView numA;
    private TextView lixianA;
    private TextView yitaiwangA;
    private TextView sangiA;
    private TextView numB;
    private TextView lixianB;
    private TextView yitaiwangB;
    private TextView sangiB;
    //基站定位
    private RoundRelativeLayout jizhandingwei;
    // 基站预览
    private RoundRelativeLayout jizhanyulan;
    //设备预览
    private RoundRelativeLayout shebeiyulan;
    //今日报告
    private RoundRelativeLayout jinribaogao;

    @Override
    protected void initEventListener() {
        registerEventRunner(InitCode.GetBaseStationInfoRunner, new GetBaseStationInfoRunner());
    }

    @Override
    protected void onEventRunEnd(Event event, int code) {
        if (code == InitCode.GetBaseStationInfoRunner){
            if (event.isSuccess()){
                int i = 0;
                numA.setText(String.valueOf(event.getReturnParamAtIndex(i++)));
                lixianA.setText(String.valueOf(event.getReturnParamAtIndex(i++)));
                sangiA.setText(String.valueOf(event.getReturnParamAtIndex(i++)));
                yitaiwangA.setText(String.valueOf(event.getReturnParamAtIndex(i++)));

                numB.setText(String.valueOf(event.getReturnParamAtIndex(i++)));
                lixianB.setText(String.valueOf(event.getReturnParamAtIndex(i++)));
                sangiB.setText(String.valueOf(event.getReturnParamAtIndex(i++)));
                yitaiwangB.setText(String.valueOf(event.getReturnParamAtIndex(i++)));

                int numAT=Integer.valueOf(numA.getText().toString());
                int numBT=Integer.valueOf(numB.getText().toString());
                tvNum.setText(numAT+numBT+"");
            }
        }
    }

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        ba.mActivityLayoutId = R.layout.m_five_activity_station_home;
        ba.mTitleText = "基站";
    }

    @Override
    protected void initView() {
        tvNum = (TextView) findViewById(R.id.tv_num);
        numA = (TextView) findViewById(R.id.numA);
        lixianA = (TextView) findViewById(R.id.lixianA);
        yitaiwangA = (TextView) findViewById(R.id.yitaiwangA);
        sangiA = (TextView) findViewById(R.id.sangiA);
        numB = (TextView) findViewById(R.id.numB);
        lixianB = (TextView) findViewById(R.id.lixianB);
        yitaiwangB = (TextView) findViewById(R.id.yitaiwangB);
        sangiB = (TextView) findViewById(R.id.sangiB);
        jizhandingwei = (RoundRelativeLayout) findViewById(R.id.jizhandingwei);
        jizhandingwei.setOnClickListener(this);
        jizhanyulan = (RoundRelativeLayout) findViewById(R.id.jizhanyulan);
        jizhanyulan.setOnClickListener(this);
        shebeiyulan = (RoundRelativeLayout) findViewById(R.id.shebeiyulan);
        shebeiyulan.setOnClickListener(this);
        jinribaogao = (RoundRelativeLayout) findViewById(R.id.jinribaogao);
        jinribaogao.setOnClickListener(this);
        pushEvent(InitCode.GetBaseStationInfoRunner, String.valueOf(LoginFiveParamManager.getInstance().getLoginData().getTradeRegister().getId()));
    }
 
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.jizhandingwei:
                startActivity(new Intent(this, StationListActivity.class));
                break;
            case R.id.jizhanyulan:
                String url = URLUtils.getIP()+"/cj/jzcj/pages/jzylapp.jsp?"
                        + "reId=" + LoginFiveParamManager.getInstance().getLoginData().getTradeRegister().getId()
                        + "&elseLoginId=" + LoginFiveParamManager.getInstance().getLoginData().getId();
                ARouter.getInstance().build(C.WEB).withString("url", url)
                        .withBoolean("hasTitle", true)
                        .withBoolean("hasProgress", true)
                        .withBoolean("canBack", true)
                        .navigation();
                break;
            case R.id.shebeiyulan:
                String url2 = URLUtils.getIP()+"/cj/jzcj/pages/sbyl.html?"
                        + "reId=" + LoginFiveParamManager.getInstance().getLoginData().getTradeRegister().getId()
                        + "&elseLoginId=" + LoginFiveParamManager.getInstance().getLoginData().getId()
                        + "&adminFou=" + 1;
                ARouter.getInstance().build(C.WEB).withString("url", url2)
                        .withBoolean("hasTitle", true)
                        .withBoolean("hasProgress", true)
                        .withBoolean("canBack", true)
                        .navigation();
                break;
            case R.id.jinribaogao:
                String url1 = URLUtils.getIP()+"/cj/jzcj/pages/jrbgapp.jsp?"
                        + "tradeRegisterId=" + LoginFiveParamManager.getInstance().getLoginData().getTradeRegister().getId()
                        + "&elseLoginId=" + LoginFiveParamManager.getInstance().getLoginData().getId();
                ARouter.getInstance().build(C.WEB).withString("url", url1)
                        .withBoolean("hasTitle", true)
                        .withBoolean("hasProgress", true)
                        .withBoolean("canBack", true)
                        .navigation();
                break;
        }
    }
}
