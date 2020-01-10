package com.f22labs.instalikefragmenttransaction.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.f22labs.instalikefragmenttransaction.R;
import com.f22labs.instalikefragmenttransaction.activities.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.f22labs.instalikefragmenttransaction.R.drawable.giris;


public class User_Profil extends BaseFragment{

    private Button takipet;
    private TextView adsoyad,biography,kullanici_adi,gonderi_sayisi,takip_sayisi,takipci_sayisi;
    private CircleImageView profil_resim;
    private DatabaseReference ref;
    private FirebaseAuth auth;
    private String muid,id,durum;
    private ImageView ayarlar,gridview,assigmant;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_profil, container, false);


        ref = FirebaseDatabase.getInstance().getReference();
        auth=FirebaseAuth.getInstance();

        id = this.getArguments().getString("id");
        muid = auth.getCurrentUser().getUid();



        takipet = (Button) view.findViewById(R.id.takipet);
        gridview = (ImageView) view.findViewById(R.id.grid);
        assigmant = (ImageView) view.findViewById(R.id.assigment);
        adsoyad = (TextView) view.findViewById(R.id.profilismi);
        biography = (TextView) view.findViewById(R.id.bio);
        profil_resim = (CircleImageView) view.findViewById(R.id.profilresim);
        kullanici_adi = (TextView) view.findViewById(R.id.profilkullaniciadi);
        takip_sayisi = (TextView) view.findViewById(R.id.takip_sayisi);
        takipci_sayisi = (TextView) view.findViewById(R.id.takipci_sayisi);
        gonderi_sayisi = (TextView) view.findViewById(R.id.gonderi_sayisi);

        getdata();

        ref.child("kullanicilar").child(id).child("takip_istegi").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.hasChild(muid))
                {
                    takipet.setText("İstek Gönderildi");
                    takipet.setTextColor(getResources().getColor(R.color.siyah));
                    takipet.setBackgroundResource(R.drawable.giris);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ref.child("kullanicilar").child(muid).child("takip").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.hasChild(id))
                {
                    takipet.setText("Takibi Bırak");
                    takipet.setTextColor(getResources().getColor(R.color.siyah));
                    takipet.setBackgroundResource(R.drawable.giris);
                }
                else
                {
                    takipet.setText("Takip Et");
                    takipet.setTextColor(getResources().getColor(R.color.beyaz));
                    takipet.setBackgroundResource(R.drawable.ileriaktif);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        takipet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ref.child("kullanicilar").child(id).child("takipci").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(dataSnapshot.hasChild(muid))
                        {
                            ref.child("kullanicilar").child(id).child("takipci").child(muid).removeValue();
                            ref.child("kullanicilar").child(muid).child("takip").child(id).removeValue();
                            takipet.setText("Takip Et");
                            takipet.setTextColor(getResources().getColor(R.color.beyaz));
                            takipet.setBackgroundResource(R.drawable.ileriaktif);

                            ref.child("Post").child(muid).orderByChild("uid").equalTo(id).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                for(DataSnapshot getpost:dataSnapshot.getChildren())
                                {
                                    final String key = getpost.getRef().getKey();

                                    ref.child("Post").child(muid).child(key).removeValue();

                                    getdata();
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        }
                        else
                        {

                            if(durum.equals("gizli"))
                            {

                                ref.child("kullanicilar").child(id).child("takip_istegi").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        if(dataSnapshot.hasChild(muid))
                                        {
                                            ref.child("kullanicilar").child(id).child("takip_istegi").child(muid).removeValue();
                                            takipet.setText("Takip Et");
                                            takipet.setTextColor(getResources().getColor(R.color.beyaz));
                                            takipet.setBackgroundResource(R.drawable.ileriaktif);
                                        }
                                        else
                                        {
                                            ref.child("kullanicilar").child(id).child("takip_istegi").child(muid).setValue(muid);
                                            takipet.setText("İstek Gönderildi");
                                            takipet.setTextColor(getResources().getColor(R.color.siyah));
                                            takipet.setBackgroundResource(R.drawable.giris);
                                        }

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }
                            else{
                            ref.child("kullanicilar").child(id).child("takipci").child(muid).setValue(muid);
                            ref.child("kullanicilar").child(muid).child("takip").child(id).setValue(id);
                            takipet.setText("Takibi Bırak");
                            takipet.setTextColor(getResources().getColor(R.color.siyah));
                            takipet.setBackgroundResource(R.drawable.giris);

                            ref.child("Post").child(id).orderByChild("uid").equalTo(id).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    for(DataSnapshot getpost:dataSnapshot.getChildren())
                                    {
                                        final String key = getpost.getRef().getKey();

                                        ref.child("Post").child(id).child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {


                                                Map map = new HashMap<>();
                                                map.put("url",dataSnapshot.child("url").getValue());
                                                map.put("time",dataSnapshot.child("time").getValue());
                                                map.put("uid",dataSnapshot.child("uid").getValue());

                                                ref.child("Post").child(muid).child(key).setValue(map);

                                                getdata();

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
                        }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });

            return view;
    }

    public void getdata()
    {
        ref.child("kullanicilar").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String username_string = dataSnapshot.child("kullanici_adi").getValue().toString();
                String adsoyad_string = dataSnapshot.child("adsoyad").getValue().toString();
                adsoyad.setText(adsoyad_string);
                kullanici_adi.setText(username_string);

                if(dataSnapshot.child("durum").getValue() != null)
                {
                    durum = dataSnapshot.child("durum").getValue().toString();

                    kontrol_yap();
                }
                else
                {
                    durum="acik";
                    kontrol_yap();
                }

                if(dataSnapshot.child("biography").getValue()!=null)
                {
                    String bio_string = dataSnapshot.child("biography").getValue().toString();
                    biography.setText(bio_string);
                }

                if(dataSnapshot.child("profil_resmi").getValue()!=null)
                {
                    String profilresim_string = dataSnapshot.child("profil_resmi").getValue().toString();
                    Glide.with(getActivity()).load(profilresim_string).into(profil_resim);

                }

                long takipci_s=dataSnapshot.child("takipci").getChildrenCount();
                long takip_s=dataSnapshot.child("takip").getChildrenCount();
                takipci_sayisi.setText(String.valueOf(takipci_s));
                takip_sayisi.setText(String.valueOf(takip_s));

                ref.child("Post").child(id).orderByChild("uid").equalTo(id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        long gonderi_s = dataSnapshot.getChildrenCount();
                        gonderi_sayisi.setText(String.valueOf(gonderi_s));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void kontrol_yap()
    {

        if(durum.equals("gizli"))
        {
            ref.child("kullanicilar").child(id).child("takipci").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<String> takipci_list = new ArrayList<String>();

                    for(DataSnapshot liste : dataSnapshot.getChildren())
                    {
                        String veri = String.valueOf(liste.getValue());
                        takipci_list.add(veri);
                    }

                    for(int i = 0; i < takipci_list.size();i++)
                    {
                       if(takipci_list.get(i).equals(muid))
                       {
                           GridView gridView_class = new GridView();
                           gridView_class.getid(id);

                           ((MainActivity)getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.profil_frame,gridView_class).commit();

                           gridview.setOnClickListener(new View.OnClickListener() {
                               @Override
                               public void onClick(View v) {
                                   GridView gridView_class = new GridView();
                                   gridView_class.getid(id);
                                   gridview.setColorFilter(ContextCompat.getColor(getActivity(),R.color.siyah));
                                   assigmant.setColorFilter(ContextCompat.getColor(getActivity(),R.color.gri2));
                                   ((MainActivity)getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.profil_frame,gridView_class).commit();
                               }
                           });
                       }
                       else
                       {
                           ((MainActivity)getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.profil_frame,new Lock_Layout()).commit();
                       }
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        else
        {
                    GridView gridView_class = new GridView();
                    gridView_class.getid(id);

                    ((MainActivity)getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.profil_frame,gridView_class).commit();

                    gridview.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                    GridView gridView_class = new GridView();
                    gridView_class.getid(id);
                    gridview.setColorFilter(ContextCompat.getColor(getActivity(),R.color.siyah));
                    assigmant.setColorFilter(ContextCompat.getColor(getActivity(),R.color.gri2));
                    ((MainActivity)getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.profil_frame,gridView_class).commit();
                }
            });
        }


    }

}
