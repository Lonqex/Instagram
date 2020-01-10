package com.f22labs.instalikefragmenttransaction.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.f22labs.instalikefragmenttransaction.R;
import com.f22labs.instalikefragmenttransaction.activities.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Giris_Fragment extends BaseFragment{

    private Button girisyap;
    private EditText kullanici_adi,kullanici_sifre;
    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;
    private TextView kaydoladon;
    private String dbmail;
    ProgressDialog progressDialog;

    @Override
    public void onStart() {
        super.onStart();
        ((MainActivity)getActivity()).girisbottom();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.giris_ekrani, container, false);

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference();


        progressDialog =new ProgressDialog(getActivity());

        girisyap = (Button) view.findViewById(R.id.girisyap);
        kullanici_adi = (EditText) view.findViewById(R.id.kullanici_adi);
        kullanici_sifre = (EditText) view.findViewById(R.id.kullanici_sifre);
        kaydoladon = (TextView) view.findViewById(R.id.kaydol);
        kaydoladon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.login_framelayout,new Kayit_Fragment()).addToBackStack("kayit_ekrani").commit();
            }
        });


        girisyap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.show();
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Giriş yapılıyor...");

                if(kullanici_adi.getText().equals("") || kullanici_sifre.getText().equals(""))
                {
                    Toast.makeText(getActivity(),"Boş bölümleri doldurunuz.",Toast.LENGTH_SHORT).show();
                }

                final String kadi = kullanici_adi.getText().toString();
                final String sifre = kullanici_sifre.getText().toString();
                ref.child("kullanicilar").orderByChild("kullanici_adi").equalTo(kadi).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(dataSnapshot.getValue() != null)
                        {


                            for(final DataSnapshot id:dataSnapshot.getChildren())
                            {
                                ref.child("kullanicilar").child(id.getKey()).child("kullanici_adi").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.getValue()!=null)
                                        {
                                            if(dataSnapshot.getValue().equals(kadi))
                                            {
                                                ref.child("kullanicilar").child(id.getKey()).child("email").addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        if(dataSnapshot.getValue()!=null)
                                                        {
                                                            auth.signInWithEmailAndPassword(dataSnapshot.getValue().toString(),sifre).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                                    if(task.isSuccessful())
                                                                    {
                                                                        progressDialog.hide();
                                                                        /*((MainActivity)getActivity()).getSupportFragmentManager().beginTransaction()
                                                                                .replace(R.id.content_frame,new HomeFragment()).commit();*/
                                                                        mFragmentNavigation.pushFragment(new HomeFragment());
                                                                    }
                                                                    else
                                                                    {
                                                                        progressDialog.hide();
                                                                        Toast.makeText(getActivity(),"Şifre yanlış.",Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            });
                                                        }
                                                    }
                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                    }
                                                });
                                            }
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                        }
                        else
                        {
                            progressDialog.hide();
                            Toast.makeText(getActivity(),"Kullanıcı adı yanlış.",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }

                });
            }
        });
        return view;
    }

}
