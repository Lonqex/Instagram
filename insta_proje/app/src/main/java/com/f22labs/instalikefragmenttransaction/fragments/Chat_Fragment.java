package com.f22labs.instalikefragmenttransaction.fragments;

import android.content.ClipData;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.f22labs.instalikefragmenttransaction.Adapter.Item_Chat;
import com.f22labs.instalikefragmenttransaction.Adapter.PagerAdapter;
import com.f22labs.instalikefragmenttransaction.Adapter.Recyclerview_Adapter;
import com.f22labs.instalikefragmenttransaction.R;
import com.f22labs.instalikefragmenttransaction.activities.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Chat_Fragment extends BaseFragment {

    private RecyclerView recyclerView;
    private TextView username;
    private ImageView mesaj_gonder;
    private EditText mesaj;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    private String remote_id,kadi;
    private List<Item_Chat> liste = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chat_fragment, container, false);

        remote_id = this.getArguments().getString("id");
        kadi = this.getArguments().getString("kullanici_adi");

        recyclerView = (RecyclerView) view.findViewById(R.id.recylerview2);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        manager.setStackFromEnd(true);

        recyclerView.setLayoutManager(manager);

        username = (TextView) view.findViewById(R.id.user_name);
        username.setText(kadi);
        mesaj = (EditText) view.findViewById(R.id.mesaj_edittext);
        mesaj_gonder = (ImageView) view.findViewById(R.id.mesaj_gonder);

        mesaj_gonder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Map map = new HashMap();
                map.put("mesaj",mesaj.getText().toString());
                map.put("time", ServerValue.TIMESTAMP);
                map.put("uid",auth.getCurrentUser().getUid());

                ref.child("Mesaj").child(auth.getCurrentUser().getUid()).child(remote_id).push().setValue(map);


                ref.child("Mesaj").child(remote_id).child(auth.getCurrentUser().getUid()).push().setValue(map);

                mesaj.setText("");

            }
        });

        ref.child("Mesaj").child(auth.getCurrentUser().getUid()).child(remote_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                liste.clear();

                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    liste.add(snapshot.getValue(Item_Chat.class));
                }
                Recyclerview_Adapter recyclerview_adapter = new Recyclerview_Adapter(getActivity(),liste,auth.getCurrentUser().getUid());
                recyclerView.setAdapter(recyclerview_adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }

}
