package com.f22labs.instalikefragmenttransaction.fragments;

import android.Manifest;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.f22labs.instalikefragmenttransaction.Adapter.DosyaArama;
import com.f22labs.instalikefragmenttransaction.Adapter.GridViewAdapter;
import com.f22labs.instalikefragmenttransaction.R;
import com.f22labs.instalikefragmenttransaction.activities.MainActivity;
import com.naver.android.helloyako.imagecrop.view.ImageCropView;
import com.universalvideoview.UniversalVideoView;

import java.util.ArrayList;


public class Share_Galery extends BaseFragment{

    private TextView ileri;
    private Spinner spinner;
    private android.widget.GridView gridView;
    private ImageCropView imageView;
    private String root,camera,download,WhatsApp_image,screen_shot,son_yol;
    private ArrayList<String> klasorler;
    private UniversalVideoView videoview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.share_galery, container, false);

        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);

        ileri = (TextView) view.findViewById(R.id.galeri_ileri);
        spinner = (Spinner) view.findViewById(R.id.spinner);
        gridView = (GridView) view.findViewById(R.id.galeri_gridview);
        imageView = (ImageCropView) view.findViewById(R.id.galeri_image);

        videoview = (UniversalVideoView) view.findViewById(R.id.videoView);

        klasorler = new ArrayList<>();

        root = Environment.getExternalStorageDirectory().getPath();
        camera = root +"/DCIM/Camera";
        download = root + "/Download";
        WhatsApp_image = root + "/WhatsApp/Media/WhatsApp Images";
        screen_shot = root + "/DCIM/Screenshots";

        klasorler.add(camera);
        klasorler.add(download);
        klasorler.add(WhatsApp_image);
        klasorler.add(screen_shot);

        ileri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putString("uzanti","file://"+son_yol);

                Share_Paylas paylas_fragment = new Share_Paylas();
                paylas_fragment.setArguments(bundle);
                mFragmentNavigation.pushFragment(paylas_fragment);

            }
        });


        ArrayList<String> klasor_isim = new ArrayList<>();

        for(int i = 0; i < klasorler.size();i++)
        {
            int son = klasorler.get(i).lastIndexOf("/");
            String klasor = klasorler.get(i).substring(son + 1);

            klasor_isim.add(klasor);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,klasor_isim);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);


        spinner.setSelection(0);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                final ArrayList<String> dosyalarim = DosyaArama.getDosyaYolu(klasorler.get(position));

                GridViewAdapter adapter1 = new GridViewAdapter(getActivity(),R.layout.image_goruntu,dosyalarim);

                gridView.setAdapter(adapter1);
                if(dosyalarim.size()==0){

                }
                else {
                    son_yol = dosyalarim.get(0);
                    int son = dosyalarim.get(0).lastIndexOf(".");
                    String string = dosyalarim.get(position).substring(son);

                    if (string.equals(".mp4")) {
                        imageView.setVisibility(View.GONE);
                        videoview.setVisibility(View.VISIBLE);
                        videoview.setVideoURI(Uri.parse(dosyalarim.get(0)));
                        videoview.start();
                    } else {
                        imageView.setVisibility(View.VISIBLE);
                        videoview.setVisibility(View.GONE);
                        Glide.with(getActivity()).load("file://" + dosyalarim.get(0)).into(imageView);
                    }
                }

                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        son_yol = dosyalarim.get(position);
                        int son = dosyalarim.get(position).lastIndexOf(".");
                        String deger = dosyalarim.get(position).substring(son);

                        if(deger.equals(".mp4"))
                        {
                            imageView.setVisibility(View.GONE);
                            videoview.setVisibility(View.VISIBLE);
                            videoview.setVideoURI(Uri.parse(dosyalarim.get(position)));
                            videoview.start();
                        }
                        else
                        {
                            imageView.setVisibility(View.VISIBLE);
                            videoview.setVisibility(View.GONE);
                            Glide.with(getActivity()).load("file://" + dosyalarim.get(position)).into(imageView);
                        }

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        return view;
    }

}
