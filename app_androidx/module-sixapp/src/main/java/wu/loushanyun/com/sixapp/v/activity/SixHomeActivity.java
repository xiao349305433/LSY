package wu.loushanyun.com.sixapp.v.activity;

import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.google.android.material.navigation.NavigationView;
import com.scwang.smartrefresh.layout.util.DensityUtil;
import com.sensoro.sensor.kit.entity.SensoroDevice;
import com.wu.loushanyun.base.config.K;

import java.util.ArrayList;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.activity.BaseNoPresenterActivity;
import met.hx.com.base.base.multitype.MultiTypeAdapter;
import met.hx.com.base.baseevent.Event;
import met.hx.com.librarybase.some_utils.ScreenUtils;
import met.hx.com.librarybase.some_utils.ToastUtils;
import wu.loushanyun.com.sixapp.R;
import wu.loushanyun.com.sixapp.init.LoginParamManager;
import wu.loushanyun.com.sixapp.init.SixCode;
import wu.loushanyun.com.sixapp.init.SixUtils;
import wu.loushanyun.com.sixapp.m.GetBatchInfo;
import wu.loushanyun.com.sixapp.m.GetModelInfo;
import wu.loushanyun.com.sixapp.m.LandInfo;
import wu.loushanyun.com.sixapp.m.LoginInfo;
import wu.loushanyun.com.sixapp.m.ProInfo;
import wu.loushanyun.com.sixapp.p.adapter.BatchViewBinder;
import wu.loushanyun.com.sixapp.p.runner.CheckPhoneRunner;
import wu.loushanyun.com.sixapp.p.runner.GetBatchRunner;
import wu.loushanyun.com.sixapp.p.runner.GetModelRunner;
import wu.loushanyun.com.sixapp.p.runner.ProInfRunner;


@Route(path = K.SixHomeActivity)
public class SixHomeActivity extends BaseNoPresenterActivity {
    private DrawerLayout drawerLayout;
    private LinearLayout LinearMenu;
    private LinearLayout batchLayout;
    private TextView batchTv;
    private TextView homePiciTv;
    private ImageView sixHomeImgFix;
    private ImageView sixHomeImgProduction;
    private NavigationView navView;
    private TextView headTvExit;
    private TextView homeLoginInfoTv;
    //    private TextView homeProduction;
//    private TextView homeFix;
    private GetBatchInfo getBatchInfo;
    private ProInfo proInfo;
    private PopupWindow myPop;
    private LinearLayout batchLinear;
    private LinearLayout homeToproduction;
    private GetModelInfo getModelInfo;


    @Override
    protected void initEventListener() {
        registerEventRunner(SixCode.MSixGetBatch, new GetBatchRunner());
        registerEventRunner(SixCode.MSixProInf, new ProInfRunner());
        registerEventRunner(SixCode.MSixGetModel, new GetModelRunner());
    }

