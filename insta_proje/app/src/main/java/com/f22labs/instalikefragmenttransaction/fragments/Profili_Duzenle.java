package com.f22labs.instalikefragmenttransaction.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.f22labs.instalikefragmenttransaction.R;
import com.f22labs.instalikefragmenttransaction.activities.MainActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


public class Profili_Duzenle extends BaseFragment{

    private CircleImageView profil_resim;
    private final int resim_sec=100;
    private ImageView kaydet;
    private Uri profil_image_uri;
    private StorageReference Sref;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private EditText name,username,web,biography;

    private String Fname,Fusername,Fbio,Fweb,Fprofil_resmi;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profili_duzenle_ekrani, container, false);

        ((MainActivity)getActivity()).profil_düzenle();

        profil_resim = (CircleImageView) view.findViewById(R.id.profilimage);
        kaydet = (ImageView) view.findViewById(R.id.kaydet);
        Sref = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();

        name = (EditText) view.findViewById(R.id.nameedittext);
        username = (EditText) view.findViewById(R.id.usernameedittext);
        web = (EditText) view.findViewById(R.id.websiteedittext);
        biography = (EditText) view.findViewById(R.id.bioedittext);

        databaseReference.child("kullanicilar").child(auth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Fname= dataSnapshot.child("adsoyad").getValue().toString();
                name.setText(Fname);
                Fusername = dataSnapshot.child("kullanici_adi").getValue().toString();
                username.setText(Fusername);

                if(dataSnapshot.child("biography").getValue() != null)
                {
                    Fbio = dataSnapshot.child("biography").getValue().toString();
                    biography.setText(Fbio);
                }
                if(dataSnapshot.child("web_sitesi").getValue() != null)
                {
                    Fweb = dataSnapshot.child("web_sitesi").getValue().toString();
                    web.setText(Fweb);
                }
                if(dataSnapshot.child("profil_resmi") != null)
                {
                    Fprofil_resmi = dataSnapshot.child("profil_resmi").getValue().toString();

                    Glide.with(getActivity()).load(Fprofil_resmi).into(profil_resim);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        profil_resim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(intent,resim_sec);
            }
        });

        kaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(profil_image_uri!=null)
                {
                    profilremisinyukle();
                }
                else
                {
                    denetle();
                    ((MainActivity)getActivity()).onBackPressed();
                }
            }
        });
        return view;
    }

    private void denetle()
    {
        if(!Fname.equals(name.getText().toString()))
        {
            databaseReference.child("kullanicilar").child(auth.getCurrentUser().getUid()).child("adsoyad").setValue(name.getText().toString());
        }
        if(!Fusername.equals(username.getText().toString()))
        {
            databaseReference.child("kullanicilar").child(auth.getCurrentUser().getUid()).child("kullanici_adi").setValue(username.getText().toString());
        }
        if(Fbio == null)
        {
            databaseReference.child("kullanicilar").child(auth.getCurrentUser().getUid()).child("biography").setValue(biography.getText().toString());
        }
        else if(!Fbio.equals(biography.getText().toString()))
        {
            databaseReference.child("kullanicilar").child(auth.getCurrentUser().getUid()).child("biography").setValue(biography.getText().toString());
        }
        if(Fweb == null)
        {
            databaseReference.child("kullanicilar").child(auth.getCurrentUser().getUid()).child("web_sitesi").setValue(web.getText().toString());
        }
        else if(!Fweb.equals(web.getText().toString()))
        {
            databaseReference.child("kullanicilar").child(auth.getCurrentUser().getUid()).child("web_sitesi").setValue(web.getText().toString());
        }
    }
    private void profilremisinyukle()
    {
        // final ProgressDialog progressDialog = new ProgressDialog(getActivity());



        final UploadTask uploadTask = Sref.child(profil_image_uri.getLastPathSegment()).putFile(profil_image_uri);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String down = uri.toString();
                        databaseReference.child("kullanicilar").child(auth.getCurrentUser().getUid()).child("profil_resmi")
                                .setValue(down);
                        denetle();
                        // progressDialog.hide();
                        ((MainActivity)getActivity()).onBackPressed();
                    }
                });
            }
        });

       /*StorageReference ref = Sref.child(profil_image_uri.getLastPathSegment());
       ref.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
           @Override
           public void onComplete(@NonNull Task<Uri> task) {

               String down = task.getResult().toString();
               databaseReference.child("kullanicilar").child(auth.getCurrentUser().getUid()).child("profil_resmi")
                       .setValue(down);
               down="";
               denetle();
              // progressDialog.hide();
               ((MainActivity)getActivity()).getSupportFragmentManager().beginTransaction()
                       .replace(R.id.loginframelayout,new Profil_Fragment()).commit();
           }
       }).addOnFailureListener(new OnFailureListener() {
           @Override
           public void onFailure(@NonNull Exception e) {

           }
       });*/

        // progressDialog.show();



        /*uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful())
                {

                    String url = task.getResult().getStorage().getDownloadUrl().getResult().toString();
                  //  String url = task.getResult().getUploadSessionUri().toString();

                   UploadTask.TaskSnapshot taskSnapshot = task.getResult();
                    databaseReference.child("kullanicilar").child(auth.getCurrentUser().getUid()).child("profil_resmi")
                            .setValue(url);
                    denetle();
                    progressDialog.hide();
                    ((MainActivity)getActivity()).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.loginframelayout,new Profil_Fragment()).commit();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
               double progress = (100.0 * taskSnapshot.getBytesTransferred());
                progressDialog.setMessage((double)progress+ " yüklendi");
            }
        });*/

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == resim_sec && resultCode==RESULT_OK && data!=null)
        {
            profil_image_uri = data.getData();

            try {
                profil_resim.setImageURI(profil_image_uri);
            }
            catch (Exception e)
            {

            }
        }
    }

}
