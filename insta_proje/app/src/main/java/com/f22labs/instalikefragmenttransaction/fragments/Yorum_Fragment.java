package com.f22labs.instalikefragmenttransaction.fragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.f22labs.instalikefragmenttransaction.Adapter.TimeSinceAgo;
import com.f22labs.instalikefragmenttransaction.Adapter.item_comment;
import com.f22labs.instalikefragmenttransaction.R;
import com.f22labs.instalikefragmenttransaction.activities.MainActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.github.curioustechizen.ago.RelativeTimeTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class Yorum_Fragment extends BaseFragment{

    private RecyclerView recyclerView;
    private FirebaseRecyclerAdapter<item_comment,viewholder> madapter;
    private String key,id;
    private DatabaseReference mref;
    private EditText yorum;
    private ImageView yorumyap,geri;
    private FirebaseAuth auth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_yorum, container, false);


        ((MainActivity)getActivity()).profil_düzenle();
        key = this.getArguments().getString("key");

        auth = FirebaseAuth.getInstance();
        mref = FirebaseDatabase.getInstance().getReference();

        id = auth.getCurrentUser().getUid();
        yorum = (EditText) view.findViewById(R.id.yorum);
        yorumyap = (ImageView) view.findViewById(R.id.yorum_yap);
        geri = (ImageView) view.findViewById(R.id.geri);
        recyclerView = (RecyclerView) view.findViewById(R.id.recylerview);


        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(RecyclerView.VERTICAL);

        yorumyap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map map = new HashMap();
                map.put("id",id);
                map.put("Aciklama",yorum.getText().toString());
                map.put("time", ServerValue.TIMESTAMP);
                mref.child("Yorumlar").child(key).push().setValue(map);
                yorum.setText("");
            }
        });

        setupadapter();

        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(madapter);

        return view;
    }

    private void setupadapter() {

        madapter = new FirebaseRecyclerAdapter<item_comment, viewholder>(

                item_comment.class,
                R.layout.item_comment,
                viewholder.class,
                mref.child("Yorumlar").child(key).orderByChild("time")

        ) {
            @Override
            protected void populateViewHolder(viewholder viewHolder, item_comment model, int i) {

                viewHolder.setUsername(model.getAciklama());
                viewHolder.setProfil(model.getId(),getActivity(),model.getAciklama());
                viewHolder.setZaman(model.getTime());

            }
        };
    }

    public static class viewholder extends RecyclerView.ViewHolder{

        TextView yorumtext;
        RelativeTimeTextView zaman;
        CircleImageView profil_resmi;

        public viewholder(@NonNull View itemView) {
            super(itemView);

            yorumtext = (TextView) itemView.findViewById(R.id.username);
            profil_resmi = (CircleImageView) itemView.findViewById(R.id.profilimage);
            zaman = (RelativeTimeTextView) itemView.findViewById(R.id.saat);


        }

        public void setUsername(final String aciklama)
        {
            DatabaseReference mref = FirebaseDatabase.getInstance().getReference();
            FirebaseAuth auth = FirebaseAuth.getInstance();

            mref.child("kullanicilar").child(auth.getCurrentUser().getUid()).child("kullanici_adi").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String username = dataSnapshot.getValue().toString();

                    Spannable yorum_kadi = new SpannableString(username);
                    yorum_kadi.setSpan(new ForegroundColorSpan(Color.BLACK),0,username.length(),0);
                    yorum_kadi.setSpan(new RelativeSizeSpan(1.3f),0,username.length(),0);
                    yorum_kadi.setSpan(new StyleSpan(Typeface.BOLD),0,username.length(),0);
                    yorumtext.setText(yorum_kadi);

                    Spannable yorum_gonderi = new SpannableString(aciklama);
                    yorum_gonderi.setSpan(new ForegroundColorSpan(Color.parseColor("#555555")),0,aciklama.length(),0);
                    yorum_gonderi.setSpan(new RelativeSizeSpan(1.2f),0,aciklama.length(),0);
                    yorum_gonderi.setSpan(new StyleSpan(Typeface.NORMAL),0,aciklama.length(),0);
                    yorumtext.append(" ");
                    yorumtext.append(yorum_gonderi);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            yorumtext.setText(aciklama);
        }

        public void setProfil(String id, final Context context, final String aciklama)
        {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
            ref.child("kullanicilar").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String kadi = dataSnapshot.child("kullanici_adi").getValue().toString();
                    String profil_url = dataSnapshot.child("profil_resmi").getValue().toString();

                    Glide.with(context).load(profil_url).into(profil_resmi);
                    yorumtext.setText(kadi + " " + aciklama);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        public void setZaman(long time)
        {
            String times = TimeSinceAgo.getTimeAgo(time);
            zaman.setReferenceTime(time);
        }

    }

}
