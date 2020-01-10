package com.f22labs.instalikefragmenttransaction.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.f22labs.instalikefragmenttransaction.Adapter.PagerAdapter;
import com.f22labs.instalikefragmenttransaction.R;
import com.f22labs.instalikefragmenttransaction.activities.MainActivity;
import com.f22labs.instalikefragmenttransaction.utils.Utils;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class HomeFragment extends BaseFragment {

    private FirebaseAuth auth;
    List<Fragment> fragmentlistesi=new ArrayList<>();
    ViewPager pager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ((MainActivity)getActivity()).home2();


        auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser()==null)
        {
            ((MainActivity)getActivity()).giriskontrol();
        }
        else
        {


            pager= (ViewPager) view.findViewById(R.id.viewpager);

            fragmentlistesi.clear();
            fragmentlistesi.add(new Tab_Camera());
            fragmentlistesi.add(new Tab_Home());
            fragmentlistesi.add(new Tab_Message());

            PagerAdapter adapter = new PagerAdapter(getChildFragmentManager(),fragmentlistesi);
            pager.setAdapter(adapter);
            pager.setCurrentItem(1);

            pager.setOffscreenPageLimit(3);

        }


        return view;
    }

}
