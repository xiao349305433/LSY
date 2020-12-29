package com.loushanyun.modulefactory.v.activity;

import android.content.Intent;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.loushanyun.modulefactory.R;
import com.wu.loushanyun.base.config.K;
import com.wu.loushanyun.basemvp.v.activity.BaseBlueToothActivity;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.baseevent.Event;

@Route(path = K.NewDigitalActivity)
public class NewDigitalActivity extends BaseBlueToothActivity implements View.OnClickListener {

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        ba.mActivityLayoutId = R.layout.activity_new_digital;
        ba.mTitleText = "配置";
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.mod_ttl:
//                intent = new Intent(this, ModTTLActivity.class);
//                intent = new Intent(this,LSYModule01Activity.class);
//                intent.putExtra("sensoroDevice", sensoroDevice);
//                intent.putExtra("type",0);
//                startActivity(intent);
                break;
            case R.id.mod_bus:
//                intent = new Intent(this,LSYModule01Activity.class);
//                intent.putExtra("sensoroDevice", sensoroDevice);
//                intent.putExtra("type",1);
//                startActivity(intent);
                break;
            case R.id.mod_485:
//                intent = new Intent(this,LSYModule01Activity.class);
//                intent.putExtra("sensoroDevice", sensoroDevice);
//                intent.putExtra("type",2);
//                startActivity(intent);
                break;
        }
    }

    @Override
    public void onChildNotify(byte[] output) {
    }

    @Override
    protected void onChildConnectFailed(int i) {
    }

    @Override
    protected void onChildConnectSuccess() {
    }

    @Override
    protected void onChildWriteSuccess() {
    }

    @Override
    protected void onChildWriteFailure(int i) {
    }

    @Override
    protected void initEventListener() {
    }

    @Override
    protected void onEventRunEnd(Event event, int code) {
    }
}
