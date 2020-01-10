package com.f22labs.instalikefragmenttransaction.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class PagerAdapter2 extends FragmentPagerAdapter {

    List<Fragment> fragmentList = new ArrayList<>();
    List<String> tabisim = new ArrayList<>();

    public PagerAdapter2(FragmentManager fm, List<Fragment> fraglistesi,List<String> liste) {
        super(fm);
        this.fragmentList = fraglistesi;
        this.tabisim = liste;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabisim.get(position);
    }
}