    @Override
    protected void onEventRunEnd(Event event, int code) {
        if (code == SixCode.MSixGetBatch) {
            if (event.isSuccess()) {
                getBatchInfo = (GetBatchInfo) event.getReturnParamAtIndex(0);
            }
        } else if (code == SixCode.MSixProInf) {
            if (event.isSuccess()) {
                proInfo = (ProInfo) event.getReturnParamAtIndex(0);
                homePiciTv.setText("产品形式——" + proInfo.getData().getGoodsName() + "，分配数量——" + proInfo.getData().getBatchTotal() + "个");
                homeToproduction.setEnabled(true);
                homeToproduction.setBackgroundColor(getResources().getColor(R.color.blue_bg));
            }
        } else if (code == SixCode.MSixGetModel) {
            if (event.isSuccess()) {
                getModelInfo = (GetModelInfo) event.getReturnParamAtIndex(0);

            }
        }
    }


    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        ba.mActivityLayoutId = R.layout.m_six_activity_home;
    }

    @Override
    protected void initView() {

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        LinearMenu = (LinearLayout) findViewById(R.id.Linear_menu);

        sixHomeImgFix = (ImageView) findViewById(R.id.six_home_img_fix);
        sixHomeImgProduction = (ImageView) findViewById(R.id.six_home_img_production);
        homeLoginInfoTv = (TextView) findViewById(R.id.home_logininfo_tv);

        navView = (NavigationView) findViewById(R.id.nav_view);
        View headerview = navView.getHeaderView(0);
        headTvExit = headerview.findViewById(R.id.head_tv_exit);


        //默认选择
        pushEvent(SixCode.MSixGetModel, LoginParamManager.getInstance().getLoginInfo().getAccountId());

        LandInfo.DatasBean landInfo = LoginParamManager.getInstance().getLoginInfo();
        homeLoginInfoTv.setText("手机号：" + landInfo.getTel() + " 已经被制造商 " + landInfo.getBusinessName() + " 合法授权 工号：" + landInfo.getJobNumber());
        LinearMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        headTvExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build(K.SixLoginActivity).navigation();
                finish();
            }
        });

        sixHomeImgFix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build(K.SixFixActivity).navigation();

            }
        });

        sixHomeImgProduction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getModelInfo != null) {
                    if (getModelInfo.getDatas().size() > 0) {
                        showPop();
                    } else {
                        ToastUtils.showLong("暂无产品");
                    }
                } else {
                    ToastUtils.showLong("暂无产品");
                }
            }
        });


    }


    private void showPop() {
        View view = getLayoutInflater().inflate(R.layout.m_six_view_pop_chanpin, null);
        view.setLayoutParams(new LinearLayout.LayoutParams(DensityUtil.dp2px(400), DensityUtil.dp2px(400)));

        RecyclerView recyclerView = view.findViewById(R.id.goods_rv);
        MultiTypeAdapter multiTypeAdapter = new MultiTypeAdapter();
        BatchViewBinder batchViewBinder = new BatchViewBinder(new BatchViewBinder.OnBatchListener() {
            @Override
            public void setBatch(GetModelInfo.DatasBean item) {
                runOnUiThread(() -> {
                    ARouter.getInstance().build(K.SixParameterActivity).withAction("model").withString("type", item.getGoodsName()).withParcelable("GetModelInfo",item).navigation();
                    myPop.dismiss();

                });
            }
        });


        multiTypeAdapter.register(GetModelInfo.DatasBean.class, batchViewBinder);
        multiTypeAdapter.setItems(getModelInfo.getDatas());
        recyclerView.setAdapter(multiTypeAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setNestedScrollingEnabled(false);
        myPop = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        myPop.setBackgroundDrawable(new ColorDrawable());
        myPop.setOutsideTouchable(true);
        myPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
//                WindowManager.LayoutParams lp=getWindow().getAttributes();
//                lp.alpha=1.0f;
//                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
//                getWindow().setAttributes(lp);
            }
        });

        myPop.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);

    }


//    private void showPopBatch(View layout) {
//        View view = getLayoutInflater().inflate(R.layout.m_six_view_pop_batch, null);
//
//        RecyclerView recyclerView = view.findViewById(R.id.batch_rv);
//         MultiTypeAdapter multiTypeAdapter = new MultiTypeAdapter();
//        BatchViewBinder batchViewBinder=new BatchViewBinder(new BatchViewBinder.OnBatchListener() {
//            @Override
//            public void setBatch(GetBatchInfo.DatasBean item) {
//
//                runOnUiThread(() -> {
//                    batchTv.setText(SixUtils.getChanpingPinYinByCode(item.getProductForm())+item.getBatchNumber());
//                    batchTv.setTextColor(getResources().getColor(R.color.blue_bg));
//                    pushEvent(SixCode.MSixProInf, LoginParamManager.getInstance().getLoginInfo().getLoginId(), LoginParamManager.getInstance().getLoginInfo().getId(),item.getBatchNumber());
//                    myPop.dismiss();
//
//                });
//
//            }
//        });
//        multiTypeAdapter.register(GetBatchInfo.DatasBean.class, batchViewBinder);
//        multiTypeAdapter.setItems(getBatchInfo.getDatas());
//        recyclerView.setAdapter(multiTypeAdapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setNestedScrollingEnabled(false);
//        myPop = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        myPop.setBackgroundDrawable(new ColorDrawable());
//        myPop.setOutsideTouchable(true);
//        myPop.setOnDismissListener(new PopupWindow.OnDismissListener(){
//            @Override
//            public void onDismiss() {
////                WindowManager.LayoutParams lp=getWindow().getAttributes();
////                lp.alpha=1.0f;
////                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
////                getWindow().setAttributes(lp);
//            }
//        });
////        WindowManager.LayoutParams lp=getWindow().getAttributes();
////        lp.alpha=0.3f;
////        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
////        getWindow().setAttributes(lp);
//
//
//        myPop.showAsDropDown(layout,0,-DensityUtil.dp2px(28)*getBatchInfo.getDatas().size());
//
//
//
//    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


}
