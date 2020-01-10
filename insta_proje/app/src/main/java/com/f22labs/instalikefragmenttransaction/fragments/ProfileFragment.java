package com.f22labs.instalikefragmenttransaction.fragments;

import android.media.Image;
import android.media.MediaFormat;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends BaseFragment{


    private ImageView ayarlar,gridview,assigmant;
    private Button profili_duzenle;
    private TextView adsoyad,biography,kullanici_adi,gonderi_sayisi,takip_sayisi,takipci_sayisi;
    private CircleImageView profil_resim;
    private DatabaseReference ref;
    private FirebaseAuth auth;
    private String uid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        ((MainActivity)getActivity()).profil();


        ref = FirebaseDatabase.getInstance().getReference();
        auth=FirebaseAuth.getInstance();


        if(auth.getCurrentUser() != null)
        {


            uid = auth.getCurrentUser().getUid();
            ayarlar = (ImageView) view.findViewById(R.id.ayarlar);

            gridview = (ImageView) view.findViewById(R.id.grid);
            assigmant = (ImageView) view.findViewById(R.id.assigment);

            adsoyad = (TextView) view.findViewById(R.id.profilismi);
            biography = (TextView) view.findViewById(R.id.bio);
            kullanici_adi = (TextView) view.findViewById(R.id.profilkullaniciadi);
            profil_resim = (CircleImageView) view.findViewById(R.id.profilresim);
            takip_sayisi = (TextView) view.findViewById(R.id.takip_sayisi);
            takipci_sayisi = (TextView) view.findViewById(R.id.takipci_sayisi);
            gonderi_sayisi = (TextView) view.findViewById(R.id.gonderi_sayisi);

            GridView gridView_class = new GridView();
            gridView_class.getid(auth.getCurrentUser().getUid());

            ((MainActivity)getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.profil_frame,gridView_class).commit();

            gridview.setColorFilter(ContextCompat.getColor(getActivity(),R.color.siyah));

            gridview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GridView gridView_class = new GridView();
                    gridView_class.getid(auth.getCurrentUser().getUid());
                    gridview.setColorFilter(ContextCompat.getColor(getActivity(),R.color.siyah));
                    assigmant.setColorFilter(ContextCompat.getColor(getActivity(),R.color.gri2));
                    ((MainActivity)getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.profil_frame,gridView_class).commit();
                }
            });
            assigmant.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gridview.setColorFilter(ContextCompat.getColor(getActivity(),R.color.gri2));
                    assigmant.setColorFilter(ContextCompat.getColor(getActivity(),R.color.siyah));
                }
            });


            ref.child("kullanicilar").child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String username_string = dataSnapshot.child("kullanici_adi").getValue().toString();
                    String adsoyad_string = dataSnapshot.child("adsoyad").getValue().toString();
                    adsoyad.setText(adsoyad_string);
                    kullanici_adi.setText(username_string);
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


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            ref.child("Post").child(auth.getCurrentUser().getUid()).orderByChild("uid").equalTo(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    long gonderi_s = dataSnapshot.getChildrenCount();
                    gonderi_sayisi.setText(String.valueOf(gonderi_s));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });



            ayarlar = (ImageView) view.findViewById(R.id.ayarlar);
        ayarlar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFragmentNavigation.pushFragment(new Settings_Fragment());
            }
        });


        profili_duzenle = (Button) view.findViewById(R.id.profiliduzenle);

        profili_duzenle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFragmentNavigation.pushFragment(new Profili_Duzenle());
            }
        });

        }
        else {
            ((MainActivity)getActivity()).giriskontrol();
        }


        return view;
    }


}
