package com.f22labs.instalikefragmenttransaction.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.f22labs.instalikefragmenttransaction.R;
import com.f22labs.instalikefragmenttransaction.activities.MainActivity;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;


public class Telefon_Kod_Kontrol_Fragment extends BaseFragment{

    String telefon_numarasi;
    String mverifaction,code_verifaction;
    String telno;
    private Button ileri;
    private EditText kodgir;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {

        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.telefon_kod_kontrol, container, false);

        telno = this.getArguments().getString("telefon_numarasi");
        kodgir = (EditText) view.findViewById(R.id.kodgir);
        ileri = (Button) view.findViewById(R.id.kodileri);


        ileri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String kod_deger = kodgir.getText().toString();
                if (kod_deger.equals(code_verifaction)) {
                    Kullanici_Olustur_Ekrani kayit = new Kullanici_Olustur_Ekrani();
                    Bundle bundle = new Bundle();
                    bundle.putString("verifaction",mverifaction);
                    bundle.putString("code",kod_deger);
                    bundle.putBoolean("fragment",true);
                    kayit.setArguments(bundle);
                    ((MainActivity)getActivity()).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.login_framelayout,kayit).addToBackStack("olustur").commit();
                } else {
                    Toast.makeText(getActivity(), "Hatalı kod girişi...", (Toast.LENGTH_LONG)).show();
                }
            }
        });

        setupcallbacks();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                telno,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                getActivity(),               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks

        return view;
    }

    private void setupcallbacks() {
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                code_verifaction = phoneAuthCredential.getSmsCode();
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(getActivity(),"Kod Gönderilemedi...", Toast.LENGTH_LONG).show();
            }
            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                mverifaction = verificationId;
            }
        };
    }


}
