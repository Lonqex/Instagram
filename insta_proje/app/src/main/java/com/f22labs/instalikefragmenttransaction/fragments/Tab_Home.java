package com.f22labs.instalikefragmenttransaction.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.f22labs.instalikefragmenttransaction.Adapter.Image;
import com.f22labs.instalikefragmenttransaction.Adapter.TimeSinceAgo;
import com.f22labs.instalikefragmenttransaction.Adapter.item_home;
import com.f22labs.instalikefragmenttransaction.R;
import com.f22labs.instalikefragmenttransaction.activities.MainActivity;
import com.f22labs.instalikefragmenttransaction.utils.Utils;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.github.curioustechizen.ago.RelativeTimeTextView;
import com.github.kshitij_jain.instalike.InstaLikeView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;


public class Tab_Home extends BaseFragment{


    private int[] mTabIconsSelected = {
            R.drawable.tab_home,
            R.drawable.tab_search,
            R.drawable.tab_share,
            R.drawable.tab_news,
            R.drawable.tab_profile};

    private String[] TABS={
            "Home","Search","Share","News","Profil"
    };


    private RecyclerView recyclerView;
    private FirebaseRecyclerAdapter<item_home,viewholder> mAdapter;
    private DatabaseReference ref;
    private FirebaseAuth auth;
    private Boolean kontrol = false;
    private TabLayout bottomTabLayout;
    private ImageView send;


