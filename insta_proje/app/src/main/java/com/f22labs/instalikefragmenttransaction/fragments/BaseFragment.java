package com.f22labs.instalikefragmenttransaction.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

/**
 * Created by f22labs on 07/03/17.
 */

public class BaseFragment extends Fragment {


    FragmentNavigation mFragmentNavigation;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentNavigation) {
            mFragmentNavigation = (FragmentNavigation) context;
        }
    }

    public interface FragmentNavigation {
         void pushFragment(Fragment fragment);
    }






}
