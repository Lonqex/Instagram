package com.f22labs.instalikefragmenttransaction.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.f22labs.instalikefragmenttransaction.R;

import org.w3c.dom.Text;

public class item_Gridview extends BaseFragment {


    private TextView textView;
    private ImageView image;
    private String key,id,url,tip;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.item_gridview,container,false);

        textView = (TextView) view.findViewById(R.id.text);
        image = (ImageView) view.findViewById(R.id.gonderiimage);


        key=this.getArguments().getString("key");
        id=this.getArguments().getString("id");
        url=this.getArguments().getString("url");


        textView.setText("Fotoğraf");
        Glide.with(getActivity()).load(url).into(image);


        return view;
    }
}
