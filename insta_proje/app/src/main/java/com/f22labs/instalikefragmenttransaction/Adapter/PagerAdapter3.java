package com.f22labs.instalikefragmenttransaction.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class PagerAdapter3 extends FragmentPagerAdapter {

    List<Fragment> fragmentList = new ArrayList<>();

    public PagerAdapter3(FragmentManager fm, List<Fragment> fraglistesi) {
        super(fm);
        this.fragmentList = fraglistesi;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

}
