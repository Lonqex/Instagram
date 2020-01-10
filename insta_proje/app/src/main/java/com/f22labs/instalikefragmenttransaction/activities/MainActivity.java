package com.f22labs.instalikefragmenttransaction.activities;

import android.Manifest;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStructure;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.f22labs.instalikefragmenttransaction.Adapter.PagerAdapter2;
import com.f22labs.instalikefragmenttransaction.Adapter.PagerAdapter3;
import com.f22labs.instalikefragmenttransaction.R;
import com.f22labs.instalikefragmenttransaction.fragments.BaseFragment;
import com.f22labs.instalikefragmenttransaction.fragments.Giris_Fragment;
import com.f22labs.instalikefragmenttransaction.fragments.NewsFragment;
import com.f22labs.instalikefragmenttransaction.fragments.HomeFragment;
import com.f22labs.instalikefragmenttransaction.fragments.ShareFragment;
import com.f22labs.instalikefragmenttransaction.fragments.ProfileFragment;
import com.f22labs.instalikefragmenttransaction.fragments.SearchFragment;
import com.f22labs.instalikefragmenttransaction.fragments.Share_Camera;
import com.f22labs.instalikefragmenttransaction.fragments.Share_Galery;
import com.f22labs.instalikefragmenttransaction.fragments.Share_Video;
import com.f22labs.instalikefragmenttransaction.fragments.Tab_Camera;
import com.f22labs.instalikefragmenttransaction.fragments.Tab_Home;
import com.f22labs.instalikefragmenttransaction.fragments.Tab_Message;
import com.f22labs.instalikefragmenttransaction.utils.FragmentHistory;
import com.f22labs.instalikefragmenttransaction.utils.Utils;
import com.f22labs.instalikefragmenttransaction.views.FragNavController;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements BaseFragment.FragmentNavigation, FragNavController.RootFragmentListener {


    @BindView(R.id.content_frame)
    FrameLayout contentFrame;

    private int[] mTabIconsSelected = {
            R.drawable.tab_home,
            R.drawable.tab_search,
            R.drawable.tab_share,
            R.drawable.tab_news,
            R.drawable.tab_profile};


    @BindArray(R.array.tab_name)
    String[] TABS;

    @BindView(R.id.bottom_tab_layout)
    TabLayout bottomTabLayout;

    @BindView(R.id.login_framelayout)
    FrameLayout login_frame;

    private FragNavController mNavController;

    private FragmentHistory fragmentHistory;

    private ViewPager pager,pager2,pager3;
    private TabLayout tablayout,tablayout3;
    private View view_tab;

    private FirebaseAuth auth;

    List<Fragment> fragmentList = new ArrayList<>();
    List<Fragment> fragmentList2 = new ArrayList<>();
    List<Fragment> fragmentList3 = new ArrayList<>();
    List<String> tabisim = new ArrayList<>();
    List<String> tabisim2 = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
       /* ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);*/

        auth = FirebaseAuth.getInstance();




        view_tab = findViewById(R.id.view_botton_tab);


        initTab();
        fragmentmetod(savedInstanceState);

        bottomTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                fragmentHistory.push(tab.getPosition());

                switchTab(tab.getPosition());


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

                fragmentHistory.push(tab.getPosition());
                switchTab(tab.getPosition());

                updateTabSelection(tab.getPosition());

            }
        });

    }


    public void giriskontrol()
    {
        if (auth.getCurrentUser()==null){
            try {
                FragmentManager manager=getSupportFragmentManager();
                FragmentTransaction transaction=manager.beginTransaction();
                transaction.replace(R.id.login_framelayout,new Giris_Fragment()).commit();
                giris();
            }catch (Exception e){}


        }else {
            switchTab(0);
            updateTabSelection(0);
            home2();
        }
    }

    public void giris()
    {
        login_frame.setVisibility(View.VISIBLE);
        pager.setVisibility(View.INVISIBLE);
        pager2.setVisibility(View.INVISIBLE);
        pager3.setVisibility(View.INVISIBLE);
        contentFrame.setVisibility(View.INVISIBLE);
        view_tab.setVisibility(View.INVISIBLE);
        bottomTabLayout.setVisibility(View.INVISIBLE);
        tablayout.setVisibility(View.INVISIBLE);
        tablayout3.setVisibility(View.INVISIBLE);
    }



        /////////////////GORUNUMLER/////////////////////////

    public void home(){
        login_frame.setVisibility(View.INVISIBLE);
        contentFrame.setVisibility(View.VISIBLE);
        view_tab.setVisibility(View.VISIBLE);
        bottomTabLayout.setVisibility(View.VISIBLE);

    }

    public void home2(){
        login_frame.setVisibility(View.GONE);
        contentFrame.setVisibility(View.VISIBLE);
        view_tab.setVisibility(View.GONE);
        bottomTabLayout.setVisibility(View.GONE);


    }

    public void search(){
        contentFrame.setVisibility(View.VISIBLE);
        view_tab.setVisibility(View.VISIBLE);
        bottomTabLayout.setVisibility(View.VISIBLE);
    }

    public void share(){
        contentFrame.setVisibility(View.VISIBLE);
        view_tab.setVisibility(View.INVISIBLE);
        bottomTabLayout.setVisibility(View.INVISIBLE);
    }

    public void news(){
        contentFrame.setVisibility(View.VISIBLE);
        view_tab.setVisibility(View.VISIBLE);
        bottomTabLayout.setVisibility(View.VISIBLE);
    }

    public void profil(){
        contentFrame.setVisibility(View.VISIBLE);
        view_tab.setVisibility(View.VISIBLE);
        bottomTabLayout.setVisibility(View.VISIBLE);
    }

    public void profil_düzenle(){
        contentFrame.setVisibility(View.VISIBLE);
        view_tab.setVisibility(View.INVISIBLE);
        bottomTabLayout.setVisibility(View.INVISIBLE);
    }
    public void girisbottom(){
        view_tab.setVisibility(View.INVISIBLE);
        bottomTabLayout.setVisibility(View.INVISIBLE);
    }


    private void fragmentmetod(Bundle savedInstanceState) {
        fragmentHistory = new FragmentHistory();
        mNavController = FragNavController.newBuilder(savedInstanceState, getSupportFragmentManager(), R.id.content_frame)
                .rootFragmentListener(this, TABS.length)
                .build();

        if (auth.getCurrentUser()==null){

            switchTab(0);
            updateTabSelection(0);

        }
        else {
            switchTab(0);
            updateTabSelection(0);
        }
    }

    ////////////////////SETUPLAR/////////////////////////

    private void Viewpagersetup1() {

        pager = (ViewPager) findViewById(R.id.viewpager);
        fragmentList.add(new Tab_Camera());
        fragmentList.add(new Tab_Home());
        fragmentList.add(new Tab_Message());

        PagerAdapter adapter = new com.f22labs.instalikefragmenttransaction.Adapter.PagerAdapter(getSupportFragmentManager(),fragmentList);
        pager.setCurrentItem(1);
        pager.setAdapter(adapter);

    }


    private void Viewpagersetup2() {
        pager2 = (ViewPager) findViewById(R.id.viewpager2);
        tablayout = (TabLayout) findViewById(R.id.tablayout);
        fragmentList2.add(new Share_Galery());
        fragmentList2.add(new Share_Camera());
        fragmentList2.add(new Share_Video());
        tabisim.add("Galeri");
        tabisim.add("Camera");
        tabisim.add("Video");
        PagerAdapter2 adapter2 = new com.f22labs.instalikefragmenttransaction.Adapter.PagerAdapter2(getSupportFragmentManager(),fragmentList2,tabisim);
        pager2.setCurrentItem(0);
        pager2.setAdapter(adapter2);
        tablayout.setupWithViewPager(pager2);

    }
   /* private void Viewpagersetup3() {
        pager3 = (ViewPager) findViewById(R.id.viewpager3);
        tablayout3 = (TabLayout) findViewById(R.id.tablayout3);
        fragmentList3.add(new NewsFragment());
        PagerAdapter3 adapter3 = new PagerAdapter3(getSupportFragmentManager(),fragmentList3);
        pager3.setAdapter(adapter3);


    }*/

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void initTab() {
        if (bottomTabLayout != null) {
            for (int i = 0; i < TABS.length; i++) {
                bottomTabLayout.addTab(bottomTabLayout.newTab());
                TabLayout.Tab tab = bottomTabLayout.getTabAt(i);
                if (tab != null)
                    tab.setCustomView(getTabView(i));
            }
        }
    }


    private View getTabView(int position) {
        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.tab_item_bottom, null);
        ImageView icon = (ImageView) view.findViewById(R.id.tab_icon);
        icon.setImageDrawable(Utils.setDrawableSelector(MainActivity.this, mTabIconsSelected[position], mTabIconsSelected[position]));
        return view;
    }



    private void switchTab(int position) {
        mNavController.switchTab(position);

    }


    @Override
    public void onBackPressed() {

        if (!mNavController.isRootFragment()) {
            mNavController.popFragment();
        } else {

            if (fragmentHistory.isEmpty()) {
                super.onBackPressed();
            } else {


                if (fragmentHistory.getStackSize() > 1) {

                    int position = fragmentHistory.popPrevious();

                    switchTab(position);

                    updateTabSelection(position);

                } else {

                    switchTab(0);

                    updateTabSelection(0);

                    fragmentHistory.emptyStack();
                }
            }

        }
    }


    private void updateTabSelection(int currentTab){

        for (int i = 0; i <  TABS.length; i++) {
            TabLayout.Tab selectedTab = bottomTabLayout.getTabAt(i);
            if(currentTab != i) {
                selectedTab.getCustomView().setSelected(false);
            }else{
                selectedTab.getCustomView().setSelected(true);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mNavController != null) {
            mNavController.onSaveInstanceState(outState);
        }
    }

    @Override
    public void pushFragment(Fragment fragment) {
        if (mNavController != null) {
            mNavController.pushFragment(fragment);
        }
    }



    @Override
    public Fragment getRootFragment(int index) {
        switch (index) {

            case FragNavController.TAB1:
                return new HomeFragment();
            case FragNavController.TAB2:
                return new SearchFragment();
            case FragNavController.TAB3:
                return new ShareFragment();
            case FragNavController.TAB4:
                return new NewsFragment();
            case FragNavController.TAB5:
                return new ProfileFragment();


        }
        throw new IllegalStateException("Need to send an index that we know");
    }

    public void profil_tiklandi(){

        fragmentHistory.push(4);
        switchTab(4);
        updateTabSelection(4);

    }

    public void search_tiklandi(){

        fragmentHistory.push(1);
        switchTab(1);
        updateTabSelection(1);

    }

    public void share_tiklandi(){

        fragmentHistory.push(2);
        switchTab(2);
        updateTabSelection(2);

    }

    public void news_tiklandi(){

        fragmentHistory.push(3);
        switchTab(3);
        updateTabSelection(3);

    }
}
