package wu.loushanyun.com.fivemoduleapp.v.activity;

import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.elvishew.xlog.XLog;
import com.flyco.roundview.RoundLinearLayout;
import com.wu.loushanyun.base.config.K;
import com.wu.loushanyun.base.url.URLUtils;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.activity.BaseNoPresenterActivity;
import met.hx.com.base.base.glide.GlideUtil;
import met.hx.com.base.baseevent.Event;
import met.hx.com.base.basemvp.v.views.TopTabView;
import met.hx.com.librarybase.some_utils.SizeUtils;
import met.hx.com.librarybase.views.tablayout.XTabLayout;
import wu.loushanyun.com.fivemoduleapp.R;
import wu.loushanyun.com.fivemoduleapp.v.fragment.HomeBenDiFragment;
import wu.loushanyun.com.fivemoduleapp.v.fragment.HomeFuWuFragment;
import wu.loushanyun.com.fivemoduleapp.v.fragment.HomeJiZhanFragment;
import wu.loushanyun.com.fivemoduleapp.v.fragment.HomeJieRuFragment;
import wu.loushanyun.com.libraryfive.init.LoginFiveParamManager;

@Route(path = K.HomeActivity1)
public class HomeActivity1 extends BaseNoPresenterActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private AppBarLayout appBar;
    private Toolbar toolbar;
    private TopTabView topTab;
    private NavigationView navView;

    private RoundLinearLayout imageView;
    private TextView textView;

    @Override
    protected void initEventListener() {

    }

    @Override
    protected void onEventRunEnd(Event event, int code) {

    }

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        ba.mActivityLayoutId = R.layout.m_five_activity_home;
        ba.mStatusBarActivityEnabled = true;
        ba.mStatusTranslucent = true;
        ba.toolBarId = R.id.toolbar;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void initView() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        appBar = (AppBarLayout) findViewById(R.id.app_bar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        topTab = (TopTabView) findViewById(R.id.topTab);
        navView = (NavigationView) findViewById(R.id.nav_view);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        imageView = (RoundLinearLayout) navView.getHeaderView(0).findViewById(R.id.imageView);
        textView = (TextView) navView.getHeaderView(0).findViewById(R.id.textView);
        if (URLUtils.getHost().equals("39.100.145.211")) {
            toolbar.setSubtitle("商用版本");
        } else {
            toolbar.setSubtitle("DEMO版本");
        }

        if (LoginFiveParamManager.getInstance().getLoginData() != null) {
            String s = LoginFiveParamManager.getInstance().getLoginData().getTradeRegister().getName();
            toolbar.setTitle(s);
            XLog.i(URLUtils.getIP() + LoginFiveParamManager.getInstance().getLoginData().getTradeRegister().getCompanyImage());
            GlideUtil.display(this, imageView, URLUtils.getIP() + "/" + LoginFiveParamManager.getInstance().getLoginData().getTradeRegister().getCompanyImage(), R.drawable.new_logo);
            int role = LoginFiveParamManager.getInstance().getLoginData().getRole();
            if (role == 1) {
                textView.setText("系统管理员");
            } else if (role == 2) {
                textView.setText("员工");
            } else if (role == 3) {
                textView.setText("抄表员");
            }
        }
        navView.setNavigationItemSelectedListener(this);

//        drawerLayout.openDrawer(GravityCompat.START);
        XTabLayout tabLayout = topTab.getTableLayout();
//        tabLayout.setSelectedTabIndicatorHeight(SizeUtils.dp2px(2));
        tabLayout.setSelectedIndicatorWidth(SizeUtils.dp2px(25));
        topTab.addItemCustomView(R.layout.m_five_tablayout_item, new HomeBenDiFragment(), (view1, tab) -> {
            setView(view1, "本地", null, true);
        });
        topTab.addItemCustomView(R.layout.m_five_tablayout_item, new HomeJieRuFragment(), (view1, tab) -> {
            setView(view1, "接入", null, true);
        });
        topTab.addItemCustomView(R.layout.m_five_tablayout_item, new HomeFuWuFragment(), (view1, tab) -> {
            setView(view1, "服务", null, true);
        });
        topTab.addItemCustomView(R.layout.m_five_tablayout_item, new HomeJiZhanFragment(), (view1, tab) -> {
            setView(view1, "基站", null, true);
        });
    }

    private void setView(View customView, String text, Drawable mTabIcon, boolean showDivisionLine) {
        TextView item = customView.findViewById(R.id.tablayout_item);
        item.setText(text);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.ic_main_shuiwu) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if (id == R.id.ic_main_phone) {
            sendMessageToast("正在开发中...");
        } else if (id == R.id.ic_main_manage) {
            sendMessageToast("正在开发中...");
        } else if (id == R.id.ic_main_fenxi) {
            sendMessageToast("正在开发中...");
        } else if (id == R.id.ic_main_change) {
            ARouter.getInstance().build(K.WelcomeActivity).withInt("position", 1).navigation();
            LoginFiveParamManager.getInstance().clear();
            finish();
        }


        return true;
    }
}
