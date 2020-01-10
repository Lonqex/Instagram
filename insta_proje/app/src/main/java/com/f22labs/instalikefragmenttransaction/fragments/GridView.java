package com.f22labs.instalikefragmenttransaction.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;


import com.bumptech.glide.Glide;
import com.f22labs.instalikefragmenttransaction.Adapter.Image;
import com.f22labs.instalikefragmenttransaction.Adapter.item_home;
import com.f22labs.instalikefragmenttransaction.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GridView extends BaseFragment {
    private RecyclerView recyclerView;
    private FirebaseRecyclerAdapter<item_home,holder> madapter;
    private DatabaseReference ref;
    private String id;

    public void getid(String uid)
    {
        this.id =uid;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.gridview_ekrani,container,false);



        ref = FirebaseDatabase.getInstance().getReference();

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        
        setadapter();

        GridLayoutManager manager = new GridLayoutManager(getActivity(),3);

        recyclerView.setLayoutManager(manager);

        recyclerView.setAdapter(madapter);



        return view;
    }

    private void setadapter() {

        madapter = new FirebaseRecyclerAdapter<item_home, holder>(
                item_home.class,
                R.layout.image_goruntu,
                holder.class,
                ref.child("Post").child(id).orderByChild("uid").equalTo(id)

        ) {
            @Override
            public item_home getItem(int position) {
                return super.getItem(getItemCount() - 1 - position);
            }
            @Override
            protected void populateViewHolder(holder holder, final item_home model, final int position) {

                Glide.with(getActivity()).load(model.getUrl()).into(holder.image);

                holder.frame.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Bundle bundle = new Bundle();
                        bundle.putString("key",getRef(getItemCount()-1-position).getKey());
                        bundle.putString("url",model.getUrl());
                        bundle.putString("id",model.getUid());
                        bundle.putString("tip",model.getTip());

                        item_Gridview item_gridview = new item_Gridview();
                        item_gridview.setArguments(bundle);
                        mFragmentNavigation.pushFragment(item_gridview);


                    }
                });

            }
        };

    }

    public static class holder extends RecyclerView.ViewHolder{



        Image image;
        FrameLayout frame;
        public holder(@NonNull View itemView) {
            super(itemView);

            image = (Image) itemView.findViewById(R.id.image);
            frame = (FrameLayout) itemView.findViewById(R.id.framelayout);

        }

        public void setMedia(String tip, String url, Context context){

            if (tip.equals("resim")){

                Glide.with(context).load(url).into(image);
            }
            else {
                image.setVisibility(View.GONE);
              /*  cam.setVisibility(View.VISIBLE);
                videoView.setVisibility(View.VISIBLE);*/
            }

        }


    }
}
