package com.f22labs.instalikefragmenttransaction.fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.f22labs.instalikefragmenttransaction.Adapter.PagerAdapter2;
import com.f22labs.instalikefragmenttransaction.R;
import com.f22labs.instalikefragmenttransaction.activities.MainActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ShareFragment extends BaseFragment{

    private ViewPager pager2;
    TabLayout tabLayout;

    List<Fragment> fragmentlistesi2=new ArrayList<>();
    List <String> tabisim2=new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_share, container, false);
        ((MainActivity)getActivity()).share();



        ((MainActivity)getActivity()).share();

        fragmentlistesi2.clear();
        tabisim2.clear();

        pager2= (ViewPager) view.findViewById(R.id.viewpager2);
        tabLayout= (TabLayout) view.findViewById(R.id.tablayout);
        fragmentlistesi2.add(new Share_Galery());
        fragmentlistesi2.add(new Share_Camera());
        fragmentlistesi2.add(new Share_Video());
        tabisim2.add("Gallery");
        tabisim2.add("Camera");
        tabisim2.add("Video");
        final PagerAdapter2 pagerAdapter2=new PagerAdapter2(getChildFragmentManager(),fragmentlistesi2,tabisim2);

        pager2.setAdapter(pagerAdapter2);
        tabLayout.setupWithViewPager(pager2);
        /*pagerAdapter2.item_delete(pager2,1);
        Log.i("viewpager2","viewpager2 oluşturuldu.");

        pager2.setOffscreenPageLimit(2);

        pager2.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if (position==1){
                    pagerAdapter2.item_create(pager2,1);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });*/
        return view;
    }

}
