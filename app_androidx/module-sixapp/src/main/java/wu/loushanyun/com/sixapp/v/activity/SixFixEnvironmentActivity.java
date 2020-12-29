package wu.loushanyun.com.sixapp.v.activity;

import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.wu.loushanyun.base.config.K;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.activity.BaseNoPresenterActivity;
import met.hx.com.base.baseevent.Event;
import met.hx.com.librarybase.some_utils.ToastUtils;
import met.hx.com.librarybase.some_utils.XHStringUtil;
import wu.loushanyun.com.sixapp.R;
import wu.loushanyun.com.sixapp.init.LoginParamManager;
import wu.loushanyun.com.sixapp.init.SixCode;
import wu.loushanyun.com.sixapp.init.SixUtils;
import wu.loushanyun.com.sixapp.m.EnvironmentInfInfo;
import wu.loushanyun.com.sixapp.m.EnvironmentInfInfo2;
import wu.loushanyun.com.sixapp.p.runner.EnvironmentInfRunner;
import wu.loushanyun.com.sixapp.p.runner.EnvironmentInfRunner2;

@Route(path = K.SixFixEnvironmentActivity)
public class SixFixEnvironmentActivity extends BaseNoPresenterActivity {
    private TextView mNoTV;
    private ScrollView mFixScrollview;
    private RadioButton mRadio1;
    private RadioButton mRadio2;
    private RadioButton mRadio3;
    private TextView mDisanfangTv3;
    private TextView mTvChannel0;
    private TextView mTvBottomNoise0;
    private TextView mTvChannel1;
    private TextView mTvBottomNoise1;
    private TextView mTvChannel2;
    private TextView mTvBottomNoise2;
    private TextView mTvChannel3;
    private TextView mTvBottomNoise3;
    private TextView mTvChannel4;
    private TextView mTvBottomNoise4;
    private TextView mTvChannel5;
    private TextView mTvBottomNoise5;
    private TextView mTvChannel6;
    private TextView mTvBottomNoise6;
    private TextView mTvChannel7;
    private TextView mTvBottomNoise7;
    private EnvironmentInfInfo2 environmentInfInfo;
    private RadioGroup environRadioGroup;


    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        ba.mTitleText = "无线环境";
        ba.mTitleBackgroundColorId = R.color.blue_bg;
        ba.mActivityLayoutId = R.layout.m_six_activity_fixenvironment;

    }

    @Override
    protected void initEventListener() {
        registerEventRunner(SixCode.MSixEnvironmentInf2, new EnvironmentInfRunner2());
    }

    @Override
    protected void onEventRunEnd(Event event, int code) {
        if (code == SixCode.MSixEnvironmentInf2) {
            if (event.isSuccess()) {
                environmentInfInfo = (EnvironmentInfInfo2) event.getReturnParamAtIndex(0);
                if (environmentInfInfo.getCode() == 0) {
                    mNoTV.setVisibility(View.GONE);
                    setview();
                } else {

                    ToastUtils.showLong(environmentInfInfo.getMessage());
                }

            }
        }
    }


    @Override
    protected void initView() {
        EnvironmentInfInfo environmentInfInfo = getIntent().getParcelableExtra("EnvironmentInfInfo");
        mNoTV = (TextView) findViewById(R.id.no_tv);
        environRadioGroup = (RadioGroup) findViewById(R.id.environ_radiogroup);
        mFixScrollview = (ScrollView) findViewById(R.id.fix_scrollview);
        mRadio1 = (RadioButton) findViewById(R.id.radio1);
        mRadio2 = (RadioButton) findViewById(R.id.radio2);
        mRadio3 = (RadioButton) findViewById(R.id.radio3);
        mDisanfangTv3 = (TextView) findViewById(R.id.disanfang_tv3);
        mTvChannel0 = (TextView) findViewById(R.id.tv_channel_0);
        mTvBottomNoise0 = (TextView) findViewById(R.id.tv_bottomNoise_0);
        mTvChannel1 = (TextView) findViewById(R.id.tv_channel_1);
        mTvBottomNoise1 = (TextView) findViewById(R.id.tv_bottomNoise_1);
        mTvChannel2 = (TextView) findViewById(R.id.tv_channel_2);
        mTvBottomNoise2 = (TextView) findViewById(R.id.tv_bottomNoise_2);
        mTvChannel3 = (TextView) findViewById(R.id.tv_channel_3);
        mTvBottomNoise3 = (TextView) findViewById(R.id.tv_bottomNoise_3);
        mTvChannel4 = (TextView) findViewById(R.id.tv_channel_4);
        mTvBottomNoise4 = (TextView) findViewById(R.id.tv_bottomNoise_4);
        mTvChannel5 = (TextView) findViewById(R.id.tv_channel_5);
        mTvBottomNoise5 = (TextView) findViewById(R.id.tv_bottomNoise_5);
        mTvChannel6 = (TextView) findViewById(R.id.tv_channel_6);
        mTvBottomNoise6 = (TextView) findViewById(R.id.tv_bottomNoise_6);
        mTvChannel7 = (TextView) findViewById(R.id.tv_channel_7);
        mTvBottomNoise7 = (TextView) findViewById(R.id.tv_bottomNoise_7);
        pushEvent(SixCode.MSixEnvironmentInf2, LoginParamManager.getInstance().getLoginInfo().getLoginId());

//
//


    }


    private void setview() {

        if (environmentInfInfo.getDatas().size() == 2) {
            mRadio3.setVisibility(View.GONE);
            mRadio1.setText(environmentInfInfo.getDatas().get(0).getEnvName());
            mRadio2.setText(environmentInfInfo.getDatas().get(1).getEnvName());
        } else if (environmentInfInfo.getDatas().size() == 1) {
            mRadio3.setVisibility(View.GONE);
            mRadio2.setVisibility(View.GONE);
            mRadio1.setText(environmentInfInfo.getDatas().get(0).getEnvName());
        } else if (environmentInfInfo.getDatas().size() == 0) {
            mRadio3.setVisibility(View.GONE);
            mRadio2.setVisibility(View.GONE);
            mRadio1.setVisibility(View.GONE);
        } else {
            mRadio1.setText(environmentInfInfo.getDatas().get(0).getEnvName());
            mRadio2.setText(environmentInfInfo.getDatas().get(1).getEnvName());
            mRadio3.setText(environmentInfInfo.getDatas().get(2).getEnvName());
        }

        environRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio1:
                        StringBuilder sb = new StringBuilder();
                        sb.append("环境名称 ：" + environmentInfInfo.getDatas().get(0).getEnvName() + "\n");
                        sb.append("信号强度 ：" + environmentInfInfo.getDatas().get(0).getSignalIntensity() + "\n");
                        sb.append("信噪比 ：" + environmentInfInfo.getDatas().get(0).getSignalRatio() + "\n");
                        sb.append("扩频因子 ：" + environmentInfInfo.getDatas().get(0).getSpreadFactor() + "\n");
                        sb.append("发送功率 ：" + environmentInfInfo.getDatas().get(0).getSendPower() + "\n");
                        sb.append("反馈 ：" + environmentInfInfo.getDatas().get(0).getFeedback() + "\n");
                        sb.append("反馈次数 ：" + environmentInfInfo.getDatas().get(0).getFeedbackNum() + "\n");
                        sb.append("延时RxDelay ：" + environmentInfInfo.getDatas().get(0).getDelayParameter() + "S\n");
                        sb.append("信道参数 ：" + environmentInfInfo.getDatas().get(0).getRemarks() + "\n");
                        sb.append("发送频率 ：" + environmentInfInfo.getDatas().get(0).getSendFrequency() + "\n");
                        sb.append("频段 ：" + environmentInfInfo.getDatas().get(0).getFrequencyRange() + "\n");
                        sb.append("标定人 ：" + environmentInfInfo.getDatas().get(0).getDemarcatePerson() + "\n");
                        sb.append("标定日期 ：" + environmentInfInfo.getDatas().get(0).getDemarcateTime() + "\n");
                        sb.append("基站编号 ：" + environmentInfInfo.getDatas().get(0).getBaseNum()+ "\n");
                        sb.append("底噪合格范围 ： -110 ~ -90");
                        mDisanfangTv3.setText(sb.toString());
                        String[] channels = environmentInfInfo.getDatas().get(0).getChannel().split(",");
                        String[] bottomNoises = environmentInfInfo.getDatas().get(0).getBottomNoise().split(",");
                        mTvChannel0.setText(channels[0]);
                        mTvChannel1.setText(channels[1]);
                        mTvChannel2.setText(channels[2]);
                        mTvChannel3.setText(channels[3]);
                        mTvChannel4.setText(channels[4]);
                        mTvChannel5.setText(channels[5]);
                        mTvChannel6.setText(channels[6]);
                        mTvChannel7.setText(channels[7]);

                        mTvBottomNoise0.setText(bottomNoises[0]);
                        mTvBottomNoise1.setText(bottomNoises[1]);
                        mTvBottomNoise2.setText(bottomNoises[2]);
                        mTvBottomNoise3.setText(bottomNoises[3]);
                        mTvBottomNoise4.setText(bottomNoises[4]);
                        mTvBottomNoise5.setText(bottomNoises[5]);
                        mTvBottomNoise6.setText(bottomNoises[6]);
                        mTvBottomNoise7.setText(bottomNoises[7]);
                        break;
                    case R.id.radio2:
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("环境名称 ：" + environmentInfInfo.getDatas().get(1).getEnvName() + "\n");
                        sb2.append("信号强度 ：" + environmentInfInfo.getDatas().get(1).getSignalIntensity() + "\n");
                        sb2.append("信噪比 ：" + environmentInfInfo.getDatas().get(1).getSignalRatio() + "\n");
                        sb2.append("扩频因子 ：" + environmentInfInfo.getDatas().get(1).getSpreadFactor() + "\n");
                        sb2.append("发送功率 ：" + environmentInfInfo.getDatas().get(1).getSendPower() + "\n");
                        sb2.append("反馈 ：" + environmentInfInfo.getDatas().get(1).getFeedback() + "\n");
                        sb2.append("反馈次数 ：" + environmentInfInfo.getDatas().get(1).getFeedbackNum() + "\n");
                        sb2.append("延时RxDelay ：" + environmentInfInfo.getDatas().get(1).getDelayParameter() + "S\n");
                        sb2.append("信道参数 ：" + environmentInfInfo.getDatas().get(1).getRemarks() + "\n");
                        sb2.append("发送频率 ：" + environmentInfInfo.getDatas().get(1).getSendFrequency() + "\n");
                        sb2.append("频段 ：" + environmentInfInfo.getDatas().get(1).getFrequencyRange() + "\n");
                        sb2.append("标定人 ：" + environmentInfInfo.getDatas().get(1).getDemarcatePerson() + "\n");
                        sb2.append("标定日期 ：" + environmentInfInfo.getDatas().get(1).getDemarcateTime() + "\n");
                        sb2.append("基站编号 ：" + environmentInfInfo.getDatas().get(0).getBaseNum()+ "\n");
                        sb2.append("底噪合格范围 ： -110 ~ -90");
                        mDisanfangTv3.setText(sb2.toString());
                        String[] channels2 = environmentInfInfo.getDatas().get(1).getChannel().split(",");
                        String[] bottomNoises2 = environmentInfInfo.getDatas().get(1).getBottomNoise().split(",");
                        mTvChannel0.setText(channels2[0]);
                        mTvChannel1.setText(channels2[1]);
                        mTvChannel2.setText(channels2[2]);
                        mTvChannel3.setText(channels2[3]);
                        mTvChannel4.setText(channels2[4]);
                        mTvChannel5.setText(channels2[5]);
                        mTvChannel6.setText(channels2[6]);
                        mTvChannel7.setText(channels2[7]);

                        mTvBottomNoise0.setText(bottomNoises2[0]);
                        mTvBottomNoise1.setText(bottomNoises2[1]);
                        mTvBottomNoise2.setText(bottomNoises2[2]);
                        mTvBottomNoise3.setText(bottomNoises2[3]);
                        mTvBottomNoise4.setText(bottomNoises2[4]);
                        mTvBottomNoise5.setText(bottomNoises2[5]);
                        mTvBottomNoise6.setText(bottomNoises2[6]);
                        mTvBottomNoise7.setText(bottomNoises2[7]);

                        break;
                    case R.id.radio3:
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append("环境名称 ：" + environmentInfInfo.getDatas().get(2).getEnvName() + "\n");
                        sb3.append("信号强度 ：" + environmentInfInfo.getDatas().get(2).getSignalIntensity() + "\n");
                        sb3.append("信噪比 ：" + environmentInfInfo.getDatas().get(2).getSignalRatio() + "\n");
                        sb3.append("扩频因子 ：" + environmentInfInfo.getDatas().get(2).getSpreadFactor() + "\n");
                        sb3.append("发送功率 ：" + environmentInfInfo.getDatas().get(2).getSendPower() + "\n");
                        sb3.append("反馈 ：" + environmentInfInfo.getDatas().get(2).getFeedback() + "\n");
                        sb3.append("反馈次数 ：" + environmentInfInfo.getDatas().get(2).getFeedbackNum() + "\n");
                        sb3.append("延时RxDelay ：" + environmentInfInfo.getDatas().get(2).getDelayParameter() + "S\n");
                        sb3.append("信道参数 ：" + environmentInfInfo.getDatas().get(2).getRemarks() + "\n");
                        sb3.append("发送频率 ：" + environmentInfInfo.getDatas().get(2).getSendFrequency() + "\n");
                        sb3.append("频段 ：" + environmentInfInfo.getDatas().get(2).getFrequencyRange() + "\n");
                        sb3.append("标定人 ：" + environmentInfInfo.getDatas().get(2).getDemarcatePerson() + "\n");
                        sb3.append("标定日期 ：" + environmentInfInfo.getDatas().get(2).getDemarcateTime() + "\n");
                        sb3.append("基站编号 ：" + environmentInfInfo.getDatas().get(0).getBaseNum()+ "\n");
                        sb3.append("底噪合格范围 ： -110 ~ -90");
                        mDisanfangTv3.setText(sb3.toString());
                        String[] channels3 = environmentInfInfo.getDatas().get(2).getChannel().split(",");
                        String[] bottomNoises3 = environmentInfInfo.getDatas().get(2).getBottomNoise().split(",");
                        mTvChannel0.setText(channels3[0]);
                        mTvChannel1.setText(channels3[1]);
                        mTvChannel2.setText(channels3[2]);
                        mTvChannel3.setText(channels3[3]);
                        mTvChannel4.setText(channels3[4]);
                        mTvChannel5.setText(channels3[5]);
                        mTvChannel6.setText(channels3[6]);
                        mTvChannel7.setText(channels3[7]);

                        mTvBottomNoise0.setText(bottomNoises3[0]);
                        mTvBottomNoise1.setText(bottomNoises3[1]);
                        mTvBottomNoise2.setText(bottomNoises3[2]);
                        mTvBottomNoise3.setText(bottomNoises3[3]);
                        mTvBottomNoise4.setText(bottomNoises3[4]);
                        mTvBottomNoise5.setText(bottomNoises3[5]);
                        mTvBottomNoise6.setText(bottomNoises3[6]);
                        mTvBottomNoise7.setText(bottomNoises3[7]);
                        break;
                }
            }
        });

        if (!SixUtils.EnvName.equals("")) {


            if (mRadio1.getText().toString().equals(SixUtils.EnvName)) {
                mRadio1.setChecked(true);
            }
            if (mRadio2.getText().toString().equals(SixUtils.EnvName)) {
                mRadio2.setChecked(true);
            }
            if (mRadio3.getText().toString().equals(SixUtils.EnvName)) {
                mRadio3.setChecked(true);
            }
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        RadioButton radioButton = (RadioButton) findViewById(environRadioGroup.getCheckedRadioButtonId());
        if (radioButton != null) {
            SixUtils.EnvName = radioButton.getText().toString();

            for (int i = 0; i < environmentInfInfo.getDatas().size(); i++) {
                if (radioButton.getText().toString().equals(environmentInfInfo.getDatas().get(i).getEnvName())) {
                    SixUtils.DatasBean = environmentInfInfo.getDatas().get(i);

                }

            }
        }


    }
}