   /* @Override
    public void onStart() {
        super.onStart();
        ((MainActivity)getActivity()).home();
    }

    @Override
    public void onStop() {
        super.onStop();
        ((MainActivity)getActivity()).home2();
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_home, container, false);
        ((MainActivity)getActivity()).home2();

        auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser()!=null)
        {

            bottomTabLayout = (TabLayout) view.findViewById(R.id.bottom_tab_layout);

            initTab();

            bottomTabLayout.getTabAt(0).getCustomView().setSelected(true);

            bottomTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {

                    if(tab.getPosition() == 1)
                    {
                        ((MainActivity)getActivity()).search_tiklandi();
                    }
                    if(tab.getPosition() == 2)
                    {
                        ((MainActivity)getActivity()).share_tiklandi();
                    }
                    if(tab.getPosition() == 3)
                    {
                        ((MainActivity)getActivity()).news_tiklandi();
                    }
                    if(tab.getPosition() == 4)
                    {
                        ((MainActivity)getActivity()).profil_tiklandi();
                    }

                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });

            send = (ImageView) view.findViewById(R.id.send);
            send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mFragmentNavigation.pushFragment(new Tab_Message());
                }
            });
            ref = FirebaseDatabase.getInstance().getReference().child("Post").child(auth.getCurrentUser().getUid());
            recyclerView = (RecyclerView) view.findViewById(R.id.recylerview);

            LinearLayoutManager manager = new LinearLayoutManager(getActivity());
            manager.setOrientation(RecyclerView.VERTICAL);

            setupadapter();
            //recyclerView.setNestedScrollingEnabled(false);
            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(mAdapter);

        }

        return view;
    }

    private void setupadapter() {


        mAdapter = new FirebaseRecyclerAdapter<item_home, viewholder>(
                item_home.class,
                R.layout.item_home,
                viewholder.class,
                ref

        ) {

            @Override
            public item_home getItem(int position) {
                return super.getItem(getItemCount()-1-position); // FOTOLARI TERSTEN ALDIK.
            }

            public void getview(int position)
            {
                View view = recyclerView.findViewHolderForLayoutPosition(position).itemView;
                ImageView imageview = (ImageView) view.findViewById(R.id.gonderiimage);


            }

            @Override
            protected void populateViewHolder(final viewholder view, item_home model, final int position) {


                view.setimage(model.getUrl(),getActivity());
                view.setusername(model.getUid(),getActivity());
                view.setsure(model.getTime());

                view.begen_info(getRef(getItemCount()-1-position).getKey(),getActivity());

                view.begen_click(getRef(getItemCount()-1-position).getKey());

                view.yorum_fragment(mFragmentNavigation,getRef(getItemCount()-1-position).getKey());
                view.setyorum(getRef(getItemCount()-1-position).getKey(),model.getUid());
                view.begenme_sayisi(getRef(getItemCount()-1-position).getKey());





                final long[] ilk = {0};
                final long[] son = {0};
                view.image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ilk[0] = son[0];
                        son[0] = System.currentTimeMillis();

                        if(son[0] - ilk[0] < 250)
                        {

                            final FirebaseAuth auth = FirebaseAuth.getInstance();
                            final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();



                            ref.child("Begeniler").child(getRef(getItemCount()-1-position).getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    if(dataSnapshot.hasChild(auth.getCurrentUser().getUid()))
                                    {
                                        view.mInstaLikeView.start();
                                    }
                                    else
                                    {
                                        ref.child("Begeniler").child(getRef(getItemCount()-1-position).getKey()).child(auth.getCurrentUser().getUid()).setValue(auth.getCurrentUser().getUid());
                                        view.mInstaLikeView.start();
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });



                            view.mInstaLikeView.start();

                            ilk[0] = 0;
                            son[0] = 0;
                        }
                    }
                });

            }
        };


    }


    public static class viewholder extends RecyclerView.ViewHolder {

        View view;
        ImageView image;
        Image yorumtikla,begeni,gonderi;
        TextView username,yorum,begenmesayisi;
        RelativeTimeTextView zaman;
        CircleImageView profil_image;
        InstaLikeView mInstaLikeView;
        LinearLayout ln;
        public viewholder(@NonNull View itemView) {
            super(itemView);

            view = itemView;

            mInstaLikeView  = (InstaLikeView) view.findViewById(R.id.insta_like_view);
            // gonderi = view.findViewById(R.id.gonderiimage);
            begeni = (Image) view.findViewById(R.id.begen);
            yorumtikla = (Image) view.findViewById(R.id.yorum);
            yorum = (TextView) view.findViewById(R.id.yorumtext);
            image = (ImageView) view.findViewById(R.id.gonderiimage);
            username = (TextView) view.findViewById(R.id.username);
            profil_image = (CircleImageView) view.findViewById(R.id.profilimage);
            zaman = (RelativeTimeTextView) view.findViewById(R.id.zaman);
            begenmesayisi = (TextView) view.findViewById(R.id.begenmesayisi);
            ln = (LinearLayout) view.findViewById(R.id.ln1);

        }

        public void begenme_sayisi(String key)
        {
            DatabaseReference mref = FirebaseDatabase.getInstance().getReference();
            mref.child("Begeniler").child(key).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue()!=null)
                    {
                        ln.setVisibility(View.VISIBLE);
                        long sayi = dataSnapshot.getChildrenCount();
                        begenmesayisi.setText(String.valueOf(sayi) + " beğenme");
                    }
                    else
                    {
                        ln.setVisibility(View.GONE);
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        public void begen_info(String key, final Context context)
        {
            final FirebaseAuth auth = FirebaseAuth.getInstance();
            final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

            ref.child("Begeniler").child(key).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if(dataSnapshot.hasChild(auth.getCurrentUser().getUid()))
                    {

                        begeni.setImageResource(R.drawable.likered);
                    }
                    else {
                        begeni.setImageResource(R.drawable.like);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        public void begen_click(final String key)
        {
            final FirebaseAuth auth = FirebaseAuth.getInstance();
            final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

            begeni.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ref.child("Begeniler").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if(dataSnapshot.hasChild(auth.getCurrentUser().getUid()))
                            {
                                ref.child("Begeniler").child(key).child(auth.getCurrentUser().getUid()).removeValue();
                            }
                            else
                            {
                                ref.child("Begeniler").child(key).child(auth.getCurrentUser().getUid()).setValue(auth.getCurrentUser().getUid());
                                mInstaLikeView.start();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            });
        }

        public void setimage(String imageurl, Context context)
        {
            Glide.with(context).load(imageurl).into(image);
        }

        public void setusername(String user, final Context context)
        {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

            ref.child("kullanicilar").child(user).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    String kadi = dataSnapshot.child("kullanici_adi").getValue().toString();
                    String profil_url = dataSnapshot.child("profil_resmi").getValue().toString();
                    username.setText(kadi);

                    Glide.with(context).load(profil_url).into(profil_image);


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }


        public void setsure(long time)
        {

            String sure = TimeSinceAgo.getTimeAgo(time);

            zaman.setReferenceTime(time);

        }

        public void setyorum(String ref, final String uid)
        {
            final DatabaseReference mref = FirebaseDatabase.getInstance().getReference();
            final FirebaseAuth auth = FirebaseAuth.getInstance();

            mref.child("Yorumlar").child(ref).child(ref).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    final String text = dataSnapshot.child("Aciklama").getValue().toString();

                    mref.child("kullanicilar").child(uid).child("kullanici_adi").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String username = dataSnapshot.getValue().toString();

                            Spannable yorum_kadi = new SpannableString(username);
                            yorum_kadi.setSpan(new ForegroundColorSpan(Color.BLACK),0,username.length(),0);
                            yorum_kadi.setSpan(new RelativeSizeSpan(1.3f),0,username.length(),0);
                            yorum_kadi.setSpan(new StyleSpan(Typeface.BOLD),0,username.length(),0);
                            yorum.setText(yorum_kadi);

                            Spannable yorum_gonderi = new SpannableString(text);
                            yorum_gonderi.setSpan(new ForegroundColorSpan(Color.parseColor("#555555")),0,text.length(),0);
                            yorum_gonderi.setSpan(new RelativeSizeSpan(1.2f),0,text.length(),0);
                            yorum_gonderi.setSpan(new StyleSpan(Typeface.NORMAL),0,text.length(),0);
                            yorum.append(" ");
                            yorum.append(yorum_gonderi);



                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                    yorum.setText(text);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        public void yorum_fragment(final FragmentNavigation fragmentNavigation, final String key)
        {
            yorumtikla.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Yorum_Fragment yrmfragment = new Yorum_Fragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("key",key);
                    yrmfragment.setArguments(bundle);
                    fragmentNavigation.pushFragment(yrmfragment);
                }
            });


        }



    }

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
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.tab_item_bottom, null);
        ImageView icon = (ImageView) view.findViewById(R.id.tab_icon);
        icon.setImageDrawable(Utils.setDrawableSelector(getActivity(), mTabIconsSelected[position], mTabIconsSelected[position]));
        return view;
    }

}
