package com.loushanyun.modulefactory.v.activity;

import android.Manifest;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.loushanyun.modulefactory.R;
import com.loushanyun.modulefactory.v.fragment.FirstWelcomeFragment;
import com.loushanyun.modulefactory.v.fragment.SecondWelcomeFragment;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.wu.loushanyun.base.url.URLUtils;

import org.json.JSONObject;
import org.lzh.framework.updatepluginlib.UpdateBuilder;
import org.lzh.framework.updatepluginlib.UpdateConfig;
import org.lzh.framework.updatepluginlib.base.UpdateParser;
import org.lzh.framework.updatepluginlib.model.Update;

import java.util.ArrayList;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.activity.BaseNoPresenterActivity;
import met.hx.com.base.baseevent.Event;
import met.hx.com.base.util.DefaultChecker;
import met.hx.com.base.util.Strategy;
import met.hx.com.librarybase.circleindicator.CircleIndicator;
import met.hx.com.librarybase.some_utils.LogUtils;
import met.hx.com.librarybase.some_utils.ToastManager;
import met.hx.com.librarybase.some_utils.util.AbFragmentPagerAdapter;
import met.hx.com.librarybase.some_utils.util.AbViewPager;

public class WelcomeActivity extends BaseNoPresenterActivity {
    private AbViewPager abViewPager;
    private CircleIndicator indicator;
    private AbFragmentPagerAdapter mFragmentPagerAdapter;

    @Override
    protected void initEventListener() {

    }

    @Override
    protected void onEventRunEnd(Event event, int code) {

    }

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        ba.mActivityLayoutId = R.layout.m_factory_welcome_activity;
        ba.mStatusTranslucent=true;
        ba.mStatusBarActivityEnabled = true;
    }

    @Override
    protected void initView() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.BLUETOOTH
                , Manifest.permission.BLUETOOTH_ADMIN
                , Manifest.permission.ACCESS_WIFI_STATE
                , Manifest.permission.ACCESS_NETWORK_STATE
                , Manifest.permission.CHANGE_WIFI_STATE
                , Manifest.permission.INTERNET
                , Manifest.permission.CALL_PHONE
                , Manifest.permission.CAMERA
                , Manifest.permission.ACCESS_FINE_LOCATION
                , Manifest.permission.READ_PHONE_STATE
                , Manifest.permission.READ_EXTERNAL_STORAGE
                , Manifest.permission.WRITE_EXTERNAL_STORAGE
                , Manifest.permission.ACCESS_COARSE_LOCATION
        ).subscribe(aBoolean -> {
            if (!aBoolean) {
                ToastManager.getInstance(this).show("没有权限无法进行蓝牙配置,请到安全管家设置信任此app");
                finish();
            }
        });
        createBuilder().check();
        abViewPager = (AbViewPager) findViewById(R.id.ab_viewPager);
        indicator = (CircleIndicator) findViewById(R.id.indicator);
        ArrayList<Fragment> arrayListFragment = new ArrayList<>();
        arrayListFragment.add(new FirstWelcomeFragment());
        arrayListFragment.add(new SecondWelcomeFragment());
        FragmentManager mFragmentManager = this.getSupportFragmentManager();
        mFragmentPagerAdapter = new AbFragmentPagerAdapter(mFragmentManager,arrayListFragment);
        abViewPager.setAdapter(mFragmentPagerAdapter);
        indicator.setViewPager(abViewPager);
    }

    @NonNull
    private UpdateBuilder createBuilder() {
        UpdateBuilder builder = UpdateBuilder.create(createNewConfig());
        builder.setUpdateChecker(new DefaultChecker());
        builder.setUpdateStrategy(new Strategy());
        return builder;
    }

    private UpdateConfig createNewConfig() {
        return UpdateConfig.createConfig()
                .setUrl(URLUtils.FSUpdate)
                .setUpdateParser(new UpdateParser() {
                    @Override
                    public Update parse(String httpResponse) throws Exception {
                        JSONObject object = new JSONObject(httpResponse);
                        LogUtils.i("更新返回的数据==" + httpResponse);
                        Update update = new Update();
                        // 此apk包的下载地址
                        update.setUpdateUrl(object.optString("update_url"));
                        // 此apk包的版本号
                        update.setVersionCode(object.optInt("update_ver_code"));
                        // 此apk包的版本名称
                        update.setVersionName(object.optString("update_ver_name"));
                        // 此apk包的更新内容
                        update.setUpdateContent(object.optString("update_content"));
                        // 此apk包是否为强制更新
                        update.setForced(object.optBoolean("update_forced", false));
                        // 是否显示忽略此次版本更新按钮
                        update.setIgnore(object.optBoolean("ignore_able", false));
                        return update;
                    }
                });
    }
}
