package com.f22labs.instalikefragmenttransaction.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.f22labs.instalikefragmenttransaction.R;
import com.f22labs.instalikefragmenttransaction.activities.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;


public class Kullanici_Olustur_Ekrani extends BaseFragment{

    private Boolean fragment_kontrol,kullaniciadkontrol=true;
    private String email,code,verifaction;
    private EditText sifre,adsoyad,kadi;
    private Button devametbtn;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference ref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.kullanici_olustur_ekrani, container, false);

        ref=FirebaseDatabase.getInstance().getReference().child("kullanicilar");
        fragment_kontrol=this.getArguments().getBoolean("fragment");
        sifre = (EditText) view.findViewById(R.id.adsoyadsifre);
        adsoyad = (EditText) view.findViewById(R.id.adsoyad);
        kadi = (EditText) view.findViewById(R.id.kadi);
        devametbtn = (Button) view.findViewById(R.id.devamet);



        kadi.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence text, int start, int before, int count) {
                if(!kadi.toString().equals("")) {
                    ref.child("kullanicilar").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot id : dataSnapshot.getChildren()) {
                                ref.child("kullanicilar").child(id.getKey()).child("kullanici_adi").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.getValue() == null) {

                                        } else {
                                            if (dataSnapshot.getValue().equals(kadi.toString())) {
                                                kullaniciadkontrol = false;
                                            } else
                                                kullaniciadkontrol = true;
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                else{}

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });






        if(fragment_kontrol == true) //Telefon ekranından geldiyse
        {
            code=this.getArguments().getString("code");
            verifaction=this.getArguments().getString("verifaction");
            final String telno=this.getArguments().getString("telefon_numarasi");
            devametbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(kullaniciadkontrol == true)
                    {
                        PhoneAuthCredential credential= PhoneAuthProvider.getCredential(verifaction,code);
                        auth = FirebaseAuth.getInstance();
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful())
                                {
                                    String uid2 = auth.getCurrentUser().getUid();
                                    Map map = new HashMap();
                                    map.put("adsoyad",adsoyad.getText().toString());
                                    map.put("email",telno+"@gmail.com");
                                    map.put("profil_resmi","https://firebasestorage.googleapis.com/v0/b/proinstagram-d14fc.appspot.com/o/defaultprofilimage.jpg?alt=media&token=f44feb49-626b-4514-8ed4-a23bb48c68db");
                                    map.put("kullanici_adi",kadi.getText().toString());
                                    map.put("sifre",sifre.getText().toString());
                                    ref.child(uid2).setValue(map);
                                    Toast.makeText(getActivity(),"Başarılı",Toast.LENGTH_LONG).show();
                                    fragmenttemizle();
                                    ((MainActivity)getActivity()).giriskontrol();

                                }
                            }
                        });
                    }
                    else
                    {
                        Toast.makeText(getActivity(),"Bu kullanıcı adı mevcut farklı bir kullanıcı adı giriniz.",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else
        {
            email = this.getArguments().getString("email");
            devametbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(kullaniciadkontrol == true)
                    {
                        auth=FirebaseAuth.getInstance();
                        auth.createUserWithEmailAndPassword(email,sifre.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful())
                                {
                                    String uid2 = auth.getCurrentUser().getUid();
                                    Map map = new HashMap();
                                    map.put("adsoyad",adsoyad.getText().toString());
                                    map.put("email",email);
                                    map.put("profil_resmi","https://firebasestorage.googleapis.com/v0/b/proinstagram-d14fc.appspot.com/o/defaultprofilimage.jpg?alt=media&token=f44feb49-626b-4514-8ed4-a23bb48c68db");
                                    map.put("kullanici_adi",kadi.getText().toString());
                                    map.put("sifre",sifre.getText().toString());
                                    ref.child(uid2).setValue(map);
                                    Toast.makeText(getActivity(),"Başarılı",Toast.LENGTH_LONG).show();
                                    fragmenttemizle();
                                    ((MainActivity)getActivity()).giriskontrol();
                                }
                                else
                                {
                                    Toast.makeText(getActivity(),"Kullanıcı kaydı tamamlanamadı.",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    else
                    {
                        Toast.makeText(getActivity(),"Bu kullanıcı adı mevcut farklı bir kullanıcı adı giriniz.",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }





        adsoyad.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(start+before+count>2)
                {
                    devametbtn.setEnabled(true);
                    devametbtn.setBackgroundResource(R.drawable.ileriaktif);
                    devametbtn.setTextColor(getResources().getColor(R.color.beyaz));
                }
                else
                {
                    devametbtn.setEnabled(false);
                    devametbtn.setBackgroundResource(R.drawable.ileri);
                    devametbtn.setTextColor(getResources().getColor(R.color.beyaz));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        return view;
    }

    public void fragmenttemizle()
    {
        FragmentManager manager =((MainActivity)getActivity()).getSupportFragmentManager();
        for(int i =0; i<manager.getBackStackEntryCount();i++)//Arka planda kaçtane fragment açıksa silme işlemi yapılacak.
        {
            manager.popBackStack();
        }
    }
}
