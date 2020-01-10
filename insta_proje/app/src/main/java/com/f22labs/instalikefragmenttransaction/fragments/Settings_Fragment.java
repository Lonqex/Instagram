package com.f22labs.instalikefragmenttransaction.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.f22labs.instalikefragmenttransaction.R;
import com.f22labs.instalikefragmenttransaction.activities.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Settings_Fragment extends BaseFragment{

    private TextView cikisyap;
    private FirebaseAuth auth;
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private Switch mSwitch;
    private RelativeLayout relativeLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings, container, false);
        auth = FirebaseAuth.getInstance();
        cikisyap= (TextView) view.findViewById(R.id.Cikisyap);
        relativeLayout = (RelativeLayout) view.findViewById(R.id.Gizlilik);
        mSwitch = (Switch) view.findViewById(R.id.kilit);
        cikisyap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                mFragmentNavigation.pushFragment(new Giris_Fragment());
            }
        });


        if(auth.getCurrentUser() != null)
        {
            reference.child("kullanicilar").child(auth.getCurrentUser().getUid()).child("durum").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue() != null)
                    {
                        String durum = dataSnapshot.getValue().toString();
                        if(durum.equals("gizli"))
                        {
                            mSwitch.setChecked(true);
                        }
                        else
                        {
                            mSwitch.setChecked(false);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    if(mSwitch.isChecked())
                    {
                        mSwitch.setChecked(false);
                        reference.child("kullanicilar").child(auth.getCurrentUser().getUid()).child("durum").setValue("acik");

                    }
                      else
                    {
                        mSwitch.setChecked(true);
                        reference.child("kullanicilar").child(auth.getCurrentUser().getUid()).child("durum").setValue("gizli");
                    }
                }
            });



        }

        return view;
    }


}
