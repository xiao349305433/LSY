package wu.loushanyun.com.fivemoduleapp.v.activity;

import androidx.fragment.app.Fragment;
import android.view.KeyEvent;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.wu.loushanyun.base.config.K;

import java.util.ArrayList;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.activity.PBaseBottomTabActivity;
import met.hx.com.base.util.OutOfTimeJumpManager;
import met.hx.com.librarybase.some_utils.XActivityUtils;
import wu.loushanyun.com.fivemoduleapp.R;
import wu.loushanyun.com.fivemoduleapp.p.presenter.HomePresenter;
import wu.loushanyun.com.fivemoduleapp.v.fragment.FourHomeFragment;
import wu.loushanyun.com.fivemoduleapp.v.fragment.OneHomeFragment;
import wu.loushanyun.com.fivemoduleapp.v.fragment.ThreeHomeFragment;
import wu.loushanyun.com.fivemoduleapp.v.fragment.TwoHomeFragment;

@Route(path = K.HomeActivity)
public class HomeActivity extends PBaseBottomTabActivity<HomePresenter> {
    private long mExitTime = 0;

    @Override
    protected void onChildInitAttribute(BaseAttribute ba) {
        ba.mHasTitle = false;
    }

    @Override
    protected ArrayList<Fragment> addFragment() {
        ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
        fragmentArrayList.add(new OneHomeFragment());
        fragmentArrayList.add(new TwoHomeFragment());
        fragmentArrayList.add(new ThreeHomeFragment());
        fragmentArrayList.add(new FourHomeFragment());
        return fragmentArrayList;
    }

    @Override
    protected void initView() {
        super.initView();
        OutOfTimeJumpManager.getInstance().setShowDialog(false);
        mBaseBottomTabViewpager.setEnabledScrollAnim(false);
    }

    @Override
    protected int addTabMenuId() {
        return R.menu.m_five_home_menu;
    }


    @Override
    protected HomePresenter initPresenter() {
        return new HomePresenter();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                XActivityUtils.finishAllActivities();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
