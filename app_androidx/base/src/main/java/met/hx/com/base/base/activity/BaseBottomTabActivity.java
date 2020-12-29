package met.hx.com.base.base.activity;

import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.CallSuper;
import androidx.annotation.MenuRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import met.hx.com.base.R;
import met.hx.com.base.base.BaseAttribute;
import met.hx.com.librarybase.some_utils.XHStringUtil;
import met.hx.com.librarybase.some_utils.util.AbFragmentPagerAdapter;
import met.hx.com.librarybase.some_utils.util.AbViewPager;
import met.hx.com.librarybase.views.BottomNavigationViewEx;

/**
 * Created by huxu on 2017/11/10.
 */
public abstract class BaseBottomTabActivity extends BaseNoPresenterActivity implements
        BottomNavigationView.OnNavigationItemSelectedListener, ViewPager.OnPageChangeListener {
    protected AbFragmentPagerAdapter mFragmentPagerAdapter;
    /**
     * 内容的View.
     */
    protected AbViewPager mBaseBottomTabViewpager;
    protected BottomNavigationViewEx mBaseBottomTabNavigation;
    protected RelativeLayout mBaseBottomTabRelativeContent;
    protected ArrayList<Fragment> arrayListFragment;
    protected List<Integer> listMenuTrue = new ArrayList<>();
    protected List<Integer> listMenu = new ArrayList<>();


    @CallSuper
    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        ba.mActivityLayoutId = R.layout.base_activity_bottomtab;
        ba.mStatusBarActivityEnabled=false;
        onChildInitAttribute(ba);
    }

    @CallSuper
    @Override
    protected void initView() {
        ArrayList<Fragment> array=addFragment();
        if (array != null) {
            if (array.size() > 0) {
                mBaseBottomTabRelativeContent = (RelativeLayout) findViewById(R.id.base_bottomTab_relative_content);
                mBaseBottomTabViewpager = (AbViewPager) findViewById(R.id.base_bottomTab_viewpager);
                mBaseBottomTabViewpager.setEnabledScrollAnim(onEnabledScrollAnim());
                setCanScroll(canScroll());
                mBaseBottomTabNavigation = (BottomNavigationViewEx) findViewById(R.id.base_bottomTab_navigation);
                initStyle(mBaseBottomTabNavigation, mBaseBottomTabRelativeContent);
                if (getCenterLayout() != 0) {
                    getLayoutInflater().inflate(getCenterLayout(), mBaseBottomTabRelativeContent);
                }
                if (showCenter()) {
                    mBaseBottomTabRelativeContent.setVisibility(View.VISIBLE);
                } else {
                    mBaseBottomTabRelativeContent.setVisibility(View.GONE);
                }
                mBaseBottomTabRelativeContent.setOnClickListener(v -> onCenterClick(v));
                mBaseBottomTabNavigation.setOnNavigationItemSelectedListener(this);
                FragmentManager mFragmentManager = this.getSupportFragmentManager();
                arrayListFragment = new ArrayList<>();
                mFragmentPagerAdapter = new AbFragmentPagerAdapter(mFragmentManager, arrayListFragment);
                mBaseBottomTabViewpager.setAdapter(mFragmentPagerAdapter);
                mBaseBottomTabViewpager.addOnPageChangeListener(this);
                setMenuAndList(addTabMenuId(), addFragment());
            }
        }
    }


    /**
     * 动态设置menu，menu的长度要小于等于list的长度
     *
     * @param menuId
     * @param arrayList
     */
    @CallSuper
    protected void setMenuAndList(int menuId, ArrayList<Fragment> arrayList) {
        if (menuId != 0 && arrayList != null && arrayList.size() > 0) {
            listMenu.clear();
            listMenuTrue.clear();
            mBaseBottomTabNavigation.getMenu().clear();
            mBaseBottomTabNavigation.inflateMenu(menuId);
            setCache(arrayList.size());
            mBaseBottomTabNavigation.enableAnimation(false);
            mBaseBottomTabNavigation.enableShiftingMode(false);
            mBaseBottomTabNavigation.enableItemShiftingMode(false);
            arrayListFragment.clear();
            arrayListFragment.addAll(arrayList);
            mFragmentPagerAdapter.notifyDataSetChanged();
            for (int i = 0; i < mBaseBottomTabNavigation.getMenu().size(); i++) {
                if (!XHStringUtil.isEmpty(mBaseBottomTabNavigation.getMenu().getItem(i).getTitle().toString(), false)) {
                    listMenuTrue.add(mBaseBottomTabNavigation.getMenu().getItem(i).getItemId());
                }
            }
            for (int i = 0; i < mBaseBottomTabNavigation.getMenu().size(); i++) {
                listMenu.add(mBaseBottomTabNavigation.getMenu().getItem(i).getItemId());
            }
        }
    }


    /**
     * 动态设置缓存数量
     *
     * @param cache
     */
    @CallSuper
    protected void setCache(int cache) {
        mBaseBottomTabViewpager.setOffscreenPageLimit(cache);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int i = item.getItemId();
        int indexTrue = listMenuTrue.indexOf(i);
        int index = listMenu.indexOf(i);
        if (index != -1) {
            mBaseBottomTabViewpager.setCurrentItem(indexTrue);
        }
        onBottomTabClick(index);
        mBaseBottomTabNavigation.getMenu().getItem(index).setChecked(true);
        return false;
    }

    @Override
    public void onPageSelected(int position) {
        if (position < listMenuTrue.size()) {
            onTruePageSelected(position);
            mBaseBottomTabNavigation.getMenu().findItem(listMenuTrue.get(position)).setChecked(true);
        }
    }

    /**
     * 底部点击事件（如果没fragment，它也会生效）
     * @param position
     */
    protected void onBottomTabClick(int position) {

    }

    /**
     * 是否开启viewpager切换动画
     * @return
     */
    protected boolean onEnabledScrollAnim(){
        return true;
    }
    /**
     * fragment切换事件（根据真正的fragment数量切换，比如添加了5个fragment，但是tab只添加了3个，那后面2个不会生效）
     * @param position
     */
    protected void onTruePageSelected(int position) {
    }

    @CallSuper
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @CallSuper
    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * 写activty的title样式，不可写layout
     *
     * @param ba
     */
    protected abstract void onChildInitAttribute(BaseAttribute ba);

    /**
     * 获取fragment集合
     *
     * @return
     */
    protected abstract ArrayList<Fragment> addFragment();

    /**
     * 动态设置是否可以滚动
     *
     * @param canScroll
     */
    protected void setCanScroll(boolean canScroll) {
        mBaseBottomTabViewpager.setPagingEnabled(canScroll);
    }

    /**
     * 设置样式
     *
     * @param mBaseBottomTabNavigation
     * @param mBaseBottomTabRelativeContent
     */
    protected void initStyle(BottomNavigationViewEx mBaseBottomTabNavigation, RelativeLayout mBaseBottomTabRelativeContent) {

    }

    ;


    /**
     * 设置菜单资源
     *
     * @return
     */
    protected abstract @MenuRes
    int addTabMenuId();

    /**
     * 是否可以滑动
     *
     * @return
     */
    protected boolean canScroll() {
        return false;
    }

    ;

    /**
     * 是否显示中间图标
     *
     * @return
     */
    protected boolean showCenter() {
        return false;
    }

    ;

    /**
     * 中间布局
     *
     * @return
     */
    protected int getCenterLayout() {
        return 0;
    }

    ;


    /**
     * 中间按钮的点击
     *
     * @param v
     */
    protected void onCenterClick(View v) {

    }


}
