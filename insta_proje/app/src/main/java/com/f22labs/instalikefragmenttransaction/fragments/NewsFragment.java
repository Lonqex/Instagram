package com.f22labs.instalikefragmenttransaction.fragments;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.f22labs.instalikefragmenttransaction.R;
import com.f22labs.instalikefragmenttransaction.activities.MainActivity;
import com.f22labs.instalikefragmenttransaction.utils.Utils;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;


public class NewsFragment extends BaseFragment{

    private RecyclerView recyclerView;
    private FirebaseRecyclerAdapter<String,Holder> adapter;
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        ((MainActivity)getActivity()).news();

        recyclerView = (RecyclerView) view.findViewById(R.id.news_recyclerview);

        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);

        adapter_();

        recyclerView.setAdapter(adapter);

        return view;
    }

    private void adapter_() {
        adapter = new FirebaseRecyclerAdapter<String, Holder>(
                String.class,
                R.layout.item_news,
                Holder.class,
                ref.child("kullanicilar").child(auth.getCurrentUser().getUid()).child("takip_istegi")

        ) {
            @Override
            protected void populateViewHolder(final Holder viewHolder, final String model, int position) {

                ref.child("kullanicilar").child(model).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        String username = dataSnapshot.child("kullanici_adi").getValue().toString();
                        String profil_image = dataSnapshot.child("profil_resmi").getValue().toString();

                        Spannable takipci_kadi = new SpannableString(username);
                        takipci_kadi.setSpan(new ForegroundColorSpan(Color.BLACK),0,username.length(),0);
                        takipci_kadi.setSpan(new RelativeSizeSpan(1.3f),0,username.length(),0);
                        takipci_kadi.setSpan(new StyleSpan(Typeface.BOLD),0,username.length(),0);

                        Glide.with(getActivity()).load(profil_image).into(viewHolder.image);
                        viewHolder.textView.setText(takipci_kadi + " kullanıcısı seni takip etmek istiyor.");

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                viewHolder.onayla.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        ref.child("kullanicilar").child(auth.getCurrentUser().getUid()).child("takipci").child(model).setValue(model);
                        ref.child("kullanicilar").child(model).child("takip").child(auth.getCurrentUser().getUid()).setValue(auth.getCurrentUser().getUid());

                        ref.child("Post").child(auth.getCurrentUser().getUid()).orderByChild("uid").equalTo(auth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                for(DataSnapshot getpost:dataSnapshot.getChildren())
                                {
                                    final String key = getpost.getRef().getKey();

                                    ref.child("Post").child(auth.getCurrentUser().getUid()).child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {


                                            Map map = new HashMap<>();
                                            map.put("url",dataSnapshot.child("url").getValue());
                                            map.put("time",dataSnapshot.child("time").getValue());
                                            map.put("uid",dataSnapshot.child("uid").getValue());

                                            ref.child("Post").child(model).child(key).setValue(map);
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        ref.child("kullanicilar").child(auth.getCurrentUser().getUid()).child("takip_istegi").child(model).removeValue();


                    }
                });

            }
        };
    }

    public static class Holder extends RecyclerView.ViewHolder{

        CircleImageView image;
        TextView textView,onayla;

        public Holder(View itemView) {
            super(itemView);
            image = (CircleImageView) itemView.findViewById(R.id.image2);
            textView = (TextView) itemView.findViewById(R.id.kadi2);
            onayla = (TextView) itemView.findViewById(R.id.onayla);
        }
    }

}
