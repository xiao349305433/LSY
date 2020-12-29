package wu.loushanyun.com.sixapp.v.activity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.wu.loushanyun.base.config.K;
import com.wu.loushanyun.basemvp.v.activity.BaseSnBlueToothActivity;

import met.hx.com.base.baseevent.Event;
import wu.loushanyun.com.sixapp.R;


@Route(path = K.SixJiZhongQiActivity)
public class SixJiZhongQiActivity extends BaseSnBlueToothActivity {
    @Override
    protected int onChildLayout() {
        return R.layout.m_six_activity_jizhongqi;
    }

    @Override
    public void onChildConnectFailed(int i) {

    }

    @Override
    public void onChildConnectSuccess() {

    }

    @Override
    public void onChildNotify(byte[] bytes) {

    }

    @Override
    public void onChildWriteSuccess() {

    }

    @Override
    public void onChildWriteFailure(int i) {

    }

    @Override
    protected void initEventListener() {

    }

    @Override
    protected void onEventRunEnd(Event event, int code) {

    }
}
