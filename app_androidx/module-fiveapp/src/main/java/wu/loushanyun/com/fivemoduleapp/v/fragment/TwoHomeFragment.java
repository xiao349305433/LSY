package wu.loushanyun.com.fivemoduleapp.v.fragment;

import android.os.Bundle;
import android.view.View;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.fragment.BaseNoPresenterFragment;
import met.hx.com.base.baseevent.Event;
import wu.loushanyun.com.fivemoduleapp.R;

public class TwoHomeFragment extends BaseNoPresenterFragment{
    @Override
    protected void initEventRunner() {

    }

    @Override
    public void onInitAttribute(BaseAttribute ba) {
        ba.mActivityLayoutId= R.layout.m_five_fragment_home_two;
    }

    @Override
    public void initView(View view, Bundle bundle) {

    }

    @Override
    protected void initData(Bundle bundle) {

    }

    @Override
    protected void onEventRunEnd(Event event, int code) {

    }
}
