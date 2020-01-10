package com.f22labs.instalikefragmenttransaction.Adapter;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sierra on 9.10.2017.
 */

public class PagerAdapter extends FragmentPagerAdapter {

    List<Fragment> fragmentlistesi=new ArrayList<>();


    public PagerAdapter(FragmentManager fm, List<Fragment>fraglistesi) {
        super(fm);
        this.fragmentlistesi=fraglistesi;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentlistesi.get(position);
    }

    @Override
    public int getCount() {
        return fragmentlistesi.size();
    }

    public void clearall(ViewGroup viewGroup){

        for (int i=0;i<fragmentlistesi.size();i++){

            try {

                Object object=this.instantiateItem(viewGroup,i);

                if (object!=null){
                    this.destroyItem(viewGroup,i,object);
                }

                this.instantiateItem(viewGroup,i);

            }catch (Exception e){

            }
        }

    }


    public void item_delete(ViewGroup viewGroup,int i){
        try {

            Object object=this.instantiateItem(viewGroup,i);

            this.destroyItem(viewGroup,i,object);


        }catch (Exception e){

        }
    }

    public void item_create(ViewGroup viewGroup,int i){

        this.instantiateItem(viewGroup,i);

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }
}
