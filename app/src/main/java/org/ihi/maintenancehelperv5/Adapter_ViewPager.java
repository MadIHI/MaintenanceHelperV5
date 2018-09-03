package org.ihi.maintenancehelperv5;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class Adapter_ViewPager extends FragmentPagerAdapter {

    private final List<Fragment> mFragmentList = new ArrayList<>();

    public Adapter_ViewPager(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    public Fragment setItem(int position, Fragment fragment) {
        mFragmentList.set(position, null);
        return mFragmentList.set(position, fragment);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(Fragment fragment) {
        mFragmentList.add(fragment);
    }

}
