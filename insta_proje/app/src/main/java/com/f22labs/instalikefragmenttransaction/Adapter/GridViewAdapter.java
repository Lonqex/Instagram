package com.f22labs.instalikefragmenttransaction.Adapter;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.f22labs.instalikefragmenttransaction.R;

import java.util.ArrayList;

public class GridViewAdapter extends ArrayAdapter<String> {

    private Context mcontext;
    private LayoutInflater inflate;
    private int layout;
    private ArrayList<String> files;
    private TextView saat;
    public GridViewAdapter(@NonNull Context context, int resource, ArrayList<String> dosya) {
        super(context, resource,dosya);
        this.mcontext = context;
        inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.layout = resource;
        this.files = dosya;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if(convertView == null)
        {
            convertView = inflate.inflate(layout,parent,false);
        }
        ImageView image;

        saat = (TextView) convertView.findViewById(R.id.saat);
        image = (ImageView) convertView.findViewById(R.id.image);

        int son = files.get(position).lastIndexOf(".");
        String string = files.get(position).substring(son);

        if(string.equals(".mp4"))
        {
            saat.setVisibility(View.VISIBLE);
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(mcontext, Uri.parse("file://"+files.get(position)));
            String zaman_string = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            long time = Long.parseLong(zaman_string);
            retriever.release();

            saat.setText(convertDuration(time));
            Glide.with(mcontext).load("file://"+files.get(position)).into(image);
        }
        else
        {
            saat.setVisibility(View.GONE);
            Glide.with(mcontext).load("file://"+files.get(position)).into(image);
        }

        return convertView;
    }
    public String convertDuration(long duration) {
        String out = null;
        long hours=0;
        try {
            hours = (duration / 3600000);
        } catch (Exception e) {
            e.printStackTrace();
            return out;
        }
        long remaining_minutes = (duration - (hours * 3600000)) / 60000;
        String minutes = String.valueOf(remaining_minutes);
        if (minutes.equals(0)) {
            minutes = "00";
        }
        long remaining_seconds = (duration - (hours * 3600000) - (remaining_minutes * 60000));
        String seconds = String.valueOf(remaining_seconds);
        if (seconds.length() < 2) {
            seconds = "00";
        } else {
            seconds = seconds.substring(0, 2);
        }

        if (hours > 0) {
            out = hours + ":" + minutes + ":" + seconds;
        } else {
            out = minutes + ":" + seconds;
        }

        return out;

    }
}
