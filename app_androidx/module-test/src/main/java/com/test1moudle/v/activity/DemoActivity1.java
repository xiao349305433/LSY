package com.test1moudle.v.activity;

import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.elvishew.xlog.XLog;
import com.test1moudle.R;
import com.test1moudle.m.HomeData;
import com.test1moudle.p.adapter.HomeDataViewBinder;
import com.wu.loushanyun.base.config.K;
import com.wu.loushanyun.basemvp.m.HomeDataListener;

import java.util.ArrayList;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.activity.BaseNoPresenterActivity;
import met.hx.com.base.base.multitype.MultiTypeAdapter;
import met.hx.com.base.baseconfig.C;
import met.hx.com.base.baseevent.Event;

/**
 * Created by huxu on 2017/12/1.
 */
@Route(path = C.DemoActivity1)
public class DemoActivity1 extends BaseNoPresenterActivity {
    private RecyclerView recyclerview;
    private HomeDataViewBinder homeDataViewBinder;
    public static final int RESULT_DATA = 1002;
    @Override
    protected void initLifeCycle() {
        super.initLifeCycle();
    }


    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        ba.mActivityLayoutId = R.layout.m_test_activity_demo1;
        ba.mHasTitle = true;
        ba.mTitleText = "demo列表1";
    }

    @Override
    protected void initView() {
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        homeDataViewBinder = new HomeDataViewBinder();
        ArrayList<HomeData> arrayList = new ArrayList<>();
        arrayList.add(new HomeData("Kotlin", C.DemoKotlinActivity));
        arrayList.add(new HomeData("打印机图片示例", C.PrintBitmapActivity));
        arrayList.add(new HomeData("flutter", C.FlutterViewActivity));
        arrayList.add(new HomeData("广告", C.BANNER));
        arrayList.add(new HomeData("滚动的界面", C.ScrollViewActivity));
        arrayList.add(new HomeData("Lottie动画", C.LottieActivity));
        arrayList.add(new HomeData("二维码", C.ZxingActivity));
        arrayList.add(new HomeData("下载", C.DownLoadActivity));
        arrayList.add(new HomeData("顶部导航", C.TestTopTab));
        arrayList.add(new HomeData("顶部导航C", C.CoorDiLatorTabActivity));
        arrayList.add(new HomeData("自定义Behavior  ", C.DefineBehaviorActivity));
        arrayList.add(new HomeData("蓝牙模块", C.BlueToothActivity));
        arrayList.add(new HomeData("定位", C.LocationActivity));
        arrayList.add(new HomeData("省市县  ", K.ProvincesActivity, new HomeDataListener() {
            @Override
            public void onHomeData() {
                ARouter.getInstance().build(K.ProvincesActivity).navigation(DemoActivity1.this,RESULT_DATA);
            }
        }));
        MultiTypeAdapter multiTypeAdapter = new MultiTypeAdapter(arrayList);
        multiTypeAdapter.register(HomeData.class, homeDataViewBinder);
        recyclerview.setAdapter(multiTypeAdapter);
    }


    @Override
    protected void initEventListener() {

    }

    @Override
    protected void onEventRunEnd(Event event, int code) {

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_DATA && data != null) {
            String area = data.getStringExtra("area");
            String city = data.getStringExtra("city");
            String province = data.getStringExtra("province");
            String areaCode = data.getStringExtra("areaCode");
            XLog.i(province+city+area+areaCode);
        }
    }
}
