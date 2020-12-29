package com.test1moudle.p.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class MyFragmentPagerAdapter extends FragmentStatePagerAdapter {

    public ArrayList<Fragment> fragments = new ArrayList<Fragment>();
    private FragmentManager mFragmentManager;
    private String[] fundTypes;

    public MyFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public MyFragmentPagerAdapter(FragmentManager fm,
                                  FragmentManager mFragmentManager) {
        super(fm);
        this.mFragmentManager = mFragmentManager;
    }

    public MyFragmentPagerAdapter(FragmentManager fm,
                                  FragmentManager mFragmentManager, String[] fundTypes) {
        super(fm);
        this.mFragmentManager = mFragmentManager;
        this.fundTypes = fundTypes;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    public void addTolist(Fragment fragment,boolean unNotify) {
        fragments.add(fragment);
        if(true){
            notifyDataSetChanged();
        }
    }

    public void addTolist(List<Fragment> fragments, int location) {
        this.fragments.addAll(location, fragments);
        notifyDataSetChanged();
    }

    public void resetAllFragmentsButTheFirst(List<Fragment> mfragments) {
        ArrayList<Fragment> newfragments = new ArrayList<Fragment>();
        newfragments.add(fragments.get(0));
        newfragments.addAll(mfragments);
        fragments.clear();
        fragments = newfragments;
        notifyDataSetChanged();
    }


    @Override
    public CharSequence getPageTitle(int position) {
        if (fundTypes != null) {
            return fundTypes[position];
        } else {
            return super.getPageTitle(position);
        }
    }

    public void clearList() {
        fragments.clear();
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = fragments.get(position);

        return fragment;
    }

    @SuppressWarnings("unused")
    private void addFragment(Fragment fragment) {
        addTolist(fragment,false);
    }

    @SuppressWarnings("unused")
    private void removeFragment(Fragment fragment) {
        fragments.remove(fragment);
        notifyDataSetChanged();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        return super.instantiateItem(container, position);
    }
}
