package com.f22labs.instalikefragmenttransaction.fragments;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.f22labs.instalikefragmenttransaction.R;
import com.f22labs.instalikefragmenttransaction.activities.MainActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class Share_Paylas extends BaseFragment{

    private EditText aciklama;
    private ImageView image_;
    private CircleImageView p_resim;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private StorageReference sref;
    private Uri uri;
    private String uid;
    private TextView paylas;
    private String Fprofil_resmi;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.share_paylas, container, false);


        ((MainActivity)getActivity()).profil_düzenle();
        String yol = (String) this.getArguments().get("uzanti");

        auth=FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        sref = FirebaseStorage.getInstance().getReference();
        progressDialog =new ProgressDialog(getActivity());

        uid = auth.getCurrentUser().getUid();
        uri = Uri.parse(yol);

        image_ = (ImageView) view.findViewById(R.id.image);
        p_resim = (CircleImageView) view.findViewById(R.id.profilimage);
        paylas = (TextView) view.findViewById(R.id.paylas);


        databaseReference.child("kullanicilar").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null)
                {
                    Fprofil_resmi = dataSnapshot.child("profil_resmi").getValue().toString();
                    Glide.with(getActivity()).load(Fprofil_resmi).into(p_resim);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        paylas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                progressDialog.show();
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Paylaşım yapılıyor...");


                final UploadTask uploadTask = sref.child(uid).child(uri.getLastPathSegment()).putFile(uri);

                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String down = uri.toString();
                                databaseekle(down);
                                mFragmentNavigation.pushFragment(new HomeFragment());
                                progressDialog.hide();
                            }
                        });
                    }
                });
            }
        });
        progressDialog.hide();

        image_.setImageURI(uri);

        aciklama = (EditText) view.findViewById(R.id.text);

        return view;
    }

    private void databaseekle(final String down) {

        final DatabaseReference anahtar = databaseReference.child("Post").child(uid).push();


        final String push_key = anahtar.getKey();


        final DatabaseReference yorumRef = databaseReference.child("Yorumlar").child(push_key).child(push_key);


        final DatabaseReference userRef = databaseReference.child("kullanicilar").child(uid).child("takipci");

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Map map = new HashMap();
                map.put("uid",uid);
                map.put("url",down);
                map.put("time", ServerValue.TIMESTAMP);

                Map map2 = new HashMap();
                map2.put("Aciklama", aciklama.getText().toString());
                map2.put("id",uid);
                map2.put("time", ServerValue.TIMESTAMP);

                yorumRef.setValue(map2);

                anahtar.setValue(map);

                List<String> takipci_list = new ArrayList<String>();

                for(DataSnapshot liste : dataSnapshot.getChildren())
                {
                    String veri = String.valueOf(liste.getValue());
                    takipci_list.add(veri);
                }

                for(int i = 0; i < takipci_list.size();i++)
                {
                    DatabaseReference takipRef = databaseReference.child("Post").child(takipci_list.get(i)).child(push_key);
                    takipRef.setValue(map);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
