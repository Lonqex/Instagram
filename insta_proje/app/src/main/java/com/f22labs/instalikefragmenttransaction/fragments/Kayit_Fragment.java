package com.f22labs.instalikefragmenttransaction.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.f22labs.instalikefragmenttransaction.R;
import com.f22labs.instalikefragmenttransaction.activities.MainActivity;


public class Kayit_Fragment extends BaseFragment{

    private TextView girisedon,telefontextview,epostatextview,smstextview;
    private View telefonview,epostaview;
    private EditText editText;
    private Button ileri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.kayit_ekrani, container, false);
        telefonview = view.findViewById(R.id.telefonview);
        epostaview = view.findViewById(R.id.epostaview);
        editText = (EditText) view.findViewById(R.id.kayitedittext);
        smstextview = (TextView) view.findViewById(R.id.kayitsmsyazitextview);
        ileri = (Button) view.findViewById(R.id.kayitileributton);

        telefontextview = (TextView) view.findViewById(R.id.kayittelefontextview);
        telefontextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                telefonview.setVisibility(View.VISIBLE);
                epostaview.setVisibility(View.INVISIBLE);
                telefontextview.setTextColor(getResources().getColor(R.color.siyah));
                epostatextview.setTextColor(getResources().getColor(R.color.gri3));
                editText.setText("");
                editText.setText("");
                editText.setHint("Telefon");
                editText.setInputType(InputType.TYPE_CLASS_PHONE);
                smstextview.setVisibility(View.VISIBLE);
            }
        });
        epostatextview = (TextView) view.findViewById(R.id.kayitepostatextview);
        epostatextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                telefonview.setVisibility(View.INVISIBLE);
                epostaview.setVisibility(View.VISIBLE);
                telefontextview.setTextColor(getResources().getColor(R.color.gri3));
                epostatextview.setTextColor(getResources().getColor(R.color.siyah));
                editText.setText("");
                editText.setText("");
                editText.setHint("E-Posta");
                editText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                smstextview.setVisibility(View.GONE);
            }
        });


        girisedon = (TextView) view.findViewById(R.id.giristextview);
        girisedon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.login_framelayout,new Giris_Fragment()).addToBackStack("giris_ekrani").commit();
            }
        });



        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {



            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                if(start+before+count>=10)
                {
                    ileri.setEnabled(true);
                    ileri.setTextColor(getResources().getColor(R.color.beyaz));
                    ileri.setBackgroundResource(R.drawable.ileriaktif);
                }

                else
                {
                    ileri.setEnabled(false);
                    ileri.setTextColor(getResources().getColor(R.color.beyaz));
                    ileri.setBackgroundResource(R.drawable.ileri);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        ileri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText.getHint()=="E-Posta")
                {
                    String email = editText.getText().toString();
                    Kullanici_Olustur_Ekrani kullanici_olustur_ekrani = new Kullanici_Olustur_Ekrani();
                    Bundle bundle = new Bundle();
                    bundle.putString("email",email);
                    bundle.putBoolean("fragment",false);
                    kullanici_olustur_ekrani.setArguments(bundle);

                    ((MainActivity)getActivity()).getSupportFragmentManager()
                            .beginTransaction().replace(R.id.login_framelayout,kullanici_olustur_ekrani).addToBackStack("eposta").commit();
                }
                else
                {
                    Kullanici_Olustur_Ekrani kullanici_olustur_ekrani = new Kullanici_Olustur_Ekrani();
                    Telefon_Kod_Kontrol_Fragment kod_kontrol = new Telefon_Kod_Kontrol_Fragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("telefon_numarasi",editText.getText().toString());
                    kod_kontrol.setArguments(bundle);
                    kullanici_olustur_ekrani.setArguments(bundle);
                    ((MainActivity)getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.login_framelayout, kod_kontrol).addToBackStack("kod").commit();

                }
            }
        });



        return view;
    }

}
