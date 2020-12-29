package wu.loushanyun.com.sixapp.v.activity;

import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.wu.loushanyun.base.config.K;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.activity.BaseNoPresenterActivity;
import met.hx.com.base.baseevent.Event;
import wu.loushanyun.com.sixapp.R;
import wu.loushanyun.com.sixapp.init.SixUtils;
import wu.loushanyun.com.sixapp.m.EnvironmentInfInfo;

@Route(path = K.SixWuxianParamActivity)
public class SixWuxianParamActivity extends BaseNoPresenterActivity {

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

    @Override
    protected void initEventListener() {

    }

    @Override
    protected void onEventRunEnd(Event event, int code) {

    }

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        ba.mActivityLayoutId = R.layout.m_six_activity_wuxianparam;
        ba.mTitleText = "无线环境";
        ba.mTitleBackgroundColorId=R.color.blue_bg;

    }

    @Override
    protected void initView() {

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

        StringBuilder sb=new StringBuilder();
        sb.append("环境名称 ："+ SixUtils.DatasBean.getEnvName()+"\n");
        sb.append("信号强度 ："+SixUtils.DatasBean.getSignalIntensity()+"\n");
        sb.append("信噪比 ："+SixUtils.DatasBean.getSignalRatio()+"\n");
        sb.append("扩频因子 ："+SixUtils.DatasBean.getSpreadFactor()+"\n");
        sb.append("反馈 ："+SixUtils.DatasBean.getFeedback()+"\n");
        sb.append("反馈次数 ："+SixUtils.DatasBean.getFeedbackNum()+"\n");
        sb.append("延时RxDelay ："+SixUtils.DatasBean.getDelayParameter()+"S\n");
        sb.append("信道参数 ："+SixUtils.DatasBean.getRemarks()+"\n");
        sb.append("发送频率 ："+SixUtils.DatasBean.getSendFrequency()+"\n");
        sb.append("频段 ："+SixUtils.DatasBean.getFrequencyRange()+"\n");
        sb.append("标定人 ："+SixUtils.DatasBean.getDemarcatePerson()+"\n");
        sb.append("标定日期 ："+SixUtils.DatasBean.getDemarcateTime()+"\n");
        sb.append("基站编号 ："+SixUtils.DatasBean.getBaseNum());


        mDisanfangTv3.setText(sb.toString());
        String []channels= SixUtils.DatasBean.getChannel().split(",");
        String []bottomNoises=SixUtils.DatasBean.getBottomNoise().split(",");
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



    }
}
